DROP DATABASE IF EXISTS `test_booking_schema`;

CREATE DATABASE `test_booking_schema`;
GRANT ALL ON `test_booking_schema`.* TO 'booking'@'localhost';

USE `test_booking_schema`;

CREATE TABLE `user` LIKE `booking_schema`.`user`;
CREATE TABLE `userAccount` LIKE `booking_schema`.`userAccount`;
CREATE TABLE `event` LIKE `booking_schema`.`event`;
CREATE TABLE `ticket` LIKE `booking_schema`.`ticket`;
#CREATE TABLE `ticket` AS SELECT * FROM `booking_schema`.`ticket` WHERE 2 = 1;
