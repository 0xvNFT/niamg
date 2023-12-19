package com.play.web.controller.notify.online.notify;

import com.play.common.NotifyWrapper;
import com.play.common.NotifyWrapperFactory;
import com.play.web.annotation.NotNeedLogin;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/onlinePay/notifys")
public class BaseController {
  //  private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @NotNeedLogin
    @ResponseBody
    @RequestMapping("/{iconCss}")
    public void notify(HttpServletRequest request, HttpServletResponse response,@PathVariable String iconCss) throws Exception {
        if(StringUtils.isEmpty(iconCss)){
         //   logger.error("支付不存在",iconCss);
        }
        try {
            NotifyWrapper notifyController = NotifyWrapperFactory.getNotifyController(iconCss);
            notifyController.notify(request,response,iconCss);
        }catch (Exception e){
            System.out.println(e.getMessage());
         //   logger.error("支付回调失败",e.getMessage());
        }

    }
}
