package com.play.web.utils;


import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Objects;

public class MultipartFileToFile {
    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }



    public static byte[] readImg(String urlOrPath){
        byte[] imgBytes = new byte[1024 * 1024];

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedInputStream reader = null;
        InputStream in = null;
        URLConnection conn = null;
        File temFile = null;

        try {
            if(!urlOrPath.startsWith("http:")){
                File imgFile = new File(urlOrPath);
                if(!imgFile.isFile() || !imgFile.exists() || !imgFile.canRead()){
                    throw new BaseException(BaseI18nCode.stationImageExistNot);
                    //return new byte[0];
                }
                in = new FileInputStream(imgFile);
            }else{
                URL imgUrl = new URL(urlOrPath);
                conn = imgUrl.openConnection();
                temFile = new File(System.currentTimeMillis() + ".png");
                FileOutputStream tem = new FileOutputStream(temFile);
                BufferedImage image = ImageIO.read(conn.getInputStream());
                ImageIO.write(image, "png", tem);
                in = new FileInputStream(temFile);
            }
            reader = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            while(reader.read(buffer) != -1){
                out.write(buffer);
            }
            imgBytes = out.toByteArray();

        } catch (BaseException e) {
            throw new BaseException(BaseI18nCode.stationImageExistNot);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(temFile != null){
                temFile.delete();
            }
        }
        return imgBytes;
    }


    /**
     * 将图片转为file
     *
     * @param url 图片url
     * @return File
     */
    public static File getFile(String url) throws BaseException, IOException {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."),url.length());
        File file = null;
        BufferedInputStream reader = null;
        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        try {
            /*if(!fileName.startsWith("https:")){
            file = File.createTempFile("local", fileName);
                if(!file.isFile() || !file.exists() || !file.canRead()){
                    throw new BaseException(BaseI18nCode.stationImageExistNot);
                }
                //urlfile = ClassLoader.class.getResource(fileName);
                //inStream = urlfile.openStream();
                inStream = new FileInputStream(fileName);
                os = new FileOutputStream(file);

                reader = new BufferedInputStream(inStream);
                byte[] buffer = new byte[8192];
                while(reader.read(buffer) != -1){
                    os.write(buffer);
                }
                *//*int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }*//*
            }else {*/
            file = File.createTempFile("net_url", fileName);
            //下载
            urlfile = new URL(url);
            if (!file.isFile() || !file.exists() || !file.canRead()) {
                throw new BaseException(BaseI18nCode.stationImageExistNot);
            }
            inStream = urlfile.openStream();
            os = new FileOutputStream(file);

            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            //}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    public static boolean testUrl(String webUrl) {
        try {
            // 设置此类是否应该自动执行 HTTP重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
            // 到URL所引用的远程对象的连接
            HttpURLConnection conn = (HttpURLConnection) new URL(webUrl).openConnection();
            // 设置URL请求的方法，GET POST HEAD OPTIONS PUT DELETE TRACE
            // 以上方法之一是合法的，具体取决于协议的限制。
            conn.setRequestMethod("HEAD");
            // 从HTTP响应消息获取状态码
            return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File byte2File(byte[] buf)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            /*File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath);*/
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
