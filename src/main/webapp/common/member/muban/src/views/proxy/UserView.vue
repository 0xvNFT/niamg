<template>
  <div class="userView-page">
    <div class="userView-top">
      <div class="top-left">
        <div class="searchDiv">
          <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
          <!-- 查询 -->
          <el-button type="primary" @click="userAllInfo()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{ $t("searchText") }}</el-button>
        </div>
        <div class="content">
          <div class="item">
            <div class="el-icon-edit-outline icon"></div>
            <div class="item-cont">
              <!-- 总充值 -->
              <p>{{ $t("totalPay") }}</p>
              <p>{{Number(dailyMoney.depositAmount) + Number(dailyMoney.depositArtificial) || 0 | amount}}</p>
            </div>
          </div>
          <div class="item">
            <div class="el-icon-edit-outline icon"></div>
            <div class="item-cont">
              <!-- 总提现 -->
              <p>{{ $t("totalWithdraw") }}</p>
              <p>{{dailyMoney.withdrawAmount || 0 | amount}}</p>
            </div>
          </div>
          <div class="item">
            <div class="el-icon-edit-outline icon"></div>
            <div class="item-cont">
              <!-- 代理返点 -->
              <p>{{ $t("proxyRebateAmount") }}</p>
              <p>{{ dailyMoney.proxyRebateAmount || 0 | amount }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="top-right" v-if="data.degreeShow">
        <div class="tip">
          <span>{{ $t("againPay") }} {{data.nextDegreeDepositMoney || 0}} {{ $t("againPayleft") }}</span>
        </div>
        <div class="line">
          <div class="circle active"></div>
          <el-progress :percentage="60" color="$borderColorJoker" :stroke-width="5" :show-text="false"></el-progress>
          <div class="circle"></div>
          <el-progress :percentage="0" color="$borderColorJoker" :stroke-width="5" :show-text="false"></el-progress>
        </div>
        <div class="info">
          <div>{{data.curDegreeName || $t("noSet") }} ({{ $t("needPay") }}: {{data.curDegreeDepositMoney || 0 | amount}})</div>
          <div style="margin-left:10px">{{data.newDegreeName || $t("noSet") }} ({{ $t("needPay") }}:{{data.newDegreeDepositMoney || 0}}) </div>
        </div>
      </div>
    </div>
    <div class="game-content" v-if="onOffBtn.game">
      <div class="item" v-if="onOffBtn.game.sport === 2">
        <h1>{{ $t("sport") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.sportBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.sportWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.sportRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.sportBetNum | amount }}</td>
          </tr>
        </table>
      </div>
      <div class="item" v-if="onOffBtn.game.egame === 2">
        <h1>{{ $t("egame") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.egameBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.egameWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.egameRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.egameBetNum | amount }}</td>
          </tr>
        </table>
      </div>
      <div class="item" v-if="onOffBtn.game.live === 2">
        <h1>{{ $t("live") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.liveBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.liveWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.liveRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.liveBetNum | amount }}</td>
          </tr>
        </table>
      </div>
      <div class="item" v-if="onOffBtn.game.chess === 2">
        <h1>{{ $t("chess") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.chessBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.chessWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.chessRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.chessBetNum | amount }}</td>
          </tr>
        </table>
      </div>
      <div class="item" v-if="onOffBtn.game.esport === 2">
        <h1>{{ $t("esport") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.esportBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.esportWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.esportRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.esportBetNum | amount }}</td>
          </tr>
        </table>
      </div>
      <div class="item" v-if="onOffBtn.game.fishing === 2">
        <h1>{{ $t("fish") }}</h1>
        <table>
          <tr class="text text-cont">
            <td class="cont">{{ $t("betAmount") }}</td>
            <td class="cont">{{ $t("winAmount") }}</td>
            <td class="cont">{{ $t("rebateAmount")}}</td>
            <td class="cont">{{ $t("realBettingMoney")}}</td>
          </tr>
          <tr class="text text-num">
            <td class="cont">{{ dailyMoney.fishingBetAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.fishingWinAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.fishingRebateAmount | amount }}</td>
            <td class="cont">{{ dailyMoney.fishingBetNum | amount }}</td>
          </tr>
        </table>
      </div>
    </div>

    <!-- <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;•{{ $t("indexHint") }},</p> -->
    <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;{{ $t("recordHint") }}</p>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DATE from "@/assets/js/date.js";

export default {
  data () {
    return {
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      date: [],//日期
      dailyMoney: {},//游戏内容
      data: '',
      // fastProgress: 0,
      // lastProgress: 0,
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
  },
  created () {
  },
  mounted () {
    this.userAllInfo()
  },
  methods: {
    userAllInfo () {
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
      }
      params.load = true

      this.$API.userAllInfo(params).then((res) => {
        if (res) {
          this.data = res
          if (res.dailyMoney) this.dailyMoney = res.dailyMoney
          // this.fastProgress = 100 - (res.curDegreeDepositMoney * 1 - res.nextDegreeDepositMoney * 1) / res.curDegreeDepositMoney * 100 * 1
          // console.log(this.fastProgress, 'this.fastProgress')
        }
      })
    }
  },
  watch: {
    date (e) {
      if (e == null) {
        this.date = []
        this.date[0] = this.startTime
        this.date[1] = this.endTime
      }
    }
  }
}

</script>

<style lang="scss">
.userView-page {
  border: 1px solid $borderColorJoker;
  background-color: $black;
  color: $colorJoker;
  transition: 0.3s;
  border-radius: 4px;
  padding: 20px;
  overflow: hidden;
  color: $borderColorJoker;
  &:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  .userView-top {
    display: flex;
    flex-wrap: wrap;
    .top-left {
      // width: 60%;
      margin-right: 130px;
      margin-bottom: 10px;
      .content {
        display: flex;
        margin-top: 15px;
        .item {
          display: flex;
          margin-right: 25px;
          padding: 10px 24px;
          border: 1px $borderColorJoker solid;
          align-items: center;
          transition: all 0.2s;
          border-radius: 5px;
          // background: #fff;
          &:hover {
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12),
              0 0 6px rgba(0, 0, 0, 0.04);
          }
          .icon {
            margin-right: 15px;
          }
          .item-cont {
            // display: flex;
            p{
            text-align: center;
            }
          }
        }
      }
    }
    .top-right {
      // width: 400px;
      padding: 15px 10px 10px;
      box-sizing: border-box;
      border: 1px solid $borderColorJoker;
      margin-bottom: 10px;
      transition: all 0.2s;
      &:hover {
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      }
      .tip {
        position: relative;
        border-radius: 5px;
        // height: 30px;
        line-height: 30px;
        font-weight: normal;
        font-size: 12px;
        border: #cccccc solid 1px;
        top: -6px;
        padding: 0 10px;
        width: 200px;
        text-align: center;
        margin: 0 auto;
        // color: #333;
        &:before {
          content: "";
          display: block;
          position: absolute;
          border-width: 10px;
          border-style: solid;
          border-color: #ccc transparent transparent transparent;
          bottom: -20px;
          left: 50%;
          margin-left: -10px;
        }
        &:after {
          content: "";
          display: block;
          position: absolute;
          border-width: 10px;
          border-style: solid;
          border-color: #fff transparent transparent transparent;
          bottom: -19px;
          left: 50%;
          margin-left: -10px;
        }
      }
      .line {
        display: flex;
        align-items: center;
        justify-content: center;
        .el-progress--line {
          width: 130px;
          position: relative;
          z-index: 1;
          margin-left: -3px;
        }
        .el-progress-bar__outer,
        .el-progress-bar__inner {
          border-radius: 0;
        }
        .circle {
          border-radius: 50%;
          width: 20px;
          height: 20px;
          background: #999999;
          position: relative;
          z-index: 2;
          // &.active {
          //   background: $greenBtn;
          // }
        }
      }
      .info {
        display: flex;
        font-size: 14px;
        margin-top: 7px;
      }
    }
  }

  .game-content {
    display: flex;
    flex-wrap: wrap;
    margin-top: 15px;
    color: $borderColorJoker;
    h1{
      color: $colorText;
    }
    .item {
      margin-right: 15px;
      padding: 5px 8px;
      .text-num{
        color: $titleColor;
      }
      div {
        background: $titleBg;
        color: #fff;
        box-sizing: border-box;
        border-top: 1px solid $borderColorJoker;
        border-left: 1px solid $borderColorJoker;
      }
      ul {
        border: 1px solid $borderColorJoker;
        box-sizing: border-box;
        border-top: 0;
      }
      .text {
        // display: flex;
        box-sizing: border-box;
        line-height: 35px;
        border-collapse: collapse;
        .cont {
          padding: 8px;
          text-align: center;
          min-width: 140px;
          box-sizing: border-box;
          // border-right: 1px solid $borderColorJoker;
        }
        // &:last-child {
        //   border-right: 0;
        // }
      }
      table,
      th,
      td {
        border: 1px solid $borderColorJoker;
        border-spacing: 0;
      }
    }
  }
  .hint {
    margin-top: 15px;
    color: $borderColorJoker;
  }
}
</style>
