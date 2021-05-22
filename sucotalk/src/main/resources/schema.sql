create table if not exists MEMBER
(
    id bigint auto_increment not null,
    email varchar(255) not null unique,
    password varchar(255) not null
    primary key(id)
    );