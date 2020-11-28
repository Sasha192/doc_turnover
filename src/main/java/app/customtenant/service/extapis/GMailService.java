package app.customtenant.service.extapis;

import app.configuration.spring.constants.Constants;
import app.tenantdefault.models.DocumentEntity;
import app.utils.ContentTypeFileUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import javax.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class GMailService implements IMailService {

    private InternetAddress emailInternetAddress;

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
            Message message = createMimeMessageHelper(to, subject, plainText, null);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            ;
        } catch (IOException e) {
            ;
        }
        return false;
    }

    @Override
    public boolean sendMimeMessage(String to, String subject,
                                   String html,
                                   FileSystemResource... resources) {
        try {
            if (to == null) {
                return false;
            }
            MimeMessage message = createMimeMessageHelper(to, subject, html, resources);
            Transport.send(message);
            return true;
        } catch (MessagingException | IOException e) {
            ;
        }
        return false;
    }

    @Override
    public boolean sendFile(String to, String subject,
                            String plaintext, List<DocumentEntity> entities) {
        try {
            if (to == null) {
                return false;
            }
            Multipart multipart = new MimeMultipart();
            for (DocumentEntity ent : entities) {
                addAttachment(multipart, ent);
            }
            Message message = createMessage(to, subject, plaintext, multipart);
            Transport.send(message);
            return true;
        } catch (AddressException e) {
            ;
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
            Multipart multipart = new MimeMultipart();
            for (File file : files) {
                addAttachment(multipart, file);
            }
            Message message = createMessage(to, subject, plaintext, multipart);
            Transport.send(message);
            return true;
        } catch (AddressException e) {
            ;
        } catch (MessagingException e) {
            ;
        }
        return false;
    }

    private Message createMessage(String to, String subject,
                                  String plaintext, Multipart multipart)
            throws MessagingException {
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(plaintext);
        Message message =
                createMessage(to, subject, plaintext);
        multipart.addBodyPart(textPart);
        message.setContent(multipart);
        return message;
    }

    private Message createMessage(String to, String subject, String plainText)
            throws MessagingException {
        subject = subject == null ? Constants.EMPTY_STRING : subject;
        plainText = plainText == null ? Constants.EMPTY_STRING : plainText;
        Session session = getSession();
        Message message = new MimeMessage(session);
        message.setFrom(emailInternetAddress);
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(plainText);
        return message;
    }

    private void addAttachment(Multipart multipart, File file) throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(file.getName());
        multipart.addBodyPart(messageBodyPart);
    }

    private void addAttachment(Multipart multipart, DocumentEntity entity)
            throws MessagingException {
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        byte[] bytes = entity.getDocument().getData();
        String contentType = ContentTypeFileUtil.getContentType(entity.getName());
        DataSource source = new ByteArrayDataSource(bytes, contentType);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(entity.getName());
        multipart.addBodyPart(messageBodyPart);
    }

    private MimeMessage createMimeMessageHelper(String to,
                                                String subject,
                                                String msg,
                                                FileSystemResource...resources)
            throws MessagingException, IOException {
        subject = subject == null ? Constants.EMPTY_STRING : subject;
        msg = msg == null ? Constants.EMPTY_STRING : msg;
        Session session = getSession();
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setFrom(emailInternetAddress);
        message.setTo(new InternetAddress(to));
        message.setSubject(subject);
        message.setText(msg, true);
        if (resources != null) {
            for (FileSystemResource resource : resources) {
                message.addInline("email_wallpaper", resource, "image/jpeg");
            }
        }
        return mimeMessage;
    }

    private Session getSession() throws AddressException {
        if (session != null) {
            return session;
        }
        final String login = env.getProperty("com.gmail.login");
        this.email = login;
        this.emailInternetAddress = new InternetAddress(this.email);
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
