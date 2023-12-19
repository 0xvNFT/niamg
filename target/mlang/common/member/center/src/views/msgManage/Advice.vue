<template>
  <div class="advice-page">
    <el-tabs type="border-card">
      <el-tab-pane :label="$t('advice')">
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconyonghu"></span>
          </div>
          <div class="item-con">
            <p class="name">{{ $t("onlineCust") }}</p>
            <p class="hint">{{ $t("onlineCustHint") }}</p>
          </div>
          <a class="item-btn" @click="goServer" v-if="!onOffBtn.isOnlineCustClose">
            <el-button type="primary">{{ $t("onlineCust") }}</el-button>
          </a>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconyonghu"></span>
          </div>
          <div class="item-con">
            <p class="name">{{ $t("putAdvice") }}</p>
            <p class="hint">{{ $t("putAdviceHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="subBtn($t('putAdvice'),1)">{{ $t("putAdvice") }}</el-button>
          </div>
        </el-card>
        <el-card shadow="hover">
          <div class="item-icon">
            <span class="iconfont iconyonghu"></span>
          </div>
          <div class="item-con">
            <p class="name">{{ $t("complain") }}</p>
            <p class="hint">{{ $t("complainHint") }}</p>
          </div>
          <div class="item-btn">
            <el-button type="primary" @click="subBtn($t('complain'),2)">{{ $t("complain") }}</el-button>
          </div>
        </el-card>
      </el-tab-pane>
      <el-tab-pane :label="$t('feedback')">
        <div class="searchDiv">
          <div class="item">
            {{ $t("status") }}：
            <el-select v-model="type" size="small">
              <el-option v-for="item in typeArr" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
          <DateModule @searchMethod="searchMethod"></DateModule>
        </div>
        <el-table :data="tableData" style="width: 99%">
          <el-table-column prop="typeTxt" :label="$t('adviceType')"></el-table-column>
          <el-table-column prop="content" :label="$t('adviceContent')"></el-table-column>
          <el-table-column prop="createTime" :label="$t('adviceTime')"></el-table-column>
          <el-table-column prop="finalTime" :label="$t('finalTime')"></el-table-column>
          <el-table-column prop="statusTxt" :label="$t('status')"></el-table-column>
          <el-table-column fixed="right" :label="$t('operate')" width="100">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.row)" type="text" size="small">{{ $t("look") }}</el-button>
            </template>
          </el-table-column>
          <!-- <el-table-column prop="statusTxt" label="操作"></el-table-column> -->
        </el-table>
        <PageModule :total="total" :pageNumber.sync="pageNumber" @searchMethod="searchMethod"></PageModule>
      </el-tab-pane>
    </el-tabs>
    <!-- 详情弹窗 -->
    <el-dialog :title="$t('info')" :visible.sync="dialogTableVisible" v-if="infoData" class="info-dialog">
      <div class="oldCont">
        <p class="line">{{ $t("adviceContent") }}：{{infoData.advcie.content}}</p>
        <p class="line">{{ $t("putType") }}：{{infoData.advcie.sendType === 1 ? $t("putAdvice"):$t("complain")}}</p>
        <div class="line" v-for="val in infoData.adviceList">
          <p style="color:red" v-if="val.contentType === 1">{{ $t("custReply") }}：{{val.content}}<span style="margin-left:20px">{{ $t("timeText") }}：{{val.createTime}}</span></p>
          <p v-else>{{ $t("myReply") }}：{{val.content}}<span style="margin-left:20px">{{ $t("timeText") }}：{{val.createTime}}</span></p>
        </div>
      </div>
      <p class="line">
        <el-input type="textarea" :rows="7" :placeholder="$t('putHint')" v-model="content" class="cont" :autofocus="true" ref="cont"></el-input>
      </p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn(1)">{{ $t("confirm") }}</el-button>
      </span>
    </el-dialog>
    <!-- 建议弹窗 -->
    <el-dialog :title="title" :visible.sync="dialogShow" width="40%" :close-on-click-modal="false" class="sub-dialog">
      <el-input type="textarea" :rows="7" :placeholder="$t('putHint')" v-model="content" class="cont" :autofocus="true" ref="cont"></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogShow = false">{{ $t("cancel") }}</el-button>
        <el-button type="primary" @click="sureBtn(false)">{{ $t("confirm") }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import DateModule from '@/components/common/DateModule'
import PageModule from '@/components/common/PageModule'

export default {
  data () {
    return {
      type: '',
      typeArr: [{ value: '', label: this.$t("allStatus") }, { value: 1, label: this.$t("notReply") }, { value: 2, label: this.$t("replied") }],
      tableData: [],
      total: 0,//总条数
      pageNumber: 1,//当前页码
      dialogShow: false,//是否显示弹窗
      title: this.$t("putAdvice"),
      content: '',//提交的内容
      typeSub: 1,//提交类型 
      dialogTableVisible: false,//详情弹窗是否显示
      infoData: '',
      infoDataId: '',
      startTime: this.$dataJS.today() + ' 00:00:00',//搜索开始时间
      endTime: this.$dataJS.today() + ' 23:59:59',//搜索结束时间
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
    DateModule,
    PageModule
  },
  mounted () {
    this.searchMethod()
  },
  methods: {
    // 列表
    searchMethod (startTime, endTime, isSearch, noRefresh) {
      if (startTime) this.startTime = startTime
      if (endTime) this.endTime = endTime
      if (isSearch) this.pageNumber = 1
      let load = [true, 200, null]
      if (noRefresh) load = ''

      //改变提交格式
      let params = new URLSearchParams();
      params.append('startTime', this.startTime);
      params.append('endTime', this.endTime);
      params.append('pageNumber', this.pageNumber);
      params.append('status', this.type);
      this.$axiosPack.post("/userCenter/advice/adviceList.do", { params: params, load: load }).then(res => {
        if (res) {
          for (let i = 0; i < res.data.rows.length; i++) {
            res.data.rows[i].typeTxt = res.data.rows[i].sendType === 1 ? this.$t("putAdvice") : this.$t("complain")
            res.data.rows[i].statusTxt = res.data.rows[i].status === 1 ? this.$t("notReply") : this.$t("replied")
          }
          this.tableData = res.data.rows
          this.total = res.data.total
        }
      });
    },

    handleClick (data) {
      console.log(data)
      this.infoDataId = data.id
      this.dialogTableVisible = true
      //改变提交格式
      let params = new URLSearchParams();
      params.append('adviceId', this.infoDataId);
      this.$axiosPack.post("/userCenter/advice/viewAdvice.do", { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          this.infoData = res.data
        }
      });
    },

    subBtn (txt, type) {
      this.title = txt
      this.dialogShow = true
      this.typeSub = type
      // 获取焦点
      this.$nextTick(() => {
        this.$refs.cont.$refs.textarea.focus();
      });
    },

    // 提交
    sureBtn (type) {
      let url = '/userCenter/advice/saveAdvice.do'
      console.log(type)
      if (type) {
        url = '/userCenter/advice/updateAdvice.do'
      }

      //改变提交格式
      let params = new URLSearchParams();
      if (type) params.append('adviceId', this.infoDataId);
      else params.append('sendType', this.typeSub);
      params.append('content', this.content);
      this.$axiosPack.post(url, { params: params, load: [true, 200, null] }).then(res => {
        if (res) {
          if (res.data.msg) {
            this.$notify({
              title: 'success',
              message: res.data.msg,
              type: 'success'
            });
          }
          this.dialogShow = false
          this.dialogTableVisible = false
          this.searchMethod(null, null, true, true)
        }
      });
    },

    goServer () {
      if (this.onOffBtn.kfUrl) {
        location.href = this.onOffBtn.kfUrl
      } else {
        this.toastFun(this.$t("notSet"));
      }
    }
  },
  watch: {
    dialogShow () {
      if (!this.dialogShow) this.content = ''
    },
    dialogTableVisible () {
      if (!this.dialogTableVisible) this.content = ''
    }
  }
};
</script>

<style lang="scss">
.advice-page {
  .el-card {
    margin-top: 30px;
  }
  .el-card__body {
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .item-icon {
      margin: 40px;
      width: 80px;
      height: 80px;
      background: #d6d4d5;
      color: #fff;
      display: flex;
      align-items: center;
      justify-content: center;
      .iconfont {
        font-size: 40px;
      }
    }
    .item-con {
      // width: calc(100% - 350px);
      flex: 1;
      .name {
        font-size: 20px;
        margin-bottom: 10px;
      }
      .hint {
        font-size: 16px;
        color: $grayTxt;
        padding-right: 30px;
      }
    }
    .item-btn {
      .el-button {
        min-width: 160px;
        margin-right: 15px;
        height: 50px;
        font-size: 18px;
      }
    }
  }
  .sub-dialog {
    // .cont {
    //   width: 100%;
    //   height: 150px;
    //   padding: 10px;
    //   box-sizing: border-box;
    //   font-size: 14px;
    // }
  }
  .info-dialog {
    .oldCont {
      max-height: 240px;
      overflow-y: auto;
    }
    .el-dialog__body {
      .line {
        margin-bottom: 5px;
      }
    }
  }
}
</style>


