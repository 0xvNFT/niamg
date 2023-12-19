package com.play.web.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.play.common.SystemConfig;
import com.play.web.exception.BaseException;

public class ShellUtil {
	private static Logger logger = LoggerFactory.getLogger(ShellUtil.class);
	private static final String GOD_CODE_SHELL = "/addgodcode.sh ";
	private static final String CAT_NGINX_LOG_SHELL = "/catgrepnginxlog.sh ";

	private static final String UPDATE_SSL_FILE_SHELL = "/updatesslfile.sh ";
	private static final String UPDATE_NGINX_CONF_SHELL = "/updatenginxconf.sh";
	private static final String CLEAN_NGINX_CACHE_SHELL = "/cleannginxcache.sh";
	private static final String CAT_TOMCAT_LOG_SHELL = "/catgreptomcatlog.sh ";

	public static String addGodCode(String code) {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			Process p0 = Runtime.getRuntime().exec(SystemConfig.SHELL_FILE_PATH + GOD_CODE_SHELL + code);
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				return "非正常终止";
			}

			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  addGodCode 发生错误code=" + code, e1);
		}
		return null;
	}

	public static String catGrepNginxLog(String key) {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			Process p0 = Runtime.getRuntime()
					.exec(SystemConfig.SHELL_FILE_PATH + CAT_NGINX_LOG_SHELL + "\"" + key + "\"");
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				return "非正常终止";
			}
			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  catGrepNginxLog 发生错误code=" + key, e1);
		}
		return null;
	}

	public static String updateSslFile(Integer certId) {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			Process p0 = Runtime.getRuntime().exec(SystemConfig.SHELL_FILE_PATH + UPDATE_SSL_FILE_SHELL + certId);
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				return "非正常终止";
			}
			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  UPDATE_SSL_FILE_SHELL 发生错误", e1);
		}
		return null;
	}

	public static String updateNginxConf() {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			Process p0 = Runtime.getRuntime().exec(SystemConfig.SHELL_FILE_PATH + UPDATE_NGINX_CONF_SHELL);
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				throw new BaseException("非正常终止");
			}
			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  UPDATE_NGINX_CONF_SHELL 发生错误", e1);
			throw new BaseException("运行shell  UPDATE_NGINX_CONF_SHELL 发生错误");
		}
	}

	public static String cleanNginxCache() {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			Process p0 = Runtime.getRuntime().exec(SystemConfig.SHELL_FILE_PATH + CLEAN_NGINX_CACHE_SHELL);
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				return "非正常终止";
			}
			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  CLEAN_NGINX_CACHE_SHELL 发生错误", e1);
		}
		return "";
	}

	public static String catTomcatLogForOnlinePay(String key, Integer lineNum) {
		if (StringUtils.isEmpty(SystemConfig.SHELL_FILE_PATH)) {
			throw new BaseException("没有shell文件目录");
		}
		try {
			if (lineNum == null) {
				lineNum = 1;
			}
			if (lineNum > 50) {
				lineNum = 50;
			}
			Process p0 = Runtime.getRuntime()
					.exec(SystemConfig.SHELL_FILE_PATH + CAT_TOMCAT_LOG_SHELL + "\"" + key + "\" " + lineNum);
			// 读取标准输出流
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p0.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			// 读取标准错误流
			BufferedReader brError = new BufferedReader(new InputStreamReader(p0.getErrorStream(), "utf-8"));
			while ((line = brError.readLine()) != null) {
				sb.append(line);
			}
			// waitFor()判断Process进程是否终止，通过返回值判断是否正常终止。0代表正常终止
			int c = p0.waitFor();
			bufferedReader.close();
			brError.close();
			p0.destroy();
			if (c != 0) {
				return "非正常终止";
			}
			return sb.toString();
		} catch (Exception e1) {
			logger.error("运行shell  catGrepNginxLog 发生错误code=" + key, e1);
		}
		return null;
	}
}
