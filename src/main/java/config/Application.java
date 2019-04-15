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
        contestService.createContest(firstContest());
        contestService.createContest(secondContest());
        contestService.createContest(thirdContest());
    }

    public Contest firstContest() {
        String contestName = "Конкурс на здобуття стипендій Кабінету Міністрів України для молодих вчених.";
        String contestTitle = contestName;
        String contestDescription = "Дата проведення: двічі на рік - до 1 квітня та до 1 жовтня, про що не пізніше, ніж за три місяці повідомляються за підпорядкуванням наукові установи, організації і вищі навчальні заклади.\n" +
                "Хто може прийняти участь : наукові працівники, науково-педагогічні працівники, аспіранти, ад’юнкти, докторанти, інші вчені та інженерно-технічні працівники наукових установ, організацій, вищих навчальних закладів. \n" +
                "Вік учасників: не може перевищувати 33 років для осіб, які мають вищу освіту не нижче другого (магістерського) рівня, та 38 років для осіб, які мають науковий ступінь доктора наук або навчаються в докторантурі, на час їх висування для участі у конкурсі на здобуття стипендій.\n";
        String imgUrl = "/static/img/stipendium.png";
        String pageUrl = "http://www.kdpu-nt.gov.ua/content/umovi-ta-poryadok-provedennya-konkursu-na-zdobuttya-stipendiy-kabinetu-ministriv-ukrayini-dl?fbclid=IwAR3VFbvxZUQN5MINi8sJodD9NEp3nkm2FUeAEnqRapH1f5i8DUCbVXM0jWk";

        Form files = new FileForm("Завантажити файл");

        List<Form> forms = new ArrayList<>();
        forms.add(files);

        Contest contest = new Contest(contestName, forms, contestTitle, contestDescription, imgUrl, pageUrl);
        contest.setActive(true);
        return contest;
    }

    public Contest secondContest() {
        String contestName = "Стипендії Президента України для молодих вчених";
        String contestTitle = contestName;
        String contestDescription = "Дата проведення: двічі на рік (до 25 квітня і 25 жовтня)\n" +
                "Хто може прийняти участь: аспіранти, наукові працівники науково-дослідних інститутів та інших наукових установ, організацій, підприємств (обсерваторій, ботанічних садів, дендропарків, заповідників, бібліотек, музеїв) (далі - організації), що перебувають у віданні Національної академії наук.\n" +
                "Вік учасників: не більше 35 років для осіб, які мають вищу освіту не нижче другого (магістерського) рівня, та 40 років для осіб, які мають науковий ступінь доктора наук або навчаються в докторантурі, на час їх висування для участі у конкурсі на здобуття стипендій Президента України.\n";
        String imgUrl = "/static/img/stipendium.png";
        String pageUrl = "http://kdpu-nt.gov.ua/content/polozhennya-pro-stipendiyi-prezidenta-ukrayini-dlya-molodikh-vchenikh";

        Form files = new FileForm("Завантажити файл");

        List<Form> forms = new ArrayList<>();
        forms.add(files);

        Contest contest = new Contest(contestName, forms, contestTitle, contestDescription, imgUrl, pageUrl);
        contest.setActive(true);
        return contest;
    }

    public Contest thirdContest() {
        String contestName = "Премія Верховної Ради України";
        String contestTitle = contestName;
        String contestDescription = "Дата проведення: до 15 березня\n" +
                "Хто може прийняти участь: \n" +
                "Вік учасників: не менше 35 років\n";
        String imgUrl = "/static/img/stipendium.png";
        String pageUrl = "http://www.kdpu-nt.gov.ua/content/umovi-ta-poryadok-provedennya-konkursu-na-zdobuttya-stipendiy-kabinetu-ministriv-ukrayini-dl?fbclid=IwAR3VFbvxZUQN5MINi8sJodD9NEp3nkm2FUeAEnqRapH1f5i8DUCbVXM0jWk";

        Form education = new StringForm("Освіта", "Освіта");
        Form specialty = new StringForm("Спеціальність", "Спеціальність");
        Form degree = new StringForm("Почесні звання", "Почесні звання");
        Form academicDegree = new StringForm("Науковий ступінь", "Науковий ступінь");
        Form scientistStatus = new StringForm("Вчене звання", "Вчене звання");
        Form amountOfScienceWorks = new StringForm("Кількість наукових праць", "Кількість наукових праць");
        Form identificationCode = new StringForm("Індивідуальний ідентифікаційний номер", "Індивідуальний ідентифікаційний номер");
        Form files = new FileForm("Завантажити файл");

        List<Form> forms = new ArrayList<>();
        forms.add(education);
        forms.add(specialty);
        forms.add(degree);
        forms.add(academicDegree);
        forms.add(scientistStatus);
        forms.add(amountOfScienceWorks);
        forms.add(identificationCode);
        forms.add(files);

        Contest contest = new Contest(contestName, forms, contestTitle, contestDescription, imgUrl, pageUrl);
        contest.setActive(true);
        return contest;
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
