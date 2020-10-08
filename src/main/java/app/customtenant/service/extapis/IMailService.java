package app.customtenant.service.extapis;

import java.io.File;
import org.springframework.core.io.FileSystemResource;

public interface IMailService {

    boolean send(String to, String subject, String plainText);

    boolean sendMimeMessage(String to,
                            String subject,
                            String html,
                            FileSystemResource... resources);

    boolean sendFile(String to, String subject, String plaintext, File... files);
}
