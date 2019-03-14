package controller;

import contest.form.Form;
import contest.form.Forms;
import exception.BadRequestException;
import exception.DuplicateException;
import google.GoogleDrive;
import model.Contest;
import model.ContestPage;
import model.Participant;
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
import service.ParticipantService;

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

    @Autowired
    private ParticipantService participantService;

    @PostMapping("/contest/{contestId}/application")
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
                                    Principal principal,
                                    Model model) {
        Contest contest = contestService.getContest(contestId);
        List<Form> forms = contest.getAllForms();
        Participant participant = participantService.getParticipantByEmail(principal.getName());

        if (forms == null) {
            model.addAttribute("status", 404);
            model.addAttribute("error", "Contest " + contestId + " is not exist.");
            return "error";
        }

        model.addAttribute("contestName", contest.getName());
        model.addAttribute("contestIsClosed", !contest.isActive());
        model.addAttribute("duplicateApplication", participant.hasApplicationForContest(contest));
        model.addAttribute("forms", forms);
        model.addAttribute("submitUrl", "/contest/" + contestId + "/application");
        return "forms";
    }

    @ExceptionHandler({BadRequestException.class, DisabledException.class, DuplicateException.class})
    @ResponseBody
    public void onBadRequestException(HttpServletResponse response, Exception exception) throws IOException {
        response.sendError(406, exception.getMessage());
    }
}
