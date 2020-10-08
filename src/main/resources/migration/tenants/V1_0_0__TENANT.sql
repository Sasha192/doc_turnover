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
  PRIMARY KEY (`id`),
  KEY `app_event_brief_documents_id_fk` (`doc_id`),
  KEY `app_event_comment_post_id_fk` (`comment_id`),
  KEY `app_event_performers_id_fk` (`performer_id`),
  KEY `app_event_reports_id_fk` (`report_id`),
  CONSTRAINT `app_event_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`),
  CONSTRAINT `app_event_comment_post_id_fk` FOREIGN KEY (`comment_id`) REFERENCES `comment_post` (`id`),
  CONSTRAINT `app_event_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE SET NULL,
  CONSTRAINT `app_event_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER DATABASE `bcrew` CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS app_event_trigger_on_timestamp_and_name */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger app_event_trigger_on_timestamp_and_name
    before insert
    on app_event for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_general_ci ;

--
-- Table structure for table `archive`
--

DROP TABLE IF EXISTS `archive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `archive` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `archive_path` varchar(1024) NOT NULL,
  `creation_date` date NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brief_documents`
--

DROP TABLE IF EXISTS `brief_documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brief_documents` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_path` varchar(2048) NOT NULL,
  `file_name` varchar(1024) NOT NULL,
  `is_archive` tinyint(1) NOT NULL DEFAULT '0',
  `creation_date` date NOT NULL,
  `ext_name` varchar(15) NOT NULL,
  `performer_id` bigint(20) DEFAULT NULL,
  `removed` tinyint(1) NOT NULL DEFAULT '0',
  `archive_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `brief_documents_archive_id_fk` (`archive_id`),
  KEY `brief_documents_performers_id_fk` (`performer_id`),
  CONSTRAINT `brief_documents_archive_id_fk` FOREIGN KEY (`archive_id`) REFERENCES `archive` (`id`),
  CONSTRAINT `brief_documents_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_bin ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS doc_statistics_Trigger */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 TRIGGER doc_statistics_Trigger
    AFTER INSERT
    ON brief_documents FOR EACH ROW
BEGIN
    insert into doc_statistics (doc_id, task_counter) VALUES (NEW.id, 0) ON DUPLICATE KEY UPDATE
        task_counter=task_counter+1;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_general_ci ;

--
-- Table structure for table `brief_performer`
--

DROP TABLE IF EXISTS `brief_performer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brief_performer` (
  `id` bigint(20) NOT NULL,
  `img_path` varchar(512) NOT NULL DEFAULT '/img/avatars/6.jpg',
  `name` varchar(2048) NOT NULL,
  `department_name` varchar(512) DEFAULT NULL,
  `email` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `brief_performer_id_uindex` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `brief_task_json_view`
--

DROP TABLE IF EXISTS `brief_task_json_view`;
/*!50001 DROP VIEW IF EXISTS `brief_task_json_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `brief_task_json_view` AS SELECT 
 1 AS `id`,
 1 AS `creation_date`,
 1 AS `is_deadline`,
 1 AS `priority`,
 1 AS `task`,
 1 AS `deadline`,
 1 AS `control_date`,
 1 AS `description`,
 1 AS `owner_name`,
 1 AS `owner_department`,
 1 AS `status_name`,
 1 AS `owner_id`,
 1 AS `modification_date`,
 1 AS `owner_img_path`,
 1 AS `owner_department_id`,
 1 AS `report_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `cal_perf_statistics`
--

DROP TABLE IF EXISTS `cal_perf_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cal_perf_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cal_perf_stat_type` varchar(128) NOT NULL,
  `performer_id` bigint(20) NOT NULL,
  `creation_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `expired` tinyint(1) DEFAULT NULL,
  `amount` int(11) NOT NULL,
  `completed` int(11) NOT NULL,
  `expired_deadline` int(11) DEFAULT NULL,
  `inprogress` int(11) NOT NULL,
  `onhold` int(11) NOT NULL,
  `calendar_enum_type` int(11) NOT NULL,
  `overdue` int(11) NOT NULL,
  `new_status` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cal_perf_statistics_performers_id_fk` (`performer_id`),
  CONSTRAINT `cal_perf_statistics_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
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
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS comment_post_on_null_date_time */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger comment_post_on_null_date_time
    before insert
    on comment_post for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `custom_status`
