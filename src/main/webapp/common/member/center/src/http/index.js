/**
 * axios封装
 * 请求拦截、响应拦截、错误统一处理
 */
import axios from "axios";
import Vue from "vue";
import store from "../store";
import router from "../router";
import { Loading, Notification, MessageBox } from "element-ui";
import { i18n } from '../lang/i18n'

console.log(i18n.messages[i18n.locale]  )
// 加载中
var Toast = "";

// 加载参数
let paramsAll = {
  load: null,
  loadTime: null,
  errType: null,
};
// 是否返回所有数据
let returnAll = false;

/**
 * 创建axios实例
 * @param {Number} timeout [指定请求超时的毫秒数(0表示无超时时间)]
 * @param {Object} headers [服务器响应的头]
 * @param {Object} withCredentials [跨域请求时是否需要使用凭证]
 */
var instance = axios.create({
  timeout: 30000,
  headers: { "X-Requested-With": "XMLHttpRequest" },
  withCredentials: true,
});
// Vue.$t("sureGoIndex")

/**
 * 加载函数 禁止点击蒙层
 * @param {String} msg [加载文本]
 */
const tip = (msg) => {
  if (Toast) Toast.close();
  Toast = Loading.service({
    text: msg || i18n.t('loading'),
    background: "rgba(0, 0, 0, .3)",
    target: "body",
  });
};

/**
 * 请求失败后的错误统一处理
 * @param {Number} status 请求失败的状态码
 */
// 错误信息处理
function errControl (error) {
  let { message, response = {}, config } = error;
  let code = response.status;
  let msg = "";

  if (message.includes("Network Error")) {
    // message为 Network Error 代表断网了
    // msg = '网络连接已断开，请检查网络'
  } else if (message.includes("timeout of 20000ms exceeded")) {
    // 请求超时，这里的 20000ms 对应上方 timeout 设置的值
    msg = i18n.messages[i18n.locale].requestTimeout;
  } else {
    msg = message || code + " error";
  }
  if (msg)
    Notification.error({
      title: "error",
      message: msg,
    });
  // 返回报错信息
  return false;
  // return Promise.reject(error);
}
// const errorHandle = (status) => {
//   let message = "";
//   // 状态码判断
//   switch (status) {
//     case 400:
//       message = "错误请求";
//       break;
//     case 401:
//       message = "未授权，请重新登录";
//       break;
//     case 403:
//       message = "拒绝访问";
//       break;
//     case 404:
//       message = "请求错误，未找到资源";
//       break;
//     case 405:
//       message = "请求方法未允许";
//       break;
//     case 408:
//       message = "请求超时";
//       break;
//     case 500:
//       message = "服务端出错";
//       break;
//     case 501:
//       message = "网络未实现";
//       break;
//     case 502:
//       message = "网络错误";
//       break;
//     case 503:
//       message = "服务不可用";
//       break;
//     case 504:
//       message = "网络超时";
//       break;
//     case 505:
//       message = "http版本不支持该请求";
//       break;
//     case 999:
//       message = "域名访问存在异常";
//       break;
//     default:
//   }

//   message += " " + status;

//   return _errType(message);
// };

/**
 * 报错信息显示方式
 * @param {String} msg [报错显示内容]
 * @param {Number} paramsAll.errType [报错显示形式：1-console，2-alert，默认-toast]
 */
const _errType = (msg) => {
  // 报错马上关闭加载中提示
  if (paramsAll.load) Toast.close();
  // 根据报错类型显示报错信息
  if (msg.indexOf("Network Error") === -1) {
    if (paramsAll.errType === 1) {
      console.log(msg);
    } else if (paramsAll.errType === 2) {
      alert(msg);
    } else {
      // Message.error(msg);
      Notification.error({
        title: "error",
        message: msg,
      });
    }
  } else {
    console.log("网络错误 " + msg);
  }
  // return false
};

/**
 * 请求拦截器
 */
instance.interceptors.request.use(
  (config) => {
    // 在发送请求之前做些什么
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    this.toastFun(i18n.messages[i18n.locale].interceptorErr);
    return Promise.reject(error);
  }
);

/**
 * 响应拦截器
 */
