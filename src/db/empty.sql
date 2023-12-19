CREATE SEQUENCE "public"."manager_menu_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."manager_menu" (
  "id" int4 NOT NULL DEFAULT nextval('manager_menu_id_seq'::regclass) PRIMARY KEY,
  "title" varchar(50) COLLATE "pg_catalog"."default",
  "url" varchar(200) COLLATE "pg_catalog"."default",
  "parent_id" int4,
  "sort_no" int4,
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "type" int2,
  "perm_name" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."manager_menu" OWNER TO "postgres";
COMMENT ON COLUMN "public"."manager_menu"."type" IS '菜单类别  1：菜单目录  2：外部跳转页面，3: 页面  4：操作功能';
COMMENT ON COLUMN "public"."manager_menu"."perm_name" IS '权限名称';
COMMENT ON TABLE "public"."manager_menu" IS '总控菜单';

CREATE SEQUENCE "public"."manager_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."manager_user" (
  "id" int4 NOT NULL DEFAULT nextval('manager_user_id_seq'::regclass)PRIMARY KEY,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "password" varchar(50) COLLATE "pg_catalog"."default",
  "password2" varchar(50) COLLATE "pg_catalog"."default",
  "salt" varchar(10) COLLATE "pg_catalog"."default",
  "status" int2,
  "create_datetime" timestamp(6),
  "type" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."manager_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "manager_user_unique" ON "public"."manager_user" USING btree ("username");
COMMENT ON COLUMN "public"."manager_user"."status" IS '状态  1 停用  2启用';
COMMENT ON COLUMN "public"."manager_user"."type" IS '1=超级管理员，10=普通';
COMMENT ON COLUMN "public"."manager_user"."password2" IS '二级密码';
COMMENT ON TABLE "public"."manager_user" IS '总控管理员';

INSERT INTO "public"."manager_user"("id", "username", "password", "salt", "status", "create_datetime", "type") VALUES (1, 'root', '6ff38c528c897a72abe8bdd65c6f489c', '1KG1ZPYtUl', 2, '2019-03-20 15:11:02', 1);
select setval('manager_user_id_seq',2,true);

CREATE SEQUENCE "public"."manager_user_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."manager_user_auth" (
  "id" int4 NOT NULL DEFAULT nextval('manager_user_auth_id_seq'::regclass)PRIMARY KEY,
  "user_id" int4,
  "menu_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."manager_user_auth" OWNER TO "postgres";

