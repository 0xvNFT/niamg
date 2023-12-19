<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} <@spring.message "front.chess.title"/></title>
    <link rel="stylesheet" href="${base}/common/third/css/chess.css?v=1">
</head>
<body >
    <div>
        <div class="Max-bg" >
            <div class="title">
                <img src="${base}/common/third/images/chess/Top-Title_${language}.png">
            </div>
            <div class="pop">
                <div class="center">
                    <div class="game">
                        <!-- 游戏切换hover列表 -->
                        <div class="hover-list">
                            <ul>
                                <#if platform.ky>
                                    <li <#if curPlatform == "ky">  class="init-highlight" </#if>>
                                        <a href="javascript:void(0)"><@spring.message "front.chess.ky"/></a>
                                    </li>
                                </#if>
                                <#if platform.vgqp>
                                    <li <#if curPlatform == "vgqp">  class="init-highlight" </#if>>
                                        <a href="javascript:void(0)"><@spring.message "front.chess.cs"/></a>
                                    </li>
                                </#if>
                                <#if platform.baison>
                                    <li <#if curPlatform == "baison">  class="init-highlight" </#if>>
                                        <a href="javascript:void(0)"><@spring.message "front.chess.bs"/></a>
                                    </li>
                                </#if>
                                <#if platform.leg>
                                    <li <#if curPlatform == "leg">  class="init-highlight" </#if>>
                                        <a href="javascript:void(0)"><@spring.message "front.chess.leg"/></a>
                                    </li>
                                </#if>
                                <#if platform.v8poker>
                                    <li <#if curPlatform == "v8poker">  class="init-highlight" </#if>>
                                        <a href="javascript:void(0)"><@spring.message "front.chess.v8poker"/></a>
                                    </li>
                                </#if>
                            </ul>
                        </div>
                        <!-- 游戏详情列表 -->
                        <div class="XQ-list-Max" id="latest-game">
                            <#if platform.ky>
                                <div class="fd">
                                    <ul>
                                        <li>
                                            <a <#if isLogin>href="${base }/third/forwardKYChess.do?kindId=0" target="_blank"<#else>href="javascript:loginfrom();"</#if>>
                                                <img src="${base}/common/third/images/chess/ky.png"><span>KY CARD GAME</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <#if platform.vgqp>
                                <div class="fd">
                                    <ul>
                                        <li v-for="(item, index) in vgqpChessData">
                                            <a <#if isLogin>:href="'${base}/third/forwardVgqp.do?gameCode='+item.id" target="_blank"<#else>href="javascript:loginfrom();"</#if>>
                                                <img :src="'${base}/common/third/images/'+item.img">
                                                <span>{{item.name}}</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <#if platform.baison>
                                <div class="fd">
                                    <ul>
                                        <li v-for="(item, index) in baisonChessData">
                                            <a <#if isLogin>:href="'${base}/third/forwardBaison.do?gameId='+item.id" target="_blank"<#else>href="javascript:loginfrom();"</#if>>
                                                <img :src="'${base}/common/third/images/'+item.img">
                                                <span>{{item.name}}</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <#if platform.leg>
                                <div class="fd">
                                    <ul>
                                        <li>
                                            <a <#if isLogin>href="${base}/third/forwardLeg.do?kindId=0" target="_blank"<#else>href="javascript:loginfrom();"</#if>>
                                                <img src="${base}/common/third/images/chess/leg.png"><span>LEG CARD GAME</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                            <#if platform.v9poker>
                                <div class="fd">
                                    <ul>
                                        <li>
                                            <a <#if isLogin>href="${base}/third/forwardV8Poker.do?kindId=0" target="_blank"<#else>href="javascript:loginfrom();"</#if>>
                                                <img src="${base}/common/third/images/chess/v8poker.png"><span>V8POKER CARD GAME</span>
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if !isLogin><#include "/common/third/include/unlogin.ftl"/></#if>
</body>
</html>
<script src="${base}/common/lang/${language}.js?v=3"></script>
<script src="${base}/common/js/jquery-3.5.1.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
<script src="${base}/common/js/vue/vue.2.2.0.js"></script>
<script src="${base}/common/third/js/chess.js?v=9"></script>