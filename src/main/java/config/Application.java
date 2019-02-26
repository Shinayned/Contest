package config;

import contest.form.Form;
import contest.form.SelectForm;
import contest.form.enums.SelectFormType;
import contest.form.enums.SimpleFormType;
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
        List<Form> forms = new ArrayList<>();

        Form form = new Form(SimpleFormType.EMAIL, "Enter your email");
        List<String> variants = new ArrayList<>();
        variants.add("True");
        variants.add("False");
        Form form2 = new SelectForm(SelectFormType.SINGLE_SELECT_LIST, variants);

        forms.add(form);
        forms.add(form2);

        Contest contest = new Contest("Avionica", forms, "Avionica");

        contestService.createContest(contest);
    }

}
