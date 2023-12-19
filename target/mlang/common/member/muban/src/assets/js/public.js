// 获取地址栏参数
function getQuery(name) {
  let query = window.location.href.split("#")[1];
  if (query) query = query.split("?")[1];
  if (!query) return "";

  let vars = query.split("&");
  for (let i = 0; i < vars.length; i++) {
    let pair = vars[i].split("=");
    if (pair[0] == name) {
      return pair[1];
    }
  }
  return "";
}

// 小数点后保留位数
function retainNumFun(num, wei = 2) {
  if (!isNaN(num) && String(num).indexOf(".") != -1) {
    var str = String(num).split(".");
    if (wei === "no") return parseFloat(str[0]);
    if (str[1]) {
      num = parseFloat(str[0] + "." + str[1].substring(0, wei));
    }
  }
  //  else num = num + '.00'
  return num;
}

// 复制
function copyFun(el) {
  // 创建range对象
  const range = document.createRange();
  //获取复制内容的 id 选择器
  range.selectNode(document.getElementById(el));
  //创建 selection对象
  const selection = window.getSelection();
  //如果页面已经有选取了的话，会自动删除这个选区，没有选区的话，会把这个选取加入选区
  if (selection.rangeCount > 0) selection.removeAllRanges();
  //将range对象添加到selection选区当中，会高亮文本块
  selection.addRange(range);
  //复制选中的文字到剪贴板
  document.execCommand("copy");
  // 移除选中的元素
  selection.removeRange(range);
}

// 线上图片地址
function imgUrlFun(name) {
  return "/common/member/images/muban/" + name;
}

// 重置查询表单的数据 (不需要重置的请将其作为参数传进来)
function resetForm(needClone) {
  let originalData = this.$options.data();
  let cloneData = this[needClone] || null; // 重置前需要备份的数据（如果data里有不需重置的初始值）
  delete originalData.tableData; // 刷新查询结果之前不用重置表格的数据避免页面煽动问题
  Object.assign(this.$data, originalData);
  if (cloneData) {
    this[needClone] = cloneData;
  }
  // 重置后刷新查询结果
  if (this.searchMethod) {
    this.searchMethod();
  }
}

//获取cookie
function getCookie(cookieName) {
  let cookieVal = "";
  if (document.cookie) {
    let cookies = document.cookie.split(";");
    cookies.filter((item) => {
      if (item.includes(cookieName)) {
        cookieVal = item.split(cookieName + "=")[1];
      }
    });
  }
  return cookieVal;
}

export default {
  getQuery,
  retainNumFun,
  copyFun,
  imgUrlFun,
  resetForm,
  getCookie,
};
