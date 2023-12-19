<template>
  <el-card class="securityCenter-page">
    <div class="con-header">
      <div class="item header-left">
        <el-progress type="circle" :percentage="data.securityLevel === 1 ? 33 : data.securityLevel === 2 ? 66 : 100" color="#eb031c" :stroke-width="10"></el-progress>
        <div class="right">
          <p class="grade">{{ $t("securityLevel") }}：<span style="color:red">{{data.securityLevel === 1 ? $t("low") : data.securityLevel === 2 ? $t("middle") : $t("high") }}</span></p>
          <div class="score">
            <span></span>
            <span v-if="data.securityLevel > 1"></span>
            <span v-if="data.securityLevel > 2"></span>
          </div>
        </div>
      </div>
      <div class="item header-mid">
        <div>
          <p class="title">{{ $t("lastLoginTime") }}</p>
          <p>{{ $t("loginTime") }}</p>
          <p>{{data.lastLoginTime}}</p>
        </div>
      </div>
      <div class="item header-right">
        <div>
          <p class="title">{{ $t("lastLoginIp") }}</p>
          <p>{{ $t("loginIp") }}</p>
          <p>{{data.lastLoginIp}}</p>
        </div>
      </div>
    </div>
    <div class="con-list">
      <el-card shadow="hover">
        <div class="item-icon">
          <span class="iconfont iconyonghu"></span>
        </div>
        <div class="item-con">
          <p class="name">{{ $t("basicInfo") }}</p>
          <p class="hint">{{ $t("realNameHint") }}</p>
        </div>
        <div class="item-btn">
          <el-button type="success" @click="infoShow = true" v-if="data.hasRealName">{{ $t("look") }}</el-button>
          <el-button type="primary" @click="setFun($t('realName'))" v-else>{{ $t("set") }}</el-button>
        </div>
      </el-card>
      <el-card shadow="hover" v-if="onOffBtn.isUserResetPwd">
        <div class="item-icon">
          <span class="iconfont iconmima"></span>
        </div>
        <div class="item-con">
          <p class="name">{{ configJsFlag("loginPwd") }}</p>
          <p class="hint">{{ $t("loginPwdHint") }}</p>
        </div>
        <div class="item-btn">
          <el-button type="danger" @click="editFun($t('loginPwd'),1)">{{ $t("edit") }}</el-button>
        </div>
      </el-card>
      <el-card shadow="hover">
        <div class="item-icon">
          <span class="iconfont iconmima1"></span>
        </div>
        <div class="item-con">
          <p class="name">{{ $t("withdrawPwd") }}</p>
          <p class="hint">{{ $t("okPwdHint") }}</p>
        </div>
        <div class="item-btn">
          <el-button type="danger" @click="editFun($t('withdrawPwd'),2)" v-if="data.hasWithdrawalPassword">{{ $t("edit") }}</el-button>
          <el-button type="primary" @click="setFun($t('withdrawPwd'),2)" v-else>{{ $t("set") }}</el-button>
        </div>
      </el-card>
      <template v-if="onOffBtn.onOffCommunication">
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconshouji"></span>
          </div>
          <div class="item-con">
            <p class="name">{{ $t("phone") }}</p>
            <p class="hint">{{ $t("phoneHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="setFun($t('phone'),'phone')" v-if="!data.hasPhone">{{ $t("set") }}</el-button>
            <el-button type="danger" @click="editFun($t('phone'),'phone')" v-else-if="!onOffBtn.cantUpdContact">{{ $t("edit") }}</el-button>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconyouxiang"></span>
          </div>
          <div class="item-con">
            <p class="name">{{ $t("email") }}</p>
            <p class="hint">{{ $t("emailHine") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="setFun($t('email'),'email')" v-if="!data.hasEmail">{{ $t("set") }}</el-button>
            <el-button type="danger" @click="editFun($t('email'),'email')" v-else-if="!onOffBtn.cantUpdContact">{{ $t("edit") }}</el-button>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconline-fill"></span>
          </div>
          <div class="item-con">
            <p class="name">LINE</p>
            <p class="hint">{{ $t("LINEHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="setFun('LINE','line')" v-if="!data.hasLine">{{ $t("set") }}</el-button>
            <el-button type="danger" @click="editFun('LINE','line')" v-else-if="!onOffBtn.cantUpdContact">{{ $t("edit") }}</el-button>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconQQ"></span>
          </div>
          <div class="item-con">
            <p class="name">QQ</p>
            <p class="hint">{{ $t("qqHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="setFun('QQ','qq')" v-if="!data.hasQQ">{{ $t("set") }}</el-button>
            <el-button type="danger" @click="editFun('QQ','qq')" v-else-if="!onOffBtn.cantUpdContact">{{ $t("edit") }}</el-button>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconfacebook"></span>
          </div>
          <div class="item-con">
            <p class="name">Facebook</p>
            <p class="hint">{{ $t("facebookHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="setFun('Facebook','facebook')" v-if="!data.hasFacebook">{{ $t("set") }}</el-button>
            <el-button type="danger" @click="editFun('Facebook','facebook')" v-else-if="!onOffBtn.cantUpdContact">{{ $t("edit") }}</el-button>
          </div>
        </el-card>
      </template>
    </div>
    <!-- 基本资料查看 -->
    <el-dialog :title="$t('infoLook')" :visible.sync="infoShow" width="40%" class="info-dialog">
      <div class="cont" v-if="data.userData">
        <div class="item">
          <p class="name">{{ $t("realName") }}：</p>
          <p class="val" v-if="data.userData.realName">{{data.userData.realName}}</p>
        </div>
        <div class="item">
          <p class="name">{{ $t("phone") }}：</p>
          <p class="val" v-if="data.userData.phone">{{data.userData.phone}}</p>
        </div>
        <div class="item">
          <p class="name">{{ $t("email") }}：</p>
          <p class="val" v-if="data.userData.email">{{data.userData.email}}</p>
        </div>
        <div class="item">
          <p class="name">{{ $t("line")  }}：</p>
          <p class="val" v-if="data.userData.line">{{data.userData.line}}</p>
        </div>
        <div class="item">
          <p class="name">QQ：</p>
          <p class="val" v-if="data.userData.qq">{{data.userData.qq}}</p>
        </div>
        <div class="item">
          <p class="name">facebook：</p>
          <p class="val" v-if="data.userData.facebook">{{data.userData.facebook}}</p>
        </div>
      </div>
    </el-dialog>
    <!-- 资料修改 -->
    <el-dialog :title="$t('edit') + dialogTxt" :visible.sync="editShow" width="50%" class="edit-dialog">
      <div class="cont">
        <div class="item">
          <p class="name">{{$t("inputOld") + dialogTxt}}：</p>
          <input type="text" v-model="oldContact" class="val">
        </div>
        <div class="item">
          <p class="name">{{$t("inputNew") + dialogTxt}}：</p>
          <input type="text" v-model="newContact" class="val">
        </div>
        <div class="item">
          <p class="name">{{$t("inputReNew") + dialogTxt}}：</p>
          <input type="text" v-model="reNewData" class="val">
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="clearFun">{{ $t("reset") }}</el-button>
        <el-button type="primary" @click="sureEditFun">{{ $t("confirm") }}</el-button>
      </span>
    </el-dialog>
  </el-card>
</template>

<script>
import { mapState } from 'vuex'
// import LinkBar from '@/components/index/LinkBar'

export default {
  data () {
    return {
      data: '',//
      infoShow: false,//基本资料弹窗是否显示
      editShow: false,//修改弹窗是否显示
      dialogTxt: '',//修改标题
      newContact: '',//原数据
      oldContact: '',//新数据
      reNewData: '',//确认新数据
      type: '',//类型
    };
  },
  computed: {
    ...mapState(['onOffBtn']),
    //当前站点前端配置信息
    configJs() {
      return this.$store.state.configJs;
    },
  },
  components: {
  },
  mounted () {
    this.getSecurityInfo();
  },
  methods: {
    // 获取安全信息接口
    getSecurityInfo () {
      this.$axiosPack.post("/userCenter/getSecurityInfo.do", { load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
        }
      });
    },
    // 设定函数
    setFun (txt, type) {
      this.type = type
      let verify = ''
      if (txt === this.$t("email")) {
        verify = /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/
      } else if (txt === this.$t("withdrawPwd")) {
        verify = /^[a-zA-Z0-9]{4,20}$/
      } else {
        verify = /\S/
      }
      this.$prompt(this.$t("pleaseInput") + txt, txt + ' ' + this.$t("setText"), {
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        inputPattern: verify,
        inputErrorMessage: txt + this.$t("formatRrror")
      }).then(({ value }) => {
        if (txt === this.$t("realName")) this.updateRealName(value)
        else if (txt === this.$t("withdrawPwd")) this.userPwdModifySave(value)
        else this.updateSecurityInfo(value)
      }).catch(() => {
      });
    },
    // 设定真实姓名
    updateRealName (val) {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('realName', val);
      this.$axiosPack.post("/userCenter/updateRealName.do", { params: params }).then(res => {
        if (res) {
          this.$successFun(res.data.msg)
          // 获取安全信息接口
          this.getSecurityInfo()
        }
      });
    },
    // 修改函数
    editFun (txt, type) {
      this.dialogTxt = txt
      this.editShow = true
      this.type = type
    },
    // 重置
    clearFun () {
      this.newContact = ''
      this.oldContact = ''
      this.reNewData = ''
    },
    sureEditFun () {
      if (!this.oldContact) {
        this.toastFun(this.$t("oldText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (!this.newContact) {
        this.toastFun(this.$t("newText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (!this.reNewData) {
        this.toastFun(this.$t("reNewText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (this.newContact !== this.reNewData) {
        this.toastFun(this.$t("twoInput") + this.dialogTxt + this.$t("notSame"));
      } else {
        if (this.dialogTxt === this.$t("loginPwd") || this.dialogTxt === this.$t("withdrawPwd")) {
          this.userPwdModifySave()
        } else {
          this.updateSecurityInfo();
        }
      }
    },
    // 密码更新接口
    userPwdModifySave (value) {
      //改变提交格式
      let params = new URLSearchParams();
      if (value) {
        params.append('oldPwd', '');
        params.append('newPwd', value);
        params.append('okPwd', value);
      } else {
        params.append('oldPwd', this.oldContact);
        params.append('newPwd', this.newContact);
        params.append('okPwd', this.reNewData);
      }
      params.append('type', this.type);

      this.$axiosPack.post("/userCenter/userPwdModifySave.do", { 'params': params, 'load': [true, null, this.$t("submiting")] }).then(res => {
        if (res) {
          // 获取安全信息接口
          this.getSecurityInfo()
          this.$successFun(res.data.msg)
          this.editShow = false
        }
      });
    },
    // 修改联系方式
    updateSecurityInfo (value) {
      //改变提交格式
      let params = new URLSearchParams();
      if (value) {
        params.append('oldContact', '');
        params.append('newContact', value);
      } else {
        params.append('oldContact', this.oldContact);
        params.append('newContact', this.newContact);
      }
      params.append('type', this.type);

      this.$axiosPack.post("/userCenter/updateSecurityInfo.do", { 'params': params, 'load': [true, null, this.$t("submiting")] }).then(res => {
        if (res) {
          // 获取安全信息接口
          this.getSecurityInfo()
          this.$successFun(res.data.msg)
          this.editShow = false
        }
      });
    },
  },
  watch: {
    editShow () {
      if (!this.editShow) {
        this.clearFun()
      }
    }
  }
};
</script>

<style lang="scss">
.securityCenter-page {
  // margin: 26px 50px;
  border: none;
  padding: 15px 50px;
  background: #0f1923;
  .con-header {
    display: flex;
    .item {
      width: 33.333333%;
    }
    .header-left {
      display: flex;
      .right {
        margin-left: 30px;
        .grade {
          font-size: 20px;
          color: #fff !important;
        }
        .score {
          display: flex;
          margin-top: 10px;
          span {
            width: 30px;
            height: 30px;
            background: #eb031c;
            margin-right: 10px;
            border-radius: 5px;
          }
        }
      }
    }
    .header-left,
    .header-mid {
      border:none;
    }
    .header-mid,
    .header-right {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 16px;
      line-height: 28px;
      color: #fff;
      .title {
        color: #fff;
      }
    }
  }
  .con-list {
    .el-card {
      margin-top: 40px;
      border: none;
      background-color: #0f1923;
      color: #fff;
    }
    .el-card__body {
      color: #fff;
      padding: 0;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0;
      .item-icon {
        margin: 20px;
        width: 45px;
        height: 45px;
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        .iconfont {
          font-size: 40px;
        }
      }
      .item-con {
        // width: calc(100% - 350px);
        flex: 1;
        .name {
          font-size: 20px;
          margin-bottom: 10px;
        }
        .hint {
          font-size: 16px;
          color: #fff;
          padding-right: 30px;
        }
      }
      .item-btn {
        .el-button {
          min-width: 160px;
          margin-right: 15px;
          height: 50px;
          font-size: 18px;
          
        }
        .el-button--danger{
          background: #eb031c;
          border-color: #eb031c;
        }
        .el-button--success {
          background: #0ec504;
          border-color: #0ec504;
        }
        .el-button--primary {
          background: #0ec504;
          border-color: #0ec504;
        }
        // .el-button--danger {
        //   background: #0ec504;
        //   border-color: #0ec504;
        // }
      }
    }
  }
  .info-dialog,
  .edit-dialog {
    .cont {
      width: fit-content;
      margin: 0 auto;
      .item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 16px;
        margin-bottom: 10px;
        .name {
          // width: 200px;
          text-align: center;
        }
        .val {
          padding: 5px 10px;
          background: $borderShallow;
          border: 1px solid $border;
          border-radius: 3px;
        }
      }
    }
  }
  .edit-dialog {
    .cont {
      .item {
        .name {
          width: 150px;
        }
        .val {
          background: none;
          font-size: 14px;
          border-radius: 4px;
        }
      }
    }
  }
}
</style>


