-- Create Profile table
CREATE TABLE profile (
    profile_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    user_uuid VARCHAR(255) UNIQUE NOT NULL
);

-- Create HallMap table
CREATE TABLE hall_map (
    hall_map_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    num_rows INTEGER NOT NULL,
    num_columns INTEGER NOT NULL
);

-- Create Event table
CREATE TABLE event (
    event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    date VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    hall_id BIGINT NOT NULL,
    FOREIGN KEY (hall_id) REFERENCES hall_map(hall_map_id)
);

CREATE TABLE `profile_event_favorite` (
      `event_id` bigint(20) NOT NULL,
      `user_uuid` varchar(255) NOT NULL,
      PRIMARY KEY (`event_id`, `user_uuid`),
      CONSTRAINT fk_profile_event FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`)
);

CREATE TABLE ticket (
    ticket_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL,
    event BIGINT,
    CONSTRAINT fk_ticket_event FOREIGN KEY (event) REFERENCES event (event_id)
);

CREATE TABLE purchased_tickets (
   purchase_id BIGINT PRIMARY KEY AUTO_INCREMENT,
   ticket_id BIGINT,
   user_uuid VARCHAR(255),
   quantity INT NOT NULL,
   FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id),
   FOREIGN KEY (user_uuid) REFERENCES profile(user_uuid)
);

-- Insert data into Profile table
INSERT INTO profile (first_name, last_name, phone_number, user_uuid) VALUES
    ('John', 'Doe', '123456789', 'uuid1'),
    ('Jane', 'Doe', '987654321', 'uuid2');

-- Insert data into HallMap table
INSERT INTO hall_map (num_rows, num_columns) VALUES
    (10, 5),
    (8, 6);

-- Insert data into Event table
INSERT INTO event (name, description, city, location, type, date, image, hall_id) VALUES
    ('Event 1', 'Description 1', 'Iasi', 'Location 1', 'Exhibition', '2023-01-01', 'https://images.unsplash.com/photo-1605429536388-d43598b2de59?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',  1),
    ('Event 2', 'Description 2', 'Cluj', 'Location 2', 'Concert', '2023-02-01', 'https://images.unsplash.com/photo-1495001258031-d1b407bc1776?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 2);

INSERT INTO ticket (name, price, quantity, event)
VALUES
    ('Concert Ticket', 49.99, 2, 1),
    ('Movie Ticket', 12.50, 1, 2);

# INSERT INTO purchased_tickets (ticket_id, user_uuid, quantity) VALUES (2, '358dbd97-9c25-41bc-9732-862cbe6e1caa', 3);
# INSERT INTO purchased_tickets (ticket_id, user_uuid, quantity) VALUES (1, '9b32bdea-755f-4cb1-8b77-47d28cf189bd', 4);
