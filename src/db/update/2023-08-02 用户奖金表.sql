
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
