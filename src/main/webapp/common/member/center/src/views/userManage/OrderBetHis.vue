<template>
  <div class="moneyChangeHis-page order-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="type" size="small">
          <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <DateModule @searchMethod="searchMethod"></DateModule>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column :label="$t('changeType')">
          <template slot-scope="scope">
            {{typeTxt(scope.row.type)}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('orderId')">
          <template slot-scope="scope">
            {{scope.row.orderId || '-'}}
          </template>
        </el-table-column>
        <el-table-column prop="beforeNum" :label="$t('beforeNum')"></el-table-column>
        <el-table-column prop="betNum" :label="$t('betNum')"></el-table-column>
        <el-table-column prop="afterNum" :label="$t('afterNum')"></el-table-column>
        <el-table-column prop="beforeDrawNeed" :label="$t('beforeDrawNeed')"></el-table-column>
        <el-table-column prop="drawNeed" :label="$t('drawNeed')"></el-table-column>
        <el-table-column prop="afterDrawNeed" :label="$t('afterDrawNeed')"></el-table-column>
        <el-table-column :label="$t('scoreTime')">
          <template slot-scope="scope">
            {{scope.row.createDatetimeStr}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('remarkText')">
          <template slot-scope="scope">
            <p class="lookInfo" @click="lookInfo(scope.row.remark)" v-if="String(scope.row.remark).length > 6">{{ $t("lookInfo") }}</p>
            <span v-else-if="scope.row.remark">{{scope.row.remark}}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="statistics">
        <div class="line" v-if="data.aggsData">
          {{ $t("totalText") }}：
          <div style="color: red">{{data.aggsData.betNumCount}}</div>
        </div>
      </div>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog :title="$t('info')" :visible.sync="dialogShow" width="40%" :close-on-click-modal="false" class="info-dialog">
      <div class="cont" v-if="content" v-html="content"></div>
    </el-dialog>
  </div>
</template>

<script>
// import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      type: '',
      typeArr: [{ value: '', label: this.$t("allText") }, { value: 1, label: this.$t("live") }, { value: 2, label: this.$t("egame") }, { value: 3, label: this.$t("sport") }, { value: 4, label: this.$t("chess") }, { value: 5, label: this.$t("esport") }, { value: 6, label: this.$t("fish") }, { value: 7, label: this.$t("lotText") }, { value: 10, label: this.$t("peopleAdd") }, { value: 11, label: this.$t("peopleSub") }, { value: 12, label: this.$t("regPresent") }, { value: 13, label: this.$t("redPacket") }, { value: 14, label: this.$t("everyType") }, { value: 15, label: this.$t("rebateText") }, { value: 16, label: this.$t("depositText") }, { value: 17, label: this.$t("activeWin") }, { value: 18, label: this.$t("classPresent") }, { value: 20, label: this.$t("fandianEarnings") }, { value: 21, label: this.$t("scoreToMoney") }, { value: 24, label: this.$t("active") }],
      data: '',//总数据
      tableData: [],//列表数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
      content: '',//当前订单详情
      dialogShow: false,//是否显示弹窗
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
      params.append('type', this.type);
      this.$axiosPack.post("/userCenter/betHis/list.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },

    // 变动类型转换
    typeTxt (type) {
      for (let i = 0; i < this.typeArr.length; i++) {
        if (this.typeArr[i].value === type) {
          return this.typeArr[i].label
        }
      }
    },

    // 时间函数
    dataFun (val) {
      return this.$dataJS.dateChange(val)
    },

    lookInfo (cont) {
      this.content = cont
      this.dialogShow = true
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.moneyChangeHis-page {
  .el-button--primary{
    background-color:#0ec504;
    border: #0ec504;
  }
  .list {
    margin-top: 10px;
  }
}
</style>


