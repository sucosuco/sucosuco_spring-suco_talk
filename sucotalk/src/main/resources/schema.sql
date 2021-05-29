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
    name varchar(255),
    primary key(id)
);

create table if not exists PARTICIPANTS
(
    id bigint auto_increment not null,
    member_id bigint not null,
    room_id bigint not null,
    primary key(id),
    foreign key (member_id) references MEMBER(id),
    foreign key (room_id) references ROOM(id)
);

create table if not exists MESSAGE
(
    id bigint auto_increment not null,
    sender_id bigint not null,
    room_id bigint not null,
    contents varchar(255),
    send_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    primary key(id),
    foreign key (sender_id) references MEMBER(id),
    foreign key (room_id) references ROOM(id)
);