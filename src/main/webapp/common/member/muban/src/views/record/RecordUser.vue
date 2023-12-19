<template>
  <div class="recordUser-page order-page">
    <div class="heard">
      <!-- 个人报表 -->
      <p>{{ $t("recordUser") }}</p>
    </div>
    <div class="searchDiv">
      <div class="item">
        {{ $t("userName") }}： 
        <el-input v-model="username" :placeholder="$t('pleaseInput') + $t('userName')" class="outInput" clearable
          size="small"></el-input>
      </div>

      <div class="item">
        <el-date-picker v-model="date" type="datetimerange" :start-placeholder="startTime" :end-placeholder="endTime"
          :picker-options="pickerValidate('onlyAllowSearchInMonth')" value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
        <!-- 查询 -->
        <el-button type="primary" @click="searchMethod()" icon="el-icon-search" size="medium" style="margin-left: 15px">{{
          $t("searchText") }}</el-button>
        <!-- 重置 -->
        <el-button type="primary" @click="reset()" icon="el-icon-setting" size="medium" style="margin-left: 10px">{{
          $t("reset") }}</el-button>
      </div>
      <div v-if="afterLoginOnOffBtn.canBePromo || afterLoginOnOffBtn.canBeRecommend
        ">
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
    </div>
    <div class="content" v-loading="tableLoading">
      <div class="title">
        <p>{{ $t("userName") }}</p>
        <p>{{ $t("dataText") }}</p>
        <p>{{ $t("payText") }}</p>
        <p>{{ $t("withdrawal") }}</p>
        <p>{{ $t("seeMore") }}</p>
      </div>
      <div class="list">
        <div class="cont" v-for="(item, index) in tableData" :key="index" @click="showContent(index)">
          <div class="cont-show">
            <p>{{ item.username }}</p>
            <p>{{ item.statDate }}</p>
            <p>{{ item.depositAmount | amount }}</p>
            <p>{{ item.withdrawAmount | amount }}</p>
            <p> <i class="el-icon-arrow-down" :class="key === index ? 'open' : ''"></i></p>
          </div>
          <div class="cont-hide" :class="{ showAllCont: key === index }">
            <!-- <div>
              <p>{{$t("userName")}}：</p>
              <p>{{item.username}}</p>
            </div>
            <div>
              <p>{{$t("dataText")}}：</p>
              <p>{{item.statDate}}</p>
            </div>
  
            <div>
              <p>{{$t("payText")}}：</p>
              <p>{{item.depositAmount}}</p>
            </div>
            <div>
              <p>{{$t("withdrawal")}}：</p>
              <p>{{item.withdrawAmount}}</p>
            </div> -->
            <div>
              <p>{{ $t("lotBetAmount") }}：</p>
              <span>{{ item.lotBetAmount }}</span>
            </div>
            <div>
              <p>{{ $t("lotWinAmount") }}：</p>
              <span>{{ item.lotWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("liveBetAmount") }}：</p>
              <span>{{ item.liveBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("liveWinAmount") }}：</p>
              <span>{{ item.liveWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("egameBetAmount") }}：</p>
              <span>{{ item.egameBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("egameWinAmount") }}：</p>
              <span>{{ item.egameWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("sportBetAmount") }}：</p>
              <span>{{ item.sportBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("sportWinAmount") }}：</p>
              <span>{{ item.sportWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("chessBetAmount") }}：</p>
              <span>{{ item.chessBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("chessWinAmount") }}：</p>
              <span>{{ item.chessWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("esportBetAmount") }}：</p>
              <span>{{ item.esportBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("esportWinAmount") }}：</p>
              <span>{{ item.esportWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("fishingBetAmount") }}：</p>
              <span>{{ item.fishingBetAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("fishingWinAmount") }}：</p>
              <span>{{ item.fishingWinAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("proxyRebateAmount") }}：</p>
              <span>{{ item.proxyRebateAmount | amount }}</span>
            </div>
            <div>
              <p>{{ $t("activeAwardAmount") }}：</p>
              <span>{{ item.activeAwardAmount | amount }}</span>
            </div>
          </div>
        </div>
      </div>
      <div v-if="!tableData.length">
        <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
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
  name: "recordUser",
  data() {
    return {
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      checked: true, //包含下级复选框
      username: "", //用户名
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
      key: -1, //要显示的完整内容的key值
      tableLoading: false // 表格数据加载中
    };
  },
  computed: {
    ...mapState(["onOffBtn", "afterLoginOnOffBtn"]),
  },
  components: {
    PageModule,
  },
  mounted() {
    this.searchMethod();
  },
  methods: {
    showContent(key) {
      if (this.key === key) this.key = -1;
      else this.key = key;
    },
    // 个人报表
    searchMethod(isSearch) {
      this.tableLoading = true;
      // if (startTime) this.startTime = startTime
      // if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1;

      //改变提交格式
      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        include: this.checked,
        username: this.username,
      };
      // 发送请求
      this.$API.personReport(params).then((res) => {
        // 请求成功且有数据返回
        if (res.rows) {
          this.tableData = Object.freeze(res.rows);
          this.total = res.total;
        } else if (res.success === false) {
          // success === false 等于查询异常
          this.tableData = [];
          this.total = 0;
        }
      }).finally(() => {
        this.tableLoading = false;
      });
    },
    // 重置表单（需要引入this指向）
    reset() {
      this.$publicJs.resetForm.call(this);
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
.el-input__inner:first-child {
  margin-top: -3px;
}

.cont-hide {
  height: 0;
  overflow: hidden;
  transition: height 0.5s;
}

.showAllCont {
  height: 256px;

  div p:first-child {
    text-align: right;
  }
}

.cont-hide div {

  // justify-content: space-between;
  p {
    width: 30%;
    text-align: center;
  }

  p:first-child {
    text-align: right;
  }
}

@import "../../assets/css/order.scss";
</style>