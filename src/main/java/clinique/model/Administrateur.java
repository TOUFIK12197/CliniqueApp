package clinique.model;

public class Administrateur extends Utilisateur {
    private Specialite specialite;
    private Statistiques statistiques;

    public Administrateur() { super(); }

    public Specialite getSpecialite() { return specialite; }
    public void setSpecialite(Specialite specialite) { this.specialite = specialite; }
    public Statistiques getStatistiques() { return statistiques; }
    public void setStatistiques(Statistiques statistiques) { this.statistiques = statistiques; }
}