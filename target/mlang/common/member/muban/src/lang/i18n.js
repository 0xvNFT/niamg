import Vue from "vue";
import VueI18n from "vue-i18n";
import ElementLocale from 'element-ui/lib/locale'

import CN from "./lang/CN.js";
import EN from "./lang/EN.js";
import BR from "./lang/BR.js";
import ES from "./lang/ES.js";
import IND from "./lang/IND.js";
import JA from "./lang/JA.js";
import MYR from "./lang/MYR.js";
import TH from "./lang/TH.js";
import VN from "./lang/VN.js";
import IDN from "./lang/IDN.js";

Vue.use(VueI18n);

const i18n1 = new VueI18n({
  locale: "cn", // 默认语言 中文
  messages: {
    cn: CN, //简体中文
    en: EN, //英文
    br: BR, //巴西(葡萄牙文)
    es: ES, //西班牙(西班牙文)
    in: IND,//印度(印度文)
    ja: JA, //日本(日文)
    // myr: MYR, //马来(马来文)
    ms: MYR, //马来(马来文)
    th: TH,  //泰国(泰文)
    vi: VN,  //越南(越南文)
    // vn: VN,  //越南(越南文)
    id: IDN,  //印尼(印尼文)
  },
});


ElementLocale.i18n((key, value) => i18n1.t(key, value))

export const i18n = i18n1