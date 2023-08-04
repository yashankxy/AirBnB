-------------------------------------------------------
-----------------Create User Table---------------------
-------------------------------------------------------


DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255),
    `email` VARCHAR(255),
    `password` VARCHAR(255),
    `address` VARCHAR(255),
    `occupation` VARCHAR(255),
    `sin` VARCHAR(20),
    `dob` VARCHAR(10),
    `renter` BOOLEAN
);
-- insert renter --
INSERT INTO `user` (`name`, `email`, `password`, `address`, `occupation`, `sin`, `dob`, `renter`)
VALUES ('Alice Smith', 'alice@example.com', 'password123', '456 Oak St', 'Graphic Designer', '987654321', '1985-05-15', true);

-- insert host --
INSERT INTO `user` (`name`, `email`, `password`, `address`, `occupation`, `sin`, `dob`, `renter`)
VALUES ('Bob Johnson', 'bob@example.com', 'securepass', '789 Elm Ave', 'Sales Manager', '654321987', '1992-08-20', false);


-------------------------------------------------------
-----------------Create cc Table---------------------
-------------------------------------------------------


DROP TABLE IF EXISTS `cc`;
CREATE TABLE `cc` (
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
