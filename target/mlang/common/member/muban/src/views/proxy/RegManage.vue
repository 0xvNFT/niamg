<template>
  <el-card class="regManage-page">
    <div class="cont">
      <div class="inputDiv">
        <div class="item">
          <span class="name">{{ $t("userType") }}：</span>
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
        <template v-if="type === 120">
          <div class="item" v-for="(val,i) in data.proxyRegConfigs" :key='i'>
            <span class="name">{{ val.name }}：</span>
            <el-input
              v-model="formData[val.eleName]"
              class="outInput"
              :type="val.eleType === 1 ? 'text' : 'password'"
              :placeholder="val.tips"
              clearable
              size="small"
            ></el-input>
            <span style="color: red; margin-left: 5px">{{
              val.required === 2 ? "*" : ""
            }}</span>
          </div>
        </template>
        <template v-else-if="type === 130">
          <div class="item" v-for="(val,i) in data.memberRegConfigs" :key='i'>
            <span class="name">{{ val.name }}：</span>
            <el-input
              v-model="formData[val.eleName]"
              class="outInput"
              :type="val.eleType === 1 ? 'text' : 'password'"
              :placeholder="val.tips"
              clearable
              size="small"
            ></el-input>
            <span style="color: red; margin-left: 5px">{{
              val.required === 2 ? "*" : ""
            }}</span>
          </div>
        </template>
      </div>
      <div class="list" v-if="onOffBtn.game && data">
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
            <el-button type="primary" @click="sureBtn">{{
              $t("register")
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
              $t("register")
            }}</el-button>
          </div>
        </template>
      </div>
    </div>
  </el-card>
</template>

<script>
import { mapState } from "vuex";
// import LinkBar from '@/components/index/LinkBar'

export default {
  data() {
    return {
      type: "",
      typeArr: [],
      data: "",
      formData: {
        username: "",
        pwd: "",
        rpwd: "",
        phone: "",
        email: "",
        qq: "",
        facebook: "",
        line: "",
      },
      lottery: "", //
      live: "", //
      egame: "", //
      sport: "", //
      chess: "", //
      esport: "", //
      fishing: "", //
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted() {
    this.agentRegPromotionInfo();
  },
  methods: {
    //获取推广链接
    agentRegPromotionInfo() {
      this.$API.agentRegPromotionInfo({ load: true }).then((res) => {
        if (res) {
          if (res.proxyModel === 1 || res.proxyModel === 2) {
            this.typeArr.push({ value: 120, label: this.$t("proxy") });
          }
          if (res.proxyModel === 2 || res.proxyModel === 3) {
            this.typeArr.push({ value: 130, label: this.$t("member") });
          }
          if (this.typeArr.length) this.type = this.typeArr[0].value;

          // 注册选项处理
          let member = [];
          for (let i = 0; i < res.memberRegConfigs.length; i++) {
            if (
              res.memberRegConfigs[i].eleName !== "promoCode" &&
              res.memberRegConfigs[i].eleName !== "captcha" &&
              res.memberRegConfigs[i].eleName !== "receiptPwd"
            ) {
              member.push(res.memberRegConfigs[i]);
            }
          }
          let proxy = [];
          for (let i = 0; i < res.proxyRegConfigs.length; i++) {
            if (
              res.proxyRegConfigs[i].eleName !== "promoCode" &&
              res.proxyRegConfigs[i].eleName !== "captcha" &&
              res.proxyRegConfigs[i].eleName !== "receiptPwd"
            ) {
              proxy.push(res.proxyRegConfigs[i]);
            }
          }
          res.memberRegConfigs = member;
          res.proxyRegConfigs = proxy;

          this.data = res;
        }
      });
    },

    sureBtn() {
      //改变提交格式
      let params = {};
      params.userType = this.type;

      let arr = this.data.proxyRegConfigs;
      if (this.type === 130) arr = this.data.memberRegConfigs;
      for (let i = 0; i < arr.length; i++) {
        if (this.formData[arr[i].eleName]) {
          this.$set(params,arr[i].eleName,this.formData[arr[i].eleName])
        } else {
          if (arr[i].required === 2) {
            this.$message.warning(arr[i].name + this.$t("canNotEmpty"));
            return;
          }
        }
      }
      // 未固定返点模式添加
      if (!this.data.fixRebate) {
        params.egame = this.egame;
        params.chess = this.chess;
        params.sport = this.sport;
        params.esport = this.esport;
        params.fishing = this.fishing;
        params.live = this.live;
        params.lottery = this.lottery;
      }
      params.load = true;

      this.$API.agentManageregisterSave(params).then((res) => {
        if (res) {
          if (res.success) {
            this.$notify({
              title: "success",
              message: res.msg,
              type: "success",
            });
            this.clearFun();
          }
        }
      });
    },
    clearFun() {
      this.formData = {
        username: "",
        pwd: "",
        rpwd: "",
        phone: "",
        email: "",
        qq: "",
        facebook: "",
        line: "",
      };
      this.egame = "";
      this.chess = "";
      this.sport = "";
      this.esport = "";
      this.fishing = "";
      this.live = "";
      this.lottery = "";
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.regManage-page {
  color: $colorText;
  background: $black;
  position: relative;
  .cont {
    display: flex;
  }
  .inputDiv {
    flex: 1;
    .item {
      margin-top: 30px;
      margin-bottom: 15px;
      display: flex;
      align-items: center;
      .name {
        width: 130px;
        display: inline-block;
        text-align: right;
      }
      .el-input {
        width: 215px;
      }
    }
    input {
      background: $bgColor;
      color: #fff;
    }
  }
  .list {
    flex: 1;
    margin-left: 50px;
    margin-top: 30px;
    font-size: 20px;
    .item {
      margin-bottom: 25px;
      button {
        position: absolute;
        bottom: 50px;
      }
    }
  }
}
</style>


