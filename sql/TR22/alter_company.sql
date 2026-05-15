use sotrackc_tracking_dev;

CREATE TABLE IF NOT EXISTS `tr_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `active` char(1) DEFAULT 'Y',
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
