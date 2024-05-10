package Controllers;

import Models.Fournisseur;
import Models.Societe;

import Services.FournisseurService;
import Services.SocieteService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class AddFournisseurController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtAdresse;

    @FXML
    private TextField txtNum;

    @FXML
    private TextField txtEmail;

    @FXML
    private ChoiceBox<String> cmbSociete;

    private FournisseurService fournisseurService;

    // Regular expression patterns for validation
    private static final Pattern TEXT_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]+$");
    private static final Pattern NUMDET_PATTERN = Pattern.compile("^\\d{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.-]+@gmail\\.com$");

    public AddFournisseurController() throws SQLException {
        fournisseurService = new FournisseurService();
    }

    @FXML
    public void initialize() throws SQLException {
        SocieteService societeService = new SocieteService();
        List<String> nomsSocietes = societeService.recupererTousNomsSocietes();
        ObservableList<String> observableNomsSocietes = FXCollections.observableArrayList(nomsSocietes);
        cmbSociete.setItems(observableNomsSocietes);

        Twilio.init("ACd24b05d6db7e6c68520a7dfe6486dcbb", "0bab8e9c1490779211adb7aa44807a32");
    }

    @FXML
    public void ajouterFournisseur(ActionEvent event) throws SQLException {
        // Get fournisseur details from UI
        String nom = txtNom.getText();
        String adresse = txtAdresse.getText();
        String numdetel = txtNum.getText();
        String email = txtEmail.getText();

        // Retrieve the selected société object from the choice box
        String selectedSociete = cmbSociete.getValue();

        // If the selected société is null, show an error message
        if (selectedSociete == null) {
            showAlert("Veuillez sélectionner une société.", Alert.AlertType.ERROR);
            return;
        }

        // Retrieve the Societe object based on the selected société name
        SocieteService societeService = new SocieteService();
        Societe selectedSocieteName = societeService.recupererParSociete(selectedSociete);

        // Validate inputs
        if (!isValidText(nom)) {
            showAlert("Le nom doit être composé uniquement de lettres et d'espaces.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidNumdetel(numdetel)) {
            showAlert("Le numéro de téléphone doit être composé de 8 chiffres.", Alert.AlertType.ERROR);
            return;
        }
        if (!isValidEmail(email)) {
            showAlert("L'adresse e-mail doit être de la forme 'example@gmail.com'.", Alert.AlertType.ERROR);
            return;
        }

        // Create a new Fournisseur object
        Fournisseur fournisseur = new Fournisseur(nom, adresse, numdetel, email, selectedSocieteName);

        // Add the new Fournisseur to the database
        fournisseurService.ajouter(fournisseur);
        showAlert("Fournisseur ajouté avec succès.", Alert.AlertType.INFORMATION);
        String fournisseurPhoneNumber = "+216" + fournisseur.getNumdetel();
        String messageBody = "Bonjour M. " + fournisseur.getNom() + ", vous êtes affecté à la liste de fournisseur: ";
        sendSMS(fournisseurPhoneNumber, messageBody);

    }



    private boolean isValidText(String text) {
        return TEXT_PATTERN.matcher(text).matches();
    }

    private boolean isValidNumdetel(String numdetel) {
        return NUMDET_PATTERN.matcher(numdetel).matches();
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        String twilioPhoneNumber = "+15077101574";
        try {
            Message message = Message.creator(new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber(twilioPhoneNumber), messageBody).create();
            System.out.println("SMS sent successfully. SID: " + message.getSid());
        } catch (com.twilio.exception.ApiException e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
    }

}
