<html lang="en">

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0,viewport-fit=cover">
  <meta name="theme-color" content="#000000">
  <link rel="apple-touch-icon" href="logo192.png">
  <title>Promotion Login</title>
  <meta property="og:type" content="website">
  <meta property="og:title" content="Affiliate | Hggbet.com">
  <meta property="og:description"
    content="We are home to the best online casino, sports betting and entertainment games. From your favorite casino games like Baccarat, Roulette, and Poker, to Sic Bo and Dragon Tiger, we have it all. Enjoy our selected range of slots games and live bets on our huge collection of worldwide sports events.">
  <meta name="twitter:card" content="summary_large_image">
  <meta name="twitter:title" content="Affiliate | Hggbet.com">
  <#assign res="${base}/common/promp">
  <link href="${res}/css/ceh.css?v=5111" rel="stylesheet">
  <script type="text/JavaScript" src="${res}/js/jquery.min.js?v=1.1.1"></script>
  <script type="text/JavaScript" src="${res}/js/jquery.i18n.properties.js?v=1.1.1"></script>
  <script type="text/JavaScript" src="${res}/js/device.min.js"></script>
  <script src="${base}/common/lang/${language}.js?v=6"></script>
  <script src="${base}/common/js/layer/layer.js?v=1.1.1"></script>
  <script type="text/JavaScript" src="${res}/js/manifest.js?v=1.1.5"></script>
  <script src="${base}/common/js/layer/layerWindow.js?v=1.1.1"></script>
  <script type="text/javascript">
    var base = '${base}'; 
    var res = '${res}';
    var isLogin = '${isLogin}';
    var baseUrl = "${base}";
  </script>
</head>

