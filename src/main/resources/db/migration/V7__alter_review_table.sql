alter table reviews
    add column user_id bigint not null,
    add constraint fk_reviews_users foreign key (user_id) references users (id) on delete cascade;

