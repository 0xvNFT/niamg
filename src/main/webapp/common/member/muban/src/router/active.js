const Active = () => import( /* webpackChunkName: "active" */ '@/views/active/Active')
const ActiveDetails = () => import( /* webpackChunkName: "active" */ '@/views/active/ActivesDetails')
const Help = () => import( /* webpackChunkName: "active" */ '@/views/active/Help')
const AppDown = () => import( /* webpackChunkName: "active" */ '@/views/active/AppDown')
const Notice = () => import( /* webpackChunkName: "active" */ '@/views/active/Notice')
const Advice = () => import( /* webpackChunkName: "active" */ '@/views/active/Advice')

export default [
  {
    path: '/active', //优惠活动
    name: 'active',
    component: Active,
    meta: {
      index: 20,
    }
  },
  {
    path: '/activedetails', //优惠活动详情
    name: 'activedetails',
    component: ActiveDetails,
    meta: {
      index: 20,
    }
  },
  {
    path: '/help', //帮助中心详情
    name: 'help',
    component: Help,
    meta: {
      index: 20,
    }
  },
  {
    path: '/appDown', //app下载
    name: 'appDown',
    component: AppDown,
    meta: {
      index: 52,
    }
  },
  {
    path: '/notice', //网站公告
    name: 'notice',
    component: Notice,
    meta: {
      index: 53,
    }
  },
  {
    path: '/advice', //建议反馈
    name: 'advice',
    component: Advice,
    meta: {
      index: 54,
    }
  },
]
