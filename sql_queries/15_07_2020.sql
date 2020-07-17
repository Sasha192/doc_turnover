DROP VIEW IF EXISTS brief_task_json_view;
CREATE VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       perf.name as perf_name, perf_depo.department_name as perf_department,
       cstatus.name as status_name, t.task_owner_id as owner_id, tp.performer_id as perf_id,
       t.modification_date as modification_date
FROM tasks t
INNER JOIN performers owner ON owner.id=t.task_owner_id
INNER JOIN tasks_performers tp ON tp.task_id=t.id
INNER JOIN performers perf ON tp.performer_id=perf.id
INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id
INNER JOIN departments perf_depo ON perf_depo.id=perf.department_id;

SELECT * FROM brief_task_json_view;

create table verification_code_table
(
	id bigint not null,
	code varchar(1024) not null
);

create unique index verification_code_table_id_uindex
	on verification_code_table (id);

alter table verification_code_table
	add creation_time bigint not null;

DROP VIEW IF EXISTS json_view_brief_archive_doc;
CREATE VIEW json_view_brief_archive_doc
AS
SELECT bd.id, bd.creation_date, bd.file_name, bd.ext_name, ds.task_counter as task_count,
       bd.performer_id, perf.department_id AS department_id, perf.name as performer_name,
       dep.department_name as department_name
FROM brief_documents bd
         INNER JOIN doc_statistics ds ON ds.doc_id=bd.id
         INNER JOIN performers perf ON perf.id=bd.performer_id
         INNER JOIN departments dep ON dep.id=perf.department_id;