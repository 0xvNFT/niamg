package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.play.common.utils.ProxyModelUtil;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.core.LogTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationDailyRegisterDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDepositDao;
import com.play.model.StationDailyRegister;
import com.play.model.SysUser;
import com.play.model.vo.StationDailyRegisterVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDailyRegisterService;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.export.ExportToCvsUtil;

/**
 * 站点每日注册信息表
 *
 * @author admin
 *
 */
@Service
public class StationDailyRegisterServiceImpl implements StationDailyRegisterService {

	@Autowired
	private StationDailyRegisterDao stationDailyRegisterDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserDepositDao userDepositDao;
	@Autowired
	StationDailyRegisterService stationDailyRegisterService;

	@Override
	public void registerHandle(Long stationId, Long partnerId, Integer userType, boolean isPromo) {
		Date today = new Date();
		StationDailyRegister r = stationDailyRegisterDao.getDailyByDay(stationId, today);
		boolean isNew = false;
		if (r == null) {
			isNew = true;
			r = new StationDailyRegister();
			r.setStationId(stationId);
			r.setStatDate(today);
			r.setRegisterNum(1);
			r.setMemberNum(0);
			r.setProxyNum(0);
			r.setFirstDeposit(0);
			r.setSecDeposit(0);
			r.setPromoMember(0);
			r.setPromoProxy(0);
			r.setDeposit(0);
			r.setDepositMoney(BigDecimal.ZERO);
			//全局代理模式只能添加代理
			this.addProxyOrMember(isNew,stationId,r,userType,isPromo);
			stationDailyRegisterDao.save(r);
		} else {
			r.setRegisterNum(r.getRegisterNum() + 1);
			//全局代理模式只能添加代理
			this.addProxyOrMember(isNew,stationId,r,userType,isPromo);
			stationDailyRegisterDao.updateRegister(r);
		}
	}

	private void addProxyOrMember(boolean isNew,Long stationId,StationDailyRegister r,Integer userType, boolean isPromo){
		int proxyModel = ProxyModelUtil.getProxyModel(stationId);// 代理模式
		if (ProxyModelUtil.isAllProxy(proxyModel)) {
			if(isNew){
				r.setProxyNum(1);
				if (isPromo) {
					r.setPromoProxy(1);
				}
			}else {
				r.setProxyNum(r.getProxyNum()+1);
				if (isPromo) {
					r.setPromoProxy(r.getPromoProxy()+1);
				}
			}

		} else {
			if (userType == UserTypeEnum.PROXY.getType()) {
				if(isNew){
					r.setProxyNum(1);
					if (isPromo) {
						r.setPromoProxy(1);
					}
				}else {
					r.setProxyNum(r.getProxyNum()+1);
					if (isPromo) {
						r.setPromoProxy(r.getPromoProxy()+1);
					}
				}

			} else {
				if(isNew){
					r.setMemberNum(1);
					if (isPromo) {
						r.setPromoMember(1);
					}
				}else {
					r.setMemberNum(r.getMemberNum()+1);
					if (isPromo) {
						r.setPromoMember(r.getPromoMember()+1);
					}
				}

			}
		}

	}

	@Override
	public Page<StationDailyRegister> getPage(Long stationId, Date begin, Date end) {
		return stationDailyRegisterDao.getPage(stationId, begin, end);
	}

	@Override
	public Map dailyChartsRegisterRepot(Long stationId, Date begin, Date end){
		Map<String,int[]> map = new HashMap();

		map.put("firstDeposit",new int[3]);//第一次从前人数
		map.put("officialRe",new int[3]);//官网注册人数
		map.put("proxyPromo",new int[3]);//代理推广人数
		map.put("secDeposit",new int[3]);//第二次从前人数

		Page<StationDailyRegister> page = stationDailyRegisterService.getPage(stationId, begin, end);
		if (page.hasRows()){
			page.getRows().forEach(stationDailyRegister -> {
				int dateMargin = DateUtil.getDayMargin(stationDailyRegister.getStatDate(),end);
				map.get("secDeposit")[dateMargin] = stationDailyRegister.getSecDeposit();
				map.get("proxyPromo")[dateMargin] = stationDailyRegister.getPromoProxy()+stationDailyRegister.getPromoMember();
				map.get("firstDeposit")[dateMargin] = stationDailyRegister.getFirstDeposit();
				map.get("officialRe")[dateMargin] = stationDailyRegister.getRegisterNum() - stationDailyRegister.getPromoProxy() - stationDailyRegister.getPromoMember();
			});
		}

		return map;
	}

