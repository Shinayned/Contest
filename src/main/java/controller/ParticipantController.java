package controller;

import com.google.api.client.http.HttpResponse;
import dto.ParticipantDto;
import email.EmailService;
import event.OnRegistrationCompleteEvent;
import model.Participant;
import model.VerificationToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ParticipantService;
import exception.EmailExistsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.UUID;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EmailService emailService;

    @GetMapping("/login")
    public String onLogin() {
        return "login";
    }

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createParticipantAccount(@Valid @RequestBody ParticipantDto participantDto, HttpServletRequest request) {
        String password = participantDto.getPassword();

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password can't be empty or null");

        Participant participant = participantService.registerNewAccount(participantDto);

        try {
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(participant, appUrl));
        } catch (EmailExistsException exception) {
            return null;
        }
        return "SUCCESS";
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        VerificationToken verificationToken = participantService.getToken(token);

        if (verificationToken == null)
            return "redirect:/error";

        if (verificationToken.getExpiryDate().isBefore(DateTime.now()))
            return "redirect:/error";

        Participant participant = verificationToken.getParticipant();
        participant.setEnabled(true);
        participantService.updateAccount(participant);

        return "redirect:/login";
    }

    @GetMapping("/participant/edit")
    public String editParticipantAccount(Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);
        return "edit";
    }

    @PostMapping("/participant/edit")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void onUploadEditInformation(@Valid @RequestBody ParticipantDto participantDto,
                                          Principal principal,
                                          HttpServletRequest request,
                                          Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);
        participantService.updateAccount(principal.getName(), participantDto);

        String newEmail = participantDto.getEmail();
        boolean hasChangedEmail = !participant.getEmail().equals(newEmail);
        if (hasChangedEmail) {
            Collection<SimpleGrantedAuthority> nowAuthorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                    .getContext().getAuthentication().getAuthorities();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    newEmail,
                    participant.getPassword(),
                    nowAuthorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    @GetMapping("/participant/resetPassword")
    public String onResetPasswordPage() {
        return "restoration";
    }

    @PostMapping("/participant/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void resetPassword(@RequestParam("email") String email,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {
        Participant participant = participantService.getParticipantByEmail(email);

        if (participant == null) {
            response.sendError(400);
            return;
        }

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        participantService.sendResetPasswordUrl(participant, appUrl);
    }

    @GetMapping("/participant/changePassword")
    public String onChangePasswordPage(@RequestParam(name = "token", required = false) String token,
                                       Principal principal,
                                       HttpServletResponse response) throws IOException {
        boolean isAuthorized = principal != null;
        boolean isResetPasswordProcess = token != null;
        if (isAuthorized)
            return "changePassword";
        if (!isResetPasswordProcess)
            return "redirect:/login";

        VerificationToken passwordToken = participantService.getToken(token);
        if (passwordToken == null)
            return "redirect:/error";

        return "changePassword";
    }

    @PostMapping("/participant/changePassword")
    @ResponseBody
    public void changePassword(@RequestParam(name = "token", required = false) String token,
                               @RequestParam("password") String newPassword,
                               Principal principal,
                               HttpServletResponse response) throws IOException {
        boolean isAuthorized = principal != null;
        if (isAuthorized && !principal.getName().equals("admin")) {
            participantService.changePassword(principal.getName(), newPassword);
            response.setStatus(200);
            return;
        }

        boolean isResetPasswordProcess = token != null;
        if (!isResetPasswordProcess) {
            response.sendError(400);
            return;
        }

        VerificationToken passwordToken = participantService.getToken(token);

        if (passwordToken == null) {
            response.sendError(400);
            return;
        }
        Participant participant = passwordToken.getParticipant();
        participantService.changePassword(participant, newPassword);
        response.setStatus(200);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public void onBadRequestParameters() {
    }
}
