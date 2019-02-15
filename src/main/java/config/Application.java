package config;

import google.FileInfo;
import google.GoogleDrive;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.ParticipantService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    GoogleDrive googleDrive;

    @Bean
    public CommandLineRunner demo(ParticipantService participantService) {
        return (args) -> {
            // save a couple of customers

            Participant participant = new Participant("my.email@gmail.com", "qwerty");
            participant.setFullName("Vas Vas");
            participant.setEnabled(true);

            participantService.registerNewAccount(participant);

        };
    }

}
