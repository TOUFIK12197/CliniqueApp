package clinique.controller;

import clinique.dao.PatientDAO;
import clinique.model.Patient;
import clinique.model.Utilisateur;
import clinique.util.MassAssignmentProtector;
import clinique.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {

    private PatientDAO dao = new PatientDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String role   = user.getRole();
        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {

            case "liste":
                List<Patient> patients;
                if ("MEDECIN".equals(role)) {
                    patients = dao.findByMedecin(user.getIdUtilisateur());
                } else {
                    patients = dao.findAll();
                }
                req.setAttribute("patients", patients);
                req.getRequestDispatcher("/WEB-INF/views/patient/liste.jsp")
                   .forward(req, res);
                break;

            case "rechercher":
                String terme = req.getParameter("terme");
                // Vérification injection SQL sur la recherche
                if (terme != null && ValidationUtils.hasSQLInjection(terme)) {
                    req.setAttribute("erreur", "Terme de recherche invalide.");
                    req.setAttribute("patients", dao.findAll());
                    req.getRequestDispatcher("/WEB-INF/views/patient/liste.jsp")
                       .forward(req, res);
                    return;
                }
                List<Patient> resultats;
                if ("MEDECIN".equals(role)) {
                    resultats = dao.rechercherByMedecin(terme, user.getIdUtilisateur());
                } else {
                    resultats = dao.rechercher(terme);
                }
                req.setAttribute("patients", resultats);
                req.setAttribute("terme", terme);
                req.getRequestDispatcher("/WEB-INF/views/patient/liste.jsp")
                   .forward(req, res);
                break;

            case "detail":
                String idDStr = req.getParameter("id");
                // Validation ID
                if (!ValidationUtils.isValidId(idDStr)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                int idD = Integer.parseInt(idDStr);
                // Vérification IDOR pour le médecin
                if ("MEDECIN".equals(role) && !dao.isPatientDuMedecin(idD, user.getIdUtilisateur())) {
                    req.setAttribute("erreur", "Accès refusé : ce patient ne vous appartient pas.");
                    req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                    return;
                }
                req.setAttribute("patient", dao.findById(idD));
                req.getRequestDispatcher("/WEB-INF/views/patient/detail.jsp")
                   .forward(req, res);
                break;

            case "nouveau":
                if ("MEDECIN".equals(role)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp")
                   .forward(req, res);
                break;

            case "modifier":
                if ("MEDECIN".equals(role)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                String idMStr = req.getParameter("id");
                if (!ValidationUtils.isValidId(idMStr)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                req.setAttribute("patient", dao.findById(Integer.parseInt(idMStr)));
                req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp")
                   .forward(req, res);
                break;

            case "supprimer":
                if ("MEDECIN".equals(role)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                String idSStr = req.getParameter("id");
                if (!ValidationUtils.isValidId(idSStr)) {
                    res.sendRedirect(req.getContextPath() + "/patients");
                    return;
                }
                int idS = Integer.parseInt(idSStr);
                if (!dao.delete(idS)) {
                    req.getSession().setAttribute("erreur",
                        "Impossible de supprimer : ce patient a des consultations enregistrées.");
                }
                res.sendRedirect(req.getContextPath() + "/patients");
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/patients?action=liste");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");

        // Médecins ne peuvent pas créer/modifier
        if ("MEDECIN".equals(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/patients");
            return;
        }

        // Validation des champs obligatoires
        String nom    = req.getParameter("nomPatient");
        String prenom = req.getParameter("prenomPatient");

        if (nom == null || nom.trim().isEmpty()) {
            req.setAttribute("erreur", "Le nom est obligatoire.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        if (prenom == null || prenom.trim().isEmpty()) {
            req.setAttribute("erreur", "Le prénom est obligatoire.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        // Vérification injection SQL
        if (ValidationUtils.hasSQLInjection(nom) || ValidationUtils.hasSQLInjection(prenom)) {
            req.setAttribute("erreur", "Caractères non autorisés détectés.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        // Validation email
        String email = req.getParameter("emailPatient");
        if (email != null && !email.isEmpty() && !ValidationUtils.isValidEmail(email)) {
            req.setAttribute("erreur", "Format d'email invalide.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        // Validation téléphone
        String tel = req.getParameter("telephonePatient");
        if (tel != null && !tel.isEmpty() && !ValidationUtils.isValidPhone(tel)) {
            req.setAttribute("erreur", "Format de téléphone invalide.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        // Construction sécurisée via MassAssignmentProtector
        Patient p = MassAssignmentProtector.buildPatient(req);

        // Vérification que le patient a bien été construit
        if (p.getNomPatient() == null || p.getNomPatient().isEmpty()) {
            req.setAttribute("erreur", "Erreur lors de la construction du patient.");
            req.getRequestDispatcher("/WEB-INF/views/patient/form.jsp").forward(req, res);
            return;
        }

        boolean success;
        if (p.getIdPatient() == 0) {
            success = dao.insert(p);
        } else {
            success = dao.update(p);
        }

        if (success) {
            req.getSession().setAttribute("message", "Patient enregistré avec succès.");
        } else {
            req.getSession().setAttribute("erreur", "Erreur lors de l'enregistrement.");
        }

        res.sendRedirect(req.getContextPath() + "/patients");
    }
}