import store from "../store";
import { i18n } from "../lang/i18n";
import { Message } from 'element-ui';

// 金额输入框校验
export const amountFormat = {
  bind (el, binding, vnode) {
    if (i18n.locale === 'vi') {
      // 越南盾添加千位分隔符
      el.addEventListener('input', (e) => {
        let money = e.target.value;
        money = money.replace(/[^0-9]/g, ''); // 去除文本或其他字符
        const formattedMoney = money ? Number(money).toLocaleString() : money; // 为金额添加千位分隔符
        e.target.value = formattedMoney; // 更新输入框的值
        vnode.context[binding.arg] = formattedMoney; // 同时更新绑定的data
      });
    } else {
      // 其他币种禁止输入字符
      el.children.forEach((htmlTag) => {
        if (htmlTag.localName === 'input') {
          htmlTag.setAttribute('type', 'number');
        };
      });
    };
  },
};

//试玩账号禁止操作
export const banned = {
  inserted (el, binding, vnode) {
    setTimeout(() => {
      // 是否是试玩账号
      let isDemoAcc = store.state.userInfo.type === 150 || store.state.userInfo.type === 160;

      if (!isDemoAcc) return //不是试玩 不做任何处理

      if (binding.arg && binding.arg === 'display') {
        // 方式 1：指定的节点不显示（传参 display）
        el.parentNode && el.parentNode.removeChild(el);
      } else if (binding.arg && binding.arg === 'click') {
        // 方式 2：阻止节点的绑定事件（传参 click）
        vnode.context[binding.expression] =  addWarnning();
      } else {
        // 方式 3：指定的节点禁止跳转到对应的页面（用在a标签）
        el.removeAttribute('href'); // 从节点上移除绑定的路由地址
        el.style.cursor = 'pointer';
        addWarnning(el);
      };
    })
  },
};

/**
 * @description: “无权限”提示的函数
 * @param {*} element 如果传el节点会将提示函数绑定到节点
 * @return {*} 否则返回提示函数
 */
function addWarnning (element) {
  try {
    element.addEventListener('click', () => {
      Message.error(i18n.t("authority"));
    })
  }
  catch {
    return function () {
      Message.error(i18n.t("authority"));
    }
  }
};

