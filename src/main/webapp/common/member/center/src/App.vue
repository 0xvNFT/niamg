<template>
  <div id="app" :class="isLongLang ? 'otherLang':''" v-if="reloadShow">
    <Header></Header>
    <div class="container">
      <LeftMenu></LeftMenu>
      <div class="ui-main">
        <div class="main-head">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item>{{ $t("userCenter") }}</el-breadcrumb-item>
            <el-breadcrumb-item>{{titleArr.type}}</el-breadcrumb-item>
            <el-breadcrumb-item v-if="titleArr.name">{{titleArr.name}}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="con_box">
          <router-view />
        </div>
      </div>
    </div>
    <Loading></Loading>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import Header from '@/components/common/Header'
import LeftMenu from '@/components/common/LeftMenu'
import Loading from '@/components/common/Loading'
import configJs from "@/assets/js/stationConfig";
export default {
  data () {
    return {
      titleArr: {},//当前栏目标题
      getUserInfo: null, //保存请求用户信息的定时器 用于清除定时器
      userInfo: "", //用户信息
      isLongLang: false,
      reloadShow: true,
      username: '',//用户名
      password: '',//密码
      verify: '',//验证码
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    Header,
    LeftMenu,
    Loading
  },
  created () {
    // 开关接口
    this.getConfig();
  },
  mounted () {
    // 检测浏览器路由改变页面不刷新问题,hash模式的工作原理是hashchange事件
    window.addEventListener("hashchange", () => {
      let currentPath = window.location.hash.slice(1);
      if (this.$route.path !== currentPath) {
        this.$router.push(currentPath);
      }
    }, false);
  },
  methods: {
    //进个人中心就调个接口转出所有余额
    getAutoTranout () {
      this.$axiosPack.post("/autoTranout.do").then(res => {
        if (res.data.success) {
          this.getInfo();//请求成功 刷新金额
        }
      });
    },
    // 用户信息定时器
    infoTimer () {
      //定时器存在清除用户信息请求定时器
      if (this.getUserInfo) {
        clearInterval(this.getUserInfo);
        this.getUserInfo = null;
      }
      this.getAutoTranout();
      // 重新开始定时
      // let _this = this;
      // this.getUserInfo = setInterval(function () {
      //   if (_this.$store.state.userInfo.login) {
      //     _this.getInfo();
      //   } else {
      //     clearInterval(_this.getUserInfo);
      //     _this.getUserInfo = null;
      //   }
      // }, 10000);
    },
    // 用户信息
    getInfo () {
      this.$axiosPack.post("/userCenter/userAllInfo.do").then(res => {
        if (res) {
          // 将信息存储到 store，并且存入 sessionStorage
          this.$store.dispatch("getUserInfo", res.data);
          sessionStorage.userInfo = JSON.stringify(res.data);

          // 未登录
          if (!res.data.login && !window.location.href.includes('login')) {
            this.$alert(this.$t("indexText") + '?', this.$t("noLogin"), {
              confirmButtonText: this.$t("confirm"),
              showClose: false,
              callback: action => {
                // location.href = '/index.do';
                top.location.href = '/index.do';
              }
            });
          }
        }
      });
    },
    // 开关接口
    getConfig () {
      this.$axiosPack.get("/userCenter/getConfig.do").then(res => {
        if (res) {
          // 用户信息定时器
          this.infoTimer();
          let data = res.data;
          let config = configJs.configStation(res.data.stationCode);
          this.$store.dispatch("getConfigJs", config);
          // res.data.lang='cn';
          this.$i18n.locale = res.data.lang;
          if (res.data.lang !== 'cn' && res.data.lang !== 'in' && res.data.lang !== 'th') {
            this.isLongLang = true;
          }

          // 开关接口比较慢时处理如下
          // 获取语言可能速度没有路由变化来的快，页面导航需要重新获取一次
          this.rouFun(this.$route);
          // 获取语言可能速度慢时，页面的内容可能是翻译前的，需要重新编译
          this.reloadShow = false;
          this.$nextTick(() => (this.reloadShow = true));

          let gameArr = [...data.chess, ...data.egame, ...data.esport, ...data.fish, ...data.live, ...data.sport, ...data.lottery]

          // 开关
          this.$store.dispatch("getOnOffBtn", res.data);
          this.$store.dispatch("getGameOnOff", gameArr);
        }
      });
    },
    // 页面导航多语言变化函数
    rouFun (to) {
      // console.log(111, to)
      // console.log(222, this.$route)
      if (to.fullPath !== '/') {
        this.titleArr.type = this.$t("" + to.meta.type);
        if (to.meta.name) this.titleArr.name = this.$t("" + to.meta.name);
        else this.titleArr.name = '';
        this.$store.dispatch("getCurLeftMenu", to.path);
      }
    }
  },
  watch: {
    $route (to, from) {
      this.rouFun(to)
    },
    "$i18n.locale": {
      // 监听语言切换进行对应每一种语言设置标准字体
      handler (lang) {
        let fontFamily;
        switch (lang) {
          case "es":
          case "vi":
          case "id":
          case "ms":
          case "br":
          case "en":
            // 西班牙，葡萄牙，马来西亚，印尼，英文和越南文的标准字体
            fontFamily = "Times New Roman, Arial, Calibri";
            break;
          case "th":
            // 泰文标准字体
            fontFamily = "Krungthep, Sarabun";
            break;
          case "ja":
            // 日文标准字体
            fontFamily = "Meiryo, Open Sans";
            break;
          case "in":
            // 印度文标准字体
            fontFamily = "Mangal, Nirmala UI";
            break;
          default:
            // 默认中文字体
            fontFamily = "Montserrat, sans-serif";
        }
        document.querySelector("body").style.fontFamily = fontFamily;
      },
      immediate: true
    },
  }
};
</script>

<style lang="scss">
@import './assets/css/common';
@import './assets/css/otherLang';

.el-button--success {
  background-color: #0ec504 !important;
}

#app {
  .container {
    display: flex;
    min-height: 900px;
  }
  .ui-main {
    width: calc(100% - 220px);
    // height: 100%;
    background: #1a242d;
    .main-head {
      background: #1a242d;
      border-bottom: 1px solid #d0d0d0;
      padding: 0 10px;
      height: 50px;
      .el-breadcrumb {
        line-height: 50px;
        display: inline-block;
        border-bottom: 2px solid #0ec504;
        font-size: 16px;
        padding: 0 10px;
        .el-breadcrumb__inner {
          color: #fff;
        }
      }
    }
    .con_box {
      margin: 20px;
    }
  }
}
.aaaaa {
  z-index: 5000 !important;
}
</style>