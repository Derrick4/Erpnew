package Controllers;


import Models.Fournisseur;
import Models.Societe;
import Services.FournisseurService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Models.Societe.getSociete;



public class IndexFournisseurController {

    @FXML
    private TextField Search;

    @FXML
    private TableView<Fournisseur> ListTable;

    @FXML
    private TableColumn<Fournisseur, Integer> IdCol;

    @FXML
    private TableColumn<Fournisseur, String> NomCol;

    @FXML
    private TableColumn<Fournisseur, String> AdresseCol;

    @FXML
    private TableColumn<Fournisseur, String> NumCol;

    @FXML
    private TableColumn<Fournisseur, String> EmailCol;

    @FXML
    private TableColumn<Fournisseur, String> SocieteCol;

    @FXML
    private Button addbtn;

    @FXML
    private Button updbtn;

    @FXML
    private Button showbtn;

    @FXML
    private Button deletebtn;

    @FXML
    private Label statisticsLabel;

    @FXML
    private BarChart<String, Number> statisticsChart;

    private boolean isChartVisible = false;

    private FournisseurService fournisseurService;


    private ObservableList<Fournisseur> fournisseurList;

    public void initialize() throws SQLException {
        fournisseurService = new FournisseurService();
        fournisseurList = FXCollections.observableArrayList(); // Initialize the list
        setupTable();
        loadData();
    }

    public void initData(Fournisseur fournisseur) {
        NomCol.setText("Nom: " + fournisseur.getNom() +
                "\nAdresse: " + fournisseur.getAdresse() +
                "\nNuméro de téléphone: " + fournisseur.getNumdetel() +
                "\nEmail: " + fournisseur.getEmail() +
                "\nSociété: " + fournisseur.getSociete()); // Assuming 'getName()' returns the name of the Societe
    }


    private void setupTable() {
        IdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        NomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        AdresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        NumCol.setCellValueFactory(new PropertyValueFactory<>("numdetel"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Use a custom cell value factory for SocieteCol to display only the societe name
        SocieteCol.setCellValueFactory(cellData -> {
            Fournisseur fournisseur = cellData.getValue();
            return new SimpleStringProperty(getSociete());
        });
    }


    @FXML
    private void refreshData() {
        loadData();
    }

    @FXML
    public void Onrecherches(javafx.scene.input.KeyEvent keyEvent) {
        String keyword = Search.getText();
        List<Fournisseur> filteredFournisseurs = filterFournisseurs(keyword);
        updateTableView(filteredFournisseurs);
    }

    private List<Fournisseur> filterFournisseurs(String keyword) {
        List<Fournisseur> filteredList = new ArrayList<>();

        if (fournisseurList != null) {
            for (Fournisseur fournisseur : fournisseurList) {
                // Check if any field of fournisseur matches the keyword
                if (fournisseur.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                        fournisseur.getAdresse().toLowerCase().contains(keyword.toLowerCase()) ||
                        fournisseur.getNumdetel().toLowerCase().contains(keyword.toLowerCase()) ||
                        fournisseur.getEmail().toLowerCase().contains(keyword.toLowerCase()) ||
                        fournisseur.getSociete().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(fournisseur);
                }
            }
        } else {
            System.err.println("The list fournisseurList is null.");
        }

        return filteredList;
    }

    private void updateTableView(List<Fournisseur> fournisseurs) {
        ListTable.getItems().clear();
        ListTable.getItems().addAll(fournisseurs);
    }

    private void loadData() {
        try {
            List<Fournisseur> fournisseurs = fournisseurService.recuperer();
            ListTable.getItems().clear(); // Clear existing items
            ListTable.getItems().addAll(fournisseurs); // Add updated items
            fournisseurList.clear(); // Clear the list before loading new data
            fournisseurList.addAll(fournisseurService.recuperer()); // Add data to the list
            updateTableView(fournisseurList);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error loading data from database: " + e.getMessage());
            alert.showAndWait();
        }
    }


    @FXML
    void AddFournisseur(ActionEvent event) {
        loadFXML("/AddFournisseur.fxml", "Add Fournisseur");
    }

    @FXML
    void updateFournisseur(ActionEvent event) {
        Fournisseur selectedFournisseur = ListTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditFournisseur.fxml"));
                Parent root = loader.load();

                // Get the controller
                EditFournisseurController editFournisseurController = loader.getController();

                // Pass the selected Fournisseur to the controller
                editFournisseurController.initData(selectedFournisseur);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Update Fournisseur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error loading FXML file
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No fournisseur selected.");
            alert.showAndWait();
        }
    }

    @FXML
    void showFournisseur(ActionEvent event) {
        Fournisseur selectedFournisseur = ListTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowFournisseur.fxml"));
                Parent root = loader.load();

                // Get the controller
                ShowFournisseurController showFournisseurController = loader.getController();

                // Pass the selected Fournisseur to the controller
                showFournisseurController.initData(selectedFournisseur.getId());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Show Fournisseur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error loading FXML file
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No fournisseur selected.");
            alert.showAndWait();
        }
    }


