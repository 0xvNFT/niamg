<template>
  <div class="userList-page order-page">
    <!-- 用户列表 -->
    <div class="heard">
      {{ $t("userList") }}
    </div>
    <div class="searchDiv">
      <div class="item" v-if="afterLoginOnOffBtn.canBePromo">
        {{ $t("level") }}：
        <el-select v-model="level" size="small">
          <el-option
            v-for="item in levelArr"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
      <div class="item">
        {{ $t("userName") }}：
        <el-input
          v-model="username"
          :placeholder="$t('pleaseInput') + $t('userName')"
          class="outInput"
          clearable
          size="small"
          spellcheck="false"
        ></el-input>
      </div>
      <div class="item">
        {{ $t("depositTotal") }}：
        <el-input
          v-model="depositTotal"
          v-amountFormat:depositTotal
          :placeholder="$t('pleaseInput') + $t('depositTotal')"
          class="outInput"
          clearable
          size="small"
        ></el-input>
      </div>
      <div class="item input">
        {{ $t("afterMoney") }}：
        <el-input
          v-model="minBalance"
          v-amountFormat:minBalance
          :placeholder="$t('pleaseInput')"
          class="outInput"
          clearable
          size="small"
        ></el-input>
        ~
        <el-input
          v-model="maxBalance"
          v-amountFormat:maxBalance
          :placeholder="$t('pleaseInput')"
          class="outInput"
          clearable
          size="small"
        ></el-input>
      </div>
      <!-- 选择是否包含下级 -->
      <div
        class="checkbox"
        v-if="
          afterLoginOnOffBtn.canBePromo || afterLoginOnOffBtn.canBeRecommend
        "
      >
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
      <div class="item">
        <el-date-picker
          v-model="date"
          type="datetimerange"
          :start-placeholder="startTime"
          :end-placeholder="endTime"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="margin-left: 10px"
        >
        </el-date-picker>
        <!-- 查询按钮 -->
        <el-button
          type="primary"
          size="medium"
          icon="el-icon-search"
          style="margin-left: 5px"
          @click="searchMethod"
          >{{ $t("searchText") }}</el-button
        >
        <!-- 重置按钮 -->
        <el-button
          type="primary"
          @click="reset()"
          icon="el-icon-setting"
          size="medium"
          style="margin-left: 5px"
          >{{ $t("reset") }}</el-button
        >
      </div>
    </div>
    <!-- table内容 -->
    <el-table :data="tableData" v-loading="tableLoading" style="width: 99%">
      <div slot="empty">
        <el-empty :image="require('@images/nodata')" :image-size="128" :description="$t('noData')"></el-empty>
      </div>
      <el-table-column
        prop="username"
        :label="$t('userName')"
      ></el-table-column>
      <el-table-column :label="$t('degreeName')">
        <template slot-scope="scope">
          <p>{{ scope.row.degreeName || "-" }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="level" :label="$t('userLevel')"></el-table-column>
      <el-table-column :label="$t('userType')">
        <template slot-scope="scope">
          {{ scope.row.type === 130 ? $t("member") : $t("proxy") }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createDatetime"
        :label="$t('registerTime')"
      ></el-table-column>
      <el-table-column
        prop="lastLoginDatetime"
        :label="$t('lastLoginDatetime')"
      ></el-table-column>
      <el-table-column :label="$t('onlineStatus')">
        <template slot-scope="scope">
          {{ scope.row.status === 2 ? $t("open") : $t("forbidden") }}
        </template>
      </el-table-column>
      <el-table-column prop="money" :label="$t('afterMoney')"></el-table-column>
      <el-table-column :label="$t('status')">
        <template slot-scope="scope">
          {{ scope.row.onlineStatus === 2 ? $t("online") : $t("offline") }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" :label="$t('operate')" width="110">
        <template slot-scope="scope">
          <router-link to="/viewTeam">
            <div class="operateLink">{{ $t("viewTeam") }}</div>
          </router-link>
          <!-- 代理返点设定 -->
          <div
            class="operateLink"
            @click="handleClick(scope.row, scope.row.type)"
            v-if="
              userInfo.userId !== scope.row.id && afterLoginOnOffBtn.isProxy
            "
          >
            {{ $t("setRebate") }}
          </div>
          <div class="operateLink" @click="goZB">
            {{ $t("goRecordChange") }}
          </div>
        </template>
      </el-table-column>
    </el-table>
    <PageModule
      :total="total"
      :pageNumber.sync="pageNumber"
      @searchMethod="searchMethod"
    ></PageModule>

    <!-- 返点设定弹窗 -->
    <el-dialog
      :title="$t('setRebate')"
      :visible.sync="dialogShow"
      width="40%"
      :close-on-click-modal="false"
      class="set-dialog common_dialog"
    >
      <div class="dialog_cont">
        <div class="item">
          <div class="line">
            {{ $t("userAccount") }}：{{ dialogData.username }}
          </div>
        </div>
        <template
          v-if="
            dialogData.type === 120 &&
            !fandianData.fixRebate &&
            fandianData.curRebate
          "
        >
          <div class="item">
            <div class="user">
              {{ $t("curLotRebate") }}：{{ fandianData.curRebate.lottery }}‰
            </div>
            <div class="other">
              {{ $t("lotRebate") }}：
              <el-select v-model="lottery" size="small">
                <el-option
                  v-for="item in fandianData.lotteryArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curLiveRebate") }}：{{ fandianData.curRebate.live }}‰
            </div>
            <div class="other">
              {{ $t("liveRebate") }}：
              <el-select v-model="live" size="small">
                <el-option
                  v-for="item in fandianData.liveArray"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curEgameRebate") }}：{{ fandianData.curRebate.egame }}‰
            </div>
            <div class="other">
              {{ $t("egameRebate") }}：
              <el-select v-model="egame" size="small">
                <el-option
                  v-for="item in fandianData.egameArray"
                  :key="item.value"
                  :label="item.value + '‰'"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curSportRebate") }}：{{ fandianData.curRebate.sport }}‰
            </div>
            <div class="other">
              {{ $t("sportRebate") }}：
              <el-select v-model="sport" size="small">
                <el-option
                  v-for="item in fandianData.sportArray"
                  :key="item.value"
                  :label="item.value + '‰'"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curChessRebate") }}：{{ fandianData.curRebate.chess }}‰
            </div>
            <div class="other">
              {{ $t("chessRebate") }}：
              <el-select v-model="chess" size="small">
                <el-option
                  v-for="item in fandianData.chessArray"
                  :key="item.value"
                  :label="item.value + '‰'"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curEsportRebate") }}：{{ fandianData.curRebate.esport }}‰
            </div>
            <div class="other">
              {{ $t("esportRebate") }}：
              <el-select v-model="esport" size="small">
                <el-option
                  v-for="item in fandianData.esportArray"
                  :key="item.value"
                  :label="item.value + '‰'"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
          <div class="item">
            <div class="user">
              {{ $t("curFishRebate") }}：{{ fandianData.curRebate.fishing }}‰
            </div>
            <div class="other">
              {{ $t("fishRebate") }}：
              <el-select v-model="fishing" size="small">
                <el-option
                  v-for="item in fandianData.fishingArray"
                  :key="item.value"
                  :label="item.value + '‰'"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </div>
          </div>
        </template>
        <template v-else>
          <div class="item">
            <div class="line">{{ $t("lotRebate") }}：{{ lottery }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("liveRebate") }}：{{ live }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("egameRebate") }}：{{ egame }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("sportRebate") }}：{{ sport }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("chessRebate") }}：{{ chess }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("esportRebate") }}：{{ esport }}‰</div>
          </div>
          <div class="item">
            <div class="line">{{ $t("fishRebate") }}：{{ fishing }}‰</div>
          </div>
        </template>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from "vuex";
