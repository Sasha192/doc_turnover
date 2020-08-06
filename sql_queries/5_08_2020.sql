create table brief_performer
(
	id bigint not null,
	img_path varchar(512) not null,
	name varchar(2048) not null,
	department_name varchar(512) null
);

create unique index brief_performer_id_uindex
	on brief_performer (id);

alter table brief_performer
	add constraint brief_performer_pk
		primary key (id);

### TRIGGERS FOR MATERIALIZED VIEW brief_performer

drop trigger IF EXISTS brief_performer_trigger_insert;
DELIMITER $$
create trigger brief_performer_trigger_insert
    after insert
    on performers for each row
begin
    DECLARE department_name_var VARCHAR(512) DEFAULT NULL;
    IF New.department_id is NOT NULL THEN
        SET department_name_var = (SELECT department_name FROM departments d WHERE d.id=New.department_id);
        INSERT brief_performer (id, img_path, name, department_name) VALUES
        (New.id, New.img_path, New.name, department_name_var);
    ELSE
        INSERT brief_performer (id, img_path, name, department_name) VALUES
        (New.id, New.img_path, New.name, department_name_var);
    end if;
END$$

drop trigger IF EXISTS brief_performer_trigger_update;
DELIMITER $$
create trigger brief_performer_trigger_update
    after update
    on performers for each row
begin
    DECLARE department_name_var VARCHAR(512) DEFAULT NULL;
    IF New.id <> OLD.id THEN
        UPDATE brief_performer bp SET bp.id=New.id WHERE bp.id=OLD.id;
    end if;
    IF New.department_id is NOT NULL THEN
        SET department_name_var = (SELECT department_name FROM departments d WHERE d.id=New.department_id);
            UPDATE brief_performer bp
            SET bp.name=New.name,
                bp.department_name=department_name_var,
                bp.img_path = New.img_path
            WHERE bp.id=New.id;
    ELSE
        UPDATE brief_performer bp
        SET bp.name=New.name,
            bp.department_name=null,
            bp.img_path = New.img_path
        WHERE bp.id=New.id;
    end if;
END$$

drop view brief_performer_json_view;

