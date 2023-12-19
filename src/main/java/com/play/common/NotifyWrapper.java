package com.play.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface NotifyWrapper {

    /**
     * 支付回调接口方法
     * @param request
     * @param response
     * @throws IOException
     */
    public void notify(HttpServletRequest request, HttpServletResponse response,String name) throws IOException;
}
