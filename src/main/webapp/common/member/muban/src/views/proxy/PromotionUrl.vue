<template>
  <el-tabs type="border-card" class="promotionUrl-page" v-model="tabIndex">
    <el-tab-pane :label="$t('promoUrl')" name="1">
      <el-card class="selectDiv">
        <div class="item">
          {{ $t("domain") }}：
          <el-select v-model="domain" size="small">
            <el-option v-for="item in domainArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="item">
          {{ $t("userType") }}：
          <el-select v-model="type" size="small">
            <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="item">
          {{ $t("linkPage") }}：
          <el-select v-model="link" size="small">
            <el-option v-for="item in linkArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
      </el-card>
      <el-card class="list" v-if="onOffBtn.game && data">
        <template v-if="data.fixRebate">
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{ $t("nowLotRebateAmount") }}：{{ data.sr.lottery }}
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{ $t("nowLiveRebateAmount") }}：{{ data.sr.live }}
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ $t("nowEgameRebateAmount") }}：{{ data.sr.egame }}
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{ $t("nowSportRebateAmount") }}：{{ data.sr.sport }}
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{ $t("nowChessRebateAmount") }}：{{ data.sr.chess }}
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ $t("nowEsportRebateAmount") }}：{{ data.sr.esport }}
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ $t("nowFishingRebateAmount") }}：{{ data.sr.fishing }}
          </div>
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{ $t("createLink") }}</el-button>
          </div>
        </template>
        <template v-else>
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{ $t("setLotRebateAmount") }}：
            <el-select v-model="lottery" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.lotteryArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{ $t("setLiveRebateAmount") }}：
            <el-select v-model="live" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.liveArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ $t("setEgameRebateAmount") }}：
            <el-select v-model="egame" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.egameArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{ $t("setSportRebateAmount") }}：
            <el-select v-model="sport" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.sportArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{ $t("setChessRebateAmount") }}：
            <el-select v-model="chess" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.chessArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ $t("setEsportRebateAmount") }}：
            <el-select v-model="esport" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.esportArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ $t("setFishingRebateAmount") }}：
            <el-select v-model="fishing" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.fishingArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <!-- <div class="item">{{ $t("promotionUrlHint") }}</div> -->
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{ $t("createLink") }}</el-button>
          </div>
        </template>
      </el-card>
    </el-tab-pane>
    <el-tab-pane :label="$t('promotion')" name="2">
      <el-table :data="tableData" style="width: 99%">
        <el-table-column :label="$t('promotionLink')" width="220">
          <template slot-scope="scope">
            <p>{{ $t("commonLink") }}：{{ scope.row.linkUrl }}</p>
            <p>{{ $t("encryptLink") }}：{{ scope.row.linkUrlEn }}</p>
          </template>
        </el-table-column>
        <el-table-column :label="$t('visitPage')">
          <template slot-scope="scope">
            {{ scope.row.accessPage === 1 ? $t("registerPage") : scope.row.accessPage === 2 ? $t("indexText") :
              $t("active") }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('codeMode')">
          <template slot-scope="scope">
            {{ scope.row.mode === 1 ? $t("modeAgent") : $t("modeMember") }}
          </template>
        </el-table-column>
        <el-table-column prop="accessNum" :label="$t('accessNum')"></el-table-column>
        <el-table-column prop="registerNum" :label="$t('registerNum')"></el-table-column>
        <el-table-column :label="$t('openType')">
          <template slot-scope="scope">
            {{ scope.row.type === 130 ? $t('member') : $t('proxy') }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('rebateAmount')" width="150">
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
        <el-table-column prop="createTime" :label="$t('createTime')" width="120"></el-table-column>
        <el-table-column fixed="right" :label="$t('operate')" width="100">
          <template slot-scope="scope">
            <el-button @click="delBtn(scope.row)" type="text" size="small">{{ $t("delete") }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      tabIndex: '1',//
      // 推广链接
      domain: '',
      type: '',
      link: 1,

      data: '',
      lottery: '',//
      live: '',//
      egame: '',//
      sport: '',//
      chess: '',//
      esport: '',//
      fishing: '',//
      // 推广管理
      tableData: [],//列表数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
    };
  },
  computed: {
    ...mapState(['onOffBtn']),
    linkArr () {
      return [{ value: 1, label: this.$t("register") }, { value: 2, label: this.$t("indexText") }, { value: 3, label: this.$t("active") }]
    },
    domainArr () {
      return [{ value: '', label: this.$t("homeStation") }]
    },
    typeArr: {
      get: function () {
        return [];
      },
      set: function () {

      }
    },
  },
  components: {
    PageModule
  },
  mounted () {
    this.agentRegPromotionInfo()
    this.$bus.$on('upData', () => {
      // this.agentRegPromotionInfo()
    })
  },
  methods: {

    agentRegPromotionInfo () {
      this.$API.agentRegPromotionInfo({ load: true }).then(res => {
        if (res) {
          if (res) {
            this.typeArr = []
            if (res.proxyModel === 1 || res.proxyModel === 2) {
              this.typeArr.push({ value: 120, label: this.$t("proxy") })
            }
            if (res.proxyModel === 2 || res.proxyModel === 3) {
              this.typeArr.push({ value: 130, label: this.$t("member") })
            }
            if (this.typeArr.length) this.type = this.typeArr[0].value
            this.data = res
          }
        }
      });

    },
    sureBtn () {
      //改变提交格式
      let params = {};
      params.egame = this.egame
      params.chess = this.chess
      params.sport = this.sport
      params.esport = this.esport
      params.fishing = this.fishing
      params.live = this.live
      params.lottery = this.lottery
      params.accessPage = this.link
      params.type = this.type
      params.load = true

      this.$API.agentRegPromotionSave(params).then(res => {
        if (res) {
          this.$notify({
            title: 'success',
            message: res.msg,
            type: 'success'
          });
          this.egame = ''
          this.chess = ''
          this.sport = ''
          this.esport = ''
          this.fishing = ''
          this.live = ''
          this.lottery = ''
        }
      });

    },
    searchMethod (isDel) {
      // if (isDel) load = null
      //改变提交格式
      let params = {};
      params.pageNumber = this.pageNumber
      params.load = true
      this.$API.agentRegPromotionList(params).then(res => {
        if (res) {
          this.tableData = res.rows
          this.total = res.total
        }
      });
    },
    delBtn (val) {
      this.$confirm(this.$t("sureDelete"), this.$t("hintText"), {
        confirmButtonClass: "smallSureBtn themeBtn",
        cancelButtonClass: "smallCloseBtn themeBtn",
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: 'warning'
      }).then(() => {
        //改变提交格式
        let params = {};
        params.id = val.id
        params.load = true
        this.$API.agentRegPromotionDelete(params).then(res => {
          if (res) {
            this.searchMethod(true)
            this.$message.success(res.msg);
          }
        });
      }).catch(() => {
      });
    }
  },
  watch: {
    tabIndex () {
      if (this.tabIndex === '2') {
        this.pageNumber = 1
        this.searchMethod()
      }
    },
    // onOffBtn () {
    //   console.log(222)
    //   this.agentRegPromotionInfo()
    // }
  }
};
</script>

<style lang="scss">
.promotionUrl-page {
  background: $black;

  .selectDiv {
    color: $colorText;
    margin-bottom: 10px;

    .el-card__body {
      display: flex;

      .item {
        margin-right: 20px;
      }
    }
  }

  .el-button {
    padding: 10px;
    color: #fff;
  }

  .list {
    .item {
      width: fit-content;
      margin: 15px auto 0;
    }
  }

  .el-card {
    background: $black;

    .el-card__body {
      color: $colorText;
    }
  }

  .el-tabs__header {
    background: $black;

    .el-tabs__nav-wrap {
      .el-tabs__nav-scroll {
        background-color: $black;
        border-bottom: 1px solid $borderColorJoker;

        .is-active {
          background-color: $bgColor;
          color: #ffffff;
          border-right-color: $borderColorJoker;
          border-left-color: $borderColorJoker;
        }
      }
    }
  }
}
</style>


