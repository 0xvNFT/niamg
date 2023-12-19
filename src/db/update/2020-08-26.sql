
ALTER TABLE "public"."station_article" ADD COLUMN "group_ids" VARCHAR(90) DEFAULT NULL;
COMMENT ON COLUMN "public"."station_article"."group_ids" IS '限制使用等级组别';

ALTER TABLE "public"."station_banner" ADD COLUMN "code" int2   DEFAULT NULL;
COMMENT ON COLUMN "public"."station_banner"."code" IS '轮播类型';


ALTER TABLE "public"."station_banner" ADD COLUMN "app_type" int2   DEFAULT NULL;
COMMENT ON COLUMN "public"."station_banner"."app_type" IS  'APP跳转标识(1 2彩票，3真人，4电子，5体育，6棋牌，7红包)';
