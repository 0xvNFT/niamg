<template>
  <div class="usdtIndex-page">
    <p class="headTitle">
      {{ $t("usdtIndex") }}
    </p>
    <el-button v-if="btnFlag" type="danger" size="medium" @click="addUsdt">{{
      $t("addText")
    }}</el-button>
    <div class="list">
      <el-table :data="banks" style="width: 99%">
        <!-- <el-table-column
            prop="realName"
            :label="$t('bankRealName')"
          ></el-table-column> -->
        <!-- <el-table-column
              prop="bankName"
              :label="$t('bankName')"
            ></el-table-column> -->
        <el-table-column
          prop="cardNo"
          :label="$t('usdtText')"
        ></el-table-column>
        <el-table-column :label="$t('remarkText')">
          <template slot-scope="scope">
            {{ scope.row.remarks || "--" }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('bankTime')">
          <template slot-scope="scope">
            {{ dataFun(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('status')">
          <template slot-scope="scope">
            {{ scope.row.status === 2 ? $t("open") : $t("forbidden") }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('operate')">
          <template slot-scope="scope" v-if="scope.row.bankName === 'USDT'">
            <el-button type="danger" @click="deleteUsdt(scope.row.id)">{{
              $t("delete")
            }}</el-button>
          </template>
        </el-table-column>

        <div slot="empty">
          <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
      </el-table>
    </div>
    <!-- 新增USDT -->
    <el-dialog
      :title="$t('addUsdt')"
      :visible.sync="dialogShow"
      width="40%"
      :close-on-click-modal="false"
      class="add-dialog common_dialog"
    >
      <div class="dialog_cont">
        <div class="change-box">
          <div class="item">
            <span class="title">{{ $t("usdtText") }}：</span>
            <el-input v-model="cardNo" :placeholder="$t('putHint')"></el-input>
          </div>
          <div
            class="item"
            :style="data.currency === 'INR' ? '' : 'margin-bottom: 0'"
          >
            <span class="title">{{ $t("reconfirm") }}：</span>
            <el-input
              v-model="reCardNo"
              :placeholder="$t('sureText') + ' ' + $t('usdtText')"
            ></el-input>
          </div>
          <div
            class="item"
            style="margin-bottom: 0"
            v-if="data.currency === 'INR'"
          >
            <span class="title">IFSC：</span>
            <el-input
              v-model="bankAddress"
              :placeholder="$t('putHint')"
            ></el-input>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <button class="closeBtn themeBtn" @click="dialogShow = false">
          {{ $t("cancel") }}
        </button>
        <button class="sureBtn themeBtn" @click="sureBtn">
          {{ $t("confirm") }}
        </button>
      </span>
    </el-dialog>
    <!-- 激活usdt -->
    <el-dialog
      :title="$t('hintText')"
      :visible.sync="dialogVisible"
      width="30%"
      class="common_dialog"
    >
      <p style="margin-bottom: 5px">{{ usdtRemark }}</p>
      <div class="qrCodeImg">
        <img :src="usdtInfo.qrCodeImg" alt="" style="width: 100%" />
      </div>
      <p style="text-align: center">
        <span id="bankCard"> {{ usdtInfo.bankCard }}</span
        ><el-button
          size="mini"
          style="margin-left: 15px"
          class="tag-read"
          @click.native="copyName('bankCard')"
          >{{ $t("copyText") }}</el-button
        >
      </p>
      <p style="margin-top: 10px">
        {{ $t("transferMoney") }}：<span style="color: #3a8ee6">{{
          initTrx
        }}</span
        >TRX
      </p>
    </el-dialog>
  </div>
</template>

<script>
import DATE from "@/assets/js/date";

export default {
  data() {
    return {
      data: "", //
      tableData: [],
      dialogShow: false, //是否显示USDT弹窗
      bankList: [], //开户银行列表
      bankCode: "", //开户银行
      lastRealName: "", //最近一次绑定的银行卡开户姓名
      lastCardNo: "", //最近一次绑定的银行卡卡号
      realName: "", //开户人姓名
      // bankAddress: '',//开户行地址
      cardNo: "", //usdt地址
      reCardNo: "", //确认usdt地址
      bankAddress: "", //IFSC 印度语才需要
      bankName: "", //其他银行时的银行名字
      dialogVisible: false, //激活usdt弹窗
      usdtRemark: "",
      initTrx: "", //激活usdt所需金额
      usdtInfo: {},
      btnFlag: "",
      banks: [],
    };
  },
  computed: {
    //   ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted() {
    // 银行卡列表
    this.list();
    this.getTronLink();
    this.rechargeInfo();
  },
  methods: {
    // 复制
    copyName(val) {
      this.$publicJs.copyFun(val);
    },
    // USDT列表
    list() {
      this.$API.getUserUsdt().then((res) => {
        if (res) {
          this.data = res;
          this.btnFlag = res.canAddBank;
          this.banks = res.banks;
        }
      });
    },
    //充值信息
    rechargeInfo() {
      this.$API.rechargeInfo({ load: true }).then((res) => {
        if (res) {
          this.data = res;
          res.bankList.filter((item) => {
            if (item.bankName === "USDT") {
              this.usdtInfo = item;
            }
          });
        }
      });
    },
    //获取用户TronLink信息
    getTronLink() {
      this.$API.getTronLink().then((res) => {
        if (res) {
          //1未绑定  2已绑定
          if (res.content.bindStatus == 1) {
            this.dialogVisible = true;
            this.usdtRemark = res.content.remark;
            this.initTrx = res.content.initTrx;
          }
        }
      });
    },
    // 添加USDT按钮
    addUsdt() {
      // this.list();
      if (this.data.needPerfectContact) {
        this.$prompt(this.$t("bankOtherHint"), this.$t("phone"), {
          confirmButtonClass: "smallSureBtn themeBtn",
          cancelButtonClass: "smallCloseBtn themeBtn",
          confirmButtonText: this.$t("confirm"),
          cancelButtonText: this.$t("cancel"),
        })
          .then(({ value }) => {
            this.setPhone(value);
          })
          .catch(() => {});
      } else {
        this.dialogShow = true;
      }
    },
    // 删除USDT按钮
    deleteUsdt(id) {
      this.$confirm(this.$t("deleteUsdt"), this.$t("deleteUsdtInfo"), {
        confirmButtonClass: "smallSureBtn themeBtn",
        cancelButtonClass: "smallCloseBtn themeBtn",
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: "warning",
      })
        .then(() => {
          let params = {
            id: id,
          };
          this.$API.deleteUsdt(params).then((res) => {
            if (res) {
              this.$message.success(res.msg);
              // 银行卡列表
              this.list();
            }
          });
        })
        .catch(() => {});
    },
    // 绑定手机号
    setPhone(value) {
      //改变提交格式
      let params = {
        type: "phone",
        newContact: value,
      };
      this.$API.updateSecurityInfo(params).then((res) => {
        if (res) {
          this.$message.success(res.msg);
          // 银行卡列表
          this.list();
        }
      });
    },

    // 提交
    sureBtn() {
      //改变提交格式
      let params = {
        bankCode: "USDT",
        bankName: "USDT",
        cardNo: this.cardNo,
      };
      if (this.data.currency === "INR") params.bankAddress = this.bankAddress;
      if (this.lastRealName) params.lastRealName = this.lastRealName;
      if (this.lastCardNo) params.lastCardNo = this.lastCardNo;
      //判断usdt地址是否为空
      if (this.cardNo) {
        //判断两次usdt地址是否一致
        if (this.cardNo != this.reCardNo) {
          this.$message.error(this.$t("usdtText") + this.$t("notSame"));
          return;
        } else {
          this.$API.addUserBank(params).then((res) => {
            if (res.success) {
              this.dialogShow = false;
              this.$message.success(res.msg);
              this.list();
              this.getTronLink();
              this.dialogVisible = true;
            }
          });
        }
      } else {
        this.$message.error(this.$t("usdtText") + this.$t("canNotEmpty"));
        return;
      }
    },

    // 重置
    clearFun() {
      // this.bankCode = "";
      this.lastRealName = "";
      this.lastCardNo = "";
      this.realName = "";
      this.cardNo = "";
      this.reCardNo = "";
      this.bankAddress = "";
    },

    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val);
    },
  },
  watch: {
    dialogShow() {
      if (!this.dialogShow) this.clearFun();
    },
  },
};
</script>

<style lang="scss">
.usdtIndex-page {
  margin: 0 auto;
  // max-width: 1420px;
  width: 95%;
  .headTitle {
    color: #fff;
    font-family: Montserrat-Black;
    font-size: 1.4rem;
    font-weight: 700;
    line-height: 2rem;
    margin-bottom: 1.2rem;
    margin-top: 18px;
    text-align: center;
  }
  .el-button--primary {
    background-color: #0ec504;
    border: #0ec504;
  }
  .list {
    margin-top: 10px;
  }
  // .add-dialog {
  //   .dialog_cont {
  //     .title {
  //       min-width: 200px;
  //     }
  //   }
  // }
  .qrCodeImg {
    width: 200px;
    margin: auto;
  }
}
</style>
