package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/login")
    public String onAdminLogin() {
        return "adminLogin";
    }

    @GetMapping("/admin/adminPage")
    public String onAdminPage(){
        return "main";
    }
}
