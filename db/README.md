# Database Setup

## Files
- `create_schema.sql`: Creates all required tables and indexes.
- `seed_data.sql`: Inserts sample users, memberships, classes, and merch.

## Run Order
1. Create your database in PostgreSQL.
2. Run `01_create_schema.sql`.
3. Run `02_seed_data.sql`.

## Example (psql)
```sql
CREATE DATABASE gym_management;
\c gym_management
\i db/01_create_schema.sql
\i db/02_seed_data.sql
```

## Notes
- Roles used in the schema: `ADMIN`, `TRAINER`, `MEMBER`.
- Passwords are stored as BCrypt hashes.
- The seed script uses a known sample hash for password `password`.
