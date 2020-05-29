package app.service.extapis;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class GMailService implements IMailService {

    private static final String EMPTY_STRING = "".intern();

    private Session session;

    private String email;

    @Autowired
    private Environment env;

    @Override
    public boolean send(String to, String subject, String plainText) {
        try {
            if (to == null) {
                return false;
            }
            Message message = createMessage(to, subject, plainText);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            ;
        }
        return false;
    }

    @Override
    public boolean sendFile(String to, String subject, String plaintext, File... files) {
        try {
            if (to == null) {
                return false;
            }
            Message message = createMessage(to, subject, plaintext);
            Multipart multipart = new MimeMultipart();
            for (File file : files) {
                addAttachment(multipart, file);
            }
            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (AddressException e) {
            ;
        } catch (MessagingException e) {
            ;
        }
        return false;
    }

    private void addAttachment(Multipart multipart, File file) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }

    private Message createMessage(String to, String subject, String plainText) throws MessagingException {
        subject = subject == null ? EMPTY_STRING : subject;
        plainText = plainText == null ? EMPTY_STRING : plainText;
        Session session = getSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        if (plainText == null) {
            plainText = "";
        }
        message.setText(plainText);
        return message;
    }

    private Session getSession() {
        if (session != null) {
            return session;
        }
        final String login = env.getProperty("com.gmail.login");
        this.email = login;
        String password = env.getProperty("com.gmail.password2f");
        if (password == null) {
            password = env.getProperty("com.gmail.password");
        }
        final Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        String finalPassword = password;
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, finalPassword);
                    }
                });
        return session;
    }
}
