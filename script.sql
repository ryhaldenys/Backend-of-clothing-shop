create database staff_db
    with owner postgres;

create sequence chose_clothes_seq
    increment by 50 start 50;

alter sequence chose_clothes_seq owner to postgres;

create sequence clothes_seq
    increment by 50 start 50;

alter sequence clothes_seq owner to postgres;

create sequence person_order_seq
    increment by 50 start 50;

alter sequence person_order_seq owner to postgres;

create sequence person_seq
    increment by 50 start 50;

alter sequence person_seq owner to postgres;

create table clothes
(
    id          bigint         not null
        primary key,
    added_at    timestamp(6)   not null,
    article     varchar(255)   not null
        constraint uk_5rnyjgbfruurlrjciv0xq0opl
            unique
        constraint uk_ar4hfq5alf88fnl7mgesrwfkf
            unique,
    description varchar(255)   not null,
    discount    numeric(38, 2) default 0,
    name        varchar(255)   not null,
    price       numeric(38, 2) not null,
    sex         varchar(255)   not null,
    subtype     varchar(255)   not null,
    type        varchar(255)   not null
);

alter table clothes
    owner to postgres;

create table clothes_images
(
    clothes_id bigint                not null
        constraint fkpeomm8qgtekxppxvdvlh5j0sf
            references clothes,
    is_main    boolean default false not null,
    url        varchar(255)          not null
        constraint uk_3ylbsch5x3wc6ptte3y7m5mmd
            unique,
    primary key (clothes_id, is_main, url)
);

alter table clothes_images
    owner to postgres;

create index clothes_images_clothes_id_idx
    on clothes_images (clothes_id);

create table clothes_sizes
(
    clothes_id bigint            not null
        constraint fka4ydjpfg8j5oq1dyikysra5eu
            references clothes,
    amount     integer default 0 not null,
    size       varchar(255)      not null,
    primary key (clothes_id, amount, size)
);

alter table clothes_sizes
    owner to postgres;

create index clothes_sizes_clothes_id_idx
    on clothes_sizes (clothes_id);

create table person
(
    id            bigint                                not null
        primary key,
    bonuses       numeric(38, 2) default 0              not null,
    created_at    timestamp(6)                          not null,
    first_name    varchar(255)                          not null,
    last_name     varchar(255)                          not null,
    password      varchar(255)                          not null,
    city          varchar(255),
    city_justin   varchar(255),
    justin_office varchar(255),
    post_office   varchar(255),
    email         text           default 'em'::text     not null
        constraint uk_cyhdnr5caye52dmk9w7yl0re1
            unique,
    status        text           default 'ACTIVE'::text not null,
    role          text           default 'USER'::text   not null
);

alter table person
    owner to postgres;

create table person_order
(
    id               bigint         not null
        primary key,
    created_at       timestamp(6)   not null,
    order_number     varchar(255)   not null
        constraint uk_7u0mh5idcysrs87smsjleja0c
            unique,
    status           varchar(255)   not null,
    total_price      numeric(38, 2) not null,
    used_bonuses     numeric(38, 2),
    person_id        bigint         not null
        constraint fkduvv84bbxgpqntcfrswlmwebr
            references person,
    address          varchar(255)   not null,
    delivery_price   numeric(38, 2) not null,
    delivery_type    varchar(255)   not null,
    payment_kind     varchar(255)   not null,
    person_full_name varchar(255)   not null
);

alter table person_order
    owner to postgres;

create index order_person_id_idx
    on person_order (person_id);

create table basket
(
    person_id    bigint not null
        primary key
        constraint fkl5jjb7kjhn3ka1617yt24dgwd
            references person,
    used_bonuses numeric(38, 2),
    total_price  numeric(38, 2)
);

alter table basket
    owner to postgres;

create index basket_person_id_idx
    on basket (person_id);

