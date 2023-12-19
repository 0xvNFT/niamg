package com.play.dao;

import com.play.cache.CacheKey;
import com.play.cache.CacheUtil;
import com.play.common.Constants;
import com.play.core.PlatformType;
import com.play.model.ThirdPlatform;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.model.vo.ThirdPlatformVo;
import com.play.orm.jdbc.JdbcRepository;
import com.play.orm.jdbc.page.Page;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 第三方游戏开关
 *
 * @author admin
 */
@Repository
public class ThirdPlatformDao extends JdbcRepository<ThirdPlatform> {

    public Page<ThirdPlatform> page(Long stationId, Integer platform) {
        StringBuilder sql = new StringBuilder("select * from third_platform where 1=1");
        Map<String, Object> map = new HashMap<>();
        if (stationId != null) {
            sql.append(" and station_id=:stationId");
            map.put("stationId", stationId);
        }
        if (platform != null) {
            sql.append(" and platform=:platform");
            map.put("platform", platform);
        }
        sql.append(" order by id desc");
        return queryByPage(sql.toString(), map);
    }

    public void changeStatus(Long id, Long stationId, Integer status) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("status", status);
        update("update third_platform set status=:status where id=:id", map);
        CacheUtil.delCache(CacheKey.THIRD_PLATFORM, getKey(stationId));
    }

    private String getKey(Long stationId) {
        return new StringBuilder("plat:").append(stationId).toString();
    }

    public List<ThirdPlatformVo> find(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        return find("select id,platform,status from third_platform where station_id=:stationId order by platform asc",
                new BeanPropertyRowMapper<ThirdPlatformVo>(ThirdPlatformVo.class), map);
    }

    public List<ThirdPlatformVo> findAvaid(Long stationId) {
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("status", Constants.STATUS_ENABLE);
        return find("select id,platform,status from third_platform where status=:status and station_id=:stationId order by platform asc",
                new BeanPropertyRowMapper<ThirdPlatformVo>(ThirdPlatformVo.class), map);
    }


    /**
     * 新增 第三方 游戏 需要全部站点执行sql：
     * with aa as (select id,2,50 from station where status=2)
     * INSERT INTO third_platform(station_id, status, platform)
     * select * from aa ON CONFLICT (station_id,platform) DO NOTHING;
     * <p>
     * 其中50 是@PlatformType 的value
     * 执行完sql 后，需要清理redis db = 3 的缓存
     *
     * @param stationId
     * @return
     */
    public ThirdPlatformSwitch getPlatformSwitch(Long stationId) {
        String key = getKey(stationId);
        ThirdPlatformSwitch thirdPlatformSwitch = CacheUtil.getCache(CacheKey.THIRD_PLATFORM, key, ThirdPlatformSwitch.class);
        if (thirdPlatformSwitch != null) {
            return thirdPlatformSwitch;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        List<ThirdPlatformVo> list = find("select platform,status from third_platform where station_id=:stationId",
                new BeanPropertyRowMapper<>(ThirdPlatformVo.class), map);
        thirdPlatformSwitch = new ThirdPlatformSwitch();
        if (list == null) {
            return thirdPlatformSwitch;
        }
        for (ThirdPlatformVo thirdPlatformVo : list) {
            if (PlatformType.getPlatform(thirdPlatformVo.getPlatform()) == null) {
                continue;
            }
            switch (PlatformType.getPlatform(thirdPlatformVo.getPlatform())) {
                case AG:
                    thirdPlatformSwitch.setAg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case BBIN:
                    thirdPlatformSwitch.setBbin(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case MG:
                    thirdPlatformSwitch.setMg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case EBET:
                // ps.setEbet(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                // case OG:
                // ps.setOg(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                // case AB:
                // ps.setAb(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case PT:
                    thirdPlatformSwitch.setPt(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case PG:
                    thirdPlatformSwitch.setPg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case SKYWIND:
                // ps.setSkywind(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case BG:
                    thirdPlatformSwitch.setBg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case DG:
                    thirdPlatformSwitch.setDg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case TYXJ:
                // ps.setTyxj(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case TYSB:
                    thirdPlatformSwitch.setTysb(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case SBO:
                // ps.setSbo(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                // case TYCR:
                // ps.setTycr(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case CQ9:
                    thirdPlatformSwitch.setCq9(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case DJLH:
                // ps.setDjlh(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case KY:
                    thirdPlatformSwitch.setKy(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                // case VGQP:
                // ps.setVgqp(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                // case BAISON:
                // ps.setBaison(Objects.equals(p.getStatus(), Constants.STATUS_ENABLE));
                // break;
                case AVIA:
                    thirdPlatformSwitch.setAvia(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case LEG:
                    thirdPlatformSwitch.setLeg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case TCG:
                    thirdPlatformSwitch.setTcg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case EVO:
                    thirdPlatformSwitch.setEvo(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case EVOLUTION:
                    thirdPlatformSwitch.setEvolution(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case PP:
                    thirdPlatformSwitch.setPp(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case FG:
                	thirdPlatformSwitch.setFg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                	break;
                case YG:
                    thirdPlatformSwitch.setYg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case IYG:
                    thirdPlatformSwitch.setIyg(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case JL:
                    thirdPlatformSwitch.setJl(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case JDB:
                    thirdPlatformSwitch.setJdb(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case TADA:
                    thirdPlatformSwitch.setTada(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case BS:
                    thirdPlatformSwitch.setBs(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case FB:
                    thirdPlatformSwitch.setFb(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case ES:
                    thirdPlatformSwitch.setEs(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case AWC:
                    thirdPlatformSwitch.setAwc(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case V8POKER:
                    thirdPlatformSwitch.setV8Poker(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
                case VDD:
                    thirdPlatformSwitch.setVdd(Objects.equals(thirdPlatformVo.getStatus(), Constants.STATUS_ENABLE));
                    break;
            }

        }
        CacheUtil.addCache(CacheKey.THIRD_PLATFORM, key, thirdPlatformSwitch);
        return thirdPlatformSwitch;
    }

    public ThirdPlatform findOne(Long stationId, Integer platform) {
        String key = new StringBuilder("tpsId:").append(stationId).append(":p:").append(platform).toString();
        ThirdPlatform tp = CacheUtil.getCache(CacheKey.THIRD_PLATFORM, key, ThirdPlatform.class);
        if (tp != null) {
            return tp;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("stationId", stationId);
        map.put("platform", platform);
        tp = findOne(
                "select id,platform,status from third_platform where station_id=:stationId and platform=:platform limit 1",
                map);
        if (tp != null) {
            CacheUtil.addCache(CacheKey.THIRD_PLATFORM, key, tp);
        }
        return tp;
    }

}
