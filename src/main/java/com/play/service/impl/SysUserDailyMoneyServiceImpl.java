package com.play.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.play.model.*;
import com.play.orm.jdbc.support.Aggregation;
import com.play.orm.jdbc.support.AggregationFunction;
import com.play.service.SysUserDepositService;
import com.play.web.var.GuestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.utils.BigDecimalUtil;
import com.play.common.utils.DateUtil;
import com.play.common.utils.LogUtils;
import com.play.common.utils.ProxyModelUtil;
import com.play.core.LogTypeEnum;
import com.play.core.StationConfigEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.MnyDepositRecordDao;
import com.play.dao.MnyDrawRecordDao;
import com.play.dao.StationDailyRegisterDao;
import com.play.dao.SysUserBetNumDao;
import com.play.dao.SysUserDailyMoneyDao;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserDepositDao;
import com.play.dao.SysUserLoginDao;
import com.play.dao.SysUserMoneyDao;
import com.play.model.vo.MonthReportVo;
import com.play.model.vo.RiskReportVo;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserDailyMoneyService;
import com.play.service.SysUserDegreeService;
import com.play.service.SysUserInfoService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.user.online.OnlineManager;
import com.play.web.utils.ServletUtils;
import com.play.web.utils.StationConfigUtil;
import com.play.web.utils.export.ExportToCvsUtil;
import com.play.web.var.LoginMemberUtil;

/**
 * @author admin
 */
@Service
public class SysUserDailyMoneyServiceImpl implements SysUserDailyMoneyService {

	@Autowired
	private SysUserDailyMoneyDao sysUserDailyMoneyDao;
	@Autowired
	private SysUserDao userDao;
	@Autowired
	private SysUserLoginDao userLoginDao;
	@Autowired
	private SysUserDepositDao userDepositDao;
	@Autowired
	private SysUserMoneyDao moneyDao;
	@Autowired
	private SysUserBetNumDao userBetNumDao;
	@Autowired
	private MnyDepositRecordDao mnyDepositRecordDao;
	@Autowired
	private MnyDrawRecordDao mnyDrawRecordDao;
	@Autowired
	private StationDailyRegisterDao stationDailyRegisterDao;
	@Autowired
	private SysUserDegreeService degreeService;
	@Autowired
	private SysUserInfoService userInfoService;
	@Autowired
	private SysUserDepositService sysUserDepositService;

	@Override
	public SysUserDailyMoney getDailyBet(Long userId, Long stationId, Date begin, Date end, Long proxyId,
			String agentName, String userRemark, String degreeIds, String groupIds, Integer level, Integer userType) {
		return sysUserDailyMoneyDao.getDailyBet(userId, stationId, begin, end, proxyId, agentName, userRemark,
				degreeIds, groupIds, level, userType);
	}

	@Override
	public SysUserDailyMoney personOverview(Long stationId, Long userId, Date begin, Date end) {
		String key = "personOverview:" + stationId + ":" + userId + ":" + DateUtil.toDateStr(begin) + ":"
				+ DateUtil.toDateStr(end);
		SysUserDailyMoney dailyMoney = CacheUtil.getCache(CacheKey.STATISTIC, key, SysUserDailyMoney.class);
		if (dailyMoney == null) {
			dailyMoney = sysUserDailyMoneyDao.overview(stationId, userId, begin, end, true, false);
			CacheUtil.addCache(CacheKey.STATISTIC, key, dailyMoney, 120);
		}
		return dailyMoney;
	}

