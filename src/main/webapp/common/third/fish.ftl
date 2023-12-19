<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${base}/common/third/css/fish.css?v=11">
</head>
<body>
    <div class="Max-Center">
        <div style="overflow: hidden;">
            <div class="left">
                <div class="XTB"></div>
                <div class="list">
                    <ul>
                    <#if platform.bg>
                        <li <#if curPlatform == "bg">  class="init-highlight" </#if>><a href="javascript:(0)"><div class="bg"><p><b>BG</b><span><@spring.message "admin.fishing"/></span></p></div></a></li>
                    </#if>
                    <#if platform.bbin>
                        <li <#if curPlatform == "bbin">  class="init-highlight" </#if>><a href="javascript:(0)"><div class="bbin"><p><b>BBIN</b><span><@spring.message "admin.fishing"/></span></p></div></a></li>
                    </#if>
                    <#if platform.ag>
                        <li <#if curPlatform == "ag">  class="init-highlight" </#if>><a href="javascript:(0)"><div class="ag"><p><b>AG</b><span><@spring.message "admin.fishing"/></span></p></div></a></li>
                    </#if>
                    <#if platform.cq9>
                        <li <#if curPlatform == "cq9">  class="init-highlight" </#if>><a href="javascript:(0)"><div class="bg"><p><b>CQ9</b><span><@spring.message "admin.fishing"/></span></p></div></a></li>
                    </#if>
                    </ul>
                </div>
            </div>
            <div class="right">
                <div class="title">
                    <img src="${base}/common/third/images/fish/title_${language}.png">
                </div>
                <div class="YX-content">
                    <div class="content-Details">
                        <!-- BG捕鱼游戏 -->
                        <#if platform.bg>
                            <ul class="BG fd" >
                            <li><a <#if isLogin>href="${base }/third/forwardBg.do?gameType=3&gameId=105" target="_blank"<#else>href="javascript:void(0)" class="unlogin"</#if>>
                                <div class="content-Details-text"><img src="${base}/common/third/images/fish/gpk2.png"><span><@spring.message "admin.bg.fish"/></span></div></a></li>
                            <li><a <#if isLogin>href="${base }/third/forwardBg.do?gameType=3&gameId=411" target="_blank"<#else>href="javascript:void(0)" class="unlogin"</#if>>
                                <div class="content-Details-text"><img src="${base}/common/third/images/fish/gpk1.png"><span><@spring.message "admin.bg.west.fish"/></span></div></a></li>
                        </ul></#if>
                        <!-- BBIN捕鱼游戏 -->
                        <#if  platform.bbin><ul class="BBIN fd" >
                            <li><a <#if isLogin>href="${base }/third/forwardBbin2.do?gameType=fisharea" target="_blank"<#else>href="javascript:void(0)" class="unlogin"</#if>>
                                <div class="content-Details-text"><img src="${base}/common/third/images/fish/gpk2.png"><span><@spring.message "admin.bbin.fish.game.hall"/></span></div></a></li>
                        </ul></#if>
                        <!-- AG捕鱼游戏 -->
                        <#if  platform.ag><ul class="AG fd" >
                            <li><a <#if isLogin>href="${base }/third/forwardAg.do?gameType=HMPL" target="_blank"<#else>href="javascript:void(0)" class="unlogin"</#if>>
                                <div class="content-Details-text"><img src="${base}/common/third/images/fish/gpk1.png"><span><@spring.message "admin.ag.fish.game"/></span></div></a></li>
                        </ul></#if>
                        <#if  platform.cq9><ul class="CQ9 fd" >
                            <li><a <#if isLogin>href="${base}/third/forwardCq9.do" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                <div class="content-Details-text"><img src="${base}/common/third/images/fish/gpk1.png"><span><@spring.message "admin.nine.fish.game"/></span></div></a></li>
                        </ul></#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <#if !isLogin><#include "/common/third/include/unlogin.ftl"/></#if>
<script src="${base}/common/js/jquery-3.5.1.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
    <script src="${base}/common/js/layer/layerWindow.js"></script>
<script>
$(function(){
	$(".list li").hover(function(){
        $(this).addClass("active").siblings().removeClass("active")
        gogo($(this).index())
    }).first().addClass("active");
    function gogo(data){
        $('.content-Details').children().each(function(j){
            if(j==data){
                $(this).show();
            } else{
                $(this).hide();
            }
        })
    };
    gogo(0);
    <#if !isLogin>$(".unlogin").click(function(){
    	layer.open({
	        type : 1,
	        title : '<@spring.message "admin.not.login.now"/>',
	        shadeClose : true,
	        shade : 0.8,
	        area : [ '380px', '200px' ],
	        content : $('#loginfrom')
    	});
    });</#if>
});
</script>
</body>
</html>