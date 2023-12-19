import httpJS from '../index'

export default {
  moneyHistoryInfo: (params) => httpJS('/userCenter/report/moneyHistoryInfo.do', params, 'post'), //收入支出类型（账变报表）
  moneyHistoryList: (params) => httpJS('/userCenter/report/moneyHistoryList.do', params, 'post'), //收入支出详情（账变报表）
  depositRecordList: (params) => httpJS('/userCenter/report/depositRecordList.do', params, 'post'), //充值记录
  withdrawRecordList: (params) => httpJS('/userCenter/report/withdrawRecordList.do', params, 'post'), //提现记录
  teamReport: (params) => httpJS('/userCenter/report/teamReport.do', params, 'post'), //团队报表
}
