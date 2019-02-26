package service;

import contest.form.FileForm;
import contest.form.Form;
import contest.form.FormData;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void submitApplication(long contestId, String participantEmail, Map<String, String[]> formsData, MultipartFile[] files)
            throws ResourceNotFoundException, BadRequestException{
        Contest contest = getContest(contestId);
        Participant participant = participantService.getParticipantByEmail(participantEmail);

        checkForDuplicateSubmit(participant, contestId);

        List<Form> contestForms = contest.getForms();
        List<Form> forms = new ArrayList<>();
        List<FileForm> fileForms = new ArrayList<>();

        contestForms.forEach(form -> {
            if (form instanceof FileForm)
                fileForms.add((FileForm) form);
            else
                forms.add(form);
        });

        List<FormData> applicationData = new ArrayList<>();
        applicationData.addAll(prepareApplicationData(forms, formsData, files));

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
            throw new ResourceNotFoundException("Contest #" + contestId + " is not exist.");
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

    private List<FormData> prepareApplicationData(List<Form> forms, Map<String, String[]> formsData, MultipartFile[] files) {
        List<FormData> applicationData = new ArrayList<>();

        List<FormData> fieldsData = prepareFormsData(forms, formsData);
        applicationData.addAll(fieldsData);

        List<FormData> filesData = prepareFileFormsData(forms, files);
        applicationData.addAll(fieldsData);

        return applicationData;
    }

    private List<FormData> prepareFormsData(List<Form> forms, Map<String, String[]> formsData) {
        List<FormData> applicationData = new ArrayList<>();

        forms.forEach(form -> {
            int formId = form.getId();
            String[] values = formsData.get(formId);

            if (form.validate(values))
                throw new BadRequestException("Form №" + formId + " invalid data");

            FormData formData = new FormData(formId, values);
            applicationData.add(formData);
        });

        return applicationData;
    }

    private List<FormData> prepareFileFormsData(Contest contest, Participant participant, List<FileForm> fileForms, List<MultipartFile> files) throws BadRequestException{
        List<FormData> fileData = new ArrayList<>();

        for (FileForm form : fileForms) {
            List<MultipartFile> formFiles = new ArrayList<>();

            for (int index = 0; index < files.size(); index++) {
                MultipartFile file = files.get(index);

                String fileId = file.getName();
                String formId = Integer.toString(form.getId());

                if (fileId.equals(formId)) {
                    formFiles.add(file);
                    files.remove(index);
                }
            }

            try {
                form.filesValidation(formFiles);
            } catch (InvalidParameterException exception) {
                throw new BadRequestException(exception.getMessage());
            }

        }

        if(!files.isEmpty())
            throw new BadRequestException("Request has excess files");

        return uploadFiles(contest, participant, files);
    }

    private List<FormData> uploadFiles(Contest contest, Participant participant, List<MultipartFile> files) {
        List<String> fileIds = driveService.uploadApplicationFiles(contest, participant, files);

    }
}