    @FXML
    void deleteFournisseur(ActionEvent event) {
        Fournisseur selectedFournisseur = ListTable.getSelectionModel().getSelectedItem();
        if (selectedFournisseur != null) {
            try {
                fournisseurService.supprimer(selectedFournisseur.getId());
                loadData(); // Reload data after deletion
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting fournisseur from database: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("No fournisseur selected.");
            alert.showAndWait();
        }
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

    @FXML
    void printListToPDF(ActionEvent event) {
        try {
            // Retrieve the list of fournisseurs
            List<Fournisseur> fournisseurList = fournisseurService.recuperer();

            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for writing to the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Define table parameters
            float margin = 50;
            float yStart = page.getMediaBox().getHeight() - margin;
            float bottomMargin = 70;
            float yPosition = yStart;

            // Define column widths
            float[] columnWidths = {30, 110, 110, 100, 130, 70}; // Reduced column widths

            // Add header row
            yPosition -= 20; // Adjust position for header row
            drawRow(contentStream, yPosition, columnWidths, true, "ID", "Nom", "Adresse", "Numéro de téléphone", "Email","Societe");

            // Add data rows
            yPosition -= 20; // Adjust position for data rows
            for (Fournisseur fournisseur : fournisseurList) {
                drawRow(contentStream, yPosition, columnWidths, false, String.valueOf(fournisseur.getId()), fournisseur.getNom(), fournisseur.getAdresse(), fournisseur.getNumdetel(), fournisseur.getEmail(), Societe.getSociete());
                yPosition -= 20; // Adjust position for next row
            }

            // Close the content stream
            contentStream.close();

            // Save the PDF document
            File file = new File("fournisseurs.pdf");
            document.save(file);
            document.close();

            // Open the PDF file
            Desktop.getDesktop().open(file);

            showAlert("PDF généré avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException | IOException e) {
            showAlert("Erreur lors de la génération du PDF: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void drawRow(PDPageContentStream contentStream, float y, float[] columnWidths, boolean isHeader, String... cellValues) throws IOException {
        float x = 60; // Initial x position
        float height = 20; // Row height

        // Set fill color for header row
        if (isHeader) {
            contentStream.setNonStrokingColor(Color.lightGray);
        }

        // Draw cells
        for (int i = 0; i < cellValues.length; i++) {
            float width = columnWidths[i];
            contentStream.addRect(x, y, width, height); // Draw cell border
            contentStream.stroke();
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 8);
            contentStream.setNonStrokingColor(Color.BLACK); // Set text color to black
            contentStream.newLineAtOffset(x + 2, y + 10); // Adjust text position
            contentStream.showText(cellValues[i]); // Draw cell text
            contentStream.endText();
            x += width;
        }
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showStatistics() {
        try {
            int totalFournisseurs = fournisseurService.getTotalFournisseurs();

            // Create a new series for the histogram
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            // Add data to the series
            series.getData().add(new XYChart.Data<>("Total Fournisseurs", totalFournisseurs));

            // Clear existing data
            statisticsChart.getData().clear();

            // Add the series to the chart
            statisticsChart.getData().add(series);

            // Show or hide the chart based on its current visibility
            if (isChartVisible) {
                statisticsChart.setVisible(false);
                isChartVisible = false;
            } else {
                statisticsChart.setVisible(true);
                isChartVisible = true;
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            // Display error message or handle it as needed
        }
    }

}


