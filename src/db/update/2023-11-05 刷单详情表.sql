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