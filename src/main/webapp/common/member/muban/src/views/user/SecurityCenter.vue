<template>
  <div class="securityCenter-page">
    <div class="heard">
      <!-- 安全中心 -->
      {{ $t("securityCenter") }}
    </div>
    <el-card class="securityCenter-content">
      <div class="con-header">
        <div class="item header-left">
          <el-progress type="circle" :percentage="data.securityLevel === 1 ? 33 : data.securityLevel === 2 ? 66 : 100" color="#2283f6" :stroke-width="10"></el-progress>
          <div class="right">
            <p class="grade">{{ $t("securityLevel") }}：<br><span>{{data.securityLevel === 1 ? $t("low") : data.securityLevel === 2 ? $t("middle") : $t("high") }}</span></p>
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
            <!-- <p>{{ $t("loginTime") }}</p> -->
            <p>{{data.lastLoginTime}}</p>
          </div>
        </div>
        <div class="item header-right">
          <div>
            <p class="title">{{ $t("lastLoginIp") }}</p>
            <!-- <p>{{ $t("loginIp") }}</p> -->
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
            <p class="name">{{ $t("loginPwd") }}</p>
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
      <el-dialog :title="$t('infoLook')" :visible.sync="infoShow" width="40%" class="info-dialog common_dialog">
        <div class="dialog_cont" v-if="data.userData">
          <!-- <div class="item">
            <p class="title">{{ $t("realName") }}：</p>
            <p class="val">{{data.userData.realName || '--'}}</p>
          </div> -->
          <div class="item">
            <div class="line">{{ $t("realName") }}：{{data.userData.realName || '--'}}</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("phone") }}：{{data.userData.phone || '--'}}</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("email") }}：{{data.userData.email || '--'}}</div>
          </div>
          <div class="item">
            <div class="line">Line：{{data.userData.line || '--'}}</div>
          </div>
          <div class="item">
            <div class="line">QQ：{{data.userData.qq || '--'}}</div>
          </div>
          <div class="item">
            <div class="line">Facebook：{{data.userData.facebook || '--'}}</div>
          </div>
        </div>
      </el-dialog>
      <!-- 资料修改 -->
      <el-dialog :title="$t('edit') + dialogTxt" :visible.sync="editShow" width="50%" class="edit-dialog common_dialog">
        <div class="dialog_cont">
          <div class="item">
            <p class="title">{{$t("inputOld") + dialogTxt}}：</p>
            <el-input v-model="oldContact" clearable></el-input>
          </div>
          <div class="item">
            <p class="title">{{$t("inputNew") + dialogTxt}}：</p>
            <el-input v-model="newContact" clearable></el-input>
          </div>
          <div class="item">
            <p class="title">{{$t("inputReNew") + dialogTxt}}：</p>
            <el-input v-model="reNewData" clearable></el-input>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
          <button class="closeBtn themeBtn" @click="clearFun">
            {{ $t("reset") }}
          </button>
          <button class="sureBtn themeBtn" @click="sureEditFun">
            {{ $t("confirm") }}
          </button>
        </span>
      </el-dialog>
    </el-card>
  </div>

</template>
<script>
import { mapState } from 'vuex'

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
    ...mapState(['userInfo', 'onOffBtn', 'gameOnOff'])
  },
  components: {

  },
  mounted () {
    this.getSecurityInfo()

  },
  methods: {
    // 获取安全信息接口
    getSecurityInfo () {
      this.$API.getSecurityInfo({ load: true }).then((res) => {
        if (res) {
          this.data = res
        }
      })
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
      this.$prompt(this.$t("pleaseInput") + txt,this.$t("setText") + txt, {
        confirmButtonClass: "smallSureBtn themeBtn",
        cancelButtonClass: "smallCloseBtn themeBtn",
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        customClass: 'messageBox',
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
      let params = {};
      params.realName = val
      params.load = true
      this.$API.updateRealName(params).then((res) => {
        if (res.success) {
          this.$message.success(res.msg);
          // 获取安全信息接口
          this.getSecurityInfo()
        }
      })
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
        this.$message.warning(this.$t("oldText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (!this.newContact) {
        this.$message.warning(this.$t("newText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (!this.reNewData) {
        this.$message.warning(this.$t("reNewText") + this.dialogTxt + this.$t("canNotEmpty"));
      } else if (this.newContact !== this.reNewData) {
        this.$message.warning(this.$t("twoInput") + this.dialogTxt + this.$t("notSame"));
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
      let params = {};
      if (value) {
        params.oldPwd = ''
        params.newPwd = value
        params.okPwd = value
      } else {
        params.oldPwd = this.oldContact
        params.newPwd = this.newContact
        params.okPwd = this.reNewData
      }
      params.type = this.type
      params.load = true

      this.$API.userPwdModifySave(params).then((res) => {
        if (res.success) {
          // 获取安全信息接口
          this.getSecurityInfo()
          this.$message.success(res.msg);
          this.editShow = false
        }
      })
    },
    // 修改联系方式
    updateSecurityInfo (value) {
      //改变提交格式
      let params = {};
      if (value) {
        // params['oldContact'] = ''
        params.oldContact = ''
        params.newContact = value

      } else {
        params.oldContact = this.oldContact
        params.newContact = this.newContact
      }
      params.type = this.type
      params.load = true
      this.$API.updateSecurityInfo(params).then((res) => {
        if (res.success) {
          // 获取安全信息接口
          this.getSecurityInfo()
          this.$message.success(res.msg);
          this.editShow = false
        }
      })
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
  border: none;
  padding: 10px;
  .securityCenter-content {
    background: #0f1923;
    padding: 5px 30px;
    margin: 5px 10px;
    .con-header {
      display: flex;
      .item {
        flex: 1;
      }
      .header-left {
        display: flex;
        .right {
          margin-left: 30px;
          .grade {
            font-size: 20px;
            color: #fff !important;
            span {
              color: $blue;
            }
          }
          .score {
            display: flex;
            margin-top: 10px;
            span {
              width: 30px;
              height: 30px;
              background: $blue;
              margin-right: 10px;
              border-radius: 5px;
            }
          }
        }
      }
      .header-left,
      .header-mid {
        border: none;
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
          .el-button--danger {
            background: #eb031c;
            border-color: #eb031c;
          }
          .el-button--success {
            background: $blue;
            border-color: $blue;
          }
          .el-button--primary {
            background: $blue;
            border-color: $blue;
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
      .dialog_cont {
        .item {
          font-size: 16px;
          // .title {
          //   width: 200px;
          // }
        }
      }
    }
  }
}
.messageBox {
  .el-message-box__message {
    color: $colorText;
  }
  input {
    background: $blackinput;
    color: $colorText;
  }
}
</style>
