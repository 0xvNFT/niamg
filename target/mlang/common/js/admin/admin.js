define(['jquery', 'Fui'], function () {
    // 开奖提示语音
    var cacheName = 'SoundTable'
		, waringCycle = 10
        , sourceFlag = 'on'
        , sourceType = '1'
		, waringCycleKey = 'waringCycle_' + baseInfo.username
        , sourceFlagKey = 'sourceFlag_' + baseInfo.username
        , getBaseInfoTimer
        , getAnnouncementTimer;
    $("#home_soundicon").click(function () {
        var it = $(this);
        if (it.hasClass("glyphicon-volume-up")) {
            Fui.setCache(cacheName, {key: sourceFlagKey, value: 'off'});
            it.removeClass("glyphicon-volume-up").addClass("glyphicon-volume-off").attr("value", "off").attr("title", Admin.turnOnSound);
            sourceFlag = 'off';
        } else {
            Fui.setCache(cacheName, {key: sourceFlagKey, remove: true});
            it.removeClass("glyphicon-volume-off").addClass("glyphicon-volume-up").attr("value", "on").attr("title",  Admin.turnOffSound);
            sourceFlag = 'on';
        }
    });
    var cacheSound = Fui.setCache(cacheName);
    if (cacheSound) {
        if (cacheSound[sourceFlagKey]) {
            sourceFlag = cacheSound[sourceFlagKey];
            if (sourceFlag == 'off') {
                $("#home_soundicon").removeClass("glyphicon-volume-up").addClass("glyphicon-volume-off").attr("value", "off").attr("title",  Admin.turnOnSound);
            }
        }
    }
    $("#home_waringCycle").change(function () {
        waringCycle = $(this).val();
        Fui.setCache(cacheName, {key: waringCycleKey, value: waringCycle});
        clearTimeout(getBaseInfoTimer);
        getBaseInfoTimer = setTimeout(getBaseInfo, 1000 * waringCycle);
    });
    function playDepositSound() {
        if (sourceFlag === 'on') {
            var borswer = window.navigator.userAgent.toLowerCase();
            var voice = baseInfo.depositVoice;
            if(!voice || voice === '') {
                voice = baseInfo.baseUrl+"/common/audio/1.mp3";
            }
            if ( borswer.indexOf( "ie" ) >= 0 ){
            	var $body = $("body");
                //IE内核浏览器
                if ($body.find("#embedDepositPlay").length <= 0){
                    $body.append('<embed id="embedDepositPlay" name="embedDepositPlay" src="'+voice+'" autostart="true" hidden="true" loop="false"></embed>');
                }
                document.embedDepositPlay.volume = 100;
            } else{
            	if(!baseInfo.depositAudio){
        			var audio = new Audio();
        			audio.src=voice;
        			audio.autoplay =false;
        			baseInfo.depositAudio=audio;
        		}
    			baseInfo.depositAudio.play();
            }
        }
    }
    function playWithdrawSound() {
        if (sourceFlag === 'on') {
            var borswer = window.navigator.userAgent.toLowerCase();
            var voice = baseInfo.withdrawVoice;
            if(!voice || voice === '') {
                voice = baseInfo.baseUrl+"/common/audio/2.mp3";
            }
            if ( borswer.indexOf( "ie" ) >= 0 ){
            	 	var $body = $("body");
                //IE内核浏览器
                if ($body.find("#embedWithdrawPlay").length <= 0){
                    $body.append('<embed id="embedWithdrawPlay" name="embedWithdrawPlay" src="'+voice+'" autostart="true" hidden="true" loop="false"></embed>');
                }
                document.embedWithdrawPlay.volume = 100;
            }else{
            	if(!baseInfo.withdrawAudio){
        			var audio = new Audio();
        			audio.src=voice;
        			audio.autoplay =false;
        			baseInfo.withdrawAudio=audio;
        		}
    			baseInfo.withdrawAudio.play();
            }
        }
    }
    var lastSysLotTips=0;
    function getBaseInfo() {
        $.ajax({
            url: baseInfo.adminBaseUrl + '/getBaseInfo.do',
            success: function (data) {
                var depositCount = data.depositCount;
                var withdrawCount = data.withdrawCount;
                $("#online_span").html(data.onlineUser);
                $("#deposit_span").html(depositCount);
                $("#withdraw_span").html(withdrawCount);
                if (depositCount > 0) {
                    setTimeout(playDepositSound, 1000);
                }
                if (withdrawCount > 0) {
                    setTimeout(playWithdrawSound, 3000);
                }
            },
            errorFn: function (data, ceipstate) {
                if (ceipstate == 4) {
                    clearTimeout(getBaseInfoTimer);
                }
            },
            error: function () {
                clearTimeout(getBaseInfoTimer);
            }
        });
        getBaseInfoTimer = setTimeout(getBaseInfo, 1000 * waringCycle);
    }
    getBaseInfo();
    getAnnouncement();

    Fui.config.loginSuccessFn = function () {
        getBaseInfo();
    };
    //后台公告
    function getAnnouncement() {
        $.ajax({
            url: baseInfo.adminBaseUrl + '/getPopAnnouncementList.do',
            success: function (data) {
                if (data && data.length >0) {
                    handelAnnouncement(data);
                }
            },
            errorFn: function (data, ceipstate) {
                if (ceipstate == 4) {
                    clearTimeout(getAnnouncementTimer);
                }
            },
            error: function () {
                clearTimeout(getAnnouncementTimer);
            }
        });
        //五分钟
        getAnnouncementTimer = setTimeout(getAnnouncement, 300000);
    }

    function handelAnnouncement(noticeList) {
        if ($("body").find('#admin_notice_dialog_pop_id').length >0) {
            return;
        }
        //获取租户公告
        var notice_html = '<div class="noticeDialogTitle"><span>'+Admin.announcementTitle+'</span></div>';
        notice_html += '<div class="noticeDialog"><div class="left-nav"><ul>';
        $.each(noticeList,function (index,notice) {
            if (notice.dialogFlag == 1) {
                notice_html += '<li onclick="announcementClick(this)" data-index="'+index+'" data-id="'+notice.id+'" class="choose ';
                if (index ===0) {
                    notice_html +='active';
                }
                notice_html += '"><div class="icon"><i class="fa fa-clone"></i></div><span>'+notice.content+'</span></li>';
            }
        });
        notice_html += '</ul><div class="content-border"><div id="line" class="line"></div></div></div><div class="right-content">';
        $.each(noticeList,function (index,notice) {
            if (notice.dialogFlag == 1) {
                notice_html += '<div class="notice_index_'+index+' notice_content ';
                if (index === 0) {
                    notice_html +='active';
                }
                notice_html +='"><p>'+notice.content+'</p></div>';
            }
        });
        notice_html +='<div class="read_button"><button class="btn btn-success btn-primary" onclick="announcementRead()">'+Admin.announcementReaded+'</button></div>'
        notice_html += '</div></div>';
        layer.open({
            id:'admin_notice_dialog_pop_id',
            type: 1,
            shade: .5,
            title: false,
            area: ['900px', '580px'],
            content: notice_html
        });
    }
});
function announcementClick($this) {
    var _this = $($this), index = _this.data("index"),id = _this.data("id");
    _this.addClass("active").siblings("li").removeClass("active");
    _this.parent().siblings(".content-border").find("#line").attr("class","line"+index);
    $("#admin_notice_dialog_pop_id").find(".right-content .notice_index_" + index).addClass("active").siblings().removeClass("active");
    if(id){
        $.ajax({
            url:baseInfo.adminBaseUrl+"/readAnnouncementMsg.do",
            data:{mid:id},
            success:function(res){}
        })
    }
}

function announcementRead() {
    $("#admin_notice_dialog_pop_id ul li").each(function () {
        var _this = $(this),id = _this.data("id");
        if(id){
            $.ajax({
                url:baseInfo.adminBaseUrl+"/readAnnouncementMsg.do",
                data:{mid:id},
                success:function(res){}
            })
        }
    });
   $("#admin_notice_dialog_pop_id").next().find(".layui-layer-close").click();
}