	@Override
	public StationDailyRegister getDailyData(Long stationId, Date date) {
		StationDailyRegister dailyRegister = stationDailyRegisterDao.getDailyByDay(stationId, date);
		if (dailyRegister == null) {
			dailyRegister = new StationDailyRegister();
			dailyRegister.setMemberNum(0);
			dailyRegister.setProxyNum(0);
			dailyRegister.setRegisterNum(0);
			dailyRegister.setFirstDeposit(0);
			dailyRegister.setSecDeposit(0);
		}
		return dailyRegister;
	}

	@Override
	public void export(Long stationId, Date begin, Date end) {
		List<StationDailyRegister> list = stationDailyRegisterDao.exportList(stationId, begin, end);
		String title = I18nTool.getMessage(BaseI18nCode.dailyRegReport)+ "(" + DateUtil.toDateStr(begin) + I18nTool.getMessage(BaseI18nCode.until) + DateUtil.toDateStr(end) + ")";
		String[] rowsName = new String[] { I18nTool.getMessage(BaseI18nCode.stationSerialNumber), I18nTool.getMessage(BaseI18nCode.dateTime), I18nTool.getMessage(BaseI18nCode.registAllPer), I18nTool.getMessage(BaseI18nCode.regisVipNum), I18nTool.getMessage(BaseI18nCode.regProxyNum),
				I18nTool.getMessage(BaseI18nCode.firstPayNum), I18nTool.getMessage(BaseI18nCode.secChargeNum), I18nTool.getMessage(BaseI18nCode.thirdChargeNum), I18nTool.getMessage(BaseI18nCode.payCount), I18nTool.getMessage(BaseI18nCode.stationInputMoney) };
		List<Object[]> dataList = new ArrayList<>();
		Object[] objs;
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				objs = new Object[rowsName.length];
				objs[1] = DateUtil.toDateStr(list.get(i).getStatDate());
				objs[2] = list.get(i).getProxyNum() + list.get(i).getMemberNum();
				objs[3] = list.get(i).getMemberNum();
				objs[4] = list.get(i).getProxyNum();
				objs[5] = list.get(i).getFirstDeposit();
				objs[6] = list.get(i).getSecDeposit();
				objs[7] = list.get(i).getThirdDeposit();
				objs[8] = list.get(i).getDeposit();
				objs[9] = list.get(i).getDepositMoney();
				dataList.add(objs);
			}
		}
		ExportToCvsUtil.export(title, rowsName, dataList);
		LogUtils.log("导出" + title, LogTypeEnum.EXPORT);
	}

	@Override
	public Page<StationDailyRegisterVo> getPageByproxyName(Long stationId, Date begin, Date end, String proxyName,
			String agentName) {
		Long proxyId = null;
		boolean isRecommend = false;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
			proxyId = proxy.getId();
		}
		// 获取站点注册报表
		Page<StationDailyRegisterVo> page = userDao.getPageByProxy(stationId, begin, end, proxyId, agentName,
				isRecommend);
		// 站点首充
		List<StationDailyRegisterVo> firstDeposit = userDepositDao.getDailyDepositByProxy(stationId, begin, end,
				proxyId, 1, agentName, isRecommend);
		// 站点二充
		List<StationDailyRegisterVo> secDeposit = userDepositDao.getDailyDepositByProxy(stationId, begin, end, proxyId,
				2, agentName, isRecommend);
		// 站点三充
		List<StationDailyRegisterVo> thirdDeposit = userDepositDao.getDailyDepositByProxy(stationId, begin, end,
				proxyId, 3, agentName, isRecommend);
		page.getRows().forEach(x -> {
			x.setFirstDeposit(0);
			x.setSecDeposit(0);
			x.setThirdDeposit(0);
			firstDeposit.forEach(y -> {
				if (x.getStatDate().equals(y.getStatDate())) {
					x.setFirstDeposit(y.getFirstDeposit());
				}
			});
			secDeposit.forEach(y -> {
				if (x.getStatDate().equals(y.getStatDate())) {
					x.setSecDeposit(y.getFirstDeposit());
				}
			});
			thirdDeposit.forEach(y -> {
				if (x.getStatDate().equals(y.getStatDate())) {
					x.setThirdDeposit(y.getFirstDeposit());
				}
			});
		});
		return page;
	}
}
