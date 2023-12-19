<template>
  <div class="moneyChangeHis-page">
    <el-button
      type="primary"
      size="medium"
      @click="addBank"
      v-if="data.canAddBank"
      >{{ $t("addText") }}</el-button
    >
    <div class="list">
      <el-table :data="data.banks" style="width: 99%">
        <el-table-column
          prop="realName"
          :label="configJsFlag('bankRealName')"
        ></el-table-column>
        <el-table-column
          prop="bankName"
          :label="$t('bankName')"
        ></el-table-column>
        <el-table-column
          prop="cardNo"
          :label="configJsFlag('bankCardNo')"
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
        <!-- <el-table-column :label="$t('operate')">
          <template slot-scope="scope" v-if="scope.row.bankName === 'USDT'">
            <el-button type="danger" @click="deleteBank(scope.row.id)"
              >删除</el-button
            >
          </template>
        </el-table-column> -->
      </el-table>
    </div>
    <!-- 新增银行卡 -->
    <el-dialog
      :title="$t('addBank')"
      :visible.sync="dialogShow"
      width="40%"
      :close-on-click-modal="false"
      class="add-dialog"
    >
      <div class="cont">
        <el-card
          shadow="always"
          style="margin-bottom: 10px"
          v-if="data.banks && data.banks.length"
        >
          <p class="hint">{{ $t("bankHint") }}</p>
          <div class="item">
            <span class="title">
              {{ configJsFlag("bankRealName") }}
              ：
            </span>
            <el-input
              v-model="lastRealName"
              :placeholder="$t('lastBankName')"
            ></el-input>
          </div>
          <div class="item" style="margin-bottom: 0">
            <span class="title">{{ configJsFlag("bankCardNo") }}：</span>
            <el-input
              v-model="lastCardNo"
              :placeholder="configJsFlag('bankCardNo')"
            ></el-input>
          </div>
        </el-card>
        <!-- <p class="hint">提示：<span style="color:red"> * </span>为必填信息，如需要删除请联系客服提供银行卡照片和充值截图进行处理！</p> -->
        <el-card shadow="always" class="change-box">
          <div
            class="pixTip"
            style="margin-bottom: 10px;"
            v-if="bankCode.split('-')[0] === 'PIX'"
          >
            <p class="hint">
              {{ configJsFlag("pixTip") }}
            </p>
            <p class="hint">
              {{ configJsFlag("pixTip1") }}
            </p>
            <p class="hint">
              {{ configJsFlag("pixTip2") }}
            </p>
            <p class="hint">
              {{ configJsFlag("pixTip3") }}
            </p>
            <p class="hint">
              {{ configJsFlag("pixTip4") }}
            </p>
          </div>
          <div class="item" v-if="this.onOffBtn.stationCode != 'bx101'">
            <span class="title">{{ $t("lastBank") }}：</span>
            <el-select
              v-model="bankCode"
              :placeholder="$t('pleaseBank')"
              @change="selectType"
            >
              <el-option
                v-for="item in data.bankList"
                :key="item.code"
                :label="item.name"
                :value="item.code + '-' + item.name"
              >
              </el-option>
            </el-select>
          </div>
          <div
            class="item"
            v-if="showBankName && this.onOffBtn.stationCode != 'bx101'"
          >
            <span class="title">{{ $t("lastBank") }}：</span>
            <el-input
              v-model="bankName"
              :placeholder="$t('inputBankName')"
            ></el-input>
          </div>
          <div class="item">
            <span class="title">{{ configJsFlag("bankRealName") }}：</span>
            <el-input
              v-model="realName"
              :placeholder="$t('putBankRealName')"
            ></el-input>
          </div>
          <!-- <div class="item">
            <span class="title">{{ $t("bankAddress") }}：</span>
            <el-input v-model="bankAddress" :placeholder="$t('putAddress')"></el-input>
          </div> -->
          <div class="item">
            <span class="title">{{ configJsFlag("bankCardNo") }}：</span>
            <el-input v-model="cardNo" :placeholder="$t('putHint')"></el-input>
          </div>
          <div
            class="item"
            :style="data.currency === 'INR' ? '' : 'margin-bottom: 0'"
          >
            <span class="title">{{ configJsFlag("sureBank") }}：</span>
            <el-input
              v-model="reCardNo"
              :placeholder="configJsFlag('rePutBankNo')"
            ></el-input>
          </div>
          <!-- 如果填巴西手机号要加+55 -->
          <div class="item" v-if="showPhoneNumerAdd55" style="margin-top:10px;">
            <span class="title">
              <i class="el-icon-warning-outline" style="color:red;"></i>
            </span>
            <span style="color: red;">{{ $t("phoneNumerAdd55") }}</span>
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
        </el-card>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn">{{
          $t("confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  data() {
    return {
      data: "", //
      tableData: [],
      dialogShow: false, //是否显示新增银行卡弹窗
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
      showBankName: false, //是否显示银行名字
      pixTipFlag: false, //是否显示pix提示
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
    //当前站点前端配置信息
    configJs() {
      return this.$store.state.configJs;
    },
    showPhoneNumerAdd55() {
      if (this.onOffBtn) {
        //获取站点前缀
        let stationCode = this.onOffBtn.stationCode.slice(0, 2);
        if (stationCode === "bx") {
          return true;
        }
      }
    },
  },
  components: {},
  mounted() {
    // 银行卡列表
    this.list();
  },
  methods: {
    // 银行卡列表
    list() {
      this.$axiosPack
        .post("/userCenter/userBank/list.do?type=bank")
        .then((res) => {
          if (res) {
            this.data = res.data;
            let arr = res.data.bankList.filter((item) => item.name != "USDT");
            this.data.bankList = arr;
            //是否配置PIX
            // let result = res.data.bankList.find((obj) => obj.code === "PIX");
          }
        });
    },

    // 添加银行卡按钮
    addBank() {
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
    // 删除银行卡按钮
    // deleteBank(id) {
    //   this.$confirm(
    //     this.$t("deleteBank"),
    //     this.$t("deleteBankInfo"),
    //     {
    //       confirmButtonText: this.$t("confirm"),
    //       cancelButtonText: this.$t("cancel"),
    //       type: "warning",
    //     }
    //   )
    //     .then(() => {
    //       this.$axiosPack
    //         .get("/userCenter/userBank/delUserBank.do?"+'id='+id, )
    //         .then((res) => {
    //           if (res) {
    //             this.$successFun(res.data.msg);
    //             // 银行卡列表
    //             this.list();
    //           }
    //         });
    //     })
    //     .catch(() => {});
    // },
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
      let bank = this.bankCode.split("-");
      //改变提交格式
      let params = new URLSearchParams();
      //判断2次输入是否一致
      if (this.reCardNo != this.cardNo) {
        this.$message.error(this.$t("cardNumDifferent"));
        return;
      }
      //bx101主要推pix通道
      if (this.onOffBtn.stationCode === "bx101") {
        params.append("bankCode", "PIX");
        params.append("bankName", "PIX");
      } else {
        params.append("bankCode", bank[0]);
        if (this.showBankName) params.append("bankName", this.bankName);
        else params.append("bankName", bank[1]);
      }
      params.append("realName", this.realName);
      params.append("cardNo", this.cardNo);
      if (this.data.currency === "INR")
        params.append("bankAddress", this.bankAddress);
      if (this.lastRealName) params.append("lastRealName", this.lastRealName);
      if (this.lastCardNo) params.append("lastCardNo", this.lastCardNo);
      this.$axiosPack
        .post("/userCenter/userBank/bankAddSave.do", { params: params })
        .then((res) => {
          if (res) {
            this.dialogShow = false;
            this.$successFun(res.data.msg);
            // 银行卡列表
            this.list();
          }
        });
    },

    // 重置
    clearFun() {
      this.bankCode = "";
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
    //选择开户银行类型
    selectType() {},
  },
  watch: {
    dialogShow() {
      if (!this.dialogShow) this.clearFun();
    },
    bankCode() {
      let bank = this.bankCode.split("-");
      if (bank[0] === "other") {
        this.showBankName = true;
      } else {
        this.showBankName = false;
        this.bankName = "";
      }
    },
  },
};
</script>

<style lang="scss">
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
        text-align: left;
        margin: 8px;
      }
      .item {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
        .title {
          min-width: 100px;
          text-align: right;
        }
        .el-input {
          // width: 150px;
        }
      }
    }
  }
}
</style>
