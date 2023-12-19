package com.play.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.play.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.StationDepositStrategyDao;
import com.play.model.MnyDepositRecord;
import com.play.model.StationDepositStrategy;
import com.play.model.SysUser;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationDepositStrategyService;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;

/**
 * 用户充值赠送策略
 *
 * @author admin
 */
@Service
public class StationDepositStrategyServiceImpl implements StationDepositStrategyService {
	private static final Logger logger = LoggerFactory.getLogger(StationDepositStrategyServiceImpl.class);
	@Autowired
	private StationDepositStrategyDao stationDepositStrategyDao;

	@Override
	public Page<StationDepositStrategy> getPage(Integer depositType, Integer giftType, Integer valueType, Date begin,
			Date end, Long stationId) {
		return stationDepositStrategyDao.getPage(depositType, giftType, valueType, begin, end, stationId);
	}

	@Override
	public StationDepositStrategy getOne(Long id, Long stationId) {
		return stationDepositStrategyDao.getOne(id, stationId);
	}

	@Override
	public void delete(Long id, Long stationId) {
		stationDepositStrategyDao.delete(id, stationId);
	}

	@Override
	public void addSave(StationDepositStrategy com) {
		if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
			throw new ParamException(BaseI18nCode.selectActivityTime);
		}
		if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
			throw new ParamException(BaseI18nCode.endMustBeforeStart);
		}
		com.setCreateDatetime(new Date());
		com.setStatus(Constants.STATUS_ENABLE);
		if (com.getDepositCount() == null) {
			com.setDepositCount(0);
		}
		if (com.getMinMoney() == null) {
			com.setMinMoney(BigDecimal.ZERO);
		}
		if (com.getMaxMoney() == null) {
			com.setMaxMoney(new BigDecimal("10000000000000"));
		}
		if (com.getMinMoney().compareTo(com.getMaxMoney()) >= 0) {
			throw new ParamException(BaseI18nCode.depositMoneyMustLtMax);
		}
		validStrategy(com);
		com.setDepositConfigIds(StringUtils.isNotEmpty(com.getDepositConfigIds()) ? com.getDepositConfigIds() : "");
		stationDepositStrategyDao.save(com);
		LogUtils.addLog("添加存款赠送策略");
	}


	private void validStrategy(StationDepositStrategy com) {
		List<StationDepositStrategy> list = stationDepositStrategyDao.findByDepositType(com.getDepositType(),
				com.getStationId(), Constants.STATUS_ENABLE, com.getValueType(), null, null);
		if (list == null || list.isEmpty()) {
			return;
		}
		long start = com.getStartDatetime().getTime();
		long end = com.getEndDatetime().getTime();
		BigDecimal min = com.getMinMoney();
		BigDecimal max = com.getMaxMoney();
		Set<Long> degreeIds, groupIds, configIds;
		Set<Long> curDegreeIds = getSet(com.getDegreeIds());
		Set<Long> curGroupIds = getSet(com.getGroupIds());
		Set<Long> curConfigIds = getSet(com.getDepositConfigIds());
		// 同一个支付方式，同一时间内，同一个充值金额范围内，同一个会员等级/组别范围内，同种赠送类型，同种赠送频率不能存在2条赠送策略
		for (StationDepositStrategy mcs : list) {
			if (com.getId() != null && com.getId().equals(mcs.getId())) {
				continue;
			}
			if (mcs.getEndDatetime().getTime() < start || mcs.getStartDatetime().getTime() > end) {
				continue;
			}
			if (mcs.getMaxMoney().compareTo(min) < 0 || mcs.getMinMoney().compareTo(max) > 0) {
				continue;
			}
			if (!mcs.getDepositCount().equals(com.getDepositCount())) {
				continue;
			}
			degreeIds = getSet(mcs.getDegreeIds());
			for (Long did : curDegreeIds) {
				if (degreeIds.contains(did)) {// 等级有包含
					groupIds = getSet(mcs.getGroupIds());
					for (Long gid : curGroupIds) {
						if (groupIds.contains(gid)) {// 组别有包含
							if (curConfigIds.isEmpty()) {
								throw new ParamException(BaseI18nCode.activityConflict, mcs.getDesc());
							}
							configIds = getSet(mcs.getDepositConfigIds());
							if (configIds.isEmpty()) {
								throw new ParamException(BaseI18nCode.activityConflict, mcs.getDesc());
							}
							for (Long cid : curConfigIds) {
								if (configIds.contains(cid)) {
									throw new ParamException(BaseI18nCode.depositMethodConflict, mcs.getDesc());
								}
							}
						}
					}
				}
			}
		}
	}

	private Set<Long> getSet(String ids) {
		Set<Long> set = new HashSet<Long>();
		if (ids == null) {
			return set;
		}
		for (String id : ids.split(",")) {
			if (StringUtils.isNotEmpty(id)) {
				set.add(NumberUtils.toLong(id));
			}
		}
		return set;
	}

	@Override
	public void updStatus(Integer status, Long id, Long stationId) {
		StationDepositStrategy mcs = stationDepositStrategyDao.getOne(id, stationId);
		if (mcs == null) {
			throw new ParamException(BaseI18nCode.depositStrategyNotExist);
		}
		String statusStr = I18nTool.getMessage(BaseI18nCode.disable);
		if (status == Constants.STATUS_ENABLE) {
			if (mcs.getEndDatetime().before(new Date())) {
				throw new ParamException(BaseI18nCode.endMustBeforeStart);
			}
			validStrategy(mcs);
			statusStr = I18nTool.getMessage(BaseI18nCode.enable);
		} else {
			status = Constants.STATUS_DISABLE;
		}
		if (!mcs.getStatus().equals(status)) {
			stationDepositStrategyDao.updStatus(id, status);
			LogUtils.modifyLog("修改存款赠送策略：" + id + "状态为：" + statusStr);
		}
	}

	@Override
	public void update(StationDepositStrategy com) {
		if (com.getEndDatetime() == null || com.getStartDatetime() == null) {
			throw new ParamException(BaseI18nCode.selectActivityTime);
		}
		if (com.getEndDatetime().getTime() <= com.getStartDatetime().getTime()) {
			throw new ParamException(BaseI18nCode.endMustBeforeStart);
		}
		if (com.getMinMoney() == null) {
			com.setMinMoney(BigDecimal.ZERO);
		}
		if (com.getMaxMoney() == null) {
			com.setMaxMoney(new BigDecimal("10000000"));
		}
		if (com.getMinMoney().compareTo(com.getMaxMoney()) >= 0) {
			throw new ParamException(BaseI18nCode.depositMoneyMustLtMax);
		}
		StationDepositStrategy old = stationDepositStrategyDao.getOne(com.getId(), com.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.selectDepositStrategy);
		}
		old.setDepositType(com.getDepositType());
		old.setGiftType(com.getGiftType());
		old.setValueType(com.getValueType());

		old.setDepositCount(com.getDepositCount());
		if (old.getDepositCount() == null) {
			old.setDepositCount(0);
		}
		old.setGiftValue(com.getGiftValue());
		old.setBackGiftType(com.getBackGiftType());
		old.setBackBonusLevelDiff(com.getBackBonusLevelDiff());
		old.setUpperLimit(com.getUpperLimit());
		old.setMinMoney(com.getMinMoney());
		old.setMaxMoney(com.getMaxMoney());
		old.setBonusBackBetRate(com.getBonusBackBetRate());
		old.setBonusBackValue(com.getBonusBackValue());
		old.setBetRate(com.getBetRate());
		old.setStartDatetime(com.getStartDatetime());
		old.setEndDatetime(com.getEndDatetime());
		old.setRemark(com.getRemark());
		old.setDayupperLimit(com.getDayupperLimit() == null ? BigDecimal.ZERO : com.getDayupperLimit());
		old.setDepositConfigIds(StringUtils.isNotEmpty(com.getDepositConfigIds()) ? com.getDepositConfigIds() : "");
		old.setDegreeIds(com.getDegreeIds());
		old.setGroupIds(com.getGroupIds());
		if (old.getStatus() == Constants.STATUS_ENABLE) {
			validStrategy(old);
		}
		stationDepositStrategyDao.update(old);
		LogUtils.modifyLog("修改存款赠送策略" + old.getId());
	}

	/**
	 * 根据账变类型获取存款类型
	 */
	private Integer getDepositType(Integer depositType) {
		if(depositType == null)
			return null;
		Integer type = 0;
		if (depositType == MnyDepositRecord.TYPE_BANK) {
			type = StationDepositStrategy.TYPE_BANK;
		} else if (depositType == MnyDepositRecord.TYPE_HAND) {
			type = StationDepositStrategy.TYPE_ARTIFICIAL;
		} else if (depositType == MnyDepositRecord.TYPE_ONLINE) {
			type = StationDepositStrategy.TYPE_ONLINE;
		} else if (depositType == MnyDepositRecord.TYPE_FAST) {
			type = StationDepositStrategy.TYPE_FAST;
		}
		return type;
	}

	@Override
	public List<StationDepositStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
											   Integer depositType, BigDecimal money, Date depositDate, Long payId) {
		return filter(account, allDepositCount, dayDepositCount, depositType, money, depositDate, payId, false);
	}

	@Override
	public List<StationDepositStrategy> filter(SysUser account, Integer allDepositCount, Integer dayDepositCount,
			Integer depositType, BigDecimal money, Date depositDate, Long payId, boolean filterBeforeFirstDeposit) {

		Integer type = getDepositType(depositType);
		logger.error("depositStrategy filter, username:{}, stationId:{}, money:{}, allDepositCount:{}, dayDepositCount:{}, depositType:{}, payId:{}, filterBeforeFirstDeposit:{}, depositDate:{}",
				account.getUsername(), account.getStationId(), money, allDepositCount, dayDepositCount, depositType, payId, filterBeforeFirstDeposit, DateUtil.toDatetimeStr(depositDate));

		List<StationDepositStrategy> list = stationDepositStrategyDao.findByDepositType(type, account.getStationId(),
				Constants.STATUS_ENABLE, null, depositDate, money);
		list = filterByDepositCount(allDepositCount, dayDepositCount, list, filterBeforeFirstDeposit);
		list = filterByMemberDegreeAndGroup(account, list);
		list = filterByDepositConfig(list, payId);
		list = filterSameValueType(list);

		logger.error("depositStrategy filter, list:{}", JSON.toJSON(list));
		return list;
	}

	/**
	 * 赠送同种类型的只取一个,积分，彩金每种只能一个
	 *
	 * @param list
	 * @return
	 */
	private List<StationDepositStrategy> filterSameValueType(List<StationDepositStrategy> list) {
		if (list == null || list.isEmpty()) {
			return list;
		}
		logger.error("depositStrategy filter filterSameValueType, size:{}", list.size());

		Collections.sort(list, new Comparator<StationDepositStrategy>() {
			@Override
			public int compare(StationDepositStrategy o1, StationDepositStrategy o2) {// 赠送频率倒序
				return o2.getDepositCount().compareTo(o1.getDepositCount());
			}
		});
		Map<Integer, StationDepositStrategy> map = new HashMap<>();
		for (StationDepositStrategy s : list) {
			if (map.containsKey(s.getValueType())) {
				continue;
			}
			map.put(s.getValueType(), s);
		}
		return new ArrayList<>(map.values());
	}

	private List<StationDepositStrategy> filterByDepositCount(Integer depositCount, Integer dayDepositCount, List<StationDepositStrategy> list) {
		return filterByDepositCount(depositCount, dayDepositCount, list, false);
	}
	/**
	 * 根据存款次数过滤
	 *
	 * @param depositCount 所有存款次数
	 * @param dayDepositCount 今日存款次数
	 * @param filterBeforeFirstDeposit 是否是从未充值之前获取策略
	 * @param list
	 * @return
	 */
	private List<StationDepositStrategy> filterByDepositCount(Integer depositCount, Integer dayDepositCount,
			List<StationDepositStrategy> list, boolean filterBeforeFirstDeposit) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		logger.error("depositStrategy filter filterByDepositCount, size:{}", list.size());

		List<StationDepositStrategy> rlist = new ArrayList<>();
		for (StationDepositStrategy m : list) {
			switch (m.getDepositCount()) {
			case StationDepositStrategy.deposit_count_0:// 每次
				rlist.add(m);
				break;
			case StationDepositStrategy.deposit_count_1:// 首充
				if (!filterBeforeFirstDeposit) {
					if (depositCount == 1) {
						rlist.add(m);
					}
				}
//				if (depositCount == 1 || filterBeforeFirstDeposit) {
//					rlist.add(m);
//				}
				break;
			case StationDepositStrategy.deposit_count_2:// 第二次充值
				if (depositCount == 2) {
					rlist.add(m);
				}
				break;
			case StationDepositStrategy.deposit_count_3:// 第三次充值
				if (depositCount == 3) {
					rlist.add(m);
				}
				break;
			case StationDepositStrategy.deposit_count_222:// 前两次充值
				if (depositCount == 1 || depositCount == 2) {
					rlist.add(m);
				}
				break;
			case StationDepositStrategy.deposit_count_333:// 前三次充值
				if (depositCount == 1 || depositCount == 2 || depositCount == 3) {
					rlist.add(m);
				}
				break;
			case StationDepositStrategy.deposit_count_444:// 每日首充
				if (dayDepositCount == 1) {
					rlist.add(m);
				}
				break;
			default:
				break;
			}
		}
		return rlist;
	}

	/**
	 * 根据会员等级和组别过滤策略
	 */
	private List<StationDepositStrategy> filterByMemberDegreeAndGroup(SysUser user, List<StationDepositStrategy> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}
		logger.error("depositStrategy filter filterByMemberDegreeAndGroup, size:{}", list.size());

		if (user == null || user.getDegreeId() == null || user.getDegreeId() <= 0 || user.getGroupId() == null
				|| user.getGroupId() <= 0) {
			logger.error("depositStrategy filterByMemberDegreeAndGroup, user degree or group error");
			return null;
		}
		List<StationDepositStrategy> rlist = new ArrayList<>();
		for (StationDepositStrategy m : list) {
			Set<Long> degreeSet = getSet(m.getDegreeIds());
			if (degreeSet.contains(user.getDegreeId())) {// 等级包含在里面
				Set<Long> groupSet = getSet(m.getGroupIds());
				if (groupSet.contains(user.getGroupId())) {// 组别包含在里面
					rlist.add(m);
				}
			}
		}
		return rlist;
	}

	private List<StationDepositStrategy> filterByDepositConfig(List<StationDepositStrategy> list, Long payId) {
		if (list == null || list.isEmpty()) {
			return list;
		}
		logger.error("depositStrategy filter filterByDepositConfig, size:{}", list.size());

		return list.stream().filter(x -> StringUtils.isEmpty(x.getDepositConfigIds())
				|| (x.getDepositConfigIds()).contains("," + payId + ",")).collect(Collectors.toList());
	}
}
