package Tests;

import Models.Fournisseur;
import Models.Societe;
import Services.FournisseurService;
import Services.SocieteService;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        FournisseurService fournisseurService = new FournisseurService();

        // Test Adding a Fournisseur
        // Create an instance of SocieteService
        SocieteService societeService = new SocieteService();

        // Retrieve all existing Societes from the database
        List<Societe> existingSocietes = societeService.recuperer();

        // Display the existing Societes for selection
        System.out.println("Existing Societes:");
        for (Societe societe : existingSocietes) {
            System.out.println("ID: " + societe.getId() + " - " + societe.getSociete());
        }

        // Assuming societeId is the ID of the selected Societe
        int societeId = 7; // Change this to the actual ID of the selected Societe

        Societe selectedSociete = null;
        for (Societe societe : existingSocietes) {
            if (societe.getId() == societeId) {
                selectedSociete = societe;
                break;
            }
        }

        // Check if the selected Societe exists
        if (selectedSociete != null) {
            // Create a new Fournisseur with the selected Societe
            Fournisseur newFournisseur = new Fournisseur("Test Fournisseur1", "Fournisseur Address", "98754321", "fournisseur@example.com", selectedSociete);
            fournisseurService.ajouter(newFournisseur);
            System.out.println("Fournisseur added successfully.");
        } else {
            System.out.println("Selected Societe does not exist.");
        }

        // Test Retrieving Fournisseurs
        try {
            List<Fournisseur> fournisseurs = fournisseurService.recuperer();
            System.out.println("Retrieved Fournisseurs:");
            for (Fournisseur fournisseur : fournisseurs) {
                System.out.println("ID: " + fournisseur.getId());
                System.out.println("Nom: " + fournisseur.getNom());
                System.out.println("Adresse: " + fournisseur.getAdresse());
                System.out.println("Numéro de téléphone: " + fournisseur.getNumdetel());
                System.out.println("Email: " + fournisseur.getEmail());
                System.out.println("Société: " + fournisseur.getSociete());
                System.out.println(); // Add a newline for readability
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving Fournisseurs: " + e.getMessage());
        }

        // Test Modifying a Fournisseur
        try {
            // Retrieve the Fournisseur that was created earlier
            Fournisseur fournisseurToUpdate = fournisseurService.recupererParId(1); // Assuming ID 1 exists
            if (fournisseurToUpdate != null) {
                fournisseurToUpdate.setNom("Updated Fournisseur Name");
                fournisseurService.modifier(fournisseurToUpdate);
                System.out.println("Fournisseur updated successfully.");
            } else {
                System.out.println("Fournisseur not found for update.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating Fournisseur: " + e.getMessage());
        }

        // Test Deleting a Fournisseur
        try {
            int fournisseurIdToDelete = 1; // Assuming ID 1 exists
            fournisseurService.supprimer(fournisseurIdToDelete);
            System.out.println("Fournisseur deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting Fournisseur: " + e.getMessage());
        }
    }
}
