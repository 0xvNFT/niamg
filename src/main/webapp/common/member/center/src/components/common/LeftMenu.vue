<template>
  <div class="side-left">
    <!-- 用户信息 -->
    <div class="user-info">
      <div class="userBox">
        <div class="item clearfix">
          <img src="../../assets/images/common/user.png" alt="" class="icon" />
          <div class="info">
            <div class="stc_c">{{ userInfo.username || $t("noLogin") }}</div>
            <img
              :src="userInfo.userDegreeeIcon ? userInfo.userDegreeeIcon : require('@/assets/images/common/grade.png')"
              v-if="onOffBtn.degreeShow"
              class="level-icon"
            />
          </div>
        </div>
        <div class="item clearfix balance">
          <img src="../../assets/images/common/money.png" alt="" class="icon" />
          <div class="info">
            <div class="stc_t">{{ $t("afterMoney") }}：</div>
            <div class="stc_c">{{ userInfo.money || 0 }}</div>
            <img
              src="../../assets/images/common/refresh.png"
              alt=""
              class="refresh"
              @click="refreshInfo"
            />
          </div>
        </div>
      </div>
      <div class="acc-links" v-if="!isGuest">
        <router-link to="/recharge" class="btn-navacc">
          <span class="iconfont iconchongzhi2"></span>{{ $t("payText") }}
        </router-link>
        <router-link to="withdraw" class="btn-navacc">
          <span class="iconfont iconedbde"></span>{{ $t("withdrawal") }}
        </router-link>
      </div>
    </div>
    <!-- 菜单栏 -->
    <el-menu
      :default-active="$route.path"
      :default-openeds="[$route.meta.openeds]"
      class="leftMenu"
      @open="handleOpen"
      background-color="#363636"
      text-color="#fff"
      :unique-opened="true"
      :router="true"
      ref="leftMenu"
    >
      <!-- 投注记录 -->
      <el-submenu index="1" v-if="onOffBtn.game">
        <template slot="title">
          <i class="el-icon-document"></i>
          <span :title="$t('orderMenu')">{{ $t("orderMenu") }}</span>
        </template>
        <el-menu-item
          index="/orderSport"
          v-if="onOffBtn.game.sport === 2 && !isGuest"
          :class="curLeftMenu === '/orderSport' ? 'cur-active' : ''"
          >{{ $t("orderSport") }}</el-menu-item
        >
        <el-menu-item
          index="/orderEgame"
          v-if="onOffBtn.game.egame === 2 && !isGuest"
          :class="curLeftMenu === '/orderEgame' ? 'cur-active' : ''"
          >{{ $t("orderEgame") }}</el-menu-item
        >
        <!-- v-if="onOffBtn.game.egame === 2"  -->
        <el-menu-item
          index="/orderEgamePt"
          v-if="!isGuest"
          :class="curLeftMenu === '/orderEgamePt' ? 'cur-active' : ''"
          >{{ $t("orderEgamePt") }}</el-menu-item
        >
        <el-menu-item
          index="/orderLive"
          v-if="onOffBtn.game.live === 2 && !isGuest"
          :class="curLeftMenu === '/orderLive' ? 'cur-active' : ''"
          >{{ $t("orderLive") }}</el-menu-item
        >
        <el-menu-item
          index="/orderChess"
          v-if="onOffBtn.game.chess === 2 && !isGuest"
          :class="curLeftMenu === '/orderChess' ? 'cur-active' : ''"
          >{{ $t("orderChess") }}</el-menu-item
        >
        <el-menu-item
          index="/orderEsport"
          v-if="onOffBtn.game.esport === 2 && !isGuest"
          :class="curLeftMenu === '/orderEsport' ? 'cur-active' : ''"
          >{{ $t("orderEsport") }}</el-menu-item
        >
        <el-menu-item
          index="/orderFishing"
          v-if="onOffBtn.game.fishing === 2 && !isGuest"
          :class="curLeftMenu === '/orderFishing' ? 'cur-active' : ''"
          >{{ $t("orderFishing") }}</el-menu-item
        >
        <el-menu-item
          index="/orderLottery"
          v-if="onOffBtn.game.lottery === 2"
          :class="curLeftMenu === '/orderLottery' ? 'cur-active' : ''"
          >{{ $t("lotBetAmount") }}</el-menu-item
        >
      </el-submenu>
      <!-- 报表管理 -->
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-document-copy"></i>
          <span :title="$t('recordMenu')">{{ $t("recordMenu") }}</span>
        </template>

        <el-menu-item
          index="/recordCharge"
          :class="curLeftMenu === '/recordCharge' ? 'cur-active' : ''"
          v-if="!isGuest"
          >{{ $t("recordCharge") }}</el-menu-item
        >
        <el-menu-item
          index="/recordChange"
          :class="curLeftMenu === '/recordChange' ? 'cur-active' : ''"
          >{{ $t("recordChange") }}</el-menu-item
        >
        <el-menu-item
          index="/recordUser"
          :class="curLeftMenu === '/recordUser' ? 'cur-active' : ''"
          >{{ $t("recordUser") }}</el-menu-item
        >
        <el-menu-item
          index="/recordTeam"
          v-if="onOffBtn.isProxy"
          :class="curLeftMenu === '/recordTeam' ? 'cur-active' : ''"
          >{{ $t("recordTeam") }}</el-menu-item
        >
      </el-submenu>
      <!-- 账户管理 -->
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-menu"></i>
          <span :title="$t('accountMeun')">{{ $t("accountMeun") }}</span>
        </template>
        <el-menu-item
          index="/index"
          :class="curLeftMenu === '/index' ? 'cur-active' : ''"
          >{{ $t("viewUser") }}</el-menu-item
        >
        <el-menu-item
          index="/securityCenter"
          :class="curLeftMenu === '/securityCenter' ? 'cur-active' : ''"
          >{{ $t("securityCenter") }}</el-menu-item
        >
        <el-menu-item
          index="/moneyChange"
          :class="curLeftMenu === '/moneyChange' ? 'cur-active' : ''"
           v-if="!isGuest"
          >{{ $t("moneyChange") }}</el-menu-item
        >
        <el-menu-item
          index="/moneyChangeHis"
          :class="curLeftMenu === '/moneyChangeHis' ? 'cur-active' : ''"
          >{{ $t("moneyChangeHis") }}</el-menu-item
        >
        <el-menu-item
          index="/bankIndex"
          :class="curLeftMenu === '/bankIndex' ? 'cur-active' : ''"
          :title="$t('bankIndex')"
          v-if="!isGuest"
          >{{ configJsFlag("bankIndex") }}</el-menu-item
        >
        <el-menu-item
          index="/usdtIndex"
          :class="curLeftMenu === '/usdtIndex' ? 'cur-active' : ''"
          v-if="!isGuest"
          >{{ $t("usdtIndex") }}</el-menu-item
        >
        <el-menu-item
          index="/scoreExchange"
          v-if="onOffBtn.isExchange"
          :class="curLeftMenu === '/scoreExchange' ? 'cur-active' : ''"
          
          >{{ $t("scoreExchange") }}</el-menu-item
        >
        <!-- <el-menu-item index="/scoreExchangeHis">积分记录</el-menu-item> -->
        <el-menu-item
          index="/orderBetHis"
          :class="curLeftMenu === '/orderBetHis' ? 'cur-active' : ''"
          >{{ $t("orderBetHis") }}</el-menu-item
        >
      </el-submenu>
      <!-- 代理管理 -->
      <el-submenu index="4" v-if="!isGuest">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span :title="$t('agentMenu')">{{ $t("agentMenu") }}</span>
        </template>
        <el-menu-item
          index="/myRecommend"
          :class="curLeftMenu === '/myRecommend' ? 'cur-active' : ''"
          >{{ $t("myRecommend") }}</el-menu-item
        >
        <el-menu-item
          index="/viewTeam"
          :class="curLeftMenu === '/viewTeam' ? 'cur-active' : ''"
          >{{ $t("viewTeam") }}</el-menu-item
        >
        <el-menu-item
          index="/userList"
          :class="curLeftMenu === '/userList' ? 'cur-active' : ''"
          >{{ $t("userList") }}</el-menu-item
        >
        <el-menu-item
          index="/promotionUrl"
          :class="curLeftMenu === '/promotionUrl' ? 'cur-active' : ''"
          v-if="onOffBtn.canBePromo"
          >{{ $t("promotionUrl") }}</el-menu-item
        >
        <el-menu-item
          index="/regManage"
          :class="curLeftMenu === '/regManage' ? 'cur-active' : ''"
          v-if="onOffBtn.canBePromo"
          >{{ $t("regManage") }}</el-menu-item
        >
        <el-menu-item
          index="/recommend"
          :class="curLeftMenu === '/recommend' ? 'cur-active' : ''"
          v-if="onOffBtn.canBeRecommend"
          >{{ $t("recommend") }}</el-menu-item
        >
        <el-menu-item
          index="/recommendAgent"
          :class="curLeftMenu === '/recommendAgent' ? 'cur-active' : ''"
          v-if="onOffBtn.canBeRecommend"
          >{{ $t("recommendAgent") }}</el-menu-item
        >
      </el-submenu>
      <!-- 短信公告 -->
      <el-submenu index="5">
        <template slot="title">
          <i class="el-icon-s-comment"></i>
          <span :title="$t('msgMenu')">{{ $t("msgMenu") }}</span>
        </template>
        <el-menu-item
          index="/message"
          :class="curLeftMenu === '/message' ? 'cur-active' : ''"
          >{{ $t("message") }}</el-menu-item
        >
        <el-menu-item
          index="/notice"
          :class="curLeftMenu === '/notice' ? 'cur-active' : ''"
          >{{ $t("notice") }}</el-menu-item
        >
        <el-menu-item
          index="/advice"
          v-if="onOffBtn.onOffStationAdvice"
          :class="curLeftMenu === '/advice' ? 'cur-active' : ''"
          >{{ $t("advice") }}</el-menu-item
        >
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      defaultActive: "", //默认选中子菜单
      openeds: [], //默认选中菜单栏
      // curLeftMenu: '',//当前菜单栏
      isFrist: true,
    };
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn", "curLeftMenu"]),
    //当前站点前端配置信息
    configJs() {
      return this.$store.state.configJs;
    },
    //是否是试玩账号
    isGuest() {
      //   用户信息 type 150 为 前台试玩账号 type 160 为 后台试玩账号
      return this.userInfo.type === 150 || this.userInfo.type === 160;
    },
  },
  mounted() {
    this.$bus.$on("openBigMenu", (data) => {
      this.$nextTick(() => {
        if (this.$refs.leftMenu) this.$refs.leftMenu.open(data);
      });
    });
  },
  methods: {
    refreshInfo() {
      this.$axiosPack
        .post("/userCenter/userAllInfo.do", {
          load: [true, 200, this.$t("loading")],
        })
        .then((res) => {
          if (res) {
            // 将信息存储到 store，并且存入 sessionStorage
            this.$store.dispatch("getUserInfo", res.data);
            sessionStorage.userInfo = JSON.stringify(res.data);

            // 未登录
            if (!res.data.login) {
              this.$alert(this.$t("indexText") + "？", this.$t("noLogin"), {
                confirmButtonText: this.$t("confirm"),
                showClose: false,
                callback: (action) => {
                  // location.href = '/index.do'
                  top.location.href = "/index.do";
                },
              });
            }
          }
        });
    },
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
  },
  watch: {
    $route(to, from) {},
  },
};
</script>

