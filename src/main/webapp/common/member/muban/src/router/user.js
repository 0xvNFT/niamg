const SecurityCenter = () => import( /* webpackChunkName: "user" */ '@/views/user/SecurityCenter')
const MoneyChange = () => import( /* webpackChunkName: "user" */ '@/views/user/MoneyChange')
const MoneyChangeHis = () => import( /* webpackChunkName: "user" */ '@/views/user/MoneyChangeHis')
const BankIndex = () => import( /* webpackChunkName: "user" */ '@/views/user/BankIndex')
const UsdtIndex = () => import( /* webpackChunkName: "user" */ '@/views/user/UsdtIndex')
const ScoreExchange = () => import( /* webpackChunkName: "user" */ '@/views/user/ScoreExchange')
const OrderBetHis = () => import( /* webpackChunkName: "user" */ '@/views/user/OrderBetHis')
const Banking = () => import( /* webpackChunkName: "active" */ '@/views/user/Banking')

export default [
  {
    path: '/securityCenter', //安全中心
    name: 'securityCenter',
    component: SecurityCenter,
    meta: {
      index: 56,
    }
  },
  {
    path: '/moneyChange', //额度转换
    name: 'moneyChange',
    component: MoneyChange,
    meta: {
      index: 55,
    }
  },
  {
    path: '/moneyChangeHis', //额度转换记录
    name: 'moneyChangeHis',
    component: MoneyChangeHis,
    meta: {
      index: 58,
    }
  },
  {
    path: '/bankIndex', //银行卡管理
    name: 'bankIndex',
    component: BankIndex,
    meta: {
      index: 53,
    }
  },
  {
    path: '/usdtIndex', //USDT管理
    name: 'usdtIndex',
    component: UsdtIndex,
    meta: {
      index: 53,
    }
  },
  {
    path: '/scoreExchange', //积分兑换
    name: 'scoreExchange',
    component: ScoreExchange,
    meta: {
      index: 53,
    }
  },
  {
    path: '/orderBetHis', //打码量记录
    name: 'orderBetHis',
    component: OrderBetHis,
    meta: {
      index: 53,
    }
  },
  {
    path: '/banking', //存取款
    name: 'banking',
    component: Banking,
    meta: {
      index: 20,
    }
  },
]
