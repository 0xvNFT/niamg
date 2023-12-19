<template>
  <div class="thirdPage-page">
    <iframe
      :src="thirdPageUrl || iframeHtml"
      frameborder="0"
      scrolling="auto"
      class="iframeGame"
      ref="iframeDom"
    >
    </iframe>
    <!-- 操作按钮 -->
    <div class="controls">
      <!-- 关闭按钮 -->
      <a href="#/index" class="controls-btn"><i class="el-icon-close"></i></a>
      <!-- 全屏按钮 -->
      <p class="controls-btn" @click="full">
        <i class="el-icon-full-screen"></i>
      </p>
      <!-- 新窗口打开 -->
      <!-- <a :href="thirdPageUrl" target="_blank" class="controls-btn" @click="goPage($event)"><i class="el-icon-monitor"></i></a> -->
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      isFull: false,
      iframeHtml: "",
    };
  },
  computed: {
    ...mapState(["thirdPageUrl", "userInfo", "thirdPageHtml"]),
  },
  components: {},
  created() {},
  mounted() {
    if (this.thirdPageHtml) {
      // 将二进制流文件转换成blob对象
      let blob = new Blob([this.thirdPageHtml], { type: "text/html" });
      // 创建url对象
      this.iframeHtml = URL.createObjectURL(blob);
      // 将url赋给iframe的src属性
      this.$refs.iframeDom.src = this.iframeHtml;
    } 

    if (!this.thirdPageUrl && !this.thirdPageHtml) {
      this.$router.push("/index");
    }
  },
  methods: {
    // 切换全屏
    fullOrExit() {
      if (this.isFull) this.exitFullscreen();
      else this.full();
    },
    // 全屏
    full() {
      let ele = document.querySelector(".iframeGame");
      if (ele.requestFullscreen) {
        ele.requestFullscreen();
      } else if (ele.mozRequestFullScreen) {
        ele.mozRequestFullScreen();
      } else if (ele.webkitRequestFullscreen) {
        ele.webkitRequestFullscreen();
      } else if (ele.msRequestFullscreen) {
        ele.msRequestFullscreen();
      }
      this.isFull = true;
    },
    // 退出全屏
    exitFullscreen() {
      let ele = document.querySelector(".iframeGame");
      if (document.exitFullScreen) {
        document.exitFullScreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
      } else if (ele.msExitFullscreen) {
        ele.msExitFullscreen();
      }
      this.isFull = false;
    },
    // 前往其他页面
    goPage(event) {
      // 判断是否登录
      if (!this.userInfo.login) {
        var val = { select: 0, show: true };
        this.$bus.$emit("registerShow", val);
        // 如果没有登录，禁止跳转
        event.preventDefault();
      }
      console.log("thirdPage isLogin");
    },
  },
  beforeDestroy() {
    // 销毁url对象，释放内存
    URL.revokeObjectURL(this.iframeHtml);
    this.$store.dispatch("getThirdPageHtml", '');
     this.$store.dispatch("getThirdPageUrl", '');
  },
};
</script>

<style lang="scss">
.thirdPage-page {
  margin: 36px auto;
  text-align: center;
  width: calc(100% - 50px);
  display: flex;
  iframe {
    border: none;
    border-radius: 14px;
    // width: 100%;
    height: 100%;
    min-height: calc(100vh - 120px);
    width: calc(100% - 60px);
    background: $titleBg;
  }
  .controls-btn {
    background-color: $titleBg;
    border-radius: 10px;
    color: $borderColorJoker;
    cursor: pointer;
    font-size: 22px;
    margin-left: 15px;
    margin-bottom: 8px;
    display: block;
    width: 40px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    transition: all 0.3s ease;
    &:hover {
      color: #fff;
    }
  }
}
</style>
