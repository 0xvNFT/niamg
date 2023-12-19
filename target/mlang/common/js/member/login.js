$(function() {
	var $form = $("#memberLoginForm");
	var $username=$form.find("[name='username']"),$pwd=$form.find("[name='password']")
	,$verifyCode=$form.find("[name='verifyCode']");
	function doLogin() {
		var username = $username.val().trim()
			,pwd = $pwd.val()
			,verifyCode=$verifyCode.val();
		if (username == '') {
			layer.tips(Admin.inputAccount,$username);
			$username.focus();
			return;
		}
		if (pwd == '') {
			layer.tips(Admin.inputPass,$pwd);
			$pwd.focus();
			return;
		}
		if (verifyCode == '') {
			layer.tips(Admin.verifyCodeStr,$verifyCode);
			$verifyCode.focus();
			return;
		}
		$.ajax({
			type:"POST",
			url : baseUrl+"/login.do",
			data : {
				username : username,
				password : pwd,
				verifyCode : verifyCode
			},
			success : function(data, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					window.location.href = baseUrl;
				} else {// 后台异常
					layer.msg(data.msg||"后台异常，请联系管理员!");
					refreshVerifyCode();
				}
			}
		});
	}
	function refreshVerifyCode() {
		$("#verifyImg").attr("src",baseUrl+"/loginVerifycode.do?time=" + (new Date()).getTime());
		$verifyCode.val("");
	}
	$("#verifyImg").click(function(){
		refreshVerifyCode();
	});
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
	//试玩
	$("#shiwanBtn").click(function(){
		$.ajax({
			type:"POST",
			url : baseUrl+"/registerGuest.do",
			success : function(data, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (!ceipstate || ceipstate == 1) {// 正常响应
					window.location.href = baseUrl;
				} else {// 后台异常
					layer.msg(data.msg||"后台异常，请联系管理员!");
					refreshVerifyCode();
				}
			}
		});
		return false;
	});
});