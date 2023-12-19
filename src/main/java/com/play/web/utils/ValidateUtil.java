package com.play.web.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.play.common.utils.PayUtils;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18ErrorCode;

/**
 * 校验器：利用正则表达式校验邮箱、手机号等
 *
 * @author liujiduo
 */
public class ValidateUtil {
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-z0-9]{5,11}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[\\S]{6,30}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String DRAW_PASSWORD = "^[a-zA-Z0-9]{4,20}$";

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_PHONE_NUMBER = "^(\\+[0-9]{1,3})?( )?[0-9]{4,15}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\\u4E00-\\u9FA5]{0,}(·[\\u4E00-\\u9FA5]{0,})*$";

    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_FINGERPRINT = "^[0-9a-zA-Z]{32}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d{2}|[1-9]?\\d)){3}$";

    /**
     * 正则表达式：数字
     */
    public static final String REGEX_NUMBER = "^\\d+$";

    /**
     * 正则表达式：银行卡
     */
    public static final String REGEX_BANK_ACCOUNT = "^\\d{7,25}$";

    /**
     * 正则表达式：真实姓名
     */
    // 中文名，1个以上汉字
    public static final String REAL_NAME_CN1 = "^[\\u4E00-\\u9FA5]+(·[\\u4E00-\\u9FA5]+)*$";
    // 中文名，2-4个汉字
    public static final String REAL_NAME_CN2 = "^[\\u4E00-\\u9FA5]{2,4}$";
    // 英文名， 1-2个单词
    public static final String REAL_NAME_EN1 = "^([A-Za-z]+\\s)?[A-Za-z]+$";
    // 英文名， 2个以上单词（英文姓名可能包含连字符 "-" 或空格 " "，因此使用 "[ -]" 匹配这两种情况
    public static final String REAL_NAME_EN2 = "^[A-Za-z]+([ -][A-Za-z]+)+$";
    // 越南名，2个以上大写单词
    public static final String REAL_NAME_VI = "^[A-Z]+(\\s[A-Z]+)+$";

    /**
     * 推广码
     */
    public static final String PROMO_CODE = "^[0-9a-z]{5,50}$";
    /**
     * 脸书
     */
    public static final String FACEBOOK = "^[.-_@a-zA-Z0-9]{5,50}$";

    public static final String QQ = "^[0-9]{5,15}$";

    public static final String WECHAT = "^[-_a-zA-Z0-9]{2,20}$";

    public static final String LINE = "^[-_a-zA-Z0-9]{2,20}$";

    /**
     * 校微信号
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isWechat(String w) {
        if (StringUtils.isEmpty(w)) {
            return false;
        }
        return Pattern.matches(WECHAT, w);
    }

    /**
     * 校QQ
     *
     * @param qq
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isQq(String qq) {
        if (StringUtils.isEmpty(qq)) {
            return false;
        }
        return Pattern.matches(QQ, qq);
    }

    /**
     * 校验脸书
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isFacebook(String facebook) {
        if (StringUtils.isEmpty(facebook)) {
            return false;
        }
        return Pattern.matches(FACEBOOK, facebook);
    }


    public static boolean isLine(String line) {
        if (StringUtils.isEmpty(line)) {
            return false;
        }
        return Pattern.matches(LINE, line);
    }

    /**
     * 校验推广码
     *
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPromoCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        return Pattern.matches(PROMO_CODE, code);
    }

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验数字
     *
     * @param param
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isNumber(String param) {
        if (StringUtils.isEmpty(param)) {
            return false;
        }
        return Pattern.matches(REGEX_NUMBER, param);
    }

    /**
     * 校验登录密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验取款密码
     *
     * @param password
     * @return
     */
    public static boolean isDrawPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }
        return Pattern.matches(DRAW_PASSWORD, password);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPhoneNumber(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            return false;
        }
        return Pattern.matches(REGEX_PHONE_NUMBER, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        if (StringUtils.isEmpty(chinese)) {
            return false;
        }
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return false;
        }
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验是否真实姓名
     *
     * @param realName
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isRealName(String realName, String language) {
        if (StringUtils.isEmpty(realName)) {
            return false;
        }
        switch (language) {
            case "cn":
                return  Pattern.matches(REAL_NAME_CN2, realName);
            case "en":
            case "in":
            case "es":
            case "br":
            case "ms":
            case "id":
                return Pattern.matches(REAL_NAME_EN1, realName) || Pattern.matches(REAL_NAME_EN2, realName);
            case "vi":
                return  Pattern.matches(REAL_NAME_VI, realName);
            case "th":
            case "jp":
                //return false;
            default:
                return false;
        }
    }

    /**
     * 校验IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        if (StringUtils.isEmpty(ipAddr)) {
            return false;
        }
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }

    // 防止xss攻击
    public static boolean isUnsafeChar(String content) {
        if (content == null) {
            return false;
        }
        if (content.indexOf(">") > -1 || content.indexOf("<") > -1) {
            return true;
        }
        return false;
    }


    public static boolean isBigDecimal(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        int i = (chars[0] == '-') ? 1 : 0;
        if (i == sz) {
            return false;
        }
        if (chars[i] == '.') {
            return false;//除了负号，第一位不能为'小数点'
        }
        boolean radixPoint = false;
        for (; i < sz; i++) {
            if (chars[i] == '.') {
                if (radixPoint) {
                    return false;
                }
                radixPoint = true;
            } else if (!(chars[i] >= '0' && chars[i] <= '9')) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
         String username = "TÀI@^ XỈU PHÚT";
         username = username.replace(" ","");
        System.out.println(ValidateUtil.isRealName(username, "vi"));
//         System.out.println(ValidateUtil.isChinese(username));
    }

    /*
     * 根据〖中华人民共和国国家标准 GB
     * 11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。排列顺序从左至右依次为：
     * 六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。 地址码表示编码对象常住户口所在县(市、旗、区)的行政区划代码。
     * 出生日期码表示编码对象出生的年、月、日，其中年份用四位数字表示，年、月、日之间不用分隔符。
     * 顺序码表示同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。顺序码的奇数分给男性，偶数分给女性。
     * 校验码是根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。
     *
     * 出生日期计算方法。 15位的身份证编码首先把出生年扩展为4位，简单的就是增加一个19或18,这样就包含了所有1800-1999年出生的人;
     * 2000年后出生的肯定都是18位的了没有这个烦恼，至于1800年前出生的,那啥那时应该还没身份证号这个东东，⊙﹏⊙b汗... 下面是正则表达式:
     * 出生日期1800-2099 (18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01]) 身份证正则表达式
     * /^\d{6}(18|19|20)?\d{2}(0[1-9]|1[12])(0[1-9]|[12]\d|3[01])\d{3}(\d|X)$/i
     * 15位校验规则 6位地址编码+6位出生日期+3位顺序号 18位校验规则 6位地址编码+8位出生日期+3位顺序号+1位校验位
     *
     * 校验位规则 公式:∑(ai×Wi)(mod 11)……………………………………(1) 公式(1)中：
     * i----表示号码字符从由至左包括校验码在内的位置序号； ai----表示第i位置上的号码字符值；
     * Wi----示第i位置上的加权因子，其数值依据公式Wi=2^(n-1）(mod 11)计算得出。 i 18 17 16 15 14 13 12 11 10
     * 9 8 7 6 5 4 3 2 1 Wi 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 1
     *
     */
    // 身份证号合法性验证
    // 支持15位和18位身份证号
    // 支持地址编码、出生日期、校验位验证
    private static final Set<String> citySet = new HashSet<>();
    private static final int[] factor = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final String[] parity = new String[]{"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    static {
        citySet.add("11");
        citySet.add("12");
        citySet.add("13");
        citySet.add("14");
        citySet.add("15");
        citySet.add("21");
        citySet.add("22");
        citySet.add("23");
        citySet.add("31");
        citySet.add("32");
        citySet.add("33");
        citySet.add("34");
        citySet.add("35");
        citySet.add("36");
        citySet.add("37");
        citySet.add("41");
        citySet.add("42");
        citySet.add("43");
        citySet.add("44");
        citySet.add("45");
        citySet.add("46");
        citySet.add("50");
        citySet.add("51");
        citySet.add("52");
        citySet.add("53");
        citySet.add("54");
        citySet.add("61");
        citySet.add("62");
        citySet.add("63");
        citySet.add("64");
        citySet.add("65");
        citySet.add("71");
        citySet.add("81");
        citySet.add("82");
        citySet.add("91");
    }

    public static boolean isIDCard2(String idCard) {
        if (StringUtils.length(idCard) != 18) {
            return false;
        }
        if (!idCard.matches("^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[12])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X)$")) {
            return false;
        }
        if (!citySet.contains(idCard.substring(0, 2))) {
            return false;
        }
        // 18位身份证需要验证最后一位校验位
        int sum = 0;
        int ai = 0;
        int wi = 0;
        for (int i = 0; i < 17; i++) {
            ai = NumberUtils.toInt(idCard.substring(i, i + 1));
            wi = factor[i];
            sum += ai * wi;
        }
        if (!parity[sum % 11].equals(idCard.substring(17, 18))) {
            return false;
        }
        return true;
    }


    public static boolean isBankCardNo(String cardNo) {
		return Pattern.matches(REGEX_BANK_ACCOUNT, cardNo) || PayUtils.isValidBrazilPixAccount(cardNo);
	}

    /**
     * 校验字符串是否在规定字符数内
     *
     * @param str
     * @param maxLength
     * @return
     */
    public static boolean isOver(String str, Integer maxLength) {
        try {
            int length = str.getBytes("GBK").length;
            if (length > maxLength) {
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String isCodeError(String code, String args) {
        if (Objects.nonNull(args)) {
            for (BaseI18ErrorCode i : BaseI18ErrorCode.values()) {
                if (Objects.equals(code, i.getCode())) {
                    //return 	I18nTool.getMessage(BaseI18ErrorCode.serverIpNotOnWhite,new Object[] {args});
                    return I18nTool.getMessage(i.getCode(), new Object[]{args});
                }
            }
        } else {
            for (BaseI18ErrorCode i : BaseI18ErrorCode.values()) {
                if (Objects.equals(code, i.getCode())) {
                    return I18nTool.getMessage(i.getCode(), i.getMessage());
                }
            }
        }
        return I18nTool.getMessage(code, args);
    }

    public static String isCodeErrorGetCode(String code, String args) {
        if (Objects.nonNull(args)) {
            for (BaseI18ErrorCode i : BaseI18ErrorCode.values()) {
                if (Objects.equals(code, i.getCode())) {
                    return i.getCode();
                }
            }
        } else {
            for (BaseI18ErrorCode i : BaseI18ErrorCode.values()) {
                if (Objects.equals(code, i.getCode())) {
                    return i.getCode();
                }
            }
        }
        return code;
    }

    public static boolean isFingerprint(String ipAddr) {
        return Pattern.matches(REGEX_FINGERPRINT, ipAddr);
    }
}
