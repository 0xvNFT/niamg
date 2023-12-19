<template>
  <div class="register">
    <div class="close" @click="registerShowNo">x</div>

    <div class="mainBox">
      <div class="selectBotton">
        <div
          class="top_Btn"
          @click="active = 0"
          :class="active == 0 ? 'active_top_Btn' : ''"
        >
          {{ $t("login") }}
        </div>
        <div
          class="top_Btn"
          @click="active = 1"
          :class="active == 1 ? 'active_top_Btn' : ''"
        >
          {{ $t("register") }}
        </div>
      </div>
      <div class="overflow">
        <!-- 用户名登录注册 -->
        <div>
          <!-- 登录 -->
          <div class="ant_form_logIn" v-show="active == 0">
            <div class="ant_form_item" v-if="registerModel == 'v1'">
              <p>{{ $t("userName") }}</p>
              <div class="item_input">
                <i class="el-icon-user"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('userName')"
                  v-model="account"
                  clearable
                  spellcheck="false"
                >
                </el-input>
              </div>
            </div>
            <div class="ant_form_item" v-else-if="registerModel == 'v2'">
              <p>{{ $t("email") + "/" + $t("accountText") }}</p>
              <div class="item_input">
                <i class="el-icon-message"></i>
                <el-input
                  :placeholder="
                    $t('pleaseInput') + $t('email') + '/' + $t('accountText')
                  "
                  v-model="account"
                  clearable
                >
                </el-input>
              </div>
            </div>
            <div class="ant_form_item" v-else-if="registerModel == 'v3'">
              <p>{{ $t("phone") + "/" + $t("accountText") }}</p>
              <div class="item_input">
                <i class="el-icon-mobile"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('phone')"
                  v-model="account"
                  clearable
                >
                </el-input>
              </div>
            </div>
            <!-- 登录密码 -->
            <div class="ant_form_item">
              <p>{{ $t("loginPwd") }}</p>
              <div class="item_input">
                <i class="el-icon-unlock"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('loginPwd')"
                  v-model="password"
                  show-password
                  @keyup.enter.native="LoginClick"
                ></el-input>
              </div>
            </div>
            <!-- 验证码 后台暂时没判断 -->
            <div class="ant_form_item">
              <p>{{ $t("verificationCode") }}</p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('verificationCode')"
                  v-model="verifyCod"
                  @focus="changeVerify(0)"
                  clearable
                  @keyup.enter.native="LoginClick"
                >
                </el-input>
                <img
                  :src="VerifyUrl"
                  alt=""
                  @click="changeVerify(0)"
                  style="cursor: pointer"
                />
              </div>
            </div>
          </div>
          <!-- 注册 -->
          <div v-if="registerModel == 'v1'">
            <div class="ant_form_register" v-show="active == 1">
              <div
                class="ant_form_item"
                v-for="(item, key) in registerData"
                :key="key"
              >
                <p>
                  {{ item.name
                  }}<span class="required">{{
                    item.required == 2 ? "*" : ""
                  }}</span>
                </p>
                <div class="item_input">
                  <i :class="iconName(key)"></i>
                  <el-input
                    :placeholder="$t('pleaseInput') + item.name"
                    v-model="item.val"
                    :show-password="item.eleName.includes('pwd')"
                    :clearable="!item.eleName.includes('pwd')"
                    @focus="item.eleName == 'captcha' ? changeVerify(1) : ''"
                    @keyup.enter.native="register"
                    spellcheck="false"
                  ></el-input>
                  <img
                    :src="VerifyUrl"
                    alt=""
                    @click="changeVerify(1)"
                    style="cursor: pointer"
                    v-if="item.eleName == 'captcha'"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 邮箱注册 -->
        <div v-if="registerModel == 'v2'">
          <!-- 注册 -->
          <div class="ant_form_register" v-show="active == 1">
            <div class="ant_form_item">
              <p>{{ $t("email") }}</p>
              <div class="item_input">
                <i class="el-icon-message"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('email')"
                  v-model="registerEmail"
                  clearable
                >
                </el-input>
                <p
                  class="sendCode"
                  @click="sendCode"
                  :class="timeOutShow ? 'sendCodeNone' : ''"
                >
                  {{ $t("sendCode") }}
                </p>
                <span v-show="timeOutShow">{{ timeOutnum }}s</span>
              </div>
            </div>
            <div class="ant_form_item">
              <p>{{ $t("loginPwd") }}</p>
              <div class="item_input">
                <i class="el-icon-unlock"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('loginPwd')"
                  v-model="EmailPassword"
                  show-password
                ></el-input>
              </div>
            </div>
            <div class="ant_form_item">
              <p>{{ $t("sendCode") }}</p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('verificationCode')"
                  v-model="registerEmailCode"
                  clearable
                ></el-input>
              </div>
            </div>
            <div
              class="ant_form_item"
              v-for="(item, index) in registerData"
              :key="index"
              v-if="item.eleName == 'promoCode'"
            >
              <p>
                {{ item.name
                }}<span class="required">{{
                  item.required == 2 ? "*" : ""
                }}</span>
              </p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + item.name"
                  v-model="registerPromoCode"
                  clearable
                  @keyup.enter.native="register"
                ></el-input>
              </div>
            </div>
            <div class="ant_form_item" v-else-if="item.eleName == 'captcha'">
              <p>
                {{ item.name
                }}<span class="required">{{
                  item.required == 2 ? "*" : ""
                }}</span>
              </p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('verificationCode')"
                  v-model="verifyCod"
                  @focus="changeVerify(1)"
                  clearable
                >
                </el-input>
                <img
                  :src="VerifyUrl"
                  alt=""
                  @click="changeVerify(1)"
                  style="cursor: pointer"
                />
              </div>
            </div>
          </div>
        </div>
        <!-- 手机注册 -->
        <div v-if="registerModel == 'v3'">
          <!-- 注册 -->
          <div class="ant_form_register" v-show="active == 1">
            <div class="ant_form_item">
              <p>{{ $t("phone") }}<span class="required">*</span></p>
              <div class="item_input">
                <i class="el-icon-mobile"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('phone')"
                  v-model="registerPhone"
                  clearable
                >
                </el-input>
                <p
                  class="sendCode"
                  @click="sendCode"
                  :class="timeOutShow ? 'sendCodeNone' : ''"
                >
                  {{ $t("sendCode") }}
                </p>
                <span v-show="timeOutShow">{{ timeOutnum }}s</span>
              </div>
            </div>
            <div class="ant_form_item">
              <p>{{ $t("loginPwd") }}<span class="required">*</span></p>
              <div class="item_input">
                <i class="el-icon-unlock"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('loginPwd')"
                  v-model="phonePassword"
                  show-password
                ></el-input>
              </div>
            </div>
            <div class="ant_form_item">
              <p>{{ $t("verificationCode") }}<span class="required">*</span></p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + $t('verificationCode')"
                  v-model="registerPhoneCode"
                  clearable
                ></el-input>
              </div>
            </div>
            <div
              class="ant_form_item"
              v-for="(item, index) in registerData"
              :key="index"
              v-if="item.eleName == 'promoCode'"
            >
              <p>
                {{ item.name
                }}<span class="required">{{
                  item.required == 2 ? "*" : ""
                }}</span>
              </p>
              <div class="item_input">
                <i class="el-icon-postcard"></i>
                <el-input
                  :placeholder="$t('pleaseInput') + item.name"
                  v-model="registerPromoCode"
                  clearable
                  @keyup.enter.native="register"
                ></el-input>
              </div>
            </div>
            <!-- <div class="ant_form_item" v-else-if="item.eleName == 'captcha'">
            <p>{{item.name}}<span class="required">{{item.required==2?'*':''}}</span></p>
            <div class="item_input">
              <i class="el-icon-s-promotion"></i>
              <el-input placeholder="请输入图片中的验证码" v-model="verifyCod" @focus="changeVerify(1)" clearable>
              </el-input>
              <img :src="VerifyUrl" alt="" @click="changeVerify(1)" style="cursor: pointer;">
            </div>
          </div> -->
          </div>
        </div>
        <!-- 登录按钮 -->
        <div class="loginBottom" v-show="active == 0">
          <div style="margin-bottom: 10px">
            <el-checkbox v-model="passChecked">{{
              $t("rememberPass")
            }}</el-checkbox>
          </div>
          <div
            class="bottom"
            @click="LoginClick"
            :style="isDisabled ? 'pointer-events: none' : ''"
          >
            {{ $t("login") }}
          </div>
          <div class="goobook">
            <a
              :href="onOffBtn.instagram_url || 'javascript:;'"
              :target="onOffBtn.instagram_url ? '_blank' : '_self'"
            >
              <img src="../../assets/images/register/instagram.png" alt="" />
            </a>
            <a
              :href="onOffBtn.facebook_url || 'javascript:;'"
              :target="onOffBtn.facebook_url ? '_blank' : '_self'"
            >
              <img src="../../assets/images/register/facebook.jpg" alt="" />
            </a>
          </div>
          <p class="forget">
            <a
              :href="onOffBtn.kfUrl || 'javascript:;'"
              :target="onOffBtn.kfUrl ? '_blank' : '_self'"
              >{{ $t("forgetPass") }}</a
            >
          </p>
          <p class="loginText">
            {{ $t("RemarkClause") }}
            <a
              class="underline"
              :href="onOffBtn.facebook_url || 'javascript:;'"
              :target="onOffBtn.facebook_url ? '_blank' : '_self'"
              >{{ $t("termsService") }}</a
            >
          </p>
        </div>
        <!-- //注册按钮 -->
        <div class="loginBottom" v-show="active == 1">
          <p class="loginText">
            <el-checkbox v-model="checked">{{
              $t("TurnEighteen")
            }}</el-checkbox>
            <a
              class="underline"
              :href="onOffBtn.facebook_url || 'javascript:;'"
              :target="onOffBtn.facebook_url ? '_blank' : '_self'"
              >{{ $t("termsService") }}</a
            >
          </p>
          <div
            class="bottom"
            @click="register"
            :class="!checked ? 'disableBtn' : ''"
          >
            {{ $t("createAccount") }}
          </div>
        </div>
      </div>
    </div>
    <div class="Registration_mainBox">
      <div>{{ $t("welcome") }}</div>
      <p>{{ $t("welcomeText") }}</p>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import Utils from "@/assets/js/utils";
