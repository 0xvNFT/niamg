<template>
  <div class="moneyChangeHis-page">
    <div class="searchDiv">
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="type" size="small">
          <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <DateModule @searchMethod="searchMethod"></DateModule>
      <el-button type="danger" size="small" style="height: 32px; margin-left: 20px" @click="dialogShow = true">{{ $t("scoreExChange") }}</el-button>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column :label="$t('changeType')">
          <template slot-scope="scope">
            {{typeTxt(scope.row.type)}}
          </template>
        </el-table-column>
        <el-table-column prop="beforeScore" :label="$t('beforeScore')"></el-table-column>
        <el-table-column prop="score" :label="$t('scoreNum')"></el-table-column>
        <el-table-column prop="afterScore" :label="$t('afterScore')"></el-table-column>
        <el-table-column :label="$t('scoreTime')">
          <template slot-scope="scope">
            {{scope.row.createDatetimeStr}}
          </template>
        </el-table-column>
        <el-table-column prop="remark" :label="$t('remarkText')"></el-table-column>
      </el-table>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    </div>
    <!-- 积分兑换弹窗 -->
    <el-dialog :title="$t('scoreExChange')" :visible.sync="dialogShow" width="50%" :close-on-click-modal="false" class="change-dialog">
      <div class="cont" v-if="curData">
        <div class="item">{{ $t("money") }}：{{data.money}}</div>
        <div class="item">{{ $t("score") }}：{{data.score}}</div>
        <div class="item">{{ $t("exchangeType") }}：
          <div style="display: inline-block;">
            <el-radio v-model="radio" label="1" v-if="data.moneyToScore">{{ $t("moneyToScore") }}</el-radio>
            <el-radio v-model="radio" label="2" v-if="data.scoreToMoney">{{ $t("scoreToMoney") }}</el-radio>
          </div>
        </div>
        <div class="item">{{ $t("exchangeScale") }}：{{curData.numerator}}:{{curData.denominator}}</div>
        <div class="item" style="color: red">{{ $t("exchangeMin") }}：{{curData.minVal}}，{{ $t("exchangeMax") }}：{{curData.maxVal}}</div>
        <div class="item">{{ $t("exchangeNum") }}：<input type="number" v-model="exchangeNum" class="changeNum"></div>
        <div class="item">{{ $t("exchangeToNum") }}：{{change}}</div>
      </div>
      <div v-else>{{ $t("notSet") }}</div>
      <span slot="footer" class="dialog-footer" v-if="curData">
        <el-button @click="dialogShow = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn">{{ $t("confirm") }}</el-button>
      </span>
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
      data: '',//积分兑换信息
      type: '',
      typeArr: [{ value: '', label: this.$t("allText") }, { value: 1, label: this.$t("typeActiveSub") }, { value: 2, label: this.$t("memberSign") }, { value: 3, label: this.$t("moneyToScore") }, { value: 4, label: this.$t("scoreToMoney") }, { value: 5, label: this.$t("depositPresented") }, { value: 6, label: this.$t("peopleAdd") }, { value: 7, label: this.$t("peopleSub") }],
      tableData: [],//积分兑换纪录
      dialogShow: false,//是否显示积分兑换弹窗
      radio: '1',//积分兑换类型
      exchangeNum: '',//兑换数量
      change: 0,//转换数量
      total: 0,//总条数
      pageNumber: 1,//当前页码
      curData: '',//当前积分数据
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
    // 积分兑换信息
    this.getScoreExchangeInfo()
    this.searchMethod()
  },
  methods: {
    // 积分兑换信息
    getScoreExchangeInfo () {
      this.$axiosPack.post("/userCenter/getScoreExchangeInfo.do").then(res => {
        if (res) {
          this.data = res.data
          if (res.data.moneyToScore) {
            this.radio = '1'
            this.curData = this.data.moneyToScore
          } else {
            this.radio = '2'
            this.curData = this.data.scoreToMoney
          }
        }
      });
    },
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
      params.append('type', this.type);
      this.$axiosPack.post("/userCenter/scoreHisData.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },

    // 提交
    sureBtn () {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('configId', this.curData.id);
      params.append('exchangeNum', this.exchangeNum);
      this.$axiosPack.post("/userCenter/confirmExchange.do", { params: params }).then(res => {
        if (res) {
          this.dialogShow = false
          this.$notify({
            title: 'success',
            message: res.data.msg,
            type: 'success'
          });
          this.searchMethod(null, null, true)
        }
      });
    },

    // 变动类型转换
    typeTxt (type) {
      if (type === 1) {
        return this.$t("typeActiveSub");
      } else if (type === 2) {
        return this.$t("memberSign");
      } else if (type === 3) {
        return this.$t("moneyToScore");
      } else if (type === 4) {
        return this.$t("scoreToMoney");
      } else if (type === 5) {
        return this.$t("depositPresented");
      } else if (type === 6) {
        return this.$t("peopleAdd");
      } else if (type === 7) {
        return this.$t("peopleSub");
      } else if (type === 8) {
        return this.$t("activeAdd");
      } else if (type === 12) {
        return this.$t("active");
      }
    },

    // 时间函数
    dataFun (val) {
      return this.$dataJS.dateChange(val)
    },
  },
  watch: {
    exchangeNum () {
      let str = String(this.exchangeNum)
      if (str.includes('.')) {
        this.exchangeNum = parseInt(this.exchangeNum);
      } else {
        this.change = this.exchangeNum / Number(this.curData.numerator) * Number(this.curData.denominator)
      }
    },
    dialogShow () {
      this.exchangeNum = ''
    },
    radio () {
      if (this.radio === '1' && this.data.moneyToScore) {
        this.curData = this.data.moneyToScore
      } else {
        this.curData = this.data.scoreToMoney
      }
      this.exchangeNum = ''
    },
  }
};
</script>

<style lang="scss">
.moneyChangeHis-page {
  .el-button--primary{
    background-color:#0ec504;
    border: #0ec504;
  }
  .el-button--danger {
    background-color:#f12c4c;
    border: #f12c4c;
  }
  .list {
    margin-top: 10px;
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
          border: 1px solid $borderShallow;
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


