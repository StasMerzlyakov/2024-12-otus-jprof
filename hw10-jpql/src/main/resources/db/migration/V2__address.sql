create table address
(
    id   bigserial not null primary key,
    street varchar(50)
);

alter table client add column address_id bigint;
