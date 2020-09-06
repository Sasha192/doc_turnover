
create table cal_perf_statistics
(
    id bigint auto_increment,
    cal_perf_stat_type varchar(128) not null,
    performer_id bigint not null,
    creation_timestamp TIMESTAMP not null,
    expired boolean null,
    amount int not null,
    completed int not null,
    expired_deadline int null,
    constraint cal_perf_statistics_pk
        primary key (id)
);


alter table cal_perf_statistics
    add inprogress int not null;

alter table cal_perf_statistics
    add onhold int not null;

alter table cal_perf_statistics
    add calendar_enum_type int not null;

alter table cal_perf_statistics
	add constraint cal_perf_statistics_performers_id_fk
		foreign key (performer_id) references performers (id)
			on delete cascade;

