// Member role model for gym users with standard member access.
public class Member extends User {
    public Member() {
        setRole("MEMBER");
    }

    public Member(int userId, String username, String passwordHash, String email, String phoneNumber, String address) {
        super(userId, username, passwordHash, email, phoneNumber, address, "MEMBER");
    }
}
