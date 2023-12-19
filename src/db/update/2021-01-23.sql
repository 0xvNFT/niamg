CREATE OR REPLACE FUNCTION "public"."f_update"(IN "tablename" text, IN "condition" VARCHAR, IN "feildname" _text, IN "feildvalue" _text, OUT "returnvalue" text)
  RETURNS "pg_catalog"."text" AS $BODY$
declare
    code       	text;
    id        	integer;
    myresult    integer;
    items       text;
		ele_name    VARCHAR;
		recs       	record;
    counts      integer;
    i           integer;
begin
    counts:=array_length(feildname,1);
    code:='update '||quote_ident(tablename)||' set ';
    for i in 1..counts loop
        code:= code||quote_ident(feildname[i])||'='''||feildvalue[i]||''',';
				code:= code||quote_ident(feildname)||'='''||feildvalue;
    end loop;
    code:=substring(code from 1 for (char_length(code)-1)) || ' where 1=1 '||condition;
    execute code;
    GET DIAGNOSTICS myresult:= ROW_COUNT;
    if myresult<>0 then    returnvalue='{"success":"执行更新'||code||'成功！"}';
    else                   returnvalue='{"success":"执行更新'||code||'失败！"}';
    end if;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100



  ======================================================================================================
  使用方式:select f_update('station_register_config','and id=1 and station_id=1 ','{code}','{admin.account.login}');