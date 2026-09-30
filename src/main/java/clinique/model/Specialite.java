package clinique.model;

public class Specialite {
    private int idspecialite;
    private String libelle;
    private String description;

    public Specialite() {}

    public Specialite(int idspecialite, String libelle, String description) {
        this.idspecialite = idspecialite;
        this.libelle = libelle;
        this.description = description;
    }

    public int getIdspecialite() { return idspecialite; }
    public void setIdspecialite(int idspecialite) { this.idspecialite = idspecialite; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}