alter table brief_performer
	add email varchar(512) null;

drop trigger IF EXISTS users_brief_performer_trigger_insert;
DELIMITER $$
create trigger users_brief_performer_trigger_insert
    after insert
    on users for each row
begin
    DECLARE email_var VARCHAR(512) DEFAULT NULL;
    IF New.performer_id is NOT NULL THEN
        SET email_var = New.email;
        UPDATE brief_performer bf SET bf.email=email_var WHERE bf.id=New.performer_id;
    end if;
END$$

drop trigger IF EXISTS users_brief_performer_trigger_update;
DELIMITER $$
create trigger users_brief_performer_trigger_update
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