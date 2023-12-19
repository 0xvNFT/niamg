package com.play.web.vcode;

import java.awt.Font;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.play.cache.redis.RedisAPI;
import com.play.core.StationConfigEnum;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.var.StationType;
import com.play.web.var.SystemUtil;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.SpecCaptcha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifyCodeUtil {

	// 验证码KEY
	public static final String VERIFY_CODE_LOGIN_KEY = "verify_code_login_";
	public static final String VERIFY_CODE_LINE_KEY = "verify_line_login_";
	public static final String VERIFY_CODE_REGISTER_KEY = "verify_code_reg_";
	public static final String VERIFY_CODE_SEND_EMAIL_KEY = "verify_code_email_";
	public static final String VERIFY_CODE_SEND_SMS_KEY = "verify_code_sms_";
	public static final String VERIFY_CODE_LOGIN_EMAIL_KEY = "verify_code_email_login";
	public static final String VERIFY_CODE_REGISTER_EMAIL_KEY = "verify_code_email_register";
	public static final String VERIFY_CODE_LOGIN_SMS_KEY = "verify_code_sms_login";
	public static final String VERIFY_CODE_REGISTER_SMS_KEY = "verify_code_sms_register";
	// 验证码过期时间:5分钟
	public static int VERIFY_CODE_KEY_TIME_OUT = 120;

	// 验证码图片的宽度。
	private static int width = 80;
	// 验证码图片的高度。
	private static int height = 30;
	// 验证码字符个数
	private static int codeCount = 4;

	public static String createVerifyCode(String keyPre, StationConfigEnum sc)
			throws ServletException, java.io.IOException {
		String type = null;
		if (sc != null) {
			type = StationConfigUtil.get(SystemUtil.getStationId(), sc);
		}
		if (StringUtils.isEmpty(type)) {
			type = "a";
		}
		HttpServletResponse resp = ServletUtils.getResponse();
		String key = getCacheKey(keyPre);
		String c = RedisAPI.getCache(key);
		switch (type) {
		case "a1":// 5字符
			c = DefaultVerifyCode.createVerifyJPGCode(c, width, height, 5, false, false, false);
			break;
		case "b":// 纯数字
			c = DefaultVerifyCode.createVerifyJPGCode(c, width, height, 4, false, false, true);
			break;
		case "b1":// 5纯数字
			c = DefaultVerifyCode.createVerifyJPGCode(c, width, height, 5, false, false, true);
			break;
		case "c":// 纯数字扭曲
			// 三个参数分别为宽、高、位数
			SpecCaptcha specCaptcha = new SpecCaptcha(80, 35, 4);
			// 设置字体
			specCaptcha.setFont(new Font("Verdana", Font.ITALIC | Font.PLAIN, 24)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = specCaptcha.text().toLowerCase();
			specCaptcha.out(resp.getOutputStream());
			break;
		case "c1":// 5纯数字扭曲
			// 三个参数分别为宽、高、位数
			specCaptcha = new SpecCaptcha(80, 35, 5);
			// 设置字体
			specCaptcha.setFont(new Font("Verdana", Font.ITALIC | Font.PLAIN, 24)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = specCaptcha.text().toLowerCase();
			specCaptcha.out(resp.getOutputStream());
			break;
		case "d":// 扭曲字符
			// 三个参数分别为宽、高、位数
			specCaptcha = new SpecCaptcha(80, 35, 4);
			// 设置字体
			specCaptcha.setFont(new Font("Verdana", Font.ITALIC | Font.PLAIN, 24)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = specCaptcha.text().toLowerCase();
			specCaptcha.out(resp.getOutputStream());
			break;
		case "d1":// 扭曲5英文
			// 三个参数分别为宽、高、位数
			specCaptcha = new SpecCaptcha(80, 35, 5);
			// 设置字体
			specCaptcha.setFont(new Font("Verdana", Font.ITALIC | Font.PLAIN, 24)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			specCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = specCaptcha.text().toLowerCase();
			specCaptcha.out(resp.getOutputStream());
			break;
		case "zz":// 普通中文
			// 三个参数分别为宽、高、位数
			ChineseCaptcha chineseCaptcha = new ChineseCaptcha(80, 35, 4);
			// 设置字体
			chineseCaptcha.setFont(new Font("SimSun", Font.ITALIC | Font.PLAIN, 20)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			chineseCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = chineseCaptcha.text().toLowerCase();
			chineseCaptcha.out(resp.getOutputStream());
			break;
		case "e":// 扭曲中文
			// 三个参数分别为宽、高、位数
			chineseCaptcha = new ChineseCaptcha(80, 35, 5);
			// 设置字体
			chineseCaptcha.setFont(new Font("SimSun", Font.ITALIC | Font.PLAIN, 20)); // 有默认字体，可以不用设置
			// 设置类型，纯数字、纯字母、字母数字混合
			chineseCaptcha.setCharType(com.wf.captcha.base.Captcha.TYPE_ONLY_NUMBER);
			c = chineseCaptcha.text().toLowerCase();
			chineseCaptcha.out(resp.getOutputStream());
			break;
//		case "e1":// 扭曲中文1
//			c = KaptchaVerifyCode.createChinese1VerifyCode(c, resp);
//			break;
		case "f":// gif英文
			GifCaptcha g = new GifCaptcha(width, height, 4);
			if (StringUtils.isNotEmpty(c)) {
				g.setRandStrs(c.toCharArray());
			}
			g.out(resp.getOutputStream());
			c = g.text();
			break;
		case "f1":// gif5英文
			g = new GifCaptcha(width, height, 5);
			if (StringUtils.isNotEmpty(c)) {
				g.setRandStrs(c.toCharArray());
			}
			g.out(resp.getOutputStream());
			c = g.text();
			break;
		case "g":// gif中文
			HanZiGifCaptcha hzg = new HanZiGifCaptcha(width, height, codeCount, normalFonts);
			if (StringUtils.isNotEmpty(c)) {
				hzg.setRandStrs(c.split(""));
			}
			hzg.out(resp.getOutputStream());
			c = hzg.text();
			break;
		case "g1":// gif中文1
			hzg = new HanZiGifCaptcha(width, height, codeCount, hardFonts);
			if (StringUtils.isNotEmpty(c)) {
				hzg.setRandStrs(c.split(""));
			}
			hzg.out(resp.getOutputStream());
			c = hzg.text();
			break;
		default:
			c = DefaultVerifyCode.createVerifyJPGCode(c, width, height, codeCount, false, false, false);
		}

		RedisAPI.addCache(key, c, VERIFY_CODE_KEY_TIME_OUT);
		return c;
	}

	private static Font[] normalFonts = new Font[] { new Font("SimHei", Font.ITALIC | Font.BOLD, 20),
			new Font("SimSun", Font.ITALIC | Font.BOLD, 20) };

	private static Font[] hardFonts = new Font[] { new Font("AaStrawberrySister", Font.ITALIC | Font.BOLD, 20),
			new Font("AaMissLemon", Font.ITALIC | Font.BOLD, 20),
			new Font("woziku-mmxysm-CN6967", Font.ITALIC | Font.BOLD, 20), new Font("JST", Font.ITALIC | Font.BOLD, 20),
			new Font("STHupo", Font.ITALIC | Font.BOLD, 20),
			new Font("ZoomlaYingXing-A024", Font.ITALIC | Font.BOLD, 20),
			new Font("STXingkai", Font.ITALIC | Font.BOLD, 20) };

	private static String getCacheKey(String keyPre) {
		HttpSession session = ServletUtils.getSession();
		String sessionId = session.getId();
		StationType t = SystemUtil.getStationType();
		switch (t) {
		case MANAGER:
			return keyPre + "manager_" + sessionId;
		case ADMIN:
			return keyPre + "admin_" + sessionId;
		case PARTNER:
			return keyPre + "partner_" + sessionId;
		case AGENT:
			return keyPre + "agent_" + sessionId;
		default:
			return keyPre + "member_" + sessionId;
		}
	}

	public static String getVerifyCode(String keyPre) {
		return RedisAPI.getCache(getCacheKey(keyPre));
	}

	public static void validateCode(String keyPre, String code) {
		String key = getCacheKey(keyPre);
		String code1 = RedisAPI.getAndDel(key);
		if (StringUtils.isEmpty(code) || !code.equalsIgnoreCase(code1)) {
			throw new BaseException(BaseI18nCode.captchaError);
		}
	}

	public static void validateLine(String keyPre, String code) {
		String key = getCacheKey(keyPre);
		String code1 = RedisAPI.getAndDel(key);
		if (code != null && code.equals("")) {
			throw new BaseException(BaseI18nCode.requiredLineError);
		}
	}
}
