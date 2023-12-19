import Vue from "vue";
// import Element from 'element-ui'
import VueI18n from "vue-i18n";
import CNLang from "../assets/lang/CN.js";
import ENLang from "../assets/lang/EN.js";
import VNLang from "../assets/lang/VN.js";
import INDLang from "../assets/lang/IND.js";
import IDNLang from "../assets/lang/IDN.js";
import MYRLang from "../assets/lang/MYR.js";
import THLang from "../assets/lang/TH.js";
import JALang from "../assets/lang/JA.js";
import BRLang from "../assets/lang/BR.js";
import ESlang from "../assets/lang/ES.js";

Vue.use(VueI18n);

export const i18n = new VueI18n({
  locale: "en", // 默认语言 英文
  messages: {
    cn: CNLang, //简体中文
    en: ENLang, //英文
    vi: VNLang, //越南(越南文)
    in: INDLang, //印度(印度文)
    id: IDNLang, //印尼(印尼文)
    ms: MYRLang, //马来(马来文)
    th: THLang, //泰国(泰文)
    br: BRLang, //巴西(葡萄牙文)
    ja: JALang, //日本(日文)
    es: ESlang, //西班牙(西班牙文)
  },
});
