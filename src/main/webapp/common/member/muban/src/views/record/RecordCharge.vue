<template>
  <div class="recordCharge-page order-page">
    <!-- 存取记录 -->
    <div class="heard">
      <div @click="tabIndex = '1'" class="item" :class="{ active: tabIndex == '1' }">
        {{ $t("orderPay") }}
      </div>
      <div @click="tabIndex = '2'" class="item" :class="{ active: tabIndex == '2' }">
        {{ $t("withdrawOrder") }}
      </div>
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("orderId") }}：
        <el-input v-model="orderId" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="type" size="small">
          <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
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
      </div>
      <div v-if="afterLoginOnOffBtn.canBePromo || afterLoginOnOffBtn.canBeRecommend
        ">
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
    </div>
    <template v-if="tabIndex === '1'">
      <div class="content">
        <div class="title">
          <p>{{ $t("orderId") }}</p>
          <p>{{ $t("userName") }}</p>
          <p>{{ $t("payMoney") }}</p>
          <p>{{ $t("payType") }}</p>
          <p>{{ $t("payStatus") }}</p>
          <p>{{ $t("payTime") }}</p>
          <p>{{ $t("remarkText") }}</p>
        </div>
        <div class="list">
          <div class="cont" v-for="(item, index) in data.rows" :key="index">
            <div class="cont-show">
              <p>{{ item.orderId }}</p>
              <p>{{ item.username }}</p>
              <p>{{ item.money | amount }}</p>
              <p>{{ item.fsText }}</p>
              <p>{{ item.statusText }}</p>
              <p>{{ item.createDatetime }}</p>
              <p>{{ item.remark }}</p>
            </div>
          </div>
        </div>
      </div>
    </template>
    <template v-if="tabIndex === '2'">
      <div class="content">
        <div class="title">
          <p>{{ $t("orderId") }}</p>
          <p>{{ $t("userName") }}</p>
          <p>{{ $t("applyMoney") }}</p>
          <p>{{ $t("withdrawalMoney") }}</p>
          <p>{{ $t("serverMoney") }}:</p>
          <p>{{ $t("withdrawalBank") }}:</p>
          <p>{{ $t("bankCardNo") }}:</p>
          <p>{{ $t("withdrawalStatus") }}:</p>
          <p>{{ $t("withdrawalTime") }}</p>

          <p>{{ $t("remarkText") }}:</p>
        </div>
        <div class="list">
          <div class="cont" v-for="(item, index) in data.rows" :key="index">
            <div class="cont-show">
              <p>{{ item.orderId }}</p>
              <p>{{ item.username }}</p>
              <p>{{ item.drawMoney }}</p>
              <p>{{ item.trueMoney }}</p>
              <p>{{ item.feeMoney }}</p>
              <p>{{ item.bankName }}</p>
              <p>{{ item.cardNo }}</p>
              <p>{{ item.statusText }}</p>
              <p>{{ item.createDatetime }}</p>

              <p>{{ item.remark }}</p>
            </div>
          </div>
        </div>
      </div>
    </template>
    <div v-if="!data.rows || (data.rows && !data.rows.length)">
      <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
    </div>
    <div class="statistics" v-show="tabIndex === '1'">
      <div class="line">
        {{ $t("totalText") }}：
        <div style="color: red">{{ data.total || 0 }}{{ $t("everyBi") }}</div>
        <div style="margin-left: 20px" v-if="data.aggsData">
          {{ $t("payMoney") }}：{{ data.aggsData.totalMoney || 0 | amount }}
        </div>
      </div>
    </div>
    <div class="statistics" v-show="tabIndex === '2'">
      <div class="line">
        {{ $t("totalText") }}：
        <div style="color: red">{{ data.total || 0 }}{{ $t("everyBi") }}</div>
        <template v-if="data.aggsData">
          <span style="margin-left: 20px">{{ $t("withdrawalMoney") }}：{{
            data.aggsData.totalMoney || 0 | amount
          }}</span>
          <div style="margin-left: 20px">
            {{ $t("serverMoney") }}：{{ data.aggsData.feeMoney || 0 | amount }}
          </div>
          <div style="margin-left: 20px">
            {{ $t("trueMoney") }}：{{ data.aggsData.trueMoney || 0 | amount }}
          </div>
        </template>
      </div>
    </div>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    <i style="margin-top: 15px">{{ $t("recordHint") }}</i>
  </div>
