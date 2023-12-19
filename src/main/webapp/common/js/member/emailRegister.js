$(function(){
    var $regCenter=$("#regCenter"),
    $img=$regCenter.find(".verification_img>img"),
    $butImg=$regCenter.find('.verification_but>img');
    $reqCodeBtn=$regCenter.find('.verification_but>reqCodeBtn');

    $reqCodeBtn.click(function () {
        var email = $regCenter.find("[name='email']").val();
        if (!email) {
            warnMsg(Admin.validEmail);
            $regCenter.find("[name='email']").focus();
            return;
        }
        $.ajax({
            url: baseUrl + "/reqEmailVcode.do",
            type: "post",
            data: {
                email: email,
                type: 3,
            },
            success: function (result, textStatus, xhr) {
                if (result.success) {
                    warnMsg(Admin.successfulOperation);
                } else {
                    warnMsg(result.msg);
                    // $butImg.click();
                }
            }
        });
    });

    $butImg.click(function () {
        var _this= $(this);
        _this.addClass("verification_onclick");
        $img.attr("src",  "${base}/registerVerifycode.do?timestamp=" + (new Date().getTime()));
        _this.removeClass("verification_onclick");
    }).click();
    $regCenter.find(".register").click(function(){
    var username = $regCenter.find("[name='email']").val();
    if (!username) {
        warnMsg(Admin.validEmail);
        $regCenter.find("[name='email']").focus();
        return;
    }
    var password = $regCenter.find("[name='pwd']").val();
    if (!password) {
        warnMsg(Admin.passNotNull);
        $regCenter.find("[name='pwd']").focus();
        return;
    } else if (password.length < 6) {
        warnMsg(Admin.passNotMinSixNum);
        $regCenter.find("[name='pwd']").focus();
        return;
    }
    var vcode = $regCenter.find("[name='code']").val();
    if (vcode.length == 0) {
        warnMsg(Admin.verifyCodeStr);
        $regCenter.find("[name='code']").focus();
        return;
    }
    // var $captcha = $regCenter.find("[name='captcha']");
    // if ($captcha.length>0) {
    //     var cap=$captcha.val();
    //     if(!cap){
    //         warnMsg(Admin.verifyCodeStr);
    //         $captcha.focus();
    //         return;
    //     }
    // }
    $.ajax({
    url: baseUrl + "/emailRegister.do",
    type: "post",
    data: $regCenter.serialize(),
    success: function (result, textStatus, xhr) {
        if (result.success) {
            warnMsg(Admin.registerSucc);
            //layer.alert(Admin.registerSucc);
            location.href = "${base}/index.do";
        } else {
            warnMsg(result.msg);
            // layer.alert(result.msg);
            $butImg.click();
        }
    }
});
});
});