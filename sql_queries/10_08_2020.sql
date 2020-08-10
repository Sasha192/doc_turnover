alter table departments drop column parent_department;

alter table departments
	add parent_department_id bigint null;

alter table performers_events
	add seen boolean default false not null;

alter table performers_events
	add constraint performers_events_pk
		primary key (perf_id, event_id);

alter table app_event
	add task_id bigint null;

