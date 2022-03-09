create table match_profile
(
    id          varchar(36)  not null,
    p_id        varchar(255),
    orgnr       varchar(255),
    source_id   varchar(36)  not null,
    type        varchar(255) not null,
    status      varchar(255) not null,
    title       varchar(1024),
    description varchar(1024),
    profile     jsonb        not null,
    created_by  varchar(255) not null,
    updated_by  varchar(255) not null,
    expires     timestamptz  not null default clock_timestamp(),
    created     timestamptz  not null default clock_timestamp(),
    updated     timestamptz  not null default clock_timestamp(),
    primary key (id),
    unique (source_id)
);

create index match_profile_orgnr on match_profile (orgnr);
create index match_profile_pid on match_profile(p_id);

create table outbox
(
    id      varchar(36)    not null,
    key_id  varchar(255)   not null,
    type    varchar(255)   not null,
    status  varchar(255)   not null,
    payload jsonb          not null,
    updated timestamptz    not null default clock_timestamp(),
    primary key (id)
);

create index outbox_status on outbox (status);
create index outbox_updated on outbox (updated);
