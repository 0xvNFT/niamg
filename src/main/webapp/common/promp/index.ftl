<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Promotion Plan</title>
    <link rel="stylesheet" href="${base}/common/promp/css/index.css">
    <script src="${base}/common/lang/${language}.js?v=6"></script>
    <script src="${base}/common/js/layer/layer.js?v=1.1.1"></script>
    <script src="${base}/common/js/layer/layerWindow.js?v=1.1.1"></script>
    <script type="text/JavaScript" src="${base}/common/promp/jquery.min.js?v=1.1.1"></script>
    <script type="text/JavaScript" src="${base}/common/promp/jquery.i18n.properties.js?v=1.1.1"></script>
    <script type="text/JavaScript" src="${base}/common/promp/manifest.js?v=1.1.3"></script>
    <script type="text/JavaScript" src="${base}/common/promp/js/device.min.js"></script>

    <script type="text/javascript">
        var base = '${base}';
        var res = '${res}';
        var isLogin = '${isLogin}';
        var baseUrl = "${base}";
    </script>
</head>

<body data-layout-mode="light">
    <div id="root">
        <div>
            <div class="xTopBar">
                <div class="topbar">
                    <div class="topbar-inner">
                        <a class="pull-left logo" href="${base}/promp/index.do">
                            <img class="img-logo-main-responsive" src="${stationLogo}"
                                alt="logo">
                        </a>
                        <div class="topbar-buttons pull-right d-dev">
                            <div class="menuItem">
                                <div class="d-in text-grey-3 sublinks">
                                    <a class="d-in btn-menu-link" href="${base}/common/promp/index.do">
                                        <img class="img-responsive" src="${base}/common/promp/images/dating.png" alt="home">
                                    </a>
                                    <a class="d-in btn-menu-link vBtnLaiveChat gameLobby" href="${base}/promp/index.do">游戏大厅</a><a
                                        class="d-in btn-menu-link vBtnLaiveChat chat" href="${base}/promp/chat.do">在线聊天</a>
                                </div><a id="btnLogout"
                                    class="d-in btn btn-2 btn-pink-grad btn-round text-uppercase mgl10 btn-menu loginG"
                                    href="${base}/promp/login.do">登录</a><a id="btnLogout"
                                    class="d-in btn btn-2 btn-pink btn-round text-uppercase mgl10 btn-menu join"
                                    href="${base}/promp/register.do">立即加入</a>
                                <div class="btn-language d-dev d-in mgl10">
                                    <img id="btnLanguage" class="dd-languge langImg" src="${base}/common/promp/images/cn.png" alt="lang"
                                        style="border-radius: 50%;">
                                </div>
                            </div>
                        </div>
                        <div class="btn-language m-dev"><a
                                class="d-in  btn-2  btn-round text-uppercase mgl10 btn-menu m-btn btn-login"
                                id="xbtnLogOut" href="${base}/promp/login.do">登录</a><a id="btnLanguageMob" class="dd-languge" href="#"></a>
                        </div>
                        <div class="clearfix"></div>
                    </div>
                </div>
            </div>
            <div id="languageModal" class="modal fade black-modal in" style="z-index: 99999999;display: none;">
            </div>
            <div class="mainBanner">
                <img src="${base}/common/promp/images/lunbopr.jpg" alt="banner" class="imgIndex">
            </div>
            <div class="row steps">
                <div class=" steps-container">
                    <h1 class="containerH1">代理加盟计划</h1>
                    <h3 class="containerH3">全球最具吸引力的在线游戏运营商和提供商</h3>
                    <span class="containerSpan1">我们为您提供有回报的分层收益分享计划。今天注册，今天就享受佣金。</span><br><span
                        class="containerSpan2">务实是我们的原则，诚实是我们的方针。让我们共同努力，取得新的成功。</span>
                    <h3 class="mgt40 mgb0 containerH4">今天开始分享成功并赚钱！</h3>
                    <div class="clearfix"></div>
                    <div class=" inner-steps">
                        <div class="col-sm-4 steps-outerline">
                            <div class="steps-line steps-image">
                                <img src="${base}/common/promp/images/text.png" alt="">
                            </div>
                            <div class="steps-inner">
                                <div class="clearfix"></div><span class="applications">提交申请</span>
                            </div>
                        </div>
                        <div class="col-sm-4 steps-outerline">
                            <div class="steps-line steps-image">
                                <img src="${base}/common/promp/images/laba.png" alt="">
                            </div>
                            <div class="steps-inner">
                                <div class="clearfix"></div><span class="promotion">开始推广</span>
                            </div>
                        </div>
                        <div class="col-sm-4 steps-outerline">
                            <div class=" steps-image"><img src="${base}/common/promp/images/money.png" alt="earn_money">
                            </div>
                            <div class="steps-inner" style="text-align: center;">
                                <div class="clearfix"></div><span class="money">赚钱</span>
                            </div>
                        </div>
                    </div>
                    <div class="clearfix"></div>
                    <div><a class="d-in btn btn-page btn-round text-uppercase mgt30 plan"
                            href="/commission-plans">佣金计划</a>
                    </div>
                </div>
            </div>
            <div class="row why-join">
                <div class=" why-join-container">
                    <h1 class="joinH1">为什么要加入我们的联盟计划？</h1>
                    <div class="clearfix"></div>
                    <h3 class="joinH3">通过成为我们的加盟代理，您可以</h3>
                    <div class="clearfix"></div>
                    <div class="why-join-table">
                        <table class="whyjt-1">
                            <tbody style="display: flex; flex-direction: column;">
                                <tr></tr>
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td1">从您的收益分成中赚取佣金</td>
                                </tr>
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td2">全交叉产品收益</td>
                                </tr>
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td3">范围广泛的优质产品</td>
                                </tr>
                            </tbody>
                        </table>
                        <table class="whyjt-2">
                            <tbody style="display: flex; flex-direction: column;">
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td4">从推荐的玩家身上赚取终生收入</td>
                                </tr>
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td5">成为下一代在线体育博彩的一部分</td>
                                </tr>
                                <tr>
                                    <td><img src="${base}/common/promp/images/li.png" alt="ico-li"></td>
                                    <td class="td6">来自我们敬业的加盟经理的全力支持</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div class="section-footer">
            <div class="inner d-dev">
                <div class="seperateBox footerFirstCont">
                    <div class="footerBox">
                        <div class="lb-1 aboutUs">关于我们</div>
                        <div class="lb-2 mgt15" style="text-align: left;">
                            <span
                                class="site-name UStext">我们拥有最好的在线赌场、体育博彩和娱乐游戏。从百家乐、轮盘和扑克等您最喜欢的娱乐场游戏，到骰宝和龙虎，我们应有尽有。享受我们精选的电子游戏，并在我们收集的大量全球体育赛事中进行现场投注。</span>
                        </div>
                    </div>
                    <div class="footerBox">
                        <div class="lb-1 GameOffer">游戏提供商</div>
                        <div class="lb-2 mgt15 icon posRel slick-initialized slick-slider"
                            style="padding: 15px 30px; background: rgb(22, 16, 28); height: 70px;">
                            <div class="slick-slider slick-initialized" dir="ltr"><button
                                    class="slick-prev slick-arrow slick-disabled" aria-label="Previous" type="button"
                                    disabled="">Previous</button>
                                <div class="slick-list">
                                    <div class="slick-track"
                                        style="width: 2080px; opacity: 1; transform: translate3d(0px, 0px, 0px);">
                                        <div data-index="0" class="slick-slide slick-active slick-current" tabindex="-1"
                                            aria-hidden="false" style="outline: none; width: 104px;">
                                            <div>
                                                <img class="img-responsive slick-slide slick-current slick-active"
                                                    src="${base}/common/promp/images/gameImg/JDB.png" alt=""
                                                    data-slick-index="0" aria-hidden="false" tabindex="-1"
                                                    style="width: 100%; display: inline-block;">
                                            </div>
                                        </div>
                                        <div data-index="1" class="slick-slide slick-active" tabindex="-1"
                                            aria-hidden="false" style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/BACC.png" alt=""
                                                    data-slick-index="1" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="2" class="slick-slide slick-active" tabindex="-1"
                                            aria-hidden="false" style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/JOKER.png" alt=""
                                                    data-slick-index="2" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="3" class="slick-slide slick-active" tabindex="-1"
                                            aria-hidden="false" style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/MicroG.png" alt=""
                                                    data-slick-index="3" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="4" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/HABA.png" alt=""
                                                    data-slick-index="4" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="5" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div>
                                                <img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/AG.png" alt=""
                                                    data-slick-index="5" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;">
                                            </div>
                                        </div>
                                        <div data-index="6" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div>
                                                <img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/AG.png" alt=""
                                                    data-slick-index="6" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;">
                                            </div>
                                        </div>
                                        <div data-index="7" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/BTi.png" alt=""
                                                    data-slick-index="7" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="8" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div>
                                                <img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/CQ9.png" alt=""
                                                    data-slick-index="8" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;">
                                            </div>
                                        </div>
                                        <div data-index="9" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div>
                                                <img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/Spadeg.png" alt=""
                                                    data-slick-index="9" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;">
                                            </div>
                                        </div>
                                        <div data-index="10" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/Evolution.png" alt=""
                                                    data-slick-index="10" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="11" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/JILI.png" alt=""
                                                    data-slick-index="11" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="12" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/PLAYSON.png" alt=""
                                                    data-slick-index="12" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="13" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/DREAM.png" alt=""
                                                    data-slick-index="13" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="14" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/PG.png" alt=""
                                                    data-slick-index="14" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="15" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/SAGAM.png" alt=""
                                                    data-slick-index="15" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="16" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/KAGA.png" alt=""
                                                    data-slick-index="16" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="17" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/KING.png" alt=""
                                                    data-slick-index="17" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="18" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/PRAGMA.png" alt=""
                                                    data-slick-index="18" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                        <div data-index="19" class="slick-slide" tabindex="-1" aria-hidden="true"
                                            style="outline: none; width: 104px;">
                                            <div><img class="img-responsive slick-slide "
                                                    src="${base}/common/promp/images/gameImg/CASINO.png" alt=""
                                                    data-slick-index="19" aria-hidden="true" tabindex="-1"
                                                    style="width: 100%; display: inline-block;"></div>
                                        </div>
                                    </div>
                                </div><button class="slick-next slick-arrow " aria-label="Next"
                                    type="button">Next</button>
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
        $("#languageModal").load("${base}/common/promp/languageModal.ftl");
        $(function(){
     		if(device.mobile()){$('.section-home').addClass('section-home-m')}
  		})
    </script>
</body>


</html>