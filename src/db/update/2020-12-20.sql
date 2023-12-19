INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (310, '在线入款设置', 'admin.menu.depositOnline', '/depositOnline/index.do', 7, 8, '', 'admin:depositOnline', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (311, '修改', 'admin.menu.modify', '', 310, 3, '', 'admin:depositOnline:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (312, '删除', 'admin.menu.del', '', 310, 1, '', 'admin:depositOnline:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (313, '新增', 'admin.menu.add', '', 310, 2, '', 'admin:depositOnline:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (314, '查看', 'admin.menu.viewDetail', '', 310, 2, '', 'admin:depositOnline:viewDetail', 4);
ALTER TABLE station_deposit_online ADD COLUMN "group_ids" varchar(700) COLLATE "pg_catalog"."default";

CREATE SEQUENCE "public"."station_replace_withdraw_id_seq" INCREMENT 1 START 5 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_replace_withdraw" (
  "id" int4 NOT NULL default nextval('station_replace_withdraw_id_seq') PRIMARY KEY,
  "merchant_code" varchar(5000) COLLATE "pg_catalog"."default",
  "merchant_key" varchar(5000) COLLATE "pg_catalog"."default",
  "url" varchar(256) COLLATE "pg_catalog"."default",
  "account" varchar(5000) COLLATE "pg_catalog"."default",
  "min" numeric,
  "max" numeric,
  "def" int2,
  "status" int2,
  "version" int2,
  "station_id" int4,
  "icon" varchar(256) COLLATE "pg_catalog"."default",
  "pay_platform_id" int4,
  "pay_type" varchar(20) COLLATE "pg_catalog"."default",
  "pay_getway" varchar(500) COLLATE "pg_catalog"."default",
  "level_group" varchar COLLATE "pg_catalog"."default" DEFAULT 0,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "appid" varchar(500) COLLATE "pg_catalog"."default",
  "sort_no" int4 DEFAULT 0,
  "show_type" varchar(20) COLLATE "pg_catalog"."default",
  "recharge_type" varchar(20) COLLATE "pg_catalog"."default",
  "fixed_amount" varchar(50) COLLATE "pg_catalog"."default",
  "extra" varchar COLLATE "pg_catalog"."default",
  "pay_status" varchar COLLATE "pg_catalog"."default",
  "search_getway" varchar(500),
  "white_list_ip" varchar(500),
  "remark" varchar(500),
  "update_key_time" timestamp(6) NULL
);
COMMENT ON COLUMN "public"."station_replace_withdraw"."merchant_code" IS '商户编号';
COMMENT ON COLUMN "public"."station_replace_withdraw"."merchant_key" IS '商户密钥';
COMMENT ON COLUMN "public"."station_replace_withdraw"."url" IS '接口域名';
COMMENT ON COLUMN "public"."station_replace_withdraw"."account" IS '账号';
COMMENT ON COLUMN "public"."station_replace_withdraw"."min" IS '最小金额';
COMMENT ON COLUMN "public"."station_replace_withdraw"."max" IS '最大金额';
COMMENT ON COLUMN "public"."station_replace_withdraw"."def" IS '是否默认';
COMMENT ON COLUMN "public"."station_replace_withdraw"."status" IS '状态 ';
COMMENT ON COLUMN "public"."station_replace_withdraw"."version" IS '版本（1=老版 2=新版） ';
COMMENT ON COLUMN "public"."station_replace_withdraw"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_replace_withdraw"."icon" IS '图标样式';
COMMENT ON COLUMN "public"."station_replace_withdraw"."pay_platform_id" IS '支付平台ID';
COMMENT ON COLUMN "public"."station_replace_withdraw"."pay_type" IS '支付方式： 1-收银台，2-银行直连，3-单微信，4-单支付宝，5-QQ钱包，6-京东扫码';
COMMENT ON COLUMN "public"."station_replace_withdraw"."level_group" IS '限制会员等级';
COMMENT ON COLUMN "public"."station_replace_withdraw"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."station_replace_withdraw"."show_type" IS '显示类型: all - 所有终端都显示、 pc - pc端显示、 mobile - 手机端显示、 app - app端显示';
COMMENT ON COLUMN "public"."station_replace_withdraw"."recharge_type" IS '充值金额类型 默认为空-可输入金额 fixed-可选固定金额 only-单一固定金额';
COMMENT ON COLUMN "public"."station_replace_withdraw"."fixed_amount" IS '固定金额 多个金额使用英文逗号隔开';
COMMENT ON COLUMN "public"."station_replace_withdraw"."extra" IS '备选参数';
COMMENT ON COLUMN "public"."station_replace_withdraw"."pay_status" IS '针对支付宝微信扫码条码选择,1为扫码，2为条码，默认为0';
COMMENT ON COLUMN "public"."station_replace_withdraw"."search_getway" IS '代付查询网关';
COMMENT ON COLUMN "public"."station_replace_withdraw"."white_list_ip" IS '代付白名单';
COMMENT ON COLUMN "public"."station_replace_withdraw"."remark" IS '备注';
COMMENT ON COLUMN "public"."station_replace_withdraw"."update_key_time" IS '更新密钥时间';

INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (315, '代付出款设置', 'admin.menu.replaceWithdraw', '/replaceWithdraw/index.do', 7, 8, '', 'admin:replaceWithdraw', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (316, '修改', 'admin.menu.modify', '', 315, 3, '', 'admin:replaceWithdraw:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (317, '删除', 'admin.menu.del', '', 315, 1, '', 'admin:replaceWithdraw:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (318, '新增', 'admin.menu.add', '', 315, 2, '', 'admin:replaceWithdraw:add', 4);

ALTER TABLE mny_draw_record ADD COLUMN "type" int2;

ALTER TABLE mny_deposit_record ADD COLUMN "num" int4;
ALTER TABLE mny_deposit_record ADD COLUMN "rate" numeric(20,2);

ALTER TABLE mny_draw_record ADD COLUMN "pay_id" int4;
ALTER TABLE mny_draw_record ADD COLUMN "pay_name" varchar(200) COLLATE "default";