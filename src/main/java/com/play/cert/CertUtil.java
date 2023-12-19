package com.play.cert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.play.web.exception.BaseException;

public class CertUtil {
    private static Logger logger = LoggerFactory.getLogger(CertUtil.class);

    public static String getName(File crtFile) {
        FileInputStream file_inputstream = null;
        DataInputStream data_inputstream = null;
        try {
            file_inputstream = new FileInputStream(crtFile);
            data_inputstream = new DataInputStream(file_inputstream);
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            byte[] bytes = new byte[data_inputstream.available()];
            data_inputstream.readFully(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            // 遍历得到所有的证书属性
            if (bais.available() > 0) {
                X509Certificate cert = (X509Certificate) certificatefactory.generateCertificate(bais);
                String name = cert.getSubjectDN().getName();
                if (StringUtils.isNotEmpty(name)) {
                    return name.substring(3, name.indexOf(","));
                }
            }
        } catch (Exception e) {
           // logger.error("获取证书名称错误" + crtFile.getAbsolutePath(), e);
        } finally {
            if (file_inputstream != null) {
                try {
                    file_inputstream.close();
                } catch (IOException e) {
                }
            }
            if (data_inputstream != null) {
                try {
                    data_inputstream.close();
                } catch (IOException e) {
                }
            }
        }
        return "";
    }

    public static String readFileContent(File f) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        String line = null;
        FileInputStream fio = null;
        InputStreamReader isr = null;
        try {
            fio = new FileInputStream(f);
            isr = new InputStreamReader(fio);
            reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            logger.error("读取文件发生错误" + f.getAbsolutePath(), e);
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (Exception e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
        return sb.toString();
    }

    public static List<String> readFileContent2List(File f) {
        BufferedReader reader = null;
        List<String> list = new ArrayList<>();
        String line = null;
        FileInputStream fio = null;
        InputStreamReader isr = null;
        try {
            fio = new FileInputStream(f);
            isr = new InputStreamReader(fio);
            reader = new BufferedReader(isr);
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (Exception e) {
            logger.error("读取文件发生错误" + f.getAbsolutePath(), e);
        } finally {
            if (fio != null) {
                try {
                    fio.close();
                } catch (Exception e) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
        return list;
    }

    /*
     * 获取公钥key的方法（读取.crt认证文件）
     */
    public static List<String> getDomainFrmCrt(String fileName) {
        List<String> domainList = new ArrayList<>();
        FileInputStream fis = null;
        DataInputStream dis = null;
        try {
            fis = new FileInputStream(fileName);
            dis = new DataInputStream(fis);
            CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
            byte[] bytes = new byte[dis.available()];
            dis.readFully(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            X509Certificate cert = null;
            // 遍历得到所有的证书域名

            Collection<List<?>> slist = null;
            if (bais.available() > 0) {
                cert = (X509Certificate) certificatefactory.generateCertificate(bais);
                slist = cert.getSubjectAlternativeNames();
                if (slist != null && !slist.isEmpty()) {
                    for (List<?> l : slist) {
                        domainList.add((String) l.get(1));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取证书里面的域名发生错误" + fileName, e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (Exception e) {
                }
            }
        }
        return domainList;
    }

    /**
     * 按字符缓冲写入 BufferedWriter
     *
     * @param str 写入字符串
     */
    public static void writeFile(String str, String fileName) {

        File f = new File(fileName);
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            writer = new OutputStreamWriter(os);
            bw = new BufferedWriter(writer);
            bw.write(str);
            bw.flush();
        } catch (Exception e) {
            logger.error("写入文件出错发生错误" + fileName, e);
            throw new BaseException("写入文件出错！");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
