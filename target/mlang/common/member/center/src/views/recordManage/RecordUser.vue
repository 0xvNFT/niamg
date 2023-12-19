<template>
  <div class="recordUser-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item" v-if="onOffBtn.canBePromo || onOffBtn.canBeRecommend">
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
      <DateModule :fastBtn="true" @searchMethod="searchMethod"></DateModule>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column prop="username" :label="$t('userName')"></el-table-column>
        <el-table-column prop="statDate" :label="$t('dataText')"></el-table-column>
        <el-table-column prop="depositAmount" :label="$t('payText')"></el-table-column>
        <el-table-column prop="withdrawAmount" :label="$t('withdrawal')"></el-table-column>
        <el-table-column prop="lotBetAmount" :label="$t('lotBetAmount')"></el-table-column>
        <el-table-column prop="lotWinAmount" :label="$t('lotWinAmount')"></el-table-column>
        <el-table-column prop="liveBetAmount" :label="$t('liveBetAmount')"></el-table-column>
        <el-table-column prop="liveWinAmount" :label="$t('liveWinAmount')"></el-table-column>
        <el-table-column prop="egameBetAmount" :label="$t('egameBetAmount')"></el-table-column>
        <el-table-column prop="egameWinAmount" :label="$t('egameWinAmount')"></el-table-column>
        <el-table-column prop="sportBetAmount" :label="$t('sportBetAmount')"></el-table-column>
        <el-table-column prop="sportWinAmount" :label="$t('sportWinAmount')"></el-table-column>
        <el-table-column prop="chessBetAmount" :label="$t('chessBetAmount')"></el-table-column>
        <el-table-column prop="chessWinAmount" :label="$t('chessWinAmount')"></el-table-column>
        <el-table-column prop="esportBetAmount" :label="$t('esportBetAmount')"></el-table-column>
        <el-table-column prop="esportWinAmount" :label="$t('esportWinAmount')"></el-table-column>
        <el-table-column prop="fishingBetAmount" :label="$t('fishingBetAmount')"></el-table-column>
        <el-table-column prop="fishingWinAmount" :label="$t('fishingWinAmount')"></el-table-column>
        <el-table-column prop="proxyRebateAmount" :label="$t('proxyRebateAmount')"></el-table-column>
        <el-table-column prop="activeAwardAmount" :label="$t('activeAwardAmount')"></el-table-column>
      </el-table>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      <p style="margin-top: 15px">{{ $t("recordHint") }}</p>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      checked: true,//包含下级复选框
      username: '',//用户名
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
  mounted () {
    this.searchMethod()
  },
  methods: {
    // 积分兑换记录
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
      params.append('include', this.checked);
      this.$axiosPack.post("/userCenter/report/personReport.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.tableData = res.data.rows
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
  .recordUser-page {
    .el-button--primary{
      background-color: #f12c4c;
      border-color: #f12c4c;
    }
  }
</style>


