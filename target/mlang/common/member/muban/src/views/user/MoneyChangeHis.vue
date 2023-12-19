<template>
  <div class="moneyChangeHis-page order-page">
    <div class="heard">
      <!-- 额度转换记录 -->
      {{ $t("moneyChangeHis") }}
    </div>
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
      <div class="item">
        <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{ $t("searchText") }}</el-button>
        <!-- 重置按钮 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left:10px;">{{ $t("reset") }}</el-button>
      </div>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%" height="540">
        <div slot="empty">
          <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
        <el-table-column type="index"></el-table-column>
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
        <el-table-column :label="$t('transferMoney')">
          <template slot-scope="scope">
            {{scope.row.money | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('transferStatus')">
          <template slot-scope="scope">
            <p :class="`status--${scope.row.status}`">{{ statusMoney(scope.row.status) }}</p>
          </template>
        </el-table-column>
      </el-table>
      <PageModule
      :total="total"
      :pageNumber.sync="pageNumber"
      layout="total, prev, pager, next, sizes, jumper"
      @change="paginationChange"
      @searchMethod="searchMethod"
      ></PageModule>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'
import DATE from "@/assets/js/date.js";

export default {
  data () {
    return {
      status: '',
      // statusArr: [{ value: '', label: this.$t('allStatus') }, { value: 1, label: this.$t('transferError') }, { value: 2, label: this.$t('transferSuccess') }, { value: 3, label: this.$t('transfering') }],
      type: '',
      // typeArr: [{ value: '', label: this.$t('moneyChangeAllType') }, { value: 2, label: this.$t('fromSysToSf') }, { value: 1, label: this.$t('fromSfToSys') }],
      platform: '',
      platformArr: [],
      tableData: [],//数据
      gameArr: [],
      total: 0,//总条数
      pageNumber: 1,//当前页码
      pageSize: 10, // 当前页条数
      // startTime: '2023-08-25 00:00:00',//搜索开始时间
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      date: [],//日期
      key: -1,//要显示的完整内容的key值
    };
  },
  computed: {
    ...mapState(['onOffBtn', 'gameOnOff']),
    statusArr: {
      get: function () {
        return [{ value: '', label: this.$t('allStatus') }, { value: 1, label: this.$t('transferError') }, { value: 2, label: this.$t('transferSuccess') }, { value: 3, label: this.$t('transfering') }]
      },
      set: function () {

      }
    },
    typeArr: {
      get: function () {
        return [{ value: '', label: this.$t('moneyChangeAllType') }, { value: 2, label: this.$t('fromSysToSf') }, { value: 1, label: this.$t('fromSfToSys') }]
      },
      set: function () {

      }
    }
  },
  components: {
    PageModule,
  },
  created () {
    // 获取数据
    this.searchMethod()
    // 获取三方额度数据
    this.exchangePageInfo()
  },
  mounted () {
  },
  methods: {
    // 获取三方额度数据
    exchangePageInfo () {
      this.$API.exchangePageInfo().then(res => {
        if (res) {
          this.platformArr = [{ name: this.$t("allText"), platform: '' }, ...res.third]
          // this.$store.dispatch("getGameOnOff", res.third);
        }
      });
    },
    // 获取数据
    searchMethod (startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1
      //改变提交格式
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
        'pageNumber': this.pageNumber,
        'status': this.status,
        'platform': this.platform,
        'type': this.type,
        'pageSize': this.pageSize
      }
      params.load = true

      this.$API.exchangeHistory(params).then(res => {
        if (res) {
          this.tableData = res.rows
          this.total = res.total
        }
      });
    },
    // 重置表单
    reset () {
      this.$publicJs.resetForm.call(this, 'platformArr')
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
      if (noFind) return this.$t('other')
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
    paginationChange (val) {
      this.pageSize = val;
      this.searchMethod();
    }
  },
  watch: {
    date (e) {
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

.moneyChangeHis-page {
  .el-button--small {
    background-color: #f12c4c;
    border: 1px solid #0ec504;
  }
  .list {
    margin-top: 10px;
    padding: 0 10px;
  }
}
</style>


