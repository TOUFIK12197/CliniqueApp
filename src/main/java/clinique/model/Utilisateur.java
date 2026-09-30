package clinique.model;

public class Utilisateur {
    private int idUtilisateur;
    private String login;
    private String motDePasse;
    private String role;
    private boolean actif;
    private String nomUtilisateur;
    private String prenomUtilisateur;
    private String telephoneUtilisateur;
    private String emailUtilisateur;

    public Utilisateur() {}

    public int getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(int idUtilisateur) { this.idUtilisateur = idUtilisateur; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
    public String getNomUtilisateur() { return nomUtilisateur; }
    public void setNomUtilisateur(String nomUtilisateur) { this.nomUtilisateur = nomUtilisateur; }
    public String getPrenomUtilisateur() { return prenomUtilisateur; }
    public void setPrenomUtilisateur(String prenomUtilisateur) { this.prenomUtilisateur = prenomUtilisateur; }
    public String getTelephoneUtilisateur() { return telephoneUtilisateur; }
    public void setTelephoneUtilisateur(String t) { this.telephoneUtilisateur = t; }
    public String getEmailUtilisateur() { return emailUtilisateur; }
    public void setEmailUtilisateur(String emailUtilisateur) { this.emailUtilisateur = emailUtilisateur; }
    public String getNomComplet() { return prenomUtilisateur + " " + nomUtilisateur; }
}