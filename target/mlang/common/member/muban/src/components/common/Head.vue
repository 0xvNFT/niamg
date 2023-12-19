<template>
  <div class="header">
    <!-- 左边菜单、logo -->
    <div class="header-left">
      <i class="el-icon-s-fold" :class="leftMenuShow ? '' : 'flip'" @click="toggleLeftMenu"></i>
      <!-- 网站logo -->
      <a href="#/index" v-if="onOffBtn.logo">
        <img :src="onOffBtn.logo" alt="" />
      </a>
    </div>
    <!-- 未登录时 登录、注册按钮 -->
    <div class="header-right" v-if="!userInfo.login">
      <div class="loginBtn rightBtn" @click="registerShow(0)">
        {{ $t("login") }}
      </div>
      <div class="signupBtn rightBtn" @click="registerShow(1)">
        {{ $t("register") }}
      </div>
      <div class="guestBtn rightBtn" @click="guest">
        {{ $t("freeGuest") }}
      </div>
    </div>
    <!-- 已登录用户信息等 -->
    <div class="header-right" v-else>
      <!-- 用户余额 -->
      <div class="user_balance_bg">
        <div class="balance">
          <img src="@/assets/images/index/refresh_icon.png"
          class="icon-refresh" :class="{refreshing: spinning}" @click="refreshInfo" alt="refresh_icon">
          <span>{{ onOffBtn.currency || "" }} {{ userInfo.money | amount }}</span>
        </div>
        <!-- 存款按钮 -->
        <a v-banned href="#/banking" class="chargeBtn">
          <i class="el-icon-wallet"></i>{{ $t("depositText") }}
        </a>
      </div>
      <!-- 用户头像 -->
      <el-popover placement="bottom" width="400" trigger="click" popper-class="userHead-popover" :visible-arrow="false"
        v-model="headPopover">
        <!-- 用户头像点击弹窗 -->
        <div class="userHead-list">
          <!-- 用户信息 -->
          <!-- <div class="user_userinfo">
            用户头像
            <div class="user_head">
              <img :src="onOffBtn.avatarUrl" alt="" v-if="onOffBtn.avatarUrl" />
              <img src="../../assets/images/avatar.png" alt="" v-else />
            </div>
            用户名
            <div class="user_name" id="copyName">{{ userInfo.username }}</div>
            复制按钮
            <div class="user_copy" @click="copyName">
              <i class="el-icon-copy-document"></i>
            </div>
          </div> -->
          <div class="user_userDj">
            <!-- 用户信息 -->
            <div class="user_userinfo">
              <!-- 用户头像 -->
              <div class="user_head">
                <img :src="onOffBtn.avatarUrl" alt="" v-if="onOffBtn.avatarUrl" />
                <img src="../../assets/images/avatar.png" alt="" v-else />
              </div>
              <!-- 用户名 -->
              <div class="user_name" id="copyName">{{ userInfo.username }}</div>
              <!-- 复制按钮 -->
              <div class="user_copy" @click="copyName('copyName')">
                <i class="el-icon-copy-document"></i>
              </div>
            </div>
            <div class="user_bonus">
              <!-- 等级图标 -->
              <div class="user_userDj_logo">
                <img :src="userInfo.userDegreeeIcon" alt="" v-if="userInfo.userDegreeeIcon" />
                <img src="../../assets/images/grade.png" alt="" v-else />
                <h2>{{ userInfo.curDegreeName }}</h2>
              </div>
              <div class="user_userDj_details">
                <div class="user_djt" v-if="userInfo.degreeShow">
                  <div class="item">
                    <div class="info">
                      <div style="margin-left: 5px">
                        {{ userInfo.newDegreeName || $t("noSet") }}
                      </div>
                      <div>
                        {{
                          $publicJs.retainNumFun(
                            Number(userInfo.curDegreeDepositMoney),
                            2
                          ) || 0.0
                        }}/{{
                          $publicJs.retainNumFun(
                            userInfo.newDegreeDepositMoney * 1,
                            2
                          ) || 0.0
                        }}
                      </div>
                    </div>
                    <div class="percentage">
                      <el-progress :percentage="percentage" :text-inside="true" :show-text="false"
                        :stroke-width="10"></el-progress>
                      <span>{{ percentage }}%</span>
                    </div>
                    <p class="needPay">
                      {{ $t("needPay") }}:
                      {{
                        $publicJs.retainNumFun(
                          Number(userInfo.newDegreeDepositMoney),
                          2
                        ) || 0.0
                      }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 菜单 -->
          <div class="menu_pane">
            <el-collapse v-model="activeMenu" accordion>
              <!-- 账户管理 -->
              <el-collapse-item name="1">
                <template slot="title">
                  <i class="el-icon-user menu-i"></i>{{ $t("accountMeun") }}
                </template>
                <div class="cur-list">
                  <!-- 安全中心 -->
                  <a href="#/securityCenter">{{ $t("securityCenter") }}</a>
                  <!-- 额度转换 -->
                  <a href="#/moneyChange" v-banned>{{ $t("moneyChange") }}</a>
                  <!-- 银行卡管理 -->
                  <a href="#/bankIndex" v-banned>{{ $t("bankIndex") }}</a>
                  <!-- USDT管理 -->
                  <a href="#/usdtIndex" v-banned>{{ $t("usdtIndex") }}</a>
                  <!-- 积分兑换 -->
                  <a href="#/scoreExchange" v-if="onOffBtn.isExchange">{{
                    $t("scoreExChange")
                  }}</a>
                  <!-- 打码量记录 -->
                  <a href="#/orderBetHis">{{ $t("orderBetHis") }}</a>
                </div>
              </el-collapse-item>
              <!-- 代理管理 -->
              <el-collapse-item name="2" v-banned:display>
                <template slot="title">
                  <img class="img-icon" src="@/assets/images/index/proxy_icon.png" alt="" />{{ $t("agentMenu") }}
                </template>
                <div class="cur-list">
                  <!-- 我的分享 -->
                  <a href="#/myRecommend">{{ $t("myRecommend") }}</a>
                  <!-- 团队总览 -->
                  <a href="#/viewTeam" v-if="afterLoginOnOffBtn.isProxy">{{
                    $t("viewTeam")
                  }}</a>
                  <!-- 用户列表 -->
                  <a href="#/userList">{{ $t("userList") }}</a>
                  <!-- 推荐总览 -->
                  <a href="#/recommend" v-if="afterLoginOnOffBtn.canBeRecommend">{{ $t("recommend") }}</a>
                  <!-- 我的推荐 -->
                  <a href="#/recommendAgent" v-if="afterLoginOnOffBtn.canBeRecommend">{{ $t("recommendAgent") }}</a>
                  <!-- 推广注册 -->
                  <a href="#/promotionUrl" v-if="afterLoginOnOffBtn.canBePromo">{{ $t("promotionUrl") }}</a>
                  <!-- 注册管理 -->
                  <a href="#/regManage" v-if="afterLoginOnOffBtn.canBePromo">{{
                    $t("regManage")
                  }}</a>
                </div>
              </el-collapse-item>
              <!-- 投注记录 -->
              <el-collapse-item name="3" v-if="onOffBtn.game">
                <template slot="title">
                  <i class="el-icon-notebook-2 menu-i"></i>{{ $t("orderMenu") }}
                </template>
                <div class="cur-list">
                  <!-- 体育注单 -->
                  <a v-if="onOffBtn.game.sport === 2" href="#/orderSport" v-banned>{{ $t("orderSport") }}</a>
                  <!-- 电子注单 -->
                  <a v-if="onOffBtn.game.egame === 2" href="#/orderEgame" v-banned>{{ $t("orderEgame") }}</a>
                  <!-- PT电子注单 -->
                  <a v-if="onOffBtn.platforms.pt" href="#/orderEgamePt" v-banned>{{ $t("orderEgamePt") }}</a>
                  <!-- 真人娱乐注单 -->
                  <a v-if="onOffBtn.game.live === 2" href="#/orderLive" v-banned>{{ $t("orderLive") }}</a>
                  <!-- 棋牌注单 -->
                  <a v-if="onOffBtn.game.chess === 2" href="#/orderChess" v-banned>{{ $t("orderChess") }}</a>
                  <!-- 电竞注单 -->
                  <a v-if="onOffBtn.game.esport === 2" href="#/orderEsport" v-banned>{{ $t("orderEsport") }}</a>
                  <!-- 捕鱼注单 -->
                  <a v-if="onOffBtn.game.fishing === 2" href="#/orderFishing" v-banned>{{ $t("orderFishing") }}</a>
                  <!-- 彩票注单 -->
                  <a href="#/orderLottery" v-if="onOffBtn.game.lottery === 2">{{
                    $t("orderLottery")
                  }}</a>
                </div>
              </el-collapse-item>
              <!-- 报表管理 -->
              <el-collapse-item name="4">
                <template slot="title">
                  <img class="img-icon" src="@/assets/images/index/report_icon.png" alt="" />{{ $t("recordMenu") }}
                </template>
                <div class="cur-list">
                  <!-- 存取记录 -->
                  <a href="#/recordCharge" v-banned>{{ $t("recordCharge") }}</a>
                  <!--账变报表 -->
                  <a href="#/recordChange" v-banned>{{ $t("recordChange") }}</a>
                  <!--个人报表 -->
                  <a href="#/recordUser">{{ $t("recordUser") }}</a>
                  <!--团队报表 -->
                  <a href="#/recordTeam" v-banned:display v-if="!afterLoginOnOffBtn.isProxy">{{ $t("recordTeam") }}</a>
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
          <!-- 退出登录 -->
          <div class="log_out_div" @click="logout">
            <i class="el-icon-coordinate"></i>{{ $t("logOut") }}
          </div>
          <div class="bottom-version">{{ copyright }}</div>
        </div>

        <!-- 用户头像 -->
        <div class="header-right-userHead" slot="reference">
          <img :src="onOffBtn.avatarUrl" alt="" v-if="onOffBtn.avatarUrl" />
          <img src="../../assets/images/avatar.png" alt="" v-else />
          <i class="el-icon-arrow-down" :class="headPopover ? 'open' : ''"></i>
        </div>
      </el-popover>
      <!-- 站内信 -->
      <div class="message" @click="msgDrawer = true">
        <el-badge :max="99" class="item" :is-dot="hasUnRead">
          <i class="el-icon-message"></i>
        </el-badge>
      </div>
    </div>

    <!-- 站内信弹窗 -->
    <el-drawer :title="$t('message')" :visible.sync="msgDrawer" size="30%" class="msgDrawer">
      <div class="msg-list" v-if="msgData && msgData.length">
        <div class="list" v-for="(val, key) in msgData" :key="key">
          <div class="list-card">
            <!-- status 为1 未读，2 已读 -->
            <el-badge :is-dot="val.status === 1" class="list-card-dot">
              <!-- 标题 -->
              <h3 class="card-title" @click="readMsg(val)">{{ val.title }}</h3>
            </el-badge>
            <!-- 内容 -->
            <div class="card-content" :class="val.show ? 'all' : ''" v-html="val.content"></div>
            <!-- 操作按钮-显示所有 -->
            <div class="showAllbtn" v-if="!val.show" @click="readMsg(val)">
              {{ $t("showAll") }}
            </div>
            <!-- 操作按钮-隐藏 -->
            <div class="showAllbtn" v-else @click="readMsg(val)">
              {{ $t("hideText") }}
            </div>
          </div>
          <!-- 创建时间 -->
          <div class="list-date">{{ val.createTime }}</div>
        </div>
      </div>
      <template v-else>
        <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
      </template>
    </el-drawer>
  </div>
</template>

<script>
import { mapState } from "vuex";
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      msgDrawer: false, //是否显示站内信弹窗
      pageNumber: 1, //站内信页码
      msgData: [], //站内信数据
      hasUnRead: false, //是否有未读站内信
      activeMenu: "", //显示的菜单
      loadMessaged: false, //登录成功时是否加载了站内信接口
      copyright: copyright, //版本
      headPopover: false, //头像点击菜单列表弹窗是否显示
      spinning: false,  // 是否转圈
    };
  },
  props: {
    leftMenuShow: true,
  },
  computed: {
    ...mapState(["onOffBtn", "afterLoginOnOffBtn"]),
    userInfo() {
      if (!this.loadMessaged && this.$store.state.userInfo.login) {
        // 请求站内信接口
        this.messageList();
      }
      // 用户信息 type 150 为 前台试玩账号 type 160 为 后台试玩账号
      return this.$store.state.userInfo;
    },
    //进度条占百分比计算
    percentage() {
      let degree =
        (this.userInfo.curDegreeDepositMoney /
          this.userInfo.newDegreeDepositMoney) *
        100;
      if (degree >= 0 && degree <= 100) return Number(degree.toLocaleString());
      return 0;
    },
  },
  components: {},
  created() { },
  mounted() { },
  methods: {
    // 左边菜单是否显示切换
    toggleLeftMenu() {
      this.$emit("update:leftMenuShow", !this.leftMenuShow);
    },
    // 显示登录注册弹窗
    registerShow(val) {
      let selectVal = { select: val, show: true };
      this.$bus.$emit("registerShow", selectVal);
    },
    //试玩注册
    guest() {
      this.$API.guestRegister().then((res) => {
        if (res.success) {
          this.$message({
            message: res.msg,
            type: "success",
          });
          // 刷新用户信息事件
          this.$bus.$emit("refreshUserInfo");
          // 关闭用户登录/ 注册弹出框
          this.$bus.$emit("registerShow", { select: 0, show: false });
          this.$bus.$emit("refreshTags"); // 试玩时页面没有刷新所以要强制更新一些特殊元素
        }
      });
    },
    // 刷新用户信息事件
    refreshInfo() {
      this.spinning = true;
      this.$bus.$emit("refreshUserInfo");
      setTimeout(() => {
        this.spinning = false;
      }, 2000);
    },
    // 请求站内信接口
    messageList() {
      let params = { pageNumber: this.pageNumber };
      this.hasUnRead = false;
      this.$API.messageList(params).then((res) => {
        if (res) {
          this.loadMessaged = true;
          if (res.rows && res.rows.length) {
            for (let i = 0; i < res.rows.length; i++) {
              res.rows[i].createTime = DATE.dateChange(res.rows[i].createTime);
              res.rows[i].show = false;
              if (res.rows[i].status === 1) this.hasUnRead = true;
            }
            this.msgData = res.rows;
          }
        }
      });
    },
    // 站内信已读
    readMsg(val) {
      val.show = !val.show;

      if (val.status === 1) {
        let params = { sid: val.id, rid: val.receiveMessageId };
        this.$API.readMessage(params).then((res) => {
          if (res) {
            val.status = 2;
          }
        });
      }
    },
    // 复制名字
    copyName(val) {
      this.$publicJs.copyFun(val);
      this.$message.success(this.$t("successCopy"));
    },
    // 退出登录
    logout() {
      this.$API.logout().then((res) => {
        if (res) {
          sessionStorage.userInfo = "";
          this.$store.dispatch("getUserInfo", "");
          this.$message.success(this.$t("successText"));
          setTimeout(() => {
            location.reload();
          }, 500);
        }
      });
    },
  },
  watch: {
    $route(to, from) {
      this.headPopover = false;
    },
    msgDrawer() {
      // 站内信不管开启关闭都请求一次站内信接口
      // 请求站内信接口
      this.messageList();
    },
  },
};
</script>

