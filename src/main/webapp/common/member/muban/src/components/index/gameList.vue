<template>
  <div class="index-gameList" id="backTop">
    <!-- 一级游戏 tab 加载中 -->
    <template v-if="!tabsList.length">
      <div class="swiper-gameTab"></div>
    </template>
    <!-- 一级游戏 tab -->
    <swiper
      class="swiper-gameTab"
      :options="swiperOption"
      ref="mySwiperTab"
      v-else
    >
      <swiper-slide
        v-for="(item, index) in tabsList"
        :key="index"
        class="col-8--2"
        @click.native="chooseTabs(item.type)"
        :class="{ active: item.type === tabValue }"
      >
        <img :src="item.icon" alt="" v-if="item.icon" />
        <img
          :src="
            require('@images/index/index_' + item.code)
          "
          alt=""
          v-else
        />
        {{ item.customTitle ? item.customTitle : item.name }}
      </swiper-slide>
      <div class="swiper-pagination" slot="pagination"></div>
    </swiper>

    <div class="game-content">
      <!-- 二级tab -->
      <swiper
        class="swiper twoTabSwiper"
        :options="swiperOption"
        ref="slotSwiper"
        v-if="childTabsList && childTabsList.length"
      >
        <swiper-slide
          v-for="(slot, j) in childTabsList"
          :key="j"
          class="itemPadd1"
        >
          <img
            class="soltImg"
            :src="tabChildValue === j ? slot.imgUrlSelect : slot.imgUrl"
            @click="chooseChildTabs(slot, j)"
          />
        </swiper-slide>
      </swiper>
      <!-- 对应tab显示游戏列表 -->
      <!-- 全部游戏列表 -->
      <template v-if="loading">
        <template v-if="tabValue === 11">
          <gameSkeleton :listNum="3"></gameSkeleton>
        </template>
        <template v-else>
          <gameSkeleton :hideTitle="true" :listNum="1"></gameSkeleton>
        </template>
      </template>
      <template v-else-if="!gameList.length">
        <el-empty
          :image="require('@images/nodata')"
          :image-size="128"
          :description="$t('noData')"
        ></el-empty>
      </template>
      <template v-else>
        <!-- 大厅游戏 -->
        <div class="lobbyList" v-if="tabValue === 11">
          <div class="blurImg" v-for="(game, k) in gameList" :key="k">
            <!-- 对应分类游戏头部 -->
            <div class="homeTitle" v-if="game.tab">
              <!-- 分类标题图片、标题 -->
              <div class="left">
                <img :src="game.tab.icon" alt="" v-if="game.tab.icon" />
                <img
                  :src="require('@images/index/index_' + game.tab.code)"
                  alt=""
                  v-else
                />
                <span>{{
                  game.tab.customTitle ? game.tab.customTitle : game.tab.name
                }}</span>
              </div>
              <!-- 加载更多、前进、后退按钮 -->
              <div class="right">
                <div class="btn hoverBtn" @click="loadMoreFun(game.tab)">
                  {{ $t("loadMore") }}
                </div>
                <div class="arrow-left hoverBtn" @click="showPrev(k)">
                  <i class="el-icon-arrow-left"></i>
                </div>
                <div class="arrow-right hoverBtn" @click="showNext(k)">
                  <i class="el-icon-arrow-right"></i>
                </div>
              </div>
            </div>
            <!-- 对应分类游戏列表 -->
            <swiper class="swiper" :options="swiperOption" ref="mySwiper">
              <swiper-slide
                v-for="(item, j) in game.games"
                :key="j"
                class="col-8--2 itemPadd"
              >
                <div class="hoverImg" @click="goPage(item, j)">
                  <img class="gameImg" v-lazy="item.imgUrl" />
                  <div class="cover">
                    <p class="coverTitle ellipsis2">{{ item.name }}</p>
                    <div class="coverBall">
                      <i class="el-icon-caret-right"></i>
                    </div>
                  </div>
                </div>
              </swiper-slide>
              <div class="swiper-pagination" slot="pagination"></div>
            </swiper>
          </div>
        </div>
        <!-- 单个分类游戏列表 -->
        <div class="gametypeList" v-else>
          <div
            v-for="(game, k) in gameList"
            :key="game.czCode + k"
            class="itemPadd col-8--2"
          >
            <div class="hoverImg" @click="goPage(game)">
              <img class="gameImg" v-lazy="game.imgUrl" />
              <div class="cover">
                <p class="coverTitle ellipsis2">{{ game.name }}</p>
                <div class="coverBall">
                  <i class="el-icon-caret-right"></i>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { swiper, swiperSlide } from "vue-awesome-swiper";
