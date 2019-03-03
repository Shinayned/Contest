package controller;

import com.google.api.client.http.HttpResponse;
import dto.ParticipantDto;
import email.EmailService;
import event.OnRegistrationCompleteEvent;
import model.Participant;
import model.VerificationToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
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
import java.util.UUID;

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
    public String confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException{
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
    public String onUploadEditInformation(@Valid @RequestBody ParticipantDto participantDto, Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);

        participantService.updateAccount(principal.getName(), participantDto);
        return "edit";
    }

    @GetMapping("/participant/resetPassword")
    public String onResetPasswordPage() {
        return "restoration";
    }

    @PostMapping("/participant/resetPassword")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void resetPassword(@RequestParam("email")String email,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        Participant participant = participantService.getParticipantByEmail(email);
        if (participant == null)
            response.setStatus(400);

        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        participantService.sendResetPasswordUrl(participant, appUrl);

        response.setStatus(200);
    }

    @GetMapping("/participant/changePassword")
    public String onChangePasswordPage(@RequestParam(name = "token",required = false)String token,
                                     Principal principal,
                                     HttpServletResponse response) throws IOException{
        boolean isAuthorized = principal == null;
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
    public String changePassword(@RequestParam(name = "token", required = false)String token,
                                 @RequestParam("newPassword")String newPassword,
                                 Principal principal,
                                 HttpServletResponse response) {
        boolean isAuthorized = principal == null;
        if (isAuthorized) {
            participantService.changePassword(principal.getName(), newPassword);
            return "redirect:/login";
        }

        boolean isResetPasswordProcess = token != null;
        if (!isResetPasswordProcess)
            return "redirect:/error";

        VerificationToken passwordToken = participantService.getToken(token);

        if (passwordToken == null)
            return "redirect:/error";

        Participant participant= passwordToken.getParticipant();
        participantService.changePassword(participant, newPassword);
        return "redirect:/login";
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public void onBadRequestParameters() {
    }
}
