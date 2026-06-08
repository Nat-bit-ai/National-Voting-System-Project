# National Digital Voting System

Desktop Java Swing application for an Ethiopian national digital voting workflow with Fayda-based identity verification and PostgreSQL persistence.

## Features

- Swing login, voter registration, voter dashboard, and admin dashboard
- PostgreSQL schemas for `citizens` and `voting_db`
- Fayda QR lookup flow through `citizens.fayda_citizens` and `citizens.civil_registry`
- BCrypt password hashing
- Role-based admin access
- Admin CRUD for elections, parties, candidates, and officers
- One-person-one-vote validation through application services and PostgreSQL trigger
- Audit logging, result monitoring chart, and PDF export support
- English/Amharic language resource files
- OOP design using abstraction, inheritance, polymorphism, and encapsulation

## Run

1. Create both databases in PostgreSQL.
2. Execute `src/main/resources/database/citizens.sql` against the `citizens` database.
3. Execute `src/main/resources/database/voting_db.sql` against the `voting_db` database.
4. Configure database settings using environment variables if needed:
   - `VOTING_DB_URL`
   - `CITIZENS_DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
5. Start the app:

```bash
mvn clean compile exec:java
```

Default local connection values are in `config.DatabaseConfig`.
