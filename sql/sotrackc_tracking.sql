-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Хост: localhost:3306
-- Время создания: Июн 19 2017 г., 09:11
-- Версия сервера: 5.5.54-cll
-- Версия PHP: 5.6.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `sotrackc_tracking`
--
CREATE DATABASE IF NOT EXISTS `sotrackc_tracking` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `sotrackc_tracking`;

-- --------------------------------------------------------

--
-- Структура таблицы `authenticated_user`
--

DROP TABLE IF EXISTS `authenticated_user`;
CREATE TABLE IF NOT EXISTS `authenticated_user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `user_id` int(10) NOT NULL,
  `expired_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_auth_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `authenticated_user`
--

INSERT INTO `authenticated_user` (`id`, `token`, `user_id`, `expired_date`) VALUES
(1, 'd183606f-8f93-4966-b7a2-8b33b8bf2f8c', 1, '2016-11-15 01:08:52'),
(2, '734874da-f2b3-48e7-a8f5-1e50c5449af9', 1, '2016-11-16 21:25:51'),
(3, 'bb1ce594-c7c5-43a2-b933-8b6b6d7ef0da', 1, '2016-11-20 04:52:13'),
(4, '1be873e6-a9d4-4ddb-99c8-654d4ca20245', 1, '2016-11-21 03:17:53'),
(5, 'd5bc76cb-5135-497e-978f-c2c21e07ee85', 3, '2016-11-21 03:17:43'),
(6, '05816c8c-a23d-4926-8999-28912b8b4257', 1, '2016-11-26 07:36:33'),
(7, '0a2cbce4-03db-4331-bdee-6d509739eb54', 1, '2016-12-06 00:38:11'),
(8, 'bed10141-a024-4357-8098-4f55cdb0cbc0', 1, '2017-02-18 06:43:15'),
(9, 'cab19a99-2546-44c1-899a-da9220aa04f2', 1, '2017-02-22 06:50:45'),
(10, '03c9c956-83f9-41af-8b9b-9dd8a3329564', 2, '2017-02-22 06:30:18'),
(11, '50a94adb-d991-48b1-a8f4-bc9c9f389e25', 1, '2017-02-25 15:59:44'),
(12, '3b83eb90-4ee3-43c5-840e-3910ed37c668', 1, '2017-02-28 14:59:41'),
(13, '0474f73c-e552-42f9-b74f-21c365df83ed', 1, '2017-03-01 10:47:28'),
(14, '067bd0c6-f62a-4574-8cae-4a3fc25f2949', 1, '2017-03-02 14:14:02'),
(15, '615f7859-9f80-4626-abeb-47f1ec6d0411', 1, '2017-03-03 10:53:16'),
(16, '663d464a-94e8-40c9-86d2-c6d82416e896', 1, '2017-03-04 13:29:48'),
(17, '53fa6026-4b2f-49eb-879b-129ccb61d6a5', 1, '2017-03-05 10:27:00'),
(18, '84d7a324-8ca7-4f53-ac60-b7bcf8841fc8', 1, '2017-03-07 12:54:25'),
(19, 'cf6c5ccb-8766-469b-8c82-65f92de844a4', 1, '2017-03-12 05:38:16'),
(20, '31773699-d99d-45b7-872d-94faf6300a63', 1, '2017-03-15 10:21:18'),
(21, '5795a28a-30ad-4f9d-9602-39441d23e647', 1, '2017-03-18 15:07:14'),
(22, '50919d1a-c5ed-40c5-a2d5-7f583fe8ed9c', 1, '2017-03-20 13:20:29'),
(23, 'c987edcc-7c35-49dd-aa26-343c4ecc869f', 1, '2017-03-21 06:46:13'),
(24, '5ef8d6bd-6c0d-410d-9325-fbfeb937d980', 1, '2017-03-24 02:47:36'),
(25, '2157e6c8-e3ff-4685-94db-2d1f6fc8672b', 6, '2017-03-24 02:48:40'),
(26, 'c704ce7c-e658-4677-b4e4-edfec74574a9', 1, '2017-03-29 11:19:57'),
(27, '1b9b8b00-1d48-4a49-8c0c-b9d19a756c42', 1, '2017-03-30 03:40:48'),
(28, 'e30220b0-63cf-4a3b-a2cc-36cddb412d9d', 1, '2017-04-01 12:36:19'),
(29, 'b15095e6-e7d7-4b36-ac36-7159a261db1a', 1, '2017-04-03 11:44:44'),
(30, '897a9dbc-309c-4d4d-b174-926cc4b60ba8', 1, '2017-04-05 06:44:01'),
(31, 'dbd47612-9778-469f-bc0f-ccdbbe988c2a', 6, '2017-04-05 06:47:40'),
(32, 'acddd818-90b4-450f-b175-548f00b2285b', 6, '2017-04-06 00:12:54'),
(33, '4203736a-6886-483c-b574-d02699a465eb', 6, '2017-04-07 03:38:26'),
(34, '22bc3b24-ae90-47fd-9e98-9ccb3ce66487', 1, '2017-04-07 03:37:40'),
(35, '4ac1b28a-84ab-4c31-b90c-1b305565f88a', 1, '2017-04-11 05:27:42'),
(36, '8607b81a-2de1-4075-a595-2d39ab24c494', 1, '2017-04-13 05:26:37'),
(37, '0326e2f8-fb15-4837-9546-3d028d21b856', 1, '2017-04-15 00:54:44'),
(38, '257c027a-041a-4688-b138-edf2a966d07f', 1, '2017-04-19 03:40:09'),
(39, '2bc7b3fd-8bb6-46b1-8a93-767996df7775', 7, '2017-04-19 04:51:45'),
(40, '095d4849-e6e4-4338-b7a1-d15872a411e9', 7, '2017-04-21 00:52:07'),
(41, 'b7a6ccd9-945f-45ae-9271-78c0540b4df3', 1, '2017-04-21 01:22:56'),
(42, 'cfa5963c-8bef-4807-92e5-2fe2613044eb', 7, '2017-04-22 02:05:59'),
(43, '4fe73b2b-fedf-4128-af50-e41dd60a7c16', 1, '2017-04-22 02:59:13'),
(44, '4d7b9040-3182-496d-9460-4685bf1b290d', 7, '2017-04-25 05:58:25'),
(45, '38c4ece9-874b-47d6-a5a2-f85e3aba7828', 1, '2017-04-25 06:23:29'),
(46, '31f755ad-5a1a-496f-9f5b-0b7059120aa4', 1, '2017-04-27 09:13:32'),
(47, 'fb027bf8-16ab-4c39-b3fa-f82e78407502', 1, '2017-05-05 00:43:38'),
(48, '5253a1a6-fd9c-41fa-be8e-7a70dba643b4', 1, '2017-05-07 05:37:21'),
(49, '23428735-ef5f-48ec-8ede-52aaf23ba30c', 6, '2017-05-06 23:59:41'),
(50, '4a371ce9-0a5f-4118-b413-ed13ced8aab9', 1, '2017-05-08 05:56:04'),
(51, '855fd357-65b6-439e-bb3a-24ff6817429e', 7, '2017-05-09 00:45:33'),
(52, '1d2eea6a-73de-4388-99ee-4829606f44d5', 1, '2017-05-10 12:28:28'),
(53, '9ac60eef-3548-4376-accc-b1999dcb9397', 1, '2017-05-11 08:44:53'),
(54, 'd2f55b04-1503-45ec-bf92-3b8a17dde51f', 1, '2017-05-12 13:40:45'),
(55, '27530913-3504-49cc-87e3-6b74d64c7a97', 1, '2017-05-13 12:57:51'),
(56, 'd5a08a85-cafb-48b1-8d77-705927e3878d', 1, '2017-05-16 14:08:13'),
(57, '64559427-2988-47cd-ad3a-96cdaabad747', 1, '2017-05-27 07:48:13'),
(58, '700d7e52-296e-49b1-8c19-a540962425a5', 6, '2017-05-27 08:50:29'),
(59, '356f8f45-18c9-47eb-a5d2-899a3574e7c9', 7, '2017-05-28 10:49:09'),
(60, 'af350196-e2d6-424d-adc6-6ad2517e3981', 6, '2017-05-29 02:21:40'),
(61, 'f95cf803-a055-4339-b2fc-ccbc6a8f20dc', 1, '2017-05-29 02:39:40'),
(62, '2a390f21-53db-4693-850b-22ad1dada611', 6, '2017-05-31 10:08:43'),
(63, 'bef9928f-05d4-4e7e-b14a-3b5c5cb35515', 7, '2017-05-31 13:15:31'),
(64, '48ec206f-b6df-416d-b1c0-bd434d49a05c', 7, '2017-06-01 01:54:02'),
(65, '052964e1-32ce-49b9-9cc1-59d6c9040cb8', 7, '2017-06-04 10:08:59'),
(66, 'f51c3fea-427f-4491-884d-0cf4a2ac902e', 6, '2017-06-04 12:25:50'),
(67, '9584b85e-6926-4469-aa4e-e9f292be9b7a', 6, '2017-06-06 07:59:03'),
(68, '1f48f059-e755-4f39-a7f6-b395946b61b0', 7, '2017-06-05 10:59:49'),
(69, 'ba997b5c-f519-45d3-b074-3c031aa539ba', 6, '2017-06-07 14:52:47'),
(70, 'c6dd96c6-ac89-4421-8346-5799eb42ad29', 6, '2017-06-08 08:09:13'),
(71, '571e09a4-f0f4-405d-99dc-c11db0bd5c42', 6, '2017-06-09 07:34:01'),
(72, '2f6bb891-4f94-457a-a734-40b6a581eeff', 6, '2017-06-10 02:42:58'),
(73, '005c67a4-7ac5-42da-ad9a-32a896c142fc', 6, '2017-06-12 14:06:09'),
(74, '72ce497f-2fe5-4897-9b84-cf43de6c492d', 7, '2017-06-14 10:18:10'),
(75, '1745ed4e-8dc5-438d-bccd-9656025a913b', 6, '2017-06-16 06:10:11'),
(76, 'da6e6af2-1402-4484-8eea-fd43b0ee08d4', 7, '2017-06-17 23:31:38'),
(77, '558e3374-9391-461b-a99a-dc425528092a', 7, '2017-06-19 08:07:45'),
(78, '02ea1dba-f798-4e43-980a-bdb569045403', 6, '2017-06-20 06:00:35');

