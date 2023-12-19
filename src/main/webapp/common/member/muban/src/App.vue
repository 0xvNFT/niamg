<template>
  <div id="app" v-if="onOffBtn.lang && reloadShow">
    <!-- 头部 -->
    <Header :leftMenuShow.sync="leftMenuShow"></Header>
    <div class="container">
      <!-- 左边菜单 -->
      <LeftMenu :leftMenuShow="leftMenuShow"></LeftMenu>
      <div class="con_box" :class="leftMenuShow ? '' : 'full'">
        <keep-alive include="index">
          <!-- 中间页面 -->
          <router-view class="pageCommon" />
        </keep-alive>
        <!-- 底部 -->
        <Footer :stationIntroduce="stationIntroduce"></Footer>
      </div>
    </div>
    <!-- 登录注册弹窗 -->
    <Register v-if="registerShow" :activeNum="active"></Register>
    <!-- 右下角悬浮框 -->
    <Float></Float>
  </div>
</template>

<script>
import { mapState } from "vuex";
import LeftMenu from "@/components/common/LeftMenu";
import Register from "@/components/common/Register";
import Float from "@/components/common/Float";
import configJs from "@/assets/js/stationConfig";

export default {
  data() {
    return {
      reloadShow: true, //刷新页面
      leftMenuShow: true, //左边菜单是否显示
      userInfoTimer: null, //用户信息定时器
      registerShow: false, //登录弹窗
      active: 0, //登录弹窗
      stationName: "", //网站名称
      stationIntroduce: "", //网站介绍
      timer: null, //初始化定时器
    };
  },
  computed: {
    ...mapState(["onOffBtn", "loadPage", "userInfo", "afterLoginOnOffBtn"]),
  },
  components: {
    LeftMenu,
    Register,
    Float,
  },
  created() {
    this.goTypePage();
    // 获取开关配置
    this.getStationConfig();
    // 用户信息
    this.getInfo();

    // 用户信息定时器
    this.userInfoTimer = setInterval(() => {
      if (this.$store.state.userInfo.login) {
        // 用户信息
        this.getInfo();
      } else {
        clearInterval(this.userInfoTimer);
        this.userInfoTimer = null;
      }
    }, 10000);

    // 屏幕小于1024时，隐藏左边菜单栏
    if (document.body.clientWidth < 1024) this.leftMenuShow = false;
    this.$store.dispatch("getPageWidth", document.body.clientWidth);
    window.onresize = () => {
      this.$store.dispatch("getPageWidth", document.body.clientWidth);
      if (document.body.clientWidth < 1024) this.leftMenuShow = false;
      else this.leftMenuShow = true;
    };
  },
  mounted() {
    // 检测浏览器路由改变页面不刷新问题,hash模式的工作原理是hashchange事件
    window.addEventListener(
      "hashchange",
      () => {
        let currentPath = window.location.hash.slice(1);
        if (this.$route.path !== currentPath) {
          this.$router.push(currentPath);
        }
      },
      false
    );

    //登录注册弹出框
    this.$bus.$on("registerShow", (val) => {
      this.active = val.select;
      this.registerShow = val.show;
    });
    // 刷新用户信息
    this.$bus.$on("refreshUserInfo", (refresh) => {
      // 用户信息
      this.getInfo(refresh);
    });
    this.$bus.$on("refreshAutoTranout", (refresh) => {
      //把钱从三方转出来
      this.autoTranout();
    });
    this.$bus.$on("upDataRefresh", () => {
      this.reloadShow = false;
      this.$nextTick(() => {
        this.reloadShow = true;
      });
    });
    // 监听 游戏容器重置滚动条事件
    this.$bus.$on("needResetScroll", () => {
      this.resetScroll();
    });
  },
  methods: {
    //根据推广链接的 type后缀 跳转不同的页面
    goTypePage() {
      let pathType = this.$publicJs.getQuery("type"); //获取路径
      let pcode = this.$publicJs.getQuery("pcode"); //获取推广码
      if (!sessionStorage.getItem("pcode")) {
        sessionStorage.setItem("pcode", pcode); //session存值 防止带后缀的链接 在登录退出后丢失
      }
      let myQuery = "?type=" + pathType + "&pcode=" + pcode;
      switch (pathType) {
        case "register": //跳转首页 并且弹出注册弹窗
          this.$router.replace("/index" + myQuery);
          this.active = 1;
          this.registerShow = true;
          break;
        case "index": //跳转首页
          this.$router.replace("/index" + myQuery);
          break;
        case "active": //跳转优惠活动
          this.$router.replace("/active" + myQuery);
          break;

        default:
          break;
      }
    },

    // 登陆之前获取开关配置
    getStationConfig() {
      this.$API.getStationConfig({ load: true }).then((res) => {
        if (res) {
          // 获取语言可能速度慢时，页面的内容可能是翻译前的，需要重新编译
          // this.reloadShow = false;
          // this.$nextTick(() => (this.reloadShow = true));
          // 开关
          // res.lang = 'cn';
          this.$i18n.locale = res.lang;

          let gameArr = [
            ...res.chess,
            ...res.egame,
            ...res.esport,
            ...res.fish,
            ...res.live,
            ...res.sport,
            ...res.lottery,
          ];

          this.$store.dispatch(
            "getConfigJs",
            configJs.configStation(res.stationCode)
          );
          this.$store.dispatch("getOnOffBtn", res);
          this.$store.dispatch("getGameOnOff", gameArr);
          this.stationName = res.stationName;
          this.stationIntroduce = res.stationIntroduce;
          setTimeout(() => {
            $(".pageTitle").html(this.stationName);
          }, 1000);
        }
      });
    },
    // 登陆之后获取开关配置
    getConfig() {
      this.$API.getConfig().then((res) => {
        if (res) {
          this.$store.dispatch("getAfterLoginOnOffBtn", res);
        }
      });
    },
    // 用户信息
    getInfo(refresh) {
      this.$API.userAllInfo({ load: refresh }).then((res) => {
        if (res) {
          // 将信息存储到 store，并且存入 sessionStorage
          this.$store.dispatch("getUserInfo", res);
          sessionStorage.userInfo = JSON.stringify(res);
        }
      });
    },
    //把钱从三方转出来
    autoTranout() {
      this.$API.autoTranout().then((res) => {
        if (res.success) {
          // 用户信息
          this.getInfo();
        }
      });
    },
    // 游戏容器重置滚动条
    resetScroll() {
      let el = document.getElementsByClassName("con_box");
      this.timer = setInterval(() => {
        el[0].scrollTop += -8;
        if (el[0].scrollTop === 0) {
          clearInterval(this.timer);
        }
      }, 2);
    },
  },
  watch: {
    $route: {
      handler: function (to, from) {
        //关闭游戏页面
        if (from.path === "/thirdPage" && this.$store.state.userInfo.login) {
          this.autoTranout();
        }
      },
      deep: true
    },
    "$i18n.locale"(lang) {
      // 监听语言切换进行对应每一种语言设置标准字体
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
      let el = document.getElementsByTagName("body")[0];
      el.style.fontFamily = fontFamily;
    },
    "userInfo.login"(newVal, oldVal) {
      if (newVal) {
        this.getConfig();
      }
    },
  },
};
</script>

