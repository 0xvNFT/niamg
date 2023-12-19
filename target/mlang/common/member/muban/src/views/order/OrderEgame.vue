<template>
  <div class="order-page">
    <div class="heard">
      <!-- 电子注单 -->
      {{ $t("orderEgame") }}
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
        {{ $t("platform") }}：
        <el-select v-model="platform" size="small">
          <el-option v-for="item in platformArr" :key="item.value" :label="item.name" :value="item.value">
          </el-option>
        </el-select>
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
        <p>{{ $t("platformType") }}</p>
        <p>{{ $t("platformText") }}</p>
        <p>{{ $t("betOrderId") }}</p>
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("bettingTime") }}</p>
        <p>{{ $t("bettingMoney") }}</p>
        <p>{{ $t("realBettingMoney") }}</p>
        <p>{{ $t("winMoney") }}</p>
        <p>{{ $t("tableCode") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key='index'>
          <div class="cont-show">
            <p>{{ item.platformType }}</p>
            <p>{{ item.gameName }}</p>
            <p>{{ item.orderId }}</p>
            <div class="doubleP">
              <p>
                {{ item.username }}
              </p>
              <p>{{ item.thirdUsername }}</p>
            </div>
            <p>{{ dataFun(item.bettingTime) }}</p>
            <p>{{ item.bettingMoney | amount }}</p>
            <p>{{ item.realBettingMoney || '-' | amount }}</p>
            <p>{{ item.winMoney | amount }}</p>
            <p>{{ item.gameCode || "-" }}</p>
          </div>
        </div>
      </div>

    </div>
    <div v-if="!tableData.length">
      <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>
    <div class="statistics">
      <div class="line" v-if="data.aggsData">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">
          {{ $t("bettingMoney") }}：{{
            data.aggsData.bettingMoneyCount || 0 | amount
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("realBettingMoney") }}：{{
            data.aggsData.realBettingMoneyCount || 0 | amount
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("winMoney") }}：{{ data.aggsData.winMoneyCount || 0 | amount }}
        </div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </div>
</template>

<script>
import { mapState } from "vuex";
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      platform: "", //类型
      username: "", //用户名
      orderId: "", //订单号
      // checked: true,//包含下级复选框
      data: "", //数据
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      // startTime: '2023-08-16 00:00:00',//搜索开始时间
      startTime: DATE.today() + ' 00:00:00',//搜索开始时间
      endTime: DATE.today() + ' 23:59:59',//搜索结束时间
      date: [],//日期
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
    platformArr: {
      get: function () {
        let arr = [{ name: this.$t("allPlatform"), value: '' }].concat(this.onOffBtn.egame)
        return arr
      },
      set: function () {

      }
    }
  },
  components: {
    PageModule,
  },
  mounted() {
    let _this = this;
    var timer = setInterval(() => {
      if (_this.onOffBtn) {
        clearInterval(timer);
        // _this.platformArr = _this.platformArr.concat(_this.onOffBtn.egame);
        // 获取数据
        _this.searchMethod();
      }
    }, 300);
    this.$once("hook:beforeDestroy", function () {
      clearInterval(timer);
    });
  },
  methods: {
    // 记录
    searchMethod(isSearch) {
      if (isSearch) this.pageNumber = 1;

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

      this.$API.egameRecord(params).then(res => {
        if (res) {
          this.data = res
          this.tableData = res.rows
          this.total = res.total
          this.tableData.forEach((item) => {
            switch (item.gameType) {
              case "0":
                item.gameName = this.$t("ganmeType0");
                break;
              case "7":
                item.gameName = this.$t("ganmeType7");
                break;
              case "9":
                item.gameName = this.$t("ganmeType9");
                break;
              case "12":
                item.gameName = this.$t("ganmeType12");
                break;
              case "18":
                item.gameName = this.$t("chess");
                break;
              default:
                break;
            }
          });
        }
      });
    },

    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this)
    },

    // 平台类型
    typeFun(val) {
      let arr = this.onOffBtn.egame;
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].value === val) {
          return arr[i].name;
        }
      }
    },

    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val);
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
};
</script>

<style lang="scss">
@import '../../assets/css/order.scss';
</style>
