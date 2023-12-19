/*
//公告
window.noticeFn = {
    /// 本地存储兼容
    localStorage: window.sessionStorage ? sessionStorage : {
        getItem: function (name) {
            return utils.cookie.get(name);
        },
        setItem: function (key, value) {
            return utils.cookie.set(key, value, 0, "/");
        }
    },
    /// 过滤html标签
    filterHtml: function (content) {
        var div = document.createElement("div");
        div.innerHTML = content;
        return div.innerText;
    },
    ///重整公告内容
    filterAHref: function (content) {
        var div = $('<div></div>');
        content = content.replace(/\n/g, '<br>').replace('&lt;', '<').replace('&gt;', '>');
        div.html(content);
        div.find('a').each(function (idx, ele) {
            ele = $(ele);
            var href = ele.attr('href');
            if (href && href.indexOf('undefined') > -1) {
                var text = ele.text();
                if (!(/^http/i.test(text))) {
                    text = href.replace('undefined', ele.text());
                }
                ele.attr('href', text);
            }
        });
        return div.html();
    },

    /// 弹窗公告
    ///showDialog : 是否显示弹窗
    ///showNotice : 是否显示滚动
    ///position: 弹出位置 1：首页，2:注册页 3:投注页 4：充值页面
    message: function (showDialog, showNotice, position) {
        noticeFn.message.hasInit = true;
        var that = this;
        var dialogNotice = [];
        var newsNoticeData;
        if (showDialog) {
            if (!position) {
                position = 1;
            }
            if(!$.cookie('showMoreTime')){
            	getDialog(position);
            }
        }
        if (showNotice) {
            getNotice();
        }

        function getDialog(po) {
            $.ajax({
                url: base + "/newNotices.do",
                dataType: "json",
                cache: false,
                data: {'position': po, 'code': '9'},
                success: function (data) {
                    dialogNotice = data;
                    tip();
                }
            });
        }
        
        function setCookie(x){
        	var date = new Date();
        	if (x == 0) {
        		date = new Date( // 当天23:59
        		new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60
        				* 1000 - 1);
        	} else {
        		date.setTime(date.getTime() + (x * 60 * 1000));
        	}
        	$.cookie('showMoreTime', 'show1fenzhong', { expires: date });
        }

        function getNotice() {
            $.ajax({
                url: base + "/newNotices.do",
                dataType: "json",
                cache: false,
                data: {'code': '13'},
                success: function (data) {
                    newsNoticeData = data;
                    notice();
                }
            });
        }

        //滚动公告
        function notice() {
            if (newsNoticeData) {
                var _content = "";
                $.each(newsNoticeData, function (i, v) {
                    v.content = noticeFn.filterAHref(v.content);
                    _content += noticeFn.filterHtml(v.content) + "                ";
                });
                $("#notices marquee").html(_content);
            }
        }

        //显示弹窗公告
        function tip() {
            if (!dialogNotice || dialogNotice.length < 1) {
                return;
            }
            var vm = {
                dialogNotice: ko.observableArray(dialogNotice),
                activeMessage: ko.observable(dialogNotice[0]),
                activeIndex: ko.observable(0),
                active: function (index, data) {
                    vm.activeMessage(data);
                    vm.activeIndex(index);
                }
            };
            popShowTime = popShowTime || 1;
            var okVal="";
            if(popShowTime == 0){
            	okVal="当天不再提示";
            }else{
            	okVal=popShowTime + '分钟内不再提示';
            }
            utils.dialog({
                id: "tpl-message",
                title: "平台公告",
                skin: "dialog-notice",
                template: 'tpl-message',
                viewModel: vm,
                okValue: okVal,
                ok: function () {
                	setCookie(popShowTime);
                }
            }).showModal();
        }

        return this;
    }
};
$(function () {
    getUnreadMsg();
    var getMsgNumTimer;

    function getUnreadMsg() {
        $.ajax({
            url: base + "/getUnreadMsg.do",
            dataType: "json",
            cache: false,
            success: function (data) {
                if (data.success === 'false') {
                    clearTimeout(getMsgNumTimer);
                } else if (data > 0) {
                    tipUnreadMsg();
                }
            }
        });
    }

    getMsgNumTimer = setTimeout(getUnreadMsg, 30000);

    function tipUnreadMsg() {
        var html = '';
        html += '<div class="message-container">';
        html += '<p class="content">您有未读的<a href="' + base + '/userCenter/msgManage/message.do" target="_blank" style="color: red;font-weight: bold;">站内信</a></p>';
        html += '<p><a href="' + base + '/userCenter/msgManage/message.do" target="_blank" style="color: gray;">(点击查看详情)</a></p>';
        html += '</div>';
        utils.cornerDialog({
            id: "immediateMessageDialog",
            content: html,
            skin: "dialog-custom"
        })
    }
});

function refreshMemberBalance(){
	$('#AvailableBalance').html('刷新中..');
    $.ajax({
        url: base + "/accInfo/getAccount.do",
        type: "GET",
        success: function (j) {
            if (j) {
                $('#AvailableBalance').html(j.money);
            }
        }
    })
}*/
