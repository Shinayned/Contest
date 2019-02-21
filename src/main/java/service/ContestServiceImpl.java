package service;

import exception.DuplicateException;
import exception.ResourceNotFoundException;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ApplicationRepository;
import repository.ContestRepository;
import repository.ParticipantRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ContestServiceImpl implements ContestService{
    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ParticipantService participantService;

    @Override
    public ContestPage getPageData(String contestName) throws ResourceNotFoundException {
        Contest contest = getContest(contestName);

        return contest.getPageData();
    }

    @Override
    public List<ContestField> getContestFields(String contestName) throws ResourceNotFoundException {
        Contest contest = getContest(contestName);

        return contest.getFields();
    }

    @Override
    @Transactional
    public void submitApplication(String contestName, String participantEmail, Map<String, String[]> filledForms) throws ResourceNotFoundException {
        Contest contest = getContest(contestName);
        checkForDuplicate(contest, participantEmail);

        Participant participant = participantService.getParticipantByEmail(participantEmail);
        Application application = new Application(contest, participant, filledForms);
        applicationRepository.save(application);
    }

    @Override
    public void createContest(Contest contest) throws DuplicateException {
        boolean isExists = contestRepository.existsByContestName(contest.getName());
        if(isExists) {
            throw new DuplicateException("Contest '" + contest.getName() + "' is already exists.");
        }

        contestRepository.save(contest);
    }

    @Override
    public Contest getContest(Contest contest) throws ResourceNotFoundException {
        return getContest(contest.getName());
    }

    private Contest getContest(String contestName) throws ResourceNotFoundException{
        Contest contest = contestRepository.findById(contestName).get();
        if(contest == null) {
            throw new ResourceNotFoundException("Contest '" + contestName + "' is not exist.");
        }

        return contest;
    }

    private void checkForDuplicate(Contest contest, String participantEmail) {
        List<Application> applications = contest.getApplications();

        for(Application application : applications) {
            Participant participant = application.getParticipant();

            if(participant.getEmail().equals(participantEmail)) {
                throw new DuplicateException(participant.getEmail() + " has already submitted application for " + contest.getName());
            }
        }
    }
}
