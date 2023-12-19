/*
 * @Author: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @Date: 2023-11-14 22:39:06
 * @LastEditors: error: error: git config user.name & please set dead value or install git && error: git config user.email & please set dead value or install git & please set dead value or install git
 * @LastEditTime: 2023-11-17 20:26:43
 * @FilePath: \muban\src\http\api\active.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import httpJS from '../index'

export default {
  articleList: (params) => httpJS('/userCenter/msgManage/articleList.do', params, 'get'),//网站公告
  adviceList: (params) => httpJS('/userCenter/advice/adviceList.do', params, 'post'), // 获取反馈记录列表
  viewAdvice: (params) => httpJS('/userCenter/advice/viewAdvice.do', params, 'post'), // 建议反馈记录查看详情
  saveAdvice: (params) => httpJS('/userCenter/advice/saveAdvice.do', params, 'get'),//建议反馈提交
  updateAdvice: (params) => httpJS('/userCenter/advice/updateAdvice.do', params, 'post'), //建议反馈提交详情
  active: (params) => httpJS('/activityPage.do', params, 'post'),//优惠活动
  help: (params) => httpJS('/article/list.do?type=3', params, 'post'),//帮助中心
  sign: (params) => httpJS('/userCenter/sign.do', params, 'post'),//签到
  signIn: (params) => httpJS('/signIn.do', params, 'post'),//签到记录
  signRuleConfig: (params) => httpJS('/signRuleConfig2.do', params, 'post'),//签到规则
  getTurntablesRewards: (params) => httpJS('/turnlate.do', params), // 请求大转盘奖品
  spinTurntable: (params) => httpJS('/userTurnlate/award.do', params), // 玩大转盘游戏
  getUsersWinRecord: (params) => httpJS('/userTurnlate/turnlateRecordList.do', params), // 获取该用户的大转盘中奖纪录

}















