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

public class ALLIpInit {
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
		writeFile("src/main/resources/apnicIps.txt", content);
		String arinContent = getArinContentFromUrl(
				"https://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest");
		writeFile("src/main/resources/arinIps.txt", arinContent);
		String afrinicContent = getAfrinicContentFromUrl(
				"http://ftp.afrinic.net/stats/afrinic/delegated-afrinic-latest");
		writeFile("src/main/resources/afrinicIps.txt", afrinicContent);
		String lacnicContent = getLacnicContentFromUrl(
				"https://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest");
		writeFile("src/main/resources/lacnicIps.txt", lacnicContent);
		String ripeContent = getRipeContentFromUrl("https://ftp.ripe.net/ripe/stats/delegated-ripencc-latest");
		writeFile("src/main/resources/ripeIps.txt", ripeContent);
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
				if (data.indexOf("apnic|CN|ipv4|") != -1 || data.indexOf("apnic|HK|ipv4|") != -1
						|| data.indexOf("apnic|TW|ipv4|") != -1 || data.indexOf("apnic|PH|ipv4|") != -1
						|| data.indexOf("apnic|KH|ipv4|") != -1 || data.indexOf("apnic|MY|ipv4|") != -1
						|| data.indexOf("apnic|TH|ipv4|") != -1 || data.indexOf("apnic|MM|ipv4|") != -1
						|| data.indexOf("apnic|LA|ipv4|") != -1 || data.indexOf("apnic|BD|ipv4|") != -1
						|| data.indexOf("apnic|IR|ipv4|") != -1 || data.indexOf("apnic|MX|ipv4|") != -1
						|| data.indexOf("apnic|FR|ipv4|") != -1 || data.indexOf("apnic|BY|ipv4|") != -1
						|| data.indexOf("apnic|SG|ipv4|") != -1 || data.indexOf("apnic|VN|ipv4|") != -1
						|| data.indexOf("apnic|IE|ipv4|") != -1 || data.indexOf("apnic|KR|ipv4|") != -1
						|| data.indexOf("apnic|DE|ipv4|") != -1 || data.indexOf("apnic|GN|ipv4|") != -1
						|| data.indexOf("apnic|LB|ipv4|") != -1 || data.indexOf("apnic|UA|ipv4|") != -1
						|| data.indexOf("apnic|LV|ipv4|") != -1 || data.indexOf("apnic|RU|ipv4|") != -1
						|| data.indexOf("apnic|US|ipv4|") != -1 || data.indexOf("apnic|SO|ipv4|") != -1
						|| data.indexOf("apnic|BR|ipv4|") != -1 || data.indexOf("apnic|ID|ipv4|") != -1
						|| data.indexOf("apnic|JP|ipv4|") != -1) {// 中国,菲律宾,柬埔寨,马来西亚,香港,台湾,泰国,缅甸,老挝,孟加拉,伊朗,墨西哥,法国,白俄罗斯,新加坡,越南,爱尔兰,韩国,德国,几内亚,黎巴嫩,乌克兰,拉脱维亚,俄罗斯,美国,索马里,巴西,印尼,日本
					String[] ss = data.split("\\|");
					System.out.println(ss[1]);
					sb.append(ALLIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append(",").append(ss[1])
							.append("|");
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

	public static String getArinContentFromUrl(String httpUrl) {
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
				if (data.indexOf("arin|CN|ipv4|") != -1 || data.indexOf("arin|HK|ipv4|") != -1
						|| data.indexOf("arin|TW|ipv4|") != -1 || data.indexOf("arin|PH|ipv4|") != -1
						|| data.indexOf("arin|KH|ipv4|") != -1 || data.indexOf("arin|MY|ipv4|") != -1
						|| data.indexOf("arin|TH|ipv4|") != -1 || data.indexOf("arin|MM|ipv4|") != -1
						|| data.indexOf("arin|LA|ipv4|") != -1 || data.indexOf("arin|BD|ipv4|") != -1
						|| data.indexOf("arin|IR|ipv4|") != -1 || data.indexOf("arin|MX|ipv4|") != -1
						|| data.indexOf("arin|FR|ipv4|") != -1 || data.indexOf("arin|BY|ipv4|") != -1
						|| data.indexOf("arin|SG|ipv4|") != -1 || data.indexOf("arin|VN|ipv4|") != -1
						|| data.indexOf("arin|IE|ipv4|") != -1 || data.indexOf("arin|KR|ipv4|") != -1
						|| data.indexOf("arin|DE|ipv4|") != -1 || data.indexOf("arin|GN|ipv4|") != -1
						|| data.indexOf("arin|LB|ipv4|") != -1 || data.indexOf("arin|UA|ipv4|") != -1
						|| data.indexOf("arin|LV|ipv4|") != -1 || data.indexOf("arin|RU|ipv4|") != -1
						|| data.indexOf("arin|US|ipv4|") != -1 || data.indexOf("arin|SO|ipv4|") != -1
						|| data.indexOf("arin|BR|ipv4|") != -1 || data.indexOf("arin|ID|ipv4|") != -1
						|| data.indexOf("arin|JP|ipv4|") != -1) {// 中国,菲律宾,柬埔寨,马来西亚,香港,台湾,泰国,缅甸,老挝,孟加拉,伊朗,墨西哥,法国,白俄罗斯,新加坡,越南,爱尔兰,韩国,德国,几内亚,黎巴嫩,乌克兰,拉脱维亚,俄罗斯,美国,索马里,巴西,印尼,日本
					String[] ss = data.split("\\|");
					System.out.println(ss[1]);
					sb.append(ALLIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append(",").append(ss[1])
							.append("|");
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

	public static String getAfrinicContentFromUrl(String httpUrl) {
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
				if (data.indexOf("afrinic|CN|ipv4|") != -1 || data.indexOf("afrinic|HK|ipv4|") != -1
						|| data.indexOf("afrinic|TW|ipv4|") != -1 || data.indexOf("afrinic|PH|ipv4|") != -1
						|| data.indexOf("afrinic|KH|ipv4|") != -1 || data.indexOf("afrinic|MY|ipv4|") != -1
						|| data.indexOf("afrinic|TH|ipv4|") != -1 || data.indexOf("afrinic|MM|ipv4|") != -1
						|| data.indexOf("afrinic|LA|ipv4|") != -1 || data.indexOf("afrinic|BD|ipv4|") != -1
						|| data.indexOf("afrinic|IR|ipv4|") != -1 || data.indexOf("afrinic|MX|ipv4|") != -1
						|| data.indexOf("afrinic|FR|ipv4|") != -1 || data.indexOf("afrinic|BY|ipv4|") != -1
						|| data.indexOf("afrinic|SG|ipv4|") != -1 || data.indexOf("afrinic|VN|ipv4|") != -1
						|| data.indexOf("afrinic|IE|ipv4|") != -1 || data.indexOf("afrinic|KR|ipv4|") != -1
						|| data.indexOf("afrinic|DE|ipv4|") != -1 || data.indexOf("afrinic|GN|ipv4|") != -1
						|| data.indexOf("afrinic|LB|ipv4|") != -1 || data.indexOf("afrinic|UA|ipv4|") != -1
						|| data.indexOf("afrinic|LV|ipv4|") != -1 || data.indexOf("afrinic|RU|ipv4|") != -1
						|| data.indexOf("afrinic|US|ipv4|") != -1 || data.indexOf("afrinic|SO|ipv4|") != -1
						|| data.indexOf("afrinic|BR|ipv4|") != -1 || data.indexOf("afrinic|ID|ipv4|") != -1
						|| data.indexOf("afrinic|JP|ipv4|") != -1) {// 中国,菲律宾,柬埔寨,马来西亚,香港,台湾,泰国,缅甸,老挝,孟加拉,伊朗,墨西哥,法国,白俄罗斯,新加坡,越南,爱尔兰,韩国,德国,几内亚,黎巴嫩,乌克兰,拉脱维亚,俄罗斯,美国,索马里,巴西,印尼,日本
					String[] ss = data.split("\\|");
					System.out.println(ss[1]);
					sb.append(ALLIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append(",").append(ss[1])
							.append("|");
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

	public static String getLacnicContentFromUrl(String httpUrl) {
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
				if (data.indexOf("lacnic|CN|ipv4|") != -1 || data.indexOf("lacnic|HK|ipv4|") != -1
						|| data.indexOf("lacnic|TW|ipv4|") != -1 || data.indexOf("lacnic|PH|ipv4|") != -1
						|| data.indexOf("lacnic|KH|ipv4|") != -1 || data.indexOf("lacnic|MY|ipv4|") != -1
						|| data.indexOf("lacnic|TH|ipv4|") != -1 || data.indexOf("lacnic|MM|ipv4|") != -1
						|| data.indexOf("lacnic|LA|ipv4|") != -1 || data.indexOf("lacnic|BD|ipv4|") != -1
						|| data.indexOf("lacnic|IR|ipv4|") != -1 || data.indexOf("lacnic|MX|ipv4|") != -1
						|| data.indexOf("lacnic|FR|ipv4|") != -1 || data.indexOf("lacnic|BY|ipv4|") != -1
						|| data.indexOf("lacnic|SG|ipv4|") != -1 || data.indexOf("lacnic|VN|ipv4|") != -1
						|| data.indexOf("lacnic|IE|ipv4|") != -1 || data.indexOf("lacnic|KR|ipv4|") != -1
						|| data.indexOf("lacnic|DE|ipv4|") != -1 || data.indexOf("lacnic|GN|ipv4|") != -1
						|| data.indexOf("lacnic|LB|ipv4|") != -1 || data.indexOf("lacnic|UA|ipv4|") != -1
						|| data.indexOf("lacnic|LV|ipv4|") != -1 || data.indexOf("lacnic|RU|ipv4|") != -1
						|| data.indexOf("lacnic|US|ipv4|") != -1 || data.indexOf("lacnic|SO|ipv4|") != -1
						|| data.indexOf("lacnic|BR|ipv4|") != -1 || data.indexOf("lacnic|ID|ipv4|") != -1
						|| data.indexOf("lacnic|JP|ipv4|") != -1) {// 中国,菲律宾,柬埔寨,马来西亚,香港,台湾,泰国,缅甸,老挝,孟加拉,伊朗,墨西哥,法国,白俄罗斯,新加坡,越南,爱尔兰,韩国,德国,几内亚,黎巴嫩,乌克兰,拉脱维亚,俄罗斯,美国,索马里,巴西,印尼,日本
					String[] ss = data.split("\\|");
					System.out.println(ss[1]);
					sb.append(ALLIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append(",").append(ss[1])
							.append("|");
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

	public static String getRipeContentFromUrl(String httpUrl) {
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
				if (data.indexOf("ripencc|CN|ipv4|") != -1 || data.indexOf("ripencc|HK|ipv4|") != -1
						|| data.indexOf("ripencc|TW|ipv4|") != -1 || data.indexOf("ripencc|PH|ipv4|") != -1
						|| data.indexOf("ripencc|KH|ipv4|") != -1 || data.indexOf("ripencc|MY|ipv4|") != -1
						|| data.indexOf("ripencc|TH|ipv4|") != -1 || data.indexOf("ripencc|MM|ipv4|") != -1
						|| data.indexOf("ripencc|LA|ipv4|") != -1 || data.indexOf("ripencc|BD|ipv4|") != -1
						|| data.indexOf("ripencc|IR|ipv4|") != -1 || data.indexOf("ripencc|MX|ipv4|") != -1
						|| data.indexOf("ripencc|FR|ipv4|") != -1 || data.indexOf("ripencc|BY|ipv4|") != -1
						|| data.indexOf("ripencc|SG|ipv4|") != -1 || data.indexOf("ripencc|VN|ipv4|") != -1
						|| data.indexOf("ripencc|IE|ipv4|") != -1 || data.indexOf("ripencc|KR|ipv4|") != -1
						|| data.indexOf("ripencc|DE|ipv4|") != -1 || data.indexOf("ripencc|GN|ipv4|") != -1
						|| data.indexOf("ripencc|LB|ipv4|") != -1 || data.indexOf("ripencc|UA|ipv4|") != -1
						|| data.indexOf("ripencc|LV|ipv4|") != -1 || data.indexOf("ripencc|RU|ipv4|") != -1
						|| data.indexOf("ripencc|US|ipv4|") != -1 || data.indexOf("ripencc|SO|ipv4|") != -1
						|| data.indexOf("ripencc|BR|ipv4|") != -1 || data.indexOf("ripencc|ID|ipv4|") != -1
						|| data.indexOf("ripencc|JP|ipv4|") != -1) {// 中国,菲律宾,柬埔寨,马来西亚,香港,台湾,泰国,缅甸,老挝,孟加拉,伊朗,墨西哥,法国,白俄罗斯,新加坡,越南,爱尔兰,韩国,德国,几内亚,黎巴嫩,乌克兰,拉脱维亚,俄罗斯,美国,索马里,巴西,印尼,日本
					String[] ss = data.split("\\|");
					System.out.println(ss[1]);
					sb.append(ALLIpTool.ipToLong(ss[3])).append(",").append(ss[4]).append(",").append(ss[1])
							.append("|");
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
