<template>
  <div class="userList-page">
    <div class="searchDiv">
      <div class="item" v-if="onOffBtn.canBePromo">
        {{ $t("level") }}：
        <el-select v-model="level" size="small">
          <el-option v-for="item in levelArr" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </div>
      <div class="item">
        {{ $t("userName") }}：
        <el-input v-model="username" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("depositTotal") }}：
        <el-input v-model="depositTotal" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item">
        {{ $t("afterMoney") }}：
        <el-input v-model="minBalance" class="outInput" clearable size="small"></el-input>
        {{ $t("depositRange") }}
        <el-input v-model="maxBalance" class="outInput" clearable size="small"></el-input>
      </div>
      <div class="item" v-if="onOffBtn.canBePromo || onOffBtn.canBeRecommend">
        <el-checkbox v-model="checked">{{ $t("includeJunior") }}</el-checkbox>
      </div>
      <DateModule :fastBtn="true" @searchMethod="searchMethod" :showClear="true"></DateModule>
    </div>
    <el-table :data="tableData" style="width: 99%">
      <el-table-column prop="username" :label="$t('userName')"></el-table-column>
      <el-table-column :label="$t('degreeName')">
        <template slot-scope="scope">
          <p>{{ scope.row.degreeName || '-' }}</p>
        </template>
      </el-table-column>
      <el-table-column prop="level" :label="$t('userLevel')"></el-table-column>
      <el-table-column :label="$t('userType')">
        <template slot-scope="scope">
          {{ scope.row.type === 130 ? $t('member') : $t('proxy') }}
        </template>
      </el-table-column>
      <el-table-column prop="createDatetime" :label="$t('registerTime')"></el-table-column>
      <el-table-column prop="lastLoginDatetime" :label="$t('lastLoginDatetime')"></el-table-column>
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
      <el-table-column fixed="right" :label="$t('operate')" width="100">
        <template slot-scope="scope">
          <router-link to="/viewTeam">
            <div class="operateLink">{{ $t("viewTeam") }}</div>
          </router-link>
          <!-- 代理返点设定 -->
          <div class="operateLink" @click="handleClick(scope.row, scope.row.type)"
            v-if="userInfo.userId !== scope.row.id && onOffBtn.isProxy">{{ $t("setRebate") }}</div>
          <div class="operateLink" @click="goZB">{{ $t("goRecordChange") }}</div>
        </template>
      </el-table-column>
    </el-table>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>

    <!-- 返点设定弹窗 -->
    <el-dialog :title="$t('setRebate')" :visible.sync="dialogShow" width="40%" :close-on-click-modal="false"
      class="set-dialog">
      <div class="cont">
        <p>{{ $t("userAccount") }}：{{ dialogData.username }}</p>
        <template v-if="dialogData.type === 120">
          <template v-if="!fandianData.fixRebate && fandianData.curRebate">
            <div class="item">
              <div class="user">
                {{ $t("curLotRebate") }}：{{ fandianData.curRebate.lottery }}‰
              </div>
              <div class="other">
                {{ $t("lotRebate") }}：
                <el-select v-model="lottery" size="small">
                  <el-option v-for="item in fandianData.lotteryArray" :key="item.value" :label="item.label"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.liveArray" :key="item.value" :label="item.label"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.egameArray" :key="item.value" :label="item.value + '‰'"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.sportArray" :key="item.value" :label="item.value + '‰'"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.chessArray" :key="item.value" :label="item.value + '‰'"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.esportArray" :key="item.value" :label="item.value + '‰'"
                    :value="item.value">
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
                  <el-option v-for="item in fandianData.fishingArray" :key="item.value" :label="item.value + '‰'"
                    :value="item.value">
                  </el-option>
                </el-select>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="item">{{ $t("lotRebate") }}：{{ lottery }}‰</div>
            <div class="item">{{ $t("liveRebate") }}：{{ live }}‰</div>
            <div class="item">{{ $t("egameRebate") }}：{{ egame }}‰</div>
            <div class="item">{{ $t("sportRebate") }}：{{ sport }}‰</div>
            <div class="item">{{ $t("chessRebate") }}：{{ chess }}‰</div>
            <div class="item">{{ $t("esportRebate") }}：{{ esport }}‰</div>
            <div class="item">{{ $t("fishRebate") }}：{{ fishing }}‰</div>
          </template>
        </template>
        <template v-else>
          <div class="item">{{ $t("lotRebate") }}：{{ lottery }}‰</div>
          <div class="item">{{ $t("liveRebate") }}：{{ live }}‰</div>
          <div class="item">{{ $t("egameRebate") }}：{{ egame }}‰</div>
          <div class="item">{{ $t("sportRebate") }}：{{ sport }}‰</div>
          <div class="item">{{ $t("chessRebate") }}：{{ chess }}‰</div>
          <div class="item">{{ $t("esportRebate") }}：{{ esport }}‰</div>
          <div class="item">{{ $t("fishRebate") }}：{{ fishing }}‰</div>
        </template>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn">{{ $t("confirm") }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data() {
    return {
      level: '',
      levelArr: [],//层级
      username: '',//用户名
      minBalance: '',//余额最小
      maxBalance: '',//余额最大
      depositTotal: '',//充值金额达到
      checked: true,//包含下级复选框
      tableData: [],//数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
      dialogShow: false,//是否显示返点设定弹窗
      dialogData: '',//当前选中的行信息
      lottery: '',//返点设定彩票
      live: '',//返点设定真人
      egame: '',//返点设定电子
      sport: '',//返点设定体育
      chess: '',//返点设定棋牌
      esport: '',//返点设定电竞
      fishing: '',//返点设定捕鱼
      fandianData: '',//返点信息
      startTime: '',//搜索开始时间
      endTime: '',//搜索结束时间
    };
  },
  computed: {
    ...mapState(['userInfo', 'onOffBtn'])
  },
  components: {
    DateModule,
    PageModule
  },
  created() {
    this.searchMethod()

    let _this = this
    var timer = setInterval(() => {
      if (_this.onOffBtn) {
        clearInterval(timer);
        // 是代理获取层级
        if (_this.onOffBtn.isProxy) {
          // 获取层级
          _this.levelInfo();
        }
      }
    }, 300);
    this.$once('hook:beforeDestroy', function () {
      clearInterval(timer);
    });
  },
  methods: {
    // 记录
    levelInfo() {
      this.$axiosPack.post("/userCenter/agentManage/levelInfo.do").then(res => {
        if (res) {
          this.levelArr = [{ value: '', label: this.$t("allLevel") }]
          for (let i = 1; i <= res.data.lowestLevel; i++) {
            this.levelArr.push({ value: i, label: i + this.$t("levelShow") })
          }
        }
      });
    },
    // 记录
    searchMethod(startTime, endTime, isSearch) {
      if (isSearch) {
        this.startTime = startTime
        this.endTime = endTime
        this.pageNumber = 1
      }

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('username', this.username);
      params.append('minBalance', this.minBalance);
      params.append('maxBalance', this.maxBalance);
      params.append('depositTotal', this.depositTotal);
      params.append('level', this.level);
      params.append('include', this.checked);
      this.$axiosPack.post("/userCenter/agentManage/userListData.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },

    handleClick(data, type) {
      this.dialogShow = true
      this.dialogData = data
      // 被访问的用户是代理 返点设定接口
      if (type === 120) {
        //改变提交格式
        let params = new URLSearchParams();
        params.append('userId', this.dialogData.id);
        this.$axiosPack.post("/userCenter/agentManage/kickbackResetInfo.do", { params: params, load: [true, 200, null] }).then(res => {
          if (res) {
            this.fandianData = res.data
            // 当前用户返点信息
            let arr = res.data.userRebate
            this.lottery = arr.lottery
            this.live = arr.live
            this.egame = arr.egame
            this.sport = arr.sport
            this.chess = arr.chess
            this.esport = arr.esport
            this.fishing = arr.fishing
          }
        });
      } else {
        // 被访问的用户是会员 返点设定接口
        //改变提交格式
        let params = new URLSearchParams();
        params.append('userId', this.dialogData.id);
        this.$axiosPack.post("/userCenter/agentManage/kickbackInfoForMember.do", { params: params, load: [true, 200, null] }).then(res => {
          if (res) {
            // 当前用户返点信息
            let arr = res.data.rebate
            this.lottery = arr.lottery
            this.live = arr.live
            this.egame = arr.egame
            this.sport = arr.sport
            this.chess = arr.chess
            this.esport = arr.esport
            this.fishing = arr.fishing
          }
        });
      }
    },
    sureBtn() {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('userId', this.dialogData.id);
      params.append('live', this.live);
      params.append('egame', this.egame);
      params.append('chess', this.chess);
      params.append('sport', this.sport);
      params.append('esport', this.esport);
      params.append('fishing', this.fishing);
      params.append('lot', this.lottery);
      this.$axiosPack.post("/userCenter/agentManage/kickbackResetSave.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.dialogShow = false
          this.$notify({
            title: 'success',
            message: res.data.msg,
            type: 'success'
          });
        }
      });
    },
    goZB() {
      this.$bus.$emit("openBigMenu", '4');
      this.$router.push('/recordChange')
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.userList-page {
  .el-button--small {
    background-color: #f12c4c;
    border: #f12c4c;
  }

  .operateLink {
    color: #409eff;
    font-size: 12px;
    cursor: pointer;
  }

  .set-dialog {
    .cont {
      width: fit-content;
      margin: 0 auto;
    }

    .item {
      display: flex;
      align-items: center;
      margin-top: 8px;

      .user {
        margin-right: 20px;
        width: 130px;
        text-align: left;
      }
    }
  }
}</style>


