package ly.ui;

import ly.business.MyMail;
import ly.entity.MailMessage;
import ly.entity.SMTPServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MailUI {

    /**
     * This is used for referencing the values of these components
     */
    private static HashMap<String, JComponent> componentHashMap = new HashMap();
    private static String[] labels = {"From: ", "To: ", "Subject: ", "SMTP Server: ", "Username: ", "Password: ", "Message: "};
    private static String[] defaultValues = {
            "lydhfx00181@funix.edu.vn", "hailyduong@gmail.com", "Test Email",
            "587", "lydhfx00181@funix.edu.vn", "eekkoekkscgyakzp", "Hello, this is a test email"
    };
    private static JFrame frame = new JFrame("Send My Email");
    private static MailMessage mailEntity = MailMessage.getInstance();
    private static SMTPServer serverEntity = SMTPServer.getInstance();

    public static void main(String[] args) {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {

        // Set up the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up the content pane
        Container contentPane = frame.getContentPane();
        SpringLayout layout = new SpringLayout();
        contentPane.setLayout(layout);

        // Text fields
        int numberOfFields = labels.length;
        for (int i = 0; i < numberOfFields; i++) {
            String fieldName = labels[i];
            JLabel label = new JLabel(fieldName, JLabel.TRAILING);
            contentPane.add(label);

            Boolean isMessageIndex = i == (numberOfFields - 1);
            Boolean isSMTPServerIndex = i == 3;

            // For message textarea
            if (isMessageIndex) {
                JTextArea textArea = new JTextArea(defaultValues[i], 5, 30);
                label.setLabelFor(textArea);
                contentPane.add(textArea);
                componentHashMap.put(fieldName, textArea);

            }

            // For the SMTP Server drop down
            else if (isSMTPServerIndex) {
                String[] SmtpServerList = {"smtp.gmail.com (SSL)", "smtp.gmail.com (TLS)"};
                JComboBox serverComboBox = new JComboBox(SmtpServerList);
                label.setLabelFor(serverComboBox);
                contentPane.add(serverComboBox);
                componentHashMap.put(fieldName, serverComboBox);
            }

            // For other text fields
            else {

                JTextField textField = new JTextField(defaultValues[i], 30);
                label.setLabelFor(textField);
                contentPane.add(textField);
                componentHashMap.put(fieldName, textField);
            }
        }

        // Button
        JLabel buttonLabel = new JLabel("", JLabel.TRAILING);
        JButton sendButton = new JButton("Send E-Mail");
        buttonLabel.setLabelFor(sendButton);
        contentPane.add(buttonLabel);
        contentPane.add(sendButton);

        // Add action listener
        sendButton.addActionListener(new CustomActionListener());

        // Set up the constraints
        SpringUtilities.makeCompactGrid(
                contentPane,
                8, 2,
                6, 6,
                6, 6
        );

        // Display the window
        frame.pack();
        frame.setVisible(true);

    }

    public static void updateDataFromUiToModel() {

        JTextField toField = (JTextField) componentHashMap.get(labels[0]);
        String toText = toField.getText();

        JTextField fromField = (JTextField) componentHashMap.get(labels[1]);
        String fromText = fromField.getText();

        JTextField subjectField = (JTextField) componentHashMap.get(labels[2]);
        String subjectText = subjectField.getText();

        JTextField SMTPField = (JTextField) componentHashMap.get(labels[3]);
        String SMTPText = SMTPField.getText();

        JTextField userNameField = (JTextField) componentHashMap.get(labels[4]);
        String userNameText = userNameField.getText();

        JTextField passwordField = (JTextField) componentHashMap.get(labels[5]);
        String passwordText = passwordField.getText();

        JTextArea messageField = (JTextArea) componentHashMap.get(labels[6]);
        String messageText = messageField.getText();

        mailEntity.setData(fromText, toText, subjectText, messageText);
        serverEntity.setData(Integer.parseInt(SMTPText), "smtp.gmail.com", userNameText, passwordText);
    }

    static class CustomActionListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            // Update data from UI to Model
            updateDataFromUiToModel();

            // Send Mail
            boolean success = MyMail.sendEmail();

            // Inform the result
            if (success) {
                String toEmail = mailEntity.getTo();
                JOptionPane.showMessageDialog(frame, "Message has been sent to " + toEmail + " successfully");
            } else {
                JOptionPane.showMessageDialog(frame, "There was an error sending the message. Please try again.");
            }

        }


    }

    // TODO: Switching between the severs, should change the port

}
