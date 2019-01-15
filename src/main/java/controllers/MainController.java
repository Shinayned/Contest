package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String onIndex() {
        return "index";
    }

    @RequestMapping("/avtorization")
    public String onAvtorization() {
        return "avtorization";
    }

    @RequestMapping("/main")
    public String onMain() {
        return "main";
    }

    @RequestMapping("/registration")
    public String onRegistration() {
        return "registration";
    }


    @RequestMapping("/style.css")
    public String onKek() {
        return "/WEB-INF/dynamic/style.css";
    }
}
