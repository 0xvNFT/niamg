import "babel-polyfill";
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "url-search-params-polyfill";
import { i18n } from "./lang/i18n";
// 引入自定义指令
import { amountFormat, banned } from "@/directives"

//iconfont 引用
import './assets/css/icon/iconfont.css'

// 页面加载进度条
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

// 页面加载进度条配置
NProgress.configure({
  easing: 'ease',
  speed: 500,
  // showSpinner: false,
  trickleSpeed: 200,
  minimum: 0.3
})

// 全局组件
import "./plugins";
import "./plugins/page";

Vue.config.productionTip = false;

let loadFailedNum = 0;
//模块加载失败
router.onError((error) => {
  if (loadFailedNum < 10) {
    loadFailedNum++;
    const pattern = /Loading chunk (\d)+ failed/g;
    const isChunkLoadFailed = error.message.match(pattern);
    const targetPath = router.history.pending.fullPath;
    if (isChunkLoadFailed) {
      router.replace(targetPath);
    }
  }
});

//路由全局守卫--进入前
router.beforeEach((to, from, next) => {
  let routePath = ['/index', '/active', '/activeDetails', '/appDown', '/help',]
  if (routePath.indexOf(to.path) === -1) {
    let times = 0
    let timer = setInterval(() => {
      if (times > 300) {
        clearInterval(timer)
        timer = null
      }
      times++

      if (store.state.userInfo.login === false || store.state.userInfo.login) {
        clearInterval(timer)
        timer = null

        if (!store.state.userInfo.login) {
          // if (!store.state.userInfo.login && !userInfo.login) {
          router.push('/index');
        } else {
          // 页面加载进度条 开启
          NProgress.start();
          next();
        }
      }
    }, 100);
  } else {
    // 页面加载进度条 开启
    NProgress.start();
    next();
  }
});

router.afterEach((to, from) => {
  // 页面加载进度条 关闭
  NProgress.done()
  // 每次切换页面，滚动到最顶部
  document.body.scrollTop = 0
  document.documentElement.scrollTop = 0
  if (document.querySelector('.con_box')) {
    document.querySelector('.con_box').scrollTop = 0
  }
})

// 自定义指令挂载到全局
Vue.directive('amountFormat', amountFormat)
Vue.directive('banned', banned)

new Vue({
  router,
  store,
  i18n, // i18n
  render: (h) => h(App),
}).$mount("#app");
