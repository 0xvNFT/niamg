<!--
 * @FilePath: \muban\src\views\proxy\RecommendAgent.vue
 * @Description: 我的推荐页面
-->
<template>
  <div>
    <div class="heard">
      {{ $t("recommendAgent") }}
    </div>
    <div class="recommend-container">
      <div class="card-item--outsite">
        <div class="card-item--insite">
          <div class="card-item first-card">
            <h2 class="card-title">{{ $t("personalInfo") }}</h2>
            <div class="user-avatar-box">
              <div>
                <img src="@images/grade" alt="" />
                <i></i>
              </div>
              <p>{{ userInfo.curDegreeName }}</p>
            </div>
            <div>
              <p class="info-text">{{ $t("myName") }}: {{ data.username }}</p>
              <p class="info-text">
                {{ $t("myCode") }}:
                <span id="code-for-copy">{{ data.code }}</span>
                <sup
                  ><i
                    class="el-icon-copy-document"
                    @click="toCopy('code-for-copy')"
                  ></i
                ></sup>
              </p>

              <p class="info-text">
                {{ $t("setTime") }}: {{ data.createTime }}
              </p>
            </div>
          </div>
        </div>
      </div>
      <div class="card-item--outsite">
        <div class="card-item--insite">
          <article class="card-item second-card">
            <h2 class="card-title">{{ $t("recommendAgent") }}</h2>
            <hr />
            <div class="linkBox" style="margin-bottom: 10px">
              <p class="info-text_2">{{ $t("linkUrl") }}:</p>
              <i id="url-for-copy">{{ data.linkUrl }}</i>
              <sup
                ><i
                  class="el-icon-copy-document"
                  @click="toCopy('url-for-copy')"
                ></i
              ></sup>
            </div>
            <div class="linkBox">
              <p class="info-text_2">{{ $t("linkUrlEn") }}:</p>
              <i id="url-for-copy">{{ data.linkUrlEn }}</i>
              <sup
                ><i
                  class="el-icon-copy-document"
                  @click="toCopy('url-for-copy')"
                ></i
              ></sup>
            </div>
            <hr />
            <div class="pageBox">
              <p class="info-text_2">
                {{ $t("accessPage") }}: {{ data.accessPage }}
              </p>
              <p class="info-text_2">
                {{ $t("accessNum") }}: {{ data.accessNum }}
              </p>
              <p class="info-text_2">
                {{ $t("registerNum") }}: {{ data.registerNum }}
              </p>
            </div>
          </article>
        </div>
      </div>
      <div class="card-item--outsite">
        <div class="card-item--insite">
          <div class="card-item thirt-card">
            <h2 class="card-title">{{ $t("addMember") }}</h2>
            <el-form
              :model="formData"
              :rules="defineRules"
              ref="ruleForm"
              label-width="auto"
              label-position="top"
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
              <el-form-item style="text-align: center;">
                <el-button type="primary" @click="sureBtn">{{
                  $t("confirm")
                }}</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

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
    ...mapState(["userInfo"]),
    // 定义表单的校验规则
    defineRules() {
      let rules = {};
      let source = this.memberRegConfigs;
      for (let int = 0; int < source.length; int++) {
        rules[source[int].eleName] = [
          {
            required: source[int].required === 2,
            message: this.$t("pleaseInput") + source[int].name,
            trigger: "change",
          },
        ];
      }
      return rules;
    },
  },
  components: {},
  mounted() {
    this.recommendInfo();
    this.agentRegPromotionInfo();
  },
  methods: {
    // 推荐信息
    recommendInfo() {
      this.$API.recommendInfo({ load: true }).then((res) => {
        if (res) {
          this.data = res;
        }
      });
    },

    // 获取会员注册选项
    agentRegPromotionInfo() {
      this.$API.agentRegPromotionInfo({ load: true }).then((res) => {
        if (res) {
          // 注册选项处理
          let member = [];
          for (let i = 0; i < res.memberRegConfigs.length; i++) {
            if (
              res.memberRegConfigs[i].eleName !== "promoCode" &&
              res.memberRegConfigs[i].eleName !== "captcha" &&
              res.memberRegConfigs[i].eleName !== "receiptPwd"
            ) {
              member.push(res.memberRegConfigs[i]);
            }
          }
          this.memberRegConfigs = member;
        }
      });
    },

    // 提交
    sureBtn() {
      //改变提交格式
      let params = {};
      let arr = this.memberRegConfigs;
      for (var i in arr) {
        if (this.formData[arr[i].eleName]) {
          params[arr[i].eleName] = this.formData[arr[i].eleName];
        } else {
          if (arr[i].required === 2) {
            this.$message.warning(arr[i].name + this.$t("canNotEmpty"));
            return;
          }
        }
      }
      if (this.formData.pwd !== this.formData.rpwd) {
        this.$message.warning(this.$t("loginPwdAgain"));
        return;
      }
      params.load = true;

      this.$API.recommendAddMember(params).then((res) => {
        if (res.success) {
          this.$message.success(res.msg);
          this.formData = this.$options.data().formData;
        }
      });
    },
    /**
     * @description: 复制文本
     * @param {*} elId 要复制的元素ID
     */
    toCopy(elId) {
      this.$publicJs.copyFun(elId);
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.recommend-container {
  width: 90%;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  color: #3f526e;
  font-size: 18px;
  font-weight: 800;
  .card-item--outsite {
    width: 33.3333%;
    padding: 44px 8px 0;
    .card-item--insite {
      border-radius: 12px;
      padding: 8px;
      background: #161e2a url("../../assets/images/user_center/card_bottom_bg")
        bottom no-repeat;
      .first-card {
        display: grid;
        grid-template-rows: 90px 216px 175px;
      }
      .second-card {
      }
      .thirt-card {
        overflow-y: auto;
      }
      .card-item {
        width: 100%;
        height: 483px;
        border-radius: 4px;
        border: 1px solid #222d3b;
        padding: 0 16px;
        hr {
          background-color: #1c2532;
          border-width: 0;
          border-top: initial;
          color: #1c2532;
          height: 2px;
          margin: auto;
          width: 60%;
          margin-top: 10px;
          margin-bottom: 10px;
        }
        .linkBox {
          width: auto;
          height: auto;
          background: #1c2532;
          border: 0 solid #182535;
          border-radius: 0.75rem;
          margin: 3px;
          color: #3f526e;
          line-height: 1;
          text-align: center;
          padding: 8px 0;
        }
        .pageBox {
          margin: auto;
          width: 65%;
          text-align: center;
        }
        .card-title {
          text-align: center;
          line-height: 80px;
        }
        .user-avatar-box {
          div {
            width: 122px;
            height: 132px;
            margin: 28px auto 6px;
            position: relative;
            display: flex;
            justify-content: center;
            background-image: url("../../assets/images/user_center/image_border.svg");
            img {
              width: 100px;
              height: 100px;
              margin: auto;
              z-index: 2;
            }
            i {
              position: absolute;
              box-shadow: 0 0 100px 60px #fed791;
              top: 50%;
              left: 50%;
              transform: translate(-50%, -50%);
            }
          }
          p {
            background: linear-gradient(180deg, #f39d42, #fed791);
            background-clip: text;
            text-align: center;
            color: transparent;
            font-size: 24px;
          }
        }
        .info-text {
          text-align: center;
          line-height: 38px;
        }
        .info-text_2 {
          margin-bottom: 18px;
          line-break: anywhere;
        }
        .el-icon-copy-document {
          margin-left: 4px;
          color: #1a55ef;
          cursor: pointer;
        }
        .el-input__inner {
          background: $blackinput;
          color: #fff;
        }
        .el-form-item {
            margin-bottom: 16px;
        }
        .el-form-item__label {
          color: inherit;
          padding: unset;
          line-height: unset;
        }
        .el-button--primary {
          margin-top: 4px;
        }
      }
    }
  }
  @media screen and (max-width: 1350px) {
    .card-item--outsite {
      width: 100%;
    }
  }
}
</style>


