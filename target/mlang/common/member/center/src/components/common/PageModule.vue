<template>
  <div class="pageModule">
    <el-pagination @current-change="handleCurrentChange" :current-page="pageNumber" layout="total, prev, pager, next, jumper" :total="total" :hide-on-single-page="true">
    </el-pagination>
  </div>
</template>

<script>
// import { mapState } from 'vuex'
import dataJS from '@/assets/js/date'

export default {
  data () {
    return {
      // currentPage: 1,
    };
  },
  props: {
    total: '',
    pageNumber: ''
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
  },
  components: {
  },
  created () {
  },
  methods: {
    handleCurrentChange (val) {
      console.log(`当前页: ${val}`);
      this.$emit('update:pageNumber', val)
      this.$emit('searchMethod');
    },
  },
  watch: {
  }
};
</script>

<style lang="scss">
.dateModule {
  // padding: 10px 0;
  margin-bottom: 10px;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .el-date-editor--datetimerange.el-input__inner {
    width: 335px;
  }
}
</style>


