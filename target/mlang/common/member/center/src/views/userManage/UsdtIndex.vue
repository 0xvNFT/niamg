<template>
  <div class="moneyChangeHis-page">
    <el-button v-if="btnFlag" type="primary" size="medium" @click="addUsdt">{{
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
      </el-table>
    </div>
    <!-- 新增USDT -->
    <el-dialog
      :title="$t('addUsdt')"
      :visible.sync="dialogShow"
      width="40%"
      :close-on-click-modal="false"
      class="add-dialog"
    >
      <div class="cont">
        <!-- <el-card
            shadow="always"
            style="margin-bottom: 10px"
            v-if="data.banks && data.banks.length"
          >
            <p class="hint">{{ $t("bankHint") }}</p>
            <div class="item">
              <span class="title">{{ $t("bankRealName") }}：</span>
              <el-input
                v-model="lastRealName"
                :placeholder="$t('lastBankName')"
              ></el-input>
            </div>
            <div class="item" style="margin-bottom: 0">
              <span class="title">{{ $t("bankCardNo") }}：</span>
              <el-input
                v-model="lastCardNo"
                :placeholder="$t('lastBankCardNo')"
              ></el-input>
            </div>
          </el-card> -->
        <!-- <p class="hint">提示：<span style="color:red"> * </span>为必填信息，如需要删除请联系客服提供银行卡照片和充值截图进行处理！</p> -->
        <el-card shadow="always" class="change-box">
          <!-- <div class="item">
            <span class="title">USDT：</span>
            <el-select
              v-model="bankCode"
              :placeholder="$t('pleaseBank')"
              disabled
            >
              <el-option
                v-for="item in data.bankList"
                :key="item.code"
                :label="item.name"
                :value="item.code + '-' + item.name"
              >
              </el-option>
            </el-select>
          </div> -->
          <!-- <div class="item">
              <span class="title">{{ $t("bankAddress") }}：</span>
              <el-input v-model="bankAddress" :placeholder="$t('putAddress')"></el-input>
            </div> -->
          <div class="item">
            <span class="title">{{ $t("usdtText") }}：</span>
            <el-input v-model="cardNo" :placeholder="$t('putHint')"></el-input>
          </div>
          <div
            class="item"
            :style="data.currency === 'INR' ? '' : 'margin-bottom: 0'"
          >
            <span class="title">{{ $t("rePutUsdtNo") }}：</span>
            <el-input
              v-model="reCardNo"
              :placeholder="$t('sureText') + ' ' + $t('usdtText')"
            ></el-input>
          </div>
          <!-- <div class="item" style="margin-bottom: 0" v-if="data.currency === 'INR'">
            <span class="title">IFSC：</span>
            <el-input v-model="bankAddress" :placeholder="$t('putHint')"></el-input>
          </div> -->
        </el-card>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn">{{
          $t("confirm")
        }}</el-button>
      </span>
    </el-dialog>
    <!-- 激活usdt -->
    <el-dialog
      :title="$t('hintText')"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <p style="margin-bottom: 5px;">{{ usdtRemark }}</p>
      <div class="qrCodeImg">
        <img :src="usdtInfo.qrCodeImg" alt="" style="width: 100%;" />
      </div>
      <p style="text-align: center;">
        {{ usdtInfo.bankCard
        }}<el-button
          size="mini"
          style="margin-left: 15px;"
          class="tag-read"
          @click.native="copy()"
          :data-clipboard-text="usdtInfo.bankCard"
          >{{ $t("copyText") }}</el-button
        >
      </p>
      <p style="margin-top: 10px;">
        {{ $t("transferMoney") }}：<span style="color: #3a8ee6;">{{
          initTrx
        }}</span
        >TRX
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
      data: "", //
      tableData: [],
      dialogShow: false, //是否显示USDT弹窗
      bankList: [], //开户银行列表
      bankCode: "", //开户银行
      lastRealName: "", //最近一次绑定的银行卡开户姓名
      lastCardNo: "", //最近一次绑定的银行卡卡号
      realName: "", //开户人姓名
      // bankAddress: '',//开户行地址
      cardNo: "", //银行卡号
      reCardNo: "", //确认银行
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
    ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted() {
    // 银行卡列表
    this.list();
    this.getTronLink();
    this.rechargeInfo();
  },
  methods: {
    //复制
    copy() {
      let clipboard = new Clipboard(".tag-read");
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
    // 银行卡列表
    list() {
      this.$axiosPack
        .get("/userCenter/userBank/list.do?type=USDT")
        .then((res) => {
          if (res) {
            this.data = res.data;
            this.btnFlag = res.data.canAddBank;
            this.banks = res.data.banks;
            // let arr = res.data.bankList.filter((item) => item.name == "USDT");
            // this.bankCode = arr[0].code + "-" + arr[0].name;
            // console.log(this.bankCode)
          }
        });
    },
    rechargeInfo() {
      this.$axiosPack
        .post("/userCenter/finance/rechargeInfo.do")
        .then((res) => {
          if (res) {
            this.data = res.data;
            res.data.bankList.filter((item) => {
              if (item.bankName === "USDT") {
                this.usdtInfo = item;
              }
            });
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
              this.dialogVisible = true;
              this.usdtRemark = res.data.content.remark;
              this.initTrx = res.data.content.initTrx;
              // this.$confirm(res.data.content.remark, this.$t("hintText"), {
              //   confirmButtonText: this.$t("confirm"),
              //   cancelButtonText: this.$t("cancel"),
              //   type: "warning",
              // })
              //   .then(() => {})
              //   .catch(() => {});
            } else {
            }
          }
        });
    },
    // 添加USDT按钮
    addUsdt() {
      // this.list();
      if (this.data.needPerfectContact) {
        this.$prompt(this.$t("bankOtherHint"), this.$t("phone"), {
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
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: "warning",
      })
        .then(() => {
          this.$axiosPack
            .get("/userCenter/userBank/delUserBank.do?" + "id=" + id)
            .then((res) => {
              if (res) {
                this.$successFun(res.data.msg);
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
      let params = new URLSearchParams();
      params.append("type", "phone");
      params.append("newContact", value);

      this.$axiosPack
        .post("/userCenter/updateSecurityInfo.do", { params: params })
        .then((res) => {
          if (res) {
            this.$successFun(res.data.msg);
            // 银行卡列表
            this.list();
          }
        });
    },

    // 提交
    sureBtn() {
      // let bank = this.bankCode.split("-");
      //改变提交格式
      let params = new URLSearchParams();
      params.append("bankCode", "USDT");
      params.append("bankName", "USDT");
      params.append("cardNo", this.cardNo);
      // if (this.data.currency === "INR")
      //   params.append("bankAddress", this.bankAddress);
      if (this.lastRealName) params.append("lastRealName", this.lastRealName);
      if (this.lastCardNo) params.append("lastCardNo", this.lastCardNo);
      //判断usdt地址是否为空
      if (this.cardNo) {
        //判断两次usdt地址是否一致
        if (this.cardNo != this.reCardNo) {
          this.$message.error(this.$t("usdtText") + this.$t("notSame"));
          return;
        } else {
          this.$axiosPack
            .post("/userCenter/userBank/bankAddSave.do", { params: params })
            .then((res) => {
              if (res) {
                this.dialogShow = false;
                this.$successFun(res.data.msg);
                // 银行卡列表
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
      return this.$dataJS.dateChange(val);
    },
  },
  watch: {
    dialogShow() {
      if (!this.dialogShow) this.clearFun();
    },
  },
};
</script>

<style lang="scss" scoped>
.moneyChangeHis-page {
  .el-button--primary {
    background-color: #0ec504;
    border: #0ec504;
  }

  .list {
    margin-top: 10px;
  }

  .add-dialog {
    .cont {
      .hint {
        color: red;
        text-align: right;
      }

      .item {
        display: flex;
        align-items: center;
        margin-bottom: 10px;

        .title {
          min-width: 200px !important;
          text-align: right;
        }

        .el-input {
          // width: 150px;
        }
      }
    }
  }

  .qrCodeImg {
    width: 200px;
    margin: auto;
  }
}
</style>
