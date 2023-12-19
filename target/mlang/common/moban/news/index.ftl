<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${pageTitle}</title>
<#assign res="${base}/common/moban/news">
<script src="${base}/common/js/jquery-1.12.4.min.js"></script>
<link href="${res}/css/standard.css" rel="stylesheet" type="text/css">
<link href="${res}/css/HotNewsHistory.css" rel="stylesheet" type="text/css">
<style type="text/css">
#top {
	background: url(${res}/images/bg_t.png) left top no-repeat;
	width: 720px;
	height: 58px;
}

#container {
	background: url(${res}/images/bg_c.png) left top repeat-y;
	margin: 0 auto;
	padding: 0 37px;
	width: 646px;
}

#footer {
	background: url(${res}/images/bg_f.png) left top no-repeat;
	padding-top: 20px;
	width: 720px;
	height: 35px;
}
</style>
</head>
<body>
	<div id="main-wrap">
		<div id="top">
			<div id="title"></div>
		</div>
		<div id="container">
			<div class="content">
				<div class="date">
					<span class="content-title">日期</span>
				</div>
				<div class="msg">
					<span class="content-title">内容</span>
				</div>
				<div class="clear"></div>
			</div>
			<div class="line"></div>
		</div>
		<div id="footer"></div>
	</div>
	<script type="text/javascript">
		$(function(){
			$.ajax({
				url : "${base}/newNotices.do",
				data:{code:13},
				type : "post",
				dataType : 'json',
				success : function(r) {
					var col='';
					for(var i=0;i<r.length;i++){
						col+='<div class="content">';
						col+='<div class="date">';
						col+='<span class="inner">'+r[i].updateTime+'</span>';
						col+='</div>';
						col+='<div class="msg">';
						col+='<p class="inner-title">'+r[i].content+'</p>';
						col+='</div>';
						col+='<div class="clear"></div>';
						col+='</div>';
						col+='<div class="line"></div>';
					}
					$("#container").append(col);
				}
			});
		})
	</script>
</body>
</html>