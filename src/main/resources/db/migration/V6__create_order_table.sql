create table orders(
    id bigint auto_increment primary key,
    number varchar(20) not null unique,
    status varchar(20) not null,
    delivery_street varchar(100) not null,
    delivery_city varchar(100) not null,
    delivery_state_province varchar(100) not null,
    delivery_country varchar(100) not null,
    delivery_postal_code varchar(20) not null,
    payment_method varchar(20) not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    shipped_at timestamp null,
    delivered_at timestamp null,
    user_id bigint not null,
    constraint fk_orders_users foreign key (user_id) references users (id)
);

create table order_items(
    id bigint auto_increment primary key,
    quantity int not null,
    unit_net_price decimal(10, 2) not null,
    unit_shipping_net_price decimal(10, 2) not null,
    tax_rate decimal(6, 4) not null,
    product_id bigint not null,
    order_id bigint not null,
    constraint ck_order_items_quantity_positive check (quantity > 0),
    constraint ck_order_items_unit_net_price_positive_or_zero check (unit_net_price >= 0),
    constraint ck_order_items_unit_shipping_net_price_positive_or_zero check (unit_shipping_net_price >= 0),
    constraint ck_order_items_tax_rate_positive_or_zero check (tax_rate >= 0),
    constraint fk_order_items_products foreign key (product_id) references products (id),
    constraint fk_order_items_orders foreign key (order_id) references orders (id)
);
