package com.play.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.play.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.cert.CertDomainVo;
import com.play.cert.CertUtil;
import com.play.cert.CertVo;
import com.play.cert.FileAppendUtil;
import com.play.common.utils.DateUtil;
import com.play.service.StationDomainService;
import com.play.service.SysCertificateService;
import com.play.web.exception.BaseException;
import com.play.web.utils.ShellUtil;

@Service
public class SysCertificateServiceImpl implements SysCertificateService {
    private Logger logger = LoggerFactory.getLogger(SysCertificateService.class);
    @Autowired
    private StationDomainService stationDomainService;

    @Override
    public String uploadCertificate(MultipartFile crt1, MultipartFile crt2, MultipartFile keyFile,
                                    MultipartFile csrFile, Integer certId) {

        if (certId == null || certId < 1) {
            throw new BaseException("请选择需要修改的证书！");
        }

        if ((crt1 == null || crt1.getSize() == 0) && (crt2 == null || crt2.getSize() == 0)
                && (keyFile == null || keyFile.getSize() == 0) && (csrFile == null || csrFile.getSize() == 0)) {
            throw new BaseException("至少上传一个文件！");
        }
        if (crt2 != null && crt2.getSize() > 0) {
            if (crt1 == null || crt1.getSize() == 0) {
                throw new BaseException("请上传证书1！");
            }
        }
        File f = null;
        File desc = null;
        try {
            String backSuffix = DateUtil.formatDate(new Date(), "yyyyMMddHHmmss");
            String file = Constants.CERT_DIRECTORY + "server" + certId + ".crt";

            if (crt1 != null && crt1.getSize() > 0) {
                f = new File(file);
                desc = new File(file + backSuffix);
                f.renameTo(desc);
                crt1.transferTo(f);
            }
            if (crt2 != null && crt2.getSize() > 0) {
                String content = FileAppendUtil.readFileContent(crt2.getInputStream());
                FileAppendUtil.write(file, content);
            }
            if (keyFile != null && keyFile.getSize() > 0) {
                file = Constants.CERT_DIRECTORY + "server" + certId + ".key";
                f = new File(file);
                desc = new File(file + backSuffix);
                f.renameTo(desc);
                keyFile.transferTo(f);
            }
            if (csrFile != null && csrFile.getSize() > 0) {
                file = Constants.CERT_DIRECTORY + "server" + certId + ".csr";
                f = new File(file);
                desc = new File(file + backSuffix);
                f.renameTo(desc);
                csrFile.transferTo(f);
            }
            return ShellUtil.updateSslFile(certId);
        } catch (Exception e) {
            logger.error("上传文件发生错误！", e);
            if (desc != null && f != null) {
                desc.renameTo(f);
            }
            throw new BaseException("上传文件发生错误！");
        }
    }

    @Override
    public String saveConfig(String data) {
        Map<Integer, String> configMap = segDomainConfig(data);
        List<String> nginxConfigs = CertUtil.readFileContent2List(new File(nginxConfigFile));
        List<String> newNginxConfigs = getNginxConfigs(nginxConfigs, configMap);
        wirteToConfigFile(newNginxConfigs);
        return ShellUtil.updateNginxConf();
    }

    private void wirteToConfigFile(List<String> newNginxConfigs) {
        StringBuilder sb = new StringBuilder();
        for (String s : newNginxConfigs) {
            sb.append(s).append("\n");
        }
     //   logger.error("新的配置内容：\n" + sb);
        CertUtil.writeFile(sb.toString(), nginxConfigFile);
    }

