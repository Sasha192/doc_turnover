create table core_properties
(
	id int auto_increment,
	name varchar(512) not null,
	value varchar(2048) null,
	description varchar(1024) null,
	constraint core_properties_pk
		primary key (id)
);

create table files_brief_info
(
    id         bigint auto_increment,
    full_path  varchar(2048) not null,
    file_name  varchar(1024) not null,
    is_archive boolean       null,
    constraint files_brief_info_pk
        primary key (id)
);

create table doc_brief_events
(
    id         bigint auto_increment,
    event_date date         null,
    event_time time         null,
    user_id    int          null,
    event_type varchar(255) null,
    constraint doc_brief_events_pk
        primary key (id)
);

alter table brief_documents
    modify creation_date bigint null;

alter table brief_documents
    modify modification_date bigint null;

alter table brief_documents
    add constraint files_brief_info_doc_brief_events_id_fk
        foreign key (creation_date) references doc_brief_events (id)
            on delete cascade;

alter table brief_documents
    add constraint files_brief_info_doc_brief_events_id_fk_2
        foreign key (modification_date) references doc_brief_events (id)
            on delete cascade;



alter table doc_brief_events
    drop column event_type;

rename table doc_brief_events to doc_brief_tasks;


create table departments
(
    id                int auto_increment,
    department_name   varchar(1024) null,
    parent_department varchar(1024) null,
    constraint departments_pk
        primary key (id)
);

create table performers
(
    id            int auto_increment,
    first_name    varchar(255) null,
    last_name     varchar(255) null,
    department_id int          null,
    constraint performers_pk
        primary key (id)
);

alter table performers
    add constraint performers_departments_id_fk
        foreign key (department_id) references departments (id)
            on delete set null;


alter table brief_documents
    drop foreign key files_brief_info_doc_brief_events_id_fk;

alter table brief_documents
    add constraint files_brief_info_doc_brief_events_id_fk
        foreign key (creation_date) references tasks (id)
            on delete set null;

alter table brief_documents
    drop foreign key files_brief_info_doc_brief_events_id_fk_2;

alter table brief_documents
    add constraint files_brief_info_doc_brief_events_id_fk_2
        foreign key (modification_date) references tasks (id)
            on delete set null;

alter table brief_documents
    drop foreign key files_brief_info_doc_brief_events_id_fk;

alter table brief_documents
    drop foreign key files_brief_info_doc_brief_events_id_fk_2;

# alter table brief_documents
#     change creation_id creation_date date null;
#
# alter table brief_documents
#     change modification_id modification_date date null;

alter table brief_documents
    add is_deadline boolean null;

alter table brief_documents
    add performer_id int null;

alter table brief_documents
    modify is_deadline boolean null;

alter table brief_documents
    add constraint brief_documents_performers_id_fk
        foreign key (performer_id) references performers (id)
            on delete set null;

alter table brief_documents
    add significance int null;


create table tasks
(
    id                int auto_increment,
    performer_id      int          null,
    doc_id            int          null,
    creation_date     DATE         null,
    modification_date DATE         null,
    deadline          boolean      null,
    priority          varchar(100) null,
    constraint tasks_pk
        primary key (id)
);

alter table tasks
    add task varchar(2048) null;

alter table tasks
    change deadline is_deadline tinyint(1) null;

alter table tasks
    add deadline date null;

alter table tasks
    add control_date date null;

alter table tasks
    add status boolean null;

alter table tasks
    modify status varchar(100) null;

alter table tasks
    drop column status;

alter table tasks
    add status_id int null;

create table custom_status
(
    id          int auto_increment,
    name        varchar(255) null,
    customer_id int          null,
    is_custom   boolean      not null,
    constraint custom_status_pk
        primary key (id)
);

alter table custom_status
    change customer_id performer_id int null;

