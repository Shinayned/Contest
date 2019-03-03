package controller;

import contest.form.Form;
import contest.form.Forms;
import exception.BadRequestException;
import exception.DuplicateException;
import google.GoogleDrive;
import model.Contest;
import model.ContestPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import service.ContestService;

import javax.mail.Multipart;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
                                  MultipartRequest multipartRequest,
                                  Principal principal,
                                  HttpServletRequest request,
                                  Model model) {
        String participantEmail = principal.getName();
        Map<String, String[]> formsData = request.getParameterMap();

        List<MultipartFile> files = new ArrayList<>();
        for (List<MultipartFile> list : multipartRequest.getMultiFileMap().values()) {
            files.addAll(list);
        }

        contestService.submitApplication(contestId, participantEmail, formsData, files);
    }

    @GetMapping("/contest/{contestId}/application")
    public String onApplicationPage(@PathVariable("contestId") long contestId,
                                    Model model) {
        List<Form> forms = contestService.getAllForms(contestId);
        model.addAttribute("forms", forms);
        model.addAttribute("submitUrl", "/contest/" + contestId + "/submit/application");
        return "forms";
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseBody
    public void onDuplicateException(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(406, exception.getMessage());
    }

    @ExceptionHandler({BadRequestException.class, DisabledException.class})
    @ResponseBody
    public void onBadRequestException(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(400, exception.getMessage());
    }
}
