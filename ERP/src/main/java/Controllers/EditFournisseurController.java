package Controllers;

import Models.Fournisseur;
import Models.Societe;
import Services.FournisseurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.SQLException;
import java.util.Properties;

public class EditFournisseurController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtAdresse;

    @FXML
    private TextField txtNum;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtSociete;

    @FXML
    private Button btnupd;

    private FournisseurService fournisseurService;

    private Fournisseur fournisseurToUpdate;

    public EditFournisseurController() throws SQLException {
        fournisseurService = new FournisseurService();
    }

    public void initData(Fournisseur fournisseur) {
        fournisseurToUpdate = fournisseur;
        txtNom.setText(fournisseur.getNom());
        txtAdresse.setText(fournisseur.getAdresse());
        txtNum.setText(fournisseur.getNumdetel());
        txtEmail.setText(fournisseur.getEmail());
        txtSociete.setText(Societe.getSociete());
    }

    @FXML
    void updateFournisseur(ActionEvent event) {

        if (!validateInput()) {
            return;
        }

        fournisseurToUpdate.setNom(txtNom.getText());
        fournisseurToUpdate.setAdresse(txtAdresse.getText());
        fournisseurToUpdate.setNumdetel(txtNum.getText());
        fournisseurToUpdate.setEmail(txtEmail.getText());
        fournisseurToUpdate.setSociete(txtSociete.getText());

        try {
            fournisseurService.modifier(fournisseurToUpdate);
            showAlert("Fournisseur mis à jour avec succès.", Alert.AlertType.INFORMATION);

            // Send email notification
            String recipientEmail = txtEmail.getText();
            String nomFournisseur = txtNom.getText(); // Get the name of the fournisseur
            int fournisseurId = fournisseurToUpdate.getId(); // Get the ID of the fournisseur
            String subject = "Fournisseur mis à jour";
            String body = "Cher Fournisseur,\n\nLes informations de votre compte ont été mises à jour avec succès.\n\nNom du Fournisseur: " + nomFournisseur + "\nID du Fournisseur: " + fournisseurId;
            sendEmail(recipientEmail, subject, body);
        } catch (SQLException | MessagingException e) {
            showAlert("Erreur lors de la mise à jour du fournisseur: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateInput() {
        String email = txtEmail.getText();
        String phoneNumber = txtNum.getText();
        String name = txtNom.getText();

        if (!email.matches("\\b[A-Za-z0-9._%+-]+@gmail\\.com\\b")) {
            showAlert("Email format is invalid. Please enter a valid Gmail address.", Alert.AlertType.ERROR);
            return false;
        }

        if (!phoneNumber.matches("\\d{8}")) {
            showAlert("Phone number must be 8 digits.", Alert.AlertType.ERROR);
            return false;
        }

        if (!name.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            showAlert("Name must contain only letters.", Alert.AlertType.ERROR);
            return false;
        }

        if (txtNom.getText().isEmpty() ||
                txtAdresse.getText().isEmpty() ||
                txtNum.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtSociete.getText().isEmpty()) {
            showAlert("All fields are required.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static final String USERNAME = "fehdounis8@gmail.com";
    private static final String PASSWORD = "qszn dzex zfbx marr";

    // Method to send email
    private void sendEmail(String recipientEmail, String subject, String body) throws MessagingException {
        // Email configuration
        String HOST = "smtp.gmail.com";
        String PORT = "587";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }

}
