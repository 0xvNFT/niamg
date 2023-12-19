package com.play.web.vcode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * <p>
 * Gif验证码类
 * </p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class HanZiGifCaptcha extends Captcha {
	private String chars = null; // 随机字符串
	private Font[] fonts = null;

	public HanZiGifCaptcha() {
	}

	public HanZiGifCaptcha(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public HanZiGifCaptcha(int width, int height, int len) {
		this(width, height);
		this.len = len;
	}

	public HanZiGifCaptcha(int width, int height, int len, Font[] fonts) {
		this(width, height, len);
		this.fonts = fonts;
	}

	private String[] randStrs = null;

	public String[] getRandStrs() {
		return randStrs;
	}

	public void setRandStrs(String[] randStrs) {
		this.randStrs = randStrs;
	}

	@Override
	public void out(OutputStream os) {
		try {
			GifEncoder gifEncoder = new GifEncoder(); // gif编码类，这个利用了洋人写的编码类，所有类都在附件中
			// 生成字符
			gifEncoder.start(os);
			gifEncoder.setQuality(180);
			gifEncoder.setDelay(100);
			gifEncoder.setRepeat(0);
			BufferedImage frame;
			if (randStrs == null || randStrs.length < len) {
				randStrs = randomStrs();
			}
			Color fontcolor[] = new Color[len];
			for (int i = 0; i < len; i++) {
				fontcolor[i] = new Color(20 + num(110), 20 + num(110), 20 + num(110));
			}
			for (int i = 0; i < len; i++) {
				frame = graphicsImage(fontcolor, randStrs, i);
				gifEncoder.addFrame(frame);
				frame.flush();
			}
			gifEncoder.finish();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	private static Random random = new Random();
	private static String[] array = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	private String[] randomStrs() {
		String[] cs = new String[len];
		for (int i = 0; i < len; i++) {
			int r1, r2, r3, r4;
			String strH, strL;// high&low
			r1 = random.nextInt(3) + 11; // 前闭后开[11,14)
			if (r1 == 13) {
				r2 = random.nextInt(7);
			} else {
				r2 = random.nextInt(16);
			}

			r3 = random.nextInt(6) + 10;
			if (r3 == 10) {
				r4 = random.nextInt(15) + 1;
			} else if (r3 == 15) {
				r4 = random.nextInt(15);
			} else {
				r4 = random.nextInt(16);
			}

			strH = array[r1] + array[r2];
			strL = array[r3] + array[r4];

			byte[] bytes = new byte[2];
			bytes[0] = (byte) (Integer.parseInt(strH, 16));
			bytes[1] = (byte) (Integer.parseInt(strL, 16));

			try {
				cs[i] = new String(bytes, "GB2312");
				chars += cs[i];
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return cs;

	}

	public String text() {
		return chars;
	}

	private static Font[] defaultFonts = new Font[] { new Font("黑体", Font.ITALIC | Font.BOLD, 20),
			new Font("华文彩云", Font.ITALIC | Font.BOLD, 20), new Font("华文行楷", Font.ITALIC | Font.BOLD, 20),
			new Font("方正舒体", Font.ITALIC | Font.BOLD, 20), new Font("华文隶书", Font.ITALIC | Font.BOLD, 20),
			new Font("幼圆", Font.ITALIC | Font.BOLD, 20), new Font("华文琥珀", Font.ITALIC | Font.BOLD, 20),
			new Font("宋体", Font.ITALIC | Font.BOLD, 20), new Font("楷体", Font.ITALIC | Font.BOLD, 20) };

	private Font getRandomFont() {
		if (fonts == null) {
			return defaultFonts[random.nextInt(fonts.length)];
		}
		return fonts[random.nextInt(fonts.length)];
	}

	/**
	 * 画随机码图
	 * 
	 * @param fontcolor
	 *            随机字体颜色
	 * @param strs
	 *            字符数组
	 * @param flag
	 *            透明度使用
	 * @return BufferedImage
	 */
	private BufferedImage graphicsImage(Color[] fontcolor, String[] strs, int flag) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 或得图形上下文
		// Graphics2D g2d=image.createGraphics();
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		// 利用指定颜色填充背景
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		AlphaComposite ac3;
		int h = height - ((height - 20) >> 1);
		int w = width / len - 4;
		for (int i = 0; i < len; i++) {
			g2d.setFont(getRandomFont());
			ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
			g2d.setComposite(ac3);
			g2d.setColor(fontcolor[i]);
			g2d.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));
			rotateString(strs[i], width - (len - i + 1) * w + 3 * i, h, g2d);
		}

		// 设置干扰线
		for (int i = 0; i < 20; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width);
			int ye = ys + random.nextInt(height);
			g2d.setColor(getRandColor(1, 255));
			g2d.drawLine(xs, ys, xe, ye);
		}

		g2d.dispose();
		return image;
	}

	private void rotateString(String s, int x, int y, Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		double rot = -0.6 + Math.abs(Math.toRadians(random.nextInt(60)));
		// 平移原点到图形环境的中心 ,这个方法的作用实际上就是将字符串移动到某一个位置
		g2d.translate(x - 2, y);
		// 旋转文本
		g2d.rotate(rot);
		// 特别需要注意的是,这里的画笔已经具有了上次指定的一个位置,所以这里指定的其实是一个相对位置
		g2d.drawString(s, 0, 0);
	}

	// 得到随机颜色
	private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 获取透明度,从0到1,自动计算步长
	 * 
	 * @return float 透明度
	 */
	private float getAlpha(int i, int j) {
		int num = i + j;
		float r = (float) 1 / len, s = (len + 1) * r;
		return num > len ? (num * r - s) : num * r;
	}

}
