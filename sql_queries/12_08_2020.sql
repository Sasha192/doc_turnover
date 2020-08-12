alter table performers_events drop foreign key performers_events_performers_id_fk

alter table performers_events
	add constraint performers_events_performers_id_fk
		foreign key (perf_id) references performers (id)
			on delete cascade;

drop trigger IF EXISTS brief_performer_trigger_delete;
DELIMITER $$
create trigger brief_performer_trigger_delete
    after delete
    on performers for each row
begin
        DELETE FROM brief_performer WHERE id = OLD.id;
END$$
DELIMITER ;

alter table tasks_performers drop foreign key tasks_performers_performers_id_fk;

alter table tasks_performers
	add constraint tasks_performers_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table performer_user_roles drop foreign key performer_user_roles_performers_id_fk;

alter table performer_user_roles
	add constraint performer_user_roles_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

alter table users drop foreign key users_performers_id_fk;

alter table users
	add constraint users_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete set null;

