import Vue from "vue";
import dataJS from "@/assets/js/date";
import store from '../store'
import { i18n } from '../lang/i18n'

//全局组件
// import Footer from "../components/common/Foot";
// import Header from "../components/common/Head";
// import NoDataImg from "../components/common/NoDataImg";

// 全局注册
// Vue.component("Header", Header);
// Vue.component("Footer", Footer);
// Vue.component("NoDataImg", NoDataImg);

// 简单提示报错函数
Vue.prototype.toastFun = function toastFun(txt) {
  this.$message.error(txt);
};
 //当前站点前端配置信息
    
// 简单提示成功函数
Vue.prototype.$successFun = function toastFun(txt) {
  this.$notify({
    title: "success",
    message: txt,
    type: "success",
  });
};

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

// 静态资源文件设置全局变量
// Vue.prototype.mobileResDomain = mobileResDomain;
Vue.prototype.$dataJS = dataJS;

// 小数点后保留位数
Vue.prototype.retainNumFun = function(num, wei) {
  if (!isNaN(num) && String(num).indexOf(".") != -1) {
    if (!wei) wei = 2;
    var str = String(num).split(".");

    if (wei === "no") return parseFloat(str[0]);

    if (str[1]) {
      num = parseFloat(str[0] + "." + str[1].substring(0, wei));
    }
  }
  return num;
};