<body data-layout-mode="light"><noscript>You need to enable JavaScript to run this app.</noscript>
  <div id="root">
    <div>
      <div class="section-home">
        <div class="lang-div" style="display: block; align-items: center; padding-right: 16rem;">
          <div class="logo xx2 d-dev">
            <a class="logo xx2 d-dev" href="${base}/promp/index.do" style="margin: 20px;">
              <img src="${stationLogo}" alt="">
            </a>
            <div style="display: flex; align-items: center; margin-left: 1rem; float: right;">
              <p style="color: white; margin-right: 1rem; font-size: 15px; font-weight: bold;" class="language">Language</p>
              <img id="btnLanguage " class="dd-languge langImg" src="${res}/images/cn.png" alt="lang"
                style="border-radius: 50%; width: 30px; height: 30px; cursor: pointer;">
            </div>
          </div>
        </div>
        <div class="mob_home_header m-dev">
          <div
            style="display: flex; align-items: center; justify-content: flex-end; padding: 1rem; margin-bottom: 1rem;">
            <a class="logo" href="${base}/promp/index.do">
              <img class="img-responsive" src="${stationLogo}" alt="">
            </a>
            <img id="btnLanguage" class="dd-languge langImg" src="${res}/images/cn.png" alt="lang"
              style="border-radius: 50%; width: 30px; height: 30px; cursor: pointer; margin-left: 1rem;">
          </div>
        </div>
        <div class="section-home-inner">
          <div class="member-section">
            <div class="btn-holder"><a id="btn-loginHolder" class="active loginG" href="${base}/promp/login.do">登录</a><a
                id="btn-registerHolder" class="registerR" href="${base}/promp/register.do">注册</a></div>
            <form id="loginHolder" novalidate="" action="#" class="form-holder" style="display: block;">
              <div class="form-group">
                <div class="input-group"><input id="login_username" name="userName" autocomplete="off" placeholder="用户名"
                    type="text" class="form-control form-control userName" aria-invalid="false" value="">
                </div>
                <div class="jErr"></div>
              </div>
              <div class="form-group">
                <div class="input-group"><input id="login_password" autocomplete="off" name="password" placeholder="*密码"
                    type="password" class="form-control pe-5 form-control passWord" aria-invalid="false" value="">
                  <!-- <span class="input-group-addon btn-icon"><i class="fa fa-eye-slash vPassView"></i></span> -->
                </div>
                <div class="jErr">
                </div>
              </div>
              <div class="form-group">
                <div class="input-group">
                  <input id="login_validcode" autocomplete="off" name="password" placeholder="*请输入验证码" type="text"
                    class="form-control pe-5 form-control verification" aria-invalid="false" value="">
                  <span class="input-group-addon btn-icon">
                    <div class="verification_but" style="margin-top:-8px"><img id="vcode_img"
                        src="${base}/loginVerifycode.do" id="validcode"></div>
                  </span>
                </div>
              </div>
              <div onclick="login()" type="submit" id="btnLogin"
                class="btn-logon btn-long btn-brown-gra btn btn-success loginG">登录</div>
            </form>
          </div>
        </div>
        <div id="languageModal" class="modal fade black-modal in" style="z-index: 99999999; display: none;"></div>
      </div>
    </div>
    <div class="section-footer">
      <div class="inner d-dev">
        <div class="seperateBox footerFirstCont">
          <div class="footerBox">
            <div class="lb-1 aboutUs">关于我们</div>
            <div class="lb-2 mgt15" style="text-align: left;"><span
                class="site-name UStext">我们拥有最好的在线赌场、体育博彩和娱乐游戏。从百家乐、轮盘和扑克等您最喜欢的娱乐场游戏，到骰宝和龙虎，我们应有尽有。享受我们精选的电子游戏，并在我们收集的大量全球体育赛事中进行现场投注。</span>
            </div>
          </div>
          <div class="footerBox">
            <div class="lb-1 GameOffer">游戏提供商</div>
            <div class="lb-2 mgt15 icon posRel slick-initialized slick-slider"
              style="padding: 15px 30px; background: rgb(22, 16, 28); height: 70px;">
              <div class="slick-slider slick-initialized" dir="ltr"><button
                  class="slick-prev slick-arrow slick-disabled" aria-label="Previous" type="button"
                  onclick="clickLeft()">Previous</button>
                <div class="slick-list">
                  <div class="slick-track" style="width: 2080px; opacity: 1; transform: translate3d(0px, 0px, 0px);">
                    <div data-index="0" class="slick-slide slick-active slick-current" tabindex="-1" aria-hidden="false"
                      style="outline: none; width: 104px;">
                      <div>
                        <img class="img-responsive slick-slide slick-current slick-active"
                          src="${res}/images/gameImg/JDB.png" alt="" data-slick-index="0"
                          aria-hidden="false" tabindex="-1" style="width: 100%; display: inline-block;">
                      </div>
                    </div>
                    <div data-index="1" class="slick-slide slick-active" tabindex="-1" aria-hidden="false"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/BACC.png"
                          alt="" data-slick-index="1" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="2" class="slick-slide slick-active" tabindex="-1" aria-hidden="false"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/JOKER.png"
                          alt="" data-slick-index="2" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="3" class="slick-slide slick-active" tabindex="-1" aria-hidden="false"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/MicroG.png"
                          alt="" data-slick-index="3" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="4" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/HABA.png"
                          alt="" data-slick-index="4" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="6" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div>
                        <img class="img-responsive slick-slide " src="${res}/images/gameImg/AG.png" alt=""
                          data-slick-index="6" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;">
                      </div>
                    </div>
                    <div data-index="7" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/BTi.png"
                          alt="" data-slick-index="7" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="8" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div>
                        <img class="img-responsive slick-slide " src="${res}/images/gameImg/CQ9.png"
                          alt="" data-slick-index="8" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;">
                      </div>
                    </div>
                    <div data-index="9" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div>
                        <img class="img-responsive slick-slide " src="${res}/images/gameImg/Spadeg.png"
                          alt="" data-slick-index="9" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;">
                      </div>
                    </div>
                    <div data-index="10" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide "
                          src="${res}/images/gameImg/Evolution.png" alt="" data-slick-index="10"
                          aria-hidden="true" tabindex="-1" style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="11" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/JILI.png"
                          alt="" data-slick-index="11" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="12" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide "
                          src="${res}/images/gameImg/PLAYSON.png" alt="" data-slick-index="12"
                          aria-hidden="true" tabindex="-1" style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="13" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/DREAM.png"
                          alt="" data-slick-index="13" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="14" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/PG.png"
                          alt="" data-slick-index="14" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="15" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/SAGAM.png"
                          alt="" data-slick-index="15" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="16" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/KAGA.png"
                          alt="" data-slick-index="16" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="17" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/KING.png"
                          alt="" data-slick-index="17" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="18" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/PRAGMA.png"
                          alt="" data-slick-index="18" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                    <div data-index="19" class="slick-slide" tabindex="-1" aria-hidden="true"
                      style="outline: none; width: 104px;">
                      <div><img class="img-responsive slick-slide " src="${res}/images/gameImg/CASINO.png"
                          alt="" data-slick-index="19" aria-hidden="true" tabindex="-1"
                          style="width: 100%; display: inline-block;"></div>
                    </div>
                  </div>
                </div><button class="slick-next slick-arrow " aria-label="Next" type="button"
                  onclick="clickRight()">Next</button>
              </div>
            </div>
          </div>
          <div class="footerBox">
            <div class="lb-1">
              <div class="icoIBS"></div>
            </div>
          </div>
        </div>
        <div class="seperateBox footerCont footerSecondCont">
          <div class="footerBox">
            <div class="lb-1 quickLink" style="cursor: pointer;">快速链接</div>
            <div class="mgt15">
              <div class="box">
                <ul>
                  <li><a href="/faq" class="faqs">常见问题</a></li>
                  <li><a href="/contact-us" class="connectUs">联系我们</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div class="footerBox"></div>
        </div>
        <div class="seperateBox footerCont"></div>
      </div>
    </div>
    <div class="section-copyright">${stationName} @2023 Copyright。</div>
  </div>
  <script>
    $("#languageModal").load("${res}/languageModal.ftl");
  </script>
</body>
<script>
  $(function(){
     if(device.mobile()){$('.section-home').addClass('section-home-m')}
  })
  
  $('#vcode_img').click(function () {
    var _this = $(this);
    $('#vcode_img').attr("src", "${base}/loginVerifycode.do?timestamp=" + (new Date().getTime()));
  });

  function login () {
    var username = $('#login_username').val()
    var pwd = $('#login_password').val()
    var verifyCode = $('#login_validcode').val()
    $.ajax({
      type: "POST",
      url: base + '/promp/login.do',
      data: {
        username: username,
        pwd: pwd,
        captcha: verifyCode
      },
      success: function (data, textStatus, xhr) {
        var ceipstate = xhr.getResponseHeader("ceipstate")
        if (!ceipstate || ceipstate == 1) {// 正常响应
          location.href = "${base}/redBag2.do";
        } else {// 后台异常
          layer.msg(data.msg || "System error，contact customer service!");
          // refreshVerifyCode();
          $('#vcode_img').click();
        }
      }
    });
  }
  var leftW = 0
  function clickRight () {
    var widthH = $('.slick-track').width()
    console.log(11, leftW);
    if (leftW > -1800) {
      leftW -= 100
      $('.slick-track').css({ 'margin-left': leftW + 'px' })
    }
  }
  function clickLeft () {
    var widthH = $('.slick-track').width()
    console.log(11, leftW);
    if (leftW < 0) {
      leftW += 100
      $('.slick-track').css({ 'margin-left': leftW + 'px' })
    }
  }
</script>

</html>