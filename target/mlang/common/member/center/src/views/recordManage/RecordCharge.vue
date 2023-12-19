<template>
  <div class="index-page recordCharge-page">
    <el-tabs type="border-card" v-model="tabIndex">
      <el-tab-pane :label="$t('orderPay')" name="1">
        <div class="searchDiv">
          <div class="item">
            {{ $t("userName") }}：
            <el-input v-model="username" class="outInput" clearable size="small"></el-input>
          </div>
          <div class="item">
            {{ $t("orderId") }}：
            <el-input v-model="orderId" class="outInput" clearable size="small"></el-input>
          </div>
          <div class="item">
            {{ $t("status") }}：
            <el-select v-model="type" size="small">
              <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.canBePromo || onOffBtn.canBeRecommend">
            <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
          </div>
          <DateModule :fastBtn="true" @searchMethod="searchMethod"></DateModule>
        </div>
        <el-table :data="tableData" style="width: 99%">
          <el-table-column prop="orderId" :label="$t('orderId')"></el-table-column>
          <el-table-column prop="username" :label="$t('userName')"></el-table-column>
          <el-table-column prop="createDatetime" :label="$t('payTime')"></el-table-column>
          <el-table-column prop="money" :label="$t('payMoney')"></el-table-column>
          <el-table-column prop="fsText" :label="$t('payType')"></el-table-column>
          <el-table-column prop="statusText" :label="$t('payStatus')"></el-table-column>
          <el-table-column prop="remark" :label="$t('remarkText')"></el-table-column>
        </el-table>
        <div class="statistics">
          <div class="line">
            {{ $t("totalText") }}：
            <div style="color: red">{{data.total || 0}}{{ $t("everyBi") }}</div>
            <div style="margin-left: 20px" v-if="data.aggsData">{{ $t("payMoney") }}：{{data.aggsData.totalMoney || 0}}</div>
          </div>
        </div>
        <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      </el-tab-pane>
      <el-tab-pane :label="$t('withdrawOrder')" name="2">
        <div class="searchDiv">
          <div class="item">
            {{ $t("userName") }}：
            <el-input v-model="username" class="outInput" clearable size="small"></el-input>
          </div>
          <div class="item">
            {{ $t("orderId") }}：
            <el-input v-model="orderId" class="outInput" clearable size="small"></el-input>
          </div>
          <div class="item">
            {{ $t("status") }}：
            <el-select v-model="type" size="small">
              <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.canBePromo || onOffBtn.canBeRecommend">
            <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
          </div>
          <DateModule :fastBtn="true" @searchMethod="searchMethod"></DateModule>
        </div>
        <el-table :data="tableData" style="width: 99%">
          <el-table-column prop="orderId" :label="$t('orderId')"></el-table-column>
          <el-table-column prop="username" :label="$t('userName')"></el-table-column>
          <el-table-column prop="createDatetime" :label="$t('withdrawalTime')"></el-table-column>
          <el-table-column prop="drawMoney" :label="$t('applyMoney')"></el-table-column>
          <el-table-column prop="trueMoney" :label="$t('withdrawalMoney')"></el-table-column>
          <el-table-column prop="feeMoney" :label="$t('serverMoney')"></el-table-column>
          <el-table-column prop="bankName" :label="$t('withdrawalBank')"></el-table-column>
          <el-table-column prop="cardNo" :label="$t('bankCardNo')"></el-table-column>
          <el-table-column prop="statusText" :label="$t('withdrawalStatus')"></el-table-column>
          <el-table-column prop="remark" :label="$t('remarkText')"></el-table-column>
        </el-table>
        <div class="statistics">
          <div class="line">
            {{ $t("totalText") }}：
            <div style="color: red">{{data.total || 0}}{{ $t("everyBi") }}</div>
            <template v-if="data.aggsData">
              <span style="margin-left: 20px">{{ $t("withdrawalMoney") }}：{{data.aggsData.totalMoney || 0}}</span>
              <div style="margin-left: 20px">{{ $t("serverMoney") }}：{{data.aggsData.feeMoney || 0}}</div>
              <div style="margin-left: 20px">{{ $t("trueMoney") }}：{{data.aggsData.trueMoney || 0}}</div>
            </template>
          </div>
        </div>
        <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      tabIndex: '1',//
      type: '',
      typeArr: [{ value: '', label: this.$t("allStatus") }, { value: 1, label: this.$t("transfering") }, { value: 2, label: this.$t("successText") }, { value: 3, label: this.$t("errorText") }, { value: 4, label: this.$t("timeFailed") }],
      checked: false,//包含下级复选框
      orderId: '',//订单号
      username: '',//用户名
      data: '',//总数据
      tableData: [],//列表数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
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
    searchMethod (startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      let url = '/userCenter/report/depositRecordList.do'
      if (this.tabIndex === '2') url = '/userCenter/report/withdrawRecordList.do'

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('orderId', this.orderId);
      params.append('include', this.checked);
      params.append('status', this.type);
      params.append('username', this.username);
      this.$axiosPack.post(url, { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          for (let i = 0; i < res.data.rows.length; i++) {
            switch (res.data.rows[i].depositType) {
              case 1: //在线充值
              res.data.rows[i].fsText =this.$t("depositType")
                break;
                case 2: //快速入款
              res.data.rows[i].fsText =this.$t("quickDeposit")
                break;
                case 3: //银行转账
              res.data.rows[i].fsText =this.$t("bankDeposit")
                break;
                case 4: //手动加款
              res.data.rows[i].fsText =this.$t("depositArtificial")
                break;
              default:
                break;
            }
            res.data.rows[i].statusText = this.statusFun(res.data.rows[i].status)
          }
          this.data = res.data
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },
    statusFun (status) {
      if (status == 1) {
        return this.$t("untreated")
      } else if (status == 2) {
        return this.$t("success")
      } else if (status == 3) {
        return this.$t("failed")
      } else if (status == 4) {
        if (this.tabIndex === '1') return this.$t("canceled")
        else return this.$t("haveExpired")
      } else {
        return this.$t("haveExpired")
      }
    }
  },
  watch: {
    tabIndex () {
      this.pageNumber = 1
      this.type = ''
      this.checked = false
      this.orderId = ''
      this.username = ''
      this.searchMethod()
    }
  }
};
</script>

<style lang="scss">
.recordCharge-page {
  .el-button--primary {
    background-color: #eb031c;
    border: #eb031c;
  }
}
</style>


