create table match_profile(
    id varchar(36) not null,
    owner varchar(255) not null,
    source_id varchar(36) not null,
    type varchar(255) not null,
    status varchar(255) not null,
    title varchar(512),
    description varchar(1024),
    profile jsonb not null,
    created_by varchar(255) not null,
    updated_by varchar(255) not null,
    expires timestamptz not null default clock_timestamp(),
    created timestamptz not null default clock_timestamp(),
    updated timestamptz not null default clock_timestamp(),
    primary key (id),
    unique (source_id)
);

create index match_profile_owner on match_profile(owner);
