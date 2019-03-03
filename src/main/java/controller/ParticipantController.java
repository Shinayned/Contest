package controller;

import dto.ParticipantDto;
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

@Controller
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

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

    @GetMapping("/edit")
    public String editParticipantAccount(Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);
        return "edit";
    }

    @PostMapping("/edit")
    public String onUploadEditInformation(@Valid @RequestBody ParticipantDto participantDto, Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);

        participantService.updateAccount(principal.getName(), participantDto);
        return "edit";
    }

    @GetMapping("/restoration")
    public String onPasswordRestoration() {
        return "restoration";
    }

    @GetMapping(value = "/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        VerificationToken verificationToken = participantService.getVerificationToken(token);

        Participant participant = verificationToken.getParticipant();
        DateTime currentTime = new DateTime();
        if (verificationToken.getExpiryDate().isBefore(currentTime)) {
            return "redirect:/badUser";
        }

        participant.setEnabled(true);
        participantService.updateAccount(participant);
        return "redirect:/login";
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public void onBadRequestParameters() {
    }
}
