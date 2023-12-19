<template>
  <el-tabs type="border-card" class="promotionUrl-page" v-model="tabIndex">
    <el-tab-pane :label="$t('promoUrl')" name="1">
      <el-card class="selectDiv">
        <div class="item">
          {{ $t("domain") }}：
          <el-select v-model="domain" size="small">
            <el-option
              v-for="item in domainArr"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div class="item">
          {{ $t("userType") }}：
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
          {{ $t("linkPage") }}：
          <el-select v-model="link" size="small">
            <el-option
              v-for="item in linkArr"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
      </el-card>
      <el-card class="list" v-if="onOffBtn.game && data">
        <template v-if="data.fixRebate">
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{  configJsFlag("nowLotRebateAmount")  }}：{{
              onOffBtn.lang === "br" ? (data.sr.lottery / 10)+'%' : data.sr.lottery
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{  configJsFlag("nowLiveRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.live / 10)+'%' : data.sr.live
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ configJsFlag("nowEgameRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.egame / 10)+'%' : data.sr.egame
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{configJsFlag("nowSportRebateAmount") }}：{{
              onOffBtn.lang === "br" ? (data.sr.sport / 10) +'%': data.sr.sport
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{configJsFlag("nowChessRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.chess / 10)+'%' : data.sr.chess
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ configJsFlag("nowEsportRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.esport / 10)+'%' : data.sr.esport
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ configJsFlag("nowFishingRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.fishing / 10)+'%' : data.sr.fishing
            }}
          </div>
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{
              $t("createLink")
            }}</el-button>
          </div>
        </template>
        <template v-else>
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{ $t("setLotRebateAmount") }}：
            <el-select
              v-model="lottery"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.lotteryArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{ $t("setLiveRebateAmount") }}：
            <el-select
              v-model="live"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.liveArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ $t("setEgameRebateAmount") }}：
            <el-select
              v-model="egame"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.egameArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{ $t("setSportRebateAmount") }}：
            <el-select
              v-model="sport"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.sportArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{ $t("setChessRebateAmount") }}：
            <el-select
              v-model="chess"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.chessArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ $t("setEsportRebateAmount") }}：
            <el-select
              v-model="esport"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.esportArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ $t("setFishingRebateAmount") }}：
            <el-select
              v-model="fishing"
              size="small"
              :placeholder="$t('changeBonus')"
            >
              <el-option
                v-for="item in data.fishingArray"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <!-- <div class="item">{{ $t("promotionUrlHint") }}</div> -->
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{
              $t("createLink")
            }}</el-button>
          </div>
        </template>
      </el-card>
    </el-tab-pane>
    <el-tab-pane :label="$t('promotion')" name="2">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column :label="$t('promotionLink')" >
          <template slot-scope="scope">
            <p>{{ $t("commonLink") }}：{{ scope.row.linkUrl }}</p>
            <p>{{ $t("encryptLink") }}：{{ scope.row.linkUrlEn }}</p>
          </template>
        </el-table-column>
        <el-table-column :label="$t('visitPage')">
          <template slot-scope="scope">
            {{
              scope.row.accessPage === 1
                ? $t("registerPage")
                : scope.row.accessPage === 2
                ? $t("indexText")
                : $t("active")
            }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('codeMode')">
          <template slot-scope="scope">
            {{ scope.row.mode === 1 ? $t("modeAgent") : $t("modeMember") }}
          </template>
        </el-table-column>
        <el-table-column
          prop="accessNum"
          :label="$t('accessNum')"
          width="200px"
        ></el-table-column>
        <el-table-column
          prop="registerNum"
          :label="$t('registerNum')"
          width="200px"
        ></el-table-column>
        <el-table-column :label="$t('openType')" >
          <template slot-scope="scope">
            {{ scope.row.type === 130 ? $t("member") : $t("proxy") }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('rebateAmount')"  width="210px">
          <template slot-scope="scope">
            <p>{{ $t("lotRebateAmount") }}：{{ scope.row.lottery }}‰</p>
            <p>{{ $t("egameRebateAmount") }}：{{ scope.row.egame }}‰</p>
            <p>{{ $t("sportRebateAmount") }}：{{ scope.row.sport }}‰</p>
            <p>{{ $t("chessRebateAmount") }}：{{ scope.row.chess }}‰</p>
            <p>{{ $t("liveRebateAmount") }}：{{ scope.row.live }}‰</p>
            <p>{{ $t("esportRebateAmount") }}：{{ scope.row.esport }}‰</p>
            <p>{{ $t("fishingRebateAmount") }}：{{ scope.row.fishing }}‰</p>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('createTime')"
        ></el-table-column>
        <el-table-column fixed="right" :label="$t('operate')" width="100">
          <template slot-scope="scope">
            <el-button @click="delBtn(scope.row)" type="text" size="small">{{
              $t("delete")
            }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <PageModule
        :total="total"
        :pageNumber.sync="pageNumber"
        @searchMethod="searchMethod"
      ></PageModule>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapState } from "vuex";
import PageModule from "@/components/common/PageModule";

export default {
  data() {
    return {
      tabIndex: "1", //
      // 推广链接
      domain: "",
      domainArr: [{ value: "", label: this.$t("homeStation") }],
      type: "",
      typeArr: [],
      link: 1,
      linkArr: [
        { value: 1, label: this.$t("register") },
        { value: 2, label: this.$t("indexText") },
        { value: 3, label: this.$t("active") },
      ],
      data: "",
      lottery: "", //
      live: "", //
      egame: "", //
      sport: "", //
      chess: "", //
      esport: "", //
      fishing: "", //
      // 推广管理
      tableData: [], //列表数据
      total: 0, //总条数
      pageNumber: 1, //当前页码
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {
    PageModule,
  },
  mounted() {
    this.agentRegPromotionInfo();
  },
  methods: {
    agentRegPromotionInfo() {
      this.$axiosPack
        .post("/userCenter/agentManage/agentRegPromotionInfo.do", {
          load: [true, 200, null],
        })
        .then((res) => {
          if (res) {
            if (res.data.proxyModel === 1 || res.data.proxyModel === 2) {
              this.typeArr.push({ value: 120, label: this.$t("proxy") });
            }
            if (res.data.proxyModel === 2 || res.data.proxyModel === 3) {
              this.typeArr.push({ value: 130, label: this.$t("member") });
            }
            if (this.typeArr.length) this.type = this.typeArr[0].value;
            this.data = res.data;
          }
        });
    },
    sureBtn() {
      //改变提交格式
      let params = new URLSearchParams();
      params.append("egame", this.egame);
      params.append("chess", this.chess);
      params.append("sport", this.sport);
      params.append("esport", this.esport);
      params.append("fishing", this.fishing);
      params.append("live", this.live);
      params.append("lottery", this.lottery);
      params.append("accessPage", this.link);
      params.append("type", this.type);
      this.$axiosPack
        .post("/userCenter/agentManage/agentRegPromotion/save.do", {
          params: params,
          load: [true, 200, null],
        })
        .then((res) => {
          if (res) {
            this.$notify({
              title: "success",
              message: res.data.msg,
              type: "success",
            });
            this.egame = "";
            this.chess = "";
            this.sport = "";
            this.esport = "";
            this.fishing = "";
            this.live = "";
            this.lottery = "";
          }
        });
    },
    searchMethod(isDel) {
      let load = [true, 200, null];
      if (isDel) load = null;
      //改变提交格式
      let params = new URLSearchParams();
      params.append("pageNumber", this.pageNumber);
      this.$axiosPack
        .post("/userCenter/agentManage/agentRegPromotion/list.do", {
          params: params,
          load: load,
        })
        .then((res) => {
          if (res) {
            this.tableData = res.data.rows;
            this.total = res.data.total;
          }
        });
    },
    delBtn(val) {
      this.$confirm(this.$t("sureDelete"), this.$t("hintText"), {
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: "warning",
      })
        .then(() => {
          //改变提交格式
          let params = new URLSearchParams();
          params.append("id", val.id);
          this.$axiosPack
            .post("/userCenter/agentManage/agentRegPromotion/delete.do", {
              params: params,
              load: [true, 200, this.$t("deletion")],
            })
            .then((res) => {
              if (res) {
                this.$message({
                  type: "success",
                  message: res.data.msg,
                });
                this.searchMethod(true);
              }
            });
        })
        .catch(() => {});
    },
  },
  watch: {
    tabIndex() {
      if (this.tabIndex === "2") {
        this.pageNumber = 1;
        this.searchMethod();
      }
    },
  },
};
</script>

<style lang="scss">
.promotionUrl-page {
  .selectDiv {
    margin-bottom: 10px;

    .el-card__body {
      display: flex;

      .item {
        margin-right: 20px;
      }
    }
  }

  .list {
    .item {
      width: fit-content;
      margin: 15px auto 0;
    }
  }
}
</style>
