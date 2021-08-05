CREATE TABLE `student` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` varchar(255) NOT NULL,
	`login` varchar(6) NOT NULL,
  	`password` char(60) NOT NULL,
	`classroom_id` BIGINT NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `student` ADD CONSTRAINT `student_fk0` FOREIGN KEY (`classroom_id`) REFERENCES `classroom`(`id`);