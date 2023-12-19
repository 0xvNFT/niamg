<template>
  <div class="bankIndex-page">
    <p class="headTitle">
      {{ $t("bankIndex") }}
    </p>
    <el-button type="danger" size="medium" @click="addBank" v-if="data.canAddBank">{{ $t("addText") }}</el-button>
    <div class="list">
      <el-table :data="data.banks" style="width: 99%" :stripe="true">
        <el-table-column prop="realName" :label="$t('bankRealName')"></el-table-column>
        <el-table-column prop="bankName" :label="$t('bankName')"></el-table-column>
        <el-table-column prop="cardNo" :label="$t('bankCardNo')"></el-table-column>
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
        <div slot="empty">
          <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
      </el-table>
    </div>
    <!-- 新增银行卡 -->
    <el-dialog :title="$t('addBank')" :visible.sync="dialogShow" width="40%" :close-on-click-modal="false" class="add-dialog common_dialog">
      <div class="dialog_cont">
        <div style="margin-bottom: 10px" v-if="data.banks && data.banks.length">
          <p class="hint">{{ $t("bankHint") }}</p>
          <div class="item">
            <span class="title">{{ $t("bankRealName") }}：</span>
            <el-input v-model="lastRealName" :placeholder="$t('lastBankName')"></el-input>
          </div>
          <div class="item" style="margin-bottom: 0">
            <span class="title">{{ $t("bankCardNo") }}：</span>
            <el-input v-model="lastCardNo" :placeholder="$t('lastBankCardNo')"></el-input>
          </div>
        </div>
        <!-- <p class="hint">提示：<span style="color:red"> * </span>为必填信息，如需要删除请联系客服提供银行卡照片和充值截图进行处理！</p> -->
        <div class="change-box">
          <div class="item">
            <span class="title">{{ $t("lastBank") }}：</span>
            <el-select v-model="bankCode" :placeholder="$t('pleaseBank')">
              <el-option v-for="item in bankList" :key="item.code" :label="item.name" :value="item.code + '-' + item.name">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="showBankName">
            <span class="title">{{ $t("lastBank") }}：</span>
            <el-input v-model="bankName" :placeholder="$t('inputBankName')"></el-input>
          </div>
          <div class="item">
            <span class="title">{{ $t("bankRealName") }}：</span>
            <el-input v-model="realName" :placeholder="$t('putBankRealName')"></el-input>
          </div>
          <!-- <div class="item">
            <span class="title">{{ $t("bankAddress") }}：</span>
            <el-input v-model="bankAddress" :placeholder="$t('putAddress')"></el-input>
          </div> -->
          <div class="item">
            <span class="title">{{ $t("bankCardNo") }}：</span>
            <el-input v-model="cardNo" :placeholder="$t('putHint')"></el-input>
          </div>
          <div class="item" :style="data.currency === 'INR' ? '' : 'margin-bottom: 0'">
            <span class="title">{{ $t("sureBank") }}：</span>
            <el-input v-model="reCardNo" :placeholder="$t('rePutBankNo')"></el-input>
          </div>
           <!-- 如果填巴西手机号要加+55 -->
           <div class="item" v-if="showPhoneNumerAdd55" style="margin-top:10px;">
            <span class="title">
              <i class="el-icon-warning-outline" style="color:red;"></i>
            </span>
            <span style="color: red;">{{ $t("phoneNumerAdd55") }}</span>
          </div>
          <div class="item" style="margin-bottom: 0" v-if="data.currency === 'INR'">
            <span class="title">IFSC：</span>
            <el-input v-model="bankAddress" :placeholder="$t('putHint')"></el-input>
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
  </div>
</template>

<script>
import DATE from "@/assets/js/date.js";
import { mapState } from 'vuex'
export default {
  data () {
    return {
      data: {}, //
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
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
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
  mounted () {
    // 银行卡列表
    this.list();
  },
  methods: {
    // 银行卡列表
    list () {
      this.$API.getUserBank().then((res) => {
        if (res) {
          this.data = res;
          //去除usdt
          let arr = res.bankList.filter((item) => item.name != "USDT");
          this.bankList = arr;
          //获取站点前缀
          let stationCode = this.onOffBtn.stationCode.split(0, 1)[0];
          //是否配置PIX
          // let result = res.bankList.find((obj) => obj.code === "PIX");
        }
      });
    },

    // 添加银行卡按钮
    addBank () {
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
          .catch(() => { });
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
    //             this.$message.success(res.msg);
    //             // 银行卡列表
    //             this.list();
    //           }
    //         });
    //     })
    //     .catch(() => {});
    // },
    // 绑定手机号
    setPhone (value) {
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
    sureBtn () {
      if(this.cardNo!=this.reCardNo){
        this.$message.error(this.$t("cardNumDifferent")); 
        return
      }
      let bank = this.bankCode.split("-");
      //改变提交格式
      let params = {
        bankCode: bank[0],
      };
      params.bankCode = bank[0];
      if (this.showBankName) params.bankName = this.bankName;
      else params.bankName = bank[1];
      params.realName = this.realName;
      params.cardNo = this.cardNo;
      if (this.data.currency === "INR") params.bankAddress = this.bankAddress;
      if (this.lastRealName) params.lastRealName = this.lastRealName;
      if (this.lastCardNo) params.lastCardNo = this.lastCardNo;
      //新增银行卡
      this.$API.addUserBank(params).then((res) => {
        if (res.success) {
          this.dialogShow = false;
          this.$message.success(res.msg);
          // 银行卡列表
          this.list();
        }
      });
    },

    // 重置
    clearFun () {
      this.bankCode = "";
      this.lastRealName = "";
      this.lastCardNo = "";
      this.realName = "";
      this.cardNo = "";
      this.reCardNo = "";
      this.bankAddress = "";
    },

    // 时间函数
    dataFun (val) {
      return DATE.dateChange(val);
    },
  },
  watch: {
    dialogShow () {
      if (!this.dialogShow) this.clearFun();
    },
    bankCode () {
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
.bankIndex-page {
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
}
</style>
