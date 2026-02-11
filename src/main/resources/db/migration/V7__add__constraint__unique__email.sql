alter table users
    add constraint users_unique_email
        unique (email);
