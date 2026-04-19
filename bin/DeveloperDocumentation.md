# Gym Management System Developer Guide

This guide is for developers joining the project.

## Architecture Overview
The project uses a layered design.

- Console UI layer: `ConsoleApp`
- Service layer: `UserService`, `MembershipService`, `WorkoutClassService`, `GymMerchService`
- DAO layer: `UserDAO`, `MembershipDAO`, `WorkoutClassDAO`, `GymMerchDAO`
- Database layer: PostgreSQL tables defined in `create_schema.sql`

Flow:
- Console UI collects input
- Service layer applies validation and business rules
- DAO layer executes SQL via JDBC
- Data is persisted in PostgreSQL

## Class Design

### Key classes and responsibilities
- `User`: Base user model
- `Admin`, `Trainer`, `Member`: Role-specific user models extending `User`
- `Membership`, `WorkoutClass`, `GymMerch`: Domain entities
- `DbConnection`: Database connection utility
- `AppLogger`: Logging configuration for file output

### Class Diagram
```mermaid
classDiagram
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
    class DbConnection
    class AppLogger

    class UserService
    class MembershipService
    class WorkoutClassService
    class GymMerchService

    class UserDAO
    class MembershipDAO
    class WorkoutClassDAO
    class GymMerchDAO

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
```

## Database Design

### Tables and purpose
- `users`: Login/profile data and role
- `memberships`: Membership purchases tied to users
- `workout_classes`: Trainer-owned classes
- `gym_merch`: Merch inventory and pricing

### Relationships
- `memberships.member_id` -> `users.user_id`
- `workout_classes.trainer_id` -> `users.user_id`

### ERD Diagram
```mermaid
erDiagram
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
```

## Setup Instructions

### 1) Clone
```bash
git clone https://github.com/PureBoost/Java-Final
cd "Java Final"
```

### 2) Configure PostgreSQL
1. Create a database named `qap4`.
2. Run scripts:
```sql
CREATE DATABASE qap4;
\c qap4
\i create_schema.sql
\i seed_data.sql
```
3. Confirm credentials in `DbConnection.java`.

## Dependencies
- Java JDK
- PostgreSQL
- JDBC driver: `postgresql-42.7.10.jar`
- BCrypt: `jbcrypt-0.4.jar`

Referenced in `.vscode/settings.json`.

## Build and Run
```bash
javac -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" *.java
java -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" ConsoleApp
```

## Logging
- Configured in `AppLogger.java`
- Output file: `GymApp.log`
- Logs include startup, authentication events, key actions, and failures

Why logging is used:
- Faster troubleshooting
- Basic activity history for debugging and demos
