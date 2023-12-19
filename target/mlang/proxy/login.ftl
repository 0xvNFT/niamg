<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=yes">
    <title>代理后台</title>
    <link id="bootstrap-css" rel="stylesheet" href="${base}/common/js/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/common/css/adminLoginCss.css?v=2"/>
</head>
<body>
<div class="login-container container">
	<div class="row">
		<div class="col-md-6 col-lg-5 col-sm-8 pull-right text-center">
			<div class="login">
	            <div class="login-form text-left">
	            		<div class="form-group">
				    		<label for="username"><@spring.message "admin.username"/>:</label>
				    		<div class="input-group input-group-lg">
						  	<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
						   	<input id="username" type="text" class="form-control" placeholder="<@spring.message "admin.username"/>">
						</div>
				  	</div>
				  	<div class="form-group">
				    		<label for="password"><@spring.message "admin.password"/>:</label>
				    		<div class="input-group input-group-lg">
						  	<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						   	<input id="password" type="password" class="form-control" placeholder="<@spring.message "admin.password"/>">
						</div>
				  	</div>
				  	<div class="form-group">
				    		<label for="verifyCode"><@spring.message "admin.login.vcode"/>:</label>
				    		<div class="input-group input-group-lg">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-picture"></i></span>
                            <input id="verifyCode" class="form-control" placeholder="<@spring.message "admin.login.vcode"/>" type="text" maxlength="6">
                            <span class="input-group-addon addon2"><img id="verifyImg" src="${proxyBase}/logVerifycode.do" border="0" width="89" height="38" alt="<@spring.message "admin.login.flush.vcode"/>"></span>
                        </div>
				  	</div>
	                <button type="button" id="loginBtn"><@spring.message "admin.login.btn"/></button>
	            </div>
	        </div>
		</div>
	</div>
</div>
</body>
</html>
<script src="${base}/common/js/jquery-1.12.4.min.js"></script>
<script src="${base}/common/js/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="${base}/common/js/layer/layer.js"></script>
<script>
    $(function () {
        var $username = $("#username"), $pwd = $("#password"), $verifyCode = $("#verifyCode,span.addon2"),$verifyImg=$("#verifyImg");

        function doLogin() {
            var username = $username.val().trim()
                    , pwd = $pwd.val()
                    , verifyCode = $verifyCode.val();
            if (username == '') {
                layer.tips("请输入账号", $username,{tips: [4, '#15cc12']});
                $username.focus();
                return;
            }
            if (pwd == '') {
                layer.tips("请输入密码", $pwd,{tips: [4, '#15cc12']});
                $pwd.focus();
                return;
            }
            if (verifyCode == '') {
                layer.tips("请输入验证码", $verifyCode,{tips: [4, '#15cc12']});
                $verifyCode.focus();
                return;
            }
            $.ajax({
                type: "POST",
                url: "${proxyBase}/login.do",
                data: {
                    username: username,
                    password: pwd,
                    verifyCode: verifyCode
                },
                success: function (data, textStatus, xhr) {
                    var ceipstate = xhr.getResponseHeader("ceipstate");
                    if (!ceipstate || ceipstate == 1) {// 正常响应
                        window.location.href = "${proxyBase}";
                    } else {// 后台异常
                        var msg = data.msg || "后台异常，请联系管理员!";
                        layer.msg(msg,{icon: 5});
                        refreshVerifyCode();
                    }
                }
            });
        }

        function refreshVerifyCode() {
        		if($verifyImg.length){
            		$verifyImg.attr("src", "${proxyBase}/logVerifycode.do?time=" + (new Date()).getTime());
            }
            $verifyCode.val("");
        }
        $verifyCode.click(function () {
            refreshVerifyCode();
        }).keyup(function (event) {
            if (event.keyCode == 13) {
                doLogin();
            }
        });
        $username.keyup(function (event) {
            if (event.keyCode == 13) {
                if (!$pwd.val()) {
                    $pwd.focus();
                    return;
                }
                if (!$verifyCode.val()) {
                    $verifyCode.focus();
                    return;
                }
                doLogin();
            }
        });
        $pwd.keyup(function (event) {
            if (event.keyCode == 13) {
                if (!$verifyCode.val()) {
                    $verifyCode.focus();
                    return;
                }
                doLogin();
            }
        });
        $("#loginBtn").click(function () {
            doLogin();
            return false;
        });
    });
</script>
