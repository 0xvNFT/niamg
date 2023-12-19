<template>
  <el-tabs type="border-card" class="myRecommend-page order-page" v-model="tabIndex" @tab-click="handleClick">
    <!-- 邀请总览 -->
    <el-tab-pane :label="$t('promotionInfo')" name="1">
      <!-- 详细数据 -->
      <div class="content">
        <el-row>
          <el-col :span="24">
            <div class="grid-content bg-purple">
              <p class="refresh">
                <i class="el-icon-refresh"></i>
                <span style="margin-right: 5px;">{{ $t('hourRefresh') }}</span>
              </p>
            </div>
          </el-col>
        </el-row>
        <ul>
          <li>
            <p class="num">
              {{ dataList.totalInvitePerson }}
            </p>
            <p class="type">
              {{ $t("totalInvite") }}
            </p>
          </li>

          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.monthCommission | amount }}
            </p>
            <p class="type">
              {{ $t("monthCommission") }}
            </p>
          </li>
          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.todayInviteBonus | amount }}
            </p>
            <p class="type">
              {{ $t("totalIncome") }}
            </p>
          </li>
          <li>
            <p class="num">
              {{ dataList.todayInvitePerson }}
            </p>
            <p class="type">
              {{ $t("todayInvite") }}
            </p>
          </li>

          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.monthInviteBonus | amount }}
            </p>
            <p class="type">
              {{ $t("monthInviteBonus") }}
            </p>
          </li>
          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.todayInviteBonus | amount }}
            </p>
            <p class="type">
              {{ $t("todayIncome") }}
            </p>
          </li>
        </ul>
      </div>
      <!-- 推广方式 -->
      <div class="promotion">
        <p class="text">{{ $t("invitationLink") }}：</p>
        <p class="link">
           <span id="linkUrl">{{ dataList.prompInfo.linkUrl }}</span>
          <el-button class="tag-read" type="primary" size="medium"  @click.native="copyName('linkUrl')">{{ $t("copyText") }}</el-button>
        </p>
        <p class="text">{{ $t("invitationCode") }}：</p>
        <p class="link">
          <span id="code">{{ dataList.prompInfo.code }} </span>
          <el-button class="tag-read" type="primary" size="medium"  @click.native="copyName('code')">{{ $t("copyText") }}</el-button>
        </p>
      </div>
      <!-- 邀请语 -->
      <div class="join">
        <p style="font-size: 18px;">{{ $t("join") }}</p>
        <p>{{ $t("joinText") }}</p>
      </div>
    </el-tab-pane>
    <!-- 存款信息 -->
    <el-tab-pane :label="$t('depositInfo')" name="2">
      <div class="searchDiv">
        <div class="item">
          <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
          <el-button type="primary" size="medium" icon="el-icon-search" style="margin-left: 5px" @click="searchMethod">{{$t("searchText")}}</el-button>
        </div>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <div slot="empty">
          <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
        <el-table-column :label="$t('dataText')">
          <template slot-scope="scope">
            {{ dataFun(scope.row.createDatetime) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" :label="$t('userName')">
        </el-table-column>
        <el-table-column :label="$t('depositAmount')">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{moneyUnit}}&nbsp;{{ scope.row.depositMoney }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('realBettingMoney')">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{moneyUnit}}&nbsp;{{ scope.row.thirdBetAmount }}</el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-tab-pane>
    <!-- 奖金信息 -->
    <el-tab-pane :label="$t('bonusInfo')" name="3">
      <div class="searchDiv">
        <div class="item">
          <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime" value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
          <el-button type="primary" size="medium" icon="el-icon-search" style="margin-left: 5px" @click="searchMethod">{{$t("searchText")}}</el-button>
        </div>
      </div>
      <el-table :data="tableData" style="width: 100%">
        <div slot="empty">
          <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
        <el-table-column :label="$t('dataText')">
          <template slot-scope="scope">
            {{ dataFun(scope.row.createDatetime) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" :label="$t('userName')">
        </el-table-column>
        <el-table-column prop="bonusTypeText" :label="$t('bonusType')">
        </el-table-column>
        <el-table-column :label="$t('bonus')">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{moneyUnit}}&nbsp;{{ scope.row.money }}</el-tag>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <!-- <PageModule
        :total="total"
        :pageNumber.sync="pageNumber"
        @searchMethod="searchMethod"
      ></PageModule> -->
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapState } from "vuex";
import DATE from "@/assets/js/date.js";

// import PageModule from "@/components/common/PageModule";

export default {
  data () {
    return {
      tabIndex: "1",
      // 邀请总览
      dataList: {
        prompInfo: {},
      },
      moneyUnit: "", //货币单元
      // 推广管理 or 奖金信息
      date: [],//日期
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      tableData: [],
      // total: 0,
      // pageNumber: 1, //当前页码
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted () {
    this.getInviteOverview();
  },
  methods: {
    // 复制
    copyName(val) {
      this.$publicJs.copyFun(val);
      this.$message.success(this.$t("successCopy"));
    },
    //tabs 标签页
    handleClick () {
      this.searchMethod();
    },
    // 时间函数
    dataFun (val) {
      return DATE.dateChange(val);
    },

    //获取邀请人获得的返佣概况
    getInviteOverview () {
      this.$API.getInviteOverview().then(res => {
        if (res) {
          this.dataList = res.content;
          this.moneyUnit = res.moneyUnit;
        }
      });
    },
    // 搜索
    searchMethod () {
      //清空原有赋值
      this.tableData = [];
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
      }
      params.load = true

      if (this.tabIndex === "2") {
        //获取被邀请人的存款信息列表
        this.$API.getInviteDeposits(params).then(res => {
          if (res) {
            this.tableData = res.content;
          }
        });
      } else if (this.tabIndex === "3") {
        //获取被邀请人的奖金信息列表
        this.$API.getInviteBonus({ 'startDate': this.date[0] || this.startTime, ' endDate': this.date[1] || this.endTime, 'load': true }).then(res => {
          if (res) {
            this.tableData = res.content;
          }
        });
      }
    },
  },
  watch: {
    date (e) {
      if (e == null) {
        this.date = []
        this.date[0] = this.startTime
        this.date[1] = this.endTime
      }
    }
  },
};
</script>

<style lang="scss" >
@import '../../assets/css/order.scss';

.myRecommend-page {
  color: $borderColorJoker;
  background: $black;
  padding: 0;
  .content {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    .bg-purple {
      .refresh {
        line-height: 40px;
        font-size: 16px;
        padding-left: 10px;
        background: $borderColorJoker;
        color: #fff;
      }
    }
    ul {
      display: flex;
      flex-direction: row;
      flex-wrap: wrap;
      li {
        width: 33.3%;
        text-align: center;
        height: 120px;
        // background: #1f51c299;
        display: flex;
        flex-direction: column;
        justify-content: center;
        .num {
          font-size: 20px;
          font-weight: 700;
          color: #088cffc7;
        }
        .type {
          font-size: 20px;
          margin-top: 8px;
        }
      }
    }
  }
  .promotion {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 15px;
    padding: 15px;
    font-size: 16px;
    font-weight: 600;
    p {
      margin: 10px 0;
    }
    .text {
      color: #088cffc7;
    }
    .tag-read {
      margin-left: 10px;
    }
  }
  .join {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-top: 15px;
    padding: 15px;
    font-size: 16px;
    font-weight: 600;
    p {
      margin: 20px 0;
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
  .el-textarea__inner {
    background-color: $bgColor;
  }
  .el-dialog__title {
    color: #fff;
  }

  .el-tabs__content {
    background-color: $black;
  }
  .el-input {
    width: 200px;
    .el-input__inner {
      background-color: $black;
    }
  }
  .el-checkbox__inner {
    display: inline-block;
    position: relative;
    border: 1px solid #dcdfe6;
    border-radius: 2px;
    box-sizing: border-box;
    width: 14px;
    height: 14px;
    background-color: #fff;
    z-index: 1;
    transition: border-color 0.25s cubic-bezier(0.71, -0.46, 0.29, 1.46),
      background-color 0.25s cubic-bezier(0.71, -0.46, 0.29, 1.46);
  }
}
</style>
