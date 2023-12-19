<template>
  <div class="recordTeam-page order-page">
    <div class="heard">
      <!-- 团队报表 -->
      <p>{{ $t("recordTeam") }}</p>
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{
          $t("searchText") }}</el-button>
      </div>
    </div>
    <div class="content">
      <div class="title">
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("primaryGroup") }}</p>
        <p>{{ $t("bettingMoney") }}</p>
        <p>{{ $t("rebateText") }}</p>
        <p>{{ $t("proxyRebateAmount") }}</p>
        <p>{{ $t("winAmount") }}</p>
        <p>{{ $t("activeAwardAmount") }}</p>
        <p>{{ $t("depositAmount") }}</p>
        <p>{{ $t("withdrawAmount") }}</p>
        <p>{{ $t("realBetNum") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key='index'>
          <div class="cont-show">
            <p>{{ item.username }}</p>
            <p>{{ item.userType === 130 ? $t("member") : $t("proxy") }}</p>
            <p>{{ item.liveBetAmount | amount }}</p>
            <p>{{ item.liveRebateAmount | amount }}</p>
            <p>{{ item.proxyRebateAmount | amount }}</p>
            <p>{{ item.liveWinAmount | amount }}</p>
            <p>{{ item.activeAwardAmount | amount }}</p>
            <p>{{ item.depositAmount | amount }}</p>
            <p>{{ item.withdrawAmount | amount }}</p>
            <p>{{ item.liveBetNum | amount }}</p>
          </div>
        </div>
      </div>

    </div>
    <div v-if="!tableData.length">
      <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>
    <div class="statistics">
      <div class="line" v-if="totalData">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">{{ $t("bettingMoney") }}：{{ totalData.liveBetAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("rebateText") }}：{{ totalData.liveRebateAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("proxyRebateAmount") }}：{{ totalData.proxyRebateAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("winAmount") }}：{{ totalData.liveWinAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("activeAwardAmount") }}：{{ totalData.activeAwardAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("depositAmount") }}：{{ totalData.depositAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("withdrawAmount") }}：{{ totalData.withdrawAmount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("realBetNum") }}：{{ totalData.liveBetNum || 0 | amount }}</div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    <i style="margin-top: 15px">{{ $t("recordHint") }}</i>
  </div>
</template>

<script>
// import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      username: '',//用户名
      totalData: '',//总计的数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      date: [],//日期
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
  },
  components: {
    PageModule
  },
  mounted() {
    this.searchMethod()
  },
  methods: {
    // 记录
    searchMethod(isSearch) {
      if (isSearch) this.pageNumber = 1
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
        'pageNumber': this.pageNumber,
        'include': this.checked,
        'username': this.username,
      }
      params.load = true

      this.$API.teamReport(params).then(res => {
        if (res) {
          this.totalData = res.total
          this.tableData = res.page.rows
          this.total = res.page.total
        }
      });
    },
  },
  watch: {
    date(e) {
      if (e == null) {
        this.date = []
        this.date[0] = this.startTime
        this.date[1] = this.endTime
      }
    }
  }
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';
</style>


