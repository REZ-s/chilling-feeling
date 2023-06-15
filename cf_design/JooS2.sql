CREATE TABLE `User` (
	`user_id`	uuid	NOT NULL,
	`user_name`	varchar(16)	NOT NULL,
	`account_type`	tinyint	NOT NULL
);

CREATE TABLE `Profile` (
	`profile_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`nickname`	varchar(16)	NOT NULL,
	`image`	blob	NULL,
	`introduction`	text(500)	NULL,
	`created`	datetime	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Authentication` (
	`Authentication_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`gather_agree`	tinyint(1)	NOT NULL,
	`phone_number`	text(12)	NOT NULL,
	`email`	text(32)	NOT NULL,
	`sex`	text(16)	NOT NULL,
	`country`	text(32)	NOT NULL,
	`birthday`	date	NOT NULL,
	`auth_date`	datetime	NOT NULL
);

CREATE TABLE `Address` (
	`address_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`address_type`	tinyint	NOT NULL,
	`postal_code`	varchar(5)	NOT NULL,
	`base_address`	text(32)	NOT NULL,
	`detail_address`	text(32)	NOT NULL
);

CREATE TABLE `Device` (
	`device_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`in_use`	tinyint	NOT NULL,
	`register_no`	int	NOT NULL,
	`uuid`	varchar(36)	NOT NULL,
	`model`	text(32)	NOT NULL,
	`os_type`	int	NOT NULL,
	`os_version`	int	NOT NULL
);

CREATE TABLE `Subscription` (
	`subscription_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`subscription_type`	tinyint	NOT NULL,
	`total_price`	int	NOT NULL,
	`purchase_date`	datetime	NOT NULL,
	`expriation_date`	datetime	NOT NULL
);

CREATE TABLE `Orders` (
	`order_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`order_status`	tinyint	NOT NULL,
	`created`	datetime	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Delivery` (
	`delivery_id`	uuid	NOT NULL,
	`order_id`	uuid	NOT NULL,
	`delivery_status`	tinyint	NOT NULL,
	`created`	datetime	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Payment` (
	`payment_id`	uuid	NOT NULL,
	`order_id`	uuid	NOT NULL,
	`raw_price`	int	NOT NULL,
	`discount_rate`	tinyint	NOT NULL,
	`point`	int	NOT NULL,
	`is_refundable`	tinyint(1)	NOT NULL,
	`payment_type`	tinyint	NOT NULL,
	`payment_status`	tinyint	NOT NULL,
	`created`	datetime	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Refund` (
	`refund_id`	uuid	NOT NULL,
	`order_id`	uuid	NOT NULL,
	`refund_status`	tinyint	NOT NULL,
	`is_refundable`	tinyint(1)	NOT NULL,
	`refund_point`	int	NOT NULL,
	`refund_price`	int	NOT NULL,
	`created`	datetime	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Goods` (
	`goods_id`	uuid	NOT NULL,
	`category_id`	uuid	NOT NULL,
	`goods_name`	varchar(64)	NOT NULL,
	`price`	int	NOT NULL,
	`stock`	int	NOT NULL,
	`description`	text(500)	NOT NULL
);

CREATE TABLE `SubscriptionGoods` (
	`subscription_goods_id`	uuid	NOT NULL,
	`subscription_goods_name`	varchar(64)	NOT NULL,
	`discount_rate`	tinyint	NOT NULL,
	`price`	int	NOT NULL,
	`stock`	int	NOT NULL,
	`description`	text(500)	NOT NULL,
	`is_active`	tinyint(1)	NOT NULL,
	`display_date`	datetime	NOT NULL
);

CREATE TABLE `Category` (
	`category_id`	uuid	NOT NULL,
	`category_name`	varchar(32)	NOT NULL,
	`parent_id`	uuid	NOT NULL
);

CREATE TABLE `Favorite` (
	`favorite_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL
);

CREATE TABLE `Auth` (

);

CREATE TABLE `Product` (

);

CREATE TABLE `Member` (

);

CREATE TABLE `Billing` (

);

CREATE TABLE `Log` (

);

CREATE TABLE `GoodsSet` (
	`goods_set_id`	uuid	NOT NULL,
	`subscription_goods_id`	uuid	NOT NULL,
	`goods_id`	uuid	NOT NULL
);

CREATE TABLE `SubscriptionGoodsSet` (
	`subscription_goods_set_id`	uuid	NOT NULL,
	`subscription_id`	uuid	NOT NULL,
	`subscription_goods_id`	uuid	NOT NULL
);

CREATE TABLE `Favorite_SubscriptionGoods` (
	`favorite_subscription_goods_id`	uuid	NOT NULL,
	`favorite_id`	uuid	NOT NULL,
	`subscription_goods_id`	uuid	NOT NULL
);

CREATE TABLE `UserActivityLog` (
	`user_activity_log_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`table_name`	varchar(30)	NOT NULL,
	`row_id`	int	NOT NULL,
	`activity_code`	char(1)	NOT NULL,
	`as_is`	varchar(256)	NOT NULL,
	`to_be`	varchar(256)	NOT NULL,
	`modifier`	tinyint	NOT NULL,
	`log_time`	datetime	NOT NULL
);

CREATE TABLE `LoginLog` (
	`login_log_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`device_id`	uuid	NOT NULL,
	`activity_code`	tinyint	NOT NULL,
	`ip`	varchar(15)	NOT NULL,
	`fail_count`	tinyint	NOT NULL,
	`fail_reason`	tinyint	NOT NULL,
	`log_time`	datetime	NOT NULL
);

CREATE TABLE `SocialLogin` (
	`social_login_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`social_code`	tinyint	NOT NULL,
	`external_id`	varchar(64)	NOT NULL,
	`access_token`	varchar(256)	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `Password` (
	`password_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`salt`	varchar(128)	NOT NULL,
	`pw`	varchar(128)	NOT NULL,
	`last_update`	datetime	NOT NULL
);

CREATE TABLE `CIDI` (
	`cidi_id`	uuid	NOT NULL,
	`user_id`	uuid	NOT NULL,
	`ci`	varchar(88)	NOT NULL,
	`di`	varchar(64)	NOT NULL
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `Profile` ADD CONSTRAINT `PK_PROFILE` PRIMARY KEY (
	`profile_id`
);

ALTER TABLE `Authentication` ADD CONSTRAINT `PK_AUTHENTICATION` PRIMARY KEY (
	`Authentication_id`
);

ALTER TABLE `Address` ADD CONSTRAINT `PK_ADDRESS` PRIMARY KEY (
	`address_id`
);

ALTER TABLE `Device` ADD CONSTRAINT `PK_DEVICE` PRIMARY KEY (
	`device_id`
);

ALTER TABLE `Subscription` ADD CONSTRAINT `PK_SUBSCRIPTION` PRIMARY KEY (
	`subscription_id`
);

ALTER TABLE `Orders` ADD CONSTRAINT `PK_ORDERS` PRIMARY KEY (
	`order_id`
);

ALTER TABLE `Delivery` ADD CONSTRAINT `PK_DELIVERY` PRIMARY KEY (
	`delivery_id`
);

ALTER TABLE `Payment` ADD CONSTRAINT `PK_PAYMENT` PRIMARY KEY (
	`payment_id`
);

ALTER TABLE `Refund` ADD CONSTRAINT `PK_REFUND` PRIMARY KEY (
	`refund_id`
);

ALTER TABLE `Goods` ADD CONSTRAINT `PK_GOODS` PRIMARY KEY (
	`goods_id`
);

ALTER TABLE `SubscriptionGoods` ADD CONSTRAINT `PK_SUBSCRIPTIONGOODS` PRIMARY KEY (
	`subscription_goods_id`
);

ALTER TABLE `Category` ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
	`category_id`
);

ALTER TABLE `Favorite` ADD CONSTRAINT `PK_FAVORITE` PRIMARY KEY (
	`favorite_id`
);

ALTER TABLE `GoodsSet` ADD CONSTRAINT `PK_GOODSSET` PRIMARY KEY (
	`goods_set_id`
);

ALTER TABLE `SubscriptionGoodsSet` ADD CONSTRAINT `PK_SUBSCRIPTIONGOODSSET` PRIMARY KEY (
	`subscription_goods_set_id`
);

ALTER TABLE `Favorite_SubscriptionGoods` ADD CONSTRAINT `PK_FAVORITE_SUBSCRIPTIONGOODS` PRIMARY KEY (
	`favorite_subscription_goods_id`
);

ALTER TABLE `UserActivityLog` ADD CONSTRAINT `PK_USERACTIVITYLOG` PRIMARY KEY (
	`user_activity_log_id`
);

ALTER TABLE `LoginLog` ADD CONSTRAINT `PK_LOGINLOG` PRIMARY KEY (
	`login_log_id`
);

ALTER TABLE `SocialLogin` ADD CONSTRAINT `PK_SOCIALLOGIN` PRIMARY KEY (
	`social_login_id`
);

ALTER TABLE `Password` ADD CONSTRAINT `PK_PASSWORD` PRIMARY KEY (
	`password_id`
);

ALTER TABLE `CIDI` ADD CONSTRAINT `PK_CIDI` PRIMARY KEY (
	`cidi_id`
);

