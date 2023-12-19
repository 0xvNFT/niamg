<template>
  <div class="viewTeam-page">
    <!-- 团队总览 -->
    <div class="heard">
      {{ $t("viewTeam") }}
    </div>
    <div class="top">
      <!-- <DateModule @searchMethod="searchMethod" :fastBtn="true"></DateModule> -->
      <div class="top-bottom">
        <div class="item">
          <p>
            {{ $t("nowTeamMoney")
            }}<span class="unit">（{{ onOffBtn.currency }}）</span>
          </p>
          <p class="num">{{ data.teamMoney || 0 | amount }}</p>
        </div>
        <div class="item">
          <p>
            {{ $t("teamWithdrawAmount")
            }}<span class="unit">（{{ onOffBtn.currency }}）</span>
          </p>
          <p class="num" v-if="data.dailyMoney">
            {{ data.dailyMoney.withdrawAmount || 0 | amount }}
          </p>
        </div>
        <div class="item">
          <p>
            {{ $t("teamDepositAmount")
            }}<span class="unit">（{{ onOffBtn.currency }}）</span>
          </p>
          <p class="num" v-if="data.dailyMoney">
            {{ data.dailyMoney.depositAmount || 0 | amount }}
          </p>
        </div>
        <div class="item">
          <p>
            {{ $t("depositArtificial")
            }}<span class="unit">（{{ onOffBtn.currency }}）</span>
          </p>
          <p class="num" v-if="data.dailyMoney">
            {{ data.dailyMoney.depositArtificial || 0 | amount }}
          </p>
        </div>
        <div class="item">
          <p>
            {{ $t("proxyRebateAmount")
            }}<span class="unit">（{{ onOffBtn.currency }}）</span>
          </p>
          <p class="num" v-if="data.dailyMoney">
            {{ data.dailyMoney.proxyRebateAmount || 0 | amount }}
          </p>
        </div>
        <div
          class="item cursor"
          @click="userTeamListData($t('firstDepositMemNum'), 8)"
        >
          <p>
            {{ $t("firstDepositMemNum")
            }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num">
            {{ data.firstDepositMemNum || 0 }}
          </p>
        </div>
        <div class="item cursor" @click="userTeamListData($t('effeBetNum'), 7)">
          <p>
            {{ $t("betNum") }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num">
            {{ data.betNum || 0 }}
          </p>
        </div>
        <div class="item cursor" @click="userTeamListData($t('teamCount'), 3)">
          <p>
            {{ $t("teamCount")
            }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num" style="font-size: 14px">
            <span style="margin-right: 15px"
              >{{ $t("proxy") }}：{{ data.proxyCount || 0 }}</span
            >
            {{ $t("member") }}：{{ data.memberCount || 0 }}
          </p>
        </div>
        <div class="item cursor" @click="userTeamListData($t('onlineNum'), 5)">
          <p>
            {{ $t("onlineNum")
            }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num">
            {{ data.onlineNum || 0 }}
          </p>
        </div>
        <div
          class="item cursor"
          @click="userTeamListData($t('threeNotLoginNum'), 6)"
        >
          <p>
            {{ $t("threeNotLoginNum")
            }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num">
            {{ data.threeNotLoginNum || 0 }}
          </p>
        </div>
        <div
          class="item cursor"
          @click="userTeamListData($t('newRegisterCount'), 4)"
        >
          <p>
            {{ $t("newRegisterCount")
            }}<span class="unit">（{{ $t("people") }}）</span>
            <i class="el-icon-s-data" style="color: gray"></i>
          </p>
          <p class="num">
            {{ data.registerCount || 0 }}
          </p>
        </div>
      </div>
    </div>
    <div class="gamelist" v-if="onOffBtn.game">
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
            <div class="num">{{data.dailyMoney.lotBetAmount | amount}}</div>
            <div class="num">{{data.dailyMoney.lotWinAmount | amount}}</div>
            <div class="num">{{data.dailyMoney.lotRebateAmount | amount}}</div>
            <div class="num">{{data.dailyMoney.lotBetNum | amount}}</div>
          </div>
        </div>
      </div> -->
      <div class="item" v-if="onOffBtn.game.sport === 2">
        <h1>{{ $t("sport") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{ data.dailyMoney.sportBetAmount | amount }}</div>
            <div class="num">{{ data.dailyMoney.sportWinAmount | amount }}</div>
            <div class="num">
              {{ data.dailyMoney.sportRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.sportBetNum | amount }}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.egame === 2">
        <h1>{{ $t("egame") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{ data.dailyMoney.egameBetAmount | amount }}</div>
            <div class="num">{{ data.dailyMoney.egameWinAmount | amount }}</div>
            <div class="num">
              {{ data.dailyMoney.egameRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.egameBetNum | amount }}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.live === 2">
        <h1>{{ $t("live") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{ data.dailyMoney.liveBetAmount | amount }}</div>
            <div class="num">{{ data.dailyMoney.liveWinAmount | amount }}</div>
            <div class="num">
              {{ data.dailyMoney.liveRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.liveBetNum | amount }}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.chess === 2">
        <h1>{{ $t("chess") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">{{ data.dailyMoney.chessBetAmount | amount }}</div>
            <div class="num">{{ data.dailyMoney.chessWinAmount | amount }}</div>
            <div class="num">
              {{ data.dailyMoney.chessRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.chessBetNum | amount }}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.esport === 2">
        <h1>{{ $t("esport") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">
              {{ data.dailyMoney.esportBetAmount | amount }}
            </div>
            <div class="num">
              {{ data.dailyMoney.esportWinAmount | amount }}
            </div>
            <div class="num">
              {{ data.dailyMoney.esportRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.esportBetNum | amount }}</div>
          </div>
        </div>
      </div>
      <div class="item" v-if="onOffBtn.game.fishing === 2">
        <h1>{{ $t("fish") }}</h1>
        <div class="table">
          <div class="th">
            <div class="title">{{ $t("betAmount") }}</div>
            <div class="title">{{ $t("winAmount") }}</div>
            <div class="title">{{ $t("rebateAmount") }}</div>
            <div class="title">{{ $t("realBettingMoney") }}</div>
          </div>
          <div class="td" v-if="data.dailyMoney">
            <div class="num">
              {{ data.dailyMoney.fishingBetAmount | amount }}
            </div>
            <div class="num">
              {{ data.dailyMoney.fishingWinAmount | amount }}
            </div>
            <div class="num">
              {{ data.dailyMoney.fishingRebateAmount | amount }}
            </div>
            <div class="num">{{ data.dailyMoney.fishingBetNum | amount }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;•{{ $t("indexHint") }}</p> -->
    <p class="hint">&nbsp;&nbsp;&nbsp;&nbsp;{{ $t("indexRemark") }}</p>
    <!-- 详情弹窗 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogShow"
      width="80%"
      class="common_dialog order-page"
    >
      <div class="content" v-if="teamClickType !== 7">
        <div class="title">
          <p>{{ $t("userName") }}</p>
          <p>{{ $t("degreeName") }}</p>
          <p>{{ $t("userLevel") }}</p>
          <p>{{ $t("userType") }}</p>
          <p>{{ $t("onlineStatus") }}</p>
          <p>{{ $t("registerTime") }}</p>
          <p>{{ $t("afterMoney") }}</p>
          <p>{{ $t("unLoginDays") }}</p>
          <p>{{ $t("status") }}</p>
          <p>{{ $t("lastLoginDatetime") }}</p>
        </div>
        <div class="list">
          <div class="cont" v-for="(item, index) in tableData" :key="index">
            <div class="cont-show">
              <p>{{ item.username }}</p>
              <p>{{ item.degreeName }}</p>
              <p>{{ item.level }}</p>
              <p>{{ item.typeText }}</p>
              <p>{{ item.onlineText }}</p>
              <p>{{ item.createDatetime }}</p>
              <p>{{ item.money }}</p>
              <p>{{ item.unLoginDays }}</p>
              <p>{{ item.statusText }}</p>
              <p>{{ item.lastLoginDatetime }}</p>
            </div>
          </div>
          <div v-if="!tableData.length">
            <el-empty
              :image="require('../../assets/images/nodata.png')"
              :image-size="128"
              :description="$t('noData')"
            ></el-empty>
          </div>
        </div>
      </div>
      <div class="content" v-else>
        <div class="title">
          <p>{{ $t("userName") }}</p>
          <p>{{ $t("userType") }}</p>
          <p>{{ $t("liveBetAmount") }}</p>
          <p>{{ $t("liveWinAmount") }}</p>
          <p>{{ $t("sportBetAmount") }}</p>
          <p>{{ $t("sportWinAmount") }}</p>
          <p>{{ $t("chessBetAmount") }}</p>
          <p>{{ $t("chessWinAmount") }}</p>
          <p>{{ $t("egameBetAmount") }}</p>
          <p>{{ $t("egameWinAmount") }}</p>
          <p>{{ $t("esportBetAmount") }}</p>
          <p>{{ $t("esportWinAmount") }}</p>
          <p>{{ $t("fishingBetAmount") }}</p>
          <p>{{ $t("fishingWinAmount") }}</p>
        </div>
        <div class="list">
          <div
            class="cont"
            v-for="(item, index) in tableData"
            :key="index"
          >
            <div class="cont-show">
              <p>{{ item.username }}</p>
              <p>{{ item.typeText }}</p>
              <p>{{ item.liveBetAmount }}</p>
              <p>{{ item.liveWinAmount | amount }}</p>
              <p>{{ item.sportBetAmount | amount }}</p>
              <p>{{ item.sportWinAmount | amount }}</p>
              <p>{{ item.chessBetAmount }}</p>
              <p>{{ item.chessWinAmount }}</p>
              <p>{{ item.egameBetAmount | amount }}</p>
              <p>{{ item.egameWinAmount }}</p>
              <p>{{ item.esportBetAmount }}</p>
              <p>{{ item.egameWinAmount }}</p>
              <p>{{ item.fishingBetAmount }}</p>
              <p>{{ item.fishingWinAmount }}</p>          
            </div>
          </div>
          <div v-if="!tableData.length">
            <el-empty
              :image="require('../../assets/images/nodata.png')"
              :image-size="128"
              :description="$t('noData')"
            ></el-empty>
          </div>
        </div>
      </div>

      <PageModule
        :total="total"
        :pageNumber.sync="pageNumber"
        @searchMethod="userTeamListData"
      ></PageModule>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
// import DateModule from '@/components/common/DateModule'
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  name: "index",
  data() {
    return {
      data: "", //数据
      username: "", //用户名
      dialogShow: false, //是否显示弹窗
      dialogTitle: "", //弹窗标题
      tableData: [], //详情数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      teamClickType: "", //统计数据当前点击的类型
      key: -1, //要显示的完整内容的key值
    };
  },
  computed: {
    ...mapState(["onOffBtn", "userInfo"]),
  },
  components: {
    // DateModule,
    PageModule,
  },
  created() {
    this.searchMethod();
  },
  methods: {
    
    // 团队列表记录筛选
    searchMethod(startTime, endTime) {
      if (startTime) this.startTime = startTime;
      if (endTime) this.endTime = endTime;
      //改变提交格式
      let params = {};
      params.startDate = this.startTime;
      params.endDate = this.endTime;
      params.username = this.username;
      params.load = true;
      this.$API.agentTeam(params).then((res) => {
        if (res) {
          this.data = res;
        }
      });
    },
    // 团队列表点击详情
    userTeamListData(text, type) {
      if (text) this.dialogTitle = text;
      if (type) this.teamClickType = type;
      this.dialogShow = true;
      //改变提交格式
      let params = {};
      params.start = this.startTime;
      params.end = this.endTime;
      params.include = true;
      params.type = this.teamClickType;
      params.pageNumber = this.pageNumber;
      params.proxyName = "";
      // params.append('proxyName', this.userInfo.username);
      params.load = true;
      this.$API.userTeamListData(params).then((res) => {
        if (res) {
          for (let i = 0; i < res.rows.length; i++) {
            if (this.teamClickType === 7) {
              res.rows[i].typeText =
                res.rows[i].userType === 120
                  ? this.$t("proxy")
                  : this.$t("member");
            } else {
              res.rows[i].typeText =
                res.rows[i].type === 120 ? this.$t("proxy") : this.$t("member");
            }
            res.rows[i].statusText =
              res.rows[i].status === 2 ? this.$t("open") : this.$t("forbidden");
            res.rows[i].onlineText =
              res.rows[i].onlineStatus === 2
                ? this.$t("online")
                : this.$t("offline");
          }
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },

    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val);
    },
  },
  watch: {},
};
</script>

<style lang="scss">
@import "../../assets/css/order.scss";
.el-icon-s-data {
  font-size: 18px;
}
.viewTeam-page {
  padding: 0 10px;

  .el-range-input {
    color: $colorText;
  }

  .top {
    display: flex;
    flex-wrap: wrap;
    margin-top: 10px;
  }

  .top-bottom {
    display: flex;
    flex-wrap: wrap;

    .item {
      width: 180px;
      padding: 10px 15px;
      // background: #fff;
      border: 1px solid #dcdfe6;
      line-height: 30px;
      font-size: 14px;
      margin-right: 10px;
      margin-bottom: 10px;
      transition: all 0.2s;

      &.cursor {
        cursor: pointer;

        &:hover {
          background: #55657e;
        }
      }

      &:hover {
        box-shadow: 0 2px 4px rgba(150, 119, 119, 0.74),
          0 0 6px rgb(5, 100, 243);
      }

      .unit {
        color: $blue;
      }

      .num {
        color: $blue;
        font-size: 20px;
      }
    }
  }

  .gamelist {
    display: flex;
    flex-wrap: wrap;

    .item {
      margin-right: 10px;
      margin-top: 20px;
      color: $colorText;

      // &:nth-child(2n) {
      //   margin-right: 0;
      // }
      h1 {
        font-size: 20px;
        color: $colorText;
        line-height: 36px;
      }

      .title,
      .num {
        width: 130px;
        padding: 10px;
        box-sizing: border-box;
        text-align: center;
      }

      .th {
        background: $borderColorJoker;
        display: flex;
        font-size: 16px;

        .title {
          border-right: 1px solid #d6d6d6;
        }
      }

      .td {
        display: flex;

        .num {
          border-right: 1px solid $colorText;
          border-bottom: 1px solid$colorText;

          &:first-child {
            border-left: 1px solid $colorText;
          }
        }
      }
    }
  }

  .hint {
    margin-top: 15px;
    color: $blue;
  }

  .detailsContent {
    width: 500px;
    height: 500px;
  }

  .order-page {
    .content {
      .list {
        .cont {
          .cont-hide {
            div {
              width: auto;
            }
          }
        }
      }
    }
  }
}
</style>


