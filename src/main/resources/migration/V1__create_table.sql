create sequence tokens_seq start with 1 increment by 50;

create table users
(
    id          varchar(255) default gen_random_uuid(),
    birthday    date,
    first_name  varchar(255),
    last_name   varchar(255),
    login       varchar(255) unique not null,
    middle_name varchar(255),
    password    varchar(255),
    primary key (id)
);


create table tokens
(
    id        bigserial,
    is_active boolean not null,
    login     varchar(255),
    token     varchar(255),
    primary key (id)
);









