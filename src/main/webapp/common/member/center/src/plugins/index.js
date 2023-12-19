import Vue from 'vue'
// URLSearchParams兼容处理
// import 'url-search-params-polyfill'
//引入懒加载插件 lazyload;
// import VueLazyload from 'vue-lazyload'
//引入拖拽插件 draggable;
// import VueDraggableResizable from 'vue-draggable-resizable'

// 懒加载
// Vue.use(VueLazyload, {
//   preLoad: 1.3,
//   loading: '/mobile/newImages/loading.gif', //预加载图片
// })

// 全局注册
// Vue.component('VueDraggableResizable', VueDraggableResizable);

// element-ui
import { Menu, Submenu, MenuItem, MenuItemGroup, Breadcrumb, BreadcrumbItem, DatePicker, Button, ButtonGroup, Progress, Popover, Divider, Card, Dialog, MessageBox, Message, Loading, Notification, Select, Option, Input, Table, TableColumn, Pagination, Radio, Collapse, CollapseItem, Tabs, TabPane, Checkbox,Tooltip,Row,Col } from 'element-ui';
// 引入 element-ui 样式
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(Menu).use(Submenu).use(MenuItem).use(MenuItemGroup).use(Breadcrumb).use(BreadcrumbItem).use(DatePicker).use(Button).use(ButtonGroup).use(Progress).use(Popover).use(Divider).use(Card).use(Dialog).use(Select).use(Option).use(Input).use(Table).use(TableColumn).use(Pagination).use(Radio).use(Collapse).use(CollapseItem).use(Tabs).use(TabPane).use(Checkbox).use(Tooltip).use(Row).use(Col).use(Loading.directive)
// .use(MessageBox)
Vue.prototype.$loading = Loading.service;
Vue.prototype.$notify = Notification;
// Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$message = Message;

// element-ui 语言包配合 vue-i18n
import ElementLocale from 'element-ui/lib/locale'
import { i18n } from '../lang/i18n'

ElementLocale.i18n((key, value) => i18n.t(key, value))

