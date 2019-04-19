package ly.ui;

import ly.business.MyMail;
import ly.entity.MailMessage;
import ly.entity.SMTPServer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class MailUI {

    /**
     * This is used for referencing the values of these components
     */
    private static HashMap<String, JComponent> componentHashMap = new HashMap();
    private static String[] labels = {
            "From: ", "To: ", "Subject: ", "SMTP Server: ", "Username: ", "Password: ",
            "Message: ", "Attachment: ", "Selected file: "};
    private static String[] defaultValues = {
            "lydhfx00181@funix.edu.vn", "hailyduong@gmail.com", "Test Email",
            "587", "lydhfx00181@funix.edu.vn", "eekkoekkscgyakzp", "Hello, this is a test email"
    };
    private static String[][] componentData = {
            {"name", "type", "label", "defaultValue"},
            {"FROM", "Text", "From: ", "lydhfx00181@funix.edu.vn"},
            {"TO", "Text", "From: ", "hailyduong@gmail.com"},
            {"SUBJECT", "Text", "From: ", "Test Email"},
            {"SMTP_SERVEER", "Text", "From: ", "587"},
            {"USER", "Text", "From: ", "lydhfx00181@funix.edu.vn"},
            {"PASSWORD", "Text", "From: ", "eekkoekkscgyakzp"},
            {"MESSAGE", "Text", "From: ", "Hello, this is a test email"},
            {"ATTACHMENT", "Text", "From: ", ""},
            {"SELECTED_FILE", "Text", "From: ", "No file selected!"}
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

            Boolean isMessageIndex = i == (numberOfFields - 3);
            Boolean isSMTPServerIndex = i == 3;
            Boolean isAttachmentIndex = i == (numberOfFields - 2);
            Boolean isAttachmentFilePath = i == (numberOfFields - 1);

            // For message textarea
            if (isMessageIndex) {
                JTextArea textArea = new JTextArea(defaultValues[i], 5, 30);
                label.setLabelFor(textArea);
                contentPane.add(textArea);
                componentHashMap.put(fieldName, textArea);

            }

            // For the SMTP Server drop down
            else if (isSMTPServerIndex) {
                String[] SmtpServerList = {
                        "smtp.gmail.com (SSL)",
                        "smtp.gmail.com (TLS)",
                        "smtp.mail.yahoo.com (SSL)",
                        "smtp.live.com (SSL)"
                };
                JComboBox serverPortDropdown = new JComboBox(SmtpServerList);
                serverPortDropdown.addActionListener(new ServerPortActionListener());
                label.setLabelFor(serverPortDropdown);
                contentPane.add(serverPortDropdown);
                componentHashMap.put(fieldName, serverPortDropdown);
            }

            // For the attachment field, open button
            else if (isAttachmentIndex) {
                JButton openButton = new JButton("Choose a file...");
                openButton.addActionListener(new OpenFileChooserLisenter());
                label.setLabelFor(openButton);
                contentPane.add(openButton);
                componentHashMap.put(fieldName, openButton);

            }

            // File path text
            else if (isAttachmentFilePath) {
                JLabel filePathLabel = new JLabel("No file chosen!");
                label.setLabelFor(filePathLabel);
                contentPane.add(filePathLabel);
                componentHashMap.put(fieldName, filePathLabel);
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
        sendButton.addActionListener(new ButtonActionListener());

        // Set up the constraints
        SpringUtilities.makeCompactGrid(
                contentPane,
                10, 2,
                6, 6,
                6, 6
        );

        // Display the window
        frame.pack();
        frame.setVisible(true);

    }

    public static void updateDataFromUiToModel() {

        JTextField fromField = (JTextField) componentHashMap.get(labels[0]);
        String fromText = fromField.getText();

        JTextField toField = (JTextField) componentHashMap.get(labels[1]);
        String toText = toField.getText();

        JTextField subjectField = (JTextField) componentHashMap.get(labels[2]);
        String subjectText = subjectField.getText();

        JTextField userNameField = (JTextField) componentHashMap.get(labels[4]);
        String userNameText = userNameField.getText();

        JTextField passwordField = (JTextField) componentHashMap.get(labels[5]);
        String passwordText = passwordField.getText();

        JTextArea messageField = (JTextArea) componentHashMap.get(labels[6]);
        String messageText = messageField.getText();

        mailEntity.setData(fromText, toText, subjectText, messageText);
        serverEntity.setUsername(userNameText);
        serverEntity.setPassword(passwordText);
    }

    static class ButtonActionListener implements ActionListener {

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

    static class ServerPortActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            // Change the port
            JComboBox serverComboBox = (JComboBox) event.getSource();
            String selectedServer = (String) serverComboBox.getSelectedItem();

            // Gmail SSL
            if (selectedServer.equals("smtp.gmail.com (SSL)")) {
                serverEntity.setServer("smtp.gmail.com");
                serverEntity.setPort(465);
            }

            // Yahoo SSL
            else if (selectedServer.equals("smtp.mail.yahoo.com (SSL)")) {
                serverEntity.setServer("smtp.mail.yahoo.com");
                serverEntity.setPort(465);
            }

            // Outlook SSL
            else if (selectedServer.equals("smtp.live.com (SSL)")) {
                serverEntity.setServer("smtp.live.com");
                serverEntity.setPort(465);
            }

            // Gmail TLS
            else {
                serverEntity.setServer("smtp.gmail.com");
                serverEntity.setPort(587);
            }

            System.out.println("Server port changed to: " + serverEntity.getPort());
            System.out.println("Server changed to: " + serverEntity.getServer());
        }
    }

    static class OpenFileChooserLisenter implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            // Open file chooser
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setDialogTitle("Select a file");

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getPath();
                System.out.println(filePath);

                JLabel selectedLabel = (JLabel) componentHashMap.get("Selected file: ");
                selectedLabel.setText(filePath);
                mailEntity.setAttachment(filePath);
            }

        }


    }

}
