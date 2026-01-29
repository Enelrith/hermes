create table carts(
    id bigint auto_increment primary key,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    status varchar(20) not null,
    user_id bigint not null,
    constraint fk_carts_users foreign key (user_id) references users (id) on delete cascade
);

create table cart_items(
    id bigint auto_increment primary key,
    quantity int not null,
    total_net_price decimal(10, 2) not null,
    product_id bigint not null,
    cart_id bigint not null,
    constraint ck_cart_items_quantity_positive check (quantity > 0),
    constraint ck_cart_items_total_net_price_positive_or_zero check (total_net_price >= 0),
    constraint fk_cart_items_products foreign key (product_id) references products (id) on delete restrict,
    constraint fk_cart_items_carts foreign key (cart_id) references carts (id) on delete cascade
);