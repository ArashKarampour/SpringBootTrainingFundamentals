create table tags
(
    id   int auto_increment
        primary key,
    name VARCHAR(255) not null
);

create table user_tags
(
    user_id BIGINT not null,
    tag_id  int    not null,
    constraint user_tags_pk -- writing a constraint is optional
        primary key (user_id, tag_id), -- this is a composite primary key
    constraint user_tags_tags_id_fk -- again writing a constraint is optional
        foreign key (tag_id) references tags (id)
            on delete cascade,
    constraint user_tags_users_id_fk
        foreign key (user_id) references users (id)
            on delete cascade
);