package config;

import com.google.api.services.drive.model.File;
import model.Participant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import service.ParticipantService;
import google.GoogleDrive;

import java.util.Map;


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
            participant.setFullName("Vas Vas");
            participant.setEnabled(true);
            participantService.registerNewAccount(participant);

            java.io.File file = new java.io.File("C:\\Users\\Molodoy\\Desktop\\doc.pdf");
            GoogleDrive.uploadParticipantFiles(participant, file);

            Map<String, String> fileId = GoogleDrive.getFolderMap(participant);
            fileId.forEach((id, name) -> {
                try {
                    File result = GoogleDrive.downloadFile(id);
                    System.out.println("Found file: " + result.getName() + " with id: " + result.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        };
    }

}
