-- 先从    station 表    检索站点ID，假如结果：123456	
select id station where name = 'mtest00000';

-- 根据站点ID（station_id），清理常规表数据
-- 删除三方登录配置
delete from admin_login_google_auth where station_id = '123456';
-- 删除站点管理员
delete from admin_user where station_id = '123456';
-- 删除站点管理员分组
delete from admin_user_group where station_id = '123456';
-- 删除站点管理员分组权限
delete from admin_user_group_auth where station_id = '123456';
-- 删除站点白名单
delete from admin_white_ip where station_id = '123456';
-- 删除站点agent
delete from agent where station_id = '123456';
-- 删除站点agent用户
delete from agent_user where station_id = '123456';
-- 删除站点agen白名单
delete from agent_white_ip where station_id = '123456';
-- 删除站点公告
delete from announcement_station where station_id = '123456';
-- 删除站点反馈信息
delete from app_feedback where station_id = '123456';
-- 删除站点的APP游戏足迹
delete from app_game_foot where station_id = '123456';
-- 删除站点的APP热门游戏
delete from app_hot_game where station_id = '123456';
-- 删除站点的首页菜单
delete from app_index_menu where station_id = '123456';
-- 删除站点的APP检索历史
delete from app_search_history where station_id = '123456';
-- 删除站点的APP主页游戏分类
delete from app_tab where station_id = '123456';
-- 删除站点的代金券配置
delete from coupons where station_id = '123456';
-- 删除站点的玩家代金券
delete from coupons_user where station_id = '123456';
-- 删除站点的充值记录
delete from mny_deposit_record where station_id = '123456';
-- 删除站点的提款记录
delete from mny_draw_record where station_id = '123456';
-- 删除站点的充值卡记录
delete from rechargeable_card where station_id = '123456';
-- 删除站点基础数据
delete from station where id = '123456';
-- 删除站点的活动记录
delete from station_activity where station_id = '123456';
-- 删除站点的建议反馈记录
delete from station_advice_feedback where station_id = '123456';
-- 删除站点的公告、帮助、说明等记录
delete from station_article where station_id = '123456';
-- 删除站点的银行记录
delete from station_bank where station_id = '123456';
-- 删除站点的banner记录
delete from station_banner where station_id = '123456';
-- 删除站点的配置记录
delete from station_config where station_id = '123456';
-- 删除站点的存款统计记录
delete from station_daily_deposit where station_id = '123456';
-- 删除站点的注册统计记录
delete from station_daily_register where station_id = '123456';
-- 删除站点的收款银行记录
delete from station_deposit_bank where station_id = '123456';
-- 删除站点的在线收款配置记录
delete from station_deposit_online where station_id = '123456';
-- 删除站点的在线收款策略记录
delete from station_deposit_strategy where station_id = '123456';
-- 删除站点的域名记录
delete from station_domain where station_id = '123456';
-- 删除站点的取款手续费策略
delete from station_draw_fee_strategy where station_id = '123456';
-- 删除站点的取款规则策略
delete from station_draw_rule_strategy where station_id = '123456';
-- 删除站点的假数据
delete from station_dummy_data where station_id = '123456';
-- 删除站点的浮动信息窗口
delete from station_float_frame where station_id = '123456';
-- 删除站点的主页游戏管理
delete from station_homepage_game where station_id = '123456';
-- 删除站点的反水策略
delete from station_kickback_strategy where station_id = '123456';
-- 删除站点的消息记录
delete from station_message where station_id = '123456';
-- 删除站点的消息读取记录
delete from station_message_receive where station_id = '123456';
-- 删除站点的消收益记录
delete from station_money_income_record where station_id = '123456';
-- 删除站点的消收益策略
delete from station_money_income_strategy where station_id = '123456';
-- 删除站点的在线人数
delete from station_online_num where station_id = '123456';
-- 删除站点的推广记录
delete from station_promotion where station_id = '123456';
-- 删除站点的返点记录
delete from station_rebate where station_id = '123456';
-- 删除站点的红包记录
delete from station_red_packet where station_id = '123456';
-- 删除站点的红包等级配置
delete from station_red_packet_degree where station_id = '123456';
-- 删除站点的红包记录
delete from station_red_packet_record where station_id = '123456';
-- 删除站点的注册配置
delete from station_register_config where station_id = '123456';
-- 删除站点的注册策略
delete from station_register_strategy where station_id = '123456';
-- 删除站点的代付配置
delete from station_replace_withdraw where station_id = '123456';
-- 删除站点的分数兑换配置
delete from station_score_exchange where station_id = '123456';
-- 删除站点的签到记录
delete from station_sign_record where station_id = '123456';
-- 删除站点的签到规则
delete from station_sign_rule where station_id = '123456';
-- 删除站点的大转盘配置
delete from station_turntable where station_id = '123456';
-- 删除站点的大转盘礼品
delete from station_turntable_gift where station_id = '123456';
-- 删除站点的大转盘记录
delete from station_turntable_record where station_id = '123456';
-- 删除站点的白名单区域
delete from station_white_area where station_id = '123456';
-- 删除站点的白名单IP
delete from station_white_ip where station_id = '123456';
-- 删除站点的登录日志
delete from sys_login_log where station_id = '123456';
-- 删除站点的玩家信息
delete from sys_user where station_id = '123456';
-- 删除站点的玩家信息
delete from sys_user_avatar where station_id = '123456';
-- 删除站点的玩家信息
delete from sys_user_avatar_record where station_id = '123456';
-- 删除站点的玩家银行卡信息
delete from sys_user_bank where station_id = '123456';
-- 删除站点的玩家数据
delete from sys_user_bet_num where station_id = '123456';
-- 删除站点的玩家奖金记录
delete from sys_user_bonus where station_id = '123456';
-- 删除站点的玩家等级
delete from sys_user_degree where station_id = '123456';
-- 删除站点的玩家等级日志
delete from sys_user_degree_log where station_id = '123456';
-- 删除站点的玩家存款记录
delete from sys_user_deposit where station_id = '123456';
-- 删除站点的玩家的游戏足迹
delete from sys_user_footprint_games where station_id = '123456';
-- 删除站点的玩家分组信息
delete from sys_user_group where station_id = '123456';
-- 删除站点的玩家信息
delete from sys_user_info where station_id = '123456';
-- 删除站点的玩家的登录信息
delete from sys_user_login where station_id = '123456';
-- 删除站点的玩家权限信息
delete from sys_user_perm where station_id = '123456';
-- 删除站点的玩家返点信息
delete from sys_user_rebate where station_id = '123456';
-- 删除站点的玩家签到信息
delete from sys_user_score where station_id = '123456';
-- 删除站点的玩家三方信息
delete from sys_user_third_auth where station_id = '123456';
-- 删除站点的玩家tron链信息
delete from sys_user_tron_link where station_id = '123456';
-- 删除站点的玩家提款信息
delete from sys_user_withdraw where station_id = '123456';
-- 删除站点的自动转账信息
delete from third_auto_exchange where station_id = '123456';
-- 删除站点的三方游戏类型配置
delete from third_game where station_id = '123456';
-- 删除站点的三方游戏配置
delete from third_platform where station_id = '123456';
-- 删除站点的三方玩家配置
delete from third_player_config where station_id = '123456';
-- 删除站点的三方转账记录
delete from third_trans_log where station_id = '123456';
-- 删除站点的三方转账配置
delete from third_transfer_out_limit where station_id = '123456';
-- 删除站点的tron链任务
delete from tron_trans_user_task where station_id = '123456';


