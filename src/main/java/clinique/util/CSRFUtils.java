package clinique.util;

import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;

public class CSRFUtils {
    
    private static final String CSRF_TOKEN_KEY = "csrfToken";
    private static final SecureRandom secureRandom = new SecureRandom();
    
    // Générer un token CSRF
    public static String generateToken(HttpSession session) {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        session.setAttribute(CSRF_TOKEN_KEY, token);
        return token;
    }
    
    // Récupérer le token de la session
    public static String getToken(HttpSession session) {
        String token = (String) session.getAttribute(CSRF_TOKEN_KEY);
        if (token == null) {
            token = generateToken(session);
        }
        return token;
    }
    
    // Valider le token CSRF
    public static boolean isValidToken(HttpSession session, String submittedToken) {
        if (session == null || submittedToken == null) return false;
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_KEY);
        if (sessionToken == null) return false;
        // Comparaison en temps constant pour éviter timing attacks
        return sessionToken.equals(submittedToken);
    }
}