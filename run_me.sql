-------------------------------------------------------
-----------------Create User Table---------------------
-------------------------------------------------------
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
    `renter` BOOLEAN
);
-- insert renter --
INSERT INTO user (`name`, `email`, `password`, `address`, `occupation`, `sin`, `dob`, `renter`)
VALUES ('Alice Smith', 'alice@example.com', 'password123', '456 Oak St', 'Graphic Designer', '987654321', '1985-05-15', true);

-- insert host --
INSERT INTO user (`name`, `email`, `password`, `address`, `occupation`, `sin`, `dob`, `renter`)
VALUES ('Bob Johnson', 'bob@example.com', 'securepass', '789 Elm Ave', 'Sales Manager', '654321987', '1992-08-20', false);


-------------------------------------------------------
-----------------Create cc Table---------------------
-------------------------------------------------------
CREATE TABLE cc (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cc_num` VARCHAR(20) NOT NULL,
    `cc_name` VARCHAR(255) NOT NULL,
    `cc_exp` VARCHAR(10) NOT NULL,
    `cc_cvv` VARCHAR(5) NOT NULL,
    uid INT,
    FOREIGN KEY (uid) REFERENCES user(id) ON DELETE CASCADE
);

-- insert cc to renter --
INSERT INTO cc (cc_num, cc_name, cc_exp, cc_cvv, uid)
VALUES ('9876543298765432', 'Alice Smith', '08/25', '123', 1);


-------------------------------------------------------
-----------------Create listing------------------------
-------------------------------------------------------
CREATE TABLE listing(
    id INT AUTO_INCREMENT PRIMARY KEY,
    host_id INT NOT NULL,
    type_of_listing ENUM('full house', 'apartment', 'room') NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    postal_code VARCHAR(10),
    city VARCHAR(100),
    country VARCHAR(100),
    FOREIGN KEY (host_id) REFERENCES user (id) ON DELETE CASCADE
);
-- insert new listing for user with id 1--
INSERT INTO listing (host_id, type_of_listing, latitude, longitude, postal_code, city, country)
VALUES (2, 'apartment', 37.7749, -122.4194, '94105', 'San Francisco', 'United States');

-------------------------------------------------------
-----------------Create listing_amenities--------------
-------------------------------------------------------
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

INSERT INTO listing_amenities (
    listing_id, wifi, washer, air_conditioning, dedicated_workspace, hair_dryer, kitchen, dryer, heating, tv, iron
) VALUES (
    1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0
);

-------------------------------------------------------
-------------     Create availability        ----------
-------------------------------------------------------
CREATE TABLE availability (
    listing_id INT NOT NULL,
    date DATE NOT NULL,
    price float NOT NULL DEFAULT 0,
    PRIMARY KEY (listing_id, date),
    FOREIGN KEY (listing_id) REFERENCES listing (id) ON DELETE CASCADE
);
-- Query for today
INSERT INTO availability (listing_id, date) VALUES (1, CURRENT_DATE());

-- Query for tomorrow
INSERT INTO availability (listing_id, date) VALUES (1, DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY));

-- Query for the day after tomorrow
INSERT INTO availability (listing_id, date) VALUES (1, DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY));

-- Query for 3 days from today
INSERT INTO availability (listing_id, date) VALUES (1, DATE_ADD(CURRENT_DATE(), INTERVAL 3 DAY));

INSERT INTO availability (listing_id, date) VALUES (1, "2023-12-24");

INSERT INTO availability (listing_id, date, price) VALUES (1, "2023-12-25", 19.2) ON DUPLICATE KEY UPDATE price = 192;
-------------------------------------------------------
-------------     Create bookings        --------------
-------------------------------------------------------
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    listing_id INT NOT NULL,
    renter_id INT NOT NULL,
    start_date Date NOT NULL,
    finish_date Date NOT NULL,
    pricing FLOAT NOT NULL DEFAULT 0,
    status ENUM('user_cancelled', 'renter_cancelled', 'normal') 
        NOT NULL DEFAULT 'normal',
    FOREIGN KEY (listing_id) REFERENCES listing (id),
    FOREIGN KEY (renter_id) REFERENCES user (id)
);