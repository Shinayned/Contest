package service;

import dto.ParticipantDto;
import model.Participant;
import model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ParticipantRepository;
import repository.VerificationTokenRepository;
import exception.EmailExistsException;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    @Transactional(readOnly = true)
    public Participant getParticipantByEmail(String email) {
        return participantRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return participantRepository.existsByEmail(email);
    }

    @Override
    public Participant registerNewAccount(ParticipantDto participantDto) {
        if(existsByEmail(participantDto.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress:" + participantDto.getEmail());
        }

        participantDto.setPassword(encoder.encode(participantDto.getPassword()));
        Participant participant = new Participant(participantDto);

        return participantRepository.save(participant);
    }

    @Override
    public Participant registerNewAccount(Participant participant) {
        if(existsByEmail(participant.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress:" + participant.getEmail());
        }

        participant.setPassword(encoder.encode(participant.getPassword()));

        return participantRepository.save(participant);
    }

    @Override
    public void updateAccount(Participant user) {
        participantRepository.save(user);
    }

    @Override
    public void createVerificationToken(Participant participant, String token) {
        VerificationToken verificationToken = new VerificationToken(participant, token);
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }
}
