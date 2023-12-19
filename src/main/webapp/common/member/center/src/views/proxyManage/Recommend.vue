<template>
  <el-card class="recommend-page">
    <div class="top">
      <div class="searchDiv">
        <div class="item">
          {{ $t("agentJunior") }}：
          <el-input v-model="username" class="outInput" clearable size="small"></el-input>
        </div>
        <div class="item">
          <el-date-picker v-model="dateVal" type="date" size="small" :placeholder="$t('changeData')" value-format="yyyy-MM-dd">
          </el-date-picker>
          <el-button-group style="margin-left: 10px">
            <el-button type="success" size="small" @click="yesterday">{{ $t("yesterday") }}</el-button>
            <el-button type="success" size="small" @click="today">{{ $t("today") }}</el-button>
            <el-button type="success" size="small" @click="beforeYesterday">{{ $t("beforeYesterday") }}</el-button>
          </el-button-group>
          <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 30px" @click="searchMethod(true)">{{ $t("searchText") }}</el-button>
        </div>
      </div>
      <div class="top-bottom">
        <div class="item">
          <p>{{ $t("memberCount") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num">{{data.memberCount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamMoney") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{data.teamMoney || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamDepositAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{daily.depositAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("teamTithdrawAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{daily.withdrawAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("depositArtificial") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{daily.depositArtificial || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("proxyRebateAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
          <p class="num">{{daily.proxyRebateAmount || 0}}</p>
        </div>
        <div class="item">
          <p>{{ $t("betNum") }}<span class="unit">（{{ $t("people") }}）</span></p>
          <p class="num">{{data.betNum || 0}}</p>
        </div>
      </div>
    </div>

    <el-table :data="tableData" style="width: 99%">
      <el-table-column prop="username" :label="$t('memberName')"></el-table-column>
      <el-table-column prop="statDate" :label="$t('dataText')"></el-table-column>
      <el-table-column prop="depositAmount" :label="$t('deposit')"></el-table-column>
      <el-table-column prop="depositArtificial" :label="$t('depositArtificial')"></el-table-column>
      <el-table-column prop="liveBetAmount" :label="$t('totalBetAmount')"></el-table-column>
      <el-table-column prop="liveBetNum" :label="$t('liveBetNum')"></el-table-column>
    </el-table>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </el-card>
</template>

<script>
import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      data: '',//
      daily: '',//
      dateVal: '',//时间
      username: '',//代理及下级
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    PageModule
  },
  mounted () {
    this.searchMethod()
  },
  methods: {
    today () {
      this.dateVal = this.$dataJS.today()
      this.searchMethod()
    },
    yesterday () {
      this.dateVal = this.$dataJS.yesterdayStart()
      this.searchMethod()
    },
    beforeYesterday () {
      this.dateVal = this.$dataJS.beforeDayStart(2)
      this.searchMethod()
    },
    // 记录
    searchMethod (isSearch) {
      if (!this.dateVal) this.dateVal = this.$dataJS.today()
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = new URLSearchParams();
      params.append('date', this.dateVal);
      params.append('pageNumber', this.pageNumber);
      params.append('username', this.username);
      this.$axiosPack.post("/userCenter/agentManage/recommendList.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
          this.daily = res.data.daily
          this.tableData = res.data.page.rows
          this.total = res.data.total
        }
      });
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
.recommend-page {
  .el-button--primary {
    background-color: #f12c4c;
    border: #f12c4c;
  }
  .searchDiv {
    margin-bottom: 10px;
  }
  .top-bottom {
    display: flex;
    flex-wrap: wrap;
    .item {
      width: 144px;
      padding: 10px 24px;
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
}
</style>


