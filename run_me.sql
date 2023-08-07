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
    `renter` BOOLEAN,
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
    ('1', '1', '1', '1', '1', '1', '1', false);

INSERT INTO cc (cc_num, cc_name, cc_exp, cc_cvv, uid)
VALUES ('9876543298765432', 'Alice Smith', '08/25', '123', 1);

INSERT INTO listing (host_id, type_of_listing, latitude, longitude, postal_code, city, country)
VALUES
    (1, 'apartment', 37.7749, -122.4194, '94105', 'San Francisco', 'United States'),
    (3, 'apartment', 37.7749, -122.4194, '94105', 'San Francisco', 'United States'),
    (3, 'room', 40.7128, -74.0060, '10001', 'New York City', 'United States'),
    (3, 'room', 34.0522, -118.2437, '90001', 'Los Angeles', 'United States'),
    (3, 'apartment', 41.8781, -87.6298, '60601', 'Chicago', 'United States'),
    (3, 'apartment', 25.7617, -80.1918, '33101', 'Miami', 'United States'),
    (3, 'apartment', 47.6062, -122.3321, '98101', 'Seattle', 'United States');

INSERT INTO listing_amenities (
    listing_id, wifi, washer, air_conditioning, dedicated_workspace, hair_dryer, kitchen, dryer, heating, tv, iron
) VALUES 
    (1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0),
    (2, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1),
    (3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
    (4, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0),
    (5, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1),
    (6, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1),
    (7, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1);

INSERT INTO availability (listing_id, date, price)
VALUES 
    (1, CURRENT_DATE(), 12),
    (2, DATE_ADD(CURRENT_DATE(), INTERVAL 3 DAY), 31),
    (2, '2023-09-24', 12),
    (2, '2023-12-26', 12),
    (2, '2023-12-27', 12),
    (2, '2023-12-25', 19.2);

INSERT INTO bookings (listing_id, renter_id, start_date, finish_date, pricing, status)
VALUES 
  (1, 1, '2023-08-06', '2023-08-07', 150.00, 'normal'),
  (2, 1, '2023-08-16', '2023-08-17', 150.00, 'normal'),
  (4, 1, '2023-08-26', '2023-08-27', 170.00, 'normal'),
  (7, 1, '2023-08-26', '2023-08-27', 150.00, 'normal');