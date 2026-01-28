create table categories(
    id int auto_increment primary key,
    name varchar(50) not null unique
);

create table manufacturers(
    id bigint auto_increment primary key,
    name varchar(255) not null unique
);

create table tags(
    id int auto_increment primary key,
    name varchar(50) not null unique
);

create table products(
    id bigint auto_increment primary key,
    name varchar(255) not null unique,
    short_description varchar(255) null default 'No Description',
    long_description text null,
    net_price decimal(10, 2) not null,
    stock_quantity int not null,
    is_available boolean not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    deleted_at timestamp null,
    category_id int not null,
    manufacturer_id bigint not null,
    constraint ck_products_net_price_positive_or_zero check (net_price >= 0),
    constraint fk_products_categories foreign key (category_id) references categories (id),
    constraint fk_products_manufacturers foreign key (manufacturer_id) references manufacturers (id)
);

create table product_tags(
    product_id bigint not null,
    tag_id int not null,
    constraint pk_product_tags_product_id_tag_id primary key (product_id, tag_id),
    constraint fk_product_tags_products foreign key (product_id) references products (id) on delete cascade,
    constraint fk_product_tags_tags foreign key (tag_id) references tags (id) on delete cascade
);

create table reviews(
    id bigint auto_increment primary key,
    rating tinyint not null,
    description varchar(255) null,
    product_id bigint not null,
    constraint ck_reviews_rating_between_one_and_five check (rating >= 1 and rating <= 5),
    constraint fk_reviews_products foreign key (product_id) references products (id)
);