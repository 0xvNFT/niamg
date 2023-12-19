<!--
 * @FilePath: \muban\src\components\common\TurntableGame.vue
 * @Description: 大转盘游戏组件
-->
<template>
  <el-dialog :visible.sync="showGame" :show-close="false" data-v-container>
    <!-- 转盘主页 -->
    <div class="main-container">
      <div class="game-box">
        <img class="img-spin" src="@images/da_zhuan_pan/circle_blue" alt="蓝色外层旋转盘">
        <img class="img-spin" src="@images/da_zhuan_pan/circle_white" alt="白色外层旋转盘">
        <div class="all-rewards">
          <div class="circle-box">
            <img :src="prizesPicture" @error="handleSrcErr" alt="转盘板面" :style="spinningStyle">
            <img src="@images/da_zhuan_pan/icon_arrow" alt="箭头">
            <img src="@images/da_zhuan_pan/circle_centerbutton" alt="转盘按钮" @click="toSpin()">
          </div>
        </div>
      </div>
      <div class="right--content-box">
        <div class="right--content-top">
          <div class="top--controls">
            <b>{{ $t('totalTurnableWinning') }}</b>
            <nav class="controls-box">
              <img src="@images/da_zhuan_pan/icon_question" @click="toShowDrawer(true, true)" alt="查看游戏规则">
              <img @click="toControlAudio(!music)" :src="require(`@images/da_zhuan_pan/icon_music_${music ? 'on' : 'off'}`)" alt="音频">
              <img @click="showGame = false" src="@images/da_zhuan_pan/icon_close" alt="关闭">
            </nav>
            <!-- 游戏音频 -->
            <audio :src="mp3url" ref="audioEl" preload="auto" loop></audio>
          </div>
          <!-- 币种单位，总奖金要从后台取 dlt -->
          <strong class="top--bonus">₱58,000,819.50</strong>
        </div>
        <img src="@images/da_zhuan_pan/person" class="right--content-peson" />
        <div class="right--content-bottom">
          <div class="bottom--playslots">
            <em>1.00 / 1</em>
            <div>
              <em>4</em>
            </div>
          </div>
          <p class="bottom--current" @click="toShowDrawer(true, false)">
            {{ $t('recentJackpot') }}
            <i class="el-icon-arrow-right"></i>
          </p>
        </div>
      </div>
    </div>

    <!-- 游戏规则， 最近中奖 -->
    <div class="right--drawer-box">
      <aside class="right-drawer" :class="{show: showDrawer}">
        <div class="drawer--title">
          <h1>{{ $t(`${showRules?'rules':'recentJackpot'}`) }}</h1>
          <img src="@images/da_zhuan_pan/icon_close" @click="toShowDrawer(false)" alt="关闭">
        </div>
        <article v-if="showRules" class="drawer--content">
          <p>WinBet 有趣的轮盘</p>
          <br>
          <p>每天旋转我们的幸运轮，赢取真钱大奖和 iPhone 14 1BTC 超级大奖!</p>
          <br>
          <p>幸运轮盘规则。</p>
          <ol>
            <li>旋转已准备好 24 小时</li>
            <li>每投注 10,000 PHP 即可旋转一次幸运轮盘，投注越多，中奖越多，无上限！</li>
            <li>赢得实体奖品（iPhone 14）的玩家将收到来自WinBet的内部信，VIP客服经理将联系客户发送 奖。</li>
          </ol>
          <br>
          <p>条件。</p>
          <ol>
            <li>每个玩家只能拥有一个账户。</li>
            <li>开设多个账户或冒充一个账户的玩家将被取消促销资格。余额可能会被没收，账户可能会被冻结。</li>
            <li>WinBet保留自行决定修改、修改、暂停、取消、拒绝或取消本次促销活动的权利。</li>
          </ol>
        </article>
        <div v-else class="drawer--content">
          <span class="current--get-bonus">{{ $t('recentAwards') }}</span>
          <table>
            <thead>
              <tr>
                <th>{{ $t('timeText') }}</th>
                <th>{{ $t('userName') }}</th>
                <th>{{ $t('bonus') }}</th>
              </tr>
            </thead>
          </table>
          <div class="table--rows-box" ref="autoScrollBox" @mouseenter="clearAutoScroll" @mouseleave="autoScroll">
            <table v-if="recentAwards.length">
              <tbody>
                <tr v-for="user, ind in recentAwards" :key="ind">
                  <td>{{ dateChange(user.createDatetime) }}</td>
                  <td>{{ user.username }}</td>
                  <td>{{ user.awardValue }}</td>
                </tr>
              </tbody>
              <tfoot>
                <tr>
                  <td>-</td>
                  <td>-</td>
                  <td>-</td>
                </tr>
              </tfoot>
            </table>
            <el-empty
              :image="require('@images/nodata')"
              :description="$t('noData')"
              :image-size="128"
              v-else
            ></el-empty>
          </div>
        </div>
      </aside>
    </div>
    <!-- 转盘旋转时禁止所有操作 -->
    <div class="dialog--forbidden" @touchmove.prevent.stop v-if="isRunning"></div>
  </el-dialog>
