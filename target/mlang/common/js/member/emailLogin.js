$(function() {
    var $form = $("#memberLoginForm");
    var $email=$form.find("[name='email']"),$pwd=$form.find("[name='password']")
        ,$verifyCode=$form.find("[name='verifyCode']");
    function doLogin() {
        var email = $email.val().trim()
            ,pwd = $pwd.val()
            ,verifyCode=$verifyCode.val();
        if (email == '') {
            layer.tips(Admin.validEmail,$email);
            $email.focus();
            return;
        }
        if (pwd == '') {
            layer.tips(Admin.inputPass,$pwd);
            $pwd.focus();
            return;
        }
        // if (verifyCode == '') {
        //     layer.tips(Admin.verifyCodeStr,$verifyCode);
        //     $verifyCode.focus();
        //     return;
        // }
        $.ajax({
            type:"POST",
            url : baseUrl+"/emailLogin.do",
            data : {
                email : email,
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
        $("#verifyImg").attr("src",baseUrl+"/emailLoginVerifycode.do?time=" + (new Date()).getTime());
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
});