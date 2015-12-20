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
	`prepaidAmount` decimal(10,2) not null,
	constraint `fk_user_account_user` 
		foreign key (`userId`) references `user`(`id`)
		on update cascade 
		on delete cascade
);

CREATE TABLE `event`(
	`id` bigint primary key auto_increment,
	`title` varchar(500) not null,
	`date` date not null,
	`ticketPrice` decimal(10,2) not null
);

CREATE TABLE `ticket`(
	`id` bigint primary key auto_increment,
	`eventId` bigint not null,
	`userId` bigint not null,
	`place` int not null,
	`category` enum ('STANDARD', 'PREMIUM', 'BAR'),
	constraint `fk_ticket_event` 
		foreign key (`eventId`) references `event`(`id`) 
		on update cascade 
		on delete cascade,
	constraint `fk_ticket_user` 
		foreign key (`userId`) references `user`(`id`)
		on update cascade 
		on delete cascade
);