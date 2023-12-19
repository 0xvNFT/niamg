<template>
  <div class="banking-page">
    <div class="radios">
      <el-radio-group v-model="radio" class="radioBox">
        <el-radio-button class="in" label="deposit">{{
          $t("depositText")
        }}</el-radio-button>
        <el-radio-button class="out" label="withdraw">{{
          $t("withdrawal")
        }}</el-radio-button>
      </el-radio-group>
    </div>
    <!-- 存款 -->
    <div class="deposit" v-if="radio === 'deposit'">
      <div class="chooseTypes">
        <span class="chooseTitle">{{ $t("TransferType") }}：</span>
        <el-radio-group
          v-model="rechargeType"
          @input="changeRechargeType(rechargeType)"
        >
          <el-radio :label="1">{{ $t("payType1") }}</el-radio>
          <el-radio :label="2">{{ $t("payType2") }} </el-radio>
        </el-radio-group>
      </div>
      <!-- 转账充值 -->
      <General v-if="rechargeType === 1"></General>
      <!-- 在线充值 -->
      <Online v-if="rechargeType === 2"></Online>
    </div>
    <!-- 提款 -->
    <div class="deposit" v-if="radio === 'withdraw'">
      <withdraw></withdraw>
    </div>
    <!-- 优惠活动弹窗 充值时 显示 -->
    <div
      class="preferential"
      v-if="showFlag && onOffBtn.deposit_discount_hint"
    >
      <!-- <img src="../../assets/images/BG.png" alt="" /> -->
      <div class="box">
        <p class="title">{{ $t("preferentialTitle") }}</p>
        <p class="money">203 RMB !</p>
        <button @click="showFlag = false">{{ $t("payText") }}</button>
        <p class="close" @click="leavePath">{{ $t("cancel") }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import General from "@/components/money/General.vue";
import Online from "@/components/money/Online.vue";
import Withdraw from "@/components/money/WithdrawMoney.vue";

export default {
  data() {
    return {
      radio: "deposit", // 存取款
      rechargeType: 1, //充值方式
      showFlag: false,
      leaveFlag: false,
      toPath: "", //跳转路径
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: { General, Online, Withdraw },
  mounted() {},
  beforeRouteLeave(to, from, next) {
    this.toPath = to;
    //判断是否开启 充值优惠提示
    if (this.onOffBtn.deposit_discount_hint) {
      this.getDepositStragety();
    }
    if (this.leaveFlag || !this.onOffBtn.deposit_discount_hint) {
      next();
    }
  },
  methods: {
    //选择 转账充值 or 在线充值
    changeRechargeType(val) {
      console.log(val);
    },
    //获取充值活动
    getDepositStragety() {
      this.$API.getDepositStragety().then((res) => {
        if (res.success) {
          this.showFlag = true;
        } else {
          this.showFlag = false;
          this.leaveFlag = true;
          this.$router.push(this.toPath.path);
        }
      });
    },
    //路由离开
    leavePath() {
      this.showFlag = false;
      this.leaveFlag = true;
      this.$router.push(this.toPath.path);
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.banking-page {
  margin: 0 auto;
  // max-width: 1420px;
  width: 95%;
  position: relative;
  .preferential {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    background-color: #0c09099e;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 999;
    img {
      width: 20%;
      position: fixed;
      top: 15%;
    }
    .box {
      position: fixed;
      text-align: center;
      padding-top: 120px;
      top: 25%;
      background-image: url(../../assets//images/BG.png);
      background-repeat: no-repeat;
      width: 20%;
      height: 400px;
      background-size: 100% 100%;
      .title {
        color: #fff;
        margin: 30px 0;
        font-size: 18px;
      }
      .money {
        color: #e68f10;
        font-size: 24px;
        font-weight: 700;
        margin-bottom: 30px;
      }
      .close:hover {
        color: #398dff;
      }
      .close {
        color: #fff;
        margin: 30px 0;
        cursor: pointer;
      }
      button:hover {
        background: linear-gradient(25deg, #ebc0c1, #f57b17);
      }
      button {
        background: linear-gradient(25deg, #f0a6a8, #ff7100);
        border: none;
        height: 3rem;
        padding: 1.5rem 3rem;
        border-radius: 25px;
        line-height: 0rem;
        color: #b70909;
        font-weight: 700;
        font-size: 20px;
        cursor: pointer;
      }
    }
  }
  .radios {
    font-weight: 700;
    margin-bottom: 2rem;
    margin-top: 2rem;
    text-align: center;
    .radioBox {
      align-self: center;
      background-color: #0f1318;
      border: 2px solid #303338;
      border-radius: 3rem;
      margin: 2rem 7rem;
      position: relative;
      .out {
        .el-radio-button__orig-radio:checked + .el-radio-button__inner {
          background-color: #b31d22 !important;
          border-color: #b31d22 !important;
          box-shadow: -1px 0 0 0 #b31d22;
        }
      }
    }
    .el-radio-button__inner {
      border: none;
      border-radius: 3rem;
      font-size: 1.5rem;
      font-weight: 900;
      outline: none;
      padding: 0.5rem 3rem;
      height: 3.5rem;
      line-height: 2.5rem;
      color: #fff;
      background-color: transparent;
    }
    .el-radio-button__orig-radio:checked + .el-radio-button__inner {
      background-color: #409eff;
      border-color: #409eff;
      box-shadow: -1px 0 0 0 #409eff;
    }
    .is-plain {
      background-color: #0f1318;
      color: #fff;
    }
  }
  .deposit {
    align-items: center;
    background-color: $black;
    border-radius: 1rem;
    margin: 2rem auto;
    padding: 1rem 2rem;
    width: 80%;
    .chooseTypes {
      margin: 30px 0px;
      .chooseTitle {
        margin-right: 10px;
      }
    }
    .inputMoney {
      margin-bottom: 20px;
      .el-input {
        width: 40%;
        .el-input__inner {
          min-width: 200px;
        }
      }
    }
    .qrCodeImg {
      width: 200px;
      margin: auto;
    }
    .el-input-group__prepend {
      background-color: #212b3c;
      border: none;
    }
    .el-input__inner {
      background-color: #0e131b;
      border: none;
      color: #fff;
    }

    .point {
      background: linear-gradient(
        90deg,
        rgba(0, 255, 0, 0.2),
        rgba(0, 100, 0, 0.2)
      );
      border-radius: 5rem;
      color: #fff;
      padding: 0.5rem 1rem;
      // width: 50%;
      // max-width: 100%;
      margin: 20px 0px;
      // display: flex;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      .el-icon-warning-outline {
        color: red;
        font-size: 24px;
        margin-right: 10px;
      }
    }
    .inputBtn {
      .confirm {
        color: #fff;
        align-self: center;
        background: linear-gradient(180deg, #69b858, #288d11);
        border: none;
        border-radius: 1rem;
        box-shadow: 0 4px 8px #98c48f;
        font-size: 1.2rem;
        font-weight: 900;
        letter-spacing: 0.3rem;
        margin: 2rem 0;
        padding: 1rem 7rem;
        width: -webkit-fit-content;
        width: -moz-fit-content;
        width: fit-content;
        font-family: inherit;
        cursor: pointer;
      }
      display: flex;
      justify-content: center;
      .confirm:hover {
        background: linear-gradient(25deg, #69b858, #318f1c) !important;
      }
    }
    .pay-types {
      .el-tabs__header {
        border-bottom: 1px solid gray;
        height: 4.1rem;
        .el-tabs__nav {
          border: none;
          .el-tabs__item {
            background-color: #212b3c;
            border: none;
            border-radius: 0.5rem;
            font-weight: 900;
            padding: 2rem 3rem;
            line-height: 0rem;
            margin-right: 10px;
          }
        }
      }
      .el-tabs__item {
        color: #fff;
      }
      .el-tabs__item:hover {
        color: #409eff;
      }
      .is-active {
        color: #409eff !important;
      }
      .el-tabs__content {
        .el-tab-pane {
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
          }
          .tipBox {
            p {
              padding-left: 10px;
              margin: 10px 0;
            }
          }

          .contentBox {
            .qrBox {
              .qr {
                display: flex;
                justify-content: center;
                img {
                  width: 250px;
                  border-radius: 20px;
                }
              }
            }
            .usdtBox {
              p {
                padding-left: 10px;
                margin: 10px 0;
              }
            }

            .inputRemark {
              margin-bottom: 20px;
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
  }
}
</style>
