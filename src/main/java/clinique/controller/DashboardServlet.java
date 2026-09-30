package clinique.controller;

import clinique.dao.StatistiquesDAO;
import clinique.model.Utilisateur;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Utilisateur user = (Utilisateur) req.getSession().getAttribute("utilisateur");
        switch (user.getRole()) {
            case "ADMIN":
                req.setAttribute("stats", new StatistiquesDAO().getStatistiquesGlobales());
                req.getRequestDispatcher("/WEB-INF/views/dashboard/admin.jsp").forward(req, res);
                break;
            case "MEDECIN":
                req.getRequestDispatcher("/WEB-INF/views/dashboard/medecin.jsp").forward(req, res);
                break;
            case "SECRETAIRE":
                req.getRequestDispatcher("/WEB-INF/views/dashboard/secretaire.jsp").forward(req, res);
                break;
            default:
                res.sendRedirect(req.getContextPath() + "/auth?action=logout");
        }
    }
}