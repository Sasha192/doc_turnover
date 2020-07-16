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



