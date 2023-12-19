
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
  "remark" text COLLATE "pg_catalog"."default"
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