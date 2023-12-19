<template>
  <el-tabs type="border-card" class="recommendAgent-page" v-model="tabIndex">
    <el-tab-pane :label="$t('recommendAgent')" class="list info" name="1">
      <div class="item">
        <span class="name">{{ $t("myName") }}：</span>
        <span class="val">{{data.username}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("myCode") }}：</span>
        <span class="val">{{data.code}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("linkUrl") }}：</span>
        <span class="val">{{data.linkUrl}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("linkUrlEn") }}：</span>
        <span class="val">{{data.linkUrlEn}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("accessPage") }}：</span>
        <span class="val">{{data.accessPage === 1 ? $t("registerPage") : data.accessPage === 2 ? $t("indexText") : $t("active")}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("accessNum") }}：</span>
        <span class="val">{{data.accessNum}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("registerNum") }}：</span>
        <span class="val">{{data.registerNum}}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("setTime") }}：</span>
        <span class="val">{{data.createTime}}</span>
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('addMember')" class="list add" name="2">
      <el-form
      :model="formData"
      :rules="defineRules"
      ref="ruleForm"
      label-width="auto"
      label-position="left"
      >
        <el-form-item
        v-for="(item, index) in memberRegConfigs"
        :key="index"
        :label="item.name"
        :prop="item.eleName"
        >
          <el-input
          v-model="formData[item.eleName]"
          :type="item.eleType === 1 ? 'text' : 'password'"
          :placeholder="item.tips"
          clearable
          size="medium"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="sureBtn">{{ $t("confirm") }}</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
// import { mapState } from 'vuex'

export default {
  data () {
    return {
      tabIndex: '1',//
      data: '',
      username: '',
      realName: '',
      phone: '',
      pwd: '',
      rePwd: '',
      memberRegConfigs: '',//会员注册信息
      formData: {
        username: '',
        pwd: '',
        rpwd: '',
        phone: '',
        email: '',
        qq: '',
        facebook: '',
        line: '',
      },
    };
  },
  computed: {
    // ...mapState(['onOffBtn'])
    // 定义表单的校验规则
    defineRules () {
      let rules = {};
      let source = this.memberRegConfigs;
      for (let int = 0; int < source.length; int++) {
        rules[source[int].eleName] = [
          {
            required: source[int].required === 2,
            message: this.$t('pleaseInput') + source[int].name,
            trigger: 'change'
          }
        ];
      };
      return rules
    }
  },
  components: {
  },
  mounted () {
    this.recommendInfo()
    this.agentRegPromotionInfo()
  },
  methods: {
    // 推荐信息
    recommendInfo () {
      this.$API.recommendInfo({ load: true }).then(res => {
        if (res) {
          this.data = res
        }
      });
    },

    // 获取会员注册选项
    agentRegPromotionInfo () {
      this.$API.agentRegPromotionInfo({ load: true }).then(res => {
        if (res) {
          // 注册选项处理
          let member = []
          for (let i = 0; i < res.memberRegConfigs.length; i++) {
            if (res.memberRegConfigs[i].eleName !== 'promoCode' && res.memberRegConfigs[i].eleName !== 'captcha' && res.memberRegConfigs[i].eleName !== 'receiptPwd') {
              member.push(res.memberRegConfigs[i])
            }
          }
          this.memberRegConfigs = member
        }
      });
    },

    // 提交
    sureBtn () {
      //改变提交格式
      let params = {};
      let arr = this.memberRegConfigs
      for (var i in arr) {
        if (this.formData[arr[i].eleName]) {
          params[arr[i].eleName] = this.formData[arr[i].eleName];
        } else {
          if (arr[i].required === 2) {
            this.$message.warning(arr[i].name + this.$t("canNotEmpty"));
            return
          }
        }
      }
      if (this.formData.pwd !== this.formData.rpwd) {
        this.$message.warning(this.$t("loginPwdAgain"));
        return
      }
      params.load = true

      this.$API.recommendAddMember(params).then(res => {
        if (res.success) {
          this.$message.success(res.msg);
          this.clearFun()
        }
      });
    },

    clearFun () {
      this.formData = {
        username: '',
        pwd: '',
        rpwd: '',
        phone: '',
        email: '',
        qq: '',
        facebook: '',
        line: '',
      }
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.recommendAgent-page {
  background: $black;
  color: $colorText;
  font-size: 15px;
  // position: relative;
  .list {
    margin: 0 auto;
    max-height: 500px;
    display: flex;
    flex-direction: column;
    flex-wrap: wrap;
    .item {
      margin-bottom: 20px;
      margin-top: 20px;
      font-size: 20px;
      // text-align: center;
    }
    button {
      position: absolute;
      transform: translateX(-50%) translateY(-50%);
      left: 50%;
    }
    .name {
      display: inline-block;
      text-align: right;
      margin-right: 10px;
    }
  }
  .info {
    .name {
      width: 290px;
      text-align: right;
    }
  }
  .add {
    .el-input {
      width: 200px;
    }
  }
  .el-tabs__header {
    background: $black;
    .el-tabs__nav-wrap {
      .el-tabs__nav-scroll {
        background-color: $black;
        border-bottom: 1px solid $borderColorJoker;
        .is-active {
          background-color: $bgColor;
          color: #ffffff;
          border-right-color: $borderColorJoker;
          border-left-color: $borderColorJoker;
        }
      }
    }
  }
  .el-input__inner {
    background: $blackinput;
    color: #fff;
  }
  .el-form-item__label {
    color: inherit;
    font-size: 20px;
  }
}
</style>


