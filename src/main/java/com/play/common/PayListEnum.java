package com.play.common;


public enum PayListEnum {
    /**
     * 多个相同支付配置
     */
    XIANGYUN("XIANGYUN", "LIFENG,WANGWANG,FUYINTONG,YONGSHUN,WUYANZU,HUAJI,YUBAO,XINFU,RENZHI,MIAOFU,YIXING,JUZI,JIUFUV2,YIDA,SUIBIAN,ZHENZHOUJUHE,FUCHOUZHE,WANTONGV2,LOGO,WULIUQIPAY,JQY,RONGHUI,YANGJIE,HUIAN,SANYUE,XIQIU,XINMEI,QIBAJIU,TIANMAOV2,UXIN,CAIXILIE,JUGOUFU,WANSHUN,DINGSHENG,DIANDIAN,LINGLINGYI,JUFUZHIFU,DINGDANG,TIANQIANG,HAITUN,BAOLI,WENFU,CHAOFANV2,JINYUAN,BOBO,TNPAY,MENG,YUNFUTONG,LINGMIAOTONG,QQFUV3,DOUYING,SHUNLONG,DASHENG,CBA,FAJIAZHIFU,WANLUTONGBAI,XIANGJIAOHUITONGV2,JIANDAN,XINHEV3,DOUXIN", "祥云支付"),
    OTHER("OTHER", "OTHER", "不匹配时返回");

    private String payName;
    private String include;
    private String companyName;

    PayListEnum(String payName, String include, String companyName) {
        this.payName = payName;
        this.include = include;
        this.companyName = companyName;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public static boolean isInclude(String name) {
        for (PayListEnum p : values()) {
            if (p.getInclude().contains(name)) {
                if (p.getInclude().contains(",")) {
                    String[] includeArray = p.getInclude().split(",");
                    for (int i = 0; i < includeArray.length; i++) {
                        if (includeArray[i].equals(name)) {
                            return true;
                        }
                    }
                } else if(p.getInclude().equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public static String getPaylistByPayName(String name) {
        for (PayListEnum p : values()) {
            if (p.getInclude().contains(name)) {
                if(p.getInclude().contains(",")){
                    String[] includeArray = p.getInclude().split(",");
                    for(int i=0; i<includeArray.length;i++){
                        if(includeArray[i].equals(name)){
                            return p.getPayName();
                        }
                    }
                }else if(p.getInclude().equals(name)){
                    return p.getPayName();
                }
            }
        }
        return "OTHER";
    }
}
