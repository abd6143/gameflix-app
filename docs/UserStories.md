# User Stories — GameFlix

## Story 1: Guest Registration

**As a** guest,  
**I want to** register for a GameFlix account with my email and password,  
**So that** I can subscribe to a plan and access the game catalog.

### Acceptance Criteria
- [ ] Registration form accepts username (3–50 chars), valid email, and password (8+ chars)
- [ ] Duplicate email returns HTTP 409 with a clear error message
- [ ] Successful registration returns a JWT access token and refresh token
- [ ] Password is stored as a BCrypt hash, never in plain text
- [ ] User is redirected to the login page after web registration

---

## Story 2: Browse Game Catalog

**As a** subscriber,  
**I want to** browse and filter the game catalog by genre, platform, and plan tier,  
**So that** I can discover games available on my subscription.

### Acceptance Criteria
- [ ] Catalog displays games in a paginated grid with cover art, title, genre, and rating
- [ ] Sidebar filters for genre, platform, and plan tier update results
- [ ] Search bar finds games by title or developer name
- [ ] Clicking a game card navigates to the game detail page
- [ ] Pagination controls allow navigation between pages

---

## Story 3: Manage Subscription

**As a** subscriber,  
**I want to** view, pause, cancel, and upgrade my subscription,  
**So that** I can control my billing and access level.

### Acceptance Criteria
- [ ] Subscription page shows current plan name, price, status, and renewal date
- [ ] User can pause an ACTIVE subscription (status → PAUSED)
- [ ] User can cancel with a confirmation modal (status → CANCELLED)
- [ ] User can upgrade/downgrade to any available plan
- [ ] Attempting to subscribe when already ACTIVE returns an error
- [ ] Mock billing history table is displayed

---

## Story 4: Admin Game Management

**As an** admin,  
**I want to** add, edit, and remove games from the catalog,  
**So that** the platform content stays current and accurate.

### Acceptance Criteria
- [ ] Admin games page lists all games in a searchable data table
- [ ] Add-game modal form creates a game via POST `/api/games`
- [ ] Only users with ADMIN role can access admin pages and endpoints
- [ ] Non-admin requests to admin API return HTTP 403
- [ ] Game fields include title, genre, description, developer, publisher, rating, platforms, and plan tier

---

## Story 5: Search for Games

**As a** subscriber,  
**I want to** search for games by title or developer,  
**So that** I can quickly find specific games I want to play.

### Acceptance Criteria
- [ ] Search input on catalog page queries `/api/games/search?q=`
- [ ] Results update to show only matching games
- [ ] Search is case-insensitive
- [ ] Empty search returns the full paginated catalog
- [ ] Search works in combination with genre/platform/plan filters
