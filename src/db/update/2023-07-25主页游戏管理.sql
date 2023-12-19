INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (460, '主页游戏管理', 'admin.menu.homepageGame', '/stationHomepageGame/index.do', 7, 4, '','admin:homepageGame', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (461, '修改', 'admin.menu.modify', '', 460, 3, '', 'admin:homepageGame:modify', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (462, '删除', 'admin.menu.del', '', 460, 1, '', 'admin:homepageGame:delete', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (463, '新增', 'admin.menu.add', '', 460, 2, '', 'admin:homepageGame:add', 4);



DROP TABLE IF EXISTS "public"."station_homepage_game";
DROP SEQUENCE IF EXISTS "public"."station_homepage_game_id_seq";
CREATE SEQUENCE "public"."station_homepage_game_id_seq" INCREMENT 1 START 1 MAXVALUE 9223372036854775807 MINVALUE 1 CACHE 1;
CREATE TABLE "public"."station_homepage_game" (
    "id" int8 NOT NULL DEFAULT nextval('station_homepage_game_id_seq'::regclass)PRIMARY KEY,
    "partner_id" int8,
    "station_id" int8,
    "game_tab_id" int8,
    "game_type" int2,
    "game_code" varchar(128),
    "game_name" varchar(128),
    "parent_game_code" varchar(128),
    "third_game_url" varchar(256),
    "image_url" varchar(256),
    "status" int2,
    "create_datetime" timestamp(6),
    "sort_no" int2
)WITH (OIDS=FALSE);

ALTER TABLE "public"."station_homepage_game" OWNER TO "postgres";

COMMENT ON COLUMN "public"."station_homepage_game"."id" IS '主键ID';
COMMENT ON COLUMN "public"."station_homepage_game"."partner_id" IS '合作商ID';
COMMENT ON COLUMN "public"."station_homepage_game"."station_id" IS '站点ID';
COMMENT ON COLUMN "public"."station_homepage_game"."game_tab_id" IS '游戏标签ID（关联app_tab表）';
COMMENT ON COLUMN "public"."station_homepage_game"."game_type" IS '游戏类型(1彩票 2真人 3电子 4体育 5电竞 6捕鱼 7棋牌 8自定义)';
COMMENT ON COLUMN "public"."station_homepage_game"."game_code" IS '游戏代码';
COMMENT ON COLUMN "public"."station_homepage_game"."game_name" IS '游戏名称';
COMMENT ON COLUMN "public"."station_homepage_game"."parent_game_code" IS '上级游戏代码';
COMMENT ON COLUMN "public"."station_homepage_game"."third_game_url" IS '第三方游戏链接';
COMMENT ON COLUMN "public"."station_homepage_game"."image_url" IS '游戏图片链接';
COMMENT ON COLUMN "public"."station_homepage_game"."status" IS '状态(1启用 2禁用)';
COMMENT ON COLUMN "public"."station_homepage_game"."create_datetime" IS '创建时间';
COMMENT ON COLUMN "public"."station_homepage_game"."sort_no" IS '序号';
COMMENT ON TABLE "public"."station_homepage_game" IS '主页游戏管理';