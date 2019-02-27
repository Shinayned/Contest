package service;

import contest.form.FileForm;
import contest.form.Form;
import contest.form.FormData;
import contest.form.Forms;
import exception.BadRequestException;
import exception.DuplicateException;
import exception.ResourceNotFoundException;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import repository.ApplicationRepository;
import repository.ContestRepository;

import javax.transaction.Transactional;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private DriveService driveService;

    @Override
    public String getPage(long contestId) throws ResourceNotFoundException {
        Contest contest = getContest(contestId);

        return contest.getPage();
    }

    @Override
    public List<Form> getForms(long contestId) throws ResourceNotFoundException {
        Contest contest = getContest(contestId);

        return contest.getForms();
    }

    @Override
    @Transactional
    public void submitApplication(long contestId, String participantEmail, Map<String, String[]> formsData, List<MultipartFile> files)
            throws ResourceNotFoundException, BadRequestException{
        Contest contest = getContest(contestId);
        Participant participant = participantService.getParticipantByEmail(participantEmail);

        checkForDuplicateSubmit(participant, contestId);

        try {
            contest.validateData(formsData, files);
        } catch (InvalidParameterException exception) {
            throw new BadRequestException(exception.getMessage());
        }

        List<FormData> applicationData = new ArrayList<>();
        applicationData.addAll(createFormDataList(formsData));
        applicationData.addAll(uploadFiles(contest, participant, files));

        Application application = new Application(contest, participant, applicationData);

        applicationRepository.save(application);
    }

    @Override
    public void createContest(Contest contest) throws DuplicateException {
        contestRepository.save(contest);
    }

    @Override
    public Contest getContest(long contestId) throws ResourceNotFoundException {
        Contest contest = contestRepository.findById(contestId).get();
        if (contest == null) {
            throw new ResourceNotFoundException("Contest №" + contestId + " is not exist.");
        }

        return contest;
    }

    private void checkForDuplicateSubmit(Participant participant, long contestId) {
        List<Application> applications = participant.getApplications();

        for (Application application : applications) {
            Contest contest = application.getContest();

            if (contest.getId() == contestId) {
                throw new DuplicateException(participant.getEmail() + " has already submitted application for " + contest.getName());
            }
        }
    }

    private List<FormData> createFormDataList(Map<String, String[]> formsData) {
        List<FormData> formDataList = new ArrayList<>();

        formsData.forEach((key, data) -> {
            FormData formData = new FormData(Integer.parseInt(key), data);
            formDataList.add(formData);
        });

        return formDataList;
    }

    private List<FormData> uploadFiles(Contest contest, Participant participant, List<MultipartFile> files) {
        return driveService.uploadApplicationFiles(contest, participant, files);
    }
}
