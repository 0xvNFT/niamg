import Vue from "vue";
// 轮播3d
import Carousel3d from "vue-carousel-3d";
//引入懒加载插件 lazyload;
import VueLazyload from "vue-lazyload";
import { pickerValidate } from "../assets/js/date";

Vue.use(Carousel3d);

// 懒加载
Vue.use(VueLazyload, {
  preLoad: 1,
  // loading: require('../assets/images/loading.gif')//预加载图片
});

// element-ui
import {
  MessageBox,
  Message,
  Loading,
  Notification,
  Button,
  Checkbox,
  Input,
  DatePicker,
  Progress,
  Image,
  Badge,
  Drawer,
  Carousel,
  CarouselItem,
  RadioGroup,
  RadioButton,
  Divider,
  Table,
  TableColumn,
  Pagination,
  Radio,
  Collapse,
  CollapseItem,
  Tabs,
  TabPane,
  Tooltip,
  Row,
  Col,
  Select,
  Option,
  Popover,
  Card,
  Dialog,
  Empty,
  ButtonGroup,
  Tag,
  Skeleton,
  SkeletonItem,
  InfiniteScroll,
  Form,
  FormItem
} from "element-ui";
// 引入 element-ui 样式
import "element-ui/lib/theme-chalk/index.css";

Vue.prototype.$loading = Loading.service;
Vue.prototype.$notify = Notification;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$message = Message;
Vue.prototype.pickerValidate = pickerValidate; // El组件的时间选择器校验

//按需引入
Vue.use(Loading.directive)
  .use(Badge)
  .use(Drawer)
  .use(Popover)
  .use(Skeleton)
  .use(SkeletonItem)
  .use(InfiniteScroll)
  .use(Tabs)
  .use(Row)
  .use(Image)
  .use(Carousel)
  .use(CarouselItem)
  .use(Empty)
  .use(RadioGroup)
  .use(RadioButton)
  .use(Divider)
  .use(Button)
  .use(DatePicker)
  .use(Progress)
  .use(Table)
  .use(TableColumn)
  .use(Pagination)
  .use(Radio)
  .use(Collapse)
  .use(CollapseItem)
  .use(TabPane)
  .use(Checkbox)
  .use(Tooltip)
  .use(Col)
  .use(Select)
  .use(Option)
  .use(Dialog)
  .use(Input)
  .use(Card)
  .use(ButtonGroup)
  .use(Tag)
  .use(Form)
  .use(FormItem);

// 格式化金额的过滤器（千分位加逗号）
Vue.filter("amount", (amount) => {
  return Number(amount) ? Number(amount).toLocaleString() : amount
});

Vue.config.errorHandler = function (err, vm, info) {
  console.error("错误来源:", info)
  console.error("错误信息 =>", err)
}