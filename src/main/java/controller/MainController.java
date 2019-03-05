package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.Contest;
import model.Participant;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ContestService;
import service.ParticipantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ContestService contestService;

    @RequestMapping("/")
    public String onIndex(Principal principal, Model model) {
        if (principal == null || principal.getName().equals("admin"))
            model.addAttribute("isAuthenticated", false);
        else
            model.addAttribute("isAuthenticated", true);

        return "index";
    }

    @RequestMapping("/main")
    public String onMain(Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);
        return "main";
    }

    @RequestMapping("/home")
    public String onHome(Principal principal, Model model) {
        Participant participant = participantService.getParticipantByEmail(principal.getName());
        model.addAttribute("participant", participant);

        List<Contest> contests = contestService.getAllContests();
        model.addAttribute("contests", contests);
        return "home";
    }
}