package clinique.model;

import java.sql.Timestamp;

public class DocumentMedical {

    private int idDocument;
    private int idPatient;
    private Integer idConsultation;
    private int idUtilisateur;
    private String typeDocument;
    private String nomFichier;
    private String nomOriginal;
    private String cheminFichier;
    private long tailleFichier;
    private String typeMime;
    private String description;
    private Timestamp dateUpload;

    // Relations pour affichage
    private Patient patient;
    private Utilisateur uploadeur;

    public DocumentMedical() {}

    // Getters & Setters
    public int getIdDocument() { return idDocument; }
    public void setIdDocument(int idDocument) { this.idDocument = idDocument; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public Integer getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Integer idConsultation) { this.idConsultation = idConsultation; }

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getTypeDocument() { return typeDocument; }
    public void setTypeDocument(String typeDocument) { this.typeDocument = typeDocument; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public String getNomOriginal() { return nomOriginal; }
    public void setNomOriginal(String nomOriginal) { this.nomOriginal = nomOriginal; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(long tailleFichier) { this.tailleFichier = tailleFichier; }

    public String getTypeMime() { return typeMime; }
    public void setTypeMime(String typeMime) { this.typeMime = typeMime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getDateUpload() { return dateUpload; }
    public void setDateUpload(Timestamp dateUpload) { this.dateUpload = dateUpload; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Utilisateur getUploadeur() { return uploadeur; }
    public void setUploadeur(Utilisateur uploadeur) { this.uploadeur = uploadeur; }

    // Méthode utilitaire : taille lisible
    public String getTailleFormatee() {
        if (tailleFichier < 1024) return tailleFichier + " o";
        if (tailleFichier < 1024 * 1024) return String.format("%.1f Ko", tailleFichier / 1024.0);
        return String.format("%.1f Mo", tailleFichier / (1024.0 * 1024));
    }

    // Méthode utilitaire : icône selon type MIME
    public String getIcone() {
        if (typeMime == null) return "📄";
        if (typeMime.contains("pdf"))   return "📕";
        if (typeMime.contains("image")) return "🖼️";
        if (typeMime.contains("word"))  return "📝";
        if (typeMime.contains("excel")) return "📊";
        return "📄";
    }

    // Méthode utilitaire : est-ce une image ?
    public boolean isImage() {
        return typeMime != null && typeMime.startsWith("image/");
    }

    // Méthode utilitaire : est-ce un PDF ?
    public boolean isPdf() {
        return typeMime != null && typeMime.contains("pdf");
    }
}