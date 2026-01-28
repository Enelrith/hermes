create table refresh_tokens(
    id bigint auto_increment primary key,
    token varchar(255) not null unique,
    expiry_date timestamp not null,
    created_at timestamp not null default current_timestamp,
    user_id bigint not null,
    constraint fk_refresh_tokens_users foreign key (user_id) references users (id) on delete cascade
);