-- --------------------------------------------------------

--
-- Структура таблицы `container_location`
--

DROP TABLE IF EXISTS `container_location`;
CREATE TABLE IF NOT EXISTS `container_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detail` int(11) NOT NULL,
  `map_latitude` double NOT NULL,
  `map_longitude` double NOT NULL,
  `location_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` int(11) NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `container_location_fk_idx` (`detail`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `container_location`
--

INSERT INTO `container_location` (`id`, `detail`, `map_latitude`, `map_longitude`, `location_date`, `version`, `last_update`) VALUES
(2, 23, 40.70979201243496, -73.67362976074219, '2017-05-09 19:32:08', 1, '2017-01-09 17:00:00'),
(3, 23, 40.684803661591275, -73.91944885253906, '2017-05-09 19:31:52', 1, '2017-01-09 17:00:00'),
(4, 22, 41.357226341046065, -74.67750549316406, '2017-05-09 19:32:38', 1, '2017-01-09 17:00:00'),
(5, 22, 40.54824374987604, -74.29435729980469, '2017-05-09 19:32:22', 1, '2017-01-09 17:00:00'),
(6, 19, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(7, 19, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(8, 18, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(9, 18, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(10, 17, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(11, 17, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(12, 16, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(13, 16, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(14, 15, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(15, 15, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(16, 14, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(17, 14, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(18, 13, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(19, 13, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(20, 12, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(21, 12, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(22, 8, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(23, 8, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(24, 6, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(25, 6, 40.741895, -73.989308, '2017-05-09 19:28:36', 1, '2017-01-09 17:00:00'),
(26, 19, 40.49813666850851, -74.26414489746094, '2017-05-09 19:33:19', 1, '2017-01-09 17:00:00'),
(27, 19, 40.75557964275589, -74.98649597167969, '2017-05-09 19:33:08', 1, '2017-01-09 17:00:00'),
(28, 25, 41.341762527112614, -74.82444763183594, '2017-05-09 19:31:30', 1, '2017-01-09 06:00:00'),
(29, 26, 41.155910350545035, -74.23805236816406, '2017-05-09 19:31:10', 1, '2017-01-09 06:00:00'),
(30, 24, 41.3417625271126, -74.26414489746094, '2017-05-09 05:00:00', 1, '2017-01-09 06:00:00');

-- --------------------------------------------------------

--
-- Структура таблицы `container_type`
--

DROP TABLE IF EXISTS `container_type`;
CREATE TABLE IF NOT EXISTS `container_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  `length` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `container_type`
--

INSERT INTO `container_type` (`id`, `type`, `length`) VALUES
(1, '20’ DV', 20),
(2, '40’ DV', 40),
(3, '40’ HC', 40),
(4, '20’ RF', 20),
(5, '40’ RF', 40),
(6, '20’ OT', 20),
(7, '40’ OT', 40),
(8, '20’ FR', 20),
(9, '40’ FR', 40),
(10, '20’ TC', 20);

-- --------------------------------------------------------

--
-- Структура таблицы `driver`
--

DROP TABLE IF EXISTS `driver`;
CREATE TABLE IF NOT EXISTS `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `trailer_number` varchar(45) DEFAULT NULL,
  `tractor_number` varchar(45) DEFAULT NULL,
  `mobile` varchar(45) NOT NULL,
  `length` int(11) NOT NULL,
  `tractorNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `driver`
--

INSERT INTO `driver` (`id`, `first_name`, `last_name`, `trailer_number`, `tractor_number`, `mobile`, `length`, `tractorNumber`) VALUES
(1, 'Валера', 'Дыня', 'ВН3999??', '??67766', '09688800022', 0, NULL),
(3, 'Бублик', 'Дырявый', 'ВВ8898РР', '??67766', '09688800022', 0, NULL),
(4, 'Шкандылябрик', 'Корявый', 'АО7888ОО', '??67766', '09688800022', 0, NULL);

-- --------------------------------------------------------

--
-- Структура таблицы `job_status_info`
--

DROP TABLE IF EXISTS `job_status_info`;
CREATE TABLE IF NOT EXISTS `job_status_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `stop_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `running` char(1) DEFAULT 'N',
  `enabled` char(1) NOT NULL DEFAULT 'Y',
  `enabled_by` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hvhw4d5opit5sdu09s7axsx8q` (`enabled_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Структура таблицы `order_details`
--

DROP TABLE IF EXISTS `order_details`;
CREATE TABLE IF NOT EXISTS `order_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracking_order` int(11) NOT NULL,
  `driver` int(11) NOT NULL,
  `container_type` int(11) DEFAULT NULL,
  `container_number` varchar(45) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  PRIMARY KEY (`id`,`tracking_order`,`driver`),
  KEY `transaction_details_fk_idx` (`tracking_order`),
  KEY `transaction_details_driver_fk_idx` (`driver`),
  KEY `transaction_details_container_fk_idx` (`container_type`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `order_details`
--

INSERT INTO `order_details` (`id`, `tracking_order`, `driver`, `container_type`, `container_number`, `weight`) VALUES
(3, 2, 1, 1, 'MSCH2452074', 15825),
(6, 2, 1, 2, 'MSCH24521', 20209),
(8, 3, 3, 4, 'MSCH24522', 18378),
(12, 3, 3, 5, 'MSCH24523', 1212),
(13, 3, 4, 2, 'MSCH24524', 12092),
(14, 4, 1, 1, 'MSCH24525', 15825),
(15, 4, 1, 2, 'MSCH24526', 15825),
(16, 5, 1, 2, 'MSCH24527', 15835),
(17, 5, 3, 1, 'MSCH24528', 15835),
(18, 12, 1, 1, 'MSCH24529', 15845),
(19, 11, 1, 1, 'MSCH24531', 15845),
(22, 11, 3, 1, 'MSCH24532', 15845),
(23, 12, 3, 1, 'MSCH24533', 15945),
(24, 10, 1, 1, 'MSCH24534', 15845),
(25, 10, 3, 1, 'MSCH24535', 15945),
(26, 10, 4, 1, 'MSCH24536', 15945);

-- --------------------------------------------------------

--
-- Структура таблицы `order_workflow`
--

DROP TABLE IF EXISTS `order_workflow`;
CREATE TABLE IF NOT EXISTS `order_workflow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tracking_order` int(11) NOT NULL,
  `status` varchar(45) NOT NULL,
  `deal_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`,`tracking_order`,`status`,`deal_date`),
  KEY `transaction_workflow_fk_idx` (`tracking_order`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `order_workflow`
--

INSERT INTO `order_workflow` (`id`, `tracking_order`, `status`, `deal_date`) VALUES
(1, 10, 'REGISTERED', '2017-01-22 06:00:00'),
(2, 10, 'TRANSPORTING', '2017-01-23 06:00:00'),
(3, 10, 'DELIVERED', '2017-01-26 06:00:00'),
(4, 11, 'REGISTERED', '2017-01-22 06:00:00'),
(5, 11, 'TRANSPORTING', '2017-05-26 14:33:27'),
(6, 11, 'DELIVERED', '2017-02-26 06:00:00');

-- --------------------------------------------------------

--
-- Структура таблицы `terminal`
--

DROP TABLE IF EXISTS `terminal`;
CREATE TABLE IF NOT EXISTS `terminal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `terminal`
--

INSERT INTO `terminal` (`id`, `name`, `address`, `city`, `country`) VALUES
(1, 'Yuzhny', 'Petrova 12', 'Yuzhny', 'Ukraine'),
(2, 'GPK', 'Bocharova 33', 'Odessa', 'Ukraine');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_line`
--

DROP TABLE IF EXISTS `tracking_line`;
CREATE TABLE IF NOT EXISTS `tracking_line` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_line`
--

INSERT INTO `tracking_line` (`id`, `name`) VALUES
(1, 'ZIM'),
(2, 'Happag-Lloyd'),
(3, 'Yang Ming '),
(4, 'UASC'),
(5, 'SEAGO'),
(6, 'Evergreen'),
(7, 'OOCL'),
(8, 'MSC'),
(9, 'MOL'),
(10, 'MAERSK'),
(11, 'HMM'),
(12, 'COSCO'),
(13, 'CMA CGM'),
(14, 'Arkas'),
(15, 'APL'),
(16, 'ANL'),
(17, 'Admiral'),
(18, 'TURKON');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_order`
--

DROP TABLE IF EXISTS `tracking_order`;
CREATE TABLE IF NOT EXISTS `tracking_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_1c` varchar(100) NOT NULL,
  `tracking_line` int(11) NOT NULL,
  `terminal` int(11) NOT NULL,
  `content_name` varchar(255) NOT NULL,
  `order_date` datetime NOT NULL,
  `client` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `transaction_terminal_fk` (`terminal`),
  KEY `transaction_client_fk` (`client`),
  KEY `transaction_line_fk` (`tracking_line`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_order`
--

INSERT INTO `tracking_order` (`id`, `order_1c`, `tracking_line`, `terminal`, `content_name`, `order_date`, `client`) VALUES
(2, '34oifj3904fj34', 1, 1, 'Dolls', '2017-03-14 00:00:00', 7),
(3, '34fj304mfopk;l', 2, 1, 'Bells', '2017-03-04 00:00:00', 7),
(4, 'af3e434f3434f', 2, 1, 'Dolls', '2017-03-12 00:00:00', 7),
(5, 'asdfasdf3f334f3', 1, 1, '?????', '2017-03-12 00:00:00', 7),
(10, 'asf34f34f34f1', 1, 1, 'Bells', '2017-03-21 00:00:00', 6),
(11, 'asf34f34f34f2', 2, 1, 'Bells', '2017-03-21 00:00:00', 6),
(12, '34f34fd14c3', 1, 1, 'Dolls', '2017-03-21 00:00:00', 6);

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_permission`
--

DROP TABLE IF EXISTS `tracking_permission`;
CREATE TABLE IF NOT EXISTS `tracking_permission` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `role_permission_fk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_permission`
--

INSERT INTO `tracking_permission` (`id`, `name`, `description`) VALUES
(3, 'SHOW_ORDERS', 'Show all orders'),
(4, 'SHOW_DETAILS', 'Show details on order'),
(5, 'SHOW_WORKFLOW', 'Show the workflow'),
(6, 'SHOW_DICTIONARIES', 'Show dictionaries'),
(7, 'SHOW_ROLES', 'Role Management'),
(8, 'SHOW_USERS', 'User Management'),
(9, 'SHOW_STATUS', 'Status page'),
(10, 'LOGIN_APP', 'Login availability to site'),
(11, 'JOB_STATUS', 'The page with jobs'),
(12, 'SHOW_SYSTEM_CONFIG_MANAGEMENT', 'System configuration'),
(13, 'SHOW_SYSTEM_MONITORING', 'System monitoring and tests');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_role`
--

DROP TABLE IF EXISTS `tracking_role`;
CREATE TABLE IF NOT EXISTS `tracking_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) NOT NULL,
  `editable` char(1) NOT NULL DEFAULT 'Y',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_role`
--

INSERT INTO `tracking_role` (`id`, `role_name`, `editable`) VALUES
(1, 'System Admin', 'N'),
(2, 'User', 'Y'),
(8, 'New Role', 'Y'),
(10, 'Manager', 'N');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_role_tracking_permission`
--

DROP TABLE IF EXISTS `tracking_role_tracking_permission`;
CREATE TABLE IF NOT EXISTS `tracking_role_tracking_permission` (
  `tracking_role_id` bigint(20) NOT NULL,
  `permissionList_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tracking_role_id`,`permissionList_id`),
  KEY `FK_a1d7g1muce0y6yh2amihq0c62` (`permissionList_id`),
  KEY `FK_a6kdmfd1p5v4lh3wlyj870fuh` (`tracking_role_id`),
  KEY `UK_a1d7g1muce0y6yh2amihq0c62` (`permissionList_id`,`tracking_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_role_tracking_permission`
--

INSERT INTO `tracking_role_tracking_permission` (`tracking_role_id`, `permissionList_id`) VALUES
(1, 3),
(2, 3),
(10, 3),
(1, 4),
(2, 4),
(10, 4),
(1, 5),
(2, 5),
(10, 5),
(1, 6),
(10, 6),
(1, 7),
(10, 7),
(1, 8),
(10, 8),
(1, 9),
(8, 9),
(10, 9),
(1, 10),
(8, 10),
(10, 10),
(8, 11),
(10, 11),
(10, 12),
(10, 13);

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_site`
--

DROP TABLE IF EXISTS `tracking_site`;
CREATE TABLE IF NOT EXISTS `tracking_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `get_url` varchar(255) DEFAULT NULL,
  `post_url` varchar(255) DEFAULT NULL,
  `success_element` varchar(255) NOT NULL,
  `failure_element` varchar(255) DEFAULT NULL,
  `enclose` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_site`
--

INSERT INTO `tracking_site` (`id`, `get_url`, `post_url`, `success_element`, `failure_element`, `enclose`) VALUES
(1, 'http://www.hapag-lloyd.com/en/online-business/tracing/tracing-by-container.html?container={0}', NULL, 'Container Information', 'Container for given Container Number not found', 'represent actual data'),
(2, 'http://www.zim.com/pages/findcontainer.aspx?searchvalue1={0}', NULL, 'Success line', 'The Information you requested may not be available', 'Success'),
(3, 'http://www.yangming.com/e-service/Track_Trace/mul_ctnr.aspx?str={0},&rdolType=CT', NULL, '<td>Container No.</td>', 'we can\'t identify', '</tbody>'),
(4, 'http://uasconline.uasc.net/TrackingResults?Sequence={0}&itype=iTrack&callType=NoAjax&firstcall_track=no', NULL, '<table width=\"100%\" border=\"0\" cellpadding=\"3\" cellspacing=\"1\" class=\"bdr_lgold\" id=\"table-trackSummary\">', 'Following numbers are invalid', '<td align=\"center\" bgcolor=\"#FFFFFF\">'),
(5, 'http://moc.oocl.com/party/cargotracking/ct_search_from_other_domain.jsf?ANONYMOUS_BEHAVIOR=BUILD_UP&domainName=PARTY_DOMAIN&ENTRY_TYPE=OOCL&ENTRY=MCC&ctSearchType=CNTR&ctShipmentNumber={0}', NULL, '<table id=\"summaryTable\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"dataTable\">', 'todo', '</table></td>'),
(6, 'https://my.maerskline.com/tracking/search?searchNumber={0}', NULL, 'success', 'todo', NULL),
(7, 'http://www.cma-cgm.com/ebusiness/tracking/search?SearchBy=Container&Reference={0}', NULL, '<table class=\"data-table\" id=\"container-moves\">', 'No Results', '</table>'),
(8, 'http://homeport.apl.com/gentrack/trackingMain.do?trackInput01={0}', NULL, 'Container Number', 'todo', 'Clearances'),
(9, 'http://www.anl.com.au/ebusiness/tracking/search?SearchBy=Container&Reference={0}', NULL, '<table class=\"data-table\" id=\"container-moves\">', 'No Results', '</table>');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_user`
--

DROP TABLE IF EXISTS `tracking_user`;
CREATE TABLE IF NOT EXISTS `tracking_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `active` char(1) DEFAULT 'Y',
  `last_updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `locale` varchar(10) NOT NULL DEFAULT 'en_us',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_user`
--

INSERT INTO `tracking_user` (`id`, `email`, `first_name`, `last_name`, `active`, `last_updated`, `password`, `phone`, `locale`) VALUES
(1, 'WILLY2005@GMAIL.COM', 'WILLY2005@GMAIL.COM', 'WILLY2005@GMAIL.COM', 'Y', '2017-03-28 18:19:44', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', '+380938513201', 'en_us'),
(2, 'test@example.com', 'Test', 'Test', 'N', '2017-03-28 18:19:54', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', '+380935563311', 'en_US'),
(3, 'TEST3@EXAMPLE.COM', 'test3', 'test3', 'N', '2017-03-28 18:20:22', '70c89eebbe7d46d4eb807438f5e5fae6c56b01b45620f9ee5939c61c275e9d4f', '+380565333222', 'en_US'),
(4, 'FIRST@EXAMPLE.COM', 'First', 'Last', 'N', '2017-03-28 18:20:24', 'd6e3a8f3a4367b032963b07ff374c8f018259c0cc3584ae40ac4fb7f6ffcf6da', '+306639394833', 'en_US'),
(6, 'OMSD@LIST.RU', 'Влад', 'Олесюк', 'Y', '2017-06-07 20:09:47', '3b612c75a7b5048a435fb6ec81e52ff92d6d795a8b5a9c17070f6a63c97a53b2', '+380675573108', 'en_US'),
(7, 'OMIHAZ@GMAIL.COM', 'Vlad', 'Shvaydetsky', 'Y', '2017-04-18 14:56:07', 'a0ec06301bf1814970a70f89d1d373afdff9a36d1ba6675fc02f8a975f4efaeb', '+380936995666', 'en_us');

-- --------------------------------------------------------

--
-- Структура таблицы `tracking_user_role`
--

DROP TABLE IF EXISTS `tracking_user_role`;
CREATE TABLE IF NOT EXISTS `tracking_user_role` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `role_id` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role_role_idx` (`role_id`),
  KEY `fk_user_role_user_idx` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `tracking_user_role`
--

INSERT INTO `tracking_user_role` (`id`, `user_id`, `role_id`) VALUES
(1, 1, 1),
(3, 2, 2),
(5, 3, 8),
(6, 3, 1),
(7, 4, 8),
(8, 4, 2),
(9, 4, 1),
(13, 6, 1),
(14, 7, 1);

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `authenticated_user`
--
ALTER TABLE `authenticated_user`
  ADD CONSTRAINT `fk_auth_user` FOREIGN KEY (`user_id`) REFERENCES `tracking_user` (`id`);

--
-- Ограничения внешнего ключа таблицы `container_location`
--
ALTER TABLE `container_location`
  ADD CONSTRAINT `container_location_fk` FOREIGN KEY (`detail`) REFERENCES `order_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `job_status_info`
--
ALTER TABLE `job_status_info`
  ADD CONSTRAINT `FK_hvhw4d5opit5sdu09s7axsx8q` FOREIGN KEY (`enabled_by`) REFERENCES `tracking_user` (`id`);

--
-- Ограничения внешнего ключа таблицы `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `transaction_details_container_fk` FOREIGN KEY (`container_type`) REFERENCES `container_type` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `transaction_details_driver_fk` FOREIGN KEY (`driver`) REFERENCES `driver` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `transaction_details_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tracking_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `order_workflow`
--
ALTER TABLE `order_workflow`
  ADD CONSTRAINT `transaction_workflow_fk` FOREIGN KEY (`tracking_order`) REFERENCES `tracking_order` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Ограничения внешнего ключа таблицы `tracking_order`
--
ALTER TABLE `tracking_order`
  ADD CONSTRAINT `transaction_client_fk` FOREIGN KEY (`client`) REFERENCES `tracking_user` (`id`),
  ADD CONSTRAINT `transaction_line_fk` FOREIGN KEY (`tracking_line`) REFERENCES `tracking_line` (`id`),
  ADD CONSTRAINT `transaction_terminal_fk` FOREIGN KEY (`terminal`) REFERENCES `terminal` (`id`);

--
-- Ограничения внешнего ключа таблицы `tracking_user_role`
--
ALTER TABLE `tracking_user_role`
  ADD CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `tracking_role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `tracking_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;