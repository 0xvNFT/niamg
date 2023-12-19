$(function() {

    //缩放页面
    /*var sca = document.body.offsetWidth / 640;
    var bh = document.body.offsetHeight * sca;
    var bw = document.body.offsetWidth * sca;
    $('.Iphone').css({
        'transform': 'scale(' + sca + ')',
        'transformOrigin': '0 0 0'
    });
    $('body').css('height', bh + 'px')
    $('body').css('width', bw + 'px')*/

    var signFun = function() {
        var $dateBox = $("#js-qiandao-list"),
            $currentDate = $(".current-date"),
            _html = '',
            _handle = true,
            myDate = new Date();
        $currentDate.text(myDate.getFullYear() + '-' + parseInt(myDate.getMonth() + 1) + '-' + myDate.getDate());

        var monthFirst = new Date(myDate.getFullYear(), parseInt(myDate.getMonth()), 1).getDay();

        var d = new Date(myDate.getFullYear(), parseInt(myDate.getMonth() + 1), 0);
        var totalDay = d.getDate(); //获取当前月的天数

        for (var i = 0; i < 42; i++) {
            _html += ' <li><div class="qiandao-icon"></div></li>'
        }
        $dateBox.html(_html) //生成日历网格

        var $dateLi = $dateBox.find("li");
        for (var i = 0; i < totalDay; i++) {
            $dateLi.eq(i + monthFirst).addClass("date" + parseInt(i + 1));
            for (var j = 0; j < dateArray.length; j++) {
                if (i == dateArray[j]) {
                    $dateLi.eq(i + monthFirst).addClass("qiandao");
                }
            }
        } //生成当月的日历且含已签到

        $(".date" + myDate.getDate()).addClass('able-qiandao');

        $dateBox.on("click", "li", function() {
                if ($(this).hasClass('able-qiandao') && _handle) {
                    $(this).addClass('qiandao');
                    qiandaoFun();
                }
            }) //签到

        function qianDao() {
            $(".date" + myDate.getDate()).addClass('qiandao');
        }
    }();

    function openLayer(a, Fun) {
        $('.' + a).fadeIn(Fun)
    } //打开弹窗

    var closeLayer = function() {
            $("body").on("click", ".close-qiandao-layer", function() {
                $(this).parents(".qiandao-layer").fadeOut()
            })
        }() //关闭弹窗

    $("#js-qiandao-history").on("click", function() {
        openLayer("qiandao-history-layer", myFun);

        function myFun() {
            console.log(1)
        } //打开弹窗返回函数
    })
    refreshMoney();
})
function refreshMoney(){
   $.ajax({
       url: base + "/userCenter/accInfo.do",
       type: "get",
       dataType : 'json',
	   success : function(j) {
		   if(j){
			   if(j.login){
				   $('.user_money').html(j.money);
				   $('.user_name').html(j.userName);
			   }
		   }
	   }
   });
}
function signToday(){
	$.ajax({
		"url" : base + "/userCenter/sign.do",
		"type" : "POST",
	    "dataType" : "json",
	    "contentType" : "application/x-www-form-urlencoded",
		success : function(result, textStatus, xhr) {
			var ceipstate = xhr.getResponseHeader("ceipstate")
			if (!ceipstate || ceipstate == 1) {// 正常响应
				location.href = location.href;
			} else if (ceipstate == 2) {// 后台异常
				alert(result.msg);
			} else if (ceipstate == 3) { // 业务异常
				alert(result.msg);
			}
		}
	});
}

function showSignInfo(){
	$('#showSignInfo').show();
}
