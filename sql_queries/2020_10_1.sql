create schema bcrew_default;

create table users
(
	id bigint auto_increment,
	login varchar(255) not null,
	password varchar(255) not null,
	enabled boolean default true not null,
	constraint users_pk
		primary key (id)
);

create unique index users_login_uindex
	on users (login);

alter table users change enabled enable tinyint(1) default 1 not null;

alter table users
	add role int not null;

create table user_info
(
	id bigint auto_increment,
	first_name varchar(255) null,
	middle_name varchar(255) null,
	last_name varchar(255) null,
	img_path varchar(255) null,
	img_id_token varchar(255) null,
	constraint user_info_pk
		primary key (id)
);

create table rem_me_tokens
(
	id bigint auto_increment,
	uuid_col BINARY(16) not null,
	user_id bigint not null,
	creation_time bigint not null,
	token_ip varchar(255) not null,
	constraint rem_me_tokens_pk
		primary key (id),
	constraint rem_me_tokens_users_id_fk
		foreign key (user_id) references bcrew.users (id)
);

alter table users
	add user_info_id bigint not null;

alter table users
	add constraint users_user_info_id_fk
		foreign key (user_info_id) references user_info (id);

alter table users drop foreign key users_user_info_id_fk;

alter table users
	add constraint users_user_info_id_fk
		foreign key (user_info_id) references user_info (id)
			on delete cascade;

alter table rem_me_tokens drop foreign key rem_me_tokens_users_id_fk;

alter table rem_me_tokens
	add constraint rem_me_tokens_users_id_fk
		foreign key (user_id) references bcrew.users (id)
			on delete cascade;


alter table rem_me_tokens drop foreign key rem_me_tokens_users_id_fk;

alter table rem_me_tokens
    add constraint rem_me_tokens_users_id_fk
        foreign key (user_id) references bcrew.users (id)
            on delete cascade;

alter table rem_me_tokens drop foreign key rem_me_tokens_users_id_fk;

alter table rem_me_tokens
	add constraint rem_me_tokens_users_id_fk
		foreign key (user_id) references bcrew_default.users (id);

alter table bcrew.performers
	add role int not null;

alter table user_info modify first_name varchar(63) null;

alter table user_info modify middle_name varchar(63) null;

alter table user_info modify last_name varchar(63) null;

alter table user_info modify img_path varchar(128) null;

alter table user_info modify img_id_token varchar(128) null;

create table core_properties
(
	id bigint auto_increment,
	name varchar(63) not null,
	description varchar(63) null,
	type_value int not null,
	string_value varchar(255) null,
	int_value int null,
	decimal_value decimal null,
	constraint core_properties_pk
		primary key (id)
);

