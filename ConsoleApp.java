// Console entry point and menu router.
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner;
    private final UserService userService;
    private final MembershipService membershipService;
    private final WorkoutClassService workoutClassService;
    private final GymMerchService gymMerchService;

    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        this.userService = new UserService(new UserDAO());
        this.membershipService = new MembershipService(new MembershipDAO());
        this.workoutClassService = new WorkoutClassService(new WorkoutClassDAO());
        this.gymMerchService = new GymMerchService(new GymMerchDAO());
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
                System.out.println("1. View user console");
                System.out.println("2. View memberships and revenue");
                System.out.println("3. Manage merch");
            } else if ("TRAINER".equals(user.getRole())) {
                System.out.println("1. Manage workout classes");
                System.out.println("2. View assigned classes");
                System.out.println("3. Purchase membership");
                System.out.println("4. View merch list");
            } else {
                System.out.println("1. Browse workout classes");
                System.out.println("2. Purchase membership");
                System.out.println("3. View merch list");
            }

            System.out.println("0. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            if ("0".equals(choice)) {
                System.out.println("Logged out.");
                return;
            }

            if ("ADMIN".equals(user.getRole())) {
                handleAdminOption(choice, user);
            } else if ("TRAINER".equals(user.getRole())) {
                handleTrainerOption(choice, user);
            } else {
                handleMemberOption(choice, user);
            }
        }
    }

    private void handleAdminOption(String choice, User adminUser) {
        switch (choice) {
            case "1" -> handleAdminUsers(adminUser);
            case "2" -> handleAdminMembershipRevenue();
            case "3" -> handleAdminMerchMenu();
            default -> System.out.println("Invalid option.");
        }
    }

    private void handleAdminMerchMenu() {
        while (true) {
            System.out.println();
            System.out.println("--- Admin: Merch ---");
            System.out.println("1. Add merch item");
            System.out.println("2. Update merch price/stock");
            System.out.println("3. Print stock report");
            System.out.println("4. View total stock value");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleAddMerchItem();
                case "2" -> handleUpdateMerchItem();
                case "3" -> handleBrowseMerch();
                case "4" -> handleViewTotalStockValue();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleAddMerchItem() {
        try {
            System.out.print("Merch name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Merch type: ");
            String type = scanner.nextLine().trim();
            System.out.print("Price: ");
            double price = readDouble(scanner.nextLine(), "Price must be a valid number");
            System.out.print("Quantity in stock: ");
            int quantity = readInt(scanner.nextLine(), "Quantity must be a valid whole number");

            GymMerch merch = new GymMerch();
            merch.setMerchName(name);
            merch.setMerchType(type);
            merch.setMerchPrice(price);
            merch.setQuantityInStock(quantity);

            GymMerch created = gymMerchService.addItem(merch);
            System.out.println("Merch item added. ID: " + created.getMerchId());
        } catch (RuntimeException exception) {
            System.out.println("Add merch failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleUpdateMerchItem() {
        try {
            System.out.print("Merch ID to update: ");
            int merchId = readInt(scanner.nextLine(), "Merch ID must be a valid whole number");

            GymMerch existing = gymMerchService.findById(merchId);
            if (existing == null) {
                System.out.println("Merch item not found.");
                return;
            }

            System.out.print("New name (current: " + existing.getMerchName() + "): ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) {
                existing.setMerchName(name);
            }

            System.out.print("New type (current: " + existing.getMerchType() + "): ");
            String type = scanner.nextLine().trim();
            if (!type.isEmpty()) {
                existing.setMerchType(type);
            }

            System.out.print("New price (current: " + existing.getMerchPrice() + "): ");
            String priceInput = scanner.nextLine().trim();
            if (!priceInput.isEmpty()) {
                existing.setMerchPrice(readDouble(priceInput, "Price must be a valid number"));
            }

            System.out.print("New quantity (current: " + existing.getQuantityInStock() + "): ");
            String quantityInput = scanner.nextLine().trim();
            if (!quantityInput.isEmpty()) {
                existing.setQuantityInStock(readInt(quantityInput, "Quantity must be a valid whole number"));
            }

            boolean updated = gymMerchService.updateItem(existing);
            System.out.println(updated ? "Merch item updated." : "No merch item was updated.");
        } catch (RuntimeException exception) {
            System.out.println("Update merch failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleViewTotalStockValue() {
        try {
            double totalValue = gymMerchService.getTotalStockValue();
            System.out.println("Total stock value: $" + String.format("%.2f", totalValue));
        } catch (RuntimeException exception) {
            System.out.println("Unable to calculate stock value: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleAdminUsers(User adminUser) {
        while (true) {
            System.out.println();
            System.out.println("--- User Console ---");
            System.out.println("1. View all users");
            System.out.println("2. Delete user");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showAllUsers();
                case "2" -> handleDeleteUser(adminUser);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showAllUsers() {
        try {
            List<User> users = userService.getUserDAO().getAllUsers();
            System.out.println();
            System.out.println("--- All Users ---");
            if (users.isEmpty()) {
                System.out.println("No users found.");
                return;
            }

            for (User user : users) {
                System.out.println("ID: " + user.getUserId());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Phone: " + user.getPhoneNumber());
                System.out.println("Address: " + user.getAddress());
                System.out.println("Role: " + user.getRole());
                System.out.println();
            }
        } catch (RuntimeException exception) {
            System.out.println("Unable to load users: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleDeleteUser(User adminUser) {
        try {
            System.out.print("Enter user ID to delete: ");
            int userId = readInt(scanner.nextLine(), "User ID must be a valid whole number");

            if (userId == adminUser.getUserId()) {
                System.out.println("You cannot delete your own admin account.");
                return;
            }

            System.out.println("1. Confirm delete");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String confirm = scanner.nextLine().trim();
            if ("0".equals(confirm)) {
                System.out.println("Delete cancelled.");
                return;
            }
            if (!"1".equals(confirm)) {
                System.out.println("Invalid option.");
                return;
            }

            boolean deleted = userService.getUserDAO().deleteUserById(userId);
            System.out.println(deleted ? "User deleted." : "No user found with that ID.");
        } catch (RuntimeException exception) {
            System.out.println("Delete user failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleAdminMembershipRevenue() {
        try {
            List<Membership> memberships = membershipService.getMembershipDAO().getAllMemberships();
            double totalRevenue = membershipService.getMembershipDAO().getTotalRevenueCurrentYear();

            System.out.println();
            System.out.println("--- Memberships ---");
            if (memberships.isEmpty()) {
                System.out.println("No memberships found.");
            } else {
                for (Membership membership : memberships) {
                    System.out.println("Membership ID: " + membership.getMembershipId());
                    System.out.println("Type: " + membership.getMembershipType());
                    System.out.println("Description: " + membership.getMembershipDescription());
                    System.out.println("Cost: $" + String.format("%.2f", membership.getMembershipCost()));
                    System.out.println("Member ID: " + membership.getMemberId());
                    System.out.println("Start Date: " + membership.getStartDate());
                    System.out.println("End Date: " + membership.getEndDate());
                    System.out.println();
                }
            }

            System.out.println("Total membership revenue this year: $" + String.format("%.2f", totalRevenue));
        } catch (RuntimeException exception) {
            System.out.println("Unable to load memberships/revenue: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleTrainerOption(String choice, User user) {
        switch (choice) {
            case "1" -> handleManageWorkoutClasses(user);
            case "2" -> handleViewAssignedClasses(user);
            case "3" -> handlePurchaseMembership(user);
            case "4" -> handleBrowseMerch();
            default -> System.out.println("Invalid option.");
        }
    }

    private void handleManageWorkoutClasses(User user) {
        while (true) {
            System.out.println();
            System.out.println("--- Manage Workout Classes ---");
            System.out.println("1. Create class");
            System.out.println("2. Update class");
            System.out.println("3. Delete class");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleCreateWorkoutClass(user);
                case "2" -> handleUpdateWorkoutClass(user);
                case "3" -> handleDeleteWorkoutClass(user);
                case "0" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void handleCreateWorkoutClass(User user) {
        try {
            System.out.print("Class type (e.g., HIIT, Yoga): ");
            String type = scanner.nextLine().trim();
            System.out.print("Description: ");
            String description = scanner.nextLine().trim();
            System.out.print("Duration in minutes: ");
            int duration = readInt(scanner.nextLine(), "Duration must be a valid whole number");
            System.out.print("Capacity: ");
            int capacity = readInt(scanner.nextLine(), "Capacity must be a valid whole number");
            System.out.println("Time slot: 1) 12Pm  2) 5Pm");
            System.out.print("Choose a time slot: ");
            String timeSlot = scanner.nextLine().trim();

            WorkoutClass workoutClass = new WorkoutClass();
            workoutClass.setWorkoutClassType(type);
            workoutClass.setWorkoutClassDescription(description);
            workoutClass.setTrainerId(user.getUserId());
            workoutClass.setDurationMinutes(duration);
            workoutClass.setCapacity(capacity);
            workoutClass.setClassDateTime(workoutClassService.getFixedTimeSlot(timeSlot));

            WorkoutClass created = workoutClassService.createClass(workoutClass);
            System.out.println("Workout class created. ID: " + created.getWorkoutClassId());
        } catch (RuntimeException exception) {
            System.out.println("Create class failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleUpdateWorkoutClass(User user) {
        try {
            System.out.print("Class ID to update: ");
            int classId = readInt(scanner.nextLine(), "Class ID must be a valid whole number");

            WorkoutClass existing = workoutClassService.getById(classId);
            if (existing == null) {
                System.out.println("Workout class not found.");
                return;
            }

            if (existing.getTrainerId() != user.getUserId()) {
                System.out.println("You can only update your own classes.");
                return;
            }

            System.out.print("New class type (current: " + existing.getWorkoutClassType() + "): ");
            String type = scanner.nextLine().trim();
            if (!type.isEmpty()) {
                existing.setWorkoutClassType(type);
            }

            System.out.print("New description (current: " + existing.getWorkoutClassDescription() + "): ");
            String description = scanner.nextLine().trim();
            if (!description.isEmpty()) {
                existing.setWorkoutClassDescription(description);
            }

            System.out.print("New duration in minutes (current: " + existing.getDurationMinutes() + "): ");
            String durationInput = scanner.nextLine().trim();
            if (!durationInput.isEmpty()) {
                existing.setDurationMinutes(readInt(durationInput, "Duration must be a valid whole number"));
            }

            System.out.print("New capacity (current: " + existing.getCapacity() + "): ");
            String capacityInput = scanner.nextLine().trim();
            if (!capacityInput.isEmpty()) {
                existing.setCapacity(readInt(capacityInput, "Capacity must be a valid whole number"));
            }

            System.out.print("New time slot (1=12Pm, 2=5Pm, Enter=keep current): ");
            String timeSlot = scanner.nextLine().trim();
            if (!timeSlot.isEmpty()) {
                existing.setClassDateTime(workoutClassService.getFixedTimeSlot(timeSlot));
            }

            boolean updated = workoutClassService.updateClass(existing);
            System.out.println(updated ? "Workout class updated." : "No class was updated.");
        } catch (RuntimeException exception) {
            System.out.println("Update class failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleDeleteWorkoutClass(User user) {
        try {
            System.out.print("Class ID to delete: ");
            int classId = readInt(scanner.nextLine(), "Class ID must be a valid whole number");

            System.out.println("1. Confirm delete");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");
            String confirm = scanner.nextLine().trim();
            if ("0".equals(confirm)) {
                System.out.println("Delete cancelled.");
                return;
            }
            if (!"1".equals(confirm)) {
                System.out.println("Invalid option.");
                return;
            }

            boolean deleted = workoutClassService.deleteClass(classId, user.getUserId());
            System.out.println(deleted ? "Workout class deleted." : "No class was deleted.");
        } catch (RuntimeException exception) {
            System.out.println("Delete class failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleViewAssignedClasses(User user) {
        try {
            List<WorkoutClass> classes = workoutClassService.getAssignedClasses(user.getUserId());
            System.out.println();
            System.out.println("--- Your Assigned Classes ---");
            if (classes.isEmpty()) {
                System.out.println("No classes assigned yet.");
                return;
            }

            for (WorkoutClass workoutClass : classes) {
                String classTime = getClassTimeLabel(workoutClass);
                System.out.println("ID: " + workoutClass.getWorkoutClassId());
                System.out.println("Type: " + workoutClass.getWorkoutClassType());
                System.out.println("Description: " + workoutClass.getWorkoutClassDescription());
                System.out.println("Time: " + classTime);
                System.out.println("Duration: " + workoutClass.getDurationMinutes() + " min");
                System.out.println("Capacity: " + workoutClass.getCapacity());
                System.out.println();
            }
        } catch (RuntimeException exception) {
            System.out.println("Unable to load assigned classes: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleMemberOption(String choice, User user) {
        switch (choice) {
            case "1" -> handleBrowseWorkoutClasses();
            case "2" -> handlePurchaseMembership(user);
            case "3" -> handleBrowseMerch();
            default -> System.out.println("Invalid option.");
        }
    }

    private void handleBrowseMerch() {
        try {
            List<GymMerch> merchItems = gymMerchService.browseMerch();
            System.out.println();
            System.out.println("--- Available Gym Merch ---");

            if (merchItems.isEmpty()) {
                System.out.println("No merch items available right now.");
                return;
            }

            for (GymMerch merch : merchItems) {
                System.out.println("ID: " + merch.getMerchId());
                System.out.println("Name: " + merch.getMerchName());
                System.out.println("Type: " + merch.getMerchType());
                System.out.println("Price: $" + String.format("%.2f", merch.getMerchPrice()));
                System.out.println("In Stock: " + merch.getQuantityInStock());
                System.out.println();
            }
        } catch (RuntimeException exception) {
            System.out.println("Unable to browse merch: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private void handleBrowseWorkoutClasses() {
        try {
            List<WorkoutClass> classes = workoutClassService.browseClasses();
            System.out.println();
            System.out.println("--- Available Workout Classes ---");

            if (classes.isEmpty()) {
                System.out.println("No workout classes available right now.");
                return;
            }

            for (WorkoutClass workoutClass : classes) {
                String classTime = getClassTimeLabel(workoutClass);

                System.out.println("ID: " + workoutClass.getWorkoutClassId());
                System.out.println("Type: " + workoutClass.getWorkoutClassType());
                System.out.println("Description: " + workoutClass.getWorkoutClassDescription());
                System.out.println("Trainer ID: " + workoutClass.getTrainerId());
                System.out.println("Time: " + classTime);
                System.out.println("Duration: " + workoutClass.getDurationMinutes() + " min");
                System.out.println("Capacity: " + workoutClass.getCapacity());
                System.out.println();
            }
        } catch (RuntimeException exception) {
            System.out.println("Unable to browse workout classes: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private String getClassTimeLabel(WorkoutClass workoutClass) {
        if ("HIIT".equalsIgnoreCase(workoutClass.getWorkoutClassType())) {
            return "12pm Daily";
        }

        if ("Yoga".equalsIgnoreCase(workoutClass.getWorkoutClassType())) {
            return "5pm Daily";
        }

        return "TBD";
    }

    private void handlePurchaseMembership(User user) {
        try {
            System.out.println();
            System.out.println("--- Purchase Membership ---");
            System.out.println("1. Basic - $15.00 (24/7 gym access)");
            System.out.println("2. Premium - $35.00 (24/7 + classes)");
            System.out.println("3. VIP - $50.00 (24/7 + personal training + extras)");
            System.out.println("0. Back");
            System.out.print("Choose a membership tier: ");
            String tierChoice = scanner.nextLine().trim();

            if ("0".equals(tierChoice)) {
                System.out.println("Purchase cancelled.");
                return;
            }

            String type;
            String description;
            double cost;

            switch (tierChoice) {
                case "1" -> {
                    type = "Basic";
                    description = "24/7 gym access";
                    cost = 15.00;
                }
                case "2" -> {
                    type = "Premium";
                    description = "24/7 + classes";
                    cost = 35.00;
                }
                case "3" -> {
                    type = "VIP";
                    description = "24/7 + personal training + extras";
                    cost = 50.00;
                }
                default -> {
                    System.out.println("Invalid tier option.");
                    return;
                }
            }

            System.out.println("Selected: " + type + " - $" + String.format("%.2f", cost));
            while (true) {
                System.out.println("1. Confirm purchase");
                System.out.println("0. Back");
                System.out.print("Choose an option: ");
                String confirmChoice = scanner.nextLine().trim();

                if ("1".equals(confirmChoice)) {
                    break;
                }

                if ("0".equals(confirmChoice)) {
                    System.out.println("Purchase cancelled.");
                    return;
                }

                System.out.println("Invalid option.");
            }

            Membership membership = new Membership();
            membership.setMembershipType(type);
            membership.setMembershipDescription(description);
            membership.setMembershipCost(cost);
            membership.setMemberId(user.getUserId());

            Membership created = membershipService.purchaseMembership(membership);
            System.out.println("Membership purchased. ID: " + created.getMembershipId());
        } catch (RuntimeException exception) {
            System.out.println("Membership purchase failed: " + exception.getMessage());
            if (exception.getCause() != null) {
                System.out.println("Cause: " + exception.getCause().getMessage());
            }
        }
    }

    private int readInt(String value, String errorMessage) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private double readDouble(String value, String errorMessage) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
