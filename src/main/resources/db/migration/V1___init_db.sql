create table if not exists category
(
    category_id   bigint       not null
        primary key,
    category_name varchar(255) null,
    created_at    datetime(6)  null,
    created_by    int          not null,
    updated_at    datetime(6)  null
);

create table if not exists category_seq
(
    id       bigint auto_increment
        primary key,
    next_val bigint null
);

create table if not exists contact_message
(
    id      bigint auto_increment
        primary key,
    email   varchar(255) null,
    message text         null,
    name    varchar(255) null,
    title   varchar(255) null
);
create table if not exists supplier
(
    vendor_id            varchar(255) not null
        primary key,
    p_o_box              varchar(255) null,
    city                 varchar(255) null,
    country              varchar(255) null,
    location             varchar(255) null,
    contact_information  varchar(255) null,
    contact_person       varchar(255) null,
    created_at           datetime(6)  null,
    created_by           int          not null,
    email                varchar(255) null,
    name                 varchar(255) null,
    payment_type         tinyint      null,
    phone_number         varchar(255) null,
    terms_and_conditions varchar(255) null,
    updated_at           datetime(6)  null);

create table if not exists item
(
    item_id          varchar(255)   not null
        primary key,
    created_at       datetime(6)    null,
    created_by       int            not null,
    item_description text           null,
    item_name        varchar(255)   null,
    item_number      varchar(255)   null,
    quantity         int            not null,
    total_price      decimal(38, 2) null,
    unit_price       decimal(38, 2) null,
    updated_at       datetime(6)    null,
    category_id      bigint         null,
    constraint FK2n9w8d0dp4bsfra9dcg0046l4
        foreign key (category_id) references category (category_id)
);
create table if not exists contract
(
    contract_id          varchar(255) not null
        primary key,
    contract_end_date    date         null,
    contract_start_date  date         null,
    contract_status      tinyint      null,
    contract_title       varchar(255) null,
    contract_type        varchar(255) null,
    created_at           datetime(6)  null,
    created_by           int          null,
    terms_and_conditions text         null,
    updated_at           datetime(6)  null,
    supplier_id          varchar(255) null,
    constraint FKes2m8smbcafbyoa7nu3806iyn
        foreign key (supplier_id) references supplier (vendor_id)
);
create table if not exists purchase_order
(
    purchase_order_id    bigint auto_increment
        primary key,
    approval_status      tinyint        null,
    created_at           datetime(6)    null,
    created_by           int            null,
    delivery_date        date           null,
    payment_type         tinyint        null,
    purchase_order_title varchar(255)   null,
    terms_and_conditions varchar(255)   null,
    total_amount         decimal(38, 2) null,
    updated_at           datetime(6)    null,
    supplier_id          varchar(255)   null,
    constraint FK4traogu3jriq9u7e8rvm86k7i
        foreign key (supplier_id) references supplier (vendor_id)
);
create table if not exists deliveries
(
    id                bigint auto_increment
        primary key,
    delivered_date    datetime(6)  null,
    expected_on       datetime(6)  null,
    received_by       varchar(255) null,
    received_date     datetime(6)  null,
    received_on       datetime(6)  null,
    purchase_order_id bigint       null,
    delivered_on      datetime(6)  null,
    delivered_via     varchar(255) null,
    constraint UK_cdxcn7mulotbu224yrk0147xb
        unique (purchase_order_id),
    constraint FK4s99vvqorx0ad0vmbykxi0k36
        foreign key (purchase_order_id) references purchase_order (purchase_order_id)
);

create table if not exists demo_request
(
    id           bigint auto_increment
        primary key,
    company_name varchar(255) null,
    description  varchar(255) null,
    email        varchar(255) null,
    first_name   varchar(255) null,
    last_name    varchar(255) null,
    phone_number varchar(255) null
);

create table if not exists email
(
    id       decimal(38, 2) not null
        primary key,
    email_to varchar(255)   null,
    message  varchar(255)   null,
    subject  varchar(255)   null
);

create table if not exists email_seq
(
    next_val bigint null
);


create table if not exists user
(
    id           int          not null
        primary key,
    avatar       varchar(255) null,
    email        varchar(255) not null,
    firstname    varchar(255) not null,
    lastname     varchar(255) null,
    password     varchar(255) null,
    phone_number varchar(255) null,
    username     varchar(255) null,
    constraint UK_839p4y7o6ub9djmc6c6niwtr3
        unique (firstname),
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
);
create table if not exists user_seq
(
    next_val bigint null
);


create table if not exists forgot_password
(
    id              bigint auto_increment
        primary key,
    expiration_time datetime(6) not null,
    otp             int         not null,
    user_id         int         null,
    constraint FK95rqabtnw8wouua80mbixrq4
        foreign key (user_id) references user (id)
);
create table if not exists invoice
(
    invoice_id        varchar(255) not null
        primary key,
    created_at        datetime(6)  null,
    created_by        int          null,
    due_date          date         null,
    invoice_date      date         null,
    invoice_number    varchar(255) null,
    invoice_status    tinyint      null,
    updated_at        datetime(6)  null,
    purchase_order_id bigint       null,
    constraint UK_6wau8a22poajgbo68gmhgujys
        unique (purchase_order_id),
    constraint FKpbnhtmx9crcudpxcr5j2xjool
        foreign key (purchase_order_id) references purchase_order (purchase_order_id)
);



create table if not exists organization
(
    id           bigint       not null
        primary key,
    box          varchar(255) null,
    city         varchar(255) null,
    country      varchar(255) null,
    location     varchar(255) null,
    created_by   int          not null,
    name         varchar(255) null,
    phone_number varchar(255) null
);
create table if not exists organization_seq
(
    next_val bigint null
);



