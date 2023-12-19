package com.play.web.controller.front.usercenter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.play.web.var.GuestTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.play.model.StationDummyData;
import com.play.model.StationRedPacket;
import com.play.model.StationRedPacketRecord;
import com.play.model.SysUser;
import com.play.service.StationDummyDataService;
import com.play.service.StationRedPacketService;
import com.play.web.annotation.NotNeedLogin;
import com.play.web.controller.front.FrontBaseController;
import com.play.web.exception.BaseException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.HidePartUtil;
import com.play.web.var.LoginMemberUtil;
import com.play.web.var.SystemUtil;

/**
 * 抢红包功能
 * 
 * @author macair
 *
 */
@Controller
@RequestMapping("/userCenter/redpacket")
public class UserRedPacketController extends FrontBaseController {
	@Autowired
	private StationRedPacketService redPacketService;
	@Autowired
	private StationDummyDataService dummyDataService;

	/**
	 * 获取当前红包
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/cur")
	public void cur() {
		StationRedPacket rp = redPacketService.getCurrentRedPacketNew();
		JSONObject obj = new JSONObject();
		SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		if (rp != null) {
			obj.put("id", rp.getId());
			obj.put("title", rp.getTitle());
			obj.put("beginDatetime", time.format(rp.getBeginDatetime().getTime()));
			obj.put("endDatetime", time.format(rp.getEndDatetime().getTime()));
			obj.put("stat", "0");
		} else {
			obj.put("stat", "-404");
		}
		renderJSON(obj.toJSONString());
	}

	/**
	 * 获取当前红包
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/curNew")
	public void curNew(Long packetId) {
		super.renderJSON(JSONObject.toJSONString(packetId == null ? redPacketService.getCurrentRedPacketNew() :
				redPacketService.getRedPacket(packetId), SerializerFeature.WriteDateUseDateFormat));
	}

	/**
	 * 抢红包
	 * 
	 * @param redPacketId
	 */
	@ResponseBody
	@RequestMapping("/grab")
	public void grab(Long redPacketId) {
		SysUser user = LoginMemberUtil.currentUser();
		if(GuestTool.isGuest(user)) {
			 throw new BaseException(BaseI18nCode.gusetPleaseRegister);
		}
		JSONObject obj = new JSONObject();
		obj.put("redPacketMoney", redPacketService.grabRedPacketPlan(redPacketId));
		renderJSON(obj);
	}

	/**
	 * 抢到红包的记录
	 * 
	 * @param redPacketId
	 */
	@NotNeedLogin
	@ResponseBody
	@RequestMapping("/record")
	public void record(Long redPacketId, String username, Integer limit) {
		SysUser user = LoginMemberUtil.currentUser();
		Long stationId = SystemUtil.getStationId();
		List<StationRedPacketRecord> mrrList = redPacketService.getListBysidAndRid(stationId, redPacketId,
				username != null ? username : (user != null ? user.getUsername() : null), limit);
		List<StationDummyData> dataList = dummyDataService.getList(stationId, StationDummyData.REG_BAG, new Date());
		int idx = 0;
		if (dataList != null && dataList.size() > 0) {
			for (StationDummyData f : dataList) {
				if (idx < 200) {// 防止红包页面卡顿，控制假数据条数
					StationRedPacketRecord mrr = new StationRedPacketRecord();
					mrr.setUsername(f.getUsername());
					if (f.getWinTime() != null) {
						mrr.setCreateDatetime(f.getWinTime());
					}
					if (f.getWinMoney() != null) {
						mrr.setMoney(f.getWinMoney());
					}
					mrrList.add(mrr);
				}
				idx++;
			}
		}
		Collections.sort(mrrList, new Comparator<StationRedPacketRecord>() {
			@Override
			public int compare(StationRedPacketRecord o1, StationRedPacketRecord o2) {
				return o2.getCreateDatetime().compareTo(o1.getCreateDatetime());
			}
		});
		for (int i = 0; i < mrrList.size(); i++) {
			mrrList.get(i).setUsername(HidePartUtil.removeAllKeep2(mrrList.get(i).getUsername()));
		}
		super.renderJSON(JSONObject.toJSONString(mrrList, SerializerFeature.WriteDateUseDateFormat));
	}
}
