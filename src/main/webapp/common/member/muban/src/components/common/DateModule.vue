<template>
  <div class="dateModule">
    <!-- 时间：
      <el-date-picker v-model="startTime" type="datetime" placeholder="选择日期时间">
      </el-date-picker>
      至
      <el-date-picker v-model="endTime" type="datetime" placeholder="选择日期时间" default-time="23:59:59">
      </el-date-picker> -->
    <!-- <el-date-picker v-model="dataVal" type="datetimerange" size="small" :start-placeholder="$t('startTime')" :end-placeholder="$t('endTime')" :default-time="['00:00:00', '23:59:59']" :editable="false" :clearable="false" :popper-class="showClear ? 'showClear':''">
    </el-date-picker> -->
    <el-button-group style="margin-left: 10px" v-if="fastBtn">
      <el-button type="success" size="small" @click="yesterday">{{$t("yesterday")}}</el-button>
      <el-button type="success" size="small" @click="today">{{$t("today")}}</el-button>
      <el-button type="success" size="small" @click="lastWeek">{{$t("lastWeek")}}</el-button>
      <el-button type="success" size="small" @click="nowWeek">{{$t("nowWeek")}}</el-button>
      <el-button type="success" size="small" @click="lastMonth">{{$t("lastMonth")}}</el-button>
      <el-button type="success" size="small" @click="nowMonth">{{$t("nowMonth")}}</el-button>
    </el-button-group>
    <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 5px" @click="search">{{$t("searchText")}}</el-button>
  </div>
</template>

<script>
// import { mapState } from 'vuex'
import DATE from "@/assets/js/date";

export default {
  data () {
    return {
      // dataVal: this.$route.path == '/userList' ? '' : [DATE.today() + ' 00:00:00', DATE.today() + ' 23:59:59'], //会员列表 默认时间为空
      dataVal: []
    };
  },
  props: {
    fastBtn: '',
    showClear: false,
    date: '',
  },
  computed: {
    // ...mapState(['nextAlert', 'onOffBtn'])
  },
  components: {
  },
  created () {
    console.log()
  },
  methods: {
    yesterday () {
      this.dataVal = [DATE.yesterdayStart() + ' 00:00:00', DATE.yesterdayStart() + ' 23:59:59']
      this.search()
    },
    today () {
      this.dataVal = [DATE.today() + ' 00:00:00', DATE.today() + ' 23:59:59']
      this.search()
    },
    lastWeek () {
      this.dataVal = [DATE.lastWeekStart() + ' 00:00:00', DATE.lastWeekEnd() + ' 23:59:59']
      this.search()
    },
    nowWeek () {
      this.dataVal = [DATE.nowWeekStart() + ' 00:00:00', DATE.nowWeekEnd() + ' 23:59:59']
      this.search()
    },
    lastMonth () {
      this.dataVal = [DATE.lastMonthStart() + ' 00:00:00', DATE.lastMonthEnd() + ' 23:59:59']
      this.search()
    },
    nowMonth () {
      this.dataVal = [DATE.nowMonthStart() + ' 00:00:00', DATE.nowMonthEnd() + ' 23:59:59']
      this.search()
    },
    search () {
      let start = this.dataVal[0]
      let end = this.dataVal[1]

      if (start instanceof Date) {
        start = DATE.dateChangeStr(this.dataVal[0])
        end = DATE.dateChangeStr(this.dataVal[1])
      }
      if (this.dataVal) {
        this.$emit('searchMethod', start, end, true);
      } else {
        this.$emit('searchMethod', '', '', true);
      }
      // this.$parent.searchMethod(this.dataVal[0], this.dataVal[1], true);
    }
  },
  watch: {
    date () {
      this.dataVal = this.date
    },
  }
};
</script>

<style lang="scss">
.dateModule {
  padding: 5px 0;
  margin-bottom: 10px;
  // display: flex;
  // flex-wrap: wrap;
  align-items: center;
  .el-date-editor--datetimerange.el-input__inner {
    width: 335px;
  }
}
</style>


