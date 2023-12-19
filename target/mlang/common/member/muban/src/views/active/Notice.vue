<template>
  <div class="notice-page">
    <!-- 网站公告 -->
    <div class="heard">
      {{ $t("notice") }}
    </div>
    <el-collapse v-model="activeName" accordion>
      <div class="notiveList" v-if="notiveList && notiveList.length">
        <el-collapse-item :title="item.title + '【' + item.updateTime + '】'" :name="index" v-for="(item, index) in notiveList" :key="index">
          <div v-html="item.content"></div>
        </el-collapse-item>
      </div>
      <template v-else>
        <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
      </template>
    </el-collapse>
  </div>
</template>

<script>
import DATE from "../../assets/js/date";

export default {
  data () {
    return {
      notiveList: [], //网站公告
      activeName: "0",
    };
  },
  computed: {
    // ...mapState(['onOffBtn'])
  },
  components: {},
  created () { },
  mounted () {
    this.$API.articleList().then((res) => {
      // if (res.success)
      if (res) {
        this.notiveList = res.rows;
        for (let i = 0; i < this.notiveList.length; i++) {
          this.notiveList[i].updateTime = DATE.dateChange(
            this.notiveList[i].updateTime
          );
        }
      }
    });
  },
  methods: {},
  watch: {},
};
</script>

<style lang="scss">
.notice-page {
  width: 100%;
  padding: 0 10px;
  .el-collapse {
    border-top: 0px;
    border-bottom: 0px;
  }
  .notiveList {
    width: 90%;
    // height: 80px;
    // background: #fff;
    margin: 0 auto;
    margin-top: 20px;
    border-radius: 10px;
    padding: 20px 10px;
    .el-collapse-item__header {
      // background: $black;
      border-top: 1px solid #ebeef5;
      border-radius: 10px;
      margin: 20px;
      padding: 0 20px;
    }
    .el-collapse-item__wrap {
      border-radius: 10px;
    }
    .el-collapse-item__content {
      padding: 20px;
    }
  }
}
</style>
