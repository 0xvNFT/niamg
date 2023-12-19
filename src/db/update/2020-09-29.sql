
ALTER TABLE "public"."station_deposit_bank" 
  ADD COLUMN "group_ids" varchar(700);

COMMENT ON COLUMN "public"."station_deposit_bank"."group_ids" IS '有效会员组别id 多个以,分割';


ALTER TABLE "public"."station_deposit_strategy" RENAME COLUMN "reamrk" TO "remark";


ALTER TABLE "public"."station_kickback_record" 
  ADD COLUMN "group_id" int4;

COMMENT ON COLUMN "public"."station_kickback_record"."group_id" IS '会员组别id';

INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (12, '系统风险评估', 'admin.menu.riskReport', '', 0, 36, 'fa-exclamation-triangle', '', 1);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (293, '导出', 'admin.menu.export', '', 57, 3, '', 'admin:kickback:export', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (294, '导出', 'admin.menu.export', '', 115, 1, '', 'admin:moneyReport:export', 4);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (295, '代理报表管理', 'admin.menu.proxyReport', '/proxyReport/index.do', 4, 30, '', 'admin:proxyReport', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (296, '登录情况', 'admin.menu.riskLogin', '/riskLogin/index.do', 12, 40, '', 'admin:riskLogin', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (297, '存取款分析', 'admin.menu.riskMoney', '/riskMoney/index.do', 12, 32, '', 'admin:riskMoney', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (298, '真人分析', 'admin.menu.riskLive', '/riskLive/index.do', 12, 28, '', 'admin:riskLive', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (299, '电子分析', 'admin.menu.riskEgame', '/riskEgame/index.do', 12, 24, '', 'admin:riskEgame', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (300, '体育分析', 'admin.menu.riskSport', '/riskSport/index.do', 12, 20, '', 'admin:riskSport', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (301, '棋牌分析', 'admin.menu.riskChess', '/riskChess/index.do', 12, 16, '', 'admin:riskChess', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (302, '电竞分析', 'admin.menu.riskEsport', '/riskEsport/index.do', 12, 12, '', 'admin:riskEsport', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (303, '捕鱼分析', 'admin.menu.riskFishing', '/riskFishing/index.do', 12, 8, '', 'admin:riskFishing', 3);
INSERT INTO "public"."admin_menu"("id", "title", "code", "url", "parent_id", "sort_no", "icon", "perm_name", "type") VALUES (304, '彩票分析', 'admin.menu.riskLottery', '/riskLottery/index.do', 12, 4, '', 'admin:riskLottery', 3);

delete from admin_menu where id=117 or id=122;