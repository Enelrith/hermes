alter table products
    add column weight_g int not null default 0,
    add column length_mm int not null default 0,
    add column width_mm int not null default 0,
    add column height_mm int not null default 0,
    add constraint ck_products_weight_g_positive_or_zero check (weight_g >= 0),
    add constraint ck_products_length_mm_positive_or_zero check (length_mm>= 0),
    add constraint ck_products_width_mm_positive_or_zero check (width_mm >= 0),
    add constraint ck_products_height_mm_positive_or_zero check (height_mm >= 0);