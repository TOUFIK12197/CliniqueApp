package clinique.model;

public class Secretaire extends Utilisateur {
    private int idPatient;

    public Secretaire() { super(); }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
}