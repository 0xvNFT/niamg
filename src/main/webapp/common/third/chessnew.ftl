<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${base}/common/third/css/chessnew.css?v=3:1:4">
    <script type="text/javascript">var base='${base}'</script>
</head>
<body>
<div class="cbox">
    <#if isLogin><#include "/common/third/include/thirdMoneyChange.ftl"/>
    <nav class="nav"><#else><nav class="nav nav1"></#if>
            <ul>
                <#if platform.ky>
                    <li <#if curPlatform == "ky">  class="init-highlight" </#if>>
                        <a <#if isLogin>href="${base}/third/forwardKYChess.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>><@spring.message "front.chess.ky"/></a>
                    </li></#if>
                <#if platform.vgqp><li <#if curPlatform == "vgqp">  class="init-highlight" </#if>>
                    <a <#if isLogin>href="'${base}/third/forwardVgqp.do?gameCode='+item.id" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>><@spring.message "front.chess.cs"/></a>
                    </li></#if>
                </li>
                <#if platform.baison><li <#if curPlatform == "baison">  class="init-highlight" </#if>>
                   <a <#if isLogin>href="'${base}/third/forwardBaison.do?gameId='+item.id" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>><@spring.message "front.chess.bs"/></a>
                    </li></#if>
                </li>
                <#if platform.leg><li <#if curPlatform == "leg">  class="init-highlight" </#if>>
                    <a <#if isLogin>href="${base}/third/forwardLeg.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>><@spring.message "front.chess.leg"/></a>
                    </li></#if>
                </li>
                <#if platform.v8poker><li <#if curPlatform == "v8poker">  class="init-highlight" </#if>>
                    <a <#if isLogin>href="${base}/third/forwardV8Poker.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>><@spring.message "front.chess.v8poker"/></a>
                    </li></#if>
                </li>
            </ul></nav>
        <main class="card_box">
            <ul>
                <#if platform.ky>
                    <li>
                        <div class="box">
                            <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/chess/ky-poker-bg.png"></a></div>
                            <div class="box-content">
                               <a <#if isLogin>href="${base }/third/forwardKYChess.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                    <img src="${base}/common/third/images/chess/ky-poker-${language}.png"><span>KY CARD GAME</span></a></div>
                        </div>
                    </li>
                </#if>
                <#if platform.vgqp>
                    <li>
                        <div class="box">
                            <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/'+item.img"></a></div>
                            <div class="box-content">
                               <a <#if isLogin>href="'${base}/third/forwardVgqp.do?gameCode='+item.id" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                    <img src="'${base}/common/third/images/'+item.img"><span>{{item.name}}</span></a></div>
                        </div>
                    </li>
                </#if>
                <#if platform.baison>
                    <li>
                        <div class="box">
                            <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/'+item.img"></a></div>
                            <div class="box-content">
                                <a <#if isLogin>href="'${base }/third/forwardBaison.do?gameId='+item.id" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                    <img src="'${base}/common/third/images/'+item.img"><span>{{item.name}}</span></a></div>
                        </div>
                    </li>
                </#if>
                <#if platform.leg>
                    <li>
                        <div class="box">
                            <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/chess/leg-poker-bg.png"></a></div>
                            <div class="box-content">
                               <a <#if isLogin>href="${base }/third/forwardLeg.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                    <img src="${base}/common/third/images/chess/leg-poker-${language}.png"><span>LEG CARD GAME</span></a></div>

                        </div>
                    </li>
                </#if>
                <#if platform.leg>
                    <li>
                        <div class="box">
                            <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/chess/v8-poker-bg.png"></a></div>
                            <div class="box-content">
                                <a <#if isLogin>href="${base }/third/forwardV8Poker.do?kindId=0" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                    <img src="${base}/common/third/images/chess/v8-poker-${language}.png"><span>V8POKER CARD GAME</span></a></div>

                        </div>
                    </li>
                </#if>
            </ul>
        </main>
</div>
<#if !isLogin><#include "/common/third/include/unlogin.ftl"/></#if>
</body>
</html>
<script src="${base}/common/lang/${language}.js?v=4"></script>
<script src="${base}/common/js/jquery-3.5.1.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
<script src="${base}/common/js/layer/layerWindow.js"></script>
<#if isLogin><script src="${base}/common/js/member/thirdMoneyChange.js"></script>
<#else>
    <script>
        $(function(){
            $(".unlogin").click(function(){
                layer.open({
                    type : 1,
                    title : '<@spring.message "admin.not.login.now"/>',
                    shadeClose : true,
                    shade : 0.8,
                    area : [ '380px', '200px' ],
                    content : $('#loginfrom')
                });
            });
        });
    </script>
</#if>