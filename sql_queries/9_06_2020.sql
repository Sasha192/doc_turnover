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

