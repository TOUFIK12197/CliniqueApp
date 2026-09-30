package clinique.filter;

import clinique.dao.PatientDAO;
import clinique.dao.RendezVousDAO;
import clinique.model.Utilisateur;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class IDORFilter implements Filter {

    private PatientDAO patientDAO     = new PatientDAO();
    private RendezVousDAO rendezVousDAO = new RendezVousDAO();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session     = req.getSession(false);

        if (session == null) {
            chain.doFilter(request, response);
            return;
        }

        Utilisateur user = (Utilisateur) session.getAttribute("utilisateur");
        if (user == null) {
            chain.doFilter(request, response);
            return;
        }

        String uri    = req.getRequestURI();
        String ctx    = req.getContextPath();
        String idStr  = req.getParameter("id");
        String action = req.getParameter("action");

        // Vérification IDOR pour les patients (médecin)
        if ("MEDECIN".equals(user.getRole()) 
            && uri.contains(ctx + "/patients") 
            && idStr != null && !idStr.isEmpty()) {
            
            try {
                int idPatient = Integer.parseInt(idStr);
                if (!patientDAO.isPatientDuMedecin(idPatient, user.getIdUtilisateur())) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    req.setAttribute("erreur", "Accès refusé : vous n'êtes pas autorisé à accéder à ce patient.");
                    req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                    return;
                }
            } catch (NumberFormatException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                return;
            }
        }

        // Vérification IDOR pour les RDV (médecin)
        if ("MEDECIN".equals(user.getRole()) 
            && uri.contains(ctx + "/rendezvous") 
            && idStr != null && !idStr.isEmpty()
            && action != null && !action.equals("planning") && !action.equals("nouveau")) {
            
            try {
                int idRdv = Integer.parseInt(idStr);
                if (!rendezVousDAO.isRdvDuMedecin(idRdv, user.getIdUtilisateur())) {
                    res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    req.setAttribute("erreur", "Accès refusé : ce rendez-vous ne vous appartient pas.");
                    req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                    return;
                }
            } catch (NumberFormatException e) {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                return;
            }
        }

        // Vérification IDOR pour les pages Admin
        if (!("ADMIN".equals(user.getRole())) 
            && uri.contains(ctx + "/admin")) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            req.setAttribute("erreur", "Accès refusé : vous n'avez pas les droits nécessaires.");
            req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
            return;
        }

        chain.doFilter(request, response);
    }
}