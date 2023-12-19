import Vue from 'vue'
import Vuex from 'vuex'
import { Loading } from 'element-ui';
import { i18n } from '../lang/i18n'

Vue.use(Vuex)

const state = { //要设置的全局访问的state对象
  langType: localStorage.centerLang || 'zh-CN', //app语言
  lang: {}, //app语言
  showSpin: false, //路由加载中 spinner 是否显示
  onOffBtn: '', //开关信息
  userInfo: '', //用户信息
  gameOnOff: '', //首页菜单数据
  curLeftMenu: '', //首页菜单数据
  configJs: {}//当前站点前端配置信息
}

const getters = { //实时监听state值的变化(最新状态) 
  updateLangType: state => state.langType,
  updateLang: state => state.lang,
  updateShowSpin: state => state.showSpin,
  updateOnOffBtn: state => state.onOffBtn,
  updateUserInfo: state => state.userInfo,
  updateGameOnOff: state => state.gameOnOff,
  updateCurLeftMenu: state => state.curLeftMenu,
  updateConfigJs: state => state.configJs,
}

const mutations = { //自定义改变state初始值的方法，这里面的参数除了state之外还可以再传额外的参数(变量或对象);
  changeLangType (state, data) {
    if (data) state.langType = data;
    else state.langType = '';
  },
  changeLang (state, data) {
    if (data) state.lang = data;
    else state.lang = '';
  },
  changeShowSpin (state, data) {
    var loadingInstance = Loading.service({
      lock: true,
      text: i18n.t('loading'),
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });
    if (data) {
      state.showSpin = data;
      // loadingInstance = Loading.service({
      //   lock: true,
      //   text: 'Loading',
      //   spinner: 'el-icon-loading',
      //   background: 'rgba(0, 0, 0, 0.7)'
      // });
    } else {
      state.showSpin = '';
      loadingInstance.close();
      // Vue.nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
      //   loadingInstance.close();
      // });
    }
  },
  changeOnOffBtn (state, data) {
    if (data) state.onOffBtn = data;
    else state.onOffBtn = '';
  },
  changeUserInfo (state, data) {
    if (data) state.userInfo = data;
    else state.userInfo = '';
  },
  changeGameOnOff (state, data) {
    if (data) state.gameOnOff = data;
    else state.gameOnOff = '';
  },
  changeCurLeftMenu (state, data) {
    if (data) state.curLeftMenu = data;
    else state.curLeftMenu = '';
  },
  changeConfigJs: (state, data) => state.configJs = data || {},
}

const actions = { //自定义触发mutations里函数的方法，context与store 实例具有相同方法和属性
  getLangType (context, data) {
    context.commit('changeLangType', data);
  },
  getLang (context, data) {
    context.commit('changeLang', data);
  },
  getShowSpin (context, data) {
    context.commit('changeShowSpin', data);
  },
  getOnOffBtn (context, data) {
    context.commit('changeOnOffBtn', data);
  },
  getUserInfo (context, data) {
    context.commit('changeUserInfo', data);
  },
  getGameOnOff (context, data) {
    context.commit('changeGameOnOff', data);
  },
  getCurLeftMenu (context, data) {
    context.commit('changeCurLeftMenu', data);
  },
  getConfigJs: ({ commit }, data) => commit('changeConfigJs', data),
}

const store = new Vuex.Store({
  state,
  getters,
  mutations,
  actions,
});

export default store
