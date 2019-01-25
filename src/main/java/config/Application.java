package config;

import model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import services.UserService;


@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserService userService) {
        return (args) -> {
            // save a couple of customers
            userService.addUser(new User("my.email@gmail.com", "qwerty"));
        };
    }

}
