package com.play.spring.config.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I18nJsFileInit {
	static private Logger logger = LoggerFactory.getLogger(I18nJsFileInit.class);

	public static void main(String[] args) throws Exception {
		Map<String, I18nLanguageText> map = getMap("messages/需要翻译1(英,越,馬,印尼,泰,印度).xlsx");
		Map<String, I18nLanguageText> map2 = getMap("messages/需要翻译2(英,越,馬,印尼,泰).xlsx");
		Map<String, I18nLanguageText> map3 = getMap("messages/需要翻译3(英,越,馬,印尼,泰).xlsx");
		writeJsFile(map, map2, map3);
	}
	//
	// private static Set<String> readFile(String fileName) throws Exception {
	// String content = FileUtils.readFileToString(
	// new File(I18nJsFileInit.class.getResource("/").getPath() + fileName),
	// "UTF-8");
	// Set<String> set = new HashSet<>();
	// String[] lines = content.split("\n");
	// for (String s : lines) {
	// try {
	// if (StringUtils.isEmpty(s)) {
	// continue;
	// }
	// set.add(s);
	// } catch (Exception e) {
	// logger.error("错误22" + s, e);
	// }
	// }
	// return set;
	// }

	private static void writeJsFile(Map<String, I18nLanguageText> map, Map<String, I18nLanguageText> map2,
			Map<String, I18nLanguageText> map3) throws Exception {
		List<I18nLanguageText> list = readTextJsList();
		I18nLanguageText t = null;
		String v = null;
		StringBuilder cn = new StringBuilder("var Admin={");
		StringBuilder en = new StringBuilder("var Admin={");
		StringBuilder th = new StringBuilder("var Admin={");
		StringBuilder vn = new StringBuilder("var Admin={");
		StringBuilder my = new StringBuilder("var Admin={");
		StringBuilder in = new StringBuilder("var Admin={");
		StringBuilder yinDu = new StringBuilder("var Admin={");
		StringBuilder es = new StringBuilder("var Admin={");
		StringBuilder jp = new StringBuilder("var Admin={");
		String key = null;
		for (I18nLanguageText text : list) {
			key = text.getKey();
			v = text.getCn();
			t = map.get(v);
			if (t != null) {
				append(key, t, cn, en, th, vn, my, in, yinDu, es, jp);
			} else {
				if (map2.containsKey(v)) {
					t = map2.get(v);
					append(key, t, cn, en, th, vn, my, in, yinDu, es, jp);
					continue;
				}
				if (map3.containsKey(v)) {
					t = map3.get(v);
					append(key, t, cn, en, th, vn, my, in, yinDu, es, jp);
					continue;
				}
				cn.append(key).append(":\"").append(v).append("\",\n");
				en.append(key).append(":\"").append(v).append("\",\n");
				th.append(key).append(":\"").append(v).append("\",\n");
				vn.append(key).append(":\"").append(v).append("\",\n");
				my.append(key).append(":\"").append(v).append("\",\n");
				in.append(key).append(":\"").append(v).append("\",\n");
				yinDu.append(key).append(":\"").append(v).append("\",\n");
				es.append(key).append(":\"").append(v).append("\",\n");
				jp.append(key).append(":\"").append(v).append("\",\n");
			}
		}
		writeFile(new File("/Users/macair/Documents/props/cn.js"), cn.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/en.js"), en.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/vi.js"), vn.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/th.js"), th.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/ms.js"), my.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/id.js"), in.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/in.js"), yinDu.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/es.js"), es.append("}").toString());
		writeFile(new File("/Users/macair/Documents/props/jp.js"), jp.append("}").toString());
	}

	public static void append(String key, I18nLanguageText t, StringBuilder cn, StringBuilder en, StringBuilder th,
			StringBuilder vn, StringBuilder my, StringBuilder in, StringBuilder yinDu, StringBuilder es, StringBuilder jp) {
		cn.append(key).append(":\"").append(t.getCn()).append("\",\n");
		en.append(key).append(":\"").append(t.getEn()).append("\",\n");
		th.append(key).append(":\"").append(t.getTha()).append("\",\n");
		vn.append(key).append(":\"").append(t.getVn()).append("\",\n");
		my.append(key).append(":\"").append(t.getMyr()).append("\",\n");
		in.append(key).append(":\"").append(t.getIdn()).append("\",\n");
		yinDu.append(key).append(":\"").append(t.getYinDu()).append("\",\n");
		es.append(key).append(":\"").append(t.getEs()).append("\",\n");
		jp.append(key).append(":\"").append(t.getEs()).append("\",\n");
	}
	
	private static void writeFile(File file, String content) throws Exception {
		FileUtils.write(file, content, "UTF-8");
	}

	private static Map<String, I18nLanguageText> getMap(String fileName) {
		Map<String, I18nLanguageText> map = new HashMap<>();
		try {
			List<I18nLanguageText> list = readExcel(I18nJsFileInit.class.getResource("/").getPath() + fileName);
			if (list != null) {
				for (I18nLanguageText t : list) {
					map.put(t.getCn(), t);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	private static List<I18nLanguageText> readTextJsList() throws Exception {
		String content = FileUtils.readFileToString(
				new File(I18nJsFileInit.class.getResource("/").getPath() + "messages/cn.js"), "UTF-8");
		List<I18nLanguageText> list = new ArrayList<>();
		I18nLanguageText t = null;
		String[] lines = content.split("\n");
		for (String s : lines) {
			try {
				if (StringUtils.isEmpty(s)) {
					continue;
				}
				String[] ol = s.split(":", 2);
				t = new I18nLanguageText();
				t.setKey(ol[0].trim());
				s = ol[1].trim();
				t.setCn(s.substring(1, s.length() - 2));
				list.add(t);
			} catch (Exception e) {
				logger.error("错误11" + s, e);
			}
		}
		return list;
	}

	public static List<I18nLanguageText> readExcel(String filepath) throws Exception {
		InputStream is = null;
		Workbook wb = null;

		List<I18nLanguageText> result = new ArrayList<>();// 对应excel文件
		try {
			is = new FileInputStream(filepath);
			wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(0);
			int rowSize = sheet.getLastRowNum() + 1;
		//	logger.error("rowSize=" + rowSize);
			Row row = null;
			I18nLanguageText t = null;
			for (int j = 1; j < rowSize; j++) {// 遍历行
				row = sheet.getRow(j);
				if (row == null) {// 略过空行
					continue;
				}
				t = new I18nLanguageText();
				t.setCn(getString(row.getCell(0)));
				t.setEn(getString(row.getCell(2)));
				t.setVn(getString(row.getCell(3)));
				t.setMyr(getString(row.getCell(4)));
				t.setIdn(getString(row.getCell(5)));
				t.setTha(getString(row.getCell(6)));
				t.setYinDu(getString(row.getCell(7)));
				t.setEs(getString(row.getCell(8)));
				t.setJp(getString(row.getCell(9)));
				result.add(t);
			}
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return result;
	}

	private static String getString(Cell cell) {
		if (cell == null)
			return "";
		cell.setCellType(CellType.STRING);
		return cell.getRichStringCellValue().getString().trim();
	}

	private static String getString2(Cell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case STRING:
			return cell.getRichStringCellValue().getString();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				if (cell.getDateCellValue() != null) {
					return com.play.common.utils.DateUtil.toDatetimeStr(cell.getDateCellValue());
				}
				return "";
			} else {
				return String.valueOf((int) cell.getNumericCellValue());
			}
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		default:
			return cell.getRichStringCellValue().getString();
		}
	}
}
