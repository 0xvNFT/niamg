package com.play.service.impl;

import com.play.common.utils.RandomStringUtils;
import com.play.core.ThirdAuthLoginEnum;
import com.play.dao.SysUserThirdAuthDao;
import com.play.model.SysUser;
import com.play.model.SysUserThirdAuth;
import com.play.model.bo.UserRegisterBo;
import com.play.model.dto.SysUserThirdAuthDto;
import com.play.service.SysUserLoginService;
import com.play.service.SysUserRegisterService;
import com.play.service.ThirdAuthLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class ThirdAuthLoginServiceImpl implements ThirdAuthLoginService {
    private static final Logger logger = LoggerFactory.getLogger(ThirdAuthLoginServiceImpl.class);

    @Autowired
    private SysUserThirdAuthDao sysUserThirdAuthDao;

    @Autowired
    private SysUserRegisterService userRegisterService;

    @Autowired
    private SysUserLoginService sysUserLoginService;

    @Override
    public boolean processData(SysUserThirdAuthDto dto) {
        SysUserThirdAuth sysUserThirdAuth = sysUserThirdAuthDao.getOne(dto.getStationId(), dto.getSource(), dto.getThirdId());
        if (!ObjectUtils.isEmpty(sysUserThirdAuth)) {
            // 用户登录
            sysUserLoginService.doLoginForThirdAuth(sysUserThirdAuth.getUserId());
        }else {
            // 注册用户
            UserRegisterBo bo = new UserRegisterBo();
            bo.setUsername(RandomStringUtils.getCode(11));
            bo.setPwd(RandomStringUtils.randomAll(8));
            bo.setUserRegisterType(this.convertUserRegisterType(dto.getSource()));
            SysUser sysUser = userRegisterService.doThirdAuthRegister(bo);

            // 获取userId userName,写入sys_user_third_auth表
            dto.setUserId(sysUser.getId());
            dto.setLocalUsername(bo.getUsername());
            dto.setCreateDatetime(new Date());
            sysUserThirdAuthDao.save(dto);
        }

        return Boolean.TRUE;
    }

    private String convertUserRegisterType(String source) {
        if (source.equalsIgnoreCase(ThirdAuthLoginEnum.Facebook.name())) {
            return ThirdAuthLoginEnum.Facebook.getType();
        }
        if (source.equalsIgnoreCase(ThirdAuthLoginEnum.Google.name())) {
            return ThirdAuthLoginEnum.Google.getType();
        }
        return null;
    }
}