// import DateModule from '@/components/common/DateModule'
import PageModule from "@/components/common/PageModule";
import DATE from "@/assets/js/date.js";

export default {
  data() {
    return {
      level: "",
      levelArr: [], //层级
      username: "", //用户名
      minBalance: "", //余额最小
      maxBalance: "", //余额最大
      depositTotal: "", //充值金额达到
      checked: true, //包含下级复选框
      tableData: [], //数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
      dialogShow: false, //是否显示返点设定弹窗
      dialogData: "", //当前选中的行信息
      lottery: "", //返点设定彩票
      live: "", //返点设定真人
      egame: "", //返点设定电子
      sport: "", //返点设定体育
      chess: "", //返点设定棋牌
      esport: "", //返点设定电竞
      fishing: "", //返点设定捕鱼
      fandianData: "", //返点信息
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      date: [], //日期
      tableLoading: false // 表格数据加载中
    };
  },
  computed: {
    ...mapState(["userInfo", "afterLoginOnOffBtn"]),
  },
  components: {
    // DateModule,
    PageModule,
  },
  created() {
    this.searchMethod();

    let _this = this;
    var timer = setInterval(() => {
      if (_this.afterLoginOnOffBtn) {
        clearInterval(timer);
        // 是代理获取层级
        if (_this.afterLoginOnOffBtn.isProxy) {
          // 获取层级
          _this.levelInfo();
        }
      }
    }, 300);
    this.$once("hook:beforeDestroy", function () {
      clearInterval(timer);
    });
  },
  methods: {
    // 获取层级
    levelInfo() {
      this.$API.levelInfo().then((res) => {
        if (res) {
          this.levelArr = [{ value: "", label: this.$t("allLevel") }];
          for (let i = 1; i <= res.lowestLevel; i++) {
            this.levelArr.push({ value: i, label: i + this.$t("levelShow") });
          }
        }
      });
    },
    // 记录
    searchMethod(startTime, endTime, isSearch) {
      this.tableLoading = true;
      if (isSearch) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pageNumber = 1;
      }
      //改变提交格式
      let params = {};
      params.startTime = this.date.length ? this.date[0] : this.startTime;
      params.endTime = this.date.length ? this.date[1] : this.endTime;
      params.pageNumber = this.pageNumber;
      params.username = this.username;
      params.minBalance = this.minBalance;
      params.maxBalance = this.maxBalance;
      params.depositTotal = this.depositTotal;
      params.level = this.level;
      params.include = this.checked;

      this.$API.userListData(params).then((res) => {
        if (res) {
          this.tableData = Object.freeze(res.rows);
          this.total = res.total;
        }
      }).finally(() => {
        this.tableLoading = false;
      });
    },
    // 重置表单
    reset() {
      this.$publicJs.resetForm.call(this, "levelArr");
    },
    handleClick(data, type) {
      this.dialogShow = true;
      this.dialogData = data;
      // 被访问的用户是代理 返点设定接口
      if (type === 120) {
        //改变提交格式
        let params = {};
        params.userId = this.dialogData.id;

        this.$API.kickbackResetInfo( params ).then((res) => {
          if (res) {
            this.fandianData = res;
            // 当前用户返点信息
            let arr = res.userRebate;
            this.lottery = arr.lottery;
            this.live = arr.live;
            this.egame = arr.egame;
            this.sport = arr.sport;
            this.chess = arr.chess;
            this.esport = arr.esport;
            this.fishing = arr.fishing;
          }
        });
      } else {
        // 被访问的用户是会员 返点设定接口
        //改变提交格式
        let params = {};
        params.userId = this.dialogData.id;
        params.load = true;
        this.$API.kickbackInfoForMember(params).then((res) => {
          if (res) {
            // 当前用户返点信息
            let arr = res.rebate;
            this.lottery = arr.lottery;
            this.live = arr.live;
            this.egame = arr.egame;
            this.sport = arr.sport;
            this.chess = arr.chess;
            this.esport = arr.esport;
            this.fishing = arr.fishing;
          }
        });
      }
    },
    sureBtn() {
      //改变提交格式
      let params = {};
      params.userId = this.dialogData.id;
      params.live = this.live;
      params.egame = this.egame;
      params.chess = this.chess;
      params.sport = this.sport;
      params.esport = this.esport;
      params.fishing = this.fishing;
      params.lot = this.lottery;
      params.load = true;
      this.$API.kickbackResetSave(params).then((res) => {
        if (res) {
          this.dialogShow = false;
          this.$notify({
            title: "success",
            message: res.msg,
            type: "success",
          });
        }
      });
    },
    goZB() {
      this.$bus.$emit("openBigMenu", "4");
      this.$router.push("/recordChange");
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

.userList-page {
  .operateLink {
    color: $blue;
    font-size: 12px;
    cursor: pointer;
  }
   .operateLink:hover{
    color: rgb(241, 195, 143);
   }
  .set-dialog {
    .dialog_cont {
      width: fit-content;
      margin: 0 auto;
    }

    .item {
      .user {
        margin-right: 20px;
        width: 130px;
        text-align: left;
      }
    }
  }
}

.checkbox {
  margin-right: 15px;
  margin-bottom: 10px;
  line-height: 45px;
}
</style>


