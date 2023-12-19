<template>
  <div class="loading">

  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      data: ''
    };
  },
  computed: {
    ...mapState(['loading'])
  },
  components: {
  },
  mounted () {
    // this.newNoticesA()
  },
  methods: {

  },
  watch: {
  }
};
</script>

<style lang="scss">
.index-page {
}
</style>


