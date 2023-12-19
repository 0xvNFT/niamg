package com.play.cert;

public class CertDomainVo {
    private String domainName;
    private String folder;
    private Integer certId;
    private Integer ownCertId;
    private Boolean https;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Integer getCertId() {
        return certId;
    }

    public void setCertId(Integer certId) {
        this.certId = certId;
    }

    public Integer getOwnCertId() {
        return ownCertId;
    }

    public void setOwnCertId(Integer ownCertId) {
        this.ownCertId = ownCertId;
    }

    public Boolean getHttps() {
        return https;
    }

    public void setHttps(Boolean https) {
        this.https = https;
    }

}
