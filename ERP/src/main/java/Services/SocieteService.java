package Services;

import Models.Fournisseur;
import Models.Societe;
import ConnectionBD.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocieteService implements IService<Societe> {

    private Connection connection;
    private final MyDatabase myDatabase;

    public SocieteService() throws SQLException {
        myDatabase = MyDatabase.getInstance();
        connection = myDatabase.getConnection();
    }

    @Override
    public void ajouter(Societe societe) {
        String sql = "INSERT INTO societe (societe, adresse, numdetel, email, statutJuridique) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, societe.getSociete());
            preparedStatement.setString(2, societe.getAdresse());
            preparedStatement.setString(3, societe.getNumdetel());
            preparedStatement.setString(4, societe.getEmail());
            preparedStatement.setString(5, societe.getStatutJuridique());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la société: " + e.getMessage(), e);
        }
    }

    @Override
    public void modifier(Societe societe) {
        String sql = "UPDATE societe SET adresse = ?, numdetel = ?, email = ?, statutJuridique = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, societe.getAdresse());
            preparedStatement.setString(2, societe.getNumdetel());
            preparedStatement.setString(3, societe.getEmail());
            preparedStatement.setString(4, societe.getStatutJuridique());
            preparedStatement.setInt(5, societe.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la modification de la société: " + e.getMessage(), e);
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM societe WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la société: " + e.getMessage(), e);
        }
    }

    public List<Societe> recuperer() throws SQLException {
        List<Societe> societes = new ArrayList<>();
        String sql = "SELECT * FROM societe";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Societe societe = mapResultSetToSociete(resultSet);
                societes.add(societe);
            }
        }
        return societes;
    }

    @Override
    public Fournisseur recupererParId(int id) {
        return null;
    }

    public Societe recuperersParId(int id) {
        String sql = "SELECT * FROM societe WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSociete(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la récupération de la société par ID: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la société par ID: " + e.getMessage(), e);
        }
        return null;
    }


    public Societe recupererParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM societe WHERE societe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, nom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSociete(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la récupération de la société: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la société: " + e.getMessage(), e);
        }
        return null;
    }

    private Societe mapResultSetToSociete(ResultSet resultSet) throws SQLException {
        Societe societe = new Societe();
        societe.setId(resultSet.getInt("id"));
        societe.setSociete(resultSet.getString("societe"));
        // Map other attributes if needed
        return societe;
    }
    public List<String> recupererTousNomsSocietes() throws SQLException {
        List<String> nomsSocietes = new ArrayList<>();
        String sql = "SELECT DISTINCT societe FROM societe";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String nomSociete = resultSet.getString("societe");
                nomsSocietes.add(nomSociete);
            }
        }
        return nomsSocietes;
    }
    public Societe recupererParSociete(String selectedSociete) {
        String sql = "SELECT * FROM societe WHERE societe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, selectedSociete);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToSociete(resultSet);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de la récupération de la société: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la société: " + e.getMessage(), e);
        }
        return null;
    }

    public List<Societe> recupererToutesSocietes() throws SQLException {
        List<Societe> societes = new ArrayList<>();
        String sql = "SELECT * FROM societe";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Societe societe = new Societe();
                societe.setId(resultSet.getInt("id"));
                societe.setSociete(resultSet.getString("societe"));
                societes.add(societe);
            }
        }
        return societes;
    }


}
