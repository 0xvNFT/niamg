<template>
  <el-card class="regManage-page">
    <div class="cont">
      <div class="inputDiv">
        <div class="item">
          <span class="name">{{ $t("userType") }}：</span>
          <el-select v-model="type" size="small">
            <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <template v-if="type === 120">
          <div class="item" v-for="(val,i) in data.proxyRegConfigs" :key="i">
            <span class="name">{{val.name}}：</span>
            <el-input v-model="formData[val.eleName]" class="outInput" :type="val.eleType === 1 ? 'text' : 'password'" :placeholder="val.tips" clearable size="small"></el-input>
            <span style="color: red;margin-left: 5px">{{val.required === 2 ? '*':''}}</span>
          </div>
        </template>
        <template v-else-if="type === 130">
          <div class="item" v-for="(val,i) in data.memberRegConfigs" :key="i">
            <span class="name">{{val.name}}：</span>
            <el-input v-model="formData[val.eleName]" class="outInput" :type="val.eleType === 1 ? 'text' : 'password'" :placeholder="val.tips" clearable size="small"></el-input>
            <span style="color: red;margin-left: 5px">{{val.required === 2 ? '*':''}}</span>
          </div>
        </template>
      </div>
      <div class="list" v-if="onOffBtn.game && data">
        <template v-if="data.fixRebate">
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{  configJsFlag("nowLotRebateAmount")  }}：{{
              onOffBtn.lang === "br" ? (data.sr.lottery / 10)+'%' : data.sr.lottery
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{  configJsFlag("nowLiveRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.live/10) +'%' : data.sr.live
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ configJsFlag("nowEgameRebateAmount")}}：{{
              onOffBtn.lang === "br" ?( data.sr.egame / 10)+'%' : data.sr.egame
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{configJsFlag("nowSportRebateAmount") }}：{{
              onOffBtn.lang === "br" ? (data.sr.sport / 10)+'%' : data.sr.sport
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{configJsFlag("nowChessRebateAmount")}}：{{
              onOffBtn.lang === "br" ?( data.sr.chess / 10)+'%' : data.sr.chess
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ configJsFlag("nowEsportRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.esport / 10 )+'%': data.sr.esport
            }}
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ configJsFlag("nowFishingRebateAmount")}}：{{
              onOffBtn.lang === "br" ? (data.sr.fishing / 10)+'%' : data.sr.fishing
            }}
          </div>
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{ $t("register") }}</el-button>
          </div>
        </template>
        <template v-else>
          <div class="item" v-if="onOffBtn.game.lottery === 2">
            {{ $t("setLotRebateAmount") }}：
            <el-select v-model="lottery" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.lotteryArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.live === 2">
            {{ $t("setLiveRebateAmount") }}：
            <el-select v-model="live" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.liveArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.egame === 2">
            {{ $t("setEgameRebateAmount") }}：
            <el-select v-model="egame" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.egameArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.sport === 2">
            {{ $t("setSportRebateAmount") }}：
            <el-select v-model="sport" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.sportArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.chess === 2">
            {{ $t("setChessRebateAmount") }}：
            <el-select v-model="chess" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.chessArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.esport === 2">
            {{ $t("setEsportRebateAmount") }}：
            <el-select v-model="esport" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.esportArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <div class="item" v-if="onOffBtn.game.fishing === 2">
            {{ $t("setFishingRebateAmount") }}：
            <el-select v-model="fishing" size="small" :placeholder="$t('changeBonus')">
              <el-option v-for="item in data.fishingArray" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <!-- <div class="item">{{ $t("promotionUrlHint") }}</div> -->
          <div class="item">
            <el-button type="primary" @click="sureBtn">{{ $t("register") }}</el-button>
          </div>
        </template>
      </div>
    </div>
  </el-card>
</template>

<script>
import { mapState } from 'vuex'
// import LinkBar from '@/components/index/LinkBar'

export default {
  data () {
    return {
      type: '',
      typeArr: [],
      data: '',
      formData: {
        username: '',
        pwd: '',
        rpwd: '',
        phone: '',
        email: '',
        qq: '',
        facebook: '',
        line: '',
      },
      lottery: '',//
      live: '',//
      egame: '',//
      sport: '',//
      chess: '',//
      esport: '',//
      fishing: '',//
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
  },
  mounted () {
    this.agentRegPromotionInfo()
  },
  methods: {
    agentRegPromotionInfo () {
      this.$axiosPack.post("/userCenter/agentManage/agentRegPromotionInfo.do", { load: [true, 200, null] }).then(res => {
        if (res) {
          if (res.data.proxyModel === 1 || res.data.proxyModel === 2) {
            this.typeArr.push({ value: 120, label: this.$t("proxy") })
          }
          if (res.data.proxyModel === 2 || res.data.proxyModel === 3) {
            this.typeArr.push({ value: 130, label: this.$t("member") })
          }
          if (this.typeArr.length) this.type = this.typeArr[0].value

          // 注册选项处理
          let member = []
          for (let i = 0; i < res.data.memberRegConfigs.length; i++) {
            if (res.data.memberRegConfigs[i].eleName !== 'promoCode' && res.data.memberRegConfigs[i].eleName !== 'captcha' && res.data.memberRegConfigs[i].eleName !== 'receiptPwd') {
              member.push(res.data.memberRegConfigs[i])
            }
          }
          let proxy = []
          for (let i = 0; i < res.data.proxyRegConfigs.length; i++) {
            if (res.data.proxyRegConfigs[i].eleName !== 'promoCode' && res.data.proxyRegConfigs[i].eleName !== 'captcha' && res.data.proxyRegConfigs[i].eleName !== 'receiptPwd') {
              proxy.push(res.data.proxyRegConfigs[i])
            }
          }
          res.data.memberRegConfigs = member
          res.data.proxyRegConfigs = proxy

          this.data = res.data
        }
      });
    },

    sureBtn () {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('userType', this.type);

      let arr = this.data.proxyRegConfigs
      if (this.type === 130) arr = this.data.memberRegConfigs
      for (var i in arr) {
        if (this.formData[arr[i].eleName]) {
          params.append(arr[i].eleName, this.formData[arr[i].eleName]);
        } else {
          if (arr[i].required === 2) {
            this.toastFun(arr[i].name + this.$t("canNotEmpty"));
            return
          }
        }
      }
      // 未固定返点模式添加
      if (!this.data.fixRebate) {
        params.append('egame', this.egame);
        params.append('chess', this.chess);
        params.append('sport', this.sport);
        params.append('esport', this.esport);
        params.append('fishing', this.fishing);
        params.append('live', this.live);
        params.append('lottery', this.lottery);
      }
      this.$axiosPack.post("/userCenter/agentManage/registerSave.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.$notify({
            title: 'success',
            message: res.data.msg,
            type: 'success'
          });
          this.clearFun()
        }
      });
    },
    clearFun () {
      this.formData = {
        username: '',
        pwd: '',
        rpwd: '',
        phone: '',
        email: '',
        qq: '',
        facebook: '',
        line: '',
      }
      this.egame = ''
      this.chess = ''
      this.sport = ''
      this.esport = ''
      this.fishing = ''
      this.live = ''
      this.lottery = ''
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.regManage-page {
  .cont {
    display: flex;
  }
  .inputDiv {
    .item {
      margin-bottom: 15px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .name {
        // width: 130px;
        display: inline-block;
        text-align: right;
        flex: 1;
      }
      .el-input {
        width: 215px;
      }
    }
  }
  .list {
    margin-left: 50px;
    .item {
      margin-bottom: 15px;
    }
  }
}
</style>


