package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/loginAdmin")
    public String onAdminLogin() {
        return "adminLogin";
    }

    @GetMapping("/adminPage")
    public String onAdminPage(){
        return "main";
    }
}
