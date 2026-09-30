package clinique.model;

public class Statistiques {
    private int idstatistiques;
    private String periode;
    private int nbRendezVous;
    private int nbPatient;
    private int nbAnnulations;
    private int nbconsultation;

    public Statistiques() {}

    public int getIdstatistiques() { return idstatistiques; }
    public void setIdstatistiques(int id) { this.idstatistiques = id; }
    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }
    public int getNbRendezVous() { return nbRendezVous; }
    public void setNbRendezVous(int nbRendezVous) { this.nbRendezVous = nbRendezVous; }
    public int getNbPatient() { return nbPatient; }
    public void setNbPatient(int nbPatient) { this.nbPatient = nbPatient; }
    public int getNbAnnulations() { return nbAnnulations; }
    public void setNbAnnulations(int nbAnnulations) { this.nbAnnulations = nbAnnulations; }
    public int getNbconsultation() { return nbconsultation; }
    public void setNbconsultation(int nbconsultation) { this.nbconsultation = nbconsultation; }
}