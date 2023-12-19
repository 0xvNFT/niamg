<template>
  <div class="index-page">
    <Carousel></Carousel>
    <GameList></GameList>
    <Notification v-if="userInfo&&userInfo.login"></Notification>
    <div class="statistics">
      <div class="statistics--titlebox">
        <div class="line-box">
          <div class="line"></div>
          <div class="point"></div>
        </div>
        <h1 class="title">{{$t('statistics')}}</h1>
        <div class="line-box">
          <div class="point"></div>
          <div class="line"></div>
        </div>
      </div>
      <div class="playNumber">
        <div class="item">
          <img src="../assets/images/gold.png" alt="">
          <div>
            <p>{{$t('numberOfPeopleOnline')}}</p>
            <h2>{{ data.activeMember }}</h2>
          </div>
        </div>
        <div class="item">
          <img src="../assets/images/vip.png" alt="">
          <div>
            <p>{{$t('numberOfWinners')}}</p>
            <h2>{{ data.cumulativeOrder }}</h2>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import Carousel from "@/components/index/carousel.vue";
import GameList from "@/components/index/gameList.vue";
import Notification from "@/components/index/notification.vue";
import { mapState } from "vuex";

export default {
  name: 'index',
  data () {
    return {
      data: '',
      timer: '',//定时器
    };
  },
  computed: {
    ...mapState(['userInfo'])
  },
  components: { Carousel, GameList,Notification},
  created () {

  },
  activated () {
    this.timer = setInterval(() => {
      this.randomNumber()
    }, 15000)
  },
  mounted () {
    this.stationFake()
  },
  methods: {
    // 获取在线人数、获胜玩家数据
    stationFake () {
      this.$API.stationFake().then((res) => {
        if (res) {
          this.data = res
        }
      })
    },
    // 随机人数
    randomNumber () {
      let num = Math.floor(Math.random() * 100 - 0)
      let num1 = Math.floor(Math.random() * 80 - 0)
      this.data.activeMember += num
      this.data.cumulativeOrder += num1
    }
  },
  watch: {
    $route () {
      if (this.$route.path != 'index') {
        clearInterval(this.timer);
      }
    },
  },
};
</script>

<style lang="scss">
.index-page {
  margin: 0 auto;
  max-width: 1420px;
  width: 95%;
  .statistics {
    .statistics--titlebox {
      display: flex;
      align-content: center;
      justify-content: center;
      gap: 32px;
      border-bottom: 1px solid #fff;
      margin-bottom: 32px;
      .line-box {
        display: inherit;
        align-items: center;
        width: 89px;
        height: 82px;
        .line {
          background: rgba(228,152,72,.2);
          height: 1px;
          width: 80px;
        }
        .point {
          background: #f6a045;
          -webkit-transform: rotate(45deg);
          transform: rotate(45deg);
          height: 9px;
          width: 9px;
        }
      }
    }
    .title {
      text-align: center;
      padding: 20px 0;
      font-weight: bold;
      font-size: 26px;
    }
    .playNumber {
      display: flex;
      justify-content: center;
      flex-wrap: wrap;
      margin: 0 auto;
      .item {
        display: flex;
        margin-right: 15px;
        background: $black1;
        align-items: center;
        border-radius: 8px;
        padding: 15px;
        width: 32%;
        min-width: 260px;
        margin-top: 10px;
        img {
          width: 80px;
          height: 80px;
        }
        div {
          margin-left: 15px;
          p {
            margin-bottom: 5px;
            color: #fff;
            font-weight: 600;
          }
          h2 {
            color: #ffaa17;
          }
        }
      }
    }
  }
}
</style>
