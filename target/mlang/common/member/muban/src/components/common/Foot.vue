<template>
  <div class="footer">
    <div class="footer-content">
      <div class="footer-content-list">
        <a class="list-val" v-for="(item, index) in helpList" @click="onHelp(item)" :key="index">{{ item.title }}</a>
      </div>
      <!-- 三方图片 -->
      <div class="footer-content-about">
        <div class="about-img">
          <a v-for="val in sanfangList" :href="val.url" :target="val.target" :key="val.id">
            <img :src="$publicJs.imgUrlFun('common/footer' + val.id + '.png')" alt="brand icon" />
          </a>
        </div>
        <div class="about-text" v-html="stationIntroduce"></div>
      </div>
    </div>
    <div class="footer-line"></div>
    <div class="footer-linkslist">
      <img :src="$publicJs.imgUrlFun('common/footer' + (val + 8) + '.png')" alt="" v-for="val in 8" :key="val" />
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      helpList: [], //帮助中心
    };
  },
  props: {
    stationIntroduce: "",
  },
  computed: {
    ...mapState(["onOffBtn"]),
    // 生成三方图片以及链接
    sanfangList() {
      // 一共8个图标，先定义好索引值
      const source = [
        { id: 1},
        { id: 2},
        { id: 3},
        { id: 4},
        { id: 5, url: this.onOffBtn.facebook_url || 'javascript:;', target: this.onOffBtn.facebook_url ? '_blank' : '_self' },
        { id: 6, url: this.onOffBtn.instagram_url || 'javascript:;', target: this.onOffBtn.instagram_url ? '_blank' : '_self' },
        { id: 7, url: this.onOffBtn.telegram_url || 'javascript:;', target: this.onOffBtn.telegram_url ? '_blank' : '_self' },
        { id: 8}
      ]
      return source
    }
  },
  components: {},
  created() { },
  mounted() {
    this.clickHelp();
  },
  methods: {
    //获取帮助中心
    clickHelp() {
      this.$API.help().then((res) => {
        if (res) {
          this.helpList = res.slice(0, 10);
          this.$store.dispatch("getHelpData", res);
        }
      });
    },
    //帮助 点击事件
    onHelp(val) {
      this.$router.replace("/help?helpId=" + val.id);
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.footer {
  background-color: $black;
  font-family: Montserrat;
  font-size: 12px;
  // margin-top: 20px;
  overflow: hidden;
  padding-bottom: 24px;
  width: 100%;
  min-height: 300px;

  .footer-content {
    display: flex;
    flex-wrap: wrap;
    // grid-template-columns: 65% 35%;
    justify-content: space-between;
    margin: 0 auto;
    max-width: 1080px;
    padding: 20px;

    .footer-content-list {
      flex: 2;
      display: flex;
      flex-wrap: wrap;
      // grid-template-columns: 55% 45%;
      // justify-content: space-between;
      min-width: 300px;

      a {
        display: block;
      }

      .list-title {
        color: #adb6c4;
        display: block;
        font-size: 12px;
        font-weight: 700;
        line-height: 16px;
        margin: 8px 0;
        text-transform: uppercase;
      }

      .list-val {
        color: #3f526e;
        cursor: pointer;
        font-size: 11px;
        font-weight: 600;
        transition: color 0.3s;
        width: 50%;
        word-wrap: break-word;
        padding: 0 5px 10px;

        &:hover {
          color: #cccccc;
        }
      }
    }

    .footer-content-about {
      flex: 1;

      .about-img {
        display: flex;
        align-items: center;
        justify-content: left;
        flex-wrap: wrap;
        margin: 0 auto 5px;
        width: 100%;

        img {
          height: 40px;
          display: block;
          margin: 0 5px 5px 0;
          opacity: 0.8;

          &:hover {
            opacity: 1;
          }
        }
      }

      .about-text {
        color: #3f526e;
        font-weight: 600;
        line-height: 1.3;
        margin: 0 auto;
        padding-top: 8px;
        text-align: left;
        width: 100%;
      }
    }
  }

  .footer-line {
    margin: 0 auto;
    height: 1px;
    width: 70%;
    background-color: #3f526e;
  }

  .footer-linkslist {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    justify-content: center;
    margin: 10px auto;
  }
}
</style>
