package clinique.controller;

import clinique.dao.DocumentMedicalDAO;
import clinique.dao.PatientDAO;
import clinique.dao.ConsultationDAO;
import clinique.model.DocumentMedical;
import clinique.model.Patient;
import clinique.model.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@WebServlet("/documents")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize       = 10 * 1024 * 1024,  // 10 MB max par fichier
    maxRequestSize    = 20 * 1024 * 1024   // 20 MB max par requête
)
public class DocumentServlet extends HttpServlet {

    private DocumentMedicalDAO docDAO   = new DocumentMedicalDAO();
    private PatientDAO patientDAO       = new PatientDAO();
    private ConsultationDAO consultDAO  = new ConsultationDAO();

    // Dossier de stockage des fichiers
    private static final String UPLOAD_DIR = "documents_medicaux";

    // Types MIME autorisés
    private static final String[] TYPES_AUTORISES = {
        "application/pdf",
        "image/jpeg", "image/jpg", "image/png",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    };

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {

            // Liste des documents d'un patient
            case "liste":
                int idPatient = Integer.parseInt(req.getParameter("idPatient"));
                Patient patient = patientDAO.findById(idPatient);
                List<DocumentMedical> documents = docDAO.findByPatient(idPatient);

                // Filtrer par type si demandé
                String filtre = req.getParameter("type");
                if (filtre != null && !filtre.isEmpty()) {
                    documents = docDAO.findByPatientAndType(idPatient, filtre);
                    req.setAttribute("filtreType", filtre);
                }

                req.setAttribute("patient", patient);
                req.setAttribute("documents", documents);
                req.setAttribute("nbDocuments", docDAO.countByPatient(idPatient));
                req.getRequestDispatcher("/WEB-INF/views/document/liste.jsp")
                   .forward(req, res);
                break;

            // Formulaire d'upload
            case "nouveau":
                int idP = Integer.parseInt(req.getParameter("idPatient"));
                req.setAttribute("patient", patientDAO.findById(idP));
                req.getRequestDispatcher("/WEB-INF/views/document/form.jsp")
                   .forward(req, res);
                break;

            // Télécharger un document
            case "telecharger":
                telechargerDocument(req, res, user);
                break;

            // Aperçu d'un document
            case "apercu":
                apercuDocument(req, res, user);
                break;

            // Supprimer un document
            case "supprimer":
                supprimerDocument(req, res, user);
                break;

            default:
                res.sendRedirect(req.getContextPath() + "/patients");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");

        // Secrétaire et Médecin peuvent uploader
        if ("SECRETAIRE".equals(user.getRole()) || "MEDECIN".equals(user.getRole())
                || "ADMIN".equals(user.getRole())) {

            String idPatientStr = req.getParameter("idPatient");
            int idPatient = Integer.parseInt(idPatientStr);

            // Récupérer le fichier uploadé
            Part filePart = req.getPart("fichier");

            if (filePart == null || filePart.getSize() == 0) {
                req.setAttribute("erreur", "Veuillez sélectionner un fichier.");
                req.setAttribute("patient", patientDAO.findById(idPatient));
                req.getRequestDispatcher("/WEB-INF/views/document/form.jsp")
                   .forward(req, res);
                return;
            }

            // Vérification taille
            if (filePart.getSize() > 10 * 1024 * 1024) {
                req.setAttribute("erreur", "Fichier trop volumineux (max 10 Mo).");
                req.setAttribute("patient", patientDAO.findById(idPatient));
                req.getRequestDispatcher("/WEB-INF/views/document/form.jsp")
                   .forward(req, res);
                return;
            }

            // Vérification type MIME
            String typeMime = filePart.getContentType();
            if (!isTypeAutorise(typeMime)) {
                req.setAttribute("erreur",
                    "Type de fichier non autorisé. Formats acceptés : PDF, JPG, PNG, DOC, DOCX");
                req.setAttribute("patient", patientDAO.findById(idPatient));
                req.getRequestDispatcher("/WEB-INF/views/document/form.jsp")
                   .forward(req, res);
                return;
            }

            // Nom original du fichier
            String nomOriginal = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Générer un nom unique pour éviter les conflits
            String extension  = nomOriginal.contains(".")
                ? nomOriginal.substring(nomOriginal.lastIndexOf("."))
                : "";
            String nomFichier = UUID.randomUUID().toString() + extension;

            // Dossier de stockage
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Sous-dossier par patient
            String patientDir = uploadPath + File.separator + "patient_" + idPatient;
            File patientFolder = new File(patientDir);
            if (!patientFolder.exists()) patientFolder.mkdirs();

            // Sauvegarder le fichier
            String cheminComplet = patientDir + File.separator + nomFichier;
            filePart.write(cheminComplet);

            // Créer l'objet DocumentMedical
            DocumentMedical doc = new DocumentMedical();
            doc.setIdPatient(idPatient);
            doc.setIdUtilisateur(user.getIdUtilisateur());
            doc.setTypeDocument(req.getParameter("typeDocument"));
            doc.setNomFichier(nomFichier);
            doc.setNomOriginal(nomOriginal);
            doc.setCheminFichier(cheminComplet);
            doc.setTailleFichier(filePart.getSize());
            doc.setTypeMime(typeMime);
            doc.setDescription(req.getParameter("description"));

            // Lier à une consultation si fourni
            String idConsultStr = req.getParameter("idConsultation");
            if (idConsultStr != null && !idConsultStr.isEmpty()) {
                doc.setIdConsultation(Integer.parseInt(idConsultStr));
            }

            boolean success = docDAO.insert(doc);

            if (success) {
                req.getSession().setAttribute("message",
                    "✅ Document '" + nomOriginal + "' uploadé avec succès.");
            } else {
                req.getSession().setAttribute("erreur",
                    "❌ Erreur lors de l'enregistrement du document.");
            }

            res.sendRedirect(req.getContextPath()
                + "/documents?action=liste&idPatient=" + idPatient);
        }
    }

