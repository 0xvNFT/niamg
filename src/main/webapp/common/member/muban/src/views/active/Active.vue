<template>
  <div class="active-page">
    <div class="title">{{ $t('active') }}</div>
    <div class="active_list">
      <div class="active_list_item" v-for="(item, index) in activeData" :key="index" @click="goDetail(item)">
        <div class="item_img_div" v-lazy:background-image="item.titleImgUrl">
        </div>
        <div class="item_info">
          <h2 class="item_info_title ellipsis2" :title="item.title">{{ item.title }}</h2>
          <span class="link_text">{{ $t('lookInfo') }}<i class="el-icon-arrow-right"></i></span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
    };
  },
  computed: {
    ...mapState(['activeData']),
  },
  components: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
    goDetail (val) {
      this.$router.push('/activeDetails?&id=' + val.id)
    },
  },
}
</script>

<style lang="scss">
.active-page {
  margin: 0 auto;
  // max-width: 1420px;
  width: 95%;

  .title {
    color: #fff;
    font-size: 1.4rem;
    font-weight: 700;
    line-height: 2rem;
    margin-bottom: 1.2rem;
    margin-top: 18px;
  }

  .active_list {
    display: flex;
    flex-wrap: wrap;

    .active_list_item {
      background: $black;
      border-radius: 12px;
      overflow: hidden;
      margin-right: 12px;
      margin-bottom: 20px;
      width: calc((100% - 48px) / 4);
      max-width: 400px;

      .item_img_div {
        width: 100%;
        height: 0;
        padding-bottom: 60%;
        background-position: center center;
        background-size: cover;
        background-repeat: no-repeat;
        cursor: pointer;
      }

      .item_info {
        padding: 16px;

        .item_info_title {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 10px;
          max-height: 60px;
          min-height: 30px;
        }

        .link_text {
          color: #2283f6;
          cursor: pointer;
          font-size: 14px;
        }
      }
    }
  }
}
</style>
