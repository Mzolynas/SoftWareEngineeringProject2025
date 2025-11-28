public class AuthService {

    // Email must look like normal address, e.g. name@example.com
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Password: only letters and digits, no other special rules
    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        // at least 1 character, letters and numbers only
        return password.matches("^[A-Za-z0-9]+$");
    }
}