	@Override
	public Page<SysUserDailyMoney> betUserPage(SysUser user, Date begin, Date end, Integer pageSize,
			Integer pageNumber) {
		String key = "betUserPage:" + user.getId() + ":" + DateUtil.toDateStr(begin) + ":" + DateUtil.toDateStr(end)
				+ ":" + pageSize + ":" + pageNumber;
		Page<SysUserDailyMoney> page = CacheUtil.getCache(CacheKey.STATISTIC, key, Page.class);
		if (page != null) {
			return page;
		}
		boolean isProxy = (user.getType() == UserTypeEnum.PROXY.getType());
		page = sysUserDailyMoneyDao.betUserPage(user.getId(), user.getStationId(), begin, end, isProxy);
		CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat),
				120);
		return page;
	}

	@Override
	public JSONObject teamOverview(SysUser user, String username, Date begin, Date end) {
		Long userId = user.getId();
		Long stationId = user.getStationId();
		boolean isMember = (user.getType() == UserTypeEnum.MEMBER.getType());
		if (StringUtils.isNotEmpty(username) && !user.getUsername().equals(username)) {
			SysUser suser = userDao.findOneByUsername(username, stationId, null);
			if (suser == null) {
				throw new ParamException(BaseI18nCode.searchUserNotExist);
			}
			if (isMember) {// 会员则判断是否是推荐好友来的
				if (!user.getRecommendId().equals(userId)) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
			} else {// 代理推广来的
				if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
					throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
				}
			}
			userId = user.getId();
		}
		String key = "teamOverview:" + userId + ":" + DateUtil.toDateStr(begin) + ":" + DateUtil.toDateStr(end);
		JSONObject overviewJson = CacheUtil.getCache(CacheKey.STATISTIC, key, JSONObject.class);
		if (overviewJson == null) {
			overviewJson = new JSONObject();
			overviewJson.put("dailyMoney",
					sysUserDailyMoneyDao.overview(stationId, userId, begin, end, false, isMember));
			overviewJson.put("registerCount", userDao.countTeamNum(stationId, userId, begin, end, null, isMember));// 时间段内注册会员人数
			overviewJson.put("memberCount",
					userDao.countTeamNum(stationId, userId, null, null, UserTypeEnum.MEMBER.getType(), isMember));// 会员总数
			overviewJson.put("proxyCount",
					userDao.countTeamNum(stationId, userId, null, null, UserTypeEnum.PROXY.getType(), isMember));// 代理总数
			overviewJson.put("teamMoney", BigDecimalUtil.formatValue(moneyDao.teamMoneyCount(userId, stationId, isMember)));
			overviewJson.put("onlineNum", OnlineManager.getOnlineCount(stationId, userId) + 1);//// 当前在线人数+1是包含自己
			overviewJson.put("threeNotLoginNum", userLoginDao.countOffLineNumForProxy(stationId, userId,
					DateUtils.addDays(new Date(), -3), isMember));// 三天未登录人数
			// 投注人数
			overviewJson.put("betNum", sysUserDailyMoneyDao.countBetUserNum(stationId, begin, end, userId, null, null,
					null, null, null, null, isMember));
			overviewJson.put("firstDepositMemNum", userDepositDao.countTeamFirstDepositTodayNum(stationId, userId,
					begin, end, UserTypeEnum.MEMBER.getType(), isMember));// 当日首充会员人数
			overviewJson.put("firstDepositProxyNum", userDepositDao.countTeamFirstDepositTodayNum(stationId, userId,
					begin, end, UserTypeEnum.PROXY.getType(), isMember));// 当日首充代理人数
			CacheUtil.addCache(CacheKey.STATISTIC, key, overviewJson, 180);
		}
		return overviewJson;
	}

	@Override
	public JSONObject recommendList(SysUser login, String dateStr, String username, Integer pageSize,
			Integer pageNumber) {
		Long userId = login.getId();
		Long stationId = login.getStationId();
		if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
			SysUser recommend = userDao.findOneByUsername(username, stationId, null);
			if (recommend == null) {
				throw new BaseException(BaseI18nCode.proxyUnExist);
			}
			if (!Objects.equals(recommend.getRecommendId(), userId)) {
				throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
			}
			userId = recommend.getId();
		}
		String key = "ucrecommendList:" + dateStr + ":" + userId + ":" + pageSize + ":" + pageNumber;
		JSONObject object = CacheUtil.getCache(CacheKey.STATISTIC, key, JSONObject.class);
		if (object != null) {
			return object;
		}
		object = new JSONObject();
		Date date = DateUtil.toDate(dateStr);
		if (date == null) {
			date = new Date();
		}
		object.put("daily", sysUserDailyMoneyDao.overview(stationId, userId, date, date, false, true));
		object.put("memberCount",
				userDao.countTeamNum(stationId, userId, null, null, UserTypeEnum.MEMBER.getType(), true));// 会员总数
		object.put("teamMoney", moneyDao.teamMoneyCount(userId, stationId, true));
		object.put("betNum", sysUserDailyMoneyDao.countBetUserNum(stationId, date, date, userId, null, null, null, null,
				null, null, true));
		object.put("page", sysUserDailyMoneyDao.recommendPage(stationId, userId, date));
		CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat),
				60);
		return object;
	}

	@Override
	public Page<SysUserDailyMoney> personReport(Date begin, Date end, String username, Boolean include,
			Integer pageSize, Integer pageNumber) {
		SysUser login = LoginMemberUtil.currentUser();
		Long userId = login.getId();
		Long stationId = login.getStationId();
		boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.proxy_view_account_data)) {
			include = false;
		} else {
			if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.searchUserNotExist);
				}
				if (isMember) {// 会员则判断是否是推荐好友来的
					if (!user.getRecommendId().equals(userId)) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					include = false;// 会员推广只能查看直属的会员，不能再看下一级
				} else {// 代理推广来的
					if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
				}
				userId = user.getId();
			}
		}

		if (begin.after(end)) {
			throw new ParamException(BaseI18nCode.startTimeLtEndTime);
		}
		if (begin.getYear() != end.getYear() || begin.getMonth() != end.getMonth()) {
			throw new ParamException(BaseI18nCode.monthMustSame);// 因为分表的关系，查询跨月的，性能太低，不能修改
		}
		String key = "personReport:" + stationId + ":" + userId + ":" + DateUtil.toDateStr(begin) + ":"
				+ DateUtil.toDateStr(end) + ":" + pageSize + ":" + pageNumber + ":" + include;
		Page<SysUserDailyMoney> page = CacheUtil.getCache(CacheKey.STATISTIC, key, Page.class);
		if (page != null) {
			return page;
		}
		page = sysUserDailyMoneyDao.personReport(stationId, userId, begin, end, include, isMember);
		CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(page, SerializerFeature.WriteDateUseDateFormat),
				120);
		return page;
	}

	@Override
	public JSONObject teamReport(String username, Date begin, Date end, Integer pageNumber, Integer pageSize,
			String sortName, String sortOrder) {
		SysUser login = LoginMemberUtil.currentUser();
		Long userId = login.getId();
		Long stationId = login.getStationId();
		boolean isMember = (login.getType() == UserTypeEnum.MEMBER.getType());
		boolean include = true;
		if (StationConfigUtil.isOff(stationId, StationConfigEnum.proxy_view_account_data)) {
			include = false;
		} else {
			if (StringUtils.isNotEmpty(username) && !login.getUsername().equals(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.searchUserNotExist);
				}
				if (isMember) {// 会员则判断是否是推荐好友来的
					if (!user.getRecommendId().equals(userId)) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
					include = false;// 会员推广只能查看直属的会员，不能再看下一级
				} else {// 代理推广来的
					if (user.getParentIds() == null || !user.getParentIds().contains("," + userId + ",")) {
						throw new ParamException(BaseI18nCode.onlySearchUserAndChild);
					}
				}
				userId = user.getId();
			}
		}
		String key = "teamReport:" + stationId + ":" + userId + ":" + DateUtil.toDateStr(begin) + ":"
				+ DateUtil.toDateStr(end) + ":" + pageNumber + ":" + pageSize + ":" + sortName + ":" + sortOrder;
		JSONObject object = CacheUtil.getCache(CacheKey.STATISTIC, key, JSONObject.class);
		if (object != null) {
			return object;
		}
		Page<SysUserDailyMoney> page = sysUserDailyMoneyDao.userCenterTeamReport(stationId, begin, end, userId,
				sortName, sortOrder, include, isMember);
		// 团队总计
		SysUserDailyMoney total = sysUserDailyMoneyDao.sumTeamReport(stationId, null, begin, end, userId, null, null,
				null, null, isMember);
		if (total != null && total.getProfitAndLoss() != null) {
			total.setProfitAndLoss(total.getProfitAndLoss().negate());
		}
		object = new JSONObject();
		object.put("page", page);
		object.put("total", total);
		CacheUtil.addCache(CacheKey.STATISTIC, key, object, 120);
		return object;
	}

	@Override
	public String globalReport(Long stationId, Date begin, Date end, String proxyName, String username, String agentUser, String userRemark,
							   String degreeIds, Integer level, Integer userType) {
		return globalReport(stationId, begin, end, proxyName, username, agentUser, userRemark, degreeIds, level, userType, false);
	}

	@Override
	public String globalReport(Long stationId, Date begin, Date end, String proxyName, String username,
			String agentUser, String userRemark, String degreeIds, Integer level, Integer userType,boolean directSub) {
		String key = "globalReport:" + stationId + ":" + username + ":" + proxyName + ":" + DateUtil.toDateStr(begin)
				+ ":" + DateUtil.toDateStr(end) + ":" + agentUser + ":" + userRemark + ":" + degreeIds + ":" + level
				+ ":" + userType + ":" + directSub;
		String jsoStr = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (StringUtils.isEmpty(jsoStr)) {
			Long proxyId = null, userId = null;
			boolean isRecommend = false;
			if (StringUtils.isNotEmpty(proxyName)) {
				SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
				if (proxy == null) {
					throw new ParamException(BaseI18nCode.proxyUnExist);
				}
				isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
				proxyId = proxy.getId();
			}
			if (StringUtils.isNotEmpty(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null || GuestTool.isGuest(user)) {
					throw new ParamException(BaseI18nCode.memberUnExist);
				}
				userId = user.getId();
			}
			SysUserDailyMoney dailyMoney = sysUserDailyMoneyDao.globalReport(stationId, begin, end, proxyId, userId,
					null, agentUser, userRemark, degreeIds, level, userType, isRecommend,directSub);
			JSONObject obj = winOrLoseHandle(dailyMoney);
			if (userId == null) {
				obj.put("betUserNum", sysUserDailyMoneyDao.countBetUserNum(stationId, begin, end, proxyId, userId,
						agentUser, userRemark, degreeIds, level, userType, isRecommend));
				obj.put("depositUserNum", sysUserDailyMoneyDao.countDepositUserNum(stationId, begin, end, proxyId,
						userId, agentUser, userRemark, degreeIds, level, userType, isRecommend));
				obj.put("drawUserNum", sysUserDailyMoneyDao.countDrawUserNum(stationId, begin, end, proxyId, userId,
						agentUser, userRemark, degreeIds, level, userType, isRecommend));
				obj.put("rebateUserNum", sysUserDailyMoneyDao.countBackWaterUserNum(stationId, begin, end, proxyId,
						userId, agentUser, userRemark, degreeIds, level, userType, isRecommend));
				obj.put("proxyRebateUserNum", sysUserDailyMoneyDao.countProxyRebateUserNum(stationId, begin, end,
						proxyId, userId, agentUser, userRemark, degreeIds, level, userType, isRecommend));
			}
			String proxyNameForDeposit = null;
			SysUser sysUser = userDao.findOne(userId, stationId);
			if (sysUser != null) {
				proxyNameForDeposit = sysUser.getUsername();
			}
			if (directSub) {
				end = DateUtil.dayEndTime(end, 0);
				obj.put("firstDeposit", userDepositDao.countDepositByUser(stationId, begin, end, userId, true));
				SysUserDeposit firstDeposit = userDepositDao.findOne2(userId, stationId, true, begin, end);
				if (firstDeposit != null) {
					obj.put("firstDepositTotalMoney", firstDeposit.getFirstMoney());
				}
				SysUserDeposit secDeposit = userDepositDao.findOne2(userId, stationId, false, begin, end);
				if (secDeposit != null) {
					obj.put("secDepositTotalMoney", secDeposit.getSecMoney());
				}
				obj.put("secDeposit", userDepositDao.countDepositByUser(stationId, begin, end, userId, false));
			}else{
				if (StringUtils.isNotEmpty(proxyNameForDeposit)) {
					end = DateUtil.dayEndTime(end, 0);
					SysUserDeposit firstDeposit = userDepositDao.findOneByUsername(proxyNameForDeposit, stationId,begin,end,true);
					if (firstDeposit != null) {
						obj.put("firstDepositTotalMoney", firstDeposit.getFirstMoney());
					}
					SysUserDeposit secDeposit = userDepositDao.findOneByUsername(proxyNameForDeposit, stationId,begin,end,false);
					if (secDeposit != null) {
						obj.put("secDepositTotalMoney", secDeposit.getSecMoney());
					}
					obj.put("firstDeposit", userDepositDao.countDepositByProxyName(stationId, begin, end, proxyNameForDeposit, true));
					obj.put("secDeposit", userDepositDao.countDepositByProxyName(stationId, begin, end, proxyNameForDeposit, false));
				}
			}
			BigDecimal proxyRebateAmount = new BigDecimal(obj.getString("proxyRebateAmount"));
			proxyRebateAmount.setScale(2, BigDecimal.ROUND_DOWN);
			obj.put("proxyRebateAmount",proxyRebateAmount.setScale(2, BigDecimal.ROUND_DOWN));
			jsoStr = obj.toJSONString();
			CacheUtil.addCache(CacheKey.STATISTIC, key, jsoStr, 60);
		}
		return jsoStr;
	}

	// 首页数据展示
	@Override
	public Map<String, Object> indexGlobleReport(long stationId, Date begin, Date end) {
		Map<String, Object> map = new HashMap<>();
		map.put("betUserNum", sysUserDailyMoneyDao.countBetUserNum(stationId, begin, end, null, null, null, null, null,
				null, null, false));
		map.put("depositUserNum", sysUserDailyMoneyDao.countDepositUserNum(stationId, begin, end, null, null, null,
				null, null, null, null, false));
		map.put("drawUserNum", sysUserDailyMoneyDao.countDrawUserNum(stationId, begin, end, null, null, null, null,
				null, null, null, false));
		return map;
	}

	private JSONObject winOrLoseHandle(SysUserDailyMoney d) {
		JSONObject dailyMoneyJson = (JSONObject) JSON.toJSON(d);
		// 体育输赢
		BigDecimal sport = nullToZero(BigDecimalUtil.subtract(d.getSportBetAmount(), d.getSportWinAmount()));
		dailyMoneyJson.put("sportWinOrLose", sport);
		// 真人输赢
		BigDecimal live = nullToZero(BigDecimalUtil.subtract(d.getLiveBetAmount(), d.getLiveWinAmount()));
		dailyMoneyJson.put("liveWinOrLose", live);
		// 电子输赢
		BigDecimal egame = nullToZero(BigDecimalUtil.subtract(d.getEgameBetAmount(), d.getEgameWinAmount()));
		dailyMoneyJson.put("egameWinOrLose", egame);
		// 棋牌输赢
		BigDecimal chess = nullToZero(BigDecimalUtil.subtract(d.getChessBetAmount(), d.getChessWinAmount()));
		dailyMoneyJson.put("chessWinOrLose", chess);
		// 彩票输赢
		BigDecimal lot = nullToZero(BigDecimalUtil.subtract(d.getLotBetAmount(), d.getLotWinAmount()));
		dailyMoneyJson.put("lotteryWinOrLose", lot);
		// 电竞输赢
		BigDecimal esport = nullToZero(BigDecimalUtil.subtract(d.getEsportBetAmount(), d.getEsportWinAmount()));
		dailyMoneyJson.put("esportWinOrLose", esport);
		// 捕鱼输赢
		BigDecimal fishing = nullToZero(BigDecimalUtil.subtract(d.getFishingBetAmount(), d.getFishingWinAmount()));
		dailyMoneyJson.put("fishingWinOrLose", fishing);
		// 输赢总计
		BigDecimal totalBet = BigDecimalUtil.addAll(d.getSportBetAmount(), d.getLiveBetAmount(), d.getEgameBetAmount(),
				d.getChessBetAmount(), d.getLotBetAmount(), d.getEsportBetAmount(), d.getFishingBetAmount());
		BigDecimal totalWin = BigDecimalUtil.addAll(d.getSportWinAmount(), d.getLiveWinAmount(), d.getEgameWinAmount(),
				d.getChessWinAmount(), d.getLotWinAmount(), d.getEsportWinAmount(), d.getFishingWinAmount());
		BigDecimal totalGift = BigDecimalUtil.addAll(d.getProxyRebateAmount(), d.getLiveRebateAmount(),
				d.getGiftOtherAmount(), d.getActiveAwardAmount(), d.getDepositGiftAmount(),
				d.getRedActiveAwardAmount());
		dailyMoneyJson.put("totalBetAmount", totalBet);
		dailyMoneyJson.put("totalWinAmount", totalWin);
		dailyMoneyJson.put("totalGiftAmount", totalGift);
		dailyMoneyJson.put("totalWinOrLose",
				BigDecimalUtil.subtract(BigDecimalUtil.addAll(d.getSubGiftAmount(), totalBet), totalWin, totalGift));
		dailyMoneyJson.put("money", d.getMoney());
		return dailyMoneyJson;
	}

	private BigDecimal nullToZero(BigDecimal b) {
		if (b == null) {
			return BigDecimal.ZERO;
		}
		return b;
	}

	@Override
	public String userData(String username, Long stationId, Date begin, Date end) {
		String key = "userData:" + stationId + ":" + username + ":" + DateUtil.toDateStr(begin) + ":"
				+ DateUtil.toDateStr(end);
		String jsonStr = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (jsonStr != null) {
			return jsonStr;
		}
		// 获取会员基本信息
		SysUser user = userDao.findOneByUsername(username, stationId, null);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}

		// 试玩账户不展示信息
		if (GuestTool.isGuest(user)) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		// 获取下注信息
		SysUserDailyMoney dailyMoney = sysUserDailyMoneyDao.globalReport(stationId, begin, end, null, user.getId(),
				null, null, null, null, null, null, false);
		// 输赢没有统计的问题
		JSONObject json = winOrLoseHandle(dailyMoney);
	//	JSONObject json = (JSONObject) JSON.toJSON(dailyMoney);
		json.put("username", username);
		json.put("proxyName", user.getProxyName());
		json.put("createDatetime", DateUtil.toDatetimeStr(user.getCreateDatetime()));
		json.put("userType", user.getType());
		// 获取余额
		json.put("money", moneyDao.getMoney(user.getId()));
		// 获取打码量
		SysUserBetNum betNum = userBetNumDao.findOne(user.getId(), stationId);
		json.put("betNum", betNum.getBetNum());
		json.put("drawNeed", betNum.getDrawNeed());
		json.put("totalBetNum", betNum.getTotalBetNum());
		// 获取最后充值信息
		MnyDepositRecord depositRecord = mnyDepositRecordDao.getNewestRecord(stationId, user.getId(), begin, end,
				MnyDepositRecord.STATUS_SUCCESS, null);
		if (depositRecord != null) {
			json.put("lastDepositMoney", depositRecord.getMoney());
			json.put("lastDepositTime", DateUtil.toDatetimeStr(depositRecord.getCreateDatetime()));
			json.put("lastDepositRemark", depositRecord.getRemark());
		}
		// 获取最后提款信息
		MnyDrawRecord drawRecord = mnyDrawRecordDao.getNewestRecord(stationId, user.getId(), end,
				MnyDrawRecord.STATUS_SUCCESS);
		if (drawRecord != null) {
			json.put("lastDrawMoney", drawRecord.getDrawMoney());
			json.put("lastDrawTime", DateUtil.toDatetimeStr(drawRecord.getCreateDatetime()));
			json.put("lastDrawRemark", drawRecord.getRemark());
		}
		jsonStr = json.toJSONString();
		CacheUtil.addCache(CacheKey.STATISTIC, key, jsonStr, 60);
		return jsonStr;
	}

	@Override
	public String adminTeamReport(Long stationId, Date begin, Date end, String username, String proxyName,
			Integer pageNumber, Integer pageSize, Integer level, String agentName, String degreeIds,
			String userRemark) {
		HttpServletRequest request = ServletUtils.getRequest();
		String sortOrder = request.getParameterMap().get("sortOrder")[0];
		String sortName = "";
		if (request.getParameterMap().get("sortName") != null) {
			sortName = request.getParameterMap().get("sortName")[0];
		}
		String key = "adminTeamReport:" + stationId + ":" + username + ":" + proxyName + ":" + DateUtil.toDateStr(begin)
				+ ":" + DateUtil.toDateStr(end) + ":" + pageSize + ":" + pageNumber + ":" + sortOrder + ":" + sortName
				+ ":" + level + ":" + degreeIds + ":" + userRemark + ":" + agentName;
		String jsonStr = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (StringUtils.isNotEmpty(jsonStr)) {
			return jsonStr;
		}
		Long proxyId = null, userId = null;
		boolean isRecommend = false;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
			proxyId = proxy.getId();
		}
		if (StringUtils.isNotEmpty(username)) {
			SysUser user = userDao.findOneByUsername(username, stationId, null);
			if (user == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			userId = user.getId();
		}
		Page<SysUserDailyMoney> page = sysUserDailyMoneyDao.teamReport(stationId, userId, begin, end, proxyId, sortName,
				sortOrder, level, agentName, degreeIds, userRemark, isRecommend);
		// 计算团队盈亏
		JSONObject dailyMoney = (JSONObject) JSON.toJSON(page);
		SysUserDailyMoney total = sysUserDailyMoneyDao.sumTeamReport(stationId, userId, begin, end, proxyId, level,
				agentName, degreeIds, userRemark, isRecommend);
		dailyMoney.put("aggsData", total);
		jsonStr = dailyMoney.toJSONString();
		CacheUtil.addCache(CacheKey.STATISTIC, key, jsonStr, 60);
		return jsonStr;
	}

	@Override
	public void adminTeamReportExport(Long stationId, Date begin, Date end, String username, String proxyName,
			Integer level, String agentName, String degreeIds, String userRemark) {
		String key = "adminTeamReportEx:" + stationId + ":" + username + ":" + proxyName + ":"
				+ DateUtil.toDateStr(begin) + ":" + DateUtil.toDateStr(end) + ":" + ":" + level + ":" + degreeIds + ":"
				+ userRemark + ":" + agentName;
		List<SysUserDailyMoney> dailyMoneyList = null;
		String json = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (StringUtils.isEmpty(json)) {
			Long proxyId = null, userId = null;
			boolean isRecommend = false;
			if (StringUtils.isNotEmpty(proxyName)) {
				SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
				if (proxy == null) {
					throw new ParamException(BaseI18nCode.proxyUnExist);
				}
				isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
				proxyId = proxy.getId();
			}
			if (StringUtils.isNotEmpty(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.memberUnExist);
				}
				userId = user.getId();
			}
			dailyMoneyList = sysUserDailyMoneyDao.teamReportExport(stationId, userId, begin, end, proxyId, level,
					agentName, degreeIds, userRemark, isRecommend);
			if (dailyMoneyList != null) {
				CacheUtil.addCache(CacheKey.STATISTIC, key, JSON.toJSONString(dailyMoneyList), 60);
			}
		} else {
			dailyMoneyList = JSON.parseArray(json, SysUserDailyMoney.class);
		}
		String[] rowsName = new String[] { I18nTool.getMessage(BaseI18nCode.stationSerialNumber),
				I18nTool.getMessage(BaseI18nCode.vipMemberAcc), I18nTool.getMessage(BaseI18nCode.stationInfulMoney),
				I18nTool.getMessage(BaseI18nCode.betAllCount), I18nTool.getMessage(BaseI18nCode.winnerAllCount),
				I18nTool.getMessage(BaseI18nCode.onLineDepositCount),
				I18nTool.getMessage(BaseI18nCode.stationHandAddMoney),
				I18nTool.getMessage(BaseI18nCode.depositGiveCount), I18nTool.getMessage(BaseI18nCode.otherCashCount),
				I18nTool.getMessage(BaseI18nCode.onlineDrawCount), I18nTool.getMessage(BaseI18nCode.handDeduceCash),
				I18nTool.getMessage(BaseI18nCode.colorCashDeduce), I18nTool.getMessage(BaseI18nCode.backWaterCount),
				I18nTool.getMessage(BaseI18nCode.backPointCount), I18nTool.getMessage(BaseI18nCode.actCashCount),
				I18nTool.getMessage(BaseI18nCode.winOrLose) };
		List<Object[]> dataList = new ArrayList<>();
		Object[] objs;
		if (dailyMoneyList != null && !dailyMoneyList.isEmpty()) {
			SysUserDailyMoney daily = null;
			for (int i = 0; i < dailyMoneyList.size(); i++) {
				daily = dailyMoneyList.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i + "";
				objs[1] = getStrNull(daily.getUsername());
				objs[2] = getStrNull(daily.getMoney());
				objs[3] = getStrNull(daily.getLiveBetAmount());
				objs[4] = getStrNull(daily.getLiveWinAmount());
				objs[5] = getStrNull(daily.getDepositAmount());
				objs[6] = getStrNull(daily.getDepositArtificial());
				objs[7] = getStrNull(daily.getDepositGiftAmount());
				objs[8] = getStrNull(daily.getGiftOtherAmount());
				objs[9] = getStrNull(daily.getWithdrawAmount());
				objs[10] = getStrNull(daily.getWithdrawArtificial());
				objs[11] = getStrNull(daily.getSubGiftAmount());
				objs[12] = getStrNull(daily.getLiveRebateAmount());
				objs[13] = getStrNull(daily.getProxyRebateAmount());
				objs[14] = getStrNull(daily.getActiveAwardAmount());
				objs[15] = getStrNull(daily.getProfitAndLoss());
				dataList.add(objs);
			}
		}
		// PoiUtil.export("团队报表", rowsName, dataList);
		ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.teamReport), rowsName, dataList);
		LogUtils.log("导出团队报表", LogTypeEnum.EXPORT);
	}

	private String getStrNull(Object obj) {
		if (obj == null || obj.toString().isEmpty()) {
			return "-";
		}
		return obj.toString();
	}

	@Override
	public Page<SysUserDailyMoney> moneyReport(Long stationId, Date begin, Date end, String username, String proxyName,
			Integer pageNumber, Integer pageSize, String agentName) {
		HttpServletRequest request = ServletUtils.getRequest();
		String sortOrder = request.getParameterMap().get("sortOrder")[0];
		String sortName = "";
		if (request.getParameterMap().get("sortName") != null) {
			sortName = request.getParameterMap().get("sortName")[0];
		}
		String key = "adminMoneyReport:" + stationId + ":" + username + ":" + proxyName + ":"
				+ DateUtil.toDateStr(begin) + ":" + DateUtil.toDateStr(end) + ":" + pageSize + ":" + pageNumber + ":"
				+ sortOrder + ":" + sortName + ":" + agentName;
		Page<SysUserDailyMoney> moneyReport = CacheUtil.getCache(CacheKey.STATISTIC, key, Page.class);
		if (moneyReport == null) {
			Long proxyId = null, userId = null;
			boolean isRecommend = false;
			if (StringUtils.isNotEmpty(proxyName)) {
				SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
				if (proxy == null) {
					throw new ParamException(BaseI18nCode.proxyUnExist);
				}
				isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
				proxyId = proxy.getId();
			}
			if (StringUtils.isNotEmpty(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.memberUnExist);
				}
				userId = user.getId();
			}
			moneyReport = sysUserDailyMoneyDao.teamReport(stationId, userId, begin, end, proxyId, sortName, sortOrder,
					null, agentName, null, null, isRecommend);
			CacheUtil.addCache(CacheKey.STATISTIC, key, moneyReport, 60);
		}
		return moneyReport;
	}

	@Override
	public void moneyReportExport(Long stationId, Date begin, Date end, String username, String proxyName,
			String agentName) {
		String key = "adminMoneyReportEx:" + stationId + ":" + username + ":" + proxyName + ":"
				+ DateUtil.toDateStr(begin) + ":" + DateUtil.toDateStr(end);
		List<SysUserDailyMoney> moneyReport = null;
		String json = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (StringUtils.isEmpty(json)) {
			Long proxyId = null, userId = null;
			boolean isRecommend = false;
			if (StringUtils.isNotEmpty(proxyName)) {
				SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
				if (proxy == null) {
					throw new ParamException(BaseI18nCode.proxyUnExist);
				}
				isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
				proxyId = proxy.getId();
			}
			if (StringUtils.isNotEmpty(username)) {
				SysUser user = userDao.findOneByUsername(username, stationId, null);
				if (user == null) {
					throw new ParamException(BaseI18nCode.memberUnExist);
				}
				userId = user.getId();
			}
			moneyReport = sysUserDailyMoneyDao.teamReportExport(stationId, userId, begin, end, proxyId, null, agentName,
					null, null, isRecommend);
			CacheUtil.addCache(CacheKey.STATISTIC, key, moneyReport, 60);
		} else {
			moneyReport = JSON.parseArray(json, SysUserDailyMoney.class);
		}
		String[] rowsName = new String[] { I18nTool.getMessage(BaseI18nCode.stationSerialNumber),
				I18nTool.getMessage(BaseI18nCode.vipMemberAcc), I18nTool.getMessage(BaseI18nCode.stationProxyBelong),
				I18nTool.getMessage(BaseI18nCode.onLineDepositCount),
				I18nTool.getMessage(BaseI18nCode.stationHandAddMoney),
				I18nTool.getMessage(BaseI18nCode.onlineDrawCount), I18nTool.getMessage(BaseI18nCode.handDeduceCash),
				I18nTool.getMessage(BaseI18nCode.backWaterCount), I18nTool.getMessage(BaseI18nCode.backPointCount),
				I18nTool.getMessage(BaseI18nCode.depositGiveCount), I18nTool.getMessage(BaseI18nCode.actGiveAllCount),
				I18nTool.getMessage(BaseI18nCode.otherCashAll) };
		List<Object[]> dataList = new ArrayList<>();
		Object[] objs;
		if (moneyReport != null && !moneyReport.isEmpty()) {
			SysUserDailyMoney daily = null;
			for (int i = 0; i < moneyReport.size(); i++) {
				daily = moneyReport.get(i);
				objs = new Object[rowsName.length];
				objs[0] = i + "";
				objs[1] = getStrNull(daily.getUsername());
				objs[2] = getStrNull(daily.getProxyName());
				objs[3] = getStrNull(daily.getDepositAmount());
				objs[4] = getStrNull(daily.getDepositArtificial());
				objs[5] = getStrNull(daily.getWithdrawAmount());
				objs[6] = getStrNull(daily.getWithdrawArtificial());
				objs[7] = getStrNull(daily.getLiveRebateAmount());
				objs[8] = getStrNull(daily.getProxyRebateAmount());
				objs[9] = getStrNull(daily.getDepositGiftAmount());
				objs[10] = getStrNull(daily.getActiveAwardAmount());
				objs[11] = getStrNull(daily.getGiftOtherAmount());
				dataList.add(objs);
			}
		}
		ExportToCvsUtil.export(I18nTool.getMessage(BaseI18nCode.financeReport), rowsName, dataList);
		LogUtils.log("导出财务报表", LogTypeEnum.EXPORT);
	}

	@Override
	public String adminMonthReport(Long stationId) {
		String key = "adminMonthReport:s:" + stationId;
		String jsonStr = CacheUtil.getCache(CacheKey.STATISTIC, key);
		if (jsonStr != null) {
			return jsonStr;
		}
		Calendar c = Calendar.getInstance();
		Date now = c.getTime();
		// 获取今日盈亏数据
		SysUserDailyMoney today = sysUserDailyMoneyDao.countDataByDay(now, stationId, null);
		BigDecimal profitTo = today.getLiveBetAmount().subtract(today.getLiveWinAmount())
				.subtract(today.getProxyRebateAmount()).subtract(today.getLiveRebateAmount());
		// 获取昨日盈亏数据
		c.add(Calendar.DAY_OF_MONTH, -1);
		Date yes = DateUtil.dayFirstTime(now, -1);
		SysUserDailyMoney yesterday = sysUserDailyMoneyDao.countDataByDay(yes, stationId, null);
		BigDecimal profitYes = yesterday.getLiveBetAmount().subtract(yesterday.getLiveWinAmount())
				.subtract(yesterday.getProxyRebateAmount()).subtract(yesterday.getLiveRebateAmount());
		// 获取过去十二个月的数据
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.MONTH, -12);
		Date start = c.getTime();
		List<MonthReportVo> dailyMonies = sysUserDailyMoneyDao.countMonthProfitData(stationId, start, now);
		// 统计昨日注册
		StationDailyRegister register = stationDailyRegisterDao.getDailyByDay(stationId, yes);
		// 统计今日注册
		StationDailyRegister todayReg = stationDailyRegisterDao.getDailyByDay(stationId, now);

		JSONObject object = new JSONObject();
		object.put("today", today);
		object.put("yes", yesterday);

		object.put("money", moneyDao.stationMoneyCount(stationId));// 获取站点用户总余额
		// 获取在线会员
		object.put("onlineNum", OnlineManager.getOnlineCount(stationId, null));
		object.put("proxyNum", userDao.countTeamNum(stationId, null, null, null, UserTypeEnum.PROXY.getType(), false));// 总代理数
		object.put("memberNum",
				userDao.countTeamNum(stationId, null, null, null, UserTypeEnum.MEMBER.getType(), false));// 总会员数
		object.put("totalNum", userDao.countTeamNum(stationId, null, null, null, null, false));// 总用户数

		object.put("yesterdayReg", register == null ? 0 : register.getMemberNum() + register.getProxyNum());
		object.put("todayReg", todayReg == null ? 0 : todayReg.getMemberNum() + todayReg.getProxyNum());

		object.put("betYes", yesterday.getLiveBetAmount());
		object.put("winYes", yesterday.getLiveWinAmount());
		object.put("profitYes", profitYes);
		object.put("winTo", today.getLiveWinAmount());
		object.put("betTo", today.getLiveBetAmount());
		object.put("profitTo", profitTo);

		object.put("monthMoneyData", dailyMonies);
		jsonStr = object.toJSONString();
		CacheUtil.addCache(CacheKey.STATISTIC, key, jsonStr, 60);
		return jsonStr;
	}

	@Override
	public JSONObject adminProxyReport(Long stationId, Date begin, Date end, String proxyName) {
		return adminProxyReport(stationId, begin, end, proxyName, false);
	}

	/**
	 *
	 * @param stationId
	 * @param begin
	 * @param end
	 * @param proxyName
	 * @param directSub 只查询直属下级的报表数据
	 * @return
	 */
	@Override
	public JSONObject adminProxyReport(Long stationId, Date begin, Date end, String proxyName, boolean directSub) {
		// 去除代理模式为 全部会员
		if (ProxyModelUtil.isAllMember(stationId)) {
			return new JSONObject();
		}
		if (StringUtils.isEmpty(proxyName)) {
			return new JSONObject();
		}
		Integer level = 1;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userDao.findOneByUsername(proxyName, stationId, UserTypeEnum.PROXY.getType());
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			level += proxy.getLevel();
		}
		Page<SysUser> proxyPage = userDao.getProxyReport(stationId, level, proxyName,directSub);
		JSONArray proxyArr = new JSONArray();
		BigDecimal totalBalance = BigDecimal.ZERO;
		BigDecimal withdrawAmount = BigDecimal.ZERO;
		BigDecimal depositAmount = BigDecimal.ZERO;
		Integer firstDepositSum = 0;
		Integer secDepositSum = 0;
		BigDecimal secDepositMoneySum = BigDecimal.ZERO;
		BigDecimal firstDepositMoneySum = BigDecimal.ZERO;
		for (SysUser user : proxyPage.getRows()) {
			JSONObject glo = JSON.parseObject(
					globalReport(stationId, begin, end, proxyName, user.getUsername(), null,
							null, null, null, null,directSub));
			glo.put("username", user.getUsername());
			glo.put("level", level);
			glo.put("proxyName", user.getProxyName());
			glo.put("userId", user.getId());
			glo.put("money", moneyDao.getMoney(user.getId()));
			glo.put("firstDepositMoney", glo.getBigDecimal("firstDepositTotalMoney"));
			glo.put("secDepositMoney", glo.getBigDecimal("secDepositTotalMoney"));
			proxyArr.add(glo);
			totalBalance = totalBalance.add(glo.getBigDecimal("money"));
			withdrawAmount = withdrawAmount.add(glo.getBigDecimal("withdrawAmount"));
			depositAmount = BigDecimalUtil.addAll(depositAmount, glo.getBigDecimal("depositAmount"), glo.getBigDecimal("depositArtificial"));
			firstDepositSum += glo.getInteger("firstDeposit");
			secDepositSum += glo.getInteger("secDeposit");
			firstDepositMoneySum = BigDecimalUtil.addAll(firstDepositMoneySum,glo.getBigDecimal("firstDepositTotalMoney"));
			secDepositMoneySum = BigDecimalUtil.addAll(secDepositMoneySum,glo.getBigDecimal("secDepositTotalMoney"));
		}
		Map<String, Object> aggs = new HashMap<>();
		aggs.put("withdrawAmountSum", withdrawAmount);
		aggs.put("depositAmountSum", depositAmount);
		aggs.put("firstDepositMoneySum", firstDepositMoneySum);
		aggs.put("secDepositMoneySum", secDepositMoneySum);
		aggs.put("secDepositSum", secDepositSum);
		aggs.put("firstDepositSum", firstDepositSum);
		aggs.put("moneySum", totalBalance);
		proxyPage.setAggsData(aggs);
		JSONObject result = (JSONObject) JSON.toJSON(proxyPage);
		result.put("rows", proxyArr);
		return result;
	}

	@Override
	public Page<RiskReportVo> adminRiskReport(Long stationId, String gameType, Date begin, Date end, String username,
			String proxyName, String degreeIds, String agentName, String sortName, String sortOrder) {
		Long proxyId = null, userId = null;
		boolean isRecommend = false;
		if (StringUtils.isNotEmpty(proxyName)) {
			SysUser proxy = userDao.findOneByUsername(proxyName, stationId, null);
			if (proxy == null) {
				throw new ParamException(BaseI18nCode.proxyUnExist);
			}
			isRecommend = (proxy.getType() == UserTypeEnum.MEMBER.getType());
			proxyId = proxy.getId();
		}
		if (StringUtils.isNotEmpty(username)) {
			SysUser user = userDao.findOneByUsername(username, stationId, null);
			if (user == null) {
				throw new ParamException(BaseI18nCode.memberUnExist);
			}
			userId = user.getId();
		}
		Page<RiskReportVo> p = sysUserDailyMoneyDao.adminRiskReport(stationId, gameType, begin, end, proxyId, userId,
				sortName, sortOrder, degreeIds, agentName, isRecommend);
		if (p.hasRows()) {
			Map<Long, String> degreeMap = new HashMap<>();
			String tmp;
			for (RiskReportVo v : p.getRows()) {
				tmp = degreeMap.get(v.getDegreeId());
				if (tmp == null) {
					tmp = degreeService.getName(v.getDegreeId(), stationId);
					degreeMap.put(v.getDegreeId(), tmp);
				}
				v.setDegreeName(tmp);
				v.setRealName(userInfoService.getRealName(v.getUserId(), stationId));
			}
		}
		return p;
	}

	@Override
	public Map<String, BigDecimal[]> dailyChartsMoneyRepot(Long stationId, Date begin, Date end) {
		Map<String, BigDecimal[]> map = new HashMap<>();

		map.put("chessBetAmount", new BigDecimal[3]);
		map.put("liveBetAmount", new BigDecimal[3]);
		map.put("egameBetAmount", new BigDecimal[3]);
		map.put("sportBetAmount", new BigDecimal[3]);
		map.put("esportBetAmount", new BigDecimal[3]);
		map.put("fishingBetAmount", new BigDecimal[3]);
		map.put("lotBetAmount", new BigDecimal[3]);

		List<SysUserDailyMoney> sysUserDailyMonies = sysUserDailyMoneyDao.dailyChartsMoneyRepot(stationId, begin, end);
		sysUserDailyMonies.forEach(sysUserDailyMoney -> {
			int dateMargin = DateUtil.getDayMargin(sysUserDailyMoney.getStatDate(), end);
			map.get("chessBetAmount")[dateMargin] = sysUserDailyMoney.getChessBetAmount();
			map.get("liveBetAmount")[dateMargin] = sysUserDailyMoney.getLiveBetAmount();
			map.get("egameBetAmount")[dateMargin] = sysUserDailyMoney.getEgameBetAmount();
			map.get("sportBetAmount")[dateMargin] = sysUserDailyMoney.getSportBetAmount();
			map.get("esportBetAmount")[dateMargin] = sysUserDailyMoney.getEsportBetAmount();
			map.get("fishingBetAmount")[dateMargin] = sysUserDailyMoney.getFishingBetAmount();
			map.get("lotBetAmount")[dateMargin] = sysUserDailyMoney.getLotBetAmount();
		});
		for (BigDecimal[] bigDecimals : map.values()) {
			for (int i = 0; i < 3; i++) {
				if (bigDecimals[i] == null) {
					bigDecimals[i] = BigDecimal.ZERO;
				}
			}
		}
		return map;
	}

	@Override
	public Map<String, BigDecimal> dailyChartsWinOrLossRepot(Long stationId, Date begin, Date end) {
		Map<String, BigDecimal> map = new HashMap<>();

		SysUserDailyMoney sysUserDailyMoney = sysUserDailyMoneyDao.globalReport(stationId, begin, end, null, null, null,
				null, null, null, null, null, false);
		// 体育输赢
		BigDecimal sport = nullToZero(
				BigDecimalUtil.subtract(sysUserDailyMoney.getSportBetAmount(), sysUserDailyMoney.getSportWinAmount()));
		map.put("sportWinOrLose", sport);
		// 真人输赢
		BigDecimal live = nullToZero(
				BigDecimalUtil.subtract(sysUserDailyMoney.getLiveBetAmount(), sysUserDailyMoney.getLiveWinAmount()));
		map.put("liveWinOrLose", live);
		// 电子输赢
		BigDecimal egame = nullToZero(
				BigDecimalUtil.subtract(sysUserDailyMoney.getEgameBetAmount(), sysUserDailyMoney.getEgameWinAmount()));
		map.put("egameWinOrLose", egame);
		// 棋牌输赢
		BigDecimal chess = nullToZero(
				BigDecimalUtil.subtract(sysUserDailyMoney.getChessBetAmount(), sysUserDailyMoney.getChessWinAmount()));
		map.put("chessWinOrLose", chess);
		// 彩票输赢
		BigDecimal lot = nullToZero(
				BigDecimalUtil.subtract(sysUserDailyMoney.getLotBetAmount(), sysUserDailyMoney.getLotWinAmount()));
		map.put("lotteryWinOrLose", lot);
		// 电竞输赢
		BigDecimal esport = nullToZero(BigDecimalUtil.subtract(sysUserDailyMoney.getEsportBetAmount(),
				sysUserDailyMoney.getEsportWinAmount()));
		map.put("esportWinOrLose", esport);
		// 捕鱼输赢
		BigDecimal fishing = nullToZero(BigDecimalUtil.subtract(sysUserDailyMoney.getFishingBetAmount(),
				sysUserDailyMoney.getFishingWinAmount()));
		map.put("fishingWinOrLose", fishing);
		return map;
	}

	@Override
	public Map<String, BigDecimal[]> dailyChartsFinanceRepot(Long stationId, Date begin, Date end) {
		Map<String, BigDecimal[]> map = new HashMap<>();

		map.put("withdrawAmount", new BigDecimal[3]);
		map.put("depositAmount", new BigDecimal[3]);
		map.put("liveRebateAmount", new BigDecimal[3]);
		map.put("proxyRebateNum", new BigDecimal[3]);

		List<SysUserDailyMoney> sysUserDailyMonies = sysUserDailyMoneyDao.dailyChartsFinanceRepot(stationId, begin,
				end);
		sysUserDailyMonies.forEach(sysUserDailyMoney -> {
			int dateMargin = DateUtil.getDayMargin(sysUserDailyMoney.getStatDate(), end);
			map.get("withdrawAmount")[dateMargin] = sysUserDailyMoney.getWithdrawAmount();
			map.get("depositAmount")[dateMargin] = sysUserDailyMoney.getDepositAmount();
			map.get("liveRebateAmount")[dateMargin] = sysUserDailyMoney.getLiveRebateAmount();
			map.get("proxyRebateNum")[dateMargin] = sysUserDailyMoney.getProxyRebateAmount();
		});
		for (BigDecimal[] bigDecimals : map.values()) {
			for (int i = 0; i < 3; i++) {
				if (bigDecimals[i] == null) {
					bigDecimals[i] = BigDecimal.ZERO;
				}
			}
		}
		return map;
	}
}
