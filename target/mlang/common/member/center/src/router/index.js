import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store"
import user from './user'
import msg from './msg'
import record from './record'
import proxy from './proxy'
import order from './order'
import pay from './pay'

Vue.use(VueRouter)

// 重复路由冗余
const originalPush = VueRouter.prototype.push
const originalReplace = VueRouter.prototype.replace
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
VueRouter.prototype.replace = function replace (location) {
  return originalReplace.call(this, location).catch(err => err)
}

const Index = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Index" */ '../views/Index').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

const Login = () => {
  store.dispatch("getShowSpin", true);
  return import( /* webpackChunkName: "Login" */ '../views/Login').then(m => {
    store.dispatch("getShowSpin", false);
    return m
  })
}

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
      // type: '账户管理',
      // name: '个人总览',
      type: 'accountMeun',
      name: 'viewUser',
      openeds: '3'
    }
  }, {
    path: '/login',
    name: 'login',
    component: Login,
    meta: {
      index: 2,
      type: 'accountMeun',
      name: 'viewUser',
      openeds: '3'
    }
  },
  ...user,
  ...msg,
  ...record,
  ...proxy,
  ...order,
  ...pay
]

const router = new VueRouter({
  routes
})

export default router
