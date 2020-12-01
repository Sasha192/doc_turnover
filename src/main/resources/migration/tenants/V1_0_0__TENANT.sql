-- MySQL dump 10.13  Distrib 5.7.31, for Linux (x86_64)
--
-- Host: 192.168.31.76    Database: bcrew
-- ------------------------------------------------------
-- Server version	5.7.31-0ubuntu0.16.04.1

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
-- Table structure for table `app_event`
--

DROP TABLE IF EXISTS `app_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `event_type_enum` int(11) NOT NULL,
  `event_type` varchar(16) NOT NULL,
  `performer_id` bigint(20) DEFAULT NULL COMMENT 'origin of event could be not a performer',
  `comment_id` bigint(20) DEFAULT NULL,
  `report_id` bigint(20) DEFAULT NULL,
  `doc_id` bigint(20) DEFAULT NULL,
  `task_id` bigint(20) DEFAULT NULL,
  `event_message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_event_brief_documents_id_fk` (`doc_id`),
  KEY `app_event_comment_post_id_fk` (`comment_id`),
  KEY `app_event_performers_id_fk` (`performer_id`),
  KEY `app_event_reports_id_fk` (`report_id`),
  KEY `app_event_tasks_id_fk` (`task_id`),
  CONSTRAINT `app_event_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`),
  CONSTRAINT `app_event_comment_post_id_fk` FOREIGN KEY (`comment_id`) REFERENCES `comment_post` (`id`),
  CONSTRAINT `app_event_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE SET NULL,
  CONSTRAINT `app_event_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`),
  CONSTRAINT `app_event_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brief_documents`
--

DROP TABLE IF EXISTS `brief_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brief_documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(256) NOT NULL,
  `ext_name` varchar(15) NOT NULL,
  `creation_date` date NOT NULL,
  `creation_time` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `performer_id` bigint(20) DEFAULT NULL,
  `signed` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `brief_documents_creation_time_index` (`creation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cal_perf_statistics`
--

DROP TABLE IF EXISTS `cal_perf_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cal_perf_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cal_perf_stat_type` varchar(128) NOT NULL,
  `calendar_enum_type` int(11) NOT NULL,
  `expiration_calendar` date NOT NULL,
  `creation_calendar` date NOT NULL,
  `performer_id` bigint(20) NOT NULL,
  `onhold` int(11) NOT NULL,
  `amount` int(11) NOT NULL,
  `overdue` int(11) NOT NULL,
  `new_status` int(11) NOT NULL,
  `inprogress` int(11) NOT NULL,
  `completed` int(11) NOT NULL,
  `expired_deadline` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cal_perf_statistics_performers_id_fk` (`performer_id`),
  CONSTRAINT `cal_perf_statistics_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment_post`
--

DROP TABLE IF EXISTS `comment_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `performer_id` bigint(20) NOT NULL,
  `comment` varchar(4096) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `report_id` bigint(20) DEFAULT NULL,
  `task_id` bigint(20) DEFAULT NULL,
  `comment_type` varchar(32) NOT NULL,
  `date_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `comment_post_reports_id_fk` (`report_id`),
  KEY `comment_post_tasks_id_fk` (`task_id`),
  CONSTRAINT `comment_post_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`),
  CONSTRAINT `comment_post_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `departments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(512) NOT NULL,
  `parent_department_id` bigint(20) DEFAULT NULL,
  `perf_counter` int(11) NOT NULL COMMENT 'NUMBER OF PERFORMERS IN DEPARTMENT\n',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `performers`
--

DROP TABLE IF EXISTS `performers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `performers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `department_id` bigint(20) DEFAULT NULL,
  `name` varchar(2048) NOT NULL,
  `role` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `img_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `performers_user_id_uindex` (`user_id`),
  KEY `performers_departments_id_fk` (`department_id`),
  CONSTRAINT `performers_departments_id_fk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `performers_events`
--

DROP TABLE IF EXISTS `performers_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `performers_events` (
  `perf_id` bigint(20) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `seen` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`perf_id`,`event_id`),
  KEY `performers_events_app_event_id_fk` (`event_id`),
  CONSTRAINT `performers_events_app_event_id_fk` FOREIGN KEY (`event_id`) REFERENCES `app_event` (`id`),
  CONSTRAINT `performers_events_performers_id_fk` FOREIGN KEY (`perf_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reports` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_general_ci ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS report_on_null_date_time */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger report_on_null_date_time
    before insert
    on reports for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bcrew` CHARACTER SET latin1 COLLATE latin1_swedish_ci ;

