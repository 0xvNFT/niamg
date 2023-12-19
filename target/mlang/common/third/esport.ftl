<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="${base}/common/third/css/live.css?v=3">
    <script type="text/javascript">var base='${base}'</script>
</head>
<body>
<div class="cbox">
<#if isLogin><#include "/common/third/include/thirdMoneyChange.ftl"/>
    <nav class="nav"><#else><nav class="nav nav1"></#if>
        <ul>
            <#if platform.djlh>
                <li class="item sport_span">
                    <a <#if isLogin>href="${base}/third/forwardDjlh.do" target="_blank"class="ico ag-ico"<#else>href="javascript:void(0);"class="ico ag-ico unlogin"</#if>><span style="margin-left:-15px"><@spring.message "admin.fire.esport"/></span></a>
                </li>
            </#if>
            <#if platform.avia>
                <li <#if curPlatform == "avia">  class="init-highlight" </#if>>
                <a <#if isLogin>href="${base}/third/forwardAvia.do" target="_blank"class="ico ag-ico"<#else>href="javascript:void(0);"class="ico ag-ico unlogin"</#if>><span style="margin-left:-15px"><@spring.message "admin.asia.esport"/></span></a>
                </li>
            </#if>
        </ul>
    </nav>
    <main class="card_box">
        <ul>
            <#if platform.djlh>
                <li>
                    <div class="box">
                        <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/esport/esport_4.png"></a></div>
                        <div class="box-content">
                            <a <#if isLogin>href="${base }/third/forwardDjlh.do" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                <img src="${base}/common/third/images/esport/esport_4.png.png"></a></div>
                    </div>
                </li>
            </#if>
            <#if platform.avia>
                <li>
                    <div class="box">
                        <div class="box-img"><a href="javascript:void(0)"><img src="${base}/common/third/images/esport/avia-esport-bg.png"></a></div>
                        <div class="box-content">
                            <a <#if isLogin>href="${base }/third/forwardAvia.do" target="_blank"<#else>href="javascript:void(0);"class="unlogin"</#if>>
                                <img src="${base}/common/third/images/esport/avia-esport-${language}.png"></a></div>
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