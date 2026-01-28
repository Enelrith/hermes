create table users(
    id bigint auto_increment primary key,
    email varchar(255) not null unique,
    password varchar(255) not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    deleted_at timestamp null
);

create table roles(
    id tinyint auto_increment primary key,
    name varchar(30) not null unique
);

create table user_roles(
    user_id bigint not null,
    role_id tinyint not null,
    constraint pk_user_roles_user_id_role_id primary key (user_id, role_id),
    constraint fk_user_roles_users foreign key (user_id) references users (id) on delete cascade,
    constraint fk_user_roles_roles foreign key (role_id) references roles (id) on delete cascade
);

create table customers(
    id bigint auto_increment primary key,
    first_name varchar(50) null,
    last_name varchar(50) null,
    phone_number varchar(20) null unique,
    updated_at timestamp null on update current_timestamp,
    completed_at timestamp null,
    user_id bigint not null unique,
    constraint fk_customers_users foreign key (user_id) references users (id) on delete cascade
);

create table addresses(
    id bigint auto_increment primary key,
    street varchar(100) not null,
    city varchar(100) not null,
    state_province varchar(100) null,
    country varchar(100) not null,
    postal_code varchar(20) not null
);

create table customer_addresses(
    customer_id bigint not null,
    address_id bigint not null,
    constraint pk_customer_addresses_customer_id_address_id primary key (customer_id, address_id),
    constraint fk_customer_addresses_customers foreign key (customer_id) references customers (id) on delete cascade,
    constraint fk_customer_addresses_addresses foreign key (address_id) references addresses (id) on delete cascade
)
