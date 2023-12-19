<template>
  <el-dialog :visible.sync="showFlag" style="text-align: center" :show-close="false" class="dialog_notify">
    <div class="box">
      <div class="notify-title">
        <div class="notify-Gg" style="display: flex;">
          <p>{{ $t('notice') }}</p>
        </div>
        <div class="dialog-close">
          <img src="@images/notify-close" @click="showFlag = false" alt="">
        </div>
      </div>
      <div class="notify-button">
        <div class="lfetText">
          <p class="titleBox" v-for="(item, i) in notiveList" @click="choose(i)" :key="i"
            :class="chooseIndex === i ? 'chooseClass' : ''">
            <span>
              {{ item.title }}
            </span>
          </p>
        </div>
        <div class="content" :key="upData">
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
      upData: false
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
      // 切换后 数据重新渲染
      this.upData = !this.upData
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.dialog_notify {
  .el-dialog {
    background: transparent;
    box-shadow: none;
    width: 44%;
    min-width: 520px;

    .el-dialog__body {
      padding: 0;
    }

    .box {
      display: flex;
      flex-direction: column;

      .notify-title {
        background-image: url("../../assets/images/notify-title");
        background-repeat: no-repeat;
        background-size: 100% 100%;
        display: flex;
        align-items: center;
        height: 84px;

        .notify-Gg {
          width: 90%;
          color: $borderColor;
          font-size: 22px;
          font-weight: 800;

          p {
            width: 50%;
          }
        }

        .dialog-close {
          width: 10%;
          height: 33px;
          cursor: pointer;

          img {
            width: 32px;
          }

          img:hover {
            opacity: 0.5;
          }
        }

      }

      .notify-button {
        height: 550px;
        background-image: url("../../assets/images/notify-body");
        background-repeat: no-repeat;
        background-size: 100% 100%;
        display: flex;

        .lfetText {
          width: 25%;
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
          color: $borderColor;
          background-image: linear-gradient(to right, #0E2E53, #5D7FAF);
          overflow: hidden;
          text-overflow: ellipsis;
          width: 90%;
          white-space: nowrap;
          border-radius: 10px;
        }

        .chooseClass {
          color: $borderColor;
          background-image: linear-gradient(to right, #0E2E53, #5D7FAF);
          overflow: hidden;
          text-overflow: ellipsis;
          width: 90%;
          white-space: nowrap;
          border-radius: 10px;
        }

        .content {
          overflow-y: auto;
          width: 72%;
          color: $borderColor;
          padding: 20px;
          display: flex;
          flex-direction: column;
          height: 95%;
          margin: 10px 10px 10px 2px;


          .title_p {
            color: #3FD1FF;
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
</style>
