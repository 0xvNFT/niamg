import store from "../store"

const RecordCharge = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RecordCharge" */ '../views/recordManage/RecordCharge').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const RecordChange = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RecordChange" */ '../views/recordManage/RecordChange').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const RecordUser = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RecordUser" */ '../views/recordManage/RecordUser').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}
const RecordTeam = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "RecordTeam" */ '../views/recordManage/RecordTeam').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

export default [
  {
    path: '/recordCharge',
    name: 'recordCharge',
    component: RecordCharge,
    meta: {
      index: 21,
      // type: '报表管理',
      // name: '存取记录',
      type: 'recordMenu',
      name: 'recordCharge',
      openeds: '2'
    }
  },
  {
    path: '/recordChange',
    name: 'recordChange',
    component: RecordChange,
    meta: {
      index: 12,
      // type: '报表管理',
      // name: '账变报表',
      type: 'recordMenu',
      name: 'recordChange',
      openeds: '2'
    }
  },
  {
    path: '/recordUser',
    name: 'recordUser',
    component: RecordUser,
    meta: {
      index: 23,
      // type: '报表管理',
      // name: '个人报表',
      type: 'recordMenu',
      name: 'recordUser',
      openeds: '2'
    }
  },
  {
    path: '/recordTeam',
    name: 'recordTeam',
    component: RecordTeam,
    meta: {
      index: 24,
      // type: '报表管理',
      // name: '团队报表',
      type: 'recordMenu',
      name: 'recordTeam',
      openeds: '2'
    }
  },
]
