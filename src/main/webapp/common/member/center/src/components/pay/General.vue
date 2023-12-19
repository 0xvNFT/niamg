<template>
  <div class="general-ponent">
    <template v-if="data.bankList && data.bankList.length">
      <!-- 支付方式 -->
      <p class="hint">
        <span class="num">1</span>{{ $t("payCheckFS") }}：{{
          data.payTipsDepositGeneral
        }}
      </p>
      <div class="list">
        <div class="item" v-for="(val, key) in data.bankList" :key="key" @click="changeBank(val, key)"
          :class="bankIndex === key ? 'checked' : ''">
          <img :src="val.icon" alt="" v-if="val.icon" class="icon" />
          <i class="iconfont iconyinxingqia" v-else></i>
          <div class="info">
            <!-- 类型 -->
            <p style="margin: 3px;">{{ configJsFlag("typeText") }}：{{ val.bankName }}</p>
            <!-- 姓名 -->
            <template v-if="val.bankName != 'USDT'">
              <p style="margin: 3px;">
                {{ $t("nameText") }}：{{ val.realName }}
                <el-button size="mini" style="margin-left: 15px;" class="tag-read" @click.native="copy()"
                  :data-clipboard-text="val.realName">{{ $t("copyText") }}</el-button>
              </p>
              <!-- 银行卡号 / tron链地址 -->
              <p style="margin: 3px;">
                {{ $t("accountText") }}：{{ val.bankCard
                }}<el-button size="mini" style="margin-left: 15px;" class="tag-read" @click.native="copy()"
                  :data-clipboard-text="val.bankCard">{{ $t("copyText") }}</el-button>
              </p>
              <!-- 银行地址 -->
              <p style="margin: 3px;">
                {{ $t("addressText") }}：{{ val.bankAddress
                }}<el-button size="mini" style="margin-left: 15px;" class="tag-read" @click.native="copy()"
                  :data-clipboard-text="val.bankAddress">{{ $t("copyText") }}</el-button>
              </p>
            </template>
            <template v-else>
              <!-- 银行卡号 / tron链地址 -->
              <p style="margin: 3px;margin-top: 35px;">
                {{ $t("usdtText") }}：{{ val.bankCard
                }}<el-button size="mini" style="margin-left: 15px;" class="tag-read" @click.native="copy()"
                  :data-clipboard-text="val.bankCard">{{ $t("copyText") }}</el-button>
              </p>
            </template>
          </div>
          <img src="../../assets/images/common/xz-icon.png" alt="" v-if="bankIndex === key" class="checkImg" />
        </div>
      </div>
      <!-- 二维码信息 -->

      <template v-if="bankCheckData.qrCodeImg && showQrCodeImg">
        <p class="hint"><span class="num">2</span>{{ $t("pleaseQR") }}：</p>
        <template v-if="bankCheckData.bankName === 'USDT'">
          <!-- 汇率 -->
          <div class="name">
            {{ $t("rateText") }}：{{ payTipsDepositUSDTRate }}
          </div>
        </template>
        <!-- usdt充值说明 -->
        <p v-if="usdtRemark" style="color:red;margin-top: 10px;">
          {{ usdtRemark }}
        </p>
        <p v-else style="color:red;margin-top: 10px;">
          {{ $t("QRHint") }}
        </p>
        <div class="qrCodeDiv">
          <img :src="bankCheckData.qrCodeImg" alt="" class="qrCodeImg" />
        </div>
      </template>
      <!-- 充值数据 -->
      <template v-if="bankCheckData.bankName != 'USDT'">
        <p class="hint">
          <span class="num">{{ bankCheckData.qrCodeImg ? 3 : 2 }}</span>{{ $t("putTransferInfo") }}：
        </p>
        <div class="inputDiv">
          <div class="left">
            <template>
              <!-- 存入金额 -->
              <div class="name">{{ $t("depositMoney") }}：</div>
              <!-- 订单备注 -->
              <div class="name">{{ $t("orderRemark") }}：</div>
            </template>
            <!-- <span class="name">{{ $t("belongsBank") }}：</span> -->
          </div>
          <div class="right">
            <template>
              <!-- 订单备注 -->
              <el-input v-model="amount" class="outInput" clearable size="small"></el-input>
              <el-input v-model="depositName" :placeholder="$t('orderRemarkHint')" class="outInput" clearable
                size="small"></el-input>
              <!-- <el-input v-model="belongsBank" class="outInput" clearable size="small"></el-input> -->
              <el-button type="primary" @click="sureBtn" round>{{
                $t("confirm")
              }}</el-button>
            </template>
          </div>
        </div>
      </template>
      <!-- 提示信息 -->
      <div class="hintDiv">
        <p class="icon">
          <i class="iconfont iconjinggao"></i>{{ $t("rechargeHint") }}：
        </p>
        <p class="line">
          {{ $t("hintMinMoney") }} {{ bankCheckData.min }}
          <template v-if="onOffBtn.stationCode === 'c001' &&
            (onOffBtn.lang === 'cn' || onOffBtn.lang === 'en')
            ">{{ $t("c001UnitText") }}</template>
          <template v-else-if="bankCheckData.bankName === 'USDT'">U</template>
          <template v-else>{{ onOffBtn.currency }}</template>
        </p>
        <p class="line">
          {{ $t("hintMaxMoney") }} {{ bankCheckData.max }}
          <template v-if="onOffBtn.stationCode === 'c001' &&
            (onOffBtn.lang === 'cn' || onOffBtn.lang === 'en')
            ">{{ $t("c001UnitText") }}</template>
          <template v-else-if="bankCheckData.bankName === 'USDT'">U</template>
          <template v-else>{{ onOffBtn.currency }}</template>
        </p>
        <p class="line" style="color: red">{{ bankCheckData.remark }}</p>
      </div>
    </template>
    <div v-else>{{ $t("notSet") }}</div>
    <!-- 激活usdt -->
    <el-dialog :title="$t('hintText')" :visible.sync="dialogVisible" width="30%">
      <p style="margin-bottom: 5px;">{{ remark }}</p>
      <div class="qrCodeImg">
        <img :src="usdtInfo.qrCodeImg" alt="" style="width: 100%;" />
      </div>
      <p style="text-align: center;">
        {{ usdtInfo.bankCard
        }}<el-button size="mini" style="margin-left: 15px;" class="tag-read" @click.native="copy()"
          :data-clipboard-text="usdtInfo.bankCard">{{ $t("copyText") }}</el-button>
      </p>
      <p style="margin-top: 10px;">
        {{ $t("transferMoney") }}：<span style="color: #3a8ee6;">{{
          initTrx
        }}</span>TRX
      </p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="dialogVisible = false">{{
          $t("confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
import Clipboard from "clipboard";

export default {
  data() {
    return {
      data: "", //数据
      bankIndex: 0, //选中的银行卡下标
      bankCheckData: "", //选中的银行卡数据
      amount: "", //存入金额
      depositName: "", //存款人姓名
      // bankWay: 1,//转账类型
      belongsBank: "", //所属分行
      // usdtID: '',// usdt 区块链交易ID
      isDisabled: false, //确定按钮是否可用
      showQrCodeImg: true, //显示二维码
      payTipsDepositUSDTRate: "", //usdt汇率
      usdtInfoFlg: false, //是否请求usdt数据接口
      usdtRemark: "", //usdt充值说明(后台配置)
      dialogVisible: false, //激活usdt弹窗
      remark: "",
      initTrx: "", //激活usdt所需金额
      usdtInfo: {},
    };
  },
  props: ["hasGeberal", "tabIndex"],
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted() {
    // 获取一般入款支付
    this.rechargeInfo(true);
  },
  methods: {
    //复制
    copy() {
      var clipboard = new Clipboard(".tag-read");
      clipboard.on("success", (e) => {
        this.$notify({
          title: this.$t("successText"),
          message: this.$t("successCopy"),
          type: "success",
        });
        // 释放内存
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$notify.error({
          title: this.$t("errorText"),
          message: this.$t("failCopy"),
        });
        // 不支持复制
        console.log("该浏览器不支持自动复制");
        // 释放内存
        clipboard.destroy();
      });
    },
    //获取存款信息
    rechargeInfo(isFirst) {
      this.$axiosPack
        .post("/userCenter/finance/rechargeInfo.do")
        .then((res) => {
          if (res) {
            this.data = res.data;
            let usdtList = [];
            let allList = [];
            //如果只绑定USDT
            if (
              this.data.bankList.length === 1 &&
              this.data.bankList[0].payPlatformCode === "USDT"
            ) {
              this.usdtInfo = this.data.bankList[0];
              this.getTronLink();
            } else {
              //如果usdt存在 将usdt放到数据的最后一位
              res.data.bankList.filter((item) => {
                if (item.bankName === "USDT") {
                  usdtList.push(item);
                  this.usdtInfo = item;
                } else {
                  allList.push(item);
                }
              });
              res.data.bankList = allList;
              res.data.bankList.push(...usdtList);
              //判断是否配置USDT
              if (usdtList.length) this.rechargeUsdtInfo();
            }
            //isFirst 为 true  默认选中第一个数据
            if (isFirst) {
              if (res.data.bankList && res.data.bankList.length) {
                this.bankCheckData = res.data.bankList[0];
              } else {
                this.$emit("update:tabIndex", "2");
                this.$emit("update:hasGeberal", false);
              }
            }
          }
        });
    },
    //获取usdt信息
    rechargeUsdtInfo() {
      this.$axiosPack.post("/userCenter/finance/usdtInfo.do").then((res) => {
        if (res) {
          this.payTipsDepositUSDTRate = res.data.depositRate;
          this.usdtRemark = res.data.remark;
        }
      });
    },
    //获取用户TronLink信息
    getTronLink() {
      this.$axiosPack
        .post("/userCenter/tronLink/getUserTronLink.do")
        .then((res) => {
          if (res) {
            //1未绑定  2已绑定
            if (res.data.content.bindStatus == 1) {
              this.remark = res.data.content.remark;
              this.initTrx = res.data.content.initTrx;
              this.dialogVisible = true;
              this.showQrCodeImg = false;
            } else {
              this.showQrCodeImg = true;
            }
          } else {
            this.showQrCodeImg = false;
            this.$confirm(this.$t("sureSetTronLink"), this.$t("TronLinkHint"), {
              confirmButtonText: this.$t("confirm"),
              cancelButtonText: this.$t("cancel"),
              type: "warning",
            })
              .then(() => {
                this.$router.replace("/usdtIndex");
              })
              .catch(() => {
                this.$router.go(-1);
              });
          }
        });
    },
    changeBank(val, key) {
      this.bankCheckData = val;
      this.bankIndex = key;
      this.amount = "";
      this.depositName = "";
      // this.belongsBank = ''
      // this.bankWay = 1
      if (
        this.bankCheckData.bankName === "USDT" ||
        this.bankCheckData.bankName === "usdt" ||
        this.bankCheckData.payPlatformCode === "USDT" ||
        this.bankCheckData.payPlatformCode === "usdt"
      ) {
        this.rechargeInfo();
        this.getTronLink();
      }
    },

    // changeType (key) {
    //   this.bankWay = key
    // },

    sureBtn() {
      if (!this.amount) {
        this.toastFun(this.$t("canNotMoney"));
      } else if (!this.depositName) {
        this.toastFun(this.$t("canNotName"));
      } else if (this.amount < this.bankCheckData.min) {
  
        this.toastFun(this.$t("lowestDeposit"));
      } else if (this.amount > this.bankCheckData.max) {
        this.toastFun(this.$t("highestDeposit"));
      } else {
        //改变提交格式
        let params = new URLSearchParams();
        params.append("payCode", "bank");
        params.append("amount", this.amount);
        params.append("payId", this.bankCheckData.id);
        params.append("depositName", this.depositName);
        // params.append('bankWay', this.bankWay);
        // params.append('belongsBank', this.belongsBank);
        params.append("payPlatformCode", this.bankCheckData.payPlatformCode);

        // usdt需要多传这几个参数
        if (
          this.bankCheckData.bankName === "USDT" ||
          this.bankCheckData.bankName === "usdt" ||
          this.bankCheckData.payPlatformCode === "USDT" ||
          this.bankCheckData.payPlatformCode === "usdt"
        ) {
          params.append("rate", this.payTipsDepositUSDTRate);
        }
        this.$axiosPack
          .post("/userCenter/finance/rechargeOfflineSave.do", {
            params: params,
            load: [true, 200, null],
          })
          .then((res) => {
            if (res) {
              this.$notify({
                title: "success",
                message: res.data.msg,
                type: "success",
              });
              this.amount = "";
              this.depositName = "";
              // this.belongsBank = ''
              // this.bankWay = 1
            }
          });
      }
    },
  },
  watch: {
    dialogVisible(newval, oldval) {
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
.online-ponent {
  .fangshiImg {
    margin: 0 auto;
  }
}

.qrCodeImg {
  width: 200px;
  margin: auto;
}
</style>
