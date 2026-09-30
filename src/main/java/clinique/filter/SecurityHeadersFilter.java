package clinique.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class SecurityHeadersFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        // Anti-Clickjacking : interdire l'intégration dans une iframe
        res.setHeader("X-Frame-Options", "DENY");
        
        // Anti-XSS navigateur
        res.setHeader("X-XSS-Protection", "1; mode=block");
        
        // Empêcher le MIME sniffing
        res.setHeader("X-Content-Type-Options", "nosniff");
        
        // Politique de référent
        res.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // Cache désactivé pour les pages sensibles
        res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        res.setHeader("Pragma", "no-cache");
        res.setHeader("Expires", "0");
        
        // Permissions Policy
        res.setHeader("Permissions-Policy", 
            "camera=(), microphone=(), geolocation=(), payment=()");
        
        // HSTS (activer en production avec HTTPS)
        // res.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        chain.doFilter(request, response);
    }
}