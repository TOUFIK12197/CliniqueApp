package clinique.filter;

import clinique.util.CSRFUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class CSRFFilter implements Filter {
    
    // Méthodes qui modifient des données → vérification CSRF
    private static final String[] PROTECTED_METHODS = {"POST", "PUT", "DELETE", "PATCH"};
    
    // URLs exclues de la vérification CSRF
    private static final String[] EXCLUDED_URLS = {"/auth"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String method = req.getMethod().toUpperCase();
        String uri    = req.getRequestURI();
        
        // Vérifier seulement les POST
        if ("POST".equals(method) && !isExcluded(uri, req.getContextPath())) {
            
            HttpSession session = req.getSession(false);
            String submittedToken = req.getParameter("csrfToken");
            
            if (!CSRFUtils.isValidToken(session, submittedToken)) {
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                req.setAttribute("erreur", "Requête invalide (token CSRF manquant ou expiré). Veuillez réessayer.");
                req.getRequestDispatcher("/WEB-INF/views/erreur.jsp").forward(req, res);
                return;
            }
            
            // Régénérer le token après chaque POST réussi
            CSRFUtils.generateToken(session);
        }
        
        chain.doFilter(request, response);
    }
    
    private boolean isExcluded(String uri, String contextPath) {
        for (String url : EXCLUDED_URLS) {
            if (uri.equals(contextPath + url)) return true;
        }
        return false;
    }
}