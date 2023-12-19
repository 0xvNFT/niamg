package com.play.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.play.common.SystemConfig;
import com.play.service.ModelCreatorService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.utils.ControllerRender;

@Controller
public class ModelCreatorController {
	@Autowired
	private ModelCreatorService creatorService;
//http://localhost:8180/caiwu/manager/createModel.do?tableName=111&path=/Volumes/MYTEST/svn/caiwu
	
	@NotNeedLogin
	@RequestMapping("/manager/createModel")
	@ResponseBody
	public void createModel(String tableName, String path) {
		if (!SystemConfig.SYS_MODE_DEVELOP) {
			ControllerRender.renderText("正是环境不能执行该命令");
			return;
		}
		String output = creatorService.createModel(tableName, path);
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		map.put("code", output);
		ControllerRender.renderJSON(map);
	}

}
