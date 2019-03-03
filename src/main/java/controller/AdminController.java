package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.ContestService;

import java.io.IOException;

@Controller
public class AdminController {
    @Autowired
    private ContestService contestService;

    @GetMapping("/admin/login")
    public String onAdminLogin() {
        return "adminLogin";
    }

    @GetMapping("/admin/adminPage")
    public String onAdminPage(){
        return "admin";
    }

    @PostMapping("/admin/contest/sendApplicationsList")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void sendToEmail(@RequestParam("contestId")long contestId,
                              @RequestParam("emailTo")String sendToEmail) throws IOException {
        contestService.sendContestApplications(contestId, sendToEmail);
    }
}
