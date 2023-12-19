
CREATE OR REPLACE FUNCTION "public"."insert_sys_user_daily_money_partition"()
  RETURNS "pg_catalog"."trigger" AS $BODY$
BEGIN
EXECUTE 'INSERT INTO sys_user_daily_money_'||to_char(NEW.stat_date,'YYYYMM')||'  AS aa VALUES ($1.*) 
	on CONFLICT(user_id,stat_date,"type") 
	DO UPDATE SET money=COALESCE(aa.money,0)+$1.money,times=COALESCE(aa.times,0)+$1.times,
	real_bet_num=COALESCE(aa.real_bet_num,0)+$1.real_bet_num
	RETURNING *' USING NEW;
RETURN NULL;
END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
  
  ALTER TABLE "public"."station_sign_rule" 
  ADD COLUMN "group_ids" varchar(700);

COMMENT ON COLUMN "public"."station_sign_rule"."group_ids" IS '限制会员组别id';