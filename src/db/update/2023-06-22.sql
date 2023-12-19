-- 用户充值记录
ALTER TABLE mny_deposit_record alter COLUMN order_id type varchar(255);

-- 会员金额变动表
ALTER TABLE sys_user_money_history alter COLUMN order_id type varchar(255);


-- 会员打码量变动记录
ALTER TABLE sys_user_bet_num_history alter COLUMN order_id type varchar(255);

