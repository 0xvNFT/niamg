import store from "../store"

const ViewTeam = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "ViewTeam" */ '../views/proxyManage/ViewTeam').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const UserList = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "UserList" */ '../views/proxyManage/UserList').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const PromotionUrl = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "PromotionUrl" */ '../views/proxyManage/PromotionUrl').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const MyRecommend = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "MyRecommend" */ '../views/proxyManage/MyRecommend').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const RegManage = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RegManage" */ '../views/proxyManage/RegManage').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const Recommend = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Recommend" */ '../views/proxyManage/Recommend').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const RecommendAgent = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RecommendAgent" */ '../views/proxyManage/RecommendAgent').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/viewTeam',
    name: 'viewTeam',
    component: ViewTeam,
    meta: {
      index: 31,
      // type: '代理管理',
      // name: '团队总览',
      type: 'agentMenu',
      name: 'viewTeam',
      openeds: '4'
    }
  },
  {
    path: '/userList',
    name: 'userList',
    component: UserList,
    meta: {
      index: 32,
      // type: '代理管理',
      // name: '用户列表',
      type: 'agentMenu',
      name: 'userList',
      openeds: '4'
    }
  },
  {
    path: '/promotionUrl',
    name: 'promotionUrl',
    component: PromotionUrl,
    meta: {
      index: 33,
      // type: '代理管理',
      // name: '推广注册',
      type: 'agentMenu',
      name: 'promotionUrl',
      openeds: '4'
    }
  },{
    path: '/myRecommend',
    name: 'myRecommend',
    component: MyRecommend,
    meta: {
      index: 33,
      // type: '代理管理',
      // name: '我的推荐',
      type: 'agentMenu',
      name: 'myRecommend',
      openeds: '4'
    }
  },
  {
    path: '/regManage',
    name: 'regManage',
    component: RegManage,
    meta: {
      index: 34,
      // type: '代理管理',
      // name: '注册管理',
      type: 'agentMenu',
      name: 'regManage',
      openeds: '4'
    }
  },
  {
    path: '/recommend',
    name: 'recommend',
    component: Recommend,
    meta: {
      index: 35,
      // type: '代理管理',
      // name: '推荐总览',
      type: 'agentMenu',
      name: 'recommend',
      openeds: '4'
    }
  },
  {
    path: '/recommendAgent',
    name: 'recommendAgent',
    component: RecommendAgent,
    meta: {
      index: 36,
      // type: '代理管理',
      // name: '我的推荐',
      type: 'agentMenu',
      name: 'recommendAgent',
      openeds: '4'
    }
  },
]
