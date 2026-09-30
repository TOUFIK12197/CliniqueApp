package clinique.controller;

import clinique.dao.*;
import clinique.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/rendezvous")
public class RendezVousServlet extends HttpServlet {

    private RendezVousDAO rdvDAO = new RendezVousDAO();
    private PatientDAO patDAO = new PatientDAO();
    private MedecinDAO medDAO = new MedecinDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "liste";

        switch (action) {
            case "nouveau":
                req.setAttribute("patients", patDAO.findAll());
                req.setAttribute("medecins", medDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/rendezVous/form.jsp").forward(req, res);
                break;
            case "modifier":
                req.setAttribute("rdv", rdvDAO.findById(Integer.parseInt(req.getParameter("id"))));
                req.setAttribute("patients", patDAO.findAll());
                req.setAttribute("medecins", medDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/rendezVous/form.jsp").forward(req, res);
                break;
            case "annuler":
                rdvDAO.updateStatut(Integer.parseInt(req.getParameter("id")), "ANNULE");
                res.sendRedirect(req.getContextPath() + "/rendezvous");
                break;
            case "planning":
                Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
                String date = req.getParameter("date");
                if (date == null || date.isEmpty())
                    date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                req.setAttribute("planning", rdvDAO.findByMedecinAndDate(user.getIdUtilisateur(), date));
                req.setAttribute("date", date);
                req.getRequestDispatcher("/WEB-INF/views/rendezVous/planning.jsp").forward(req, res);
                break;
            default:
                req.setAttribute("rdvList", rdvDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/rendezVous/liste.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String idStr = req.getParameter("idRdv");
        String dateHeureStr = req.getParameter("dateHeure");

        RendezVous rdv = new RendezVous();
        if (idStr != null && !idStr.isEmpty()) rdv.setIdRdv(Integer.parseInt(idStr));
        rdv.setMotif(req.getParameter("motif"));
        rdv.setStatut(req.getParameter("statut") != null ? req.getParameter("statut") : "EN_ATTENTE");
        if (dateHeureStr != null && !dateHeureStr.isEmpty())
            rdv.setDateHeure(Timestamp.valueOf(dateHeureStr.replace("T", " ") + ":00"));

        if (rdv.getIdRdv() == 0) {
            int idMedecin = Integer.parseInt(req.getParameter("idMedecin"));
            int idPatient = Integer.parseInt(req.getParameter("idPatient"));
            boolean ok = rdvDAO.insert(rdv, idMedecin, idPatient);
            if (!ok) {
                req.setAttribute("erreur", "Le médecin n'est pas disponible à ce créneau !");
                req.setAttribute("patients", patDAO.findAll());
                req.setAttribute("medecins", medDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/rendezVous/form.jsp").forward(req, res);
                return;
            }
        } else {
            rdvDAO.update(rdv);
        }
        res.sendRedirect(req.getContextPath() + "/rendezvous");
    }
}