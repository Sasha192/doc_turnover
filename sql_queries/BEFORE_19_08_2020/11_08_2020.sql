DROP VIEW IF EXISTS brief_task_json_view;
CREATE VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       perf.name as perf_name, perf_depo.department_name as perf_department,
       cstatus.name as status_name, t.task_owner_id as owner_id, tp.performer_id as perf_id,
       t.modification_date as modification_date, owner.img_path as owner_img_path, perf.img_path as perf_img_path,
       owner.department_id as owner_department_id, perf.department_id as performer_department_id
FROM tasks t
         INNER JOIN performers owner ON owner.id=t.task_owner_id
         INNER JOIN tasks_performers tp ON tp.task_id=t.id
         INNER JOIN performers perf ON tp.performer_id=perf.id
         INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
         INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id
         INNER JOIN departments perf_depo ON perf_depo.id=perf.department_id;

DROP VIEW IF EXISTS brief_task_json_view;
CREATE VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       perf.name as perf_name, perf_depo.department_name as perf_department,
       cstatus.name as status_name, t.task_owner_id as owner_id, tp.performer_id as perf_id,
       t.modification_date as modification_date, owner.img_path as owner_img_path, perf.img_path as perf_img_path,
       owner.department_id as owner_department_id, perf.department_id as performer_department_id, t.report_id as report_id
FROM tasks t
         INNER JOIN performers owner ON owner.id=t.task_owner_id
         INNER JOIN tasks_performers tp ON tp.task_id=t.id
         INNER JOIN performers perf ON tp.performer_id=perf.id
         INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
         INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id
         INNER JOIN departments perf_depo ON perf_depo.id=perf.department_id;



        ##############################

        alter table app_event
	add constraint app_event_tasks_id_fk
		foreign key (task_id) references tasks (id);

DROP VIEW IF EXISTS brief_task_json_view;
CREATE VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       cstatus.name as status_name, t.task_owner_id as owner_id,
       t.modification_date as modification_date, owner.img_path as owner_img_path,
       owner.department_id as owner_department_id, t.report_id as report_id
FROM tasks t
         INNER JOIN performers owner ON owner.id=t.task_owner_id
         INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
         INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id;