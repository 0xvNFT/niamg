package com.play.common.utils;

import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.core.MoneyRecordType;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.web.utils.StationConfigUtil;

public class MoneyRecordTypeUtil {

    /**
     * 获取类型
     */
    public static Map<String, Object> getRecordType(Map<String, Object> map, Long stationId, Integer userType) {
        JSONArray incomeType = new JSONArray();
        JSONArray payType = new JSONArray();
        for (MoneyRecordType t : MoneyRecordType.values()) {
            switch (t) {
                case DEPOSIT_ARTIFICIAL:// 人工加款
                case WITHDRAW_ARTIFICIAL:// 人工扣款
                case WITHDRAW_ONLINE_FAILED:// 在线取款失败
                case WITHDRAW_ONLINE:// 在线取款
                case DEPOSIT_ONLINE_THIRD:// 在线支付
                case DEPOSIT_ONLINE_FAST:// 快速入款
                case DEPOSIT_ONLINE_BANK:// 一般入款
                case DEPOSIT_RECHARGE_CARD:// 充值卡入款
                case DEPOSIT_COUPONS:// 代金券
                case MEMBER_ROLL_BACK_ADD:// 反水加钱
                case MEMBER_ROLL_BACK_SUB:// 反水回滚
                    break;
                case PROXY_REBATE_ADD:// 代理反点加钱
                case PROXY_REBATE_SUB:// 代理反点回滚
                    if (!Objects.equals(userType, UserTypeEnum.PROXY.getType())) {
                        continue;
                    }
                    break;
                case REAL_GAME_ADD:// 三方额度转入系统额度
                case REAL_GAME_SUB:// 系统额度转入三方额度
                case REAL_GAME_FAILED:// 三方转账失败
                    break;
                case DEPOSIT_GIFT_ACTIVITY:// 存款赠送
                    break;
                case ACTIVE_AWARD:// 转盘中奖
                    if (!StationConfigUtil.isOn(stationId, StationConfigEnum.switch_turnlate)) {
                        continue;
                    }
                    break;
                case RED_ACTIVE_AWARD:// 红包中奖
                    if (!StationConfigUtil.isOn(stationId, StationConfigEnum.switch_redbag)) {
                        continue;
                    }
                    break;
                case EXCHANGE_MNY_TO_SCORE:// 现金兑换积分
                case EXCHANGE_SCORE_TO_MNY:// 积分兑换现金
                    if (!StationConfigUtil.isOn(stationId, StationConfigEnum.exchange_score)) {
                        continue;
                    }
                    break;
            }
            JSONObject object = new JSONObject();
            object.put("type", t.getType());
            object.put("name", t.getName());
            if (t.isAdd()) {
                incomeType.add(object);
            } else {
                payType.add(object);
            }
        }
        map.put("incomeType", incomeType);
        map.put("payType", payType);
        return map;
    }
}
