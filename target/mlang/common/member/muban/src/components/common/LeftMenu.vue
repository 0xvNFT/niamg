<template>
  <div class="leftMenu" :class="leftMenuShow ? '' : 'hide'">
    <!-- 头部图 -->
    <div class="leftMenu-top">
      <!-- 个人总览 -->
      <a href="#/userView" @click="goPage($event)">
        <img :src="$publicJs.imgUrlFun(appLang + '/grzl_img.png')" alt="" />
      </a>
      <div class="leftMenu-top-div">
        <!-- 大转盘 -->
        <a
          target="_blank"
          @click="toVisitGame"
          :class="!onOffBtn.isRedBag && !onOffBtn.isSignIn ? 'all' : ''"
          v-if="onOffBtn.isTurnlate"
          v-banned:click="toVisitGame"
          :key="forceUpdateKey"
        >
          <img :src="$publicJs.imgUrlFun(appLang + '/dzp_img.png')" alt="" />
        </a>
        <!-- 大转盘弹出框 -->
        <TurntableGame ref="turnableGame" />
        <!-- 红包 -->
        <a
          href="/redBag2.do"
          target="_blank"
          @click="goPage($event)"
          :class="!onOffBtn.isTurnlate ? 'all' : ''"
          v-if="onOffBtn.isRedBag"
        >
          <img :src="$publicJs.imgUrlFun(appLang + '/hb_img.png')" alt="" />
        </a>
        <!-- 签到 -->
        <a
          href="/signIn.do"
          target="_blank"
          @click="goPage($event)"
          :class="!onOffBtn.isTurnlate ? 'all' : ''"
          v-else-if="onOffBtn.isSignIn"
        >
          <img :src="$publicJs.imgUrlFun(appLang + '/qd_img.png')" alt="" />
        </a>
      </div>
    </div>
    <!-- 中间图 优惠活动 -->
    <div class="leftMenu-center" v-if="activelist.length">
      <div class="leftMenu-center-img">
        <img
          :src="item.titleImgUrl"
          v-for="(item, index) in activelist"
          :key="index"
          @click="goDetail(item)"
        />
      </div>
      <div class="leftMenu-center-btn">
        <router-link to="/active">
          <el-button type="primary" class="activeBtn">{{
            $t("loadMore")
          }}</el-button>
        </router-link>
      </div>
    </div>
    <!-- 菜单一 -->
    <div class="leftMenu-list1">
      <a href="#/recordUser" @click="goPage($event)">
        <i class="el-icon-user"></i>
        <span>{{ $t("recordUser") }}</span>
      </a>
      <a href="#/myRecommend" v-banned :key="forceUpdateKey * 3">
        <i class="el-icon-document-copy"></i>
        <span>{{ $t("myRecommend") }}</span>
      </a>
      <a href="#/userList" v-banned :key="forceUpdateKey * 5">
        <i class="el-icon-bank-card"></i>
        <span>{{ $t("userList") }}</span>
      </a>
      <a href="#/securityCenter" @click="goPage($event)">
        <i class="el-icon-suitcase"></i>
        <span>{{ $t("securityCenter") }}</span>
      </a>
      <a href="#/moneyChange" v-banned :key="forceUpdateKey * 7">
        <i class="el-icon-connection"></i>
        <span>{{ $t("moneyChange") }}</span>
      </a>
    </div>
    <!-- 菜单二 -->
    <div class="leftMenu-list2">
      <!-- 切换语言 -->
      <el-dialog
        :title="$t('chooseLang')"
        :visible.sync="dialogShow"
        width="40%"
        class="chooseLang-dialog common_dialog"
      >
        <div class="content">
          <span class="title">{{ $t("curLang") }}：</span>
          <el-select
            v-model="curLang"
            :placeholder="$t('pleaseBank')"
            @change="changeLang"
          >
            <el-option
              v-for="(item, key) in onOffBtn.languages"
              :key="key"
              :label="item.lang"
              :value="item.name"
            >
            </el-option>
          </el-select>
        </div>
        <span slot="title" class="dialog-header">
          <img src="../../assets//images/earth.png" alt="" />
          <span>{{ $t("chooseLang") }}</span>
        </span>
      </el-dialog>
      <a href="javascript:;" @click="dialogShow = true">
        <i class="el-icon-money"></i>{{ $t("langText") }}
      </a>
      <a
        :href="onOffBtn.kfUrl || 'javascript:;'"
        :target="onOffBtn.kfUrl ? '_blank' : '_self'"
      >
        <i class="el-icon-service"></i>{{ $t("onlineCust") }}
      </a>
      <a href="#/appDown">
        <i class="el-icon-mobile-phone"></i>{{ $t("appDown") }}
      </a>
      <a href="#/notice" @click="goPage($event)">
        <i class="el-icon-document"></i>{{ $t("notice") }}
      </a>
      <a
        href="#/advice"
        @click="goPage($event)"
        v-if="onOffBtn.onOffStationAdvice"
      >
        <i class="el-icon-chat-line-round"></i>{{ $t("advice") }}
      </a>
    </div>
    <!-- 时间版本 -->
    <div class="leftMenu-bottom">
      <div class="leftMenu-bottom-time">{{ nowDate }}</div>
      <div class="leftMenu-bottom-version">{{ copyright }}</div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import TurntableGame from "./TurntableGame.vue";

