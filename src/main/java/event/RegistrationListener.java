package event;

import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import service.ParticipantService;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private ParticipantService service;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Participant participant = event.getParticipant();

        String token = UUID.randomUUID().toString();
        service.createVerificationToken(participant, token);

        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(participant.getEmail());
        email.setSubject("Registration Confirmation");
        email.setText("Please, follow the link to confirm your registration:\n" + confirmationUrl);
        mailSender.send(email);
    }
}
