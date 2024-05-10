package Models;

public class Fournisseur {


    private int id;
    private String nom;
    private String adresse;
    private String numdetel;
    private String email;
    private String societe;

    public Fournisseur() {
        // Default constructor implementation
    }

    public Fournisseur(String nom, String adresse, String numdetel, String email, String societe) {
        this.nom = nom;
        this.adresse = adresse;
        this.numdetel = numdetel;
        this.email = email;
        this.societe = societe;
    }



    public Fournisseur(String nom, String adresse, String numdetel, String email, Societe societe) {
        this.nom = nom;
        this.adresse = adresse;
        this.numdetel = numdetel;
        this.email = email;
        this.societe = String.valueOf(societe);
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", numdetel='" + numdetel + '\'' +
                ", email='" + email + '\'' +
                ", societe='" + societe + '\'' +
                '}';
    }
}
