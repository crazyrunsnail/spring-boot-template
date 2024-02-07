
-- sudo -u postgres psql
create role "spring-boot-template" password 'spring-boot-template' login;
create database "spring-boot-template" owner "spring-boot-template";

-- 用户表
create table users (
 id bigserial primary key,
 username varchar(32) unique not null,
 name varchar(32),
 password varchar(128),
 roles_array_json varchar(512) default '[]',
 logged_in_at timestamp,
 created_at timestamp default current_timestamp,
 updated_at timestamp default current_timestamp
);

insert into users (username, name , password) values ('admin', '管理员', '$2a$10$CgUdJJ2F2GmrEqcqyMtWluC.dEU.9t4aZzQqsHi0HlQLa410vkXby');