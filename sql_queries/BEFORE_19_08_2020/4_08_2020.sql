alter table performers modify department_id bigint null;

create table performer_user_roles
(
	performer_id bigint not null,
	role int not null,
	constraint performer_user_roles_performers_id_fk
		foreign key (performer_id) references performers (id)
);

