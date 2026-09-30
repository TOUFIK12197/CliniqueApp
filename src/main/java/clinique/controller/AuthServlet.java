package clinique.controller;

import clinique.dao.UtilisateurDAO;
import clinique.model.Utilisateur;
import clinique.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private UtilisateurDAO dao = new UtilisateurDAO();
    
    // Protection Brute Force : compteur de tentatives par IP
    private static final ConcurrentHashMap<String, AtomicInteger> loginAttempts 
        = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> lockoutTime 
        = new ConcurrentHashMap<>();
    
    private static final int MAX_ATTEMPTS  = 5;
    private static final int LOCKOUT_MINUTES = 15;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("logout".equals(action)) {
            // Invalider proprement la session
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            // Cookie de session expiré
            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setMaxAge(0);
            cookie.setPath(req.getContextPath());
            res.addCookie(cookie);
            res.sendRedirect(req.getContextPath() + "/auth?action=loginPage");
        } else {
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        
        String ip = getClientIP(req);
        
        // === BRUTE FORCE : Vérifier si IP est bloquée ===
        if (isLockedOut(ip)) {
            long remainingMs = lockoutTime.get(ip) - System.currentTimeMillis();
            long remainingMin = Math.max(1, remainingMs / 60000);
            req.setAttribute("erreur", 
                "Trop de tentatives échouées. Compte bloqué pour " + remainingMin + " minute(s).");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
            return;
        }
        
        String login = req.getParameter("login");
        String mdp   = req.getParameter("motDePasse");
        
        // === VALIDATION des entrées ===
        if (login == null || login.isEmpty() || mdp == null || mdp.isEmpty()) {
            req.setAttribute("erreur", "Identifiant et mot de passe obligatoires.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
            return;
        }
        
        // Vérification injection SQL sur le login
        if (ValidationUtils.hasSQLInjection(login)) {
            req.setAttribute("erreur", "Identifiants incorrects.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
            return;
        }
        
        Utilisateur user = dao.authentifier(login, mdp);
        
        if (user != null) {
            // Succès : réinitialiser le compteur
            loginAttempts.remove(ip);
            lockoutTime.remove(ip);
            
            // === SESSION FIXATION : Invalider l'ancienne session AVANT de créer une nouvelle ===
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            
            // Créer une NOUVELLE session
            HttpSession newSession = req.getSession(true);
            newSession.setAttribute("utilisateur", user);
            newSession.setMaxInactiveInterval(30 * 60); // 30 minutes
            
            // Cookie sécurisé
            Cookie sessionCookie = new Cookie("JSESSIONID", newSession.getId());
            sessionCookie.setHttpOnly(true);   // Inaccessible via JavaScript
            sessionCookie.setSecure(false);    // Mettre true en HTTPS
            sessionCookie.setPath(req.getContextPath());
            sessionCookie.setMaxAge(-1);       // Session cookie
            res.addCookie(sessionCookie);
            
            res.sendRedirect(req.getContextPath() + "/dashboard");
            
        } else {
            // Échec : incrémenter le compteur
            incrementAttempts(ip);
            int attempts = loginAttempts.get(ip).get();
            int remaining = MAX_ATTEMPTS - attempts;
            
            String erreur = "Identifiants incorrects.";
            if (remaining <= 2 && remaining > 0) {
                erreur += " (" + remaining + " tentative(s) restante(s) avant blocage)";
            }
            
            req.setAttribute("erreur", erreur);
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, res);
        }
    }
    
    private boolean isLockedOut(String ip) {
        if (!lockoutTime.containsKey(ip)) return false;
        if (System.currentTimeMillis() > lockoutTime.get(ip)) {
            // Lockout expiré : réinitialiser
            lockoutTime.remove(ip);
            loginAttempts.remove(ip);
            return false;
        }
        return true;
    }
    
    private void incrementAttempts(String ip) {
        loginAttempts.computeIfAbsent(ip, k -> new AtomicInteger(0)).incrementAndGet();
        int attempts = loginAttempts.get(ip).get();
        if (attempts >= MAX_ATTEMPTS) {
            lockoutTime.put(ip, System.currentTimeMillis() + (LOCKOUT_MINUTES * 60 * 1000L));
        }
    }
    
    private String getClientIP(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        // Prendre la première IP si plusieurs
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}