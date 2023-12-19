package com.play.web.utils;

import com.play.common.utils.CookieHelper;
import com.play.core.StationConfigEnum;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;

public class FbUtils {

    /**
     * 替换网站统计代码里的fid_placeholder,tid_placeholder占位符为真实的pixel id
     * @param fid
     * @param tid
     * @return
     */
    public static String replace(String fid,String tid) {
        String code = StationConfigUtil.get(SystemUtil.getStationId(), StationConfigEnum.station_statistics_code);
        if (StringUtils.isNotEmpty(code)) {
            if (StringUtils.isEmpty(fid)) {
                fid = CookieHelper.get(ServletUtils.getRequest(), "fid");
            }
            if (StringUtils.isEmpty(tid)) {
                tid = CookieHelper.get(ServletUtils.getRequest(), "tid");
            }
            if (StringUtils.isNotEmpty(fid)) {
                code = code.replace("fid_placeholder", fid);//替换facebook统计代码中的占位符
            }
            if (StringUtils.isNotEmpty(tid)) {
                code = code.replace("tid_placeholder", tid);//替换tiktok统计代码中的占位符
            }
        }
        return code;
    }

}
