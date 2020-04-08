create table core_properties
(
	id int auto_increment,
	name varchar(512) not null,
	value varchar(2048) null,
	description varchar(1024) null,
	constraint core_properties_pk
		primary key (id)
);

