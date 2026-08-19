-- Admin event log for the audit-trail / monitoring feature.
-- Every mutating @MethodCall-annotated action is recorded here automatically
-- (see MethodCallInterceptor). Read-only GET endpoints are not logged.
--
-- Note: PermissionType.SYSTEM_MONITORING (used to gate /tmw/admin/monitoring/**)
-- already exists as a tr_permission row (id=13, name='SYSTEM_MONITORING') — it was
-- reserved but unused. Assign it to the relevant admin role(s) via Role Management.

CREATE TABLE IF NOT EXISTS `event_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL DEFAULT 0,
  `last_updated` timestamp NULL DEFAULT NULL,
  `tenant` int(11) NOT NULL,
  `event_user` int(11) NOT NULL,
  `event_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `action` varchar(255) NOT NULL,
  `permission` varchar(100) DEFAULT NULL,
  `http_method` varchar(10) DEFAULT NULL,
  `success` char(1) NOT NULL DEFAULT 'Y',
  `error_message` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `event_log_tenant_fk` (`tenant`),
  KEY `event_log_user_fk` (`event_user`),
  KEY `event_log_date_idx` (`event_date`),
  CONSTRAINT `event_log_tenant_fk` FOREIGN KEY (`tenant`) REFERENCES `tr_company` (`id`),
  CONSTRAINT `event_log_user_fk` FOREIGN KEY (`event_user`) REFERENCES `tr_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
