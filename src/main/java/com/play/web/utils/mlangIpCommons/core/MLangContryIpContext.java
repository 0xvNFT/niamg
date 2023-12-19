package com.play.web.utils.mlangIpCommons.core;

import com.play.web.utils.mlangIpCommons.ALLIpRecord;
import com.play.web.utils.mlangIpCommons.ALLIpTool;
import com.play.web.utils.mlangIpCommons.MlangLifecycle;
import com.play.web.utils.mlangIpCommons.parser.IpParser;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-06 13:41
 **/


public class MLangContryIpContext implements ContryIpContext, MlangLifecycle {
    //ipMap存储IP段
    private ConcurrentSkipListMap<Long, ALLIpRecord> ipMap;
    private IpParser ipParser;

    public MLangContryIpContext(IpParser ipParser) {
        this.ipParser = ipParser;
        init();
    }

    //根据ip获得区域code，核心代码
    @Override
    public String getContryCode(String ip) {
        String countryCodeStr = "";
        if (ip == null || ip.trim().isEmpty()) {
            return "";
        }
        if (StringUtils.equals("127.0.0.1", ip)) {
            return "";
        }
        if (ALLIpTool.isValidIpV4Address(ip)) {
            long value = ALLIpTool.ipToLong(ip);
            //从ipmap获取 最接近ip的ip段
            Map.Entry<Long,ALLIpRecord> entry = ipMap.floorEntry(value);
            if (entry!=null&&entry.getValue().contains(value)){
                //判断 如果存在ip段，并且该ip存在ip段范围内
                return entry.getValue().countryCode;
            }
        }
        return countryCodeStr;
    }

    @Override
    public void init() {
        ipMap = new ConcurrentSkipListMap<>();
        ipParser.parse().forEach(allIpRecord -> ipMap.put(allIpRecord.start,allIpRecord));
    }
}
