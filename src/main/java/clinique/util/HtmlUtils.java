package clinique.util;

public class HtmlUtils {
    
    // Échapper tous les caractères HTML dangereux
    public static String escape(String input) {
        if (input == null) return "";
        return input
            .replace("&",  "&amp;")
            .replace("<",  "&lt;")
            .replace(">",  "&gt;")
            .replace("\"", "&quot;")
            .replace("'",  "&#x27;")
            .replace("/",  "&#x2F;")
            .replace("`",  "&#x60;");
    }
    
    // Nettoyer une entrée utilisateur
    public static String sanitize(String input) {
        if (input == null) return null;
        // Supprimer les balises script
        input = input.replaceAll("(?i)<script[^>]*>.*?</script>", "");
        // Supprimer les événements onclick, onload, etc.
        input = input.replaceAll("(?i)on\\w+\\s*=\\s*[\"'][^\"']*[\"']", "");
        // Supprimer javascript:
        input = input.replaceAll("(?i)javascript:", "");
        return input.trim();
    }
}