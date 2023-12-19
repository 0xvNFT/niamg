import Vue from 'vue'
import VueRouter from 'vue-router'
import active from './active'
import order from './order'
import proxy from './proxy'
import record from './record'
import user from './user'

// 重复路由冗余
const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
VueRouter.prototype.replace = function replace (location) {
  return originalReplace.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const Index = () => import( /* webpackChunkName: "Index" */ '@/views/Index')
const ThirdPage = () => import( /* webpackChunkName: "Index" */ '@/views/ThirdPage')

const routes = [
  {
    path: "*",
    redirect: "/"
  }, {
    path: '/',
    redirect: 'index',
    meta: {
      index: 0
    }
  }, {
    path: '/index',
    name: 'index',
    component: Index,
    meta: {
      index: 1,
    }
  }, {
    path: '/thirdPage',
    name: 'thirdPage',
    component: ThirdPage,
    meta: {
      index: 2,
    }
  },
  ...active,
  ...order,
  ...proxy,
  ...record,
  ...user,
]

const router = new VueRouter({
  routes
})

export default router
