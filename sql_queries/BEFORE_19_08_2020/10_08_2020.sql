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

alter table users modify email varchar(255) null;

alter table users modify password varchar(1024) null;

alter table performers alter column img_path set default '/img/avatars/6.jpg';

drop index users_username_uindex on users;

alter table users modify email varchar(255) default '' not null;

alter table users modify password varchar(1024) default '' not null;



create unique index users_email_uindex
	on users (email);

create unique index users_password_uindex
	on users (password);

alter table brief_performer alter column img_path set default '/img/avatars/6.jpg';

alter table brief_performer drop column email;

alter table users alter column email drop default;

alter table users alter column password drop default;

