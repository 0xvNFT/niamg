package com.play.tronscan.business.reload;

import java.util.Date;
import java.util.Objects;

import com.play.core.BankInfo;
import com.play.core.UserPermEnum;
import com.play.dao.MnyDepositRecordDao;
import com.play.model.*;
import com.play.model.dto.TronUSDTDto;
import com.play.service.*;
import com.play.web.exception.BaseException;
import com.play.web.exception.user.UnauthorizedException;
import com.play.web.var.GuestTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.SysUserDao;
import com.play.web.i18n.BaseI18nCode;


@Component
public class TronUserBalanceReload {
    /**
     * log
     */
    static Logger log = LoggerFactory.getLogger(TronUserBalanceReload.class);
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private TronUSDTExchange tronUSDTExchange;
    @Autowired
    private TronTransUserTaskService userTaskService;
    @Autowired
    private SysUserTronLinkService userTronLinkService;
    @Autowired
    private SysUserPermService userPermService;
    @Autowired
    private MnyDepositRecordService mnyDepositRecordService;
    @Autowired
    private StationDepositBankService stationDepositBankService;
    @Autowired
    private MnyDepositRecordDao mnyDepositRecordDao;

    @Transactional(rollbackFor = Exception.class)
    public void doReload(TronTransUserTask task) {
        SysUserTronLink userLink = userTronLinkService.findOne(task.getTransFrom());
        if (userLink == null) {
        //    log.error("can not find this user, tronAddress={}", task.getTransFrom());
            return;
        }
        SysUser user = sysUserDao.findOneById(userLink.getUserId(), userLink.getStationId());
        if (user == null) {
        //    log.error("can not find this user, userID={}, stationID={}", userLink.getUserId(), userLink.getStationId());
            throw new RuntimeException("can not find this user..");
        }

        SysUserPerm perm = userPermService.findOne(user.getId(), user.getStationId(), UserPermEnum.deposit);
        if (perm == null || !Objects.equals(Constants.STATUS_ENABLE, perm.getStatus())) {
        //    log.error("此用户:{},没有充值权限", user.getUsername());
            throw new UnauthorizedException();
        }
        StationDepositBank depositBank = stationDepositBankService.getUsdtBank(userLink.getStationId(), BankInfo.USDT.getBankName(), task.getTransTo());
        if (depositBank == null) {
         //   log.error("can not find this station_deposit_bank, tronAddress={}", task.getTransTo());
            return;
        }
        TronUSDTDto dto = tronUSDTExchange.doExchangeReload(task, user, depositBank);
        MnyDepositRecord record = tronDepositSave(task, user, dto, depositBank);
        if (record == null) {
            throw new BaseException(BaseI18nCode.saveDepositOrderException);
        }
        // 后台试玩账号不记录日志
        if (!GuestTool.isGuest(user)) {
            LogUtils.addLog("保存支付订单成功，订单号：" + record.getOrderId() + ",订单金额：" + record.getMoney());
        }
        mnyDepositRecordService.confirmHandle(record.getId(),user.getStationId(), record.getRemark(), record.getBgRemark(), MnyDepositRecord.STATUS_SUCCESS, dto.getExchangeMoney(), Boolean.FALSE);

        // 删除掉任务
        userTaskService.deleteByID(task.getId());
    }

    public MnyDepositRecord tronDepositSave(TronTransUserTask task, SysUser account, TronUSDTDto dto, StationDepositBank depositBank) {
        MnyDepositRecord record = new MnyDepositRecord();
        record.setOrderId(task.getTransactionID());
        record.setUserId(account.getId());
        record.setUserType(account.getType());
        record.setMoney(dto.getExchangeMoney());
        record.setCreateDatetime(new Date(task.getTransactionTimestamp())); // Unix时间需除以1000
        record.setCreateUserId(account.getId());
        record.setStationId(account.getStationId());
        record.setUsername(account.getUsername());
        record.setDepositor(account.getUsername());
		record.setPayPlatformCode(depositBank.getPayPlatformCode());
        // 试玩账号直接充值成功
        if (GuestTool.isGuest(account)) {
            record.setStatus(MnyDepositRecord.STATUS_SUCCESS);
        } else {
            record.setStatus(MnyDepositRecord.STATUS_UNDO);
        }
        record.setLockFlag(MnyDepositRecord.LOCK_FLAG_UNLOCK);
        record.setDepositType(MnyDepositRecord.TYPE_BANK);
        record.setPayId(depositBank.getId());
        record.setPayName(depositBank.getBankName());

        record.setParentIds(account.getParentIds());
        record.setRecommendId(account.getRecommendId());
        record.setBankWay(MnyDepositRecord.TYPE_ONLINE);
        record.setAgentName(account.getAgentName());
        record.setHandlerType(MnyDepositRecord.HANDLE_TYPE_SYS);
        // 原本是存会员备注的 现在存后台操作描述
        // record.setBgRemark(account.getRemark());

        record.setDepositVirtualCoinNum(dto.getNum());
        record.setDepositVirtualCoinRate(dto.getRate());
        return mnyDepositRecordDao.save(record);
    }
}
