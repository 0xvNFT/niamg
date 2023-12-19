<template>
  <el-card class="withdraw-page">
    <p v-if="data.receiptPwd">{{ $t("withdrawPwdNoHint") }}</p>
    <p v-if="data.needBank">{{ $t("bankNoHint") }}</p>
    <div class="item">
      {{ $t("canWithdraw")
      }}<strong style="margin-left: 35px;">{{ userInfo.money }}</strong>
      <span style="margin-left: 30px;" v-if="strategyFlag"
        >{{ $t("strategyfeeValue") }}（{{ onOffBtn.currency }}）<strong>{{
          data.strategyfeeValue || sxf
        }}</strong></span
      >
    </div>
    <div class="item">
      <span class="name">{{ $t("withdrawBank") }}</span>
      <div class="bankList">
        <el-tooltip
          v-for="(val, key) in data.bankList"
          :key="key"
          class="listItem"
          effect="dark"
          :content="val.cardNo"
          placement="top-start"
          :class="bankIndex === key ? 'checked' : ''"
        >
          <div @click="changeBank(key, val)">
            {{ val.bankName }} {{ val.realName }} {{ val.cardNo
            }}<img
              src="../../assets/images/common/xz-icon.png"
              alt=""
              v-if="bankIndex === key"
            />
          </div>
        </el-tooltip>
      </div>
    </div>
    <template v-if="bankCheck.bankName === 'USDT'">
      <!-- usdt汇率 -->
      <div class="item">
        <span class="name">{{ $t("usdtRate") }}</span>
        <div class="" style="margin-left: 8px;">{{ usdtRate }}</div>
      </div>
      <!-- usdt数量 -->
      <div class="item">
        <span class="name">{{ $t("usdtNum") }}</span>
        <div class="" style="margin-left: 8px;">{{ usdtNum }}</div>
      </div>
    </template>
    <div class="item">
      <span class="name">{{ $t("withdrawAmount") }}</span>
      <div class="getNum">
        <el-input
          v-model="amount"
          class="outInput"
          clearable
          size="small"
          type="number"
          :controls="false"
        ></el-input>
      </div>
      <span style="margin-left: 10px"
        >{{ $t("drawMoney") }} {{ data.minDrawMoney }} ~
        {{ data.maxDrawMoney }} {{ onOffBtn.currency }}</span
      >
    </div>
    <div class="item">
      <span class="name">{{ $t("moneyPwd") }}</span>
      <div>
        <el-input
          v-model="pwd"
          type="password"
          class="outInput"
          clearable
          size="small"
        ></el-input>
      </div>
    </div>
    <div class="item btnDiv">
      <el-button
        type="primary"
        size="medium"
        style="margin-right: 30px; margin-left: 100px"
        @click="sureBtn"
        >{{ $t("putSubmit") }}</el-button
      >
      <router-link to="/recordChange">
        <el-button type="primary" size="medium">{{
          $t("lookOrder")
        }}</el-button>
      </router-link>
    </div>
    <div class="footBox">
      <!-- 今日已提次数 -->
      <div class="item-bottom">
        <span>{{ $t("dayTimes") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{ data.dayTimes }}</strong>
        <span>{{ $t("times") }}</span>
      </div>
      <!-- 今日剩余提款次数 -->
      <div class="item-bottom">
        <span>{{ $t("canDrawNum") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          data.lastTimes || 0
        }}</strong>
        <span>{{ $t("times") }}</span>
      </div>
      <!-- 所需手续费 -->
      <div class="item-bottom" v-if="strategyFlag">
        <span>{{ $t("strategy") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          strategy.feeType === 1 ? strategy.feeValue : sxf
        }}</strong>
        <span>{{ onOffBtn.currency }}</span>
      </div>
      <!-- 账户总余额 -->
      <!-- <div class="item-bottom">
        <span>{{ $t("totalMoney") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          userInfo.money || 0
        }}</strong>
        <span></span>
      </div> -->
      <!-- 单笔最小出款金额 -->
      <div class="item-bottom">
        <span>{{ $t("minDrawMoney") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          data.minDrawMoney || 0
        }}</strong>
        <span>{{ onOffBtn.currency }}</span>
      </div>
      <!-- 单笔最大出款金额 -->
      <div class="item-bottom">
        <span>{{ $t("maxDrawMoney") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          data.maxDrawMoney || 0
        }}</strong>
        <span>{{ onOffBtn.currency }}</span>
      </div>
      <!-- 当前打码量	 -->
      <div class="item-bottom">
        <span>{{ configJsFlag("nowBetNum") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          data.bet ? data.bet.betNum || 0 : 0
        }}</strong>
        <span> </span>
      </div>
      <!-- 出款所需打码量	 -->
      <div class="item-bottom">
        <span>{{ configJsFlag("drawNeedBet") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{
          data.bet ? data.bet.drawNeed || 0 : 0
        }}</strong>
        <span></span>
      </div>
      <!-- 提现时间 -->
      <div class="item-bottom">
        <span>{{ $t("withdrawalTime") }}：</span>
        <strong style="color: rgb(233, 86, 86)">{{ data.minDrawTime }}</strong
        >-
        <strong style="color: rgb(233, 86, 86)">{{
          data.maxDrawTime
        }}</strong>
      </div>
      <!--手续费说明	-->
      <div class="item-bottom item1"
      :style="{ width: strategy.feeType === 1 ? '35%' : '90%' }"
      v-if="strategyFlag"
      >
        <span v-if="strategy.feeType === 1">{{ $t("drawNum") }}：</span>
        <template v-else>
          <span>{{ $t("strategyInfo") }}：</span>
          <span>{{ $t("drawNum") }}</span>
        </template>
        <strong style="color: rgb(233, 86, 86)">{{
          strategy.drawNum || 0
        }}</strong>
        {{ $t("times") }}
        <template v-if="strategy.feeType === 2"
          >，
          <span>{{ $t("strategyInfo1") }}</span>
          <strong style="color: rgb(233, 86, 86)"
            >{{ strategy.feeValue }}%</strong
          >
          <span>{{ $t("strategyInfo2") }}</span>
        </template>
      </div>
      <!--提款说明	-->
      <div class="item" style="width: 90%" v-if="data.withdrawPageIntroduce">
        <span>{{ $t("withdrawHint") }}：</span>
        <span>{{ data.withdrawPageIntroduce }}</span>
      </div>
    </div>
  </el-card>
</template>

<script>
import { mapState } from "vuex";
// import LinkBar from '@/components/index/LinkBar'

export default {
  data() {
    return {
      data: "",
      amount: "", //提款金额
      pwd: "", //提款密码
      strategyFlag: false, //手续费是否显示
      strategy: "", //手续费数据
      sxf: 0, //手续费
      bankIndex: 0, //当前选中的银行卡key
      bankCheck: "", //当前选中的银行卡信息
      usdtRate: "", //usdt汇率
      usdtNum: 0, //usdt数量
    };
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn"]),
  },
  components: {},
  mounted() {
    this.withdrawInfo();
    // this.rechargeUsdtInfo();
  },
  methods: {
    //获取usdt信息
    rechargeUsdtInfo() {
      this.$axiosPack.post("/userCenter/finance/usdtInfo.do").then((res) => {
        if (res) {
          this.usdtRate = res.data.withdrawRate;
        }
      });
    },
    //提款信息
    withdrawInfo() {
      this.$axiosPack
        .post("/userCenter/finance/withdrawInfo.do")
        .then((res) => {
          if (res) {
            this.strategy = res.data.strategy;
            this.data = res.data;
            if (JSON.stringify(this.strategy) != "{}") {
              this.strategyFlag = true;
            }
            if (res.data.receiptPwd) {
              this.$confirm(
                this.$t("sureSetWithPwd"),
                this.$t("withdrawPwdNoHint"),
                {
                  confirmButtonText: this.$t("confirm"),
                  cancelButtonText: this.$t("cancel"),
                  type: "warning",
                }
              )
                .then(() => {
                  this.$router.replace("/securityCenter");
                })
                .catch(() => {
                  this.$router.go(-1);
                });
              return;
            } else if (res.data.needBank) {
              this.$confirm(this.$t("sureSetBank"), this.$t("bankNoHint"), {
                confirmButtonText: this.$t("confirm"),
                cancelButtonText: this.$t("cancel"),
                type: "warning",
              })
                .then(() => {
                  this.$router.replace("/bankIndex");
                })
                .catch(() => {
                  this.$router.go(-1);
                });
            }

            if (res.data.bankList && res.data.bankList.length) {
              this.bankCheck = res.data.bankList[0];
              if (this.bankCheck.bankName === "USDT") this.rechargeUsdtInfo();
            }
          }
        });
    },

    changeBank(key, val) {
      this.bankIndex = key;
      this.bankCheck = val;
      if (this.bankCheck.bankName === "USDT") this.rechargeUsdtInfo();
    },

    sureBtn() {
      if(!this.data.lastTimes){
        this.$message.error(this.$t("InsufficienWithdrawals"));
        return
      }
      //改变提交格式
      let params = new URLSearchParams();
      params.append("amount", this.amount);
      params.append("bankId", this.bankCheck.id);
      params.append("pwd", this.pwd);
      this.$axiosPack
        .post("/userCenter/finance/withdrawSave.do", {
          params: params,
          load: [true, 200, null],
        })
        .then((res) => {
          this.withdrawInfo();
          if (res) {
            this.$notify({
              title: "success",
              message: res.data.msg,
              type: "success",
            });
            this.amount = "";
            this.pwd = "";
          }
        });
    },
    mouseenter() {
      console.log(1111);
    },
  },
  watch: {
    amount() {
      if (this.amount) {
        if (this.usdtRate)
          this.usdtNum = (this.amount / this.usdtRate).toFixed(2);
        // else this.usdtNum = 0;
        if (this.strategy && this.strategy.feeType === 2) {
          let money = (this.amount * this.strategy.feeValue) / 100;

          if (this.strategy.upperLimit && money > this.strategy.upperLimit) {
            money = this.strategy.upperLimit;
          } else if (
            this.strategy.lowerLimit &&
            money < this.strategy.lowerLimit
          ) {
            money = this.strategy.lowerLimit;
          }
          this.sxf = money;
        }
      } else {
        this.usdtNum = 0;
        this.sxf = 0;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.el-card {
  border: none;
  // background: #0f1923 ;
  color: #fff;
}
#app {
  .ui-main {
    .con_box {
      .withdraw-page {
        background: #0f1923;
      }
    }
  }
}
.withdraw-page {
  padding-left: 80px;
  .bankList {
    display: flex;
    flex-wrap: wrap;
    .listItem {
      width: 200px;
      text-align: center;
      cursor: pointer;
      border: 1px solid #e9e9e9;
      margin-right: 10px;
      padding: 5px 10px;
      border-radius: 3px;
      margin-bottom: 5px;
      position: relative;
      overflow: hidden;
      text-overflow: ellipsis;
      // white-space: nowrap;
      &.checked {
        border-color: #56ace5;
      }
      img {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 16px;
        height: 16px;
      }
    }
  }
  .item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    strong {
      font-size: 20px;
      color: #e95656;
    }
    .name {
      width: 100px;
      margin-right: 8px;
    }
    .getNum {
      //清除 el-input  type为number时  出现上下箭头
      ::v-deep input::-webkit-outer-spin-button,
      ::v-deep input::-webkit-inner-spin-button {
        -webkit-appearance: none !important;
      }
      ::v-deep input[type="number"] {
        -moz-appearance: textfield !important;
      }
    }
  }
  .btnDiv {
    margin-top: 30px;
    .el-button--medium {
      background-color: #0ec504;
      border: 1px solid #0ec504;
    }
  }
  table {
    // border-top: 1px solid #cdcdcd;
    // border-right: none;
    border: 1px solid #e9e9e9;
    font-size: 16px;
  }
  table tr td {
    border: 1px solid #e9e9e9;
    padding: 7px 30px;
  }

  .footBox {
    margin-top: 40px;
    max-width: 860px;
    display: flex;
    flex-wrap: wrap;
    justify-content:space-between;
    .item-bottom {
      font-size: 14px;
      border: 1px solid #fff;
      border-radius: 3px;
      display: flex;
      align-items: center;
      min-height: 60px;
      text-align: center;
      min-height: 75px;
      width: 35%;
      padding: 0 55px;
      gap: 10px;
      margin-bottom: 15px;
    }
  }
}
</style>
