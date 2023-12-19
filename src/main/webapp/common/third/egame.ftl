<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${base}/common/css/font-awesome5/css/all.min.css">
    <link rel="stylesheet" href="${base}/common/third/css/egame.css?v=4">
    <script type="text/javascript">var base = '${base}'</script>
</head>
<body>
<div class="cbox" id="cbox">
	<div id="curPlatform" style="display:none">${curPlatform}</div>
    <#if isLogin><#include "/common/third/include/thirdMoneyChange.ftl"/>
    <nav class="nav"><#else>
        <nav class="nav nav1"></#if>
            <ul>
                <#if platform.ag>
                    <li :class="{on:tabTxt == 'ag'}" @click="tabChange('ag')" data-type="ag">
                        <a href="javascript:;"><@spring.message "admin.ag.electronic"/></a>
                    </li>
                </#if>
                <#if platform.bbin>
                  <li :class="{on:tabTxt == 'bbin'}" @click="tabChange('bbin')" data-type="bbin">
                      <a href="javascript:;"><@spring.message "admin.bbin.electronic"/></a>
                  </li>
                    <#--<li :class="{on:tabTxt == 'bbin'}">-->
                        <#--<a <#if isLogin>href="${base}/third/forwardBbin2.do?gameType=game" target="_blank"-->
                           <#--<#else>href="javascript:void(0);" class="unlogin"</#if>><@spring.message "admin.bbin.electronic"/>-->
                        <#--</a>-->
                    <#--</li>-->
                </#if>
                <#if platform.mg>
                    <li :class="{on:tabTxt == 'mg'}" @click="tabChange('mg')" data-type="mg">
                        <a href="javascript:;"><@spring.message "admin.mg.electronic"/></a>
                    </li>
                </#if>
                <#if platform.skywind>
                    <li :class="{on:tabTxt == 'skywind'}" @click="tabChange('skywind')" data-type="skywind">
                        <a href="javascript:;"><@spring.message "admin.sky.wind.ele"/></a>
                    </li>
                </#if>
                <#if platform.ab>
                    <li :class="{on:tabTxt == 'ab'}">
                        <a <#if isLogin>href="${base}/third/forwardAb.do?gameType=1" target="_blank"
                           <#else>href="javascript:void(0);" class="unlogin"</#if>><@spring.message "admin.ab.ele"/>
                        </a>
                    </li>
                </#if>
                <#if platform.cq9>
                     <li :class="{on:tabTxt == 'cq9'}" @click="tabChange('cq9')" data-type="cq9">
                         <a href="javascript:;"><@spring.message "admin.nine.ele"/></a>
                     </li>
                    <#--<li :class="{on:tabTxt == 'cq9'}">-->
                        <#--<a <#if isLogin>href="${base}/third/forwardCq9.do" target="_blank"-->
                           <#--<#else>href="javascript:void(0);" class="unlogin"</#if>><@spring.message "admin.nine.ele"/></a>-->
                    <#--</li>-->
                </#if>
                <#if platform.pt>
                    <li :class="{on:tabTxt == 'pt'}" @click="tabChange('pt')" data-type="pt">
                        <a href="javascript:;"><@spring.message "admin.pt.ele"/></a>
                    </li>
                </#if>
                <#if platform.pg>
                    <li :class="{on:tabTxt == 'pg'}" @click="tabChange('pg')" data-type="pg">
                        <a href="javascript:;"><@spring.message "admin.pg.electronic"/></a>
                    </li>
                </#if>
                <#if platform.evo>
                    <li :class="{on:tabTxt == 'evo'}" @click="tabChange('evo')" data-type="evo">
                        <a href="javascript:;"><@spring.message "admin.evo.electronic"/></a>
                    </li>
                </#if>
                <#if platform.pp>
                    <li :class="{on:tabTxt == 'pp'}" @click="tabChange('pp')" data-type="pp">
                        <a href="javascript:;"><@spring.message "admin.pp.electronic"/></a>
                    </li>
                </#if>
                <#if platform.fg>
                    <li :class="{on:tabTxt == 'fg'}" @click="tabChange('fg')" data-type="fg">
                        <a href="javascript:;"><@spring.message "admin.fg.electronic"/></a>
                    </li>
                </#if>
                <#if platform.jl>
                    <li :class="{on:tabTxt == 'jl'}" @click="tabChange('jl')" data-type="jl">
                        <a href="javascript:;"><@spring.message "admin.jl.electronic"/></a>
                    </li>
                </#if>
                <#if platform.jdb>
                    <li :class="{on:tabTxt == 'jdb'}" @click="tabChange('jdb')" data-type="jdb">
                        <a href="javascript:;"><@spring.message "admin.jdb.electronic"/></a>
                    </li>
                </#if>
                <#if platform.tada>
                    <li :class="{on:tabTxt == 'tada'}" @click="tabChange('tada')" data-type="tada">
                        <a href="javascript:;"><@spring.message "admin.tada.electronic"/></a>
                    </li>
                </#if>
                <#if platform.bs>
                    <li :class="{on:tabTxt == 'bs'}" @click="tabChange('bs')" data-type="bs">
                        <a href="javascript:;"><@spring.message "admin.bs.electronic"/></a>
                    </li>
                </#if>
                <#if platform.es>
                    <li :class="{on:tabTxt == 'es'}" @click="tabChange('es')" data-type="es">
                        <a href="javascript:;"><@spring.message "admin.es.electronic"/></a>
                    </li>
                </#if>
                <#if platform.vdd>
                    <li :class="{on:tabTxt == 'vdd'}" @click="tabChange('vdd')" data-type="vdd">
                        <a href="javascript:;"><@spring.message "admin.vdd.electronic"/></a>
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
                                            :src="'${base}/common/third/images/egame/' + val.img"></a>
                            </div>
                            <div class="img-innerwrap-name">{{val.name}}</div>
                        </div>
                        <!-- 2 -->
                        <div class="games-style2">
                            <div class="elenew-game-name ele-view-mini-hiden">
                                <a href="#" class="elenew-game-tool tool-btn-favorite favorite-icon-N"><i
                                            class="fa fa-star fa-lg"></i></a>
                                <h3 :title="val.name">{{val.name}}</h3><img
                                        :src="'${base}/common/third/images/egame/' + val.img">
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
    <script src="${base}/common/third/js/pcData/ag.js?v=3"></script></#if>
