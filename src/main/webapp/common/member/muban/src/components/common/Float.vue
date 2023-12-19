<template>
  <div class="floatBox">
    <!-- 签到悬浮框 -->
    <el-badge :is-dot="!signed" class="item" v-if="userInfo.login && onOffBtn.isSignIn">
      <a target="_blank" class="fixed sign" v-banned:click="signBtn" @click="signBtn()" :key="signUpdata">
        <i class="iconfont icon-qiandao"></i>
      </a>
    </el-badge>
    <!-- 在线客服悬浮框 -->
    <a :href="onOffBtn.kfUrl" target="_blank" class="fixed chat" v-if="onOffBtn.kfUrl">
      <i class="iconfont icon-zaixiankefu"></i>
    </a>
    <!-- 签到弹窗 -->
    <el-dialog :visible.sync="signShow" :class="{ markModal: dialogVisible }" style="text-align: center" :modal="false">
      <div class="dialog-title">
        <div class="dialog-hguan">
          <img src="@images/hguan" alt="" />
        </div>
        <p class="dialog-deng">{{ $t("vipAward") }}</p>
        <!-- 签到规则弹窗 -->
        <el-tooltip offset effect="dark" placement="right-start">
          <div slot="content" class="ruleBox">
            <!-- 规则标题 -->
            <div class="rule-gz">
              <h2>{{ $t("signRule") }}</h2>
            </div>
            <!-- 签到内容 -->
            <div class="rule-content" v-if="signRule && signRule.length">
              <div v-for="(val, index) in signRule" :key="index" class="rule-text">
                <p>{{ val.title }}</p>
                <div class="ruleCont" v-html="val.content"></div>
              </div>
            </div>
            <el-empty :image="require('@images/nodata')" :description="$t('noData')" :image-size="128"
              v-else></el-empty>
            <!-- 现金图标 / 积分图标 -->
            <div class="sign-money">
              <div class="money-monImg" v-if="onOffBtn.isShowOnSign">
                <img src="@images/cash-icon" alt="" class="monImg" />
                <span>: {{ $t("bonus") }}</span>
              </div>
              <div class="money-monImg" v-if="onOffBtn.isExchange">
                <img src="@images/score-icon" alt="" class="monImg" />
                <span>: {{ $t("jFen") }}</span>
              </div>
            </div>
          </div>
          <!--签到规则图标 -->
          <i class="el-icon-question question"></i>
        </el-tooltip>
      </div>
      <div class="dialog_Bg">
        <div v-for="(item, index) in dataList" :key="index" @click="siGn(item, index)" class="bjin"
          :class="item.className">
          <img :class="index == 6 ? 'swipe--img1' : 'swipe--img'" src="@images/swip" alt=""
            v-if="item.canSign" />
          <div class="day-item">{{ $t("di") }}{{ item.day }}</div>
          <img :src="item.img" alt="" class="gold-image" />
          <div :class="index == 6 ? 'bonus-item1' : 'bonus-item'">
            <div class="cashBox" v-if="onOffBtn.isShowOnSign">
              <div>
                <img src="@images/cash-icon" class="cashImg" />
              </div>
              <span> {{ item.cash }}</span>
            </div>
            <div class="cashBox" v-if="onOffBtn.isExchange">
              <div>
                <img src="@images/score-icon" alt="" class="cashImg" />
              </div>
              <span> {{ item.score }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 签到记录弹窗 -->
      <span class="rewards-des" @click="signRecord(true)">
        {{ $t("signRecourd") }}
        <i class="el-icon-arrow-right"></i>
      </span>

      <div class="right--drawer-box">
        <aside class="right-drawer" :class="{ show: showDrawer }">
          <div class="drawer--title">
            <h1>{{ $t("signRecourd") }}</h1>
            <img src="@images/da_zhuan_pan/icon_close" @click="signRecord(false)" alt="" />
          </div>
          <div class="table--rows-box">
            <table>
              <thead>
                <th>{{ $t("timeText") }}</th>
                <th>{{ $t("userName") }}</th>
                <th v-if="onOffBtn.isShowOnSign">{{ $t("bonus") }}</th>
                <th v-if="onOffBtn.isExchange">{{ $t("jFen") }}</th>
              </thead>
              <tbody v-if="signRecordList.length">
                <tr v-for="(val, index) in signRecordList" :key="index">
                  <td>{{ dataFun(val.signDate) }}</td>
                  <td>{{ val.username }}</td>
                  <td v-if="onOffBtn.isShowOnSign">{{ val.money }}</td>
                  <td v-if="onOffBtn.isExchange">{{ val.score }}</td>
                </tr>
              </tbody>
            </table>
            <!-- 暂无数据 -->
            <el-empty :image="require('@images/nodata')" :description="$t('noData')" :image-size="128"
              v-if="!signRecordList.length"></el-empty>
          </div>
        </aside>
      </div>
    </el-dialog>
    <!-- 签到成功弹窗 -->
    <el-dialog :visible.sync="dialogVisible" class="finish" style="text-align: center" :modal="false" :show-close="false">
      <div class="finish-body">
        <div class="imgBox">
          <img class="swipe-xuan" src="@images/swips" alt="" />
          <img class="finish-gongxi" src="@images/finish" alt="" />
          <img class="finish-yhua" src="@images/yhua" />
        </div>
        <div class="finish-text">
          <div class="moneyBox" v-if="onOffBtn.isShowOnSign">
            <img src="@images/cash-icon" />
            <span>{{ money }}</span>
          </div>
          <div class="moneyBox" v-if="onOffBtn.isExchange">
            <img src="@images/score-icon" />
            <span>{{ score }}</span>
          </div>
        </div>
        <div>
          <el-button type="primary" @click="close()">{{
            $t("sureText")
          }}</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { setModalMark } from "@/assets/js/utils";
import DATE from "@/assets/js/date";
export default {
  data() {
    return {
      signShow: false, //签到弹窗显示
      dataList: [], //签到数据
      signCount: "", //签到点击次数
      dialogVisible: false, //签到成功弹窗
      signed: "", //今日是否签到
      money: "", //奖金
      score: "", //积分
      signUpdata: 1,
      showDrawer: false, //记录弹窗
      signRecordList: [], //签到记录数据
      signRule: [], //规则说明
    };
  },
  props: {},
  computed: {
    ...mapState(["onOffBtn", "userInfo"]),
  },
  components: {},
  created() { },
  mounted() {
    //登录试玩账号 刷新按钮
    this.$bus.$on("refreshTags", () => {
      setTimeout(() => {
        // 强制更新元素
        this.signUpdata++;
      }, 300);
    });
    //页面刷新  获取今日是否能签到
    if (this.userInfo.login) {
      this.signIn();
    }
  },
  methods: {
    //签到浮框显示
    signBtn() {
      // 判断是否登录
      if (!this.userInfo.login) {
        var val = { select: 0, show: true };
        this.$bus.$emit("registerShow", val);
      } else {
        //签到弹窗
        this.signShow = true;
        this.signRuleConfig();
      }
    },
    //签到点击动作
    siGn(item, index) {
      if (item.canSign) {
        this.$API.sign().then((res) => {
          if (res.success) {
            this.$message({
              message: res.msg,
              type: "success",
            });
            //奖金
            this.money = item.cash;
            //积分
            this.score = item.score;
            // 重新请求一遍
            this.signIn();
            this.signRuleConfig();
            //签到成功弹窗
            this.dialogVisible = true;
          }
        });
      } else {
        //下标小于次数
        if (index < this.signCount) {
          this.$message.error(this.$t("received"));
        } else {
          //下标大于次数
          this.$message.error(this.$t("notEligible"));
        }
      }
    },
    //签到规则
    signRuleConfig() {
      this.$API.signRuleConfig().then((res) => {
        if (res.success) {
          //dayGiftConfig 转为 数组dataList
          this.dataList = JSON.parse(res.content.dayGiftConfig);
          for (let i = 0; i < this.dataList.length; i++) {
            let item = this.dataList[i];
            //根据下标 添加图片
            if (i <= 2) {
              item.img = require(`@images/qiandao`);
            } else if (i >= 3 && i <= 5) {
              item.img = require(`@images/qiandao1`);
            } else {
              item.img = require(`@images/qiandao2`);
              //给第7天 长条样式
              item.className = "day-seven";
            }
            //给高亮样式
            if (item.canSign === true) {
              item.className += " active";
            }
            //给已经签到 暗沉样式
            if (i < this.signCount) {
              item.className += " isSign";
            }
          }
        }
      });
    },
    //签到记录
    signIn() {
      this.$API.signIn().then((res) => {
        if (res) {
          this.signed = res.signed; //今日是否签到
          this.signCount = res.signCount; //签到天数
          this.signRecordList = res.signRecordList; //签到的记录数据
          this.signRule = res.signRule; //规则说明
        }
      });
    },
    //关闭签到成功弹窗
    close() {
      // this.signShow = false;
      this.dialogVisible = false;
    },
    signRecord(show) {
      this.showDrawer = show;
    },
    dataFun(val) {
      return DATE.dateChange(val);
    },
  },
  watch: {
    //弹窗遮罩
    signShow(show) {
      if (show) {
        // 模态框背后模糊
        setModalMark(true);
      } else {
        // 去除模糊
        setModalMark(false);
        this.showDrawer = false;
      }
    },
    //页面登录时 获取今日是否能签到
    "userInfo.login"(newVal, oldVal) {
      if (newVal) {
        this.signIn();
      }
    },
  },
};
</script>

<style lang="scss">
/deep/.el-badge__content.is-dot {
  top: 5px !important;
  right: 10px;
}

.floatBox {
  position: fixed;
  bottom: 50px;
  right: 2%;
  z-index: 1999;

  .fixed {
    color: #fff;
    display: block;
    border-radius: 100%;
    cursor: pointer;
    height: 64px;
    width: 64px;
    font-size: 32px;
    line-height: 64px;
    text-align: center;
    opacity: 0.9;
    transition: all 0.3s ease;

    i {
      font-size: 34px;
    }

    &:hover {
      opacity: 1;
    }
  }

  .chat {
    background-color: #2b82eb;
    margin-top: 10px;
  }

  .sign {
    background: linear-gradient(312deg, #ed812d, #feba5a);
  }

  // -----------------签到页面弹窗
  .el-dialog {
    width: fit-content;
    background: $black1;
    border-radius: 16px;

    .dialog-title {
      display: flex;
      align-items: center;

      .dialog-hguan {
        width: 95px;

        img {
          width: 35%;
        }
      }

      .question {
        width: 95px;
        font-size: 18px;
        cursor: pointer;

        &:hover {
          color: #f1bf1d;
        }
      }

      .dialog-deng {
        width: 200px;
      }
    }

    .el-dialog__body {
      padding: 5px 0px;
      max-width: 390px;
      max-height: 510px;
      font-weight: 800;
      color: #fff;
    }

    .dialog_Bg {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      margin: 5px 15px 15px 15px;

      .bjin {
        background-color: $blue;
        border-radius: 10px;
        cursor: pointer;
        margin: 6px;
        width: 30%;
        max-height: 137px;

        .gold-image {
          width: 108px;
          height: 59px;
          margin: auto;
        }

        .cashBox {
          display: flex;
          justify-content: center;
          align-items: center;
          height: 23px;
          width: 100%;

          div {
            width: 35%;
            height: 18px;
          }

          span {
            display: block;
            width: 35%;
          }

          .cashImg {
            width: 18px;
            margin-top: 0;
          }
        }

        .day-item {
          background-color: #f89827;
          border-radius: 10px 0;
          font-size: 12px;
          height: 24px;
          line-height: 24px;
          width: 65px;
          opacity: 0.99;
        }

        .bonus-item {
          display: flex;
          flex-direction: column;
          justify-content: center;
          height: 46px;
        }
      }

      .day-seven {
        height: 81px;
        width: 100%;
        display: flex;

        .bonus-item1 {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          flex: 1;
        }
      }

      .isSign {
        cursor: not-allowed;
        opacity: 0.5;
      }

      .active {
        background: linear-gradient(180deg, #fed791, #f39d42);
        overflow: hidden;
        position: relative;

        .swipe--img {
          animation: stroll 10s linear infinite;
          position: absolute;
          width: 170px;
          left: -30px;
          top: -16px;
        }

        .swipe--img1 {
          animation: stroll 10s linear infinite;
          position: absolute;
          height: 250px;
          top: -80px;
        }

        @keyframes stroll {
          0% {
            transform: rotate(0deg);
          }

          100% {
            transform: rotate(360deg);
          }
        }
      }
    }

    //-----------------签到记录弹窗
    .rewards-des {
      font-size: 14px;
      font-weight: 700;
      cursor: pointer;

      &:hover {
        color: $blue;
      }
    }
  }

  .right--drawer-box {
    width: 567px;
    height: 611px;
    bottom: 560px;
    border-radius: 20px;
    background: transparent;
    position: relative;
    pointer-events: none;
    overflow: hidden;
  }

  .right-drawer {
    font-weight: 400;
    width: 100%;
    height: 100%;
    left: 570px;
    border-radius: 20px;
    position: absolute;
    transition: left 1s;
    pointer-events: all;
    background: linear-gradient(180deg, #2078fb, #0a0f32 13%, #0a0f32);

    .drawer--title {
      display: flex;
      align-items: center;
      height: 73px;

      h1 {
        width: 100%;
        text-align: center;
        font-size: 24px;
      }

      img {
        width: 64px;
        min-height: 40px;
        max-height: 40px;
        right: 33px;
        position: absolute;
        background: transparent;
        cursor: pointer;
      }
    }

    table {
      width: 100%;
      padding: 0 8px;

      th {
        width: 20%;
        border-bottom: 1px solid #333030;
        background: linear-gradient(180deg, #db003b, #9844b7 86%, #156af6);
        background-clip: text;
        -webkit-background-clip: text;
        color: transparent;
        font-size: 18px;
        height: 40px;
      }

      th:nth-child(1) {
        width: 30%;
      }

      th:nth-child(2) {
        width: 30%;
      }

      td {
        text-align: center;
        height: 40px;
      }
    }

    div {
      width: 100%;
      height: 460px;
      overflow-y: auto;
    }

    .table--rows-box::-webkit-scrollbar {
      display: none;
    }
  }

  .show {
    left: 0;
  }

  // --------------------------------------------------------签到成功弹窗
  .markModal {
    filter: blur(10px);
  }

  .finish {
    .el-dialog {
      background: transparent;
      box-shadow: none;

      .finish-body {
        display: flex;
        flex-direction: column;
        max-height: 465px;

        .imgBox {
          .swipe-xuan {
            animation: stroll 10s linear infinite;
          }
        }

        .finish-gongxi {
          position: absolute;
          left: 20%;
          top: 20%;
          height: 45%;
        }

        .finish-yhua {
          position: absolute;
          width: 100%;
          top: 15%;
          right: 0;
        }

        .finish-text {
          display: flex;
          justify-content: space-evenly;
          align-items: center;
          background: no-repeat center/100% 100% url('../../assets/images/butt');
          margin: auto;
          width: 216px;

          .moneyBox {
            height: 50px;
            font-size: 24px;
            display: flex;
            align-items: center;
            justify-content: center;

            span {
              margin-left: 10px;
            }
          }
        }
      }

      .el-button {
        margin-top: 20px;
        width: 130px;
      }

      @keyframes stroll {
        0% {
          transform: rotate(0deg);
        }

        100% {
          transform: rotate(360deg);
        }
      }
    }
  }
}

// ------------------签到规则
.ruleBox {
  width: 330px;
  max-height: 430px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .rule-gz {
    height: 7%;
  }

  ::-webkit-scrollbar-track {
    display: none;
  }

  ::-webkit-scrollbar-thumb {
    background-color: $blackinput;
  }

  .rule-content {
    width: 100%;
    height: 80%;
    overflow-y: auto;
    text-align: center;
    display: flex;
    flex-direction: column;

    .rule-text {
      margin: 10px 0;

      p {
        font-size: 16px;
        margin: 10px 0;
      }
    }
  }

  .sign-money {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 80%;

    .money-monImg {
      width: 50%;
      display: flex;
      justify-content: center;
      align-items: center;

      .monImg {
        width: 20px;
      }

      span {
        font-size: 14px;
        margin-left: 5px;
      }
    }
  }
}
</style>