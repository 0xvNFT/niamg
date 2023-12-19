<template>
  <div class="pageModule">
    <el-pagination
      @current-change="handleCurrentChange"
      @size-change="toChangeSizes"
      :current-page="pageNumber"
      :layout="layout"
      :total="total"
      :hide-on-single-page="true"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="10"
    >
    </el-pagination>
  </div>
</template>

<script>
// import { mapState } from 'vuex'

export default {
  data () {
    return {
      // currentPage: 1,
    };
  },
  props: {
    total: '',
    pageNumber: '',
    layout: {
      default: 'total, prev, pager, next, jumper'
    },
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
    toChangeSizes (val) {
      this.$emit('change', val)
    }
  },
  watch: {
  }
};
</script>

<style lang="scss" scoped>
.pageModule {
  margin-top: 10px;
  .el-pagination {
    text-align: center;

  }
  /deep/ .el-pagination__sizes > div > div > input {
    height: 28px;
  }
  /deep/ .el-pagination__sizes {
    line-height: 23px;
  }
}

</style>


