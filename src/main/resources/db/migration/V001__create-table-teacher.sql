CREATE TABLE `teacher` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` varchar(60) NOT NULL,
	`email` varchar(255) NOT NULL,
	`password` varchar(60) NOT NULL,
	PRIMARY KEY (`id`)
);