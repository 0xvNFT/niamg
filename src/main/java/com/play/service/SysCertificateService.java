package com.play.service;

import com.play.cert.CertDomainVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SysCertificateService {

    String uploadCertificate(MultipartFile crt1, MultipartFile crt2, MultipartFile keyFile, MultipartFile csrFile,
                             Integer certId);

    List<CertDomainVo> getDomains();

    String saveConfig(String data);

}
