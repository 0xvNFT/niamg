package com.play.service;

import com.play.model.dto.SysUserThirdAuthDto;

public interface ThirdAuthLoginService {

    /**
     * 处理从第三登录后授权获取的信息
     * @param dto
     */
    boolean processData(SysUserThirdAuthDto dto);
}
