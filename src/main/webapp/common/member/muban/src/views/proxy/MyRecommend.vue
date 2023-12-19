<template>
  <div>
    <div class="heard">
      {{ $t("myRecommend") }}
    </div>
    <el-tabs
      type="border-card"
      class="card_text"
      v-model="tabIndex"
      @tab-click="handleClick"
    >
      <!-- 邀请总览 -->
      <el-tab-pane name="1">
        <div class="label_span" slot="label">
          <p class="label_icon">
            <i class="el-icon-s-custom"></i>
          </p>
          <p>{{ $t("promotionInfo") }}</p>
        </div>

        <div class="content">
          <!--左边 推广方式 -->
          <div class="promotion">
            <p class="partner">{{ $t("invitehb") }}</p>
            <div class="partner_link">
              <label>{{ $t("invitationLink") }}：</label>
              <div class="promotion_link">
                <el-tooltip
                  effect="dark"
                  :content="dataList.prompInfo.linkUrl"
                  placement="bottom"
                >
                  <p id="linkUrl" class="Ljie">
                    {{ dataList.prompInfo.linkUrl }}
                  </p>
                </el-tooltip>
                <p class="tag-copy" type="primary" @click="copyName('linkUrl')">
                  <i class="el-icon-copy-document"></i>
                </p>
              </div>
            </div>
            <div class="partner_link">
              <label>{{ $t("invitationCode") }}：</label>
              <div class="promotion_link">
                <p id="code" class="Ljie">{{ dataList.prompInfo.code }}</p>
                <p class="tag-copy" type="primary" @click="copyName('code')">
                  <i class="el-icon-copy-document"></i>
                </p>
              </div>
            </div>
            <div class="goobook_link">
              <a
                :href="onOffBtn.instagram_url || 'javascript:;'"
                :target="onOffBtn.instagram_url ? '_blank' : '_self'"
              >
                <img src="@images/register/instagram" alt="" />
              </a>
              <a
                :href="onOffBtn.facebook_url || 'javascript:;'"
                :target="onOffBtn.facebook_url ? '_blank' : '_self'"
              >
                <img src="@images/register/facebook.jpg" alt="" />
              </a>
            </div>
          </div>
          <!-- 右边内容 -->
          <div class="right-content">
            <!-- 右边 上面内容 -->
            <div class="right-text">
              <ul>
                <li>
                  <p class="num">
                    {{ dataList.totalInvitePerson }}
                  </p>
                  <p class="type">
                    {{ $t("totalInvite") }}
                  </p>
                </li>

                <li>
                  <p class="num">
                    {{ moneyUnit }}&nbsp;{{ dataList.monthCommission | amount }}
                  </p>
                  <p class="type">
                    {{ $t("monthCommission") }}
                  </p>
                </li>
                <li>
                  <p class="num">
                    {{ moneyUnit }}&nbsp;{{
                      dataList.totalInviteBonus | amount
                    }}
                  </p>
                  <p class="type">
                    {{ $t("totalIncome") }}
                  </p>
                </li>
                <li>
                  <p class="num">
                    {{ dataList.todayInvitePerson }}
                  </p>
                  <p class="type">
                    {{ $t("todayInvite") }}
                  </p>
                </li>

                <li>
                  <p class="num">
                    {{ moneyUnit }}&nbsp;{{
                      dataList.monthInviteBonus | amount
                    }}
                  </p>
                  <p class="type">
                    {{ $t("monthInviteBonus") }}
                  </p>
                </li>
                <li>
                  <p class="num">
                    {{ moneyUnit }}&nbsp;{{
                      dataList.todayInviteBonus | amount
                    }}
                  </p>
                  <p class="type">
                    {{ $t("todayIncome") }}
                  </p>
                </li>
              </ul>
            </div>
            <!--右边 下面 邀请语 -->
            <div class="join">
              <p class="join_img">
                <img src="@images/jiang" alt="" />
              </p>
              <div class="join_text">
                <p style="font-size: 18px">{{ $t("join") }}</p>
                <p class="join_text_p2">{{ $t("joinText") }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
      <!-- 存款信息 -->
      <el-tab-pane name="2">
        <div class="label_span" slot="label">
          <p class="label_icon">
            <i class="el-icon-s-finance"></i>
          </p>
          <p>{{ $t("depositInfo") }}</p>
        </div>
        <div class="searchDiv">
          <div class="item">
            <el-date-picker
              v-model="date"
              type="datetimerange"
              :start-placeholder="startTime"
              :end-placeholder="endTime"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
            <el-button
              type="primary"
              size="medium"
              icon="el-icon-search"
              style="margin-left: 15px"
              @click="searchMethod"
              >{{ $t("searchText") }}</el-button
            >
          </div>
        </div>
        <el-table :data="tableData" style="width: 100%">
          <div slot="empty">
            <el-empty
              :image="require('@images/nodata')"
              :image-size="128"
              :description="$t('noData')"
            ></el-empty>
          </div>
          <el-table-column :label="$t('dataText')">
            <template slot-scope="scope">
              {{ dataFun(scope.row.createDatetime) }}
            </template>
          </el-table-column>
          <el-table-column prop="username" :label="$t('userName')">
          </el-table-column>
          <el-table-column :label="$t('depositAmount')">
            <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium"
                  >{{ moneyUnit }}&nbsp;{{ scope.row.depositMoney }}</el-tag
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('realBettingMoney')">
            <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium"
                  >{{ moneyUnit }}&nbsp;{{ scope.row.thirdBetAmount }}</el-tag
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <!-- 奖金信息 -->
      <el-tab-pane name="3">
        <div class="label_span" slot="label">
          <p class="label_icon">
            <i class="el-icon-s-data"></i>
          </p>
          <p>{{ $t("bonusInfo") }}</p>
        </div>
        <div class="searchDiv">
          <div class="item">
            <el-date-picker
              v-model="date"
              type="datetimerange"
              :start-placeholder="startTime"
              :end-placeholder="endTime"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
            <el-button
              type="primary"
              size="medium"
              icon="el-icon-search"
              style="margin-left: 15px"
              @click="searchMethod"
              >{{ $t("searchText") }}</el-button
            >
          </div>
        </div>
        <el-table :data="tableData" style="width: 100%">
          <div slot="empty">
            <el-empty
              :image="require('@images/nodata')"
              :image-size="128"
              :description="$t('noData')"
            ></el-empty>
          </div>
          <el-table-column :label="$t('dataText')">
            <template slot-scope="scope">
              {{ dataFun(scope.row.createDatetime) }}
            </template>
          </el-table-column>
          <el-table-column prop="username" :label="$t('userName')">
          </el-table-column>
          <el-table-column prop="bonusTypeText" :label="$t('bonusType')">
          </el-table-column>
          <el-table-column :label="$t('bonus')">
            <template slot-scope="scope">
              <div slot="reference" class="name-wrapper">
                <el-tag size="medium"
                  >{{ moneyUnit }}&nbsp;{{ scope.row.money }}</el-tag
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapState } from "vuex";
import DATE from "@/assets/js/date.js";
export default {
  data() {
    return {
      tabIndex: "1",
      // 邀请总览
      dataList: {
        prompInfo: {},
      },
      moneyUnit: "", //货币单元
      // 推广管理 or 奖金信息
      date: [], //日期
      startTime: DATE.today() + " 00:00:00", //搜索开始时间
      endTime: DATE.today() + " 23:59:59", //搜索结束时间
      tableData: [],
      // total: 0,
      // pageNumber: 1, //当前页码
    };
  },
  computed: {
    ...mapState(["onOffBtn"]),
  },
  components: {},
  mounted() {
    this.getInviteOverview();
  },
  methods: {
    // 复制
    copyName(val) {
      this.$publicJs.copyFun(val);
    },
    //tabs 标签页
    handleClick() {
      this.searchMethod();
    },
    // 时间函数
    dataFun(val) {
      return DATE.dateChange(val);
    },

    //获取邀请人获得的返佣概况
    getInviteOverview() {
      this.$API.getInviteOverview().then((res) => {
        if (res) {
          this.dataList = res.content;
          this.moneyUnit = res.moneyUnit;
        }
      });
    },
    // 搜索
    searchMethod() {
      //清空原有赋值
      this.tableData = [];
      let params = {
        startTime: this.date.length ? this.date[0] : this.startTime,
        endTime: this.date.length ? this.date[1] : this.endTime,
      };
      params.load = true;

      if (this.tabIndex === "2") {
        //获取被邀请人的存款信息列表
        this.$API.getInviteDeposits(params).then((res) => {
          if (res) {
            this.tableData = res.content;
          }
        });
      } else if (this.tabIndex === "3") {
        //获取被邀请人的奖金信息列表
        this.$API
          .getInviteBonus({
            startDate: this.date[0] || this.startTime,
            " endDate": this.date[1] || this.endTime,
            load: true,
          })
          .then((res) => {
            if (res) {
              this.tableData = res.content;
            }
          });
      }
    },
  },
  watch: {
    date(e) {
      if (e == null) {
        this.date = [];
        this.date[0] = this.startTime;
        this.date[1] = this.endTime;
      }
    },
  },
};
</script>

