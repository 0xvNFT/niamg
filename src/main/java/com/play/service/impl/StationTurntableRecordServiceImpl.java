package com.play.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.play.spring.config.i18n.I18nTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.LogUtils;
import com.play.core.BetNumTypeEnum;
import com.play.core.MoneyRecordType;
import com.play.core.ScoreRecordTypeEnum;
import com.play.dao.StationTurntableRecordDao;
import com.play.model.StationTurntableAward;
import com.play.model.StationTurntableRecord;
import com.play.model.SysUser;
import com.play.model.bo.MnyMoneyBo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationTurntableRecordService;
import com.play.service.SysUserBetNumService;
import com.play.service.SysUserMoneyService;
import com.play.service.SysUserScoreService;
import com.play.service.SysUserService;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 用户大转盘中奖记录
 *
 * @author admin
 */
@Service
public class StationTurntableRecordServiceImpl implements StationTurntableRecordService {

    @Autowired
    private StationTurntableRecordDao stationTurntableRecordDao;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserScoreService userScoreService;
    @Autowired
    private SysUserMoneyService userMoneyService;
    @Autowired
    private SysUserBetNumService userBetNumService;

    @Override
    public Page<StationTurntableRecord> getPage(Long stationId, Long turntableId, Long userId, String username,
                                                Date begin, Date end, Integer awardType, Integer status) {
        return stationTurntableRecordDao.gtPage(stationId, turntableId, userId, username, begin, end, awardType,
                status);
    }

    @Override
    public StationTurntableRecord findOne(Long id, Long stationId) {
        return stationTurntableRecordDao.findOne(id, stationId);
    }

    @Override
    public void handelRecord(Long id, Integer status, String remark, Long stationId) {
        StationTurntableRecord tr = stationTurntableRecordDao.findOne(id, stationId);
        List <String> remarkList = I18nTool.convertCodeToList();
        if (tr == null) {
            throw new BaseException(BaseI18nCode.illegalRequest);
        }
        if (Objects.equals(StationTurntableRecord.STATUS_SUCCESS, status)) {
            SysUser user = userService.findOneById(tr.getUserId(), stationId);
            if (user == null) {
                throw new BaseException(BaseI18nCode.memberUnExist);
            }
            if (Objects.equals(tr.getAwardType(), StationTurntableAward.AWARD_TYPE_SCORE)) {// 积分
                if (tr.getAwardValue() != null && tr.getAwardValue().compareTo(BigDecimal.ZERO) > 0) {
                    userScoreService.operateScore(ScoreRecordTypeEnum.ACTIVE_ADD, user, tr.getAwardValue().longValue(),
                            BaseI18nCode.stationScoreJacket.getMessage());
                }
            } else if (Objects.equals(tr.getAwardType(), StationTurntableAward.AWARD_TYPE_MONEY)) {// 现金
                if (tr.getAwardValue() != null && tr.getAwardValue().compareTo(BigDecimal.ZERO) > 0) {
                    MnyMoneyBo mvo = new MnyMoneyBo();
                    mvo.setUser(user);
                    mvo.setMoney(tr.getAwardValue());
                    mvo.setMoneyRecordType(MoneyRecordType.ACTIVE_AWARD);
                    remarkList.add(BaseI18nCode.stationBigRoundJa.getCode());
                    mvo.setRemark(I18nTool.convertCodeToArrStr(remarkList));
                    mvo.setBgRemark(remark);
                    mvo.setOrderId(tr.getId().toString());
                    mvo.setBizDatetime(tr.getCreateDatetime());
                    userMoneyService.updMnyAndRecord(mvo);
                    // 保存打码量
                    if (tr.getBetRate() != null && tr.getBetRate().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal betnum = tr.getAwardValue().multiply(tr.getBetRate());
                        remarkList.add(BaseI18nCode.stationCodeBy.getCode());
                        remarkList.add(String.valueOf(betnum));
                        userBetNumService.updateDrawNeed(tr.getUserId(), tr.getStationId(), betnum,
                                BetNumTypeEnum.active.getType(), I18nTool.convertCodeToArrStr(remarkList), null);
                    }
                }
            }
            //原来的先注释掉,先统一换成英文,以后肯定要国际化的
            /*stationTurntableRecordDao.updateStatus(id, status, BaseI18nCode.stationCaluteGet.getMessage()
                    + tr.getGiftName() + I18nTool.getMessage(BaseI18nCode.stationRemark, Locale.ENGLISH) + remark);*/
            stationTurntableRecordDao.updateStatus(id, status,remark);
            LogUtils.modifyStatusLog("大转盘记录处理为成功" + tr.getGiftName() + "  success:" + remark);
        } else {
            //原来的先注释掉,先统一换成英文,以后肯定要国际化的
            //stationTurntableRecordDao.updateStatus(id, status, "手动处理成失败:" + remark);
            stationTurntableRecordDao.updateStatus(id, status,"  fail:"+ remark);
            LogUtils.modifyStatusLog("大转盘记录处理为失败:" + remark);
        }
    }

    @Override
    public List<StationTurntableRecord> getRecoreList(Long stationId, Date begin, Date end, String username,
                                                      Integer awardType, Long turntableId, Integer limit, Integer status, Long userId,String productName) {
        return stationTurntableRecordDao.getRecordList(stationId, begin, end, username, awardType, turntableId, limit, status, userId,productName);
    }
}
