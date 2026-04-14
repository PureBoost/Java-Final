# Gym Management Console App

Java console application for gym operations, including user login by role, memberships, workout classes, and merchandise.

## 1. Product and User Documentation

### System Overview
This system helps a gym run day-to-day operations from a simple text menu.

It allows users to:
- Register and log in
- Access features based on role (Admin, Trainer, Member)
- Manage memberships
- Manage and browse workout classes
- Manage and browse gym merchandise

The main purpose is to keep core gym information in one place using a PostgreSQL database.

### User Roles

#### Admin
- View all users and contact information
- Delete users
- View all memberships and total annual membership revenue
- Manage merchandise inventory: add, update, delete, print stock list, and view total stock value

#### Trainer
- Create, update, and delete their workout classes
- View their assigned classes
- Purchase a membership
- View gym merchandise

#### Member
- Browse workout classes
- Purchase a membership
- View gym merchandise
- View total membership expenses

### Common Workflows

#### Purchase a Membership (Member or Trainer)
1. Start the app and log in.
2. Open Purchase Membership.
3. Choose a tier (Basic, Premium, or VIP).
4. Confirm purchase.
5. The system saves the membership and shows the new membership ID.

#### Create or View Workout Classes
Create class (Trainer):
1. Log in as Trainer.
2. Open Manage Workout Classes.
3. Choose Create class.
4. Enter class type, description, duration, capacity, and time slot.
5. Save the class.

View classes (Member):
1. Log in as Member.
2. Choose Browse workout classes.
3. Review available classes listed in the menu.

#### Manage Gym Merchandise (Admin)
1. Log in as Admin.
2. Open Manage merch.
3. Choose an action:
- Add merch item
- Update merch price or stock
- Delete merch item
- Print stock report
- View total stock value
4. Confirm action when prompted.

### System Limitations
- Console-only interface (no web or mobile app).
- No payment gateway integration; purchases are recorded in the database only.
- Members can browse merch but do not complete checkout in this version.
- Class attendance and booking are not tracked.

## 2. Technical and Developer Documentation

### Architecture Overview
The app follows a layered approach:

- Console UI layer:
	- `ConsoleApp` handles menus, input, and flow by role.
- Service layer:
	- Business rules and validation in `UserService`, `MembershipService`, `WorkoutClassService`, `GymMerchService`.
- DAO layer:
	- Database CRUD logic in `UserDAO`, `MembershipDAO`, `WorkoutClassDAO`, `GymMerchDAO`.
- Database layer:
	- PostgreSQL tables defined in `create_schema.sql` and sample data in `seed_data.sql`.

### Class Design

#### Key classes and responsibilities
- `User`: Base user model.
- `Admin`, `Trainer`, `Member`: Role-specific subclasses of `User`.
- `Membership`, `WorkoutClass`, `GymMerch`: Domain models.
- `ConsoleApp`: Entry point and role-based console navigation.
- `DbConnection`: JDBC connection utility.
- `AppLogger`: File logger configuration.

#### Class Diagram
		class User {
			+int userId
			+String username
			+String passwordHash
			+String email
			+String phoneNumber
			+String address
			+String role
		}

		class Admin
		class Trainer
		class Member

		User <|-- Admin
		User <|-- Trainer
		User <|-- Member

		class ConsoleApp
		class AppLogger
		class DbConnection

		class UserDAO
		class MembershipDAO
		class WorkoutClassDAO
		class GymMerchDAO

		class UserService
		class MembershipService
		class WorkoutClassService
		class GymMerchService

		class Membership
		class WorkoutClass
		class GymMerch

		ConsoleApp --> UserService
		ConsoleApp --> MembershipService
		ConsoleApp --> WorkoutClassService
		ConsoleApp --> GymMerchService

		UserService --> UserDAO
		MembershipService --> MembershipDAO
		WorkoutClassService --> WorkoutClassDAO
		GymMerchService --> GymMerchDAO

		UserDAO --> DbConnection
		MembershipDAO --> DbConnection
		WorkoutClassDAO --> DbConnection
		GymMerchDAO --> DbConnection

		ConsoleApp --> AppLogger
		UserService --> AppLogger
		UserDAO --> AppLogger

### Database Design

#### Tables and purpose
- `users`: Stores login and profile data for Admin, Trainer, and Member roles.
- `memberships`: Stores membership purchases and dates tied to a user.
- `workout_classes`: Stores trainer-created classes.
- `gym_merch`: Stores merchandise inventory and pricing.

#### Relationships
- `memberships.member_id` references `users.user_id`.
- `workout_classes.trainer_id` references `users.user_id`.

#### ERD Diagram
	USERS {
		int user_id PK
		string username
		string password_hash
		string email
		string phone_number
		string address
		string role
		datetime created_at
	}

	MEMBERSHIPS {
		int membership_id PK
		string membership_type
		string membership_description
		decimal membership_cost
		int member_id FK
		date start_date
		date end_date
		datetime created_at
	}

	WORKOUT_CLASSES {
		int workout_class_id PK
		string workout_class_type
		string workout_class_description
		int trainer_id FK
		datetime class_datetime
		int duration_minutes
		int capacity
	}

	GYM_MERCH {
		int merch_id PK
		string merch_name
		string merch_type
		decimal merch_price
		int quantity_in_stock
	}

	USERS ||--o{ MEMBERSHIPS : purchases
	USERS ||--o{ WORKOUT_CLASSES : teaches

### Setup Instructions

#### 1) Clone
```bash
git clone https://github.com/PureBoost/Java-Final
cd "Java Final"
```

#### 2) Configure database
1. Create PostgreSQL database named `qap4`.
2. Run schema and seed scripts:
```sql
CREATE DATABASE qap4;
\c qap4
\i create_schema.sql
\i seed_data.sql
```

3. Confirm credentials in `DbConnection.java`:
- URL: `jdbc:postgresql://localhost:5432/qap4`
- User: `postgres`
- Password: `1234` (change to your password)

#### 3) Run locally
From VS Code:
- Open folder and run `ConsoleApp.java`.

### Dependencies
- Java JDK
- PostgreSQL server
- PostgreSQL JDBC driver: `postgresql-42.7.10.jar`
- BCrypt library: `jbcrypt-0.4.jar`

### Build and Run from Terminal
```bash
javac -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" *.java
java -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" ConsoleApp
```

### Logging
- Logger config is in `AppLogger.java`.
- Logs are written to `GymApp.log` in project root.
- Logging is used to track:
- startup and shutdown events
- successful and failed login and registration
- key actions (membership purchases, class changes, merch changes)
- important failures and exceptions

Why logging is used:
- Helps diagnose issues quickly.
- Provides a basic activity history for troubleshooting and demo evidence.
