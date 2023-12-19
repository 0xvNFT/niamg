package com.play.common.utils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.play.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.play.core.PlatformType;
import com.play.model.Station;
import com.play.model.SysUser;
import com.play.model.ThirdGame;
import com.play.model.vo.PlatformVo;
import com.play.model.vo.ThirdPlatformSwitch;
import com.play.service.ThirdCenterService;
import com.play.service.YGCenterService;

public class PlatformTypeUtil {
	private final static Logger log = LoggerFactory.getLogger(PlatformTypeUtil.class);
	
	private static YGCenterService ygCenterService = SpringUtils.getBean(YGCenterService.class);
	
    private static ThirdCenterService thirdCenterService = SpringUtils.getBean(ThirdCenterService.class);
    // private static ThirdGameService gameService =
    // SpringUtils.getBean(ThirdGameService.class);
    // private static ThirdPlatformService platformService =
    // SpringUtils.getBean(ThirdPlatformService.class);

    /**
     * 获取类型
     */
    public static void getThirdType(Map<String, Object> map, ThirdGame game, ThirdPlatformSwitch platform,
                                    List<PlatformVo> typeList) {
        JSONArray third = new JSONArray();
        PlatformType[] types;
        if (typeList != null && !typeList.isEmpty()) {
            types = new PlatformType[typeList.size()];
            int i = 0;
            for (PlatformVo v : typeList) {
                types[i] = v.getType();
                i++;
            }
        } else {
            types = PlatformType.values();
        }
        for (PlatformType platformType : types) {
            switch (platformType) {
                case AG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE) && !Objects.equals(game.getFishing(), Constants.STATUS_ENABLE))
                            || !platform.isAg()) {
                        continue;
                    }
                    break;
                case BBIN:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isBbin()) {
                        continue;
                    }
                    break;
                case MG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isMg()) {
                        continue;
                    }
                    break;
                // case AB:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isAb()) {
                // continue;
                // }
                // break;
                case PT:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isPt()) {
                        continue;
                    }
                    break;
                // case OG:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isOg()) {
                // continue;
                // }
                // break;
                // case SKYWIND:
                // if (!Objects.equals(game.getEgame(), 2) || !platform.isSkywind()) {
                // continue;
                // }
                // break;
                case KY:
                    if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isKy()) {
                        continue;
                    }
                    break;
                case BG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) || !platform.isBg()) {
                        continue;
                    }
                    break;
                case CQ9:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isCq9()) {
                        continue;
                    }
                    break;
                // case EBET:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isEbet()) {
                // continue;
                // }
                // break;
                // case BAISON:
                // if (!Objects.equals(game.getChess(), 2) || !platform.isBaison()) {
                // continue;
                // }
                // break;
                // case SBO:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isSbo()) {
                // continue;
                // }
                // break;
                // case TYXJ:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isTyxj()) {
                // continue;
                // }
                // break;
                case TYSB:
                    if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isTysb()) {
                        continue;
                    }
                    break;
                // case DJLH:
                // if (!Objects.equals(game.getEsport(), 2) || !platform.isDjlh()) {
                // continue;
                // }
                // break;
                // case VGQP:
                // if (!Objects.equals(game.getChess(), 2) || !platform.isVgqp()) {
                // continue;
                // }
                // break;
                // case TYCR:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isTycr()) {
                // continue;
                // }
                // break;
                case AVIA:
                    if (!Objects.equals(game.getEsport(), Constants.STATUS_ENABLE) || !platform.isAvia()) {
                        continue;
                    }
                    break;
                case DG:
                    if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isDg()) {
                        continue;
                    }
                    break;
                case LEG:
                    if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isLeg()) {
                        continue;
                    }
                    break;
                case PG:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isPg()) {
                        continue;
                    }
                    break;
                case EVO:
                    if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEvo()) {
                        continue;
                    }
                    break;
                case EVOLUTION:
                    if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEvolution()) {
                        continue;
                    }
                    break;
                case PP:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) || !platform.isPp()) {
                        continue;
                    }
                    break;    
                case FG:
                	if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isFg()) {
                		continue;
                	}
                	break;
                case JL:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isJl()) {
                        continue;
                    }
                    break;
                case JDB:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isJdb()) {
                        continue;
                    }
                    break;
                case TADA:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isTada()) {
                        continue;
                    }
                    break;
                case BS:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isBs()) {
                        continue;
                    }
                    break;
                case FB:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isFb()) {
                        continue;
                    }
                    break;
                case YG:
                    if ((!Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) || !platform.isYg()) {
                        continue;
                    }
                    break;
                case ES:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isEs()) {
                        continue;
                    }
                    break;

            }

            JSONObject object = new JSONObject();
            object.put("platform", platformType.getValue());
            object.put("name", platformType.getTitle());
            object.put("pname", platformType.name());
            third.add(object);
        }
        map.put("third", third);
    }

    public static void getThirdInfo(Map<String, Object> map, ThirdGame game, ThirdPlatformSwitch platform, SysUser user,
                                    Station station) {
        JSONArray third = new JSONArray();
        for (PlatformType t : PlatformType.values()) {
            switch (t) {
                case AG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE) && !Objects.equals(game.getFishing(), Constants.STATUS_ENABLE))
                            || !platform.isAg()) {
                        continue;
                    }
                    break;
                case BBIN:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isBbin()) {
                        continue;
                    }
                    break;
                case MG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isMg()) {
                        continue;
                    }
                    break;
                // case AB:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isAb()) {
                // continue;
                // }
                // break;
                case PT:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isPt()) {
                        continue;
                    }
                    break;
                case PG:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isPg()) {
                        continue;
                    }
                    break;
                // case OG:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isOg()) {
                // continue;
                // }
                // break;
                // case SKYWIND:
                // if (!Objects.equals(game.getEgame(), 2) || !platform.isSkywind()) {
                // continue;
                // }
                // break;
                case KY:
                    if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isKy()) {
                        continue;
                    }
                    break;
                case BG:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getFishing(), Constants.STATUS_ENABLE)) || !platform.isBg()) {
                        continue;
                    }
                    break;
                case CQ9:
                    if (!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE) || !platform.isCq9()) {
                        continue;
                    }
                    break;
                // case EBET:
                // if (!Objects.equals(game.getLive(), 2) || !platform.isEbet()) {
                // continue;
                // }
                // break;
                // case BAISON:
                // if (!Objects.equals(game.getChess(), 2) || !platform.isBaison()) {
                // continue;
                // }
                // break;
                // case SBO:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isSbo()) {
                // continue;
                // }
                // break;
                // case TYXJ:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isTyxj()) {
                // continue;
                // }
                // break;
                case TYSB:
                    if (!Objects.equals(game.getSport(), Constants.STATUS_ENABLE) || !platform.isTysb()) {
                        continue;
                    }
                    break;
                // case DJLH:
                // if (!Objects.equals(game.getEsport(), 2) || !platform.isDjlh()) {
                // continue;
                // }
                // break;
                // case VGQP:
                // if (!Objects.equals(game.getChess(), 2) || !platform.isVgqp()) {
                // continue;
                // }
                // break;
                // case TYCR:
                // if (!Objects.equals(game.getSport(), 2) || !platform.isTycr()) {
                // continue;
                // }
                // break;
                case AVIA:
                    if (!Objects.equals(game.getEsport(), Constants.STATUS_ENABLE) || !platform.isAvia()) {
                        continue;
                    }
                    break;
                case DG:
                    if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isDg()) {
                        continue;
                    }
                    break;
                case LEG:
                    if (!Objects.equals(game.getChess(), Constants.STATUS_ENABLE) || !platform.isLeg()) {
                        continue;
                    }
                    break;
                case TCG:
                    if (!Objects.equals(game.getLottery(), Constants.STATUS_ENABLE) || !platform.isTcg()) {
                        continue;
                    }
                    break;
                case EVO:
                    if (!Objects.equals(game.getLottery(), Constants.STATUS_ENABLE) || !platform.isEvo()) {
                        continue;
                    }
                    break;
                case EVOLUTION:
                    if (!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) || !platform.isEvolution()) {
                        continue;
                    }
                    break;
                case PP:
                    if ((!Objects.equals(game.getLive(), Constants.STATUS_ENABLE) && !Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)
                            && !Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) || !platform.isPp()) {
                        continue;
                    }
                    break;
                case FG:
                	if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isFg()) {
                		continue;
                	}
                case JL:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isJl()) {
                        continue;
                    }
                	break;
                case JDB:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isJdb()) {
                        continue;
                    }
                    break;
                case TADA:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isTada()) {
                        continue;
                    }
                    break;
                case BS:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isBs()) {
                        continue;
                    }
                    break;
                case YG:
                    if ((!Objects.equals(game.getLottery(), Constants.STATUS_ENABLE)) || !platform.isYg()) {
                        continue;
                    }
                    break;
                case FB:
                    if ((!Objects.equals(game.getSport(), Constants.STATUS_ENABLE)) || !platform.isFb()) {
                        continue;
                    }
                    break;
                case ES:
                    if ((!Objects.equals(game.getEgame(), Constants.STATUS_ENABLE)) || !platform.isEs()) {
                        continue;
                    }
                    break;
            }
            JSONObject object = new JSONObject();
            object.put("platform", t.getValue());
            object.put("name", t.getTitle());
//            try {
//            	if(t == PlatformType.YG) {
//            		object.put("money", ygCenterService.getBalanceForTrans(t, user, station));
//            	}else {
//            		object.put("money", thirdCenterService.getBalanceForTrans(t, user, station));
//            	}
//            } catch (Exception e) {
//                object.put("money", 0);
//                log.error("catch an exception, e:", e);
//            }
            third.add(object);
        }
        map.put("third", third);
    }
}
