package controller;

import dto.ContestDto;
import model.Contest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ContestService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private ContestService contestService;

    @GetMapping("/admin/login")
    public String onAdminLogin() {
        return "adminLogin";
    }

    @GetMapping("/admin/adminPage")
    public String onAdminPage(Model model) {
        List<Contest> contests = contestService.getAllContests();
        model.addAttribute("contests", contests);
        return "admin";
    }

    @PostMapping("/admin/contest/sendApplicationsList")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void sendToEmail(@RequestParam("contestId") long contestId,
                            @RequestParam("email") String sendToEmail) throws IOException {
        contestService.sendContestApplications(contestId, sendToEmail);
    }

    @PostMapping("/admin/contest/changeStatus")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void closeOpenContest(@RequestParam("contestId") long contestId,
                                 HttpServletResponse response) throws IOException {
        boolean contestExist = contestService.closeOpenContest(contestId);
        if (!contestExist)
            response.sendError(400);
    }

    @PostMapping("/admin/contest/setExpirationTime")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void setContestExpirationTime(@RequestParam("contestId") long contestId,
                                         @RequestParam("expirationTime") DateTime expirationTime,
                                         HttpServletResponse response) throws IOException {
        if (expirationTime.isBefore(DateTime.now())) {
            response.sendError(406, "Expiration time must be to future.");
            return;
        }

        boolean expirationTimeHasBeenSet = contestService.setExpirationTime(contestId, expirationTime);
        if (!expirationTimeHasBeenSet)
            response.sendError(406, "Contest " + contestId + " is not exist.");
    }

    @PostMapping("/admin/contest/getInfo")
    @ResponseBody
    public ContestDto getContestInfo(@RequestParam("contestId") long contestId,
                                     HttpServletResponse response) throws IOException {
        Contest contest = contestService.getContest(contestId);

        if (contest == null) {
            response.sendError(400);
            return null;
        }

        ContestDto contestDto = new ContestDto(contest);
        return contestDto;
    }
}
