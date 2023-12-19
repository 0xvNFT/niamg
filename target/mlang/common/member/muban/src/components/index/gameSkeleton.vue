<template>
  <div class="gameSkeleton">
    <div class="blurImg" v-for="val in listNum" :key="val">
      <div class="homeTitle" v-if="!hideTitle">
        <!-- 分类标题图片、标题 -->
        <div class="left">
          <el-skeleton-item variant="image" style="width: 20px; height: 20px;" />
        </div>
        <!-- 加载更多、前进、后退按钮 -->
        <div class="right">
          <div class="arrow-left">
            <i class="el-icon-arrow-left"></i>
          </div>
          <div class="arrow-right">
            <i class="el-icon-arrow-right"></i>
          </div>
        </div>
      </div>
      <div class="gametypeList" style="flex-wrap: initial;">
        <div v-for="val in 6" :key="val" class="itemPadd col-8--2">
          <el-skeleton animated>
            <template slot="template">
              <el-skeleton-item variant="image" style="width: 100%; padding-bottom: 130%;" />
            </template>
          </el-skeleton>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { mapState } from 'vuex'

export default {
  data () {
    return {
    };
  },
  props: {
    listNum: Number,
    hideTitle: Boolean
  },
  computed: {
    // ...mapState(['onOffBtn'])
  },
  components: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
  },
  watch: {
  }
};
</script>

<style lang="scss">
.gameSkeleton {
  .el-skeleton__item {
    background: $titleBg;
  }
  .el-skeleton.is-animated .el-skeleton__item {
    background: linear-gradient(90deg, $black 25%, $black 37%, #191b1f 63%);
    background-size: 400% 100%;
    animation: el-skeleton-loading 1.4s ease infinite;
    border-radius: 15px;
  }
}
</style>
