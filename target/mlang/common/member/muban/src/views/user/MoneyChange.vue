<template>
  <div class="moneyChange-page order-page">
    <div class="heard">
      <!-- 额度转换 -->
      {{ $t("moneyChange") }}
    </div>
    <!-- <el-card shadow="always" class="change-head">
      <p class="base-info">{{ $t("welcomeLogin") }}，
        <span class="blueText" style="margin-left:0">{{userInfo.username}}</span>
        <router-link to="/securityCenter" class="blueText">{{ $t("pwdEdit") }}</router-link>
        <router-link to="/message" class="blueText">{{ $t("message") }}</router-link>
        <router-link to="/notice" class="blueText">{{ $t("notice") }}</router-link>
        <el-button type="primary" size="mini" @click="goPay">{{ $t("imPay") }}</el-button>
        <el-button type="danger" size="mini" @click="goAgency">{{ $t("applyAgency") }}</el-button>
      </p>
      <p class="base-info">{{ $t("lastLogin") }}：{{data.lastLoginTime}}</p>
    </el-card> -->
    <div class="change-box">
      <div class="searchDiv">
        <div class="item">
          {{ $t("from") }}：
          <el-select v-model="outcash" :placeholder="$t('pleaseSelect')">
            <el-option v-for="item in fromArr" :key="item.platform" :label="item.name" :value="item.platform">
            </el-option>
          </el-select>
        </div>
        <div class="item">
          {{ $t("shiftTo") }}：
          <el-select v-model="incash" :placeholder="$t('pleaseSelect')">
            <el-option v-for="item in toArr" :key="item.platform" :label="item.name" :value="item.platform">
            </el-option>
          </el-select>
        </div>
        <div class="item">
          {{ $t("outMoney") }}：
          <el-input v-model="outCashInput" v-amountFormat:outCashInput :placeholder="$t('pleaseInput')" class="outInput" clearable></el-input>
        </div>
        <div class="item">
          <!-- 查询 -->
          <el-button type="danger" style="margin: 0 15px" @click="thirdRealTransMoney">{{ $t("surePut") }}</el-button>
          <el-button type="primary" @click="goMoneyChangeHis()" style="margin-left:15px;">{{ $t("moneyChangeHis") }}</el-button>
        </div>
      </div>
      <div class="cont">
        <div class="left">
          <p>{{ $t("money") }}</p>
          <p class="num">{{ userInfo.money || 0 | amount }}</p>
          <p style="margin-top: 20px">{{ $t("score") }}</p>
          <p class="num">{{ data.score | amount }}</p>
        </div>
        <div class="right" v-if="data.third">
          <div class="item" v-for="(val,i) in data.third" :key="i">
            {{val.name}}&nbsp;{{ $t("limitMoney") }}<span :class="{num: !val.money, money: val.money}">{{ val.money | amount}}</span>
            <img src="@/assets/images/index/refresh_icon.png" class="icon-refresh"
            :class="{refreshing: spinning && spinningIndex == i}" @click="getBalance(val.platform,i)" alt="refresh_icon">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      data: {
        score: "",
        third: [],
      }, //额度转换数据
      fromArr: [],//从
      toArr: [],//去
      incash: '',//转入
      outcash: '',//转出
      outCashInput: '',//转换的值
      spinning: false,  // 是否转圈
      spinningIndex: -1 // 转圈的图标下标
    };
  },
  computed: {
    ...mapState(['userInfo', 'onOffBtn', 'gameOnOff'])
  },
  components: {
  },
  mounted () {
    // 获取三方额度数据
    this.exchangePageInfo();
  },
  methods: {
    goMoneyChangeHis () {
      this.$router.push('/moneyChangeHis');
    },
    // 立即充值
    goPay () {
      this.$router.push('/recharge');
    },
    // 申请代理
    goAgency () {
      top.location.href = '/help.do?code=4';
    },
    // 根据开关判断游戏列表
    getGameList (arr) {
      let sys = [{ platform: 'sys', name: this.$t("sysMoney") }];
      this.fromArr = [...sys, ...arr];
      this.toArr = [...arr, ...sys];
    },

    // 获取三方额度数据
    exchangePageInfo (update) {
      // let load = null
      // if (!update) load = true

      this.$API.exchangePageInfo({ load: true }).then(res => {
        if (res) {
          this.data.score = res.score;
          this.data.third = res.third;
          if (!update) this.getGameList(res.third);
          //所有数据单独请求一次接口
          for (let i = 0; i < res.third.length; i++) {
              const platform = res.third[i].platform;
              this.getBalance(platform, i);
            }
        }
      });
    },

    // 获取三方额度数据
    getBalance (platform,i) {
      this.spinning = true;
      this.spinningIndex = i;
      //改变提交格式
      let params = {
        'platform': platform
      }
      this.$API.getBalance(params).then(res => {
        if (res) {
          // 转换之后更新系统余额
          // let user = this.userInfo
          // user.money = res.sysMoney
          // 将信息存储到 store，并且存入 sessionStorage
          // this.$store.dispatch("getUserInfo", user);
          // sessionStorage.userInfo = JSON.stringify(user);
           //金额赋值
           this.$set(this.data.third[i], "money", res.money);
          this.$bus.$emit("refreshUserInfo");
          this.spinning = false;
        }
      });
    },
    // 转换额度
    thirdRealTransMoney () {
      if (!this.incash || !this.outcash || !this.outCashInput) {
        // 表单输入是必填否则有提示
        let msg = `${this.$t("from") + '/ ' + this.$t("shiftTo") + '/ ' + this.$t("outMoney") + this.$t('canNotEmpty')}!`
        this.$message({
          message: msg,
          type: 'warning'
        });
        return
      }

      //改变提交格式
      let params = {
        'changeTo': this.incash,
        'changeFrom': this.outcash,
        'money': this.outCashInput,
      }
      params.load = true
      this.$API.thirdRealTransMoney(params).then(res => {
        if (res.success) {
          this.$notify({
            title: 'success',
            message: res.msg,
            type: 'success'
          });

          let platform = ''
          if (this.incash === 'sys') platform = this.outcash
          else platform = this.incash

          this.getBalance(platform)
          this.exchangePageInfo(true)
        }
      });
    },
  },
  watch: {
    outCashInput () {
      // 转换的额度 bbin 必须是整数，其他可以2位小数
      if (this.incash === 'BBIN' || this.outcash === 'BBIN') {
        this.outCashInput = this.$publicJs.retainNumFun(this.outCashInput, 'no')
      } else {
        this.outCashInput = this.$publicJs.retainNumFun(this.outCashInput)
      }
    },
    outcash () {
      // console.log(22, this.outcash)
      if (this.outcash === 'sys') {
        if (this.incash === 'sys') this.incash = ''
      } else {
        this.incash = 'sys'
      }
    },
    incash () {
      // console.log(11, this.incash)
      if (this.incash === 'sys') {
        if (this.outcash === 'sys') this.outcash = ''
      } else {
        this.outcash = 'sys'
      }
    }
  }
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';