--

DROP TABLE IF EXISTS `custom_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `custom_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `performer_id` bigint(20) DEFAULT NULL,
  `is_custom` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `custom_status_performers_id_fk` (`performer_id`),
  CONSTRAINT `custom_status_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `doc_statistics`
--

DROP TABLE IF EXISTS `doc_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doc_statistics` (
  `doc_id` bigint(20) NOT NULL,
  `task_counter` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`doc_id`),
  UNIQUE KEY `doc_statistics_doc_id_uindex` (`doc_id`),
  CONSTRAINT `doc_statistics_brief_documents_id_fk` FOREIGN KEY (`doc_id`) REFERENCES `brief_documents` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `json_view_brief_archive_doc`
--

DROP TABLE IF EXISTS `json_view_brief_archive_doc`;
/*!50001 DROP VIEW IF EXISTS `json_view_brief_archive_doc`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `json_view_brief_archive_doc` AS SELECT 
 1 AS `id`,
 1 AS `creation_date`,
 1 AS `file_name`,
 1 AS `ext_name`,
 1 AS `task_count`,
 1 AS `performer_id`,
 1 AS `department_id`,
 1 AS `performer_name`,
 1 AS `department_name`*/;
SET character_set_client = @saved_cs_client;

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
  `img_path` varchar(512) NOT NULL DEFAULT '/img/avatars/6.jpg',
  `removed` tinyint(1) NOT NULL DEFAULT '0',
  `img_token` varchar(128) NOT NULL,
  `role` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `performers_departments_id_fk` (`department_id`),
  CONSTRAINT `performers_departments_id_fk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS brief_performer_trigger_insert */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger brief_performer_trigger_insert
    after insert
    on performers for each row
begin
    DECLARE department_name_var VARCHAR(512) DEFAULT NULL;
    IF New.department_id is NOT NULL THEN
        SET department_name_var = (SELECT department_name FROM departments d WHERE d.id=New.department_id);
        INSERT brief_performer (id, img_path, name, department_name) VALUES
        (New.id, New.img_path, New.name, department_name_var);
    ELSE
        INSERT brief_performer (id, img_path, name, department_name) VALUES
        (New.id, New.img_path, New.name, department_name_var);
    end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS brief_performer_trigger_update */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger brief_performer_trigger_update
    after update
    on performers for each row
begin
    DECLARE department_name_var VARCHAR(512) DEFAULT NULL;
    IF New.id <> OLD.id THEN
        UPDATE brief_performer bp SET bp.id=New.id WHERE bp.id=OLD.id;
    end if;
    IF New.department_id is NOT NULL THEN
        SET department_name_var = (SELECT department_name FROM departments d WHERE d.id=New.department_id);
        UPDATE brief_performer bp
        SET bp.name=New.name,
            bp.department_name=department_name_var,
            bp.img_path = New.img_path
        WHERE bp.id=New.id;
    ELSE
        UPDATE brief_performer bp
        SET bp.name=New.name,
            bp.department_name=null,
            bp.img_path = New.img_path
        WHERE bp.id=New.id;
    end if;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS performer_trigger_remove */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 trigger performer_trigger_remove
    after delete
    on performers for each row
begin
    DELETE FROM brief_performer WHERE id=OLD.id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
-- Table structure for table `task_statistics`
--

