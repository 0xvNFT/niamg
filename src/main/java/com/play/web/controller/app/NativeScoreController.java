package com.play.web.controller.app;

import com.play.common.SystemConfig;
import com.play.model.StationScoreExchange;
import com.play.model.bo.SubmitExchangeBo;
import com.play.service.StationScoreExchangeService;
import com.play.service.SysUserPermService;
import com.play.service.SysUserScoreService;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.play.web.utils.ControllerRender.renderJSON;


@Controller
@CrossOrigin
@RequestMapping(SystemConfig.CONTROL_PATH_NATIVE+"/v2")
public class NativeScoreController extends BaseNativeController {

    @Autowired
    StationScoreExchangeService stationScoreExchangeService;
    @Autowired
    SysUserScoreService sysUserScoreService;
    @Autowired
    SysUserPermService sysUserPermService;


    /**
     * 获得积分兑换配置
     */
    @RequestMapping("/exchangeConfig")
    @ResponseBody
    public void getExchangeConfig() {
        List<StationScoreExchange> configs = stationScoreExchangeService.findByStationId(SystemUtil.getStationId());
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> mnyConfigs = new ArrayList<>();
        if (configs != null && !configs.isEmpty()) {
            for (StationScoreExchange config : configs) {
                Map<String, Object> map = new HashMap<>();
                map.put("numerator", config.getNumerator() != null ? config.getNumerator().floatValue() : 0);
                map.put("denominator", config.getDenominator() != null ? config.getDenominator().floatValue() : 0);
                map.put("maxVal", config.getMaxVal() != null ? config.getMaxVal().floatValue() : 0);
                map.put("minVal", config.getMinVal() != null ? config.getMinVal().floatValue() : 0);
                map.put("status", config.getStatus() != null ? config.getStatus().longValue() : 0);
                map.put("id", config.getId() != null ? config.getId().longValue() : 0);
                map.put("type", config.getType() != null ? config.getType().longValue() : 0);
                mnyConfigs.add(map);
            }
        }
        results.put("configs", mnyConfigs);
        // 获取当前会员的积分
        Long score = sysUserScoreService.getScore(LoginMemberUtil.getUserId(), SystemUtil.getStationId());
        results.put("score", score != null ? score : 0);
        Map<String, Object> content = new HashMap<>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        content.put("content", results);
        renderJSON(content);
    }

    @RequestMapping("/exchangeForRN")
    @ResponseBody
    public void exchangeForRN(@RequestBody SubmitExchangeBo bo) {
        stationScoreExchangeService.confirmExchange(LoginMemberUtil.currentUser(), new BigDecimal(bo.getAmount()), bo.getTypeId());
        Map<String, Object> content = new HashMap<>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(content);
    }

    @RequestMapping("/initPerm")
    @ResponseBody
    public void initPerm() {
        sysUserPermService.init(LoginMemberUtil.getUserId(), SystemUtil.getStationId(), LoginMemberUtil.currentUser().getUsername(),
        LoginMemberUtil.getAccountType());
        Map<String, Object> content = new HashMap<>();
        content.put("success", true);
        content.put("accessToken", ServletUtils.getSession().getId());
        renderJSON(content);
    }

}
