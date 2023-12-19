import httpJS from '../index'
import active from './active'
import order from './order'
import proxy from './proxy'
import record from './record'
import user from './user'

let index = {
  getStationConfig: (params) => httpJS('/userCenter/getStationConfig.do?app', params, 'get'), //登陆之前开关配置
  getConfig: (params) => httpJS('/userCenter/getConfig.do', params, 'get'), //登陆之后开关配置
  getInfo: (params) => httpJS('/userInfo/getInfo.do?app_=' + new Date().getTime(), params, 'get'), //用户信息
  indexPage: (params) => httpJS('/indexPage.do', params, 'get'), // 获取登录注册配置
  regConfigData: (params) => httpJS('/regConfigData.do', params, 'post'), //获取注册信息
  login: (params) => httpJS('/login.do', params, 'post'), //登录
  guestRegister: (params) => httpJS('/registerGuest.do', params, 'post'), //试玩注册
  // register: (params) => httpJS('/register.do', params, 'post'), //正常注册
  register: (params) => httpJS('/register.do', params, 'post'), //正常注册

  emailLogin: (params) => httpJS('/emailLogin.do', params, 'post'), //邮箱登录
  emailRegister: (params) => httpJS('/emailRegister.do', params, 'post'), //邮箱注册
  reqEmailVcode: (params) => httpJS('/reqEmailVcode.do', params, 'post'), //注册获取邮箱验证码
  smsLogin: (params) => httpJS('/smsLogin.do', params, 'post'), //手机登录
  smsRegister: (params) => httpJS('/smsRegister.do', params, 'post'), //手机注册
  reqSmsCode: (params) => httpJS('/reqSmsCode.do', params, 'post'), //注册获取手机验证码
  userAllInfo: (params) => httpJS('/userCenter/userAllInfo.do', params, 'post'), //个人总览信息
  messageList: (params) => httpJS('/userCenter/msgManage/messageList.do', params, 'post'), //站内信列表
  readMessage: (params) => httpJS('/userCenter/msgManage/readMessage.do', params, 'post'), //站内信已读
  logout: () => httpJS('/native/v2/logout.do', 'post'), //退出登录
  getBanner: (params) => httpJS('/banner.do?code=1', params, 'get'), //获取轮播图
  getTabs: (params) => httpJS('/getTabs.do', params, 'get'), //获取游戏分类
  getGames: (params) => httpJS('/getGames.do', params, 'get'), //获取游戏
  stationFake: (params) => httpJS('/stationFake.do', params, 'get'), // 获取在线人数、获胜玩家数据
  changeLanguage: (params) => httpJS('/changeLanguage.do', params, 'get'),//切换语言传给后台
  getGame2: (params) => httpJS('/native/v2/getGame2.do', params, 'get'),//获取Slots下的游戏tabs的接口
  getFactoryGames: (params) => httpJS('/getFactoryGames.do', params, 'get'),//获取电子厂商下的所有游戏
  gamesanfangUrl: (url, params) => httpJS(url, params, 'get'),//获取电子厂商下的所有游戏
  forwardPg2: (url, params) => httpJS('/forwardPg2.do', params, 'get'),//获取电子厂商下的所有游戏
}

const API = Object.assign({}, index, active, order, proxy, record, user)

export default API















