create table performers_events
(
	perf_id bigint not null,
	event_id bigint not null,
	constraint performers_events_app_event_id_fk
		foreign key (event_id) references app_event (id),
	constraint performers_events_performers_id_fk
		foreign key (perf_id) references performers (id)
);


alter table brief_performer
	add email varchar(512) not null;

