package Models;

import java.util.ArrayList;
import java.util.List;

public class Societe {
    private int id;
    private static String societe;
    private String adresse;
    private String numdetel;
    private String email;
    private String statutJuridique;
    private List<Fournisseur> fournisseurs;


    public Societe(int id, String societe, String adresse, String numdetel, String email, String statutJuridique) {
        this.id = id;
        this.societe = societe;
        this.adresse = adresse;
        this.numdetel = numdetel;
        this.email = email;
        this.statutJuridique = statutJuridique;
        this.fournisseurs = new ArrayList<>();
    }

    public Societe() {

    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumdetel() {
        return numdetel;
    }

    public void setNumdetel(String numdetel) {
        this.numdetel = numdetel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatutJuridique() {
        return statutJuridique;
    }

    public void setStatutJuridique(String statutJuridique) {
        this.statutJuridique = statutJuridique;
    }

    public List<Fournisseur> getFournisseurs() {
        return fournisseurs;
    }

    public void setFournisseurs(List<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public void addFournisseur(Fournisseur fournisseur) {
        if (fournisseurs == null) {
            fournisseurs = new ArrayList<>();
        }
        fournisseurs.add(fournisseur);
    }

    @Override
    public String toString() {
        return societe;
    }


    public int SetInt(int societe_id) {
        return societe_id;
    }
}


