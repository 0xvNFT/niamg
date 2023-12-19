const MyRecommend = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/MyRecommend')
const ViewTeam = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/ViewTeam')
const UserList = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/UserList')
const Recommend = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/Recommend')
const RecommendAgent = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/RecommendAgent')
const PromotionUrl = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/PromotionUrl')
const RegManage = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/RegManage')
const UserView = () => import( /* webpackChunkName: "proxy" */ '@/views/proxy/UserView')

export default [
  {
    path: '/myRecommend', //我的分享
    name: 'myRecommend',
    component: MyRecommend,
    meta: {
      index: 60,
    }
  },
  {
    path: '/viewTeam', //团队列表
    name: 'viewTeam',
    component: ViewTeam,
    meta: {
      index: 62,
    }
  },
  {
    path: '/userList', //用户列表
    name: 'userList',
    component: UserList,
    meta: {
      index: 61,
    }
  },
  {
    path: '/recommend', //推荐总览
    name: 'recommend',
    component: Recommend,
    meta: {
      index: 64,
    }
  },
  {
    path: '/recommendAgent', //我的推荐
    name: 'recommendAgent',
    component: RecommendAgent,
    meta: {
      index: 64,
    }
  },
  {
    path: '/promotionUrl', //推广链接
    name: 'promotionUrl',
    component: PromotionUrl,
    meta: {
      index: 63,
    }
  },
  {
    path: '/regManage', //注册管理
    name: 'regManage',
    component: RegManage,
    meta: {
      index: 64,
    }
  },
  {
    path: '/userView', //个人总览
    name: 'userView',
    component: UserView,
    meta: {
      index: 50,
    }
  },
]
