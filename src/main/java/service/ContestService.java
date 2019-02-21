package service;

import exception.DuplicateException;
import exception.ResourceNotFoundException;
import model.Contest;
import model.ContestField;
import model.ContestPage;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ContestService {
    ContestPage getPageData(String contestName) throws ResourceNotFoundException;
    List<ContestField> getContestFields(String contestName) throws ResourceNotFoundException;

    void submitApplication(String contestName, String participantEmail, Map<String, String[]> filledForms) throws ResourceNotFoundException;
    void createContest(Contest contest) throws DuplicateException;
    Contest getContest(Contest contest) throws ResourceNotFoundException;
}
