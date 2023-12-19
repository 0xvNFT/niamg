<template>
  <div class="pay-types">
    <!-- 支付方式 -->
    <el-tabs v-model="activeName" type="card" @tab-click="checkoutCounterByType(activeName)">
      <el-tab-pane :label="type" v-for="(type, i) in onlineList" :key="i" :name="String(i)">
        <!-- 支付通道 -->
        <div class="tdBox">
          <el-tooltip class="item" effect="dark" :content="payTipsDepositOnline" placement="top-start">
            <div class="point">
              {{ $t("payCheckTD") }}：{{ payTipsDepositOnline }}
            </div>
          </el-tooltip>

          <div class="typeList">
            <div class="TD" v-for="(item, i) in TDList" :key="i" @click="getNowData(i)" :class="checkIndexTD === i ? 'isTDChecked' : ''">
              <span>{{ item.payAlias }}</span>
              <img v-if="checkIndexTD === i" class="checkImg" src="@images/xz" alt="" />
            </div>
          </div>
        </div>
        <!-- 充值金额 -->
        <div class="moneyBox">
          <el-tooltip class="item" effect="dark" :content="$t('payMoneyHint1')+min+'，'+ $t('payMoneyHint2')+max" placement="top-start">
            <div class="point">
              {{ $t("payMoney") }}：{{ $t("payMoneyHint1") }} {{ min | amount }}.
              {{ $t("payMoneyHint2") }} {{ max | amount }}
            </div>
          </el-tooltip>
          <div class="allow-number-container" v-if="nowTDData.isFixedAmount === 3">
            <div class="amount_btn" v-for="(item, k) in isChangeMoney" :key="k" :class="checkMoney == k ? 'isMoneyChecked' : ''" @click="chooseMoney($event, k, item)">
              <div class="light_img">
                <img src="@images/moneybackground" alt="" />
              </div>
              <div class="button_inner">
                <span>{{ onOffBtn.currency }}&nbsp;</span>
                <span>{{ item }}</span>
              </div>
            </div>
          </div>
          <div class="inputMoney" v-else>
            <el-input v-model="amount" v-amountFormat:amount :placeholder="$t('pleaseInput')" @change="inputMoney" :clearable="true">
              <template slot="prepend">{{ $t("depositMoney") }}</template>
            </el-input>
          </div>
        </div>
        <!-- 充值按钮 -->
        <div class="inputBtn">
          <button class="confirm" @click="sureBtn" :disabled="isDisabled">
            {{ $t("confirm") }}
          </button>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "index",
  data () {
    return {
      radio: 0,
      activeName: "0",
      data: "", //在线入款支付方式数据
      onlineList: [], //在线入款支付方式列表
      checkIndexFS: 0, //支付方式被选中下标
      TDList: [], //在线支付通道列表
      nowTDData: "", //当前选中的支付通道的数据
      checkIndexTD: 0, //支付方式被选中下标
      isChangeMoney: [], //当前点击的可选金额数组
      clickMoneyIndex: 0, //当前点击的可选充值金额
      amount: "", //提交的入款金额可选金额
      isDisabled: false, //确定按钮是否可用
      payTipsDepositOnline: "", //请选择支付通道 提示
      min: "", //单笔最低充值
      max: "", //单笔最高充值
      checkMoney: -1, //金额框被选中下标
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  created () { },
  mounted () {
    this.getOnlineInfo();
  },
  methods: {
    //  获取在线入款支付方式
    getOnlineInfo () {
      this.$API.getOnlineInfo().then((res) => {
        if (res) {
          this.data = res;
          this.onlineList = res.onlineList;
          this.payTipsDepositOnline = res.payTipsDepositOnline;
          // 根据支付方式查询支付通道
          if (this.onlineList.length) this.checkoutCounterByType(0);
          else this.$emit("update:hasOnline", false);
        }
      });
    },
    // 根据支付方式查询支付通道
    checkoutCounterByType (key) {
      // 改变选中下标
      this.checkIndexFS = key;
      let params = {
        payType: this.onlineList[key],
      };
      this.$API.checkoutCounterByType(params).then((res) => {
        if (res) {
          this.TDList = res.onlineList;
          this.getNowData(0);
        }
      });
    },
    // 得到当前点击的通道数据
    getNowData (key) {
      // 重置提交金额
      this.amount = "";
      // 改变选中下标
      this.checkIndexTD = key;
      // 改变选中数据
      this.nowTDData = this.TDList[key];
      this.min = this.nowTDData.min;
      this.max = this.nowTDData.max;
      if (this.nowTDData.isFixedAmount === 3) {
        //为设置可选固定金额
        this.isChangeMoney = this.nowTDData.fixedAmount.split(",");
        this.clickMoneyIndex = -1;
      } else if (this.nowTDData.isFixedAmount === 2) {
        //为设置单一固定金额
        this.amount = this.nowTDData.fixedAmount;
      }
    },
    //选择快捷金额
    chooseMoney (e, i, m) {
      this.checkMoney = i; //选中下标
      this.amount = m; //选中金额
      //判断是否点击是的当前金额框
      if (!e.target) {
        this.checkMoney = -1;
      }
    },
    //输入框金额
    inputMoney () {
      console.log(this.amount);
    },
    //提交
    sureBtn () {
      if (!this.amount) {
        this.$message.error(this.$t("canNotMoney"));
      } else if ( Number(this.amount.replaceAll(',', '')) < this.min ) {
        this.$message.error(this.$t('lowestDeposit'));
      } else if ( Number(this.amount.replaceAll(',', '')) > this.max ) {
        this.$message.error(this.$t('highestDeposit'));  
      } else {
        this.isDisabled = true;
        //改变提交格式
        let params = {
          payId: this.nowTDData.id,
          bankCode: this.nowTDData.payType,
          amount: this.amount,
        };
        this.$API.pay(params).then((res) => {
          this.isDisabled = false;
          if (res) {
            var form = $("#rechargeSubmitForm");
            form.empty();
            var data = res;
            if (res.returnType === "qrcodeUrl") {
              form.attr("action", play + "/onlinePay/qrcodeRedirect.do");
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitFormData' value='" +
                  data.url +
                  "'/>"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitOrderId' value='" +
                  data.orderId +
                  "' />"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitPayName' value='" +
                  data.payName +
                  "' />"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitPayType' value='" +
                  data.payType +
                  "' />"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitOrderTime' value='" +
                  data.orderTime +
                  "' />"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitPayAmount' value='" +
                  data.payAmount +
                  "' />"
                )
              );
              form.append(
                $(
                  "<input type='hidden' name='rechargeSubmitPayFlag' value='" +
                  data.flag +
                  "' />"
                )
              );
              form.submit();
              form.empty();
            } else if (res.returnType === "postSubmit") {
              form.attr("action", data.url);
              if (data.form_method == "get") {
                form.attr("method", "get");
              }
              $.each(data.dataMap, function (key, value) {
                form.append(
                  $(
                    "<input hidden type='text' name='" +
                    key +
                    "' value='" +
                    value +
                    "'/>"
                  )
                );
              });
              form.submit();
              form.empty();
            } else {
              if (res.returnType === "href") {
                // window.location.href = data.url;
                // window.open(data.url, "_blank");
                location.href = data.url;
              } else if (res.returnType === "write") {
                var newwindow = window.open("");
                newwindow.document.write(data.url);
              } else {
                form.attr("action", data.url);
                form.submit();
                form.empty();
              }
            }
          }
        });
      }
    },
  },
};
</script>