<style lang="scss">
.el-submenu__title {
  background: #0f1923 !important;
}
.side-left {
  width: 220px;
  padding: 0 8px;
  background: #0f1923;
  color: #fff;
  font-size: 14px;
  flex-shrink: 0;
  .user-info {
    .item {
      line-height: 31px;
      margin-bottom: 12px;
      display: flex;
      align-items: center;
      // background: url(../../assets/images/common/base-ico2.png) no-repeat;
      .icon {
        width: 43px;
        height: 43px;
        position: relative;
        z-index: 11;
      }
      .info {
        min-width: 142px;
        border: 2px solid #0ec504;
        border-top-right-radius: 8px;
        border-bottom-right-radius: 8px;
        display: flex;
        align-items: center;
        height: 28px;
        line-height: 28px;
        margin-left: -18px;
        padding: 0 2px 0 25px;
        .level-icon {
          width: 30px;
          margin-right: 10px;
        }
      }
      .stc_c {
        max-width: 100px;
        margin-right: 5px;
        text-align: left;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      &.balance {
        font-size: 12px;
        .stc_c {
          max-width: 65px;
        }
      }
      .refresh {
        height: 21px;
        cursor: pointer;
      }
    }
    .acc-links {
      margin: 10px 0 3px;
      display: flex;
      flex-wrap: wrap;
      font-size: 15px;
      .btn-navacc {
        height: 45px;
        display: flex;
        align-items: center;
        margin-right: 14px;
        margin-left: 10px;
        cursor: pointer;
        color: #fff;
      }
      .iconfont {
        color: #0ec504;
        font-size: 30px;
        margin-right: 5px;
      }
      // .navacc {
      //   background: url(../../assets/images/common/user-btn.png) no-repeat;
      //   height: 45px;
      //   width: 45px;
      // }
      // .btn-navacc-deposit {
      //   background-position: -2px 4px;
      // }
      // .btn-navacc-withdraw {
      //   background-position: -82px 6px;
      // }
    }
  }
  .leftMenu {
    .el-menu-item.is-active {
      color: #fff !important;
    }
    .el-menu-item.cur-active {
      color: #0ec504 !important;
    }
    .el-menu-item {
      padding: 0;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      background: #0f1923 !important;
    }
  }
}
.side-left > ul > li > div > span {
  display: inline-block;
  width: 170px;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
