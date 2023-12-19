<template>
  <div class="order-page">
    <div class="searchDiv">
      <!-- <div class="item">
        {{ $t("userName") }}：
        <el-input
          v-model="username"
          class="outInput"
          :disabled="true"
          size="small"
        ></el-input>
      </div> -->
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
      <el-table-column :label="$t('platformAndOrder')">
        <template slot-scope="scope">
          <p class="text-primary">{{ scope.row.orderId }}</p>
          <p>{{ typeFun(scope.row.platformType) }}</p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('bettingTime')" width="170px">
        <template slot-scope="scope">
          <p>{{ scope.row.createDatetimeStr }}</p>
          <!-- <p>{{ dataFun(scope.row.buyTime) }}</p> -->
        </template>
      </el-table-column>
      <el-table-column :label="$t('userName')" width="170px">
        <template slot-scope="scope">
          <p>{{ scope.row.username }}</p>
          <p>{{ scope.row.thirdUserName }}</p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('lotNameAndOrderId')">
        <template slot-scope="scope">
          <p>{{ scope.row.gameName || "-" }}</p>
          <p>{{ scope.row.qiHao || "-" }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="playName" :label="$t('playName')"></el-table-column>

      <el-table-column prop="bettingMoney" :label="$t('bettingMoney')"></el-table-column>
      <el-table-column :label="$t('realBettingMoney')">
        <template slot-scope="scope">
          <p>{{ scope.row.realBettingMoney || "-" }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="winMoney" :label="$t('winMoney')"></el-table-column>
      <el-table-column :label="$t('status')" width="100px">
        <template slot-scope="scope">
          <p :class="'status--' + scope.row.status">
            {{ $t("resultStatus" + scope.row.status) }}
          </p>
        </template>
      </el-table-column>
      <el-table-column prop="content" :label="$t('bettingContent')"></el-table-column>
      <!-- </el-table-column> -->
    </el-table>
    <div class="statistics">
      <div class="line" v-if="data">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">
          {{ $t("bettingMoney") }}：{{ data.bettingMoneyCount || 0 }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("realBettingMoney") }}：{{
            data.realBettingMoneyCount || 0
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("winMoney") }}：{{ data.winMoneyCount || 0 }}
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
      data: "", //数据
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      content: "", //当前订单详情
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
        _this.platformArr = _this.platformArr.concat(_this.onOffBtn.lottery);
        this.username = this.onOffBtn.username;
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
        .post("/userCenter/third/lotRecord.do", {
          params: params,
          load: [true, 200, null],
        })
        .then((res) => {
          if (res) {
            this.data = JSON.parse(res.data.aggsData);
            this.tableData = res.data.rows;
            this.total = res.data.total;
          }
        });
    },

    // 平台类型
    typeFun(val) {
      let arr = this.onOffBtn.lottery;
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

.status--3 {
  color: rgb(255, 0, 0);
}

.status--2 {
  color: rgb(14, 197, 4);
}

.status--1 {
  color: rgb(255, 177, 31);
}

.status--4 {
  color: rgb(128, 128, 128);
}

.status--5 {
  color: rgb(186, 186, 2);
}

.status--6 {
  color: rgb(30, 144, 255);
}

.status--7 {
  color: rgb(186, 85, 211);
}

.status--8 {
  color: rgb(210, 105, 30);
}
</style>
