"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.i18n = void 0;

var _vue = _interopRequireDefault(require("vue"));

var _vueI18n = _interopRequireDefault(require("vue-i18n"));

var _CN = _interopRequireDefault(require("../assets/lang/CN.js"));

var _EN = _interopRequireDefault(require("../assets/lang/EN.js"));

var _VN = _interopRequireDefault(require("../assets/lang/VN.js"));

var _IND = _interopRequireDefault(require("../assets/lang/IND.js"));

var _IDN = _interopRequireDefault(require("../assets/lang/IDN.js"));

var _MYR = _interopRequireDefault(require("../assets/lang/MYR.js"));

var _TH = _interopRequireDefault(require("../assets/lang/TH.js"));

var _JA = _interopRequireDefault(require("../assets/lang/JA.js"));

var _BR = _interopRequireDefault(require("../assets/lang/BR.js"));

var _ES = _interopRequireDefault(require("../assets/lang/ES.js"));



function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { "default": obj }; }

// import Element from 'element-ui'
_vue["default"].use(_vueI18n["default"]);

var i18n = new _vueI18n["default"]({
  locale: 'en',
  // 默认语言 英文
  messages: {
    'cn': _CN["default"],
    //简体中文
    'en': _EN["default"],
    //英文
    'vi': _VN["default"],
    //越南(越南文)
    'in': _IND["default"],
    //印度(印地文)
    'id': _IDN["default"],
    //印尼(印尼文)
    'ms': _MYR["default"],
    //马来(马来文)
    'th': _TH["default"],
    //泰国(泰文)
    'br': _BR["default"],
    //巴西(葡萄牙文) 
    'ja': _JA["default"], 
    //日本(日文)
    'es': _ES["default"],
    //墨西哥(西班牙文)
  }
});
exports.i18n = i18n;