<#if platform.mg>
    <script src="${base}/common/third/js/pcData/mg.js?v=1"></script></#if>
<#if platform.pt>
    <script src="${base}/common/third/js/pcData/pt.js?v=1"></script></#if>
<#if platform.evo>
    <script src="${base}/common/third/js/pcData/evo.js?v=1"></script></#if>
<#if platform.pp>
    <script src="${base}/common/third/js/pcData/pp.js?v=2"></script></#if>
<#if platform.fg>
    <script src="${base}/common/third/js/pcData/fg.js?v=1"></script></#if>
<#if platform.jl>
    <script src="${base}/common/third/js/pcData/jl.js?v=1"></script>
</#if>
<#if platform.tada>
    <script src="${base}/common/third/js/pcData/tada.js?v=1"></script>
</#if>
<#if platform.bs>
    <script src="${base}/common/third/js/pcData/bs.js?v=1"></script>
</#if>
<#if platform.pg>
    <script src="${base}/common/third/js/pcData/pg.js?v=2"></script>
</#if>
<#if platform.jdb>
    <script src="${base}/common/third/js/pcData/jdb.js?v=1"></script>
</#if>
<#if platform.bbin>
    <script src="${base}/common/third/js/pcData/bbin.js?v=1"></script>
</#if>
<#if platform.cq9>
    <script src="${base}/common/third/js/pcData/cq9.js?v=4"></script>
</#if>
<#if platform.es>
    <script src="${base}/common/third/js/pcData/es.js?v=1"></script>
</#if>
<#if platform.vdd>
    <script src="${base}/common/third/js/pcData/vdd.js?v=1"></script>
</#if>
<#if isLogin>
    <script src="${base}/common/js/member/thirdMoneyChange.js"></script></#if>
<script src="${base}/common/third/js/egame.js?v=1"></script>