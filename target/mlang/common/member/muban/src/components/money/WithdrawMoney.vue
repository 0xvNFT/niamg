<template>
  <div class="withdrawMoney-page">
    <!-- 可提款金额 -->
    <div class="headBox">
      <div class="head">
        <div class="canWithdraw">
          <span>{{ $t("canWithdraw") }}：</span>
          <strong style="color: #e95656">{{ userInfo.money | amount }}</strong>
        </div>
        <div class="strategyfee" v-if="strategyFlag">
          <span
            >{{ $t("strategyfeeValue") }}：({{
              onOffBtn.currency
            }})&nbsp;&nbsp;</span
          >
          <strong style="color: #e95656">{{
            strategy.feeType == 1 ? data.strategy.feeValue : sxf
          }}</strong>
        </div>
      </div>
      <div class="lookOrder">
        <button @click="goLookOrder">{{ $t("lookOrder") }}</button>
      </div>
    </div>
    <el-divider></el-divider>
    <!-- 提款银行卡 -->
    <div class="point">{{ $t("withdrawBank") }}：</div>
    <div class="cardBox">
      <el-card
        class="box-card"
        :class="item.id === curId ? 'blueBorder' : ''"
        @click.native="chooseCard(item)"
        v-for="(item, k) in data.bankList"
        :key="k"
      >
        <div slot="header" class="clearfix">
          <span>{{ item.bankName }}</span>
        </div>
        <div class="content" v-if="item.bankCode != 'USDT'">
          <span>{{ $t("nameText") }}</span>
          <div class="right">
            <span id="realName">{{ item.realName }}</span>
            <div class="user_copy" @click="copyName('realName')">
              <i class="el-icon-copy-document"></i>
            </div>
          </div>
        </div>
        <div class="content">
          <span>{{ $t("accountText") }}</span>
          <div class="right">
            <span id="cardNo">{{ item.cardNo }}</span>
            <div class="user_copy" @click="copyName('cardNo')">
              <i class="el-icon-copy-document"></i>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    <!-- 填写信息 -->
    <div class="point">{{ $t("putTransferInfo") }}：</div>
    <div class="inputBox">
      <div class="inputMoney">
        <el-input
          v-model="amount"
          v-amountFormat:amount
          :placeholder="$t('pleaseInput')"
          :clearable="true"
          style="margin-bottom: 20px"
        >
          <template slot="prepend">{{ $t("withdrawAmount") }}</template>
        </el-input>
        <el-input
          v-model="pwd"
          :placeholder="$t('pleaseInput')"
          show-password
          :clearable="true"
        >
          <template slot="prepend">{{ $t("withdrawPwd") }}</template>
        </el-input>
      </div>
    </div>
    <!-- 提交 -->
    <div class="inputBtn">
      <button class="confirm" @click="sureBtn">
        {{ $t("putSubmit") }}
      </button>
    </div>
    <!-- 详细数据 -->
    <div class="footBox">
      <div class="box">
        <!-- 今日已提次数 -->
        <div class="item">
          <span>{{ $t("dayTimes") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{ data.dayTimes }}</strong>
          <span>{{ $t("times") }}</span>
        </div>
        <!-- 今日还可以提款次数 -->
        <div class="item">
          <span>{{ $t("canDrawNum") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            data.lastTimes || 0
          }}</strong>
          <span>{{ $t("times") }}</span>
        </div>

        <!-- 所需手续费 -->
        <div class="item" v-if="strategyFlag">
          <span>{{ $t("strategy") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            strategy.feeType === 1 ? strategy.feeValue : sxf
          }}</strong>
          <span class="item-currency">{{ onOffBtn.currency }}</span>
        </div>

        <!-- 账户总余额 -->
        <!-- <div class="item">
          <span>{{ $t("totalMoney") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            userInfo.money || 0 | amount
          }}</strong>
          <span></span>
        </div> -->
        <!-- 单笔最小出款金额 -->
        <div class="item">
          <span>{{ $t("minDrawMoney") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            data.minDrawMoney || 0 | amount
          }}</strong>
          <span class="item-currency">{{ onOffBtn.currency }}</span>
        </div>
        <!-- 单笔最大出款金额 -->
        <div class="item">
          <span>{{ $t("maxDrawMoney") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            data.maxDrawMoney || 0 | amount
          }}</strong>
          <span class="item-currency">{{ onOffBtn.currency }}</span>
        </div>

        <!-- 当前打码量	 -->
        <div class="item">
          <span>{{ $t("nowBetNum") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            data.bet ? data.bet.betNum || 0 : 0 | amount
          }}</strong>
          <span> </span>
        </div>
        <!-- 出款所需打码量	 -->
        <div class="item">
          <span>{{ $t("drawNeedBet") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{
            data.bet ? data.bet.drawNeed || 0 : 0 | amount
          }}</strong>
          <span></span>
        </div>
        <!-- 提现时间 -->
        <div class="item">
          <span>{{ $t("withdrawalTime") }}：</span>
          <strong style="color: rgb(233, 86, 86)">{{ data.minDrawTime }}</strong
          >-
          <strong style="color: rgb(233, 86, 86)">{{
            data.maxDrawTime
          }}</strong>
        </div>
        <!--手续费说明	-->
        <div
          class="item1"
          :style="{ width: strategy.feeType === 1 ? '40%' : '90%' }"
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
            >，{{ $t("strategyInfo1") }}
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
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "index",
  data() {
    return {
      data: "",
      amount: "", //提款金额
      pwd: "", //提款密码
      showSXF: false, //手续费是否显示
      strategy: {
        drawNum: undefined,
        feeValue: undefined,
        feeType: undefined
      }, // 初始化手续费数据
      sxf: 0, //手续费
      bankIndex: 0, //当前选中的银行卡key
      bankCheck: "", //当前选中的银行卡信息
      usdtRate: "", //usdt汇率
      usdtNum: 0, //usdt数量
      curId: 0, //选中后 显示蓝色边框
      strategyFlag: false, //手续费判断
    };
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn"]),
  },
  components: {},
  created() {},
  mounted() {
    this.withdrawInfo();
  },
  methods: {
    // 复制
    copyName(val) {
      this.$publicJs.copyFun(val);
      this.$message.success(this.$t("successCopy"));
    },
    //选择银行卡
    chooseCard(val) {
      this.curId = val.id;
    },
    // 获获取usdt信息
    getUsdtInfo() {
      this.$API.getUsdtInfo().then((res) => {
        if (res) {
          this.usdtaRate = res.depositRate;
          this.usdtRemark = res.remark;
        }
      });
    },
    //提款信息
    withdrawInfo() {
      this.$API.withdrawInfo().then((res) => {
        if (res) {
          this.data = res;
          if (res.strategy && JSON.stringify(res.strategy) != "{}") {
            this.strategy = res.strategy;
            this.strategyFlag = true;
          }
          if (res.receiptPwd) {
            this.$confirm(
              this.$t("sureSetWithPwd"),
              this.$t("withdrawPwdNoHint"),
              {
                confirmButtonClass: "smallSureBtn themeBtn",
                cancelButtonClass: "smallCloseBtn themeBtn",
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
          } else if (res.needBank) {
            this.$confirm(this.$t("sureSetBank"), this.$t("bankNoHint"), {
              confirmButtonClass: "smallSureBtn themeBtn",
              cancelButtonClass: "smallCloseBtn themeBtn",
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

          if (res.bankList && res.bankList.length) {
            this.bankCheck = res.bankList[0];
            if (this.bankCheck.bankName === "USDT") this.rechargeUsdtInfo();
          }
        }
      });
    },
    //跳转查看流水
    goLookOrder() {
      this.$router.push("/recordChange");
    },
    //提交
    sureBtn() {
      if(!this.data.lastTimes){
        this.$message.error(this.$t("InsufficienWithdrawals"));
        return
      }
      //改变提交格式
      let params = {
        amount: this.amount,
        bankId: this.bankCheck.id,
        pwd: this.pwd,
      };
      this.$API.withdrawSave(params).then((res) => {
        this.withdrawInfo();
        if (res.success) {
          this.$notify({
            title: "success",
            message: res.msg,
            type: "success",
          });
          this.amount = "";
          this.pwd = "";
        }
      });
    },
  },
  watch: {
    amount() {
      if (this.amount) {
        if (this.usdtRate)
          this.usdtNum = (this.amount / this.usdtRate).toFixed(2);
        // else this.usdtNum = 0;
        if (this.strategy && this.strategy.feeType === 2) {
          let money = (this.amount.replaceAll(',', '') * this.strategy.feeValue) / 100;

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

<style lang="scss">
.withdrawMoney-page {
  margin: 0 auto;
  max-width: 1420px;
  width: 95%;
  .headBox {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .head {
      display: flex;
      font-size: 16px;
      .canWithdraw {
        margin-right: 30px;
      }
    }
    .lookOrder {
      button:hover {
        background: linear-gradient(25deg, #b32c31, #e93d45);
      }
      button {
        align-items: center;
        cursor: pointer;
        display: flex;
        justify-content: center;
        background: linear-gradient(25deg, #8b181b, #de1f27);
        color: #fff;
        font-weight: 900;
        height: 3rem;
        border: none;
        border-radius: 0.8rem;
        padding: 1rem 1.5rem;
      }
    }
  }
  .cardBox {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    .el-card {
      background-color: #202835;
      border-radius: 1rem;
      margin: 1rem 0;
      padding: 1rem;
      width: 48%;
      color: white;
      cursor: pointer;
      .content {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;
        .right {
          display: flex;
          span {
            color: #9acd32;
            font-weight: bolder;
          }
          .user_copy {
            margin-left: 20px;
            cursor: pointer;
          }
        }
      }
    }
    .el-card:hover {
      box-shadow: 0 2px 12px 0 rgba(221, 210, 210, 0.6);
    }
    .blueBorder {
      border: 1px solid #56ace5;
      box-shadow: 0 2px 12px 0 rgba(221, 210, 210, 0.6);
    }
  }
  .inputBox {
    .inputMoney {
      display: flex;
      flex-direction: column;
    }
    .el-input {
      width: 40%;
    }
  }
  .footBox {
    margin-top: 10px;
    .box {
      //   border: 1px solid rgb(235, 142, 132);
      display: flex;
      margin-bottom: 20px;
      flex-wrap: wrap;
      .item {
        font-size: 14px;
        background: #112545;
        border-radius: 0.5rem;
        display: flex;
        align-items: center;
        min-height: 60px;
        min-height: 75px;
        width: 40%;
        padding: 0 55px;
        gap: 10px;
        margin-bottom: 15px;
        margin-left: 5%;
        margin-right: 5%;
        .item-currency {
          white-space: nowrap;
        }
      }
      .item1 {
        font-size: 14px;
        background: #112545;
        border-radius: 0.5rem;
        display: block;
        display: flex;
        align-items: center;
        flex-direction: row;
        flex-wrap: wrap;
        min-height: 60px;
        min-height: 75px;
        width: 40%;
        padding: 0 55px;
        gap: 20px;
      }
    }
  }
}
</style>
