package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @RequestMapping("/")
    public String onIndex() throws JsonProcessingException {
        return "index";
    }

    @GetMapping("/login")
    public String onLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String onLoginProcessing() {
        System.out.println("\n*********************\nIt's working\n*********************\n");
        return null;
    }

    @RequestMapping("/main")
    public String onMain() {
        return "main";
    }

    @GetMapping("/registration")
    public String onRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String onRegistrationProcessing(@RequestBody UserDto user) {
        System.out.println("\n Email: " + user.getEmail() + "\n Password:" + user.getPassword() + "\n");
        return "registration";
    }


    @RequestMapping("/home")
    public String onHome() {
        return "home";
    }
}