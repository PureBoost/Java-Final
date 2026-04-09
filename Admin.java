// Admin role model for users with system access.
public class Admin extends User {
    public Admin() {
        setRole("ADMIN");
    }

    public Admin(int userId, String username, String passwordHash, String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, "ADMIN");
    }
}
