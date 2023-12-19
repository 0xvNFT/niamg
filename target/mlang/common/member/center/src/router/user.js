import store from "../store"

const SecurityCenter = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "SecurityCenter" */ '../views/userManage/SecurityCenter').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const MoneyChange = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "MoneyChange" */ '../views/userManage/MoneyChange').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const MoneyChangeHis = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "MoneyChangeHis" */ '../views/userManage/MoneyChangeHis').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const BankIndex = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "BankIndex" */ '../views/userManage/BankIndex').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const UsdtIndex = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "UsdtIndex" */ '../views/userManage/UsdtIndex').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const ScoreExchange = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "ScoreExchange" */ '../views/userManage/ScoreExchange').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderBetHis = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderBetHis" */ '../views/userManage/OrderBetHis').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/securityCenter',
    name: 'securityCenter',
    component: SecurityCenter,
    meta: {
      index: 2,
      // type: '账户管理',
      // name: '安全中心',
      type: 'accountMeun',
      name: 'securityCenter',
      openeds: '3'
    }
  },
  {
    path: '/moneyChange',
    name: 'moneyChange',
    component: MoneyChange,
    meta: {
      index: 3,
      // type: '账户管理',
      // name: '额度转换',
      type: 'accountMeun',
      name: 'moneyChange',
      openeds: '3'
    }
  },
  {
    path: '/moneyChangeHis',
    name: 'moneyChangeHis',
    component: MoneyChangeHis,
    meta: {
      index: 4,
      // type: '账户管理',
      // name: '额度转换记录',
      type: 'accountMeun',
      name: 'moneyChangeHis',
      openeds: '3'
    }
  },
  {
    path: '/bankIndex',
    name: 'bankIndex',
    component: BankIndex,
    meta: {
      index: 5,
      // type: '账户管理',
      // name: '银行卡管理',
      type: 'accountMeun',
      name: 'bankIndex',
      openeds: '3'
    }
  },
  {
    path: '/usdtIndex',
    name: 'usdtIndex',
    component: UsdtIndex,
    meta: {
      index: 6,
      // type: '账户管理',
      // name: '银行卡管理',
      type: 'accountMeun',
      name: 'usdtIndex',
      openeds: '3'
    }
  },
  {
    path: '/scoreExchange',
    name: 'scoreExchange',
    component: ScoreExchange,
    meta: {
      index: 7,
      // type: '账户管理',
      // name: '积分兑换',
      type: 'accountMeun',
      name: 'scoreExchange',
      openeds: '3'
    }
  },
  {
    path: '/orderBetHis',
    name: 'orderBetHis',
    component: OrderBetHis,
    meta: {
      index: 8,
      // type: '账户管理',
      // name: '打码量记录',
      type: 'accountMeun',
      name: 'orderBetHis',
      openeds: '3'
    }
  },
]
