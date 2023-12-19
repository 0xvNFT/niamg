<template>
  <div class="help-page" v-if="helpOnlik.length">
    <div class="help_title">
      {{ data.title }}
    </div>
    <div class="help_content" v-html="data.content"></div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      data: {} //帮助中心数据
    };
  },
  computed: {
    ...mapState(['helpData']),
    //判断是否有数据
    helpOnlik () {
      if (this.helpData.length) {
        this.helpText()
      }
      return this.helpData
    }
  },
  components: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
    //对应id的值
    helpText () {
      for (let i = 0; i < this.helpData.length; i++) {
        let helpId = this.helpData[i].id
        if (this.$route.query.helpId == helpId) {
          this.data = this.helpData[i]
        }
      }
    }
  },
  watch: {
    //监听id
    $route () {
      if (this.$route.query.helpId) this.helpText()
    },
  }
}
</script>

<style lang="scss">
.help-page {
  text-align: center;
  padding: 20px;
  .help_title {
    font-size: 32px;
  }
  .help_content {
    font-size: 20px;
    margin-top: 20px;
    img {
      max-width: 90%;
      margin: 0 auto;
    }
  }
}
</style>
