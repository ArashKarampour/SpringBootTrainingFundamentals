create table usres
(
    id       bigint auto_increment
        primary key,
    name     varchar(255) not null,
    email    varchar(255) not null,
    password varchar(255) not null
);

create table addresses
(
    id      bigint auto_increment
        primary key,
    street  varchar(255) not null,
    city    varchar(255) not null,
    zip     varchar(255) not null,
    user_id bigint       not null,
    constraint addresses_usres_id_fk
        foreign key (user_id) references usres (id)
);

