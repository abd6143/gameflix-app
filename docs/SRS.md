# Software Requirements Specification (SRS)
# GameFlix — Video Game Subscription Management System

## 1. Introduction

### 1.1 Purpose
This document specifies the functional and non-functional requirements for **GameFlix**, a web-based platform that allows users to browse, subscribe to, and access a catalog of video games through tiered subscription plans. It serves as the authoritative reference for developers, testers, and stakeholders throughout the university course project lifecycle.

### 1.2 Scope
GameFlix provides:
- User registration and JWT-based authentication
- A searchable, filterable game catalog
- Subscription plan management (subscribe, pause, cancel, upgrade)
- An admin portal for user and catalog management
- A Thymeleaf web UI and REST API for all core operations

The system is designed for demonstration and academic evaluation, deployable locally via H2 or in production via PostgreSQL and Docker.

### 1.3 Definitions
| Term | Definition |
|------|------------|
| Subscriber | A registered user with an active or paused subscription |
| Plan Tier | BASIC, STANDARD, or PREMIUM access level |
| JWT | JSON Web Token used for stateless API authentication |
| Catalog | The collection of games available on the platform |

---

## 2. Overall Description

### 2.1 Product Perspective
GameFlix is a standalone three-tier web application:
- **Presentation:** Thymeleaf templates + REST API consumers
- **Application:** Spring Boot services and controllers
- **Data:** PostgreSQL (production) / H2 (development)

### 2.2 User Classes
| Class | Description |
|-------|-------------|
| **Guest** | Unauthenticated visitor; can view landing page, catalog, and plans |
| **Subscriber** | Registered user; can manage profile, subscription, and play eligible games |
| **Admin** | Platform operator; full CRUD on games and users, access to analytics |

### 2.3 Operating Environment
- Java 17, Spring Boot 3.x
- Modern browsers (Chrome, Firefox, Safari, Edge)
- PostgreSQL 15 or H2 in-memory database

---

## 3. Functional Requirements

### Module 1 — Authentication & Authorization
- **FR-1.1** The system shall allow users to register with username, email, and password.
- **FR-1.2** Passwords shall be hashed using BCrypt (strength 12).
- **FR-1.3** The system shall issue JWT access tokens (15 min) and refresh tokens (7 days) on login/register.
- **FR-1.4** The system shall support token logout via an in-memory blocklist.
- **FR-1.5** Role-based access control shall restrict admin endpoints to ADMIN role.
- **FR-1.6** Auth endpoints shall be rate-limited to 10 requests/minute per IP.

### Module 2 — Game Catalog
- **FR-2.1** The system shall display a paginated list of games.
- **FR-2.2** Users shall filter games by genre, platform, and plan tier.
- **FR-2.3** Users shall search games by title or developer.
- **FR-2.4** Admins shall create, update, and delete games via the REST API.
- **FR-2.5** Each game shall display metadata: title, genre, rating, platforms, and required plan tier.

### Module 3 — Subscription Management
- **FR-3.1** The system shall offer three plans: Basic ($4.99), Standard ($9.99), Premium ($14.99).
- **FR-3.2** Subscribers shall subscribe to a plan and receive ACTIVE status.
- **FR-3.3** Subscribers shall pause, cancel, reactivate, and upgrade/downgrade plans.
- **FR-3.4** The system shall prevent duplicate active subscriptions per user.

### Module 4 — User Profile
- **FR-4.1** Authenticated users shall view and update their profile.
- **FR-4.2** Users shall change their password with current-password verification.
- **FR-4.3** Users shall delete their own account.

### Module 5 — Admin Portal
- **FR-5.1** Admins shall list all users with subscription status.
- **FR-5.2** Admins shall change user roles and delete users.
- **FR-5.3** Admins shall view dashboard statistics (users, revenue, games).
- **FR-5.4** Admins shall manage the game catalog via web UI and API.

### Module 6 — Web Frontend
- **FR-6.1** The UI shall use a dark gaming theme with Tailwind CSS.
- **FR-6.2** All specified pages (landing, auth, dashboard, catalog, admin) shall be implemented.
- **FR-6.3** CSRF protection shall be enabled for Thymeleaf form routes.

---

## 4. Non-Functional Requirements

### 4.1 Performance
- API list endpoints shall respond within 500 ms under normal load (≤100 concurrent users).
- Pagination shall default to 20 items per page.

### 4.2 Security
- All secrets shall be supplied via environment variables.
- API responses shall not expose entity passwords or internal IDs unnecessarily.
- CORS shall be restricted to `localhost:3000` and `localhost:8080`.

### 4.3 Scalability
- Stateless JWT auth enables horizontal scaling of application instances.
- Database connection pooling via HikariCP (Spring Boot default).

### 4.4 Usability
- UI shall be responsive for desktop and tablet viewports.
- Error messages shall be human-readable on both web and API layers.

### 4.5 Maintainability
- Code shall follow layered architecture (controller → service → repository).
- DTOs shall be used for all API input/output; no direct entity serialization.

---

## 5. External Interface Requirements

### 5.1 REST API
All endpoints documented at `/swagger-ui.html`. Standard response envelope:
```json
{ "success": true, "data": {}, "message": "OK", "timestamp": "..." }
```

### 5.2 Database
- PostgreSQL schema managed via JPA `ddl-auto=update`
- Seed data loaded from `data.sql` on startup

### 5.3 Third-Party Services
- Tailwind CSS CDN for styling
- Chart.js for admin dashboard charts
- Picsum Photos for placeholder game cover images

---

## 6. Constraints and Assumptions

### Constraints
- No Lombok; explicit getters/setters required for code auditability.
- Maven build system; Java 17 minimum.
- University project scope — payment processing is mocked.

### Assumptions
- Users have modern browsers with JavaScript enabled.
- Email uniqueness is sufficient for account identification at login.
- A single subscription per user is sufficient for the prototype.
- Game "Play Now" is a UI affordance; actual game streaming is out of scope.
