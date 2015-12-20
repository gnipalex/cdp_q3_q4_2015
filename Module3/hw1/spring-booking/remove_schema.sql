#DELETE FROM mysql.user WHERE user = 'booking';
DROP USER 'booking'@'localhost';
FLUSH PRIVILEGES;

DROP DATABASE IF EXISTS `booking_schema`;