<style lang="scss" >
.card_text {
  background: transparent;
  margin: 0 50px 0 50px;
  border: none;
  border-radius: 20px;
  box-shadow: none;

  .el-tabs__header {
    border: none;
    background: linear-gradient(180deg, rgba(28, 37, 50, 0), #1c2532);
    border-radius: 10px;
    width: 100%;

    .el-tabs__nav {
      width: 100%;
      display: flex;
      text-align: center;

      .el-tabs__item {
        height: 100%;
        width: 33.3%;

        .label_span {
          color: #fff;
          font-size: 14px;
          font-weight: 800;
          display: flex;
          flex-direction: column;
          margin-top: 15px;

          .label_icon {
            line-height: 0;

            i {
              font-size: 32px;
              opacity: 0.5;
            }
          }
        }
      }

      .is-active {
        width: 34%;
        background-color: $bgColor;
        border-right-color: $bgColor;
        border-left-color: $bgColor;
        border-bottom-color: $bgColor;
      }
    }
  }

  .el-tabs--border-card {
    padding: 0;
  }

  .el-tabs__content {
    background: #1c2532;
    margin-top: 30px;
    border-radius: 10px;
    width: 100%;

    .el-tab-pane {
      .content {
        display: flex;
        text-align: center;
        align-items: center;
        justify-content: space-around;

        .promotion {
          width: 20%;
          background: linear-gradient(180deg, #151c26, #003a8d);
          height: 350px;
          border-radius: 15px;
          display: flex;
          flex-direction: column;
          text-align: left;
          padding: 20px;

          .partner {
            color: #fce90b;
            font-size: 24px;
            height: 35%;
          }

          .partner_link {
            height: 50%;
            display: flex;
            flex-direction: column;

            .promotion_link {
              display: flex;
              align-items: center;
              // height: 85%;
              font-size: 10px;

              .Ljie {
                line-height: 12px;
                background: #151c26;
                padding: 10px;
                width: 100%;
                border-radius: 6px;
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
              }

              .tag-copy {
                background: #1a55ef;
                border-radius: 0.25rem;
                cursor: pointer;
                display: flex;
                align-items: center;
                height: 30px;
                justify-content: center;
                min-width: 28px;
                color: #fff;
                margin-left: -8px;

                i {
                  font-size: 22px;
                }
              }
            }

            label {
              font-size: 12px;
              font-weight: 600;
              padding: 0 0 2px 4px;
            }
          }

          .goobook_link {
            img {
              width: 30px;
              margin: 10px;
              border-radius: 5px;
              opacity: 0.8;

              &:hover {
                opacity: 1;
              }
            }
          }
        }

        .right-content {
          // margin: 0 0 0 30px;
          width: 75%;

          .right-text {
            background: #151c26;
            border-radius: 10px;

            .bg-purple {
              .refresh {
                line-height: 40px;
                font-size: 16px;
                padding-left: 10px;
                background: $borderColorJoker;
                color: #fff;
                border-radius: 10px 10px 0 0;
              }
            }

            ul {
              display: flex;
              flex-direction: row;
              flex-wrap: wrap;

              li {
                width: 33.3%;
                height: 90px;
                display: flex;
                flex-direction: column;
                justify-content: center;

                .num {
                  font-size: 16px;
                  font-weight: 700;
                  color: #088cffc7;
                }

                .type {
                  font-size: 14px;
                  font-weight: 600;
                  margin-top: 8px;
                }
              }
            }
          }

          .join {
            margin-top: 20px;
            font-size: 16px;
            font-weight: 600;
            background: linear-gradient(95deg, #151c26, #003a8d);
            border-radius: 10px;
            display: flex;

            .join_img {
              width: 30%;
            }

            .join_text {
              padding: 0 30px;
            }

            .join_text_p2 {
              text-align: left;
              margin: 0 0 20px 0;
            }

            p {
              margin: 20px 0;
            }
          }
        }
      }
    }
  }
}
</style>
