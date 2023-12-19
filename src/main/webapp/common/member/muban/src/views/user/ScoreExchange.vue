<template>
  <div class="scoreExchange-page order-page">
    <div class="heard">
      <!-- 积分兑换 -->
      {{ $t("scoreExChange") }}
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("status") }}：
        <el-select v-model="type" size="small">
          <el-option
            v-for="item in typeArr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="item">
        <el-date-picker
          style="margin-left: 20px"
          v-model="date"
          type="datetimerange"
          :start-placeholder="startTime"
          :end-placeholder="endTime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
        <!-- 查询 -->
        <el-button
          type="primary"
          @click="searchMethod()"
          icon="el-icon-search"
          size="medium"
          style="margin-left: 15px"
          >{{ $t("searchText") }}</el-button
        >
        <!--积分兑换弹窗 -->
        <el-button
          type="danger"
          size="medium"
          style="margin-left: 15px"
          v-banned:click="dialogShowFlag"
          @click="dialogShowFlag()"
          >{{ $t("scoreExChange") }}</el-button
        >
      </div>
    </div>
    <div class="list">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column :label="$t('changeType')">
          <template slot-scope="scope">
            {{ typeTxt(scope.row.type) }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('beforeScore')"
        >
        <template slot-scope="scope">
          {{ scope.row.beforeScore | amount }}
        </template>
        </el-table-column>
        <el-table-column prop="score" :label="$t('scoreNum')"></el-table-column>
        <el-table-column
          :label="$t('afterScore')"
        >
          <template slot-scope="scope">
            {{ scope.row.afterScore | amount }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('scoreTime')">
          <template slot-scope="scope">
            {{ scope.row.createDatetimeStr }}
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          :label="$t('remarkText')"
        ></el-table-column>
        <div slot="empty">
          <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
      </el-table>
      <PageModule
        :total="total"
        :pageNumber.sync="pageNumber"
        @searchMethod="searchMethod"
      ></PageModule>
    </div>
    <!-- 积分兑换弹窗 -->
    <el-dialog
      :title="$t('scoreExChange')"
      :visible.sync="dialogShow"
      width="50%"
      :close-on-click-modal="false"
      class="exChange-dialog common_dialog"
    >
      <div class="dialog_cont" v-if="curData">
        <div class="item">{{ $t("money") }}：{{ data.money | amount }}</div>
        <div class="item">{{ $t("score") }}：{{ data.score }}</div>
        <div class="item">
          {{ $t("exchangeType") }}：
          <div style="display: inline-block">
            <el-radio v-model="radio" label="1" v-if="data.moneyToScore">{{
              $t("moneyToScore")
            }}</el-radio>
            <el-radio v-model="radio" label="2" v-if="data.scoreToMoney">{{
              $t("scoreToMoney")
            }}</el-radio>
          </div>
        </div>
        <div class="item">
          {{ $t("exchangeScale") }}：{{ curData.numerator }}:{{
            curData.denominator
          }}
        </div>
        <div class="item" style="color: red">
          {{ $t("exchangeMin") }}：{{ curData.minVal }}，{{
            $t("exchangeMax")
          }}：{{ curData.maxVal }}
        </div>
        <div class="item">
          {{ $t("exchangeNum") }}：<input
            type="number"
            v-model="exchangeNum"
            class="changeNum"
          />
        </div>
        <div class="item">{{ $t("exchangeToNum") }}：{{ change }}</div>
      </div>
      <div v-else>
        <i class="el-icon-setting"></i>
        <p>{{ $t("notSet") }}</p>
      </div>
      <span slot="footer" class="dialog-footer" v-if="curData">
        <button class="closeBtn themeBtn" @click="dialogShow = false">
          {{ $t("cancel") }}
        </button>
        <button class="sureBtn themeBtn" @click="sureBtn">
          {{ $t("confirm") }}
        </button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
//   import DateModule from '@/components/common/DateModule'
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      data: "", //积分兑换信息
      type: "",
      tableData: [], //积分兑换纪录
      dialogShow: false, //是否显示积分兑换弹窗
      radio: "1", //积分兑换类型
      exchangeNum: "", //兑换数量
      change: 0, //转换数量
      total: 0, //总条数
      pageNumber: 1, //当前页码
      curData: "", //当前积分数据
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
    };
  },
  computed: {
    ...mapState(["userInfo"]),
    typeArr: {
      get: function () {
        return [
          { value: "", label: this.$t("allText") },
          { value: 1, label: this.$t("typeActiveSub") },
          { value: 2, label: this.$t("memberSign") },
          { value: 3, label: this.$t("moneyToScore") },
          { value: 4, label: this.$t("scoreToMoney") },
          { value: 5, label: this.$t("depositPresented") },
          { value: 6, label: this.$t("peopleAdd") },
          { value: 7, label: this.$t("peopleSub") },
        ];
      },
      set: function () {},
    },
  },
  components: {
    //   DateModule,
    PageModule,
  },
  mounted() {
    // 积分兑换信息
    this.getScoreExchangeInfo();
    this.searchMethod();
  },
  methods: {
    //显示积分兑换弹窗
    dialogShowFlag() {
      this.dialogShow = true;
    },
    // 积分兑换信息
    getScoreExchangeInfo() {
      this.$API.getScoreExchangeInfo().then((res) => {
        if (res) {
          this.data = res;
          if (res.moneyToScore) {
            this.radio = "1";
            this.curData = this.data.moneyToScore;
          } else {
            this.radio = "2";
            this.curData = this.data.scoreToMoney;
          }
        }
      });
    },
    // 积分兑换记录
    searchMethod(isSearch) {
      // if (startTime) this.startTime = startTime;
      // if (endTime) this.endTime = endTime;
      if (isSearch) this.pageNumber = 1;
      //改变提交格式
      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        type: this.type,
      };
      params.load = true;

      this.$API.getScoreHisData(params).then((res) => {
        if (res) {
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },

    // 提交
    sureBtn() {
      //改变提交格式
      let params = {
        configId: this.curData.id,
        exchangeNum: this.exchangeNum,
      };
      this.$API.getConfirmExchange(params).then((res) => {
        if (res.success) {
          this.dialogShow = false;
          this.$notify({
            title: "success",
            message: res.msg,
            type: "success",
          });
          this.searchMethod(null, null, true);
          this.getScoreExchangeInfo();
        }
      });
    },

    // 变动类型转换
    typeTxt(type) {
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
    dataFun(val) {
      return DATE.dateChange(val);
    },
  },
  watch: {
    exchangeNum() {
      let str = String(this.exchangeNum);
      if (str.includes(".")) {
        this.exchangeNum = parseInt(this.exchangeNum);
      } else {
        this.change =
          (this.exchangeNum / Number(this.curData.numerator)) *
          Number(this.curData.denominator);
      }
    },
    dialogShow() {
      this.exchangeNum = "";
    },
    radio() {
      if (this.radio === "1" && this.data.moneyToScore) {
        this.curData = this.data.moneyToScore;
      } else {
        this.curData = this.data.scoreToMoney;
      }
      this.exchangeNum = "";
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

.scoreExchange-page {
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
  }
  .nodata {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px;
    font-size: 25px;
    p {
      margin-left: 8px;
    }
  }
  .exChange-dialog {
    .dialog_cont {
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
