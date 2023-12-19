<template>
  <el-card class="viewTeam-page">
    <div class="top">
      <DateModule @searchMethod="searchMethod" :fastBtn="true"></DateModule>
      <div class="top-bottom">
        <div class="item">
          <p>{{ $t("nowTeamMoney") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{data.teamMoney || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamWithdrawAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.withdrawAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamDepositAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.depositAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("depositArtificial") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.depositArtificial || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("proxyRebateAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num" v-if="data.dailyMoney">{{data.dailyMoney.proxyRebateAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("firstDepositMemNum") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" @click="userTeamListData($t('firstDepositMemNum'),8)">{{data.firstDepositMemNum || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("betNum") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" @click="userTeamListData($t('betNum'),7)">{{data.betNum || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamCount") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" style="font-size:14px" @click="userTeamListData($t('teamCount'),3)">
            <span style="margin-right:15px">{{ $t("proxy") }}：{{data.proxyCount || 0}}</span>
            {{ $t("member") }}：{{data.memberCount || 0}}
          </p>
        </div>
        <div class="item">
          <p>{{ $t("onlineNum") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" @click="userTeamListData($t('onlineNum'),5)">{{data.onlineNum || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("threeNotLoginNum") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" @click="userTeamListData($t('threeNotLoginNum'),6)">{{data.threeNotLoginNum || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("newRegisterCount") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num cursor" @click="userTeamListData($t('newRegisterCount'),4)">{{data.registerCount || 0}}</p>
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
    <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;{{ $t("recordHint") }}</p>

    <!-- 详情弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogShow" width="80%" :close-on-click-modal="false" class="set-dialog">
      <div class="cont">
        <el-table :data="tableData" style="width: 99%" v-if="teamClickType !== 7">
          <el-table-column prop="username" :label="$t('userName')"></el-table-column>
          <el-table-column :label="$t('degreeName')">
            <template slot-scope="scope">
              <p>{{ scope.row.degreeName || '-' }}</p>
            </template>
          </el-table-column>
          <el-table-column prop="level" :label="$t('userLevel')"></el-table-column>
          <el-table-column prop="typeText" :label="$t('userType')"></el-table-column>
          <el-table-column prop="createDatetime" :label="$t('registerTime')"></el-table-column>
          <el-table-column prop="lastLoginDatetime" :label="$t('lastLoginDatetime')"></el-table-column>
          <el-table-column prop="onlineText" :label="$t('onlineStatus')"></el-table-column>
          <el-table-column prop="money" :label="$t('afterMoney')"></el-table-column>
          <el-table-column prop="statusText" :label="$t('status')"></el-table-column>
          <el-table-column prop="unLoginDays" :label="$t('unLoginDays')"></el-table-column>
        </el-table>
        <el-table :data="tableData" style="width: 99%" v-else>
          <el-table-column prop="username" :label="$t('userName')"></el-table-column>
          <el-table-column prop="typeText" :label="$t('userType')"></el-table-column>
          <el-table-column prop="liveBetAmount" :label="$t('liveBetAmount')"></el-table-column>
          <el-table-column prop="liveWinAmount" :label="$t('liveWinAmount')"></el-table-column>
          <el-table-column prop="sportBetAmount" :label="$t('sportBetAmount')"></el-table-column>
          <el-table-column prop="sportWinAmount" :label="$t('sportWinAmount')"></el-table-column>
          <el-table-column prop="chessBetAmount" :label="$t('chessBetAmount')"></el-table-column>
          <el-table-column prop="chessWinAmount" :label="$t('chessWinAmount')"></el-table-column>
          <el-table-column prop="egameBetAmount" :label="$t('egameBetAmount')"></el-table-column>
          <el-table-column prop="egameWinAmount" :label="$t('egameWinAmount')"></el-table-column>
          <el-table-column prop="esportBetAmount" :label="$t('esportBetAmount')"></el-table-column>
          <el-table-column prop="esportWinAmount" :label="$t('egameWinAmount')"></el-table-column>
          <el-table-column prop="fishingBetAmount" :label="$t('fishingBetAmount')"></el-table-column>
          <el-table-column prop="fishingWinAmount" :label="$t('fishingWinAmount')"></el-table-column>
        </el-table>
        <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="userTeamListData"></PageModule>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  name: "index",
  data () {
    return {
      data: '',//数据
      username: '',//用户名
      dialogShow: false,//是否显示弹窗
      dialogTitle: '',//弹窗标题
      tableData: [],//详情数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
      teamClickType: '',//统计数据当前点击的类型
    };
  },
  computed: {
    ...mapState(['onOffBtn', 'userInfo'])
  },
  components: {
    DateModule,
    PageModule
  },
  created () {
    this.searchMethod()
  },
  methods: {
    searchMethod (startTime, endTime) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startDate', this.startTime);
      params.append('endDate', this.endTime);
      params.append('username', this.username);
      this.$axiosPack.post("/userCenter/agentManage/agentTeam.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
        }
      });
    },

    userTeamListData (text, type) {
      if (text) this.dialogTitle = text
      if (type) this.teamClickType = type
      this.dialogShow = true

      //改变提交格式
      let params = new URLSearchParams();
      params.append('start', this.startTime);
      params.append('end', this.endTime);
      params.append('include', true);
      params.append('type', this.teamClickType);
      params.append('pageNumber', this.pageNumber);
      params.append('proxyName', '');
      // params.append('proxyName', this.userInfo.username);
      this.$axiosPack.post("/userCenter/agentManage/userTeamListData.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          for (let i = 0; i < res.data.rows.length; i++) {
            if (this.teamClickType === 7) {
              res.data.rows[i].typeText = res.data.rows[i].userType === 120 ? this.$t("proxy") : this.$t("member")
            } else {
              res.data.rows[i].typeText = res.data.rows[i].type === 120 ? this.$t("proxy") : this.$t("member")
            }
            res.data.rows[i].statusText = res.data.rows[i].status === 2 ? this.$t("open") : this.$t("forbidden")
            res.data.rows[i].onlineText = res.data.rows[i].onlineStatus === 2 ? this.$t("online") : this.$t("offline")
          }
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.viewTeam-page {
  .el-range-input {
    color: #333;
  }
  .el-button--success {
    background-color: #fff;
    border: #fff;
  }
  .el-button--primary {
    background-color: #f12c4c;
    border: #f12c4c;
  }
  .top {
    display: flex;
    flex-wrap: wrap;
  }
  .top-bottom {
    display: flex;
    flex-wrap: wrap;
    .item {
      width: 170px;
      padding: 10px 15px;
      background: #fff;
      border: 1px solid #dcdfe6;
      line-height: 30px;
      font-size: 14px;
      margin-right: 10px;
      margin-bottom: 10px;
      transition: all 0.2s;
      &:hover {
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      }
      .unit {
        color: $greenBtn;
      }
      .num {
        color: $red;
        font-size: 20px;
        &.cursor {
          cursor: pointer;
        }
      }
    }
  }
  .list {
    display: flex;
    flex-wrap: wrap;
    .item {
      margin-right: 10px;
      margin-top: 20px;
      color: #333;
      // &:nth-child(2n) {
      //   margin-right: 0;
      // }
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
          border-right: 1px solid #d6d6d6;
        }
      }
      .td {
        display: flex;
        .num {
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


