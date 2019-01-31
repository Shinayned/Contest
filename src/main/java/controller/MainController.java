package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Participant;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ParticipantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;

@Controller
public class MainController {
    @Autowired
    private ParticipantService participantService;

    @RequestMapping("/")
    public String onIndex() throws JsonProcessingException {
        return "index";
    }

    @RequestMapping("/main")
    public String onMain() {
        return "main";
    }

    @RequestMapping("/home")
    public String onHome(Principal principal, Model model) {
        if(principal != null) {
            Participant participant = participantService.getParticipantByEmail(principal.getName());
            if(participant != null) {
                model.addAttribute("participant", participant);
            }
        }
        return "home";
    }
}