// Console entry point and menu router.
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner;
    private final UserService userService;

    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService(new UserDAO());
    }

    public static void main(String[] args) {
        new ConsoleApp().run();
    }

    private void run() {
        while (true) {
            System.out.println();
            System.out.println("=== Gym Management ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleRegister();
                case "2" -> handleLogin();
                case "3" -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void handleRegister() {
        try {
            System.out.println();
            System.out.println("--- Register User ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Phone number: ");
            String phoneNumber = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            System.out.print("Role (Admin, Trainer, Member): ");
            String role = scanner.nextLine();

            User user = userService.register(username, password, email, phoneNumber, address, role);
            System.out.println("User created: " + user.getUsername() + " (" + user.getRole() + ")");
        } catch (RuntimeException exception) {
            System.out.println("Registration failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleLogin() {
        try {
            System.out.println();
            System.out.println("--- Login ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            User user = userService.login(username, password);
            System.out.println("Login successful. Welcome, " + user.getUsername() + ".");
            showRoleMenu(user);
        } catch (RuntimeException exception) {
            System.out.println("Login failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void showRoleMenu(User user) {
        while (true) {
            System.out.println();
            System.out.println("--- " + user.getRole() + " Menu ---");

            if ("ADMIN".equals(user.getRole())) {
                System.out.println("1. View all users");
                System.out.println("2. View memberships and revenue");
            } else if ("TRAINER".equals(user.getRole())) {
                System.out.println("1. Manage workout classes");
                System.out.println("2. View assigned classes");
            } else {
                System.out.println("1. Browse workout classes");
                System.out.println("2. Purchase membership");
            }

            System.out.println("0. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Logged out.");
                return;
            }

            System.out.println("Feature actions are next. Role-based menu routing is now working.");
        }
    }
}
