# center

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


# 未处理细节
暂无数据


# 开发常用
if (!startTime) startTime = this.$dataJS.today() + ' 00:00:00'
if (!endTime) endTime = this.$dataJS.today() + ' 23:59:59'

{ params: params, load: [true, 200, null] }

this.$message.success(res.data.msg);

this.toastFun(res.data.msg);

iconfont

this.$notify({
  title: 'success',
  message: res.data.msg,
  type: 'success'
});

this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
  confirmButtonText: '确定',
  cancelButtonText: '取消',
  type: 'warning'
}).then(() => {
  this.$message({
    type: 'success',
    message: '删除成功!'
  });
}).catch(() => {         
});

let _this = this
var timer = setInterval(() => {
  if (_this.onOffBtn) {
    clearInterval(timer);
    _this.getGameList();
  }
}, 300);
this.$once('hook:beforeDestroy', function () {
  clearInterval(timer);
});

// 积分兑换记录
searchMethod (startTime, endTime, isSearch) {
  if (startTime) this.startTime = startTime
  if (endTime) this.endTime = endTime
  if (isSearch) this.pageNumber = 1

  //改变提交格式
  let params = new URLSearchParams();
  params.append('startTime', startTime);
  params.append('endTime', endTime);
  params.append('pageNumber', this.pageNumber);
  params.append('type', this.type);
  this.$axiosPack.post("/userCenter/scoreHisData.do", { params: params, load: [true, 200, null] }).then(res => {
    if (res) {
      this.tableData = res.data.rows
      this.total = res.data.total
    }
  });
},

623668111111111111

$t('word.adviceType')
:label="$t('word.adviceType')"
{{ $t("word.complain") }}
this.$t("word.allStatus")


团队报表排序没做
标题栏英文


这两个删掉
againPay: "Isi ulang 10lagi untuk naik level",
againPay: "Isi ulang 10lagi untuk naik level",

setSportRebateAmount: '设置体育返点',
setChessRebateAmount: '设置棋牌返点',

这两个删掉
canNotSame: '原QQ与新QQ不能一样',
twoPutInconformity: '两次输入QQ不一致',

bankStatus 去掉
bankName 第二个去掉

depositRange: '至',
payText 152行 换成 depositText

activeAwardAmount 235行 换成 activeAward

rebateAmount: '反水金额',
rebateAmount: '投注反水',  211 行 betRebate
rebateAmount: '返点率', 269 行 rebateRate
冲突了

message: 63 行的删掉

times 前面有空格
everyBi  去掉数字 前面有空格
levelShow 去掉数字 前面有空格
inputOld
inputNew
inputReNew 去掉后面的

applyMoney: 196行 换成 serverMoney


后面 3 要改的

drawNum  445行 drawNumLeft

againPayleft 去掉数字



站内信打开有问题
启用未读站内信提醒
弹窗公告












