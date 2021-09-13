CREATE TABLE `evaluation` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`student_id` BIGINT NOT NULL,
	`type` varchar(255) NOT NULL,
	`subject` varchar(255) NOT NULL,
	`grade` BIGINT NOT NULL,
	`trimester` BIGINT NOT NULL,
	PRIMARY KEY (`id`)
);

ALTER TABLE `evaluation` ADD CONSTRAINT `evaluation_fk0` FOREIGN KEY (`student_id`) REFERENCES `student`(`id`);