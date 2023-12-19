package com.play.service.impl;

import java.util.*;


import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.spring.config.i18n.I18nTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.play.common.Constants;
import com.play.common.utils.LogUtils;
import com.play.dao.SysUserDao;
import com.play.dao.SysUserGroupDao;
import com.play.model.SysUser;
import com.play.model.SysUserGroup;
import com.play.orm.jdbc.page.Page;
import com.play.service.SysUserGroupService;
import com.play.web.exception.BaseException;
import com.play.web.exception.ParamException;
import com.play.web.i18n.BaseI18nCode;
import com.play.web.var.GuestTool;
import org.springframework.util.CollectionUtils;

/**
 * 会员组别信息
 *
 * @author admin
 */
@Service
public class SysUserGroupServiceImpl implements SysUserGroupService {

    @Autowired
    private SysUserGroupDao sysUserGroupDao;
    @Autowired
    private SysUserDao sysUserDao;

    @Override
    public void init(Long stationId, Long partnerId) {
        SysUserGroup group = new SysUserGroup();
        group.setStationId(stationId);
        group.setPartnerId(partnerId);
        group.setName(BaseI18nCode.staionUserGroupDefault.getMessage());
        group.setCreateDatetime(new Date());
        group.setMemberCount(0L);
        group.setStatus(Constants.STATUS_ENABLE);
        group.setOriginal(Constants.USER_ORIGINAL);
        sysUserGroupDao.save(group);
    }

    @Override
    public Page<SysUserGroup> getPage(Long stationId) {
        Page<SysUserGroup> groupPage = sysUserGroupDao.getPage(stationId);
        if (!CollectionUtils.isEmpty(groupPage.getRows())) {
            // 根据name去重
//            List<SysUserGroup> unique = groupPage.getRows().stream().map(sysUserGroup -> {
//                BaseI18nCode baseI18nCode = BaseI18nCode.getBaseI18nCode(sysUserGroup.getName());
//                if (baseI18nCode != null) {
//                    sysUserGroup.setName(I18nTool.getMessage(baseI18nCode));
//                }
//                return sysUserGroup;
//            }).collect(
//                    Collectors.collectingAndThen(
//                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysUserGroup::getName))), ArrayList::new)
//            );
//            groupPage.setRows(unique);
//            groupPage.setTotal(unique.size());
        }
        return groupPage;
    }

    @Override
    public void save(SysUserGroup group) {
        group.setRemark(StringUtils.deleteWhitespace(group.getRemark()));
        group.setCreateDatetime(new Date());
        group.setMemberCount(0L);
        group.setStatus(Constants.STATUS_ENABLE);
        group.setOriginal(Constants.USER_UNORIGINAL);
        sysUserGroupDao.save(group);
        LogUtils.addLog("添加会员组别" + group.getName() + " 每日最高提款次数" + group.getDailyDrawNum() + " 最大提款金额"
                + group.getMaxDrawMoney() + " 最小提款金额" + group.getMinDrawMoney());
    }

    @Override
    public SysUserGroup findOne(Long id, Long stationId) {
        return sysUserGroupDao.findOne(id, stationId);
    }

    @Override
    public SysUserGroup findOne(Long id, Long stationId, String name) {
        return sysUserGroupDao.findOne(id, stationId, name);
    }


    @Override
    public void update(SysUserGroup group) {
        group.setRemark(StringUtils.deleteWhitespace(group.getRemark()));
        SysUserGroup old = sysUserGroupDao.findOne(group.getId(), group.getStationId());
        if (old == null) {
            throw new ParamException(BaseI18nCode.userGroupNotExist);
        }
        StringBuilder sb = new StringBuilder(I18nTool.getMessage(BaseI18nCode.updateVipGroup));
        sb.append(old.getName()).append(I18nTool.getMessage(BaseI18nCode.titleModify)).append(group.getName());
        sb.append(I18nTool.getMessage(BaseI18nCode.dailyMaxDrawCash)).append(old.getDailyDrawNum());
        sb.append(I18nTool.getMessage(BaseI18nCode.modifyBe)).append(group.getDailyDrawNum());
        sb.append(I18nTool.getMessage(BaseI18nCode.maxDrawCash)).append(old.getMaxDrawMoney());
        sb.append(I18nTool.getMessage(BaseI18nCode.modifyBe)).append(group.getMaxDrawMoney());
        sb.append(I18nTool.getMessage(BaseI18nCode.minDrawCash)).append(old.getMinDrawMoney());
        sb.append(I18nTool.getMessage(BaseI18nCode.modifyBe)).append(group.getMinDrawMoney());
        old.setName(group.getName());
        old.setRemark(group.getRemark());
        old.setDailyDrawNum(group.getDailyDrawNum());
        old.setMaxDrawMoney(group.getMaxDrawMoney());
        old.setMinDrawMoney(group.getMinDrawMoney());
        sysUserGroupDao.update(old);
        CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
        LogUtils.modifyLog(sb.toString());
    }

