<template>
  <div class="recommend-page order-page">
    <!-- 推荐总览 -->
    <div class="heard">
      {{ $t("recommend") }}
    </div>
    <el-card class="content">
      <div class="top">
        <div class="searchDiv">
          <div class="item">
            {{ $t("agentJunior") }}：
            <el-input v-model="username" class="outInput" clearable size="small"></el-input>
          </div>
          <div class="item">
            <el-date-picker v-model="dateVal" type="date" size="small" :placeholder="$t('changeData')" value-format="yyyy-MM-dd">
            </el-date-picker>
            <el-button type="primary" size="medium" icon="el-icon-search" style="margin-left: 5px" @click="searchMethod(true)">{{$t("searchText")}}</el-button>
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
            <p>{{ $t("teamWithdrawAmount") }}<span class="unit">（{{ onOffBtn.currency }}）</span></p>
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
  </div>
</template>

<script>
import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'
import DATE from "@/assets/js/date.js";

export default {
  data () {
    return {
      data: '',//
      daily: {},//
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
    // 记录
    searchMethod (isSearch) {
      if (!this.dateVal) this.dateVal = DATE.today()
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = {};
      params.date = this.dateVal
      params.pageNumber = this.pageNumber
      params.username = this.username
      params.load = true

      this.$API.recommendList(params).then(res => {
        if (res) {
          this.data = res
          this.daily = res.daily
          this.tableData = res.page.rows
          this.total = res.total
        }
      });
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';

.recommend-page {
  // background: $black;
  // color: $colorText;
  padding: 0 10px;
  .el-button--primary {
    background-color: $blue;
    border: $blue;
  }
  .content {
    margin-top: 10px;
    background: $black;
    color: $colorText;
    padding: 0;
    .top-bottom {
      display: flex;
      flex-wrap: wrap;
      .item {
        // width: 144px;
        padding: 10px 24px;
        // background: #fff;
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
          color: $blue;
        }
        .num {
          color: $blue;
          font-size: 20px;
          &.cursor {
            cursor: pointer;
          }
        }
      }
    }
  }
  .el-button--success {
    background-color: $blue;
    border-color: $blue;
  }
}
</style>


