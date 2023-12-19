<!DOCTYPE html><html><head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="initial-scale=1.0,user-scalable=no,maximum-scale=1,width=device-width,minimal-ui,viewport-fit=cover">
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="format-detection" content="telephone=no"/>
<meta name="x5-fullscreen" content="true">
	<title>${msg}</title>
	<style>
	.main{margin:10px auto 0;width:330px;text-align: center;}
	.msg{font-size:16px;color:#827878;padding:20px 0px;}
	.btn{margin:10px auto;display:block;width:200px;height:40px;text-align:center;background:url(${base}/common/images/btn.png);color:#fff;text-decoration:none;line-height:40px;}
	</style>
</head><body>
	<div class="main">
		<img src="${base}/common/images/guestUnPerm.png" width="330px" height="350px">
		<div class="msg">${msg}</div>
		<a class="btn" href="javascript:history.go(-1);"><@spring.message 'base.goBack'/></a>
		<#if !unauth><a class="btn" href="<#if mobile>${base}/m/register.do<#else>${base}/register.do</#if>"><@spring.message 'base.goToRegisterAdMember'/></a></#if>
	</div>
</body></html>
