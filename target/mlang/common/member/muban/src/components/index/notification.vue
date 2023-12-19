<template>
  <el-dialog :visible.sync="showFlag" style="text-align: center" :show-close="false" class="common_dialogs">
    <div class="box">
      <div class="notify-title">
        <div class="website">{{ $t('notice') }}</div>
        <div class="close" @click="showFlag = false"><img src="../../assets/images/notify-close.png" alt=""></div>
      </div>
      <div class="notify-body">
        <div class="lfetText">
          <p class="titleBox" v-for="(item, i) in notiveList" @click="choose(i)" :key="i"
            :class="chooseIndex === i ? 'chooseClass' : ''">
            <span>
              {{ item.title }}
            </span>
          </p>
        </div>
        <div class="content">
          <p class="title_p">{{ titleText }}</p>
          <div class="content_div" v-html="content"></div>
        </div>
      </div>
    </div>

  </el-dialog>
</template>

<script>
import { mapState } from "vuex";
export default {
  data() {
    return {
      showFlag: false,
      notiveList: [], //网站公告数据
      content: "", //详情
      chooseIndex: 0, //默认第一个选中
      titleText: '',//内容标题
    };
  },
  computed: {
    ...mapState(["userInfo"]),
  },
  components: {},
  created() { },
  mounted() {
    //网站公告
    this.$API.articleList().then((res) => {
      if (res) {
        // popupStatus===2 为需要弹出的数据
        res.rows.filter((item) => {
          if (item.popupStatus && item.popupStatus === 2) {
            this.notiveList.push(item);
          }
        });
        if (this.notiveList.length) {
          this.choose(0);
          this.showFlag = true;
        }
      }
    });
  },
  activated() { },
  methods: {
    choose(key) {
      this.content = this.notiveList[key].content;
      this.titleText = this.notiveList[key].title;
      this.chooseIndex = key;
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.common_dialogs {
  .el-dialog {
    height: 570px;
    width: 700px;
    box-shadow: none;
    background: no-repeat center/100% url('../../assets/images/notify-Bg.png');

    .el-dialog__body {
      padding: 0px 20px;
      height: 90%;

      .box {
        display: flex;
        flex-direction: column;
        height: 100%;
        margin-top: -15px;

        .notify-title {
          height: 10%;

          .website {
            font-size: 24px;
            color: #fff;
            font-weight: 800;
            width: 50%;
          }

          .close {
            img {
              width: 15%;
              position: absolute;
              top: -15px;
              right: 0;
              cursor: pointer;
            }
          }
        }

        .notify-body {
          display: flex;
          margin: 10px 10px 10px 0px;
          height: 90%;

          .lfetText {
            width: 25%;
            // border-right: 1px solid #fff;
            overflow: auto;
            margin-top: 20px;
          }

          ::-webkit-scrollbar-track {
            display: none;
          }

          .titleBox {
            text-align: center;
            cursor: pointer;
            color: #C6CDD6;

            font-size: 16px;
            padding: 10px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: 90%;
            margin-bottom: 20px;
          }

          .titleBox:hover {
            color: #ffffff;
            background-image: linear-gradient(to right, #0E2E53, #5D7FAF);
            overflow: hidden;
            text-overflow: ellipsis;
            width: 90%;
            white-space: nowrap;
            border-radius: 10px;
          }

          .chooseClass {
            color: #ffffff;
            background-image: linear-gradient(to right, #0E2E53, #5D7FAF);
            overflow: hidden;
            text-overflow: ellipsis;
            width: 90%;
            white-space: nowrap;
            border-radius: 10px;
          }

          .content {
            overflow-y: auto;
            width: 75%;
            color: rgba(255, 255, 255, 0.85);
            padding: 18px;
            display: flex;
            flex-direction: column;
            height: 100%;

            .title_p {
              margin-top: 20px;
              font-size: 22px;
              font-weight: 600;
            }

            .content_div {
              margin-top: 20px;
            }
          }
        }
      }
    }
  }
}
</style>
