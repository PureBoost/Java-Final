// Service class for registration and login business logic.
import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

public class UserService {
    private final UserDAO userDAO;
    private static final Logger LOGGER = AppLogger.getLogger();

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User register(String username, String password, String email, String phoneNumber, String address, String role) {
        validateRegistrationInput(username, password, email, role);

        String normalizedUsername = username.trim();
        String normalizedEmail = email.trim();
        String normalizedRole = role.trim().toUpperCase();

        if (userDAO.usernameExists(normalizedUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userDAO.emailExists(normalizedEmail)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = createRoleUser(normalizedRole);
        user.setUsername(normalizedUsername);
        user.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt(10)));
        user.setEmail(normalizedEmail);
        user.setPhoneNumber(phoneNumber == null ? null : phoneNumber.trim());
        user.setAddress(address == null ? null : address.trim());

        User createdUser = userDAO.createUser(user);
        LOGGER.info("Registered new user: username=" + createdUser.getUsername() + ", role=" + createdUser.getRole());
        return createdUser;
    }

    public User login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            throw new IllegalArgumentException("Username and password are required");
        }

        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            LOGGER.warning("Failed login attempt for unknown username: " + username.trim());
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!BCrypt.checkpw(password, user.getPasswordHash())) {
            LOGGER.warning("Failed login attempt due to bad password: username=" + username.trim());
            throw new IllegalArgumentException("Invalid username or password");
        }

        LOGGER.info("User logged in successfully: username=" + user.getUsername() + ", role=" + user.getRole());
        return user;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    private void validateRegistrationInput(String username, String password, String email, String role) {
        if (isBlank(username) || isBlank(password) || isBlank(email) || isBlank(role)) {
            throw new IllegalArgumentException("Username, password, email, and role are required");
        }

        if (password.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email format is invalid");
        }

        String normalizedRole = role.trim().toUpperCase();
        if (!"ADMIN".equals(normalizedRole) && !"TRAINER".equals(normalizedRole) && !"MEMBER".equals(normalizedRole)) {
            throw new IllegalArgumentException("Role must be Admin, Trainer, or Member");
        }
    }

    private User createRoleUser(String role) {
        return switch (role) {
            case "ADMIN" -> new Admin();
            case "TRAINER" -> new Trainer();
            case "MEMBER" -> new Member();
            default -> throw new IllegalArgumentException("Unsupported role");
        };
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
