<!DOCTYPE html><html><head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title><@spring.message 'base.maintenance'/></title>
</head><body>
	<div style="margin:60px auto 0;width:590px;position:relative;">
		<img src="${base}/common/images/maintenance/weihu_${language}.png" width="600px">
		<div style="position:absolute;top:175px;left:178px;font-size:14px;color:#2f2d2d;font-weight: bold;">${cause}</div>
		<#if backUrl?has_content>
			<a href="${backUrl}"><@spring.message 'base.goBack'/></div>
		</#if>
	</div>
</body></html>