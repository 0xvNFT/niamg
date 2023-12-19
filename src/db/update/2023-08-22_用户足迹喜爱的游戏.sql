DROP TABLE IF EXISTS "public"."sys_user_footprint_games";
DROP SEQUENCE IF EXISTS "public"."sys_user_footprint_games_id_seq";
CREATE SEQUENCE "public"."sys_user_footprint_games_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."sys_user_footprint_games" (
  "id" int4 NOT NULL DEFAULT nextval('sys_user_footprint_games_id_seq'::regclass),
  "station_id" int4 NOT NULL,
  "user_id" int4 NOT NULL,
  "game_type" int2,
  "platform" varchar(50) COLLATE "pg_catalog"."default",
  "game_code" varchar(128) COLLATE "pg_catalog"."default",
  "is_sub_game" int2,
  "login_times" int2,
  CONSTRAINT "sys_user_copy1_pkey" PRIMARY KEY ("id")
);

ALTER TABLE "public"."sys_user_footprint_games" 
  OWNER TO "postgres";

CREATE UNIQUE INDEX "sys_user_footprint_games_station_id_user_id_game_type_platf_idx" ON "public"."sys_user_footprint_games" USING btree (
  "station_id" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "user_id" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "game_type" "pg_catalog"."int2_ops" ASC NULLS LAST,
  "platform" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "game_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "public"."sys_user_footprint_games"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."user_id" IS '用户id';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."game_type" IS '游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."platform" IS '平台（ag bbin等）';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."game_code" IS '游戏代码';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."is_sub_game" IS '是否某类大游戏下的子游戏 1--是 0--否';
COMMENT ON COLUMN "public"."sys_user_footprint_games"."login_times" IS '进入游戏次数';
COMMENT ON TABLE "public"."sys_user_footprint_games" IS '会员账号信息';