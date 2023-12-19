drop table station_highest_online_num;
drop table station_online_num;
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
CREATE UNIQUE INDEX "statin_highest_online_num_unique" ON "public"."station_online_num"("station_id","stat_minute");
CREATE INDEX "station_online_num_sidrd" ON "public"."station_online_num" USING btree ("station_id", "report_datetime");


ALTER TABLE "public"."agent" 
  ADD COLUMN "promo_code" varchar(50);

COMMENT ON COLUMN "public"."agent"."promo_code" IS '推广码';

CREATE UNIQUE INDEX "agent_sid_name" ON "public"."agent" USING btree (
  "station_id" ,
  "name" 
);

CREATE UNIQUE INDEX "agent_sid_pc" ON "public"."agent" USING btree (
  "station_id"  ,
  "promo_code"  
);
ALTER TABLE "public"."station_draw_fee_strategy" 
  ADD COLUMN "group_ids" varchar(700);

COMMENT ON COLUMN "public"."station_draw_fee_strategy"."group_ids" IS '会员组别id';