    @Override
    public void updateStatus(Long id, Integer status, Long stationId) {
        SysUserGroup old = sysUserGroupDao.findOne(id, stationId);
        if (old == null) {
            throw new ParamException(BaseI18nCode.userGroupNotExist);
        }
        String s = I18nTool.getMessage(BaseI18nCode.enable);
        if (status == null || status != Constants.STATUS_ENABLE) {
            status = Constants.STATUS_DISABLE;
            s = I18nTool.getMessage(BaseI18nCode.disable);
        } else {
            SysUserGroup sysUserGroup = this.findOne(id, stationId, old.getName());
            if (null != sysUserGroup) {
                throw new BaseException(BaseI18nCode.stationMemberGroupSelectedSame);
            }
        }
        if (Objects.equals(old.getOriginal(), Constants.USER_ORIGINAL) && status == Constants.STATUS_DISABLE) {
            throw new BaseException(BaseI18nCode.stationDefVipGroupNot);
        }
        boolean memberExist = sysUserDao.findOneByGroupId(id, stationId, Constants.STATUS_ENABLE);
        if (memberExist) {
            throw new BaseException(BaseI18nCode.memberExistInGroupSwitch);
        }
        if (!Objects.equals(status, old.getStatus())) {
            sysUserGroupDao.updateStatus(id, status, stationId);
            LogUtils.modifyStatusLog("会员组别:" + old.getName() + " 状态修改成：" + s);
        }
    }

    @Override
    public void restatMemberCount(Long stationId) {
        sysUserGroupDao.restatMemberCount(stationId);
        LogUtils.modifyLog("重新统计会员组别的会员数量");
    }

    @Override
    public void delete(Long id, Long stationId) {
        SysUserGroup old = sysUserGroupDao.findOne(id, stationId);
        if (old == null) {
            throw new ParamException(BaseI18nCode.userGroupNotExist);
        }
        if (Objects.equals(Constants.USER_ORIGINAL, old.getOriginal())) {
            throw new BaseException(BaseI18nCode.stationDefVipGroupNotDel);
        }
        boolean memberExist = sysUserDao.findOneByGroupId(id, stationId, Constants.STATUS_ENABLE);
        if (memberExist) {
            throw new BaseException(BaseI18nCode.memberExistInGroup);
        }
        sysUserGroupDao.deleteById(id);
        String key = ":id" + id + ":s" + stationId;
        CacheUtil.delCacheByPrefix(CacheKey.USER_GROUP);
        LogUtils.delLog("删除会员组别:" + old.getName());
    }

    @Override
    public List<SysUserGroup> find(String sql, Map<String, Object> map) {
        return sysUserGroupDao.find(sql, map);
    }

    @Override
    public List<SysUserGroup> find(Long stationId, Integer status) {
        return sysUserGroupDao.find(stationId, status);
    }

    @Override
    public void changeMemberGroup(Long userId, Long stationId, Long groupId) {
        SysUserGroup newGroup = sysUserGroupDao.findOne(groupId, stationId);
        if (newGroup == null) {
            throw new BaseException(BaseI18nCode.groupUnExist);
        }
        SysUser user = sysUserDao.findOne(userId, stationId);
        if (user == null) {
            throw new BaseException(BaseI18nCode.memberUnExist);
        }
        SysUserGroup old = sysUserGroupDao.findOne(user.getGroupId(), stationId);
        String oldName = "";
        if (old != null) {
            oldName = old.getName();
        }
        sysUserDao.changeGroup(userId, stationId, groupId);
        sysUserGroupDao.restatMemberCount(stationId);
        LogUtils.delLog("修改会员 " + user.getUsername() + " 旧组别:" + oldName + " 新组别:" + newGroup.getName());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public void batchChangeGroup(String usernames, Long groupId, Long stationId) {
        if (groupId == null || StringUtils.isEmpty(usernames)) {
            throw new ParamException();
        }
        SysUserGroup newGroup = sysUserGroupDao.findOne(groupId, stationId);
        if (newGroup == null) {
            throw new BaseException(BaseI18nCode.groupUnExist);
        }
        String[] uns = usernames.split(" |,|\n");
        StringBuilder errors = new StringBuilder();
        SysUser user = null;
        for (String un : uns) {
            un = StringUtils.trim(un);
            if (StringUtils.isEmpty(un)) {
                continue;
            }
            user = sysUserDao.findOneByUsername(un.toLowerCase(), stationId, null);
            if (user == null || GuestTool.isGuest(user)) {
                errors.append(un).append(BaseI18nCode.stationUserNotExist.getMessage());
                continue;
            }
            try {
                sysUserDao.changeGroup(user.getId(), stationId, groupId);
            } catch (Exception e) {
                errors.append(un).append(":").append(e);
            }
        }
        sysUserGroupDao.restatMemberCount(stationId);
        LogUtils.delLog("批量修改会员组别 " + usernames + " 新组别:" + newGroup.getName());
        if (errors.length() > 0) {
            errors.deleteCharAt(errors.length() - 1);
            throw new BaseException(BaseI18nCode.stationErrorMessage + errors.toString());
        }
    }

    @Override
    public String getGroupNames(String groupIds, Long stationId) {
        if (StringUtils.isNotEmpty(groupIds)) {
            SysUserGroup g = null;
            StringBuilder sb = new StringBuilder();
            for (String id : groupIds.split(",")) {
                long l = NumberUtils.toLong(id);
                if (l > 0) {
                    g = sysUserGroupDao.findOne(l, stationId);
                    if (g != null) {
                        sb.append(g.getName()).append(",");
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    @Override
    public String getName(Long id, Long stationId) {
        SysUserGroup g = sysUserGroupDao.findOne(id, stationId);
        if (g != null) {
            return g.getName();
        }
        return "";
    }

    @Override
    public SysUserGroup getDefault(Long stationId) {
        return sysUserGroupDao.getDefault(stationId);
    }

    @Override
    public Long getDefaultId(Long stationId) {
        SysUserGroup d = sysUserGroupDao.getDefault(stationId);
        if (d == null) {
            throw new BaseException(BaseI18nCode.stationDefVipGroupNotExist);
        }
        return d.getId();
    }
}
