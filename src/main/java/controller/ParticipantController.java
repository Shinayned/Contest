package controller;

import dto.ParticipantDto;
import event.OnRegistrationCompleteEvent;
import model.Participant;
import model.VerificationToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ParticipantService;
import exception.EmailExistsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView createParticipantAccount(@RequestBody ParticipantDto participantDto, HttpServletRequest request) {
        Participant participant = participantService.registerNewAccount(participantDto);
        if (participant == null) {
            return new ModelAndView("registration", "participant", participantDto);
        }
        try {
            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(participant, appUrl));
        } catch (EmailExistsException exception) {
            return new ModelAndView("registration", "participant", participantDto);
        }
        return new ModelAndView("redirect:registration", "participant", participantDto);
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


    @GetMapping("/login")
    public String onLogin() {
        return "login";
    }
}