CREATE SEQUENCE "public"."sys_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_config" (
  "id" int4 NOT NULL DEFAULT nextval('sys_config_id_seq'::regclass)PRIMARY KEY,
  "key" varchar(50) COLLATE "pg_catalog"."default",
  "value" varchar(100) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_config" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_config_key" ON "public"."sys_config" USING btree ("key");
COMMENT ON COLUMN "public"."sys_config"."key" IS '唯一键';
COMMENT ON COLUMN "public"."sys_config"."value" IS '值';
COMMENT ON TABLE "public"."sys_config" IS '系统配置信息';


CREATE SEQUENCE "public"."sys_log_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_log" (
  "id" int4 NOT NULL DEFAULT nextval('sys_log_id_seq'::regclass)PRIMARY KEY ,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "user_type" int2,
  "type" int2,
  "create_datetime" timestamp(6),
  "ip" varchar(100) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "params" text COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_log" OWNER TO "postgres";

COMMENT ON COLUMN "public"."sys_log"."user_id" IS '操作员ID';
COMMENT ON COLUMN "public"."sys_log"."username" IS '操作员';
COMMENT ON COLUMN "public"."sys_log"."params" IS '参数';
COMMENT ON COLUMN "public"."sys_log"."user_type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."sys_log"."partner_id" IS '合作商';
COMMENT ON COLUMN "public"."sys_log"."agent_name" IS '站点下代理商名称';
COMMENT ON TABLE "public"."sys_log" IS '操作日志';

CREATE OR REPLACE FUNCTION "public"."create_sys_log_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
  RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
    (CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
      INHERITS (sys_log);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE INDEX '||tablename||'_create_datetime ON ' ||tablename||' USING btree(create_datetime);' ;
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_user_id_type_station_id ON '||tablename||' USING btree(station_id,user_id,type,user_type);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


CREATE OR REPLACE FUNCTION "public"."insert_sys_log_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO sys_log_'||to_char(NEW.create_datetime,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER "insert_sys_log_partition_trigger" BEFORE INSERT ON "public"."sys_log"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_sys_log_partition"();

CREATE SEQUENCE "public"."sys_login_log_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_login_log" (
  "id" int4 NOT NULL DEFAULT nextval('sys_login_log_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "user_type" int2,
  "create_datetime" timestamp(6),
  "login_ip" varchar(200) COLLATE "pg_catalog"."default",
  "login_os" varchar(200) COLLATE "pg_catalog"."default",
  "domain" varchar(200) COLLATE "pg_catalog"."default",
  "user_agent" varchar(500) COLLATE "pg_catalog"."default",
  "status" int2,
  "remark" varchar(500) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_login_log" OWNER TO "postgres";
CREATE INDEX "sys_login_log_uc" ON "public"."sys_login_log" USING btree ("username","create_datetime");
COMMENT ON COLUMN "public"."sys_login_log"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."sys_login_log"."username" IS '会员账号';
COMMENT ON COLUMN "public"."sys_login_log"."user_type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."sys_login_log"."login_ip" IS '操作员IP';
COMMENT ON COLUMN "public"."sys_login_log"."login_os" IS '登录系统';
COMMENT ON COLUMN "public"."sys_login_log"."domain" IS '域名';
COMMENT ON COLUMN "public"."sys_login_log"."user_agent" IS '浏览器类型';
COMMENT ON COLUMN "public"."sys_login_log"."status" IS '1=失败，2=成功';
COMMENT ON COLUMN "public"."sys_login_log"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_login_log"."partner_id" IS '合作商';
COMMENT ON COLUMN "public"."sys_login_log"."agent_name" IS '站点下代理商名称';
COMMENT ON TABLE "public"."sys_login_log" IS '登陆日志';



CREATE SEQUENCE "public"."partner_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."partner" (
  "id" int4 NOT NULL DEFAULT nextval('partner_id_seq'::regclass)PRIMARY KEY,
  "name" varchar(50),
  "remark" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."partner" OWNER TO "postgres";
COMMENT ON COLUMN "public"."partner"."name" IS '合作商';
COMMENT ON COLUMN "public"."partner"."remark" IS '备注';
COMMENT ON TABLE "public"."partner" IS '合作商信息表';

CREATE SEQUENCE "public"."partner_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."partner_user" (
  "id" int4 NOT NULL DEFAULT nextval('partner_user_id_seq'::regclass)PRIMARY KEY,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "password" varchar(50) COLLATE "pg_catalog"."default",
  "password2" varchar(50) COLLATE "pg_catalog"."default",
  "salt" varchar(10) COLLATE "pg_catalog"."default",
  "status" int2,
  "create_datetime" timestamp(6),
  "type" int2,
  "partner_id" int4,
  "original" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."partner_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "partner_user_unique" ON "public"."partner_user" USING btree ("username","partner_id");
COMMENT ON COLUMN "public"."partner_user"."status" IS '状态  1 停用  2启用';
COMMENT ON COLUMN "public"."partner_user"."type" IS 'UserTypeEnum的值';
COMMENT ON COLUMN "public"."partner_user"."password2" IS '二级密码';
COMMENT ON COLUMN "public"."partner_user"."partner_id" IS '合作商id';
COMMENT ON COLUMN "public"."partner_user"."original" IS '默认账号，2=默认，1=后期添加';
COMMENT ON TABLE "public"."partner_user" IS '合作商后台管理员';


CREATE SEQUENCE "public"."partner_menu_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."partner_menu" (
  "id" int4 NOT NULL DEFAULT nextval('partner_menu_id_seq'::regclass) PRIMARY KEY,
  "title" varchar(50) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "url" varchar(200) COLLATE "pg_catalog"."default",
  "parent_id" int4,
  "sort_no" int4,
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "type" int2,
  "perm_name" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."partner_menu" OWNER TO "postgres";
COMMENT ON COLUMN "public"."partner_menu"."type" IS '菜单类别  1：菜单目录  2：外部跳转页面，3: 页面  4：操作功能';
COMMENT ON COLUMN "public"."partner_menu"."perm_name" IS '权限名称';
COMMENT ON COLUMN "public"."partner_menu"."code" IS '权限名称多语言的code';
COMMENT ON TABLE "public"."partner_menu" IS '合作商后台菜单';

CREATE SEQUENCE "public"."partner_user_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."partner_user_auth" (
  "id" int4 NOT NULL DEFAULT nextval('partner_user_auth_id_seq'::regclass)PRIMARY KEY,
  "user_id" int4,
  "menu_id" int4,
  "partner_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."partner_user_auth" OWNER TO "postgres";

CREATE SEQUENCE "public"."partner_white_ip_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."partner_white_ip" (
  "id" int4 NOT NULL DEFAULT nextval('partner_white_ip_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "ip" varchar(200),
  "type" int2,
  "create_datetime" timestamp(6) NULL,
  "remark" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."partner_white_ip" OWNER TO "postgres";
COMMENT ON COLUMN "public"."partner_white_ip"."ip" IS '白名单ip';
COMMENT ON COLUMN "public"."partner_white_ip"."remark" IS '备注';
COMMENT ON COLUMN "public"."partner_white_ip"."type" IS '2:白名单 ，1：黑名单';
COMMENT ON TABLE "public"."partner_white_ip" IS '合作商后台ip白名单';
CREATE UNIQUE INDEX "partner_white_ip_uindex" ON "public"."partner_white_ip" ("ip","partner_id");

CREATE SEQUENCE "public"."station_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station" (
  	"id" int4 not null default nextval('station_id_seq') PRIMARY KEY,
	"name" varchar(100) COLLATE "default",
	"code" varchar(10) COLLATE "default",
	"create_datetime" timestamp(6) NULL,
	"status" int2,
	"bg_status" int2 DEFAULT 2,
	"language" varchar(10) COLLATE "default",
	"currency" varchar(10) COLLATE "default",
	"partner_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station" OWNER TO "postgres";
COMMENT ON TABLE "public"."station" IS '站点信息';
COMMENT ON COLUMN "public"."station"."name" IS '站点名称';
COMMENT ON COLUMN "public"."station"."code" IS '站点编号';
COMMENT ON COLUMN "public"."station"."status" IS '站点状态  1-关闭  2-开启';
COMMENT ON COLUMN "public"."station"."bg_status" IS '站点后台状态1=关闭，2=启用';
COMMENT ON COLUMN "public"."station"."language" IS '站点默认语种';
COMMENT ON COLUMN "public"."station"."currency" IS '站点币种';
CREATE UNIQUE INDEX "station_code_uniquekey" ON "public"."station" ("code");


CREATE SEQUENCE "public"."station_domain_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_domain" (
  "id" int4 not null default nextval('station_domain_id_seq') PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "agent_promo_code" varchar(50) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "type" int2,
  "default_home" varchar(200) COLLATE "pg_catalog"."default",
  "proxy_id" int4,
  "proxy_username" varchar(50) COLLATE "pg_catalog"."default",
  "status" int2,
  "switch_domain" int2,
  "sort_no" int4,
  "remark" text COLLATE "pg_catalog"."default",
  "ip_mode" int2,
  "platform" int2 DEFAULT 2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_domain" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_domain_uniquekey" ON "public"."station_domain" USING btree ("name");
COMMENT ON COLUMN "public"."station_domain"."partner_id" IS '合作商id';
COMMENT ON COLUMN "public"."station_domain"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_domain"."agent_promo_code" IS '代理商名称';
COMMENT ON COLUMN "public"."station_domain"."agent_promo_code" IS '代理商的推广码';
COMMENT ON COLUMN "public"."station_domain"."name" IS '域名';
COMMENT ON COLUMN "public"."station_domain"."type" IS '域名类型(1-站点通用，2-站点前台,3-站点后台,4-合作商后台)';
COMMENT ON COLUMN "public"."station_domain"."default_home" IS '前台默认主页，不填时默认访问index.do';
COMMENT ON COLUMN "public"."station_domain"."proxy_id" IS '所属代理id';
COMMENT ON COLUMN "public"."station_domain"."proxy_username" IS '所属代理账号';
COMMENT ON COLUMN "public"."station_domain"."status" IS '状态 1-停用 2-启用';
COMMENT ON COLUMN "public"."station_domain"."switch_domain" IS '线路检测开关';
COMMENT ON COLUMN "public"."station_domain"."sort_no" IS '序号';
COMMENT ON COLUMN "public"."station_domain"."remark" IS '备注内容';
COMMENT ON COLUMN "public"."station_domain"."ip_mode" IS '获取ip的模式，1=安全模式，2=普通模式';
COMMENT ON COLUMN "public"."station_domain"."platform" IS '平台类型，1=合作商后台域名，2=站点域名';
COMMENT ON TABLE "public"."station_domain" IS '站点域名信息';


CREATE SEQUENCE "public"."admin_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_user" (
  	"id" int4 not null default nextval('admin_user_id_seq') PRIMARY KEY,
  	"partner_id" int4,
  	"station_id" int4,
	"username" varchar(50) COLLATE "default",
	"password" varchar(50) COLLATE "default",
	"receipt_password" varchar(50) COLLATE "default",
	"salt" varchar(10) COLLATE "default",
	"group_id" int4,
	"status" int2,
	"creator_id" int4,
	"create_datetime" timestamp(6) NULL,
	"type" int2,
	"create_ip" varchar(200) COLLATE "default",
	"last_login_ip" varchar(200),
  	"last_login_time" timestamp(6),
	"remark" varchar(100),
	"add_money_limit" numeric(20,6),
	"deposit_limit" numeric(16,2),
  	"withdraw_limit" numeric(16,2),
	"original" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."admin_user" OWNER TO "postgres";
COMMENT ON TABLE "public"."admin_user" IS '站点管理员信息';
COMMENT ON COLUMN "public"."admin_user"."partner_id" IS '合作商id';
COMMENT ON COLUMN "public"."admin_user"."group_id" IS '分组id';
COMMENT ON COLUMN "public"."admin_user"."status" IS '1=禁用，2=启用';
COMMENT ON COLUMN "public"."admin_user"."creator_id" IS '创建者id';
COMMENT ON COLUMN "public"."admin_user"."create_datetime" IS '添加时间';
COMMENT ON COLUMN "public"."admin_user"."type" IS '类型，看UserTypeEnum ';
COMMENT ON COLUMN "public"."admin_user"."last_login_ip" IS '最后登陆ip';
COMMENT ON COLUMN "public"."admin_user"."last_login_time" IS '最后登陆时间';
COMMENT ON COLUMN "public"."admin_user"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."admin_user"."receipt_password" IS '二级密码';
COMMENT ON COLUMN "public"."admin_user"."add_money_limit" IS '手动加款金额上限';
COMMENT ON COLUMN "public"."admin_user"."original" IS '默认账号，2=默认，1=后期添加';
COMMENT ON COLUMN "public"."admin_user"."deposit_limit" IS '处理单笔入款上限';
COMMENT ON COLUMN "public"."admin_user"."withdraw_limit" IS '处理单笔出款上限';
CREATE UNIQUE INDEX  "admin_user_un_s" ON "public"."admin_user" USING btree(username,station_id);

CREATE SEQUENCE "public"."admin_menu_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_menu" (
  	"id" int4 not null default nextval('admin_menu_id_seq') PRIMARY KEY,
	"title" varchar(50) COLLATE "default",
	"code" varchar(50) COLLATE "default",
	"url" varchar(200) COLLATE "default",
	"parent_id" int4,
	"sort_no" int4,
	"icon" varchar(200) COLLATE "default",
	"perm_name" varchar(50) COLLATE "default",
	"type" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."admin_menu" OWNER TO "postgres";
COMMENT ON TABLE "public"."admin_menu" IS '站点菜单信息';
COMMENT ON TABLE "public"."admin_menu" IS 'admin user 权限表';
COMMENT ON COLUMN "public"."admin_menu"."title" IS '菜单名称';
COMMENT ON COLUMN "public"."admin_menu"."code" IS '菜单名称多语言的code';
COMMENT ON COLUMN "public"."admin_menu"."sort_no" IS '序号';
COMMENT ON COLUMN "public"."admin_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."admin_menu"."perm_name" IS '权限名称';
COMMENT ON COLUMN "public"."admin_menu"."type" IS '菜单类别  1：菜单目录  2：外部跳转页面，3: 页面  4：操作功能';


CREATE SEQUENCE "public"."admin_user_group_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_user_group" (
  	"id" int4 not null default nextval('admin_user_group_id_seq') PRIMARY KEY,
	"name" varchar(20) COLLATE "default",
	"partner_id" int4,
  	"station_id" int4,
	"type" int2 DEFAULT 2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."admin_user_group" OWNER TO "postgres";
COMMENT ON TABLE "public"."admin_user_group" IS '站点管理员组别信息';
COMMENT ON COLUMN "public"."admin_user_group"."name" IS '分组名称';
COMMENT ON COLUMN "public"."admin_user_group"."type" IS '1=总控可操作，2=合作商只可看，3=合作商可操作，4=站点只可看，5=站点可修改权限';

CREATE SEQUENCE "public"."admin_user_group_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_user_group_auth" (
  	"id" int4 not null default nextval('admin_user_group_auth_id_seq') PRIMARY KEY,
  	"partner_id" int4,
  	"station_id" int4,
	"group_id" int4,
	"menu_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."admin_user_group_auth" OWNER TO "postgres";
COMMENT ON TABLE "public"."admin_user_group_auth" IS '站点管理员组别权限信息';
CREATE UNIQUE INDEX  "admin_uga_gas" ON "public"."admin_user_group_auth" USING btree(group_id, menu_id,station_id);

CREATE SEQUENCE "public"."admin_white_ip_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."admin_white_ip" (
  "id" int4 not null default nextval('admin_white_ip_id_seq') PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "ip" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "type" int2,
  "remark" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."admin_white_ip" OWNER TO "postgres";
COMMENT ON COLUMN "public"."admin_white_ip"."remark" IS '备注内容';
COMMENT ON COLUMN "public"."admin_white_ip"."type" IS '2:白名单 ，1：黑名单';
CREATE UNIQUE INDEX "admin_white_ip_uk" ON "public"."admin_white_ip" ("ip","station_id");


CREATE SEQUENCE "public"."station_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_config" (
  	"id" int4 not null default nextval('station_config_id_seq') PRIMARY KEY,
  	"partner_id" int4,
  	"station_id" int4,
	"key" varchar(50) COLLATE "default",
	"title" varchar(100),
	"group_name" varchar(20),
	"value" text COLLATE "default",
	"ele_type" varchar(50),
  	"visible" int2,
  	"sort_no" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_config" OWNER TO "postgres";
COMMENT ON TABLE "public"."station_config" IS '站点配置信息';
COMMENT ON COLUMN "public"."station_config"."key" IS '唯一键';
COMMENT ON COLUMN "public"."station_config"."value" IS '值';
COMMENT ON COLUMN "public"."station_config"."group_name" IS '分组名称';
COMMENT ON COLUMN "public"."station_config"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_config"."ele_type" IS '前端输入的类型（text, switchSelect)';
COMMENT ON COLUMN "public"."station_config"."title" IS '描述标题';
COMMENT ON COLUMN "public"."station_config"."visible" IS '1=站点不可见，2=站点可见';
COMMENT ON COLUMN "public"."station_config"."sort_no" IS '序号';
CREATE UNIQUE INDEX "station_config_skey" ON "public"."station_config" ("station_id","key");


CREATE SEQUENCE "public"."announcement_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."announcement" (
  	"id" int4 not null default nextval('announcement_id_seq') PRIMARY KEY,
	"type" int2,
	"content" text COLLATE "default",
	"create_datetime" timestamp(6) NULL,
	"start_datetime" timestamp(6) NULL,
	"end_datetime" timestamp(6) NULL,
	"accept_type" varchar(50) COLLATE "pg_catalog"."default",
	"dialog_flag" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."announcement" OWNER TO "postgres";
COMMENT ON TABLE "public"."announcement" IS '站点公告';
COMMENT ON COLUMN "public"."announcement"."type" IS '类别，1=群发，2=个别站点';
COMMENT ON COLUMN "public"."announcement"."content" IS '内容';
COMMENT ON COLUMN "public"."announcement"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."announcement"."start_datetime" IS '开始时间';
COMMENT ON COLUMN "public"."announcement"."end_datetime" IS '结束时间';
COMMENT ON COLUMN "public"."announcement"."dialog_flag" IS '是否弹出1是2否';
COMMENT ON COLUMN "public"."announcement"."accept_type" IS '接收类型 1：真人 2:电子 3：体育 4：棋牌 5:彩票';


CREATE SEQUENCE "public"."announcement_station_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."announcement_station" (
  "id" int4 not null default nextval('announcement_station_id_seq') PRIMARY KEY,
  "station_id" int4,
  "announcement_id" int4,
  "status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."announcement_station" OWNER TO "postgres";
COMMENT ON COLUMN "public"."announcement_station"."status" IS '已读状态 1未读 2已读';



CREATE SEQUENCE "public"."mny_deposit_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."mny_deposit_record" (
  "id" int4 NOT NULL DEFAULT nextval('mny_deposit_record_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50),
  "parent_ids" text COLLATE "pg_catalog"."default",
  "recommend_id" int4,
  "user_id" int4,
  "username" varchar(100) COLLATE "default",
  "degree_id" int4,
  "user_type" int2,
  "order_id" varchar(255) COLLATE "default",
  "money" numeric(20,6),
  "gift_money" numeric(20,6),
  "create_user_id" int4,
  "create_datetime" timestamp(6),
  "status" int2,
  "deposit_type" int2,
  "depositor" varchar(200) COLLATE "pg_catalog"."default",
  "lock_flag" int2,
  "pay_id" int4,
  "pay_name" varchar(50) COLLATE "pg_catalog"."default",
  "bank_way" int2,
  "bank_address" varchar(255) COLLATE "pg_catalog"."default",
  "pay_bank_name" varchar(255) COLLATE "pg_catalog"."default",
  "pay_platform_code" varchar(50) COLLATE "pg_catalog"."default",
  "handler_type" int2 DEFAULT 2,
  "op_username" varchar(50) COLLATE "pg_catalog"."default",
  "op_user_id" int4,
  "op_datetime" timestamp(6),
  "user_remark" text,
  "remark" varchar(200) COLLATE "pg_catalog"."default",
  "bg_remark" varchar(500) COLLATE "pg_catalog"."default",
  "deposit_virtual_coin_num" numeric(20,2),
  "deposit_virtual_coin_rate" numeric(20,2)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."mny_deposit_record" OWNER TO "postgres";
CREATE INDEX "mny_deposit_record_cdt" ON "public"."mny_deposit_record" USING btree ("create_datetime");
CREATE UNIQUE INDEX "mny_deposit_record_oid" ON "public"."mny_deposit_record" USING btree ("order_id");
CREATE INDEX "mny_deposit_record_st" ON "public"."mny_deposit_record" USING btree ("station_id","user_id","deposit_type");
COMMENT ON COLUMN "public"."mny_deposit_record"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."agent_name" IS '代理商名称';
COMMENT ON COLUMN "public"."mny_deposit_record"."parent_ids" IS '代理层级关系';
COMMENT ON COLUMN "public"."mny_deposit_record"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."username" IS '用户账号';
COMMENT ON COLUMN "public"."mny_deposit_record"."degree_id" IS '会员层级ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."user_type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."mny_deposit_record"."order_id" IS '订单号';
COMMENT ON COLUMN "public"."mny_deposit_record"."money" IS '存入金额';
COMMENT ON COLUMN "public"."mny_deposit_record"."gift_money" IS '赠送彩金，优惠彩金';
COMMENT ON COLUMN "public"."mny_deposit_record"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."mny_deposit_record"."create_user_id" IS '创建人';
COMMENT ON COLUMN "public"."mny_deposit_record"."status" IS '状态，1=未处理，2=处理成功，3=处理失败，4=已取消，5=已过期';
COMMENT ON COLUMN "public"."mny_deposit_record"."deposit_type" IS '支付类型，1=在线支付，2=快速入款，3=银行转账，4=手动加款';
COMMENT ON COLUMN "public"."mny_deposit_record"."depositor" IS '汇款人姓名';
COMMENT ON COLUMN "public"."mny_deposit_record"."lock_flag" IS '锁定（1未锁，2锁定）';
COMMENT ON COLUMN "public"."mny_deposit_record"."pay_id" IS '支付方式ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."pay_name" IS '支付方式名字';
COMMENT ON COLUMN "public"."mny_deposit_record"."bank_way" IS '银行转账方式  1:网银转账，2  ATM入款 3：银行柜台 4：手机转账 5：支付宝';
COMMENT ON COLUMN "public"."mny_deposit_record"."bank_address" IS '银行所属分行';
COMMENT ON COLUMN "public"."mny_deposit_record"."pay_bank_name" IS '在线支付收银台支付名称';
COMMENT ON COLUMN "public"."mny_deposit_record"."handler_type" IS '处理方式（1、手动处理,2、系统处理）';
COMMENT ON COLUMN "public"."mny_deposit_record"."op_username" IS '操作人账号';
COMMENT ON COLUMN "public"."mny_deposit_record"."op_user_id" IS '操作人ID';
COMMENT ON COLUMN "public"."mny_deposit_record"."op_datetime" IS '操作时间';
COMMENT ON COLUMN "public"."mny_deposit_record"."user_remark" IS '用户前台入款时备注';
COMMENT ON COLUMN "public"."mny_deposit_record"."remark" IS '前台可以看到的备注';
COMMENT ON COLUMN "public"."mny_deposit_record"."bg_remark" IS '只有后台可见的备注';
COMMENT ON COLUMN "public"."mny_deposit_record"."recommend_id" IS '推荐者id';
COMMENT ON COLUMN "public"."mny_deposit_record"."deposit_virtual_coin_num" IS '存款虚拟币数量';
COMMENT ON COLUMN "public"."mny_deposit_record"."deposit_virtual_coin_rate" IS '存款虚拟币汇率';
COMMENT ON TABLE "public"."mny_deposit_record" IS '用户充值记录';


CREATE SEQUENCE "public"."mny_draw_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."mny_draw_record" (
  "id" int4 NOT NULL DEFAULT nextval('mny_draw_record_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50),
  "parent_ids" text COLLATE "pg_catalog"."default",
  "recommend_id" int4,
  "user_id" int4,
  "username" varchar(100) COLLATE "default",
  "degree_id" int4,
  "user_type" int2,
  "second_review" int2 default 1,
  "record_ip" varchar(50),
  "record_os" varchar(200),
  "type" int2,
  "order_id" varchar(36) COLLATE "default",
  "draw_money" numeric(20,6),
  "fee_money" numeric(20,6),
  "true_money" numeric(20,6),
  "status" int2,
  "create_user_id" int4,
  "create_datetime" timestamp(6),
  "lock_flag" int2,
  "money_record_id" int4,
  "bank_name" varchar(50) COLLATE "pg_catalog"."default",
  "real_name" varchar(500) COLLATE "pg_catalog"."default",
  "card_no" varchar(500) COLLATE "pg_catalog"."default",
  "op_username" varchar(50) COLLATE "pg_catalog"."default",
  "op_user_id" int4,
  "op_datetime" timestamp(6),
  "user_remark" text COLLATE "pg_catalog"."default",
  "remark" varchar(200) COLLATE "pg_catalog"."default",
  "pay_id" int4,
  "pay_name" varchar(200) COLLATE "default",
  "bg_remark" varchar(500) COLLATE "pg_catalog"."default",
  "withdraw_virtual_coin_num" numeric(20,2),
  "withdraw_virtual_coin_rate" numeric(20,2)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."mny_draw_record" OWNER TO "postgres";
CREATE INDEX "mny_draw_record_as" ON "public"."mny_draw_record" USING btree ("user_id","station_id");
CREATE UNIQUE INDEX "mny_draw_record_oid" ON "public"."mny_draw_record" USING btree ("order_id");
CREATE INDEX "mny_draw_record_cdt" ON "public"."mny_draw_record" USING btree ("create_datetime");
COMMENT ON COLUMN "public"."mny_draw_record"."recommend_id" IS '推荐者id';
COMMENT ON COLUMN "public"."mny_draw_record"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."mny_draw_record"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."mny_draw_record"."agent_name" IS '代理商名称';
COMMENT ON COLUMN "public"."mny_draw_record"."second_review" IS '是否需要二次审核 2--需要 1--不需要';
COMMENT ON COLUMN "public"."mny_draw_record"."parent_ids" IS '代理层级关系';
COMMENT ON COLUMN "public"."mny_draw_record"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."mny_draw_record"."username" IS '用户账号';
COMMENT ON COLUMN "public"."mny_draw_record"."degree_id" IS '会员层级ID';
COMMENT ON COLUMN "public"."mny_draw_record"."user_type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."mny_draw_record"."order_id" IS '订单号';
COMMENT ON COLUMN "public"."mny_draw_record"."draw_money" IS '出款金额';
COMMENT ON COLUMN "public"."mny_draw_record"."fee_money" IS '手续费';
COMMENT ON COLUMN "public"."mny_draw_record"."true_money" IS '实际出款金额';
COMMENT ON COLUMN "public"."mny_draw_record"."status" IS '状态，1=未处理，2=处理成功，3=处理失败，4=已过期';
COMMENT ON COLUMN "public"."mny_draw_record"."create_user_id" IS '创建者';
COMMENT ON COLUMN "public"."mny_draw_record"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."mny_draw_record"."lock_flag" IS '锁定（1未锁，2锁定）';
COMMENT ON COLUMN "public"."mny_draw_record"."money_record_id" IS '账变记录ID';
COMMENT ON COLUMN "public"."mny_draw_record"."bank_name" IS '银行名称';
COMMENT ON COLUMN "public"."mny_draw_record"."real_name" IS '用户姓名';
COMMENT ON COLUMN "public"."mny_draw_record"."card_no" IS '银行卡号';
COMMENT ON COLUMN "public"."mny_draw_record"."op_username" IS '操作人账号';
COMMENT ON COLUMN "public"."mny_draw_record"."op_user_id" IS '操作人ID';
COMMENT ON COLUMN "public"."mny_draw_record"."op_datetime" IS '操作时间';
COMMENT ON COLUMN "public"."mny_draw_record"."user_remark" IS '用户前台入款时备注';
COMMENT ON COLUMN "public"."mny_draw_record"."remark" IS '前台可以看到的备注';
COMMENT ON COLUMN "public"."mny_draw_record"."bg_remark" IS '只有后台可见的备注';
COMMENT ON COLUMN "public"."mny_draw_record"."pay_id" IS '代付id';
COMMENT ON COLUMN "public"."mny_draw_record"."pay_name" IS '代付名';
COMMENT ON COLUMN "public"."mny_draw_record"."withdraw_virtual_coin_num" IS '取款虚拟币数量';
COMMENT ON COLUMN "public"."mny_draw_record"."withdraw_virtual_coin_rate" IS '取款虚拟币汇率';
COMMENT ON COLUMN "public"."mny_draw_record"."record_ip" IS '提交记录时的ip';
COMMENT ON COLUMN "public"."mny_draw_record"."second_review" IS '是否需要二次审核 2--需要 1--不需要';
COMMENT ON COLUMN "public"."mny_draw_record"."record_os" IS  '提交记录时的设备操作系统';
COMMENT ON TABLE "public"."mny_draw_record" IS '用户提款记录';

CREATE SEQUENCE "public"."agent_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."agent" (
  "id" int4 NOT NULL DEFAULT nextval('agent_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "create_user" varchar(50) COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "status" int2,
  "promo_code" varchar(50),
  "access_page" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."agent" OWNER TO "postgres";
COMMENT ON COLUMN "public"."agent"."station_id" IS '所属站点';
COMMENT ON COLUMN "public"."agent"."name" IS '代理商名称，作为推广码';
COMMENT ON COLUMN "public"."agent"."remark" IS '备注';
COMMENT ON COLUMN "public"."agent"."promo_code" IS '推广码';
COMMENT ON COLUMN "public"."agent"."access_page" IS '链接访问页面 1:注册页面 2：首页 3:优惠活动';
COMMENT ON TABLE "public"."agent" IS '站点代理商信息表';
CREATE UNIQUE INDEX "agent_sid_name" ON "public"."agent" USING btree ("station_id","name");
CREATE UNIQUE INDEX "agent_sid_pc" ON "public"."agent" USING btree ("station_id","promo_code");

CREATE SEQUENCE "public"."agent_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."agent_user" (
  "id" int4 NOT NULL DEFAULT nextval('agent_user_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "agent_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "password" varchar(50) COLLATE "pg_catalog"."default",
  "salt" varchar(10) COLLATE "pg_catalog"."default",
  "real_name" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "create_user" varchar(50) COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "type" int2,
  "status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."agent_user" OWNER TO "postgres";
COMMENT ON TABLE "public"."agent_user" IS '站点下代理商账号表';
CREATE UNIQUE INDEX "agent_user_sid_unkey" ON "public"."agent_user" (
  "station_id",
  "username"
);

CREATE SEQUENCE "public"."agent_menu_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."agent_menu" (
  "id" int4 NOT NULL DEFAULT nextval('agent_menu_id_seq'::regclass) PRIMARY KEY,
  "title" varchar(50) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "url" varchar(200) COLLATE "pg_catalog"."default",
  "parent_id" int4,
  "sort_no" int4,
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "type" int2,
  "perm_name" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."agent_menu" OWNER TO "postgres";
COMMENT ON TABLE "public"."agent_menu" IS '站点下代理商后台菜单';
COMMENT ON COLUMN "public"."agent_menu"."title" IS '菜单名称';
COMMENT ON COLUMN "public"."agent_menu"."code" IS '菜单名称多语言的code';
COMMENT ON COLUMN "public"."agent_menu"."sort_no" IS '序号';
COMMENT ON COLUMN "public"."agent_menu"."icon" IS '图标';
COMMENT ON COLUMN "public"."agent_menu"."perm_name" IS '权限名称';
COMMENT ON COLUMN "public"."agent_menu"."type" IS '菜单类别  1：菜单目录  2：外部跳转页面，3: 页面  4：操作功能';


CREATE SEQUENCE "public"."agent_white_ip_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."agent_white_ip" (
  "id" int4 NOT NULL DEFAULT nextval('agent_white_ip_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "agent_id" int4,
  "ip" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "type" int2,
  "remark" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."agent_white_ip" OWNER TO "postgres";
COMMENT ON COLUMN "public"."agent_white_ip"."type" IS '2:白名单 ，1：黑名单';
COMMENT ON TABLE "public"."agent_white_ip" IS '站点前台IP白名单列表';
CREATE UNIQUE INDEX "agent_white_ip_unique" ON "public"."agent_white_ip" (
  "agent_id",
  "ip"
);
  
CREATE SEQUENCE "public"."sys_guest_id_seq" INCREMENT 1 START 1000 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE SEQUENCE "public"."sys_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass) PRIMARY KEY,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "type" int2,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "partner_id" int4,
  "proxy_id" int4 DEFAULT 0,
  "parent_ids" text COLLATE "pg_catalog"."default",
  "parent_names" text COLLATE "pg_catalog"."default",
  "recommend_id" int4 DEFAULT 0,
  "recommend_username" varchar(50) COLLATE "pg_catalog"."default",
  "degree_id" int4,
  "group_id" int4,
  "password" varchar(200) COLLATE "pg_catalog"."default",
  "salt" varchar(20) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "create_user_id" int4,
  "create_username" varchar(50) COLLATE "pg_catalog"."default",
  "status" int2,
  "remark" text COLLATE "pg_catalog"."default",
  "lock_degree" int2,
  "level" int2,
  "online_warn" int2,
  "update_pwd_datetime" timestamp(6),
  "money_status" int2,
  "register_ip" varchar(100) COLLATE "default",
  "register_url" varchar(255) COLLATE "pg_catalog"."default",
  "register_os" varchar(500) COLLATE "pg_catalog"."default",
  "promo_code" varchar(20) COLLATE "pg_catalog"."default",
  "uid" varchar(30) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "user_register_type" varchar(16)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_user_unkey" ON "public"."sys_user" USING btree ("username","station_id");
CREATE INDEX "sys_user_cdt" ON "public"."sys_user" USING btree ("create_datetime");
COMMENT ON COLUMN "public"."sys_user"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."sys_user"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user"."agent_name" IS '代理商名称';
COMMENT ON COLUMN "public"."sys_user"."proxy_id" IS '上级代理ID';
COMMENT ON COLUMN "public"."sys_user"."proxy_name" IS '上级代理账号';
COMMENT ON COLUMN "public"."sys_user"."parent_ids" IS '所有代理上级，以逗号隔开(,1,2,3,)';
COMMENT ON COLUMN "public"."sys_user"."parent_names" IS '所有上级代理账号，以逗号隔开(,a,b,c,)';
COMMENT ON COLUMN "public"."sys_user"."recommend_id" IS '推荐会员ID';
COMMENT ON COLUMN "public"."sys_user"."recommend_username" IS '推荐会员账号';
COMMENT ON COLUMN "public"."sys_user"."degree_id" IS '会员等级ID';
COMMENT ON COLUMN "public"."sys_user"."group_id" IS '会员组别ID';
COMMENT ON COLUMN "public"."sys_user"."username" IS '账号';
COMMENT ON COLUMN "public"."sys_user"."password" IS '密码';
COMMENT ON COLUMN "public"."sys_user"."salt" IS '密码的盐';
COMMENT ON COLUMN "public"."sys_user"."type" IS '用户类型(UserTypeEnum的值)';
COMMENT ON COLUMN "public"."sys_user"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user"."create_user_id" IS '创建者ID';
COMMENT ON COLUMN "public"."sys_user"."create_username" IS '创建者账号';
COMMENT ON COLUMN "public"."sys_user"."status" IS '账号状态 1:禁用 2:启用';
COMMENT ON COLUMN "public"."sys_user"."remark" IS '备注内容';
COMMENT ON COLUMN "public"."sys_user"."lock_degree" IS '2=锁定会员等级，1=不锁定';
COMMENT ON COLUMN "public"."sys_user"."level" IS '会员层级';
COMMENT ON COLUMN "public"."sys_user"."online_warn" IS '在线告警';
COMMENT ON COLUMN "public"."sys_user"."update_pwd_datetime" IS '最后一次修改密码时间';
COMMENT ON COLUMN "public"."sys_user"."money_status" IS '账户余额状态，1=冻结，2=正常';
COMMENT ON COLUMN "public"."sys_user"."register_ip" IS '注册IP';
COMMENT ON COLUMN "public"."sys_user"."register_url" IS '注册URL';
COMMENT ON COLUMN "public"."sys_user"."register_os" IS '注册电脑或手机系统';
COMMENT ON COLUMN "public"."sys_user"."promo_code" IS '推广码(代理商\代理\会员的推广码都保存在这里)';
COMMENT ON COLUMN "public"."sys_user"."email" IS '邮件地址';
COMMENT ON COLUMN "public"."sys_user"."uid" IS '用户UID';
COMMENT ON COLUMN "public"."sys_user"."user_register_type" IS '会员注册类型';
COMMENT ON TABLE "public"."sys_user" IS '会员账号信息';

CREATE TABLE "public"."sys_user_money" (
	"user_id" int4 PRIMARY KEY,
	"money" numeric(20,6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_money" OWNER TO "postgres";
COMMENT ON TABLE "public"."sys_user_money" IS '会员余额信息表';
 

CREATE TABLE "public"."sys_user_login" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "station_id" int4,
  "last_login_datetime" timestamp(6),
  "online_status" int2,
  "last_login_ip" varchar(200) COLLATE "pg_catalog"."default",
  "terminal" int2 DEFAULT 1,
  "domain" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_login" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_login"."last_login_datetime" IS '最后登录时间';
COMMENT ON COLUMN "public"."sys_user_login"."online_status" IS '在线状态：1=下线，2=在线';
COMMENT ON COLUMN "public"."sys_user_login"."last_login_ip" IS '最后登录ip';
COMMENT ON COLUMN "public"."sys_user_login"."terminal" IS '1=电脑端，2=手机web端，3=原生app端';
COMMENT ON TABLE "public"."sys_user_login" IS '存储会员最后登录信息';
COMMENT ON COLUMN "public"."sys_user_login"."domain" IS '登录域名';

CREATE SEQUENCE "public"."sys_user_money_history_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_money_history" (
	"id" int4 not null default nextval('sys_user_money_history_id_seq') PRIMARY KEY,
	"partner_id" int4,
	"station_id" int4,
	"agent_name" varchar(50),
	"parent_ids" text COLLATE "pg_catalog"."default",
	"recommend_id" int4,
	"user_id" int4 NOT NULL,
	"username" varchar(50) COLLATE "default",
	"money" numeric(20,6),
	"before_money" numeric(20,6),
	"after_money" numeric(20,6),
	"type" int2,
	"create_datetime" timestamp(6) NULL,
	"order_id" varchar(255) COLLATE "default",
	"biz_datetime" timestamp(6) NULL,
	"remark" text COLLATE "default",
	"bg_remark" text COLLATE "default",
	"operator_id" int4,
	"operator_name" varchar(50) COLLATE "default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_money_history" OWNER TO "postgres";
COMMENT ON TABLE "public"."sys_user_money_history" IS '会员金额变动表';
COMMENT ON COLUMN "public"."sys_user_money_history"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sys_user_money_history"."before_money" IS '变动前金额';
COMMENT ON COLUMN "public"."sys_user_money_history"."after_money" IS '变动后金额';
COMMENT ON COLUMN "public"."sys_user_money_history"."money" IS '变动金额';
COMMENT ON COLUMN "public"."sys_user_money_history"."type" IS '导致变动的类型';
COMMENT ON COLUMN "public"."sys_user_money_history"."remark" IS '描述';
COMMENT ON COLUMN "public"."sys_user_money_history"."bg_remark" IS '后台备注';
COMMENT ON COLUMN "public"."sys_user_money_history"."order_id" IS '对应订单id';
COMMENT ON COLUMN "public"."sys_user_money_history"."biz_datetime" IS '业务时间';
COMMENT ON COLUMN "public"."sys_user_money_history"."operator_id" IS '操作者用户id';
COMMENT ON COLUMN "public"."sys_user_money_history"."operator_name" IS '操作者账号';
COMMENT ON COLUMN "public"."sys_user_money_history"."recommend_id" IS '推荐者id';

CREATE OR REPLACE FUNCTION "public"."create_money_history_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
			INHERITS (sys_user_money_history);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE INDEX '||tablename||'_uid_type ON ' ||tablename||' USING btree(user_id,type);' ;
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_create_datetime ON '||tablename||' USING btree(create_datetime);';
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_station_id ON '||tablename||' USING btree(station_id);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_money_history_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO sys_user_money_history_'||to_char(NEW.create_datetime,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER "insert_money_history_partition_trigger" BEFORE INSERT ON "public"."sys_user_money_history"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_money_history_partition"();

CREATE OR REPLACE FUNCTION "public"."optmoney"("userid" int4, "optmoney" numeric)
  RETURNS numeric[2] AS $BODY$
    DECLARE 
	mny numeric(20,6);
	sql1 varchar;
	sql2 varchar;
	result numeric[2];
 BEGIN
    sql1 := 'select money from sys_user_money where user_id = ''' || userId ||''' for update ';
    execute sql1 into mny ;
    IF mny + optmoney < 0 AND optmoney < 0 THEN
	result[0] := 0;
	result[1] := mny;
	return result;
    END IF;
    sql2 := 'update sys_user_money set money = money + ''' || optMoney ||''' where  user_id = ''' || userId ||'''';
    execute sql2;
    result[0] := 1;
    result[1] := mny;
    RETURN result;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
CREATE SEQUENCE "public"."sys_user_daily_money_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_daily_money" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_daily_money_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "proxy_id" int4,
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "parent_ids" text COLLATE "pg_catalog"."default",
  "recommend_id" int4,
  "user_id" int4 NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "user_type" int2,
  "stat_date" date NOT NULL,
  "withdraw_amount" numeric(20,6) DEFAULT 0,
  "withdraw_times" int4 DEFAULT 0,
  "withdraw_artificial" numeric(20,6) DEFAULT 0,
  "deposit_amount" numeric(20,6) DEFAULT 0,
  "deposit_times" int4 DEFAULT 0,
  "deposit_artificial" numeric(20,6) DEFAULT 0,
  "deposit_gift_amount" numeric(20,6) DEFAULT 0,
  "deposit_gift_times" int4 DEFAULT 0,
  "deposit_handler_artificial" numeric(20,6) DEFAULT 0,
  "deposit_handler_artificial_times" int4 DEFAULT 0,
  "live_bet_amount" numeric(20,6) DEFAULT 0,
  "live_bet_num" numeric(20,6) DEFAULT 0,
  "live_win_amount" numeric(20,6) DEFAULT 0,
  "live_rebate_amount" numeric(20,6) DEFAULT 0,
  "live_bet_times" int4 DEFAULT 0,
  "live_win_times" int4 DEFAULT 0,
  "egame_bet_amount" numeric(20,6) DEFAULT 0,
  "egame_bet_num" numeric(20,6) DEFAULT 0,
  "egame_win_amount" numeric(20,6) DEFAULT 0,
  "egame_rebate_amount" numeric(20,6) DEFAULT 0,
  "egame_bet_times" int4 DEFAULT 0,
  "egame_win_times" int4 DEFAULT 0,
  "sport_bet_amount" numeric(20,6) DEFAULT 0,
  "sport_bet_num" numeric(20,6) DEFAULT 0,
  "sport_win_amount" numeric(20,6) DEFAULT 0,
  "sport_rebate_amount" numeric(20,6) DEFAULT 0,
  "sport_bet_times" int4 DEFAULT 0,
  "sport_win_times" int4 DEFAULT 0,
  "chess_bet_amount" numeric(20,6) DEFAULT 0,
  "chess_bet_num" numeric(20,6) DEFAULT 0,
  "chess_win_amount" numeric(20,6) DEFAULT 0,
  "chess_rebate_amount" numeric(20,6) DEFAULT 0,
  "chess_bet_times" int4 DEFAULT 0,
  "chess_win_times" int4 DEFAULT 0,
  "esport_bet_amount" numeric(20,6) DEFAULT 0,
  "esport_bet_num" numeric(20,6) DEFAULT 0,
  "esport_win_amount" numeric(20,6) DEFAULT 0,
  "esport_rebate_amount" numeric(20,6) DEFAULT 0,
  "esport_bet_times" int4 DEFAULT 0,
  "esport_win_times" int4 DEFAULT 0,
  "fishing_bet_amount" numeric(20,6) DEFAULT 0,
  "fishing_bet_num" numeric(20,6) DEFAULT 0,
  "fishing_win_amount" numeric(20,6) DEFAULT 0,
  "fishing_rebate_amount" numeric(20,6) DEFAULT 0,
  "fishing_bet_times" int4 DEFAULT 0,
  "fishing_win_times" int4 DEFAULT 0,
  "lot_bet_amount" numeric(20,6) DEFAULT 0,
  "lot_bet_num" numeric(20,6) DEFAULT 0,
  "lot_win_amount" numeric(20,6) DEFAULT 0,
  "lot_rebate_amount" numeric(20,6) DEFAULT 0,
  "lot_bet_times" int4 DEFAULT 0,
  "lot_win_times" int4 DEFAULT 0,
  "proxy_rebate_amount" numeric(20,6) DEFAULT 0,
  "active_award_amount" numeric(20,6) DEFAULT 0,
  "active_award_times" int4 DEFAULT 0,
  "score_to_money" numeric(20,6) DEFAULT 0,
  "score_to_money_times" int4 DEFAULT 0,
  "money_to_score" numeric(20,6) DEFAULT 0,
  "money_to_score_times" int4 DEFAULT 0,
  "jackpot" numeric(20,6) DEFAULT 0,
  "third_tip" numeric(20,6) DEFAULT 0,
  "third_other_money" numeric(20,6) DEFAULT 0,
  "gift_other_amount" numeric(20,6) DEFAULT 0,
  "recharge_card_amount" numeric(20,6) DEFAULT 0,
  "recharge_card_times" int4 DEFAULT 0,
  "coupons_amount" numeric(20,6) DEFAULT 0,
  "coupons_times" int4 DEFAULT 0,
  "sub_gift_amount" numeric(20,6) default 0,
  "red_active_award_amount" numeric(20,6) DEFAULT 0,
  "red_active_award_times" int4 DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_daily_money" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_user_daily_money_unikey" ON "public"."sys_user_daily_money" USING btree ("user_id","stat_date");
COMMENT ON COLUMN "public"."sys_user_daily_money"."partner_id" IS '合作商id';
COMMENT ON COLUMN "public"."sys_user_daily_money"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."sys_user_daily_money"."agent_name" IS '代理商账号';
COMMENT ON COLUMN "public"."sys_user_daily_money"."proxy_id" IS '上级代理';
COMMENT ON COLUMN "public"."sys_user_daily_money"."proxy_name" IS '上级代理账号';
COMMENT ON COLUMN "public"."sys_user_daily_money"."parent_ids" IS '多级代理层级id，使用逗号分开';
COMMENT ON COLUMN "public"."sys_user_daily_money"."recommend_id" IS '推荐者id';
COMMENT ON COLUMN "public"."sys_user_daily_money"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sys_user_daily_money"."username" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_daily_money"."user_type" IS '用户类型';
COMMENT ON COLUMN "public"."sys_user_daily_money"."stat_date" IS '日期';
COMMENT ON COLUMN "public"."sys_user_daily_money"."withdraw_amount" IS '取款金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."withdraw_times" IS '取款次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."withdraw_artificial" IS '手动扣款金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_amount" IS '存款金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_times" IS '存款次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_artificial" IS '手动加款金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_gift_amount" IS '存款赠送金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_gift_times" IS '存款赠送次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_handler_artificial" IS '手动处理存款数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."deposit_handler_artificial_times" IS '手动处理存款次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_bet_amount" IS '真人投注';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_bet_num" IS '真人实际打码';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_win_amount" IS '真人中奖金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_rebate_amount" IS '真人反水';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_bet_times" IS '真人投注次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."live_win_times" IS '真人中奖次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."proxy_rebate_amount" IS '代理返点';
COMMENT ON COLUMN "public"."sys_user_daily_money"."active_award_amount" IS '活动中奖金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."active_award_times" IS '活动中奖次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."score_to_money" IS '积分兑换金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."money_to_score" IS '金额兑换积分';
COMMENT ON COLUMN "public"."sys_user_daily_money"."jackpot" IS '真人电子jackpot';
COMMENT ON COLUMN "public"."sys_user_daily_money"."third_tip" IS '真人电子小费';
COMMENT ON COLUMN "public"."sys_user_daily_money"."third_other_money" IS '真人电子其他金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."gift_other_amount" IS '其他';
COMMENT ON COLUMN "public"."sys_user_daily_money"."recharge_card_amount" IS '充值卡金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."recharge_card_times" IS '充值卡次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."coupons_amount" IS '代金券金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."coupons_times" IS '代金券次数';
COMMENT ON COLUMN "public"."sys_user_daily_money"."sub_gift_amount" IS '彩金扣款';
COMMENT ON COLUMN "public"."sys_user_daily_money"."red_active_award_amount" IS '红包中奖金额';
COMMENT ON COLUMN "public"."sys_user_daily_money"."red_active_award_times" IS '红包中奖次数';

CREATE OR REPLACE FUNCTION "public"."create_sys_user_daily_money_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(stat_date>='''|| startTime ||''' AND stat_date< '''|| endTime ||'''))
			INHERITS (sys_user_daily_money);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE UNIQUE INDEX  '||tablename||'_usd ON '||tablename||' USING btree(user_id,stat_date);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;


CREATE OR REPLACE FUNCTION "public"."insert_sys_user_daily_money_partition"()
  RETURNS "pg_catalog"."trigger" AS $BODY$  
DECLARE  
    strSQL  varchar;  
    tab_cols  record;
    col_val varchar;
BEGIN  
  
    -- 插入数据到子分区!  
    strSQL := 'INSERT INTO sys_user_daily_money_'||to_char(NEW.stat_date,'YYYYMM')||' AS a SELECT $1.*
on CONFLICT(user_id,stat_date) DO UPDATE SET station_id=EXCLUDED.station_id,';

FOR tab_cols IN SELECT "column_name","data_type" from information_schema.columns
  WHERE table_schema='public' AND table_name='sys_user_daily_money' 
LOOP
 EXECUTE 'SELECT $1.'||tab_cols."column_name" INTO col_val USING NEW;  
  IF tab_cols."column_name" <> 'id' AND tab_cols."column_name" <> 'partner_id'
    AND tab_cols."column_name" <> 'station_id' AND tab_cols."column_name" <> 'proxy_id'
    AND tab_cols."column_name" <> 'user_id' AND tab_cols."column_name" <> 'user_type'
    AND tab_cols."column_name" <> 'recommend_id'
    AND (tab_cols."data_type" = 'integer' OR tab_cols."data_type" = 'numeric')
    AND col_val::numeric <> 0 
    THEN
        strSQL := strSQL ||tab_cols."column_name"||'=COALESCE(a.'||tab_cols."column_name"||',0)+EXCLUDED.'||tab_cols."column_name"||',';
    END IF;     
END LOOP;
strSQL := SUBSTRING(strSQL,1,length(strSQL)-1);
RAISE INFO 'strSQL % ',strSQL;
    EXECUTE strSQL USING NEW;  
    RETURN NULL;   
END  
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

ALTER FUNCTION "public"."insert_sys_user_daily_money_partition"() OWNER TO "postgres";

CREATE TRIGGER "insert_sys_user_daily_money_trigger" BEFORE INSERT ON "public"."sys_user_daily_money"
FOR EACH ROW
EXECUTE PROCEDURE "public"."insert_sys_user_daily_money_partition"();

CREATE SEQUENCE "public"."sys_user_perm_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_perm" (
   "id" int4 NOT NULL DEFAULT nextval('sys_user_perm_id_seq'::regclass) PRIMARY KEY,
	"user_id" int4,
 	"station_id" int4,
	"username" varchar(50) COLLATE "pg_catalog"."default",
	"user_type" int2,
	"type" int2,
	"status" int2 default 2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_perm" OWNER TO "postgres";
COMMENT ON TABLE "public"."sys_user_perm" IS '会员权限';
COMMENT ON COLUMN "public"."sys_user_perm"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."sys_user_perm"."type" IS '权限类型，UserPermEnum';
COMMENT ON COLUMN "public"."sys_user_perm"."status" IS '状态，1=禁用，2=启用';
COMMENT ON COLUMN "public"."sys_user_perm"."username" IS '用户名';
COMMENT ON COLUMN "public"."sys_user_perm"."user_type" IS '用户类型';
CREATE UNIQUE INDEX "sys_user_perm_siau" ON "public"."sys_user_perm" ("station_id","user_id","type");
CREATE INDEX "sys_user_perm_siaun" ON "public"."sys_user_perm" ("station_id","username");

CREATE SEQUENCE "public"."station_banner_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_banner" (
  "id" int4 NOT NULL DEFAULT nextval('station_banner_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "language" varchar(10) COLLATE "pg_catalog"."default",
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "title_img" varchar(255) COLLATE "pg_catalog"."default",
  "title_url" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "over_time" timestamp(6),
  "status" int2,
  "sort_no" int2,
  "code" int2,
  "app_type" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_banner" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_banner"."language" IS '对应语种';
COMMENT ON COLUMN "public"."station_banner"."title" IS '标题';
COMMENT ON COLUMN "public"."station_banner"."title_img" IS '标题图片地址';
COMMENT ON COLUMN "public"."station_banner"."title_url" IS '轮播链接地址';
COMMENT ON COLUMN "public"."station_banner"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."station_banner"."over_time" IS '结束时间';
COMMENT ON COLUMN "public"."station_banner"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_banner"."status" IS '状态';
COMMENT ON COLUMN "public"."station_banner"."sort_no" IS '排序，大的排前面';
COMMENT ON COLUMN "public"."station_banner"."code" IS '轮播类型';
COMMENT ON COLUMN "public"."station_banner"."app_type" IS 'APP跳转标识(1 2彩票，3真人，4电子，5体育，6棋牌，7红包)';

CREATE TABLE "public"."sys_user_withdraw" (
	"user_id" int4 PRIMARY KEY,
	"times" int,
	"total_money" numeric(20,6),
	"station_id" int4,
	"first_time" timestamp(6) NULL,
	"first_money" numeric(20,6),
	"sec_time" timestamp(6) NULL,
	"sec_money" numeric(20,6),
	"third_time" timestamp(6) NULL,
	"third_money" numeric(20,6),
	"max_time" timestamp(6) NULL,
	"max_money" numeric(20,6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_withdraw" OWNER TO "postgres";
COMMENT ON TABLE "public"."sys_user_withdraw" IS '会员取款总计';
COMMENT ON COLUMN "public"."sys_user_withdraw"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."sys_user_withdraw"."times" IS '取款次数';
COMMENT ON COLUMN "public"."sys_user_withdraw"."total_money" IS '取款总额';
COMMENT ON COLUMN "public"."sys_user_withdraw"."first_time" IS '首次提款时间';
COMMENT ON COLUMN "public"."sys_user_withdraw"."first_money" IS '首次提款金额';
COMMENT ON COLUMN "public"."sys_user_withdraw"."sec_time" IS '二次提款时间';
COMMENT ON COLUMN "public"."sys_user_withdraw"."sec_money" IS '二次提款金额';
COMMENT ON COLUMN "public"."sys_user_withdraw"."third_time" IS '三次提款时间';
COMMENT ON COLUMN "public"."sys_user_withdraw"."third_money" IS '三次提款金额';
COMMENT ON COLUMN "public"."sys_user_withdraw"."max_money" IS '最大充值金额';
COMMENT ON COLUMN "public"."sys_user_withdraw"."max_time" IS '最大充值时间';

CREATE TABLE "public"."sys_user_deposit" (
	"user_id" int4 PRIMARY KEY,
	"times" int2,
	"total_money" numeric(20,6),
	"station_id" int4,
	"first_time" timestamp(6) NULL,
    "first_money" numeric(20,6),
	"sec_time" timestamp(6) NULL,
	"sec_money" numeric(20,6),
	"third_time" timestamp(6) NULL,
	"third_money" numeric(20,6),
	"max_money" numeric(20,6),
	"max_time" timestamp(6) NULL
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_deposit" OWNER TO "postgres";
COMMENT ON TABLE "public"."sys_user_deposit" IS '会员存款总计';
COMMENT ON COLUMN "public"."sys_user_deposit"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."sys_user_deposit"."times" IS '存款次数';
COMMENT ON COLUMN "public"."sys_user_deposit"."total_money" IS '存款总额';
COMMENT ON COLUMN "public"."sys_user_deposit"."first_time" IS '首次充值时间';
COMMENT ON COLUMN "public"."sys_user_deposit"."first_money" IS '首次充值金额';
COMMENT ON COLUMN "public"."sys_user_deposit"."sec_time" IS '二次充值时间';
COMMENT ON COLUMN "public"."sys_user_deposit"."sec_money" IS '二次充值金额';
COMMENT ON COLUMN "public"."sys_user_deposit"."third_time" IS '三次充值时间';
COMMENT ON COLUMN "public"."sys_user_deposit"."third_money" IS '三次充值金额';
COMMENT ON COLUMN "public"."sys_user_deposit"."max_money" IS '最大充值金额';
COMMENT ON COLUMN "public"."sys_user_deposit"."max_time" IS '最大充值时间';

CREATE SEQUENCE "public"."station_deposit_bank_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_deposit_bank" (
  "id" int4 NOT NULL DEFAULT nextval('station_deposit_bank_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "bank_name" varchar(50) COLLATE "pg_catalog"."default",
  "bank_card" varchar(100) COLLATE "pg_catalog"."default",
  "real_name" varchar(50) COLLATE "pg_catalog"."default",
  "bank_address" varchar(100) COLLATE "pg_catalog"."default",
  "min" numeric(20,6),
  "max" numeric(20,6),
  "status" int2,
  "deposit_rate" numeric(20,2),
  "withdraw_rate" numeric(20,2),
  "icon" varchar(256) COLLATE "pg_catalog"."default",
  "pay_platform_code" varchar(20) COLLATE "pg_catalog"."default",
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "sort_no" int4 DEFAULT 0,
  "create_user" int4,
  "open_user" int4,
  "qr_code_img" varchar(256) COLLATE "pg_catalog"."default",
  "ali_qrcode_status" int2 DEFAULT 1,
  "ali_qrcode_link" varchar(500) COLLATE "pg_catalog"."default",
  "card_index" varchar(200) COLLATE "pg_catalog"."default",
  "remark" varchar(1024) COLLATE "pg_catalog"."default",
  "bg_remark" varchar(100) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_deposit_bank" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_deposit_bank"."bank_name" IS '银行名称';
COMMENT ON COLUMN "public"."station_deposit_bank"."bank_card" IS '银行卡号';
COMMENT ON COLUMN "public"."station_deposit_bank"."real_name" IS '开户人姓名';
COMMENT ON COLUMN "public"."station_deposit_bank"."bank_address" IS '开户银行地址';
COMMENT ON COLUMN "public"."station_deposit_bank"."min" IS '最小金额';
COMMENT ON COLUMN "public"."station_deposit_bank"."max" IS '最大金额';
COMMENT ON COLUMN "public"."station_deposit_bank"."status" IS '状态';
COMMENT ON COLUMN "public"."station_deposit_bank"."deposit_rate" IS '存款汇率';
COMMENT ON COLUMN "public"."station_deposit_bank"."withdraw_rate" IS '取款汇率';
COMMENT ON COLUMN "public"."station_deposit_bank"."icon" IS '图标样式 ';
COMMENT ON COLUMN "public"."station_deposit_bank"."pay_platform_code" IS '支付平台代码';
COMMENT ON COLUMN "public"."station_deposit_bank"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_deposit_bank"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_deposit_bank"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."station_deposit_bank"."create_user" IS '创建人';
COMMENT ON COLUMN "public"."station_deposit_bank"."open_user" IS '开启人';
COMMENT ON COLUMN "public"."station_deposit_bank"."qr_code_img" IS '二维码路径';
COMMENT ON COLUMN "public"."station_deposit_bank"."ali_qrcode_status" IS '支付宝转卡状态';
COMMENT ON COLUMN "public"."station_deposit_bank"."ali_qrcode_link" IS '支付宝转卡链接(自动生成)';
COMMENT ON COLUMN "public"."station_deposit_bank"."card_index" IS '支付宝转卡隐藏部分卡号';
COMMENT ON COLUMN "public"."station_deposit_bank"."remark" IS '前台备注';
COMMENT ON COLUMN "public"."station_deposit_bank"."bg_remark" IS '后台备注';

CREATE SEQUENCE "public"."station_deposit_online_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_deposit_online" (
  "id" int4 NOT NULL DEFAULT nextval('station_deposit_online_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "merchant_code" varchar(1024) COLLATE "pg_catalog"."default",
  "merchant_key" varchar(5000) COLLATE "pg_catalog"."default",
  "url" varchar(256) COLLATE "pg_catalog"."default",
  "account" varchar(5000) COLLATE "pg_catalog"."default",
  "min" numeric(10,2),
  "max" numeric(16,2),
  "def" int4,
  "status" int4,
  "version" int2 default 1,
  "icon" varchar(256) COLLATE "pg_catalog"."default",
  "pay_platform_code" varchar(50) COLLATE "pg_catalog"."default",
  "pay_type" varchar(20) COLLATE "pg_catalog"."default",
  "pay_getway" varchar(500) COLLATE "pg_catalog"."default",
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "appid" varchar(500) COLLATE "pg_catalog"."default",
  "sort_no" int4 DEFAULT 0,
  "show_type" varchar(20) COLLATE "pg_catalog"."default",
  "random_add" int2 DEFAULT 0,
  "random_sub" int2 DEFAULT 0,
  "is_fixed_amount" int2,
  "fixed_amount" varchar(255) COLLATE "pg_catalog"."default",
  "bank_list" varchar(255) COLLATE "pg_catalog"."default",
  "pay_alias" varchar(32) COLLATE "pg_catalog"."default",
  "alter_native" varchar(255) COLLATE "pg_catalog"."default",
  "white_list_ip" varchar(255) COLLATE "pg_catalog"."default",
  "create_user" int4,
  "open_user" int4,
  "deposit_daily_start_time" timestamp(6),
  "deposit_daily_end_time" timestamp(6),
  "query_gateway" varchar(500) COLLATE "pg_catalog"."default",
  "pc_remark" varchar(500) COLLATE "pg_catalog"."default",
  "wap_remark" varchar(500) COLLATE "pg_catalog"."default",
  "app_remark" varchar(500) COLLATE "pg_catalog"."default",
  "bg_remark" varchar(255) COLLATE "pg_catalog"."default",
  "system_type" int2 DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_deposit_online" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_deposit_online"."merchant_code" IS '商户编号';
COMMENT ON COLUMN "public"."station_deposit_online"."merchant_key" IS '商户密钥';
COMMENT ON COLUMN "public"."station_deposit_online"."url" IS '接口域名';
COMMENT ON COLUMN "public"."station_deposit_online"."account" IS '账号';
COMMENT ON COLUMN "public"."station_deposit_online"."min" IS '最小金额';
COMMENT ON COLUMN "public"."station_deposit_online"."max" IS '最大金额';
COMMENT ON COLUMN "public"."station_deposit_online"."def" IS '是否默认';
COMMENT ON COLUMN "public"."station_deposit_online"."status" IS '状态 ';
COMMENT ON COLUMN "public"."station_deposit_online"."version" IS '版本';
COMMENT ON COLUMN "public"."station_deposit_online"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_deposit_online"."icon" IS '图标样式';
COMMENT ON COLUMN "public"."station_deposit_online"."pay_type" IS '支付方式： 1-收银台，2-银行直连，3-单微信，4-单支付宝，5-QQ钱包，6-京东扫码';
COMMENT ON COLUMN "public"."station_deposit_online"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_deposit_online"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."station_deposit_online"."show_type" IS '显示类型: all - 所有终端都显示、 pc - pc端显示、 mobile - 手机端显示、 app - app端显示';
COMMENT ON COLUMN "public"."station_deposit_online"."random_add" IS '随机加最大额度，单位分';
COMMENT ON COLUMN "public"."station_deposit_online"."random_sub" IS '随机减最大额度，单位分';
COMMENT ON COLUMN "public"."station_deposit_online"."is_fixed_amount" IS '是否固定金额';
COMMENT ON COLUMN "public"."station_deposit_online"."fixed_amount" IS '固定金额 ，分割';
COMMENT ON COLUMN "public"."station_deposit_online"."bank_list" IS '收银台列表';
COMMENT ON COLUMN "public"."station_deposit_online"."pay_alias" IS '前台显示别名';
COMMENT ON COLUMN "public"."station_deposit_online"."alter_native" IS '备选参数';
COMMENT ON COLUMN "public"."station_deposit_online"."white_list_ip" IS '白名单Ip';
COMMENT ON COLUMN "public"."station_deposit_online"."create_user" IS '创建人';
COMMENT ON COLUMN "public"."station_deposit_online"."open_user" IS '开启人';
COMMENT ON COLUMN "public"."station_deposit_online"."pc_remark" IS 'PC备注';
COMMENT ON COLUMN "public"."station_deposit_online"."wap_remark" IS 'WAP备注';
COMMENT ON COLUMN "public"."station_deposit_online"."app_remark" IS 'APP备注';
COMMENT ON COLUMN "public"."station_deposit_online"."bg_remark" IS '后台备注';
COMMENT ON COLUMN "public"."station_deposit_online"."system_type" IS '系统';

CREATE SEQUENCE "public"."app_tab_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
-- ----------------------------
--  Table structure for app_tab
-- ----------------------------
DROP TABLE IF EXISTS "public"."app_tab";
CREATE TABLE "public"."app_tab" (
    "id" int4 NOT NULL DEFAULT nextval('app_tab_id_seq'::regclass) PRIMARY KEY,
    "station_id" int4,
    "name" varchar COLLATE "default",
    "code" varchar COLLATE "default",
    "custom_title" varchar,
    "sort_no" int4,
    "create_time" timestamp(6),
    "icon" varchar COLLATE "default",
    "status" int2 default 2,
    "partner_id" int4,
    "selected_icon" varchar,
    "bg_icon" varchar,
    "type" int2
)
    WITH (OIDS=FALSE);
ALTER TABLE "public"."app_tab" OWNER TO "postgres";

COMMENT ON COLUMN "public"."app_tab"."type" IS '游戏标签类型';
COMMENT ON COLUMN  "public"."app_tab"."custom_title" IS '自定义标题';
CREATE UNIQUE INDEX "station_id_sc" ON "public"."app_tab" ("station_id","code");

CREATE SEQUENCE "public"."app_feedback_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_feedback" (
  "id" int4 NOT NULL DEFAULT nextval('app_feedback_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "content" varchar(500) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "type" int2,
  "parent_id" int4,
  "is_reply" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."app_feedback" OWNER TO "postgres";
COMMENT ON COLUMN "public"."app_feedback"."content" IS '意见内容';
COMMENT ON COLUMN "public"."app_feedback"."user_id" IS '发建议的用户id';
COMMENT ON COLUMN "public"."app_feedback"."type" IS '消息类型 2--管理员消息 1--用户消息';
COMMENT ON COLUMN "public"."app_feedback"."parent_id" IS '被引用消息的id';
COMMENT ON COLUMN "public"."app_feedback"."is_reply" IS '是否已回复 2--已回复0--未回复';
COMMENT ON COLUMN "public"."app_feedback"."username" IS '用户名';

CREATE SEQUENCE "public"."station_activity_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_activity" (
  "id" int4 NOT NULL DEFAULT nextval('station_activity_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "language" varchar(10) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "title_img_url" varchar(256) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "status" int2,
  "device_type" int2,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "over_time" timestamp(6),
  "sort_no" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_activity" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_activity"."title" IS '标题';
COMMENT ON COLUMN "public"."station_activity"."content" IS '内容';
COMMENT ON COLUMN "public"."station_activity"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."station_activity"."status" IS '状态';
COMMENT ON COLUMN "public"."station_activity"."device_type" IS '';
COMMENT ON COLUMN "public"."station_activity"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_activity"."over_time" IS '过期时间';
COMMENT ON COLUMN "public"."station_activity"."title_img_url" IS '标题图片';
COMMENT ON COLUMN "public"."station_activity"."sort_no" IS '排序 ';

CREATE SEQUENCE "public"."station_article_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_article" (
  "id" int4 NOT NULL DEFAULT nextval('station_article_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "language" varchar(10) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "type" int2,
  "content" text COLLATE "pg_catalog"."default",
  "status" int2,
  "popup_status" int2,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "over_time" timestamp(6),
  "is_index" bool DEFAULT false,
  "is_reg" bool DEFAULT false,
  "is_deposit" bool DEFAULT false,
  "is_bet" bool DEFAULT false,
  "frame_width" int4,
  "frame_height" int4,
  "sort_no" int4,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_article" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_article"."title" IS '标题';
COMMENT ON COLUMN "public"."station_article"."type" IS '类型 1:关于我们,2:取款帮助,3:存款帮助,4:合作伙伴->联盟方案,5:合作伙伴->联盟协议,6:联系我们,7:常见问题 ,8:玩法介绍,9:彩票公告,10:视讯公告,11:体育公告,12:电子公告,13:最新公告,14:页面弹窗,15签到公告,16最新资讯,17签到规则,18新手教程,19手机弹窗20责任条款21红包规则';
COMMENT ON COLUMN "public"."station_article"."content" IS '文本内容';
COMMENT ON COLUMN "public"."station_article"."status" IS '状态 1:禁用  2:启用';
COMMENT ON COLUMN "public"."station_article"."popup_status" IS '弹出状态 1:禁用  2:启用';
COMMENT ON COLUMN "public"."station_article"."update_time" IS '更新/插入时间';
COMMENT ON COLUMN "public"."station_article"."over_time" IS '过期时间';
COMMENT ON COLUMN "public"."station_article"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_article"."is_index" IS '是否首页弹窗';
COMMENT ON COLUMN "public"."station_article"."is_reg" IS '是否注册页面弹窗';
COMMENT ON COLUMN "public"."station_article"."is_deposit" IS '是否充值页面弹窗';
COMMENT ON COLUMN "public"."station_article"."is_bet" IS '是否显示投注页弹窗';
COMMENT ON COLUMN "public"."station_article"."frame_width" IS '弹窗宽度';
COMMENT ON COLUMN "public"."station_article"."frame_height" IS '弹窗高度';
COMMENT ON COLUMN "public"."station_article"."sort_no" IS '序号';
COMMENT ON COLUMN "public"."station_article"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_article"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON TABLE "public"."station_article" IS '网站资料表(系统公告,玩法介绍,关于我们)';

CREATE SEQUENCE "public"."station_float_frame_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_float_frame" (
  "id" int4 NOT NULL DEFAULT nextval('station_float_frame_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "language" varchar(10) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "show_page" int2,
  "show_position" varchar(50) COLLATE "pg_catalog"."default",
  "status" int2,
  "img_type" int2,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "begin_time" date,
  "over_time" date,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "platform" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_float_frame" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_float_frame"."title" IS '标题';
COMMENT ON COLUMN "public"."station_float_frame"."show_page" IS '显示页面1主站 2个人中心';
COMMENT ON COLUMN "public"."station_float_frame"."show_position" IS '显示位置';
COMMENT ON COLUMN "public"."station_float_frame"."update_time" IS '创建时间';
COMMENT ON COLUMN "public"."station_float_frame"."status" IS '状态';
COMMENT ON COLUMN "public"."station_float_frame"."img_type" IS '图片类型1盖楼2轮播';
COMMENT ON COLUMN "public"."station_float_frame"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_float_frame"."begin_time" IS '开始时间';
COMMENT ON COLUMN "public"."station_float_frame"."over_time" IS '过期时间';
COMMENT ON COLUMN "public"."station_float_frame"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_float_frame"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_float_frame"."platform" IS '显示终端';

CREATE SEQUENCE "public"."station_float_frame_setting_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_float_frame_setting" (
  "id" int4 NOT NULL DEFAULT nextval('station_float_frame_setting_id_seq'::regclass)PRIMARY KEY,
  "afr_id" int4,
  "img_url" varchar(300) COLLATE "pg_catalog"."default",
  "img_hover_url" varchar(300) COLLATE "pg_catalog"."default",
  "img_sort" int2,
  "link_url" varchar(300) COLLATE "pg_catalog"."default",
  "link_type" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_float_frame_setting" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_float_frame_setting"."afr_id" IS '浮窗id';
COMMENT ON COLUMN "public"."station_float_frame_setting"."img_url" IS '图片地址';
COMMENT ON COLUMN "public"."station_float_frame_setting"."img_hover_url" IS '鼠标移入图片显示地址';
COMMENT ON COLUMN "public"."station_float_frame_setting"."img_sort" IS '图片顺序';
COMMENT ON COLUMN "public"."station_float_frame_setting"."link_url" IS '链接地址';
COMMENT ON COLUMN "public"."station_float_frame_setting"."link_type" IS '链接类型(1普通跳转,2新窗打开,3关闭按钮)';

CREATE SEQUENCE "public"."app_update_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_update" (
  "id" int4 NOT NULL DEFAULT nextval('app_update_id_seq'::regclass) PRIMARY KEY,
  "version" varchar(50) COLLATE "pg_catalog"."default",
  "content" varchar(500) COLLATE "pg_catalog"."default",
  "status" int4 DEFAULT 1,
  "station_ids" varchar(5000) COLLATE "pg_catalog"."default",
  "platform" varchar(200) COLLATE "pg_catalog"."default" DEFAULT 'android'::character varying
)WITH (OIDS=FALSE);
ALTER TABLE "public"."app_update"  OWNER TO "postgres";
COMMENT ON COLUMN "public"."app_update"."version" IS 'APP 版本号';
COMMENT ON COLUMN "public"."app_update"."content" IS '每个版本的更新内容';
COMMENT ON COLUMN "public"."app_update"."status" IS '开关状态  1-关闭 2-开启';
COMMENT ON COLUMN "public"."app_update"."station_ids" IS '更新的站点集，以逗号分隔。';

CREATE SEQUENCE "public"."coupons_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."coupons" (
  "id" int4 NOT NULL DEFAULT nextval('coupons_id_seq'::regclass) PRIMARY KEY ,
  "partner_id" int4,
  "station_id" int4,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "type" int2,
  "money" numeric(20,6) DEFAULT 0,
  "min_amount" numeric(20,6) DEFAULT 0,
  "max_amount" numeric(20,6) DEFAULT 0,
  "create_datetime" timestamp(6),
  "create_username" varchar(50) COLLATE "pg_catalog"."default",
  "create_userid" int4,
  "bet_rate" numeric(10,3),
  "status" int2,
  "start_datetime" timestamp(6),
  "end_datetime" timestamp(6),
  "numbers" int4 DEFAULT 0,
  "used" int4 DEFAULT 0,
  "valid" int2,
  "valid_days" int4,
  "send_numbers" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."coupons" OWNER TO "postgres";
COMMENT ON COLUMN "public"."coupons"."name" IS '代金券名称';
COMMENT ON COLUMN "public"."coupons"."type" IS '发放显示类型(1普通发放,2投注回馈,3老用户回馈,4新用户注册回馈)';
COMMENT ON COLUMN "public"."coupons"."money" IS '代金券面额';
COMMENT ON COLUMN "public"."coupons"."min_amount" IS '最小充值金额可使用';
COMMENT ON COLUMN "public"."coupons"."max_amount" IS '最大充值金额可使用';
COMMENT ON COLUMN "public"."coupons"."create_username" IS '充值卡创建人';
COMMENT ON COLUMN "public"."coupons"."create_userid" IS '充值卡创建人id';
COMMENT ON COLUMN "public"."coupons"."bet_rate" IS '所需打码量倍数';
COMMENT ON COLUMN "public"."coupons"."status" IS '是否可用(1禁用2正常)';
COMMENT ON COLUMN "public"."coupons"."start_datetime" IS '有效期开始时间';
COMMENT ON COLUMN "public"."coupons"."end_datetime" IS '有效期结束时间';
COMMENT ON COLUMN "public"."coupons"."numbers" IS '总发放数量';
COMMENT ON COLUMN "public"."coupons"."used" IS '已使用数量';
COMMENT ON COLUMN "public"."coupons"."valid" IS '1指定日期范围有效,2有效天数(被领取开始算起),3长期有效';
COMMENT ON COLUMN "public"."coupons"."valid_days" IS '有效天数';
COMMENT ON COLUMN "public"."coupons"."send_numbers" IS '已发放数量';

CREATE SEQUENCE "public"."coupons_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."coupons_user" (
  "id" int4 NOT NULL DEFAULT nextval('coupons_user_id_seq'::regclass) PRIMARY KEY ,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "status" int2,
  "create_datetime" timestamp(6),
  "used_datetime" timestamp(6),
  "cname" varchar(50) COLLATE "pg_catalog"."default",
  "cid" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."coupons_user" OWNER TO "postgres";
COMMENT ON COLUMN "public"."coupons_user"."status" IS '是否可用(1可用2已用3过期)';
COMMENT ON COLUMN "public"."coupons_user"."create_datetime" IS '领取时间';
COMMENT ON COLUMN "public"."coupons_user"."used_datetime" IS '使用时间';
COMMENT ON COLUMN "public"."coupons_user"."cname" IS '代金券名称';
COMMENT ON COLUMN "public"."coupons_user"."cid" IS '优惠券关联id';

CREATE SEQUENCE "public"."rechargeable_card_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."rechargeable_card" (
  "id" int4 NOT NULL DEFAULT nextval('rechargeable_card_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "card_no" varchar(50) COLLATE "pg_catalog"."default",
  "card_password" varchar(50) COLLATE "pg_catalog"."default",
  "money" numeric(20,6) DEFAULT 0,
  "status" int4,
  "used_datetime" timestamp(6),
  "used_username" varchar(50) COLLATE "pg_catalog"."default",
  "used_userid" int4,
  "create_datetime" timestamp(6),
  "create_username" varchar(50) COLLATE "pg_catalog"."default",
  "create_userid" int4,
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."rechargeable_card" OWNER TO "postgres";
CREATE UNIQUE INDEX "rechargeable_card_station_index" ON "public"."rechargeable_card" USING btree ("card_no","station_id");
COMMENT ON COLUMN "public"."rechargeable_card"."card_no" IS '卡号';
COMMENT ON COLUMN "public"."rechargeable_card"."card_password" IS '密码';
COMMENT ON COLUMN "public"."rechargeable_card"."money" IS '充值卡面额';
COMMENT ON COLUMN "public"."rechargeable_card"."status" IS '状态(1未使用2已使用)';
COMMENT ON COLUMN "public"."rechargeable_card"."used_datetime" IS '充值日期';
COMMENT ON COLUMN "public"."rechargeable_card"."used_username" IS '充值卡使用人';
COMMENT ON COLUMN "public"."rechargeable_card"."used_userid" IS '充值卡使用人id';
COMMENT ON COLUMN "public"."rechargeable_card"."create_username" IS '充值卡创建人';
COMMENT ON COLUMN "public"."rechargeable_card"."create_userid" IS '充值卡创建人id';
COMMENT ON COLUMN "public"."rechargeable_card"."bet_rate" IS '打码量倍数';


CREATE OR REPLACE FUNCTION "public"."create_proxy_daily_bet_stat_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(stat_date>='''|| startTime ||''' AND stat_date< '''|| endTime ||'''))
			INHERITS (proxy_daily_bet_stat);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE UNIQUE INDEX '||tablename||'_usidex ON ' ||tablename||' USING btree("stat_date","user_id");' ;
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_proxy_daily_bet_stat_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO proxy_daily_bet_stat_'||to_char(NEW.stat_date,'YYYYMM')||
' as aa SELECT $1.* on CONFLICT(stat_date,user_id) DO UPDATE SET lot_bet=COALESCE(aa.lot_bet,0)+EXCLUDED.lot_bet,
	lot_rollback=COALESCE(aa.lot_rollback,0)+EXCLUDED.lot_rollback,
live_bet=COALESCE(aa.live_bet,0)+EXCLUDED.live_bet,live_rollback=COALESCE(aa.live_rollback,0)+EXCLUDED.live_rollback,
egame_bet=COALESCE(aa.egame_bet,0)+EXCLUDED.egame_bet,egame_rollback=COALESCE(aa.egame_rollback,0)+EXCLUDED.egame_rollback,
sport_bet=COALESCE(aa.sport_bet,0)+EXCLUDED.sport_bet,sport_rollback=COALESCE(aa.sport_rollback,0)+EXCLUDED.sport_rollback,
chess_bet=COALESCE(aa.chess_bet,0)+EXCLUDED.chess_bet,chess_rollback=COALESCE(aa.chess_rollback,0)+EXCLUDED.chess_rollback,
esport_bet=COALESCE(aa.esport_bet,0)+EXCLUDED.esport_bet,esport_rollback=COALESCE(aa.esport_rollback,0)+EXCLUDED.esport_rollback,
fishing_bet=COALESCE(aa.fishing_bet,0)+EXCLUDED.fishing_bet,fishing_rollback=COALESCE(aa.fishing_rollback,0)+EXCLUDED.fishing_rollback,
draw_num=COALESCE(aa.draw_num,0)+EXCLUDED.draw_num' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE SEQUENCE "public"."proxy_daily_bet_stat_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."proxy_daily_bet_stat" (
  "id" int4 NOT NULL DEFAULT nextval('proxy_daily_bet_stat_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "stat_date" date,
  "status" int2 DEFAULT 1,
  "lot_bet" numeric(20,6) DEFAULT 0,
  "lot_rollback" numeric(20,6) DEFAULT 0,
  "live_bet" numeric(20,6) DEFAULT 0,
  "live_rollback" numeric(20,6) DEFAULT 0,
  "egame_bet" numeric(20,6) DEFAULT 0,
  "egame_rollback" numeric(20,6) DEFAULT 0,
  "chess_bet" numeric(20,6) DEFAULT 0,
  "chess_rollback" numeric(20,6) DEFAULT 0,
  "sport_bet" numeric(20,6) DEFAULT 0,
  "sport_rollback" numeric(20,6) DEFAULT 0,
  "esport_bet" numeric(20,6) DEFAULT 0,
  "esport_rollback" numeric(20,6) DEFAULT 0,
  "fishing_bet" numeric(20,6) DEFAULT 0,
  "fishing_rollback" numeric(20,6) DEFAULT 0,
  "draw_num" numeric(20,6) DEFAULT 0,
  "operator_id" int4,
  "operator" varchar(50) COLLATE "pg_catalog"."default",
  "update_time" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."proxy_daily_bet_stat"  OWNER TO "postgres";
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."stat_date" IS '日期';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."user_id" IS '代理账号id';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."username" IS '代理账号';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."status" IS '1=未返，2=已返';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."lot_bet" IS '彩票下注';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."lot_rollback" IS '彩票返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."live_bet" IS '真人下注';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."live_rollback" IS '真人返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."egame_bet" IS '电子';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."egame_rollback" IS '电子返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."chess_bet" IS '棋牌';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."chess_rollback" IS '棋牌返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."sport_bet" IS '体育';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."sport_rollback" IS '体育返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."draw_num" IS '返点出款所需打码';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."operator_id" IS '操作者id';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."operator" IS '操作者账号';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."update_time" IS '操作时间';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."esport_bet" IS '电竞投注';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."esport_rollback" IS '电竞返点';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."fishing_bet" IS '捕鱼投注';
COMMENT ON COLUMN "public"."proxy_daily_bet_stat"."fishing_rollback" IS '捕鱼返点';
CREATE UNIQUE INDEX "proxy_daily_bet_stat_usd" ON "public"."proxy_daily_bet_stat" (
  "user_id",
  "stat_date"
);

CREATE TRIGGER "insert_proxy_daily_bet_stat_partition_trigger" BEFORE INSERT ON "public"."proxy_daily_bet_stat"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_proxy_daily_bet_stat_partition"();


CREATE SEQUENCE "public"."station_white_area_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_white_area" (
	"id" int4 NOT NULL DEFAULT nextval('station_white_area_id_seq'::regclass),
	"partner_id" int4,
  	"station_id" int4,
	"country_code" varchar(50),
	"country_name" varchar(100) COLLATE "default",
	"status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_white_area" OWNER TO "postgres";
COMMENT ON TABLE "public"."station_white_area" IS '国家/地区限制';
COMMENT ON COLUMN "public"."station_white_area"."country_code" IS '国家地区编码';
COMMENT ON COLUMN "public"."station_white_area"."country_name" IS '国家地区名称';
COMMENT ON COLUMN "public"."station_white_area"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_white_area"."status" IS '类型状态';

CREATE SEQUENCE "public"."station_white_ip_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_white_ip" (
  "id" int4 NOT NULL DEFAULT nextval('station_white_ip_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "ip" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "type" int2,
  "remark" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_white_ip" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_white_ip"."type" IS '2:白名单 ，1：黑名单';
COMMENT ON TABLE "public"."station_white_ip" IS '站点前台IP白名单列表';

CREATE SEQUENCE "public"."station_daily_deposit_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_daily_deposit" (
  "id" int4 NOT NULL DEFAULT nextval('station_daily_deposit_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "stat_date" date,
  "deposit_times" int2,
  "success_times" int2,
  "deposit_amount" numeric(20,6),
  "success_amount" numeric(20,6),
  "max_money" numeric(20,6),
  "min_money" numeric(20,6),
  "deposit_type" int2,
  "deposit_user" int2,
  "pay_platform_code" varchar(200) COLLATE "pg_catalog"."default",
  "pay_name" varchar(200) COLLATE "pg_catalog"."default",
  "failed_times" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_daily_deposit" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_daily_deposit_ukey" ON "public"."station_daily_deposit" USING btree ("station_id","stat_date","deposit_type","pay_name");
COMMENT ON COLUMN "public"."station_daily_deposit"."deposit_times" IS '提交充值次数';
COMMENT ON COLUMN "public"."station_daily_deposit"."success_times" IS '充值成功次数';
COMMENT ON COLUMN "public"."station_daily_deposit"."deposit_amount" IS '提交充值金额';
COMMENT ON COLUMN "public"."station_daily_deposit"."success_amount" IS '充值成功金额';
COMMENT ON COLUMN "public"."station_daily_deposit"."max_money" IS '最大充值金额';
COMMENT ON COLUMN "public"."station_daily_deposit"."min_money" IS '最小充值金额';
COMMENT ON COLUMN "public"."station_daily_deposit"."deposit_type" IS '充值类型';
COMMENT ON COLUMN "public"."station_daily_deposit"."deposit_user" IS '充值人数';
COMMENT ON COLUMN "public"."station_daily_deposit"."pay_platform_code" IS '充值平台';
COMMENT ON COLUMN "public"."station_daily_deposit"."pay_name" IS '支付名称';
COMMENT ON COLUMN "public"."station_daily_deposit"."failed_times" IS '充值失败人数';
COMMENT ON TABLE "public"."station_daily_deposit" IS '站点每日充值报表';

CREATE SEQUENCE "public"."station_daily_register_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_daily_register" (
  "id" int4 NOT NULL DEFAULT nextval('station_daily_register_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "stat_date" date,
  "register_num" int2,
  "member_num" int2,
  "proxy_num" int2,
  "first_deposit" int2,
  "sec_deposit" int2,
  "third_deposit" int2,
  "promo_member" int2,
  "promo_proxy" int2,
  "deposit" int2 DEFAULT 0,
  "deposit_money" numeric(20,6) default 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_daily_register" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_daily_register_ukey" ON "public"."station_daily_register" USING btree ("station_id","stat_date");
COMMENT ON COLUMN "public"."station_daily_register"."stat_date" IS '日期';
COMMENT ON COLUMN "public"."station_daily_register"."register_num" IS '当天注册人数';
COMMENT ON COLUMN "public"."station_daily_register"."member_num" IS '当天注册会员人数';
COMMENT ON COLUMN "public"."station_daily_register"."proxy_num" IS '当天注册代理人数';
COMMENT ON COLUMN "public"."station_daily_register"."first_deposit" IS '当天注册并且首充人数';
COMMENT ON COLUMN "public"."station_daily_register"."sec_deposit" IS '当天注册并且二充人数';
COMMENT ON COLUMN "public"."station_daily_register"."promo_member" IS '当天通过代理推广注册人数';
COMMENT ON COLUMN "public"."station_daily_register"."promo_proxy" IS '当天通过代理推广注册代理人数';
COMMENT ON COLUMN "public"."station_daily_register"."deposit" IS '当天注册充值次数';
COMMENT ON COLUMN "public"."station_daily_register"."deposit_money" IS '当天注册充值金额';
COMMENT ON TABLE "public"."station_daily_register" IS '站点每日注册信息表';

CREATE SEQUENCE "public"."station_deposit_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_deposit_strategy" (
  "id" int4 NOT NULL DEFAULT nextval('station_deposit_strategy_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "deposit_type" int2,
  "gift_type" int2,
  "back_gift_type" int2 default 1,
  "value_type" int2,
  "deposit_count" int2,
  "gift_value" numeric(16,2),
  "back_bonus_level_diff" numeric(16,2),
  "bet_rate"  numeric(10,3),
  "bonus_back_vaue" numeric(16,2),
  "bonus_back_bet_rate"  numeric(10,3),
  "upper_limit" numeric(16,4),
  "create_datetime" timestamp(6),
  "start_datetime" timestamp(6),
  "end_datetime" timestamp(6),
  "remark" varchar(200) COLLATE "pg_catalog"."default",
  "status" int2,
  "min_money" numeric(11,2),
  "max_money" numeric(16,2),
  "dayupper_limit" numeric(20,6),
  "deposit_config_ids" varchar(250) COLLATE "pg_catalog"."default",
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_deposit_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_deposit_strategy"."deposit_type" IS '支付类型，5=在线支付，6=快速入款，7=银行转账，12=手动加款';
COMMENT ON COLUMN "public"."station_deposit_strategy"."gift_type" IS '赠送方式 1=固定数额 2=浮动比例';
COMMENT ON COLUMN "public"."station_deposit_strategy"."back_gift_type" IS '返佣赠送方式 1=固定数额 2=浮动比例';
COMMENT ON COLUMN "public"."station_deposit_strategy"."value_type" IS '赠送类型  1=彩金  2=积分';
COMMENT ON COLUMN "public"."station_deposit_strategy"."deposit_count" IS '0表示每次, 1表示首充, N表示第N次';
COMMENT ON COLUMN "public"."station_deposit_strategy"."gift_value" IS '赠送额度';
COMMENT ON COLUMN "public"."station_deposit_strategy"."back_bonus_level_diff" IS '返佣层级差';
COMMENT ON COLUMN "public"."station_deposit_strategy"."bet_rate" IS '打码量倍数。(充值金额+赠送)x流水倍数=出款需要达到的投注量';
COMMENT ON COLUMN "public"."station_deposit_strategy"."bonus_back_vaue" IS '给上级代理/推荐人返佣的额度';
COMMENT ON COLUMN "public"."station_deposit_strategy"."bonus_back_bet_rate" IS '上级代理/推荐人得到返佣额度时的出款所需打码量倍数';
COMMENT ON COLUMN "public"."station_deposit_strategy"."upper_limit" IS '活动期间赠送上限';
COMMENT ON COLUMN "public"."station_deposit_strategy"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_deposit_strategy"."start_datetime" IS '开始时间';
COMMENT ON COLUMN "public"."station_deposit_strategy"."end_datetime" IS '截止时间';
COMMENT ON COLUMN "public"."station_deposit_strategy"."remark" IS '备注';
COMMENT ON COLUMN "public"."station_deposit_strategy"."status" IS '状态 1=禁用，2=启用';
COMMENT ON COLUMN "public"."station_deposit_strategy"."min_money" IS '最小充值金额，=0代表不限制';
COMMENT ON COLUMN "public"."station_deposit_strategy"."max_money" IS '最大充值金额，=0代表不限制';
COMMENT ON COLUMN "public"."station_deposit_strategy"."dayupper_limit" IS '单日赠送上限';
COMMENT ON COLUMN "public"."station_deposit_strategy"."deposit_config_ids" IS '站点入款方式Ids';
COMMENT ON COLUMN "public"."station_deposit_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_deposit_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON TABLE "public"."station_deposit_strategy" IS '用户充值赠送策略';

CREATE SEQUENCE "public"."station_draw_fee_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_draw_fee_strategy" (
  "id" int4 NOT NULL DEFAULT nextval('station_draw_fee_strategy_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "fee_type" int2,
  "fee_value" numeric(20,6),
  "draw_num" int2,
  "upper_limit" numeric(20,6),
  "lower_limit" numeric(20,6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_draw_fee_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."fee_type" IS '手续费类型 1:固定手续费 2:浮动手续费';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."fee_value" IS '手续费值';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."draw_num" IS '免费提款次数';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."upper_limit" IS '手续费上限';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."lower_limit" IS '手续费下限';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."status" IS '启用状态';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_draw_fee_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON TABLE "public"."station_draw_fee_strategy" IS '站点每日最高在线统计';

CREATE SEQUENCE "public"."station_score_exchange_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_score_exchange" (
  "id" int4 NOT NULL DEFAULT nextval('station_score_exchange_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "name" varchar(25) COLLATE "pg_catalog"."default",
  "type" int2,
  "numerator" numeric(16,2),
  "denominator" numeric(16,2),
  "max_val" numeric(16,2),
  "min_val" numeric(16,2),
  "status" int2,
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_score_exchange" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_score_exchange"."name" IS '配置名称';
COMMENT ON COLUMN "public"."station_score_exchange"."type" IS '配置类型';
COMMENT ON COLUMN "public"."station_score_exchange"."numerator" IS '兑换分子数';
COMMENT ON COLUMN "public"."station_score_exchange"."denominator" IS '兑换分母数';
COMMENT ON COLUMN "public"."station_score_exchange"."max_val" IS '单次兑换最大值';
COMMENT ON COLUMN "public"."station_score_exchange"."min_val" IS '单次兑换最小值';
COMMENT ON COLUMN "public"."station_score_exchange"."status" IS '状态(1、禁用，2、启用)';
COMMENT ON COLUMN "public"."station_score_exchange"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_score_exchange"."bet_rate" IS '打码量倍数';

CREATE SEQUENCE "public"."station_dummy_data_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_dummy_data" (
  "id" int4 NOT NULL DEFAULT nextval('station_dummy_data_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "win_money" numeric(20,6),
  "win_time" timestamp(6),
  "type" int2,
  "item_name" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_dummy_data" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_dummy_data"."username" IS '会员账号名';
COMMENT ON COLUMN "public"."station_dummy_data"."win_money" IS '中奖金额';
COMMENT ON COLUMN "public"."station_dummy_data"."win_time" IS '中奖时间';
COMMENT ON COLUMN "public"."station_dummy_data"."type" IS '类型，1=红包，2=大转盘，3=彩票';
COMMENT ON COLUMN "public"."station_dummy_data"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_dummy_data"."item_name" IS '大转盘产品名称,彩种名称';
COMMENT ON TABLE "public"."station_dummy_data" IS '虚拟数据';

CREATE SEQUENCE "public"."station_online_num_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_online_num" (
  "id" int4 NOT NULL DEFAULT nextval('station_online_num_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "report_datetime" timestamp(6),
  "stat_date" varchar(10),
  "stat_minute" varchar(5),
  "online_num" int2,
  "minute_login_num" int2,
  "day_login_num" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_online_num" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_online_num"."stat_date" IS '统计的日期';
COMMENT ON COLUMN "public"."station_online_num"."stat_minute" IS '统计的分钟';
COMMENT ON COLUMN "public"."station_online_num"."report_datetime" IS '具体时间';
COMMENT ON COLUMN "public"."station_online_num"."online_num" IS '当前在线人数';
COMMENT ON COLUMN "public"."station_online_num"."minute_login_num" IS '每分钟登陆的会员数';
COMMENT ON COLUMN "public"."station_online_num"."day_login_num" IS '当天总登陆个数';
COMMENT ON TABLE "public"."station_online_num" IS '站点每日最高在线统计';
CREATE UNIQUE INDEX "statin_highest_online_num_unique" ON "public"."station_online_num"("station_id","stat_date","stat_minute");
CREATE INDEX "station_online_num_sidrd" ON "public"."station_online_num" USING btree ("station_id", "report_datetime");

CREATE OR REPLACE FUNCTION "public"."create_station_kickback_record_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(bet_date>='''|| startTime ||''' AND bet_date< '''|| endTime ||'''))
			INHERITS (station_kickback_record);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE UNIQUE INDEX '||tablename||'_uidex ON ' ||tablename||' USING btree("bet_date","username","bet_type","station_id");' ;
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_station_kickback_record_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO station_kickback_record_'||to_char(NEW.bet_date,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE SEQUENCE "public"."station_kickback_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_kickback_record" (
  "id" int4 NOT NULL DEFAULT nextval('station_kickback_record_id_seq'::regclass)PRIMARY KEY ,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "bet_date" date,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "bet_type" int2,
  "bet_money" numeric(26,6) DEFAULT 0,
  "real_bet_num" numeric(26,6) DEFAULT 0,
  "win_money" numeric(26,6) DEFAULT 0,
  "kickback_money" numeric(26,6),
  "kickback_rate" numeric(6,3),
  "kickback_desc" varchar(500) COLLATE "pg_catalog"."default",
  "draw_need" numeric(16,2) DEFAULT 0,
  "status" int2,
  "proxy_id" int4,
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "operator" varchar(50) COLLATE "pg_catalog"."default",
  "operator_id" int4,
  "degree_id" int4,
  "degree_name" varchar(50) COLLATE "pg_catalog"."default",
  "group_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_kickback_record" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_kickback_record"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_kickback_record"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."station_kickback_record"."bet_date" IS '投注日期';
COMMENT ON COLUMN "public"."station_kickback_record"."username" IS '用户名';
COMMENT ON COLUMN "public"."station_kickback_record"."bet_type" IS '投注类型(1真人,2电子游艺,3体育,4沙巴体育)';
COMMENT ON COLUMN "public"."station_kickback_record"."bet_money" IS '投注金额';
COMMENT ON COLUMN "public"."station_kickback_record"."real_bet_num" IS '有效打码';
COMMENT ON COLUMN "public"."station_kickback_record"."win_money" IS '会员中奖金额';
COMMENT ON COLUMN "public"."station_kickback_record"."kickback_money" IS '反水金额';
COMMENT ON COLUMN "public"."station_kickback_record"."kickback_rate" IS '返水比率(1-100)';
COMMENT ON COLUMN "public"."station_kickback_record"."kickback_desc" IS '反水描述';
COMMENT ON COLUMN "public"."station_kickback_record"."draw_need" IS '反水需要的打码量';
COMMENT ON COLUMN "public"."station_kickback_record"."status" IS '会员返水状态(1还未返水 2返水已到账  3返水已经回滚)';
COMMENT ON COLUMN "public"."station_kickback_record"."proxy_id" IS '代理id';
COMMENT ON COLUMN "public"."station_kickback_record"."degree_id" IS '会员等级id';
COMMENT ON COLUMN "public"."station_kickback_record"."degree_name" IS '会员等级名称';
COMMENT ON COLUMN "public"."station_kickback_record"."group_id" IS '会员组别id';
COMMENT ON TABLE "public"."station_kickback_record" IS '会员反水记录表，按日投注和游戏类型来反水';
COMMENT ON COLUMN "public"."station_kickback_record"."proxy_name" IS '代理账号';

CREATE TRIGGER "insert_station_kickback_record_partition_trigger" BEFORE INSERT ON "public"."station_kickback_record"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_station_kickback_record_partition"();

CREATE SEQUENCE "public"."station_kickback_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_kickback_strategy" (
  "id" int4 NOT NULL DEFAULT nextval('station_kickback_strategy_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "kickback" numeric(6,4),
  "min_bet" numeric(20,6),
  "bet_rate" numeric(10,3),
  "max_back" numeric(20,6),
  "status" int2 DEFAULT 2,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "type" int2,
  "create_datetime" timestamp(6),
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_kickback_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_kickback_strategy"."kickback" IS '反水比例';
COMMENT ON COLUMN "public"."station_kickback_strategy"."min_bet" IS '有效投注（大于等于）';
COMMENT ON COLUMN "public"."station_kickback_strategy"."bet_rate" IS '打码量倍数';
COMMENT ON COLUMN "public"."station_kickback_strategy"."max_back" IS '反水上限';
COMMENT ON COLUMN "public"."station_kickback_strategy"."type" IS '类型：1：彩票反水 ，2：体育反水 ，3：真人反水，4：电子反水，5：六合彩反水';
COMMENT ON COLUMN "public"."station_kickback_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_kickback_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON TABLE "public"."station_kickback_strategy" IS '会员按日反水策略';

CREATE SEQUENCE "public"."sys_user_avatar_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_avatar" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_avatar_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "create_user_id" int4,
  "create_username" varchar(50) COLLATE "pg_catalog"."default",
  "url" varchar(256) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_avatar"OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_avatar"."url" IS '头像url';
COMMENT ON COLUMN "public"."sys_user_avatar"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_avatar"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."sys_user_avatar"."create_user_id" IS '创建账号id';
COMMENT ON COLUMN "public"."sys_user_avatar"."create_username" IS '创建账号';
COMMENT ON TABLE "public"."sys_user_avatar" IS '会员头像表';

CREATE SEQUENCE "public"."sys_user_avatar_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_avatar_record" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_avatar_record_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "avatar_id" int4,
  "url" varchar(256) COLLATE "pg_catalog"."default",
  "type" int2,
  "create_datetime" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_avatar_record" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_avatar_record"."avatar_id" IS '头像ID';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."url" IS '头像url';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."username" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."type" IS '头像类型:1.当前头像 2.历史头像';
COMMENT ON COLUMN "public"."sys_user_avatar_record"."create_datetime" IS '创建时间';
COMMENT ON TABLE "public"."sys_user_avatar_record" IS '会员头像记录';

CREATE SEQUENCE "public"."station_money_income_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_money_income_strategy" (
  "id" int4 NOT NULL DEFAULT nextval('station_money_income_strategy_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "balance_limit" numeric(20,4),
  "balance_max_limit" numeric(20,4),
  "bet_rate" numeric(10,3),
  "less_have_income" int2,
  "less_min_scale" numeric(10,3),
  "less_max_scale" numeric(10,3),
  "more_min_scale" numeric(10,3),
  "more_max_scale" numeric(10,3),
  "income_bet_rate" numeric(10,3),
  "remark" varchar(200) COLLATE "pg_catalog"."default",
  "status" int2,
  "send_msg" int2,
  "start_time" date,
  "end_time" date
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_money_income_strategy" OWNER TO "postgres";
CREATE INDEX "station_money_income_strategy_station_id_index" ON "public"."station_money_income_strategy" USING btree ("station_id");
COMMENT ON COLUMN "public"."station_money_income_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_money_income_strategy"."balance_limit" IS '最低余额';
COMMENT ON COLUMN "public"."station_money_income_strategy"."balance_max_limit" IS '最大余额';
COMMENT ON COLUMN "public"."station_money_income_strategy"."bet_rate" IS '余额所需打码倍数';
COMMENT ON COLUMN "public"."station_money_income_strategy"."less_have_income" IS '少于打码时是否计算收益';
COMMENT ON COLUMN "public"."station_money_income_strategy"."less_min_scale" IS '少于打码时最小利率';
COMMENT ON COLUMN "public"."station_money_income_strategy"."less_max_scale" IS '少于打码时最大利率';
COMMENT ON COLUMN "public"."station_money_income_strategy"."more_min_scale" IS '大于打码时最大利率';
COMMENT ON COLUMN "public"."station_money_income_strategy"."more_max_scale" IS '大于打码时最大利率';
COMMENT ON COLUMN "public"."station_money_income_strategy"."income_bet_rate" IS '收益打码量';
COMMENT ON COLUMN "public"."station_money_income_strategy"."send_msg" IS '发放收益后是否发送站内信';
COMMENT ON COLUMN "public"."station_money_income_strategy"."start_time" IS '开始时间';
COMMENT ON COLUMN "public"."station_money_income_strategy"."end_time" IS '结束时间';
COMMENT ON TABLE "public"."station_money_income_strategy" IS '余额生金策略';

CREATE SEQUENCE "public"."station_money_income_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_money_income_record" (
  "id" int4 NOT NULL DEFAULT nextval('station_money_income_record_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "stat_date" date,
  "balance" numeric(20,4),
  "income" numeric(20,4),
  "scale" numeric(10,4),
  "bet_rate" numeric(10,3),
  "status" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_money_income_record" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_money_income_record_sdus_ind" ON "public"."station_money_income_record" USING btree ("station_id","user_id","stat_date");
COMMENT ON COLUMN "public"."station_money_income_record"."balance" IS '余额';
COMMENT ON COLUMN "public"."station_money_income_record"."income" IS '收益';
COMMENT ON COLUMN "public"."station_money_income_record"."scale" IS '利率';
COMMENT ON COLUMN "public"."station_money_income_record"."bet_rate" IS '当日打码';
COMMENT ON COLUMN "public"."station_money_income_record"."status" IS '1:赠送成功 2：回滚成功 3：回滚失败';
COMMENT ON TABLE "public"."station_money_income_record" IS '余额生金记录';

CREATE SEQUENCE "public"."station_promotion_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_promotion" (
  "id" int4 NOT NULL DEFAULT nextval('station_promotion_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "type" int2,
  "mode" int2,
  "code" varchar(15) COLLATE "pg_catalog"."default",
  "domain" varchar(140) COLLATE "pg_catalog"."default",
  "register_num" int4,
  "access_num" int4,
  "create_time" timestamp(6),
  "access_page" int2 DEFAULT 1,
  "live" numeric(10,4) DEFAULT 0,
  "egame" numeric(10,4) DEFAULT 0,
  "chess" numeric(10,4) DEFAULT 0,
  "esport" numeric(10,4) DEFAULT 0,
  "sport" numeric(10,4) DEFAULT 0,
  "fishing" numeric(10,4) DEFAULT 0,
  "lottery" numeric(10,4) DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_promotion" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_promotion_lk" ON "public"."station_promotion" USING btree ("code");
COMMENT ON COLUMN "public"."station_promotion"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_promotion"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."station_promotion"."username" IS '推广会员账号';
COMMENT ON COLUMN "public"."station_promotion"."type" IS '链接注册进来的会员类型UserTypeEnum.PROXY 和 UserTypeEnum.USER';
COMMENT ON COLUMN "public"."station_promotion"."mode" IS '推广码类型，1=代理推广代理或会员，2=会员推会员';
COMMENT ON COLUMN "public"."station_promotion"."code" IS '推广码';
COMMENT ON COLUMN "public"."station_promotion"."domain" IS '前端域名';
COMMENT ON COLUMN "public"."station_promotion"."register_num" IS '注册人数';
COMMENT ON COLUMN "public"."station_promotion"."access_num" IS '访问人数';
COMMENT ON COLUMN "public"."station_promotion"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."station_promotion"."access_page" IS '链接访问页面 1：注册页面，2：首页,3:优惠活动，4：游戏大厅';
COMMENT ON COLUMN "public"."station_promotion"."live" IS '真人返点数';
COMMENT ON TABLE "public"."station_promotion" IS '代理推会员模式的推广链接表';

CREATE SEQUENCE "public"."station_message_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_message" (
  "id" int4 NOT NULL DEFAULT nextval('station_message_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "content" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "title" varchar(200) COLLATE "pg_catalog"."default",
  "receive_type" int2,
  "degree_id" int4,
  "group_id" int4,
  "send_type" int2,
  "send_username" varchar(50) COLLATE "pg_catalog"."default",
  "send_user_id" int4,
  "pop_status" int2,
  "proxy_name" varchar(50)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_message" OWNER TO "postgres";
CREATE INDEX "station_message_create_time_index" ON "public"."station_message" USING btree ("create_time");
CREATE INDEX "station_message_send_receive_type_index" ON "public"."station_message" USING btree ("receive_type","send_type","send_username");
COMMENT ON COLUMN "public"."station_message"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_message"."content" IS '信息内容';
COMMENT ON COLUMN "public"."station_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."station_message"."title" IS '信息标题';
COMMENT ON COLUMN "public"."station_message"."receive_type" IS '接收类型 1个人 2群发 3层级 4上级 5下级   ';
COMMENT ON COLUMN "public"."station_message"."degree_id" IS '用户等级ID';
COMMENT ON COLUMN "public"."station_message"."group_id" IS '用户组别ID';
COMMENT ON COLUMN "public"."station_message"."send_type" IS '发送方类型，1租户后台，2会员';
COMMENT ON COLUMN "public"."station_message"."send_username" IS '发送方账号';
COMMENT ON COLUMN "public"."station_message"."send_user_id" IS '发送方ID';
COMMENT ON COLUMN "public"."station_message"."pop_status" IS '是否弹窗显示 1否 2是';
COMMENT ON COLUMN "public"."station_message"."proxy_name" IS '代理线';
COMMENT ON TABLE "public"."station_message" IS '站点站内信发送表';

CREATE SEQUENCE "public"."station_message_receive_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_message_receive" (
  "id" int4 NOT NULL DEFAULT nextval('station_message_receive_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "message_id" int4,
  "status" int2,
  "pop_status" int2 DEFAULT 1,
  "send_type" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_message_receive" OWNER TO "postgres";
CREATE INDEX "station_message_receive_uid_idx" ON "public"."station_message_receive" USING btree ("user_id");
CREATE INDEX "station_message_receive_sps_idx" ON "public"."station_message_receive" USING btree ("status","pop_status","send_type");
COMMENT ON COLUMN "public"."station_message_receive"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."station_message_receive"."username" IS '用户账号';
COMMENT ON COLUMN "public"."station_message_receive"."message_id" IS '站内信ID';
COMMENT ON COLUMN "public"."station_message_receive"."status" IS '状态（1、未读，2、已读）';
COMMENT ON COLUMN "public"."station_message_receive"."pop_status" IS '是否弹窗显示 1否 2是';
COMMENT ON COLUMN "public"."station_message_receive"."send_type" IS '发送方类型，1租户后台，2会员';
COMMENT ON TABLE "public"."station_message_receive" IS '站点站内信接收表';

CREATE SEQUENCE "public"."station_red_packet_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_red_packet" (
  "id" int4 NOT NULL DEFAULT nextval('station_red_packet_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "total_number" int2,
  "total_money" numeric(20,6),
  "min_money" numeric(20,6),
  "remain_money" numeric(20,6),
  "remain_number" int2,
  "begin_datetime" timestamp(6),
  "end_datetime" timestamp(6),
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "status" int2,
  "ip_number" int2,
  "invite_depositer_num" int2,
  "bet_rate" numeric(10,3),
  "money_base" numeric(16,2),
  "today_deposit" int2,
  "manual_deposit" int2,
  "max_money" numeric(20,6),
  "packet_strict" int2,
  "rednum_type" int2,
  "money_custom" varchar(300) COLLATE "pg_catalog"."default",
  "red_bag_type" int2,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
   "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "select_mutil_deposit" int2,
  "his_begin" timestamp(6),
  "his_end" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_red_packet" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_red_packet"."total_number" IS '总人数';
COMMENT ON COLUMN "public"."station_red_packet"."total_money" IS '总金额';
COMMENT ON COLUMN "public"."station_red_packet"."min_money" IS '最小红包金额';
COMMENT ON COLUMN "public"."station_red_packet"."remain_money" IS '剩余金额';
COMMENT ON COLUMN "public"."station_red_packet"."remain_number" IS '剩余次数';
COMMENT ON COLUMN "public"."station_red_packet"."title" IS '红包标题';
COMMENT ON COLUMN "public"."station_red_packet"."status" IS '启用状态';
COMMENT ON COLUMN "public"."station_red_packet"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_red_packet"."ip_number" IS '同一IP可以抢多少次';
COMMENT ON COLUMN "public"."station_red_packet"."bet_rate" IS '打码量倍数';
COMMENT ON COLUMN "public"."station_red_packet"."money_base" IS '抢一次红包所需充值金额';
COMMENT ON COLUMN "public"."station_red_packet"."today_deposit" IS '当天有充值的会员才能抢红包,2=需要当天有充值，1=不限制';
COMMENT ON COLUMN "public"."station_red_packet"."manual_deposit" IS '手工充值是否纳入充值金额,2=有效，1=无效';
COMMENT ON COLUMN "public"."station_red_packet"."max_money" IS '单个会员可领取最大金额';
COMMENT ON COLUMN "public"."station_red_packet"."packet_strict" IS '红包个数等级限制,2=有效，1=无效';
COMMENT ON COLUMN "public"."station_red_packet"."rednum_type" IS '单个会员领取红包次数统计方案,2=方案二，1=方案一';
COMMENT ON COLUMN "public"."station_red_packet"."money_custom" IS '红包间隔金额设置';
COMMENT ON COLUMN "public"."station_red_packet"."red_bag_type" IS '1或者为空按方案配置;2按层级配置;3 按充值量配置';
COMMENT ON COLUMN "public"."station_red_packet"."select_mutil_deposit" IS '1只能在一个区间领取2每个区间都能领取';
COMMENT ON COLUMN "public"."station_red_packet"."his_begin" IS '历史累计充值开始时间';
COMMENT ON COLUMN "public"."station_red_packet"."his_end" IS '历史累计充值结束时间';
COMMENT ON COLUMN "public"."station_red_packet"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_red_packet"."group_ids" IS '有效会员组别id 多个以,分割';

CREATE SEQUENCE "public"."station_red_packet_degree_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_red_packet_degree" (
  "id" int4 NOT NULL DEFAULT nextval('station_red_packet_degree_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "packet_id" int4,
  "degree_id" int4,
  "packet_number" int2,
  "packet_number_got" int2,
  "red_bag_min" numeric(20,6),
  "red_bag_max" numeric(20,6),
  "red_bag_recharge_min" numeric(20,6),
  "red_bag_recharge_max" numeric(20,6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_red_packet_degree" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_red_packet_degree"."packet_id" IS '红包id';
COMMENT ON COLUMN "public"."station_red_packet_degree"."degree_id" IS '会员等级id';
COMMENT ON COLUMN "public"."station_red_packet_degree"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_red_packet_degree"."packet_number" IS '单个等级红包数量';
COMMENT ON COLUMN "public"."station_red_packet_degree"."packet_number_got" IS '单个等级红包剩余数量';
COMMENT ON COLUMN "public"."station_red_packet_degree"."red_bag_min" IS '可抽取最小金额';
COMMENT ON COLUMN "public"."station_red_packet_degree"."red_bag_max" IS '可抽取最大金额';
COMMENT ON COLUMN "public"."station_red_packet_degree"."red_bag_recharge_min" IS '充值最小金额';
COMMENT ON COLUMN "public"."station_red_packet_degree"."red_bag_recharge_max" IS '充值最大金额';

CREATE SEQUENCE "public"."station_red_packet_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_red_packet_record" (
  "id" int4 NOT NULL DEFAULT nextval('station_red_packet_record_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "user_id" int4 NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "money" numeric(20,6),
  "create_datetime" timestamp(6),
  "packet_id" int4 NOT NULL,
  "packet_name" varchar(100) COLLATE "pg_catalog"."default",
  "status" int2,
  "remark" text COLLATE "pg_catalog"."default",
  "ip" varchar(100) COLLATE "pg_catalog"."default",
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_red_packet_record" OWNER TO "postgres";
CREATE INDEX "station_red_packet_record_cdt" ON "public"."station_red_packet_record" USING btree ("create_datetime");
CREATE INDEX "station_red_packet_record_iid" ON "public"."station_red_packet_record" USING btree ("packet_id","station_id");
COMMENT ON COLUMN "public"."station_red_packet_record"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_red_packet_record"."status" IS '处理状态';
COMMENT ON COLUMN "public"."station_red_packet_record"."username" IS '用户名';
COMMENT ON COLUMN "public"."station_red_packet_record"."remark" IS '备注';
COMMENT ON COLUMN "public"."station_red_packet_record"."packet_name" IS '红包名称';
COMMENT ON COLUMN "public"."station_red_packet_record"."ip" IS 'ip地址';
COMMENT ON COLUMN "public"."station_red_packet_record"."bet_rate" IS '打码量倍数';

CREATE SEQUENCE "public"."station_register_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_register_config" (
  "id" int4 NOT NULL DEFAULT nextval('station_register_config_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "name" varchar(20) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "default",
  "platform" int2 NOT NULL DEFAULT 1,
  "ele_name" varchar(20) COLLATE "pg_catalog"."default",
  "show" int2,
  "validate" int2,
  "required" int2,
  "uniqueness" int2,
  "sort_no" int4,
  "ele_type" int2,
  "tips" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_register_config" OWNER TO "postgres";
CREATE UNIQUE INDEX "station_register_config_spen" ON "public"."station_register_config" USING btree ("station_id","platform","ele_name");
COMMENT ON COLUMN "public"."station_register_config"."name" IS '字段名称';
COMMENT ON COLUMN "public"."station_register_config"."platform" IS '作用平台（1会员、2代理）';
COMMENT ON COLUMN "public"."station_register_config"."ele_name" IS '对应用户的信息表的字段';
COMMENT ON COLUMN "public"."station_register_config"."show" IS '是否展示（1否2是）';
COMMENT ON COLUMN "public"."station_register_config"."validate" IS '是否需要验证（1否2是）';
COMMENT ON COLUMN "public"."station_register_config"."required" IS '是否必填（1否2是）';
COMMENT ON COLUMN "public"."station_register_config"."uniqueness" IS '是否唯一';
COMMENT ON COLUMN "public"."station_register_config"."sort_no" IS '序号';
COMMENT ON COLUMN "public"."station_register_config"."ele_type" IS '字段类型，1=text,2=password';
COMMENT ON COLUMN "public"."station_register_config"."tips" IS '注册提示语';
COMMENT ON COLUMN "public"."station_register_config"."code" IS '多语言的code';
COMMENT ON TABLE "public"."station_register_config" IS '站点注册页面有哪些字段';

CREATE SEQUENCE "public"."station_sign_rule_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_sign_rule" (
  "id" int4 NOT NULL DEFAULT nextval('station_sign_rule_id_seq'::regclass)PRIMARY KEY,
  "station_id" int4,
  "days" int2,
  "score" int4,
  "today_deposit" int2,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "day_gift_config" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700),
  "is_reset" int2,
  "deposit_money" numeric(20,6),
  "bet_need" numeric(20,6),
  "money" numeric(20,6),
  "today_bet" int2,
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_sign_rule" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_sign_rule"."today_deposit" IS '当天有充值的会员才能签到,2=需要当天有充值，1=不限制';
COMMENT ON COLUMN "public"."station_sign_rule"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_sign_rule"."group_ids" IS '有效会员组别id';
COMMENT ON COLUMN "public"."station_sign_rule"."day_gift_config" IS '签到每日赠送彩金配置';
COMMENT ON COLUMN "public"."station_sign_rule"."is_reset" IS '签到天数到达后是否重置签到 1否2是';
COMMENT ON COLUMN "public"."station_sign_rule"."deposit_money" IS '需充值金额';
COMMENT ON COLUMN "public"."station_sign_rule"."bet_need" IS '所需打码';
COMMENT ON COLUMN "public"."station_sign_rule"."money" IS '赠送金额';
COMMENT ON COLUMN "public"."station_sign_rule"."today_bet" IS '当天有打码才能签到，2=当天需要打码，1不限制';
COMMENT ON COLUMN "public"."station_sign_rule"."bet_rate" IS '彩金所需打码倍数';
COMMENT ON COLUMN "public"."station_sign_rule"."days" IS '连续签到天数';
COMMENT ON TABLE "public"."station_sign_rule" IS '会员签到赠送积分、金额规则表';

CREATE SEQUENCE "public"."station_sign_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_sign_record" (
  "id" int4 NOT NULL DEFAULT nextval('station_sign_record_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4 NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "sign_date" timestamp(6),
  "score" int4,
  "sign_days" int2,
  "rule_id" int4,
  "money" numeric(20,6),
  "ip" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_sign_record" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_sign_record"."user_id" IS '会员id';
COMMENT ON COLUMN "public"."station_sign_record"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."station_sign_record"."score" IS '签到获得积分';
COMMENT ON COLUMN "public"."station_sign_record"."sign_days" IS '连续签到天数';
COMMENT ON COLUMN "public"."station_sign_record"."username" IS '会员账号';
COMMENT ON COLUMN "public"."station_sign_record"."rule_id" IS '签到规则ID';
COMMENT ON COLUMN "public"."station_sign_record"."money" IS '签到获得彩金';
COMMENT ON COLUMN "public"."station_sign_record"."ip" IS '签到IP';
COMMENT ON TABLE "public"."station_sign_record" IS '会员签到日志表';

CREATE SEQUENCE "public"."station_turntable_gift_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_turntable_gift" (
  "id" int4 NOT NULL DEFAULT nextval('station_turntable_gift_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "product_name" varchar(200) COLLATE "pg_catalog"."default",
  "product_desc" text COLLATE "pg_catalog"."default",
  "price" numeric(16,2),
  "create_datetime" timestamp(6),
  "product_img" varchar(500) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_turntable_gift" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_turntable_gift"."product_name" IS '奖品名称';
COMMENT ON COLUMN "public"."station_turntable_gift"."product_desc" IS '奖品描述';
COMMENT ON COLUMN "public"."station_turntable_gift"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_turntable_gift"."price" IS '价值';
COMMENT ON COLUMN "public"."station_turntable_gift"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_turntable_gift"."product_img" IS '奖品图片路径';
COMMENT ON TABLE "public"."station_turntable_gift" IS '转盘奖品表';


CREATE SEQUENCE "public"."station_turntable_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_turntable" (
  "id" int4 NOT NULL DEFAULT nextval('station_turntable_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "status" int2,
  "create_datetime" timestamp(6),
  "begin_datetime" timestamp(6),
  "end_datetime" timestamp(6),
  "score" numeric(16,2),
  "play_count" int2,
  "award_count" int2,
  "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
  "group_ids" varchar(700) COLLATE "pg_catalog"."default",
  "active_help" text COLLATE "pg_catalog"."default",
  "img_path" varchar(255) COLLATE "pg_catalog"."default",
  "active_role" text COLLATE "pg_catalog"."default",
  "active_remark" text COLLATE "pg_catalog"."default",
  "play_num_type" int2,
  "join_type" int2,
  "count_type" int2,
  "play_config" text COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_turntable" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_turntable"."name" IS '大转盘名称';
COMMENT ON COLUMN "public"."station_turntable"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_turntable"."status" IS '状态';
COMMENT ON COLUMN "public"."station_turntable"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_turntable"."begin_datetime" IS '开始时间';
COMMENT ON COLUMN "public"."station_turntable"."end_datetime" IS '结束时间';
COMMENT ON COLUMN "public"."station_turntable"."score" IS '消耗积分';
COMMENT ON COLUMN "public"."station_turntable"."play_count" IS '单天可玩次数';
COMMENT ON COLUMN "public"."station_turntable"."award_count" IS '奖项数量';
COMMENT ON COLUMN "public"."station_turntable"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_turntable"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_turntable"."active_help" IS '活动规则';
COMMENT ON COLUMN "public"."station_turntable"."active_role" IS '抽奖资格';
COMMENT ON COLUMN "public"."station_turntable"."active_remark" IS '活动声明';
COMMENT ON COLUMN "public"."station_turntable"."play_num_type" IS '可玩次数类型(1 总次数  2 区间次数 )';
COMMENT ON COLUMN "public"."station_turntable"."join_type" IS '参与类型，1：按充值计算，2：按打码量计算';
COMMENT ON COLUMN "public"."station_turntable"."count_type" IS '统计类型，1：当天统计，2：累计统计';
COMMENT ON COLUMN "public"."station_turntable"."play_config" IS '配置, [{"min":"最小金额","max"："最大金额","num":"可玩次数"}]';
COMMENT ON TABLE "public"."station_turntable" IS '转盘活动表';

CREATE SEQUENCE "public"."station_turntable_award_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_turntable_award" (
  "id" int4 NOT NULL DEFAULT nextval('station_turntable_award_id_seq'::regclass) PRIMARY KEY,
  "turntable_id" int4,
  "award_name" varchar(100) COLLATE "pg_catalog"."default",
  "gift_id" int4,
  "chance" numeric(20,6),
  "award_type" int2,
  "award_count" int4,
  "award_value" numeric(16,2),
  "award_index" int4,
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_turntable_award" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_turntable_award"."award_name" IS '奖项名称';
COMMENT ON COLUMN "public"."station_turntable_award"."gift_id" IS '转盘奖品ID';
COMMENT ON COLUMN "public"."station_turntable_award"."chance" IS '概率基数';
COMMENT ON COLUMN "public"."station_turntable_award"."award_type" IS '奖项类型（1、不中奖，2、现金，3、奖品，4、积分）';
COMMENT ON COLUMN "public"."station_turntable_award"."award_count" IS '奖项数量';
COMMENT ON COLUMN "public"."station_turntable_award"."award_value" IS '面值';
COMMENT ON COLUMN "public"."station_turntable_award"."award_index" IS '奖项索引';
COMMENT ON COLUMN "public"."station_turntable_award"."turntable_id" IS '转盘活动ID';
COMMENT ON COLUMN "public"."station_turntable_award"."bet_rate" IS '打码量';
COMMENT ON TABLE "public"."station_turntable_award" IS '转盘活动奖项表';

CREATE SEQUENCE "public"."station_turntable_play_num_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_turntable_play_num" (
  "id" int4 NOT NULL DEFAULT nextval('station_turntable_play_num_id_seq'::regclass) PRIMARY KEY,
  "user_id" int4 NOT NULL,
  "cur_date" date,
  "active_num" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_turntable_play_num" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_turntable_play_num"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."station_turntable_play_num"."cur_date" IS '当前日期';
COMMENT ON COLUMN "public"."station_turntable_play_num"."active_num" IS '参与活动次数';
COMMENT ON TABLE "public"."station_turntable_play_num" IS '用户当天参与活动次数表';
CREATE UNIQUE INDEX "station_turntable_play_num_ukey" ON "public"."station_turntable_play_num" USING btree ("user_id","cur_date");

CREATE SEQUENCE "public"."station_turntable_record_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_turntable_record" (
  "id" int4 NOT NULL DEFAULT nextval('station_turntable_record_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "turntable_id" int4,
  "award_type" int2,
  "gift_id" int4,
  "gift_name" varchar(200) COLLATE "pg_catalog"."default",
  "award_value" numeric(16,2),
  "create_datetime" timestamp(6),
  "status" int2,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_turntable_record" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_turntable_record"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_turntable_record"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."station_turntable_record"."username" IS '用户账号';
COMMENT ON COLUMN "public"."station_turntable_record"."turntable_id" IS '转盘活动ID';
COMMENT ON COLUMN "public"."station_turntable_record"."award_type" IS '奖项类型（1、不中奖，2、现金，3、商品，4、积分）';
COMMENT ON COLUMN "public"."station_turntable_record"."gift_id" IS '奖品ID';
COMMENT ON COLUMN "public"."station_turntable_record"."gift_name" IS '奖品名称';
COMMENT ON COLUMN "public"."station_turntable_record"."award_value" IS '奖项面值';
COMMENT ON COLUMN "public"."station_turntable_record"."create_datetime" IS '中奖时间';
COMMENT ON COLUMN "public"."station_turntable_record"."status" IS '处理状态(1、未处理，2、已处理，3、处理失败)';
COMMENT ON COLUMN "public"."station_turntable_record"."remark" IS '备注说明';
COMMENT ON COLUMN "public"."station_turntable_record"."bet_rate" IS '现金打码量';
COMMENT ON TABLE "public"."station_turntable_record" IS '用户大转盘中奖记录';

CREATE SEQUENCE "public"."sys_user_bank_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_bank" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_bank_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "card_no" varchar(500) COLLATE "pg_catalog"."default",
  "real_name" varchar(500) COLLATE "pg_catalog"."default",
  "bank_code" varchar(20) COLLATE "pg_catalog"."default",
  "bank_address" varchar(100) COLLATE "pg_catalog"."default",
  "bank_name" varchar(100) COLLATE "pg_catalog"."default",
  "remarks" varchar(255) COLLATE "pg_catalog"."default",
  "status" int2,
  "create_time" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_bank" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_bank"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_bank"."username" IS '会员账号';
COMMENT ON COLUMN "public"."sys_user_bank"."card_no" IS '卡号';
COMMENT ON COLUMN "public"."sys_user_bank"."real_name" IS '真实姓名';
COMMENT ON COLUMN "public"."sys_user_bank"."bank_code" IS '银行代码';
COMMENT ON COLUMN "public"."sys_user_bank"."bank_address" IS '银行地址';
COMMENT ON COLUMN "public"."sys_user_bank"."bank_name" IS '银行名称';
COMMENT ON COLUMN "public"."sys_user_bank"."remarks" IS '备注';
COMMENT ON COLUMN "public"."sys_user_bank"."status" IS '状态：2正常，1禁用';
COMMENT ON TABLE "public"."sys_user_bank" IS '用户银行卡信息表';
CREATE INDEX "sys_user_bank_su" ON "public"."sys_user_bank" (
  "station_id",
  "user_id"
);

CREATE TABLE "public"."sys_user_bet_num" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "station_id" int4,
  "draw_need" numeric(16,2) DEFAULT 0,
  "bet_num" numeric(16,2) DEFAULT 0,
  "total_bet_num" numeric(20,2) DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_bet_num" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_bet_num"."draw_need" IS '出款所需打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num"."bet_num" IS '当前打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num"."total_bet_num" IS '总打码量';
COMMENT ON TABLE "public"."sys_user_bet_num" IS '会员打码量信息';

CREATE SEQUENCE "public"."sys_user_bet_num_history_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_bet_num_history" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_bet_num_history_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "agent_name" varchar(50) COLLATE "pg_catalog"."default",
  "proxy_id" int4,
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "parent_ids" text COLLATE "pg_catalog"."default",
  "user_id" int4 NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "bet_num" numeric(16,2),
  "before_num" numeric(16,2),
  "after_num" numeric(16,2),
  "type" int2,
  "remark" text COLLATE "pg_catalog"."default",
  "operator_id" int4,
  "operator_name" varchar(50) COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "order_id" varchar(255) COLLATE "pg_catalog"."default",
  "draw_need" numeric(16,2),
  "before_draw_need" numeric(16,2),
  "after_draw_need" numeric(16,2),
  "biz_datetime" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_bet_num_history" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."user_id" IS '账号id';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."before_num" IS '变动前打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."after_num" IS '变动后打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."bet_num" IS '变动的打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."type" IS '变动类型';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."remark" IS '描述';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."operator_id" IS '操作人id';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."operator_name" IS '操作人账号';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."order_id" IS '订单号';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."parent_ids" IS '用户父级';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."username" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."draw_need" IS '提款所需打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."before_draw_need" IS '变动前提款所需打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."after_draw_need" IS '变动后提款所需打码量';
COMMENT ON COLUMN "public"."sys_user_bet_num_history"."biz_datetime" IS '投注时间';
COMMENT ON TABLE "public"."sys_user_bet_num_history" IS '会员打码量变动记录';


CREATE OR REPLACE FUNCTION "public"."create_bet_num_history_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
  RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
			INHERITS (sys_user_bet_num_history);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE INDEX '||tablename||'_uid_type ON ' ||tablename||' USING btree(user_id,type);' ;
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_create_datetime ON '||tablename||' USING btree(create_datetime);';
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_station_id ON '||tablename||' USING btree(station_id);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_bet_num_history_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO sys_user_bet_num_history_'||to_char(NEW.create_datetime,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER "insert_bet_num_history_partition_trigger" BEFORE INSERT ON "public"."sys_user_bet_num_history"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_bet_num_history_partition"();
  
  
CREATE TABLE "public"."sys_user_info" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "station_id" int4,
  "real_name" varchar(500) COLLATE "pg_catalog"."default",
  "neck_name" varchar(100) COLLATE "pg_catalog"."default",
  "receipt_pwd" varchar(50) COLLATE "pg_catalog"."default",
  "phone" varchar(500) COLLATE "pg_catalog"."default",
  "email" varchar(500) COLLATE "pg_catalog"."default",
  "qq" varchar(500) COLLATE "pg_catalog"."default",
  "wechat" varchar(500) COLLATE "pg_catalog"."default",
  "facebook" varchar(500) COLLATE "pg_catalog"."default",
  "line" varchar(500) COLLATE "pg_catalog"."default",
  "province" varchar(100) COLLATE "pg_catalog"."default",
  "city" varchar(100) COLLATE "pg_catalog"."default",
  "modify_bankcard_time" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_info" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_info"."real_name" IS '真实姓名';
COMMENT ON COLUMN "public"."sys_user_info"."neck_name" IS '昵称';
COMMENT ON COLUMN "public"."sys_user_info"."receipt_pwd" IS '提款密码';
COMMENT ON COLUMN "public"."sys_user_info"."phone" IS '手机';
COMMENT ON COLUMN "public"."sys_user_info"."email" IS '电子邮箱';
COMMENT ON COLUMN "public"."sys_user_info"."qq" IS 'QQ号码';
COMMENT ON COLUMN "public"."sys_user_info"."wechat" IS '微信号';
COMMENT ON COLUMN "public"."sys_user_info"."facebook" IS '脸书';
COMMENT ON COLUMN "public"."sys_user_info"."line" IS 'Line账户';
COMMENT ON COLUMN "public"."sys_user_info"."province" IS '省份';
COMMENT ON COLUMN "public"."sys_user_info"."city" IS '城市';
COMMENT ON COLUMN "public"."sys_user_info"."modify_bankcard_time" IS '新增或修改银行卡时间';
COMMENT ON TABLE "public"."sys_user_info" IS '存储会员基本信息'; 

CREATE SEQUENCE "public"."sys_user_degree_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_degree" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_degree_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "member_count" int4 NOT NULL DEFAULT 0,
  "deposit_money" numeric(16,2) NOT NULL DEFAULT 0,
  "status" int2,
  "create_datetime" timestamp(6),
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "remark" text COLLATE "pg_catalog"."default",
  "original" int2 default 1,
  "icon" varchar(256) COLLATE "pg_catalog"."default",
  "upgrade_money" numeric(16,2),
  "upgrade_send_msg" int2 DEFAULT 1,
  "bet_rate" numeric(10,3),
  "bet_num" numeric(20,2) DEFAULT 0,
  "type" int2 DEFAULT 1,
  "skip_money" numeric(16,2)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_degree" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_degree"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_degree"."member_count" IS '成员数量';
COMMENT ON COLUMN "public"."sys_user_degree"."deposit_money" IS '充值金额';
COMMENT ON COLUMN "public"."sys_user_degree"."status" IS '状态(1禁用，2启用)';
COMMENT ON COLUMN "public"."sys_user_degree"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_degree"."name" IS '等级名称';
COMMENT ON COLUMN "public"."sys_user_degree"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_user_degree"."original" IS '是否默认未分层(1否，2是)';
COMMENT ON COLUMN "public"."sys_user_degree"."icon" IS '用户等级图标';
COMMENT ON COLUMN "public"."sys_user_degree"."upgrade_money" IS '等级升级奖金';
COMMENT ON COLUMN "public"."sys_user_degree"."upgrade_send_msg" IS '升级是否发送站内信通知,1=否，2=是';
COMMENT ON COLUMN "public"."sys_user_degree"."bet_rate" IS '升级赠送金额打码量倍数';
COMMENT ON COLUMN "public"."sys_user_degree"."bet_num" IS '打码量';
COMMENT ON COLUMN "public"."sys_user_degree"."type" IS '1=使用充值金额，2使用打码量，3=充值与打码量并存';
 COMMENT ON COLUMN "public"."sys_user_degree"."skip_money" IS '跨级奖励金额';
COMMENT ON TABLE "public"."sys_user_degree" IS '会员层级信息';
  
CREATE SEQUENCE "public"."sys_user_group_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_group" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_group_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "name" varchar(50) COLLATE "pg_catalog"."default",
  "member_count" int4 NOT NULL DEFAULT 0,
  "status" int2,
  "create_datetime" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default",
  "daily_draw_num" int2,
  "max_draw_money" numeric(20,2),
  "min_draw_money" numeric(20,2),
  "original" int2 default 1
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_group" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_group"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_group"."name" IS '组别名称';
COMMENT ON COLUMN "public"."sys_user_group"."member_count" IS '成员数量';
COMMENT ON COLUMN "public"."sys_user_group"."status" IS '状态(1禁用，2启用)';
COMMENT ON COLUMN "public"."sys_user_group"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_group"."remark" IS '备注';
COMMENT ON COLUMN "public"."sys_user_group"."original" IS '是否默认组别(1否，2是)';
COMMENT ON COLUMN "public"."sys_user_group"."daily_draw_num" IS '每日最高提款次数';
COMMENT ON COLUMN "public"."sys_user_group"."max_draw_money" IS '最大提款金额';
COMMENT ON COLUMN "public"."sys_user_group"."min_draw_money" IS '最小提款金额';
COMMENT ON TABLE "public"."sys_user_group" IS '会员组别信息';

CREATE SEQUENCE "public"."sys_user_degree_log_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_degree_log" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_degree_log_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4 NOT NULL,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "content" text COLLATE "pg_catalog"."default",
  "create_datetime" timestamp(6),
  "old_id" int4,
  "old_name" varchar(50) COLLATE "pg_catalog"."default",
  "new_id" int4,
  "new_name" varchar(50) COLLATE "pg_catalog"."default",
  "operator" varchar(50) COLLATE "pg_catalog"."default",
  "operator_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_degree_log" OWNER TO "postgres";
CREATE INDEX "sys_user_degree_log_su" ON "public"."sys_user_degree_log" USING btree ("station_id","user_id");
COMMENT ON COLUMN "public"."sys_user_degree_log"."operator" IS '操作者账号';
COMMENT ON COLUMN "public"."sys_user_degree_log"."operator_id" IS '操作者id';
COMMENT ON COLUMN "public"."sys_user_degree_log"."username" IS '会员账号';
COMMENT ON TABLE "public"."sys_user_degree_log" IS '会员等级变动日志';

CREATE TABLE "public"."sys_user_proxy_num" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "proxy_num" int4,
  "member_num" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_proxy_num" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_proxy_num"."user_id" IS '会员ID';
COMMENT ON COLUMN "public"."sys_user_proxy_num"."proxy_num" IS '下级代理数量';
COMMENT ON COLUMN "public"."sys_user_proxy_num"."member_num" IS '下级会员数量';
COMMENT ON TABLE "public"."sys_user_proxy_num" IS '代理下级人数记录表';

CREATE TABLE "public"."sys_user_rebate" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "station_id" int4,
  "live" numeric(10,4) DEFAULT 0,
  "egame" numeric(10,4) DEFAULT 0,
  "chess" numeric(10,4) DEFAULT 0,
  "esport" numeric(10,4) DEFAULT 0,
  "sport" numeric(10,4) DEFAULT 0,
  "fishing" numeric(10,4) DEFAULT 0,
  "lottery" numeric(10,4) DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_rebate" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_rebate"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_rebate"."live" IS '真人返点';
COMMENT ON COLUMN "public"."sys_user_rebate"."egame" IS '电子返点';
COMMENT ON TABLE "public"."sys_user_rebate" IS '代理返点设置';

CREATE TABLE "public"."sys_user_score" (
  "user_id" int4 NOT NULL PRIMARY KEY,
  "score" int4 DEFAULT 0,
  "last_sign_date" timestamp(6),
  "sign_count" int2,
  "station_id" int4
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_score" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_score"."score" IS '会员积分';
COMMENT ON COLUMN "public"."sys_user_score"."last_sign_date" IS '最后一次签到时间';
COMMENT ON COLUMN "public"."sys_user_score"."sign_count" IS '连续签到次数';
COMMENT ON TABLE "public"."sys_user_score" IS '会员积分信息';

CREATE SEQUENCE "public"."sys_user_score_history_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_score_history" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_score_history_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "before_score" int4,
  "score" int4,
  "after_score" int4,
  "type" int2,
  "create_datetime" timestamp(6),
  "remark" text COLLATE "pg_catalog"."default",
  "sign_count" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."sys_user_score_history" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_score_history"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_score_history"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_score_history"."username" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_score_history"."before_score" IS '变动前积分';
COMMENT ON COLUMN "public"."sys_user_score_history"."score" IS '变动积分';
COMMENT ON COLUMN "public"."sys_user_score_history"."after_score" IS '变动后积分';
COMMENT ON COLUMN "public"."sys_user_score_history"."type" IS '账变类型';
COMMENT ON COLUMN "public"."sys_user_score_history"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_score_history"."remark" IS '备注说明';
COMMENT ON COLUMN "public"."sys_user_score_history"."sign_count" IS '连续签到次数';
COMMENT ON TABLE "public"."sys_user_score_history" IS '积分变动记录表';


CREATE OR REPLACE FUNCTION "public"."create_score_history_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
			INHERITS (sys_user_score_history);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE INDEX '||tablename||'_uid_type ON ' ||tablename||' USING btree(station_id,user_id,type);' ;
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_create_datetime ON '||tablename||' USING btree(create_datetime);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_score_history_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO sys_user_score_history_'||to_char(NEW.create_datetime,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER "insert_score_history_partition_trigger" BEFORE INSERT ON "public"."sys_user_score_history"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_score_history_partition"();


CREATE TABLE "public"."third_game" (
  "station_id" int4  NOT NULL PRIMARY KEY,
  "partner_id" int4,
  "live" int2 default 1,
  "egame" int2 default 1,
  "chess" int2 default 1,
  "fishing" int2 default 1,
  "esport" int2 default 1,
  "sport" int2 default 1,
  "lottery" int2 default 1
)WITH (OIDS=FALSE);
ALTER TABLE "public"."third_game" OWNER TO "postgres";
COMMENT ON COLUMN "public"."third_game"."live" IS '真人视讯，1=关，2=开';
COMMENT ON COLUMN "public"."third_game"."egame" IS '电子';
COMMENT ON COLUMN "public"."third_game"."chess" IS '棋牌';
COMMENT ON COLUMN "public"."third_game"."fishing" IS '捕鱼';
COMMENT ON COLUMN "public"."third_game"."esport" IS '电竞';
COMMENT ON COLUMN "public"."third_game"."sport" IS '体育';
COMMENT ON COLUMN "public"."third_game"."lottery" IS '彩票';
COMMENT ON COLUMN "public"."third_game"."station_id" IS '站点id';
COMMENT ON TABLE "public"."third_game" IS '游戏大开关';

CREATE SEQUENCE "public"."third_platform_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."third_platform" (
  "id" int4 NOT NULL DEFAULT nextval('third_platform_id_seq'::regclass) PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "status" int2,
  "platform" int2
)WITH (OIDS=FALSE);
ALTER TABLE "public"."third_platform" OWNER TO "postgres";
COMMENT ON COLUMN "public"."third_platform"."status" IS '状态，1=关，2=开';
COMMENT ON COLUMN "public"."third_platform"."platform" IS '平台类型，AG、BBIN';
COMMENT ON COLUMN "public"."third_platform"."station_id" IS '站点id';
COMMENT ON TABLE "public"."third_platform" IS '第三方游戏开关';
CREATE UNIQUE INDEX "third_platform_sidp" ON "public"."third_platform" ("station_id","platform");

CREATE SEQUENCE "public"."third_auto_exchange_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."third_auto_exchange" (
  "id" int4 NOT NULL DEFAULT nextval('third_auto_exchange_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "platform" int2,
  "type" int2,
  "update_time" timestamp(0)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."third_auto_exchange" OWNER TO "postgres";
CREATE UNIQUE INDEX "third_auto_exchange_unique" ON "public"."third_auto_exchange" USING btree ("platform","station_id","user_id");
COMMENT ON COLUMN "public"."third_auto_exchange"."platform" IS '第三方游戏类型';
COMMENT ON COLUMN "public"."third_auto_exchange"."station_id" IS '站点id';
COMMENT ON COLUMN "public"."third_auto_exchange"."user_id" IS '会员id';
COMMENT ON COLUMN "public"."third_auto_exchange"."type" IS '最后操作类型，2=系统转入第三方，1=第三方转入系统';
COMMENT ON TABLE "public"."third_auto_exchange" IS '第三方游戏额度自动转换记录表';

CREATE SEQUENCE "public"."third_player_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."third_player_config" (
  "id" int4 NOT NULL DEFAULT nextval('third_player_config_id_seq'::regclass),
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "config_name" varchar(50) COLLATE "pg_catalog"."default",
  "config_value" varchar(50) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."third_player_config" OWNER TO "postgres";
COMMENT ON COLUMN "public"."third_player_config"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."third_player_config"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."third_player_config"."config_value" IS '配置值';
COMMENT ON TABLE "public"."third_player_config" IS '用户第三方配置表';

CREATE SEQUENCE "public"."third_trans_log_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."third_trans_log" (
  "id" int4 NOT NULL DEFAULT nextval('third_trans_log_id_seq'::regclass)PRIMARY KEY,
  "partner_id" int4,
  "station_id" int4,
  "user_id" int4,
  "username" varchar(50) COLLATE "pg_catalog"."default",
  "order_id" varchar(36) COLLATE "pg_catalog"."default",
  "platform" int2 NOT NULL,
  "type" int2 NOT NULL,
  "money" numeric(26,6) NOT NULL,
  "status" int2 NOT NULL,
  "create_datetime" timestamp(6),
  "before_money" numeric(26,6),
  "after_money" numeric(26,6),
  "update_datetime" timestamp(6),
  "msg" varchar(500)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."third_trans_log" OWNER TO "postgres";
CREATE INDEX "third_trans_log_cdt" ON "public"."third_trans_log" USING btree ("create_datetime");
COMMENT ON COLUMN "public"."third_trans_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."third_trans_log"."username" IS '账号';
COMMENT ON COLUMN "public"."third_trans_log"."order_id" IS '本地交易ID';
COMMENT ON COLUMN "public"."third_trans_log"."platform" IS '1,表示AG 2,表示BBin 3，表示MG';
COMMENT ON COLUMN "public"."third_trans_log"."type" IS '1.从第三方转出;2.转入第三方';
COMMENT ON COLUMN "public"."third_trans_log"."status" IS '1，表示转账成功 2，表示转账失败 3，未知状态';
COMMENT ON COLUMN "public"."third_trans_log"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."third_trans_log"."update_datetime" IS '修改时间';
COMMENT ON COLUMN "public"."third_trans_log"."msg" IS '原因';

CREATE SEQUENCE "public"."app_game_foot_id_seq" INCREMENT 1 START 104 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_game_foot" (
    "id" int4 NOT NULL DEFAULT nextval('app_game_foot_id_seq'::regclass)PRIMARY KEY,
    "partner_id" int4,
    "station_id" int4,
    "user_id" int4,
    "username" varchar(50) COLLATE "pg_catalog"."default",
	"game_name" varchar(50) COLLATE "default",
	"game_code" varchar(20) COLLATE "default",
	"parent_game_code" varchar(20) COLLATE "default",
	"game_type" int2 DEFAULT 1,
	"custom_link" varchar(200) COLLATE "default",
	"custom_icon" varchar(200) COLLATE "default",
	"visit_num" int4,
	"is_sub_game" int2 default 0,
	"create_datetime" timestamp(6) NULL,
	"status" int2,
	"type" int2 DEFAULT 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."app_game_foot" OWNER TO "postgres";
COMMENT ON COLUMN "public"."app_game_foot"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."app_game_foot"."username" IS '用户名';
COMMENT ON COLUMN "public"."app_game_foot"."game_name" IS '游戏名称';
COMMENT ON COLUMN "public"."app_game_foot"."game_code" IS '游戏code';
COMMENT ON COLUMN "public"."app_game_foot"."parent_game_code" IS '子游戏时对应的父游戏code(只有在子游戏时存在)';
COMMENT ON COLUMN "public"."app_game_foot"."game_type" IS '游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义 9--其他';
COMMENT ON COLUMN "public"."app_game_foot"."custom_link" IS '点击跳转链接 gameType 为自定义时使用';
COMMENT ON COLUMN "public"."app_game_foot"."custom_icon" IS '图标icon gameType 为自定义时使用';
COMMENT ON COLUMN "public"."app_game_foot"."visit_num" IS '访问量';
COMMENT ON COLUMN "public"."app_game_foot"."is_sub_game" IS '是否某类大游戏下的子游戏 1--是 0--否';
COMMENT ON COLUMN "public"."app_game_foot"."status" IS '状态  2--开启 1--关闭';
COMMENT ON COLUMN "public"."app_game_foot"."type" IS '足迹类型 1--租户配置的足迹 0--用户足迹';
COMMENT ON TABLE "public"."app_game_foot" IS '用户足迹';

CREATE SEQUENCE "public"."app_hot_game_id_seq" INCREMENT 1 START 104 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_hot_game" (
    "id" int4 NOT NULL DEFAULT nextval('app_hot_game_id_seq'::regclass)PRIMARY KEY,
    "partner_id" int4,
    "station_id" int4,
	"name" varchar(50) COLLATE "default",
	"code" varchar(50) COLLATE "default",
	"parent_code" varchar(50) COLLATE "default",
	"link" varchar(200) COLLATE "default",
	"icon" varchar(200) COLLATE "default",
	"status" int2,
	"create_datetime" timestamp(6) NULL,
	"sort_no" int4,
	"type" int2 default 1
)
WITH (OIDS=FALSE);
ALTER TABLE "public"."app_hot_game" OWNER TO "postgres";
COMMENT ON COLUMN "public"."app_hot_game"."code" IS '游戏code';
COMMENT ON COLUMN "public"."app_hot_game"."parent_code" IS '子游戏时对应的父游戏code(只有在子游戏时存在)';
COMMENT ON COLUMN "public"."app_hot_game"."link" IS '三方游戏跳转链接';
COMMENT ON COLUMN "public"."app_hot_game"."icon" IS '三方游戏icon链接';
COMMENT ON COLUMN "public"."app_hot_game"."type" IS '游戏类型 1--彩票 2--真人 3--电子 4--体育 5--电竞 6--捕鱼 7--棋牌 8--自定义';
COMMENT ON TABLE "public"."app_hot_game" IS '热门游戏';

CREATE SEQUENCE "public"."app_index_menu_id_seq" INCREMENT 1 START 104 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_index_menu" (
    "id" int4 NOT NULL DEFAULT nextval('app_index_menu_id_seq'::regclass)PRIMARY KEY,
    "partner_id" int4,
    "station_id" int4,
	"name" varchar(50) COLLATE "default",
	"code" varchar(20) COLLATE "default",
	"status" int2,
	"create_datetime" timestamp(6) NULL,
	"sort_no" int2,
	"link" varchar(200) COLLATE "default",
	"icon" varchar(200) COLLATE "default"
)WITH (OIDS=FALSE);
ALTER TABLE "public"."app_index_menu" OWNER TO "postgres";
COMMENT ON TABLE "public"."app_index_menu" IS 'APP首页菜单表';

CREATE SEQUENCE "public"."app_search_history_id_seq" INCREMENT 1 START 104 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."app_search_history" (
    "id" int4 NOT NULL DEFAULT nextval('app_search_history_id_seq'::regclass),
    "partner_id" int4,
    "station_id" int4,
    "user_id" int4,
    "username" varchar(50) COLLATE "pg_catalog"."default",
	"keyword" varchar(50) COLLATE "default",
	"create_datetime" timestamp(6) NULL,
	"count" int4 default 0
)WITH (OIDS=FALSE);
ALTER TABLE "public"."app_search_history" OWNER TO "postgres";
COMMENT ON TABLE "public"."app_search_history" IS '历史搜索';
COMMENT ON COLUMN "public"."app_search_history"."keyword" IS '搜索关键字';
COMMENT ON COLUMN "public"."app_search_history"."count" IS '搜索次数';



CREATE OR REPLACE FUNCTION "public"."change_user_degree"("curid" int4, "nextid" int4, "stationid" int4)
  RETURNS "pg_catalog"."int2" AS $BODY$
	DECLARE 
	strSQL varchar; 
 BEGIN
	IF ( nextId > 0 ) THEN
         -- 创建主键
        strSQL := 'UPDATE sys_user SET degree_id='||nextId||' WHERE station_id ='||stationId||' AND degree_id='||curId;
        EXECUTE strSQL;
	END IF;
        -- 创建索引  
        strSQL := 'update sys_user_degree set member_count=0 where station_id='||stationId;  
        EXECUTE strSQL;
        strSQL := 'with aaa as (select count(*) count,degree_id from sys_user where type<>150 and station_id='||stationId
		||' group by degree_id) update sys_user_degree set member_count=aaa.count from aaa where aaa.degree_id=sys_user_degree.id' ;  
        EXECUTE strSQL;
	return 1;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

ALTER FUNCTION "public"."change_user_degree"("curid" int4, "nextid" int4, "stationid" int4) OWNER TO "postgres";


CREATE OR REPLACE FUNCTION "public"."restat_member_count"("stationid" int4)
  RETURNS "pg_catalog"."int2" AS $BODY$
	DECLARE 
	strSQL varchar; 
 BEGIN
        -- 创建索引  
        strSQL := 'update sys_user_group set member_count=0 where station_id='||stationId;  
        EXECUTE strSQL;
        strSQL := 'with aaa as (select count(*) count,group_id from sys_user where type<>150 and station_id='||stationId
		||' group by group_id) update sys_user_group set member_count=aaa.count from aaa where aaa.group_id=sys_user_group.id' ;  
        EXECUTE strSQL;
	return 1;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

ALTER FUNCTION "public"."restat_member_count"("stationid" int4) OWNER TO "postgres";

DROP FUNCTION IF EXISTS "public".optbetnumber(int4,numeric);
CREATE OR REPLACE FUNCTION "public"."optbetnumber"("userid" int4, "drawneed" numeric, "clear" bool, "minmoney" numeric)
  RETURNS TABLE("user_id" int4, "bet_num" numeric, "before_draw_need" numeric, "draw_need" numeric) AS $BODY$
DECLARE 
	mnys numeric[2];
	sql1 varchar;
	row1 RECORD;
	mny numeric;
	betnum numeric;
 BEGIN
    sql1 := 'select ARRAY[draw_need::numeric,bet_num::numeric] as coltext from sys_user_bet_num where user_id = ' || userid ||' for update ';
    execute sql1 into mnys ;

    if mnys ISNULL or mnys[1] ISNULL or  mnys[2] ISNULL then 
			return; 
    end if;

    if clear then
        sql1 := 'select money from sys_user_money where user_id = ' || userid;
        execute sql1 into mny;
        if mny ISNULL or mny <=minmoney then
            betnum=drawneed;
        end if;
    end if;

    if betnum ISNULL then
        IF mnys[1]-mnys[2] <= 0 THEN
            betnum := drawneed;
        ELSE
            betnum := mnys[1]-mnys[2]+drawneed;
        END IF;
	end if;

    sql1 := 'update sys_user_bet_num set bet_num =0,draw_need= ' || betnum ||' where  user_id = ' || userid ||' RETURNING *';
    execute sql1 into row1;

    RETURN QUERY SELECT row1.user_id,mnys[2],mnys[1],betnum ;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

ALTER FUNCTION "public"."optbetnumber"("userid" int4, "drawneed" numeric, "clear" bool, "minmoney" numeric) OWNER TO "postgres";

CREATE OR REPLACE FUNCTION "public"."optscore"("userid" int4, "optmoney" int4)
  RETURNS "pg_catalog"."_int4" AS $BODY$
    DECLARE
	mny int4;
	sql1 varchar;
	sql2 varchar;
	result int4[2];
 BEGIN
    sql1 := 'select score from sys_user_score where user_id = ''' || userId ||''' for update ';
    execute sql1 into mny ;
    IF mny + optmoney < 0 AND optmoney < 0 THEN
	result[0] := 0;
	result[1] := mny;
	return result;
    END IF;
    sql2 := 'update sys_user_score set score = score + ''' || optMoney ||''' where user_id = ''' || userId ||'''';
    execute sql2;
    result[0] := 1;
    result[1] := mny;
    RETURN result;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
 
CREATE SEQUENCE "public"."third_transfer_out_limit_id_seq" INCREMENT 1 START 104 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."third_transfer_out_limit" (
  "id" int4 NOT NULL DEFAULT nextval('third_transfer_out_limit_id_seq'::regclass)PRIMARY KEY ,
  "platform" int2,
  "station_id" int4,
  "min_money" numeric(10,2),
  "max_money" numeric(16,2),
  "limit_accounts" text COLLATE "pg_catalog"."default"
);
ALTER TABLE "public"."third_transfer_out_limit" OWNER TO "postgres";
COMMENT ON COLUMN "public"."third_transfer_out_limit"."platform" IS '平台';
COMMENT ON COLUMN "public"."third_transfer_out_limit"."min_money" IS '最小金额';
COMMENT ON COLUMN "public"."third_transfer_out_limit"."max_money" IS '最大金额';
COMMENT ON COLUMN "public"."third_transfer_out_limit"."limit_accounts" IS '限制会员账号';
COMMENT ON TABLE "public"."third_transfer_out_limit" IS '站点转入第三方限额表';


CREATE SEQUENCE "public"."station_rebate_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_rebate" (
  "id" int4 NOT NULL DEFAULT nextval('station_rebate_id_seq'::regclass) PRIMARY KEY,
  "station_id" int4,
  "user_type" int2,
  "type" int2,
  "rebate_mode" int2,
  "live" numeric(10,4) DEFAULT 0,
  "egame" numeric(10,4) DEFAULT 0,
  "chess" numeric(10,4) DEFAULT 0,
  "esport" numeric(10,4) DEFAULT 0,
  "sport" numeric(10,4) DEFAULT 0,
  "fishing" numeric(10,4) DEFAULT 0,
  "lottery" numeric(10,4) DEFAULT 0,
  "level_diff" numeric(10,4) DEFAULT 0,
  "max_diff" numeric(10,4) DEFAULT 0,
  "bet_rate" numeric(10,3)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_rebate" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_rebate"."user_type" IS '1=代理返点，2=会员推荐返点';
COMMENT ON COLUMN "public"."station_rebate"."type" IS '代理返点类型，1=所有层级返点一样，2=逐级递减';
COMMENT ON COLUMN "public"."station_rebate"."rebate_mode" IS '返点方式，1=第二天自动返点，2=第二天手动返点';
COMMENT ON COLUMN "public"."station_rebate"."level_diff" IS '返点层级差';
COMMENT ON COLUMN "public"."station_rebate"."max_diff" IS '返点层级最大差值';
COMMENT ON COLUMN "public"."station_rebate"."bet_rate" IS '打码量倍数';
COMMENT ON TABLE "public"."station_rebate" IS '站点返点设置信息';
CREATE UNIQUE INDEX "station_rebate_unkey" ON "public"."station_rebate" ("station_id","user_type");


CREATE SEQUENCE "public"."station_advice_feedback_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_advice_feedback" (
  "id" int4 not null default nextval('station_advice_feedback_id_seq') PRIMARY KEY,
  "station_id" int4,
  "content" text COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "final_time" timestamp(6),
  "status" int2,
  "send_type" int2,
  "send_username" varchar(50),
  "send_user_id" int4
);
COMMENT ON TABLE "public"."station_advice_feedback" IS '建议反馈记录表';
COMMENT ON COLUMN "public"."station_advice_feedback"."send_type" IS '建议类型，1提交建议、2我要投诉';
COMMENT ON COLUMN "public"."station_advice_feedback"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_advice_feedback"."content" IS '建议内容';
COMMENT ON COLUMN "public"."station_advice_feedback"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."station_advice_feedback"."final_time" IS '最后反馈时间';
COMMENT ON COLUMN "public"."station_advice_feedback"."status" IS '状态 1待回复 2 已回复 ';
COMMENT ON COLUMN "public"."station_advice_feedback"."send_username" IS '发送方账号';
COMMENT ON COLUMN "public"."station_advice_feedback"."send_user_id" IS '发送方ID';
CREATE INDEX station_advice_feedback_create_time_index ON station_advice_feedback (create_time);
CREATE INDEX station_advice_feedback_send_type_index ON station_advice_feedback (send_type,send_user_id);


CREATE SEQUENCE "public"."station_advice_content_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_advice_content" (
  "id" int4 not null default nextval('station_advice_content_id_seq') PRIMARY KEY,
  "user_id" int4,
  "username" varchar(50),
  "content" text COLLATE "pg_catalog"."default",
  "advice_id" int4,
  "create_time" timestamp(6),
  "content_type" int2
);
COMMENT ON TABLE "public"."station_advice_content" IS '建议反馈回复表';
COMMENT ON COLUMN "public"."station_advice_content"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."station_advice_content"."username" IS '用户账号';
COMMENT ON COLUMN "public"."station_advice_content"."advice_id" IS '建议反馈ID';
COMMENT ON COLUMN "public"."station_advice_content"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."station_advice_content"."content_type" IS '回复类型（1、后台管理员，2、会员）';
COMMENT ON COLUMN "public"."station_advice_content"."content" IS '回复内容';
CREATE INDEX "station_advice_content_user_id_idx" ON "public"."station_advice_content" USING btree ("user_id");


CREATE SEQUENCE "public"."station_bank_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_bank" (
  "id" int4 NOT NULL DEFAULT nextval('station_bank_id_seq'::regclass) PRIMARY KEY,
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "code" varchar(50) COLLATE "pg_catalog"."default",
  "sort_no" int2,
  "station_id" int4,
  "create_time" timestamp(6)
)WITH (OIDS=FALSE);
ALTER TABLE "public"."station_bank" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_bank"."name" IS '银行名称';
COMMENT ON COLUMN "public"."station_bank"."code" IS '编号';
COMMENT ON COLUMN "public"."station_bank"."sort_no" IS '排序序号，大的排前面';
COMMENT ON TABLE "public"."station_bank" IS '站点银行信息表';
CREATE UNIQUE INDEX "station_bank_sc" ON "public"."station_bank" ("station_id","code");

CREATE SEQUENCE "public"."station_replace_withdraw_id_seq" INCREMENT 1 START 5 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_replace_withdraw"
(
    "id"              int4         NOT NULL                  default nextval('station_replace_withdraw_id_seq') PRIMARY KEY,
    "merchant_code"   varchar(5000) COLLATE "pg_catalog"."default",
    "merchant_key"    varchar(5000) COLLATE "pg_catalog"."default",
    "url"             varchar(256) COLLATE "pg_catalog"."default",
    "account"         varchar(5000) COLLATE "pg_catalog"."default",
    "min"             numeric,
    "max"             numeric,
    "def"             int2,
    "status"          int2,
    "version"         int2,
    "station_id"      int4,
    "icon"            varchar(256) COLLATE "pg_catalog"."default",
    "pay_platform_id" int4,
    "pay_type"        varchar(20) COLLATE "pg_catalog"."default",
    "pay_getway"      varchar(500) COLLATE "pg_catalog"."default",
    "level_group"     varchar COLLATE "pg_catalog"."default" DEFAULT 0,
    "degree_ids"      varchar(700) COLLATE "pg_catalog"."default",
    "group_ids"       varchar(700) COLLATE "pg_catalog"."default",
    "appid"           varchar(500) COLLATE "pg_catalog"."default",
    "sort_no"         int4                                   DEFAULT 0,
    "show_type"       varchar(20) COLLATE "pg_catalog"."default",
    "recharge_type"   varchar(20) COLLATE "pg_catalog"."default",
    "fixed_amount"    varchar(50) COLLATE "pg_catalog"."default",
    "extra"           varchar COLLATE "pg_catalog"."default",
    "pay_status"      varchar COLLATE "pg_catalog"."default",
    "search_getway"   varchar(500),
    "white_list_ip"   varchar(500),
    "remark"          varchar(500),
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


DROP TABLE IF EXISTS "public"."sys_user_tron_link";
DROP SEQUENCE IF EXISTS "public"."sys_user_tron_link_id_seq";
CREATE SEQUENCE "public"."sys_user_tron_link_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_tron_link" (
    "id" int8 NOT NULL DEFAULT nextval('sys_user_tron_link_id_seq'::regclass)PRIMARY KEY,
    "station_id" int8,
    "user_id" int8,
    "user_name" varchar(50),
    "tron_link_addr" varchar(512),
    "init_trx" numeric(16,4),
    "create_datetime" timestamp(6),
    "init_valid_datetime" timestamp(6),
    "bind_status" int2
)WITH (OIDS=FALSE);

ALTER TABLE "public"."sys_user_tron_link" OWNER TO "postgres";
CREATE UNIQUE INDEX "sys_user_tron_link_idx" ON "public"."sys_user_tron_link" USING btree ("tron_link_addr");
CREATE INDEX "sys_user_tron_link_ui_idx" ON "public"."sys_user_tron_link" USING btree ("user_id");
CREATE INDEX "sys_user_tron_link_ur_idx" ON "public"."sys_user_tron_link" USING btree ("user_name");

COMMENT ON COLUMN "public"."sys_user_tron_link"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_tron_link"."user_name" IS '用户账号';
COMMENT ON COLUMN "public"."sys_user_tron_link"."tron_link_addr" IS 'Tron链地址';
COMMENT ON COLUMN "public"."sys_user_tron_link"."init_trx" IS '初始化trx金额';
COMMENT ON COLUMN "public"."sys_user_tron_link"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."sys_user_tron_link"."init_valid_datetime" IS '初始化转账最后时间';
COMMENT ON COLUMN "public"."sys_user_tron_link"."bind_status" IS '绑定状态（1未绑定 2已绑定）';
COMMENT ON TABLE "public"."sys_user_tron_link" IS '用户Tron链管理';


DROP TABLE IF EXISTS "public"."tron_trans_index";
DROP SEQUENCE IF EXISTS "public"."tron_trans_index_id_seq";
CREATE SEQUENCE "public"."tron_trans_index_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_index" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_index_id_seq'::regclass)PRIMARY KEY,
    "last_timestamp" timestamp(6),
    "last_transaction_id" varchar(255) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_index" OWNER TO "postgres";

COMMENT ON COLUMN "public"."tron_trans_index"."last_timestamp" IS '最后执行到时间点';
COMMENT ON COLUMN "public"."tron_trans_index"."last_transaction_id" IS '最后执行到的交易ID';
COMMENT ON TABLE "public"."tron_trans_index" IS '站点转入第三方限额表';


DROP TABLE IF EXISTS "public"."tron_trans_index_address";
DROP SEQUENCE IF EXISTS "public"."tron_trans_index_address_id_seq";
CREATE SEQUENCE "public"."tron_trans_index_address_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_index_address" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_index_address_id_seq'::regclass)PRIMARY KEY,
    "last_timestamp" timestamp(6),
    "tron_link_addr" varchar(255) COLLATE "pg_catalog"."default"
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_index_address" OWNER TO "postgres";

COMMENT ON COLUMN "public"."tron_trans_index_address"."last_timestamp" IS '最后执行到时间点';
COMMENT ON COLUMN "public"."tron_trans_index_address"."tron_link_addr" IS 'Tron链地址';
COMMENT ON TABLE "public"."tron_trans_index_address" IS '站点转入第三方限额表';


DROP TABLE IF EXISTS "public"."tron_trans_user";
DROP SEQUENCE IF EXISTS "public"."tron_trans_user_id_seq";
CREATE SEQUENCE "public"."tron_trans_user_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_user" (
    "id" int8 NOT NULL DEFAULT nextval('tron_trans_user_id_seq'::regclass)PRIMARY KEY,
    "trans_from" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "trans_to" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "trans_value" numeric(16,6) NOT NULL,
    "block" int8 NOT NULL,
    "transaction_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
    "transaction_type" int4 NOT NULL,
    "transaction_timestamp" int8 NOT NULL,
    "create_datetime" timestamp(6),
    "station_id" int4
)WITH (OIDS=FALSE);

ALTER TABLE "public"."tron_trans_user" OWNER TO "postgres";
CREATE UNIQUE INDEX "tron_usdt_trans_uti_idx" ON "public"."tron_trans_user" USING btree ("transaction_id");
CREATE INDEX "tron_usdt_trans_uf_idx" ON "public"."tron_trans_user" USING btree ("trans_from");
CREATE INDEX "tron_usdt_trans_ut_idx" ON "public"."tron_trans_user" USING btree ("trans_to");
CREATE INDEX "tron_usdt_create_datetime" ON "public"."tron_trans_user" USING btree ("create_datetime");
CREATE INDEX "tron_usdt_station_id" ON "public"."tron_trans_user" USING btree ("station_id");

COMMENT ON COLUMN "public"."tron_trans_user"."trans_from" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user"."trans_to" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user"."trans_value" IS '交易金额';
COMMENT ON COLUMN "public"."tron_trans_user"."block" IS '区块';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_id" IS '交易ID';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_type" IS '转账类型（1trx 2usdt）';
COMMENT ON COLUMN "public"."tron_trans_user"."transaction_timestamp" IS '转账时间';
COMMENT ON COLUMN "public"."tron_trans_user"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."tron_trans_user"."station_id" IS '站点id';
COMMENT ON TABLE "public"."tron_trans_user" IS '站点转入第三方限额表';

DROP TABLE IF EXISTS "public"."tron_trans_user_task";
DROP SEQUENCE IF EXISTS "public"."tron_trans_user_task_seq";
CREATE SEQUENCE "public"."tron_trans_user_task_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."tron_trans_user_task" (
  "id" int4 NOT NULL DEFAULT nextval('tron_trans_user_task_seq'::regclass),
  "trans_from" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "trans_to" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "trans_value" numeric(16,6) NOT NULL,
  "block" int8 NOT NULL,
  "transaction_id" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "transaction_type" int4 NOT NULL,
  "transaction_timestamp" int8 NOT NULL,
  "create_datetime" timestamp(6),
  "station_id" int4
)
;
ALTER TABLE "public"."tron_trans_user_task" OWNER TO "postgres";
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_from" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_to" IS '交易地址';
COMMENT ON COLUMN "public"."tron_trans_user_task"."trans_value" IS '交易金额';
COMMENT ON COLUMN "public"."tron_trans_user_task"."block" IS '区块';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_id" IS '交易ID';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_type" IS '转账类型（1trx 2usdt）';
COMMENT ON COLUMN "public"."tron_trans_user_task"."transaction_timestamp" IS '转账时间';
COMMENT ON COLUMN "public"."tron_trans_user_task"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."tron_trans_user_task"."station_id" IS '站点id';
COMMENT ON TABLE "public"."tron_trans_user_task" IS 'tron链事件表';

CREATE OR REPLACE FUNCTION "public"."create_tron_record_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
  RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
			INHERITS (tron_trans_user);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE INDEX '||tablename||'_tron_usdt_trans_uti_idx ON ' ||tablename||' USING btree(transaction_id);' ;
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_tron_usdt_create_datetime ON '||tablename||' USING btree(create_datetime);';
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_tron_usdt_trans_uf_idx ON '||tablename||' USING btree(trans_from);';
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_tron_usdt_trans_ut_idx ON '||tablename||' USING btree(trans_to);';
EXECUTE strSQL;
strSQL := 'CREATE INDEX  '||tablename||'_tron_usdt_station_id ON '||tablename||' USING btree(station_id);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100;

CREATE OR REPLACE FUNCTION "public"."insert_tron_record_partition"()
RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO tron_trans_user_'||to_char(NEW.create_datetime,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER "insert_tron_record_partition_trigger" BEFORE INSERT ON "public"."tron_trans_user"
    FOR EACH ROW
    EXECUTE PROCEDURE "public"."insert_tron_record_partition"();


CREATE SEQUENCE "public"."station_draw_rule_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."station_draw_rule_strategy";
CREATE TABLE "public"."station_draw_rule_strategy" (
   "id" int4 NOT NULL DEFAULT nextval('station_draw_rule_strategy_id_seq'::regclass) PRIMARY KEY,
   "station_id" int4,
   "type" int2,
   "value" numeric(20,6),
   "rate" numeric(20,6),
   "remark" varchar(500) COLLATE "pg_catalog"."default",
   "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
   "group_ids" varchar(700) COLLATE "pg_catalog"."default",
   "status" int2,
   "days" int2,
   "limit_country" varchar(30) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "public"."station_draw_rule_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."type" IS '规则类型';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."value" IS '设定值';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."rate" IS '比率 0-1';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."status" IS '启用状态';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."days" IS '多少天内';
COMMENT ON COLUMN "public"."station_draw_rule_strategy"."limit_country" IS '限制国家代码';
COMMENT ON TABLE "public"."station_draw_rule_strategy" IS '站点每日最高在线统计';


DROP TABLE IF EXISTS "public"."station_homepage_game";
DROP SEQUENCE IF EXISTS "public"."station_homepage_game_id_seq";
CREATE SEQUENCE "public"."station_homepage_game_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_homepage_game" (
    "id" int8 NOT NULL DEFAULT nextval('station_homepage_game_id_seq'::regclass)PRIMARY KEY,
    "partner_id" int8,
    "station_id" int8,
    "game_tab_id" int8,
    "game_type" int2,
    "game_code" varchar(128),
    "game_name" varchar(128),
    "parent_game_code" varchar(128),
    "third_game_url" varchar(256),
    "image_url" varchar(256),
    "status" int2,
    "create_datetime" timestamp(6),
    "sort_no" int2
)WITH (OIDS=FALSE);

ALTER TABLE "public"."station_homepage_game" OWNER TO "postgres";

COMMENT ON COLUMN "public"."station_homepage_game"."id" IS '主键ID';
COMMENT ON COLUMN "public"."station_homepage_game"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."station_homepage_game"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_homepage_game"."game_tab_id" IS '游戏标签ID（关联app_tab表）';
COMMENT ON COLUMN "public"."station_homepage_game"."game_type" IS '游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)';
COMMENT ON COLUMN "public"."station_homepage_game"."game_code" IS '游戏代码';
COMMENT ON COLUMN "public"."station_homepage_game"."game_name" IS '游戏名称';
COMMENT ON COLUMN "public"."station_homepage_game"."parent_game_code" IS '上级游戏代码';
COMMENT ON COLUMN "public"."station_homepage_game"."third_game_url" IS '第三方游戏链接';
COMMENT ON COLUMN "public"."station_homepage_game"."image_url" IS '游戏图片链接';
COMMENT ON COLUMN "public"."station_homepage_game"."status" IS '状态(1启用 2禁用)';
COMMENT ON COLUMN "public"."station_homepage_game"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_homepage_game"."sort_no" IS '序号';
COMMENT ON TABLE "public"."station_homepage_game" IS '主页游戏管理';

-- ----------------------------
-- Table structure for station_register_strategy
-- ----------------------------

CREATE SEQUENCE "public"."station_register_strategy_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."station_register_strategy";
CREATE TABLE "public"."station_register_strategy" (
      "id" int4 NOT NULL DEFAULT nextval('station_register_strategy_id_seq'::regclass) PRIMARY KEY,
      "station_id" int4,
      "gift_type" int2,
      "back_gift_type" int2 default 1,
      "value_type" int2,
      "gift_value" numeric(16,2),
      "bet_rate" numeric(10,3),
      "upper_limit" numeric(16,4),
      "create_datetime" timestamp(6),
      "start_datetime" timestamp(6),
      "end_datetime" timestamp(6),
      "remark" varchar(200) COLLATE "pg_catalog"."default",
      "status" int2,
      "degree_ids" varchar(700) COLLATE "pg_catalog"."default",
      "group_ids" varchar(700) COLLATE "pg_catalog"."default",
      "bonus_back_value" numeric(16,2),
      "bonus_back_bet_rate"  numeric(10,3)
)
;
ALTER TABLE "public"."station_register_strategy" OWNER TO "postgres";
COMMENT ON COLUMN "public"."station_register_strategy"."gift_type" IS '赠送方式 1=固定数额 2=浮动比例';
COMMENT ON COLUMN "public"."station_register_strategy"."back_gift_type" IS '返佣金额赠送方式 1=固定数额 2=浮动比例';
COMMENT ON COLUMN "public"."station_register_strategy"."value_type" IS '赠送类型  1=彩金  2=积分';
COMMENT ON COLUMN "public"."station_register_strategy"."gift_value" IS '赠送额度';
COMMENT ON COLUMN "public"."station_register_strategy"."bet_rate" IS '打码量倍数。(赠送)x流水倍数=出款需要达到的投注量';
COMMENT ON COLUMN "public"."station_register_strategy"."upper_limit" IS '活动期间赠送上限';
COMMENT ON COLUMN "public"."station_register_strategy"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_register_strategy"."start_datetime" IS '开始时间';
COMMENT ON COLUMN "public"."station_register_strategy"."end_datetime" IS '截止时间';
COMMENT ON COLUMN "public"."station_register_strategy"."remark" IS '备注';
COMMENT ON COLUMN "public"."station_register_strategy"."status" IS '状态 1=禁用，2=启用';
COMMENT ON COLUMN "public"."station_register_strategy"."degree_ids" IS '有效会员等级id 多个以,分割';
COMMENT ON COLUMN "public"."station_register_strategy"."group_ids" IS '有效会员组别id 多个以,分割';
COMMENT ON COLUMN "public"."station_register_strategy"."bonus_back_value" IS '给上级代理/推荐人返佣的额度';
COMMENT ON COLUMN "public"."station_register_strategy"."bonus_back_bet_rate" IS '上级代理/推荐人得到返佣额度时的出款所需打码量倍数';
COMMENT ON TABLE "public"."station_register_strategy" IS '用户充值赠送策略';


-- ----------------------------
-- Table structure for sys_user_bonus
-- ----------------------------
CREATE SEQUENCE "public"."sys_user_bonus_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."sys_user_bonus";
CREATE TABLE "public"."sys_user_bonus" (
   "id" int4 NOT NULL DEFAULT nextval('sys_user_bonus_id_seq'::regclass) PRIMARY KEY,
   "bonus_type" int4,
   "proxy_id" int4,
   "daily_money_id" int4,
   "recommend_id" int4,
   "station_id" int4,
   "record_id" int4,
   "create_datetime" timestamp(6),
   "partner_id" int4,
   "proxy_name" varchar(255) COLLATE "pg_catalog"."default",
   "username" varchar(255) COLLATE "pg_catalog"."default",
   "parent_ids" varchar(255) COLLATE "pg_catalog"."default",
   "user_type" int2,
   "money" numeric(20,6),
   "user_id" int4
)
;
ALTER TABLE "public"."sys_user_bonus" OWNER TO "postgres";
COMMENT ON COLUMN "public"."sys_user_bonus"."bonus_type" IS '奖金来源方式 如存款赠送，邀请好友存款赠送等';
COMMENT ON COLUMN "public"."sys_user_bonus"."proxy_id" IS '上级代理id';
COMMENT ON COLUMN "public"."sys_user_bonus"."recommend_id" IS '推荐人id,针对会员推荐会员情况';
COMMENT ON COLUMN "public"."sys_user_bonus"."partner_id" IS '合作商id';
COMMENT ON COLUMN "public"."sys_user_bonus"."record_id" IS '对应的存款记录id';
COMMENT ON COLUMN "public"."sys_user_bonus"."daily_money_id" IS '帐变记录id';
COMMENT ON COLUMN "public"."sys_user_bonus"."proxy_name" IS '上级代理帐号';
COMMENT ON COLUMN "public"."sys_user_bonus"."username" IS '用户帐户名';
COMMENT ON COLUMN "public"."sys_user_bonus"."user_type" IS '用户类型';
COMMENT ON COLUMN "public"."sys_user_bonus"."money" IS '奖金';
COMMENT ON COLUMN "public"."sys_user_bonus"."parent_ids" IS '多级代理上级id集，逗号分隔';



CREATE OR REPLACE FUNCTION "public"."create_sys_user_bonus_partition"("tablename" varchar, "starttime" varchar, "endtime" varchar)
RETURNS "pg_catalog"."int4" AS $BODY$
DECLARE
strSQL varchar;
isExist	boolean;
BEGIN
select count(*) INTO isExist from pg_class where relname = (tablename);
IF ( isExist = false ) THEN
strSQL := 'CREATE TABLE IF NOT EXISTS '||tablename||'
		(CHECK(create_datetime>='''|| startTime ||''' AND create_datetime< '''|| endTime ||'''))
			INHERITS (sys_user_bonus);';
EXECUTE strSQL;
strSQL := 'ALTER TABLE '||tablename||' ADD PRIMARY KEY ("id");';
EXECUTE strSQL;
strSQL := 'CREATE UNIQUE INDEX  '||tablename||'_usd ON '||tablename||' USING btree(user_id,create_datetime);';
EXECUTE strSQL;
END IF;
return 1;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;


CREATE OR REPLACE FUNCTION "public"."insert_sys_user_bonus_partition"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
DECLARE
strSQL  varchar;
    tab_cols  record;
    col_val varchar;
BEGIN

-- 插入数据到子分区!
strSQL := 'INSERT INTO sys_user_bonus_'||to_char(NEW.create_datetime,'YYYYMM')||' AS a SELECT $1.*
on CONFLICT(user_id,create_datetime) DO UPDATE SET station_id=EXCLUDED.station_id,';

FOR tab_cols IN SELECT "column_name","data_type" from information_schema.columns
                WHERE table_schema='public' AND table_name='sys_user_bonus'
                    LOOP
 EXECUTE 'SELECT $1.'||tab_cols."column_name" INTO col_val USING NEW;
IF tab_cols."column_name" <> 'id' AND tab_cols."column_name" <> 'partner_id'
    AND tab_cols."column_name" <> 'station_id' AND tab_cols."column_name" <> 'proxy_id'
    AND tab_cols."column_name" <> 'user_id' AND tab_cols."column_name" <> 'user_type'
    AND tab_cols."column_name" <> 'recommend_id'
    AND (tab_cols."data_type" = 'integer' OR tab_cols."data_type" = 'numeric')
    AND col_val::numeric <> 0
    THEN
        strSQL := strSQL ||tab_cols."column_name"||'=COALESCE(a.'||tab_cols."column_name"||',0)+EXCLUDED.'||tab_cols."column_name"||',';
END IF;
END LOOP;
strSQL := SUBSTRING(strSQL,1,length(strSQL)-1);
RAISE INFO 'strSQL % ',strSQL;
EXECUTE strSQL USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
  COST 100;

ALTER FUNCTION "public"."insert_sys_user_bonus_partition"() OWNER TO "postgres";

CREATE TRIGGER "insert_sys_user_bonus_trigger" BEFORE INSERT ON "public"."sys_user_bonus"
FOR EACH ROW
EXECUTE PROCEDURE "public"."insert_sys_user_bonus_partition"();


-- ----------------------------
-- Table structure for system_sms_config
-- ----------------------------
CREATE SEQUENCE "public"."system_sms_config_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
DROP TABLE IF EXISTS "public"."system_sms_config";
CREATE TABLE "public"."system_sms_config" (
      "id" int4 NOT NULL DEFAULT nextval('system_sms_config_id_seq'::regclass) PRIMARY KEY,
      "country_code" varchar(10) COLLATE "pg_catalog"."default",
      "language" varchar(10) COLLATE "pg_catalog"."default",
      "country" varchar(100) COLLATE "pg_catalog"."default",
      "app_key" varchar(50) COLLATE "pg_catalog"."default",
      "app_secret" varchar(100) COLLATE "pg_catalog"."default",
      "app_code" varchar(50) COLLATE "pg_catalog"."default",
      "content" varchar(1000) COLLATE "pg_catalog"."default",
      "password" varchar(50) COLLATE "pg_catalog"."default",
      "account" varchar(50) COLLATE "pg_catalog"."default",
      "gateway_url" varchar(100) COLLATE "pg_catalog"."default",
      "status" int2
)
;
ALTER TABLE "public"."system_sms_config" OWNER TO "postgres";
COMMENT ON COLUMN "public"."system_sms_config"."country_code" IS '国家代号';
COMMENT ON COLUMN "public"."system_sms_config"."language" IS '语种代号';
COMMENT ON COLUMN "public"."system_sms_config"."country" IS '国家名称';
COMMENT ON COLUMN "public"."system_sms_config"."content" IS '短信内容模板';
COMMENT ON COLUMN "public"."system_sms_config"."password" IS '网关后台登录密码';
COMMENT ON COLUMN "public"."system_sms_config"."account" IS '网关后台登录帐号';
COMMENT ON COLUMN "public"."system_sms_config"."gateway_url" IS '网关请求地址';

delete from public.system_sms_config;
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (1, 'MX', '墨西哥', 'es', '9CSFNO', 'OkQEiw', '1000','Your verification code is:{{}}','zvBO5m','9CSFNO','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (2, 'BR', '巴西', 'br', 'SlcWWI', 'LYFTpN', '1000','Your verification code is:{{}}','BlB9ov','SlcWWI','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (3, 'VN', '越南', 'vi', 'FW4LdY', 'MQ1LQ4', '1000','Your verification code is:{{}}','W3jOGE','FW4LdY','http://47.242.85.7:9090/sms/batch/v2',2);
INSERT INTO "public"."system_sms_config"("id", "country_code", "country", "language","app_key", "app_secret", "app_code", "content", "password","account","gateway_url","status")
VALUES (4, 'IN', '印度', 'in', 'pQLCNm', 'O3bMlP', '1000','Your verification code is:{{}}','tnXab8','pQLCNm','http://47.242.85.7:9090/sms/batch/v2',2);


DROP TABLE IF EXISTS "public"."sys_user_third_auth";
DROP SEQUENCE IF EXISTS "public"."sys_user_third_auth_id_seq";
CREATE SEQUENCE "public"."sys_user_third_auth_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_third_auth" (
    "id" int8 NOT NULL DEFAULT nextval('sys_user_third_auth_id_seq'::regclass)PRIMARY KEY,
    "station_id" int8,
    "user_id" int8,
    "local_username" varchar(256),
    "source" varchar(64),
    "third_id" varchar(256),
    "username" varchar(256),
    "nickname" varchar(256),
    "gender" varchar(64),
    "avatar" text,
    "blog" text,
    "company" varchar(512),
    "location" varchar(128),
    "email" varchar(256),
    "remark" text,
    "create_datetime" timestamp(6)
)WITH (OIDS=FALSE);

ALTER TABLE "public"."sys_user_third_auth" OWNER TO "postgres";

COMMENT ON COLUMN "public"."sys_user_third_auth"."id" IS '主键ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."sys_user_third_auth"."local_username" IS '本地用户名';
COMMENT ON COLUMN "public"."sys_user_third_auth"."source" IS '授权平台来源（Google、Facebook...）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."third_id" IS '用户第三方系统的唯一ID（third_id + source 可以唯一确定一个用户）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."username" IS '第三方用户名';
COMMENT ON COLUMN "public"."sys_user_third_auth"."nickname" IS '用户昵称';
COMMENT ON COLUMN "public"."sys_user_third_auth"."gender" IS '性别';
COMMENT ON COLUMN "public"."sys_user_third_auth"."avatar" IS '用户头像';
COMMENT ON COLUMN "public"."sys_user_third_auth"."blog" IS '用户网址';
COMMENT ON COLUMN "public"."sys_user_third_auth"."company" IS '所在公司';
COMMENT ON COLUMN "public"."sys_user_third_auth"."location" IS '位置';
COMMENT ON COLUMN "public"."sys_user_third_auth"."email" IS '用户邮箱';
COMMENT ON COLUMN "public"."sys_user_third_auth"."remark" IS '用户备注（各平台中的用户个人介绍）';
COMMENT ON COLUMN "public"."sys_user_third_auth"."create_datetime" IS '创建时间';
COMMENT ON TABLE "public"."sys_user_third_auth" IS '用户第三方授权信息';

DROP TABLE IF EXISTS "public"."sys_user_footprint_games";
DROP SEQUENCE IF EXISTS "public"."sys_user_footprint_games_id_seq";
CREATE SEQUENCE "public"."sys_user_footprint_games_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_footprint_games" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_footprint_games_id_seq'::regclass),
  "station_id" int4 NOT NULL,
  "user_id" int4 NOT NULL,
  "game_type" int2,
  "platform" varchar(50) COLLATE "pg_catalog"."default",
  "game_code" varchar(128) COLLATE "pg_catalog"."default",
  "is_sub_game" int2,
  "login_times" int2,
  CONSTRAINT "sys_user_copy1_pkey" PRIMARY KEY ("id")
);

ALTER TABLE "public"."sys_user_footprint_games" 
  OWNER TO "postgres";

CREATE UNIQUE INDEX "sys_user_footprint_games_station_id_user_id_game_type_platf_idx" ON "public"."sys_user_footprint_games" USING btree (
  "station_id" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "game_type" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "platform" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "game_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "public"."sys_user_footprint_games"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."game_type" IS '游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."platform" IS '平台（ag bbin等）';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."game_code" IS '游戏代码';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."is_sub_game" IS '是否某类大游戏下的子游戏 1--是 0--否';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."login_times" IS '进入游戏次数';
COMMENT ON TABLE "public"."sys_user_footprint_games" IS '会员账号信息';

ALTER TABLE IF EXISTS public.mny_draw_record
    ADD COLUMN pay_platform_no character varying(100);

ALTER TABLE public.mny_draw_record
ALTER COLUMN record_os TYPE character varying(500) COLLATE pg_catalog."default";




CREATE TABLE "public"."draw_click_farming" (
  "order_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
  "brush_type" int2 NOT NULL,
  "brush_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "standard" numeric(20,6),
  "start_time" timestamp(6),
  "end_time" timestamp(6),
  "total_deposit" numeric(20,6),
  "total_draw" numeric(20,6),
  "diff" numeric(20,6),
  "single_draw" numeric(20,6),
  "draw_ip" varchar(100) COLLATE "pg_catalog"."default",
  "draw_os" varchar(200) COLLATE "pg_catalog"."default",
  "relation_count" int4,
  "deposit_times" int4,
  "total_money" numeric(20,6),
  "invite_user_count" int4,
  "deposit_count" int4,
  "deposit_rate" numeric(20,6),
  CONSTRAINT "draw_click_farming_pkey" PRIMARY KEY ("order_id")
)
;

ALTER TABLE "public"."draw_click_farming"
  OWNER TO "postgres";

COMMENT ON COLUMN "public"."draw_click_farming"."order_id" IS '提款记录订单号';

COMMENT ON COLUMN "public"."draw_click_farming"."brush_type" IS '刷单类型';

COMMENT ON COLUMN "public"."draw_click_farming"."brush_name" IS '刷单类型名称';

COMMENT ON COLUMN "public"."draw_click_farming"."standard" IS '规则设定值';

COMMENT ON COLUMN "public"."draw_click_farming"."start_time" IS '开始统计时间';

COMMENT ON COLUMN "public"."draw_click_farming"."end_time" IS '结束统计时间';

COMMENT ON COLUMN "public"."draw_click_farming"."total_deposit" IS '时间范围内总充值';

COMMENT ON COLUMN "public"."draw_click_farming"."total_draw" IS '时间范围内总提款';

COMMENT ON COLUMN "public"."draw_click_farming"."diff" IS '时间范围内总差值';

COMMENT ON COLUMN "public"."draw_click_farming"."single_draw" IS '单次提款';

COMMENT ON COLUMN "public"."draw_click_farming"."draw_ip" IS '关联IP';

COMMENT ON COLUMN "public"."draw_click_farming"."draw_os" IS '关联操作系统';

COMMENT ON COLUMN "public"."draw_click_farming"."relation_count" IS '关联数量';

COMMENT ON COLUMN "public"."draw_click_farming"."deposit_times" IS '充值次数';

COMMENT ON COLUMN "public"."draw_click_farming"."total_money" IS '三方盈利总额;代理返点、邀请注册返佣、邀请充值返佣总额';

COMMENT ON COLUMN "public"."draw_click_farming"."invite_user_count" IS '邀请会员的数量';

COMMENT ON COLUMN "public"."draw_click_farming"."deposit_count" IS '邀请会员的充值人数';

COMMENT ON COLUMN "public"."draw_click_farming"."deposit_rate" IS '邀请会员的充值人数/邀请会员的数量';



-- VDD 第三方游戏开关(每个站点都需要添加)
INSERT INTO "public".third_platform  (partner_id,station_id,status,platform)  SELECT partner_id,"id",1,182 FROM "public".station;