module.exports = {
  // presets: [
  //   '@vue/cli-plugin-babel/preset'
  // ]
  "presets": [["@babel/preset-env", {
    "modules": false,
    "targets": {
      "browsers": ["> 1%", "last 2 versions", "not ie <= 8"]
    }
  }]],
  "plugins": [
    [
      "component",
      {
        "libraryName": "element-ui",
        "styleLibraryName": "theme-chalk"
      }
    ]
  ]
}
