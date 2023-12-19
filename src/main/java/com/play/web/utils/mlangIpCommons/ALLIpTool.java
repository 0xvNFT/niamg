package com.play.web.utils.mlangIpCommons;


import com.play.spring.config.i18n.I18nTool;

public class ALLIpTool {

	public static String getCountryName(String countryCode) {
		String countryName = "";
		switch (countryCode) {
			case "CN":
				countryName = I18nTool.getMessage("admin.Chinesemainland");
				break;
			case "HK":
				countryName = I18nTool.getMessage("admin.HongKong,China");
				break;
			case "TW":
				countryName = I18nTool.getMessage("admin.Taiwan,China");
				break;
			case "PH":
				countryName = I18nTool.getMessage("admin.Philippines");
				break;
			case "KH":
				countryName = I18nTool.getMessage("admin.Cambodia");
				break;
			case "MY":
				countryName = I18nTool.getMessage("admin.Malaysia");
				break;
			case "TH":
				countryName = I18nTool.getMessage("admin.Thailand");
				break;
			case "MM":
				countryName = I18nTool.getMessage("admin.Burma");
				break;
			case "LA":
				countryName = I18nTool.getMessage("admin.Laos");
				break;
			case "BD":
				countryName = I18nTool.getMessage("admin.Bangladesh");
				break;
			case "IR":
				countryName = I18nTool.getMessage("admin.Iran");
				break;
			case "MX":
				countryName = I18nTool.getMessage("admin.Mexico");
				break;
			case "FR":
				countryName = I18nTool.getMessage("admin.France");
				break;
			case "BY":
				countryName = I18nTool.getMessage("admin.Belarus");
				break;
			case "SG":
				countryName = I18nTool.getMessage("admin.Singapore");
				break;
			case "VN":
				countryName = I18nTool.getMessage("admin.VietNam");
				break;
			case "IE":
				countryName = I18nTool.getMessage("admin.Ireland");
				break;
			case "KR":
				countryName = I18nTool.getMessage("admin.Korea");
				break;
			case "DE":
				countryName = I18nTool.getMessage("admin.Germany");
				break;
			case "GN":
				countryName = I18nTool.getMessage("admin.Guinea");
				break;
			case "LB":
				countryName = I18nTool.getMessage("admin.Lebanon");
				break;
			case "UA":
				countryName = I18nTool.getMessage("admin.Ukraine");
				break;
			case "LV":
				countryName = I18nTool.getMessage("admin.Latvia");
				break;
			case "RU":
				countryName = I18nTool.getMessage("admin.Russia");
				break;
			case "US":
				countryName = I18nTool.getMessage("admin.UnitedStates");
				break;
			case "SO":
				countryName = I18nTool.getMessage("admin.Somalia");
				break;
			case "BR":
				countryName = I18nTool.getMessage("admin.Brazil");
				break;
			case "ID":
				countryName = I18nTool.getMessage("admin.Indonesia");
				break;
			case "JP":
				countryName = I18nTool.getMessage("admin.Japan");
				break;
			case "IN":
				countryName = I18nTool.getMessage("admin.India");
				break;
			case "SE":
				countryName = I18nTool.getMessage("admin.Sweden");
				break;
			case "NL":
				countryName = I18nTool.getMessage("admin.Netherlands");
				break;
			case "TR":
				countryName = I18nTool.getMessage("admin.Turkey");
				break;
			case "EG":
				countryName = I18nTool.getMessage("admin.Egypt");
				break;
			case "VE":
				countryName = I18nTool.getMessage("admin.Venezuela");
				break;
			case "KZ":
				countryName = I18nTool.getMessage("admin.Kazakhstan");
				break;
			case "CO":
				countryName = I18nTool.getMessage("admin.Colombia");
				break;
			case "UY":
				countryName = I18nTool.getMessage("admin.Uruguay");
				break;
			case "BW":
				countryName = I18nTool.getMessage("admin.Botswana");
				break;
			case "ZA":
				countryName = I18nTool.getMessage("admin.SouthAfrica");
				break;
			case "DO":
				countryName = I18nTool.getMessage("admin.Dominican");
				break;
			case "NG":
				countryName = I18nTool.getMessage("admin.Nigeria");
				break;
			case "PE":
				countryName = I18nTool.getMessage("admin.Peru");
				break;
			case "LY":
				countryName = I18nTool.getMessage("admin.Libya");
				break;
			case "BA":
				countryName = I18nTool.getMessage("admin.Bosnia");
				break;
			case "CZ":
				countryName = I18nTool.getMessage("admin.Czech");
				break;
			case "IT":
				countryName = I18nTool.getMessage("admin.Italy");
				break;
			case "AE":
				countryName = I18nTool.getMessage("admin.UAE");
				break;
			case "SL":
				countryName = I18nTool.getMessage("admin.Sierraleone");
				break;
			case "EC":
				countryName = I18nTool.getMessage("admin.Ecuador");
				break;
			case "CM":
				countryName = I18nTool.getMessage("admin.Cameroon");
				break;
			case "DJ":
				countryName = I18nTool.getMessage("admin.Djibouti");
				break;
			case "PK":
				countryName = I18nTool.getMessage("admin.Pakistan");
				break;
			case "CR":
				countryName = I18nTool.getMessage("admin.CostaRica");
				break;
			case "UG":
				countryName = I18nTool.getMessage("admin.Uganda");
				break;
			case "GB":
				countryName = I18nTool.getMessage("admin.UnitedKingdom");
				break;
			case "PL":
				countryName = I18nTool.getMessage("admin.Poland");
				break;
			case "AL":
				countryName = I18nTool.getMessage("admin.Albania");
				break;
			case "PA":
				countryName = I18nTool.getMessage("admin.Panama");
				break;
			case "AM":
				countryName = I18nTool.getMessage("admin.Armenia");
				break;
			case "CA":
				countryName = I18nTool.getMessage("admin.Canada");
				break;
			case "GE":
				countryName = I18nTool.getMessage("admin.Georgia");
				break;
			case "IQ":
				countryName = I18nTool.getMessage("admin.Iraq");
				break;
			case "FI":
				countryName = I18nTool.getMessage("admin.Finland");
				break;
			case "SK":
				countryName = I18nTool.getMessage("admin.Slovakia");
				break;
			case "GR":
				countryName = I18nTool.getMessage("admin.Greece");
				break;
			case "AR":
				countryName = I18nTool.getMessage("admin.Argentina");
				break;
			case "CL":
				countryName = I18nTool.getMessage("admin.Chile");
				break;
			case "PS":
				countryName = I18nTool.getMessage("admin.Palestinian");
				break;
			case "ES":
				countryName = I18nTool.getMessage("admin.Spain");
				break;
			case "NI":
				countryName = I18nTool.getMessage("admin.Nicaragua");
				break;
			case "GT":
				countryName = I18nTool.getMessage("admin.Guatemala");
				break;
			case "KE":
				countryName = I18nTool.getMessage("admin.Kenya");
				break;
			case "TT":
				countryName = I18nTool.getMessage("admin.TrinidadandTobago");
				break;
			case "RO":
				countryName = I18nTool.getMessage("admin.Romanian");
				break;
			case "ZW":
				countryName = I18nTool.getMessage("admin.Zimbabwe");
				break;
			case "HN":
				countryName = I18nTool.getMessage("admin.Honduras");
				break;
			case "RS":
				countryName = I18nTool.getMessage("admin.Serbia");
				break;
			case "YE":
				countryName = I18nTool.getMessage("admin.Yemen");
				break;
			case "NP":
				countryName = I18nTool.getMessage("admin.Nepal");
				break;
			case "IL":
				countryName = I18nTool.getMessage("admin.Israel");
				break;
			case "MN":
				countryName = I18nTool.getMessage("admin.Mongolia");
				break;
			case "MG":
				countryName = I18nTool.getMessage("admin.Madagascar");
				break;
			case "BO":
				countryName = I18nTool.getMessage("admin.Bolivia");
				break;
			case "MW":
				countryName = I18nTool.getMessage("admin.Malawi");
				break;
			case "OM":
				countryName = I18nTool.getMessage("admin.Oman");
				break;
			case "HU":
				countryName = I18nTool.getMessage("admin.Hungary");
				break;
			case "MZ":
				countryName = I18nTool.getMessage("admin.Mozambique");
				break;
			default:
				countryName = "";
				break;
		}
		return countryName;
	}

	/**
	 * 判断字段串是否是有效的IPV4地址。
	 *
	 * @return true-IPV4地址
	 */
	public static boolean isValidIpV4Address(String value) {

		int periods = 0;
		int i;
		int length = value.length();

		if (length > 15) {
			return false;
		}
		char c;
		StringBuilder word = new StringBuilder();
		for (i = 0; i < length; i++) {
			c = value.charAt(i);
			if (c == '.') {
				periods++;
				if (periods > 3) {
					return false;
				}
				if (word.length() == 0) {
					return false;
				}
				if (Integer.parseInt(word.toString()) > 255) {
					return false;
				}
				word.delete(0, word.length());
			} else if (!Character.isDigit(c)) {
				return false;
			} else {
				if (word.length() > 2) {
					return false;
				}
				word.append(c);
			}
		}

		if (word.length() == 0 || Integer.parseInt(word.toString()) > 255) {
			return false;
		}

		return periods == 3;
	}

	public static long ipToLong(String ip) {
		if (ip == null || ip.isEmpty()) {
			return 0;
		}
		String[] sips = ip.split("\\.");
		long ips = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
		}
		return ips;
	}
}
