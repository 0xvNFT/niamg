package com.play.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.dao.ModelCreatorDao;
import com.play.service.ModelCreatorService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

@Service
public class ModelCreatorServiceImpl implements ModelCreatorService {
	@Autowired
	private ModelCreatorDao creatorDao;

	private static Logger logger = LoggerFactory.getLogger(ModelCreatorServiceImpl.class);

	private Configuration getFreemarkerConfig() {
		try {
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
			cfg.setDefaultEncoding("UTF-8");
			cfg.setOutputEncoding("UTF-8");
			cfg.setClassicCompatible(true);// 如果变量为null,转化为空字符串,比如做比较的时候按照空字符做比较
			cfg.setLocale(Locale.CHINA);
			cfg.setNumberFormat("#.##");
			cfg.setWhitespaceStripping(true);// 去掉多余的空格
			cfg.setTemplateUpdateDelayMilliseconds(1000);// 模版更新时间,这里配置是1秒更新一次,正式环境,模版不会改变,可以将这个值设很大,提高效率
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
			return cfg;
		} catch (Exception e) {
			logger.error("初始化freemarker 配置信息发生错误", e);
		}
		return null;
	}

	@Override
	public String createModel(String tableName, String path) {
		if (StringUtils.isEmpty(tableName)) {
			List<String> tables = creatorDao.getTables();
			for (String t : tables) {
				if (t.matches("[a-z_]+[\\d]+$")) {
					continue;
				}
				generateFile(t, path);
			}
		} else {
			generateFile(tableName, path);
		}
		return "model 已生成";
	}

	private void generateFile(String t, String path) {
		Map<String, Object> map = new HashMap<>();
		String tn = toCamelName(t);
		String javaName = Character.toUpperCase(tn.charAt(0)) + tn.substring(1);
		map.put("javaName", javaName);
		map.put("tName", tn);
		map.put("tableName", t);
		map.put("common", creatorDao.getTableCommon(t));
		map.put("colList", creatorDao.getColumnInfo(t));
		map.put("pk", creatorDao.getPrimaryKey(t));
		renderFTLtoFile("ftl/model.ftl", "model/" + javaName + ".java", map, path);
		renderFTLtoFile("ftl/dao.ftl", "dao/" + javaName + "Dao.java", map, path);
		renderFTLtoFile("ftl/service.ftl", "service/" + javaName + "Service.java", map, path);
		renderFTLtoFile("ftl/serviceImpl.ftl", "service/impl/" + javaName + "ServiceImpl.java", map, path);
	}

	private String toCamelName(String name) {
		int len = name.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);
			if (c == '_') {
				if (++i < len) {
					sb.append(Character.toUpperCase(name.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private void renderFTLtoFile(String srcFile, String destFile, Map<String, Object> map, String path) {
	//	logger.info("Render model.ftl to " + destFile);
		OutputStreamWriter outWriter = null;
		FileOutputStream fos = null;
		try {
			File destFolder = new File(ModelCreatorServiceImpl.class.getResource("/").getPath());
			File f = null;
			if (StringUtils.isNotEmpty(path)) {
				if (!path.endsWith("/")) {
					path = path + "/";
				}
				f = new File(path + "src/main/java/com/play/" + destFile);
			} else {
				f = new File(destFolder.getParentFile(), "src/main/java/com/play/" + destFile);
			}
			f.getParentFile().mkdirs();
			fos = new FileOutputStream(f);
			outWriter = new OutputStreamWriter(fos, "UTF-8");
			Configuration conf = getFreemarkerConfig();
			conf.setDirectoryForTemplateLoading(destFolder);
			Template temp = conf.getTemplate(srcFile);
			temp.process(map, outWriter);
	//		logger.info("FTL解析完成");
		} catch (Exception e) {
			logger.error("renderFTLtoFile生成彩票投注页面发生错误", e);
		} finally {
			try {
				outWriter.close();
				fos.close();
			} catch (Exception e) {
			}
		}
	}
}
