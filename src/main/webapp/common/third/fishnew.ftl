<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${base}/common/css/font-awesome5/css/all.min.css">
    <link rel="stylesheet" href="${base}/common/third/css/live.css?v=33">
    <script type="text/javascript">var base = '${base}'</script>
</head>
<body>
<div class="cbox" id="cboxLive">
    <div id="curPlatform" style="display:none">${curPlatform}</div>
    <#if isLogin><#include "/common/third/include/thirdMoneyChange.ftl"/>
    <nav class="nav"><#else>
        <nav class="nav nav1"></#if>
    <ul>
                <#if platform.bbin>
                    <li :class="{on:tabTxt == 'bbinFish'}" @click="tabChange('bbinFish')" data-type="bbinFish">
                        <a href="javascript:;"><p><b>BBIN </b><span><@spring.message "admin.fishing"/></span></p></a>
                    </li>
                </#if>
                <#if platform.cq9>
                    <li :class="{on:tabTxt == 'cq9Fish'}" @click="tabChange('cq9Fish')" data-type="cq9Fish">
                        <a href="javascript:;"><p><b>CQ9 </b><span><@spring.message "admin.fishing"/></span></p></a>
                    </li>
                </#if>

    </ul>
</nav>
    <main class="card_box">
        <!-- 面包屑 -->
        <div>
            <div class="elenew-live-trace elenew-trace-hall-23">
                    <span class="elenew-trace-sub">
                        <span>
                            <span class="gamenew-trace">
                                <span class="gamenew-trace-total">
                                    <a href="javascript:;" @click="gameType(tabTxt,tabTxt+'data')">All</a>
                                </span>
                                <span>
                                    (<b class="gamenew-trace-num-0">{{gameLength}}</b>)
                                </span>
                            </span>
                        </span>
                        <span class="trace-1">
                            <span>&gt;</span>
                            <#if platform.pt><span class="gamenew-trace gamenew-trace-1" v-if="tabTxt == 'pt'">
                                <span class="gamenew-trace-current" @click="gameType('pt','slot')">Slot</span>
                                <span class="gamenew-trace-current" @click="gameType('pt','table')">Roulette</span>
                                <span class="gamenew-trace-current" @click="gameType('pt','video')">Video</span>
                                <span class="gamenew-trace-current" @click="gameType('pt','card')">Card</span>
                            </span></#if>
                            <#if platform.skywind><span class="gamenew-trace gamenew-trace-1" v-if="tabTxt == 'skywind'">
                                <span class="gamenew-trace-current"
                                      @click="gameType('skywind','skyWindNewGame')"><@spring.message "admin.last.new.game"/></span>
                                <span class="gamenew-trace-current"
                                      @click="gameType('skywind','skyWindHotGame')"><@spring.message "admin.hot.game"/></span>
                                </span></#if>
                        </span>
                    </span>
            </div>
            <!-- 搜索框 -->
            <div id="elenew-search-wrap">
                <div class="elenew-search-btn" @click="searchGame"><i class="fa fa-search"></i></div>
                <div class="elenew-search-input"><input type="search" id="elenew-search-game"
                                                        placeholder="<@spring.message "admin.write.game"/>"></div>
            </div>
            <!-- 切换显示 -->
            <div class="elenew-live-view">
                    <span>
                        <a href="javascript:void(0);" class="elenew-viewbtn-block view-active"><i class="fa fa-th"></i></a>
                    </span>
                <span>
                        <a href="javascript:void(0);" class="elenew-viewbtn-mini"><i class="fa fa-align-justify"></i></a>
                    </span>
            </div>
        </div>
        <div class="clear"></div>
        <!-- 内容区 按照div数目和切换栏对应 -->
        <div>
            <!-- 模式1：elenew-view-block 模式2：elenew-view-mini -->
            <div class="elenew-game-wrap elenew-view-block">
                <div class="elenew-game-layout" v-for="(val, key) in searchData">
                    <!-- 1 -->
                    <div class="elenew-img-innerwrap">
                        <div class="mask-wrap curGame">
                            <div class="elenew-game-ctl-wrap inner-one"
                                 v-html="forwardGame1(${isLogin?string("true", "false")},val.type)"></div>
                            <a class="elenew-game-img" href="javascript:void(0)"><img
                                    :src="'${base}/common/third/images/fish/' + val.img"></a>
                        </div>
                        <div class="img-innerwrap-name">{{val.name}}</div>
                    </div>
                    <!-- 2 -->
                    <div class="games-style2">
                        <div class="elenew-game-name ele-view-mini-hiden">
                            <a href="#" class="elenew-game-tool tool-btn-favorite favorite-icon-N"><i
                                    class="fa fa-star fa-lg"></i></a>
                            <h3 :title="val.name">{{val.name}}</h3><img
                                :src="'${base}/common/third/images/fish/' + val.img">
                        </div>
                        <div class="elenew-img-wrap">
                            <div class="elenew-game-ctl-wrap"
                                 v-html="forwardGame1(${isLogin?string("true", "false")},val.type,'cc')"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
<#if !isLogin><#include "/common/third/include/unlogin.ftl"/></#if>
</body>
</html>
<script src="${base}/common/lang/${language}.js?v=4"></script>
<script src="${base}/common/js/jquery-3.5.1.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
<script src="${base}/common/js/layer/layerWindow.js"></script>
<script src="${base}/common/js/vue/vue.2.2.0.js"></script>
<#if platform.ag>
    <script src="${base}/common/third/js/pcData/cq9Fish.js?v=4"></script>
</#if>
<#if platform.bbin>
    <script src="${base}/common/third/js/pcData/bbinFish.js?v=5"></script>
</#if>


<#if isLogin>
    <script src="${base}/common/js/member/thirdMoneyChange.js"></script></#if>
<script src="${base}/common/third/js/fishnew.js?v=2"></script>