<template>
  <div class="appDown-page">
    <div class="appDown">
      <img src="../../assets/images/appDown/ys.png" alt="" class="ys" />
      <div class="downloadBox">
        <div class="left">
          <img :src="onOffBtn.logo" alt="" class="logo" />
          <p>
            <span style="margin-right: 20px" @click="androidUrl">
              <i class="iconfont icon-android"></i><strong>Android</strong>
            </span>
            <span @click="iosUrl">
              <i class="iconfont icon-iOS"></i>
              <strong>iOS</strong>
            </span>
          </p>
          <img
            src="../../assets/images/appDown/gameList.png"
            alt=""
            class="gameList"
          />
        </div>
        <div class="right">
          <img :src="imgUrl" alt="" />
          <p @click="gotoUrl">{{ $t("downLoadText") }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      imgUrl: "", //二维码
      appflag: "android", //判断当前点击的是ios 还是 android
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  created() {},
  mounted() {
    //默认显示安卓二维码
    this.androidUrl();
  },
  methods: {
    //获取安卓二维码
    androidUrl() {
      this.appflag = "android";
      this.imgUrl =
        this.onOffBtn.androidQrDownloadLinkSuper ||
        this.onOffBtn.androidQrDownloadLink;
    },
    //获取ios二维码
    iosUrl() {
      this.appflag = "ios";
      this.imgUrl =
        this.onOffBtn.iosQrDownloadLinkSuper || this.onOffBtn.iosQrDownloadLink;
    },
    //跳转下载页面
    gotoUrl() {
      console.log(111111111)
      if (this.appflag === "android") {
        window.location.href =
          this.onOffBtn.androidDownloadLinkSuper ||
          this.onOffBtn.androidDownloadLink;
      } else {
        window.location.href =
          this.onOffBtn.iosDownloadLinkSuper || this.onOffBtn.iosDownloadLink;
      }
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.appDown-page {
  background: url("../../assets/images/appDown/bg.png");
  background-size: 100% 100%;
  background-position: center center;
  overflow: auto;
  overflow: hidden;

  padding-top: 50px;
  .appDown {
    margin: auto;
    max-width: 1200px;
    display: flex;
    justify-content: space-around;
    align-items: top;
  }
  .ys {
    width: 50%;
    min-width: 380px;
  }
  .downloadBox {
    height: 350px;
    min-width: 400px;
    width: 35%;
    border: 2px solid gainsboro;
    border-radius: 20px;
    margin-bottom: 5%;
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    padding: 10px;
    margin-top: 50px;
    .left {
      width: 60%;
      .logo {
        width: 70%;
      }
      p {
        margin-top: 20px;
        font-size: 28px;
        white-space: nowrap;
        span {
          cursor: pointer;
          i {
            font-size: 32px;
          }
        }
      }
      .gameList {
        margin-top: 30px;
        width: 100%;
      }
    }
    .right {
      width: 40%;

      img {
        width: 60%;
        margin-bottom: 20px;
      }
      p {
        width: 65%;
        padding: 5px;
        border: 2px solid gainsboro;
        border-radius: 20px;
        margin: auto;
        cursor: pointer;
      }
      p:hover {
        color: #1a59cfe7;
      }
    }
  }
  .download {
    display: flex;
    margin-top: 30px;

    a {
      margin: 10px;
      text-align: center;
      height: 40px;
      width: 150px;
      line-height: 40px;
      background: #ea6d6d;
      color: #fff;
      border-radius: 10px;
      cursor: pointer;

      i {
        font-size: 20px;
      }

      &:hover {
        opacity: 0.9;
      }
    }
  }
}
</style>
