package clinique.filter;

import clinique.model.Utilisateur;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        boolean isPublic = uri.equals(ctx + "/")
                        || uri.equals(ctx + "/index.jsp")
                        || uri.contains("/auth")
                        || uri.contains("/css/");

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        Utilisateur user = (session != null) ? (Utilisateur) session.getAttribute("utilisateur") : null;
        if (user == null) {
            res.sendRedirect(ctx + "/auth?action=loginPage");
        } else {
            chain.doFilter(request, response);
        }
    }
}