
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

create table comment_post
(
	id bigint auto_increment,
	performer_id bigint not null,
	comment varchar(4096) not null,
	date date not null,
	time TIME not null,
	constraint comment_post_pk
		primary key (id)
);

drop trigger IF EXISTS comment_post_on_null_date_time;
DELIMITER $$
create trigger comment_post_on_null_date_time
    before insert
    on comment_post for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END$$
DELIMITER ;

alter table comment_post
	add comment_type varchar(16) not null;

alter table comment_post drop column comment_type;

create table report_comment
(
	report_id bigint not null,
	comment_id bigint not null,
	constraint report_comment_comment_post_id_fk
		foreign key (comment_id) references comment_post (id)
);

create table task_comment
(
    comment_id bigint not null,
    task_id    bigint not null
);

alter table task_comment
	add constraint task_comment_comment_post_id_fk
		foreign key (comment_id) references comment_post (id);

alter table task_comment
	add constraint task_comment_tasks_id_fk
		foreign key (task_id) references tasks (id);

create table reports
(
	id bigint auto_increment,
	date bigint not null,
	time bigint not null,
	task_id bigint not null,
	constraint reports_pk
		primary key (id),
	constraint reports_tasks_id_fk
		foreign key (task_id) references tasks (id)
);

create table reports_docs
(
	report_id bigint not null,
	doc_id bigint not null,
	constraint reports_docs_brief_documents_id_fk
		foreign key (doc_id) references brief_documents (id),
	constraint reports_docs_reports_id_fk
		foreign key (report_id) references reports (id)
);

drop table report_comment;

drop table task_comment;

alter table comment_post
	add report_id bigint null;

alter table comment_post
	add task_id bigint null;

alter table comment_post
	add comment_type varchar(32) not null;

alter table comment_post
	add constraint comment_post_reports_id_fk
		foreign key (report_id) references reports (id);

alter table comment_post
	add constraint comment_post_tasks_id_fk
		foreign key (task_id) references tasks (id);

alter table reports modify date DATE not null;

alter table reports modify time TIME not null;

drop trigger IF EXISTS report_on_null_date_time;
DELIMITER $$
create trigger report_on_null_date_time
    before insert
    on reports for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END$$
DELIMITER ;

alter table comment_post
	add date_time DATETIME default NOW() not null;

alter table comment_post modify date_time timestamp default CURRENT_TIMESTAMP not null;

create table app_event
(
	id bigint auto_increment,
	time_stamp TIMESTAMP default CURRENT_TIMESTAMP() not null,
	date DATE not null,
	time TIME not null,
	event_type_enum int not null,
	event_type varchar(16) not null,
	constraint app_event_pk
		primary key (id)
);

alter table app_event
	add performer_id bigint null comment 'origin of event could be not a performer';

alter table app_event
	add comment_id bigint null;

alter table app_event
	add report_id bigint null;

alter table app_event
	add doc_id bigint null;

alter table app_event
	add constraint app_event_brief_documents_id_fk
		foreign key (doc_id) references brief_documents (id);

alter table app_event
	add constraint app_event_comment_post_id_fk
		foreign key (comment_id) references comment_post (id);

alter table app_event
	add constraint app_event_performers_id_fk
		foreign key (performer_id) references performers (id);

alter table app_event
	add constraint app_event_reports_id_fk
		foreign key (report_id) references reports (id);

drop trigger IF EXISTS app_event_trigger_on_timestamp_and_name;
DELIMITER $$
create trigger app_event_trigger_on_timestamp_and_name
    before insert
    on app_event for each row
begin
    SET NEW.time = IFNULL(NEW.time, TIME(NOW()));
    SET NEW.date = IFNULL(NEW.date, DATE(NOW()));
END$$

DROP VIEW IF EXISTS brief_performer_json_view;
CREATE VIEW brief_performer_json_view
AS
SELECT perf.id, perf.img_path, perf.name, d.department_name as department_name
FROM performers perf
INNER JOIN departments d on perf.department_id = d.id;

alter table reports drop foreign key reports_tasks_id_fk;

drop index reports_tasks_id_fk on reports;

alter table reports drop column task_id;

alter table tasks
	add report_id bigint null;

alter table tasks
	add constraint tasks_reports_id_fk
		foreign key (report_id) references reports (id);

DROP VIEW IF EXISTS brief_task_json_view;
create VIEW brief_task_json_view
AS
SELECT t.id as id, creation_date, is_deadline, priority, task, deadline, control_date, t.description,
       owner.name as owner_name, owner_depo.department_name as owner_department,
       perf.name as perf_name, perf_depo.department_name as perf_department,
       cstatus.name as status_name, t.task_owner_id as owner_id, tp.performer_id as perf_id,
       t.modification_date as modification_date, t.report_id as report_id
FROM tasks t
         INNER JOIN performers owner ON owner.id=t.task_owner_id
         INNER JOIN tasks_performers tp ON tp.task_id=t.id
         INNER JOIN performers perf ON tp.performer_id=perf.id
         INNER JOIN custom_status cstatus ON cstatus.id = t.status_id
         INNER JOIN departments owner_depo ON owner_depo.id=owner.department_id
         INNER JOIN departments perf_depo ON perf_depo.id=perf.department_id;