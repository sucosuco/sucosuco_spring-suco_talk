create table if not exists MEMBER
(
    id bigint auto_increment not null,
    name varchar(255) not null unique,
    password varchar(255) not null,
    primary key(id)
);

create table if not exists ROOM
(
    id bigint auto_increment not null,
    name varchar (255),
    primary key(id)
);

create table if not exists ENTERANCE
(
    id bigint auto_increment not null,
    member_id bigint not null,
    room_id bigint not null,
    primary key(id),
    foreign key (member_id) references MEMBER(id),
    foreign key (room_id) references ROOM(id)
)