create table if not exists purchase_request
(
    purchase_request_id    bigint       not null
        primary key,
    approval_status        tinyint      null,
    created_at             datetime(6)  null,
    created_by             int          null,
    delivery_date          date         null,
    due_date               date         null,
    purchase_request_title varchar(255) null,
    terms_and_conditions   text         null,
    updated_at             datetime(6)  null
);
create table if not exists purchase_request_seq
(
    next_val bigint null
);




create table purchase_request_item_detail
(
    id                  bigint         not null
        primary key,
    offer_total_price   double         not null,
    offer_unit_price    decimal(38, 2) null,
    quote_status        tinyint        null,
    item_id             varchar(255)   null,
    purchase_request_id bigint         null,
    supplier_id         varchar(255)   null,
    constraint FK2fil3bei4667uobym6nkt1wid
        foreign key (purchase_request_id) references purchase_request (purchase_request_id),
    constraint FK8myoeim3b3mwu0p2mh36gdp9p
        foreign key (item_id) references item (item_id),
    constraint FKkhrfiqs5abwnwqlf9061ehy7v
        foreign key (supplier_id) references supplier (vendor_id)
);
create table purchase_request_item_detail_seq
(
    next_val bigint null
);


create table if not exists purchase_requisition
(
    requisition_id    bigint       not null
        primary key,
    approval_status   tinyint      null,
    created_at        datetime(6)  null,
    created_by        int          null,
    date_created      datetime(6)  null,
    date_needed       date         null,
    description       varchar(255) null,
    requisition_title varchar(255) null,
    updated_at        datetime(6)  null
);
create table purchase_requisition_seq
(
    next_val bigint null
);


create table if not exists role
(
    id          bigint       not null
        primary key,
    description varchar(255) null,
    is_default  bit          null,
    name        varchar(255) not null,
    constraint unique_role_name
        unique (name)
);
create table if not exists role_seq
(
    next_val bigint null
);


create table if not exists subscriber
(
    id    bigint auto_increment
        primary key,
    email varchar(255) null,
        unique (email)
);


create table if not exists token
(
    id         bigint          not null
        primary key,
    expired    bit             not null,
    revoked    bit             not null,
    token      varchar(255)    null,
    token_type enum ('BEARER') null,
    fk_user_id int             null,
    constraint UK_pddrhgwxnms2aceeku9s2ewy5
        unique (token),
    constraint FK9hn5ds1xa0aban7hhyguk5y2k
        foreign key (fk_user_id) references user (id)
);
create table if not exists token_seq
(
    next_val bigint null
);



create table if not exists file
(
    id           bigint auto_increment
        primary key,
    file         tinyblob     null,
    file_name    varchar(255) null,
    content_type varchar(255) null
);
create table if not exists delivery_items
(
    id                 bigint auto_increment
        primary key,
    quantity_delivered int          null,
    quantity_received  int          not null,
    delivery_id        bigint       null,
    item_id            varchar(255) null,
    constraint FK5ueu7shret3j7s3mh0aupwtgk
        foreign key (item_id) references item (item_id),
    constraint FKtrb7ybqtdhmfsqfw5csgg5muy
        foreign key (delivery_id) references deliveries (id)
);
create table if not exists order_items
(
    purchase_order_id bigint       not null,
    item_id           varchar(255) not null,
    primary key (purchase_order_id, item_id),
    constraint FKj6d3rs824jnq7r8do771a00n2
        foreign key (purchase_order_id) references purchase_order (purchase_order_id),
    constraint FKpttit550s4ekrghi11o2q6dqx
        foreign key (item_id) references item (item_id)
);
create table if not exists request_items
(
    purchase_request_id bigint       not null,
    item_id             varchar(255) not null,
    primary key (purchase_request_id, item_id),
    constraint FKlmjpbdgb30ekg9nfeqgf6fd40
        foreign key (purchase_request_id) references purchase_request (purchase_request_id),
    constraint FKmj2n11u2klnjo4rhersor5wxd
        foreign key (item_id) references item (item_id)
);
create table if not exists request_suppliers
(
    purchase_request_id bigint       not null,
    vendor_id           varchar(255) not null,
    primary key (purchase_request_id, vendor_id),
    constraint FK5c934to99xp5m33qo61edwqcd
        foreign key (purchase_request_id) references purchase_request (purchase_request_id),
    constraint FKgyer2swde5ipwvcw1vsteai52
        foreign key (vendor_id) references supplier (vendor_id)
);

create table if not exists requisition_items
(
    purchase_requisition_id bigint       not null,
    item_id                 varchar(255) not null,
    primary key (purchase_requisition_id, item_id),
    constraint FKjc7f4bxmjwautm8eidm6gr9by
        foreign key (purchase_requisition_id) references purchase_requisition (requisition_id),
    constraint FKn85yex6hr3wk8e3wywoiwkywa
        foreign key (item_id) references item (item_id)
);

create table if not exists user_with_roles
(
    user_side_id int    not null,
    role_side_id bigint not null,
    primary key (user_side_id, role_side_id),
    constraint FKdsxywbyqo9okxwb6kiytvg1h0
        foreign key (user_side_id) references user (id),
    constraint FKrgwdihbgq27i4v1rtb6tsw8mu
        foreign key (role_side_id) references role (id)
);
create table contract_items
(
    contract_id varchar(255) not null,
    item_id     varchar(255) not null,
    primary key (contract_id, item_id),
    constraint FK6t92qryumaafxd2hfju96uoac
        foreign key (contract_id) references contract (contract_id),
    constraint FKhj560oech2y5ade9umd99p4go
        foreign key (item_id) references item (item_id)
);






















