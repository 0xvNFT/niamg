package com.play.web.utils.mlangIpCommons.parser;

import com.play.web.utils.mlangIpCommons.ALLIpRecord;

import java.util.List;

/**
 * @program: mlang
 * @description:
 * @author: Mike
 * @create: 2021-03-06 13:51
 **/

//ip数据集提供接口
public interface IpParser {
    List<ALLIpRecord> parse();
}
