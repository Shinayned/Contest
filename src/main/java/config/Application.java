package config;

import enums.StringFormType;
import field.FileForm;
import field.Form;
import field.StringForm;
import model.ContestField;
import enums.FormType;
import google.GoogleDrive;
import model.Contest;
import model.ContestPage;
import model.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.ContestService;
import service.ParticipantService;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    GoogleDrive googleDrive;

    @Autowired
    ContestService contestService;

    @Bean
    public CommandLineRunner demo(ParticipantService participantService) {
        return (args) -> {
            // save a couple of customers

            Participant participant = new Participant("my.email@gmail.com", "qwerty");
            participant.setFullName("Vas Vas");
            participant.setEnabled(true);

            participantService.registerNewAccount(participant);

            contestTest();
        };
    }

    public void contestTest() {
        List<ContestField> fields = new ArrayList<>();
        fields.add(new ContestField("User_name", FormType.STRING));
        fields.add(new ContestField("Your_works", FormType.SELECT_LIST));
        Contest contest = new Contest("Avionica", fields, new ContestPage("Some BODY"));

        contestService.createContest(contest);
    }

}
