<template>
  <div class="recharge-page">
    <el-tabs type="border-card" v-model="tabIndex">
      <el-tab-pane :label="$t('payType1')" name="1" v-if="hasGeberal">
        <General
          v-show="tabIndex == 1"
          :hasGeberal.sync="hasGeberal"
          :tabIndex.sync="tabIndex"
        ></General>
      </el-tab-pane>
      <el-tab-pane :label="$t('payType2')" name="2" v-if="hasOnline">
        <Online
          v-show="tabIndex == 2"
          ref="onLine"
          :hasOnline.sync="hasOnline"
        ></Online>
      </el-tab-pane>
    </el-tabs>
    <!-- 优惠活动弹窗 充值时 显示 -->

    <div class="preferential" v-if="showFlag && onOffBtn.deposit_discount_hint">
      <img src="../../assets/images/common/BG.png" alt="" />
      <div class="box">
        <p class="title">{{ $t("preferentialTitle") }}</p>
        <p class="money">203 {{ onOffBtn.currency }} !</p>
        <button @click="showFlag = false">{{ $t("payText") }}</button>
        <p class="close" @click="leavePath">{{ $t("cancel") }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import Online from "@/components/pay/Online";
import General from "@/components/pay/General";

export default {
  data() {
    return {
      tabIndex: "1", //
      hasGeberal: true,
      hasOnline: true,
      showFlag: false, //是否显示弹窗
      leaveFlag: false, //是否跳转路由
      toPath: "", //跳转路径
      changeFlag: "", //是否显示
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {
    Online,
    General,
  },
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
    //路由离开
    leavePath() {
      this.showFlag = false;
      this.leaveFlag = true;
      this.$router.push(this.toPath.path);
    },
    //获取充值活动
    getDepositStragety() {
      this.$axiosPack
        .post("/userCenter/finance/getDepositStragety.do")
        .then((res) => {
          if (res.data.success) {
            this.showFlag = true;
          } else {
            this.showFlag = false;
            this.leaveFlag = true;
            this.$router.push(this.toPath.path);
          }
        });
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.recharge-page {
  color: #000;
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
      width: 30%;
    }
    .box {
      position: absolute;
      text-align: center;
      padding-top: 100px;
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
  .hint {
    margin-bottom: 10px;

    .num {
      display: inline-block;
      width: 20px;
      height: 20px;
      text-align: center;
      background: #398dff;
      border-radius: 50%;
      line-height: 20px;
      color: #fff;
      margin-right: 10px;
    }
  }
  .list {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 20px;
    .item {
      // min-width: 320px;
      cursor: pointer;
      border: 1px solid #e9e9e9;
      margin-right: 10px;
      padding: 5px;
      border-radius: 3px;
      margin-bottom: 5px;
      display: flex;
      position: relative;
      justify-content: center;
      align-items: center; /* 垂直居中 */
      &.checked {
        border-color: #56ace5;
      }
      &.item-text {
        padding: 10px;
      }
      &.item-money {
        padding: 7px 10px;
      }
      .icon {
        width: 40px;
        height: 40px;
        margin-right: 5px;
      }
      .iconyinxingqia {
        font-size: 40px;
        color: #398dff;
        margin-right: 10px;
      }
      .checkImg {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 16px;
        height: 16px;
      }
    }
    .el-input {
      width: 300px;
    }
  }
  .inputDiv {
    display: flex;
    .left {
      line-height: 32px;
      text-align: right;
    }
    .name,
    .outInput {
      margin-bottom: 10px;
    }
    .outInput {
      width: 250px;
      display: block;
    }
    .right {
      .text {
        height: 32px;
        line-height: 32px;
        margin-bottom: 10px;
      }
    }
  }
  .typeDiv {
    display: flex;
    flex-wrap: wrap;
    margin-bottom: 20px;
    .item {
      min-width: 150px;
      cursor: pointer;
      border: 1px solid #e9e9e9;
      margin-right: 10px;
      padding: 10px;
      border-radius: 3px;
      margin-bottom: 5px;
      display: flex;
      justify-content: center;
      position: relative;
      font-size: 16px;
      &.checked {
        border-color: #56ace5;
      }
      .iconfont {
        font-size: 20px;
        color: #398dff;
        margin-right: 10px;
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
  .qrCodeDiv {
    width: 500px;
    text-align: center;
    margin-bottom: 20px;
    .qrCodeImg {
      width: 200px;
    }
  }
  .hintDiv {
    margin-top: 20px;
    color: #999;
    .icon {
      .iconfont {
        font-size: 20px;
        color: red;
        margin-right: 10px;
      }
    }
    .line {
      margin-bottom: 5px;
    }
  }
}
</style>
