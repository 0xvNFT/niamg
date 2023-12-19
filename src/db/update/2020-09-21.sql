ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "live_real_bet_amount" TO "live_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "egame_real_bet_amount" TO "egame_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "sport_real_bet_amount" TO "sport_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "chess_real_bet_amount" TO "chess_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "esport_real_bet_amount" TO "esport_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "fishing_real_bet_amount" TO "fishing_bet_num";
ALTER TABLE "public"."sys_user_daily_money" RENAME COLUMN "lot_real_bet_amount" TO "lot_bet_num";




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
EXECUTE 'INSERT INTO proxy_daily_bet_stat_'||to_char(NEW.stat_date,'YYYYMM')||' SELECT $1.*' USING NEW;
RETURN NULL;
END
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

DROP TABLE IF EXISTS proxy_daily_bet_stat_202009;
DROP TABLE IF EXISTS proxy_daily_bet_stat_202010;
drop table proxy_daily_bet_stat;
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


CREATE TRIGGER "insert_proxy_daily_bet_stat_partition_trigger" BEFORE INSERT ON "public"."proxy_daily_bet_stat"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_proxy_daily_bet_stat_partition"();
  
  
  

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

DROP TABLE IF EXISTS station_kickback_record_202009;
DROP TABLE IF EXISTS station_kickback_record_202010;
drop table station_kickback_record;
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
  "draw_need" numeric(11,2) DEFAULT 0,
  "status" int2,
  "proxy_id" int4,
  "proxy_name" varchar(50) COLLATE "pg_catalog"."default",
  "operator" varchar(50) COLLATE "pg_catalog"."default",
  "operator_id" int4,
  "degree_id" int4,
  "degree_name" varchar(50) COLLATE "pg_catalog"."default"
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
COMMENT ON COLUMN "public"."station_kickback_record"."degree_id" IS '会员层级id';
COMMENT ON COLUMN "public"."station_kickback_record"."degree_name" IS '会员层级名称';
COMMENT ON TABLE "public"."station_kickback_record" IS '会员反水记录表，按日投注和游戏类型来反水';
COMMENT ON COLUMN "public"."station_kickback_record"."proxy_name" IS '代理账号';

CREATE TRIGGER "insert_station_kickback_record_partition_trigger" BEFORE INSERT ON "public"."station_kickback_record"
  FOR EACH ROW
  EXECUTE PROCEDURE "public"."insert_station_kickback_record_partition"();

