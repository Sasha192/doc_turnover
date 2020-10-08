create table users_tenants
(
	user_id bigint not null,
	tenant_id varchar(255) not null
);

create unique index users_tenants_user_id_tenant_id_uindex
	on users_tenants (user_id, tenant_id);

