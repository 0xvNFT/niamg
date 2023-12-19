package com.play.common.ip.country;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CNIpInit {
	public static void main(String[] args) throws IOException {
		readApnicContent();
	}

	private static void readApnicContent() throws IOException {
		/**
		 * https://www.cnblogs.com/lsgxeva/p/9401060.html
		 * Apanic提供了每日更新的亚太地区IPv4，IPv6，AS号分配的信息表，访问url是
		 * http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest
		 */
		String content = getContentFromUrl("http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest");
		writeFile("src/main/resources/cnIps.txt", content);
	}

	private static void writeFile(String path, String content) throws IOException {
		// 获取文件
		File file = new File(path);
		System.out.println("文件路径：" + file.getAbsolutePath());
		// 如果没有文件就创建
		if (!file.isFile()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(path, true);
		BufferedWriter writer = new BufferedWriter(fw);
		writer.write(content);
		writer.close();
	}

	public static String getContentFromUrl(String httpUrl) {
		try {
			StringBuilder sb = new StringBuilder();
			URL url = new URL(httpUrl);
			URLConnection conn = url.openConnection();
			// 获取输入流
			InputStream inStream = conn.getInputStream();
			// 将字节输入流转换为字符输入流
			InputStreamReader read = new InputStreamReader(inStream, "utf-8");
			// 为字符输入流添加缓冲
			BufferedReader br = new BufferedReader(read);
			// 读取返回结果
			String data = br.readLine();
			while (data != null) {
				if (data.indexOf("apnic|CN|ipv4|") != -1) {
					String[] ss = data.split("\\|");
					sb.append(CNIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append("|");
				}
				data = br.readLine();
			}
			// 释放资源
			br.close();
			read.close();
			inStream.close();
			sb.deleteCharAt(sb.length() - 1);
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
