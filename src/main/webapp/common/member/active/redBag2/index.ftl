<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>${pageTitle}-<@spring.message "admin.grab.red.packet"/></title>
	<link rel="shortcut icon" href="${base}/images/favicon.ico">
	<meta name="keywords" content="${keywords}">
	<meta name="description" content="${descript}">
	<meta name="viewport" content="width=device-width,user-scalable=no,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0">
	<#assign res="${base}/common/member/active/redBag2">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/reset.css?v=1.0.1" type="text/css">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/global.css?v=1.0.7" type="text/css">
	<link rel="stylesheet" rev="stylesheet" href="${res}/css/sweetalert.css?v=1.0.1" type="text/css">
	<script src="${base}/common/lang/${language}.js?v=6"></script>
	<script>
		var base = '${base}';
		var res = '${res}';
		var isLogin = '${isLogin}';
		var baseUrl = "${base}";
		var nowTime = '${nowTime}';
		var nowTimeStamp = '${nowTimeStamp}';
		var fixRedPacketId = '${fixRedPacketId}';
	</script>
	<script type="text/javascript" src="${base}/common/js/jquery-1.12.4.min.js"></script>
	<script type="text/javascript" src="${res}/js/sweetalert.min.js?v=1.0.1"></script>
	<script type="text/javascript" src="${res}/js/countDown.js"></script>
	<script type="text/javascript" src="${base}/common/js/artTemplate/template.js"></script>
	<script type="text/javascript" src="${res}/js/index.js?v=1.0.6"></script>

</head>
<body>
	<!-- banner -->
	<div class="banner">
		<div class="inner">
			<div class="redTitle">
				<img src="${res}/images/H5_crazy.png">
			</div>
			<div class="dasojbs">
				<div class="sdaojwz" id="hb-message">
					<img src="${res}/images/h5_hb_wenan2.png"/>
					<div id="redBagDescTitle">
							
					</div>
					<div class="count_down">
						<span class="day_num"></span>
						<span class="hour_num"></span>
						<span class="min_num"></span>
						<span class="sec_num"></span>
					</div>
				</div>
					
				<div class="cl"></div>
			</div>
			<div class="cl"></div>
			<#if !isLogin>
			<div class="shubao">
				<a href="${base}/promp/login.do" class="qbtbn">
					<h3>Get it Now</h3>
				</a>
			</div>
			</#if>
			<#if isLogin>
			  <div class="shubao">
				<a href="javascript:;" class="qbtbn" onclick="redBagUtil.getPacket();">
					<h3>Get it Now</h3>
				</a>
			</div>
			</#if>

				<div class="cl"></div>
		</div>
	</div>
	<!-- banner -->
	<div class="cl" style="height:30px"></div>
	<!-- neir -->
	<div class="inner awardlist">
		<!--  -->
		<div class="cl"></div>
		<div class="list-Title">
			<div class="awardHeader">
                <img src="${res}/images/pc_hb_l.png" alt="">
                <h1>Awards list</h1>
                <img src="${res}/images/pc_hb_r.png" alt="">
            </div>
		</div>
		<!--  -->
		<div class="cont">
			<!--  -->
			<div class="kuai">
				<div class="h10"></div>
				<div class="luntop luntop2">
					<ul id="announce"></ul>
				</div>
			</div>
		<div class="cl"></div>
			
		</div>
	</div>
	<div class="cl h10"></div>
	<div class="foot">
		Copyright © ${pageTitle} Reserved 
	</div>
	<!--  -->
	<#--  open redbag  -->
	<div class="mainBlur mainBlurs">
        <div class="mainRewards mainR">
            <div class="openFolderDivs">
                <div class="openFolderAbsolute">
                    <img src="${res}/images/h5_hb_icon_shut.png" alt="" class="close1">
                    <img src="${res}/images/pc_tc_hb.png" class="pc_tc_hb">
                	<div class="reward">
                        <div class="gxtitle">
                            <img src="${res}/images/pc_hb_tc_wenli.png" height="10" alt="">
                            <small>Parabéns Ganhou</small> 
                            <img src="${res}/images/pc_hb_tc_wenli.png" height="10" alt="">
                        </div>   
                        <p class="winmoney"></p>  
                    </div>
                </div>
            </div>
        </div>
    </div>

<script type="text/javascript">

	$(function(){
		redBagUtil.initRedBag();//初始化红包
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
	};
	$('.close1').click(() => {
		$('.mainBlur').hide();
		$('.mainRewards').hide();
	})

</script>
<script type="text/html" id="red_bag_recore_tpl">
	{{each list as j i}}
		<li>
			<span class="w140 huang pl20"><em class="cw"><@spring.message "admin.obtain.con"/> </em> {{j.username}}</span>
			<span class="w140 cw">  <@spring.message "admin.obtain"/> <em class="huang">{{j.money}}</em></span>
			<span class="w140 cw right">{{j.createDatetime}}</span>
		</li>
	{{/each}}
</script>
</html>