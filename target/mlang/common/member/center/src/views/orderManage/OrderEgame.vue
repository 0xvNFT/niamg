<template>
  <div class="order-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("orderId") }}：
        <el-input v-model="orderId" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("platform") }}：
        <el-select v-model="platform" size="small">
          <el-option v-for="item in platformArr" :key="item.value" :label="item.name" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <!-- <div class="item" v-if="onOffBtn.canBePromo || onOffBtn.canBeRecommend">
        <el-checkbox v-model="checked">包含下级</el-checkbox>
      </div> -->
      <DateModule :fastBtn="true" @searchMethod="searchMethod"></DateModule>
    </div>
    <el-table :data="tableData" style="width: 99%">
      <el-table-column :label="$t('platformType')">
        <template slot-scope="scope">
          <p>{{ typeFun(scope.row.platformType) }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="gameName" :label="$t('platformText')"></el-table-column>
      <el-table-column :label="$t('betOrderId')">
        <template slot-scope="scope">
          <p class="text-primary">{{ scope.row.orderId }}</p>
          <p>{{ dataFun(scope.row.bettingTime) }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="username" :label="$t('userName')"></el-table-column>
      <el-table-column :label="$t('tableCode')">
        <template slot-scope="scope">
          <p>{{ scope.row.gameCode || "-" }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="bettingMoney" :label="$t('bettingMoney')"></el-table-column>
      <el-table-column :label="$t('realBettingMoney')">
        <template slot-scope="scope">
          <p>{{ scope.row.realBettingMoney || 0 }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="winMoney" :label="$t('winMoney')"></el-table-column>
      <el-table-column :label="$t('createDatetime')">
        <template slot-scope="scope">
          <p>{{ dataFun(scope.row.createDatetime) }}</p>
        </template>
      </el-table-column>
    </el-table>
    <div class="statistics">
      <div class="line" v-if="data.aggsData">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">
          {{ $t("bettingMoney") }}：{{
            data.aggsData.bettingMoneyCount || 0
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("realBettingMoney") }}：{{
            data.aggsData.realBettingMoneyCount || 0
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("winMoney") }}：{{ data.aggsData.winMoneyCount || 0 }}
        </div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </div>
</template>

<script>
import { mapState } from "vuex";
import DateModule from "@/components/common/DateModule";
import PageModule from "@/components/common/PageModule";

export default {
  data() {
    return {
      platform: "", //类型
      platformArr: [{ name: this.$t("allPlatform"), value: "" }],
      username: "", //用户名
      orderId: "", //订单号
      // checked: true,//包含下级复选框
      data: "", //数据
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      startTime: this.$dataJS.today() + " 00:00:00", //搜索开始时间
      endTime: this.$dataJS.today() + " 23:59:59", //搜索结束时间
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {
    DateModule,
    PageModule,
  },
  mounted() {
    let _this = this;
    var timer = setInterval(() => {
      if (_this.onOffBtn) {
        clearInterval(timer);
        _this.platformArr = _this.platformArr.concat(_this.onOffBtn.egame);
        // 获取数据
        _this.searchMethod();
      }
    }, 300);
    this.$once("hook:beforeDestroy", function () {
      clearInterval(timer);
    });
  },
  methods: {
    // 记录
    searchMethod(startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime;
      if (endTime) this.endTime = endTime;
      if (isSearch) this.pageNumber = 1;

      //改变提交格式
      let params = new URLSearchParams();
      params.append("startTime", this.startTime);
      params.append("endTime", this.endTime);
      params.append("pageNumber", this.pageNumber);
      params.append("platform", this.platform);
      params.append("orderId", this.orderId);
      params.append("username", this.username);
      this.$axiosPack
        .post("/userCenter/third/egameRecord.do", {
          params: params,
          load: [true, 200, null],
        })
        .then((res) => {
          if (res) {
            this.data = res.data;
            this.tableData = this.data.rows;
            this.total = this.data.total;
            // 0=老虎机 7=捕鱼机 9=街机  12=电子彩票  18=棋牌
            this.tableData.forEach((item) => {
              switch (item.gameType) {
                case "0":
                  item.gameName = this.$t("ganmeType0");
                  break;
                case "7":
                  item.gameName = this.$t("ganmeType7");
                  break;
                case "9":
                  item.gameName = this.$t("ganmeType9");
                  break;
                case "12":
                  item.gameName = this.$t("ganmeType12");
                  break;
                case "18":
                  item.gameName = this.$t("chess");
                  break;
                default:
                  break;
              }
            });
          }
        });
    },

    // 平台类型
    typeFun(val) {
      let arr = this.onOffBtn.egame;
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].value === val) {
          return arr[i].name;
        }
      }
    },

    // 时间函数
    dataFun(val) {
      return this.$dataJS.dateChange(val);
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.el-button--success {
  background: #0ec504;
  border-color: #0ec504;
}

.el-button--primary {
  background: #f12c4c;
  border-color: #f12c4c;
}

.el-button--success:hover {
  background: #0ec504;
  border-color: #0ec504;
}

.el-button--primary:hover {
  background: #f12c4c;
  border-color: #f12c4c;
}

.el-input--small {
  .el-input__inner {
    color: #1a242d;
  }
}

.dateModule {
  .el-date-editor--datetimerange.el-input__inner {
    background: #fff;

    .el-range-input {
      color: #1a242d;
    }
  }
}
</style>
