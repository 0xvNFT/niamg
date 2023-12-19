package com.play.web.utils.mlangIpCommons.template;

import com.play.web.utils.mlangIpCommons.core.IPCacheModel;

import java.util.WeakHashMap;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-08 21:36
 **/

//装饰者模式
public class WeakCacheIpCheckTemplateAdapter implements IpCheckTemplate {
    private IpCheckTemplate ipCheckTemplate;
    //弱引用缓存
    private WeakHashMap<String, IPCacheModel> cacheMap = new WeakHashMap();

    @Override
    public boolean check(long stationId, String ip) {
        String key = stationId + ":" + ip;
        IPCacheModel ipCacheModel = cacheMap.get(key);
        if (ipCacheModel == null || ipCacheModel.isTimeOut()) {
            boolean flag = ipCheckTemplate.check(stationId, ip);
            ipCacheModel = new IPCacheModel(flag, System.currentTimeMillis());
            cacheMap.put(key, ipCacheModel);
        }
        return ipCacheModel.isFlag();
    }

    public WeakCacheIpCheckTemplateAdapter(IpCheckTemplate ipCheckTemplate) {
        this.ipCheckTemplate = ipCheckTemplate;
    }
}
