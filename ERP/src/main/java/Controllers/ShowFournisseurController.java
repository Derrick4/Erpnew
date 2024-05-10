package Controllers;

import Models.Fournisseur;
import Models.Societe;
import Services.FournisseurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ShowFournisseurController {

    @FXML
    private Label titleLabel;

    @FXML
    private Button backbtn;

    @FXML
    private Button updbtn;

    @FXML
    private VBox detailsVBox;

    private FournisseurService fournisseurService;

    private int fournisseurId;

    public void initialize() throws SQLException {
        fournisseurService = new FournisseurService();
        afficherDetailsFournisseur();
    }

    public void initData(int fournisseurId) throws SQLException {
        this.fournisseurId = fournisseurId;
        afficherDetailsFournisseur();
    }

    private void afficherDetailsFournisseur() throws SQLException {
        Fournisseur fournisseur = fournisseurService.recupererParId(fournisseurId);
        if (fournisseur != null) {
            titleLabel.setText("Fournisseur Details");

            // Set details in labels
            setDetailLabel("ID: ", String.valueOf(fournisseur.getId()), true);
            setDetailLabel("Nom: ", fournisseur.getNom(), true);
            setDetailLabel("Adresse: ", fournisseur.getAdresse(), true);
            setDetailLabel("Numéro de téléphone: ", fournisseur.getNumdetel(), true);
            setDetailLabel("Email: ", fournisseur.getEmail(), true);
            setDetailLabel("Société: ", Societe.getSociete(), true);
        } else {
            titleLabel.setText("Fournisseur non trouvé.");
        }
    }

    private void setDetailLabel(String labelText, String detailText, boolean isBold) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: " + (isBold ? "bold" : "normal") + "; -fx-font-size: " + (isBold ? "14px;" : "16px;")); // Adjust font size here
        Label detail = new Label(detailText);
        VBox.setMargin(label, new Insets(3, 0, 0, 0)); // Adjust margin for spacing
        detailsVBox.getChildren().addAll(label, detail);
    }




    @FXML
    void updateFournisseur(ActionEvent event) {
        loadFXML("EditFournisseur.fxml", "Update Fournisseur");
    }

    @FXML
    void showFournisseur(ActionEvent event) {
        // Not sure what's the purpose of this method, it seems it loads the same FXML file
        loadFXML("ShowFournisseur.fxml", "Show Fournisseur");
    }

    private void loadFXML(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error loading FXML file
        }
    }
}

