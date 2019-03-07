package config;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import converter.DateTimeConverter;
import google.GoogleDrive;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan({"service", "repository", "controller", "event", "exception", "component", "google", "email", "converter"})
@EntityScan("model")
@EnableJpaRepositories("repository")
@EnableTransactionManagement
@EnableScheduling
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/WEB-INF/static/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new DateTimeConverter());
    }

    @Bean
    public GoogleDrive googleDrive() throws IOException, GeneralSecurityException{
        String APPLICATION_NAME = "Google Drive API Java Quickstart";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        java.io.File CREDENTIALS_FOLDER = new java.io.File("src/main/resources/google");
        String CLIENT_FILE_NAME = "client_secret.json";
        List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

        return new GoogleDrive(APPLICATION_NAME,
                JSON_FACTORY,
                CREDENTIALS_FOLDER,
                CLIENT_FILE_NAME,
                SCOPES);
    }
}