DROP TABLE IF EXISTS `task_statistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_statistics` (
  `task_id` bigint(20) NOT NULL,
  `doc_counter` int(11) NOT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `task_statistics_task_id_uindex` (`task_id`),
  CONSTRAINT `task_statistics_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
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
  `creation_date` date NOT NULL,
  `is_deadline` tinyint(1) NOT NULL,
  `priority` varchar(100) NOT NULL,
  `task` varchar(4096) NOT NULL,
  `deadline` date NOT NULL,
  `control_date` date NOT NULL,
  `status_id` bigint(20) NOT NULL,
  `task_owner_id` bigint(20) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `modification_date` date DEFAULT NULL,
  `report_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tasks_custom_status_id_fk` (`status_id`),
  KEY `tasks_performers_id_fk` (`task_owner_id`),
  KEY `tasks_reports_id_fk` (`report_id`),
  CONSTRAINT `tasks_custom_status_id_fk` FOREIGN KEY (`status_id`) REFERENCES `custom_status` (`id`),
  CONSTRAINT `tasks_performers_id_fk` FOREIGN KEY (`task_owner_id`) REFERENCES `performers` (`id`) ON DELETE SET NULL,
  CONSTRAINT `tasks_reports_id_fk` FOREIGN KEY (`report_id`) REFERENCES `reports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_bin ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS task_statistics_Trigger */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 TRIGGER task_statistics_Trigger
    AFTER INSERT
    ON tasks FOR EACH ROW
BEGIN
    insert into task_statistics (task_id, doc_counter) VALUES (NEW.id, 0) ON DUPLICATE KEY UPDATE
        doc_counter=doc_counter+1;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_general_ci ;

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
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_bin ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS doc_task_statistic_Trigger */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bcrew_user`@`%`*/ /*!50003 TRIGGER doc_task_statistic_Trigger
    AFTER INSERT
    ON tasks_documents FOR EACH ROW
BEGIN
    insert into doc_statistics (doc_id, task_counter) VALUES (NEW.doc_id, 1) ON DUPLICATE KEY UPDATE
        task_counter=task_counter+1;
    insert into task_statistics (task_id, doc_counter) VALUES (NEW.task_id, 1) ON DUPLICATE KEY UPDATE
        doc_counter=doc_counter+1;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `bcrew` CHARACTER SET utf8 COLLATE utf8_general_ci ;

--
-- Table structure for table `tasks_keys`
--

DROP TABLE IF EXISTS `tasks_keys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks_keys` (
  `task_id` bigint(20) NOT NULL,
  `key` varchar(255) NOT NULL,
  KEY `tasks_keys_tasks_id_fk` (`task_id`),
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
  KEY `tasks_performers_performers_id_fk` (`performer_id`),
  KEY `tasks_performers_tasks_id_fk` (`task_id`),
  CONSTRAINT `tasks_performers_performers_id_fk` FOREIGN KEY (`performer_id`) REFERENCES `performers` (`id`) ON DELETE CASCADE,
  CONSTRAINT `tasks_performers_tasks_id_fk` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `brief_task_json_view`
--

/*!50001 DROP VIEW IF EXISTS `brief_task_json_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`bcrew_user`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `brief_task_json_view` AS select `t`.`id` AS `id`,`t`.`creation_date` AS `creation_date`,`t`.`is_deadline` AS `is_deadline`,`t`.`priority` AS `priority`,`t`.`task` AS `task`,`t`.`deadline` AS `deadline`,`t`.`control_date` AS `control_date`,`t`.`description` AS `description`,`owner`.`name` AS `owner_name`,`owner_depo`.`department_name` AS `owner_department`,`cstatus`.`name` AS `status_name`,`t`.`task_owner_id` AS `owner_id`,`t`.`modification_date` AS `modification_date`,`owner`.`img_path` AS `owner_img_path`,`owner`.`department_id` AS `owner_department_id`,`t`.`report_id` AS `report_id` from (((`tasks` `t` join `performers` `owner` on((`owner`.`id` = `t`.`task_owner_id`))) join `custom_status` `cstatus` on((`cstatus`.`id` = `t`.`status_id`))) join `departments` `owner_depo` on((`owner_depo`.`id` = `owner`.`department_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `json_view_brief_archive_doc`
--

/*!50001 DROP VIEW IF EXISTS `json_view_brief_archive_doc`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`bcrew_user`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `json_view_brief_archive_doc` AS select `bd`.`id` AS `id`,`bd`.`creation_date` AS `creation_date`,`bd`.`file_name` AS `file_name`,`bd`.`ext_name` AS `ext_name`,`ds`.`task_counter` AS `task_count`,`bd`.`performer_id` AS `performer_id`,`perf`.`department_id` AS `department_id`,`perf`.`name` AS `performer_name`,`dep`.`department_name` AS `department_name` from (((`brief_documents` `bd` join `doc_statistics` `ds` on((`ds`.`doc_id` = `bd`.`id`))) join `performers` `perf` on((`perf`.`id` = `bd`.`performer_id`))) join `departments` `dep` on((`dep`.`id` = `perf`.`department_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-10-07 17:21:14
