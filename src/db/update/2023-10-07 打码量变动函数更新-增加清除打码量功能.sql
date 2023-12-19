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
