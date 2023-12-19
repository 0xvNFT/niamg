package com.play.web.utils.export;
/**
 * 数据导出成csv
 * @author macair
 *
 */

import com.play.web.exception.BaseException;
import com.play.web.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportToCvsUtil {
	private static Logger logger = LoggerFactory.getLogger(ExportToCvsUtil.class);

	/** CSV文件列分隔符 */
	private static final String CSV_COLUMN_SEPARATOR = ",";

	/** CSV文件列分隔符 */
	private static final String CSV_RN = "\r\n";

	/**
	 * 导数据
	 * 
	 * @param title
	 *            显示的导出表的标题,文件名称
	 * @param colNames
	 *            导出表的列名
	 * @param dataList
	 *            数据列表
	 */
	public static void export(String title, String[] colNames, List<Object[]> dataList) {
		StringBuilder sb = new StringBuilder();
		// 完成数据csv文件的封装
		// 输出列头
		fillHead(colNames, sb);
		fillData(dataList, sb);
		write(sb.toString(), title);
	}

	private static void fillHead(String[] colNames, StringBuilder sb) {
		for (String colName : colNames) {
			sb.append(csvHandlerStr(colName)).append(CSV_COLUMN_SEPARATOR);
		}
		sb.append(CSV_RN);
	}

	private static void fillData(List<Object[]> dataList, StringBuilder sb) {
		if (null != dataList) { // 输出数据
			for (Object[] datas : dataList) {
				for (Object data : datas) {
					if (data != null) {
						sb.append(csvHandlerStr(data.toString()));
					}
					sb.append(CSV_COLUMN_SEPARATOR);
				}
				sb.append(CSV_RN);
			}
		}
	}

	/**
	 * 导出到浏览器
	 * 
	 * @param workbook
	 * @param title
	 * @throws IOException
	 */
	public static void write(String content, String title) {
		try {
			title = new String(title.getBytes("UTF-8"), "iso8859-1") + "-"
					+ String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".csv";
			HttpServletResponse response = ServletUtils.getResponse();
			OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "GBK");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=30");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + title + "\"");
			//response.setCharacterEncoding("UTF-8");
			// 要输出的内容
			osw.write(content);
			osw.flush();
			osw.close();
		} catch (Exception e) {
			logger.error("写数据异常", e);
			throw new BaseException(e);
		}
	}

	/**
	 * 方法名称: csvHandlerStr</br>
	 * 方法描述: 处理包含逗号，或者双引号的字段</br>
	 * 方法参数: @param forecastName 方法参数: @return </br>
	 * 返回类型: String</br>
	 * 抛出异常:</br>
	 */
	private static String csvHandlerStr(String str) {
		// csv格式如果有逗号，整体用双引号括起来；如果里面还有双引号就替换成两个双引号，这样导出来的格式就不会有问题了
		// 如果有逗号
		if (str.contains(",")) {
			// 如果还有双引号，先将双引号转义，避免两边加了双引号后转义错误
			if (str.contains("\"")) {
				str = str.replaceAll("\"", "\"\"");
			}
			// 在将逗号转义
			str = "\"" + str + "\"";
		}
		return str;

	}
}
