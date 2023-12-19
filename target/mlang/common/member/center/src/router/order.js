import store from "../store"

const OrderSport = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderSport" */ '../views/orderManage/OrderSport').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderEgame = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderEgame" */ '../views/orderManage/OrderEgame').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderLive = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderLive" */ '../views/orderManage/OrderLive').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderChess = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderChess" */ '../views/orderManage/OrderChess').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderEsport = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderEsport" */ '../views/orderManage/OrderEsport').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderFishing = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderFishing" */ '../views/orderManage/OrderFishing').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderEgamePt = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderEgamePt" */ '../views/orderManage/OrderEgamePt').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const OrderLottery = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "OrderEgamePt" */ '../views/orderManage/OrderLottery').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/orderSport',
    name: 'orderSport',
    component: OrderSport,
    meta: {
      index: 41,
      // type: '投注记录',
      // name: '体育注单',
      type: 'orderMenu',
      name: 'orderSport',
      openeds: '1'
    }
  },
  {
    path: '/orderEgame',
    name: 'orderEgame',
    component: OrderEgame,
    meta: {
      index: 42,
      // type: '投注记录',
      // name: '电子游艺注单',
      type: 'orderMenu',
      name: 'orderEgame',
      openeds: '1'
    }
  },
  {
    path: '/orderLive',
    name: 'orderLive',
    component: OrderLive,
    meta: {
      index: 43,
      // type: '投注记录',
      // name: '真人娱乐注单',
      type: 'orderMenu',
      name: 'orderLive',
      openeds: '1'
    }
  },
  {
    path: '/orderChess',
    name: 'orderChess',
    component: OrderChess,
    meta: {
      index: 44,
      // type: '投注记录',
      // name: '棋牌注单',
      type: 'orderMenu',
      name: 'orderChess',
      openeds: '1'
    }
  },
  {
    path: '/orderEsport',
    name: 'orderEsport',
    component: OrderEsport,
    meta: {
      index: 45,
      // type: '投注记录',
      // name: '电竞注单',
      type: 'orderMenu',
      name: 'orderEsport',
      openeds: '1'
    }
  },
  {
    path: '/orderFishing',
    name: 'orderFishing',
    component: OrderFishing,
    meta: {
      index: 46,
      // type: '投注记录',
      // name: '捕鱼注单',
      type: 'orderMenu',
      name: 'orderFishing',
      openeds: '1'
    }
  },
  {
    path: '/orderEgamePt',
    name: 'orderEgamePt',
    component: OrderEgamePt,
    meta: {
      index: 47,
      // type: '投注记录',
      // name: 'Pt电子注单',
      type: 'orderMenu',
      name: 'orderEgamePt',
      openeds: '1'
    }
  },
  {
    path: '/orderLottery',
    name: 'orderLottery',
    component: OrderLottery,
    meta: {
      index: 47,
      // type: '投注记录',
      // name: '彩票投注',
      type: 'orderMenu',
      name: 'lotBetAmount',
      openeds: '1'
    }
  },
]
