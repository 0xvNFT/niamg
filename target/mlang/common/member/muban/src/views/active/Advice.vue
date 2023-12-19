<template>
  <div class="advice-page order-page">
    <el-tabs type="border-card" v-model="tabVal">
      <el-tab-pane :label="$t('advice')" name="1">
        <div>
          <el-row>
            <el-card class="cardBox" shadow="hover">
              <div class="card">
                <div class="item1">
                  <div class="box">
                    <i class="el-icon-user"></i>
                  </div>
                </div>
                <div class="item2">
                  <div class="text">
                    <p>{{ $t("onlineCust") }}</p>
                    <span>{{ $t("onlineCustHint") }}</span>
                  </div>
                </div>
                <div class="item3">
                  <a
                    :href="onOffBtn.kfUrl || 'javascript:;'"
                    :target="onOffBtn.kfUrl ? '_blank' : '_self'"
                    class="el-button el-button--primary"
                  >
                    {{ $t("onlineCust") }}
                  </a>
                </div>
              </div>
            </el-card>
            <el-card class="cardBox" shadow="hover">
              <div class="card">
                <div class="item1">
                  <div class="box">
                    <i class="el-icon-upload2"></i>
                  </div>
                </div>
                <div class="item2">
                  <div class="text">
                    <p>{{ $t("putAdvice") }}</p>
                    <span>{{ $t("putAdviceHint") }}</span>
                  </div>
                </div>
                <div class="item3">
                  <el-button
                    type="primary"
                    @click="visible($t('putAdvice'), 1)"
                    >{{ $t("putAdvice") }}</el-button
                  >
                </div>
              </div>
            </el-card>
            <el-card class="cardBox" shadow="hover">
              <div class="card">
                <div class="item1">
                  <div class="box">
                    <i class="el-icon-phone"></i>
                  </div>
                </div>
                <div class="item2">
                  <div class="text">
                    <p>{{ $t("complain") }}</p>
                    <span>{{ $t("complainHint") }}</span>
                  </div>
                </div>
                <div class="item3">
                  <el-button
                    type="primary"
                    @click="visible($t('complain'), 2)"
                    >{{ $t("complain") }}</el-button
                  >
                </div>
              </div>
            </el-card>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane :label="$t('feedback')" name="2">
        <div class="searchDiv">
          <div class="item">
            {{ $t("status") }}：
            <el-select v-model="type" size="small">
              <el-option
                v-for="item in typeArr"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </div>
          <div class="item">
            <el-date-picker
              v-model="date"
              type="datetimerange"
              :start-placeholder="startTime"
              :end-placeholder="endTime"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
            <!-- 查询 -->
            <el-button
              type="primary"
              @click="searchMethod()"
              icon="el-icon-search"
              size="medium"
              style="margin-left: 15px"
              >{{ $t("searchText") }}</el-button
            >
          </div>
        </div>

        <div class="content">
          <div class="title">
            <p>{{ $t("adviceType") }}</p>
            <p>{{ $t("adviceTime") }}</p>

            <p>{{ $t("finalTime") }}</p>
            <p>{{ $t("status") }}</p>
            <p>{{ $t("operate") }}</p>
          </div>
          <div class="list">
            <div
              class="cont"
              v-for="(item, index) in tableData"
              @click="showContent(index)"
              :key="index"
            >
              <div class="cont-show">
                <p>{{ item.typeTxt }}</p>
                <p>{{ item.createTime }}</p>
                <p>{{ item.finalTime }}</p>
                <p>{{ item.statusTxt }}</p>
                <p @click.stop="handleClick(item)" class="check-detail">{{ $t("look") }}</p>
              </div>
              <div v-show="key === index" class="cont-hide">
                <div>
                  <p>{{ $t("adviceContent") }}：</p>

                  <p>{{ item.content }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="!tableData.length">
          <el-empty :image="require('../../assets/images/nodata.png')" :image-size="128" :description="$t('noData')"></el-empty>
        </div>
        <PageModule
          :total="total"
          :pageNumber.sync="pageNumber"
          @searchMethod="searchMethod"
        ></PageModule>
      </el-tab-pane>
    </el-tabs>
    <!-- 详情弹窗 -->
    <el-dialog
      :title="$t('info')"
      :visible.sync="dialogTableVisible"
      v-if="infoData"
      class="common_dialog info-dialog"
    >
      <div class="oldCont">
        <p class="line">
          {{ $t("adviceContent") }}：{{ infoData.advcie.content }}
        </p>
        <p class="line">
          {{ $t("putType") }}：{{
            infoData.advcie.sendType === 1 ? $t("putAdvice") : $t("complain")
          }}
        </p>
        <div class="line" v-for="(val, i) in infoData.adviceList" :key="i">
          <p style="color: red" v-if="val.contentType === 1">
            {{ $t("custReply") }}：{{ val.content
            }}<span style="margin-left: 20px"
              >{{ $t("timeText") }}：{{ val.createTime }}</span
            >
          </p>
          <p v-else>
            {{ $t("myReply") }}：{{ val.content
            }}<span style="margin-left: 20px"
              >{{ $t("timeText") }}：{{ val.createTime }}</span
            >
          </p>
        </div>
      </div>
      <p class="line">
        <el-input
          type="textarea"
          :rows="7"
          :placeholder="$t('putHint')"
          v-model="content"
          class="cont"
          :autofocus="true"
          ref="cont"
        ></el-input>
      </p>
      <span slot="footer" class="dialog-footer">
        <button class="closeBtn themeBtn" @click="dialogTableVisible = false">
          {{ $t("cancel") }}
        </button>
        <button class="sureBtn themeBtn" @click="infoSureBtn()">
          {{ $t("confirm") }}
        </button>
      </span>
    </el-dialog>
    <!-- 建议弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="dialogShow"
      width="40%"
      class="common_dialog"
    >
      <el-input
        type="textarea"
        :rows="7"
        :placeholder="$t('putHint')"
        v-model="content"
        class="cont"
        :autofocus="true"
        ref="cont"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <button class="closeBtn themeBtn" @click="dialogShow = false">
          {{ $t("cancel") }}
        </button>
        <button class="sureBtn themeBtn" @click="sureBtn(false)">
          {{ $t("confirm") }}
        </button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import DATE from "@/assets/js/date.js";
import PageModule from "@/components/common/PageModule";
import { mapState } from "vuex";

export default {
  data() {
    return {
      total: 0, //总条数
      pageNumber: 1, //当前页码
      sendType: 1, //发送type
      dialogTableVisible: false, //详情弹窗是否显示
      infoData: "", //反馈记录查看详情数据
      infoDataId: "", //反馈记录查看详情id
      date: [], //日期
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      type: "", //反馈记录下拉框默认值
      // typeArr: [{ value: '', label: this.$t("allStatus") }, { value: 1, label: this.$t("notReply") }, { value: 2, label: this.$t("replied") }],//反馈记录下拉框数据
      tableData: [], //反馈记录列表
      dialogShow: false, //是否显示建议弹窗
      title: this.$t("putAdvice"), //建议弹窗标题
      content: "", //提交的内容
      key: -1, //要显示的完整内容的key值
      tabVal: "1", //tab的值
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
    typeArr() {
      return [
        { value: "", label: this.$t("allStatus") },
        { value: 1, label: this.$t("notReply") },
        { value: 2, label: this.$t("replied") },
      ];
    },
  },
  components: {
    PageModule,
  },
  created() {},
  mounted() {
    // 获取反馈记录列表
    // this.searchMethod()
  },
  methods: {
    // 显示详情
    showContent(key) {
      if (this.key === key) this.key = -1;
      else this.key = key;
    },
    // 显示弹窗
    visible(text, type) {
      this.sendType = type;
      // 建议弹窗标题
      this.title = text;
      // 建议弹窗显示
      this.dialogShow = true;
    },
    // 建议弹窗确定
    sureBtn() {
      if (!this.content) {
        this.$message.error(this.$t("canNotEmpty"));
        return;
      }

      //改变提交格式
      let params = {
        sendType: this.sendType,
        content: this.content,
      };
      params.load = true;

      this.$API.saveAdvice(params).then((res) => {
        if (res.success) {
          if (res.msg) this.$message.success(res.msg);
          // 建议弹窗关闭
          this.dialogShow = false;
        }
      });
    },
    // 反馈记录详情弹窗确定
    infoSureBtn() {
      if (!this.content) {
        this.$message.error(this.$t("canNotEmpty"));
        return;
      }

      //改变提交格式
      let params = {
        adviceId: this.infoDataId,
        content: this.content,
      };
      params.load = true;

      this.$API.updateAdvice(params).then((res) => {
        if (res.success) {
          if (res.msg) this.$message.success(res.msg);
          // 详情弹窗关闭
          this.dialogTableVisible = false;
        }
      });
    },
    // 反馈记录查看详情
    handleClick(data) {
      this.infoDataId = data.id;
      this.$API
        .viewAdvice({ adviceId: this.infoDataId, load: true })
        .then((res) => {
          if (res) {
            this.infoData = res;
            // 详情弹窗开启
            this.dialogTableVisible = true;
          }
        });
    },
    // 获取反馈记录列表
    searchMethod(isSearch) {
      if (isSearch) this.pageNumber = 1;

      //改变提交格式
      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
        pageNumber: this.pageNumber,
        status: this.type,
      };
      params.load = true;
      this.$API.adviceList(params).then((res) => {
        if (res) {
          for (let i = 0; i < res.rows.length; i++) {
            res.rows[i].typeTxt =
              res.rows[i].sendType === 1
                ? this.$t("putAdvice")
                : this.$t("complain");
            res.rows[i].statusTxt =
              res.rows[i].status === 1
                ? this.$t("notReply")
                : this.$t("replied");
          }
          this.tableData = res.rows;
          this.total = res.total;
        }
      });
    },
  },
  watch: {
    dialogTableVisible() {
      if (!this.dialogTableVisible) this.content = "";
    },
    dialogShow() {
      if (!this.dialogShow) this.content = "";
    },
    date(e) {
      if (e == null) {
        this.date = [];
        this.date[0] = this.startTime;
        this.date[1] = this.endTime;
      }
    },
    tabVal() {
      if (this.tabVal == "2") this.searchMethod();
    },
  },
};
</script>

<style lang="scss">
@import "../../assets/css/order.scss";

.advice-page {
  width: 100%;
  .el-textarea__inner {
    background-color: $bgColor;
    color: #fff;
  }
  .el-tabs__nav-scroll {
    background-color: $black;
    border-bottom: 1px solid $borderColorJoker;
  }
  .el-tabs--border-card {
    border: 1px solid $borderColorJoker;
    background-color: $black;
    .el-tabs__header {
      .el-tabs__item.is-active {
        background-color: $bgColor;
        color: #ffffff;
        border-right-color: $borderColorJoker;
        border-left-color: $borderColorJoker;
      }
    }
    .el-tabs__content {
      background-color: $black;
    }
  }
  .cardBox {
    color: #656262;
    width: 90%;
    margin: 20px;
    border-radius: 10px;
    border: 1px solid $borderColorJoker;
    .card {
      display: flex;
      line-height: 150px;
      font-size: 18px;
      background-color: $black;
      padding: 10px;
      .item1 {
        flex: 1;
        text-align: center;
        line-height: 150px;
        .box {
          display: inline-flex;
          width: 60px;
          height: 60px;
          margin: 0 auto;
          background: $bgjoker;
          justify-content: center;
          align-items: center;
          flex-direction: column;
          color: #ffffff;
          i {
            font-size: 40px;
          }
        }
      }
      .item2 {
        flex: 3;
        display: flex;
        align-items: center;
        .text {
          line-height: 20px;
          span {
            display: block;
            margin-top: 5px;
            line-height: 25px;
            color: #aaa;
          }
        }
      }
      .item3 {
        flex: 1;
        color: #ffffff;
        text-align: center;
      }
    }
    .el-card__body {
      padding: 0;
    }
  }
  .pop {
    position: relative;
    position: absolute;
    width: 45%;
    height: 300px;
    background-color: $borderColorJoker;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%); /* 50%为自身尺寸的一半 */
    -webkit-transform: translate(-50%, -50%);
    border-radius: 10px;
    .close {
      position: absolute;
      right: 20px;
      top: 10px;
      width: 20px;
      height: 20px;
      cursor: pointer;
    }
    .title {
      margin: 20px;
    }
    textarea {
      background-color: #aaa;
      color: #fff;
      width: 80%;
      height: 60%;
      display: block;
      margin: 0 auto;
      border: 0;
    }
    .bottom {
      float: right;
      display: flex;
      margin-top: 15px;
      margin-right: 30px;
      div {
        width: 70px;
        height: 35px;
        line-height: 35px;
        background-color: $blue;
        text-align: center;
        margin-right: 30px;
        border-radius: 5px;
        border: none;
        cursor: pointer;
      }
    }
  }
  .line {
    background-color: $black;
    .cont {
      background-color: $black;
    }
  }
  .info-dialog {
    .oldCont {
      max-height: 240px;
      overflow-y: auto;
      margin-bottom: 10px;
      .line {
        padding: 5px;
      }
    }
  }
  textarea {
    font-family: inherit;
  }
}
</style>
