package com.play.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import com.play.model.vo.StaionTurntableImageVo;
import com.play.spring.config.i18n.I18nTool;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.utils.CheckImagesFormatUtil;
import com.play.web.utils.MultipartFileToFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.play.common.utils.LogUtils;
import com.play.dao.StationTurntableAwardDao;
import com.play.dao.StationTurntableGiftDao;
import com.play.model.StationTurntableGift;
import com.play.orm.jdbc.page.Page;
import com.play.service.StationTurntableGiftService;
import com.play.web.exception.BaseException;

/**
 * 转盘奖品表
 *
 * @author admin
 *
 */
@Service
public class StationTurntableGiftServiceImpl implements StationTurntableGiftService {

	private Logger logger = LoggerFactory.getLogger(StationTurntableGiftServiceImpl.class);

	@Autowired
	private StationTurntableGiftDao stationTurntableGiftDao;
	@Autowired
	private StationTurntableAwardDao turntableAwardDao;
	//图片的宽度
	private final int imageWithNum = 37;
	//图片的高度
	private final int imageHeightNum = 37;

	@Override
	public Page<StationTurntableGift> getPage(Long stationId) {
		return stationTurntableGiftDao.getPage(stationId);
	}

	@Override
	public void save(StationTurntableGift t) {
//		checkImgValid(t);
		t.setCreateDatetime(new Date());
		if (null == t.getProductName() || t.getProductName().isEmpty()) {
			throw new BaseException(BaseI18nCode.productNameIsRequired);
		}
//		boolean nameExist = turntableAwardDao.findOneByName(t.getStationId(), t.getProductName());
//		if (nameExist) {
//			throw new BaseException("Product Name already exist");
//		}
		if (t.getPrice() == null || t.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BaseException(BaseI18nCode.productPriceIsRequired);
		}
		stationTurntableGiftDao.save(t);
		LogUtils.addLog("新增大转盘奖品" + t.getProductName());
	}

	@Override
	public void modify(StationTurntableGift t) {
		StationTurntableGift old = stationTurntableGiftDao.findOne(t.getId(), t.getStationId());
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundNotExist);
		}
//		if (!Objects.equals(t.getProductImg(),old.getProductImg())){
//			checkImgValid(t);
//		}
		String oldName = old.getProductName();
		old.setProductName(t.getProductName());
		old.setProductDesc(t.getProductDesc());
		old.setPrice(t.getPrice());
		old.setProductImg(t.getProductImg());
		if (null == t.getProductName() || t.getProductName().isEmpty()) {
			throw new BaseException(BaseI18nCode.productNameIsRequired);
		}
		if (t.getPrice() == null || t.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			throw new BaseException(BaseI18nCode.productPriceIsRequired);
		}
		stationTurntableGiftDao.update(t);
		LogUtils.modifyLog("修改大转盘奖品,旧名称:" + oldName + " 新名称:" + t.getProductName());

	}

	@Override
	public void delete(Long id, Long stationId) {
		StationTurntableGift old = stationTurntableGiftDao.findOne(id, stationId);
		if (old == null) {
			throw new BaseException(BaseI18nCode.stationRoundNotExist);
		}

		// 该商品正在别的奖项引用的数量
		Integer awardCount = turntableAwardDao.countByUsingGiftId(id);
		if (awardCount > 0) {
			throw new BaseException(BaseI18nCode.stationShopGoodBy,new Object[]{ awardCount});
		}
		stationTurntableGiftDao.deleteById(id);
		LogUtils.delLog("删除大转盘奖品" + old.getProductName());
	}

	@Override
	public List<StationTurntableGift> getCombo(Long stationId) {
		return stationTurntableGiftDao.getList(stationId);
	}

	@Override
	public StationTurntableGift findOne(Long id, Long stationId) {
		return stationTurntableGiftDao.findOne(id, stationId);
	}

	@Override
	public List<StaionTurntableImageVo> findListImages(Long id, Long stationId) {
		List<StaionTurntableImageVo> imagesPath = new ArrayList<>();
		StaionTurntableImageVo staionTurntableImageVo = new StaionTurntableImageVo();
		List<StationTurntableGift> stationTurntableGifts = stationTurntableGiftDao.getListGifts(id,stationId);

		//for (StationTurntableGift stationTurntableGift : stationTurntableGifts){
		stationTurntableGifts.forEach( str -> {
						staionTurntableImageVo.setProductImg(str.getProductImg());

		});
		imagesPath.add(staionTurntableImageVo);
		return imagesPath;
	}

	@Override
	public String getName(Long id, Long stationId) {
		if (id == null || id <= 0) {
			return "";
		}
		StationTurntableGift g = stationTurntableGiftDao.findOne(id, stationId);
		if (g == null) {
			return "";
		}
		return g.getProductName();
	}

	/**
	 * 效验图片的合法性
	 * @param gift
	 */
	private void checkImgValid(StationTurntableGift gift){
		String extension = "";
		int i = gift.getProductImg().lastIndexOf('.');
		if (i > 0) {
			extension = gift.getProductImg().substring(i+1);
		}

		if(!"png".equals(extension)&&!Objects.equals("png",extension)){
			throw new BaseException(BaseI18nCode.stationImageNotPngImage);
		}
		if (!MultipartFileToFile.testUrl(gift.getProductImg())) {
			throw new BaseException(I18nTool.getMessage(BaseI18nCode.stationImageExistNot));
		}
		Boolean imageValid = false;
		try {
			if (!MultipartFileToFile.getFile(gift.getProductImg()).exists()) {
				throw new BaseException(BaseI18nCode.stationImageExistNot);
			}
			imageValid = CheckImagesFormatUtil.checkImageScale(MultipartFileToFile.getFile(gift.getProductImg()), imageWithNum, imageHeightNum);
			if (!imageValid) {
				throw new BaseException(BaseI18nCode.stationTurntableGiftImageInvalid);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (BaseException e) {
			logger.error(e.getMessage());
			throw new BaseException(BaseI18nCode.stationTurntableGiftImageInvalid);
		}
	}
}
