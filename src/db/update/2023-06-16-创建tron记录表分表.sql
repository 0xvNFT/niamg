ALTER TABLE "public"."tron_trans_user" ADD COLUMN "create_datetime" timestamp(6);
COMMENT ON COLUMN "public"."tron_trans_user"."create_datetime" IS '创建时间';
ALTER TABLE "public"."tron_trans_user" ADD COLUMN "station_id" int4;
COMMENT ON COLUMN "public"."tron_trans_user"."station_id" IS '站点id';

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

