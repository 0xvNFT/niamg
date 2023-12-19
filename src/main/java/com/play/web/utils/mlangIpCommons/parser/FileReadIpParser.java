package com.play.web.utils.mlangIpCommons.parser;

import com.play.web.utils.mlangIpCommons.ALLIpRecord;
import com.play.web.utils.mlangIpCommons.ALLIpTool;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-06 13:52
 **/

//从文件系统获取ip数据集
public class FileReadIpParser implements IpParser{
    private static Logger logger = LoggerFactory.getLogger(ALLIpTool.class);
    private final String[] ipText = new String[]{"apnicIps.txt","arinIps.txt","afrinicIps.txt","lacnicIps.txt","ripeIps.txt"};
    @Override
    public List<ALLIpRecord> parse() {
        List<ALLIpRecord> ipRecords = new ArrayList<>();
        try {
            for (String txt : ipText){
                String content = FileUtils.readFileToString(new ClassPathResource(txt).getFile(), "UTF-8");
                String[] cs = content.split("\\|");
                String[] ips ;
                for (String s : cs) {
                    if (s == null) {
                        continue;
                    }
                    ips = s.split(",");
                    if (ips.length != 3) {
                        continue;
                    }
                    long ipLong = Long.parseLong(ips[0]);
                    int count = Integer.parseInt(ips[1]);
                    String countryCode = ips[2];
                    ipRecords.add(new ALLIpRecord(ipLong, count, countryCode));
                }
            }
        }catch (IOException e){
            logger.error("初始化国家ip范围发生错误", e);
        }
        return ipRecords;
    }
}
