<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${pageTitle }--线路检测</title>
<#assign res="${base}/common/moban/userXljc">
<link href="${res}/css/style.css?v=1" rel="stylesheet" type="text/css">
<link href="${res}/css/animate.css" rel="stylesheet" type="text/css">
<link href="${res}/css/med.css?v=1" rel="stylesheet" type="text/css">
<script src="${base}/common/js/jquery-1.12.4.min.js"></script>
</head>
<body>
	<div class="header">
    	<div class="a_auto">
        	<div class="head">
            	<div class="head_1">
            		<#if swf>
            			<embed src="${base}/images/xljc.swf?gdf" quality="high" class="logo" wmode="transparent" type="application/x-shockwave-flash" style="max-width: 100%;max-height: 100%;">
            		<#else>
            			<img src="${stationLogo}" style="max-width: 100%;max-height: 100%;">
            		</#if>
            	</div>
                <div class="head_3">
                    <a target="_blank" href="${kfUrl }">
                        <img src="${res}/images/img3.png" alt=""></a><a target="_blank" href="${kfUrl }">
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class="bar">
    	<div class="bar_1"><img src="${res}/images/img4.jpg" alt=""></div>
        <div class="bar_2">
        	<div class="a_auto">
        	<div class="bar_n">
            	<ul id="xljc_domain"></ul>
            </div>
        </div>
        </div>
    </div>
    
    <p class="about_link" style="margin: 0px; padding: 0px; color: rgb(102, 102, 102); line-height: 30px; font-family: tahoma, arial, 宋体, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; orphans: auto; text-align: center; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px;">
		<a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(1)">
	关于我们</a>|<span class="Apple-converted-space">&nbsp;</span>
		<a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(2)">取款帮助</a>|<span class="Apple-converted-space">&nbsp;</span><a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(3)">存款帮助</a>|<span class="Apple-converted-space">&nbsp;</span><a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(4)">联盟方案</a>|<span class="Apple-converted-space">&nbsp;</span><a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(5)">联盟协议</a>|<span class="Apple-converted-space">&nbsp;</span><a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(6)">联系我们</a>|<span class="Apple-converted-space">&nbsp;</span><a style="margin: 0px 10px; padding: 0px; text-decoration: none; outline: none; color: rgb(102, 102, 102);" href="javascript:tohelp(7)">常见问题</a></p>
	<p class="about_mt remind" style="margin: 0px; padding: 3px 0px; color: rgb(153, 153, 153); height: 20px; line-height: 20px; font-family: tahoma, arial, 宋体, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; orphans: auto; text-align: center; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px;">
	提醒：购买彩票有风险，在线投注需谨慎，不向未满18周岁的青少年出售彩票！</p>
	<p style="margin: 0px; padding: 0px; color: rgb(0, 0, 0); font-family: tahoma, arial, 宋体, sans-serif; font-size: 12px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 18px; orphans: auto; text-align: center; text-indent: 0px; text-transform: none; white-space: normal; widows: 1; word-spacing: 0px; -webkit-text-stroke-width: 0px;">
	<img src="${res}/images/1.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/2.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/3.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/4.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/7.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/9.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"><span class="Apple-converted-space">&nbsp;</span><img src="${res}/images/10.png" style="margin: 0px 15px 5px 0px; padding: 0px; vertical-align: middle; border: 0px;"></p>

<script>
	${domain}
	var col = '';
	for(var i = 1;i<=domain.length;i++){
		col += '<li class="li_'+i+'"><a href="http:\/\/'+domain[i-1]["name"]+'" target="_blank">';
		col += '<i><img src="${res}/images/img10.png"></i></a>';
		col += '<b><a target="_blank" href="http:\/\/'+domain[i-1]["name"]+'"><img src="${res}/images/img11.png"></a></b>';
		col += '<a href="http:\/\/'+domain[i-1]["name"]+'" target="_blank"><div class="am_1">';
		col += '<p>'+domain[i-1]["name"]+'</p><input type="text" class="pus" id="sudu'+i+'"><em></em>';
		col += '<span>线路'+i+'</span></div></a></li>';
	}
	$('#xljc_domain').html(col);	
	
	function tohelp(code){
		window.open('${base}/userCenter/help/'+code+'.do');
	}
</script>
</body>
</html>