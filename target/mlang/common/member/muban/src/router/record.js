const RecordCharge = () => import( /* webpackChunkName: "record" */ '@/views/record/RecordCharge')
const RecordChange = () => import( /* webpackChunkName: "record" */ '@/views/record/RecordChange')
const RecordUser = () => import( /* webpackChunkName: "record" */ '@/views/record/RecordUser')
const RecordTeam = () => import( /* webpackChunkName: "record" */ '@/views/record/RecordTeam')

export default [
  {
    path: '/recordCharge', //存取记录
    name: 'recordCharge',
    component: RecordCharge,
    meta: {
      index: 51,
    }
  },
  {
    path: '/recordChange', //账变报表
    name: 'recordChange',
    component: RecordChange,
    meta: {
      index: 53,
    }
  },
  {
    path: '/recordUser', //个人报表
    name: 'recordUser',
    component: RecordUser,
    meta: {
      index: 54,
    }
  },
  {
    path: '/recordTeam', //团队报表
    name: 'recordTeam',
    component: RecordTeam,
    meta: {
      index: 57,
    }
  },
]
