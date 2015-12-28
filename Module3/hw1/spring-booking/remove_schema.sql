#DELETE FROM mysql.user WHERE user = 'booking';
DROP USER 'booking'@'localhost';
FLUSH PRIVILEGES;

DROP DATABASE IF EXISTS `booking_schema`;
DROP DATABASE IF EXISTS `test_booking_schema`;
