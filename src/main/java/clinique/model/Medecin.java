package clinique.model;

public class Medecin extends Utilisateur {
    private String numOrdre;

    public Medecin() { super(); }

    public String getNumOrdre() { return numOrdre; }
    public void setNumOrdre(String numOrdre) { this.numOrdre = numOrdre; }
}