</template>

<script>
import { setModalMark } from '@/assets/js/utils';
import DATE from "@/assets/js/date";
const { dateChange, today, beforeDayStart } = DATE;

export default {
  name: 'TurntableGame',
  data () {
    return {
      showGame: false, // 大转盘游戏弹出框
      showDrawer: false, // 展示右侧抽屉
      showRules: false, // 显示游戏规则
      music: true, // 噪音/静音
      mp3url: require('../../assets/audio/turntable-game-audio.mp3'),
      prizesList: [], // 奖品清单
      prizesPicture: 'undefined', // 奖品图片
      recentAwards: [], // 最近获奖
      timer: undefined,
      resetScrollTimer: undefined,
      // -- 转盘旋转效果
      isRunning: false, // 转盘旋转中
      rotateAngle: 0, // 旋转角度
      rotateconfig: {
        duration: 6000, // 时长
        circle: 15, // 圈数
        mode: 'ease-in-out' // 模式 （由快到慢）
      },
      drawTimes: 1, // 抽奖次数
      rewardIndex: 3 // 中奖的奖品索引
    }
  },
  computed: {
    /**
     * @description: 定义旋转效果
     * @return {*} 生成CSS旋转属性
     */  
    spinningStyle () {
      const _config = this.rotateconfig;
      return `
      -webkit-transition: transform ${_config.duration}ms ${_config.mode};
      transition: transform ${_config.duration}ms ${_config.mode};
      -webkit-transform: rotate(${this.rotateAngle}deg);
      transform: rotate(${this.rotateAngle}deg);`
    }
  },
  methods: {
    // 时间戳转换方法
    dateChange,
    /**
     * @description: 展示右侧抽屉（游戏规则，最近中奖纪录）
     * @param {*} drawersShow 显示/ 关闭抽屉
     * @param {*} showRule 显示游戏规则否则显示最近中奖
     */
    toShowDrawer (drawersShow, showRule) {
      this.showDrawer = drawersShow;
      if(!drawersShow && !showRule) {
        return
      } else if (showRule) {
        this.showRules = true;
      } else {
        this.showRules = false;
        this.togetUsersWinRecord();
      }
    },
    /**
     * @description: 玩大转盘游戏
     */  
    toSpin () {
      this.isRunning = true;
      let params = {
        activeId: 34 //后续要改
      };
      this.$API.spinTurntable(params).then((res) => {
        console.log("🚀 ~ res:", res)
      });
      this.rotateAngle = this.rotateconfig.circle * 360 * this.drawTimes - (-30 + this.rewardIndex * -30); // 旋转角度计算
      this.drawTimes++;
      setTimeout(() => {
        this.isRunning = false;
      }, this.rotateconfig.duration);
    },
    /**
     * @description: 请求用户的最近中奖纪录
     */  
    togetUsersWinRecord () {
      let params = {
        startTime: beforeDayStart(6) + ' 00:00:00',
        endTime: today() + ' 23:59:59'
      };
      this.$API.getUsersWinRecord(params).then((res) => {
        if (res && res.length) {
          this.recentAwards = Object.freeze(res);
          this.autoScroll();
        }
      });
    },
    /**
     * @description: 最近投注数据的列表自动滚动
     */
    autoScroll () {
      this.clearAutoScroll();
      if (this.recentAwards.length >= 10) {
        let autoScrollBoxEl = this.$refs.autoScrollBox;
        this.timer = setInterval(() => {
          autoScrollBoxEl.scrollTop++;
          autoScrollBoxEl.firstElementChild;
        }, 50);
        this.resetScrollTimer = setInterval(() => {
          if (autoScrollBoxEl.firstElementChild.offsetHeight - autoScrollBoxEl.scrollTop === autoScrollBoxEl.offsetHeight) {
            autoScrollBoxEl.scrollTop = 0;
          };
        }, 1500);
      }
    },
    /**
     * @description: 移除自动滚动的定时器
     */
    clearAutoScroll () {
      clearInterval(this.timer);
      clearInterval(this.resetScrollTimer);
    },
    /**
     * @description: 控制音频
     * @param {*} play 值为true时进行播放 否则暂停
     * @param {*} needReset 是否需要重置当前音频的播放位置
     */
    toControlAudio (play, needReset = false) {
      const audioEl = this.$refs.audioEl;
      if (play) {
        audioEl.play();
        this.music = true;
      } else {
        audioEl.pause();
        if (needReset) { audioEl.currentTime = 0; return };
        this.music = false;
      }
    },
    /**
     * @description: 后台配置的奖品图片加载失败时展示默认图片
     * @param {*} e 目标元素
     */
    handleSrcErr(e) {
      e.target.src = require('@images/da_zhuan_pan/circle_zhuanpan');
    }
  },
  watch: {
    /**
     * @description: 监听游戏弹出框进行设置模糊背景
     * @param {*} show 为true时设置否则移除
     */
    showGame(show) {
      if (show) {
        setModalMark(true);
      } else {
        setModalMark(false);
        this.showDrawer = false;
        this.toControlAudio(false, true);
      }
    },
    /**
     * @description: 监听最近中奖记录窗口关闭时进行销毁定时器
     * @param {*} show 为false时 === 窗口关闭
     */
    showDrawer (show) {
      if(!show) {
        this.clearAutoScroll();
      }
    }
  },
}
</script>

