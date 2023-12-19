package com.play.tronscan.utils;

import com.alibaba.fastjson.JSONObject;
import com.play.common.utils.SpringUtils;
import com.play.core.TronLinkStatusEnum;
import com.play.model.SysUser;
import com.play.model.SysUserTronLink;
import com.play.model.dto.SysUserTronLinkDto;
import com.play.service.SysUserTronLinkService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.LoginMemberUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;

public class TronUtils {


    /**
     * 用户Tron链是否存在
     * @return
     */
    public static JSONObject isUserTronLinkExist() {
        SysUser user = LoginMemberUtil.currentUser();
        SysUserTronLink sysUserTronLink = SpringUtils.getBean(SysUserTronLinkService.class).findOne(user.getId(), user.getStationId());
        JSONObject jo = new JSONObject();
        if(ObjectUtils.isEmpty(sysUserTronLink)) {
            jo.put("success", false);
            jo.put("content", "");
            return jo;
        }
        SysUserTronLinkDto dto = new SysUserTronLinkDto();
        BeanUtils.copyProperties(sysUserTronLink, dto);
        if(dto.getBindStatus() == TronLinkStatusEnum.unBind.getType()) {
            dto.setRemark(I18nTool.getMessage(BaseI18nCode.tronLinkAddrInitTrxVerify));
        }
        dto.setTronLinkAddr("");//不将用户的usdt地址返回前台
        jo.put("success", true);
        jo.put("content", dto);
        return jo;
    }
}
