package com.play.web.utils.app;

import com.alibaba.fastjson.JSONArray;
import com.play.common.utils.SpringUtils;
import com.play.core.LanguageEnum;
import com.play.core.PlatformType;
import com.play.core.ThirdGameEnum;
import com.play.model.AppGameFoot;
import com.play.model.ThirdGame;
import com.play.model.app.GameItemResult;
import com.play.model.app.GameSearchResult;
import com.play.model.vo.AppThirdGameVo;
import com.play.model.vo.OtherPlayData;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.service.GameService;
import com.play.service.ThirdGameService;
import com.play.service.ThirdPlatformService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnson 通用接口工具类
 */
public class NativeUtils {

    private static Map<String, String> gameDataMap = new HashMap<>();

    public static Map<String, Object> renderExceptionJson(String error) {
        Map<String, Object> json = new HashMap<>();
        json.put("success", false);
        json.put("code", 10000);
        json.put("msg", !StringUtils.isEmpty(error) ? error : "系统错误");
        return json;
    }

    /**
     * 读取game json
     *
     * @param filePath
     * @return
     */
    public static String readGamesJson(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        if (gameDataMap.containsKey(filePath)) {
            return gameDataMap.get(filePath);
        }
        InputStream is = null;
        try {
            is = new FileInputStream(new File(filePath));
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            gameDataMap.put(filePath, text);
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 隐藏字符串
     *
     * @param str               原字符串
     * @param showBackCharCount 保留后几位字符
     * @return
     */
    public static String hideChar(String str, int showBackCharCount) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        if (str.length() < showBackCharCount + 1) {
            return str;
        }
        String aaa = "";
        for (int i = 0; i < str.length() - showBackCharCount; i++) {
            aaa += "*";
        }
        return aaa + str.substring(str.length() - showBackCharCount, str.length());
    }

    /**
     * 类型处理
     */
    public static Integer[] handelTypes(String types) {
        String[] typeStr = types.split(",");
        Integer[] typeInt = new Integer[typeStr.length];
        for (int i = 0; i < typeStr.length; i++) {
            typeInt[i] = Integer.parseInt(typeStr[i]);
        }
        return typeInt;
    }

    public static void validatorProxy() {

    }

    /**
     * 向系统外部发起同步请求
     *
     * @param url
     */
    public static String asyncRequest(String url, Map<String, String> map) {
        String content = new RequestProxy() {
            public List<Header> getHeaders() {
                List<Header> headerList = new ArrayList<Header>();
                headerList.add(new BasicHeader("X-Requested-With", "XMLHttpRequest"));
                headerList.add(new BasicHeader("Connection", "close")); // 短链接
                return headerList;
            }

            public List<NameValuePair> getParameters() {
                List<NameValuePair> ps = new ArrayList<>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    ps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                return ps;
            }
        }.doRequest(url, PostType.POST, true);
        return content;
    }

//    /**
//     * trust all cert when request is https protocol
//     */
//    private static void trustAllHosts() {
//        // Create a trust manager that does not validate certificate chains
//        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//
//            public X509Certificate[] getAcceptedIssuers() {
//                return new X509Certificate[]{};
//            }
//
//            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
////                LoggerFactory.getLogger("aaa checkClientTrusted");
//            }
//
//            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
////                LoggerFactory.getLogger("aaa checkServerTrusted");
//            }
//        }};
//
//        // Install the all-trusting trust manager
//        try {
//            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
//            SSLContext context = SSLContext.getInstance("TLS");
//            context.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            //            SSLContext sc = SSLContext.getInstance("TLS");
//            //            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            //            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//    }

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
     *
     * @param url      请求地址 form表单url地址
     * @param file     在上传的文件
     * @param fileName 文件名
     * @return String url的响应信息返回值
     * @throws IOException
     */
    public static Integer filePost(String url, InputStream file, long size, String fileName) throws Exception {
        try {
//            trustAllHosts();
            URL urlObj = new URL(url);
            // 连接
            HttpsURLConnection con = (HttpsURLConnection) urlObj.openConnection();
            /**
             * 设置关键值
             */
            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            con.setConnectTimeout(60 * 1000 * 2);
            con.setReadTimeout(60 * 1000 * 2);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false); // post方式不能使用缓存
            // 设置请求头信息
            con.setRequestProperty("charset", "UTF-8");
            con.setRequestProperty("accept", "application/json");
            con.setRequestProperty("Content-length", String.valueOf(size));
            // 设置边界
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            // 获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());
            // 请求正文信息
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            // 输出表头
            out.write(head);
            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            DataInputStream in = new DataInputStream(file);
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            // 结尾部分
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            out.close();
            BufferedReader reader = null;
            try {
                // 返回值
                int resultCode = con.getResponseCode();
                LoggerFactory.getLogger(NativeUtils.class.getSimpleName()).error("save launch img file status code = ", resultCode);
                return resultCode;
            } catch (IOException e) {
                throw e;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e2) {
            throw e2;
        }
    }

    // 字符串去重
    public static List<String> distinctString(String[] array) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                list.add(array[i]);
            }
        }
        return list;
    }

    //type 0--体育 1-真人 2-电子 3--彩票 4--棋牌 5--红包 6--电竞 7--捕鱼 10--热门 11-大厅
    public static Integer convertTabGameType2HotGameType(Integer type) {
        Integer hotGameType = 3;
        switch (type) {
            case 1:// 彩票
                hotGameType = 3;
                break;
            case 2:// 真人
                hotGameType = 1;
                break;
            case 3:// 电子
                hotGameType = 2;
                break;
            case 4:// 体育
                hotGameType = 0;
                break;
            case 5:// 电竞
                hotGameType = 6;
                break;
            case 6:// 捕鱼
                hotGameType = 7;
                break;
            case 7:// 棋牌
                hotGameType = 4;
                break;
            case 9:// 红包
                hotGameType = 5;
                break;
            default:
                hotGameType = 3;
        }
        return hotGameType;
    }

    public static List<AppThirdGameVo> getOtherGame(Integer type,Integer ver) {
        return getOtherGame(type, ver, null);
    }
    public static List<AppThirdGameVo> getOtherGame(Integer type) {
        return getOtherGame(type, null, null);
    }

    public static List<AppThirdGameVo> getOtherGame(Integer type,Integer ver,String lan) {//不能使用缓存，否则前台切换语种的时候，内容没有根据语种变化
//		String key = String.format("%s_%s_%s_%s", "native_", SystemUtil.getStationId(), "/v2/getGame",String.valueOf(type));
//		String lotteryRecord = CacheUtil.getCache(CacheKey.NATIVE, key);
//		if (StringUtils.isNotEmpty(lotteryRecord)) {
//			return NativeJsonUtil.toList(lotteryRecord, AppThirdGameVo.class);
//		} else {
        List<AppThirdGameVo> datas = new ArrayList<>();
        if (datas != null) {
            List<OtherPlayData> sports = NativeUtils.getBesidesLotterys(type, ver, lan);
            for (OtherPlayData data : sports) {
                AppThirdGameVo d = new AppThirdGameVo();
                d.setName(data.getTitle());
                d.setCzCode(data.getPlayCode());
                d.setImgUrl(data.getImgUrl());
                d.setModuleCode(1);
                d.setPopFrame(data.isPopFramge());
                d.setGameType(data.getGameType());
                d.setGameTabType(type);
                d.setForwardUrl(data.getForwardUrl());
                d.setIsListGame(data.getIsListGame());
                d.setFeeConvertUrl(data.getFeeConvertUrl());
                d.setType(data.getType());
                datas.add(d);
            }
        }
//			CacheUtil.addCache(CacheKey.NATIVE, key, datas);
        return datas;
//		}
    }

    public static List<OtherPlayData> getBesidesLotterys(Integer code) {
        return getBesidesLotterys(code, null);
    }

    public static List<OtherPlayData> getBesidesLotterys(Integer code,Integer ver) {
        return getBesidesLotterys(code, ver, null);
    }
    /**
     * 获取体育，电子，真人分类的种类信息
     *
     * @param code 种类代码 0--体育 1-真人 2-电子 3--彩票 4--棋牌 5--红包 6--电竞
     */
    public static List<OtherPlayData> getBesidesLotterys(Integer code,Integer ver,String lan) {
        List<OtherPlayData> results = new ArrayList<>();
        try {
            String baseConvertUrl = "/thirdTrans/thirdRealTransMoney.do";
            ThirdGameService thirdGameService = SpringUtils.getBean(ThirdGameService.class);
            ThirdPlatformService thirdPlatformService = SpringUtils.getBean(ThirdPlatformService.class);
            ThirdGame thirdGame = thirdGameService.findOne(SystemUtil.getStationId());
            ThirdPlatformSwitch platformSwitch = thirdPlatformService.getPlatformSwitch(SystemUtil.getStationId());
            boolean isV3 = (ver != null && ver == 3) ? true : false;
            if (StringUtils.isEmpty(lan)) {
                lan = SystemUtil.getLanguage();
            }
            LanguageEnum lanEnum = LanguageEnum.getLanguageEnum2(lan);
            if (code == 3) {// 彩票
                if (thirdGame.getLottery() == 2) {
                    if (platformSwitch.isYg()) {
                        OtherPlayData tcg = new OtherPlayData();
                        tcg.setTitle(I18nTool.getMessage("PlatformType.YG",lanEnum.getLocale()));
                        if (isV3) {
                            tcg.setImgUrl("/common/third/images/v3/yg-lottery-bg3.png");
                        } else {
                            tcg.setImgUrl("/common/third/images/lottery/yg-lottery-bg3.png");
                        }
                        tcg.setDataCode(code);
                        tcg.setPopFramge(true);
                        tcg.setForwardUrl("/third/yg/forwardYg.do?isApp=1&mobile=1");
                        StringBuilder convertUrl = new StringBuilder("/third/yg/transMoney.do");
                        tcg.setFeeConvertUrl(convertUrl.toString());
                        tcg.setConvertPlatformType(PlatformType.YG.getValue());
                        tcg.setPlayCode(ThirdGameEnum.YG.getGameType());
                        tcg.setGameType(ThirdGameEnum.YG.getGameType());
                        tcg.setIsListGame(1);
                        tcg.setType("1");
                        results.add(tcg);
                    }
                    if (platformSwitch.isIyg()) {
                        OtherPlayData tcg = new OtherPlayData();
                        tcg.setTitle(I18nTool.getMessage("PlatformType.IYG",lanEnum.getLocale()));
                        tcg.setImgUrl("/common/third/images/lottery/iyg/YNLHC.png");
                        tcg.setDataCode(code);
                        tcg.setPopFramge(true);
                        tcg.setForwardUrl("/third/forwardIyg.do?isApp=1&mobile=1");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        tcg.setFeeConvertUrl(convertUrl.toString());
                        tcg.setConvertPlatformType(PlatformType.IYG.getValue());
                        tcg.setPlayCode(ThirdGameEnum.IYG.getGameType());
                        tcg.setGameType(ThirdGameEnum.IYG.getGameType());
                        tcg.setIsListGame(1);
                        tcg.setType("1");
                        results.add(tcg);
                    }
                }
            } else if (code == 0) {// 体育
                if (thirdGame.getSport() == 2) {
                    // 沙巴体育
                    if (platformSwitch.isTysb()) {
                        OtherPlayData otherPlayData = new OtherPlayData();
                        otherPlayData.setTitle(I18nTool.getMessage("admin.sad.sports",lanEnum.getLocale()));
                        if (isV3) {
                            otherPlayData.setImgUrl("/common/third/images/sport/saba-sport-bg2.png");
                        } else {
                            otherPlayData.setImgUrl("/common/third/images/sport/new/saba-sport-bg.png");
                        }
                        otherPlayData.setDataCode(code);
                        otherPlayData.setForwardUrl("/third/forwardTysb.do?mobile=1&isApp=1");
                        otherPlayData.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        otherPlayData.setFeeConvertUrl(convertUrl.toString());
                        otherPlayData.setConvertPlatformType(PlatformType.TYSB.getValue());
                        otherPlayData.setPlayCode(ThirdGameEnum.TYSBSPORT.getGameType());
                        otherPlayData.setGameType(ThirdGameEnum.TYSBSPORT.getGameType());
                        otherPlayData.setType("4");
                        results.add(otherPlayData);
                    }
                    // AG体育
                    if (platformSwitch.isAg()) {
                        OtherPlayData otherPlayData = new OtherPlayData();
                        otherPlayData.setTitle(I18nTool.getMessage("admin.ag.sports",lanEnum.getLocale()));
                        if (isV3) {
                            otherPlayData.setImgUrl("/common/third/images/v3/ag-sport-bg2.png");
                        } else {
                            otherPlayData.setImgUrl("/common/third/images/sport/new/ag-sport-bg.png");
                        }
                        otherPlayData.setDataCode(code);
                        otherPlayData.setForwardUrl("/third/forwardAg.do?mobile=1&isApp=1&gameType=TASSPTA");
                        otherPlayData.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        otherPlayData.setFeeConvertUrl(convertUrl.toString());
                        otherPlayData.setConvertPlatformType(PlatformType.AG.getValue());
                        otherPlayData.setPlayCode(ThirdGameEnum.AGSPORT.getGameType());
                        otherPlayData.setGameType(ThirdGameEnum.AGSPORT.getGameType());
                        otherPlayData.setType("4");
                        results.add(otherPlayData);
                    }
                    // PP体育
                    if (platformSwitch.isPp()) {
                        OtherPlayData ppSport = new OtherPlayData();
                        ppSport.setTitle(I18nTool.getMessage("admin.pp.sports",lanEnum.getLocale()));
                        if (isV3) {
                            ppSport.setImgUrl("/common/third/images/v3/pp-sport-bg2.png");
                        } else {
                            ppSport.setImgUrl("/common/third/images/sport/new/pp-sport-bg3.png");
                        }
                        ppSport.setDataCode(code);
                        ppSport.setForwardUrl("/third/forwardPP.do?mobile=1&isApp=1&gameType=sport");
                        ppSport.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        ppSport.setFeeConvertUrl(convertUrl.toString());
                        ppSport.setConvertPlatformType(PlatformType.PP.getValue());
                        ppSport.setPlayCode(ThirdGameEnum.PPSPORT.getGameType());
                        ppSport.setGameType(ThirdGameEnum.PPSPORT.getGameType());
                        ppSport.setIsListGame(1);
                        ppSport.setType("4");
                        results.add(ppSport);
                    }
                    // FB体育
                    if (platformSwitch.isFb()) {
                        OtherPlayData ppSport = new OtherPlayData();
                        ppSport.setTitle(I18nTool.getMessage("admin.fb.sports",lanEnum.getLocale()));
                        if (isV3) {
                            ppSport.setImgUrl("/common/third/images/v3/pp-sport-bg2.png");
                        } else {
                            ppSport.setImgUrl("/common/third/images/sport/new/fb-sport-bg4.png");
                        }
                        ppSport.setDataCode(code);
                        ppSport.setForwardUrl("/third/forwardFB.do?mobile=1&isApp=1&gameType=sport");
                        ppSport.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        ppSport.setFeeConvertUrl(convertUrl.toString());
                        ppSport.setConvertPlatformType(PlatformType.FB.getValue());
                        ppSport.setPlayCode(ThirdGameEnum.FBSPORT.getGameType());
                        ppSport.setGameType(ThirdGameEnum.FBSPORT.getGameType());
                        ppSport.setIsListGame(1);
                        ppSport.setType("4");
                        results.add(ppSport);
                    }
                }
            } else if (code == 6) {// 电竞
                if (thirdGame.getEsport() == 2) {
                    // 泛亚电竞
                    if (platformSwitch.isAvia()) {
                        OtherPlayData data = new OtherPlayData();
                        data.setTitle(I18nTool.getMessage("admin.asia.esport",lanEnum.getLocale()));
                        if (isV3) {
                            data.setImgUrl("/common/third/images/v3/avia-esport-bg2.png");
                        } else {
                            data.setImgUrl("/common/third/images/esport/avia-esport-bg2.png");
                        }
                        data.setDataCode(code);
                        data.setPlayCode("avia");
                        data.setGameType("avia");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        data.setFeeConvertUrl(convertUrl.toString());
                        data.setPopFramge(true);
                        data.setConvertPlatformType(PlatformType.AVIA.getValue());
                        data.setForwardUrl("/third/forwardAvia.do?isApp=1&mobile=1");
                        data.setType("5");
                        results.add(data);
                    }
                }
            } else if (code == 4) {// 棋牌
                if (thirdGame.getChess() == 2) {
                    // 开元棋牌开关
                    if (platformSwitch.isKy()) {
                        OtherPlayData hgSport = new OtherPlayData();
                        hgSport.setTitle(I18nTool.getMessage("PlatformVo.KY",lanEnum.getLocale()));
                        if (isV3) {
                            hgSport.setImgUrl("/common/third/images/v3/ky-poker-bg.png");
                        } else {
                            hgSport.setImgUrl("/common/third/images/chess/new/ky-poker-bg.png");
                        }
                        hgSport.setDataCode(code);
                        hgSport.setPlayCode("kyqp");
                        hgSport.setPopFramge(true);
//						hgSport.setIsListGame(1);
                        hgSport.setForwardUrl("/third/forwardKYChess.do?gameType=0&isApp=1&mobile=1");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        hgSport.setFeeConvertUrl(convertUrl.toString());
                        hgSport.setConvertPlatformType(PlatformType.KY.getValue());
                        hgSport.setGameType("ky");
                        hgSport.setType("7");
                        results.add(hgSport);
                    }

                    // 乐游棋牌开关
                    if (platformSwitch.isLeg()) {
                        OtherPlayData hgSport = new OtherPlayData();
                        hgSport.setTitle(I18nTool.getMessage("PlatformVo.LEG",lanEnum.getLocale()));
                        if (isV3) {
                            hgSport.setImgUrl("/common/third/images/v3/leg-poker-bg.png");
                        } else {
                            hgSport.setImgUrl("/common/third/images/chess/new/leg-poker-bg.png");
                        }
                        hgSport.setDataCode(code);
                        hgSport.setPlayCode("leg");
                        hgSport.setPopFramge(true);
//						hgSport.setIsListGame(1);
                        hgSport.setForwardUrl("/third/forwardLeg.do?gameType=1&isApp=1&mobile=1");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        hgSport.setFeeConvertUrl(convertUrl.toString());
                        hgSport.setConvertPlatformType(PlatformType.LEG.getValue());
                        hgSport.setGameType("leg");
                        hgSport.setType("7");
                        results.add(hgSport);
                    }

                    // V8棋牌开关
                    if (platformSwitch.isV8Poker()) {
                        OtherPlayData data = new OtherPlayData();
                        data.setTitle(I18nTool.getMessage("PlatformVo.V8POKER",lanEnum.getLocale()));
                        if (isV3) {
                            data.setImgUrl("/common/third/images/v3/v8-poker-bg.png");
                        } else {
                            data.setImgUrl("/common/third/images/chess/new/v8-poker-bg.png");
                        }
                        data.setDataCode(code);
                        data.setPlayCode(ThirdGameEnum.V8POKER.getGameType());
                        data.setGameType(ThirdGameEnum.V8POKER.getGameType());
                        data.setPopFramge(true);
//						data.setIsListGame(1);
                        data.setForwardUrl("/third/forwardV8Poker.do?gameType=0&isApp=1&mobile=1");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        data.setFeeConvertUrl(convertUrl.toString());
                        data.setConvertPlatformType(PlatformType.V8POKER.getValue());
                        data.setType("7");
                        results.add(data);
                    }
                }
            } else if (code == 1) {// 真人
                if (thirdGame.getLive() == 2) {
                    // ag真人游戏开关
                    if (platformSwitch.isAg()) {
                        OtherPlayData ag = new OtherPlayData();
                        ag.setTitle(I18nTool.getMessage("admin.ag.hall",lanEnum.getLocale()));
                        if (isV3) {
                            ag.setImgUrl("/common/third/images/v3/ag-live-bg2.png");
                        } else {
                            ag.setImgUrl("/common/third/images/live/new/ag-live-bg.png");
                        }
                        ag.setDataCode(code);
                        ag.setPlayCode(ThirdGameEnum.AGLIVE.getGameType());
                        ag.setGameType(ThirdGameEnum.AGLIVE.getGameType());
                        ag.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        ag.setFeeConvertUrl(convertUrl.toString());
                        ag.setConvertPlatformType(PlatformType.AG.getValue());
                        ag.setForwardUrl("/third/forwardAg.do?mobile=1&isApp=1");
                        ag.setType("2");
                        ag.setIsListGame(1);
                        results.add(ag);
                    }

                    // BBIN真人游戏开关
                    if (platformSwitch.isBbin()) {
                        OtherPlayData bbin = new OtherPlayData();
                        bbin.setTitle(I18nTool.getMessage("admin.bbin.hall",lanEnum.getLocale()));
                        if (isV3) {
                            bbin.setImgUrl("/common/third/images/v3/bbin-live-bg2.png");
                        } else {
                            bbin.setImgUrl("/common/third/images/live/new/bbin-live-bg.png");
                        }
                        bbin.setDataCode(code);
                        bbin.setPlayCode(ThirdGameEnum.BBINLIVE.getGameType());
                        bbin.setGameType(ThirdGameEnum.BBINLIVE.getGameType());
                        bbin.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bbin.setFeeConvertUrl(convertUrl.toString());
                        bbin.setConvertPlatformType(PlatformType.BBIN.getValue());
                        bbin.setForwardUrl("/third/forwardBbin2.do?gameType=live&mobile=1&isApp=1");
                        bbin.setType("2");
                        bbin.setIsListGame(1);
                        results.add(bbin);
                    }

                    // mg真人游戏开关
                    if (platformSwitch.isMg()) {
                        OtherPlayData mg = new OtherPlayData();
                        mg.setTitle(I18nTool.getMessage("admin.mg.hall",lanEnum.getLocale()));
                        if (isV3) {
                            mg.setImgUrl("/common/third/images/v3/mg-live-bg2.png");
                        } else {
                            mg.setImgUrl("/common/third/images/live/new/mg-live-bg2.png");
                        }
                        mg.setDataCode(code);
                        mg.setPlayCode(ThirdGameEnum.MGLIVE.getGameType());
                        mg.setGameType(ThirdGameEnum.MGLIVE.getGameType());
                        mg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        mg.setFeeConvertUrl(convertUrl.toString());
                        mg.setConvertPlatformType(PlatformType.MG.getValue());
                        mg.setForwardUrl("/third/forwardMg.do?gameType=1210&mobile=1&isApp=1");
                        mg.setType("2");
                        mg.setIsListGame(1);
                        results.add(mg);
                    }

                    // dg真人游戏开关
                    if (platformSwitch.isDg()) {
                        OtherPlayData dg = new OtherPlayData();
                        dg.setTitle(I18nTool.getMessage("admin.dg.hall",lanEnum.getLocale()));
                        if (isV3) {
                            dg.setImgUrl("/common/third/images/v3/dg-live-bg2.png");
                        } else {
                            dg.setImgUrl("/common/third/images/live/new/dg-live-bg.png");
                        }
                        dg.setDataCode(code);
                        dg.setPlayCode(ThirdGameEnum.DGLIVE.getGameType());
                        dg.setGameType(ThirdGameEnum.DGLIVE.getGameType());
                        dg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        dg.setFeeConvertUrl(convertUrl.toString());
                        dg.setConvertPlatformType(PlatformType.DG.getValue());
                        dg.setForwardUrl("/third/forwardDg.do?mobile=1&isApp=1");
                        dg.setType("2");
                        results.add(dg);
                    }

                    // bg真人游戏开关
                    if (platformSwitch.isBg()) {
                        OtherPlayData bg = new OtherPlayData();
                        bg.setTitle(I18nTool.getMessage("admin.bg.hall",lanEnum.getLocale()));
                        if (isV3) {
                            bg.setImgUrl("/common/third/images/v3/bg-live-bg2.png");
                        } else {
                            bg.setImgUrl("/common/third/images/live/new/bg-live-bg.png");
                        }
                        bg.setDataCode(code);
                        bg.setPlayCode(ThirdGameEnum.BGLIVE.getGameType());
                        bg.setGameType(ThirdGameEnum.BGLIVE.getGameType());
                        bg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bg.setFeeConvertUrl(convertUrl.toString());
                        bg.setConvertPlatformType(PlatformType.BG.getValue());
                        bg.setForwardUrl("/third/forwardBg.do?mobile=1&isApp=1");
                        bg.setType("2");
                        results.add(bg);
                    }

                    //evolution真人
                    if (platformSwitch.isEvolution()) {
                        OtherPlayData bg = new OtherPlayData();
                        bg.setTitle(I18nTool.getMessage("admin.evolution.hall",lanEnum.getLocale()));
                        if (isV3) {
                            bg.setImgUrl("/common/third/images/v3/evolution-live-bg2.png");
                        } else {
                            bg.setImgUrl("/common/third/images/live/new/evolution-live-bg.png");
                        }
                        bg.setDataCode(code);
                        bg.setPlayCode(ThirdGameEnum.EVOLIVE.getGameType());
                        bg.setGameType(ThirdGameEnum.EVOLIVE.getGameType());
                        bg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bg.setFeeConvertUrl(convertUrl.toString());
                        bg.setConvertPlatformType(PlatformType.EVOLUTION.getValue());
                        bg.setForwardUrl("/third/forwardEvolution.do?mobile=1&isApp=1");
                        bg.setIsListGame(1);
                        bg.setType("2");
                        results.add(bg);
                    }

                    //pp 真人
                    if (platformSwitch.isPp()) {
                        OtherPlayData bg = new OtherPlayData();
                        bg.setTitle(I18nTool.getMessage("admin.pp.hall",lanEnum.getLocale()));
                        if (isV3) {
                            bg.setImgUrl("/common/third/images/v3/pp-live-bg2.png");
                        } else {
                            bg.setImgUrl("/common/third/images/live/new/pp-live-bg.png");
                        }
                        bg.setDataCode(code);
                        bg.setPlayCode(ThirdGameEnum.PPLIVE.getGameType());
                        bg.setGameType(ThirdGameEnum.PPLIVE.getGameType());
                        bg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bg.setFeeConvertUrl(convertUrl.toString());
                        bg.setConvertPlatformType(PlatformType.PP.getValue());
                        bg.setForwardUrl("/third/forwardPP.do?mobile=1&isApp=1&gameType=live");
                        bg.setIsListGame(1);
                        bg.setType("2");
                        results.add(bg);
                    }
                    //AWC 真人
                    if (platformSwitch.isAwc()) {
                        OtherPlayData bg = new OtherPlayData();
                        bg.setTitle(I18nTool.getMessage("admin.awc.hall",lanEnum.getLocale()));
                        if (isV3) {
                            bg.setImgUrl("/common/third/images/v3/awc-live-bg2.png");
                        } else {
                            bg.setImgUrl("/common/third/images/live/new/awc-live-bg.png");
                        }
                        bg.setDataCode(code);
                        bg.setPlayCode(ThirdGameEnum.AWC.getGameType());
                        bg.setGameType(ThirdGameEnum.AWC.getGameType());
                        bg.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bg.setFeeConvertUrl(convertUrl.toString());
                        bg.setConvertPlatformType(PlatformType.AWC.getValue());
                        bg.setForwardUrl("/third/forwardAwc.do?mobile=1&isApp=1&gameType=live");
                        bg.setIsListGame(1);
                        bg.setType("2");
                        results.add(bg);
                    }

                }
            } else if (code == 2) {// 电子
                if (thirdGame.getEgame() == 2) {
                    // pg电子开关
                    if (platformSwitch.isPg()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.pg.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/pg-slot-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/pg-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("pg");
                        evo.setGameType("pg");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.PG.getValue());
                        evo.setForwardUrl("/third/forwardPg.do?mobile=1&isApp=1&gameType=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    // bg游戏开关
                    if (platformSwitch.isBs()) {
                        OtherPlayData mg = new OtherPlayData();
                        mg.setTitle(I18nTool.getMessage("admin.bs.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            mg.setImgUrl("/common/third/images/v3/mg-live-bg-egame.png");
                        } else {
                            mg.setImgUrl("/common/third/images/egame/new/bs-live-bg-egame.png");
                        }
                        mg.setDataCode(code);
                        mg.setPlayCode("bs");
                        mg.setGameType("bs");
                        mg.setConvertPlatformType(PlatformType.BS.getValue());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        mg.setFeeConvertUrl(convertUrl.toString());
                        mg.setIsListGame(1);
                        mg.setType("3");
                        results.add(mg);
                    }

                    if (platformSwitch.isJdb()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.jdb.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/pp-slot-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/jdb-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("jdb");
                        evo.setGameType("jdb");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.JDB.getValue());
                        evo.setForwardUrl("/third/forwardJdb.do?mobile=1&isApp=1&gameType=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    // ag游戏开关
                    if (platformSwitch.isAg()) {
                        OtherPlayData ag = new OtherPlayData();
                        ag.setTitle(I18nTool.getMessage("admin.ag.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            ag.setImgUrl("/common/third/images/v3/ag-live-bg-egame.png");
                        } else {
                            ag.setImgUrl("/common/third/images/egame/new/ag-live-bg-egame.png");
                        }
                        ag.setDataCode(code);
                        ag.setPlayCode("ag");
                        ag.setGameType("ag");
                        ag.setForwardUrl("/third/forwardAg.do?gameType=8&mobile=1&isApp=1");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        ag.setConvertPlatformType(PlatformType.AG.getValue());
                        ag.setFeeConvertUrl(convertUrl.toString());
						ag.setIsListGame(1);
                        ag.setType("3");
                        results.add(ag);
                    }

                    // BBIN电子游戏开关
                    if (platformSwitch.isBbin()) {
                        OtherPlayData bbin = new OtherPlayData();
                        bbin.setTitle(I18nTool.getMessage("admin.bbin.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            bbin.setImgUrl("/common/third/images/v3/bbin-live-bg-egame.png");
                        } else {
                            bbin.setImgUrl("/common/third/images/egame/new/bbin-live-bg-egame.png");
                        }
                        bbin.setDataCode(code);
                        bbin.setPlayCode("bbin");
                        bbin.setGameType("bbin");
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bbin.setFeeConvertUrl(convertUrl.toString());
                        bbin.setConvertPlatformType(PlatformType.BBIN.getValue());
                        bbin.setForwardUrl("/third/forwardBbin2.do?gameType=game&gameId=5000&mobile=1&isApp=1");
                        bbin.setType("3");
                        bbin.setIsListGame(1);
                        results.add(bbin);
                    }

                    // mg游戏开关
                    if (platformSwitch.isMg()) {
                        OtherPlayData mg = new OtherPlayData();
                        mg.setTitle(I18nTool.getMessage("admin.mg.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            mg.setImgUrl("/common/third/images/v3/mg-live-bg-egame.png");
                        } else {
                            mg.setImgUrl("/common/third/images/egame/new/mg-live-bg-egame.png");
                        }
                        mg.setDataCode(code);
                        mg.setPlayCode("mg");
                        mg.setGameType("mg");
                        mg.setConvertPlatformType(PlatformType.MG.getValue());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        mg.setFeeConvertUrl(convertUrl.toString());
                        mg.setIsListGame(1);
                        mg.setType("3");
                        results.add(mg);
                    }

                    // pt游戏开关
                    if (platformSwitch.isPt()) {
                        OtherPlayData pt = new OtherPlayData();
                        pt.setTitle(I18nTool.getMessage("admin.pt.ele",lanEnum.getLocale()));
                        if (isV3) {
                            pt.setImgUrl("/common/third/images/v3/pt-live-bg-egame.png");
                        } else {
                            pt.setImgUrl("/common/third/images/egame/new/pt-live-bg-egame.png");
                        }
                        pt.setDataCode(code);
                        pt.setPlayCode("pt");
                        pt.setGameType("pt");
                        pt.setConvertPlatformType(PlatformType.PT.getValue());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        pt.setFeeConvertUrl(convertUrl.toString());
                        pt.setIsListGame(1);
                        pt.setType("3");
                        results.add(pt);
                    }

                    // cq9游戏开关
                    if (platformSwitch.isCq9()) {
                        OtherPlayData cq9 = new OtherPlayData();
                        cq9.setTitle(I18nTool.getMessage("admin.nine.ele",lanEnum.getLocale()));
                        if (isV3) {
                            cq9.setImgUrl("/common/third/images/v3/cq9-live-bg-egame.png");
                        } else {
                            cq9.setImgUrl("/common/third/images/egame/new/cq9-live-bg-egame.png");
                        }
                        cq9.setDataCode(code);
                        cq9.setPlayCode("cq9");
                        cq9.setGameType("cq9");
                        cq9.setForwardUrl("/third/forwardCq9.do?mobile=1&isApp=1");
                        cq9.setConvertPlatformType(PlatformType.CQ9.getValue());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        cq9.setFeeConvertUrl(convertUrl.toString());
                        cq9.setIsListGame(1);
                        cq9.setType("3");
                        results.add(cq9);
                    }
                    // evo电子开关
                    if (platformSwitch.isEvo()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.evo.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/evo-live-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/evo-live-bg-egame.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("evo");
                        evo.setGameType("evo");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.EVO.getValue());
                        evo.setForwardUrl("/third/forwardEvo.do?mobile=1&isApp=1");
                        evo.setType("3");
                        results.add(evo);
                    }
                    // fg电子开关
                    if (platformSwitch.isFg()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.fg.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/fg-slot-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/fg-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("fg");
                        evo.setGameType("fg");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.FG.getValue());
                        evo.setForwardUrl("/third/forwardFg.do?mobile=1&isApp=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    if (platformSwitch.isTada()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.tada.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/fg-slot-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/tada-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("tada");
                        evo.setGameType("tada");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.TADA.getValue());
                        evo.setForwardUrl("/third/forwardTada.do?mobile=1&isApp=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    // pp电子开关
                    if (platformSwitch.isPp()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.pp.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/pp-slot-live-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/pp-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("pp");
                        evo.setGameType("pp");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.PP.getValue());
                        evo.setForwardUrl("/third/forwardPP.do?mobile=1&isApp=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    // jili电子开关
                    if (platformSwitch.isJl()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("admin.jl.electronic",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/jl-slot-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/jili-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode("jl");
                        evo.setGameType("jl");
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.JL.getValue());
                        evo.setForwardUrl("/third/forwardJl.do?mobile=1&isApp=1&gameType=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                    // vdd电子开关
                    if (platformSwitch.isVdd()) {
                        OtherPlayData evo = new OtherPlayData();
                        evo.setTitle(I18nTool.getMessage("PlatformVo.VDD",lanEnum.getLocale()));
                        if (isV3) {
                            evo.setImgUrl("/common/third/images/v3/pp-slot-live-bg2.png");
                        } else {
                            evo.setImgUrl("/common/third/images/egame/new/vdd-slot-bg.png");
                        }
                        evo.setDataCode(code);
                        evo.setPlayCode(ThirdGameEnum.VDD.getGameType());
                        evo.setGameType(ThirdGameEnum.VDD.getGameType());
                        evo.setIsListGame(1);
                        evo.setPopFramge(true);
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        evo.setFeeConvertUrl(convertUrl.toString());
                        evo.setConvertPlatformType(PlatformType.VDD.getValue());
                        evo.setForwardUrl("/third/forwardVDD.do?mobile=1&isApp=1");
                        evo.setType("3");
                        results.add(evo);
                    }

                }
            } else if (code == 5) {// 红包
                //todo
            } else if (code == 7) {// 捕鱼
                if (thirdGame.getFishing() == 2) {
                    // BBIN捕鱼游戏开关
                    if (platformSwitch.isBbin()) {
                        OtherPlayData bbin = new OtherPlayData();
                        bbin.setTitle(I18nTool.getMessage("admin.bbin.fish.game.hall"));
                        if (isV3) {
                            bbin.setImgUrl("/common/third/images/v3/bbin-fish-bg2.png");
                        } else {
                            bbin.setImgUrl("/common/third/images/fish/new/bbin-fish-bg.png");
                        }
                        bbin.setDataCode(code);
                        bbin.setPlayCode(ThirdGameEnum.BBINFISH.getGameType());
                        bbin.setGameType(ThirdGameEnum.BBINFISH.getGameType());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        bbin.setFeeConvertUrl(convertUrl.toString());
                        bbin.setConvertPlatformType(PlatformType.BBIN.getValue());
//                        bbin.setForwardUrl("/third/forwardBbin2.do?gameType=fisharea&mobile=1&isApp=1");
                        bbin.setForwardUrl("/third/forwardBbin2.do?gameType=bigfish_38001&mobile=1&isApp=1");
                        bbin.setType("6");
                        bbin.setIsListGame(1);
                        results.add(bbin);
                    }
                    // CQ9捕鱼
                    if (platformSwitch.isCq9()) {
                        OtherPlayData cq9 = new OtherPlayData();
                        cq9.setTitle(I18nTool.getMessage("admin.nine.fish.game"));
                        if (isV3) {
                            cq9.setImgUrl("/common/third/images/v3/cq9-fish-bg2.png");
                        } else {
                            cq9.setImgUrl("/common/third/images/fish/new/cq9-fish-bg.png");
                        }
                        cq9.setDataCode(code);
                        cq9.setPlayCode(ThirdGameEnum.CQ9FISH.getGameType());
                        cq9.setGameType(ThirdGameEnum.CQ9FISH.getGameType());
                        cq9.setConvertPlatformType(PlatformType.CQ9.getValue());
                        StringBuilder convertUrl = new StringBuilder(baseConvertUrl);
                        cq9.setFeeConvertUrl(convertUrl.toString());
                        cq9.setForwardUrl("/third/forwardCq9.do?mobile=1&isApp=1");
                        cq9.setType("6");
                        cq9.setIsListGame(1);
                        results.add(cq9);
                    }
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger("aa").error("eeeeeeeeee = ", e);
        }
        return results;
    }

    /**
     * 获取请求Body
     *
     * @param request 请求句柄
     * @return      
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static List<GameItemResult> gameDatas(HttpServletRequest request, String gameType, Integer pageSize, Integer pageIndex) {
        if (StringUtils.isEmpty(gameType)) {
            throw new ParamException(BaseI18nCode.hasnotParamterGameTypeError);
        }
        List<GameItemResult> gameItemResults = new ArrayList<>();

        ThirdGameEnum anEnum = ThirdGameEnum.getEnum(gameType);
        if (anEnum == null) {
            throw new ParamException(BaseI18nCode.hasnotParamterGameTypeError);
        }

        // YG彩票 和 IYG国际彩 单独处理
        if (ThirdGameEnum.YG.name().equalsIgnoreCase(anEnum.getGameType())
                || ThirdGameEnum.IYG.name().equalsIgnoreCase(anEnum.getGameType())) {
            GameService gameService = SpringUtils.getBean(GameService.class);
            List<AppThirdGameVo> lotGameList = gameService.getLotGames(3, null, null, null);
            for (AppThirdGameVo vo : lotGameList) {
                // lotGameList 将 YG 和 IYG 一起返回，需过滤
                if (!vo.getGameType().equals(anEnum.getGameType())) {
                    continue;
                }
                GameItemResult result = new GameItemResult();
                result.setImg(vo.getImgUrl());
                result.setName(vo.getName());
                result.setType(vo.getLotCode());
                result.setFinalRelatveUrl(vo.getForwardUrl());
                result.setButtonImagePath(vo.getImgUrl());
                result.setDisplayName(vo.getName());
                result.setLapisId(vo.getLotCode());
                result.setSingle(0);

                gameItemResults.add(result);
            }
            return gameItemResults;
        }

        String dir = request.getServletContext().getRealPath("/native/resources/games/") + "/";
        if (anEnum.getGameType().equalsIgnoreCase("pt")) {
            dir += "pt.json";
        } else if (anEnum.getGameType().equalsIgnoreCase("qt")) {
            dir += "qt.json";
        } else if (anEnum.getGameType().equalsIgnoreCase("mg")) {
            dir += "mg.json";
        } else if (anEnum.getGameType().equalsIgnoreCase("pp")) {
            dir += "pp.json";
        } else if (anEnum.getGameType().equalsIgnoreCase("evo")) {
            dir += "evo.json";
        } else if (anEnum.getGameType().equalsIgnoreCase("fg")) {
            dir += "fg.json";
        }else if (anEnum.getGameType().equalsIgnoreCase("jl")){
            dir += "jl.json";
        }else if (anEnum.getGameType().equalsIgnoreCase("tada")){
            dir += "tada.json";
        }else if (anEnum.getGameType().equalsIgnoreCase("bs")){
            dir += "bs.json";
        }else if (anEnum.getGameType().equalsIgnoreCase("pg")){
            dir += "pg.json";
        }else if (anEnum.getGameType().equalsIgnoreCase("jdb")){
            dir += "jdb.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.PPSPORT.getGameType())){
            dir += "ppSport.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.TYSBSPORT.getGameType())){
            dir += "tysbSport.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.PPLIVE.getGameType())){
            dir += "ppLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.EVOLIVE.getGameType())){
            dir += "evoLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AG.getGameType())){
            dir += "ag.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBINLIVE.getGameType())){
            dir += "bbinLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.MGLIVE.getGameType())){
            dir += "mgLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BGLIVE.getGameType())){
            dir += "bgLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.DGLIVE.getGameType())){
            dir += "dgLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.FBSPORT.getGameType())){
            dir += "fbSport.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AGSPORT.getGameType())){
            dir += "agSport.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AGLIVE.getGameType())) {
            dir += "agLive.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBIN.getGameType())){
            dir += "bbin.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.CQ9.getGameType())){
            dir += "cq9.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.CQ9FISH.getGameType())){
            dir += "cq9Fish.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBINFISH.getGameType())){
            dir += "bbinFish.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AWC.getGameType())){
            dir += "awc.json";
        }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.VDD.getGameType())) {
            dir += "vdd.json";
        }
        String gamesJson = "";
        try {
            gamesJson = readGamesJson(dir);
        } catch (Exception e) {
            e.printStackTrace();
            return gameItemResults;
        }
        gameItemResults = JSONArray.parseArray(gamesJson, GameItemResult.class);

        Integer gameSize = gameItemResults.size();
        if (pageIndex != null && pageSize != null) {
            if (gameSize > (pageSize * pageIndex)) {
                gameItemResults = gameItemResults.subList((pageIndex - 1) * pageSize, pageIndex * pageSize);
            } else {
                if (gameSize > 1){
//                    gameItemResults = gameItemResults.subList(pageSize * (pageIndex-1), gameSize - 1);
                    gameItemResults = gameItemResults.subList(pageSize * (pageIndex-1), gameSize);
                }
            }
        }
        for (int i = 0; i < gameItemResults.size(); i++) {
            GameItemResult result = gameItemResults.get(i);
            if (StringUtils.isEmpty(result.getButtonImagePath())) {
                result.setButtonImagePath(result.getImg());
            }
            if (StringUtils.isEmpty(result.getDisplayName())) {
                result.setDisplayName(result.getName());
            }
            if (StringUtils.isEmpty(result.getLapisId())) {
                result.setLapisId(result.getType());
            }

            String egamePathPreffix = "/common/third/images/egame/";
            String fishPathPreffix = "/common/third/images/fish/";
            String livePathPreffix = "/common/third/images/live/";
            String sportPathPreffix = "/common/third/images/sport/";

            StringBuilder finalUrl = new StringBuilder();
            if (anEnum.getGameType().equalsIgnoreCase("mg")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardMg.do").append("?gameType=").append(result.getLapisId()).append("&mobile=1").append("&isApp=1");
            } else if (anEnum.getGameType().equalsIgnoreCase("pt")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardPt.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            } else if (anEnum.getGameType().equalsIgnoreCase("evo")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardEvo.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            } else if (anEnum.getGameType().equalsIgnoreCase("fg")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardFg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("jl")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardJl.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("tada")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardTada.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("bs")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardBs.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("pg")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardPg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("jdb")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardJdb.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase("pp")) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardPP.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.PPSPORT.getGameType())) {
                result.setButtonImagePath(sportPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardPP.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AGSPORT.getGameType())) {
                result.setButtonImagePath(sportPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardAg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.TYSBSPORT.getGameType())) {
                result.setButtonImagePath(sportPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardTysb.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.PPLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardPP.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AGLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardAg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.FBSPORT.getGameType())) {
                result.setButtonImagePath(sportPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardFB.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.EVOLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardEvolution.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AWC.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardAwc.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.AG.getGameType())) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardAg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBINLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardBbin2.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.MGLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardMg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BGLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardBg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.DGLIVE.getGameType())) {
                result.setButtonImagePath(livePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardDg.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBIN.getGameType())) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardBbin2.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.CQ9.getGameType())) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardCq9.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.CQ9FISH.getGameType())) {
                result.setButtonImagePath(fishPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardCq9.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.BBINFISH.getGameType())) {
                result.setButtonImagePath(fishPathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardBbin2.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }else if (anEnum.getGameType().equalsIgnoreCase(ThirdGameEnum.VDD.getGameType())) {
                result.setButtonImagePath(egamePathPreffix + result.getButtonImagePath());
                finalUrl.append("/third/forwardVDD.do").append("?mobile=1&gameType=").append(result.getLapisId()).append("&isApp=1");
            }
            result.setFinalRelatveUrl(finalUrl.toString());
        }
        return gameItemResults;
    }

    /**
     * 游戏搜索点击游戏提交的type转换为足迹对应的游戏类型
     *
     * @param type
     * @return
     */
    public static Integer convertGameSearchResultTypeForFootprint(Integer type) {
        if (type == GameSearchResult.SPORT_GAME) {
            return AppGameFoot.SPORT;
        } else if (type == GameSearchResult.ZHENREN_GAME) {
            return AppGameFoot.ZHENREN;
        } else if (type == GameSearchResult.DIANZI_GAME) {
            return AppGameFoot.DIANZI;
        } else if (type == GameSearchResult.SPORT_GAME) {
            return AppGameFoot.SPORT;
        } else if (type == GameSearchResult.CAIPIAO_GAME) {
            return AppGameFoot.CAIPIAO;
        } else if (type == GameSearchResult.QIPAI_GAME) {
            return AppGameFoot.QIPAI;
        } else if (type == GameSearchResult.HONGBAO_GAME) {
            return AppGameFoot.HONGBAO;
        } else if (type == GameSearchResult.BUYU_GAME) {
            return AppGameFoot.BUYU;
        }
        return AppGameFoot.SELF_DEFINE;
    }

    public static Integer convertGameTabTypeToFootprintType(Integer type) {
        return convertGameSearchResultTypeForFootprint(type);
    }

    public static boolean vertifyLoginKey(String key, String userAgent) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        String[] keys = key.split(":");
        if (keys != null && keys.length > 0) {
            if (StringUtils.isNotEmpty(keys[0]) && !keys[0].equalsIgnoreCase(String.valueOf(SystemUtil.getStationId()))) {
                return false;
            }
            System.out.println("login native 登录 user-agent:" + userAgent);
            if (userAgent.startsWith("wap")) {
                return true;
            }
            if ((userAgent.indexOf("android/") != -1 || userAgent.indexOf("ios/") != -1)) {
                String appVersion = userAgent.substring(userAgent.indexOf("/") + 1);
                String keyVersion = keys[1].substring(userAgent.indexOf("/") + 1);
                if (!appVersion.equalsIgnoreCase(keyVersion)) {// 判断登录时的app版本号是否与请示头中的一致
                    return false;
                }
            }
            if (userAgent.indexOf("android/") != -1) {
                System.out.println("login phone model in login ==== " + keys[2]);
                if (StringUtils.isEmpty(keys[2])) {
                    return false;
                }
                if (!keys[2].startsWith("ANDROID")) {
                    return false;
                }
            } else if (userAgent.indexOf("ios/") != -1) {
                System.out.println("login phone model in login ==== " + keys[2]);
                if (StringUtils.isEmpty(keys[2])) {
                    return false;
                }
                if (!keys[2].startsWith("iPhone")) {
                    return false;
                }
            }
        }
        return true;
    }

}