export default {
  data() {
    return {
      nowDate: "", //当前时间
      nowTimer: null, //当前时间定时器
      activelist: [], //优惠活动
      copyright: copyright, //版本
      dialogShow: false, //语言弹窗
      curLang: "cn", //当前app语言
      forceUpdateKey: 1, // 元素的唯一键
    };
  },
  props: {
    leftMenuShow: "",
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn"]),
    appLang() {
      return this.$i18n.locale;
    },
  },
  components: { TurntableGame },
  created() {
    // 时间倒计时
    this.nowTimer = setInterval(() => {
      let curDate = String(new Date());
      this.nowDate = curDate.split("+")[0];
    }, 1000);
  },
  mounted() {
    this.curLang = sessionStorage.mubanLang || this.onOffBtn.lang;
    // 获取优惠活动数据
    this.getActive();
    this.$bus.$on("refreshTags", () => {
      // 进行更新元素 禁止试玩账号操作
      this.$nextTick(() => {
        let timer;
        timer = setInterval(() => {
          // 执行到接口返回用户类型为止
          if (this.userInfo.type) {
            this.forceUpdateKey = Math.random();
            clearInterval (timer);
          }
        }, 100 );
      });
    });
  },
  methods: {
    // 获取优惠活动数据
    getActive() {
      this.$API.active().then((res) => {
        if (res) {
          this.activelist = res.activity.slice(0, 2);
          this.$store.dispatch("getActiveData", res.activity);
        }
      });
    },
    // 前往优惠活动详情页
    goDetail(val) {
      this.$router.replace("/activeDetails?&id=" + val.id);
    },
    // 前往其他页面（需要登录才能访问的页面用这个方法）
    goPage(event) {
      // 判断是否登录
      if (!this.userInfo.login) {
        var val = { select: 0, show: true };
        this.$bus.$emit("registerShow", val);
        // 如果没有登录，禁止跳转
        event.preventDefault();
      }
    },
    //语言选择
    changeLang() {
      this.$API.changeLanguage({ lang: this.curLang }).then((res) => {
        if (res.success) {
          this.dialogShow = false;
          this.$i18n.locale = this.curLang;
          sessionStorage.mubanLang = this.curLang;
          this.$message.success(res.msg);
          this.$bus.$emit("upDataRefresh");
        }
      });
    },
    // 弹出大转盘游戏窗口
    toVisitGame() {
      return //目前不开启大转盘游戏
      if (!this.userInfo.login) {
        // 未登录时弹出登录窗口
        var val = { select: 0, show: true };
        this.$bus.$emit("registerShow", val);
        return
      }
      const GAMECPN = this.$refs.turnableGame;
      // 请求大转盘游戏奖品清单
      this.$API.getTurntablesRewards().then( async (res) => {
        console.log("🚀 ~ res:", res)
        GAMECPN.prizesList = await res;
      })
      GAMECPN.showGame = true;
      setTimeout(() => {
        GAMECPN.toControlAudio(true)
      }, 1000);
    }
  },
  destroyed() {
    // 清除时间倒计时
    clearInterval(this.nowTimer);
    this.nowTimer = null;
  },
};
</script>

