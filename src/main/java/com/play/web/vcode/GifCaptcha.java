package com.play.web.vcode;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * <p>
 * Gif验证码类
 * </p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class GifCaptcha extends Captcha {
	public GifCaptcha() {
	}

	public GifCaptcha(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public GifCaptcha(int width, int height, int len) {
		this(width, height);
		this.len = len;
	}

	public GifCaptcha(int width, int height, int len, Font font) {
		this(width, height, len);
		this.font = font;
	}

	private static Random random = new Random();
	private char[] randStrs = null;

	public char[] getRandStrs() {
		return randStrs;
	}

	public void setRandStrs(char[] randStrs) {
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
				randStrs = alphas();
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

	private static Font[] fonts = new Font[] { new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20),
			new Font("Algerian", Font.ITALIC | Font.BOLD, 20), new Font("Trattatello", Font.ITALIC | Font.BOLD, 20),
			new Font("Verdana", Font.ITALIC | Font.BOLD, 20) };

	private Font getRandomFont() {
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
	private BufferedImage graphicsImage(Color[] fontcolor, char[] strs, int flag) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 或得图形上下文
		// Graphics2D g2d=image.createGraphics();
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		// 利用指定颜色填充背景
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		AlphaComposite ac3;
		int h = height - ((height - 20) >> 1);
		int w = width / len;
		// g2d.setFont(font);
		for (int i = 0; i < len; i++) {
			g2d.setFont(getRandomFont());
			ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(flag, i));
			g2d.setComposite(ac3);
			g2d.setColor(fontcolor[i]);
			g2d.drawOval(num(width), num(height), 5 + num(20), 5 + num(20));
			g2d.drawString(strs[i] + "", width - (len - i) * w + 1, h - 4);
		}

		// 设置干扰线
		for (int i = 0; i < 20; i++) {
			int xs = random.nextInt(width);
			int ys = random.nextInt(height);
			int xe = xs + random.nextInt(width);
			int ye = ys + random.nextInt(height);
			g2d.setStroke(new BasicStroke(1.0f));
			g2d.setColor(getRandColor(1, 255));
			g2d.drawLine(xs, ys, xe, ye);
		}

		g2d.dispose();
		return image;
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
