package service;

import dto.ParticipantDto;
import model.Participant;
import model.Token;

public interface ParticipantService {
    Participant getParticipantByEmail(String email);
    boolean existsByEmail(String email);
    Participant registerNewAccount(ParticipantDto participantDto);
    Participant registerNewAccount(Participant participant);
    void updateAccount(Participant participant);
    void updateAccount(String email, ParticipantDto participantDto);
    void createVerificationToken(Participant participant, String token);
    void changePassword(String email, String newPassword);
    void changePassword(Participant participant, String newPassword);
    Token getToken(String token);
    void removeToken(Token token);
    void sendResetPasswordUrl(Participant participant, String url);
}