    private List<String> getNginxConfigs(List<String> nginxConfigs, Map<Integer, String> configMap) {
        boolean begin = false;
        boolean end = false;
        List<String> newNginxConfigs = new ArrayList<>();
        try {
            List<String> serverTemps = CertUtil
                    .readFileContent2List(ResourceUtils.getFile("classpath:https_nginx.templet"));
            for (String nc : nginxConfigs) {
                if (StringUtils.equals("#sign_servername_https_begin", nc.trim())) {
                    begin = true;
                    newNginxConfigs.add(nc);
                }
                if (StringUtils.equals("#sign_servername_https_end", nc.trim())) {
                    for (int i = 1; i < 30; i++) {
                        if (configMap.containsKey(i) && StringUtils.isNotEmpty(configMap.get(i))) {
                            newNginxConfigs.add("server{");
                            newNginxConfigs.add(configMap.get(i));
                            newNginxConfigs.add("ssl_certificate " + Constants.CERT_DIRECTORY + "server" + i + ".crt;");
                            newNginxConfigs.add("ssl_certificate_key " + Constants.CERT_DIRECTORY + "server" + i + ".key;");
                            newNginxConfigs.addAll(serverTemps);
                        }
                    }
                    end = true;
                }
                if (!begin) {
                    newNginxConfigs.add(nc);
                }
                if (end) {
                    if (nc.trim().startsWith("server_name") && nc.trim().endsWith("#sign_servername_http2https")) {
                        newNginxConfigs.add(configMap.get(http2Https));
                    } else {
                        newNginxConfigs.add(nc);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("保存配置信息发生错误", e);
            throw new BaseException(e.getMessage());
        }
        return newNginxConfigs;
    }

    private Map<Integer, String> segDomainConfig(String data) {
        JSONArray arr = JSON.parseArray(data);
        if (arr == null || arr.isEmpty()) {
            throw new BaseException("需要保存的数据为空");
        }
        Map<Integer, StringBuilder> configMap = new HashMap<>();
        JSONObject obj = null;
        StringBuilder sb = null;
        Integer certId = null;
        Boolean https = null;
        for (int i = 0, len = arr.size(); i < len; i++) {
            obj = arr.getJSONObject(i);
            if (obj.containsKey("ownCertId")) {
                certId = obj.getInteger("ownCertId");
                if (certId == null || certId == 0) {
                    continue;
                }
                sb = configMap.get(certId);
                if (sb == null) {
                    sb = new StringBuilder();
                    configMap.put(certId, sb);
                }
                sb.append(obj.getString("domainName")).append(" ");
            }
            if (obj.containsKey("https")) {
                https = obj.getBoolean("https");
                if (https == null || !https) {
                    continue;
                }
                sb = configMap.get(http2Https);
                if (sb == null) {
                    sb = new StringBuilder();
                    configMap.put(http2Https, sb);
                }
                sb.append(obj.getString("domainName")).append(" ");
            }
        }
        Map<Integer, String> map = new HashMap<>();
        for (Integer i : configMap.keySet()) {
            sb = configMap.get(i);
            sb.deleteCharAt(sb.length() - 1);
            if (i.intValue() == http2Https) {
                map.put(i, "server_name " + sb.toString() + "; #sign_servername_http2https");
            } else {
                map.put(i, "server_name " + sb.toString() + "; #sign_servername_https" + i);
            }
        }
        return map;
    }

    private static Pattern pattern = Pattern.compile("#sign_servername_https(\\d+)$");
    private static String nginxConfigFile = Constants.CERT_DIRECTORY + "nginx.conf";
    private static int http2Https = 10000;

    @Override
    public List<CertDomainVo> getDomains() {
        Map<String, String> map = stationDomainService.getAllStationDomain();
        if (map == null || map.isEmpty()) {
            return null;
        }
        List<CertDomainVo> list = new ArrayList<>();
        Map<String, CertDomainVo> cdMap = createCertDomainMap(map, list);
        setInfoFromNginxConfig(list, cdMap, map);
        return list;
    }

    private void setInfoFromNginxConfig(List<CertDomainVo> list, Map<String, CertDomainVo> cdMap,
                                        Map<String, String> map) {
        List<String> nginxConfigs = CertUtil.readFileContent2List(new File(nginxConfigFile));
        Matcher matcher = null;
        String[] domains = null;
        CertDomainVo vo = null;
        for (String con : nginxConfigs) {
            con = con.trim();
            if (con.startsWith("server_name")) {
                if (con.endsWith("#sign_servername_http2https")) {
                    // http调整到https
                    domains = con.substring(11, con.indexOf(";")).trim().split(" ");
                    for (String d : domains) {
                        d = d.trim();
                        if (StringUtils.isEmpty(d)) {
                            continue;
                        }
                        vo = cdMap.get(d);
                        if (vo == null) {
                            vo = createCertDomainVo(null, null, d, map);
                            vo.setHttps(true);
                            cdMap.put(d, vo);
                            list.add(vo);
                        } else {
                            vo.setHttps(true);
                        }
                    }
                } else {
                    // 所属证书：域名在哪本证书上，证书是以1,2,3,4...编号的，
                    matcher = pattern.matcher(con);
                    if (matcher.find()) {
                        int ownCertId = NumberUtils.toInt(matcher.group(1));
                        domains = con.substring(11, con.indexOf(";")).trim().split(" ");
                        for (String d : domains) {
                            d = d.trim();
                            if (StringUtils.isEmpty(d)) {
                                continue;
                            }
                            vo = cdMap.get(d);
                            if (vo == null) {
                                vo = createCertDomainVo(null, ownCertId, d, map);
                                cdMap.put(d, vo);
                                list.add(vo);
                            } else {
                                vo.setOwnCertId(ownCertId);
                            }
                        }
                    }
                }
            }
        }
    }

    private CertDomainVo createCertDomainVo(Integer certId, Integer ownCertId, String d, Map<String, String> map) {
        CertDomainVo vo = new CertDomainVo();
        vo.setCertId(certId);
        vo.setOwnCertId(ownCertId);
        vo.setDomainName(d);
        if (map.containsKey(d)) {
            vo.setFolder(map.get(d));
        } else {
            if (d.startsWith("www.") && map.containsKey(d.substring(4))) {
                vo.setFolder(map.get(d.substring(4)));
            }
        }
        return vo;
    }

    private Map<String, CertDomainVo> createCertDomainMap(Map<String, String> map, List<CertDomainVo> list) {
        Map<String, CertDomainVo> cdMap = new HashMap<>();
        CertDomainVo vo = null;
        List<CertVo> certFiles = getCertVos();
        List<String> domainList = null;
        if (certFiles != null) {
            for (CertVo v : certFiles) {
                domainList = CertUtil.getDomainFrmCrt(v.getFileName());
                if (domainList != null && !domainList.isEmpty()) {
                    for (String d : domainList) {
                        if (StringUtils.contains(d, "yunji9.com")) {
                            continue;
                        }
                        vo = createCertDomainVo(v.getId(), null, d, map);
                        cdMap.put(d, vo);
                        list.add(vo);
                    //    System.out.println(d);
                    }
                }
            }
        }

        return cdMap;
    }

    private List<CertVo> getCertVos() {
        File file = new File(Constants.CERT_DIRECTORY);
        File[] subFile = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.matches("server[\\d]+\\.crt");
            }
        });
        if (subFile == null || subFile.length == 0) {
            return null;
        }
        List<CertVo> clist = new ArrayList<>();
        CertVo v = null;
        String name = null;
        for (File f : subFile) {
            v = new CertVo();
            name = f.getName();
            v.setId(NumberUtils.toInt(name.substring(6, name.indexOf("."))));
            v.setFileName(f.getAbsolutePath());
            clist.add(v);
        }
        return clist;
    }
}
