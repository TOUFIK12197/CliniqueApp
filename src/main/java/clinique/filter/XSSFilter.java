package clinique.filter;

import clinique.util.HtmlUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebFilter("/*")
public class XSSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        // Headers de sécurité anti-XSS
        res.setHeader("X-XSS-Protection", "1; mode=block");
        res.setHeader("X-Content-Type-Options", "nosniff");
        res.setHeader("Content-Security-Policy",
            "default-src 'self'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "script-src 'self'; " +
            "img-src 'self' data:; " +
            "font-src 'self';");
        
        // Wrapper pour nettoyer les paramètres
        chain.doFilter(new XSSRequestWrapper(req), response);
    }
    
    // Wrapper qui nettoie tous les paramètres
    public static class XSSRequestWrapper extends HttpServletRequestWrapper {
        
        public XSSRequestWrapper(HttpServletRequest request) {
            super(request);
        }
        
        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            return HtmlUtils.sanitize(value);
        }
        
        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;
            String[] sanitized = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                sanitized[i] = HtmlUtils.sanitize(values[i]);
            }
            return sanitized;
        }
        
        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> originalMap = super.getParameterMap();
            Map<String, String[]> sanitizedMap = new HashMap<>();
            for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
                String[] values = entry.getValue();
                String[] sanitized = new String[values.length];
                for (int i = 0; i < values.length; i++) {
                    sanitized[i] = HtmlUtils.sanitize(values[i]);
                }
                sanitizedMap.put(entry.getKey(), sanitized);
            }
            return Collections.unmodifiableMap(sanitizedMap);
        }
    }
}