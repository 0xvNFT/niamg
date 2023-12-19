<template>
  <div class="activesDetails-page" v-if="activeList && activeList.length">
    <div class="activesDetails_title">{{ detailData.title }}</div>
    <div style="margin: 20px 0;">
      {{ dateChange(detailData.updateTime) }} ~ {{ dateChange(detailData.overTime) }}
    </div>
    <div v-html="detailData.content" class="activesDetails_content"></div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DATE from "@/assets/js/date";

export default {
  data () {
    return {
      detailData: {},
    };
  },
  computed: {
    ...mapState(['activeData']),
    activeList () {
      if (this.activeData.length) {
        this.getDetail()
      }
      return this.activeData
    }
  },
  components: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
    getDetail () {
      this.activeData.forEach((data) => {
        if (this.$route.query.id == data.id) {
          this.detailData = data
        }
      })
    },
    dateChange (date) {
      return DATE.yearMonth(date)
    },
  },
  watch: {
    $route: {
      handler: function () {
        if (this.$route.query.id) this.getDetail();
      },
      deep: true
    }
  }
}
</script>

<style lang="scss">
.activesDetails-page {
  text-align: center;
  padding: 20px;
  .activesDetails_title {
    font-size: 20px;
  }
  .activesDetails_content {
    img {
      max-width: 90%;
      margin: 0 auto;
    }
  }
}
</style>
