package com.play.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.play.common.utils.*;
import com.play.web.var.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.play.common.Constants;
import com.play.core.HotGameTypeEnum;
import com.play.core.UserTypeEnum;
import com.play.dao.StationPromotionDao;
import com.play.dao.SysUserRebateDao;
import com.play.model.StationPromotion;
import com.play.model.StationRebate;
import com.play.model.SysUser;
import com.play.model.SysUserRebate;
import com.play.model.ThirdGame;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationPromotionService;
import com.play.service.StationRebateService;
import com.play.service.SysUserService;
import com.play.service.ThirdGameService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.RebateUtil;
import com.play.web.utils.ServletUtils;
import com.play.web.var.LoginMemberUtil;

/**
 * 代理推会员模式的推广链接表
 *
 * @author admin
 *
 */
@Service
public class StationPromotionServiceImpl implements StationPromotionService {

	@Autowired
	private StationPromotionDao stationPromotionDao;
	@Autowired
	private SysUserService userService;
	@Autowired
	private SysUserRebateDao userRebateDao;
	@Autowired
	private ThirdGameService thirdGameService;
	@Autowired
	private StationRebateService stationRebateService;

	@Override
	public Page<StationPromotion> getPage(Long userId, Long stationId, Integer mode, Integer type, String username,
			String code) {
		String domain = ServletUtils.getDomain();
		Page<StationPromotion> page = stationPromotionDao.getPage(userId, stationId, mode, type, username, code);
		if (page.hasRows()) {
			page.getRows().forEach(x -> {
				x.setLinkUrl(
						(StringUtils.isEmpty(x.getDomain()) ? domain : x.getDomain()) + "/r/" + x.getCode() + ".do");
				x.setLinkUrlEn(
						(StringUtils.isEmpty(x.getDomain()) ? domain : x.getDomain()) + "/vt_" + x.getCode() + ".do");

				// 设置站点对应时区的时间
				x.setTzCreateTime(DateUtil.getTzDateTimeStr(x.getCreateTime(), SystemUtil.getTimezone()));
			});
		}
		return page;
	}

	@Override
	public void delete(Long id, Long stationId) {
		StationPromotion p = stationPromotionDao.findOne(id, stationId);
		if (p == null) {
			throw new ParamException(BaseI18nCode.stationLinkNotExist);
		}
		stationPromotionDao.deleteById(id);
		LogUtils.delLog("删除会员：" + p.getUsername() + " 的推广链接，推广码：" + p.getCode());
	}

	@Override
	public void deleteByProxyId(Long id, Long proxyId, Long stationId) {
		StationPromotion p = stationPromotionDao.findOne(id, stationId);
		if (p == null || !Objects.equals(p.getUserId(), proxyId)) {
			throw new ParamException(BaseI18nCode.stationLinkNotExist);
		}
		stationPromotionDao.deleteById(id);
		LogUtils.delLog("删除代理：" + p.getUsername() + " 的推广链接，推广码：" + p.getCode());
	}

