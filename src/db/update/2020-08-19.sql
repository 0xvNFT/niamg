INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (259, '切换等级计算模式', 'admin.menu.degree:change', '', 51, 2, '', 'admin:userDegree:change', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (260, '会员晋级奖励', 'admin.menu.degree:upgrade', '', 51, 1, '', 'admin:userDegree:upgrade', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (261, '删除', 'admin.menu.del', '', 53, 1, '', 'admin:userAvatar:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (262, '新增', 'admin.menu.add', '', 53, 2, '', 'admin:userAvatar:add', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (263, '修改', 'admin.menu.modify', '', 53, 3, '', 'admin:userAvatar:modify', 4);



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




CREATE OR REPLACE FUNCTION "public"."change_user_group"("curid" int4, "nextid" int4, "stationid" int4)
  RETURNS "pg_catalog"."int2" AS $BODY$
	DECLARE 
	strSQL varchar; 
 BEGIN
	IF ( nextId > 0 ) THEN
         -- 创建主键
        strSQL := 'UPDATE sys_user SET group_id='||nextId||' WHERE station_id ='||stationId||' AND group_id='||curId;
        EXECUTE strSQL;
	END IF;
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

ALTER FUNCTION "public"."change_user_group"("curid" int4, "nextid" int4, "stationid" int4) OWNER TO "postgres";

drop table sys_user_perm;
CREATE SEQUENCE "public"."sys_user_perm_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_perm" (
   "id" int4 NOT NULL DEFAULT nextval('sys_user_perm_id_seq'::regclass) PRIMARY KEY,
	"user_id" int4,
	"partner_id" int4,
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

