-- Subscription Plans
INSERT INTO subscription_plans (name, price, max_devices, download_allowed, description)
SELECT 'Basic', 4.99, 1, false, 'Stream on 1 device. Access to Basic-tier games.'
WHERE NOT EXISTS (SELECT 1 FROM subscription_plans WHERE name = 'Basic');

INSERT INTO subscription_plans (name, price, max_devices, download_allowed, description)
SELECT 'Standard', 9.99, 2, false, 'Stream on 2 devices. Access to Standard-tier games.'
WHERE NOT EXISTS (SELECT 1 FROM subscription_plans WHERE name = 'Standard');

INSERT INTO subscription_plans (name, price, max_devices, download_allowed, description)
SELECT 'Premium', 14.99, 4, true, 'Stream on 4 devices. Full catalog with offline downloads.'
WHERE NOT EXISTS (SELECT 1 FROM subscription_plans WHERE name = 'Premium');

-- Admin User (password: Admin@123)
INSERT INTO users (username, email, password, role, created_at, updated_at)
SELECT 'admin', 'admin@gameflix.com', '$2b$12$vmb4MDLqOQypdVdPpjVvA.D4zGghokvEafqw1TfOlOKGPS4MRIG3W', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@gameflix.com');

-- Regular Users (password: User@123)
INSERT INTO users (username, email, password, role, created_at, updated_at)
SELECT 'gamer_jane', 'jane@gameflix.com', '$2b$12$MzeUYY06wFn5.R4Y6XAyrOoSxNIQfw9fZeGoEL/WEgu1jKV0/1oAG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'jane@gameflix.com');

INSERT INTO users (username, email, password, role, created_at, updated_at)
SELECT 'gamer_mike', 'mike@gameflix.com', '$2b$12$MzeUYY06wFn5.R4Y6XAyrOoSxNIQfw9fZeGoEL/WEgu1jKV0/1oAG', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'mike@gameflix.com');

-- Subscriptions
INSERT INTO subscriptions (user_id, plan_id, status, start_date, end_date, auto_renew, created_at, updated_at)
SELECT u.id, p.id, 'ACTIVE', CURRENT_DATE, DATE '2026-12-31', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u, subscription_plans p
WHERE u.email = 'jane@gameflix.com' AND p.name = 'Standard'
AND NOT EXISTS (SELECT 1 FROM subscriptions WHERE user_id = u.id);

INSERT INTO subscriptions (user_id, plan_id, status, start_date, end_date, auto_renew, created_at, updated_at)
SELECT u.id, p.id, 'ACTIVE', CURRENT_DATE, DATE '2026-12-31', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM users u, subscription_plans p
WHERE u.email = 'mike@gameflix.com' AND p.name = 'Premium'
AND NOT EXISTS (SELECT 1 FROM subscriptions WHERE user_id = u.id);

