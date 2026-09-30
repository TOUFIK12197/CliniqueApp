package clinique.model;

import java.sql.Timestamp;

public class RendezVous {
    private int idRdv;
    private Timestamp dateHeure;
    private String motif;
    private String statut;
    private Integer idConsultation;
    private Patient patient;
    private Medecin medecin;

    public RendezVous() {}

    public int getIdRdv() { return idRdv; }
    public void setIdRdv(int idRdv) { this.idRdv = idRdv; }
    public Timestamp getDateHeure() { return dateHeure; }
    public void setDateHeure(Timestamp dateHeure) { this.dateHeure = dateHeure; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Integer getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Integer idConsultation) { this.idConsultation = idConsultation; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Medecin getMedecin() { return medecin; }
    public void setMedecin(Medecin medecin) { this.medecin = medecin; }
}