<style lang="scss">
.header {
  box-sizing: border-box;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  height: auto;
  padding: 13px 30px;
  width: 100%;
  background: $black;

  .header-left {
    display: flex;
    align-items: center;

    img {
      height: 38px;
      display: block;
    }

    .el-icon-s-fold {
      margin-right: 6px;
      color: $borderColorJoker;
      font-size: 32px;
      cursor: pointer;
      transition: all 0.5s ease;

      &:hover {
        color: #fff;
      }
    }

    .flip {
      transform: scaleX(-1);
      transition: all 0.5s ease;
    }
  }

  .header-right {
    align-items: center;
    display: flex;

    .rightBtn {
      border-radius: 10px;
      color: #fff;
      cursor: pointer;
      font-size: 14px;
      font-weight: 600;
      padding: 8px 12px;
      transition: all 0.1s ease;

      &:hover {
        opacity: 0.8;
      }
    }

    .guestBtn {
      background-color: #43d152;
    }

    .loginBtn {
      background-color: $blue;
      margin-right: 10px;
    }

    .signupBtn {
      background-color: #dd234b;
      margin-right: 10px;
    }

    .user_balance_bg {
      font-size: 14px;
      font-weight: 600;
      background: $bgColor;
      border-radius: 0.5rem;
      display: flex;
      margin-right: 8px;

      .balance {
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 20px 0 10px;
      }

      .chargeBtn {
        display: flex;
        align-items: center;
        background: linear-gradient(180deg, #ff5075, #ed1d49);
        border: 1px solid #ed1d49;
        color: #fff;
        border-radius: 0.5rem;
        font-weight: 700;
        height: 40px;
        padding: 1px 6px;
        transition: all 0.3s ease;

        i {
          padding: 0 4px;
          font-size: 16px;
        }

        &:hover {
          opacity: 0.8;
        }
      }
    }

    .header-right-userHead {
      background: $bgColor;
      border-radius: 0.75rem;
      display: flex;
      align-items: center;
      font-family: Montserrat;
      padding: 5px 10px;
      cursor: pointer;

      img {
        width: 26px;
        height: 26px;
        margin-right: 6px;
      }

      i {
        font-weight: bold;
        transition: all 0.3s ease;

        &.open {
          transform: rotate(180deg);
        }
      }
    }

    .message {
      cursor: pointer;

      i {
        font-size: 28px;
        margin-left: 10px;
        color: $blue;
      }

      .el-badge__content.is-fixed {
        top: 6px;
        right: 7px;
      }
    }
  }

  .msgDrawer {
    .el-drawer {
      background: $black;
      min-width: 320px;
      max-width: 450px;
    }

    .msg-list {
      padding: 0 1rem;

      .list {
        margin-bottom: 1rem;

        .list-card {
          background-color: #4b5461;
          border-radius: 0.5rem;
          padding: 1rem;
          display: block;
          overflow: hidden;
          text-overflow: ellipsis;

          .card-title {
            cursor: pointer;
          }

          .list-card-dot {
            .el-badge__content.is-fixed.is-dot {
              right: 3px;
              top: 7px;
            }
          }

          .card-content {
            color: #93acd3;
            font-size: 12px;
            margin-bottom: 12px;
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;
            &.all {
              white-space: inherit;
            }
          }

          .showAllbtn {
            text-align: right;
            font-size: 12px;
            cursor: pointer;
          }
        }

        .list-date {
          color: #737376;
          padding-left: 0.5rem;
          font-size: 12px;
        }
      }
    }
  }
}

.userHead-popover {
  background: $black1;
  border-color: $black1;
  padding: 0;

  .userHead-list {
    .user_userDj {
      background: linear-gradient(180deg, #151c26, #0d499c 51%, #122033);
      border-radius: 0.5rem;
      margin: 0 auto;
      min-height: 126px;
      padding: 6px;
      width: 100%;

      .user_userinfo {
        background: #10274d;
        border-radius: 0.75rem;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px 15px;
        margin: 0 12px;

        .user_head {
          img {
            width: 40px;
            height: 40px;
            display: block;
          }
        }

        .user_name {
          color: #fff;
          font-size: 14px;
          font-weight: 600;
        }

        .user_copy {
          background: #1a55ef;
          border-radius: 0.25rem;
          cursor: pointer;
          display: flex;
          align-items: center;
          height: 28px;
          justify-content: center;
          min-width: 28px;
          width: 26px;
          color: #fff;

          i {
            font-size: 22px;
          }
        }
      }

      .user_bonus {
        display: flex;
        padding: 5px;

        .user_userDj_logo {
          margin-right: 5px;

          img {
            width: 80px;
            height: 80px;
            margin-top: 10px;
          }

          h2 {
            background: linear-gradient(180deg, #f39d42, #fed791);
            -webkit-background-clip: text;
            color: transparent;
            display: block;
            font-family: Montserrat;
            font-size: 20px;
            font-weight: 800;
            line-height: 1.3;
            text-align: center;
            text-transform: uppercase;
          }
        }

        .user_userDj_details {
          color: #fff;

          .userId {
            margin-top: 15px;
            text-align: center;
            font-weight: 600;
          }

          .user_djt {
            padding: 15px 10px 10px;
            box-sizing: border-box;
            // border: 1px solid $borderColorJoker;
            margin-bottom: 10px;
            transition: all 0.2s;
            font-size: 12px;

            &:hover {
              box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12),
                0 0 6px rgba(0, 0, 0, 0.04);
            }

            .el-progress--line {
              width: 230px;
            }

            .tip {
              margin-left: 5px;
              margin-bottom: 5px;
              text-align: right;
              font-weight: 600;
              font-size: 12px;
              color: #fff;
              padding: 0 25px;
            }

            .percentage {
              display: flex;
              align-items: center;

              span {
                margin-left: 15px;
              }
            }

            .needPay {
              text-align: center;
              margin-top: 5px;
            }

            .info {
              display: flex;
              justify-content: space-between;
              // font-size: 14px;
              margin: 7px 0;
            }
          }
        }
      }
    }

    .menu_pane {
      font-size: 12px;
      font-weight: 700;
      padding: 14px 12px;
      cursor: pointer;

      // display: grid;
      // grid-template-columns: repeat(2, 1fr);
      // grid-gap: 6px;
      // grid-row-gap: 8px;
      .el-collapse,
      .el-collapse-item__header,
      .el-collapse-item__wrap {
        border: none;
      }

      .el-collapse-item {
        margin-bottom: 5px;
      }

      .el-collapse-item__header {
        align-items: center;
        background: linear-gradient(#0045aa, rgba(0, 69, 170, 0));
        border-radius: 0.3rem;
        color: #adb6c4;
        cursor: pointer;
        display: grid;
        grid-template-columns: 48px 1fr auto;
        // min-height: 50px;
        // width: 100%;
        opacity: 0.8;

        // transition: all 0.3s ease;
        &:hover {
          opacity: 1;
        }

        i {
          font-size: 24px;
          color: #f8b133;
        }

        .menu-i {
          padding: 0 8px 0 12px;
        }
      }

      .el-collapse-item__wrap {
        background: #202530;
        border-bottom-left-radius: 0.3rem;
        border-bottom-right-radius: 0.3rem;

        .el-collapse-item__content {
          padding-bottom: 0;

          .cur-list {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            grid-gap: 6px;
            grid-row-gap: 8px;
            padding: 5px 10px;

            a {
              line-height: 1.5;
              padding: 0 5px;
              color: #adb6c4;
              opacity: 0.8;
              transition: all 0.3s ease;
              text-align: left;

              &:hover {
                opacity: 1;
              }
            }
          }
        }
      }

      .img-icon {
        margin: 0 auto;
      }
    }

    .log_out_div {
      background: #202530;
      color: #86898d;
      border-radius: 10px 10px 10px 10px;
      font-size: 14px;
      font-weight: bold;
      margin: 0 12px 12px;
      padding: 12px 0;
      text-align: center;
      cursor: pointer;
      opacity: 0.8;
      transition: all 0.3s ease;

      &:hover {
        opacity: 1;
      }
    }

    .bottom-version {
      padding-bottom: 1rem;
      text-align: center;
      color: #3f526e;
      font-size: 10px;
      font-weight: 700;
    }
  }
}
</style>