	/**
	 * 添加代理推广链接
	 */
	@Override
	public void adminSaveProxy(StationPromotion p) {
		if (p.getType() == null
				|| (p.getType() != UserTypeEnum.PROXY.getType() && p.getType() != UserTypeEnum.MEMBER.getType())) {
			throw new ParamException(BaseI18nCode.operatingTypeError);
		}
		Long stationId = p.getStationId();
		if (Objects.equals(p.getType(), UserTypeEnum.PROXY.getType())) {
			if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
				// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
				throw new ParamException(BaseI18nCode.stationMutiProxy);
			}
		}
		p.setMode(StationPromotion.MODE_PROXY);
		if (p.getAccessPage() == null) {
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_INDEX);
		}
		SysUser user = userService.findOneByUsername(p.getUsername(), stationId, null);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			throw new ParamException(BaseI18nCode.stationNumProxy);
		}
		if (stationPromotionDao.existForMember(user.getId(), stationId)) {
			throw new ParamException(BaseI18nCode.stationExistLink);
		}
		p.setUsername(user.getUsername());
		p.setUserId(user.getId());
		// 验证会员生成推广链接数，防止恶意刷推广链接
		Integer linkNum = stationPromotionDao.countUserLinkNum(user.getId(), stationId);
		if (linkNum != null && linkNum >= Constants.MAX_PROMO_LINK_NUM) {
			throw new ParamException(BaseI18nCode.stationLinkMutiDel);
		}
		validatorRebate(p);
		p.setAccessNum(0L);
		p.setCode(generateLinkKey(RandomStringUtils.randomInt(5), stationId));
		p.setRegisterNum(0L);
		p.setCreateTime(new Date());
		if (StringUtils.isEmpty(p.getDomain())) {
			p.setDomain(ServletUtils.getDomain());
		}
		stationPromotionDao.save(p);
		LogUtils.addLog("添加代理推广链接" + user.getUsername() + "  推广码：" + p.getCode());
	}

	@Override
	public void autoGenerateLink(SysUser user) {
		if (user.getType() == UserTypeEnum.PROXY.getType()) {
			Long stationId = SystemUtil.getStationId();
			int proxyModel = ProxyModelUtil.getProxyModel(stationId);
			if (!ProxyModelUtil.canBePromo(proxyModel)) {
				throw new BaseException(BaseI18nCode.stationProxySpeedLink);
			}
			Map<String, Object> map = new HashMap<>();
			map.put("proxyModel",proxyModel);
			ThirdGame game = thirdGameService.findOne(stationId);
//			map.put("game", game);
			RebateUtil.getRebateMap(stationId, (Long) user.getId(), map);

			StationPromotion p = new StationPromotion();
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_REGISTER);
			p.setUsername(user.getUsername());
			p.setStationId(user.getStationId());
			p.setType(UserTypeEnum.PROXY.getType());
			p.setUserId(user.getId());
			p.setStationId(stationId);
			if ((Boolean)map.get("fixRebate")) {
				StationRebate sr = (StationRebate) map.get("sr");
				if (sr != null) {
					p.setLive(game.getLive() == 2 ? sr.getLive() : null);
					p.setEgame(game.getEgame() == 2 ? sr.getEgame() : null);
					p.setSport(game.getSport() == 2 ? sr.getSport() : null);
					p.setLottery(game.getLottery() == 2 ? sr.getLottery() : null);
					p.setFishing(game.getFishing() == 2 ? sr.getFishing() : null);
					p.setEsport(game.getEsport() == 2 ? sr.getEsport() : null);
					p.setChess(game.getChess() == 2 ? sr.getChess() : null);
				}
			}else{
				JSONArray liveArray = (JSONArray) map.get("liveArray");
				p.setLive((liveArray != null && liveArray.size() > 0) ? liveArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray egameArray = (JSONArray) map.get("egameArray");
				p.setEgame((egameArray != null && egameArray.size() > 0) ? liveArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray sportArray = (JSONArray) map.get("sportArray");
				p.setSport((sportArray != null && sportArray.size() > 0) ? sportArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray chessArray = (JSONArray) map.get("chessArray");
				p.setChess((chessArray != null && chessArray.size() > 0) ? chessArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray esportArray = (JSONArray) map.get("esportArray");
				p.setEsport((esportArray != null && esportArray.size() > 0) ? esportArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray fishingArray = (JSONArray) map.get("fishingArray");
				p.setFishing((fishingArray != null && fishingArray.size() > 0) ? fishingArray.getJSONObject(0).getBigDecimal("value") : null);
				JSONArray lotteryArray = (JSONArray) map.get("lotteryArray");
				p.setLottery((lotteryArray != null && lotteryArray.size() > 0) ? lotteryArray.getJSONObject(0).getBigDecimal("value") : null);
			}
			adminSaveProxy(p);
		}else if(user.getType() == UserTypeEnum.MEMBER.getType()){
			Long stationId = SystemUtil.getStationId();
			if (!ProxyModelUtil.canBeRecommend(stationId)) {
				throw new BaseException(BaseI18nCode.stationMemberLinker);
			}
			StationPromotion p = new StationPromotion();
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_REGISTER);
			p.setUsername(user.getUsername());
			p.setStationId(user.getStationId());
			adminSaveMember(p);
		}

	}

	/**
	 * 添加会员推荐链接
	 */
	@Override
	public void adminSaveMember(StationPromotion p) {
		p.setMode(StationPromotion.MODE_MEMBER);
		Long stationId = p.getStationId();
		SysUser user = userService.findOneByUsername(p.getUsername(), stationId, null);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		if (user.getType() != UserTypeEnum.MEMBER.getType()) {
			throw new ParamException(BaseI18nCode.stationNotVipNum);
		}
		if (stationPromotionDao.existForMember(user.getId(), stationId)) {
			throw new ParamException(BaseI18nCode.stationExistLink);
		}
		if (p.getAccessPage() == null) {
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_INDEX);
		}
		p.setType(UserTypeEnum.MEMBER.getType());
		p.setUsername(user.getUsername());
		p.setUserId(user.getId());
		p.setAccessNum(0L);
		p.setCode(generateLinkKey(RandomStringUtils.randomInt(5), stationId));
		p.setRegisterNum(0L);
		p.setCreateTime(new Date());
		if (StringUtils.isEmpty(p.getDomain())) {
			p.setDomain(ServletUtils.getDomain());
		}
		stationPromotionDao.save(p);
		LogUtils.addLog("添加会员推荐链接" + user.getUsername() + "  推广码：" + p.getCode());
	}

	@Override
	public void adminModifyMember(Long id, Long stationId, String domain, Integer accessPage) {
		StationPromotion old = stationPromotionDao.findOne(id, stationId);
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationExtendNotLink);
		}
		if (accessPage == null) {
			accessPage = StationPromotion.ACCESS_PAGE_TYPE_INDEX;
		}
		if (StringUtils.isEmpty(domain)) {
			domain = ServletUtils.getDomain();
		}
		stationPromotionDao.updateMemberInfo(id, stationId, domain, accessPage);
		LogUtils.modifyLog("修改会员推荐链接" + old.getUsername() + "  推广码：" + old.getCode());
	}

	@Override
	public void adminModifyProxy(StationPromotion p) {
		if (p.getType() == null
				|| (p.getType() != UserTypeEnum.PROXY.getType() && p.getType() != UserTypeEnum.MEMBER.getType())) {
			throw new ParamException(BaseI18nCode.operatingTypeError);
		}
		Long stationId = p.getStationId();
		if (Objects.equals(p.getType(), UserTypeEnum.PROXY.getType())) {
			if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
				// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
				throw new ParamException(BaseI18nCode.stationMutiProxy);
			}
		}
		if (p.getAccessPage() == null) {
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_INDEX);
		}
		StationPromotion old = stationPromotionDao.findOne(p.getId(), p.getStationId());
		if (old == null) {
			throw new ParamException(BaseI18nCode.stationExtendNotLink);
		}
		p.setUserId(old.getUserId());
		validatorRebate(p);
		stationPromotionDao.updateInfo(p);
		LogUtils.modifyLog("修改代理推广链接" + old.getUsername() + "  推广码：" + p.getCode());
	}

	/**
	 * 生成站点唯一链接码
	 *
	 * @param randomString 链接码
	 * @param stationId    站点ID
	 */
	private String generateLinkKey(String randomString, Long stationId) {
		randomString = stationId + randomString;
		return stationPromotionDao.exist(randomString, stationId)
				? generateLinkKey(RandomStringUtils.randomInt(5), stationId)
				: randomString;
	}

	/**
	 * 推广链接的返点数，不能大于当前用户的返点数
	 * 
	 * @param p
	 */
	private void validatorRebate(StationPromotion p) {
		StationRebate r = stationRebateService.findOne(p.getStationId(), StationRebate.USER_TYPE_PROXY);
		if (r.getType() == StationRebate.TYPE_SAME) {
			p.setLive(r.getLive());
			p.setEgame(r.getEgame());
			p.setChess(r.getChess());
			p.setSport(r.getSport());
			p.setEsport(r.getEsport());
			p.setFishing(r.getFishing());
			p.setLottery(r.getLottery());
			return;
		}
		// 获取当前用户的返点
		SysUserRebate scale = userRebateDao.findOne(p.getUserId(), p.getStationId());
		if (scale == null) {
			throw new ParamException(BaseI18nCode.stationProxyRebate);
		}
		ThirdGame game = thirdGameService.findOne(p.getStationId());
		BigDecimal diff = r.getLevelDiff();
		BigDecimal maxDiff = r.getMaxDiff();
		p.setLive(BigDecimalUtil.absOr0(p.getLive()));
		p.setEgame(BigDecimalUtil.absOr0(p.getEgame()));
		p.setSport(BigDecimalUtil.absOr0(p.getSport()));
		p.setEsport(BigDecimalUtil.absOr0(p.getEsport()));
		p.setChess(BigDecimalUtil.absOr0(p.getChess()));
		p.setFishing(BigDecimalUtil.absOr0(p.getFishing()));
		p.setLottery(BigDecimalUtil.absOr0(p.getLottery()));
		// 判断返点值是否正确
		if (Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getSport(), scale.getSport(), diff, maxDiff,
					HotGameTypeEnum.sport.getTitle());
		}
		if (Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getEgame(), scale.getEgame(), diff, maxDiff,
					HotGameTypeEnum.dianzi.getTitle());
		}
		if (Objects.equals(game.getLive(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getLive(), scale.getLive(), diff, maxDiff, HotGameTypeEnum.real.getTitle());
		}
		if (Objects.equals(game.getChess(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getChess(), scale.getChess(), diff, maxDiff,
					HotGameTypeEnum.qipai.getTitle());
		}
		if (Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getFishing(), scale.getFishing(), diff, maxDiff,
					HotGameTypeEnum.buyu.getTitle());
		}
		if (Objects.equals(game.getEsport(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getEsport(), scale.getEsport(), diff, maxDiff,
					HotGameTypeEnum.dianjing.getTitle());
		}
		if (Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) {
			RebateUtil.verifyThirdRebate(p.getLottery(), scale.getLottery(), diff, maxDiff,
					HotGameTypeEnum.caipiao.getTitle());
		}
	}

	@Override
	public void adminUpdateAccessPage(Long stationId, Integer accessPage) {
		if (accessPage == null) {
			throw new ParamException();
		}
		stationPromotionDao.adminUpdateAccessPage(stationId, accessPage);
	}

	@Override
	public StationPromotion findOneByCode(String code, Long stationId) {
		return stationPromotionDao.findOne(code, stationId);
	}

	@Override
	public StationPromotion findOne(Long id, Long stationId) {
		return stationPromotionDao.findOne(id, stationId);
	}

	@Override
	public List<StationPromotion> getList(Long userId, Long stationId, String username, String code) {
		return stationPromotionDao.getList(userId, stationId, username, code);
	}

	@Override
	public void update(StationPromotion code) {
		stationPromotionDao.update(code);
	}

	@Override
	public StationPromotion findOneNewest(Long userId, Long stationId) {
		return stationPromotionDao.findOneNewest(userId, stationId);
	}

	@Override
	public void addNum(Long id, Long stationId, Integer num, boolean isRegister) {
		stationPromotionDao.addNum(id, stationId, num, isRegister);
	}

	/**
	 * 会员推荐链接
	 */
	@Override
	public StationPromotion memberRecommendLink(Long userId, Long stationId) {
		SysUser user = userService.findOneById(userId, stationId);
		if (user == null) {
			throw new BaseException(BaseI18nCode.memberUnExist);
		}
		if (user.getType() != UserTypeEnum.MEMBER.getType()) {
			throw new ParamException(BaseI18nCode.stationNotVipNum);
		}
		if (!ProxyModelUtil.canBeRecommend(stationId)) {
			throw new ParamException(BaseI18nCode.stationMemberLinker);
		}
		StationPromotion p = stationPromotionDao.findOneByUserId(userId, stationId);
		if (p == null) {
			p = new StationPromotion();
			p.setMode(StationPromotion.MODE_MEMBER);
			p.setStationId(stationId);
			p.setAccessPage(StationPromotion.ACCESS_PAGE_TYPE_INDEX);
			p.setType(UserTypeEnum.MEMBER.getType());
			p.setUsername(user.getUsername());
			p.setUserId(user.getId());
			p.setAccessNum(0L);
			p.setCode(generateLinkKey(RandomStringUtils.randomInt(5), stationId));
			p.setRegisterNum(0L);
			p.setCreateTime(new Date());
			p.setDomain(ServletUtils.getDomain());
			stationPromotionDao.save(p);
		}
		String domain = ServletUtils.getDomain();
		p.setLinkUrl(domain + "/r/" + p.getCode() + ".do");
		p.setLinkUrlEn(domain + "/vt_" + p.getCode() + ".do");
		return p;
	}

	/**
	 * 代理推广链接
	 * 
	 * @param type 120=代理，130=会员
	 */
	@Override
	public StationPromotion saveProxyPromoLink(Integer type, BigDecimal live, BigDecimal egame, BigDecimal sport,
			BigDecimal chess, BigDecimal esport, BigDecimal fishing, BigDecimal lottery, Integer accessPage) {
		if (type == null || (type != UserTypeEnum.PROXY.getType() && type != UserTypeEnum.MEMBER.getType())) {
			throw new ParamException(BaseI18nCode.operatingTypeError);
		}
		SysUser user = LoginMemberUtil.currentUser();
		if (user.getType() != UserTypeEnum.PROXY.getType()) {
			throw new ParamException(BaseI18nCode.stationNumProxy);
		}
		Long stationId = user.getStationId();
		// 验证会员生成推广链接数，防止恶意刷推广链接
		Integer linkNum = stationPromotionDao.countUserLinkNum(user.getId(), stationId);
		if (linkNum != null && linkNum >= Constants.MAX_PROMO_LINK_NUM) {
			throw new ParamException(BaseI18nCode.stationLinkMutiDel);
		}
		if (Objects.equals(type, UserTypeEnum.PROXY.getType())) {
			if (!ProxyModelUtil.isMultiOrAllProxy(stationId)) {
				// 不是全部代理，也不是多级代理 则当前是代理不能再添加代理
				throw new ParamException(BaseI18nCode.stationMutiProxy);
			}
		} else {// 全部代理模式，不能出现会员推会员
			if (ProxyModelUtil.isAllProxy(stationId)) {
				throw new ParamException(BaseI18nCode.stationMemberFirst);
			}
		}

		StationPromotion p = new StationPromotion();
		p.setUserId(user.getId());
		p.setStationId(stationId);
		p.setLive(BigDecimalUtil.absOr0(live));
		p.setEgame(BigDecimalUtil.absOr0(egame));
		p.setSport(BigDecimalUtil.absOr0(sport));
		p.setChess(BigDecimalUtil.absOr0(chess));
		p.setEsport(BigDecimalUtil.absOr0(esport));
		p.setFishing(BigDecimalUtil.absOr0(fishing));
		p.setLottery(BigDecimalUtil.absOr0(lottery));
		validatorRebate(p);
		p.setUsername(user.getUsername());
		p.setType(type);
		p.setMode(StationPromotion.MODE_PROXY);
		p.setRegisterNum(0L);
		p.setAccessNum(0L);
		p.setCreateTime(new Date());
		p.setAccessPage(accessPage == null ? StationPromotion.ACCESS_PAGE_TYPE_INDEX : accessPage);
		p.setCode(generateLinkKey(RandomStringUtils.randomInt(5), stationId));
		p.setDomain(ServletUtils.getDomain());
		stationPromotionDao.save(p);
		return p;
	}

	@Override
	public void updateAccessPage(Long id, Long userId, Long stationId, Integer accessPage) {
		StationPromotion link = stationPromotionDao.findOne(id, stationId);
		if (link == null || !link.getUserId().equals(userId)) {
			throw new BaseException(BaseI18nCode.stationExtendNotLink);
		}
		if (accessPage == null || (accessPage != StationPromotion.ACCESS_PAGE_TYPE_REGISTER
				&& accessPage != StationPromotion.ACCESS_PAGE_TYPE_INDEX
				&& accessPage != StationPromotion.ACCESS_PAGE_TYPE_ACTIVITY)) {
			accessPage = StationPromotion.ACCESS_PAGE_TYPE_REGISTER;
		}
		link.setAccessPage(accessPage);
		stationPromotionDao.updateAccessPage(id, accessPage);
	}
}
