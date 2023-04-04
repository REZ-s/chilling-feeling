/*

insert into auth.role (name, role_id) values ('ROLE_USER', RANDOM_UUID());
insert into auth.role (name, role_id) values ('ROLE_MANAGER', RANDOM_UUID());
insert into auth.role (name, role_id) values ('ROLE_ADMIN', RANDOM_UUID());

select * from auth.logout_token;
select * from auth.refresh_token;
select * from auth.password;
select * from auth.social_login;
select * from auth.authentication;
select * from auth.role;
select * from member.user_role;
select * from member.user;

*/

-- insert into member.user (account_type, username, user_id) values (1, '123123', RANDOM_UUID());
