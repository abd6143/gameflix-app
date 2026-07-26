# Architecture — GameFlix

## Overview

GameFlix follows a classic **three-tier architecture** built on Spring Boot, separating concerns across presentation, application, and data layers.

---

## Layer Description

### Presentation Layer
- **Thymeleaf Templates:** Server-rendered HTML pages with Tailwind CSS for the dark gaming UI
- **REST API:** JSON endpoints consumed by Postman, future SPA clients, and inline JavaScript
- **Spring Security:** Dual filter chains — stateless JWT for `/api/**`, session + CSRF for web routes

### Application Layer
- **Controllers:** Handle HTTP routing, validation, and response wrapping (`ApiResponse<T>`)
- **Services:** Business logic with `@Transactional` write operations and structured logging
- **Security:** JWT generation/validation, rate limiting, token blocklist, role checks via `@PreAuthorize`

### Data Layer
- **JPA/Hibernate:** Entity mapping for User, Game, Subscription, SubscriptionPlan
- **Repositories:** Spring Data JPA interfaces with custom query methods
- **Database:** PostgreSQL (production/Docker) or H2 (local dev/test)

---

## ASCII Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENTS                                     │
│   Browser (Thymeleaf)  │  Postman  │  Future SPA (localhost:3000)   │
└────────────┬───────────────────────────────┬────────────────────────┘
             │ HTTP                          │ HTTP + JWT
             ▼                               ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐ │
│  │ WebController│  │ REST         │  │ Security Filters         │ │
│  │ (Thymeleaf)  │  │ Controllers  │  │ JwtAuth / RateLimit / CSRF│ │
│  └──────┬───────┘  └──────┬───────┘  └────────────┬─────────────┘ │
└─────────┼─────────────────┼───────────────────────┼───────────────┘
          │                   │                       │
          ▼                   ▼                       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    APPLICATION LAYER                                │
│  ┌────────────┐ ┌────────────┐ ┌──────────────┐ ┌───────────────┐ │
│  │ AuthService│ │ GameService│ │ Subscription │ │ AdminService  │ │
│  │            │ │            │ │ Service      │ │               │ │
│  └─────┬──────┘ └─────┬──────┘ └──────┬───────┘ └───────┬───────┘ │
│        │              │               │                 │         │
│  ┌─────┴──────────────┴───────────────┴─────────────────┴───────┐ │
│  │              EntityMapper (DTO ↔ Entity)                     │ │
│  └──────────────────────────┬───────────────────────────────────┘ │
└─────────────────────────────┼─────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       DATA LAYER                                    │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────────────────┐    │
│  │ UserRepo     │ │ GameRepo     │ │ SubscriptionRepo / Plan  │    │
│  └──────┬───────┘ └──────┬───────┘ └────────────┬─────────────┘    │
│         └────────────────┼──────────────────────┘                   │
│                          ▼                                          │
│              ┌───────────────────────┐                              │
│              │  PostgreSQL / H2      │                              │
│              └───────────────────────┘                              │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Logical Microservices (Future Decomposition)

If GameFlix were split into microservices, three natural boundaries emerge:

### 1. Auth Service
**Responsibility:** User identity, registration, login, JWT issuance, password management

| API Boundary | Endpoints |
|-------------|-----------|
| Public | `POST /auth/register`, `POST /auth/login`, `POST /auth/refresh`, `POST /auth/logout` |
| Internal | User credential validation, token blocklist |

**Owns:** `users` table (credentials + role)

### 2. Catalog Service
**Responsibility:** Game metadata, search, filtering, admin CRUD

| API Boundary | Endpoints |
|-------------|-----------|
| Public | `GET /games`, `GET /games/{id}`, `GET /games/search` |
| Admin | `POST/PUT/DELETE /games` |

**Owns:** `games`, `game_platforms` tables

### 3. Subscription Service
**Responsibility:** Plans, billing state, subscription lifecycle

| API Boundary | Endpoints |
|-------------|-----------|
| Public | `GET /plans` |
| Authenticated | `POST /subscribe`, `PUT /my/pause`, `PUT /my/cancel`, `PUT /my/upgrade` |
| Admin | `GET /subscriptions`, revenue stats |

**Owns:** `subscription_plans`, `subscriptions` tables

**Inter-service communication** would use JWT claims for user identity and synchronous REST or async events (e.g., "subscription.created") for cross-service updates.

---

## Deployment Architecture (Docker)

```
┌──────────── docker-compose ────────────┐
│                                        │
│  ┌─────────┐         ┌─────────────┐  │
│  │  app    │────────▶│  PostgreSQL │  │
│  │ :8080   │  JDBC   │  :5432      │  │
│  └─────────┘         └─────────────┘  │
│       │                                │
│  env: JWT_SECRET, SPRING_PROFILES      │
└────────────────────────────────────────┘
```

---

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| Stateless JWT for API | Enables horizontal scaling and Postman/SPA testing |
| Session auth for Thymeleaf | Simpler form login UX with CSRF protection |
| Manual DTO mapping | No Lombok/MapStruct magic — auditable for university demo |
| H2 for dev/test | Zero-config local development; PostgreSQL for production parity |
| In-memory token blocklist | Simple logout without Redis dependency for prototype |
