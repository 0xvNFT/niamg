<template>
  <div class="recordTeam-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <DateModule :fastBtn="true" @searchMethod="searchMethod"></DateModule>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column prop="username" :label="$t('userName')"></el-table-column>
        <el-table-column :label="$t('primaryGroup')">
          <template slot-scope="scope">
            {{scope.row.userType === 130 ? $t("member") : $t("proxy")}}
          </template>
        </el-table-column>
        <el-table-column prop="liveBetAmount" :label="$t('bettingMoney')"></el-table-column>
        <el-table-column prop="liveRebateAmount" :label="$t('rebateText')"></el-table-column>
        <el-table-column prop="proxyRebateAmount" :label="$t('proxyRebateAmount')"></el-table-column>
        <el-table-column prop="liveWinAmount" :label="$t('winAmount')"></el-table-column>
        <el-table-column prop="activeAwardAmount" :label="$t('activeAwardAmount')"></el-table-column>
        <el-table-column prop="depositAmount" :label="$t('depositAmount')"></el-table-column>
        <el-table-column prop="withdrawAmount" :label="$t('withdrawAmount')"></el-table-column>
        <el-table-column prop="liveBetNum" :label="$t('realBetNum')"></el-table-column>
      </el-table>
      <div class="statistics">
        <div class="line" v-if="totalData">
          {{ $t("totalText") }}：
          <div style="margin-left: 20px">{{ $t("bettingMoney") }}：{{totalData.liveBetAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("rebateText") }}：{{totalData.liveRebateAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("proxyRebateAmount") }}：{{totalData.proxyRebateAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("winAmount") }}：{{totalData.liveWinAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("activeAwardAmount") }}：{{totalData.activeAwardAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("depositAmount") }}：{{totalData.depositAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("withdrawAmount") }}：{{totalData.withdrawAmount || 0}}</div>
          <div style="margin-left: 20px">{{ $t("realBetNum") }}：{{totalData.liveBetNum || 0}}</div>
        </div>
      </div>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      <p style="margin-top: 15px">{{ $t("recordHint") }}</p>
    </div>
  </div>
</template>

<script>
// import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      username: '',//用户名
      totalData: '',//总计的数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
  },
  components: {
    DateModule,
    PageModule
  },
  mounted () {
    this.searchMethod()
  },
  methods: {
    // 记录
    searchMethod (startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('username', this.username);
      this.$axiosPack.post("/userCenter/report/teamReport.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.totalData = res.data.total
          this.tableData = res.data.page.rows
          this.total = res.data.page.total
        }
      });
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
.recordTeam-page {
}
</style>


