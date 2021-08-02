ALTER TABLE teacher
MODIFY COLUMN name varchar(255); 
ALTER TABLE teacher ADD UNIQUE (name);