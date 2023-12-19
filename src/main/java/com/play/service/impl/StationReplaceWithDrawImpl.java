package com.play.service.impl;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.core.BankModelEnum;
import com.play.core.PayPlatformEnum;
import com.play.dao.StationReplaceWithDrawDao;
import com.play.model.StationReplaceWithDraw;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationReplaceWithDrawService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class StationReplaceWithDrawImpl implements StationReplaceWithDrawService {

    @Autowired
    private StationReplaceWithDrawDao replaceWithDrawDao;

    @Override
    public Page<StationReplaceWithDraw> getPage(String name, Integer status, Long stationId) {
        Page<StationReplaceWithDraw> page = replaceWithDrawDao.getPage(name, status, stationId);
        page.getRows().stream().forEach(online -> {
            Optional.of(online.getIcon()).ifPresent(x -> {
                online.setPayName(PayPlatformEnum.valueOfPayName(x));
            });
        });
        return page;
    }

    @Override
    public void save(StationReplaceWithDraw online) {
        if (online == null) {
            throw new ParamException(BaseI18nCode.dataError);
        }
        List<BankModelEnum> bank = BankModelEnum.values();
        for (int i = 0; i < bank.size(); i++) {
            if (bank.get(i).getBankCode().equals(online.getIcon())) {
                online.setPayType(bank.get(i).getBankName());
            }
        }

        online.setMerchantKey(clearEmptyStr(online.getMerchantKey()));
        online.setMerchantCode(clearEmptyStr(online.getMerchantCode()));
        online.setUrl(clearEmptyStr(online.getUrl()));
        online.setIcon(clearEmptyStr(online.getIcon()));
        online.setPayGetway(clearEmptyStr(online.getPayGetway()));
        online.setAccount(clearEmptyStr(online.getAccount()));
        online.setAppid(clearEmptyStr(online.getAppid()));
        online.setFixedAmount(clearEmptyStr(online.getFixedAmount()));
        online.setExtra(clearEmptyStr(online.getExtra()));
        Long stationId = SystemUtil.getStationId();
        online.setSearchGetway(clearEmptyStr(online.getSearchGetway()));
        online.setWhiteListIp(clearEmptyStr(online.getWhiteListIp()));
        online.setRemark(clearEmptyStr(online.getRemark()));
        online.setGroupIds(clearEmptyStr(online.getGroupIds()));
        online.setDegreeIds(clearEmptyStr(online.getDegreeIds()));
        if (online.getId() != null && online.getId() > 0) {
            StationReplaceWithDraw updateOnline = replaceWithDrawDao.findOneById(online.getId());
            if (!online.getMerchantCode().contains("*")) {
                updateOnline.setMerchantCode(online.getMerchantCode());
            }
            if (!online.getMerchantKey().contains("*")) {
                //判断是否修改了秘钥，如果修改则更新秘钥更新时间
                if (StringUtils.isNotEmpty(updateOnline.getMerchantKey()) && !online.getMerchantKey().equals(updateOnline.getMerchantKey())) {
                    updateOnline.setUpdateKeyTime(new Date());
                }
                updateOnline.setMerchantKey(online.getMerchantKey());
            }
            if (!online.getAccount().contains("*")) {
                updateOnline.setAccount(online.getAccount());
            }
            List<BankModelEnum> bank2 = BankModelEnum.values();
            for (int i = 0; i < bank2.size(); i++) {
                if (bank2.get(i).getBankCode().equals(online.getIcon())) {
                    updateOnline.setPayType(bank2.get(i).getBankName());
                }
            }
            updateOnline.setUrl(online.getUrl());
            updateOnline.setMin(online.getMin());
            updateOnline.setMax(online.getMax());
            updateOnline.setStatus(online.getStatus());
            updateOnline.setDef(online.getDef());
            updateOnline.setIcon(online.getIcon());
            updateOnline.setPayGetway(online.getPayGetway());
            updateOnline.setAppid(online.getAppid());
            updateOnline.setSortNo(online.getSortNo());
            updateOnline.setShowType(online.getShowType());
            updateOnline.setFixedAmount(online.getFixedAmount());
            updateOnline.setExtra(online.getExtra());
            updateOnline.setSearchGetway(online.getSearchGetway());
            updateOnline.setWhiteListIp(online.getWhiteListIp());
            updateOnline.setVersion(online.getVersion());
            updateOnline.setRemark(online.getRemark());
            updateOnline.setDegreeIds(online.getDegreeIds());
            updateOnline.setGroupIds(online.getGroupIds());
            LogUtils.modifyLog("修改" + updateOnline.getPayType() + "在线代付出款：" + updateOnline.getMerchantCode());
            replaceWithDrawDao.update(updateOnline);
        } else {
            online.setStationId(SystemUtil.getStationId());
            online.setUpdateKeyTime(new Date());
            LogUtils.addLog("新增" + online.getPayType() + "在线代付出款：" + online.getMerchantCode());
            replaceWithDrawDao.save(online);
        }
    }

    private String clearEmptyStr(String str) {
        if (str == null) {
            return "";
        }
        str = str.trim();
        str = str.replaceAll(" ", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll("\r", "");
        return str;
    }

    @Override
    public void delete(long id) {
        StationReplaceWithDraw updateOnline = replaceWithDrawDao.findOneById(id);
        replaceWithDrawDao.deleteById(id);
        LogUtils.delLog("删除" + updateOnline.getPayType() + "代付出款设置：" + updateOnline.getMerchantCode());
    }

    @Override
    public void updateStatus(Integer status, Long id, Long stationId) {
        if (id == null || id <= 0 || status == null || stationId == null) {
            throw new ParamException(BaseI18nCode.parameterError);
        }
        StationReplaceWithDraw updateOnline = replaceWithDrawDao.findOneById(id);
        LogUtils.modifyStatusLog("修改代付出款设置：" + updateOnline.getMerchantCode() + "  修改代付出款设置状态：" + ((status.equals(1)) ? "禁用" : "启用") + "    站点：" + stationId);
        replaceWithDrawDao.updateStatus(status, id, stationId);
    }

    /**
     * 隐藏部分数据
     *
     * @param text
     * @return
     */
    public static String removePart(String text) {
        if (text == null) {
            return "";
        }
        int len = text.length();
        if (len > 8) {
            return new StringBuilder(text.substring(0, 3)).append("***").append(text.substring(len - 4)).toString();
        }
        if (len > 4) {
            return new StringBuilder("***").append(text.substring(len - 4)).toString();
        }
        if (len > 2) {
            return new StringBuilder("***").append(text.substring(len - 2)).toString();
        }
        return text;
    }

    @Override
    public StationReplaceWithDraw findOneNoHideById(Long payId, Long stationId) {
        return replaceWithDrawDao.findOne(payId, stationId);
    }

    @Override
    public StationReplaceWithDraw findOneById(Long payId, Long stationId) {
        StationReplaceWithDraw online = replaceWithDrawDao.findOne(payId, stationId);
        if (online != null) {
            online.setPayName(PayPlatformEnum.valueOfPayName(online.getIcon()));
            online.setMerchantCode(removePart(online.getMerchantCode()));
            online.setMerchantKey(removePart(online.getMerchantKey()));
            online.setAccount(removePart(online.getAccount()));
        }
        return online;
    }

    @Override
    public StationReplaceWithDraw findOneAllInfo(Long payId, Long stationId) {
        return replaceWithDrawDao.findOne(payId, stationId);
    }

    @Override
    public List<StationReplaceWithDraw> findListByDegreeIdAndGroupId(Long stationId, Long degreeId, Long groupId) {
        return replaceWithDrawDao.findListByDegreeIdAndGroupId(stationId, Constants.STATUS_ENABLE, degreeId, groupId);
    }

    @Override
    public List<StationReplaceWithDraw> findByStationId(Long stationId) {
        return replaceWithDrawDao.findByStationId(stationId);
    }

    //回调专用
    @Override
    public StationReplaceWithDraw getReplaceWithDrawByPayId(Long payId, Long stationId) {
        return replaceWithDrawDao.getReplaceWithDrawByPayId(payId, stationId);
    }

    @Override
    public int countAll() {
        return replaceWithDrawDao.countAll();
    }

    @Override
    public void remarkModify(Long id, String remark) {
        Long stationId = SystemUtil.getStationId();
        StationReplaceWithDraw ado = replaceWithDrawDao.findOne(id, SystemUtil.getStationId());
        if (ado == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        replaceWithDrawDao.changeRemark(id, stationId, remark);
        LogUtils.modifyLog("代付:" + ado.getPayType() + " 的备注从 " + ado.getRemark() + " 修改成 " + remark);
    }

    @Override
    public void sortNoModify(Long id, Integer sortNo) {
        Long stationId = SystemUtil.getStationId();
        StationReplaceWithDraw ado = replaceWithDrawDao.findOne(id, SystemUtil.getStationId());
        if (ado == null) {
            throw new ParamException(BaseI18nCode.orderNotExist);
        }
        replaceWithDrawDao.changeSortNo(id, stationId, sortNo);
        LogUtils.modifyLog("用户:" + ado.getAccount() + " 的排序从 " + ado.getSortNo() + " 修改成 " + sortNo);
    }
}
