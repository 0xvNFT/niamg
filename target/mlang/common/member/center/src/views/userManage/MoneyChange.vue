<template>
  <div class="moneyChange-page">
    <el-card shadow="always" class="change-head">
      <p class="base-info">
        {{ $t("welcomeLogin") }}，
        <span class="blueText" style="margin-left:0">{{
          userInfo.username
        }}</span>
        <router-link to="/securityCenter" class="blueText">{{
          $t("pwdEdit")
        }}</router-link>
        <router-link to="/message" class="blueText">{{
          $t("message")
        }}</router-link>
        <router-link to="/notice" class="blueText">{{
          $t("notice")
        }}</router-link>
        <el-button type="primary" size="mini" @click="goPay">{{
          $t("imPay")
        }}</el-button>
        <el-button type="danger" size="mini" @click="goAgency">{{
          $t("applyAgency")
        }}</el-button>
      </p>
      <p class="base-info">{{ $t("lastLogin") }}：{{ data.lastLoginTime }}</p>
    </el-card>
    <el-card shadow="always" class="change-box">
      <div class="title">
        {{ $t("from") }}
        <el-select v-model="outcash" :placeholder="$t('pleaseSelect')">
          <el-option
            v-for="item in fromArr"
            :key="item.platform"
            :label="item.name"
            :value="item.platform"
          >
          </el-option>
        </el-select>
        {{ $t("shiftTo") }}
        <el-select v-model="incash" :placeholder="$t('pleaseSelect')">
          <el-option
            v-for="item in toArr"
            :key="item.platform"
            :label="item.name"
            :value="item.platform"
          >
          </el-option>
        </el-select>
        {{ $t("outMoney") }}
        <el-input
          v-model="outCashInput"
          type="number"
          class="outInput"
          clearable
        ></el-input>
        <el-button
          type="danger"
          style="margin: 0 15px"
          @click="thirdRealTransMoney"
          >{{ $t("surePut") }}</el-button
        >
        <p class="hint" v-if="onOffBtn.thirdAutoExchange">
          {{ $t("moneyChangeHint") }}
        </p>
      </div>
      <div class="cont">
        <div class="left">
          <p>{{ $t("money") }}</p>
          <p class="num">{{ userInfo.money ||0}}</p>
          <p style="margin-top: 20px">{{ $t("score") }}</p>
          <p class="num">{{ data.score }}</p>
        </div>
        <div class="right" v-if="data.third">
          <div class="item" v-for="(val,i) in data.third">
            {{ $t("limitMoney") }}&nbsp;{{ val.name }}
            <span class="num">{{ val.money }}</span
            ><span class="refresh" @click="getBalance(val.platform,i)">{{
              $t("refresh")
            }}</span>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      data: {
        score: "",
        third: [],
      }, //额度转换数据
      fromArr: [], //从
      toArr: [], //去
      incash: "", //转入
      outcash: "", //转出
      outCashInput: "", //转换的值
    };
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn", "gameOnOff"]),
  },
  components: {},
  mounted() {
    // 获取三方额度数据
    this.exchangePageInfo();

    // let _this = this
    // var timer = setInterval(() => {
    //   if (_this.gameOnOff) {
    //     clearInterval(timer);
    //     _this.getGameList();
    //   }
    // }, 300);
    // this.$once('hook:beforeDestroy', function () {
    //   clearInterval(timer);
    // });
  },
  methods: {
    // 立即充值
    goPay() {
      this.$router.push("/recharge");
    },
    // 申请代理
    goAgency() {
      top.location.href = "/help.do?code=4";
    },
    // 根据开关判断游戏列表
    getGameList(arr) {
      let sys = [{ platform: "sys", name: this.$t("sysMoney") }];
      this.fromArr = [...sys, ...arr];
      this.toArr = [...arr, ...sys];
    },

    // 获取所有三方额度数据
    exchangePageInfo(update) {
      let load = null;
      if (!update) load = [true, 200, null];

      this.$axiosPack
        .post("/userCenter/third/exchangePageInfo.do", { load: load })
        .then((res) => {
          if (res) {
            this.data.score = res.data.score;
            this.data.third = res.data.third;
            //所有数据单独请求一次接口
            for (let i = 0; i < res.data.third.length; i++) {
              const platform = res.data.third[i].platform;
              this.getBalance(platform, i);
            }
            if (!update) this.getGameList(res.data.third);
            // this.$store.dispatch("getGameOnOff", res.data.third);
          }
        });
    },

    // 获取单个三方额度数据
    getBalance(platform, i) {
      //改变提交格式
      let params = new URLSearchParams();
      params.append("platform", platform);

      this.$axiosPack
        .post("/thirdTrans/getBalance.do", { params: params })
        .then((res) => {
          if (res) {
            // 转换之后更新系统余额
            // let user = this.userInfo;
            // if (user) user.money = res.data.sysMoney;

            // 将信息存储到 store，并且存入 sessionStorage
            // this.$store.dispatch("getUserInfo", user);
            // sessionStorage.userInfo = JSON.stringify(user);
            //金额赋值
            this.$set(this.data.third[i], "money", res.data.money);
            this.$bus.$emit("refreshUserInfo");
          }
        });
    },
    // 转换额度
    thirdRealTransMoney() {
      //改变提交格式
      let params = new URLSearchParams();
      params.append("changeTo", this.incash);
      params.append("changeFrom", this.outcash);
      params.append("money", this.outCashInput);

      this.$axiosPack
        .post("/thirdTrans/thirdRealTransMoney.do", {
          params: params,
          load: [true, 200, null],
        })
        .then((res) => {
          if (res.data.success) {
            this.$notify({
              title: "success",
              message: res.data.msg,
              type: "success",
            });

            let platform = "";
            if (this.incash === "sys") platform = this.outcash;
            else platform = this.incash;

            // let platform = ''
            // for (let i = 0; i < this.gameOnOff.length; i++) {
            //   if (this.gameOnOff[i].value === value) {
            //     platform = this.gameOnOff[i].platform
            //     break;
            //   }
            // }

            this.getBalance(platform);
            this.exchangePageInfo(true);
          } else {
            this.$notify.error({
              title: "error",
              message: res.data.msg,
            });
          }
        });
    },
  },
  watch: {
    outCashInput() {
      // 转换的额度 bbin 必须是整数，其他可以2位小数
      if (this.incash === "BBIN" || this.outcash === "BBIN") {
        this.outCashInput = this.retainNumFun(this.outCashInput, "no");
      } else {
        this.outCashInput = this.retainNumFun(this.outCashInput);
      }
    },
    outcash() {
      // console.log(22, this.outcash)
      if (this.outcash === "sys") {
        if (this.incash === "sys") this.incash = "";
      } else {
        this.incash = "sys";
      }
    },
    incash() {
      // console.log(11, this.incash)
      if (this.incash === "sys") {
        if (this.outcash === "sys") this.outcash = "";
      } else {
        this.outcash = "sys";
      }
    },
  },
};
</script>

