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




    alter table brief_documents
        add removed boolean null;

    alter table brief_documents
        add archive_id int null;

    create table archive
    (
        id int auto_increment,
        archive_path varchar(1024) not null,
        creation_date DATE not null,
        description varchar(255) not null,
        constraint archive_pk
            primary key (id)
    );

    alter table brief_documents
        add constraint brief_documents_archive_id_fk
            foreign key (archive_id) references archive (id);

    drop table tasks;

    alter table tasks modify performer_id bigint null;

    alter table tasks drop column doc_id;

    alter table tasks modify is_deadline boolean null;

    alter table tasks modify status_id bigint null;

    alter table tasks drop column performer_id;

    create table tasks_performers
    (
        task_id int not null,
        performer_id int not null
    );

    create table tasks_documents
    (
        task_id int not null,
        doc_id int not null
    );

    alter table brief_documents modify full_path varchar(2048) not null;

    alter table brief_documents modify file_name varchar(1024) not null;

    alter table brief_documents modify is_archive tinyint(1) not null;

    alter table brief_documents modify creation_date date not null;

    alter table brief_documents modify ext_name varchar(15) not null;

    alter table brief_documents modify performer_id bigint not null;

    alter table brief_documents modify removed tinyint(1) not null;

    alter table brief_documents modify archive_id int default 1 not null;

    alter table core_properties modify name varchar(128) not null;

    alter table core_properties modify value varchar(512) not null;

    alter table core_properties modify description varchar(1024) not null;

    alter table departments modify department_name varchar(512) not null;

    alter table departments modify parent_department varchar(512) not null;

    alter table brief_documents drop foreign key brief_documents_archive_id_fk;

    alter table brief_documents drop foreign key brief_documents_performers_id_fk;

    alter table brief_documents drop foreign key brief_documents_archive_id_fk;

    alter table brief_documents drop foreign key brief_documents_performers_id_fk;

    drop index brief_documents_archive_id_fk on brief_documents;

    drop index brief_documents_performers_id_fk on brief_documents;

    drop table custom_status;

    drop table performers;

    drop table departments;

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

    create table task_statistic
    (
        id int auto_increment,
        task_id int null,
        counter int null,
        constraint task_statistic_pk
            primary key (id)
    );

    create table doc_statistic
    (
        id int auto_increment,
        doc_id int null,
        counter int null,
        constraint task_statistic_pk
            primary key (id)
    );

    DROP TRIGGER IF EXISTS doc_task_statistic_Trigger;
    DELIMITER $$
    CREATE TRIGGER doc_task_statistic_Trigger
        AFTER INSERT
        ON tasks_documents FOR EACH ROW
    BEGIN
        UPDATE doc_statistic SET counter_tasks=counter_tasks +1 WHERE NEW.doc_id=doc_id;
        UPDATE task_statistic SET counter_docs=counter_docs+1 WHERE NEW.task_id=task_id;
    END$$
    DELIMITER ;


    DROP VIEW IF EXISTS json_view_brief_archive_doc;
    CREATE VIEW json_view_brief_archive_doc
        AS
        SELECT bd.id, bd.creation_date, bd.file_name, bd.ext_name, ds.task_counter as task_count,
               bd.performer_id, perf.department_id AS department_id
               FROM brief_documents bd
        INNER JOIN doc_statistics ds ON ds.doc_id=bd.id
        INNER JOIN performers perf ON perf.id=bd.performer_id;

    alter table task_statistic change counter counter_docs int null;

    drop table task_statistic;

    create table task_statistics
    (
        task_id bigint not null,
        doc_counter int default 0 not null
    );

    create unique index task_statistics_task_id_uindex
        on task_statistics (task_id);

    alter table task_statistics
        add constraint task_statistics_pk
            primary key (task_id);

    drop table doc_statistic;

    create table doc_statistics
    (
        doc_id bigint not null,
        task_counter int default 0 not null
    );

    create unique index doc_statistics_doc_id_uindex
        on doc_statistics (doc_id);

    alter table doc_statistics
        add constraint doc_statistics_pk
            primary key (doc_id);

    alter table tasks modify creation_date date not null;

    alter table tasks modify is_deadline tinyint(1) not null;

    alter table tasks modify priority varchar(100) not null;

    alter table tasks modify task varchar(4096) not null;

    alter table tasks modify deadline date not null;

    alter table tasks modify control_date date not null;

    alter table tasks modify status_id bigint not null;

    alter table performers modify department_id bigint not null;

    alter table performers modify name varchar(2048) not null;

    alter table departments modify department_name varchar(512) not null;

    alter table departments modify parent_department varchar(512) not null;

    alter table performers
        add constraint performers_departments_id_fk
            foreign key (department_id) references departments (id);

    alter table custom_status modify name varchar(255) not null;

    alter table custom_status modify performer_id bigint not null;

    alter table custom_status modify is_custom tinyint(1) not null;

    alter table custom_status
        add constraint custom_status_performers_id_fk
            foreign key (performer_id) references performers (id);

    alter table tasks_documents modify task_id bigint not null;

    alter table tasks_documents modify doc_id bigint not null;

    alter table tasks_documents
        add constraint tasks_documents_brief_documents_id_fk
            foreign key (doc_id) references brief_documents (id);

    alter table tasks_documents
        add constraint tasks_documents_tasks_id_fk
            foreign key (task_id) references tasks (id);

    alter table tasks
        add constraint tasks_custom_status_id_fk
            foreign key (status_id) references custom_status (id);

    alter table tasks_performers modify task_id bigint not null;

    alter table tasks_performers modify performer_id bigint not null;

    alter table tasks_performers
        add constraint tasks_performers_performers_id_fk
            foreign key (performer_id) references performers (id);

    alter table tasks_performers
        add constraint tasks_performers_tasks_id_fk
            foreign key (task_id) references tasks (id);