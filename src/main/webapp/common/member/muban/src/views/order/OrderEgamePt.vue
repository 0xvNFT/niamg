<template>
  <div class="order-page">
    <div class="heard">
      <!-- pt电子注单 -->
      {{ $t("orderEgamePt") }}
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" :placeholder="$t('pleaseInput') + $t('userName')" class="outInput" clearable
          size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("orderId") }}：
        <el-input v-model="orderId" :placeholder="$t('pleaseInput') + $t('orderId')" class="outInput" clearable
          size="small"></el-input>
      </div>
      <div class="item">
        <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime"
          value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left:15px;">{{
          $t("searchText") }}</el-button>
        <!-- 重置按钮 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left:10px;">{{
          $t("reset") }}</el-button>
      </div>
    </div>

    <div class="content">
      <div class="title">
        <p>{{ $t("platformText") }}</p>
        <p>{{ $t("betOrderId") }}</p>
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("bettingTime") }}</p>
        <p>{{ $t("bettingMoney") }}</p>
        <p>{{ $t("winMoney") }}</p>
        <p>{{ $t("tableCode") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key='index'>
          <div class="cont-show">
            <p>{{ item.gameName }}</p>
            <p>{{ item.gameId || '-' }}</p>
            <div class="doubleP">
              <p>
                {{ item.username }}
              </p>
              <p>{{ item.thirdUsername }}</p>
            </div>
            <p>{{ dataFun(item.bettingTime) }}</p>
            <p>{{ item.bettingMoney | amount }}</p>
            <p>{{ item.winMoney | amount }}</p>
            <p>{{ item.orderId }}</p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!tableData.length">
      <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>
    <div class="statistics">
      <div class="line" v-if="data.aggsData">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">{{ $t("bettingMoney") }}：{{ data.aggsData.bettingMoneyCount || 0 | amount }}</div>
        <div style="margin-left: 20px">{{ $t("winMoney") }}：{{ data.aggsData.winMoneyCount || 0 | amount }}</div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'
import DATE from "@/assets/js/date";

export default {
  data() {
    return {
      username: '',//用户名
      orderId: '',//订单号
      // checked: true,//包含下级复选框
      data: '',//数据
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      // startTime: '2023-08-25 00:00:00',//搜索开始时间
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      date: [],//日期
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    PageModule
  },
  mounted() {
    // 获取数据
    this.searchMethod()
  },
  methods: {
    // 记录
    searchMethod(isSearch) {
      if (isSearch) this.pageNumber = 1

      //改变提交格式
      let params = {
        'startTime': this.date.length ? this.date[0] : this.startTime,
        'endTime': this.date.length ? this.date[1] : this.endTime,
        'pageNumber': this.pageNumber,
        'platform': this.platform,
        'orderId': this.orderId,
        'username': this.username,
      }
      params.load = true

      this.$API.ptRecord(params).then(res => {
        if (res) {
          this.data = res
          this.tableData = res.rows || []
          this.total = res.total
        }
      });
    },

    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this)
    },

    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val)
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
  }
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';
</style>


