package clinique.controller;

import clinique.dao.*;
import clinique.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private MedecinDAO medecinDAO       = new MedecinDAO();
    private SecretaireDAO secretaireDAO = new SecretaireDAO();
    private SpecialiteDAO specialiteDAO = new SpecialiteDAO();
    private StatistiquesDAO statsDAO    = new StatistiquesDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Sécurité : seul l'ADMIN peut accéder
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {

            // ==================== MÉDECINS ====================
            case "medecins":
                req.setAttribute("medecins", medecinDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/medecins.jsp")
                   .forward(req, res);
                break;

            case "nouveauMedecin":
                req.setAttribute("specialites", specialiteDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/formMedecin.jsp")
                   .forward(req, res);
                break;

            case "modifierMedecin":
                int idMed = Integer.parseInt(req.getParameter("id"));
                Medecin medecin = medecinDAO.findById(idMed);
                int idSpecActuelle = medecinDAO.getSpecialiteId(idMed);
                req.setAttribute("medecin", medecin);
                req.setAttribute("idSpecActuelle", idSpecActuelle);
                req.setAttribute("specialites", specialiteDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/formMedecin.jsp")
                   .forward(req, res);
                break;

            case "supprimerMedecin":
                int idMedDel = Integer.parseInt(req.getParameter("id"));
                if (!medecinDAO.delete(idMedDel)) {
                    req.getSession().setAttribute("erreur",
                        "Impossible de supprimer : le médecin a des rendez-vous ou consultations.");
                }
                res.sendRedirect(req.getContextPath() + "/admin?action=medecins");
                break;

            case "toggleMedecin":
                int idMedT = Integer.parseInt(req.getParameter("id"));
                medecinDAO.toggleActif(idMedT);
                res.sendRedirect(req.getContextPath() + "/admin?action=medecins");
                break;

            // ==================== SECRÉTAIRES ====================
            case "secretaires":
                req.setAttribute("secretaires", secretaireDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/secretaires.jsp")
                   .forward(req, res);
                break;

            case "nouvelleSecretaire":
                req.getRequestDispatcher("/WEB-INF/views/admin/formSecretaire.jsp")
                   .forward(req, res);
                break;

            case "modifierSecretaire":
                int idSec = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("secretaire", secretaireDAO.findById(idSec));
                req.getRequestDispatcher("/WEB-INF/views/admin/formSecretaire.jsp")
                   .forward(req, res);
                break;

            case "supprimerSecretaire":
                int idSecDel = Integer.parseInt(req.getParameter("id"));
                secretaireDAO.delete(idSecDel);
                res.sendRedirect(req.getContextPath() + "/admin?action=secretaires");
                break;

            case "toggleSecretaire":
                int idSecT = Integer.parseInt(req.getParameter("id"));
                secretaireDAO.toggleActif(idSecT);
                res.sendRedirect(req.getContextPath() + "/admin?action=secretaires");
                break;

            // ==================== SPÉCIALITÉS ====================
            case "specialites":
                req.setAttribute("specialites", specialiteDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/specialites.jsp")
                   .forward(req, res);
                break;

            case "modifierSpecialite":
                int idSpecM = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("specialite", specialiteDAO.findById(idSpecM));
                req.getRequestDispatcher("/WEB-INF/views/admin/specialites.jsp")
                   .forward(req, res);
                break;

            case "supprimerSpecialite":
                int idSpecDel = Integer.parseInt(req.getParameter("id"));
                if (!specialiteDAO.delete(idSpecDel)) {
                    req.getSession().setAttribute("erreur",
                        "Impossible de supprimer : spécialité utilisée par un médecin.");
                }
                res.sendRedirect(req.getContextPath() + "/admin?action=specialites");
                break;

            // ==================== STATISTIQUES ====================
            case "statistiques":
                req.setAttribute("stats", statsDAO.getStatistiquesGlobales());
                req.setAttribute("statsParMedecin", statsDAO.getNbConsultationsParMedecin());
                req.setAttribute("rdvParStatut", statsDAO.getRdvParStatut());
                req.getRequestDispatcher("/WEB-INF/views/admin/statistiques.jsp")
                   .forward(req, res);
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {

            // ==================== SAUVEGARDER MÉDECIN ====================
            case "sauvegarderMedecin":
                String idMedStr = req.getParameter("idUtilisateur");
                String login    = req.getParameter("login");
                String nom      = req.getParameter("nom");
                String prenom   = req.getParameter("prenom");
                String tel      = req.getParameter("telephone");
                String email    = req.getParameter("email");
                String numOrdre = req.getParameter("numOrdre");
                String idSpecStr = req.getParameter("idSpecialite");
                int idSpec = (idSpecStr != null && !idSpecStr.isEmpty()) 
                             ? Integer.parseInt(idSpecStr) : 0;

                if (idMedStr == null || idMedStr.isEmpty()) {
                    // Création
                    String mdp = req.getParameter("motDePasse");
                    boolean ok = medecinDAO.insert(login, mdp, nom, prenom, tel, email, numOrdre, idSpec);
                    if (!ok) {
                        req.setAttribute("erreur", "Login déjà utilisé ou erreur de création.");
                        req.setAttribute("specialites", specialiteDAO.findAll());
                        req.getRequestDispatcher("/WEB-INF/views/admin/formMedecin.jsp")
                           .forward(req, res);
                        return;
                    }
                } else {
                    // Modification
                    int idMed = Integer.parseInt(idMedStr);
                    medecinDAO.update(idMed, login, nom, prenom, tel, email, numOrdre, idSpec);
                }
                res.sendRedirect(req.getContextPath() + "/admin?action=medecins");
                break;

            // ==================== SAUVEGARDER SECRÉTAIRE ====================
            case "sauvegarderSecretaire":
                String idSecStr  = req.getParameter("idUtilisateur");
                String loginSec  = req.getParameter("login");
                String nomSec    = req.getParameter("nom");
                String prenomSec = req.getParameter("prenom");
                String telSec    = req.getParameter("telephone");
                String emailSec  = req.getParameter("email");

                if (idSecStr == null || idSecStr.isEmpty()) {
                    // Création
                    String mdpSec = req.getParameter("motDePasse");
                    boolean ok = secretaireDAO.insert(loginSec, mdpSec, nomSec, prenomSec, telSec, emailSec);
                    if (!ok) {
                        req.setAttribute("erreur", "Login déjà utilisé ou erreur de création.");
                        req.getRequestDispatcher("/WEB-INF/views/admin/formSecretaire.jsp")
                           .forward(req, res);
                        return;
                    }
                } else {
                    // Modification
                    int idSec = Integer.parseInt(idSecStr);
                    secretaireDAO.update(idSec, loginSec, nomSec, prenomSec, telSec, emailSec);
                }
                res.sendRedirect(req.getContextPath() + "/admin?action=secretaires");
                break;

            // ==================== SAUVEGARDER SPÉCIALITÉ ====================
            case "sauvegarderSpecialite":
                String idSpecEditStr = req.getParameter("idspecialite");
                String libelle       = req.getParameter("libelle");
                String description   = req.getParameter("description");

                if (idSpecEditStr == null || idSpecEditStr.isEmpty()) {
                    specialiteDAO.insert(libelle, description);
                } else {
                    specialiteDAO.update(Integer.parseInt(idSpecEditStr), libelle, description);
                }
                res.sendRedirect(req.getContextPath() + "/admin?action=specialites");
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }
}