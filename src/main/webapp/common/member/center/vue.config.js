const path = require('path')
const IS_PROD = ['production', 'test'].includes(process.env.NODE_ENV)
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
let Version = new Date().getTime();

function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  // 部署应用包时的基本 URL,用法和 webpack 本身的 build.assetsPublicPath 一致，打包的 js css 用的这个路径
  publicPath: '/common/member/center/dist/static/',
  // 输出文件目录
  outputDir: 'dist/static',
  indexPath: '../../../centerIndex.ftl',
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
    config.resolve.alias
      .set('@', resolve('src'))
      .set('@assets', resolve('src/assets'))
    // 移除 prefetch 插件
    config.plugins.delete('prefetch')
  },
  // css相关配置
  css: {
    // 是否分离css（插件ExtractTextPlugin）配置了 css 才能热更
    // extract: false,
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
  // configureWebpack: { // webpack 配置
  //   output: { // 输出重构  打包编译后的 文件名称  【模块名称.版本号.js】
  //     filename: `js/[name].${Version}.js`,
  //     // chunkFilename: `js/[name].${Version}.js`
  //   },
  //   // 修改打包后css文件名
  //   plugins: [
  //     new MiniCssExtractPlugin({
  //       filename: `css/[name].${Version}.css`,
  //       // chunkFilename: `css/[name].${Version}.css`
  //     })
  //   ]
  // },
  // configureWebpack: config => {
  // config.output.chunkFilename = `js/[name].${Version}.js`
  // config.plugin('extract-css').tap(args => [{
  //   filename: `css/[name].${Version}.css`,
  //   chunkFilename: `css/[name].${Version}.css`,
  // }])
  // },
  devServer: {
    // open: true,//设置自动打开
    host: '0.0.0.0',
    port: 8095, // 端口
    https: false,
    hotOnly: true,
    hot: true,
    disableHostCheck: true,//webpack4.0 开启热更新
    proxy: 'https://mt8.yibo22.com',//全站跨域
  },
}
