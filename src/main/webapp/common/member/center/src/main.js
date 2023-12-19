import "babel-polyfill"
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import bus from './bus';
import axiosPack from './http'
import 'url-search-params-polyfill'
import { MessageBox } from 'element-ui';
import { i18n } from './lang/i18n'

// 全局组件
import './plugins'
import './plugins/page'

Vue.prototype.$bus = bus;
Vue.prototype.$axiosPack = axiosPack
// Vue.$t("sureGoIndex")


//路由全局守卫--进入前
// router.beforeEach((to, from, next) => {
//   if (!store.state.userInfo || store.state.userInfo.login) {
//     next();
//   } else {
//     console.log(3333, i18n)
//     MessageBox.alert(i18n.$t("message.sureGoIndex"), response.data.msg || i18n.$t("message.noLoginRe"), {
//       confirmButtonText: i18n.$t("message.confirm"),
//       callback: action => {
//         // location.href = '/index.do'
//         top.location.href = '/index.do'
//       }
//     });
//   }
// });

let loadFailedNum = 0
//模块加载失败
router.onError((error) => {
  if (loadFailedNum < 10) {
    loadFailedNum++
    console.log('' + error)
    const pattern = /Loading chunk (\d)+ failed/g;
    const isChunkLoadFailed = error.message.match(pattern);
    const targetPath = router.history.pending.fullPath;
    if (isChunkLoadFailed) {
      router.replace(targetPath);
    }
  }
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  i18n, // i18n
  render: h => h(App)
}).$mount('#app')
