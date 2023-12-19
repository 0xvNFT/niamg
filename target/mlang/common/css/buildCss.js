({//执行r.js -o buildCss.js
    cssIn: "main.css",//导入文件
    out: "main.min.css",//压缩后的css 代码输出的位置
    optimizeCss: "standard.keepLines"//压缩css 代码的方式。这里我选择了保持换行，因为某些浏览器可能识别不了不换行的某些css 代码
})