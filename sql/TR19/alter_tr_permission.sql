ALTER TABLE `tr_permission` ADD COLUMN `parent` INT,
  ADD FOREIGN KEY `fk_id_permission`(`parent`)
REFERENCES `tr_permission`(`id`) ON DELETE CASCADE;