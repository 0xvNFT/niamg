<!DOCTYPE html>
<html id="cphtml" style="font-size: 177.778px;">
<head>
	<meta charset="utf-8">
	<title>${pageTitle}</title>
	<#assign res="${base}/common/moban/appDown">
	<link href="${res}/css/mui.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${res}/css/mobile.css?v=1.0.1">
</head>
<body>
	<header class="mui-bar mui-bar-nav">
		<img alt="" src="${stationLogo}" style="max-height: 55px">
		<h1 class="mui-title"><@spring.message "admin.title.name.app.down"/></h1>
	</header>
	<div class="mui-content">
		<div class="topcon" style="width: 100%;">
			<ul>
				<li class="mui-col-xs-6 mui-col-sm-6" style="width: 100%;">
					<h3><@spring.message "admin.app.shopping"/></h3>
					<h4><@spring.message "admin.mobile.game.convent"/></h4>
					<h4><@spring.message "admin.code.download"/></h4>
					<div class="erweima">
						<img src="${qrcode}">
					</div>
					<div class="downlink" style="width: 100%;">
						<a href="${iosAppUrl}" target="_blank" class="ios"><@spring.message "admin.apple.download.version"/></a>
						<div class="mui-clearfix">
						</div>
						<a href="${appUrl}" target="_blank" class="android"><@spring.message "admin.android.download.version"/></a>
					</div>
				</li>
				<div class="mui-clearfix">
				</div>
			</ul>
		</div>
		<#--
		<div class="xianlulist">
			<div class="xianlutit">
				官方线路
			</div>
			<div class="xianludot">
			</div>
			<ul>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="#"><i class="dlxianlu"></i>大陆线路</a>
				</li>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="#"><i class="twxianlu"></i>澳门线路</a>
				</li>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="#" class="czzx"><i class="czcenter"></i>充值中心</a>
				</li>
				<div class="mui-clearfix">
				</div>
			</ul>
			<ul>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="#"><i class="hwxianlu"></i>海外线路</a>
				</li>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="#"><i class="hkxianlu"></i>香港线路</a>
				</li>
				<li class="mui-col-xs-4 mui-col-sm-4 mui-pull-left">
					<a href="${kfUrl}" target="_blank"><i class="amxianlu"></i>在线客服</a>
				</li>
				<div class="mui-clearfix">
				</div>
			</ul>
		</div>
		-->
		<div class="footer">
			<p>
				<span></span><@spring.message "admin.version.long.time"/> © ${pageTitle}
			</p>
		</div>
	</div>
	<style>
		.footer p span {
			background: url(${base}/images/favicon.ico) center center no-repeat;
		}
	</style>
</body>
</html>