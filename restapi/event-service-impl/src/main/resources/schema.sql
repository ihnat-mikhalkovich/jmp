DROP TABLE IF EXISTS events;

create table events (
    id bigint auto_increment PRIMARY KEY,
    title varchar(256) not null,
    place varchar(256) not null,
    speaker varchar(256) not null,
    event_type varchar(50) not null,
    date_time TIMESTAMP WITH TIME ZONE not null
);