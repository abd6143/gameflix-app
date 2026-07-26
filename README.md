# GameFlix

Video game subscription management system — Netflix for games.

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.9+

### Run locally (H2)
```bash
mvn spring-boot:run
```

Open http://localhost:8080

### Seed credentials
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@gameflix.com | Admin@123 |
| User | jane@gameflix.com | User@123 |
| User | mike@gameflix.com | User@123 |

### API docs
http://localhost:8080/swagger-ui.html

### Run tests
```bash
mvn verify
```

### Docker
```bash
docker-compose up --build
```

## Project structure
See `docs/Architecture.md` for system design and `docs/SRS.md` for requirements.

## Postman
Import `postman/GameFlix.postman_collection.json` — run **Admin Login** and **Login** first to capture tokens.