-- 根据站点ID（station_id），清理"所有分表"数据
delete from proxy_daily_bet_stat_202305 where station_id = '123456';
delete from proxy_daily_bet_stat_XXXXXX where station_id = '123456';
-- 删除站点的反水记录
delete from station_kickback_record_202305 where station_id = '123456';
delete from station_kickback_record_XXXXXX where station_id = '123456';
-- 删除站点的系统日志
delete from sys_log_202305 where station_id = '123456';
delete from sys_log_XXXXXX where station_id = '123456';
-- 删除站点的投注信息
delete from sys_user_bet_num_history_202305 where station_id = '123456';
delete from sys_user_bet_num_history_XXXXXX where station_id = '123456';
-- 删除站点的玩家账变记录
delete from sys_user_daily_money_202305 where station_id = '123456';
delete from sys_user_daily_money_XXXXXX where station_id = '123456';
-- 删除站点的玩家账变记录
delete from sys_user_money_history_202305 where station_id = '123456';
delete from sys_user_money_history_XXXXXX where station_id = '123456';
-- 删除站点的玩家分数记录
delete from sys_user_score_history_202305 where station_id = '123456';
delete from sys_user_score_history_XXXXXX where station_id = '123456';
-- 删除站点的玩家tron链记录
delete from tron_trans_user_202305 where station_id = '123456';
delete from tron_trans_user_XXXXXX where station_id = '123456';

