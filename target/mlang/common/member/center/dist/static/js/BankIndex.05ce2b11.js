(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["BankIndex"],{"24a0":function(t,a,e){},"812d":function(t,a,e){"use strict";e.r(a);var s=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"moneyChangeHis-page"},[t.data.canAddBank?e("el-button",{attrs:{type:"primary",size:"medium"},on:{click:t.addBank}},[t._v(t._s(t.$t("addText")))]):t._e(),e("div",{staticClass:"list"},[e("el-table",{staticStyle:{width:"99%"},attrs:{data:t.data.banks}},[e("el-table-column",{attrs:{prop:"realName",label:t.configJsFlag("bankRealName")}}),e("div"),e("el-table-column",{attrs:{prop:"bankName",label:t.$t("bankName")}}),e("el-table-column",{attrs:{prop:"cardNo",label:t.configJsFlag("bankCardNo")}}),e("el-table-column",{attrs:{label:t.$t("remarkText")},scopedSlots:t._u([{key:"default",fn:function(a){return[t._v(" "+t._s(a.row.remarks||"--")+" ")]}}])}),e("el-table-column",{attrs:{label:t.$t("bankTime")},scopedSlots:t._u([{key:"default",fn:function(a){return[t._v(" "+t._s(t.dataFun(a.row.createTime))+" ")]}}])}),e("el-table-column",{attrs:{label:t.$t("status")},scopedSlots:t._u([{key:"default",fn:function(a){return[t._v(" "+t._s(2===a.row.status?t.$t("open"):t.$t("forbidden"))+" ")]}}])})],1)],1),e("el-dialog",{staticClass:"add-dialog",attrs:{title:t.$t("addBank"),visible:t.dialogShow,width:"40%","close-on-click-modal":!1},on:{"update:visible":function(a){t.dialogShow=a}}},[e("div",{staticClass:"cont"},[t.data.banks&&t.data.banks.length?e("el-card",{staticStyle:{"margin-bottom":"10px"},attrs:{shadow:"always"}},[e("p",{staticClass:"hint"},[t._v(t._s(t.$t("bankHint")))]),e("div",{staticClass:"item"},[e("span",{staticClass:"title"},[t._v(" "+t._s(t.configJsFlag("bankRealName"))+" ： ")]),e("el-input",{attrs:{placeholder:t.$t("lastBankName")},model:{value:t.lastRealName,callback:function(a){t.lastRealName=a},expression:"lastRealName"}})],1),e("div",{staticClass:"item",staticStyle:{"margin-bottom":"0"}},[e("span",{staticClass:"title"},[t._v(t._s(t.configJsFlag("bankCardNo"))+"：")]),e("el-input",{attrs:{placeholder:t.configJsFlag("bankCardNo")},model:{value:t.lastCardNo,callback:function(a){t.lastCardNo=a},expression:"lastCardNo"}})],1)]):t._e(),e("el-card",{staticClass:"change-box",attrs:{shadow:"always"}},["PIX"===t.bankCode.split("-")[0]?e("div",{staticClass:"pixTip",staticStyle:{"margin-bottom":"10px"}},[e("p",{staticClass:"hint"},[t._v(" "+t._s(t.configJsFlag("pixTip"))+" ")]),e("p",{staticClass:"hint"},[t._v(" "+t._s(t.configJsFlag("pixTip1"))+" ")]),e("p",{staticClass:"hint"},[t._v(" "+t._s(t.configJsFlag("pixTip2"))+" ")]),e("p",{staticClass:"hint"},[t._v(" "+t._s(t.configJsFlag("pixTip3"))+" ")]),e("p",{staticClass:"hint"},[t._v(" "+t._s(t.configJsFlag("pixTip4"))+" ")])]):t._e(),"bx101"!=this.onOffBtn.stationCode?e("div",{staticClass:"item"},[e("span",{staticClass:"title"},[t._v(t._s(t.$t("lastBank"))+"：")]),e("el-select",{attrs:{placeholder:t.$t("pleaseBank")},on:{change:t.selectType},model:{value:t.bankCode,callback:function(a){t.bankCode=a},expression:"bankCode"}},t._l(t.data.bankList,(function(t){return e("el-option",{key:t.code,attrs:{label:t.name,value:t.code+"-"+t.name}})})),1)],1):t._e(),t.showBankName&&"bx101"!=this.onOffBtn.stationCode?e("div",{staticClass:"item"},[e("span",{staticClass:"title"},[t._v(t._s(t.$t("lastBank"))+"：")]),e("el-input",{attrs:{placeholder:t.$t("inputBankName")},model:{value:t.bankName,callback:function(a){t.bankName=a},expression:"bankName"}})],1):t._e(),e("div",{staticClass:"item"},[e("span",{staticClass:"title"},[t._v(t._s(t.configJsFlag("bankRealName"))+"：")]),e("el-input",{attrs:{placeholder:t.$t("putBankRealName")},model:{value:t.realName,callback:function(a){t.realName=a},expression:"realName"}})],1),e("div",{staticClass:"item"},[e("span",{staticClass:"title"},[t._v(t._s(t.configJsFlag("bankCardNo"))+"：")]),e("el-input",{attrs:{placeholder:t.$t("putHint")},model:{value:t.cardNo,callback:function(a){t.cardNo=a},expression:"cardNo"}})],1),e("div",{staticClass:"item",style:"INR"===t.data.currency?"":"margin-bottom: 0"},[e("span",{staticClass:"title"},[t._v(t._s(t.configJsFlag("sureBank"))+"：")]),e("el-input",{attrs:{placeholder:t.configJsFlag("rePutBankNo")},model:{value:t.reCardNo,callback:function(a){t.reCardNo=a},expression:"reCardNo"}})],1),t.showPhoneNumerAdd55?e("div",{staticClass:"item",staticStyle:{"margin-top":"10px"}},[e("span",{staticClass:"title"},[e("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"red"}})]),e("span",{staticStyle:{color:"red"}},[t._v(t._s(t.$t("phoneNumerAdd55")))])]):t._e(),"INR"===t.data.currency?e("div",{staticClass:"item",staticStyle:{"margin-bottom":"0"}},[e("span",{staticClass:"title"},[t._v("IFSC：")]),e("el-input",{attrs:{placeholder:t.$t("putHint")},model:{value:t.bankAddress,callback:function(a){t.bankAddress=a},expression:"bankAddress"}})],1):t._e()])],1),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{on:{click:function(a){t.dialogShow=!1}}},[t._v(t._s(t.$t("cancel")))]),e("el-button",{attrs:{type:"primary"},on:{click:t.sureBtn}},[t._v(t._s(t.$t("confirm")))])],1)])],1)},n=[],i=e("2f62");function o(t,a){var e=Object.keys(t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(t);a&&(s=s.filter((function(a){return Object.getOwnPropertyDescriptor(t,a).enumerable}))),e.push.apply(e,s)}return e}function l(t){for(var a=1;a<arguments.length;a++){var e=null!=arguments[a]?arguments[a]:{};a%2?o(Object(e),!0).forEach((function(a){r(t,a,e[a])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(e)):o(Object(e)).forEach((function(a){Object.defineProperty(t,a,Object.getOwnPropertyDescriptor(e,a))}))}return t}function r(t,a,e){return a in t?Object.defineProperty(t,a,{value:e,enumerable:!0,configurable:!0,writable:!0}):t[a]=e,t}var c={data:function(){return{data:"",tableData:[],dialogShow:!1,bankList:[],bankCode:"",lastRealName:"",lastCardNo:"",realName:"",cardNo:"",reCardNo:"",bankAddress:"",bankName:"",showBankName:!1,pixTipFlag:!1}},computed:l(l({},Object(i["b"])(["onOffBtn"])),{},{configJs:function(){return this.$store.state.configJs},showPhoneNumerAdd55:function(){if(this.onOffBtn){var t=this.onOffBtn.stationCode.slice(0,2);if("bx"===t)return!0}}}),components:{},mounted:function(){this.list()},methods:{list:function(){var t=this;this.$axiosPack.post("/userCenter/userBank/list.do?type=bank").then((function(a){if(a){t.data=a.data;var e=a.data.bankList.filter((function(t){return"USDT"!=t.name}));t.data.bankList=e}}))},addBank:function(){var t=this;this.data.needPerfectContact?this.$prompt(this.$t("bankOtherHint"),this.$t("phone"),{confirmButtonText:this.$t("confirm"),cancelButtonText:this.$t("cancel")}).then((function(a){var e=a.value;t.setPhone(e)})).catch((function(){})):this.dialogShow=!0},setPhone:function(t){var a=this,e=new URLSearchParams;e.append("type","phone"),e.append("newContact",t),this.$axiosPack.post("/userCenter/updateSecurityInfo.do",{params:e}).then((function(t){t&&(a.$successFun(t.data.msg),a.list())}))},sureBtn:function(){var t=this,a=this.bankCode.split("-"),e=new URLSearchParams;this.reCardNo==this.cardNo?("bx101"===this.onOffBtn.stationCode?(e.append("bankCode","PIX"),e.append("bankName","PIX")):(e.append("bankCode",a[0]),this.showBankName?e.append("bankName",this.bankName):e.append("bankName",a[1])),e.append("realName",this.realName),e.append("cardNo",this.cardNo),"INR"===this.data.currency&&e.append("bankAddress",this.bankAddress),this.lastRealName&&e.append("lastRealName",this.lastRealName),this.lastCardNo&&e.append("lastCardNo",this.lastCardNo),this.$axiosPack.post("/userCenter/userBank/bankAddSave.do",{params:e}).then((function(a){a&&(t.dialogShow=!1,t.$successFun(a.data.msg),t.list())}))):this.$message.error(this.$t("cardNumDifferent"))},clearFun:function(){this.bankCode="",this.lastRealName="",this.lastCardNo="",this.realName="",this.cardNo="",this.reCardNo="",this.bankAddress=""},dataFun:function(t){return this.$dataJS.dateChange(t)},selectType:function(){}},watch:{dialogShow:function(){this.dialogShow||this.clearFun()},bankCode:function(){var t=this.bankCode.split("-");"other"===t[0]?this.showBankName=!0:(this.showBankName=!1,this.bankName="")}}},d=c,p=(e("8a8a"),e("2877")),u=Object(p["a"])(d,s,n,!1,null,null,null);a["default"]=u.exports},"8a8a":function(t,a,e){"use strict";var s=e("24a0"),n=e.n(s);n.a}}]);