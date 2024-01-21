CREATE TABLE IF NOT EXISTS user (
    user_uuid VARCHAR(255) PRIMARY KEY,
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'user') NOT NULL,
    confirmation_token VARCHAR(255) DEFAULT '',
    is_confirmed BOOLEAN DEFAULT FALSE,
    is_notifications_enabled BOOLEAN DEFAULT FALSE
);

-- Insert data with new fields
INSERT INTO user (user_uuid, email, password, role, confirmation_token, is_confirmed, is_notifications_enabled)
VALUES
    ('227a90f4-43de-47c7-8951-b8d9cac4fea0', 'usertest@gmail.com', '$2a$10$oXpFAFGAzOJlO6acAQTQ2ei0gQcFifZYiUhGf05zzC0N4K52kbI4C', 'user', 'IwV-e8N1jVhgw9XpAUsvzCKUHKMK9gRsGwFjrA9PZuM', true, true)