-- Games
INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Cyber Nexus', 'Action', 'A neon-soaked open-world action RPG set in a sprawling megacity where corporations rule and hackers fight back.', 'Neon Forge Studios', 'GameFlix Originals', 2024, 9.2, 'https://picsum.photos/seed/cybernexus/400/600', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Cyber Nexus');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Dragonfall Chronicles', 'RPG', 'Embark on an epic fantasy journey through kingdoms torn by ancient dragon wars.', 'Mythic Realm Games', 'Epic Interactive', 2023, 8.8, 'https://picsum.photos/seed/dragonfall/400/600', 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Dragonfall Chronicles');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Stellar Command', 'Strategy', 'Build your galactic empire, forge alliances, and conquer star systems in this deep 4X strategy game.', 'Orbital Minds', 'Strategy Plus', 2022, 8.5, 'https://picsum.photos/seed/stellar/400/600', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Stellar Command');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Pro League Soccer 26', 'Sports', 'The most realistic soccer simulation with licensed teams, dynamic weather, and career mode.', 'SportsTech Labs', 'Global Sports Inc', 2025, 8.1, 'https://picsum.photos/seed/soccer/400/600', 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Pro League Soccer 26');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Shadow Ops: Recon', 'FPS', 'Tactical first-person shooter featuring squad-based missions and destructible environments.', 'Tactical Edge', 'Warfront Publishing', 2024, 8.9, 'https://picsum.photos/seed/shadowops/400/600', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Shadow Ops: Recon');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Pixel Quest', 'RPG', 'A charming retro-inspired RPG with turn-based combat and a heartwarming story.', 'Indie Pixel Co', 'IndieFlix', 2021, 7.8, 'https://picsum.photos/seed/pixelquest/400/600', 'BASIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Pixel Quest');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Mech Assault Zero', 'Action', 'Pilot towering mechs in fast-paced arena battles across ruined cityscapes.', 'Iron Circuit Games', 'Action Dynamics', 2023, 8.3, 'https://picsum.photos/seed/mechassault/400/600', 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Mech Assault Zero');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Kingdom Builder', 'Strategy', 'Design and manage a medieval kingdom through diplomacy, trade, and warfare.', 'Crown Studios', 'Grand Strategy Co', 2020, 8.0, 'https://picsum.photos/seed/kingdom/400/600', 'BASIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Kingdom Builder');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Velocity Racers', 'Sports', 'Arcade-style racing with customizable vehicles and online multiplayer championships.', 'Speed Forge', 'Racing World', 2024, 7.9, 'https://picsum.photos/seed/velocity/400/600', 'BASIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Velocity Racers');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Void Hunter', 'FPS', 'Survive hostile alien worlds in this roguelike FPS with procedural levels.', 'Dark Star Interactive', 'SciFi Games', 2023, 8.6, 'https://picsum.photos/seed/voidhunter/400/600', 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Void Hunter');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Legends of Aetheria', 'RPG', 'Explore a vast magical world with deep character customization and co-op dungeons.', 'Aether Studios', 'Fantasy Forge', 2022, 9.0, 'https://picsum.photos/seed/aetheria/400/600', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Legends of Aetheria');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Battle Grid Tactics', 'Strategy', 'Turn-based tactical combat on hex grids with permadeath and squad management.', 'Grid Masters', 'Tactics Unlimited', 2021, 8.2, 'https://picsum.photos/seed/battlegrid/400/600', 'STANDARD', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Battle Grid Tactics');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Street Hoops Pro', 'Sports', 'Fast-paced 3v3 street basketball with trick moves and online leagues.', 'Court Kings', 'Urban Sports', 2023, 7.5, 'https://picsum.photos/seed/hoops/400/600', 'BASIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Street Hoops Pro');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Phantom Strike', 'FPS', 'Stealth-action FPS where every decision shapes the narrative outcome.', 'Ghost Line Studios', 'Stealth Interactive', 2024, 8.7, 'https://picsum.photos/seed/phantom/400/600', 'PREMIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Phantom Strike');

INSERT INTO games (title, genre, description, developer, publisher, release_year, rating, cover_image_url, available_on_plan, created_at, updated_at)
SELECT 'Survival Island', 'Action', 'Craft, explore, and survive on a mysterious island with dynamic ecosystems.', 'Wild Frontier Games', 'Adventure Co', 2022, 7.7, 'https://picsum.photos/seed/survival/400/600', 'BASIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM games WHERE title = 'Survival Island');

-- Game Platforms (resolved by game title)
INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Cyber Nexus'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Cyber Nexus'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Cyber Nexus'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Dragonfall Chronicles'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Dragonfall Chronicles'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Stellar Command'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Pro League Soccer 26'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Pro League Soccer 26'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Pro League Soccer 26'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Nintendo Switch' FROM games g WHERE g.title = 'Pro League Soccer 26'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Nintendo Switch');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Shadow Ops: Recon'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Shadow Ops: Recon'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Pixel Quest'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Nintendo Switch' FROM games g WHERE g.title = 'Pixel Quest'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Nintendo Switch');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Mech Assault Zero'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Mech Assault Zero'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Kingdom Builder'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Velocity Racers'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Velocity Racers'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Void Hunter'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Void Hunter'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Legends of Aetheria'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Legends of Aetheria'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Legends of Aetheria'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Battle Grid Tactics'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Nintendo Switch' FROM games g WHERE g.title = 'Battle Grid Tactics'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Nintendo Switch');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Street Hoops Pro'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Xbox' FROM games g WHERE g.title = 'Street Hoops Pro'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Xbox');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Phantom Strike'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PlayStation' FROM games g WHERE g.title = 'Phantom Strike'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PlayStation');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'PC' FROM games g WHERE g.title = 'Survival Island'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'PC');

INSERT INTO game_platforms (game_id, platforms)
SELECT g.id, 'Nintendo Switch' FROM games g WHERE g.title = 'Survival Island'
AND NOT EXISTS (SELECT 1 FROM game_platforms gp WHERE gp.game_id = g.id AND gp.platforms = 'Nintendo Switch');
