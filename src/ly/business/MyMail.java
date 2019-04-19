package ly.business;

import ly.entity.MailMessage;
import ly.entity.SMTPServer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MyMail {

    private static MailMessage mailMessageEntity = MailMessage.getInstance();
    private static SMTPServer serverEntity = SMTPServer.getInstance();

    public static Session getSession() {

        // Set up the properties
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // TODO: Set up the authenticator
//        final String username = serverEntity.getUsername();
        final String username = "lydhfx00181@funix.edu.vn";
//        final String password = serverEntity.getPassword();
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

            Session session = getSession();

            // Setup the email details
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailMessageEntity.getFrom()));
            message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(mailMessageEntity.getTo())[0]);
            message.setSubject(mailMessageEntity.getSubject());

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mailMessageEntity.getMessage());

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = mailMessageEntity.getAttachment();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);

            // Add the attachment
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send
            Transport.send(message);
            return true;

        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }
}
