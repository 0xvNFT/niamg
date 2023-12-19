<template>
  <el-card class="viewUser-page">
    <div class="top">
      <div class="top-left">
        <DateModule @searchMethod="searchMethod" :fastBtn="true"></DateModule>
        <div class="top-left-bottom">
          <router-link to="/moneyChange" class="goEd">{{ $t("moneyChange") }}</router-link>
          <div class="item">
            <div class="iconfont iconzhanghuchongzhi"></div>
            <div class="item-right">
              <!-- <span class="unit">（元）</span> -->
              <p>{{ $t("totalPay") }}</p>
              <p class="num" v-if="data.dailyMoney">{{Number(data.dailyMoney.depositAmount) + Number(data.dailyMoney.depositArtificial)}}</p>
            </div>
          </div>
          <div class="item">
            <div class="iconfont icontixian"></div>
            <div class="item-right">
              <p>{{ $t("totalWithdraw") }}</p>
              <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.withdrawAmount || 0}}</p>
            </div>
          </div>
          <div class="item">
            <div class="iconfont iconfandian"></div>
            <div class="item-right">
              <p>{{ $t("proxyRebateAmount") }}</p>
              <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.proxyRebateAmount || 0}}</p>
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
          <el-progress :percentage="10" color="#67C23A" :stroke-width="20" :show-text="false"></el-progress>
          <div class="circle"></div>
          <el-progress :percentage="0" color="#67C23A" :stroke-width="20" :show-text="false"></el-progress>
        </div>
        <div class="info">
          <div>{{data.curDegreeName || $t("noSet") }}({{ $t("needPay") }}:{{data.curDegreeDepositMoney || 0}})</div>
          <div style="margin-left:10px">{{data.newDegreeName || $t("noSet") }}({{ $t("needPay") }}:{{data.newDegreeDepositMoney || 0}})</div>
        </div>
      </div>
    </div>
    <div class="list" v-if="onOffBtn.game">
      <!-- <div class="item" v-if="onOffBtn.game.lottery === 2">
        <h1>{{ $t("lotText") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.lotBetAmount}}</div>
            <div class="num">{{data.dailyMoney.lotWinAmount}}</div>
            <div class="num">{{data.dailyMoney.lotRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.lotBetNum}}</div>
          </div>
        </div>
      </div> -->
      <div class="item" v-if="onOffBtn.game.sport === 2">
        <h1>{{ $t("sport") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.sportBetAmount}}</div>
            <div class="num">{{data.dailyMoney.sportWinAmount}}</div>
            <div class="num">{{data.dailyMoney.sportRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.sportBetNum}}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.egame === 2">
        <h1>{{ $t("egame") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.egameBetAmount}}</div>
            <div class="num">{{data.dailyMoney.egameWinAmount}}</div>
            <div class="num">{{data.dailyMoney.egameRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.egameBetNum}}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.live === 2">
        <h1>{{ $t("live") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.liveBetAmount}}</div>
            <div class="num">{{data.dailyMoney.liveWinAmount}}</div>
            <div class="num">{{data.dailyMoney.liveRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.liveBetNum}}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.chess === 2">
        <h1>{{ $t("chess") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.chessBetAmount}}</div>
            <div class="num">{{data.dailyMoney.chessWinAmount}}</div>
            <div class="num">{{data.dailyMoney.chessRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.chessBetNum}}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.esport === 2">
        <h1>{{ $t("esport") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.esportBetAmount}}</div>
            <div class="num">{{data.dailyMoney.esportWinAmount}}</div>
            <div class="num">{{data.dailyMoney.esportRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.esportBetNum}}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.fishing === 2">
        <h1>{{ $t("fish") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{data.dailyMoney.fishingBetAmount}}</div>
            <div class="num">{{data.dailyMoney.fishingWinAmount}}</div>
            <div class="num">{{data.dailyMoney.fishingRebateAmount}}</div>
            <div class="num">{{data.dailyMoney.fishingBetNum}}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;•{{ $t("indexHint") }}</p> -->
    <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;{{ $t("indexRemark") }}</p>
  </el-card>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'

export default {
  name: "index",
  data () {
    return {
      data: '',
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    DateModule
  },
  created () {
    if(this.onOffBtn){
      this.searchMethod();
    }
  },
  methods: {
    searchMethod (startTime, endTime) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      this.$axiosPack.post("/userCenter/userAllInfo.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
        }
      });
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.viewUser-page {
  .el-button--small {
    background-color: #f12c4c;
    border: #f12c4c;
  }
  .top {
    display: flex;
    flex-wrap: wrap;
  }
  .top-left {
    // width: 780px;
    margin-right: 20px;
    margin-bottom: 10px;
    .top-left-bottom {
      display: flex;
      justify-content: space-between;
      .goEd {
        width: 120px;
        background: #0ec504;
        border-radius: 4px;
        color: #fff;
        font-size: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .item {
        display: flex;
        align-items: center;
        padding: 10px 24px;
        background: #fff;
        border: 1px solid #dcdfe6;
        line-height: 30px;
        transition: all 0.2s;
        &:hover {
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
        }
        .iconfont {
          color: $red;
          font-size: 32px;
        }
        .item-right {
          font-size: 14px;
          margin-left: 15px;
          color: #333;
          .unit {
            color: $greenBtn;
          }
          .num {
            color: $red;
            font-size: 20px;
          }
        }
      }
    }
  }
  .top-right {
    width: 400px;
    background: #fff;
    padding: 15px 10px 10px;
    box-sizing: border-box;
    border: 1px solid #dcdfe6;
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
      color: #333;
      &:before {
        content: '';
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
        content: '';
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
        width: 40px;
        height: 40px;
        background: #999999;
        position: relative;
        z-index: 2;
        &.active {
          background: $greenBtn;
        }
      }
    }
    .info {
      display: flex;
      font-size: 14px;
      margin-top: 7px;
    }
  }
  .list {
    display: flex;
    flex-wrap: wrap;
    .item {
      margin-right: 10px;
      margin-top: 20px;
      color: #333;
      &:nth-child(2n) {
        // margin-right: 0;
      }
      h1 {
        font-size: 20px;
        color: #333;
        line-height: 36px;
      }
      .title,
      .num {
        width: 121px;
        padding: 10px;
        box-sizing: border-box;
        text-align: center;
      }
      .th {
        background: $border;
        display: flex;
        font-size: 16px;
        .title {
          word-break: break-all;
          border-right: 1px solid #d6d6d6;
          color: #333;
        }
      }
      .td {
        display: flex;
        .num {
          word-break: break-all;
          border-right: 1px solid $border;
          border-bottom: 1px solid $border;
          &:first-child {
            border-left: 1px solid $border;
          }
        }
      }
    }
  }
  .hint {
    margin-top: 15px;
    color: red;
  }
}
</style>


