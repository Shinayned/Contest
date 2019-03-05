package service;

import dto.ParticipantDto;
import email.EmailService;
import model.Participant;
import model.VerificationToken;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ParticipantRepository;
import repository.VerificationTokenRepository;
import exception.EmailExistsException;

import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

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
        if(existsByEmail(participantDto.getEmail()))
            throw new EmailExistsException(
                    "There is an account with that email adress:" + participantDto.getEmail());

        participantDto.setPassword(encoder.encode(participantDto.getPassword()));
        Participant participant = new Participant(participantDto);

        return participantRepository.save(participant);
    }

    @Override
    public Participant registerNewAccount(Participant participant) {
        if(existsByEmail(participant.getEmail()))
            throw new EmailExistsException(
                    "There is an account with that email address:" + participant.getEmail());

        participant.setPassword(encoder.encode(participant.getPassword()));

        return participantRepository.save(participant);
    }

    @Override
    public void updateAccount(Participant participant) {
        participantRepository.save(participant);
    }

    @Override
    @Transactional
    public void updateAccount(String email, ParticipantDto participantDto) {
        Participant participant = participantRepository.findByEmail(email);
        participant.updateInfo(participantDto);
        participantRepository.save(participant);
    }

    @Override
    public void createVerificationToken(Participant participant, String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken != null)
            tokenRepository.delete(verificationToken);

        verificationToken = new VerificationToken(participant, token);
        tokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    @Override
    public void sendResetPasswordUrl(Participant participant, String url) {
        String token = UUID.randomUUID().toString();
        VerificationToken restoreToken = new VerificationToken(participant, token);
        tokenRepository.save(restoreToken);

        String resetPasswordUrl = url + "/participant/changePassword?token=" + token;

        emailService.sendSimpleMessage(
                participant.getEmail(),
                "Reset Password",
                "To change password, please, follow this link: " + resetPasswordUrl
                );
    }

    @Override
    public void changePassword(String email, String newPassword) throws IllegalArgumentException{
        Participant participant = participantRepository.findByEmail(email);
        changePassword(participant, newPassword);
    }

    @Override
    public void changePassword(Participant participant, String newPassword) throws IllegalArgumentException{
        if (newPassword.isEmpty())
            throw new IllegalArgumentException("Password can't be empty.");

        newPassword = encoder.encode(newPassword);
        participant.setPassword(newPassword);
        participantRepository.save(participant);

        VerificationToken passwordToken = tokenRepository.findByParticipant(participant);
        if (passwordToken != null) {
            tokenRepository.delete(passwordToken);
        }
    }
}
