package config;

import model.Participant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.ParticipantService;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(ParticipantService participantService) {
        return (args) -> {
            // save a couple of customers
            Participant participant = new Participant("my.email@gmail.com", "qwerty");
            participant.setEnabled(true);
            participantService.registerNewAccount(participant);
        };
    }

}