instance.interceptors.response.use(
  (response) => {
    // 如果是返回成功的所有数据直接返回
    if (returnAll) return response;

    // 报错提示，success必须要判断 false，不能是 !success,因为有的页面没有返回success，但是要继续执行
    if (response.data && response.data.msg && response.data.success === false) {
      // 报错如果加载存在，马上关闭
      if (paramsAll.load) Toast.close();

      // 登录失效的情况，清除 vuex 登录状态和缓存，弹窗提示，返回首页
      console.log(window.location.href.includes("usdtIndex"));
      if (response.data.login === false) {
        // 清除缓存
        sessionStorage.userInfo = "";
        // 清除 vuex 登录状态
        store.dispatch("getUserInfo", { login: false });
        // 返回首页
        if (!window.location.href.includes("login")) {
          MessageBox.alert(
            i18n.messages[i18n.locale].MessageBoxContent,
            response.data.msg || i18n.messages[i18n.locale].MessageBoxTitle,
            {
              confirmButtonText: i18n.messages[i18n.locale].confirm,
              callback: (action) => {
                // location.href = '/index.do'
                top.location.href = "/index.do";
              },
            }
          );
        }
        return;
      } else {
        console.log(response.data)
        //usdt管理  /userCenter/tronLink/getUserTronLink，如果返回为false，且content==null，则不返回错误信息。
        if (window.location.href.includes("usdtIndex")) {
          if (response.data.content === '') return;
        }
        _errType(response.data.msg);
        return;
      }
    } else {
      // 判断加载组件存在时间多少消失
      if (paramsAll.loadTime) {
        setTimeout(() => {
          Toast.close();
        }, paramsAll.loadTime);
      } else {
        if (paramsAll.load) Toast.close();
      }
    }
    return response;
  },
  (error) => {
    if (paramsAll.load) Toast.close();
    // 拿到 error.response
    const { response } = error;
    if (response) {
      // 请求已发出，但是报错了
      errControl(error);
      // 测试结束要把这个关了
      // return Promise.reject(response)
    } else {
      Notification.error({
        title: "error",
        message: i18n.messages[i18n.locale].requestTimeout,
      });
      // 处理断网的情况
      // eg:请求超时或断网时，更新 state 的 network 状态
      // network 状态在 app.vue 中控制着一个全局的断网提示组件的显示隐藏
      if (!window.navigator.onLine) {
        console.log("断网");
      } else {
        return Promise.reject(error);
      }
    }
  }
);

/**
 * axiosPack：封装的 axios 请求
 * @param {String} url [请求地址get时参数跟随地址一起传入]
 * @param {Object} params [参数]
 * @param {Object} paramsObj.load[0] [加载动画显示文本]
 * @param {Number} paramsObj.load[1] || paramsAll.loadTime [加载动画执行时间]
 * @param {Object} paramsAll.load [是否添加加载动画]
 * @param {Number} paramsAll.errType [报错显示形式：1-console，2-alert，默认-toast]
 */
// 参数处理
function paramsDeal (paramsObj) {
  // 改变提交格式
  let params = ""; // 参数

  if (paramsObj) {
    if (paramsObj.params) params = paramsObj.params;
    // 加载动画存在显示
    if (paramsObj.load) {
      paramsAll.load = true;
      paramsAll.loadTime = paramsObj.load[1]; // 加载动画执行时间
      tip(paramsObj.load[2]); // 加载动画提示文字
    }
    // 接口报错显示类型
    if (paramsObj.errType) paramsAll.errType = paramsObj.errType;
  }

  return params;
}

export default {
  post (url, paramsObj) {
    returnAll = false;
    url = yhcDomain + url
    let params = paramsDeal(paramsObj);
    let data = instance
      .post(url, params)
      .then((res) => {
        return res;
      })
      .catch((err) => {
        console.log("报错接口", err);
        return err;
      });

    return Promise.resolve(data);
  },
  get (url) {
    returnAll = false;
    url = yhcDomain + url
    let data = instance
      .get(url)
      .then((res) => {
        return res;
      })
      .catch((err) => {
        console.log("报错接口", err);
        return err;
      });
    return Promise.resolve(data);
  },
};
