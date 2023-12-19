package com.play.spring.extra;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.play.web.permission.PermissionForAdminUser;
import com.play.web.permission.PermissionForManagerUser;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

public class MyFreeMarkerConfigurer extends FreeMarkerConfigurer {
	private Logger logger = LoggerFactory.getLogger(MyFreeMarkerConfigurer.class);

	@Override
	public void afterPropertiesSet() throws IOException, TemplateException {
		super.afterPropertiesSet();
		this.setStaticFunction(this.getConfiguration());
	}

	private void setStaticFunction(Configuration conf) {
		try {
			BeansWrapper wrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_30).build();
			TemplateHashModel staticModels = wrapper.getStaticModels();
			TemplateHashModel statics = (TemplateHashModel) staticModels.get(PermissionForManagerUser.class.getName());
			conf.setSharedVariable("permManagerFn", statics);
			
			statics = (TemplateHashModel) staticModels.get(PermissionForAdminUser.class.getName());
			conf.setSharedVariable("permAdminFn", statics);
		} catch (TemplateModelException e) {
			logger.error("初始化页面功能发生错误", e);
		}
	}
}