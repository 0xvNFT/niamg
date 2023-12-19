<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

  <head>
    <meta charset="utf-8">
    <title>${stationName}</title>

    <!--http://www.html5rocks.com/en/mobile/mobifying/-->
    <meta name="viewport"
      content="width=device-width,user-scalable=no,initial-scale=1,minimum-scale=1,maximum-scale=1,minimal-ui=true" />

    <!--https://developer.apple.com/library/safari/documentation/AppleApplications/Reference/SafariHTMLRef/Articles/MetaTags.html-->
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta name="format-detection" content="telephone=no">

    <!-- force webkit on 360 -->
    <meta name="renderer" content="webkit" />
    <meta name="force-rendering" content="webkit" />
    <!-- force edge on IE -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="msapplication-tap-highlight" content="no">
    <!-- uc强制竖屏 -->
    <meta name="screen-orientation" content="portrait" />
    <!-- QQ强制竖屏 -->
    <meta name="x5-orientation" content="portrait" />

    <!-- force full screen on some browser -->
    <meta name="full-screen" content="yes" />
    <meta name="x5-fullscreen" content="true" />
    <meta name="360-fullscreen" content="true" />

    <!--fix fireball/issues/3568 -->
    <!--<meta name="browsermode" content="application">-->
    <meta name="x5-page-mode" content="app">

    <!--<link rel="apple-touch-icon" href=".png" />-->
    <!--<link rel="apple-touch-icon-precomposed" href=".png" />-->

    <link rel="stylesheet" type="text/css" href="style.css" />


    <script>
      function initFingerprintJS() {
        FingerprintJS.load().then(fp => { fp.get().then(result => { document.cookie = 'SESSIONV=' + result.visitorId + ';path=/'; }) });
      }

      // 监听屏幕方向变化事件
      window.addEventListener("orientationchange", function () {
        console.log('orientationchange', window.orientation)
        setTimeout(function () {
          cc.view.setDesignResolutionSize(cc.winSize.width, cc.winSize.height, cc.ResolutionPolicy.SHOW_ALL);
        }, 200);
      });

    </script>
    <script async src="${domain}/common/fingerprint/fp.min.js?v=1" onload="initFingerprintJS()"></script>

  </head>

  <body>
    <div id="GameDiv" cc_exact_fit_screen="true">
      <div id="Cocos3dGameContainer">
        <canvas id="GameCanvas" oncontextmenu="event.preventDefault()" tabindex="99"></canvas>
      </div>
    </div>

    <!-- Polyfills bundle. -->
    <script src="src/polyfills.bundle.js" charset="utf-8"> </script>

    <!-- SystemJS support. -->
    <script src="src/system.bundle.js" charset="utf-8"> </script>

    <!-- Import map -->
    <script src="src/import-map.json" type="systemjs-importmap" charset="utf-8"> </script>

    <script>
      System.import('./index.js').catch(function (err) { console.error(err); })
    </script>


    <!-- <c:if test="${empty statisticsCode}"> -->
    <div style="display: none" class="station_statistics_code">${statisticsCode }</div>
    <!-- </c:if> -->

  </body>

  </html>