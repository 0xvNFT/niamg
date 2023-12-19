<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${pageTitle}-<@spring.message "admin.grab.red.packet"/></title>
	<link rel="shortcut icon" href="${base}/images/favicon.ico">
	<meta name="keywords" content="${keywords}">
	<meta name="description" content="${descript}">
	<#assign res="${base}/common/member/active/redBag">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/reset.css?v=1.0.1" type="text/css">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/global.css?v=1.0.7" type="text/css">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/sweetalert.css?v=1.0.1" type="text/css">
	<script src="${base}/common/lang/${language}.js?v=6"></script>
	<script>
		var base = '${base}';
		var nowTime = '${nowTime}';
		var nowTimeStamp = '${nowTimeStamp}';
	</script>
	<script type="text/javascript" src="${base}/common/js/jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="${res}/js/sweetalert.min.js?v=1.0.1"></script>
	<script type="text/javascript" src="${res}/js/countDown.js"></script>
	<script type="text/javascript" src="${base}/common/js/artTemplate/template.js"></script>
	<script type="text/javascript" src="${res}/js/index.js?v=1.0.9"></script>
</head>
<body>
	<div class="gonggao">
		<div class="gaoti">
			<span class="fl l30 cw f12 pl10"><@spring.message "admin.act.notice"/>：</span>
			<div class="fr">
				<span class="db w22 tac fl  cup xiao">-</span>
				<span class="db fl  tac w22 cup  chag">X</span>
			</div>
		</div>
		<div class="cl h10"></div>
		<div class="luntop luntop1">
			<marquee style="height:120px;" scrolldelay="350" direction="up" behaviour="Scroll" onmouseover=this.stop() onmouseout=this.start()>
				<table>
					<tr>
						<td>
							<#list redBagNotice as rbn>
								<li>${rbn.title}：${rbn.content}</li>
							</#list>
						</td>
					</tr>
				</table>
			</marquee>
		</div>
	</div>
	<div class="header">
		<div class="inner">	
			<div class="logo"><h1><a href="javascript:;">
				<img src="<#if stationLogo?has_content>${stationLogo}<#else>${base}/images/logo.png</#if>" alt="" style="height: 100%;max-width: 100%;"> 
			</a></h1></div>

			<div class="nav fr">
				<ul>
					<li class="cur"><a href="${base}/index.do"><@spring.message "admin.official.page"/>    </a></li>
					<li><a href="${base}/m" target="_blank"><@spring.message "admin.mobile.bet"/>    </a></li>
					<li><a href="${base}/lottery.do" target="_blank"><@spring.message "admin.lottery.hall"/>    </a></li>
					<li><a href="${base}/activity.do" target="_blank"><@spring.message "admin.act.center"/>    </a></li>
					<li><a href="${kfUrl}" target="_blank"><@spring.message "admin.online.service"/></a></li>
				</ul>
			</div>
			<div class="cl"></div>
		</div>
	</div>
	<!-- banner -->
	<div class="banner">
			<div class="inner">
				<div class="dasojbs">
					<span class="sadosp" id="hb-clock-d"></span>
					<span class="sadosp" id="hb-clock-h"></span>
					<div class="sdaojwz" id="hb-message">
						<div style="margin-top: -25px;font-size: 15px;" id="redBagDescTitle">
							<#--<@spring.message "admin.act.end.time"/>-->
						</div>
						<div class="count_down">
							<span class="day_num"></span>
							<span class="hour_num"></span>
							<span class="min_num"></span>
							<span class="sec_num"></span>
						</div>
					</div>
					<span class="sadosp" id="hb-clock-m"></span>
					<span class="sadosp" id="hb-clock-s"></span>
					<div class="cl"></div>
				</div>
				<div class="cl"></div>
				<div class="shubao">
					<div style="text-align: center;" class="cont"><p id="red-bag-title"></p></div>
					<#if language =='cn'>
						<a href="javascript:;" class="qbtbn" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'en'>
						<a href="javascript:;" class="qbtbnen" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'id'>
						<a href="javascript:;" class="qbtbnid" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'in'>
						<a href="javascript:;" class="qbtbnin" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'ms'>
						<a href="javascript:;" class="qbtbnms" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'th'>
						<a href="javascript:;" class="qbtbnth" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'vi'>
						<a href="javascript:;" class="qbtbnvi" onclick="redBagUtil.getPacket();"></a>
					<#elseif language == 'ja'>
						<a href="javascript:;" class="qbtbnja" onclick="redBagUtil.getPacket();"></a>
					</#if>
				</div>
				<div class="cl"></div>
			</div>
		</div>
	<!-- banner -->
	<div class="cl "></div>
	<!-- neir -->
	<div class="inner">
		<!--  -->
		<div class="cl h20"></div>
		<!--  -->
		<div class="cont">
			<!--  -->
			<div class="kuai">
				<div class="kuaiti tac"><@spring.message "admin.red.list.receive"/></div>
				<div class="h10"></div>
				<div class="luntop luntop2" style="height: 130px;">
					<ul id="announce"></ul>
				</div>
			</div>
			<div class="cl h10"></div>
			<div class="red-bag-rule-content">
				<#list redBagRule as rbg>
					<h5>${rbg.title}</h5>
					<div class="cl h10"></div>
					<p>${rbg.content}</p>
					<div class="cl h10"></div>
				</#list>
		</div>
	</div>
	</div>
	<div class="cl h10"></div>
	<div class="foot">
		Copyright © ${pageTitle} Reserved 
	</div>
	<!--  -->


    <!-- 查询窗口 start -->
	<div class="tanchuceng" id="querywin" style="display:none; z-index:99999999">
		<div class="tanin">
			<div class="chacha" onclick="closeQueryWin();">×</div>
			<div class="tantoux"><@spring.message "admin.turn.vip.acc.query"/></div>
			<div class="cl h35"></div>
			<div class="tac shuzangh">
				<span class=""><@spring.message "admin.money.history.username"/>：</span>
				<input id="username-query" type="text" class="shuimsnt" value="">
				<a href="javascript:;" class="chaaa" onclick="query();"><img src="${res}/images/chaa.png" class="vm" alt=""></a>
			</div>
			<div class="cl h15"></div>
			<div class="chatablewa">
				<table class="chtable">
					<tbody><tr class="toum">
						<td><@spring.message "admin.red.packet.cash"/></td>
						<td><@spring.message "admin.receive.time"/></td>
						<td><@spring.message "admin.is.not.seed"/></td>
					</tr>
					</tbody><tbody id="query-result">
                    
                    </tbody>
				</table>
			</div>
			<div class="cl h35"></div>
		</div>
	</div>
    <!-- 查询窗口 end -->
    <style>
    	.swal2-popup .swal2-styled.swal2-confirm {
		    background-color: #d63030 !important;
		}
    </style>
<script type="text/javascript">
$(function(){
	redBagUtil.initRedBag();//初始化红包
	$('body').css('background','#6E0001 url(${base}/common/member/active/redBag/images/red_bag_bg_${language}.jpg?v=2) no-repeat top center');
})

function formatDate(now) {
　　var year = now.getFullYear(),
　　month = now.getMonth() + 1,
　　date = now.getDate(),
　　hour = now.getHours(),
　　minute = now.getMinutes(),
　　second = now.getSeconds();
	if(month < 10){
		month = '0' + month;
	}
　　return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}
</script>
<script type="text/html" id="red_bag_recore_tpl">
{{each list as j i}}
	<li>
		<span class="w140 huang pl20"><em class="cw"><@spring.message "admin.obtain.con"/> </em> {{j.username}}</span>
		<span class="w140 cw">  <@spring.message "admin.obtain"/> <em class="huang">{{j.money}}</em> <@spring.message "admin.red.winner"/>       </span>
		<span class="w120 cw">{{j.createDatetime}}</span>
	</li>
{{/each}}
</script>
</body>
</html>