</template>

<script>
import { mapState } from "vuex";
import DATE from "@/assets/js/date.js";
import PageModule from "@/components/common/PageModule";

export default {
  data() {
    return {
      tabIndex: "1", //
      type: "",
      // typeArr: [{ value: '', label: this.$t("allStatus") }, { value: 1, label: this.$t("transfering") }, { value: 2, label: this.$t("successText") }, { value: 3, label: this.$t("errorText") }, { value: 4, label: this.$t("timeFailed") }],
      checked: false, //包含下级复选框
      orderId: "", //订单号
      username: "", //用户名
      data: "", //总数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
    };
  },
  computed: {
    ...mapState(["onOffBtn", "afterLoginOnOffBtn"]),
    typeArr() {
      return [
        { value: "", label: this.$t("allStatus") },
        { value: 1, label: this.$t("transfering") },
        { value: 2, label: this.$t("successText") },
        { value: 3, label: this.$t("errorText") },
        { value: 4, label: this.$t("timeFailed") },
      ];
    },
  },
  components: {
    PageModule,
  },
  mounted() {
    this.searchMethod();
  },
  methods: {
    // showContent(key) {
    //   if (this.key === key) this.key = -1;
    //   else this.key = key;
    // },
    // tabIndex为1，获取充值记录信息，tabIndex为2，获取提现记录信息，
    searchMethod(startTime, endTime, isSearch) {
      if (startTime) this.startTime = startTime;
      if (endTime) this.endTime = endTime;
      if (isSearch) this.pageNumber = 1;
      let url = "depositRecordList";
      if (this.tabIndex === "2") url = "withdrawRecordList";
      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        orderId: this.orderId,
        include: this.checked,
        status: this.type,
        username: this.username,
      };
      params.load = true;

      this.$API[url](params).then((res) => {
        if (res) {
          for (let i = 0; i < res.rows.length; i++) {
            switch (res.rows[i].depositType) {
              case 1: //在线充值
                res.rows[i].fsText = this.$t("depositType");
                break;
              case 2: //快速入款
                res.rows[i].fsText = this.$t("quickDeposit");
                break;
              case 3: //银行转账
                res.rows[i].fsText = this.$t("bankDeposit");
                break;
              case 4: //手动加款
                res.rows[i].fsText = this.$t("depositArtificial");
                break;
              default:
                break;
            }
            res.rows[i].statusText = this.statusFun(res.rows[i].status);
          }
          this.data = res;
          // this.tableData = res.rows
          this.total = res.total;
          // console.log(res, 'res')
        }
      });
    },
    statusFun(status) {
      if (status == 1) {
        return this.$t("untreated");
      } else if (status == 2) {
        return this.$t("success");
      } else if (status == 3) {
        return this.$t("failed");
      } else if (status == 4) {
        if (this.tabIndex === "1") return this.$t("canceled");
        else return this.$t("haveExpired");
      } else {
        return this.$t("haveExpired");
      }
    },
  },
  watch: {
    tabIndex() {
      this.pageNumber = 1;
      this.type = "";
      this.checked = false;
      this.orderId = "";
      this.username = "";
      this.key = -1;
      this.searchMethod();
    },
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

.recordCharge-page {
  .heard {
    display: flex;
    border-bottom: 1px solid $borderColorJoker;
    padding-bottom: 10px;

    .item {
      padding: 0 20px;
      color: $borderColorJoker;
      border: 1px solid $borderColorJoker;
      margin-right: 15px;
      line-height: 25px;
      border-radius: 5px;
      cursor: pointer;

      &.active {
        background: $titleBg;
        color: #fff;
        // background-color: $black;
      }
    }
  }
}
</style>


