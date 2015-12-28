DROP DATABASE IF EXISTS `booking_schema`;

CREATE DATABASE `booking_schema`;

CREATE USER 'booking'@'localhost' IDENTIFIED BY 'booking';
GRANT ALL ON booking_schema.* TO 'booking'@'localhost';

USE `booking_schema`;

CREATE TABLE `user`(
	`id` bigint primary key auto_increment,
	`name` varchar(100) not null,
	`email` varchar(100) unique not null 
);

CREATE TABLE `userAccount`(
	`userId` bigint not null unique,
	`prepaidAmount` decimal(10,2) not null DEFAULT 0.0 CHECK (`prepaidAmount` >= 0 ),
	constraint `fk_user_account_user` 
		foreign key (`userId`) references `user`(`id`)
		on update cascade 
		on delete cascade
);

CREATE TABLE `event`(
	`id` bigint primary key auto_increment,
	`title` varchar(500) not null,
	`date` date not null,
	`ticketPrice` decimal(10,2) not null CHECK (`ticketPrice` >= 0)
);

CREATE TABLE `ticket`(
	`id` bigint primary key auto_increment,
	`eventId` bigint not null,
	`userId` bigint not null,
	`place` int not null,
	`category` varchar(10) CHECK (`category` IN ('STANDART', 'PREMIUM', 'BAR')),
	constraint `fk_ticket_event` 
		foreign key (`eventId`) references `event`(`id`) 
		on update cascade 
		on delete no action,
	constraint `fk_ticket_user` 
		foreign key (`userId`) references `user`(`id`)
		on update cascade 
		on delete cascade
);

## creating separate schema for tests

DROP DATABASE IF EXISTS `test_booking_schema`;

CREATE DATABASE `test_booking_schema`;
GRANT ALL ON `test_booking_schema`.* TO 'booking'@'localhost';

USE `test_booking_schema`;

CREATE TABLE `user` LIKE `booking_schema`.`user`;
CREATE TABLE `userAccount` LIKE `booking_schema`.`userAccount`;
CREATE TABLE `event` LIKE `booking_schema`.`event`;
CREATE TABLE `ticket` LIKE `booking_schema`.`ticket`;
#CREATE TABLE `ticket` AS SELECT * FROM `booking_schema`.`ticket` WHERE 2 = 1;

