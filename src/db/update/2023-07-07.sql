-- 用户充值记录
ALTER TABLE mny_deposit_record alter COLUMN order_id type varchar(255);

-- 会员金额变动表
ALTER TABLE sys_user_money_history alter COLUMN order_id type varchar(255);


-- 会员打码量变动记录
ALTER TABLE sys_user_bet_num_history alter COLUMN order_id type varchar(255);


-- 第三方游戏开关(每个站点都需要添加)
-- 首先查询出有多少个站点 以及 站点的id
INSERT INTO "public".third_platform  (partner_id,station_id,status,platform)  SELECT partner_id,"id",2,120 FROM "public".station;