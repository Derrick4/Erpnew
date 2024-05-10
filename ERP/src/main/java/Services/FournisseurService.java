package Services;

import Models.Fournisseur;
import Models.Societe;
import ConnectionBD.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FournisseurService {

    private final Connection connection;

    public FournisseurService() throws SQLException {
        connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Fournisseur fournisseur) {
        String sql = "INSERT INTO fournisseur (nom, adresse, numdetel, email, societe) VALUES (?, ?, ?, ?, ?) where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, fournisseur.getNom());
            statement.setString(2, fournisseur.getAdresse());
            statement.setString(3, fournisseur.getNumdetel());
            statement.setString(4, fournisseur.getEmail());
            statement.setString(5, fournisseur.getSociete());
            statement.executeUpdate();
        } catch (SQLException e) {
            // Handle the exception
        }
    }


    public void modifier(Fournisseur fournisseur) throws SQLException {
        String sql = "UPDATE fournisseur SET nom = ?, adresse = ?, numdetel = ?, email = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, fournisseur.getNom());
            preparedStatement.setString(2, fournisseur.getAdresse());
            preparedStatement.setString(3, fournisseur.getNumdetel());
            preparedStatement.setString(4, fournisseur.getEmail());
            preparedStatement.setInt(5, fournisseur.getId());
            preparedStatement.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM fournisseur WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public List<Fournisseur> recuperer() throws SQLException {
        List<Fournisseur> fournisseurs = new ArrayList<>();
        String sql = "SELECT f.*, s.societe FROM fournisseur f JOIN societe s ON f.societe_id = s.id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Fournisseur fournisseur = mapResultSetToFournisseur(resultSet);
                fournisseurs.add(fournisseur);
            }
        }
        return fournisseurs;
    }

    public Fournisseur recupererParId(int id) throws SQLException {
        String sql = "SELECT f.*, s.societe FROM fournisseur f JOIN societe s ON f.societe_id = s.id WHERE f.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToFournisseur(resultSet);
                }
            }
        }
        return null;
    }

    public int getTotalFournisseurs() throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM fournisseur";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        }
        return count;
    }

    private Fournisseur mapResultSetToFournisseur(ResultSet resultSet) throws SQLException {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(resultSet.getInt("id"));
        fournisseur.setNom(resultSet.getString("nom"));
        fournisseur.setAdresse(resultSet.getString("adresse"));
        fournisseur.setNumdetel(resultSet.getString("numdetel"));
        fournisseur.setEmail(resultSet.getString("email"));

        // Retrieve associated Societe
        Societe societe = new Societe();
        societe.SetInt(resultSet.getInt("societe_id"));
        societe.setSociete(resultSet.getString("societe"));
        fournisseur.setSociete(String.valueOf(societe));

        return fournisseur;
    }


}

