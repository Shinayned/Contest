package email;

import javax.activation.DataSource;

public interface EmailService {
    void sendSimpleMessage(String to,
                           String subject,
                           String text);
    void sendMessageWithAttachment(String to,
                                   String subject,
                                   String text,
                                   String fileName,
                                   DataSource fileSource);
}
