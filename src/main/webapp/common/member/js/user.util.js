var siteKey = $("#verifySiteKey").val();
var hideVerifyCode = $("#hideVerifyCode").val();
var verifyDomain = $("#verifyDomain").val();
var hasRegisterVerify = $("#hasRegisterVerify").val();
var hasLoginVerify = $("#hasLoginVerify").val();
//重新加载验证码
function reloadImg() {
    if (siteKey) {
        getToken("login");
    }
    if (!hideVerifyCode) {
        var url = base + "/loginVerifycode.do?timestamp=" + (new Date().getTime());
        $(".validcode").attr("src", url);
    }
}

//重新加载验证码
function reloadRegImg() {
    if (siteKey) {
        getToken("register");
    }
    if (!hideVerifyCode) {
        var url = base + "/registerVerifycode.do?timestamp=" + (new Date().getTime());
        $(".registerValidcode").attr("src", url);
    }

}

$(function () {
    $('.validcode,input#verifyCode').click(function () {
        reloadImg();
    });
    $('.registerValidcode').click(function () {
        reloadRegImg();
    });
    reloadImg();
    reloadRegImg();
});

//跳转投注页
function loginA() {
    toLoginMember('A');
}

//停留首页
function loginB() {
    toLoginMember();
}

//跳转购彩大厅
function loginC() {
    toLoginMember('C');
}

//跳转购彩大厅(真人电子)
function loginD() {
    toLoginMember('D');
}

//跳转手机
function loginM() {
    toLoginMember('M');
}

//备用登录
function loginBY() {
    toLoginMember('BY', 'EXTRA');
}

var verifySeuccessParams,verifySeuccessUrl,verifySite;
function toLoginMember(p, e) {
    var username = $('input#username').val();
    var password = $('input#password').val();
    var verifyCode = $('input#verifyCode').val();
    if (e) {
        username = $('input#uName').val();
        password = $('input#pWord').val();
        verifyCode = $('input#vCode').val();
    }
    if (!username) {
        alert("用户名不能为空");
        $('input#username').focus();
        return;
    } else if (!password) {
        alert("密码不能为空");
        $('input#password').focus();
        return;
    } else if (password.length < 6) {
        alert("密码不能小于6位!");
        $('input#password').focus();
        return;
    } else if (!hideVerifyCode && !verifyCode) {
        alert("验证码不能为空");
        $('input#verifyCode').focus();
        return;
    }
    var param = {
        "username": username,
        "password": password,
        "verifyCode": verifyCode,
        "gToken": $("input#gToken").val()
    };
    var url = base + '/index.do';
    if (p) {
        if (p == 'A') {
            url = base + '/offcial/index.do';
        } else if (p == 'M') {
            url = base + '/m';
        } else if (p == 'C') {
            url = base + '/index/lotteryHall.do?code=1&top=1';
        } else if (p == 'D') {
            url = base + '/index/lotteryHall.do?code=1&top=1&showMore=1';
        }
    }
    if (hasLoginVerify) {
        verifySeuccessParams = param;
        verifySeuccessUrl=url;
        verifySite = 'login';
        showVerify("login");
    } else {
        todoLoginAjax(param,url);
    }
}

function todoLoginAjax(param,url) {
    $.ajax({
        url: base + "/login.do",
        type: "post",
        data: param,
        success: function (result, textStatus, xhr) {
            var ceipstate = xhr.getResponseHeader("ceipstate")
            if (!ceipstate || ceipstate == 1) {// 正常响应
                location.href = url;
            } else if (ceipstate == 2) {// 后台异常
                alert(result.msg);
                reloadImg();
                refreshVerify('login','refresh');
                $("input#verifyCode").val('');
            } else if (ceipstate == 3) { // 业务异常
                alert(result.msg);
                reloadImg();
                refreshVerify('login','refresh');
                $("input#verifyCode").val('');
            }
        }
    });
}

//-------注册-------start
//注册配置项集合
var data = {};

//初始化注册项
function initRegConfig() {
    for (var i = 0; i < rc.length; i++) {
        var filed = rc[i];
        if (rc[i].show === 2) {
            $('#' + rc[i].eleName).show();
        }
        var val = getVal(filed);
        data[filed.eleName] = val;
    }
}

