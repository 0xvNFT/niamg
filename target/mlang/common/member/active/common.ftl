<script>
    $(function (){
        $.ajax({
            type: "GET",
            async: true,
            url: base + "/index.do",
            success: function() {
                if($.cookie("refresh")!="no"){
                    setTimeout(function(){
                        window.location.reload();
                        $.cookie("refresh","no");
                    },5000);
                }
            }
        });
    });
</script>
<#if isRedBag>
    <div style="position: fixed;bottom: 0px;right: 0px;width: 100px;cursor: pointer;" onclick="<#if isLogin>window.open('${base}/redBag.do')<#else>alert('<@spring.message "admin.first.login"/>!');</#if>">
        <img src="${base}/common/images/active/icon_${language}.png" style="width: 100px" onload="this"/>
    </div>
</#if>
<#if isSignIn>
    <div style="position: fixed;bottom: 20px;left: 20px;height: 244px;width:158px;cursor: pointer;" onclick="window.open('${base}/signIn.do')">
        <img src="${base}/common/images/active/parcel-qian-dao_${language}.png"/>
    </div>
</#if>
<#--<#if isTurnlate>-->
<#--    <div style="position: fixed;bottom: -79px;left: -8px;;height: 244px;width:158px;cursor: pointer;" onclick="window.open('${base}/turnlate.do')">-->
<#--        <img src="${base}/common/images/active/parcel-turnlate.png" style="width: 100%;"/>-->
<#--    </div>-->
<#--</#if>-->
<#if isTurnlate>
    <div style="position: fixed;bottom: -79px;left: -8px;;height: 244px;width:158px;cursor: pointer;" onclick="<#if isLogin>window.open('${base}/turnlate.do')<#else>alert('<@spring.message "admin.first.login"/>!');</#if>">
        <img src="${base}/common/images/active/icon_${language}_turnlate.png" style="width: 100%;"/>
    </div>
</#if>
<#if statisticsCode>
    <div style="display: none" class="station_statistics_code">${statisticsCode }</div>
</#if>
<#include "/common/member/notice/notice.ftl">