import "swiper/swiper.min.css";
import gameSkeleton from "@/components/index/gameSkeleton";

export default {
  data() {
    return {
      tabsList: [], //一级分类游戏列表
      tabValue: 11, //一级分类游戏列表 tab 值
      gameList: [], //当前展示的游戏列表
      allGameList: {}, //所有一级游戏列表
      swiperOption: {
        slidesPerView: "auto", //一行几个
        // spaceBetween: 500,//距离
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
      },
      loading: true, //是否是加载状态
      childTabsList: [], //二级分类游戏tab
      tabChildValue: -1, //二级分类游戏 tab 下标
      childGameList: {}, //二级分类游戏列表
      hasChildTab: [0, 1, 2, 7], // 拥有二级游戏tab的类型
    };
  },
  computed: {
    ...mapState(["userInfo", "onOffBtn"]),
    sanfangList() {
      return this.onOffBtn.platforms;
    },
  },
  components: { swiper, swiperSlide, gameSkeleton },
  created() {},
  mounted() {
    //获取游戏分类
    this.getTabs();
    //第一次进来获取所有游戏 tabValue 11
    this.getGames();
  },
  methods: {
    //获取一级分类游戏 tab
    getTabs() {
      this.$API.getTabs().then((res) => {
        if (res) {
          this.tabsList = res;
        }
      });
    },
    //获取一级分类游戏
    getGames() {
      // 加载状态 开启
      this.loading = true;

      // 请求游戏数据
      let params = { type: this.tabValue };
      this.$API.getGames(params).then((res) => {
        if (res) {
          // 加载状态 关闭
          this.loading = false;

          // 11为大厅，过滤没有游戏的数据
          if (this.tabValue === 11) {
            let arr = [];
            for (let i = 0; i < res.length; i++) {
              if (res[i].games && res[i].games.length) {
                arr.push(res[i]);
              }
            }
            this.gameList = arr;
          } else {
            this.gameList = res;
          }

          // 将已经请求过的数据存储起来，下次直接调用
          if (this.gameList.length) {
            this.allGameList[this.tabValue] = this.gameList;
          }
        }
      });
    },
    //下一张图
    showNext(index) {
      this.$refs.mySwiper[index].swiper.slideNext();
    },
    //上一张图
    showPrev(index) {
      this.$refs.mySwiper[index].swiper.slidePrev();
    },
    // 加载更多
    loadMoreFun(game) {
      let type = game ? game.type : 11;
      //点击一级游戏 tab
      this.chooseTabs(type);
      // 触发 游戏容器重置滚动条事件
      this.$bus.$emit("needResetScroll");
      this.jumpSlide(game.type);
    },
    // 前往其他页面（需要登录才能访问的页面用这个方法）
    goPage(game, index) {
      // 如果有子游戏 就打开该分组的子游戏
      if (this.hasChildTab.includes(game.gameTabType)) {
        this.chooseTabs(game.gameTabType, index);
        // 触发 游戏容器重置滚动条事件
        this.$bus.$emit("needResetScroll");
        this.jumpSlide(game.type);
        return;
      }
      // 判断是否登录
      if (!this.userInfo.login) {
        var val = { select: 0, show: true };
        this.$bus.$emit("registerShow", val);
        // 如果没有登录，禁止跳转
        // event.preventDefault();
      } else {
        //FB体育需要去掉链接里面的mobile参数
        if (game.czCode === "fbSport") {
          game.forwardUrl = game.forwardUrl.replace("mobile=1&", "");
        }
        console.log("game link =>", game.forwardUrl);

        //pg游戏 单独跳转方式
        if (game.czCode === "pg") {
          let urlList = game.forwardUrl.split("&");
          let gameType = "";
          //获取pg游戏gameType
          urlList.filter((item) => {
            if (item.includes("gameType")) {
              gameType = item.split("=")[1] * 1;
            }
          });
          //请求数据
          let params = {};
          params.mobile = 1;
          params.isApp = 1;
          params.gameType = gameType;
          this.$API.forwardPg2(params, { load: true }).then((res) => {
            this.$store.dispatch("getThirdPageHtml", res);
           // 跳去三方游戏页面
            setTimeout(() => {
              this.$router.push("/thirdPage");
              // 重新获取用户信息
              this.$bus.$emit("refreshUserInfo");
            }, 500);
          });
        } else {
          this.$API
            .gamesanfangUrl(game.forwardUrl, { load: true })
            .then((res) => {
              if (res.success) {
                //yg彩票不能内嵌
                if (game.czCode.includes("yg")) {
                  window.location.href = res.url;
                } else {
                  this.$store.dispatch("getThirdPageUrl", res.url);
                  // 跳去三方游戏页面
                  setTimeout(() => {
                    console.log(11111)
                    this.$router.push("/thirdPage");
                    // 重新获取用户信息
                    this.$bus.$emit("refreshUserInfo");
                  }, 500);
                }
              }
            });
        }
      }
    },
    //点击一级游戏 tab
    chooseTabs(type, index) {
      // 一级分类游戏列表tab赋值
      this.tabValue = type;
      // 重置当前展示的游戏列表
      this.gameList = [];
      // 重置二级tab列表
      this.childTabsList = [];
      // 重置二级分类游戏 tab 下标
      this.tabChildValue = -1;

      //判断是否有二级游戏tabs && 数据不为空
      if (this.hasChildTab.includes(type)) {
        // 加载状态 开启
        this.loading = true;
        let params = { type: type };
        this.$API.getGame2(params).then((res) => {
          if (res) {
            for (let i = 0; i < res.content.length; i++) {
              // 将二级tab图片和二级tab选中图片加入数据
              res.content[i].imgUrl = this.tabImgUrl(res.content[i].gameType);
              res.content[i].imgUrlSelect = this.tabImgUrl(
                res.content[i].gameType,
                true
              );
            }
            // 二级tab列表 赋值
            this.childTabsList = res.content;
            //如果不是从游戏大厅的游戏列进来就默认选中第一个二级tab
            this.chooseChildTabs(this.childTabsList[index || 0], index || 0);
          }
        });
      } else {
        // 如果请求过当前分类，从保存的游戏列表获取数据
        if (this.allGameList[this.tabValue]) {
          this.gameList = this.allGameList[this.tabValue];
        } else {
          this.getGames();
        }
      }
    },
    //点击二级游戏tab
    chooseChildTabs(item, i) {
      // 二级分类游戏列表tab赋值
      this.tabChildValue = i;
      // 重置当前展示的游戏列表
      this.gameList = [];
      // 如果请求过当前分类，从保存的游戏列表获取数据
      if (this.childGameList[item.gameType]) {
        this.gameList = this.childGameList[item.gameType];
        // 加载状态 关闭
        this.loading = false;
        return;
      }

      // 获取二级分类游戏数据
      let params = {
        platformType: item.gameType,
        tabType: item.gameTabType,
        // pageSize: 21,
        // pageIndex: 1,
        load: true,
      };
      //目前固定写死21条数据
      this.$API.getFactoryGames(params).then((res) => {
        if (res) {
          this.gameList = res;
          // 将已经请求过的数据存储起来，下次直接调用
          if (this.gameList.length) {
            this.childGameList[item.gameType] = this.gameList;
          }
          // 加载状态 关闭
          this.loading = false;
        }
      });
    },
    // 二级tab图片
    tabImgUrl(type, isSelect) {
      let name = "";
      for (const key in this.sanfangList) {
        if (Object.hasOwnProperty.call(this.sanfangList, key)) {
          if (type.includes(key)) {
            name += key;
            break;
          }
        }
      }

      if (isSelect) name += "_select";
      try {
        return require(`@images/game/${name}`);
      } catch (err) {
        // 如果图片资源不存在会显示默认图片
        if (TypeError(err).toString().includes("Cannot find module")) {
          name = isSelect ? "default_select" : "default";
          return require(`@images/game/${name}`);
        }
      }
    },
    // 一级菜单跳转到对应的游戏分组
    jumpSlide(num) {
      this.tabsList.filter((item, i) => {
        if (item.type == num) {
          this.$refs.mySwiperTab.swiper.slideToLoop(i - 1, 1000, false);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.index-gameList {
  min-height: 60vh;
  .swiper-gameTab {
    height: 40px;
    background: $black;
    cursor: grab;
    overflow: hidden;
    .swiper-slide {
      color: #fff;
      font-size: 14px;
      font-weight: 600;
      display: flex;
      align-items: center;
      justify-content: center;
      img {
        width: 20px;
        height: 20px;
        margin-right: 5px;
      }
      &.active {
        background: $blue;
        border-radius: 5px;
      }
    }
  }
  .game-content {
    padding: 15px 0;
    .soltImg {
      cursor: pointer;
      width: 100%;
      display: block;
    }
    .blurImg {
      position: relative;
      .homeTitle {
        display: flex;
        justify-content: space-between;
        width: 100%;
        margin: 10px 0;
        span {
          align-items: center;
          color: #fff;
          display: flex;
          font-size: 16px;
          font-weight: 800;
          height: 24px;
          justify-content: center;
          margin-right: 10px;
        }
        img {
          height: auto;
          margin-right: 5px;
          width: 24px;
        }
        .left {
          display: flex;
          justify-content: center;
          align-items: center;
        }
        .right {
          display: flex;
          justify-content: center;
          justify-content: center;
          div {
            border-radius: 10px;
            color: #fff;
            font-size: 14px;
            font-weight: 700;
            height: 2rem;
            line-height: 2rem;
            padding: 0 1rem;
            text-align: center;
            white-space: nowrap;
            width: auto;
            background: $titleBg;
            cursor: pointer;
            display: flex;
            justify-content: center;
            align-content: center;
          }
          .arrow-left {
            margin-left: 6px;
            width: 2rem;
            padding-top: 0.55rem;
          }
          .arrow-right {
            margin-left: 6px;
            width: 2rem;
            padding-top: 0.55rem;
          }
          .hoverBtn {
            transition: all 0.3s ease;
            &:hover {
              background: $blue;
            }
          }
        }
      }
    }
  }
  .hoverImg {
    background: $titleBg;
    border-radius: 15px;
    cursor: pointer;
    height: 100%;
    width: 100%;
    // min-height: 107px;
    min-width: 80px;
    position: relative;
    overflow: hidden;
    color: #fff;
    display: block;
    // padding-bottom: 130%;
    .gameImg {
      transition: all 0.2s ease;
      display: block;
      width: 100%;
      height: 100%;
    }
    .cover {
      background-color: rgba(0, 0, 0, 0.45);
      display: flex;
      align-items: center;
      flex-direction: column;
      justify-content: center;
      position: absolute;
      z-index: 2;
      left: 0;
      bottom: 0;
      right: 0;
      top: 0;
      opacity: 0;
      transition: all 0.2s ease;
      .coverTitle {
        margin-bottom: 15px;
        text-align: center;
        font-weight: 700;
        padding: 0 10px;
      }
      .coverBall {
        background-color: #ed1d49;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 1.5rem;
        height: 3.5rem;
        width: 3.5rem;
      }
    }
    &:hover {
      .gameImg {
        transform: scale(1.1);
        filter: blur(5px);
      }
      .cover {
        opacity: 1;
      }
    }
  }
  .gametypeList {
    display: flex;
    flex-wrap: wrap;
  }
  .itemPadd {
    padding: 7px;
    height: auto;
  }
  .twoTabSwiper {
    margin-bottom: 5px;
    .itemPadd1 {
      padding: 2px 5px;
      width: 150px;
    }
  }
}
</style>
