import httpJS from '../index'

export default {
  sportRecord: (params) => httpJS('/userCenter/third/sportRecord.do', params, 'post'), //体育注单
  chessRecord: (params) => httpJS('/userCenter/third/chessRecord.do', params, 'post'), //棋牌注单
  egameRecord: (params) => httpJS('/userCenter/third/egameRecord.do', params, 'post'), //电子注单
  ptRecord: (params) => httpJS('/userCenter/third/ptRecord.do', params, 'post'), //pt电子注单
  esportRecord: (params) => httpJS('/userCenter/third/esportRecord.do', params, 'post'), //体育注单
  fishRecord: (params) => httpJS('/userCenter/third/fishRecord.do', params, 'post'), //捕鱼注单
  liveRecord: (params) => httpJS('/userCenter/third/liveRecord.do', params, 'post'), //真人注单
  lotRecord: (params) => httpJS('/userCenter/third/lotRecord.do', params, 'post'), //彩票注单
  viewDetail: (params) => httpJS('/userCenter/betHis/viewDetail.do', params, 'post'), //新增跳转三方查看注单接口,用户点击查看详情可以跳转三方接口
  viewDetailLive: (params) => httpJS('/userCenter/betHis/viewDetailLive.do', params, 'post'), //真人注单记录查看详情
}















