package clinique.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Consultation {
    private int idConsultation;
    private Date dateConsult;
    private String diagnostic;
    private String tension;
    private String notes;
    private String prescription;
    private BigDecimal poids;
    private Patient patient;
    private Medecin medecin;

    public Consultation() {}

    public int getIdConsultation() { return idConsultation; }
    public void setIdConsultation(int idConsultation) { this.idConsultation = idConsultation; }
    public Date getDateConsult() { return dateConsult; }
    public void setDateConsult(Date dateConsult) { this.dateConsult = dateConsult; }
    public String getDiagnostic() { return diagnostic; }
    public void setDiagnostic(String diagnostic) { this.diagnostic = diagnostic; }
    public String getTension() { return tension; }
    public void setTension(String tension) { this.tension = tension; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
}