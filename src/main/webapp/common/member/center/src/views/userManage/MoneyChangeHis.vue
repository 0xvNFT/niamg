<template>
  <div class="moneyChangeHis-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="status" size="small">
          <el-option v-for="item in statusArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="item">
        {{ $t("shiftToType") }}：
        <el-select v-model="type" size="small">
          <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="item">
        {{ $t("sanFangType") }}：
        <el-select v-model="platform" size="small">
          <el-option v-for="item in platformArr" :key="item.platform" :label="item.name" :value="item.platform">
          </el-option>
        </el-select>
      </div>
      <DateModule @searchMethod="searchMethod" :fastBtn="true"></DateModule>
    </div>
    <div class="list">
      <el-table :data="data" style="width: 99%">
        <el-table-column prop="createDatetime" :label="$t('transferTime')"></el-table-column>
        <el-table-column :label="$t('transferType')">
          <template slot-scope="scope">
            {{scope.row.type === 2 ? $t('sysTo') + gameType(scope.row.platform) : $t('from') + gameType(scope.row.platform) + $t('fromSys')}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('platformText')">
          <template slot-scope="scope">
            {{gameType(scope.row.platform)}}
          </template>
        </el-table-column>
        <el-table-column prop="orderId" :label="$t('transferOrderId')"></el-table-column>
        <el-table-column prop="money" :label="$t('transferMoney')"></el-table-column>
        <el-table-column :label="$t('transferStatus')">
          <template slot-scope="scope">
            {{statusMoney(scope.row.status)}}
          </template>
        </el-table-column>
      </el-table>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
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
      status: '',
      statusArr: [{ value: '', label: this.$t('allStatus') }, { value: 1, label: this.$t('transferError') }, { value: 2, label: this.$t('transferSuccess') }, { value: 3, label: this.$t('transfering') }],
      type: '',
      typeArr: [{ value: '', label: this.$t('moneyChangeAllType') }, { value: 2, label: this.$t('fromSysToSf') }, { value: 1, label: this.$t('fromSfToSys') }],
      platform: '',
      platformArr: [],
      data: [],//数据
      gameArr: [],
      total: 0,//总条数
      pageNumber: 1,//当前页码
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    ...mapState(['onOffBtn', 'gameOnOff'])
  },
  components: {
    DateModule,
    PageModule
  },
  created () {
    // 获取数据
    this.searchMethod()
    // 获取三方额度数据
    this.exchangePageInfo()
  },
  mounted () {
    // let _this = this
    // var timer = setInterval(() => {
    //   if (_this.onOffBtn) {
    //     clearInterval(timer);
    //     _this.platformArr = [{ value: '', label: this.$t("allText"), platform: '' }, ..._this.gameOnOff]
    //   }
    // }, 300);
    // this.$once('hook:beforeDestroy', function () {
    //   clearInterval(timer);
    // });
  },
  methods: {
    // 获取三方额度数据
    exchangePageInfo () {
      this.$axiosPack.post("/userCenter/third/exchangePageInfo.do").then(res => {
        if (res) {
          this.platformArr = [{ name: this.$t("allText"), platform: '' }, ...res.data.third]
          // this.$store.dispatch("getGameOnOff", res.data.third);
        }
      });
    },
    // 获取数据
    searchMethod (startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('status', this.status);
      params.append('platform', this.platform);
      params.append('type', this.type);
      this.$axiosPack.post("/userCenter/exchangeHistory.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data.rows
          this.total = res.data.total
        }
      });
    },

    // 平台转换
    gameType (platform) {
      let noFind = true
      for (let i = 0; i < this.gameOnOff.length; i++) {
        if (this.gameOnOff[i].platform === platform) {
          noFind = false
          return this.gameOnOff[i].name
        }
      }
      if (noFind) return '其他'
    },

    // 转账状态转换函数
    statusMoney (status) {
      var status_name = '';
      switch (status) {
        case 1:
          status_name = this.$t("errorText");
          break;
        case 2:
          status_name = this.$t("successText");
          break;
        case 3:
          status_name = this.$t("transfering");
          break;
      }
      return status_name;
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
.moneyChangeHis-page {
  .el-button--small{
    background-color: #f12c4c;
    border: 1px solid #0ec504;
  }
  .el-button--primary{
    background-color: #f12c4c;
    border: #f12c4c;
  }
  .list {
    margin-top: 10px;
  }
}
</style>


