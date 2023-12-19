<template>
  <el-tabs
    type="border-card"
    class="promotionUrl-page"
    v-model="tabIndex"
    @tab-click="handleClick"
  >
    <!-- 邀请总览 -->
    <el-tab-pane :label="$t('promotionInfo')" name="1">
      <!-- 详细数据 -->
      <div class="content">
        <el-row>
          <el-col :span="24"
            ><div class="grid-content bg-purple">
              <p class="refresh">
                <i class="el-icon-refresh"></i>
                <span style="margin-right: 5px;">{{ $t('hourRefresh') }}</span>
              </p>
            </div></el-col
          >
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
              {{ moneyUnit }}&nbsp;{{ dataList.monthCommission }}
            </p>
            <p class="type">
              {{ $t("monthCommission") }}
            </p>
          </li>
          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.todayInviteBonus }}
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
              {{ moneyUnit }}&nbsp;{{ dataList.monthInviteBonus }}
            </p>
            <p class="type">
              {{ $t("monthInviteBonus") }}
            </p>
          </li>
          <li>
            <p class="num">
              {{ moneyUnit }}&nbsp;{{ dataList.todayInviteBonus }}
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
          {{ dataList.prompInfo.linkUrl }}
          <el-button
            class="tag-read"
            type="primary"
            size="medium"
            :data-clipboard-text="dataList.prompInfo.linkUrl"
            @click.native="copy()"
            >{{ $t("copyText") }}</el-button
          >
        </p>
        <p class="text">{{ $t("invitationCode") }}：</p>
        <p class="link">
          {{ dataList.prompInfo.code }}
          <el-button
            class="tag-read"
            type="primary"
            size="medium"
            :data-clipboard-text="dataList.prompInfo.code"
            @click.native="copy()"
            >{{ $t("copyText") }}</el-button
          >
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
      <DateModule
        :fastBtn="true"
        @searchMethod="searchMethod"
        :showClear="true"
      ></DateModule>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column :label="$t('dataText')">
          <template slot-scope="scope">
            {{ dataFun(scope.row.createDatetime) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" :label="$t('userName')">
        </el-table-column>
        <el-table-column  :label="$t('depositAmount')">
          <template slot-scope="scope">
            <div slot="reference" class="name-wrapper">
              <el-tag size="medium">{{moneyUnit}}&nbsp;{{ scope.row.depositMoney }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :label="configJsFlag('realBettingMoney')"
        >
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
      <DateModule
        :fastBtn="true"
        @searchMethod="searchMethod"
        :showClear="true"
      ></DateModule>
      <el-table :data="tableData" style="width: 100%">
        <el-table-column :label="$t('dataText')">
          <template slot-scope="scope">
            {{ dataFun(scope.row.createDatetime) }}
          </template>
        </el-table-column>
        <el-table-column prop="username" :label="$t('userName')">
        </el-table-column>
        <el-table-column prop="bonusTypeText" :label="$t('bonusType')">
        </el-table-column>
        <el-table-column  :label="$t('bonus')">
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
import Clipboard from "clipboard";
import DateModule from "@/components/common/DateModule";
// import PageModule from "@/components/common/PageModule";

export default {
  data() {
    return {
      tabIndex: "1",
      // 邀请总览
      dataList: {
        prompInfo: {},
      },
      moneyUnit: "", //货币单元
      // 推广管理 or 奖金信息
      startTime: this.$dataJS.today() + " 00:00:00", //搜索开始时间
      endTime: this.$dataJS.today() + " 23:59:59", //搜索结束时间
      tableData: [],
      // total: 0,
      // pageNumber: 1, //当前页码
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: { DateModule },
  mounted() {
    this.getInviteOverview();
  },
  methods: {
    //复制
    copy() {
      let clipboard = new Clipboard(".tag-read");
      clipboard.on("success", (e) => {
        this.$notify({
          title: this.$t("successText"),
          message: this.$t("successCopy"),
          type: "success",
        });
        // 释放内存
        clipboard.destroy();
      });
      clipboard.on("error", (e) => {
        this.$notify.error({
          title: this.$t("errorText"),
          message: this.$t("failCopy"),
        });
        // 不支持复制
        console.log("该浏览器不支持自动复制");
        // 释放内存
        clipboard.destroy();
      });
    },
    //tabs 标签页
    handleClick() {
      this.searchMethod();
    },
    // 时间函数
    dataFun(val) {
      return this.$dataJS.dateChange(val);
    },
    //获取邀请人获得的返佣概况
    getInviteOverview() {
      this.$axiosPack.get("/userCenter/inviteOverview.do ").then((res) => {
        if (res) {
          this.dataList = res.data.content;
          this.moneyUnit = res.data.moneyUnit;
        }
      });
    },
    // 搜索
    searchMethod(startTime, endTime) {
      //清空原有赋值
      this.tableData = [];
      if (startTime) {
        this.startTime = startTime;
      } else {
        this.startTime = this.$dataJS.today() + " 00:00:00";
      }
      if (endTime) {
        this.endTime = endTime;
      } else {
        this.endTime = this.$dataJS.today() + " 23:59:59";
      }

      if (this.tabIndex === "2") {
        //获取被邀请人的存款信息列表
        this.$axiosPack
          .get(
            "/userCenter/inviteDeposits.do?" +
              "startDate=" +
              this.startTime +
              "&endDate=" +
              this.endTime
          )
          .then((res) => {
            if (res) {
              this.tableData = res.data.content;
            }
          });
      } else if (this.tabIndex === "3") {
        //获取被邀请人的奖金信息列表
        this.$axiosPack
          .get(
            "/userCenter/inviteBonus.do?" +
              "startDate=" +
              this.startTime +
              "&endDate=" +
              this.endTime
          )
          .then((res) => {
            if (res) {
              this.tableData = res.data.content;
            }
          });
      }
    },
  },
  watch: {},
};
</script>

<style lang="scss" scoped>
.promotionUrl-page {
  color: #303133;
  .content {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    .bg-purple {
      .refresh {
        line-height: 40px;
        font-size: 16px;
        padding-left: 10px;
        background: #d3dce6;
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
}
</style>