<style lang="scss">
.pay-types {
  .tdBox {
    .typeList {
      display: flex;
      justify-content: left;
      flex-wrap: wrap;
      padding-left: 10px;
      .TD {
        cursor: pointer;
        background-color: #212b3c;
        margin-right: 1rem;
        padding: 10px;
        border-radius: 3px;
        margin-bottom: 5px;
        display: flex;
        position: relative;
        justify-content: center;
        align-items: center;
      }
      .isTDChecked {
        border: 1px solid #56ace5;
      }
      .checkImg {
        position: absolute;
        right: 0;
        bottom: 0;
        width: 16px;
        height: 16px;
      }
    }
    .el-radio-button__inner {
      background-color: #212b3c;
      border: none !important;
      margin-right: 20px;
    }
  }
  .moneyBox {
    margin-bottom: 10px;
    .inputMoney {
      padding-left: 10px;
    }
    .allow-number-container {
      grid-gap: 1rem;
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
      justify-content: space-between;
      justify-items: center;
      width: 90%;
      .isMoneyChecked {
        background-color: #0990a7 !important;
      }
      .amount_btn {
        align-items: center;
        background-color: $titleBg;
        border: none;
        border-radius: 0.75rem;
        box-shadow: 0 0 0.3rem #fff;
        cursor: pointer;
        display: flex;
        font-size: 14px;
        font-weight: 700;
        height: 90px;
        outline: none;
        overflow: hidden;
        position: relative;
        text-align: center;
        text-decoration: none;
        white-space: nowrap;
        width: 150px;
        &:hover {
          background-color: #0a0d11d8;
          img {
            animation: roli_rotate 5s linear infinite;
          }
        }
        .light_img {
          bottom: 0;
          left: 0;
          overflow: hidden;
          position: absolute;
          right: 0;
          top: 0;
          z-index: 0;
          transition: transform 10s ease;
          img {
            margin-top: -18%;
            width: 100%;
            z-index: 0;
          }
        }
        .button_inner {
          align-items: center;
          display: flex;
          justify-content: center;
          margin: 0 auto;
          text-align: center;
          width: 100%;
          z-index: 5;
        }
      }
    }
  }
}
</style>
