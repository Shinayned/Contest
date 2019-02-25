package controller;

import exception.DuplicateException;
import model.ContestField;
import model.ContestPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.ContestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class ContestController {
    @Autowired
    private ContestService contestService;

    @GetMapping("/contest/{contestName}")
    public String onContestPage(@PathVariable("contestName")String contestName, Model model) {
        ContestPage pageData= contestService.getPageData(contestName);
        model.addAttribute("pageDate", pageData);
        return "contest";
    }

    @PostMapping("/contest/{contestName}/submit/application")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void submitApplication(@PathVariable("contestName")String contestName,
                                    Principal principal,
                                    HttpServletRequest request, Model model) {
        String participantEmail = principal.getName();
        Map<String, String[]> filledForms = request.getParameterMap();

        contestService.submitApplication(contestName, participantEmail, filledForms);
    }

    @GetMapping("/contest/{contestName}/application")
    public String onApplicationPage(@PathVariable("contestName")String contestName,
                                    Model model) {
        List<ContestField> fields = contestService.getContestFields(contestName);
        model.addAttribute("fields", fields);
        return "application";
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseBody
    public void onDuplicateException(HttpServletResponse response, Exception exception) throws IOException{
        response.sendError(406, "You have already submitted application for the contest.");
    }
}
