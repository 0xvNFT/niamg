package com.play.job;

import java.util.*;

import com.play.core.StationConfigEnum;
import com.play.web.utils.StationConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.play.service.StationOnlineNumService;
import com.play.service.StationService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserLoginService;
import com.play.web.user.online.OnlineManager;

public class UserOnlineJob {
	private Logger logger = LoggerFactory.getLogger(UserOnlineJob.class);
	private static boolean running = false;
	@Autowired
	private SysUserLoginService loginService;
	@Autowired
	private StationOnlineNumService onlineNumService;
	@Autowired
	private SysUserDegreeService userDegreeService;
	@Autowired
	private StationService stationService;

	public static Map<Long,double[]> fadeDataMap = new HashMap<>();

	public void doExecute() {
		if (running) {
			return;
		}
		running = true;
		try {
	//		logger.error("用户在线状态维护开始");
			List<Long> stationIds = stationService.getActiveIds();
			if (stationIds == null || stationIds.isEmpty()) {
				return;
			}
			try {
				Set<Long> userIds = OnlineManager.getOnlineUserIds();
				List<Long> onlineUserIds = loginService.findAllOnlineIds();
				if (onlineUserIds != null && !onlineUserIds.isEmpty() && userIds != null && !userIds.isEmpty()) {
					List<Long> onLineList = new ArrayList<>();
					for (Long id : userIds) {
						if (!onlineUserIds.contains(id)) {
							onLineList.add(id);
						}
					}
					loginService.updateOnline(onLineList);// 更新在线

					for (Iterator<Long> it = onlineUserIds.iterator(); it.hasNext();) {
						if (userIds.contains(it.next())) {
							it.remove();
						}
					}
					loginService.updateOffline(onlineUserIds);// 更新下线
				}
	//			logger.error("完成了用户在线状态维护");
			} catch (Exception e) {
				logger.error("用户在线状态维护发生错误", e);
			}

			try {// 在线人数处理
		//		logger.error("开始在线人数维护");
				onlineNumService.handleOnlineNum(stationIds);
		//		logger.error("完成了在线人数维护");
			} catch (Exception e) {
				logger.error("在线人数维护发生错误", e);
			}

			try {
				Calendar c = Calendar.getInstance();
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);
				if (minute % 5 == 0 && second < 6) {
		//			logger.error("重新统计会员等级成员数量开始");
					for (Long stationId : stationIds) {
						userDegreeService.reStatMemberAccount(stationId);
					}
		//			logger.error("完成重新统计会员等级成员数量");
				}
			} catch (Exception e) {
				logger.error("重新统计会员等级成员数量发生错误", e);
			}

			Random random = new Random();
			for (Long stationId : stationIds) {
				double[] params = fadeDataMap.computeIfAbsent(stationId, k -> new double[]{0, 0, 0});
				String string = StationConfigUtil.get(stationId, StationConfigEnum.online_count_fake);
				Integer onlineNum = OnlineManager.getOnlineCount(stationId, null);
				if (StringUtils.isNotEmpty(string)) {
					String[] strings = string.split(",");
					try {
						params[0] = Integer.parseInt(strings[0]) + onlineNum + random.nextInt(50);
						params[1] = Double.parseDouble(strings[1]);
						params[2] = Double.parseDouble(strings[2]);
					} catch (Exception ignored) {
					}
				}
			}
		} finally {
			running = false;
		}
	}

}