<style lang="scss">
.moneyChange-page {
  .change-head {
    margin-bottom: 20px;
    background-color: #0f1923;
    border: 1px solid #0f1923;
    .base-info {
      margin-top: 10px;
      color: #fff;
      .blueText {
        margin: 0 10px;
        color: #fff;
      }
      .el-button {
        margin-left: 20px;
      }
      .el-button--primary {
        background: #f12c4c;
        border-color: #f12c4c;
      }
      .el-button--danger {
        background: #0ec504;
        border-color: #67c23a;
      }
    }
  }
  .el-card__body {
    background: #0f1923;
    border: none;
  }
  .el-card {
    border: none;
    background: #0f1923 !important;
  }
  .change-box {
    .title {
      display: flex;
      flex-wrap: wrap;
      // align-items: center;
      font-size: 15px;
      line-height: 40px;
      color: #fff;
      .el-select {
        width: 150px;
        margin: 0 5px;
      }
      .outInput {
        width: 150px;
        margin-left: 5px;
      }
      .hint {
        color: red;
        font-weight: bold;
      }
      .el-button--danger {
        background: #0ec504;
        border-color: #0ec504;
      }
    }
    .cont {
      display: flex;
      text-align: center;
      margin-top: 20px;
      border: 1px solid $borderShallow;
      box-shadow: inset 0px 5px 18px #dcdfe6;
      .left {
        width: 160px;
        padding: 20px 5px;
        border-right: 1px solid $borderShallow;
        color: #fff;
        .num {
          color: #0ec504;
          font-size: 24px;
          word-break: break-all;
        }
      }
      .right {
        // width: calc(100% - 160px);
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        .item {
          width: 33%;
          border-right: 1px solid $borderShallow;
          border-bottom: 1px solid $borderShallow;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 30px 0;
          color: #fff;
          &:nth-child(3n) {
            border-right: none;
          }
          .num {
            margin: 0 20px;
            color: red;
            max-width: 200px;
            word-break: break-all;
          }
          .refresh {
            color: #0ec504;
            cursor: pointer;
          }
        }
      }
    }
  }
}
</style>
