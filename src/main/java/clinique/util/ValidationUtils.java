package clinique.util;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern NAME_PATTERN =
        Pattern.compile("^[a-zA-ZÀ-ÿ\\s''-]{2,100}$");

    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile(
        "(?i)(SELECT|INSERT|UPDATE|DELETE|DROP|CREATE|ALTER|EXEC|UNION|" +
        "SCRIPT|OR\\s+1=1|AND\\s+1=1|--|;|/\\*|\\*/)",
        Pattern.CASE_INSENSITIVE
    );

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean hasSQLInjection(String input) {
        if (input == null) return false;
        return SQL_INJECTION_PATTERN.matcher(input).find();
    }

    public static boolean isValidId(String idStr) {
        if (idStr == null || idStr.isEmpty()) return false;
        try {
            return Integer.parseInt(idStr) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return true;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return true;
        return phone.matches("^[0-9+\\-\\s().]{7,20}$");
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        return hasUpper && hasDigit;
    }
}