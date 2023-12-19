import httpJS from '../index'

export default {
  getUserBank: (params) => httpJS('/userCenter/userBank/list.do?type=bank', params, 'post'), //获取银行卡信息
  getUserUsdt: (params) => httpJS('/userCenter/userBank/list.do?type=usdt', params, 'post'), //获取USDT信息
  addUserBank: (params) => httpJS('/userCenter/userBank/bankAddSave.do', params, 'post'), //新增银行卡/USDT
  deleteUsdt: (params) => httpJS('/userCenter/userBank/delUserBank.do', params, 'get'), //删除USDT按钮
  rechargeInfo: (params) => httpJS('/userCenter/finance/rechargeInfo.do', params, 'post'), //获取一般入款支付
  getScoreExchangeInfo: (params) => httpJS('/userCenter/getScoreExchangeInfo.do', params, 'post'), //积分兑换信息
  getScoreHisData: (params) => httpJS('/userCenter/scoreHisData.do', params, 'post'), //积分兑换记录
  getConfirmExchange: (params) => httpJS('/userCenter/confirmExchange.do', params, 'post'), //积分兑换方法
  getBetHis: (params) => httpJS('/userCenter/betHis/list.do', params, 'post'), //打码量记录
  getUsdtInfo: (params) => httpJS('/userCenter/finance/usdtInfo.do', params, 'post'), //获取usdt信息
  rechargeOfflineSave: (params) => httpJS('/userCenter/finance/rechargeOfflineSave.do', params, 'post'), //提交存款
  getTronLink: (params) => httpJS('/userCenter/tronLink/getUserTronLink.do', params, 'post'), //获取用户TronLink信息
  getOnlineInfo: (params) => httpJS('/userCenter/finance/depositList.do?payCode=online', params, 'post'), //获取在线入款支付方式
  checkoutCounterByType: (params) => httpJS('/userCenter/finance/checkoutCounterByType.do', params, 'post'), //获取在线入款支付方式
  pay: (params) => httpJS('/userCenter/finance/pay.do', params, 'post'), //提交充值
  withdrawInfo: (params) => httpJS('/userCenter/finance/withdrawInfo.do', params, 'post'), //获取提款信息
  withdrawSave: (params) => httpJS('/userCenter/finance/withdrawSave.do', params, 'post'), //提交提款
  getSecurityInfo: (params) => httpJS('/userCenter/getSecurityInfo.do', params, 'post'),//安全中心获取安全信息接口
  updateSecurityInfo: (params) => httpJS('/userCenter/updateSecurityInfo.do', params, 'post'),//安全中心修改联系方式
  userPwdModifySave: (params) => httpJS('/userCenter/userPwdModifySave.do', params, 'post'),//安全中心密码更新
  updateRealName: (params) => httpJS('/userCenter/updateRealName.do', params, 'post'),//安全中心设定真实姓名
  getBalance: (params) => httpJS('/thirdTrans/getBalance.do', params, 'post'), // 获取三方额度数据
  thirdRealTransMoney: (params) => httpJS('/thirdTrans/thirdRealTransMoney.do', params, 'post'), // 转换额度
  exchangeHistory: (params) => httpJS('/userCenter/exchangeHistory.do', params, 'post'), //额度转换记录
  exchangePageInfo: (params) => httpJS('/userCenter/third/exchangePageInfo.do', params, 'post'), //获取三方额度数据
  kickbackResetSave: (params) => httpJS('/userCenter/agentManage/kickbackResetSave.do', params, 'post'),// 保存当前用户返点
  getDepositStragety: (params) => httpJS('/userCenter/finance/getDepositStragety.do', params, 'post'),// 获取充值活动

  personReport: (params) => httpJS('/userCenter/report/personReport.do', params, 'post'), //提现记录
  autoTranout: (params) => httpJS('/autoTranout.do', params, 'post'), //把钱从三方转出来

}