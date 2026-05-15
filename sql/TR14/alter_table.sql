
SET foreign_key_checks = 0;


ALTER TABLE authenticated_user RENAME TO tr_authenticated_user;
ALTER TABLE container_location RENAME TO tr_container_location;
ALTER TABLE container_type RENAME TO tr_container_type;
ALTER TABLE driver RENAME TO tr_driver;
ALTER TABLE job_status_info RENAME TO tr_job_status_info;
ALTER TABLE order_details RENAME TO tr_order_details;
ALTER TABLE order_workflow RENAME TO tr_order_workflow;
ALTER TABLE terminal RENAME TO tr_terminal;
ALTER TABLE tracking_line RENAME TO tr_line;
ALTER TABLE tracking_order RENAME TO tr_order;
ALTER TABLE tracking_permission RENAME TO tr_permission;
ALTER TABLE tracking_role RENAME TO tr_role;
ALTER TABLE tracking_role_tracking_permission RENAME TO tr_role_tr_permission;
ALTER TABLE tracking_site RENAME TO tr_site;
ALTER TABLE tracking_user RENAME TO tr_user;
ALTER TABLE tracking_user_role RENAME TO tr_user_role;

alter table `tr_role_tr_permission` CHANGE tracking_role_id tr_role_id bigint(20) NOT NULL;

ALTER TABLE `tr_authenticated_user` DROP FOREIGN KEY `fk_auth_user`;
ALTER TABLE `tr_authenticated_user` ADD CONSTRAINT `fk_auth_user` FOREIGN KEY (`user_id`) REFERENCES `tr_user` (`id`);


ALTER TABLE `tr_container_location` DROP FOREIGN KEY `container_location_fk`;
ALTER TABLE `tr_container_location` ADD CONSTRAINT `container_location_fk` FOREIGN KEY (`detail`) REFERENCES `tr_order_details` (`id`);


ALTER TABLE `tr_job_status_info` DROP FOREIGN KEY `FK_hvhw4d5opit5sdu09s7axsx8q`;
ALTER TABLE `tr_job_status_info` ADD CONSTRAINT `FK_hvhw4d5opit5sdu09s7axsx8q` FOREIGN KEY (`enabled_by`) REFERENCES `tr_user` (`id`);



ALTER TABLE `tr_job_status_info` DROP FOREIGN KEY `FK_hvhw4d5opit5sdu09s7axsx8q`;
ALTER TABLE `tr_job_status_info` ADD CONSTRAINT `FK_hvhw4d5opit5sdu09s7axsx8q` FOREIGN KEY (`enabled_by`) REFERENCES `tr_user` (`id`);



ALTER TABLE `tr_order_details` DROP FOREIGN KEY `transaction_details_container_fk`,  DROP FOREIGN KEY  `transaction_details_driver_fk`,  DROP FOREIGN KEY `transaction_details_fk`;
ALTER TABLE `tr_order_details`  ADD CONSTRAINT `transaction_details_container_fk` FOREIGN KEY (`container_type`) REFERENCES `tr_container_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `transaction_details_driver_fk` FOREIGN KEY (`driver`) REFERENCES `tr_driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `transaction_details_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tr_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `tr_order_workflow` DROP FOREIGN KEY `transaction_workflow_fk`;
ALTER TABLE `tr_order_workflow`  ADD CONSTRAINT `transaction_workflow_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tr_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `tr_order` DROP FOREIGN KEY `transaction_client_fk`,  DROP FOREIGN KEY `transaction_line_fk`,  DROP FOREIGN KEY `transaction_terminal_fk`;
ALTER TABLE `tr_order` ADD CONSTRAINT `transaction_client_fk` FOREIGN KEY (`client`) REFERENCES `tr_user` (`id`),
  ADD CONSTRAINT `transaction_line_fk` FOREIGN KEY (`tracking_line`) REFERENCES `tr_line` (`id`),
  ADD CONSTRAINT `transaction_terminal_fk` FOREIGN KEY (`terminal`) REFERENCES `tr_terminal` (`id`);


ALTER TABLE `tr_user_role` DROP FOREIGN KEY `fk_user_role_role`, DROP FOREIGN KEY `fk_user_role_user`;
ALTER TABLE `tr_user_role`  ADD CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `tr_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `tr_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

SET foreign_key_checks = 1;