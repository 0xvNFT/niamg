import Vue from "vue";
import publicJs from "@/assets/js/public";
import API from "@/http/api";
import store from "../store";
import { i18n } from "../lang/i18n";

//全局组件
import Header from "@/components/common/Head.vue";
import Footer from "@/components/common/Foot.vue";

// 全局注册
Vue.component("Header", Header);
Vue.component("Footer", Footer);

//单独站点 修改语言
Vue.prototype.configJsFlag = function (name) {
  if (
    store.state.configJs[store.state.onOffBtn.lang] &&
    store.state.configJs[store.state.onOffBtn.lang][name]
  ) {
    return store.state.configJs[store.state.onOffBtn.lang][name];
  } else {
    return i18n.t(name);
  }
};



//单项数据流
Vue.prototype.$bus = new Vue();
// 全局API挂载
Vue.prototype.$API = API;
Vue.prototype.$publicJs = publicJs;

//初始化 清空sessionStorage缓存
sessionStorage.removeItem("mubanLang");
