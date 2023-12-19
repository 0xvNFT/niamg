import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = { //要设置的全局访问的state对象
  theme: '', //皮肤
  loadPage: false, //全局加载中状态
  onOffBtn: {}, //登陆之前开关信息
  afterLoginOnOffBtn: {}, //登陆之后开关信息
  gameOnOff: [], //首页菜单数据
  userInfo: {},//用户信息
  thirdPageUrl: undefined,//其他页面链接
  activeData: [],//优惠活动数据
  pageWidth: '',//屏幕宽
  helpData: [],//帮助中心数据
  configJs: {},//当前站点前端配置信息
}

const getters = { //实时监听state值的变化(最新状态)
  updateTheme: state => state.theme,
  updateLoadPage: state => state.loadPage,
  updateOnOffBtn: state => state.onOffBtn,
  updateAfterLoginOnOffBtn: state => state.afterLoginOnOffBtn,
  updateGameOnOff: state => state.gameOnOff,
  updateUserInfo: state => state.userInfo,
  updateThirdPageUrl: state => state.thirdPageUrl,
  updateActiveData: state => state.activeData,
  updatePageWidth: state => state.pageWidth,
  updateHelpData: state => state.helpData,
  updateConfigJs: state => state.configJs,
}

const mutations = { //自定义改变state初始值的方法，这里面的参数除了state之外还可以再传额外的参数(变量或对象);
  changeTheme: (state, data) => state.theme = data || '',
  changeLoadPage: (state, data) => state.loadPage = data || false,
  changeOnOffBtn: (state, data) => state.onOffBtn = data || {},
  changeAfterLoginOnOffBtn: (state, data) => state.afterLoginOnOffBtn = data || {},
  changeGameOnOff: (state, data) => state.gameOnOff = data || [],
  changeUserInfo: (state, data) => state.userInfo = data || {},
  changeThirdPageUrl: (state, data) => state.thirdPageUrl = data || undefined,
  changeActiveData: (state, data) => state.activeData = data || [],
  changePageWidth: (state, data) => state.pageWidth = data || '',
  changeHelpData: (state, data) => state.helpData = data || [],
  changeConfigJs: (state, data) => state.configJs = data || {},
}

const actions = { //自定义触发mutations里函数的方法，context与store 实例具有相同方法和属性
  getTheme: (context, data) => context.commit('changeTheme', data),
  getLoadPage: (context, data) => context.commit('changeLoadPage', data),
  getOnOffBtn: (context, data) => context.commit('changeOnOffBtn', data),
  getAfterLoginOnOffBtn: (context, data) => context.commit('changeAfterLoginOnOffBtn', data),
  getGameOnOff (context, data) {
    context.commit('changeGameOnOff', data);
  },
  getUserInfo: (context, data) => context.commit('changeUserInfo', data),
  getThirdPageUrl: (context, data) => context.commit('changeThirdPageUrl', data),
  getActiveData: (context, data) => context.commit('changeActiveData', data),
  getPageWidth: (context, data) => context.commit('changePageWidth', data),
  getHelpData: (context, data) => context.commit('changeHelpData', data),
  getConfigJs: ({ commit }, data) => commit('changeConfigJs', data),
}

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
})

