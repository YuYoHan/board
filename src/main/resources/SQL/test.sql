create database t;
use t;
select * from member;
select * from board;
select * from comment;

drop table board;

create table member(
    member_id bigint auto_increment primary key default ,
    member_email varchar(300) unique ,
    password varchar(300),
    role varchar(300),
    user_name varchar(300)
);

create table board(
    board_id bigint auto_increment primary key ,
    reg_time datetime default now(),
    update_time datetime default now(),
    create_by varchar(300),
    modified_by varchar(300),
    contents varchar(3000),
    title varchar(300),
    member_id bigint,
    foreign key pk_memberId(member_id) references member(member_id)
);

create table comment(
    comment_id bigint auto_increment primary key ,
    reg_time datetime default now(),
    update_time datetime default now(),
    comment varchar(3000),
    board_id bigint,
    foreign key pk_boardId(board_id) references board(board_id),
    member_id bigint,
    foreign key pk_memberId(member_id) references member(member_id)
)