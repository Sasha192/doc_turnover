create table rem_me_tokens
(
	id bigint auto_increment,
	uuid_col BINARY(16) not null,
	user_id bigint not null,
	creation_time long not null,
	constraint rem_me_tokens_pk
		primary key (id),
	constraint rem_me_tokens_users_id_fk
		foreign key (user_id) references users (id)
);

