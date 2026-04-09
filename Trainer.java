// Trainer role model for staff who manage workout classes.
public class Trainer extends User {
    public Trainer() {
        setRole("TRAINER");
    }

    public Trainer(int userId, String username, String passwordHash, String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, "TRAINER");
    }
}
