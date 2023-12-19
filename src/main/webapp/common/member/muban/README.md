# pcelm

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

# axios api、vuex、config

# 模块加载失败重载、路由改变页面不更新强制更新、接口请求失败重连

# 页面切换动画、皮肤切换、头部

### 常用
``` bash
# vuex
import { mapState } from "vuex";
computed: {
  ...mapState(["theme", "onOffBtn"]),
  ...mapState({
    isLogin: state => state.userInfo.login
  })
},

# 赋值：
this.$store.dispatch("getUserInfo", data);

# 消息弹窗：
this.$message.success('复制成功');

# 复制：
this.$publicJs.copyFun(id名)

# 多语言：
{{ $t('register') }}

# 动画时间：
transition: all .3s ease;

# 宽高比：
height: 0;
width: 100%;
padding-bottom: 100%;

# 接口
# 接口外的代码需要在接口请求之后执行用 async/await 方式请求
async activeList () {
  const res = await this.$API.activeData()
  if (res) this.data = res;
},

# 如果需要获取错误信息 catch 内容，needCatch 参数设为 true
async activeList () {
  const res = await this.$API.activeData({ needCatch: true })
    .catch(err => {
      console.log('报错了', err)
    })
  if (res) this.data = res;
},

>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

this.$API.activeData().then((res) => {
  if (res) {
    this.data = res;
  }
})

# 如果需要获取错误信息 catch 内容，needCatch 参数设为 true
this.$API.activeData({ needCatch: true }).then((res) => {
  if (res) {
    this.data = res;
  }
}).catch(err => {
  console.log('报错了', err)
});



```

