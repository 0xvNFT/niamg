package com.play.service.impl;

import com.play.dao.ThirdTransferOutLimitDao;
import com.play.model.ThirdTransferOutLimit;
import com.play.orm.jdbc.page.Page;
import com.play.service.ThirdTransferOutLimitService;

import com.play.web.exception.ParamException;

import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ThirdTransferOutLimitServiceImpl implements ThirdTransferOutLimitService {

    @Autowired
    private ThirdTransferOutLimitDao thirdTransferOutLimitDao;

    @Override
    public Page<ThirdTransferOutLimit> page(Integer platform) {
        return thirdTransferOutLimitDao.getPage(SystemUtil.getStationId(), platform);
    }

    @Override
    public ThirdTransferOutLimit findOneById(Long id) {
        return thirdTransferOutLimitDao.findOneById(id);
    }

    @Override
    public void save(ThirdTransferOutLimit limit) {
        Long stationId = SystemUtil.getStationId();
        if (limit.getMinMoney() == null || limit.getMinMoney().compareTo(BigDecimal.ZERO) < 0) {
            throw new ParamException(BaseI18nCode.inputRightMinNum);
        }
        if (limit.getMaxMoney() == null || limit.getMaxMoney().compareTo(BigDecimal.ZERO) < 0) {
            throw new ParamException(BaseI18nCode.inputRightMaxNum);
        }
        if (limit.getMinMoney().compareTo(limit.getMaxMoney()) >= 0) {
            throw new ParamException(BaseI18nCode.inputMustBigMaxMin);
        }
        if (limit.getId() != null) {
            ThirdTransferOutLimit old = thirdTransferOutLimitDao.findOne(limit.getId(), stationId);
            if (old == null) {
                throw new ParamException(BaseI18nCode.orderNotExist);
            }
            if (!limit.getPlatform().equals(old.getPlatform())) {
                if (thirdTransferOutLimitDao.findByPlatformStationId(stationId, limit.getPlatform()) != null) {
                    throw new ParamException(BaseI18nCode.stationConfigExist);
                }
            }
            old.setMaxMoney(limit.getMaxMoney());
            old.setMinMoney(limit.getMinMoney());
            old.setPlatform(limit.getPlatform());
            if(StringUtils.isNotEmpty(limit.getLimitAccounts())) {
                limit.setLimitAccounts(limit.getLimitAccounts().replaceAll("，", ","));
                if(!limit.getLimitAccounts().endsWith(",")) {
                    limit.setLimitAccounts(limit.getLimitAccounts()+",");
                }
            }
            old.setLimitAccounts(limit.getLimitAccounts());
            thirdTransferOutLimitDao.update(old);
        } else {
            if (thirdTransferOutLimitDao.findByPlatformStationId(stationId, limit.getPlatform()) != null) {
                throw new ParamException(BaseI18nCode.stationConfigExist);
            }
            limit.setStationId(stationId);
            if(StringUtils.isNotEmpty(limit.getLimitAccounts())) {
                limit.setLimitAccounts(limit.getLimitAccounts().replaceAll("，", ","));
                if(!limit.getLimitAccounts().endsWith(",")) {
                    limit.setLimitAccounts(limit.getLimitAccounts()+",");
                }
            }
            thirdTransferOutLimitDao.save(limit);
        }

    }

    @Override
    public void delete(Long id) {
        thirdTransferOutLimitDao.deleteById(id);
    }
}
