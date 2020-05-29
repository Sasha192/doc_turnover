package app.service.extapis;

import java.io.File;

public interface IMailService {

    boolean send(String to, String subject, String plainText);

    boolean sendFile(String to, String subject, String plaintext, File... files);

}
