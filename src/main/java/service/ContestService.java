package service;

import contest.form.Form;
import contest.form.FormData;
import contest.form.Forms;
import exception.DuplicateException;
import exception.ResourceNotFoundException;
import model.Contest;
import model.ContestPage;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ContestService {
    String getPage(long contestId) throws ResourceNotFoundException;
    List<Form> getForms(long contestId) throws ResourceNotFoundException;
    List<Form> getAllForms(long contestId) throws ResourceNotFoundException;
    void submitApplication(long contestId, String participantEmail, Map<String, String[]> formsData, List<MultipartFile> files) throws ResourceNotFoundException;
    void createContest(Contest contest) throws DuplicateException;
    Contest getContest(long id) throws ResourceNotFoundException;
    void sendContestApplications(long contestId, String sendToEmail) throws ResourceNotFoundException, IOException;
}
