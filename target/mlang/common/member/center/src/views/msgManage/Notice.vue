<template>
  <el-card class="notice-page">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item :title="val.title + '【' + dateChange(val.updateTime) + '】'" :name="key" v-for="(val,key) in data" :key="key">
        <div v-html="val.content"></div>
      </el-collapse-item>
    </el-collapse>
    <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
  </el-card>
</template>

<script>
// import { mapState } from 'vuex'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      activeName: '',
      data: [],//列表数据
      total: 0,//总条数
      pageNumber: 1,//当前页码
    };
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
  },
  components: {
    PageModule
  },
  mounted () {
    // 列表
    this.searchMethod()
  },
  methods: {
    // 列表
    searchMethod () {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('pageNumber', this.pageNumber);
      this.$axiosPack.post("/userCenter/msgManage/articleList.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.data = res.data.rows
          this.total = res.data.total
        }
      });
    },
    dateChange (data) {
      return this.$dataJS.dateChange(data)
    }
  },
  watch: {
  }
};
</script>

<style lang="scss">
.notice-page {
}
</style>


