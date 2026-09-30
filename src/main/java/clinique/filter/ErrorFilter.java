package clinique.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebFilter("/*")
public class ErrorFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(ErrorFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Logger l'erreur complète côté serveur (pour le développeur)
            logger.log(Level.SEVERE, "Erreur interceptée : " + e.getMessage(), e);
            
            HttpServletRequest req  = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            
            // Afficher un message générique à l'utilisateur (pas de stack trace)
            req.setAttribute("erreur", 
                "Une erreur inattendue s'est produite. Veuillez contacter l'administrateur.");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            
            try {
                req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Impossible d'afficher la page d'erreur", ex);
            }
        }
    }
}