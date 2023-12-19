package com.play.web.utils.export;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;


/**
 * https://blog.csdn.net/lisen1987/article/details/56841121
 * 
 * @author macair
 *
 */
public class ExportData2ExcelSheetUtil {
	/**
	 * 每个文件的最大行数 超过请求按默认算
	 */
	public static final int MAXROWS = 50000;

	private int maxRow = MAXROWS;
	/**
	 * 用于数据查询
	 */
//	private JdbcTemplate jdbcTemplate;

	StringBuffer head = new StringBuffer("<?xml version=\"1.0\"?>")
			.append("<?mso-application progid=\"Excel.Sheet\"?> ")
			.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" ")
			.append("  xmlns:o=\"urn:schemas-microsoft-com:office:office\" ")
			.append("  xmlns:x=\"urn:schemas-microsoft-com:office:excel\" ")
			.append("  xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\" ")
			.append("  xmlns:html=\"http://www.w3.org/TR/REC-html40\">");

	StringBuffer foot = new StringBuffer("</Workbook>");

	StringBuffer sheetHead = new StringBuffer("<Worksheet ss:Name=\"sheet{0}\">").append("<Table>");
	StringBuffer sheetFoot = new StringBuffer("</Table>")
			.append("<WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">")
			.append("<ProtectObjects>False</ProtectObjects>").append("<ProtectScenarios>False</ProtectScenarios>")
			.append("</WorksheetOptions>").append("</Worksheet>");

//	public ExportData2ExcelSheetUtil(JdbcTemplate jdbcTemplate) {
//		this.jdbcTemplate = jdbcTemplate;
//	}

	/**
	 * 获取单个文件最大行数
	 * 
	 * @param maxRow
	 * @return
	 */
	protected int getMaxRow() {
		return maxRow < MAXROWS ? maxRow : MAXROWS;
	}

	/**
	 * 数据输出
	 * 
	 * @param data
	 * @param fos
	 * @throws IOException
	 */
	protected void writeToOutputStream(String data, OutputStream os) throws IOException {
		IOUtils.write(data, os, "UTF-8");
	}

	/**
	 * 文件头的写入
	 * 
	 * @param fos
	 */
	protected void writeHeaderToOutputStream(OutputStream os) throws IOException {
		writeToOutputStream(head.toString(), os);
	}

	/**
	 * 文件结尾的写入
	 * 
	 * @param fos
	 */
	protected void writeFooterToOutputStream(OutputStream os) throws IOException {
		writeToOutputStream(foot.toString(), os);
	}

	/**
	 * 文件头的写入
	 * 
	 * @param fos
	 */
	protected void writeSheetHeaderToOutputStream(OutputStream os, int count) throws IOException {
		String sh = sheetHead.toString();
		String head = java.text.MessageFormat.format(sh, count);
		writeToOutputStream(head, os);
	}

	/**
	 * 文件结尾的写入
	 * 
	 * @param fos
	 */
	protected void writeSheetFooterToOutputStream(OutputStream os) throws IOException {
		writeToOutputStream(sheetFoot.toString(), os);
	}

	protected void writeTitleToOutputStream(Collection<String> titles, OutputStream os) throws IOException {
		if (titles != null && titles.size() > 0) {
			writeToOutputStream("<Row>", os);
			for (String title : titles) {
				writeToOutputStream("<Cell><Data ss:Type=\"String\">"
						+ (title == null ? "" : HtmlUtils.htmlEscape(title)) + "</Data></Cell>", os);
			}
			writeToOutputStream("</Row>", os);
		}
	}

	protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
		return JdbcUtils.getResultSetValue(rs, index);
	}

	protected void writeOneRowToOutputStream(ResultSet rs, OutputStream os) throws SQLException, IOException {
		// 获取metaData;
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		writeToOutputStream("<Row>", os);
		for (int i = 1; i <= columnCount; i++) {
			Object obj = getColumnValue(rs, i);
			writeToOutputStream("<Cell><Data ss:Type=\"String\">"
					+ (obj == null ? "" : HtmlUtils.htmlEscape(obj.toString())) + "</Data></Cell>", os);
		}
		writeToOutputStream("</Row>", os);
	}

//	public void export(final Collection<String> titles, final Collection<String> firstRow,
//			final Collection<String> lastRow, final OutputStream os, String sql, Object... sqlParams) {
//		// 每个文件最大行数
//		final int max = getMaxRow();
//		List<SqlParameterValue> spvList = new ArrayList<SqlParameterValue>(sqlParams.length);
//		for (Object param : sqlParams) {
//			SqlParameterValue spv = new SqlParameterValue(JdbcUtils.TYPE_UNKNOWN, param);
//			spvList.add(spv);
//		}
//		PreparedStatementCreatorFactory factory = new PreparedStatementCreatorFactory(sql, spvList);
//		factory.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
//		PreparedStatementCreator psc = factory.newPreparedStatementCreator(sqlParams);
//		PreparedStatementSetter pss = factory.newPreparedStatementSetter(sqlParams);
//		jdbcTemplate.query(psc, pss, new ResultSetExtractor() {
//			@Override
//			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
//				try {
//					writeHeaderToOutputStream(os);
//					// 行数记录器
//					int i = 0, j = 0;
//					while (rs.next()) {
//						if (i == 0) {
//							// 写每个sheet头
//							writeSheetHeaderToOutputStream(os, j);
//							if (rs.isFirst() && !StaticMethod.isEmpty(firstRow)) {
//								writeTitleToOutputStream(firstRow, os);
//							}
//							// 数据区标题栏
//							writeTitleToOutputStream(titles, os);
//						}
//						// 写一行
//						i++;
//						writeOneRowToOutputStream(rs, os);
//						if (rs.isLast() && !StaticMethod.isEmpty(lastRow)) {
//							writeTitleToOutputStream(lastRow, os);
//						}
//						if (i == max) {
//							i = 0;
//							j++;
//							// 写每个sheet尾
//							writeSheetFooterToOutputStream(os);
//						} else if (rs.isLast()) {
//							writeSheetFooterToOutputStream(os);
//						}
//					}
//					writeFooterToOutputStream(os);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return null;
//			}
//		});
//	}
//
//	@Override
//	public void export(final Collection<String> titles, final OutputStream os, String sql, Object... sqlParams) {
//		// 每个文件最大行数
//		export(titles, null, null, os, sql, sqlParams);
//	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}
}
