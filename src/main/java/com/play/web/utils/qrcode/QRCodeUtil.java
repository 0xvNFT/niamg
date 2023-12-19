package com.play.web.utils.qrcode;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author Liuy
 * @ClassName: QRCodeUtil2
 * @Description: 二维码编码
 * @date 2016年7月9日 下午3:03:24
 */
public class QRCodeUtil {
    // 设置二维码编码格式
    private static final String CHARSET = "utf-8";
    // 保存的二维码格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 800;
    // LOGO宽度
    private static final int LOGO_WIDTH = 80;
    // LOGO高度
    private static final int LOGO_HEIGHT = 80;

    /**
     * @Title: createImage @Description: 将二维码内容创建到Image流 @param content
     * 二维码内容 @param imgPath logo图片地址 @param needCompress
     * 是否压缩logo图片大小 @return @throws Exception 参数说明 @return BufferedImage
     * 返回类型 @throws
     */
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoPath == null || "".equals(logoPath)) {
            return image;
        }
        // 插入logo
        insertImage(image, logoPath, needCompress);
        return image;
    }

    /**
     * @Title: insertImage @Description: 将logo插入到二维码中 @param source
     * 二维码Image流 @param imgPath logo地址 @param needCompress
     * 是否压缩大小 @throws Exception 参数说明 @return void 返回类型 @throws
     */
    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        File file = null;
        Image src = null;
        if (logoPath.contains("http")) {
            // new一个URL对象
            URL url = new URL(logoPath);
            src = ImageIO.read(url);
        }else {
            file = new File(logoPath);
            if (!file.exists()) {
                System.err.println("" + logoPath + "   该文件不存在！");
                return;
            }
            src  =ImageIO.read(new File(logoPath));
        }
        if (src == null) {
            System.err.println("" + logoPath + "   该文件不存在！");
            return;
        }
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }
//
//    private static byte[] readInputStream(InputStream inStream) throws Exception {
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        // 创建一个Buffer字符串
//        byte[] buffer = new byte[1024];
//        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
//        int len = 0;
//        // 使用一个输入流从buffer里把数据读取出来
//        while ((len = inStream.read(buffer)) != -1) {
//            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
//            outStream.write(buffer, 0, len);
//        }
//        // 关闭输入流
//        inStream.close();
//        // 把outStream里的数据写入内存
//        return outStream.toByteArray();
//    }

    /**
     * @Title: mkdirs @Description: 创建文件夹 @param destPath 文件夹地址 @return void
     * 返回类型 @throws
     */
    private static boolean mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            return true;
        }

        return false;
    }

    /**
     * @Title: encode @Description: 生成二维码 @param content 二维码内容 @param imgPath
     * logo图片地址 @param destPath 目标保存地址 @param needCompress
     * 是否压缩logo图片大小 @throws Exception 参数说明 @return void 返回类型 @throws
     */
    private static void encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoPath, needCompress);
        if (mkdirs(destPath)) {
            String file = new Random().nextInt(99999999) + ".jpg";
            ImageIO.write(image, FORMAT_NAME, new File(destPath + "/" + file));
        }
    }

    /**
     * @Title: encode @Description: 生成二维码 @param content 二维码内容 @param destPath
     * 目标保存地址 @throws Exception 参数说明 @return void 返回类型 @throws
     */
    public static void encode(String content, String destPath) throws Exception {
        encode(content, null, destPath, false);
    }

    /**
     * @Title: encode @Description: 生成二维码 @param content 二维码内容 @param imgPath
     * logo图片地址 @param output 输出流 @param needCompress
     * 是否压缩logo图片大小 @throws Exception 参数说明 @return void 返回类型 @throws
     */
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = createImage(content, logoPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * @Title: encode @Description: 生成二维码 @param content 二维码内容 @param output
     * 输出流 @throws Exception 参数说明 @return void 返回类型 @throws
     */
    public static void encode(String content, OutputStream output) throws Exception {
        encode(content, null, output, false);
    }

    /**
     * @Title: decode @Description: 对二维码解码 @param file 文件对象 @return
     * 解码后的二维码内容字符串 @throws Exception 参数说明 @return String 返回类型 @throws
     */
    private static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        return new MultiFormatReader().decode(bitmap, hints).getText();
    }

    /**
     * @Title: decode @Description: 对二维码解码 @param path 文件路径 @return @throws
     * Exception 参数说明 @return String 返回类型 @throws
     */
    public static String decode(String path) throws Exception {
        return decode(new File(path));
    }
}
