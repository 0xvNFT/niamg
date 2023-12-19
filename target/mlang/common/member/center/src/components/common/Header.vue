<template>
  <div class="header">
    <div class="logo">
      <img src="../../assets/images/common/logo.png" alt="">
    </div>
    <div class="top_menu" v-show="showMenu">
      <a href="/index.do" class="item">
        <div class="iconfont iconhome"></div>
        <!-- <div class="icon goHome"></div> -->
        <p>{{ $t("indexText") }}</p>
      </a>
      <!-- <template v-if="onOffBtn">
        <a href="/live.do" class="item" v-if="onOffBtn.game.live === 2">
          <div class="iconfont iconreal"></div>
          <div class="icon real"></div>
          <p>{{ $t("live") }}</p>
        </a>
        <a href="/chess.do" class="item" v-if="onOffBtn.game.chess === 2">
          <div class="iconfont iconchess"></div>
          <p>{{ $t("chess") }}</p>
        </a>
        <a href="/egame.do" class="item" v-if="onOffBtn.game.egame === 2">
          <div class="iconfont iconegame"></div>
          <p>{{ $t("egame") }}</p>
        </a>
        <a href="/sport.do" class="item" v-if="onOffBtn.game.sport === 2">
          <div class="iconfont iconsport"></div>
          <p>{{ $t("sport") }}</p>
        </a>
      </template> -->
      <div class="item" @click="goServer" v-if="!onOffBtn.isOnlineCustClose">
        <div class="iconfont iconserver"></div>
        <p>{{ $t("onlineCust") }}</p>
      </div>
      <a class="item" href="/logout.do">
        <div class="iconfont iconout"></div>
        <p>{{ $t("quit") }}</p>
      </a>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      showMenu: false
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
  },
  created () {
    this.showMenu = !document.location.href.includes('hideMenu')
  },
  mounted () {
  },
  methods: {
    goServer () {
      if (this.onOffBtn.kfUrl) {
        location.href = this.onOffBtn.kfUrl
      } else {
        this.toastFun(this.$t("notSet"));
      }
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
.header {
  background: #0f1923;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px 10px 0;
  .logo {
    width: 213px;
    text-align: center;
  }
  .top_menu {
    display: flex;
    .item {
      margin: 0 18px;
      text-align: center;
      font-size: 12px;
      cursor: pointer;
      color: #fff;
      .iconfont {
        font-size: 30px;
        margin: 0 auto 8px;
        color: #0ec504;
        border: 1px solid #0ec504;
        border-radius: 50%;
        width: 42px;
        height: 42px;
        transition: all 0.3s;
      }
    }
    .item:hover {
      .iconfont {
        // background: #fff;
        color: #f12c4c;
        border: 1px solid #f12c4c;
      }
    }
  }
}
</style>