//获取配置项值
function getVal(filed) {
    var val = "";
    if (filed.eleType == 1 || filed.eleType == 2) {
        val = $("#" + filed.eleName).val();
    } else if (filed.eleType == 3) {
        val = $("input[name='" + filed.eleName + "']:checked").val();
    } else if (filed.eleType == 4) {
        var vals = [];
        $("input[name='" + filed.eleName + "']:checked").each(function () {
            vals.push(this.value);
        })
        val = vals.join(",");
    }
    return val;
}

//A站注册
function regA() {
    toRegMember('A');
}

//B站注册
function regB() {
    toRegMember();
}

//手机注册
function regM() {
    toRegMember('M');
}

function toRegMember(p) {
    var username = $("#reg_form [name='username']").val();
    var password = $("#reg_form [name='password']").val();
    var repassword = $("#reg_form [name='repassword']").val();
    var verifyCode = $("#reg_form [name='verifyCode']").val();
    if (!username) {
        alert("用户名不能为空");
        $('input#username').focus();
        return;
    } else if (!password) {
        alert("密码不能为空");
        $('input#password').focus();
        return;
    } else if (password.length < 6) {
        alert("密码不能小于6位!");
        $('input#password').focus();
        return;
    } else if (repassword.length < 6) {
        alert("确认密码不能小于6位!");
        $('input#repassword').focus();
        return;
    } else if (password != repassword) {
        alert("两次密码不一致!");
        $('input#repassword').focus();
        return;
    } else if (!hideVerifyCode && !verifyCode) {
        alert("验证码不能为空");
        $('input#verifyCode').focus();
        return;
    }

    var url = base + '/index.do';
    if (p && p == 'A') {
        url = base + '/offcial/index.do';
    } else if (p && p == 'M') {
        url = base + '/m';
    }
    if (hasRegisterVerify) {
        verifySeuccessUrl=url;
        verifySite = 'register';
        showVerify("register");
    } else {
        todoRegisterAjax(url);
    }
}

function todoRegisterAjax(url) {
    $.ajax({
        url: base + "/register.do",
        type: "post",
        data: $('#reg_form').serialize(),
        success: function (result, textStatus, xhr) {
            if (result.success) {
                alert('注册成功!');
                location.href = url;
            } else {
            	$('.verify-overlay').hide();
                alert(result.msg);
                reloadRegImg();
                refreshVerify("register","refresh");
            }

        }
    });
}

function checkUsername(username) {
    var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
    if (!reg1.test(username)) {
        return false;
    }
    var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
    if (reg.test(username)) {
        return true;
    } else {
        return false;
    }
}

// -------注册-------end


//文字闪烁
//new toggleColor('.mfsw', [ 'yellow', '#FF0000' ], 1000);
function toggleColor(id, arr, s) {
    var self = this;
    self._i = 0;
    self._timer = null;

    self.run = function () {
        if (arr[self._i]) {
            $(id).css('color', arr[self._i]);
        }
        self._i == 0 ? self._i++ : self._i = 0;
        self._timer = setTimeout(function () {
            self.run(id, arr, s);
        }, s);
    }
    self.run();
}

//帮助中心
function userHelp(code) {
    location.href = base + '/userCenter/help/' + code + '.do';
}

//type 1 官方 ， 2 信用
function toCp(code, type) {
    if (!isLogin) {
        alert('请先登录账号!');
    } else {
        if (type == 1) {
            location.href = base + '/offcial/index.do?code=' + code;
        } else {
            location.href = base + '/credit/index.do?code=' + code;
        }
    }
}

//刷新余额
function refreshBalance() {
    $.ajax({
        url: base + "/accInfo/getAccount.do",
        type: "GET",
        success: function (j) {
            if (j) {
                $('.station_balance').html(j.money);
            }
        }
    })
}

//新窗打开
function toWin(url, name, width, height) {
    if (!width) {
        width = 1200;
    }
    if (!height) {
        height = 800;
    }
    window.open(url, name, "width=" + width + ", height=" + height + ",toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes")
}

//在线QQ
function toQq(p) {
    window.open("tencent://message/?uin=" + p);
}

