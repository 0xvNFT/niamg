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
      <el-table-column :label="$t('platformAndOrder')">
        <template slot-scope="scope">
          <p class="text-primary">{{ scope.row.orderId }}</p>
          <p>{{ typeFun(scope.row.platformType) }}</p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('bettingTime')">
        <template slot-scope="scope">
          <p>{{ dataFun(scope.row.bettingTime) }}</p>
          <!-- <p>{{ dataFun(scope.row.buyTime) }}</p> -->
        </template>
      </el-table-column>
      <el-table-column :label="$t('userName')">
        <template slot-scope="scope">
          <p>{{ scope.row.username }}</p>
          <p>{{ scope.row.thirdUsername }}</p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('odds')">
        <template slot-scope="scope">
          <p>{{ scope.row.odds || '-' }}</p>
          <p>{{ scope.row.oddsType || '-' }}</p>
        </template>
      </el-table-column>

      <el-table-column prop="bettingMoney" :label="$t('bettingMoney')"></el-table-column>
      <el-table-column :label="$t('realBettingMoney')">
        <template slot-scope="scope">
          <p>{{ scope.row.realBettingMoney || '-' }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="winMoney" :label="$t('winMoney')"></el-table-column>
      <el-table-column :label="$t('createDatetime')">
        <template slot-scope="scope">
          <p>{{ dataFun(scope.row.createDatetime) }}</p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('status')">
        <template slot-scope="scope">
          <p v-html="statusFun(scope.row.platformType, scope.row.status)"></p>
        </template>
      </el-table-column>
      <el-table-column :label="$t('bettingContent')">
        <template slot-scope="scope">
          <p class="lookInfo" @click="lookInfo(scope.row.bettingContent)" v-if="scope.row.bettingContent">{{
            $t("lookInfo") }}</p>
          <span v-else>-</span>
        </template>
      </el-table-column>
    </el-table>
    <div class="statistics">
      <div class="line" v-if="data.aggsData">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">{{ $t("bettingMoney") }}：{{ data.aggsData.bettingMoneyCount || 0 }}</div>
        <div style="margin-left: 20px">{{ $t("realBettingMoney") }}：{{ data.aggsData.realBettingMoneyCount || 0 }}</div>
        <div style="margin-left: 20px">{{ $t("winMoney") }}：{{ data.aggsData.winMoneyCount || 0 }}</div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>

    <!-- 详情弹窗 -->
    <el-dialog :title="$t('info')" :visible.sync="dialogShow" width="40%" :close-on-click-modal="false"
      class="info-dialog">
      <div class="cont" v-if="content" v-html="content"></div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data() {
    return {
      platform: '',//类型
      platformArr: [{ name: this.$t("allPlatform"), value: '' }],
      username: '',//用户名
      orderId: '',//订单号
      // checked: true,//包含下级复选框
      data: '',//数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      content: '',//当前订单详情
      dialogShow: false,//是否显示弹窗
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    DateModule,
    PageModule
  },
  mounted() {
    let _this = this
    var timer = setInterval(() => {
      if (_this.onOffBtn) {
        clearInterval(timer);
        _this.platformArr = _this.platformArr.concat(_this.onOffBtn.sport)
        // 获取数据
        _this.searchMethod()
      }
    }, 300);
    this.$once('hook:beforeDestroy', function () {
      clearInterval(timer);
    });
  },
  methods: {
    // 记录
    searchMethod(startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('platform', this.platform);
      params.append('orderId', this.orderId);
      params.append('username', this.username);
      this.$axiosPack.post("/userCenter/third/sportRecord.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },

    // 平台类型
    typeFun(val) {
      let arr = this.onOffBtn.sport
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].value === val) {
          return arr[i].name
        }
      }
    },

    // 时间函数
    dataFun(val) {
      return this.$dataJS.dateChange(val)
    },

    // 状态
    statusFun(type, value) {
      switch (type) {
        case "TYSB":
          switch (value) {
            case "half won":
              return this.$t("halfWon");
            case "half lose":
              return this.$t("halfLose");
            case "won":
              return this.$t("won");
            case "lose":
              return this.$t("lose");
            case "draw":
              return this.$t("draw");
            case "reject":
              return this.$t("canceled");
            case "waiting":
              return this.$t("waiting");
            case "running":
              return this.$t("running");
            case "void":
              return this.$t("void");
            case "refund":
              return this.$t("refund");
          }
          break;
        case "TYXJ":
          switch (value) {
            case "1":
              return this.$t("sureText");
            case "2":
              return this.$t("settle");
            case "3":
              return this.$t("canceled");
            case "4":
              return this.$t("invalid");
          }
          break;
        case "SBTA":
          switch (value) {
            case "1":
              return this.$t("closeSettle");
            case "0":
              return this.$t("noSettle");
            case "-8":
              return this.$t("cancelCodeOrder");
            case "-9":
              return this.$t("cancelOrder");
            case "2":
              return this.$t("betting");
            case "4":
              return this.$t("cash");
          }
          break;
        case "SBO":
          switch (value) {
            case "half won":
              return this.$t("halfWon");
            case "half lose":
              return this.$t("halfLose");
            case "won":
              return this.$t("won");
            case "lose":
              return this.$t("lose");
            case "draw":
              return this.$t("draw");
            case "reject":
              return this.$t("canceled");
            case "waiting":
              return this.$t("waiting");
            case "running":
              return this.$t("running");
            case "void":
              return this.$t("void");
            case "refund":
              return this.$t("refund");
            case "waiting rejected":
              return this.$t("dangerous");
          }
          break;
        case "TYCR":
          switch (value) {
            case "0":
              return this.$t("noSettle");
            case "L":
              return this.$t("lose");
            case "W":
              return this.$t("won");
            case "P":
              return this.$t("draw");
            case "D":
              return this.$t("cancel");
            case "A":
              return this.$t("restore");
          }
          break;
        case "DJIM":
          switch (value) {
            case "0":
              return this.$t("noSettle");
            case "L":
              return this.$t("lose");
            case "W":
              return this.$t("won");
            case "D":
              return this.$t("draw");
          }
          break;
      }
    },

    lookInfo(cont) {
      this.content = cont
      this.dialogShow = true
    }
  },
  watch: {
  }
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
}</style>


