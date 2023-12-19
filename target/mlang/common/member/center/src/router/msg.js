import store from "../store"

const Message = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Message" */ '../views/msgManage/Message').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const Notice = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Notice" */ '../views/msgManage/Notice').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const Advice = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Advice" */ '../views/msgManage/Advice').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/message',
    name: 'message',
    component: Message,
    meta: {
      index: 11,
      // type: '短信公告',
      // name: '站内短信',
      type: 'msgMenu',
      name: 'message',
      openeds: '5'
    }
  },
  {
    path: '/notice',
    name: 'notice',
    component: Notice,
    meta: {
      index: 12,
      // type: '短信公告',
      // name: '网站公告',
      type: 'msgMenu',
      name: 'notice',
      openeds: '5'
    }
  },
  {
    path: '/advice',
    name: 'advice',
    component: Advice,
    meta: {
      index: 13,
      // type: '短信公告',
      // name: '用户反馈',
      type: 'msgMenu',
      name: 'advice',
      openeds: '5'
    }
  },
]
