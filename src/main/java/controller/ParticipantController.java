package controller;

import dto.ParticipantDto;
import email.EmailService;
import event.OnParticipantInfoEditEvent;
import event.OnRegistrationCompleteEvent;
import model.Participant;
import model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import service.ParticipantService;
import exception.EmailExistsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

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
    @ResponseStatus(HttpStatus.OK)j
    @ResponseBody
    public void createParticipantAccount(@Valid @RequestBody ParticipantDto participantDto, HttpServletRequest request) {
        String password = participantDto.getPassword();

        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password can't be empty or null");

        Participant participant = participantService.registerNewAccount(participantDto);

        try {
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(participant, appUrl));
        } catch (EmailExistsException exception) {
            throw new IllegalArgumentException("Email " + participantDto.getEmail() + " is already exist.");
        }
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        Token verificationToken = participantService.getToken(token);

        if (verificationToken == null || verificationToken.isExpired())
            return "redirect:/error";

        Participant participant = verificationToken.getParticipant();
        participant.setEnabled(true);
        participantService.removeToken(verificationToken);
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

        eventPublisher.publishEvent(new OnParticipantInfoEditEvent(participant, participantDto));
        participantService.updateAccount(principal.getName(), participantDto);
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

        if (participant == null || !participant.isEnabled()) {
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

        Token passwordToken = participantService.getToken(token);
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

        Token passwordToken = participantService.getToken(token);

        if (passwordToken == null || passwordToken.isExpired()) {
            response.sendError(400);
            return;
        }

        Participant participant = passwordToken.getParticipant();
        participantService.changePassword(participant, newPassword);
        participantService.removeToken(passwordToken);
        response.setStatus(200);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseBody
    public void onBadRequestParameters(Exception exception, HttpServletResponse response) throws IOException{
        response.sendError(406, exception.getMessage());
    }
}
