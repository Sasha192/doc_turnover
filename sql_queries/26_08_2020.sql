alter table performers_events
	add constraint performers_events_performers_id_fk
		foreign key (perf_id) references performers (id)
			on delete cascade;

alter table users drop foreign key users_performers_id_fk;

alter table users
	add constraint users_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table tasks_performers drop foreign key tasks_performers_performers_id_fk;

alter table tasks_performers
	add constraint tasks_performers_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table performer_user_roles drop foreign key performer_user_roles_performers_id_fk;

alter table performer_user_roles
	add constraint performer_user_roles_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table performers drop column active;

alter table performers
	add removed boolean default true not null;

alter table performers alter column removed set default 0;

alter table app_event drop foreign key app_event_performers_id_fk;

alter table app_event
	add constraint app_event_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete set null;

alter table brief_documents modify performer_id bigint null;

alter table brief_documents drop foreign key brief_documents_performers_id_fk;

alter table brief_documents
	add constraint brief_documents_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete set null;

alter table custom_status drop foreign key custom_status_performers_id_fk;

alter table custom_status
	add constraint custom_status_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table tasks modify task_owner_id bigint null;

alter table tasks drop foreign key tasks_performers_id_fk;

alter table tasks
	add constraint tasks_performers_id_fk
		foreign key (task_owner_id) references performers (id)
			on delete set null;

alter table performers drop column removed;


drop trigger IF EXISTS performer_trigger_remove;
DELIMITER $$
create trigger performer_trigger_remove
    after delete
    on performers for each row
begin
    DELETE FROM brief_performer WHERE id=OLD.id;
END$$

alter table rem_me_tokens drop foreign key rem_me_tokens_users_id_fk;

alter table rem_me_tokens
	add constraint rem_me_tokens_users_id_fk
		foreign key (user_id) references users (id)
			on delete cascade;

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

alter table rem_me_tokens
	add token_ip varchar(64) not null;

alter table performers
	add img_token varchar(128) not null;

