package com.play.system.init;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.play.service.StationService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.parser.ParserConfig;
import com.play.common.SystemConfig;
import com.play.common.ip.country.CNIpTool;
import com.play.spring.config.EncryptYamlPropertiesFactoryBean;

@Component
public class SystemConfigurerInit {
	private Logger logger = LoggerFactory.getLogger(SystemConfigurerInit.class);
	@Resource
	private EncryptYamlPropertiesFactoryBean configProperties;

	@Autowired
	private StationService stationService;

	@PostConstruct
	public void init() {
		ParserConfig.getGlobalInstance().setSafeMode(true);
	//	logger.info("系统初始化配置信息");
		// 总控域名
		SystemConfig.SYS_CONTROL_DOMAIN = configProperties.getPropertyDefault("sys.control.domain",
				SystemConfig.SYS_CONTROL_DOMAIN);
		SystemConfig.SYS_GENERAL_DOMAIN = configProperties.getPropertyDefault("sys.general.domain",
				SystemConfig.SYS_GENERAL_DOMAIN);
		// 总控IP白名单
		SystemConfig.IP_WHITE_MANAGER = configProperties.getByKeyPref("ip.white.manager");
		// 合作商后台总控IP白名单
		SystemConfig.IP_WHITE_PARTNER = configProperties.getByKeyPref("ip.white.partner");
		// 站点后台超级管理员IP白名单
		SystemConfig.IP_WHITE_ADMIN = configProperties.getByKeyPref("ip.white.admin");
		// 推送或者接收数据的ip
		SystemConfig.IP_WHITE_RECEIVE = configProperties.getByKeyPref("ip.white.receive");
		// 开发模式
		SystemConfig.SYS_MODE_DEVELOP = BooleanUtils.toBoolean(configProperties.getProperty("sys.mode.develop"));
		SystemConfig.SHELL_FILE_PATH = configProperties.getPropertyDefault("shell.file.path", "");
		SystemConfig.ENCRYPT_KEY_DATA = configProperties.getPropertyDefault("encrypt.key.data", "");
		SystemConfig.ENCRYPT_KEY_PAY = configProperties.getPropertyDefault("encrypt.key.pay", "");

		initAccountAndPassword();
		initAllCdnIpList();

		// 大陆IP库初始化，方式一：
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				CNIpTool.init();
//			}
//		}).start();

		// 大陆IP库初始化，方式二：
		Runnable CNIpRunnable = () -> CNIpTool.init();
		CNIpRunnable.run();

		// 站点配置（StationConfig）间隔15秒定时刷新
		new Thread(new InitStationConfig()).start();

	}

	private void initAccountAndPassword() {
		// api租户管理后台账号 总控使用
		SystemConfig.PARTNER_ROOT = configProperties.getPropertyDefault("username.partner.root",
				SystemConfig.PARTNER_ROOT);
		SystemConfig.PARTNER_ROOT_PWD = configProperties.getPropertyDefault("username.partner.rootPwd",
				SystemConfig.PARTNER_ROOT_PWD);
		// api租户管理后台使用
		SystemConfig.PARTNER_ADMIN = configProperties.getPropertyDefault("username.partner.admin",
				SystemConfig.PARTNER_ADMIN);
		SystemConfig.PARTNER_ADMIN_PWD = configProperties.getPropertyDefault("username.partner.adminPwd",
				SystemConfig.PARTNER_ADMIN_PWD);
		// 站点后台账号 总控使用
		SystemConfig.ADMIN_ROOT = configProperties.getPropertyDefault("username.admin.root", SystemConfig.ADMIN_ROOT);
		SystemConfig.ADMIN_ROOT_PWD = configProperties.getPropertyDefault("username.admin.rootPwd",
				SystemConfig.ADMIN_ROOT_PWD);
		// 站点后台账号 合作商使用
		SystemConfig.ADMIN_PARTNER = configProperties.getPropertyDefault("username.admin.partner",
				SystemConfig.ADMIN_PARTNER);
		SystemConfig.ADMIN_PARTNER_PWD = configProperties.getPropertyDefault("username.admin.partnerPwd",
				SystemConfig.ADMIN_PARTNER_PWD);
		// 站点后台账号 站点使用
		SystemConfig.ADMIN = configProperties.getPropertyDefault("username.admin.user", SystemConfig.ADMIN);
		SystemConfig.ADMIN_PWD = configProperties.getPropertyDefault("username.admin.userPwd", SystemConfig.ADMIN_PWD);

	}

	private void initAllCdnIpList() {
		try {
			String ips = FileUtils.readFileToString(new ClassPathResource("cdn_ips.txt").getFile(), "UTF-8");
			if (StringUtils.isNotEmpty(ips)) {
				String[] ipArr = ips.split("\\n");
				StringBuilder sb = new StringBuilder(",");
				for (String ip : ipArr) {
					ip = StringUtils.trim(ip);
					if (ip.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}")) {
						sb.append(ip).append(",");
					}
				}
				SystemConfig.ALL_CDN_IP_LIST = sb.toString();
			}
		} catch (IOException e) {
			logger.error("读取cdnIps发生错误", e);
		}
	}
}
