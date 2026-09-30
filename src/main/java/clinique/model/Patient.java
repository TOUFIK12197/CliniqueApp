package clinique.model;

import java.sql.Date;

public class Patient {
    private int idPatient;
    private String nomPatient;
    private String prenomPatient;
    private Date dateNaissance;
    private String telephonePatient;
    private String emailPatient;
    private String adresse;
    private String groupeSanguin;

    public Patient() {}

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }
    public String getNomPatient() { return nomPatient; }
    public void setNomPatient(String nomPatient) { this.nomPatient = nomPatient; }
    public String getPrenomPatient() { return prenomPatient; }
    public void setPrenomPatient(String prenomPatient) { this.prenomPatient = prenomPatient; }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getTelephonePatient() { return telephonePatient; }
    public void setTelephonePatient(String telephonePatient) { this.telephonePatient = telephonePatient; }
    public String getEmailPatient() { return emailPatient; }
    public void setEmailPatient(String emailPatient) { this.emailPatient = emailPatient; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public String getGroupeSanguin() { return groupeSanguin; }
    public void setGroupeSanguin(String groupeSanguin) { this.groupeSanguin = groupeSanguin; }
    public String getNomComplet() { return prenomPatient + " " + nomPatient; }
}