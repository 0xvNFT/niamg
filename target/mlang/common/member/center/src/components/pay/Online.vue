<template>
  <div class="online-ponent">
    <!-- 支付方式 -->
    <p class="hint"><span class="num">1</span>{{ $t("payCheckFS") }}：{{data.payTipsDepositOnline}}</p>
    <div class="list">
      <div class="item" v-for="(val,key) in onlineList" :key="key" @click="checkoutCounterByType(key)" :class="checkIndexFS === key ? 'checked' : ''">
        <img :src="'/common/member/images/pay/' + onOffBtn.lang + '/' + val + '.png'" alt="" class="fangshiImg">
        <img src="../../assets/images/common/xz-icon.png" alt="" v-if="checkIndexFS === key" class="checkImg">
      </div>
    </div>
    <!-- 支付通道 -->
    <p class="hint"><span class="num">2</span>{{ $t("payCheckTD") }}：{{nowTDData.pcRemark }}</p>
    <div class="list">
      <div class="item item-text" v-for="(val,key) in TDList" :key="key" @click="getNowData(key)" :class="checkIndexTD === key ? 'checked' : ''">
        {{val.payAlias || val.payName}}
        <img src="../../assets/images/common/xz-icon.png" alt="" v-if="checkIndexTD === key" class="checkImg">
      </div>
    </div>
    <!-- 充值金额 -->
    <p class="hint"><span class="num">3</span>{{ $t("payMoney") }}：{{ $t("payMoneyHint1") + nowTDData.min}}，{{ $t("payMoneyHint2") + nowTDData.max}}</p>
    <div class="list" v-if="nowTDData.isFixedAmount === 3">
      <div class="item item-money" v-for="(val,key) in isChangeMoney" :key="key" @click="moneyChange(val, key)" :class="clickMoneyIndex === key ? 'checked' : ''">{{val}}</div>
    </div>
    <div class="list" v-else>
      <el-input :placeholder="$t('putHint')" v-model="amount" :disabled="nowTDData.isFixedAmount === 2"></el-input>
    </div>
    <!-- 充值按钮 -->
    <el-button type="primary" round @click="sureBtn" :disabled="isDisabled">{{ $t("confirm") }}</el-button>

    <form style="display:none;" method="post" id="rechargeSubmitForm" action="#"></form>
    <form style="display:none;" method="post" id="rechargeSubmitForm2" action>
      <input type="hidden" readonly="readonly" id="paymentId" name="payId" v-model="this.nowTDData.id">
      <input id="payAmount" name="amount" v-model="amount" type="text" readonly="readonly" style="border:none;background:none;color:#C7C5C5;padding:0;">
      <input type="hidden" readonly="readonly" id="bankCode" name="bankCode" v-model="this.nowTDData.payType">
    </form>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      data: '',//在线入款支付方式数据
      onlineList: [],//在线入款支付方式列表
      checkIndexFS: 0,//支付方式被选中下标
      TDList: [],//在线支付通道列表
      nowTDData: '',//当前选中的支付通道的数据
      checkIndexTD: 0,//支付方式被选中下标
      isChangeMoney: [],//当前点击的可选金额数组
      clickMoneyIndex: 0,//当前点击的可选充值金额
      amount: '',//提交的入款金额可选金额
      isDisabled: false,//确定按钮是否可用
    };
  },
  props: ['hasOnline'],
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
  },
  mounted () {
    // 获取在线入款支付方式
    this.depositList()
  },
  methods: {
    // 获取在线入款支付方式
    depositList () {
      this.$axiosPack.post("/userCenter/finance/depositList.do?payCode=online").then(res => {
        if (res) {
          this.data = res.data
          this.onlineList = res.data.onlineList
          // 根据支付方式查询支付通道
          if (this.onlineList.length) this.checkoutCounterByType(0)
          else this.$emit('update:hasOnline', false);
        }
      });
    },
    // 根据支付方式查询支付通道
    checkoutCounterByType (key) {
      // 改变选中下标
      this.checkIndexFS = key;
      this.$axiosPack.post("/userCenter/finance/checkoutCounterByType.do?payType=" + this.onlineList[key], { load: [true, 200, null] }).then(res => {
        if (res) {
          this.TDList = res.data.onlineList
          this.getNowData(0)
        }
      });
    },
    // 得到当前点击的通道数据
    getNowData (key) {
      // 重置提交金额
      this.amount = ''
      // 改变选中下标
      this.checkIndexTD = key
      // 改变选中数据
      this.nowTDData = this.TDList[key]

      if (this.nowTDData.isFixedAmount === 3) {//为设置可选固定金额
        this.isChangeMoney = this.nowTDData.fixedAmount.split(',');
        this.clickMoneyIndex = -1;
      } else if (this.nowTDData.isFixedAmount === 2) {//为设置单一固定金额
        this.amount = this.nowTDData.fixedAmount;
      }
    },
    // 点击可选固定金额改变
    moneyChange (val, key) {
      this.amount = val;
      this.clickMoneyIndex = key;
    },
    sureBtn () {
      if (!this.amount) {
        this.toastFun(this.$t("canNotMoney"));
      } else {
        this.isDisabled = true;
        //改变提交格式
        let params = new URLSearchParams();
        params.append('payId', this.nowTDData.id);
        params.append('bankCode', this.nowTDData.payType);
        params.append('amount', this.amount);

        this.$axiosPack.post("/userCenter/finance/pay.do", { 'params': params, 'load': [true, null, this.$t("sureBanksubmiting")] }).then(res => {
          this.isDisabled = false;
          if (res) {
            var form = $("#rechargeSubmitForm");
            form.empty();
            var data = res.data;
            if (res.data.returnType === "qrcodeUrl") {
              form.attr("action", play + "/onlinePay/qrcodeRedirect.do");
              form.append($("<input type='hidden' name='rechargeSubmitFormData' value='" + data.url + "'/>"));
              form.append($("<input type='hidden' name='rechargeSubmitOrderId' value='" + data.orderId + "' />"));
              form.append($("<input type='hidden' name='rechargeSubmitPayName' value='" + data.payName + "' />"));
              form.append($("<input type='hidden' name='rechargeSubmitPayType' value='" + data.payType + "' />"));
              form.append($("<input type='hidden' name='rechargeSubmitOrderTime' value='" + data.orderTime + "' />"));
              form.append($("<input type='hidden' name='rechargeSubmitPayAmount' value='" + data.payAmount + "' />"));
              form.append($("<input type='hidden' name='rechargeSubmitPayFlag' value='" + data.flag + "' />"));
              form.submit();
              form.empty();
            } else if (res.data.returnType === "postSubmit") {
              form.attr("action", data.url);
              if (data.form_method == "get") {
                form.attr("method", "get");
              }
              $.each(data.dataMap, function (key, value) {
                form.append($("<input hidden type='text' name='" + key + "' value='" + value + "'/>"));
              });
              form.submit();
              form.empty();
            } else {
              if (res.data.returnType === "href") {
                // window.location.href = data.url;
                // window.open(data.url, "_blank");
                location.href = data.url;
              } else if (res.data.returnType === "write") {
                var newwindow = window.open('');
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
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.online-ponent {
  .fangshiImg {
    margin: 0 auto;
  }
}
</style>


