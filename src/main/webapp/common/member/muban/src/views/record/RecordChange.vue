<template>
  <div class="recordChange-page order-page">
    <div class="heard">
      <!-- 账变报表 -->
      <p>{{ $t("recordChange") }}</p>
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" :placeholder="$t('pleaseInput') + $t('userName')" class="outInput" clearable
          size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("income") }}：
        <el-select v-model="val1" :placeholder="$t('pleaseSelect')" size="small" multiple collapse-tags>
          <el-option v-for="(val, key) in typeList.incomeType" :key="key" :label="val.name" :value="val.type">
          </el-option>
        </el-select>
      </div>
      <div class="item">
        {{ $t("expend") }}：
        <el-select v-model="val2" :placeholder="$t('pleaseSelect')" size="small" multiple collapse-tags>
          <el-option v-for="(val, key) in typeList.payType" :key="key" :label="val.name" :value="val.type">
          </el-option>
        </el-select>
      </div>
      <div v-if="afterLoginOnOffBtn.canBePromo || afterLoginOnOffBtn.canBeRecommend">
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
      <div class="item">
        <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{
          $t("searchText") }}</el-button>
        <!-- 查询全部 -->
        <el-button type="primary" @click="searchMethod(1)" icon="el-icon-search" size="medium"
          style="margin-left:15px;">{{ $t("searcAll") }}</el-button>
        <!-- 重置按钮 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left:15px;">{{
          $t("reset") }}</el-button>
      </div>
    </div>
    <div class="content">

      <div class="title">
        <p>{{ $t("typeCn") }}</p>
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("timeText") }}</p>
        <p>{{ $t("income") }}</p>
        <p>{{ $t("expend") }}</p>
        <p>{{ $t("afterMoney") }}</p>
        <p>{{ $t("remarkText") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key='index'>
          <div class="cont-show">
            <p>{{ item.typeCn }}</p>
            <p>{{ item.username }}</p>
            <p>{{ item.createDatetimeStr }}</p>
            <p>{{ item.add ? item.money : '-' | amount }}</p>
            <p>{{ item.add ? '-': item.money | amount }}</p>
            <p>{{ item.afterMoney | amount }}</p>
            <p>{{ item.remark }}</p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!tableData.length">
      <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>

    <div class="statistics">
      <div class="line">
        {{ $t("subtotal") }}：
        <div style="color: red">{{ data.subTotal || 0 }}{{ $t("everyBi") }}</div>
        <div style="margin-left: 20px">{{ $t("income") }}：{{ data.subTotalSMoney || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("expend") }}：{{ data.subTotalZMoney || 0 | amount }}</div>
      </div>
      <div class="line">
        {{ $t("totalText") }}：
        <div style="color: red">{{ data.total || 0 }}{{ $t("everyBi") }}</div>
        <div style="margin-left: 20px">{{ $t("income") }}：{{ data.totalSMoney || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("expend") }}：{{ data.totalZMoney || 0 | amount }}</div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    <p style="margin-top: 15px">{{ $t("recordChangeHint") }}</p>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DATE from "@/assets/js/date.js";

import PageModule from '@/components/common/PageModule'
export default {
  name: 'recordChange',
  data() {
    return {
      typeAll: '',
      checked: true,//包含下级复选框
      username: '',//用户名
      data: '',//数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      typeList: '',
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      date: [],//日期
      val1: [],//收入多选框数据
      val2: [],//支出多选框数据
    };
  },
  computed: {
    ...mapState(['onOffBtn', 'afterLoginOnOffBtn'])
  },
  components: { PageModule },
  mounted() {
    this.moneyHistoryInfo()
    this.searchMethod()
    this.$bus.$on('upData', () => {
      this.moneyHistoryInfo()
    })
  },
  methods: {
    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val)
    },
    moneyHistoryInfo() {
      this.$API.moneyHistoryInfo({ load: true }).then((res) => {
        if (res) {
          if (res.incomeType) {
            for (let i = 0; i < res.incomeType.length; i++) {
              res.incomeType[i].isCheck = false
            }
          }
          if (res.payType) {
            for (let i = 0; i < res.payType.length; i++) {
              res.payType[i].isCheck = false
            }
          }
          this.typeList = res
          console.log(this.typeList, 'this.typeList')
        }
      })
    },

    searchMethod(redius) {
      let type = ''
      let arr = []
      if (this.val1.length) {
        for (let i = 0; i < this.val1.length; i++) {
          arr.push(this.val1[i])
        }
      }
      if (this.val2.length) {
        for (let i = 0; i < this.val2.length; i++) {
          arr.push(this.val2[i])
        }
      }
      if (redius) {
        type = ''
        this.val1 = []
        this.val2 = []
      } else {
        if (arr.length) {
          for (let i = 0; i < arr.length; i++) {
            type += arr[i] + ','
          }
        } else {
          type = ''
        }
      }
      //改变提交格式
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
        'pageNumber': this.pageNumber,
        'types': type,
        'include': this.checked,
        'username': this.username,
      }
      params.load = true

      this.$API.moneyHistoryList(params).then((res) => {
        if (res.page) {
          this.data = res;
          this.tableData = res.page.rows;
          this.total = res.total;
        } else {
          this.data = '';
          this.tableData = [];
          this.total = 0;
        }
      })
    },
    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this, 'typeList');
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
  },
  destroyed() {
    // 销毁自定义监听器
    this.$bus.$off('upData')
  }
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';
</style>
