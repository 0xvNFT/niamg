import axios from 'axios';
import QS from 'qs'; // 引入qs模块，用来序列化post类型的数据
import { Loading, Notification, MessageBox } from 'element-ui';
import { i18n } from "@/lang/i18n";

var Toast = ''

const axiosInstance = axios.create({
  // baseURL: import.meta.env.VITE_SCREEN_BASE_URL,// 设置请求地址
  headers: { 'X-Requested-With': 'XMLHttpRequest' },//设置post请求头
  withCredentials: true,//是否携带cookie信息
  timeout: 10000,//超时时间
  retry: 2, //全局重试请求次数
  retryDelay: 1500, //全局请求间隔
});

// axios 请求拦截
axiosInstance.interceptors.request.use(
  config => {
    if (i18n.locale === 'vi') {
      // 去除包含千位分隔符的越南盾
      let data = new URLSearchParams(config.data);
      let arrFrom = Array.from(data.keys());  //获取请求的参数
      const arr = ['amount', 'money', 'minBalance', 'maxBalance', 'depositTotal']; //带金额的参数

      arr.forEach((each) => {
        if (arrFrom.includes(each)) {
          data.set(each, data.get(each).replaceAll(',', ''));
        }
      })
      config.data = String(data)
    }

    return config;
  },
  error => {
    return Promise.error(error);
  }
)
// axios 响应拦截
axiosInstance.interceptors.response.use(
  response => {
    if (response.data.success === false && response.data.msg) {
      Notification.error({
        title: 'error',
        message: response.data.msg
      });
    }
    return response;
  },
  error => {
    // 请求失败，重连
    return reconnection(error)
  }
)

// 请求失败，重连
let reconnection = (error) => {
  //失败处理 error.config是一个对象，包含上方create中设置的三个参数
  let config = error.config;
  // 如果接口配置信息获取不到,直接返回报错内容
  if (!config || !config.retry) return Promise.reject(error);

  // __retryCount用来记录当前是第几次发送请求
  config._retryCount = config._retryCount || 0;

  // 如果当前发送的请求大于等于设置好的请求次数时，不再发送请求，返回最终的错误信息
  if (config._retryCount >= config.retry) {
    let { message, response = {} } = error
    let code = response.status
    let msg = ''

    //除以上两种以外的所有错误，包括接口报错 400 500 之类的
    switch (code) {
      case 503:
        msg = i18n.t('503');
        break
      case 500:
        msg = i18n.t('500');
        break
      case 404:
        msg = i18n.t('404');
        break
      case 403:
        msg = i18n.t('403');
        break
      case 400:
        msg = i18n.t('400');
        break
      default:
        msg = message || code + ' error'
    }

    Notification.error({
      title: 'error',
      message: msg
    });

    // 返回报错信息
    // return Promise.reject(error);
    return Promise.reject({ type: 'error', msg: msg, url: config.url });
  }

  // 记录请求次数+1
  config._retryCount += 1;

  // 设置请求间隔 在发送下一次请求之前停留一段时间，时间为上方设置好的请求间隔时间
  let reload = new Promise((resolve) => {
    let timer = null;
    // 清除定时器
    clearTimeout(timer);
    timer = setTimeout(() => {
      resolve();
    }, config.retryDelay || 1000);
  });

  // 再次发送请求,拆分为 .then 执行重复接口操作是为了明确让 reload 中 Promise 的代码先执行
  return reload.then(() => {
    return axiosInstance(config);
  });
}

/**
 * 其他参数处理
 * @param {Object} load 是否显示加载中 默认false
 */
let otherParam = (params) => {
  // console.log(`output->params`, params)
  let { load = false, loadMsg = '' } = params
  if (load) {
    if (Toast) Toast.close()

    Toast = Loading.service({
      text: loadMsg || '',
      background: 'rgba(0, 0, 0, .3)',
    });
  }
}

/** 
 * @param {String} url [请求的url地址] 
 * @param {Object} params [请求时携带的参数] 
 * @param {String} type [请求类型] 
 * @param {Object} resolve [请求成功返回] 
 * @param {Object} reject [请求失败返回] 
 */
function axiosJs (url, params, type, resolve, reject) {
  if (type === 'get') {
    params = { params: Object.assign({}, params) }
  } else {
    params = QS.stringify(params)
  }

  axiosInstance[type](url, params)
    .then(res => {
      if (Toast) Toast.close()
      if (res) resolve(res.data);
    })
    .catch(err => {
      if (Toast) Toast.close()
      reject(err);
    });
}

/** 
 * @param {String} url [请求的url地址] 
 * @param {Object} paramsAll [所有的参数] 
 * @param {Object} params [请求时携带的参数] 
 * @param {Object} needCatch [请求失败是否需要在页面进行catch] 
 * @param {String} type [请求类型] 
 */
function httpJS (url, paramsAll = {}, type = 'post') {
  otherParam(paramsAll)

  let { needCatch } = paramsAll

  if (needCatch) {
    return new Promise((resolve, reject) => {
      axiosJs(url, paramsAll, type, resolve, reject)
    })
  } else {
    return new Promise((resolve, reject) => {
      axiosJs(url, paramsAll, type, resolve, reject)
    }).catch(err => {
      console.log(`output->err`, err)
    });
  }
}

export default httpJS
