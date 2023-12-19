<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=yes,shrink-to-fit=no">
		<title><@spring.message "admin.title"/></title>
		<link id="bootstrap-css" rel="stylesheet" href="${base}/common/bootstrap4.3.1/css/bootstrap.min.css">
		<link rel="stylesheet" href="${base}/common/css/loginStyle.css" />
		<link rel="stylesheet" href="${base}/common/css/font-awesome5/css/all.min.css" />
	</head>
<body>
<div class="ch-container">
	<div class="card bg-light" style="max-width:28rem;margin:100px auto;">
	  	<div class="card-header text-center"><h2><@spring.message "admin.title"/></h2></div>
	  	<div class="card-body">
		  	<div class="input-group input-group-lg mb-3">
	    			<div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-user"></i></span></div>
				<input type="text" id="username" class="form-control" placeholder="<@spring.message "admin.username"/>">
			</div>
			<div class="input-group input-group-lg mb-3">
				<div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-lock"></i></span></div>
				<input id="password" type="password" class="form-control" placeholder="<@spring.message "admin.password"/>">
			</div>
			<#if googleCode>
    		<div class="input-group input-group-lg mb-3">
    			<div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-check-circle"></i></span></div>
			  	<span class="input-group-addon"><i class="glyphicon glyphicon-check"></i></span>
			   	<input id="verifyCode" class="form-control" placeholder="谷歌验证码" type="text" maxlength="6">
			</div>
             <#else>
		  	<div class="input-group input-group-lg mb-3">
				<div class="input-group-prepend"><span class="input-group-text"><i class="fas fa-check-circle"></i></span></div>
				<input id="verifyCode" class="form-control" placeholder="<@spring.message "admin.login.vcode"/>" type="text" maxlength="6">
				<div class="input-group-append"><span class="input-group-text" style="padding:0px;"><img id="verifyImg" src="${adminBase}/logVerifycode.do" style="border:0px;border-radius:0px 5px 5px 0px;width:89px;height:46px;" alt="<@spring.message "admin.login.flush.vcode"/>"></span></div>
			</div>
		  	</#if>
			<button type="submit" class="btn btn-primary btn-lg btn-block" id="loginBtn"><i class="glyphicon glyphicon-hand-right"></i> <@spring.message "admin.login.btn"/></button>
	  	</div>
	</div>
</div>
</body></html>
<script src="${base}/common/js/jquery-3.5.1.min.js"></script>
<script src="${base}/common/bootstrap4.3.1/js/bootstrap.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
<script>
$(function() {
	var $username=$("#username"),$pwd=$("#password"),$verifyCode=$("#verifyCode");
	function doLogin() {
		var username = $username.val().trim()
			,pwd = $pwd.val()
			,verifyCode=$verifyCode.val();
		if (username == '') {
			layer.tips("<@spring.message "admin.login.input.username"/>",$username);
			$username.focus();
			return;
		}
		if (pwd == '') {
			layer.tips("<@spring.message "admin.login.input.password"/>",$pwd);
			$pwd.focus();
			return;
		}
		if (verifyCode == '') {
			layer.tips("<@spring.message "admin.login.input.vcode"/>",$verifyCode);
			$verifyCode.focus();
			return;
		}
		$.ajax({
			type:"POST",
			url : "${adminBase}/login.do",
			data : {
				username : username,
				password : pwd,
				verifyCode : verifyCode
			},
			success : function(data, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					window.location.href = "${adminBase}";
				} else {// 后台异常
					layer.msg(data.msg||"<@spring.message "admin.login.errmsg"/>");
					refreshVerifyCode();
				}
			}
		});
	}
	function refreshVerifyCode() {
		$("#verifyImg").attr("src","${adminBase}/logVerifycode.do?time=" + (new Date()).getTime());
		$verifyCode.val("");
	}
	$verifyCode.click(function(){
		refreshVerifyCode();
	}).keyup(function(event){
	  	if(event.keyCode ==13){
		  	doLogin();
	  	}
	});
	$username.keyup(function(event){
	  if(event.keyCode ==13){
		  if(!$pwd.val()){
			  $pwd.focus();
			  return;
		  }
		  if(!$verifyCode.val()){
			  $verifyCode.focus();
			  return;
		  }
		  doLogin();
	  }
	});
	$pwd.keyup(function(event){
	  if(event.keyCode ==13){
		  if(!$verifyCode.val()){
			  $verifyCode.focus();
			  return;
		  }
		  doLogin();
	  }
	});
	$("#loginBtn").click(function(){
		doLogin();
		return false;
	});
});
</script>