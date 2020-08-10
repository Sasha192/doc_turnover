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

drop trigger IF EXISTS brief_performer_trigger_update;
DELIMITER $$
create trigger brief_performer_trigger_update
    after update
    on users for each row
begin
    DECLARE email_var VARCHAR(512) DEFAULT NULL;
    IF New.performer_id <> OLD.performer_id THEN
        UPDATE brief_performer bp SET bp.id=New.performer_id
        WHERE bp.id=OLD.performer_id;
    end if;
    IF New.performer_id is NOT NULL THEN
        SET email_var = New.email;
        UPDATE brief_performer bf SET bf.email=email_var WHERE bf.id=New.performer_id;
    end if;
END$$