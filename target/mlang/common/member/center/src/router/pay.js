import store from "../store"

const Recharge = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Recharge" */ '../views/payManage/Recharge').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const Withdraw = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Withdraw" */ '../views/payManage/Withdraw').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/recharge',
    name: 'recharge',
    component: Recharge,
    meta: {
      index: 51,
      // type: '充值',
      type: 'payText',
      name: '',
      openeds: ''
    }
  },
  {
    path: '/withdraw',
    name: 'withdraw',
    component: Withdraw,
    meta: {
      index: 51,
      // type: '提款',
      type: 'withdrawal',
      name: '',
      openeds: ''
    }
  },
]
