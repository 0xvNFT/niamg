package com.play.model;

import com.play.orm.jdbc.annotation.Column;
import com.play.orm.jdbc.annotation.Table;

/**
 * 系统短信网关配置表
 */
@Table(name = "system_sms_config")
public class SystemSmsConfig {

    @Column(name = "id", primarykey = true)
    private Long id;
    /**
     * 国家代码
     */
    @Column(name = "country_code")
    private String countryCode;

    /**
     * 国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 语种代号
     */
    @Column(name = "language")
    private String language;

    /**
     * appKey
     */
    @Column(name = "app_key")
    private String appKey;

    /**
     * appSecret
     */
    @Column(name = "app_secret")
    private String appSecret;

    /**
     * appCode
     */
    @Column(name = "app_code")
    private String appCode;

    /**
     * 短信内容模板
     */
    @Column(name = "content")
    private String content;

    /**
     * 网关后台登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 网关后台登录帐号
     */
    @Column(name = "account")
    private String account;

    /**
     * 请求网关地址
     */
    @Column(name = "gateway_url")
    private String gatewayUrl;

    @Column(name = "status")
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
