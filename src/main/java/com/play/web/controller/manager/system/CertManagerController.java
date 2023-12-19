package com.play.web.controller.manager.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.play.common.Constants;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.play.cert.CertUtil;
import com.play.cert.CertVo;
import com.play.common.SystemConfig;
import com.play.core.LogTypeEnum;
import com.play.service.SysCertificateService;
import com.play.web.annotation.NeedLogView;
import com.play.web.annotation.Permission;
import com.play.web.controller.manager.ManagerBaseController;
import com.play.web.exception.BaseException;

/**
 * 证书管理
 */
@Controller
@RequestMapping(SystemConfig.CONTROL_PATH_MANAGER + "/cert")
public class CertManagerController extends ManagerBaseController {

    private Logger logger = LoggerFactory.getLogger(CertManagerController.class);
    @Autowired
    private SysCertificateService certificateService;

    @Permission("zk:cert")
    @RequestMapping("/index")
    public String index() {
        return returnPage("/cert/certIndex");
    }

    @Permission("zk:cert")
    @RequestMapping("/add")
    public String add(Map<String, Object> map, String certId) {
        map.put("certId", certId);
        return returnPage("/cert/certAdd");
    }

    /**
     * 查询证书列表
     */
    @Permission("zk:cert")
    @RequestMapping("/list")
    @ResponseBody
    @NeedLogView(value = "证书管理列表", type = LogTypeEnum.VIEW_LIST)
    public void list() {
        //读取/usr/local/nginx/conf 目录下所有.key文件
        File directory = new File(Constants.CERT_DIRECTORY);
        File[] subFile = directory.listFiles((dir, name) -> name.matches("server[\\d]+\\.key"));
        List<CertVo> list = new ArrayList<>();
        if (subFile != null && subFile.length > 0) {
            for (File f : subFile) {
                CertVo certVo = new CertVo();
                String name = f.getName();
                certVo.setId(NumberUtils.toInt(name.substring(6, name.indexOf("."))));
                certVo.setKeyFile(name);
                f = new File(directory, "server" + certVo.getId() + ".crt");
                if (f.exists()) {
                    certVo.setFileName("server" + certVo.getId() + ".crt");
                    certVo.setName(CertUtil.getName(f));
                }
                if (new File(directory, "server" + certVo.getId() + ".csr").exists()) {
                    certVo.setCsrFile("server" + certVo.getId() + ".csr");
                }
                list.add(certVo);
            }
        }
        renderJSON(list);
    }

    @Permission("zk:cert")
    @RequestMapping("/viewCsr")
    @NeedLogView(value = "证书管理编辑详情", type = LogTypeEnum.VIEW_DETAIL)
    public String viewCsr(Integer id, Map<String, Object> map) {
        if (id == null || id < 1) {
            map.put("certText", "请选择证书");
            return returnPage("/cert/certDetail");
        }
        File file = new File(Constants.CERT_DIRECTORY + "server" + id + ".csr");
        if (file.exists()) {
            map.put("certText", CertUtil.readFileContent(file));
            return returnPage("/cert/certDetail");
        } else {
            map.put("certText", "csr文件不存在");
            return returnPage("/cert/certDetail");
        }
    }

    /**
     * 上传证书
     *
     * @param crt1    证书1
     * @param crt2    证书2
     * @param keyFile key文件
     * @param csrFile csr文件
     * @param certId
     */
    @Permission("zk:cert")
    @RequestMapping(value = "/upload")
    @ResponseBody
    public void upload(MultipartFile crt1, MultipartFile crt2, MultipartFile keyFile, MultipartFile csrFile,
                       Integer certId) {
        certificateService.uploadCertificate(crt1, crt2, keyFile, csrFile, certId);
        renderSuccess();
    }

    /**
     * 删除证书
     *
     * @param id
     * @param map
     */
    @Permission("zk:cert")
    @RequestMapping(value = "delete")
    @ResponseBody
    public void delete(Integer id, Map<String, Object> map) {
        if (id == null || id < 1) {
            throw new BaseException("请选择证书");
        }
        String msg = "";
        // 删除CSR文件
        try {
            File csrFile = new File(Constants.CERT_DIRECTORY + "server" + id + ".csr");
            if (csrFile.exists() && csrFile.delete()) {
                msg += "删除server" + id + ".csr成功!";
            } else {
                msg += "删除server" + id + ".csr失败!";
            }
            File keyFile = new File(Constants.CERT_DIRECTORY + "server" + id + ".key");
            if (keyFile.exists() && keyFile.delete()) {
                msg += "删除server" + id + ".key成功!";
            } else {
                msg += "删除server" + id + ".key失败!";
            }
            File crtFile = new File(Constants.CERT_DIRECTORY + "server" + id + ".crt");
            if (crtFile.exists() && crtFile.delete()) {
                msg += "删除server" + id + ".crt成功!";
            } else {
                msg += "删除server" + id + ".crt失败!";
            }
        } catch (Exception e) {
            msg = "文件删除失败!";
        }
        map.put("success", true);
        map.put("msg", msg);
        renderJSON(map);
    }

    @Permission("zk:cert")
    @RequestMapping(value = "/newUpload")
    @ResponseBody
    public void newUpload(MultipartFile crt1, MultipartFile crt2, MultipartFile keyFile, MultipartFile csrFile,
                          Integer certId) {
        if (certId == null || certId < 1) {
            throw new BaseException("请选择证书");
        }
        // 判断证书是否存在
        File file = new File(Constants.CERT_DIRECTORY + "server" + certId + ".crt");
        if (file.exists()) {
            throw new BaseException("证书存在，请输入新的证书ID");
        }
        certificateService.uploadCertificate(crt1, crt2, keyFile, csrFile, certId);
        renderSuccess();
    }

    @Permission("zk:cert")
    @RequestMapping(value = "/download")
    public void download(String fileName, HttpServletResponse response) {
        InputStream fis = null;
        OutputStream toClient = null;
        try {
            // fileName是指欲下载的文件名称。
            File file = new File(Constants.CERT_DIRECTORY + fileName);
            // 以流的形式下载文件。
            fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
        } catch (IOException ex) {
            logger.error("下载文件发生错误" + fileName, ex);
        } finally {
            try {
                fis.close();
                toClient.close();
            } catch (Exception e) {

            }
        }
    }
}