    // ========== Méthodes privées ==========

    private void telechargerDocument(HttpServletRequest req,
                                     HttpServletResponse res,
                                     Utilisateur user)
            throws IOException, ServletException {
        int idDoc = Integer.parseInt(req.getParameter("id"));
        DocumentMedical doc = docDAO.findById(idDoc);

        if (doc == null) {
            res.sendRedirect(req.getContextPath() + "/patients");
            return;
        }

        File fichier = new File(doc.getCheminFichier());
        if (!fichier.exists()) {
            req.getSession().setAttribute("erreur", "Fichier introuvable sur le serveur.");
            res.sendRedirect(req.getContextPath()
                + "/documents?action=liste&idPatient=" + doc.getIdPatient());
            return;
        }

        // Headers pour téléchargement
        res.setContentType(doc.getTypeMime());
        res.setHeader("Content-Disposition",
            "attachment; filename=\"" + doc.getNomOriginal() + "\"");
        res.setContentLengthLong(fichier.length());

        // Envoyer le fichier
        try (FileInputStream fis = new FileInputStream(fichier);
             OutputStream out = res.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void apercuDocument(HttpServletRequest req,
                                 HttpServletResponse res,
                                 Utilisateur user)
            throws IOException, ServletException {
        int idDoc = Integer.parseInt(req.getParameter("id"));
        DocumentMedical doc = docDAO.findById(idDoc);

        if (doc == null) {
            res.sendRedirect(req.getContextPath() + "/patients");
            return;
        }

        File fichier = new File(doc.getCheminFichier());
        if (!fichier.exists()) {
            req.getSession().setAttribute("erreur", "Fichier introuvable.");
            res.sendRedirect(req.getContextPath()
                + "/documents?action=liste&idPatient=" + doc.getIdPatient());
            return;
        }

        // Affichage inline (aperçu dans le navigateur)
        res.setContentType(doc.getTypeMime());
        res.setHeader("Content-Disposition",
            "inline; filename=\"" + doc.getNomOriginal() + "\"");
        res.setContentLengthLong(fichier.length());

        try (FileInputStream fis = new FileInputStream(fichier);
             OutputStream out = res.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private void supprimerDocument(HttpServletRequest req,
                                    HttpServletResponse res,
                                    Utilisateur user)
            throws IOException {
        int idDoc = Integer.parseInt(req.getParameter("id"));
        DocumentMedical doc = docDAO.findById(idDoc);

        if (doc == null) {
            res.sendRedirect(req.getContextPath() + "/patients");
            return;
        }

        // Supprimer le fichier physique
        File fichier = new File(doc.getCheminFichier());
        if (fichier.exists()) fichier.delete();

        // Supprimer de la base
        docDAO.delete(idDoc);
        req.getSession().setAttribute("message", "Document supprimé avec succès.");

        res.sendRedirect(req.getContextPath()
            + "/documents?action=liste&idPatient=" + doc.getIdPatient());
    }

    private boolean isTypeAutorise(String typeMime) {
        if (typeMime == null) return false;
        for (String type : TYPES_AUTORISES) {
            if (type.equals(typeMime)) return true;
        }
        return false;
    }
}