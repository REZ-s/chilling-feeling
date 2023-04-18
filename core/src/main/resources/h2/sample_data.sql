
insert into auth.role (name, role_id) values ('ROLE_USER', RANDOM_UUID());
insert into auth.role (name, role_id) values ('ROLE_MANAGER', RANDOM_UUID());
insert into auth.role (name, role_id) values ('ROLE_ADMIN', RANDOM_UUID());
insert into product.goods (goods_id, name, price, stock, sales_status, sales_figures, DESCRIPTION)
                    values (RANDOM_UUID(), 'so-ju', 1500, 100, 1, 0, 'korea original so-ju');
insert into product.goods (goods_id, name, price, stock, sales_status, sales_figures, DESCRIPTION)
values (RANDOM_UUID(), 'so-ju2', 2000, 100, 1, 0, 'korea original so-ju2');

/* category root nodes */
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '일반 증류주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '위스키');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '와인');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '과실주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '브랜디');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '리큐르');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '국산 맥주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '수입 맥주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '일반 소주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '전통 소주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '청주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '탁주');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '중국술');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '사케');
insert into product.category (category_id, parent_id, category_name) values (RANDOM_UUID(), null, '기타 주류');

/*

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
