const OrderSport = () => import( /* webpackChunkName: "order" */ '../views/order/OrderSport')
const OrderEgame = () => import( /* webpackChunkName: "order" */ '../views/order/OrderEgame')
const OrderEgamePt = () => import( /* webpackChunkName: "order" */ '../views/order/OrderEgamePt')
const OrderLive = () => import( /* webpackChunkName: "order" */ '../views/order/OrderLive')
const OrderChess = () => import( /* webpackChunkName: "order" */ '../views/order/OrderChess')
const OrderEsport = () => import( /* webpackChunkName: "order" */ '../views/order/OrderEsport')
const OrderFishing = () => import( /* webpackChunkName: "order" */ '../views/order/OrderFishing')
const OrderLottery = () => import( /* webpackChunkName: "order" */ '../views/order/OrderLottery')

export default [
  {
    path: '/orderSport',
    name: 'orderSport',
    component: OrderSport,
    meta: {
      index: 70,
    }
  },
  {
    path: '/orderEgame',
    name: 'orderEgame',
    component: OrderEgame,
    meta: {
      index: 71,
    }
  },
  {
    path: '/orderEgamePt',
    name: 'orderEgamePt',
    component: OrderEgamePt,
    meta: {
      index: 76,
    }
  },
  {
    path: '/orderLive',
    name: 'orderLive',
    component: OrderLive,
    meta: {
      index: 72,
    }
  },
  {
    path: '/orderChess',
    name: 'orderChess',
    component: OrderChess,
    meta: {
      index: 73,
    }
  },
  {
    path: '/orderEsport',
    name: 'orderEsport',
    component: OrderEsport,
    meta: {
      index: 74,
    }
  },
  {
    path: '/orderFishing',
    name: 'orderFishing',
    component: OrderFishing,
    meta: {
      index: 75,
    }
  },
  {
    path: '/orderLottery',
    name: 'orderLottery',
    component: OrderLottery,
    meta: {
      index: 77,
    }
  },
]
