-- Active: 1691178371776@@127.0.0.1@3306@airbnb

DROP DATABASE IF EXISTS airbnb;
Create DATABASE airbnb;
USE airbnb;

DROP TABLE IF EXISTS user, cc, listing, listing_amenities, bookings, availability;

CREATE TABLE user (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `email` VARCHAR(255) UNIQUE,
    `password` VARCHAR(255),
    `address` VARCHAR(255),
    `occupation` VARCHAR(255),
    `sin` VARCHAR(20),
    `dob` VARCHAR(10),
    `renter` BOOLEAN NOT NULL DEFAULT 0,
    `blocked` BOOLEAN NOT NULL DEFAULT 0
);

CREATE TABLE cc (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cc_num` VARCHAR(20) NOT NULL,
    `cc_name` VARCHAR(255) NOT NULL,
    `cc_exp` VARCHAR(10) NOT NULL,
    `cc_cvv` VARCHAR(5) NOT NULL,
    uid INT,
    FOREIGN KEY (uid) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE listing(
    id INT AUTO_INCREMENT PRIMARY KEY,
    host_id INT NOT NULL,
    type_of_listing ENUM('full house', 'apartment', 'room') NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    postal_code VARCHAR(10),
    city VARCHAR(100),
    country VARCHAR(100),
    listed BOOLEAN NOT NULL DEFAULT 1,
    FOREIGN KEY (host_id) REFERENCES user (id) ON DELETE CASCADE
);

CREATE TABLE listing_amenities (
    listing_id INT PRIMARY KEY,
    wifi BOOLEAN NOT NULL DEFAULT 0,
    washer BOOLEAN NOT NULL DEFAULT 0,
    air_conditioning BOOLEAN NOT NULL DEFAULT 0,
    dedicated_workspace BOOLEAN NOT NULL DEFAULT 0,
    hair_dryer BOOLEAN NOT NULL DEFAULT 0,
    kitchen BOOLEAN NOT NULL DEFAULT 0,
    dryer BOOLEAN NOT NULL DEFAULT 0,
    heating BOOLEAN NOT NULL DEFAULT 0,
    tv BOOLEAN NOT NULL DEFAULT 0,
    iron BOOLEAN NOT NULL DEFAULT 0,
    FOREIGN KEY (listing_id) REFERENCES listing (id) ON DELETE CASCADE
);

CREATE TABLE availability (
    listing_id INT NOT NULL,
    date DATE NOT NULL,
    price float NOT NULL DEFAULT 0,
    PRIMARY KEY (listing_id, date),
    FOREIGN KEY (listing_id) REFERENCES listing (id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    renter_id INT NOT NULL,
    start_date Date NOT NULL,
    finish_date Date NOT NULL,
    pricing DOUBLE NOT NULL DEFAULT 0,
    `renter_comment_listing` varchar(3000),
    `renter_comment_host` varchar(3000),
    `host_comment_renter` varchar(3000),
    `listing_rating` varchar(5),
    `host_rating` varchar(5),
    `renter_rating` varchar(5),
    status ENUM('host_cancelled', 'renter_cancelled', 'normal') 
        NOT NULL DEFAULT 'normal',
    FOREIGN KEY (listing_id) REFERENCES listing (id),
    FOREIGN KEY (renter_id) REFERENCES user (id)
);

INSERT INTO user (`name`, `email`, `password`, `address`, `occupation`, `sin`, `dob`, `renter`)
VALUES 
    ('Alice Smith', 'alice@example.com', 'password123', '456 Oak St', 'Graphic Designer', '987654321', '1985-05-15', true),
    ('Bob Johnson', 'bob@example.com', 'securepass', '789 Elm Ave', 'Sales Manager', '654321987', '1992-08-20', false),
    ('Charlie Brown', 'charlie@example.com', 'strongpass', '123 Maple Ln', 'Software Engineer', '123456789', '1990-11-10', true),
    ('Diana Miller', 'diana@example.com', 'secretword', '789 Willow Rd', 'Doctor', '456789123', '1988-03-25', false),
    ('Renter User', '1', '1', '123 Main St', 'Student', '111111111', '1998-01-10', true),
    ('Non-Renter User', '2', '2', '456 Elm St', 'Accountant', '222222222', '1980-09-05', false);

INSERT INTO cc (cc_num, cc_name, cc_exp, cc_cvv, uid)
VALUES
    ('9876543298765432', 'Alice Smith', '08/25', '123', 1),
    ('4567891245678912', 'Charlie Brown', '06/24', '456', 3),
    ('1111222233334444', 'Renter User', '11/23', '789', 5);

INSERT INTO listing (host_id, type_of_listing, latitude, longitude, postal_code, city, country)
VALUES
    (2, 'apartment', 37.7749, -122.4194, '94105', 'San Francisco', 'United States'),
    (2, 'room', 40.7128, -74.0060, '10001', 'New York', 'United States'),
    (2, 'full house', 34.0522, -118.2437, '90001', 'Los Angeles', 'United States'),
    (2, 'apartment', 51.5074, -0.1278, 'SW1A 1AA', 'London', 'United Kingdom'),

    (4, 'apartment', 40.7128, -74.0060, '10001', 'New York', 'United States'),
    (4, 'room', 34.0522, -118.2437, '90001', 'Los Angeles', 'United States'),
    (4, 'full house', 48.8566, 2.3522, '75001', 'Paris', 'France'),
    (4, 'apartment', 52.5200, 13.4050, '10115', 'Berlin', 'Germany'),

    (6, 'apartment', 51.5074, -0.1278, 'SW1A 1AA', 'London', 'United Kingdom'),
    (6, 'room', 52.5200, 13.4050, '10115', 'Berlin', 'Germany'),
    (6, 'full house', 40.7128, -74.0060, '10001', 'New York', 'United States'),
    (6, 'apartment', 34.0522, -118.2437, '90001', 'Los Angeles', 'United States');

INSERT INTO listing_amenities (
    listing_id, wifi, washer, air_conditioning, dedicated_workspace, hair_dryer, kitchen, dryer, heating, tv, iron
) VALUES
    (1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0),
    (2, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1),
    (3, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0),
    (4, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0),
    (5, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0),
    (6, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0),
    (7, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0),
    (8, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0),
    (9, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0),
    (10, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0),
    (11, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0),
    (12, 1, 0, 0, 1, 1, 1, 0, 0, 1, 0);

INSERT INTO availability (listing_id, date, price)
VALUES
    (6, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 100),
    (6, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 110),

    (7, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 120),
    (7, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 130),

    (8, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 140),
    (8, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 150),
    (8, DATE_ADD(CURDATE(), INTERVAL 10 DAY), 150),

    (9, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 90),
    (9, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 95);

INSERT INTO bookings (listing_id, renter_id, start_date, finish_date, pricing, status)
VALUES

    (6, 1, '2023-07-29', '2023-08-02', 450, 'normal'),
    (8, 3, '2023-07-30', '2023-08-05', 900, 'normal'),
    (9, 5, '2023-08-01', '2023-08-04', 270, 'normal'),

    (7, 2, '2023-08-14', '2023-08-18', 600, 'normal'),
    (8, 4, '2023-08-15', '2023-08-20', 750, 'normal'),
    (9, 6, '2023-08-16', '2023-08-19', 270, 'normal'),
    
    (6, 1, '2023-07-01', '2023-07-05', 400, 'renter_cancelled'),
    (7, 3, '2023-09-10', '2023-09-15', 700, 'host_cancelled'),
    (8, 5, '2023-09-12', '2023-09-18', 800, 'renter_cancelled');