export default {
  data() {
    return {
      // active: 0,//0 登录 1 注册
      isDisabled: false, //登录按钮禁用
      checked: false, //注册check
      account: "", //登录用户名
      password: "", //登录密码
      // loginEmail: '',//登录邮箱
      registerEmail: "", //v2注册邮箱
      registerEmailCode: "", //注册邮箱验证码
      EmailPassword: "", //v2注册邮箱密码
      registerPhone: "", //v3注册手机
      // loginPhone: '',//登录手机
      registerPhoneCode: "", //注册手机验证码
      phonePassword: "", //v3注册手机密码
      registerPromoCode: "", //注册推广码
      VerifyUrl: "", //验证码url
      verifyCod: "", //输入的验证码
      registerShow: false,
      registerData: [], //注册信息
      registerModel: "", //注册登录方式{"v1": "用户名登录"}, {"v2": "邮件登录"}, {"v3": "手机登录"}
      timeOutShow: false, //发送验证码倒计时显示
      timeOutnum: 0, //验证码倒计时
      active: 0,
      passChecked: false, //是否记住密码
    };
  },
  props: {
    activeNum: 0, //0 登录 1 注册
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  created() {},
  mounted() {
    //弹出
    // this.$bus.$on("registerShow", (val) => {
    //   console.log(111)
    //   this.active = val
    //   this.registerShow = true
    // });
    this.active = this.activeNum;
    this.getRegister();
    this.changeVerify(this.active);
    // this.remPwd()
  },
  methods: {
    //发送验证码
    sendCode() {
      //发送邮箱
      if (this.registerModel == "v2") {
        if (!this.registerEmail || !this.registerEmail.includes("@")) {
          this.$message.warning(this.$t("email") + this.$t("formatRrror"));
          return;
        }
        this.$API
          .reqEmailVcode({ email: this.registerEmail, type: 3 })
          .then((res) => {
            if (res.success) {
              showTimedown();
              this.$message.success(
                this.$t("sendCode") + this.$t("successText")
              );
            } else {
              this.timeOutShow = false;
            }
          });
        //发送手机
      } else if (this.registerModel == "v3") {
        if (!this.registerPhone) {
          this.$message.warning(this.$t("phone") + this.$t("formatRrror"));
          return;
        }
        this.$API.reqSmsCode({ phone: this.registerPhone }).then((res) => {
          if (res.success) {
            showTimedown();
            this.$message.success(this.$t("sendCode") + this.$t("successText"));
          } else {
            this.timeOutShow = false;
          }
        });
      }
      const showTimedown = () => {
        this.timeOutShow = true;
        this.timeOutnum = 60;
        let timeOut = setInterval(() => {
          this.timeOutnum = this.timeOutnum - 1;
          if (this.timeOutnum == 0) {
            this.timeOutShow = false;
            clearInterval(timeOut);
          }
        }, 1000);
      };
    },
    // 获取登录注册配置
    getRegister() {
      this.$API.indexPage().then((res) => {
        if (res) {
          // console.log(res)
          this.registerModel = res.registerModel;
          // this.registerModel = 'v1'
          this.remPwd();
        }
      });
    },
    //验证码
    changeVerify() {
      if (this.active == 0) {
        this.VerifyUrl = "/loginVerifycode.do?time=" + new Date().getTime();
      } else {
        this.VerifyUrl = "/registerVerifycode.do?time=" + new Date().getTime();
      }
    },
    //获取注册信息
    regConfigData() {
      this.$API.regConfigData().then((res) => {
        let pcode = this.$publicJs.getQuery("pcode") || sessionStorage.getItem("pcode");
        if (pcode) {
          this.registerPromoCode = pcode;
          res.filter((item) => {
            if (item.eleName === "promoCode") {
              item.val = pcode; //赋值推广码
            }
          });
          this.registerData = res;
        } else {
          this.registerData = res;
        }
      });
    },
    //注册
    register() {
      if (!this.checked) {
        this.$message.error(this.$t("agreedTermsService"));
        return;
      }
      var params = {};
      if (this.registerModel == "v1") {
        for (let i = 0; i < this.registerData.length; i++) {
          params[this.registerData[i].eleName] = this.registerData[i].val;
        }
        params = JSON.parse(JSON.stringify(params));
        if (params.rpwd && params.pwd != params.rpwd) {
          this.$message.error(this.$t("loginPwdAgain"));
          return;
        }

        this.$API.register(params).then((res) => {
          if (res.success) {
            this.$message.success(this.$t("register") + this.$t("successText"));
            // 刷新用户信息事件
            this.$bus.$emit("refreshUserInfo");
            this.registerShowNo();
          } else {
            // this.$message.error(this.$t('register') + this.$t('errorText') + res.msg);
            this.changeVerify();
          }
        });
      } else if (this.registerModel == "v2") {
        params.email = this.registerEmail;
        params.pwd = this.EmailPassword;
        params.code = this.registerEmailCode;
        if (this.registerPromoCode != "") {
          params.promoCode = this.registerPromoCode;
        }
        if (!params.email.includes("@")) {
          this.$message.error(this.$t("email") + this.$t("formatRrror"));
          return;
        }
        this.$API.emailRegister(params).then((res) => {
          if (res.success) {
            this.$message.success(this.$t("register") + this.$t("successText"));
            // 刷新用户信息事件
            this.$bus.$emit("refreshUserInfo");
            this.registerShowNo();
          }
        });
      } else if (this.registerModel == "v3") {
        params.phone = this.registerPhone;
        params.pwd = this.phonePassword;
        params.code = this.registerPhoneCode;
        if (this.registerPromoCode != "") {
          params.promoCode = this.registerPromoCode;
        }
        if (isNaN(Number(params.phone))) {
          this.$message.error(this.$t("phone") + this.$t("formatRrror"));
          return;
        }
        this.$API.smsRegister(params).then((res) => {
          if (res.success) {
            this.$message.success(this.$t("register") + this.$t("successText"));
            // 刷新用户信息事件
            this.$bus.$emit("refreshUserInfo");
            this.registerShowNo();
          }
        });
      }
    },
    //确定登陆
    LoginClick() {
      //普通登录
      if (this.registerModel == "v1") {
        if (!this.account || !this.password) {
          this.$message.warning(
            this.$t("userName") +
              ", " +
              this.$t("loginPwd") +
              this.$t("canNotEmpty")
          );
          return;
        }
        //邮箱登录
      } else if (this.registerModel == "v2") {
        if (!this.account || !this.password) {
          this.$message.warning(
            this.$t("email") +
              ", " +
              this.$t("loginPwd") +
              this.$t("canNotEmpty")
          );
          return;
        }
        //手机登录
      } else if (this.registerModel == "v3") {
        if (!this.account || !this.password) {
          this.$message.warning(
            this.$t("phone") +
              ", " +
              this.$t("loginPwd") +
              this.$t("canNotEmpty")
          );
          return;
        }
      }
      //判断验证码为空
      if (!this.verifyCod) {
        this.$message.warning(
          this.$t("verificationCode") + this.$t("canNotEmpty")
        );
        return;
      }
      this.isDisabled = true;
      var loginfun = "login";
      var params = {};
      if (this.registerModel == "v1") {
        loginfun = "login";
        params = { username: this.account, password: this.password };
      } else if (this.registerModel == "v2") {
        loginfun = "emailLogin";
        params = { email: this.account, password: this.password };
      } else if (this.registerModel == "v3") {
        loginfun = "smsLogin";
        params = { phone: this.account, password: this.password };
      }
      if (this.verifyCod) {
        params["verifyCode"] = this.verifyCod;
      }
      this.$API[loginfun](params).then((res) => {
        if (res) {
          if (res.success) {
            this.$message.success(this.$t("login") + this.$t("successText"));
            // 记住账号密码
            if (this.passChecked) {
              let jm = Utils.encrypt(
                JSON.stringify({ name: this.account, pwd: this.password })
              );
              localStorage.rmPass = jm;
            } else {
              localStorage.rmPass = "";
            }
            // 刷新用户信息事件
            this.$bus.$emit("refreshUserInfo");
            //把钱从三方转出来
            this.$bus.$emit("refreshAutoTranout");
            this.registerShowNo();
          } else {
            // this.$message.error(this.$t('login') + this.$t('errorText') + res.msg);
            //不清
            // this.account = ''
            // this.password = ''
            // this.verifyCod = ''
            this.isDisabled = false;
          }
        }
      });
    },
    //关闭弹窗
    registerShowNo() {
      var val = { select: 0, show: false };
      this.$bus.$emit("registerShow", val);
    },
    // 拿到记住密码
    remPwd() {
      // localStorage 不能用就是页面刚进来拿不到之前存储的用户信息，不能自动填充账密文本框，都是空的
      // console.log(localStorage.rmPass, 111)
      if (localStorage.rmPass) {
        let decrypt = Utils.decrypt(localStorage.rmPass);
        if (decrypt) {
          decrypt = JSON.parse(decrypt);
          switch (this.registerModel) {
            case "v1":
              this.account = decrypt.name;
              break;
            case "v2":
              this.account = decrypt.name;
              break;
            case "v3":
              this.account = decrypt.name;
              break;
            default:
              break;
          }
          this.password = decrypt.pwd;
          this.passChecked = true;
        }
      }
    },
    // 定义图标名
    iconName(key) {
      switch (key) {
        case 1:
        case 2:
          return "el-icon-unlock";
        case 4:
          return "el-icon-postcard";
        default:
          return "el-icon-user";
      }
    },
  },
  watch: {
    active(val) {
      if (val == 1) {
        this.regConfigData();
      }
      this.changeVerify();
    },
  },
};
</script>

<style lang="scss">
// @-webkit-keyframes myfirst /* Safari 与 Chrome */ {
//   from {
//     background: red;
//   }
//   to {
//     background: yellow;
//   }
// }
.register {
  position: relative;
  position: absolute;
  height: 70%;
  min-height: 400px;
  width: 800px;
  top: 100px;
  left: calc((100% - 820px) / 2);
  background-color: $black1;
  border-radius: 20px;
  padding-bottom: 0;
  display: flex;
  z-index: 3000; //要比Element组件的优先级高
  animation: myfirst 1s;

  @keyframes myfirst {
    from {
      top: 0;
    }

    to {
      top: 100px;
    }
  }

  .close {
    position: absolute;
    width: 40px;
    height: 40px;
    background: $bgjoker;
    border-radius: 100px;
    font-size: 30px;
    text-align: center;
    line-height: 35px;
    opacity: 0.8;
    top: -20px;
    right: -20px;
    cursor: pointer;
  }

  .mainBox {
    flex: 1;
    padding: 20px 20px 10px 20px;

    .selectBotton {
      width: 300px;
      height: 36px;
      display: flex;
      text-align: center;
      background-color: #10161e;
      border-radius: 50px;
      font-size: 12px;
      font-weight: 600;

      .top_Btn {
        flex: 1;
        line-height: 36px;
        cursor: pointer;
      }

      .active_top_Btn {
        background-color: #1a55ef;
        border-radius: 50px;
      }
    }

    .overflow {
      overflow-y: auto;
      height: 90%;
      padding-bottom: 10px;
    }

    .ant_form_logIn {
      width: 100%;
      box-sizing: border-box;
      font-size: 14px;
      max-height: 95%;
      overflow-y: auto;
    }

    .ant_form_register {
      width: 100%;
      box-sizing: border-box;
      font-size: 14px;
      max-height: 95%;
      overflow-y: auto;
      padding-bottom: 10px;

      .loginText {
        margin-bottom: 10px;
      }

      .bottom {
        margin-bottom: 50px;
      }
    }

    .ant_form_item {
      margin-top: 30px;

      p {
        max-width: 100%;
        height: 32px;
        color: rgba(255, 255, 255, 0.85);
      }

      .required {
        color: #527ce9;
        float: right;
        font-size: 20px;
        margin-right: 20px;
      }

      .item_input {
        border-radius: 10px;
        border-color: rgb(32, 42, 57);
        border-width: 2px;
        background-color: initial;
        background-image: none;
        border: 1px solid #434343;
        color: hsla(0, 0%, 100%, 0.85);
        display: inline-flex;
        font-size: 14px;
        line-height: 30px;
        padding: 4px 11px;
        position: relative;
        transition: all 0.3s;
        width: 95%;

        i {
          line-height: 30px;
        }

        input {
          margin-left: 10px;
          color: rgba(255, 255, 255, 0.85);
          border: none;
          background-color: initial;
          width: 100%;
          height: 30px;
          outline: none;
        }

        .sendCode {
          position: absolute;
          right: 10px;
          color: #527ce9;
          cursor: pointer;
          top: -24px;
        }

        .sendCodeNone {
          opacity: 0.7;
          pointer-events: none;
        }
      }
    }

    .loginBottom {
      margin-top: 10px;
      .bottom {
        align-items: center;
        background: linear-gradient(180deg, #0a9dff, #1a52ee);
        border-radius: 10px;
        color: #fff;
        cursor: pointer;
        display: flex;
        font-size: 18px;
        font-weight: 700;
        height: 46px;
        justify-content: center;
        line-height: 40px;
        // margin-top: 25px;
        text-align: center;
        width: 100%;
      }

      .forget {
        margin-top: 10px;
        text-align: end;
        margin-right: 10px;
        // float: right;
        cursor: pointer;
        a {
          color: #2283f7;
        }
      }

      .goobook {
        display: flex;
        float: left;

        img {
          width: 30px;
          margin: 10px;
          border-radius: 5px;
          cursor: pointer;
        }
      }

      .loginText {
        margin-top: 25px;
        margin-bottom: 10px;
        color: #3f526e;
        font-size: 14px;
        font-weight: 500;

        .underline {
          color: #2283f7;
          text-decoration: underline;
          margin-left: 5px;
          cursor: pointer;
        }
      }
    }

    .disableBtn {
      opacity: 0.5;
      pointer-events: none;
    }
  }

  .Registration_mainBox {
    flex: 1;
    background-image: url("../../assets/images/register/loginbg.jpg");
    background-size: 100% 100%;
    border-radius: 0 20px 20px 0;

    div {
      width: 150px;
      height: 30px;
      text-align: center;
      margin: 30px auto;
      line-height: 30px;
      background: #fed700;
      border-radius: 20px;
      color: $black;
      font-size: 20px;
      font-weight: 600;
    }

    p {
      text-align: center;
      font-weight: 700;
      font-size: 24px;
      color: #fff;
    }
  }
}
</style>
