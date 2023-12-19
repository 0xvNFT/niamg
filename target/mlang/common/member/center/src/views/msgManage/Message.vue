<template>
  <el-card class="message-page">
    <el-checkbox v-model="allChecked"></el-checkbox>
    <el-button type="primary" size="medium" @click="delBtn">{{ $t("delete") }}</el-button>
    <el-button type="primary" size="medium" @click="allReceiveDelete">{{ $t("allDelete") }}</el-button>
    <el-button type="primary" size="medium" @click="allReceiveRead">{{ $t("allRead") }}</el-button>
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item :name="val.id + '-' + val.receiveMessageId + '-' + val.status + '-' + key" v-for="(val,key) in data" :key="key" :class="readArr[key] == 1 ? 'noRead':''">
        <template slot="title">
          <el-checkbox v-model="val.check"></el-checkbox>
          {{val.title}}
        </template>
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
      rids: '',//删除id
      allChecked: false,
      readArr: []
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
    this.searchMethod([true, 200, null])
  },
  methods: {
    // 列表
    searchMethod (load) {
      //改变提交格式
      let params = new URLSearchParams();
      params.append('pageNumber', this.pageNumber);
      this.$axiosPack.post("/userCenter/msgManage/messageList.do", { params: params, load: load }).then(res => {
        if (res) {
          this.readArr = []
          if (load) {
            for (let i = 0; i < res.data.rows.length; i++) {
              res.data.rows[i].check = false;
              this.readArr.push(res.data.rows[i].status)
            }
          }

          this.data = res.data.rows
          this.total = res.data.total
        }
      });
    },
    // 删除站内信
    delBtn () {
      this.$confirm(this.$t("sureDelete"), this.$t("hintText"), {
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: 'warning'
      }).then(() => {
        let ids = ''
        for (let i = 0; i < this.data.length; i++) {
          if (this.data[i].check) {
            ids += this.data[i].receiveMessageId + ','
          }
        }

        //改变提交格式
        let params = new URLSearchParams();
        params.append('ids', ids);
        this.$axiosPack.post(" /userCenter/msgManage/receiveDelete.do", { params: params, load: [true, 200, null] }).then(res => {
          if (res) {
            if (res.data.msg) {
              this.allChecked = false
              this.$notify({
                title: 'success',
                message: res.data.msg,
                type: 'success'
              });
              this.searchMethod(false)
            }
          }
        });
      }).catch(() => {
      });
    },
    // 一键删除
    allReceiveDelete () {
      let _this = this
      this.$confirm(this.$t("sureAllDelete"), this.$t("hintText"), {
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: 'warning'
      }).then(() => {
        this.$axiosPack.post("/userCenter/msgManage/allReceiveDelete.do", { load: [true, 200, null] }).then(res => {
          if (res) {
            if (res.data.msg) {
              this.$notify({
                title: 'success',
                message: res.data.msg,
                type: 'success'
              });
            }
          }
        });
      }).catch(() => {
      });
    },
    // 一键已读
    allReceiveRead () {
      this.$confirm(this.$t("sureAllRead"), this.$t("hintText"), {
        confirmButtonText: this.$t("confirm"),
        cancelButtonText: this.$t("cancel"),
        type: 'warning'
      }).then(() => {
        this.$axiosPack.post("/userCenter/msgManage/allReceiveRead.do", { load: [true, 200, null] }).then(res => {
          if (res) {
            if (res.data.msg) {
              this.$notify({
                title: 'success',
                message: res.data.msg,
                type: 'success'
              });
              this.searchMethod(false)
            }
          }
        });
      }).catch(() => {
      });
    },
  },
  watch: {
    activeName () {
      let str = this.activeName.split('-')
      if (str[2] == 1) {
        //改变提交格式
        let params = new URLSearchParams();
        params.append('sid', str[0]);
        params.append('rid', str[1]);
        this.$axiosPack.post("/userCenter/msgManage/readMessage.do", { params: params }).then(res => {
          if (res) {
            this.readArr[str[3]] = 2
            this.$forceUpdate();
            // this.data[str[3]].status = 2
          }
        });
      }
    },
    allChecked () {
      for (let i = 0; i < this.data.length; i++) {
        this.data[i].check = this.allChecked;
      }
    }
  }
};
</script>

<style lang="scss">
.message-page {
  .el-checkbox {
    margin-right: 10px;
  }
  .noRead {
    .el-collapse-item__header {
      color: blue;
    }
  }
  .el-button {
    margin-bottom: 10px;
    background-color: #0ec504;
    border: 1px solid #0ec504;
  }
}
</style>


