package com.play.web.vcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class DefaultVerifyCode {
	private static char[] numCodeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static char[] codeSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
			'h', 'k', 'm', 'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y' };

	private static Font[] fonts = new Font[] { new Font("Times New Roman", Font.ITALIC | Font.BOLD, 20),
			new Font("Verdana", Font.ITALIC | Font.BOLD, 20) };

	static private Font getRandomFont(Random random) {
		return fonts[random.nextInt(fonts.length)];
	}

	public static String createVerifyJPGCode(String code, int width, int height, int codeCount, boolean bianKuang,
			boolean ganRao, boolean onlyNum) throws ServletException, java.io.IOException {
		HttpServletResponse resp = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		// 画边框。
		if (bianKuang) {
			g.setColor(Color.BLACK);
			g.drawRect(0, 0, width - 1, height - 1);
		}
		code = getRandCode(code, codeCount, onlyNum, random);

		int x = width / (codeCount + 1);
		for (int i = 0; i < codeCount; i++) {
			// 用随机产生的颜色将验证码绘制到图像中。
			g.setColor(getColor(random));
			g.setFont(getRandomFont(random));
			g.drawString(code.substring(i, i + 1), (i) * x + x / 2, height - 6);
		}
		if (ganRao) {
			// 写一个字符到bi
			for (int i = 0; i < 12; i++) {
				g.setColor(getColor(random));
				// 画线
				g.drawLine(random.nextInt(60), random.nextInt(30), random.nextInt(60), random.nextInt(30));
			}
		}
		// 禁止图像缓存。
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
		sos.close();
		return code;
	}

	private static String getRandCode(String code, int codeCount, boolean onlyNum, Random random) {
		if (StringUtils.isNotEmpty(code) && code.length() >= codeCount) {
			return code;
		}
		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();
		// 随机产生codeCount数字的验证码。
		String strRand = null;
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			if (onlyNum) {
				strRand = String.valueOf(numCodeSequence[random.nextInt(numCodeSequence.length)]);
			} else {
				strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			}
			// 将产生的四个随机数组合在一起。
			randomCode.append(strRand);
		}
		return randomCode.toString();
	}

	private static Color getColor(Random random) {
		return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}
}
