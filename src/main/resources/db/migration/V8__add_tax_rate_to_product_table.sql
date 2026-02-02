alter table products
    add column vat_rate decimal(6, 4) not null,
    add constraint ck_products_vat_rate_positive_or_zero check (vat_rate >= 0)