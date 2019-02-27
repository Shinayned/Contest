package controller;

import contest.form.Form;
import contest.form.Forms;
import exception.DuplicateException;
import model.Contest;
import model.ContestPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import service.ContestService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ContestController {
    @Autowired
    private ContestService contestService;

    @GetMapping("/contest/{contestID}")
    public String onContestPage(@PathVariable("contestId") long contestId, Model model) {
        Contest contest = contestService.getContest(contestId);
        model.addAttribute("url", "/contest/" + contest.getId() + "/submit/application");
        return "contests/" + contest.getPage();
    }

    @PostMapping("/contest/{contestId}/submit/application")
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void submitApplication(@PathVariable("contestId") long contestId,
                                  @RequestParam(required = false) MultipartFile[] files,
                                  Principal principal,
                                  HttpServletRequest request,
                                  Model model) {
        String participantEmail = principal.getName();
        Map<String, String[]> formsData = request.getParameterMap();

        if (files == null)
            contestService.submitApplication(contestId, participantEmail, formsData, new ArrayList<>());
        else
            contestService.submitApplication(contestId, participantEmail, formsData, Arrays.asList(files));
    }

    @GetMapping("/contest/{contestId}/application")
    public String onApplicationPage(@PathVariable("contestId") long contestId,
                                    Model model) {
        List<Form> forms = contestService.getForms(contestId);
        model.addAttribute("forms", forms);

        return "application";
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseBody
    public void onDuplicateException(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(406, "You have already submitted application for the contest.");
    }
}