<style lang="scss">
.leftMenu {
  // max-width: 300px;
  // min-width: 240px;
  // width: auto;
  width: 300px;
  padding: 16px;
  height: 100%;
  overflow-y: auto;
  transition: all 0.3s ease;

  &.hide {
    margin-left: -300px;
  }

  .leftMenu-top {
    display: flex;
    flex-wrap: wrap;

    img {
      max-width: 100%;
      border-radius: 0.5rem;
      cursor: pointer;
      display: block;
      min-height: 80px;
    }

    .leftMenu-top-div {
      display: flex;
      justify-content: space-between;
      margin: 0px 0px 0.5rem;

      a {
        width: 48%;
        margin: 0.5rem 0px 0px;

        &.all {
          width: 100%;
        }
      }
    }
  }

  .leftMenu-center {
    font-family: Montserrat-ExtraBold;
    margin-bottom: 12px;
    background: linear-gradient(0deg, #1b3558, #182535);
    border-radius: 0.5rem;

    .leftMenu-center-img {
      padding: 10px 10px 5px;

      img {
        width: 100%;
        border-radius: 0.5rem;
        margin-bottom: 12px;
        cursor: pointer;
      }
    }

    .leftMenu-center-btn {
      margin: 0 auto;
      padding-bottom: 18px;
      display: flex;
      justify-content: center;
      .activeBtn {
        background-color: #0048ff !important;
        border: none;
      }
      .activeBtn:hover {
        background: linear-gradient(25deg, #1953e4, #205aec);
      }
    }
  }

  .leftMenu-list1 {
    a {
      color: $borderColorJoker;
      cursor: pointer;
      display: flex;
      align-items: center;
      font-size: 16px;
      font-weight: 600;
      height: 36px;
      line-height: 1.5;
      margin: 2px 0;
      padding-left: 12px;
      transition: all 0.15s;
      border-radius: 10px;

      i {
        font-size: 18px;
        margin-right: 5px;
      }

      &:hover {
        border: 1px solid #154fa0;
        color: #fff;
      }
    }
  }

  .leftMenu-list2 {
    a {
      background-color: rgb(28, 37, 50);
      margin-top: 8px;
      border-radius: 10px;
      color: #fff;
      cursor: pointer;
      font-size: 12px;
      font-weight: 600;
      line-height: 1.5;
      padding: 8px 16px;
      display: flex;
      align-items: center;

      i {
        font-size: 18px;
        margin-right: 5px;
      }

      &:hover {
        opacity: 0.8;
      }
    }
  }

  .leftMenu-bottom {
    margin: 2rem 0 0;
    text-align: center;
  }

  .chooseLang-dialog {
    display: flex;
    align-items: center;
    .el-dialog__header {
      height: 30px;
      padding: 0 0 0 20px;

      .dialog-header {
        display: flex;
        align-items: center;

        span {
          margin-left: 10px;
          line-height: 30px;
        }
      }

      .el-dialog__headerbtn {
        top: 4px;
      }
    }

    .el-dialog__body {
      font-weight: 900;
      padding: 30px 10px;
      color: rgba(255, 255, 255, 0.85);
      border-radius: 0 0 10px 10px;
      display: flex;
      justify-content: center;
    }
  }
}
</style>
