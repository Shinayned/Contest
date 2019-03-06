package service;

import enums.TokenType;
import model.Participant;
import model.Token;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import repository.ParticipantRepository;
import repository.VerificationTokenRepository;

import java.util.List;

@Component
public class Scheduler {
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Scheduled(cron = "0 0 1 */2 * ?", zone = "UTC")
    public void removeExpiredTokens() {
        List<Token> tokens = IteratorUtils.toList(tokenRepository.findAll().iterator());

        System.out.println("********************* REMOVING TOKENS ************************");
        for (Token token : tokens) {
            if (token.isExpired()) {
                tokenRepository.delete(token);

                Participant participant = token.getParticipant();
                System.out.println("[TOKEN " + token.getId() + " " + token.getType() + "] for " + participant.getEmail() + " has been removed.");

                if (token.getType() == TokenType.ACCOUNT_ACTIVATION)
                    participantRepository.delete(participant);
            }
        }
        System.out.println("**************************************************************");
    }
}
