<template>
  <div class="pay-types">
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane :label="type.name" v-for="(type, i) in rechargeInfoData" :key="i" :name="String(i)">
        <!-- 请选择支付方式 -->
        <el-tooltip class="item" effect="dark" :content="payTipsDepositGeneral" placement="top-start">
          <div class="point">
            {{ $t("payCheckFS") }}：{{ payTipsDepositGeneral }}
          </div>
        </el-tooltip>
        <div class="cardBox">
          <el-card class="box-card" :class="item.id === curId ? 'blueBorder' : ''" @click.native="chooseCard(item)" v-for="(item, k) in rechargeInfoData[i].list" :key="k">
            <div slot="header" class="clearfix">
              <span>{{ item.bankName }}</span>
            </div>
            <div class="content" v-if="item.payPlatformCode != 'USDT'">
              <span>{{ $t("nameText") }}</span>
              <div class="right">
                <span id="copyName1">{{ item.realName }}</span>
                <div class="user_copy" @click="copyName('copyName1')">
                  <i class="el-icon-copy-document"></i>
                </div>
              </div>
            </div>
            <div class="content">
              <span>{{ $t("accountText") }}</span>
              <div class="right">
                <span id="copyName2">{{ item.bankCard }}</span>
                <div class="user_copy" @click="copyName('copyName2')">
                  <i class="el-icon-copy-document"></i>
                </div>
              </div>
            </div>
            <div class="content" v-if="item.payPlatformCode != 'USDT'">
              <span>{{ $t("addressText") }}</span>
              <div class="right">
                <span id="copyName3">{{ item.bankAddress }}</span>
                <div class="user_copy" @click="copyName('copyName3')">
                  <i class="el-icon-copy-document"></i>
                </div>
              </div>
            </div>
          </el-card>
        </div>
        <!-- 填写转账资料 -->
        <div class="contentBox">
          <!-- 二维码 -->
          <div class="qrBox" v-if="qrCodeImg && showQrCodeImg">
            <div class="point">{{ $t("pleaseQR") }}：</div>
            <p style="color: red;margin-bottom: 10px;padding-left: 10px;">
              {{ $t("QRHint") }}
            </p>
            <div class="qr">
              <img :src="qrCodeImg" alt="二维码" />
            </div>
          </div>
          <!-- 银行卡填写 -->
          <div class="bankBox" v-if="!isUSDT">
            <div class="point">{{ $t("putTransferInfo") }}：</div>
            <div class="inputMoney">
              <el-input v-model="amount" v-amountFormat:amount  :placeholder="$t('pleaseInput')" :clearable="true">
                <template slot="prepend">{{ $t("depositMoney") }}</template>
              </el-input>
            </div>
            <div class="inputRemark">
              <span></span>
              <el-input v-model="depositName" :placeholder="$t('orderRemarkHint')" :clearable="true" spellcheck="false">
                <template slot="prepend">{{ $t("orderRemark") }}</template>
              </el-input>
            </div>
            <!-- 提交 -->
            <div class="inputBtn">
              <button class="confirm" @click="sureBtn">
                {{ $t("confirm") }}
              </button>
            </div>
          </div>
          <!-- USDT填写 -->
          <div class="usdtBox" v-else>
            <p>
              {{ $t("rateText") }}：<span style="font-weight: 600">{{
                usdtaRate
              }}</span>
            </p>
            <p v-if="usdtRemark" style="color: red">{{ usdtRemark }}</p>
          </div>
        </div>
        <!-- 转账须知 -->
        <div class="tipBox">
          <div class="point">
            <i class="el-icon-warning-outline"></i><span>{{ $t("rechargeHint") }}：</span>
          </div>
          <p>
            {{ $t("hintMinMoney") }}
            <span>&nbsp;{{ minMoney | amount }}&nbsp;</span>
            <span v-if="!isUSDT">{{ onOffBtn.currency }}</span>
            <span v-else>U</span>
          </p>
          <p>
            {{ $t("hintMaxMoney") }}
            <span>&nbsp;{{ maxMoney | amount }}&nbsp;</span>
            <span v-if="!isUSDT">{{ onOffBtn.currency }}</span>
            <span v-else>U</span>
          </p>
          <p style="color: red">{{ remark }}</p>
        </div>
      </el-tab-pane>
    </el-tabs>
    <!-- 激活usdt -->
    <el-dialog :title="$t('hintText')" :visible.sync="dialogVisible" width="30%" class="common_dialog">
      <p style="margin-bottom: 5px">{{ tip }}</p>
      <div class="qrCodeImg">
        <img :src="qrCodeImg" alt="" style="width: 100%" />
      </div>
      <p style="text-align: center">
        <span id="usdtTron"> {{ usdtTron }}</span>
        <el-button size="mini" style="margin-left: 15px" class="tag-read"  @click.native="copyName('usdtTron')" >{{ $t("copyText") }}</el-button>
      </p>
      <p style="margin-top: 10px">
        {{ $t("transferMoney") }}：<span style="color: #3a8ee6">{{
          initTrx || 0
        }}</span> TRX
      </p>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "index",
  data () {
    return {
      rechargeInfoData: [], //一般入款支付数据
      activeName: "0",
      curId: 0, //选中后 显示蓝色边框
      amount: "", //输入金额
      depositName: "", //订单备注
      minMoney: "", //最小金额
      maxMoney: "", //最大金额
      remark: "", //转账须知说明
      payTipsDepositGeneral: "", //请选择支付方式提示语
      isUSDT: false, //是否为USDT
      usdtaRate: "", //usdt汇率
      usdtTron: "", //usdt地址
      usdtRemark: "", //usdt充值说明(后台配置)
      payPlatformCode: "", //支付方式code
      qrCodeImg: "", //二维码
      showQrCodeImg: true, //显示二维码
      initTrx: "", //激活usdt 所需的金额
      tip: "", //激活usdt提示
      dialogVisible: false, //激活usdt弹窗
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  created () { },
  mounted () {
    this.rechargeInfo();
  },
  methods: {
    // 获取一般入款支付
    rechargeInfo (isFirst) {
      this.$API.rechargeInfo({ load: true }).then((res) => {
        if (res) {
          this.payTipsDepositGeneral = res.payTipsDepositGeneral;
          //如果usdt存在 将usdt放到数据的最后一位
          let usdtList = [];
          let allList = [];
          res.bankList.filter((item) => {
            if (item.payPlatformCode === "USDT") {
              usdtList.push(item);
            } else {
              allList.push(item);
            }
          });
          res.bankList = allList;
          res.bankList.push(...usdtList);
          let arr = [];
          //追加分类
          for (let i = 0; i < res.bankList.length; i++) {
            const item = res.bankList[i];
            if (arr[item.payPlatformCode]) {
              arr[item.payPlatformCode].push(item);
            } else {
              arr[item.payPlatformCode] = [item];
            }
          }
          for (var i in arr) {
            let list = {
              name: i, //获得对象中键值对的“键”
              list: arr[i], //获得对象中键值对的“值”
            };
            this.rechargeInfoData.push(list);
            if (this.rechargeInfoData.length) {
              let rechargeInfoData = this.rechargeInfoData[0].list[0];
              this.curId = rechargeInfoData.id;
              this.minMoney = rechargeInfoData.min;
              this.maxMoney = rechargeInfoData.max;
              this.remark = rechargeInfoData.remark;
              this.payPlatformCode = rechargeInfoData.payPlatformCode;
              this.qrCodeImg = rechargeInfoData.qrCodeImg;
              if (rechargeInfoData.payPlatformCode === "USDT") {
                this.getTronLink();
                this.getUsdtInfo();
                this.isUSDT = true;
                this.usdtTron = rechargeInfoData.bankCard;
              } else {
                this.isUSDT = false;
              }
            }
          }
        }
      });
    },
    // 获获取usdt信息
    getUsdtInfo () {
      this.$API.getUsdtInfo().then((res) => {
        if (res) {
          this.usdtaRate = res.depositRate;
          this.usdtRemark = res.remark;
        }
      });
    },
    //获取用户TronLink信息
    getTronLink () {
      return new Promise((resolve) => {
        this.$API.getTronLink().then((res) => {
          if (res.success) {
            //1未绑定  2已绑定
            if (res.content.bindStatus == 1) {
              this.tip = res.content.remark;
              this.initTrx = res.content.initTrx;
              this.dialogVisible = true;
              this.showQrCodeImg = false;
            } else {
              this.showQrCodeImg = true;
            }
            resolve(true);
          } else {
            this.$confirm(this.$t("sureSetTronLink"), this.$t("TronLinkHint"), {
              confirmButtonClass: "smallSureBtn themeBtn",
              cancelButtonClass: "smallCloseBtn themeBtn",
              confirmButtonText: this.$t("confirm"),
              cancelButtonText: this.$t("cancel"),
              type: "warning",
            }).then(() => {
              this.$router.replace("/usdtIndex");
            }).catch(() => {
              this.activeName = '0'
              resolve(false);
            });
          }
        });
      });
    },
    //选择存款类型
    handleClick (tab, event) {
      let i = this.activeName;
      let list = this.rechargeInfoData[i].list[0];
      this.curId = list.id;
      this.minMoney = list.min;
      this.maxMoney = list.max;
      this.remark = list.remark;
      this.payPlatformCode = list.payPlatformCode;
      this.qrCodeImg = list.qrCodeImg;
      if (list.payPlatformCode === "USDT") {
        this.getTronLink().then((onUSDT) => {
          this.isUSDT = onUSDT; // 是否展示USDT（由getTronLink函数中的resolve返回）
        });
        this.getUsdtInfo();
        this.usdtTron = list.bankCard;
      } else {
        this.isUSDT = false;
      }
    },
    // 复制名字
    copyName (val) {
      this.$publicJs.copyFun(val);
    },
    //选择银行卡
    chooseCard (val) {
      this.curId = val.id;
      this.minMoney = val.min;
      this.maxMoney = val.max;
      this.remark = val.remark;
      this.payPlatformCode = val.payPlatformCode;
    },
    //提交
    sureBtn () {
      if (!this.amount) {
        this.$message.error(this.$t("canNotMoney"));
      } else if (!this.depositName) {
        this.$message.error(this.$t("canNotName"));
      } else if ( Number(this.amount.replaceAll(',', '')) < this.minMoney ) {
        this.$message.error(this.$t('lowestDeposit'));
      } else if ( Number(this.amount.replaceAll(',', '')) > this.maxMoney ) {
        this.$message.error(this.$t('highestDeposit'));  
      } else {
        //改变提交格式
        let params = {
          payCode: "bank",
          amount: this.amount,
          payId: this.curId,
          depositName: this.depositName,
          payPlatformCode: this.payPlatformCode,
        };

        // usdt需要多传这几个参数
        if (this.payPlatformCode === "USDT") {
          params.rate = this.usdtaRate;
        }
        this.$API.rechargeOfflineSave(params).then((res) => {
          if (res.success) {
            this.$notify({
              title: "success",
              message: res.msg,
              type: "success",
            });
            this.amount = "";
            this.depositName = "";
          }
        });
      }
    },
  },
  watch: {
    dialogVisible (newval, oldval) {
      if (newval) {
        this.showQrCodeImg = false;
      } else {
        this.showQrCodeImg = true;
      }
    },
  },
};
</script>

<style lang="scss">
</style>
