<template>
  <div class="orderBetHis-page order-page">
    <div class="heard">
      <!-- 打码量记录 -->
      {{ $t("orderBetHis") }}
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="type" size="small">
          <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="item">
        <el-date-picker style="margin-left: 20px;" v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{ $t("searchText") }}</el-button>
      </div>
    </div>
    <div class="list">
      <el-table :data="tableData">
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
        <el-table-column :label="$t('beforeNum')">
          <template slot-scope="scope">
            {{scope.row.beforeNum | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('changeBetNum')">
          <template slot-scope="scope">
            {{scope.row.betNum | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('afterNum')">
          <template slot-scope="scope">
            {{scope.row.afterNum | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('beforeDrawNeed')">
          <template slot-scope="scope">
            {{scope.row.beforeDrawNeed | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('drawNeed')">
          <template slot-scope="scope">
            {{scope.row.drawNeed | amount}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('afterDrawNeed')">
          <template slot-scope="scope">
            {{scope.row.afterDrawNeed | amount}}
          </template>
        </el-table-column>
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
        <div slot="empty">
          <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
      </el-table>
      <div class="statistics">
        <div class="line" v-if="data.aggsData">
          {{ $t("totalText") }}：
          <div style="color: red">{{data.aggsData.betNumCount | amount}}</div>
        </div>
      </div>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog :title="$t('info')" :visible.sync="dialogShow" width="40%" class="common_dialog">
      <div class="cont" v-if="content" v-html="content"></div>
    </el-dialog>
  </div>
</template>
   
<script>
// import { mapState } from 'vuex'
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  data () {
    return {
      type: '',
      // typeArr: [{ value: '', label: this.$t("allText") }, { value: 1, label: this.$t("live") }, { value: 2, label: this.$t("egame") }, { value: 3, label: this.$t("sport") }, { value: 4, label: this.$t("chess") }, { value: 5, label: this.$t("esport") }, { value: 6, label: this.$t("fish") }, { value: 7, label: this.$t("lotText") }, { value: 10, label: this.$t("peopleAdd") }, { value: 11, label: this.$t("peopleSub") }, { value: 12, label: this.$t("regPresent") }, { value: 13, label: this.$t("redPacket") }, { value: 14, label: this.$t("everyType") }, { value: 15, label: this.$t("rebateText") }, { value: 16, label: this.$t("depositText") }, { value: 17, label: this.$t("activeWin") }, { value: 18, label: this.$t("classPresent") }, { value: 20, label: this.$t("fandianEarnings") }, { value: 21, label: this.$t("scoreToMoney") }, { value: 24, label: this.$t("active") }],
      data: '',//总数据
      tableData: [],//列表数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      content: '',//当前订单详情
      dialogShow: false,//是否显示弹窗
      date: [],//日期
    };
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
    typeArr: {
      get: function () {
        return [{ value: '', label: this.$t("allText") }, { value: 1, label: this.$t("live") }, { value: 2, label: this.$t("egame") }, { value: 3, label: this.$t("sport") }, { value: 4, label: this.$t("chess") }, { value: 5, label: this.$t("esport") }, { value: 6, label: this.$t("fish") }, { value: 7, label: this.$t("lotText") }, { value: 10, label: this.$t("peopleAdd") }, { value: 11, label: this.$t("peopleSub") }, { value: 12, label: this.$t("regPresent") }, { value: 13, label: this.$t("redPacket") }, { value: 14, label: this.$t("everyType") }, { value: 15, label: this.$t("rebateText") }, { value: 16, label: this.$t("depositText") }, { value: 17, label: this.$t("activeWin") }, { value: 18, label: this.$t("classPresent") }, { value: 20, label: this.$t("fandianEarnings") }, { value: 21, label: this.$t("scoreToMoney") }, { value: 24, label: this.$t("active") }, { value: 25, label: this.$t("memberSign") }]
      },
      set: function () {

      }
    }
  },
  components: {
    //   DateModule,
    PageModule
  },
  mounted () {
    this.searchMethod()
  },
  methods: {
    // 记录
    searchMethod (isSearch) {
      // if (startTime) this.startTime = startTime
      // if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        type: this.type,
      };
      params.load = true

      this.$API.getBetHis(params).then(res => {
        if (res) {
          this.data = res;
          this.tableData = res.rows;
          this.total = res.total;
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
      return DATE.dateChange(val)
    },

    lookInfo (cont) {
      this.content = cont
      this.dialogShow = true
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

.orderBetHis-page {
  .line {
    height: 50px;
    line-height: 50px;
    background-color: $titleBg;
    padding: 0 10px;
  }
  .button--primary {
    background-color: #0ec504;
    border: #0ec504;
  }
  .button--danger {
    background-color: #f12c4c;
    border: #f12c4c;
  }
  .list {
    margin-top: 10px;
    .lookInfo {
      cursor: pointer;
    }
  }
  .change-dialog {
    .cont {
      width: fit-content;
      margin: 0 auto;
      text-align: left;
      font-size: 16px;
      .item {
        margin-bottom: 10px;
        .changeNum {
          border: 1px solid;
          height: 30px;
          border-radius: 3px;
          width: 150px;
          padding-left: 5px;
          font-size: 16px;
        }
      }
    }
  }
}
</style>
  
  
  