--
-- Table structure for table `reports_docs`
--

DROP TABLE IF EXISTS `reports_docs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reports_docs` (
  `report_id` bigint(20) NOT NULL,
  `doc_id` bigint(20) NOT NULL,
  KEY `reports_docs_brief_documents_id_fk` (`doc_id`),
  KEY `reports_docs_reports_id_fk` (`report_id`),
  CONSTRAINT `reports_docs_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`),
  CONSTRAINT `reports_docs_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation_time` bigint(20) NOT NULL,
  `is_deadline` tinyint(1) NOT NULL,
  `task` varchar(4096) NOT NULL,
  `deadline` date NOT NULL,
  `control_date` date NOT NULL,
  `status_id` int(11) NOT NULL,
  `task_owner_id` bigint(20) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `report_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tasks_performers_id_fk` (`task_owner_id`),
  KEY `tasks_reports_id_fk` (`report_id`),
  KEY `tasks_status_id_index` (`status_id`),
  KEY `tasks_control_date_status_id_index` (`control_date`,`status_id`),
  KEY `tasks_deadline_is_deadline_status_id_index` (`deadline`,`is_deadline`,`status_id`),
  CONSTRAINT `tasks_performers_id_fk` FOREIGN KEY (`task_owner_id`) REFERENCES `performers` (`id`) ON DELETE SET NULL,
  CONSTRAINT `tasks_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasks_documents`
--

DROP TABLE IF EXISTS `tasks_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks_documents` (
  `task_id` bigint(20) NOT NULL,
  `doc_id` bigint(20) NOT NULL,
  KEY `tasks_documents_brief_documents_id_fk` (`doc_id`),
  KEY `tasks_documents_tasks_id_fk` (`task_id`),
  CONSTRAINT `tasks_documents_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`),
  CONSTRAINT `tasks_documents_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasks_keys`
--

DROP TABLE IF EXISTS `tasks_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks_keys` (
  `task_id` bigint(20) NOT NULL,
  `key_word` varchar(255) NOT NULL,
  KEY `tasks_keys_tasks_id_fk` (`task_id`),
  KEY `tasks_keys_key_index` (`key_word`),
  CONSTRAINT `tasks_keys_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tasks_performers`
--

DROP TABLE IF EXISTS `tasks_performers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks_performers` (
  `task_id` bigint(20) NOT NULL,
  `performer_id` bigint(20) NOT NULL,
  UNIQUE KEY `tasks_performers_task_id_performer_id_uindex` (`task_id`,`performer_id`),
  KEY `tasks_performers_performers_id_fk` (`performer_id`),
  KEY `tasks_performers_tasks_id_fk` (`task_id`),
  CONSTRAINT `tasks_performers_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_performers_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `words_documents`
--

DROP TABLE IF EXISTS `words_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `words_documents` (
  `word` varchar(63) NOT NULL,
  `doc_id` bigint(20) NOT NULL,
  KEY `words_documents_brief_documents_id_fk` (`doc_id`),
  KEY `words_documents_word_index` (`word`),
  CONSTRAINT `words_documents_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-28 13:20:52

ALTER TABLE app_event CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE brief_documents CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE cal_perf_statistics CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE comment_post CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE departments CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
/*
ALTER TABLE flyway_schema_history CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
*/
ALTER TABLE performers CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE performers_events CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reports CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE reports_docs CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE tasks CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE tasks_documents CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE tasks_keys CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE tasks_performers CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE words_documents CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

alter table app_event change time creation_time bigint not null;

alter table comment_post modify date_time bigint not null;

alter table app_event
	add task_name varchar(255) null;


/*
create table pages_number
(
	page_id varchar(63) not null,
	page_size bigint not null
);

create unique index pages_number_page_id_uindex
	on pages_number (page_id);

alter table pages_number
	add constraint pages_number_pk
		primary key (page_id);

INSERT INTO pages_number VALUES ('task_pages', 0)
INSERT INTO pages_number VALUES ('app_event', 0)
INSERT INTO pages_number VALUES ('brief_documents', 0)
INSERT INTO pages_number VALUES ('task_pages', 0)
INSERT INTO pages_number VALUES ('task_pages', 0)*/
