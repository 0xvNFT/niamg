const path = require('path')
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const IS_PROD = ['production', 'test'].includes(process.env.NODE_ENV)
let Version = new Date().getTime();

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  // 部署应用包时的基本 URL,用法和 webpack 本身的 build.assetsPublicPath 一致，打包的 js css 用的这个路径
  publicPath: '/common/member/muban/dist/static/',
  // 输出文件目录
  outputDir: 'dist/static',
  indexPath: '../../../centerMoban.ftl',
  // eslint-loader 是否在保存的时候检查
  lintOnSave: true,
  // 是否使用包含运行时编译器的 Vue 构建版本
  runtimeCompiler: false,
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: false,
  pluginOptions: {
    'style-resources-loader': {
      preProcessor: 'less',
      patterns: [
        // 注意：试过不能使用别名路径
        path.resolve(__dirname, './src/assets/css/public.less')
      ]
    }
  },
  chainWebpack: (config) => {
    config.entry.app = ['babel-polyfill', './src/main.js'];
    // 配置路径别名
    config.resolve.extensions
      .add('.js') // 项目中引入 JavaScript 文件不用加'.js'的后缀
      .add('.vue') // 项目中引入 Vue 组件文件不用加'.vue'的后缀
      .add('.png') // 组件中的template引入 PNG 图片资源不用加'.png'的后缀
    config.resolve.alias
      .set('@', resolve('src'))
      .set('@images', resolve('src/assets/images/')); // 图片路径别名，例如：src='src/assets/images/img_path.png' 可简写为 src='@images/img_path'。
    // 移除 prefetch 插件
    config.plugins.delete('prefetch')
  },
  // css相关配置
  css: {
    // 是否分离css（插件ExtractTextPlugin）配置了 css 才能热更
    // extract: true,
    extract: IS_PROD,
    // 开发环境是否开启 CSS source maps
    sourceMap: true,
    // 是否启用 CSS modules for all css / pre-processor files.
    modules: false,
    // requireModuleExtension: false,
    // css预设器配置项
    loaderOptions: {
      sass: {
        prependData: `@import "@/assets/css/public.scss";`
      }
    }
  },
  // configureWebpack: config => {
  //   // config.output.chunkFilename = `js/[name].${Version}.js`,
  // },
  configureWebpack: {
    // 去掉大文件警告
    performance: {
      hints: false
    },
  },
  devServer: {
    open: true,//设置自动打开
    host: '0.0.0.0',
    // host: 'localhost',
    port: 8096, // 端口
    https: false,
    hotOnly: true,
    hot: true,
    disableHostCheck: true,//webpack4.0 开启热更新
    proxy: 'https://mt8.yibo22.com',//全站跨域
  },
}
