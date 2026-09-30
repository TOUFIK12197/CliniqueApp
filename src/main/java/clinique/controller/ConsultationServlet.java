package clinique.controller;

import clinique.dao.*;
import clinique.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

@WebServlet("/consultations")
public class ConsultationServlet extends HttpServlet {

    private ConsultationDAO cDAO = new ConsultationDAO();
    private PatientDAO pDAO = new PatientDAO();
    private RendezVousDAO rdvDAO = new RendezVousDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "nouveau":
                String idRdvStr = req.getParameter("idRdv");
                if (idRdvStr != null)
                    req.setAttribute("rdv", rdvDAO.findById(Integer.parseInt(idRdvStr)));
                req.getRequestDispatcher("/WEB-INF/views/consultation/form.jsp").forward(req, res);
                break;
            case "historique":
                int idPatient = Integer.parseInt(req.getParameter("idPatient"));
                req.setAttribute("patient", pDAO.findById(idPatient));
                req.setAttribute("historique", cDAO.findByPatient(idPatient));
                req.getRequestDispatcher("/WEB-INF/views/consultation/historique.jsp").forward(req, res);
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
        int idRdv = Integer.parseInt(req.getParameter("idRdv"));
        int idPatient = Integer.parseInt(req.getParameter("idPatient"));

        Consultation c = new Consultation();
        c.setDateConsult(new Date(System.currentTimeMillis()));
        c.setDiagnostic(req.getParameter("diagnostic"));
        c.setTension(req.getParameter("tension"));
        c.setNotes(req.getParameter("notes"));
        c.setPrescription(req.getParameter("prescription"));
        String poids = req.getParameter("poids");
        if (poids != null && !poids.isEmpty()) c.setPoids(new BigDecimal(poids));

        cDAO.insert(c, user.getIdUtilisateur(), idPatient, idRdv);
        res.sendRedirect(req.getContextPath() + "/rendezvous?action=planning");
    }
}