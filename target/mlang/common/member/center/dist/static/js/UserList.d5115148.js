(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["UserList"],{3109:function(t,e,a){"use strict";var s=a("9944"),l=a.n(s);l.a},9321:function(t,e,a){"use strict";var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"pageModule"},[a("el-pagination",{attrs:{"current-page":t.pageNumber,layout:"total, prev, pager, next, jumper",total:t.total,"hide-on-single-page":!0},on:{"current-change":t.handleCurrentChange}})],1)},l=[],i=(a("a28e"),{data:function(){return{}},props:{total:"",pageNumber:""},computed:{},components:{},created:function(){},methods:{handleCurrentChange:function(t){console.log("当前页: ".concat(t)),this.$emit("update:pageNumber",t),this.$emit("searchMethod")}},watch:{}}),n=i,o=(a("d3e0"),a("2877")),r=Object(o["a"])(n,s,l,!1,null,null,null);e["a"]=r.exports},9944:function(t,e,a){},c5cc:function(t,e,a){},cb44:function(t,e,a){"use strict";var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"dateModule"},[a("el-date-picker",{attrs:{type:"datetimerange",size:"small","start-placeholder":t.$t("startTime"),"end-placeholder":t.$t("endTime"),"default-time":["00:00:00","23:59:59"],editable:!1,clearable:!1,"popper-class":t.showClear?"showClear":""},model:{value:t.dataVal,callback:function(e){t.dataVal=e},expression:"dataVal"}}),t.fastBtn?a("el-button-group",{staticStyle:{"margin-left":"10px"}},[a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.yesterday}},[t._v(t._s(t.$t("yesterday")))]),a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.today}},[t._v(t._s(t.$t("today")))]),a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.lastWeek}},[t._v(t._s(t.$t("lastWeek")))]),a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.nowWeek}},[t._v(t._s(t.$t("nowWeek")))]),a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.lastMonth}},[t._v(t._s(t.$t("lastMonth")))]),a("el-button",{attrs:{type:"success",size:"small"},on:{click:t.nowMonth}},[t._v(t._s(t.$t("nowMonth")))])],1):t._e(),a("el-button",{staticStyle:{"margin-left":"30px"},attrs:{type:"primary",size:"small",icon:"el-icon-search"},on:{click:t.search}},[t._v(t._s(t.$t("searchText")))])],1)},l=[],i={data:function(){return{dataVal:"/userList"==this.$route.path?"":[this.$dataJS.today()+" 00:00:00",this.$dataJS.today()+" 23:59:59"]}},props:{fastBtn:"",showClear:!1},computed:{},components:{},created:function(){console.log()},methods:{yesterday:function(){this.dataVal=[this.$dataJS.yesterdayStart()+" 00:00:00",this.$dataJS.yesterdayStart()+" 23:59:59"],this.search()},today:function(){this.dataVal=[this.$dataJS.today()+" 00:00:00",this.$dataJS.today()+" 23:59:59"],this.search()},lastWeek:function(){this.dataVal=[this.$dataJS.lastWeekStart()+" 00:00:00",this.$dataJS.lastWeekEnd()+" 23:59:59"],this.search()},nowWeek:function(){this.dataVal=[this.$dataJS.nowWeekStart()+" 00:00:00",this.$dataJS.nowWeekEnd()+" 23:59:59"],this.search()},lastMonth:function(){this.dataVal=[this.$dataJS.lastMonthStart()+" 00:00:00",this.$dataJS.lastMonthEnd()+" 23:59:59"],this.search()},nowMonth:function(){this.dataVal=[this.$dataJS.nowMonthStart()+" 00:00:00",this.$dataJS.nowMonthEnd()+" 23:59:59"],this.search()},search:function(){var t=this.dataVal[0],e=this.dataVal[1];t instanceof Date&&(t=this.$dataJS.dateChangeStr(this.dataVal[0]),e=this.$dataJS.dateChangeStr(this.dataVal[1])),this.dataVal?this.$emit("searchMethod",t,e,!0):this.$emit("searchMethod","","",!0)}},watch:{dataVal:function(){}}},n=i,o=(a("3109"),a("2877")),r=Object(o["a"])(n,s,l,!1,null,null,null);e["a"]=r.exports},d3e0:function(t,e,a){"use strict";var s=a("ebf9"),l=a.n(s);l.a},ebf9:function(t,e,a){},f54f:function(t,e,a){"use strict";var s=a("c5cc"),l=a.n(s);l.a},fa59:function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"userList-page"},[a("div",{staticClass:"searchDiv"},[t.onOffBtn.canBePromo?a("div",{staticClass:"item"},[t._v(" "+t._s(t.$t("level"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.level,callback:function(e){t.level=e},expression:"level"}},t._l(t.levelArr,(function(t){return a("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})})),1)],1):t._e(),a("div",{staticClass:"item"},[t._v(" "+t._s(t.$t("userName"))+"： "),a("el-input",{staticClass:"outInput",attrs:{clearable:"",size:"small"},model:{value:t.username,callback:function(e){t.username=e},expression:"username"}})],1),a("div",{staticClass:"item"},[t._v(" "+t._s(t.$t("depositTotal"))+"： "),a("el-input",{staticClass:"outInput",attrs:{clearable:"",size:"small"},model:{value:t.depositTotal,callback:function(e){t.depositTotal=e},expression:"depositTotal"}})],1),a("div",{staticClass:"item"},[t._v(" "+t._s(t.$t("afterMoney"))+"： "),a("el-input",{staticClass:"outInput",attrs:{clearable:"",size:"small"},model:{value:t.minBalance,callback:function(e){t.minBalance=e},expression:"minBalance"}}),t._v(" "+t._s(t.$t("depositRange"))+" "),a("el-input",{staticClass:"outInput",attrs:{clearable:"",size:"small"},model:{value:t.maxBalance,callback:function(e){t.maxBalance=e},expression:"maxBalance"}})],1),t.onOffBtn.canBePromo||t.onOffBtn.canBeRecommend?a("div",{staticClass:"item"},[a("el-checkbox",{model:{value:t.checked,callback:function(e){t.checked=e},expression:"checked"}},[t._v(t._s(t.$t("includeJunior")))])],1):t._e(),a("DateModule",{attrs:{fastBtn:!0,showClear:!0},on:{searchMethod:t.searchMethod}})],1),a("el-table",{staticStyle:{width:"99%"},attrs:{data:t.tableData}},[a("el-table-column",{attrs:{prop:"username",label:t.$t("userName")}}),a("el-table-column",{attrs:{label:t.$t("degreeName")},scopedSlots:t._u([{key:"default",fn:function(e){return[a("p",[t._v(t._s(e.row.degreeName||"-"))])]}}])}),a("el-table-column",{attrs:{prop:"level",label:t.$t("userLevel")}}),a("el-table-column",{attrs:{label:t.$t("userType")},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(130===e.row.type?t.$t("member"):t.$t("proxy"))+" ")]}}])}),a("el-table-column",{attrs:{prop:"createDatetime",label:t.$t("registerTime")}}),a("el-table-column",{attrs:{prop:"lastLoginDatetime",label:t.$t("lastLoginDatetime")}}),a("el-table-column",{attrs:{label:t.$t("onlineStatus")},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(2===e.row.status?t.$t("open"):t.$t("forbidden"))+" ")]}}])}),a("el-table-column",{attrs:{prop:"money",label:t.$t("afterMoney")}}),a("el-table-column",{attrs:{label:t.$t("status")},scopedSlots:t._u([{key:"default",fn:function(e){return[t._v(" "+t._s(2===e.row.onlineStatus?t.$t("online"):t.$t("offline"))+" ")]}}])}),a("el-table-column",{attrs:{fixed:"right",label:t.$t("operate"),width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("router-link",{attrs:{to:"/viewTeam"}},[a("div",{staticClass:"operateLink"},[t._v(t._s(t.$t("viewTeam")))])]),t.userInfo.userId!==e.row.id&&t.onOffBtn.isProxy?a("div",{staticClass:"operateLink",on:{click:function(a){return t.handleClick(e.row,e.row.type)}}},[t._v(t._s(t.$t("setRebate")))]):t._e(),a("div",{staticClass:"operateLink",on:{click:t.goZB}},[t._v(t._s(t.$t("goRecordChange")))])]}}])})],1),a("PageModule",{attrs:{total:t.total,pageNumber:t.pageNumber},on:{"update:pageNumber":function(e){t.pageNumber=e},"update:page-number":function(e){t.pageNumber=e},searchMethod:t.searchMethod}}),a("el-dialog",{staticClass:"set-dialog",attrs:{title:t.$t("setRebate"),visible:t.dialogShow,width:"40%","close-on-click-modal":!1},on:{"update:visible":function(e){t.dialogShow=e}}},[a("div",{staticClass:"cont"},[a("p",[t._v(t._s(t.$t("userAccount"))+"："+t._s(t.dialogData.username))]),120===t.dialogData.type?[!t.fandianData.fixRebate&&t.fandianData.curRebate?[a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curLotRebate"))+"："+t._s(t.fandianData.curRebate.lottery)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("lotRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.lottery,callback:function(e){t.lottery=e},expression:"lottery"}},t._l(t.fandianData.lotteryArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curLiveRebate"))+"："+t._s(t.fandianData.curRebate.live)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("liveRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.live,callback:function(e){t.live=e},expression:"live"}},t._l(t.fandianData.liveArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curEgameRebate"))+"："+t._s(t.fandianData.curRebate.egame)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("egameRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.egame,callback:function(e){t.egame=e},expression:"egame"}},t._l(t.fandianData.egameArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.value+"‰",value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curSportRebate"))+"："+t._s(t.fandianData.curRebate.sport)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("sportRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.sport,callback:function(e){t.sport=e},expression:"sport"}},t._l(t.fandianData.sportArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.value+"‰",value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curChessRebate"))+"："+t._s(t.fandianData.curRebate.chess)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("chessRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.chess,callback:function(e){t.chess=e},expression:"chess"}},t._l(t.fandianData.chessArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.value+"‰",value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curEsportRebate"))+"："+t._s(t.fandianData.curRebate.esport)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("esportRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.esport,callback:function(e){t.esport=e},expression:"esport"}},t._l(t.fandianData.esportArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.value+"‰",value:t.value}})})),1)],1)]),a("div",{staticClass:"item"},[a("div",{staticClass:"user"},[t._v(" "+t._s(t.$t("curFishRebate"))+"："+t._s(t.fandianData.curRebate.fishing)+"‰ ")]),a("div",{staticClass:"other"},[t._v(" "+t._s(t.$t("fishRebate"))+"： "),a("el-select",{attrs:{size:"small"},model:{value:t.fishing,callback:function(e){t.fishing=e},expression:"fishing"}},t._l(t.fandianData.fishingArray,(function(t){return a("el-option",{key:t.value,attrs:{label:t.value+"‰",value:t.value}})})),1)],1)])]:[a("div",{staticClass:"item"},[t._v(t._s(t.$t("lotRebate"))+"："+t._s(t.lottery)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("liveRebate"))+"："+t._s(t.live)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("egameRebate"))+"："+t._s(t.egame)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("sportRebate"))+"："+t._s(t.sport)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("chessRebate"))+"："+t._s(t.chess)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("esportRebate"))+"："+t._s(t.esport)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("fishRebate"))+"："+t._s(t.fishing)+"‰")])]]:[a("div",{staticClass:"item"},[t._v(t._s(t.$t("lotRebate"))+"："+t._s(t.lottery)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("liveRebate"))+"："+t._s(t.live)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("egameRebate"))+"："+t._s(t.egame)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("sportRebate"))+"："+t._s(t.sport)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("chessRebate"))+"："+t._s(t.chess)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("esportRebate"))+"："+t._s(t.esport)+"‰")]),a("div",{staticClass:"item"},[t._v(t._s(t.$t("fishRebate"))+"："+t._s(t.fishing)+"‰")])]],2),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.dialogShow=!1}}},[t._v(t._s(t.$t("cancel")))]),a("el-button",{attrs:{type:"primary"},on:{click:t.sureBtn}},[t._v(t._s(t.$t("confirm")))])],1)])],1)},l=[],i=a("2f62"),n=a("cb44"),o=a("9321");function r(t,e){var a=Object.keys(t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(t);e&&(s=s.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),a.push.apply(a,s)}return a}function c(t){for(var e=1;e<arguments.length;e++){var a=null!=arguments[e]?arguments[e]:{};e%2?r(Object(a),!0).forEach((function(e){u(t,e,a[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(a)):r(Object(a)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(a,e))}))}return t}function u(t,e,a){return e in t?Object.defineProperty(t,e,{value:a,enumerable:!0,configurable:!0,writable:!0}):t[e]=a,t}var d={data:function(){return{level:"",levelArr:[],username:"",minBalance:"",maxBalance:"",depositTotal:"",checked:!0,tableData:[],total:0,pageNumber:1,dialogShow:!1,dialogData:"",lottery:"",live:"",egame:"",sport:"",chess:"",esport:"",fishing:"",fandianData:"",startTime:"",endTime:""}},computed:c({},Object(i["b"])(["userInfo","onOffBtn"])),components:{DateModule:n["a"],PageModule:o["a"]},created:function(){this.searchMethod();var t=this,e=setInterval((function(){t.onOffBtn&&(clearInterval(e),t.onOffBtn.isProxy&&t.levelInfo())}),300);this.$once("hook:beforeDestroy",(function(){clearInterval(e)}))},methods:{levelInfo:function(){var t=this;this.$axiosPack.post("/userCenter/agentManage/levelInfo.do").then((function(e){if(e){t.levelArr=[{value:"",label:t.$t("allLevel")}];for(var a=1;a<=e.data.lowestLevel;a++)t.levelArr.push({value:a,label:a+t.$t("levelShow")})}}))},searchMethod:function(t,e,a){var s=this;a&&(this.startTime=t,this.endTime=e,this.pageNumber=1);var l=new URLSearchParams;l.append("startTime",this.startTime),l.append("endTime",this.endTime),l.append("pageNumber",this.pageNumber),l.append("username",this.username),l.append("minBalance",this.minBalance),l.append("maxBalance",this.maxBalance),l.append("depositTotal",this.depositTotal),l.append("level",this.level),l.append("include",this.checked),this.$axiosPack.post("/userCenter/agentManage/userListData.do",{params:l,load:[!0,200,null]}).then((function(t){t&&(s.tableData=t.data.rows,s.total=t.data.total)}))},handleClick:function(t,e){var a=this;if(this.dialogShow=!0,this.dialogData=t,120===e){var s=new URLSearchParams;s.append("userId",this.dialogData.id),this.$axiosPack.post("/userCenter/agentManage/kickbackResetInfo.do",{params:s,load:[!0,200,null]}).then((function(t){if(t){a.fandianData=t.data;var e=t.data.userRebate;a.lottery=e.lottery,a.live=e.live,a.egame=e.egame,a.sport=e.sport,a.chess=e.chess,a.esport=e.esport,a.fishing=e.fishing}}))}else{var l=new URLSearchParams;l.append("userId",this.dialogData.id),this.$axiosPack.post("/userCenter/agentManage/kickbackInfoForMember.do",{params:l,load:[!0,200,null]}).then((function(t){if(t){var e=t.data.rebate;a.lottery=e.lottery,a.live=e.live,a.egame=e.egame,a.sport=e.sport,a.chess=e.chess,a.esport=e.esport,a.fishing=e.fishing}}))}},sureBtn:function(){var t=this,e=new URLSearchParams;e.append("userId",this.dialogData.id),e.append("live",this.live),e.append("egame",this.egame),e.append("chess",this.chess),e.append("sport",this.sport),e.append("esport",this.esport),e.append("fishing",this.fishing),e.append("lot",this.lottery),this.$axiosPack.post("/userCenter/agentManage/kickbackResetSave.do",{params:e,load:[!0,200,null]}).then((function(e){e&&(t.dialogShow=!1,t.$notify({title:"success",message:e.data.msg,type:"success"}))}))},goZB:function(){this.$bus.$emit("openBigMenu","4"),this.$router.push("/recordChange")}},watch:{}},p=d,h=(a("f54f"),a("2877")),v=Object(h["a"])(p,s,l,!1,null,null,null);e["default"]=v.exports}}]);