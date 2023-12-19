import httpJS from '../index'

export default {
  getInviteOverview: (params) => httpJS('/userCenter/inviteOverview.do', params, 'get'),//获取邀请人获得的返佣概况
  getInviteDeposits: (params) => httpJS('/userCenter/inviteDeposits.do', params, 'get'),//获取被邀请人的存款信息列表
  getInviteBonus: (params) => httpJS('/userCenter/inviteBonus.do', params, 'get'),//获取被邀请人的奖金信息列表
  agentRegPromotionInfo: (params) => httpJS('/userCenter/agentManage/agentRegPromotionInfo.do', params, 'post'),// 获取会员注册选项
  agentRegPromotionSave: (params) => httpJS('/userCenter/agentManage/agentRegPromotion/save.do', params, 'post'),// 注册推广链接
  agentRegPromotionList: (params) => httpJS('/userCenter/agentManage/agentRegPromotion/list.do', params, 'post'),// 推广链接列表
  agentRegPromotionDelete: (params) => httpJS('/userCenter/agentManage/agentRegPromotion/delete.do', params, 'post'),// 删除推广链接
  recommendList: (params) => httpJS('/userCenter/agentManage/recommendList.do', params, 'post'),// 推荐总览记录
  recommendInfo: (params) => httpJS('/userCenter/agentManage/recommendInfo.do', params, 'post'),// 我的推荐注册
  userListData: (params) => httpJS('/userCenter/agentManage/userListData.do', params, 'post'),// 用户列表记录
  kickbackResetInfo: (params) => httpJS('/userCenter/agentManage/kickbackResetInfo.do', params, 'post'), // 被访问的用户是代理 返点设定接口
  kickbackInfoForMember: (params) => httpJS('/userCenter/agentManage/kickbackInfoForMember.do', params, 'post'),// 被访问的用户是会员 返点设定接口
  agentTeam: (params) => httpJS('/userCenter/agentManage/agentTeam.do', params, 'post'),// 团队列表记录详情
  userTeamListData: (params) => httpJS('/userCenter/agentManage/userTeamListData.do', params, 'post'),// 团队列表记录筛选
  recommendAddMember: (params) => httpJS('/userCenter/agentManage/recommendAddMember.do', params, 'post'),// 推荐信息
  agentManageregisterSave: (params) => httpJS('/userCenter/agentManage/registerSave.do', params, 'post'),// 注册管理注册下级
  levelInfo: (params) => httpJS('/userCenter/agentManage/levelInfo.do', params, 'post'),// 显示层级选择框
}















