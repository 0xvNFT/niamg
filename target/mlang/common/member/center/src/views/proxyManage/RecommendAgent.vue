<template>
  <el-tabs type="border-card" class="recommendAgent-page" v-model="tabIndex">
    <el-tab-pane :label="$t('recommendAgent')" class="list info" name="1">
      <div class="item">
        <span class="name">{{ $t("myName") }}：</span>
        <span class="val">{{ data.username }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("myCode") }}：</span>
        <span class="val">{{ data.code }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("linkUrl") }}：</span>
        <span class="val">{{ data.linkUrl }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("linkUrlEn") }}：</span>
        <span class="val">{{ data.linkUrlEn }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("accessPage") }}：</span>
        <span class="val">{{
          data.accessPage === 1
            ? $t("registerPage")
            : data.accessPage === 2
            ? $t("indexText")
            : $t("active")
        }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("accessNum") }}：</span>
        <span class="val">{{ data.accessNum }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("registerNum") }}：</span>
        <span class="val">{{ data.registerNum }}</span>
      </div>
      <div class="item">
        <span class="name">{{ $t("setTime") }}：</span>
        <span class="val">{{ data.createTime }}</span>
      </div>
    </el-tab-pane>
    <el-tab-pane :label="$t('addMember')" class="list add" name="2">
      <div class="item" v-for="val in memberRegConfigs">
        <span class="name">{{ val.name }}：</span>
        <el-input
          v-model="formData[val.eleName]"
          class="outInput"
          :type="val.eleType === 1 ? 'text' : 'password'"
          :placeholder="val.tips"
          clearable
          size="medium"
        ></el-input>
        <span style="color: red;margin-left: 5px">{{
          val.required === 2 ? "*" : ""
        }}</span>
      </div>
      <div class="btn">
        <el-button type="primary" @click="sureBtn">{{
          $t("confirm")
        }}</el-button>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
// import { mapState } from 'vuex'

export default {
  data() {
    return {
      tabIndex: "1", //
      data: "",
      username: "",
      realName: "",
      phone: "",
      pwd: "",
      rePwd: "",
      memberRegConfigs: "", //会员注册信息
      formData: {
        username: "",
        pwd: "",
        rpwd: "",
        phone: "",
        email: "",
        qq: "",
        facebook: "",
        line: "",
      },
    };
  },
  computed: {
    // ...mapState(['onOffBtn'])
  },
  components: {},
  mounted() {
    this.recommendInfo();
    this.agentRegPromotionInfo();
  },
  methods: {
    // 推荐信息
    recommendInfo() {
      this.$axiosPack
        .post("/userCenter/agentManage/recommendInfo.do", {
          load: [true, 200, null],
        })
        .then((res) => {
          if (res) {
            this.data = res.data;
          }
        });
    },

    // 获取会员注册选项
    agentRegPromotionInfo() {
      this.$axiosPack
        .post("/userCenter/agentManage/agentRegPromotionInfo.do")
        .then((res) => {
          if (res) {
            // 注册选项处理
            let member = [];
            for (let i = 0; i < res.data.memberRegConfigs.length; i++) {
              if (
                res.data.memberRegConfigs[i].eleName !== "promoCode" &&
                res.data.memberRegConfigs[i].eleName !== "captcha" &&
                res.data.memberRegConfigs[i].eleName !== "receiptPwd"
              ) {
                member.push(res.data.memberRegConfigs[i]);
              }
            }

            this.memberRegConfigs = member;
          }
        });
    },

    // 提交
    sureBtn() {
      //改变提交格式
      let params = new URLSearchParams();
      let arr = this.memberRegConfigs;
      for (var i in arr) {
        if (this.formData[arr[i].eleName]) {
          params.append(arr[i].eleName, this.formData[arr[i].eleName]);
        } else {
          if (arr[i].required === 2) {
            this.toastFun(arr[i].name + this.$t("canNotEmpty"));
            return;
          }
        }
      }
      this.$axiosPack
        .post("/userCenter/agentManage/recommendAddMember.do", {
          params: params,
        })
        .then((res) => {
          if (res) {
            this.$successFun(res.data.msg);
            this.clearFun();
          }
        });
    },

    clearFun() {
      this.formData = {
        username: "",
        pwd: "",
        rpwd: "",
        phone: "",
        email: "",
        qq: "",
        facebook: "",
        line: "",
      };
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.recommendAgent-page {
  color: #000;
  .list {
    width: fit-content;
    margin: 0 auto;
    .item {
      margin-bottom: 10px;
    }
    .name {
      display: inline-block;
      // width: 220px;
      text-align: right;
    }
  }
  .info {
    .item {
      margin-bottom: 10px;
    }
    .name {
      display: inline-block;
      width: 50%;
      text-align: right;
    }
    .val {
      width: 50%;
      display: inline-block;
      text-align: left;
    }
  }
  .add {
    .item {
      margin-bottom: 10px;
      display: flex;
      align-items: center;
      .name {
        display: inline-block;
        text-align: right;
        width: 280px;
      }
      .el-input {
        width: 49%;
      }
    }
    .btn {
      text-align: center;
    }
  }
}
</style>
