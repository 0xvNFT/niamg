<template>
  <div class="order-page">
    <div class="heard">
      <!-- 彩票注单 -->
      {{ $t("orderLottery") }}
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" :placeholder="$t('pleaseInput') + $t('orderId')" class="outInput" clearable
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
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left: 15px">{{
          $t("searchText") }}</el-button>
        <!-- 重置按钮 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left: 10px">{{
          $t("reset") }}</el-button>
      </div>
    </div>
    <div class="content">
      <div class="title">
        <p>{{ $t("platformAndOrder") }}</p>
        <p>{{ $t("lotNameAndOrderId") }}</p>
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("bettingTime") }}</p>
        <p>{{ $t("playName") }}</p>
        <p>{{ $t("bettingMoney") }}</p>
        <p>{{ $t("realBettingMoney") }}</p>
        <p>{{ $t("winMoney") }}</p>
        <p>{{ $t("status") }}</p>
        <p>{{ $t("bettingContent") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key="index">
          <div class="cont-show">
            <div class="doubleP">
              <p>
                {{ item.platformType }}
              </p>
              <p>{{ item.orderId }}</p>
            </div>
            <div class="doubleP">
              <p>{{ item.gameName || "-" }}</p>
              <p>{{ item.qiHao }}</p>
            </div>
            <div class="doubleP">
              <p>
                {{ item.username }}
              </p>
              <p>{{ item.thirdUserName }}</p>
            </div>
            <p>{{ item.createDatetimeStr }}</p>

            <p>{{ item.playName || "-" }}</p>
            <p>{{ item.bettingMoney }}</p>
            <p>{{ item.realBettingMoney || "-" | amount }}</p>
            <p>{{ item.winMoney | amount }}</p>
            <p :class="'status--' + item.status">
              {{ $t("resultStatus" + item.status) }}
            </p>
            <p>{{ item.content }}</p>
          </div>
        </div>
      </div>
    </div>
    <div v-if="!tableData.length">
      <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>
    <div class="statistics">
      <div class="line" v-if="data">
        {{ $t("totalText") }}：
        <div style="margin-left: 20px">
          {{ $t("bettingMoney") }}：{{
            data.bettingMoneyCount || 0 | amount
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("realBettingMoney") }}：{{
            data.realBettingMoneyCount || 0 | amount
          }}
        </div>
        <div style="margin-left: 20px">
          {{ $t("winMoney") }}：{{ data.winMoneyCount || 0 | amount }}
        </div>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { i18n } from "../../lang/i18n";
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      platform: "", //类型
      platformArr: [{ name: i18n.t("allPlatform"), value: "" }],
      username: "", //用户名
      orderId: "", //订单号
      data: "", //数据
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      content: "", //当前订单详情
      // startTime: '2023-08-30 00:00:00',//搜索开始时间
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
    userInfo() {
      return this.$store.state.userInfo;
    },
  },
  components: {
    PageModule,
  },
  mounted() {
    let _this = this;
    var timer = setInterval(() => {
      if (_this.onOffBtn) {
        clearInterval(timer);
        _this.platformArr = _this.platformArr.concat(_this.onOffBtn.lottery);
        _this.username = _this.userInfo.username;
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

      var startTime = this.date.length ? this.date[0] : this.startTime;
      var endTime = this.date.length ? this.date[1] : this.endTime;
      //改变提交格式
      let params = {
        startTime: startTime,
        endTime: endTime,
        pageNumber: this.pageNumber,
        platform: this.platform,
        orderId: this.orderId,
        username: this.username,
      };
      params.load = true;

      this.$API.lotRecord(params).then((res) => {
        if (res) {
          this.data = JSON.parse(res.aggsData);
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },

    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this, "platformArr");
    },

    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val);
    },
  },
  watch: {
    date(e) {
      if (e == null) {
        this.date = [];
        this.date[0] = this.startTime;
        this.date[1] = this.endTime;
      }
    },
  },
};
</script>

<style lang="scss">
@import "../../assets/css/order.scss";
</style>
