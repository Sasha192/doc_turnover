create table brief_documents
(
	id bigint auto_increment,
	full_path varchar(2048) null,
	file_name varchar(1024) null,
	is_archive boolean null,
	creation_date date null,
	ext_name varchar(15) null,
	constraint brief_documents_pk
		primary key (id)
);

create table custom_status
(
	id int auto_increment,
	name varchar(255) null,
	performer_id bigint null,
	is_custom boolean null,
	constraint custom_status_pk
		primary key (id)
);

create table departments
(
	id int auto_increment,
	department_name varchar(1024) null,
	parent_department varchar(1024) null,
	constraint departments_pk
		primary key (id)
);

create table performers
(
	id int auto_increment,
	department_id int null,
	name varchar(2048) null,
	constraint performers_pk
		primary key (id)
);

create table tasks
(
	id bigint auto_increment,
	performer_id int null,
	doc_id int null,
	creation_date date null,
	is_deadline int null,
	priority varchar(100) null,
	task varchar(4096) null,
	deadline date null,
	control_date date null,
	status_id int null,
	constraint tasks_pk
		primary key (id)
);

alter table brief_documents
	add performer_id bigint null;

alter table performers modify id bigint auto_increment;

alter table performers
	add constraint performers_departments_id_fk
		foreign key (department_id) references departments (id);

alter table custom_status
	add constraint custom_status_performers_id_fk
		foreign key (performer_id) references performers (id);

alter table brief_documents
	add constraint brief_documents_performers_id_fk
		foreign key (performer_id) references performers (id);


alter table tasks modify performer_id bigint null;

alter table tasks modify doc_id bigint null;

alter table tasks modify is_deadline boolean null;

alter table tasks
	add constraint tasks_brief_documents_id_fk
		foreign key (doc_id) references brief_documents (id);

alter table tasks
	add constraint tasks_custom_status_id_fk
		foreign key (status_id) references custom_status (id);

alter table tasks
	add constraint tasks_performers_id_fk
		foreign key (performer_id) references performers (id);




