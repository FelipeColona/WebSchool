CREATE TABLE `classroom` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`name` varchar(60) NOT NULL UNIQUE,
	PRIMARY KEY (`id`)
);

CREATE TABLE `teacher_classroom` (
	`teacher_id` BIGINT NOT NULL,
	`classroom_id` BIGINT NOT NULL
);

ALTER TABLE `teacher_classroom` ADD CONSTRAINT `teacher_classroom_fk0` FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`);

ALTER TABLE `teacher_classroom` ADD CONSTRAINT `teacher_classroom_fk1` FOREIGN KEY (`classroom_id`) REFERENCES `classroom`(`id`);