# Database Setup

## Files
- `create_schema.sql`: Creates all required tables.
- `seed_data.sql`: Inserts sample users, memberships, classes, and merch.

## Run Order
1. Create your database in PostgreSQL.
2. Run `create_schema.sql`.
3. Run `seed_data.sql`.

## Example (psql)
```sql
CREATE DATABASE gym_management;
\c gym_management
\i create_schema.sql
\i seed_data.sql
```

## Notes
- Roles used in the schema: `ADMIN`, `TRAINER`, `MEMBER`.
- Passwords are stored as BCrypt hashes.
- The seed script uses a sample hash for password `password`.
- Application events/errors are logged to `GymApp.log` in the project root.
