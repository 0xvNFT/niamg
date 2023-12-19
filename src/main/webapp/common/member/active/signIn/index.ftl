<!DOCTYPE html>
<html lang="en">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>${pageTitle}-<@spring.message "admin.sign.day"/></title>
	<meta name="keywords" content="${keywords}">
	<meta name="description" content="${description}">
	<#assign res="${base}/common/member/active/signIn">
	<link rel="stylesheet" href="${res}/css/qiandao_style.css?v=1.0.6">
</head>
<body>
    <div class="qiandao-warp">
        <div class="qiandap-box">
            <div class="qiandao-banner">
                <img src="<#if pcsignLogo?has_content>${pcsignLogo}<#else>${res}/images/qiandao_banner_${language}.jpg</#if>" height="551" width="1120">
            </div>
            <div class="qiandao-con clear" style="background-image: url(${res}/images/qiandao_con_${language}.png)">
                <div class="qiandao-left">
                    <div class="qiandao-left-top clear">
                        <div class="current-date"></div>
                        <div class="qiandao-history qiandao-tran qiandao-radius" id="js-qiandao-history"><@spring.message "admin.my.sign"/></div>
                    </div>
                    <div class="qiandao-main" id="js-qiandao-main">
                        <ul class="qiandao-list" id="js-qiandao-list"></ul>
                    </div>
                </div>
                <div class="qiandao-right">
                    <div class="qiandao-top" style="height: 10pc;">
	                    <#if isLogin>
	                        <div class="just-qiandao qiandao-sprits <#if signed>actived</#if>" <#if !signed>onclick="signToday()"</#if> id="js-just-qiandao" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;"></div>
	                    <#else>
	                    	<div class="just-qiandao qiandao-sprits" onclick="showLogin()" id="js-just-qiandao" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;"></div>
	                    </#if>
                        <#if signed><p class="qiandao-notic"><@spring.message "admin.signed.tomorrow"/></p></#if>
                    </div>
                    <div class="qiandao-bottom" style="margin-top: 26px;margin-left: -16px;background:url(<#if pcsignruleLogo?has_content>${pcsignruleLogo}</#if>);background-repeat: no-repeat;position: relative;left: 15px">
                    	<div style="height: 427px;overflow: scroll;">
	                        <div class="qiandao-rule-list">
	                           	<#list signRule as ar>
	                           		<#if ar.title>${ar.title}：<br/></#if>
	                           		${ar.content}
	                           		<br/>
	                           	</#list>
	                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- 我的签到 layer start -->
    <div class="qiandao-layer qiandao-history-layer">
        <div class="qiandao-layer-con qiandao-radius">
            <a href="javascript:;" class="close-qiandao-layer qiandao-sprits" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;"></a>
            <ul class="qiandao-history-inf clear">
                <li>
                    <p><@spring.message "admin.deposit.username"/></p>
                    <h4>${signUser}</h4>
                </li>
                <li>
                    <p><@spring.message "admin.money.values"/></p>
                    <h4 class="user_money">${money}</h4>
                </li>
                <li>
                    <p><@spring.message "admin.score.cash.values"/></p>
                    <h4>${score}</h4>
                </li>
                <li>
                    <p><@spring.message "admin.continue.signs"/></p>
                    <h4>${signCount}</h4>
                </li>
            </ul>
            <div class="qiandao-history-table">
                <table>
                    <thead>
                        <tr>
                            <th><@spring.message "admin.sign.date"/></th>
                            <th><@spring.message "admin.continue.sign.days"/></th>
                            <th><@spring.message "admin.jacks"/></th>
                        </tr>
                    </thead>
                    </table>
                    <table>
                        <tbody>
                        <#list signRecordList as sr>
                        <tr>
                            <td>${sr.signDate}</td>
                            <td>${sr.signDays}</td>
                            <td><#if sr.score gt 0>
                                    <@spring.message "admin.scores"/>:${sr.score}
                                </#if>
                                <#if sr.money gt 0>
                                    <@spring.message "admin.color.cash"/>:${sr.money}
                                </#if>
                            </td>
                        </tr>
                        </#list>
                    </tbody>
                	</table>
            </div>
        </div>
        <div class="qiandao-layer-bg"></div>
    </div>
    <!-- 我的签到 layer end -->
    <!-- 签到 layer start -->
    <div class="qiandao-layer qiandao-active" id="showSignInfo" style="display: none;">
        <div class="qiandao-layer-con qiandao-radius">
            <a href="javascript:;" class="close-qiandao-layer qiandao-sprits" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;"></a>
            <div class="yiqiandao clear">
                <div class="yiqiandao-icon qiandao-sprits" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;"></div><@spring.message "admin.continue.signed"/><span>${signCount}</span><@spring.message "admin.unit.day"/>
            </div>
            <div class="qiandao-jiangli qiandao-sprits" style="background-image: url(${res}/images/qiandao_sprits_${language}.png);background-repeat:no-repeat;">
                <span class="qiandao-jiangli-num"><@spring.message "admin.scores.into"/></span>
            </div>
            <a href="javascript:;" class="close-qiandao-layer qiandao-share qiandao-tran" style="position: relative;top: 0px;right: 0px"><@spring.message "manager.sure.name"/></a>
        </div>
        <div class="qiandao-layer-bg"></div>
    </div>
    <!-- 签到 layer end -->
    <script type="text/javascript">
    	var base = '${base}';
    	var isLogin = '${isLogin}';
    </script>
    <script src="${base}/common/js/jquery-1.12.4.min.js"></script>
    <script src="${base}/signByMonth.do"></script>
    <script src="${res}/js/qiandao_js.js?v=1.0.4"></script>
	<script type="text/javascript">
		function stopss(){
		   return false;
		}
		document.oncontextmenu=stopss;
		function showLogin(){
		   var r=confirm("<@spring.message "admin.first.login"/>!")
		   	if (r==true){
		   		window.open('${base}/index.do');
		    }
		}
	</script>
	<style>
	::-webkit-scrollbar {
	  width: 14px;
	  height: 14px;
	}

	::-webkit-scrollbar-track,
	::-webkit-scrollbar-thumb {
	  border-radius: 999px;
	  border: 5px solid transparent;
	}

	::-webkit-scrollbar-track {
	  box-shadow: 1px 1px 5px rgba(0,0,0,.2) inset;
	}

	::-webkit-scrollbar-thumb {
	  min-height: 20px;
	  background-clip: content-box;
	  box-shadow: 0 0 0 0px rgba(0,0,0,.2) inset;
	}

	::-webkit-scrollbar-corner {
	  background: transparent;
	}
	</style>
 </body>
 </html>
