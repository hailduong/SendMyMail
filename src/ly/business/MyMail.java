package ly.business;

import ly.entity.MailMessage;
import ly.entity.SMTPServer;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MyMail {
    public static Session getSession() {

        // Set up the properties
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Set up the authenticator
        SMTPServer serverEntity = SMTPServer.getInstance();

        // TODO: update the values here
        final String username = "lydhfx00181@funix.edu.vn";
        final String password = "eekkoekkscgyakzp";
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
        // Get the session object
        Session session = Session.getInstance(properties, authenticator);

        return session;
    }

    public static boolean sendEmail() {
        try {
            MailMessage mailMessageEntity = MailMessage.getInstance();
            Session session = getSession();

            // Setup the email details
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailMessageEntity.getFrom()));
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(mailMessageEntity.getTo())[0]);
            message.setSubject(mailMessageEntity.getSubject());
            message.setText(mailMessageEntity.getMessage());

            // Send
            Transport.send(message);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
