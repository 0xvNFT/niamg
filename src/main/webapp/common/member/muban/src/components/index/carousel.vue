<template>
  <div class="index-carousel" :style="'min-height:' + (height3d + 40) + 'px'">
    <carousel-3d ref="carousel" :perspective="30" :autoplayTimeout="3000" :clickable="false" :loop="true" :space="400" class="carousel3d" :width="width3d" :height="height3d" :autoplay="true" v-if="imgList.length && reloadShow">
      <slide v-for="(item, index) in imgList" :key="index" :index="index">
        <img :src="item.titleImg" alt="" style="border-radius: 15px;" />
      </slide>
    </carousel-3d>
  </div>
</template>

<script>
import { Carousel3d, Slide } from "vue-carousel-3d";

export default {
  data () {
    return {
      imgList: [], //轮播图数据
      width3d: '',//轮播图宽
      reloadShow: true//重新渲染
    };
  },
  computed: {
    height3d () {//轮播图高
      return parseInt(this.width3d / 1.8)
    },
    pageWidth () {//屏幕宽
      return this.$store.state.pageWidth
    }
  },
  components: {
    Carousel3d,
    Slide
  },
  created () {
    // 获取轮播图数据
    this.getBanner();

    // 初始化轮播图宽、高
    this.initCarousel3d()
  },
  mounted () {
  },
  activated () {
    // 从其他地方返回当前页面，轮播图高度失效，重新渲染轮播图
    this.reloadShow = false;
    this.$nextTick(() => (this.reloadShow = true));
  },
  methods: {
    // 初始化轮播图宽、高
    initCarousel3d () {
      let width3d = parseInt(document.body.clientWidth * 0.4)
      if (width3d < 348) {
        this.width3d = 348
      } else if (width3d > 450) {
        this.width3d = 450
      } else {
        this.width3d = width3d
      }
    },
    //获取轮播图
    getBanner () {
      this.$API.getBanner().then((res) => {
        if (res) {
          this.imgList = res;
        }
      });
    },
  },
  watch: {
    // 监听屏幕宽发生变化
    pageWidth () {
      if (this.pageWidth) this.initCarousel3d()
    }
  }
};
</script>

<style lang="scss">
.index-carousel {
  padding: 20px 0;
  .carousel-3d-slide {
    background-color: transparent;
    border: none;
  }
  .carousel-3d-container {
    margin: 0 auto;
    cursor: grab;
    // height: 100% !important;
  }
  .carousel-3d-controls {
    a {
      line-height: 0px !important;
    }
    span {
      color: #fff;
      font-size: 120px;
      font-weight: 100;
    }
  }
}
</style>
