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

    drop trigger IF EXISTS doc_task_statistic_Trigger;
    DELIMITER $$
    create trigger doc_task_statistic_Trigger
        after insert
        on tasks_documents for each row
    begin
        update doc_statistic set counter_tasks=counter_tasks +1 where NEW.doc_id=doc_id;
        update task_statistic set counter_docs=counter_docs+1 where NEW.task_id=task_id;
    END$$
    DELIMITER ;


    DROP VIEW IF EXISTS json_view_brief_archive_doc;
    create VIEW json_view_brief_archive_doc
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

alter table task_statistics
	add constraint task_statistics_tasks_id_fk
		foreign key (task_id) references tasks (id);

alter table doc_statistics
	add constraint doc_statistics_brief_documents_id_fk
		foreign key (doc_id) references brief_documents (id);

drop procedure IF EXISTS FILTERED_BRIEF_DOC_JSON_INFO;
DELIMITER //
create procedure FILTERED_BRIEF_DOC_JSON_INFO(IN fileName VARCHAR(1024),
                                              IN extName VARCHAR(15), IN fullPath VARCHAR(2048),
                                              IN creationDate DATE, IN patterLike VARCHAR(1024),
                                              IN yearValue INT, IN monthValue INT, IN dayValue INT,
                                              IN offsetValue INT, IN pageSize INT)
BEGIN
    SELECT * FROM json_view_brief_archive_doc bd WHERE
            bd.id > offsetValue and
        (fileName is null or file_name = fileName)  and
        (extName is null or ext_name = extName)  and
        (creationDate is null or creation_date = creationDate)  and
        (patterLike is null or file_name LIKE patterLike or ext_name LIKE patterLike)  and
        (yearValue is null or YEAR(creation_date)=yearValue)  and
        (monthValue is null or (MONTH(creation_date) + 1) = monthValue)  and
        (dayValue is null or DAY(creation_date) = dayValue)
    ORDER BY bd.id LIMIT pageSize;
END //
DELIMITER ;

alter table core_properties modify decimal_value float null;
alter table custom_status modify performer_id bigint null;


    drop procedure IF EXISTS FILTERED_BRIEF_DOC_INFO;
    DELIMITER //
    create procedure FILTERED_BRIEF_DOC_INFO(IN fileName VARCHAR(1024),
                                             IN extName VARCHAR(15), IN fullPath VARCHAR(2048),
                                             IN creationDate DATE, IN patterLike VARCHAR(1024),
                                             IN yearValue INT, IN monthValue INT, IN dayValue INT,
                                             IN offsetValue INT, IN pageSize INT)
    BEGIN
        SELECT * FROM brief_documents bd WHERE
                bd.id > offsetValue and
            (fileName is null or file_name = fileName)  and
            (extName is null or ext_name = extName)  and
            (fullPath is null or full_path = fullPath)  and
            (creationDate is null or creation_date = creationDate)  and
            (patterLike is null or file_name LIKE patterLike or ext_name LIKE patterLike)  and
            (yearValue is null or YEAR(creation_date)=yearValue)  and
            (monthValue is null or (MONTH(creation_date)) = monthValue)  and
            (dayValue is null or DAY(creation_date) = dayValue)
        ORDER BY bd.id desc LIMIT pageSize;
    END //
    DELIMITER ;

    drop procedure IF EXISTS FILTERED_BRIEF_DOC_JSON_INFO;
    DELIMITER //
    create procedure FILTERED_BRIEF_DOC_JSON_INFO(IN fileName VARCHAR(1024),
                                                  IN extName VARCHAR(15), IN fullPath VARCHAR(2048),
                                                  IN creationDate DATE, IN patterLike VARCHAR(1024),
                                                  IN yearValue INT, IN monthValue INT, IN dayValue INT,
                                                  IN offsetValue INT, IN pageSize INT)
    BEGIN
        SELECT * FROM json_view_brief_archive_doc bd WHERE
                bd.id > offsetValue and
            (fileName is null or file_name = fileName)  and
            (extName is null or ext_name = extName)  and
            (creationDate is null or creation_date = creationDate)  and
            (patterLike is null or file_name LIKE patterLike or ext_name LIKE patterLike)  and
            (yearValue is null or YEAR(creation_date)=yearValue)  and
            (monthValue is null or (MONTH(creation_date)) = monthValue)  and
            (dayValue is null or DAY(creation_date) = dayValue)
        ORDER BY bd.id desc LIMIT pageSize;
    END //
    DELIMITER ;

    create table tasks_keys
    (
        task_id bigint not null,
        `key` varchar(255) not null
    );


    alter table tasks
        add task_owner_id bigint not null;

    alter table tasks
        add constraint tasks_performers_id_fk
            foreign key (task_owner_id) references performers (id);

    alter table tasks
	add description varchar(2048) null;

    alter table core_properties modify description varchar(128) null;

    alter table core_properties modify type_value int not null;

    alter table tasks_keys
	add constraint tasks_keys_tasks_id_fk
		foreign key (task_id) references tasks (id);

    drop trigger IF EXISTS doc_task_statistic_Trigger;
    DELIMITER $$
    create trigger doc_task_statistic_Trigger
        after insert
        on tasks_documents for each row
    begin
        insert into doc_statistics (doc_id, task_counter) values (NEW.doc_id, 0) ON DUPLICATE KEY update
            task_counter=task_counter+1;
        insert into task_statistics (task_id, doc_counter) values (NEW.task_id, 0) ON DUPLICATE KEY update
            doc_counter=doc_counter+1;
    END$$
    DELIMITER ;

    create table users
    (
        id bigint auto_increment,
        username varchar(255) not null,
        password varchar(1024) not null,
        performer_id bigint null,
        constraint users_pk
            primary key (id),
        constraint users_performers_id_fk
            foreign key (performer_id) references performers (id)
    );

    alter table users
	add enable boolean default true not null;
	create unique index users_username_uindex
	on users (username);

    create table users_roles (
        user_id bigint not null,
        role int not null,
        constraint users_roles_users_id_fk
            foreign key (user_id) references users (id)
    );

    alter table users change username email varchar(255) not null;

    DROP VIEW IF EXISTS brief_task_json_view;
create VIEW brief_task_json_view
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
create VIEW json_view_brief_archive_doc
AS
SELECT bd.id, bd.creation_date, bd.file_name, bd.ext_name, ds.task_counter as task_count,
       bd.performer_id, perf.department_id AS department_id, perf.name as performer_name,
       dep.department_name as department_name
FROM brief_documents bd
         INNER JOIN doc_statistics ds ON ds.doc_id=bd.id
         INNER JOIN performers perf ON perf.id=bd.performer_id
         INNER JOIN departments dep ON dep.id=perf.department_id;