//在线客服
function toKf(p) {
    //获得窗口的垂直位置 
    var iTop = (window.screen.availHeight - 30 - iHeight) / 2;
    //获得窗口的水平位置 
    var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
    var iWidth = 720;//弹出窗口的宽度;
    var iHeight = 600;//弹出窗口的高度;
    window.open(p, "Service", "width=1039,height=728,status=no,scrollbars=no");
}

//AG游戏
function toAGGame(gameid) {
    if (!isLogin) {
        alert('请先登录账号!');
        return;
    }
    var sw = '';
    csw = '';
    sh = '';
    csh = '';
    ctop = '';
    cleft = '';

    sw = $(window.parent).width();
    sh = $(window.parent).height();
    csw = $(window.parent).width();
    csh = $(window.parent).height();
    ctop = 0;
    cleft = 0;
    var windowOpen = window.open("", '_blank', 'width=' + csw + ',height=' + csh + ',left=' + cleft + ',top=' + ctop + ',scrollbars=no,location=1,resizable=yes');
    $.ajax({
        url: base + "/third/forwardAg.do",
        sync: false,
        data: {
            h5: 0,
            gameType: gameid
        },
        success: function (json) {
            if (json.success) {
                windowOpen.location.href = json.url;
            } else {
                alert(json.msg);
                return;
            }
        }
    });
}

//收藏本站
function AddFavorite(title) {
    var url = "http://" + location.hostname + "/";
    try {
        window.external.addFavorite(url, title);
    } catch (e) {
        try {
            window.sidebar.addPanel(title, url, "");
        } catch (e) {
            alert('抱歉，您所使用的浏览器无法完成此操作。<br />加入收藏失败，电脑请使用Ctrl+D进行添加');
        }
    }
}

//设为首页
function SetHome() {
    var url = location.href;
    if (document.all) {
        document.body.style.behavior = 'url(#default#homepage)';
        document.body.setHomePage(url);
    } else {
        alert("您好,您的浏览器不支持自动设置页面为首页功能,请您手动在浏览器里设置该页面为首页!");
    }
}
function getToken(action) {
    if (siteKey) {
        grecaptcha.ready(function () {
            grecaptcha.execute(siteKey, {action: action}).then(function(token) {
                if (action == 'login') {
                    $("input#gToken").val(token);
                } else if (action == 'register') {
                    $("#reg_form [name='gToken']").val(token);
                }
            });
        });
    }
}

function showVerify(code) {
    (hasLoginVerify && code ==='login' && $("#verify-frame-login-id").show()&&$(".verify-overlay").show()) || (hasRegisterVerify && code ==='register' && $("#verify-frame-register-id").show() && $(".verify-overlay").show())
}

function hideVerify(code){
    (hasLoginVerify && code ==='login' && $("#verify-frame-login-id").hide()&&$(".verify-overlay").hide()) || (hasRegisterVerify && code ==='register' && $("#verify-frame-register-id").hide()&&$(".verify-overlay").hide())
}

function refreshVerify(code,msg) {
    if ( code=== 'login' && hasLoginVerify) {
        let $frame = $("#verify-frame-login-id");
        let url = $frame.attr("src");
        url=url+"&timestamp="+new Date().getTime();
        $frame.attr("src",url);
    }else if (code === 'register' && hasRegisterVerify) {
        let $frame = $("#verify-frame-register-id");
        let url = $frame.attr("src");
        url=url+"&timestamp="+new Date().getTime();
        $frame.attr("src",url);
    }
}
// a页面
window.addEventListener("message", function( event ) {
    if (event.origin !== verifyDomain) return;
    if (event.data.status) {
        if (verifySite ==='login') {
            hideVerify("login");
            todoLoginAjax(verifySeuccessParams,verifySeuccessUrl);
        } else {
            hideVerify("register");
            todoRegisterAjax(verifySeuccessUrl);
        }
    }else {
        alert(event.data.msg);
    }
});

function openChatWindow(url,name,iWidth,iHeight){
    var url,name,iWidth,iHeight;
    var iTop = (window.screen.availHeight-30-iHeight)/2;
    var iLeft = (window.screen.availWidth-10-iWidth)/2;
    window.open(url,name,'height='+iHeight+',,innerHeight='+iHeight+',width='+iWidth+',innerWidth='+iWidth+',top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
}

