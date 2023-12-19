<template>
  <div class="">
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
    <el-card style="margin-bottom: 10px; width: 99%" shadow="never">
      <div>
        <el-checkbox v-model="typeAll">{{ $t("allType") }}</el-checkbox>
      </div>
      <div style="margin-top:5px">
        <span style="color:red">{{ $t("income") }}：</span>
        <el-checkbox v-model="val.isCheck" v-for="(val,key) in typeList.incomeType" :key="key">{{val.name}}</el-checkbox>
      </div>
      <div style="margin-top:5px">
        <span style="color:red">{{ $t("expend") }}：</span>
        <el-checkbox v-model="val.isCheck" v-for="(val,key) in typeList.payType" :key="key">{{val.name}}</el-checkbox>
      </div>
    </el-card>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column prop="username" :label="$t('userName')"></el-table-column>
        <el-table-column :label="$t('timeText')">
          <template slot-scope="scope">
            {{scope.row.createDatetimeStr}}
          </template>
        </el-table-column>
        <el-table-column prop="typeCn" :label="$t('typeCn')"></el-table-column>
        <el-table-column :label="$t('income')">
          <template slot-scope="scope">
            {{scope.row.add ? scope.row.money : '-'}}
          </template>
        </el-table-column>
        <el-table-column :label="$t('expend')">
          <template slot-scope="scope">
            {{scope.row.add ? '-' : scope.row.money}}
          </template>
        </el-table-column>
        <el-table-column prop="afterMoney" :label="$t('afterMoney')"></el-table-column>
        <el-table-column prop="remark" :label="$t('remarkText')"></el-table-column>
      </el-table>
      <div class="statistics">
        <div class="line">
          {{ $t("subtotal") }}：
          <div style="color: red">{{data.subTotal || 0}}{{ $t("everyBi") }}</div>
          <div style="margin-left: 20px">{{ $t("income") }}：{{data.subTotalSMoney || 0}}</div>
          <div style="margin-left: 20px">{{ $t("expend") }}：{{data.subTotalZMoney || 0}}</div>
        </div>
        <div class="line">
          {{ $t("totalText") }}：
          <div style="color: red">{{data.total || 0}}{{ $t("everyBi") }}</div>
          <div style="margin-left: 20px">{{ $t("income") }}：{{data.totalSMoney || 0}}</div>
          <div style="margin-left: 20px">{{ $t("expend") }}：{{data.totalZMoney || 0}}</div>
        </div>
      </div>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      <p style="margin-top: 15px">{{ $t("recordChangeHint") }}</p>
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
      typeAll: '',
      checked: true,//包含下级复选框
      username: '',//用户名
      data: [],//数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      typeList: '',
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
    this.moneyHistoryInfo()
    this.searchMethod()
  },
  methods: {
    moneyHistoryInfo () {
      this.$axiosPack.post("/userCenter/report/moneyHistoryInfo.do").then(res => {
        if (res) {
          if (res.data.incomeType) {
            for (let i = 0; i < res.data.incomeType.length; i++) {
              res.data.incomeType[i].isCheck = false
            }
          }
          if (res.data.payType) {
            for (let i = 0; i < res.data.payType.length; i++) {
              res.data.payType[i].isCheck = false
            }
          }
          this.typeList = res.data
        }
      });
    },

    searchMethod (startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1

      let type = ''
      if (!this.typeAll && this.typeList) {
        for (let i = 0; i < this.typeList.incomeType.length; i++) {
          if (this.typeList.incomeType[i].isCheck) {
            type += this.typeList.incomeType[i].type + ','
          }
        }
        for (let i = 0; i < this.typeList.payType.length; i++) {
          if (this.typeList.payType[i].isCheck) {
            type += this.typeList.payType[i].type + ','
          }
        }
      }
      type = type.substring(0, type.length - 1)

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('types', type);
      params.append('username', this.username);
      params.append('include', this.checked);
      this.$axiosPack.post("/userCenter/report/moneyHistoryList.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data
          this.tableData = res.data.page.rows
          this.total = res.data.total
        }
      });
    },

    // 时间函数
    dataFun (val) {
      return this.$dataJS.dateChange(val)
    },
  },
  watch: {
    typeAll () {
      for (let i = 0; i < this.typeList.incomeType.length; i++) {
        this.typeList.incomeType[i].isCheck = this.typeAll
      }
      for (let i = 0; i < this.typeList.payType.length; i++) {
        this.typeList.payType[i].isCheck = this.typeAll
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.el-card{
  // background: #0f1923;
  border: 1px solid #d0d0d0;
}
.el-checkbox__input{
  .el-checkbox__inner{
    background-color:none;
  }
}
.el-checkbox__label{
  color: #fff;
}
</style>