<style lang="scss">
@import "./assets/css/common";
@import "./assets/css/mobile";
@import "./assets/css/modules";

#app {
  position: relative;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  background-color: $bgColor;
  color: #fff;
  .container {
    display: flex;
    height: calc(100vh - 64px);
    width: 100%;
    .con_box {
      overflow: auto;
      width: calc(100% - 300px);
      transition: all 0.3s ease;
      &.full {
        width: 100%;
      }
      .pageCommon {
        min-height: calc(100vh - 280px);
        padding-bottom: 20px;
      }
    }
  }
  .heard {
    border-bottom: 1px solid $borderColorJoker;
    padding: 10px;
    color: #fff;
    font-size: 1.4rem;
    font-weight: 700;
    line-height: 2rem;
    // margin-bottom: 1.2rem;
    // margin-top: 18px;
    padding-left: 12px;
  }
  .el-button--primary {
    background-color: #2283f6;
    border-color: #2283f6;
  }
}

// 自定义进度条颜色
#nprogress .bar {
  height: 3px !important;
}

::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

::-webkit-scrollbar-track {
  background-color: $bgColor;
}

::-webkit-scrollbar-thumb {
  background-color: $bgjoker;
  border-radius: 4px;
}

.scrollbar {
  scrollbar-color: #888 #f1f1f1;
  scrollbar-width: thin;
}
#modal-mark {
  position: fixed;
  top: 0;
  height: 100%;
  width: 100%;
  z-index: 1500;
  backdrop-filter: saturate(180%) blur(10px);
}
</style>
