/**
 * 工具类
 */

// 模态框标签
let markEl = `<div id="modal-mark"></div>`;

export default {
  /**
   * 
   * @param {*} word   待加密或者解密的字符串
   */
  //加密
  encrypt (word) {
    // 对字符串进行编码
    let encode = encodeURI(word);
    // 对编码的字符串转化base64
    let base64 = btoa(encode);
    return base64;
  },

  //解密
  decrypt (word) {
    // 对base64转编码
    var decode = atob(word);
    // 编码转字符串
    var str = decodeURI(decode);
    return str;
  },  
}

/**
 * @description: 给弹出框垫背模糊效果
 * @param {*} on 该参数为true时设置否则移除
 * @return {*}
 */
export function setModalMark (on) {
  let appEl = document.querySelector('#app');
  let fakeEl = document.createElement('div');
  fakeEl.innerHTML = markEl; //转成Node
  if (on) {
    // 模态框元素加到根节点
    appEl.appendChild(fakeEl.firstChild);
  } else {
    // 移除模态框元素
    document.querySelector('#modal-mark').remove();
  }
}
