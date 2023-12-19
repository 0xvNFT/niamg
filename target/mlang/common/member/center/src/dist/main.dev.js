"use strict";

require("babel-polyfill");

var _vue = _interopRequireDefault(require("vue"));

var _App = _interopRequireDefault(require("./App.vue"));

var _router = _interopRequireDefault(require("./router"));

var _store = _interopRequireDefault(require("./store"));

var _bus = _interopRequireDefault(require("./bus"));

var _http = _interopRequireDefault(require("./http"));

require("url-search-params-polyfill");

var _elementUi = require("element-ui");

var _i18n = require("./lang/i18n");

require("./plugins");

require("./plugins/page");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

_vue["default"].prototype.$bus = _bus["default"];
_vue["default"].prototype.$axiosPack = _http["default"]; // Vue.$t("sureGoIndex")
//路由全局守卫--进入前

_router["default"].beforeEach(function (to, from, next) {
  if (!_store["default"].state.userInfo || _store["default"].state.userInfo.login) {
    next();
  } else {
    console.log(3333);

    _elementUi.MessageBox.alert(_i18n.i18n.$t("message.sureGoIndex"), response.data.msg || _i18n.i18n.$t("message.noLoginRe"), {
      confirmButtonText: _i18n.i18n.$t("message.confirm"),
      callback: function callback(action) {
        // location.href = '/index.do'
        top.location.href = '/index.do';
      }
    });
  }
});

var loadFailedNum = 0; //模块加载失败

_router["default"].onError(function (error) {
  if (loadFailedNum < 10) {
    loadFailedNum++;
    console.log('' + error);
    var pattern = /Loading chunk (\d)+ failed/g;
    var isChunkLoadFailed = error.message.match(pattern);
    var targetPath = _router["default"].history.pending.fullPath;

    if (isChunkLoadFailed) {
      _router["default"].replace(targetPath);
    }
  }
});

_vue["default"].config.productionTip = false;
new _vue["default"]({
  router: _router["default"],
  store: _store["default"],
  i18n: _i18n.i18n,
  // i18n
  render: function render(h) {
    return h(_App["default"]);
  }
}).$mount('#app');