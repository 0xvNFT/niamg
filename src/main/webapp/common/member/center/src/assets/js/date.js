var now = new Date(); //当前日期
var nowYear = now.getFullYear(); //当前年
var nowMonth = now.getMonth(); //当前月
var nowDay = now.getDate(); //当前日
var nowDayOfWeek = now.getDay(); //今天本周的第几天
var lastMonthDate = new Date(); //上月日期
lastMonthDate.setDate(1);
lastMonthDate.setMonth(lastMonthDate.getMonth() - 1);
// var lastYear = lastMonthDate.getYear();
var lastMonth = lastMonthDate.getMonth();

// 时间前面加 0
function addZero (date) {
  if (date < 10) return '0' + date;
  else return date;
}

function timeChange (timeStr, dataStr) {
  var tempDate = ''
  if (timeStr) tempDate = new Date(Number(timeStr));
  if (dataStr) tempDate = dataStr;
  var year = tempDate.getFullYear();
  var month = ("0" + (tempDate.getMonth() + 1)).slice(-2);
  var date = ("0" + tempDate.getDate()).slice(-2);
  var hour = ("0" + tempDate.getHours()).slice(-2);
  var minute = ("0" + tempDate.getMinutes()).slice(-2);
  var second = ("0" + tempDate.getSeconds()).slice(-2);
  var arr = [];
  arr.year = year;
  arr.month = month;
  arr.date = date;
  arr.hour = hour;
  arr.minute = minute;
  arr.second = second;

  return arr;
}

//格式化日期：yyyy-MM-dd
function formatDate (date) {
  // console.log(11, date)
  let year = date.getFullYear();
  let month = addZero(date.getMonth() + 1);
  let day = addZero(date.getDate());

  return year + "-" + month + "-" + day;
}

//获得某月的天数
function getMonthDays (myMonth) {
  let monthStartDate = new Date(nowYear, myMonth, 1);
  let monthEndDate = new Date(nowYear, myMonth + 1, 1);
  let days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
  return days;
}

export default {
  //获取今天
  today: function today () {
    let getCurrentDate = new Date();
    return formatDate(getCurrentDate);
  },
  //获取昨天
  yesterdayStart: function getYesterday () {
    let getYesterdayDate = new Date(nowYear, nowMonth, nowDay - 1);
    return formatDate(getYesterdayDate);
  },
  //获取前几天
  beforeDayStart: function getBeforeDay (day) {
    let getBeforeDayDate = new Date(nowYear, nowMonth, nowDay - day);
    return formatDate(getBeforeDayDate);
  },
  //获得本周的开始日期
  nowWeekStart: function getWeekStartDate () {
    let weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);
    return formatDate(weekStartDate);
  },
  //获得本周的结束日期
  nowWeekEnd: function getWeekEndDate () {
    let weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    return formatDate(weekEndDate);
  },
  //获得上周的开始日期
  lastWeekStart: function getLastWeekStartDate () {
    let weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek - 7);
    return formatDate(weekStartDate);
  },
  //获得上周的结束日期
  lastWeekEnd: function getLastWeekEndDate () {
    let weekEndDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek - 1);
    return formatDate(weekEndDate);
  },
  //获得本月的开始日期
  nowMonthStart: function getMonthStartDate () {
    let monthStartDate = new Date(nowYear, nowMonth, 1);
    return formatDate(monthStartDate);
  },
  //获得本月的结束日期
  nowMonthEnd: function getMonthEndDate () {
    let monthEndDate = new Date(nowYear, nowMonth, getMonthDays(nowMonth));
    return formatDate(monthEndDate);
  },
  //获得上月开始时间
  lastMonthStart: function getLastMonthStartDate () {
    let lastMonthStartDate = new Date(nowYear, lastMonth, 1);
    return formatDate(lastMonthStartDate);
  },
  //获得上月结束时间
  lastMonthEnd: function getLastMonthEndDate () {
    let lastMonthEndDate = new Date(nowYear, lastMonth, getMonthDays(lastMonth));
    return formatDate(lastMonthEndDate);
  },
  // 时间戳转换时间函数 年月日时分秒
  dateChange: function dateChange (timeStr) {
    if (timeStr) {
      let arr = timeChange(timeStr);
      return arr.year + "-" + arr.month + "-" + arr.date + " " + arr.hour + ":" + arr.minute + ":" + arr.second;
    } else {
      return '-'
    }
  },
  //时间戳转换时间函数 年月日
  yearMonth: function yearMonth (timeStr) {
    if (timeStr) {
      let arr = timeChange(timeStr);
      return arr.year + "-" + arr.month + "-" + arr.date + " ";
    } else {
      return '-'
    }
  },
  // 时间戳转换时间函数 时分
  hourMin: function hourMin (timeStr) {
    if (timeStr) {
      let arr = timeChange(timeStr);
      return arr.hour + ":" + arr.minute;
    } else {
      return '-'
    }
  },
  // data转换时间函数 年月日时分秒
  dateChangeStr: function dateChangeStr (timeStr) {
    if (timeStr) {
      let arr = timeChange(null, timeStr);
      return arr.year + "-" + arr.month + "-" + arr.date + " " + arr.hour + ":" + arr.minute + ":" + arr.second;
    } else {
      return '-'
    }
  },
}