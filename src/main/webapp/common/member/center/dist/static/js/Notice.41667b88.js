(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["Notice"],{"121c":function(e,t,a){"use strict";var n=a("72f4"),o=a.n(n);o.a},"3b92":function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("el-card",{staticClass:"notice-page"},[a("el-collapse",{attrs:{accordion:""},model:{value:e.activeName,callback:function(t){e.activeName=t},expression:"activeName"}},e._l(e.data,(function(t,n){return a("el-collapse-item",{key:n,attrs:{title:t.title+"【"+e.dateChange(t.updateTime)+"】",name:n}},[a("div",{domProps:{innerHTML:e._s(t.content)}})])})),1),a("PageModule",{attrs:{total:e.total,pageNumber:e.pageNumber},on:{"update:pageNumber":function(t){e.pageNumber=t},"update:page-number":function(t){e.pageNumber=t},searchMethod:e.searchMethod}})],1)},o=[],r=a("9321"),c={data:function(){return{activeName:"",data:[],total:0,pageNumber:1}},computed:{},components:{PageModule:r["a"]},mounted:function(){this.searchMethod()},methods:{searchMethod:function(){var e=this,t=new URLSearchParams;t.append("pageNumber",this.pageNumber),this.$axiosPack.post("/userCenter/msgManage/articleList.do",{params:t,load:[!0,200,null]}).then((function(t){t&&(e.data=t.data.rows,e.total=t.data.total)}))},dateChange:function(e){return this.$dataJS.dateChange(e)}},watch:{}},u=c,i=(a("121c"),a("2877")),s=Object(i["a"])(u,n,o,!1,null,null,null);t["default"]=s.exports},"72f4":function(e,t,a){},9321:function(e,t,a){"use strict";var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"pageModule"},[a("el-pagination",{attrs:{"current-page":e.pageNumber,layout:"total, prev, pager, next, jumper",total:e.total,"hide-on-single-page":!0},on:{"current-change":e.handleCurrentChange}})],1)},o=[],r=(a("a28e"),{data:function(){return{}},props:{total:"",pageNumber:""},computed:{},components:{},created:function(){},methods:{handleCurrentChange:function(e){console.log("当前页: ".concat(e)),this.$emit("update:pageNumber",e),this.$emit("searchMethod")}},watch:{}}),c=r,u=(a("d3e0"),a("2877")),i=Object(u["a"])(c,n,o,!1,null,null,null);t["a"]=i.exports},d3e0:function(e,t,a){"use strict";var n=a("ebf9"),o=a.n(n);o.a},ebf9:function(e,t,a){}}]);