.moneyChange-page {
  padding: 0 10px;
  // color: $borderColorJoker;
  .el-card {
    background: $black;
    .el-card__body {
      color: $colorText;
    }
  }

  .change-head {
    margin-bottom: 20px;
    margin-top: 20px;
    background-color: $black;
    border: 1px solid $borderColorJoker;
    .base-info {
      margin-top: 10px;
      color: #fff;
      .blueText {
        margin: 0 10px;
        color: #fff;
      }
      .el-button {
        margin-left: 20px;
      }
      .el-button--primary {
        background: #f12c4c;
        border-color: #f12c4c;
      }
      .el-button--danger {
        background: #2283f6;
        border-color: #67c23a;
      }
    }
  }
  .change-box {
    // .toMoneyChangeHis {
    //   border: 1px solid $borderColorJoker;
    //   border-radius: 5px;
    //   color: $borderColorJoker;
    //   font-weight: 600;
    //   padding: 7px 10px;
    //   margin-left: 15px;
    // }
    .cont {
      display: flex;
      text-align: center;
      margin-top: 20px;
      border: 1px solid $borderColorJoker;
      box-shadow: inset 0px 3px 9px #dcdfe6;
      .left {
        width: 160px;
        padding: 20px 5px;
        border-right: 1px solid $borderColorJoker;
        color: #fff;
        .num {
          color: #0ec504;
          font-size: 24px;
        }
      }
      .right {
        // width: calc(100% - 160px);
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        .item {
          width: 33%;
          border-right: 1px solid $borderColorJoker;
          border-bottom: 1px solid $borderColorJoker;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 30px 10px;
          color: #fff;
          &:nth-child(3n) {
            border-right: none;
          }
          .num {
            margin: 0 20px;
            color:red;
            max-width: 200px;
          }
          .money {
            margin: 0 20px;
            color:#0ec504;
            max-width: 200px;
          }
        }
      }
    }
  }
}
</style>
