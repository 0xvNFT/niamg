<template>
  <div class="order-page">
    <div class="heard">
      <!-- 体育注单 -->
      {{ $t("orderSport") }}
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
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left: 15px">{{
          $t("searchText") }}</el-button>
        <!-- 重置按钮 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left: 10px">{{
          $t("reset") }}</el-button>
      </div>
    </div>
    <div class="content">
      <div class="title">
        <p>{{ $t("platform") }}</p>
        <p>{{ $t("orderId") }}</p>
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("bettingTime") }}</p>
        <p>{{ $t("odds") }}</p>
        <p>{{ $t("bettingMoney") }}</p>
        <p>{{ $t("realBettingMoney") }}</p>
        <p>{{ $t("winMoney") }}</p>
        <p>{{ $t("status") }}</p>

        <p>{{ $t("bettingContent") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key="index">
          <div class="cont-show">
            <p>{{ item.platformType }}</p>
            <p>{{ item.orderId }}</p>
            <div class="doubleP">
              <p>{{ item.username }}</p>
              <p>{{ item.thirdUsername }}</p>
            </div>
            <p>{{ dataFun(item.bettingTime) }}</p>
            <p>{{ item.odds || "-" }}{{ item.oddsType || "-" }}</p>
            <p>{{ item.bettingMoney | amount }}</p>
            <p>{{ item.realBettingMoney || "-" | amount }}</p>
            <p>{{ item.winMoney | amount }}</p>
            <p v-html="statusFun(item.platformType, item.status)"></p>
            <template>
              <p class="lookInfo" @click-stop="lookInfo(item.bettingContent)" v-if="item.bettingContent">
                <span @click.stop="lookInfo(item.bettingContent)">
                  {{ $t("lookInfo") }}</span>
              </p>
              <p v-else>-</p>
            </template>
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
    <!-- 详情弹窗 -->
    <el-dialog :title="$t('info')" :visible.sync="dialogShow" width="40%" class="common_dialog">
      <div class="cont" v-if="content" v-html="content"></div>
    </el-dialog>
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
      data: "", //数据
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      content: "", //当前订单详情
      dialogShow: false, //是否显示弹窗
      // startTime: '2023-08-25 00:00:00',//搜索开始时间
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
    platformArr: {
      get: function () {
        let arr = [{ name: this.$t("allPlatform"), value: "" }].concat(
          this.onOffBtn.sport
        );
        return arr;
      },
      set: function () { },
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
        // _this.platformArr = _this.platformArr.concat(_this.onOffBtn.sport)
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
      console.log(this.platform, "this.platform");
      if (isSearch) this.pageNumber = 1;

      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        platform: this.platform,
        orderId: this.orderId,
        username: this.username,
      };
      params.load = true;

      this.$API.sportRecord(params).then((res) => {
        if (res) {
          this.data = res;
          this.tableData = res.rows || [];
          this.total = res.total;
        }
      });
    },

    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this);
    },

    // 平台类型
    typeFun(val) {
      let arr = this.onOffBtn.sport;
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

    // 状态
    statusFun(type, value) {
      switch (type) {
        case "TYSB":
          switch (value) {
            case "half won":
              return this.$t("halfWon");
            case "half lose":
              return this.$t("halfLose");
            case "won":
              return this.$t("won");
            case "lose":
              return this.$t("lose");
            case "draw":
              return this.$t("draw");
            case "reject":
              return this.$t("canceled");
            case "waiting":
              return this.$t("waiting");
            case "running":
              return this.$t("running");
            case "void":
              return this.$t("void");
            case "refund":
              return this.$t("refund");
          }
          break;
        case "TYXJ":
          switch (value) {
            case "1":
              return this.$t("sureText");
            case "2":
              return this.$t("settle");
            case "3":
              return this.$t("canceled");
            case "4":
              return this.$t("invalid");
          }
          break;
        case "SBTA":
          switch (value) {
            case "1":
              return this.$t("closeSettle");
            case "0":
              return this.$t("noSettle");
            case "-8":
              return this.$t("cancelCodeOrder");
            case "-9":
              return this.$t("cancelOrder");
            case "2":
              return this.$t("betting");
            case "4":
              return this.$t("cash");
          }
          break;
        case "SBO":
          switch (value) {
            case "half won":
              return this.$t("halfWon");
            case "half lose":
              return this.$t("halfLose");
            case "won":
              return this.$t("won");
            case "lose":
              return this.$t("lose");
            case "draw":
              return this.$t("draw");
            case "reject":
              return this.$t("canceled");
            case "waiting":
              return this.$t("waiting");
            case "running":
              return this.$t("running");
            case "void":
              return this.$t("void");
            case "refund":
              return this.$t("refund");
            case "waiting rejected":
              return this.$t("dangerous");
          }
          break;
        case "TYCR":
          switch (value) {
            case "0":
              return this.$t("noSettle");
            case "L":
              return this.$t("lose");
            case "W":
              return this.$t("won");
            case "P":
              return this.$t("draw");
            case "D":
              return this.$t("cancel");
            case "A":
              return this.$t("restore");
          }
          break;
        case "DJIM":
          switch (value) {
            case "0":
              return this.$t("noSettle");
            case "L":
              return this.$t("lose");
            case "W":
              return this.$t("won");
            case "D":
              return this.$t("draw");
          }
          break;
        case "FB":
          switch (value) {
            case "0":
              return this.$t("created");
            case "1":
              return this.$t("confirming");
            case "2":
              return this.$t("rejected");
            case "3":
              return this.$t("canceled");
            case "4":
              return this.$t("confirmed");
            case "5":
              return this.$t("closeSettle");
          }
          break;
        case "PP":
          switch (value) {
            case "I":
              return this.$t("progress");
            case "C":
              return this.$t("completed");
          }
          break;
      }
    },

    lookInfo(cont) {
      this.content = cont;
      this.dialogShow = true;
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


