# Gym Management Console App

This project documentation is separated by audience.

## Documentation

- Product and User Documentation: [USER_GUIDE.md](USER_GUIDE.md)
- Technical and Developer Documentation: [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md)

## Quick Start

1. Set up PostgreSQL using `create_schema.sql` and `seed_data.sql`.
2. Confirm database credentials in `DbConnection.java`.
3. In PowerShell, run: `javac -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" *.java; java -cp ".;postgresql-42.7.10.jar;jbcrypt-0.4.jar" ConsoleApp`