create table chose_clothes
(
    id                bigint            not null
        primary key,
    amount_of_clothes integer default 1 not null,
    size_kind         varchar(255)      not null,
    basket_id         bigint
        constraint fkhaxmgg6oqb1tv82k76f85kj7i
            references basket,
    clothes_id        bigint            not null
        constraint fkp1tw7smih9r32n7q5a204ak1y
            references clothes,
    order_id          bigint
        constraint fkbpp0lgmn86k5wo5ytusn0v1wd
            references person_order
);

alter table chose_clothes
    owner to postgres;

create index chose_clothes_basket_id
    on chose_clothes (basket_id);

create index chose_clothes_order_id
    on chose_clothes (order_id);

create table person_roles
(
    person_id bigint       not null
        constraint fks955luj19xyjwi3s1omo1pgh4
            references person,
    name      varchar(255) not null
);

alter table person_roles
    owner to postgres;

create index person_roles_person_id_idx
    on person_roles (person_id);


insert into clothes values (1,now(),'PKY0472','new hoddie hostage oversize ',0.10,'hoddie hostage oversize',1030.00,'male','hoodies','hoodies/sweatshirts');
insert into clothes values (2,now(),'PK3W472','new sweatshirt milk basic',0.12,'sweatshirt milk basic',800.00,'male','sweatshirt','hoodies/sweatshirts');
insert into clothes values (3,now(),'QW3R472','new jacket green oversize',0.10,'jacket green oversize',1890.00,'female','jackets','outerwear');
insert into clothes values (4,now(),'TEW7TR2','new socks black',0.40,'socks black',60.00,'male','socks','accessories');


insert into clothes_sizes values (1,20,'XL');
insert into clothes_sizes values (1,23,'S');
insert into clothes_sizes values (1,105,'XS');
insert into clothes_sizes values (2,11,'XXL');
insert into clothes_sizes values (3,12,'M');
insert into clothes_sizes values (3,11,'S');
insert into clothes_sizes values (4,193,'Universal');

INSERT INTO clothes_images values (1,true,'/first/url');
INSERT INTO clothes_images values (1,false,'/second/url');
INSERT INTO clothes_images values (1,false,'/third/url');
INSERT INTO clothes_images values (2,true,'/fourth/url');
INSERT INTO clothes_images values (2,false,'/fifth/url');
INSERT INTO clothes_images values (2,false,'/sixth/url');
INSERT INTO clothes_images values (3,true,'/seventh/url');
INSERT INTO clothes_images values (3,false,'/eight/url');
INSERT INTO clothes_images values (3,false,'/ninth/url');
INSERT INTO clothes_images values (4,true,'/tenth/url');
INSERT INTO clothes_images values (4,false,'/eleventh/url');

insert into person(id,bonuses,created_at,first_name,last_name,password,email,status,role,city)
values (1,100,now(),'User','UserL',
        '$2a$12$oBEJBlfFe3XdAHmO4D9oa.vNQepO/SH4CmL63.VqoL2jIAIoC6HSG','user@gmail.com','ACTIVE','USER','Lviv');
insert into basket values (1,0);

insert into person(id,bonuses,created_at,first_name,last_name,password,email,status,role)
values (2,50,now(),'Admin','AdminL',
        '$2a$12$gTBbjZVaDVdGpF0LZ3waFO1LtuX9/m8rEC.JrJ8Oy6BZIHeqdjLcC','admin@gmail.com','ACTIVE','ADMIN');
insert into basket values (2,0);

insert into person_order values (1,now(),'M1601362377','CANCELLED',1737.00,0.00,2,'Pidgorodtsi, Lesi Ukrainky street 23, post office №2',100.00,'Newpost, courier delivery','after payment','Admin AdminL');
insert into person_order values (2,now(),'M1168252115','RECEIVED',36.00,36.00,1,'Lviv, Franka street 23, post office №2',67.00,'Newpost, courier delivery','after payment','User Userl');

insert into chose_clothes values(1,1,'Universal',2,4,null);
insert into chose_clothes values(2,1,'S',2,1,null);
insert into chose_clothes values(3,1,'Universal',null,4,1);
insert into chose_clothes values(4,1,'M',null,3,1);
insert into chose_clothes values(5,2,'Universal',1,4,null);

