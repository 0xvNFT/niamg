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

			<#if testLogin>
			<div id="fb-root"></div>
			<div class="fb-login-button" data-width="60px" data-size="small" data-button-type="login_with" data-layout="" data-auto-logout-link="true" data-use-continue-as="true"></div>

			<button id="loginBtn2" onclick="checkLoginState();" >login</button>
			<button id="logoutBtn2" onclick="logout();" >logout</button>

			<div id="status"></div><!-- 登录状态显示 -->
			</#if>

		</div>
	</div>
</div>
</body></html>
<script>

	window.fbAsyncInit = function() {
		FB.init({
			appId      : '654468426623334',
			cookie     : true,                     // Enable cookies to allow the server to access the session.
			xfbml      : true,                     // Parse social plugins on this webpage.
			version    : 'v17.0'           // Use this Graph API version for this call.
		});


		FB.getLoginStatus(function(response) {   // Called after the JS SDK has been initialized.
			statusChangeCallback(response);        // Returns the login status.
		});
	};

	function logout() {
		FB.logout(function(response) {
			console.log("user logout: "+JSON.stringify(response));
		});
	}

	function checkLoginState() {               // Called when a person is finished with the Login Button.
		FB.getLoginStatus(function(response) {   // See the onlogin handler
			statusChangeCallback(response);
		});
	}

	function statusChangeCallback(response) {  // Called with the results from FB.getLoginStatus().
		console.log(response);                   // The current login status of the person.
		if (response.status === 'connected') {   // Logged into your webpage and Facebook.
			testAPI();
		} else {                                 // Not logged into your webpage or we are unable to tell.

			document.getElementById('status').innerHTML = 'Please login....';
			FB.login(function(response){
				console.log(response); // 在控制台打印返回的access_token
			},{scope: 'id,cover,email,gender,name'});

		}
	}

	function testAPI() {                      // Testing Graph API after login.  See statusChangeCallback() for when this call is made.
		console.log('Welcome!  Fetching your information.... ');
		FB.api('/me?fields=id,name,first_name,last_name,email', function(response) {
			console.log('Successful login for: ' + response.name);
			document.getElementById('status').innerHTML = 'Thanks for logging in, ' + response.name + '!';

			var userInfoStr = JSON.stringify(response);
			console.log('userInfoStr: ' + userInfoStr);

			$.ajax({
				url: ${base} + '/third/login/userInfo.do',
				type: "GET",
				data: {userInfo: userInfoStr},
				success: function (res) {
					console.log('res: ' + res);
				}
			});

		});
	}
</script>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js"></script>
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