<style lang="scss" scoped>
div[data-v-container] {
  overflow: unset;
  /deep/.el-dialog {
    width: 889px;
    height: 558px;
    font-family: Poppins-BoldItalic,Poppins;
    font-style: oblique;
    user-select: none;
    box-shadow: none;
    font-weight: 700;
    background: no-repeat center/100% url('../../assets/images/da_zhuan_pan/main_background');
    .main-container {
      position: relative;
      .game-box {
        top: -120px;
        left: -82px;
        height: 517px;
        width: 521px;
        position: absolute;
        img {
          background: transparent;
        }
        img:nth-child(2) {
          left: 5px;
        }
        .img-spin {
          display: block;
          position: absolute;
          animation: outsite-spin linear 18s infinite;
        }
        .all-rewards {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          .circle-box {
            height: 417px;
            width: 417px;
            border: 2px solid red;
            border-radius: 50%;
            position: relative;
            img {
              position: absolute;
              left: 50%;
              transform: translate(-50%, -50%);
            }
            img:first-child {
              height: 415px;
              width: 415px;
              left: 0;
            }
            img:nth-child(2) {
              width: 32px;
              min-height: 28px;
            }
            img:nth-child(3) {
              top: 50%;
            }
            img:nth-child(3):active {
              width: 106px;
            }
          }
        }
      }
      .right--content-box {
        position: absolute;
        color: #fff;
        right: -18px;
        top: -54px;
        .right--content-top {
          width: 476px;
          height: 117px;
          position: relative;
          background: no-repeat center/100% url('../../assets/images/da_zhuan_pan/topright_background');
          .top--controls {
            position: absolute;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: self-end;
            width: inherit;
            b {
              width: 260px;
              padding-left: 68px;
              font-weight: bold;
              font-size: 14px;
              height: 15px;
              left: 477px;
            }
            .controls-box {
              width: 44.8%;
              display: flex;
              img {
                margin-top: -8px;
                width: 64px;
                min-height: 40px;
                max-height: 40px;
                background: transparent;
              }
              img:nth-child(2) {
                margin-left: -7px;
              }
              img:nth-child(3) {
                margin-left: -7px;
              }
            }
          }
          .top--bonus {
            width: 60%;
            top: 62%;
            left: 50%;
            padding-right: 3px;
            font-size: 36px;
            display: block;
            position: absolute;
            overflow: visible;
            transform: translate(-50%, -50%);
            background: linear-gradient(180deg,#f88bd0,#ffdf12);
            background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-background-clip: text;
          }
        }
        .right--content-peson {
          margin-top: -25px;
          margin-left: -30px;
          background: transparent;
        }
        .right--content-bottom {
          position: absolute;
          right: 80px;
          top: 410px;
          height: 150px;
          .bottom--playslots {
            position: relative;
            width: 283px;
            height: 74px;
            background: no-repeat center/100% url('../../assets/images/da_zhuan_pan/number_background');
            em {
              font-size: 26px;
              top: 50%;
              left: 50%;
              transform: translate(-50%,-50%);
              white-space: nowrap;
              position: absolute;
            }
            div {
              position: absolute;
              text-align: center;
              vertical-align: middle;
              width: 67px;
              height: 67px;
              right: -30px;
              top: -30px;
              background: no-repeat center/100% url('../../assets/images/da_zhuan_pan/circle_numberbackground');
              em {
                margin-left: -2px;
              }
            }
          }
          p {
            padding-left: 100px;
            cursor: pointer;
            font-size: 16px;
            i {
              font-weight: 600;
              margin-top: 33px;
            }
          }
        }
      }
    }
    .right--drawer-box {
      width: 567px;
      height: 611px;
      top: -112px;
      left: 414px;
      border-radius: 20px;
      background: transparent;
      position: relative;
      pointer-events: none;
      overflow: hidden;
      .right-drawer {
        width: 100%;
        height: 100%;
        left: 570px;
        border-radius: 20px;
        color: #fff;
        position: absolute;
        transition: left 1s;
        pointer-events: all;
        background: linear-gradient(180deg,#2078fb,#0a0f32 13%,#0a0f32);
        .drawer--title {
          display: flex;
          align-items: center;
          height: 73px;
          h1 {
            width: 100%;
            text-align: center;
            font-size: 24px;
          }
          img {
            width: 64px;
            min-height: 40px;
            max-height: 40px;
            right: 33px;
            position: absolute;
            background: transparent;
          }
        }
        .drawer--content {
          width: 100%;
          padding: 32px;
          font-style: normal;
          font-weight: 400;
          // 游戏规则样式
          ol {
            margin-left: 16px;
            list-style: auto;
            li {
              line-height: 22px;
            }
          }
          // 最近大奖的样式
          .current--get-bonus {
            display: inline-block;
            text-align: center;
            cursor: pointer;
            font-style: oblique;
            border-radius: 12px;
            font-weight: 700;
            font-size: 16px;
            height: 43px;
            line-height: 43px;
            padding: 0 15px;
            min-width: 107px;
            background: linear-gradient(1turn,#1043ee,#1b89ff 55%,#34d6ff);
          }
          table {
            width: 100%;
            th {
              width: 33%;
              border-bottom: 1px solid #333030;
              background: linear-gradient(180deg,#db003b,#9844b7 86%,#156af6);
              background-clip: text;
              -webkit-background-clip: text;
              color: transparent;
              font-size: 18px;
              height: 40px;
            }
            td {
              text-align: center;
              height: 40px;
            }
          }
          div {
            width: 100%;
            height: 390px;
            overflow-y: auto;
            cursor: all-scroll;
            table tbody tr td {
              width: 33%;
            }
          }
          .table--rows-box::-webkit-scrollbar {
            display: none;
          }
        }
      }
      .show {
        left: 0;
      }
    }
    .dialog--forbidden{
      position: fixed;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      z-index: 3000;
    }
  }
}
@keyframes outsite-spin {
  from {
    transform: rotate(360deg);
  }
  to {
    transform: rotate(0deg);
  }
}
@keyframes insite-spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>