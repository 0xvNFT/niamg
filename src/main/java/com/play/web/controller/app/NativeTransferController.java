package com.play.web.controller.app;

import com.play.cache.redis.DistributedLockUtil;
import com.play.common.SystemConfig;
import com.play.model.*;
import com.play.model.vo.OtherPlayData;
import com.play.service.*;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.app.NativeUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;

@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE + "/v2")
public class NativeTransferController extends BaseNativeController {

    Logger logger = LoggerFactory.getLogger(NativeTransferController.class);
    @Autowired
    protected ThirdGameService thirdGameService;
    @Autowired
    ThirdPlatformService thirdPlatformService;
    @Autowired
    SysUserService userService;
    @Autowired
    ThirdAutoExchangeService thirdAutoExchangeService;
    /**
     * 请求自动转出所有三方余额到系统
     */
    @ResponseBody
    @RequestMapping("/autoTranout")
    public void autoTranout(String tranOutPlatformType) {
        SysUser acc = LoginMemberUtil.currentUser();
        try{
            thirdAutoExchangeService.autoExchange(acc, null, SystemUtil.getStation(),tranOutPlatformType);
        }catch (Exception e){
            logger.error("yg tran out error = ",e);
        }
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(content);
    }

    @ResponseBody
    @RequestMapping("/tesgYGTranout")
    public void tesgYGTranout() {
        SysUser acc = LoginMemberUtil.currentUser();
        if (!DistributedLockUtil.tryGetDistributedLock("autoTranout:" + acc.getId(), 5)) {
//            throw new BaseException(BaseI18nCode.concurrencyLimit30);
            Map<String, Object> content = new HashMap<String, Object>();
            content.put("success", true);
            renderJSON(content);
        }
        thirdAutoExchangeService.ygAutoExchange(acc, null, SystemUtil.getStation());
        Map<String, Object> content = new HashMap<String, Object>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(content);
    }

    /**
     * 获取额度转换时的真人电子数据；RN项目用
     *
     * @param type 游戏类型
     */
    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/getFeeConvertGames")
    public void getFeeConvertGames(Integer type) {
        List<OtherPlayData> otherGames = NativeUtils.getBesidesLotterys(type);
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("accessToken", ServletUtils.getSession().getId());
        json.put("content", otherGames);
        renderJSON(json);
    }


}
