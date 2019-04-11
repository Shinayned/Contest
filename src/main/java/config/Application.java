package config;

import contest.form.*;
import contest.form.enums.SelectFormType;
import google.GoogleDrive;
import model.Contest;
import model.ContestPage;
import model.Participant;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import service.ContestService;
import service.ParticipantService;

import javax.swing.*;
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
            Participant participant = new Participant(
                    "rim.maxim@gmail.com",
                    "qwerty",
                    "Rymar Maksym",
                    new DateTime());
            participant.setEnabled(true);
            participant.setOrganization("organization");
            participant.setWorkAddress("NAU address");
            participant.setPhoneNumber("+38(800)555-35-35");
            participant.setPosition("докторант");
            participant.setScienceWorks("no works");
            participantService.registerNewAccount(participant);

            participant = new Participant(
                    "oleh.melnechyk@gmail.com",
                    "qwerty",
                    "Oleh Melnechyk",
                    new DateTime());
            participant.setEnabled(true);
            participant.setOrganization("HUB team");
            participant.setWorkAddress("Harmatna 53");
            participant.setPhoneNumber("+38(800)555-35-35");
            participant.setPosition("докторант");
            participant.setScienceWorks("no works");
            participantService.registerNewAccount(participant);

            contestTest();
        };
    }

    public void contestTest() {
        List<Form> forms = testForms();
        Contest contest = new Contest("Good Avionica", forms, "Good Avionica","description", "/static/img/stipendium.png",  "/nowhere");
        contest.setActive(true);
        contestService.createContest(contest);

        contest = new Contest("Bad Avionica", forms, "Bad Avionica", "description", "/static/img/stipendium.png", "/nowhere");
        contestService.createContest(contest);
    }

    public static List<Form> testForms() {
        List<Form> forms = new ArrayList<>();

        Form email = new EmailForm("Email");
        email.setTitle("Enter your second email address");

        Form date = new DateForm("Date");
        date.setTitle("Enter your best date");

        Form phone = new PhoneForm("Phone");
        phone.setTitle("Enter your second phone number");

        StringForm string = new StringForm("Some text");
        string.setTitle("Enter something sweet");
        string.setPlaceHolder("place holder....");
        string.setObligatory(true);

        List<String> selectFields = new ArrayList<>();
        selectFields.add("NAU");
        selectFields.add("KPI");

        SelectForm singleSelect = new SelectForm("University", SelectFormType.SINGLE_SELECT_LIST, selectFields);
        singleSelect.setTitle("Which is the best university?");
        singleSelect.setObligatory(true);

        SelectForm multipleSelect = new SelectForm("University", SelectFormType.MULTIPLE_SELECT_LIST, selectFields);
        multipleSelect.setTitle("Which is the best university?");
        multipleSelect.setObligatory(true);

        SelectForm listSelect = new SelectForm("University", SelectFormType.DROPDOWN_LIST, selectFields);
        listSelect.setTitle("Which is the best university?");
        listSelect.setObligatory(true);

        FileForm file = new FileForm("File");
        file.setTitle("Some file");
        file.setContentType("application/pdf");
        file.setObligatory(true);

        forms.add(email);
        forms.add(date);
        forms.add(phone);
        forms.add(string);
        forms.add(singleSelect);
        forms.add(multipleSelect);
        forms.add(listSelect);
        forms.add(file);

        return forms;
    }

}
