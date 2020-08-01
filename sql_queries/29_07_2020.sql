alter table performers
	add img_path varchar(512) default '/img/default.jpg' not null;

DROP VIEW IF EXISTS json_view_brief_archive_doc;
CREATE VIEW json_view_brief_archive_doc
AS
SELECT bd.id, bd.creation_date, bd.file_name, bd.ext_name, ds.task_counter as task_count,
       bd.performer_id, perf.department_id AS department_id,
       dep.department_name as department_name, perf.name as performer_name
FROM brief_documents bd
         INNER JOIN doc_statistics ds ON ds.doc_id=bd.id
         INNER JOIN performers perf ON perf.id=bd.performer_id
         INNER JOIN departments dep ON dep.id=perf.department_id;

alter table brief_documents alter column is_archive set default false;

alter table brief_documents alter column removed set default false;

DROP TRIGGER IF EXISTS doc_statistics_Trigger;
DELIMITER $$
CREATE TRIGGER doc_statistics_Trigger
    AFTER INSERT
    ON brief_documents FOR EACH ROW
BEGIN
    insert into doc_statistics (doc_id, task_counter) VALUES (NEW.id, 0) ON DUPLICATE KEY UPDATE
        task_counter=task_counter+1;
END$$
DELIMITER ;

DROP TRIGGER IF EXISTS task_statistics_Trigger;
DELIMITER $$
CREATE TRIGGER task_statistics_Trigger
    AFTER INSERT
    ON tasks FOR EACH ROW
BEGIN
    insert into task_statistics (task_id, doc_counter) VALUES (NEW.id, 0) ON DUPLICATE KEY UPDATE
        doc_counter=doc_counter+1;
END$$
DELIMITER ;

SELECT CONCAT('ALTER TABLE ',TABLE_SCHEMA,'.',TABLE_NAME,' CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;') FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'bcrew';

ALTER DATABASE bcrew CHARACTER SET utf8 COLLATE utf8_general_ci;

ALTER TABLE archive CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.brief_documents CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.core_properties CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.custom_status CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.departments CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.doc_statistics CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.performers CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.task_statistics CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.tasks CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.tasks_documents CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.tasks_keys CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.tasks_performers CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.users CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.users_roles CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE bcrew.verification_code_table CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

DROP VIEW IF EXISTS brief_task_json_view;
CREATE VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       perf.name as perf_name, perf_depo.department_name as perf_department,
       cstatus.name as status_name, t.task_owner_id as owner_id, tp.performer_id as perf_id,
       t.modification_date as modification_date, owner.img_path as owner_img_path, perf.img_path as perf_img_path
FROM tasks t
         INNER JOIN performers owner ON owner.id=t.task_owner_id
         INNER JOIN tasks_performers tp ON tp.task_id=t.id
         INNER JOIN performers perf ON tp.performer_id=perf.id
         INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
         INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id
         INNER JOIN departments perf_depo ON perf_depo.id=perf.department_id;
