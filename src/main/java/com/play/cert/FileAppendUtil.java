package com.play.cert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileAppendUtil {
	private static Logger logger = LoggerFactory.getLogger(FileAppendUtil.class);

	public static void write(String file, String conent) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			out.write(conent);
		} catch (Exception e) {
			logger.error("读取文件发生错误" + file, e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 追加文件：使用FileWriter
	 *
	 * @param fileName
	 * @param content
	 */
	public static void write2(String fileName, String content) {
		FileWriter writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriter(fileName, true);
			writer.write(content);
		} catch (IOException e) {
			logger.error("读取文件发生错误" + fileName, e);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 *
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void write3(String fileName, String content) {
		RandomAccessFile randomFile = null;
		try {
			// 打开一个随机访问文件流，按读写方式
			randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
		} catch (IOException e) {
			logger.error("读取文件发生错误" + fileName, e);
		} finally {
			if (randomFile != null) {
				try {
					randomFile.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			File file = new File("/Users/macair/Documents/text.txt");
			if (file.createNewFile()) {
			//	System.out.println("Create file successed");
			}
			write("/Users/macair/Documents/text.txt", "1231");
			write("/Users/macair/Documents/text.txt", "氛围");
			write("/Users/macair/Documents/text.txt", "afewqfewqfwq风情氛围");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String readFileContent(String fileName) {
		try {
			return readFileContent(new FileInputStream(fileName));
		} catch (Exception e) {
			logger.error("读取文件发生错误" + fileName, e);
		}
		return "";
	}

	public static String readFileContent(File f) {
		try {
			return readFileContent(new FileInputStream(f));
		} catch (Exception e) {
			logger.error("读取文件发生错误" + f.getAbsolutePath(), e);
		}
		return "";
	}

	public static String readFileContent(InputStream inputStream) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (Exception e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
				}
			}
			try {
				inputStream.close();
			} catch (Exception e) {
			}
		}
		return sb.toString();
	}
}
