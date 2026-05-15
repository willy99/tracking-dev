-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 138.201.128.230    Database: sotrackc_tracking_dev
-- ------------------------------------------------------
-- Server version	5.7.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `tr_authenticated_user`
--

DROP TABLE IF EXISTS `tr_authenticated_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_authenticated_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` int(10) NOT NULL,
  `expired_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_auth_user` (`user_id`),
  CONSTRAINT `fk_auth_user` FOREIGN KEY (`user_id`) REFERENCES `tr_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_authenticated_user`
--

LOCK TABLES `tr_authenticated_user` WRITE;
/*!40000 ALTER TABLE `tr_authenticated_user` DISABLE KEYS */;
INSERT INTO `tr_authenticated_user` VALUES (1,'d183606f-8f93-4966-b7a2-8b33b8bf2f8c',1,'2016-11-15 01:08:52'),(2,'734874da-f2b3-48e7-a8f5-1e50c5449af9',1,'2016-11-16 21:25:51'),(3,'bb1ce594-c7c5-43a2-b933-8b6b6d7ef0da',1,'2016-11-20 04:52:13'),(4,'1be873e6-a9d4-4ddb-99c8-654d4ca20245',1,'2016-11-21 03:17:53'),(5,'d5bc76cb-5135-497e-978f-c2c21e07ee85',3,'2016-11-21 03:17:43'),(6,'05816c8c-a23d-4926-8999-28912b8b4257',1,'2016-11-26 07:36:33'),(7,'0a2cbce4-03db-4331-bdee-6d509739eb54',1,'2016-12-06 00:38:11'),(8,'bed10141-a024-4357-8098-4f55cdb0cbc0',1,'2017-02-18 06:43:15'),(9,'cab19a99-2546-44c1-899a-da9220aa04f2',1,'2017-02-22 06:50:45'),(10,'03c9c956-83f9-41af-8b9b-9dd8a3329564',2,'2017-02-22 06:30:18'),(11,'50a94adb-d991-48b1-a8f4-bc9c9f389e25',1,'2017-02-25 15:59:44'),(12,'3b83eb90-4ee3-43c5-840e-3910ed37c668',1,'2017-02-28 14:59:41'),(13,'0474f73c-e552-42f9-b74f-21c365df83ed',1,'2017-03-01 10:47:28'),(14,'067bd0c6-f62a-4574-8cae-4a3fc25f2949',1,'2017-03-02 14:14:02'),(15,'615f7859-9f80-4626-abeb-47f1ec6d0411',1,'2017-03-03 10:53:16'),(16,'663d464a-94e8-40c9-86d2-c6d82416e896',1,'2017-03-04 13:29:48'),(17,'53fa6026-4b2f-49eb-879b-129ccb61d6a5',1,'2017-03-05 10:27:00'),(18,'84d7a324-8ca7-4f53-ac60-b7bcf8841fc8',1,'2017-03-07 12:54:25'),(19,'cf6c5ccb-8766-469b-8c82-65f92de844a4',1,'2017-03-12 05:38:16'),(20,'31773699-d99d-45b7-872d-94faf6300a63',1,'2017-03-15 10:21:18'),(21,'5795a28a-30ad-4f9d-9602-39441d23e647',1,'2017-03-18 15:07:14'),(22,'50919d1a-c5ed-40c5-a2d5-7f583fe8ed9c',1,'2017-03-20 13:20:29'),(23,'c987edcc-7c35-49dd-aa26-343c4ecc869f',1,'2017-03-21 06:46:13'),(24,'5ef8d6bd-6c0d-410d-9325-fbfeb937d980',1,'2017-03-24 02:47:36'),(25,'2157e6c8-e3ff-4685-94db-2d1f6fc8672b',6,'2017-03-24 02:48:40'),(26,'c704ce7c-e658-4677-b4e4-edfec74574a9',1,'2017-03-29 11:19:57'),(27,'1b9b8b00-1d48-4a49-8c0c-b9d19a756c42',1,'2017-03-30 03:40:48'),(28,'e30220b0-63cf-4a3b-a2cc-36cddb412d9d',1,'2017-04-01 12:36:19'),(29,'b15095e6-e7d7-4b36-ac36-7159a261db1a',1,'2017-04-03 11:44:44'),(30,'897a9dbc-309c-4d4d-b174-926cc4b60ba8',1,'2017-04-05 06:44:01'),(31,'dbd47612-9778-469f-bc0f-ccdbbe988c2a',6,'2017-04-05 06:47:40'),(32,'acddd818-90b4-450f-b175-548f00b2285b',6,'2017-04-06 00:12:54'),(33,'4203736a-6886-483c-b574-d02699a465eb',6,'2017-04-07 03:38:26'),(34,'22bc3b24-ae90-47fd-9e98-9ccb3ce66487',1,'2017-04-07 03:37:40'),(35,'4ac1b28a-84ab-4c31-b90c-1b305565f88a',1,'2017-04-11 05:27:42'),(36,'8607b81a-2de1-4075-a595-2d39ab24c494',1,'2017-04-13 05:26:37'),(37,'0326e2f8-fb15-4837-9546-3d028d21b856',1,'2017-04-15 00:54:44'),(38,'257c027a-041a-4688-b138-edf2a966d07f',1,'2017-04-19 03:40:09'),(39,'2bc7b3fd-8bb6-46b1-8a93-767996df7775',7,'2017-04-19 04:51:45'),(40,'095d4849-e6e4-4338-b7a1-d15872a411e9',7,'2017-04-21 00:52:07'),(41,'b7a6ccd9-945f-45ae-9271-78c0540b4df3',1,'2017-04-21 01:22:56'),(42,'cfa5963c-8bef-4807-92e5-2fe2613044eb',7,'2017-04-22 02:05:59'),(43,'4fe73b2b-fedf-4128-af50-e41dd60a7c16',1,'2017-04-22 02:59:13'),(44,'4d7b9040-3182-496d-9460-4685bf1b290d',7,'2017-04-25 05:58:25'),(45,'38c4ece9-874b-47d6-a5a2-f85e3aba7828',1,'2017-04-25 06:23:29'),(46,'31f755ad-5a1a-496f-9f5b-0b7059120aa4',1,'2017-04-27 09:13:32'),(47,'fb027bf8-16ab-4c39-b3fa-f82e78407502',1,'2017-05-05 00:43:38'),(48,'5253a1a6-fd9c-41fa-be8e-7a70dba643b4',1,'2017-05-07 05:37:21'),(49,'23428735-ef5f-48ec-8ede-52aaf23ba30c',6,'2017-05-06 23:59:41'),(50,'4a371ce9-0a5f-4118-b413-ed13ced8aab9',1,'2017-05-08 05:56:04'),(51,'855fd357-65b6-439e-bb3a-24ff6817429e',7,'2017-05-09 00:45:33'),(52,'1d2eea6a-73de-4388-99ee-4829606f44d5',1,'2017-05-10 12:28:28'),(53,'9ac60eef-3548-4376-accc-b1999dcb9397',1,'2017-05-11 08:44:53'),(54,'d2f55b04-1503-45ec-bf92-3b8a17dde51f',1,'2017-05-12 13:40:45'),(55,'27530913-3504-49cc-87e3-6b74d64c7a97',1,'2017-05-13 12:57:51'),(56,'d5a08a85-cafb-48b1-8d77-705927e3878d',1,'2017-05-16 14:08:13'),(57,'64559427-2988-47cd-ad3a-96cdaabad747',1,'2017-05-27 07:48:13'),(58,'700d7e52-296e-49b1-8c19-a540962425a5',6,'2017-05-27 08:50:29'),(59,'356f8f45-18c9-47eb-a5d2-899a3574e7c9',7,'2017-05-28 10:49:09'),(60,'af350196-e2d6-424d-adc6-6ad2517e3981',6,'2017-05-29 02:21:40'),(61,'f95cf803-a055-4339-b2fc-ccbc6a8f20dc',1,'2017-05-29 02:39:40'),(62,'2a390f21-53db-4693-850b-22ad1dada611',6,'2017-05-31 10:08:43'),(63,'bef9928f-05d4-4e7e-b14a-3b5c5cb35515',7,'2017-05-31 13:15:31'),(64,'48ec206f-b6df-416d-b1c0-bd434d49a05c',7,'2017-06-01 01:54:02'),(65,'052964e1-32ce-49b9-9cc1-59d6c9040cb8',7,'2017-06-04 10:08:59'),(66,'f51c3fea-427f-4491-884d-0cf4a2ac902e',6,'2017-06-04 12:25:50'),(67,'9584b85e-6926-4469-aa4e-e9f292be9b7a',6,'2017-06-06 07:59:03'),(68,'1f48f059-e755-4f39-a7f6-b395946b61b0',7,'2017-06-05 10:59:49'),(69,'ba997b5c-f519-45d3-b074-3c031aa539ba',6,'2017-06-07 14:52:47'),(70,'c6dd96c6-ac89-4421-8346-5799eb42ad29',6,'2017-06-08 08:09:13'),(71,'571e09a4-f0f4-405d-99dc-c11db0bd5c42',6,'2017-06-09 07:34:01'),(72,'2f6bb891-4f94-457a-a734-40b6a581eeff',6,'2017-06-10 02:42:58'),(73,'005c67a4-7ac5-42da-ad9a-32a896c142fc',6,'2017-06-12 14:06:09'),(74,'72ce497f-2fe5-4897-9b84-cf43de6c492d',7,'2017-06-14 10:18:10'),(75,'1745ed4e-8dc5-438d-bccd-9656025a913b',6,'2017-06-16 06:10:11'),(76,'da6e6af2-1402-4484-8eea-fd43b0ee08d4',7,'2017-06-17 23:31:38'),(77,'558e3374-9391-461b-a99a-dc425528092a',7,'2017-06-19 08:07:45'),(78,'02ea1dba-f798-4e43-980a-bdb569045403',6,'2017-06-20 06:00:35'),(79,'a2406b00-3932-4527-a8ab-361482799133',6,'2017-07-05 01:19:08'),(80,'20db0836-2daa-4749-b182-4e2b924984e7',6,'2017-07-07 00:30:59'),(81,'aa754765-efac-497e-bccd-5c93a33c644f',7,'2017-07-07 01:02:24'),(82,'c051c9d7-3c52-4008-8d8a-3839b715f902',7,'2017-07-08 01:26:14'),(83,'f22b7de4-153c-4cbf-82ad-6218b881ddd3',6,'2017-07-15 04:04:43'),(84,'95d676dd-da4b-461e-8e60-9a3e6ee152c4',6,'2017-07-16 20:36:26'),(85,'3ef7df22-26ea-4ca5-bd1a-5056c9ad01d3',1,'2017-07-18 06:05:11'),(86,'afd966df-f62c-4640-a8b2-e922c375d0e5',6,'2017-07-19 01:45:31'),(87,'8b0dfdb9-3cf8-4e8b-91c6-62cafd16628a',1,'2017-07-18 21:31:24'),(88,'f7e9a8d1-e340-41f9-b200-e834c2dee318',2,'2017-07-18 23:06:25'),(89,'1f60c60b-a7b8-4074-8f8e-2b33fa84a530',2,'2017-07-25 21:06:29'),(90,'c0ae981e-eff4-4534-abbe-7e6bf5e5ecd8',2,'2017-07-27 19:58:56'),(91,'e1208fc8-ae42-471a-8a21-ec8aa4b83bf8',2,'2017-07-29 17:39:26'),(92,'560ba01c-bdcd-406f-b453-833f506928e1',2,'2017-07-31 19:57:01'),(93,'6ece682a-22d1-4f70-90d5-bb8d42a0ae2d',2,'2017-08-01 21:37:06'),(94,'e04928d3-9ce5-4aa8-b382-12437f9ef54b',2,'2017-08-11 23:43:03'),(95,'16db1a27-33bc-4d6a-b204-3163dec30335',2,'2017-08-15 19:39:15'),(96,'8a828816-662a-4eee-ac90-682ec38d467a',2,'2017-08-18 16:51:44'),(97,'c053aa11-d336-420d-afc0-1fb68052c718',1,'2017-08-28 22:25:57'),(98,'76e8a672-f0d6-42ef-845e-ebdd6b307278',2,'2017-08-29 15:06:54'),(99,'9ad13b69-a735-4f5e-9e9e-ed8f64af50c8',6,'2017-08-30 02:50:35'),(100,'ef20aa93-0ec4-47b2-ac55-5ee1d24b7873',6,'2017-08-30 19:41:30'),(101,'5c74547e-0161-4436-b9ac-d86a13f0668a',6,'2017-08-31 22:27:27'),(102,'1168e87a-9762-42cf-9f3c-aae458618062',6,'2017-09-02 18:07:39'),(103,'920f070a-0598-45c0-99f7-026f43a019d0',6,'2017-09-05 03:41:59'),(104,'95b59d29-ad7b-4786-a457-cf9e1bf936a7',6,'2017-09-05 22:02:10'),(105,'571f1c35-0b97-42c7-b19f-726a12bee4e7',6,'2017-09-08 04:28:20'),(106,'c107aec3-b153-40c4-9c03-ab5253078ef3',6,'2017-09-08 22:21:21'),(107,'c098cff6-0108-41f6-9502-fb6d1a964d2a',6,'2017-09-10 08:07:58'),(108,'f5e784df-c77a-4189-96f0-4c377ed3ffe0',6,'2017-09-10 20:33:20'),(109,'8304fa59-c413-40f5-bd9f-b870e298f54f',6,'2017-09-13 04:54:59'),(110,'78540241-cc22-41f8-83b9-7eea27ce03aa',6,'2017-09-14 04:09:04'),(111,'68cf01ef-9aa1-45c8-b17b-a0cf8b9999a3',6,'2017-09-15 06:22:57'),(112,'179ddddb-767f-419c-86f2-7cf622546177',6,'2017-09-16 01:32:31'),(113,'51d85a43-aebf-4176-9462-e265a4a860f4',6,'2017-09-17 02:16:11'),(114,'8a21cfff-57b7-431f-a586-dfd10aa5bb92',6,'2017-09-19 00:13:34'),(115,'10c5446f-b98d-4d58-a169-8e09a504fb0a',6,'2017-09-20 04:53:54'),(116,'ded0604b-5d6b-443c-9bf6-9deb1da2039b',6,'2017-09-21 04:26:44'),(117,'5246a257-bc7f-4ce8-87d1-ce743ca9b549',6,'2017-09-22 01:59:38'),(118,'59d22c07-00c4-4b52-a190-3389fbd0652e',6,'2017-09-23 05:41:51'),(119,'be9054da-5be3-46aa-b4d9-43a4c03de578',6,'2017-09-25 17:55:10'),(120,'aadf527c-bbc8-4ce0-98f9-28bd4d0a9f93',6,'2017-09-29 05:24:55'),(121,'23db3def-199e-4155-955a-97442989e275',6,'2017-09-30 05:27:41'),(122,'55de4584-e7f9-49cb-9d40-fd6cf4baf6eb',6,'2017-10-01 01:10:34'),(123,'e4226943-eccb-449e-a359-cfed26fa7ebe',6,'2017-10-02 21:01:54'),(124,'c903b626-28c5-4f18-bd2b-c7eedf380fba',6,'2017-10-04 06:28:56'),(125,'682c6e8a-baf8-4562-af1d-ed08062c6cf4',6,'2017-10-05 19:58:51'),(126,'4ca0b888-f515-494e-add8-446db1567ae0',6,'2017-10-07 02:40:52'),(127,'1b47d0c9-0b81-4565-8a45-8b94621060f3',6,'2017-10-08 16:15:00'),(128,'0cc2a5bc-3eec-4bb8-9e6d-2b73a9f687ed',6,'2017-10-11 19:13:37'),(129,'7f94c2d8-93ec-4980-9b9d-b5de6c31df4e',6,'2017-11-20 23:36:54'),(130,'e390b36e-25a4-4993-b3a6-14c84d18236f',6,'2017-11-22 20:09:17'),(131,'b2f2a33a-ad79-4527-bae9-7397264b4751',6,'2017-11-24 01:59:28'),(132,'f393a432-17a6-48fd-9ca8-3d8d3c53ae10',6,'2017-11-25 00:00:11'),(133,'097fd978-1c71-46f0-8e1c-114047c78c39',6,'2017-11-28 05:02:52'),(134,'efb71446-3095-41b9-bdad-936168b15b7d',6,'2017-11-29 22:34:20'),(135,'cd169ed1-de70-42c7-832d-5345e8a720b5',6,'2018-02-04 00:49:41'),(136,'c1d2991d-87b6-4783-8ca5-4201f57dbbaa',1,'2018-02-04 00:51:39'),(137,'01b5dfd2-3984-4f81-b8f4-937d467c9b9f',6,'2018-02-07 05:53:57'),(138,'79fa5eee-5168-4447-8040-0e0a872484ef',1,'2018-02-07 05:45:02'),(139,'2b887d61-e5d6-46f2-87bf-a5f16d9703d9',1,'2018-02-07 18:46:20'),(140,'856ac9da-7bb4-49eb-b70e-c7eebfcfbee1',1,'2018-02-09 02:16:05'),(141,'4afea5ce-6877-4bac-a75b-8a9ad50daf12',6,'2018-02-09 08:45:03'),(142,'6845bee5-7a22-4d6d-8969-89e03850e230',1,'2018-02-09 22:22:17'),(143,'70804ba4-52fb-4506-8c18-600eb97508a5',6,'2018-02-10 00:10:18'),(144,'9a220090-1f58-41a3-a7bb-4b16b2c9a815',6,'2018-02-20 00:20:02'),(145,'48d54ada-3862-4e15-9f3b-32b6839e7651',1,'2018-02-20 00:22:47'),(146,'47d0081f-e102-4dc9-ac5a-c1336c76e332',1,'2018-02-22 08:09:52'),(147,'c268f6b8-1af1-4042-9543-8ccfbb072370',1,'2018-02-22 22:19:12'),(148,'3718068a-2045-4853-b5b0-815884c1b1f4',1,'2018-03-01 09:05:17');
/*!40000 ALTER TABLE `tr_authenticated_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_company`
--

DROP TABLE IF EXISTS `tr_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `version` int(11) NOT NULL,
  `active` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'Y',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_company`
--

LOCK TABLES `tr_company` WRITE;
/*!40000 ALTER TABLE `tr_company` DISABLE KEYS */;
INSERT INTO `tr_company` VALUES (3,'So-Tracking',1,'Y','2018-02-08 12:33:04'),(4,'CompanyTest',0,'Y','2018-02-09 12:33:32');
/*!40000 ALTER TABLE `tr_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_container_location`
--

DROP TABLE IF EXISTS `tr_container_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_container_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detail` int(11) NOT NULL,
  `map_latitude` double NOT NULL,
  `map_longitude` double NOT NULL,
  `location_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `container_location_fk_idx` (`detail`),
  CONSTRAINT `container_location_fk` FOREIGN KEY (`detail`) REFERENCES `tr_order_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_container_location`
--

LOCK TABLES `tr_container_location` WRITE;
/*!40000 ALTER TABLE `tr_container_location` DISABLE KEYS */;
INSERT INTO `tr_container_location` VALUES (2,23,40.70979201243496,-73.67362976074219,'2017-05-09 19:32:08',1,'2017-01-09 17:00:00'),(3,23,40.684803661591275,-73.91944885253906,'2017-05-09 19:31:52',1,'2017-01-09 17:00:00'),(4,22,41.357226341046065,-74.67750549316406,'2017-05-09 19:32:38',1,'2017-01-09 17:00:00'),(5,22,40.54824374987604,-74.29435729980469,'2017-05-09 19:32:22',1,'2017-01-09 17:00:00'),(6,19,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(7,19,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(8,18,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(9,18,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(10,17,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(11,17,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(12,16,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(13,16,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(14,15,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(15,15,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(16,14,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(17,14,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(18,13,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(19,13,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(20,12,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(21,12,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(22,8,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(23,8,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(24,6,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(25,6,40.741895,-73.989308,'2017-05-09 19:28:36',1,'2017-01-09 17:00:00'),(26,19,40.49813666850851,-74.26414489746094,'2017-05-09 19:33:19',1,'2017-01-09 17:00:00'),(27,19,40.75557964275589,-74.98649597167969,'2017-05-09 19:33:08',1,'2017-01-09 17:00:00'),(28,25,41.341762527112614,-74.82444763183594,'2017-05-09 19:31:30',1,'2017-01-09 06:00:00'),(29,26,41.155910350545035,-74.23805236816406,'2017-05-09 19:31:10',1,'2017-01-09 06:00:00'),(30,24,41.3417625271126,-74.26414489746094,'2017-05-09 05:00:00',1,'2017-01-09 06:00:00');
/*!40000 ALTER TABLE `tr_container_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_container_type`
--

DROP TABLE IF EXISTS `tr_container_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_container_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `length` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_container_type`
--

LOCK TABLES `tr_container_type` WRITE;
/*!40000 ALTER TABLE `tr_container_type` DISABLE KEYS */;
INSERT INTO `tr_container_type` VALUES (1,'20’ DV',20),(2,'40’ DV',40),(3,'40’ HC',40),(4,'20’ RF',20),(5,'40’ RF',40),(6,'20’ OT',20),(7,'40’ OT',40),(8,'20’ FR',20),(9,'40’ FR',40),(10,'20’ TC',20),(15,'40F',40),(22,'40F',40),(293,'40F',40),(294,'40F',40),(295,'40F',40),(296,'40F',40),(297,'40F',40),(298,'40F',40),(299,'40F',40),(300,'40F',40),(301,'40F',40),(302,'40F',40),(303,'40F',40),(304,'40F',40),(305,'40F',40),(306,'40F',40),(307,'40F',40),(308,'40F',40),(309,'40F',40),(310,'40F',40),(311,'40F',40),(312,'40F',40);
/*!40000 ALTER TABLE `tr_container_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_driver`
--

DROP TABLE IF EXISTS `tr_driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `trailer_number` varchar(45) DEFAULT NULL,
  `tractor_number` varchar(45) DEFAULT NULL,
  `mobile` varchar(45) NOT NULL,
  `length` int(11) NOT NULL,
  `tractorNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=310 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_driver`
--

LOCK TABLES `tr_driver` WRITE;
/*!40000 ALTER TABLE `tr_driver` DISABLE KEYS */;
INSERT INTO `tr_driver` VALUES (1,'Валера','Дыня','ВН3999??','??67766','09688800022',0,NULL),(3,'Бублик','Дырявый','ВВ8898РР','??67766','09688800022',0,NULL),(4,'Шкандылябрик','Корявый','АО7888ОО','??67766','09688800022',0,NULL),(290,'HKAKD','HIICD','MCDMB','KIHCM','KLAFI',5,NULL),(291,'DLAFE','ICBBD','AJFDB','KLDIJ','BGYIE',5,NULL),(292,'GFYHY','FIJDJ','EBMKC','JDEHE','GEAAG',5,NULL),(293,'FJHDL','AAEYI','MMCKB','IMMJB','CFMFY',5,NULL),(294,'CIDHE','DEYDA','LJMFY','KJCKB','IGLBY',5,NULL),(295,'GKLGL','AICGF','ELEAA','JDJJM','JKYED',5,NULL),(296,'IDJEL','DLMKA','GGKMK','MJCAK','KHLGI',5,NULL),(297,'YDLYG','ALKIH','EMJKC','MIGMD','JBMYG',5,NULL),(298,'HAJCK','HFKYI','YJKKE','AHDYB','KCBJL',5,NULL),(299,'LJCEG','EYMFC','JFMKB','JIGGC','KEIBE',5,NULL),(300,'JCEIY','LLMHY','LGIMG','JFYBE','KFEEA',5,NULL),(301,'FMEYL','BMKJK','JHIDD','CACFC','KJLIG',5,NULL),(302,'AMLDJ','ABFEC','CAAAE','KDLIH','YDKEL',5,NULL),(303,'BJAGG','AJGFM','GJGAK','EGHDC','CJBIY',5,NULL),(304,'BMBED','MGCEI','DCYGC','CLDIK','FMGYE',5,NULL),(305,'KAAGG','LJGCI','AYLGD','MJJEH','AAEDM',5,NULL),(306,'HBYLL','FCIHG','DLFEC','IMGKY','GYDAK',5,NULL),(307,'IYEEC','LIKAL','GIHCI','LBABI','KKMBD',5,NULL),(308,'DGBID','YMKJD','BMFYA','LKFGC','GGDDJ',5,NULL),(309,'FHBKY','DLBCF','AFJDA','MFIIG','YMIHF',5,NULL);
/*!40000 ALTER TABLE `tr_driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_job_status_info`
--

DROP TABLE IF EXISTS `tr_job_status_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_job_status_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `stop_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `running` char(1) DEFAULT 'N',
  `enabled` char(1) NOT NULL DEFAULT 'Y',
  `enabled_by` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hvhw4d5opit5sdu09s7axsx8q` (`enabled_by`),
  CONSTRAINT `FK_hvhw4d5opit5sdu09s7axsx8q` FOREIGN KEY (`enabled_by`) REFERENCES `tr_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_job_status_info`
--

LOCK TABLES `tr_job_status_info` WRITE;
/*!40000 ALTER TABLE `tr_job_status_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `tr_job_status_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_line`
--

DROP TABLE IF EXISTS `tr_line`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=369 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_line`
--

LOCK TABLES `tr_line` WRITE;
/*!40000 ALTER TABLE `tr_line` DISABLE KEYS */;
INSERT INTO `tr_line` VALUES (1,'ZIM'),(2,'Happag-Lloyd'),(3,'Yang Ming '),(4,'UASC'),(5,'SEAGO'),(6,'Evergreen'),(7,'OOCL'),(8,'MSC'),(9,'MOL'),(10,'MAERSK'),(11,'HMM'),(12,'COSCO'),(13,'CMA CGM'),(14,'Arkas'),(15,'APL'),(16,'ANL'),(17,'Admiral'),(18,'TURKON'),(42,'NewYork-Marsel'),(43,'NewYork-Marsel'),(44,'NewYork-Marsel'),(47,'NewYork-Marsel'),(48,'NewYork-Marsel'),(49,'NewYork-Marsel'),(50,'NewYork-Marsel'),(51,'NewYork-Marsel'),(55,'NewYork-Marsel'),(58,'NewYork-Marsel'),(77,'NewYork-Marsel'),(78,'NewYork-Marsel'),(349,'FFEYL'),(350,'MMFBH'),(351,'FYMBJ'),(352,'KKDEI'),(353,'GMELM'),(354,'AEJHI'),(355,'CFAAC'),(356,'MMIKD'),(357,'GAHAM'),(358,'EJEEM'),(359,'LMEYD'),(360,'KIJCH'),(361,'FFJYG'),(362,'LMBJH'),(363,'KECJL'),(364,'FBABD'),(365,'AIJYE'),(366,'JBYGY'),(367,'DEYIB'),(368,'MMLJG');
/*!40000 ALTER TABLE `tr_line` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_order`
--

DROP TABLE IF EXISTS `tr_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_1c` varchar(100) NOT NULL,
  `tracking_line` int(11) NOT NULL,
  `terminal` int(11) NOT NULL,
  `content_name` varchar(255) NOT NULL,
  `order_date` datetime NOT NULL,
  `client` int(11) NOT NULL,
  `trend` varchar(15) NOT NULL,
  `tenant` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `transaction_terminal_fk` (`terminal`),
  KEY `transaction_client_fk` (`client`),
  KEY `transaction_line_fk` (`tracking_line`),
  KEY `order_tenant_fk` (`tenant`),
  CONSTRAINT `order_client_fk` FOREIGN KEY (`client`) REFERENCES `tr_user` (`id`),
  CONSTRAINT `order_line_fk` FOREIGN KEY (`tracking_line`) REFERENCES `tr_line` (`id`),
  CONSTRAINT `order_tenant_fk` FOREIGN KEY (`tenant`) REFERENCES `tr_company` (`id`),
  CONSTRAINT `order_terminal_fk` FOREIGN KEY (`terminal`) REFERENCES `tr_terminal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=313 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_order`
--

LOCK TABLES `tr_order` WRITE;
/*!40000 ALTER TABLE `tr_order` DISABLE KEYS */;
INSERT INTO `tr_order` VALUES (2,'34oifj3904fj34',1,1,'Dolls','2017-03-14 00:00:00',7,'IMPORT',3,0),(3,'34fj304mfopk;l',2,1,'Bells','2017-03-04 00:00:00',7,'EXPORT',3,0),(4,'af3e434f3434f',2,1,'Dolls','2017-03-12 00:00:00',7,'EXPORT',3,0),(5,'asdfasdf3f334f3',1,1,'?????','2017-03-12 00:00:00',7,'EXPORT',3,0),(10,'asf34f34f34f1',1,1,'Bells','2017-03-21 00:00:00',6,'EXPORT',3,0),(11,'asf34f34f34f2',2,1,'Bells','2017-03-21 00:00:00',6,'IMPORT',3,0),(12,'34f34fd14c3',1,1,'Dolls','2017-03-21 00:00:00',6,'IMPORT',3,0),(293,'IDCDM',349,347,'LKLCA','2017-08-17 11:29:33',301,'EXPORT',3,0),(294,'BCFLG',350,348,'GEDKC','2017-08-17 11:29:34',302,'EXPORT',3,0),(295,'KMABB',351,349,'DGKEJ','2017-08-17 11:29:36',303,'EXPORT',3,0),(296,'MAMMM',352,350,'IMMJK','2017-08-17 11:29:37',304,'EXPORT',3,0),(297,'YLLYD',353,351,'BCMAF','2017-08-17 11:29:38',305,'EXPORT',3,0),(298,'FJYKK',354,352,'JKMHY','2017-08-17 11:29:40',306,'EXPORT',3,0),(299,'FMKCH',355,353,'IMBAA','2017-08-17 11:29:41',307,'EXPORT',3,0),(300,'MJEEM',356,354,'IFCYY','2017-08-17 11:29:42',308,'EXPORT',3,0),(301,'LIGEH',357,355,'CADLY','2017-08-17 11:29:44',309,'EXPORT',3,0),(302,'YEECD',358,356,'ICYDC','2017-08-17 11:29:45',310,'EXPORT',3,0),(303,'LKCMY',359,357,'GAHAI','2017-08-17 11:29:46',311,'EXPORT',3,0),(304,'YEYML',360,358,'GKLLI','2017-08-17 11:29:48',312,'EXPORT',3,0),(305,'HAYML',361,359,'ACMIF','2017-08-17 11:29:50',313,'EXPORT',3,0),(306,'MJMFG',362,360,'CIKCE','2017-08-17 11:29:51',314,'EXPORT',3,0),(307,'LAGGG',363,361,'YEHJY','2017-08-17 11:29:53',315,'EXPORT',3,0),(308,'JKJYH',364,362,'JHMJM','2017-08-17 11:29:55',316,'EXPORT',3,0),(309,'IHJDA',365,363,'AHKGG','2017-08-17 11:29:56',317,'EXPORT',3,0),(310,'CBEHF',366,364,'KAEAK','2017-08-17 11:29:57',318,'EXPORT',3,0),(311,'BGDYK',367,365,'DJDYB','2017-08-17 11:29:59',319,'EXPORT',3,0),(312,'GAEEB',368,366,'FCFCC','2017-08-17 11:30:00',320,'EXPORT',3,0);
/*!40000 ALTER TABLE `tr_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_order_details`
--

DROP TABLE IF EXISTS `tr_order_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_order_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracking_order` int(11) NOT NULL,
  `driver` int(11) NOT NULL,
  `container_type` int(11) DEFAULT NULL,
  `container_number` varchar(45) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  PRIMARY KEY (`id`,`tracking_order`,`driver`),
  KEY `transaction_details_fk_idx` (`tracking_order`),
  KEY `transaction_details_driver_fk_idx` (`driver`),
  KEY `transaction_details_container_fk_idx` (`container_type`),
  CONSTRAINT `transaction_details_container_fk` FOREIGN KEY (`container_type`) REFERENCES `tr_container_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `transaction_details_driver_fk` FOREIGN KEY (`driver`) REFERENCES `tr_driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `transaction_details_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tr_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_order_details`
--

LOCK TABLES `tr_order_details` WRITE;
/*!40000 ALTER TABLE `tr_order_details` DISABLE KEYS */;
INSERT INTO `tr_order_details` VALUES (3,2,1,1,'MSCH2452074',15825),(6,2,1,2,'MSCH24521',20209),(8,3,3,4,'MSCH24522',18378),(12,3,3,5,'MSCH24523',1212),(13,3,4,2,'MSCH24524',12092),(14,4,1,1,'MSCH24525',15825),(15,4,1,2,'MSCH24526',15825),(16,5,1,2,'MSCH24527',15835),(17,5,3,1,'MSCH24528',15835),(18,12,1,1,'MSCH24529',15845),(19,11,1,1,'MSCH24531',15845),(22,11,3,1,'MSCH24532',15845),(23,12,3,1,'MSCH24533',15945),(24,10,1,1,'MSCH24534',15845),(25,10,3,1,'MSCH24535',15945),(26,10,4,1,'MSCH24536',15945);
/*!40000 ALTER TABLE `tr_order_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_order_workflow`
--

DROP TABLE IF EXISTS `tr_order_workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_order_workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracking_order` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `deal_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`tracking_order`,`status`,`deal_date`),
  KEY `transaction_workflow_fk_idx` (`tracking_order`),
  CONSTRAINT `transaction_workflow_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tr_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_order_workflow`
--

LOCK TABLES `tr_order_workflow` WRITE;
/*!40000 ALTER TABLE `tr_order_workflow` DISABLE KEYS */;
INSERT INTO `tr_order_workflow` VALUES (1,10,'REGISTERED','2017-01-22 06:00:00'),(2,10,'TRANSPORTING','2017-01-23 06:00:00'),(3,10,'DELIVERED','2017-01-26 06:00:00'),(4,11,'REGISTERED','2017-01-22 06:00:00'),(5,11,'TRANSPORTING','2017-05-26 14:33:27'),(6,11,'DELIVERED','2017-02-26 06:00:00');
/*!40000 ALTER TABLE `tr_order_workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_permission`
--

DROP TABLE IF EXISTS `tr_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_permission` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_permission_fk` (`name`),
  KEY `fk_id_permission` (`parent`),
  CONSTRAINT `tr_permission_ibfk_1` FOREIGN KEY (`parent`) REFERENCES `tr_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_permission`
--

LOCK TABLES `tr_permission` WRITE;
/*!40000 ALTER TABLE `tr_permission` DISABLE KEYS */;
INSERT INTO `tr_permission` VALUES (3,'SHOW_ORDERS','Show all orders',NULL),(4,'SHOW_DETAILS','Show details on order',NULL),(5,'SHOW_WORKFLOW','Show the workflow',NULL),(6,'SHOW_DICTIONARIES','Show dictionaries',26),(7,'SHOW_ROLES','Role Management',26),(8,'SHOW_USERS','User Management',26),(9,'SHOW_STATUS','Status page',26),(10,'LOGIN_APP','Login availability to site',25),(11,'JOB_STATUS','The page with jobs',NULL),(12,'SHOW_SYSTEM_CONFIG_MANAGEMENT','System configuration',NULL),(13,'SHOW_SYSTEM_MONITORING','System monitoring and tests',NULL),(14,'ORDER','Order management',NULL),(15,'Show_orders','Show all orders',14),(16,'Update_order','Update order permission',14),(17,'Details','Details management',14),(18,'Show_details','Show details on order',17),(19,'Update_details','Update details of order',17),(20,'Dictionaries','Dictionaries management',NULL),(21,'Show_Dictionaries','Show all dictionaries',20),(22,'Update_Dictionaries','Update Dictionaries permission',20),(23,'COMPANY','Maintain Companies',NULL),(24,'CREATE_UPDATE_DELETE_COMPANY','Company Admin Permission (Creating new/updating)',23),(25,'SYSTEM','General Application Permissions',NULL),(26,'COMPANY_ADMIN','Company Admin Management',NULL);
/*!40000 ALTER TABLE `tr_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_role`
--

DROP TABLE IF EXISTS `tr_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `editable` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_role`
--

LOCK TABLES `tr_role` WRITE;
/*!40000 ALTER TABLE `tr_role` DISABLE KEYS */;
INSERT INTO `tr_role` VALUES (1,'System Admin','N'),(2,'User','Y'),(10,'Manager','N'),(11,'Company Admin','Y');
/*!40000 ALTER TABLE `tr_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_role_tr_permission`
--

DROP TABLE IF EXISTS `tr_role_tr_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_role_tr_permission` (
  `tr_role_id` bigint(20) NOT NULL,
  `permissionList_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tr_role_id`,`permissionList_id`),
  KEY `FK_a1d7g1muce0y6yh2amihq0c62` (`permissionList_id`),
  KEY `FK_a6kdmfd1p5v4lh3wlyj870fuh` (`tr_role_id`),
  KEY `UK_a1d7g1muce0y6yh2amihq0c62` (`permissionList_id`,`tr_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_role_tr_permission`
--

LOCK TABLES `tr_role_tr_permission` WRITE;
/*!40000 ALTER TABLE `tr_role_tr_permission` DISABLE KEYS */;
INSERT INTO `tr_role_tr_permission` VALUES (1,3),(10,3),(1,4),(10,4),(1,5),(10,5),(1,6),(10,6),(1,7),(10,7),(1,8),(10,8),(1,9),(10,9),(1,10),(10,10),(11,10),(10,11),(10,12),(10,13),(2,14),(11,23),(11,24);
/*!40000 ALTER TABLE `tr_role_tr_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_site`
--

DROP TABLE IF EXISTS `tr_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `get_url` varchar(255) DEFAULT NULL,
  `post_url` varchar(255) DEFAULT NULL,
  `success_element` varchar(255) NOT NULL,
  `failure_element` varchar(255) DEFAULT NULL,
  `enclose` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_site`
--

LOCK TABLES `tr_site` WRITE;
/*!40000 ALTER TABLE `tr_site` DISABLE KEYS */;
INSERT INTO `tr_site` VALUES (1,'http://www.hapag-lloyd.com/en/online-business/tracing/tracing-by-container.html?container={0}',NULL,'Container Information','Container for given Container Number not found','represent actual data'),(2,'http://www.zim.com/pages/findcontainer.aspx?searchvalue1={0}',NULL,'Success line','The Information you requested may not be available','Success'),(3,'http://www.yangming.com/e-service/Track_Trace/mul_ctnr.aspx?str={0},&rdolType=CT',NULL,'<td>Container No.</td>','we can\'t identify','</tbody>'),(4,'http://uasconline.uasc.net/TrackingResults?Sequence={0}&itype=iTrack&callType=NoAjax&firstcall_track=no',NULL,'<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\" class=\"bdr_lgold\" id=\"table-trackSummary\">','Following numbers are invalid','<td align=\"center\" bgcolor=\"#FFFFFF\">'),(5,'http://moc.oocl.com/party/cargotracking/ct_search_from_other_domain.jsf?ANONYMOUS_BEHAVIOR=BUILD_UP&domainName=PARTY_DOMAIN&ENTRY_TYPE=OOCL&ENTRY=MCC&ctSearchType=CNTR&ctShipmentNumber={0}',NULL,'<table id=\"summaryTable\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"dataTable\">','todo','</table></td>'),(6,'https://my.maerskline.com/tracking/search?searchNumber={0}',NULL,'success','todo',NULL),(7,'http://www.cma-cgm.com/ebusiness/tracking/search?SearchBy=Container&Reference={0}',NULL,'<table class=\"data-table\" id=\"container-moves\">','No Results','</table>'),(8,'http://homeport.apl.com/gentrack/trackingMain.do?trackInput01={0}',NULL,'Container Number','todo','Clearances'),(9,'http://www.anl.com.au/ebusiness/tracking/search?SearchBy=Container&Reference={0}',NULL,'<table class=\"data-table\" id=\"container-moves\">','No Results','</table>');
/*!40000 ALTER TABLE `tr_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_terminal`
--

DROP TABLE IF EXISTS `tr_terminal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_terminal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_terminal`
--

LOCK TABLES `tr_terminal` WRITE;
/*!40000 ALTER TABLE `tr_terminal` DISABLE KEYS */;
INSERT INTO `tr_terminal` VALUES (1,'Yuzhny','Petrova 12','Yuzhny','Ukraine'),(2,'GPK','Bocharova 33','Odessa','Ukraine'),(37,'A3','Tversky 72/2','Liverpool','England'),(40,'A3','Tversky 72/2','Liverpool','England'),(42,'A3','Tversky 72/2','Liverpool','England'),(45,'A3','Tversky 72/2','Liverpool','England'),(46,'A3','Tversky 72/2','Liverpool','England'),(47,'A3','Tversky 72/2','Liverpool','England'),(48,'A3','Tversky 72/2','Liverpool','England'),(49,'A3','Tversky 72/2','Liverpool','England'),(53,'A3','Tversky 72/2','Liverpool','England'),(56,'AGALM','IIJJE','JIMIY','LYCIK'),(75,'CGDAC','MJMFB','LEHMD','YYMAL'),(76,'DCHKB','CMDDM','YEHFI','BLEFD'),(347,'FGGFD','AJKLL','YMKHI','ABIJY'),(348,'HKHGM','LKHIF','YJKEF','CILIK'),(349,'DBMKG','HEEIH','IDIYC','MGYCM'),(350,'AYJFY','YIFEG','BFBEY','IGCIM'),(351,'CJYCG','MCBCA','YFDJE','ACIIF'),(352,'FYIGG','MCFMY','IADFB','CFFCK'),(353,'CHCHJ','ICHIF','LYBMI','KCLDG'),(354,'YHMBY','YDGEL','LEGYJ','EGAIL'),(355,'EMCDL','YHMEH','CBBKK','YLYDF'),(356,'GJCHD','YHGLY','FGKEJ','FYBAM'),(357,'BIACF','LYCHI','DEKJB','JCBIE'),(358,'MMMIH','MFYGC','AJCFI','HLMEE'),(359,'KDMYI','YHEME','BLEIM','LYJGL'),(360,'IKCDI','CJFMH','MEHGI','CCJLM'),(361,'DCGEG','HHMGD','EJFJH','MABMI'),(362,'FJJID','AYBFB','KIGHE','KGMEJ'),(363,'FYGEI','YKYBL','CMACB','EAMIF'),(364,'HCAAA','LCDBF','ILACC','DJDLM'),(365,'HCIGD','CBACJ','DAYJB','YBGBM'),(366,'CGDBD','IEKMF','ILFHF','HYCHF');
/*!40000 ALTER TABLE `tr_terminal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tr_user`
--

DROP TABLE IF EXISTS `tr_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tr_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `active` char(1) DEFAULT 'Y',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `locale` varchar(10) NOT NULL DEFAULT 'en_us',
  `role` int(11) DEFAULT NULL,
  `version` int(11) NOT NULL,
  `tenant` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tr_user_role_fk` (`role`),
  KEY `tr_user_tenant_fk` (`tenant`),
  CONSTRAINT `tr_user_role_fk` FOREIGN KEY (`role`) REFERENCES `tr_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tr_user_tenant_fk` FOREIGN KEY (`tenant`) REFERENCES `tr_company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=324 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tr_user`
--

LOCK TABLES `tr_user` WRITE;
/*!40000 ALTER TABLE `tr_user` DISABLE KEYS */;
INSERT INTO `tr_user` VALUES (1,'WILLY2005@GMAIL.COM','WILLY2005@GMAIL.COM','WILLY2005@GMAIL.COM','Y','2018-02-08 11:20:58','3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2','+380938513201','en_US',11,0,3),(2,'test@example.com','Test','1503983215816','N','2018-02-08 11:20:58','3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2','+380935563311','en_US',1,0,3),(3,'TEST3@EXAMPLE.COM','test3','test3','Y','2018-02-08 11:20:58','70c89eebbe7d46d4eb807438f5e5fae6c56b01b45620f9ee5939c61c275e9d4f','+380565333222','en_US',10,0,3),(4,'FIRST@EXAMPLE.COM','First','Last','N','2018-02-08 11:20:58','d6e3a8f3a4367b032963b07ff374c8f018259c0cc3584ae40ac4fb7f6ffcf6da','+306639394833','en_US',NULL,0,3),(6,'OMSD@LIST.RU','Влад','Олесюк','Y','2018-02-08 11:20:58','3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2','+380675573108','en_US',1,0,3),(7,'OMIHAZ@GMAIL.COM','VLAD','Shvaydetsky','Y','2018-02-08 11:20:58','a0ec06301bf1814970a70f89d1d373afdff9a36d1ba6675fc02f8a975f4efaeb','+380936995666','en_us',1,0,3),(29,'AYBYE','EGAEG','CYEGD','Y','2018-02-08 11:20:58','ADIFY','25-235','ukr',NULL,0,3),(30,'MCELI','GFYAM','LHDAA','N','2018-02-08 11:20:58','DLLHC','25-235','ukr',NULL,0,3),(301,'DKCHA','BABKY','DCYBA','Y','2018-02-08 11:20:58','MJLDC','HFEYM','ukr',NULL,0,3),(302,'CBMCD','LGDFA','GMMEA','N','2018-02-08 11:20:58','JDEHF','GEFLG','ukr',NULL,0,3),(303,'BLHJI','FBLHI','IEBGH','N','2018-02-08 11:20:58','AGJHE','FIIDB','ukr',NULL,0,3),(304,'MAAJB','EDCAE','AIJCD','N','2018-02-08 11:20:58','KFHYJ','YIBLH','ukr',NULL,0,3),(305,'CIHAE','MJJGB','BAACK','N','2018-02-08 11:20:58','BGBBA','AAJCF','ukr',NULL,0,3),(306,'IGELD','LAMIY','YEHMY','Y','2018-02-08 11:20:58','YCILE','FEGLJ','ukr',NULL,0,3),(307,'CCIBC','CDGIJ','ELHBE','Y','2018-02-08 11:20:58','AACLC','AGHKJ','ukr',NULL,0,3),(308,'CJMAI','MAHAI','JIGKY','Y','2018-02-08 11:20:58','ACKKJ','MICGK','ukr',2,0,3),(309,'YKKKC','ALEFL','EHCMM','N','2018-02-08 11:20:58','YDBAA','YBGEA','ukr',NULL,0,3),(310,'GCYIF','JCAME','JIYHA','Y','2018-02-08 11:20:58','FHYCA','BLJID','ukr',NULL,0,3),(311,'LBLKY','CYMLI2','LCJCH','Y','2018-02-08 11:20:58','JFKME','KDIFA','ukr',NULL,0,3),(312,'BHHKG','DEIEC','EFYKC','Y','2018-02-08 11:20:58','KGFCA','BFBMG','ukr',NULL,0,3),(313,'EYKDY','BACYE','CIMIA','N','2018-02-08 11:20:58','YAKAB','DFMEG','ukr',NULL,0,3),(314,'CDJAK','AHKYB','KLYJH','N','2018-02-08 11:20:58','GHEFC','ECJYH','ukr',NULL,0,3),(315,'GIIBH','LHIHD','JHDYL','N','2018-02-08 11:20:58','AFAJH','AGEDF','ukr',NULL,0,3),(316,'MFYJD','GDDIJ','LCMID','Y','2018-02-08 11:20:58','LJBLA','EKYEA','ukr',NULL,0,3),(317,'BIKFM','LAFEI','CIILM','Y','2018-02-08 11:20:58','JCGBY','EBDJB','ukr',NULL,0,3),(318,'KYAKB','HCKGM','CMJJK','Y','2018-02-08 11:20:58','FDBYA','LEBKI','ukr',NULL,0,3),(319,'BGKYG','BYHIJ','MHHIF','Y','2018-02-08 11:20:58','DGAKD','ABEEI','ukr',NULL,0,3),(320,'MFEYF','MBYYF','HAHIG','Y','2018-02-08 11:20:58','JAHLC','JHBHJ','ukr',NULL,0,3),(321,'ARSD@SDSD.RU','Vasya','Pupkin','Y','2018-02-08 11:20:58','15e2b0d3c33891ebb0f1ef609ec419420c20e320ce94c65fbc8c3312448eb225','+38050545454','en',10,0,3),(322,'first2@example.com','First','Last','Y','2018-02-08 16:49:55','1234','0938513202','en',1,0,3),(323,'first3@example.com','First','Last','Y','2018-02-09 12:33:32','1234','0938513203','en',11,0,4);
/*!40000 ALTER TABLE `tr_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'sotrackc_tracking_dev'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-01 13:40:02
