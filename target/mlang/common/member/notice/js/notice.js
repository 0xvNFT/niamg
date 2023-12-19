!
function(e) {
	function t(o) {
		if (n[o]) return n[o].exports;
		var i = n[o] = {
			exports: {},
			id: o,
			loaded: !1
		};
		return e[o].call(i.exports, i, i.exports, t), i.loaded = !0, i.exports
	}
	var n = {};
	return t.m = e, t.c = n, t.p = "", t(0)
}({
	0: function(e, t, n) {
		e.exports = n(22)
	},
	14: function(e, t) {
		e.exports = function(e) {
			function t(e, t) {
				var n = {},
					o = [];
				return ko.utils.arrayForEach(e.split(""), function(e) {
					var i = {
						l: "left",
						b: "bottom",
						r: "right",
						t: "top"
					}[e];
					0 === t ? n[i] = 0 : 1 === t && o.push(i)
				}), [n, o.join(" ")][t]
			}
			if (e.title = e.title || "提示", e.offset = e.offset || "rb", e.onshow = e.onshow || $.noop, e.close = e.close || $.noop, window.dialog || window.artDialog) {
				var n = document.getElementById("__CornerDialog");
				n && n.parentElement.removeChild(n), n = document.createElement("div"), n.setAttribute("id", "__CornerDialog"), n.style.position = "fixed", $.extend(n.style, t(e.offset, 0)), document.body.appendChild(n), window.artDialog ? artDialog($.extend(e, {
					follow: n,
					initialize: e.onshow,
					fixed: !0
				})) : dialog($.extend(e, {
					align: t(e.offset),
					fixed: !0
				})).show(n)
			} else window.layer && layer.open({
				content: e.content,
				success: e.onshow,
				yes: e.ok,
				offset: e.offset,
				title: e.title,
				fixed: !0,
				id: e.id,
				type: 1,
				shade: 0
			})
		}
	},
	22: function(e, t, n) {
		window.utils = {}, window.dialog && $.extend(dialog.defaults, {
			skin: "dialog-custom",
			okValue: "确定",
			cancelValue: "取消"
		}), window.console && console.log || (window.console = {
			log: function() {
				throw Array.prototype.join(arguments, " ")
			},
			error: this.log
		}), utils.classof = function(e) {
			return void 0 === e ? "undefined" : null === e ? "null" : Object.prototype.toString.call(e).slice(8, -1).toLowerCase()
		}, utils.isNaN = function(e) {
			return "number" == typeof e && isNaN(e)
		}, String.prototype.padLeft || (String.prototype.padLeft = function(e, t) {
			var n = this;
			return new Array(Math.max(0, e - n.length + 1)).join(t || " ") + n
		}), Number.prototype.padLeft = function(e, t) {
			return this.toString().padLeft(e, t || "0")
		}, utils.dialog = function(e) {
			if (e && "object" !== utils.classof(e)) throw TypeError("传入的参数必须为对象");
			if (e && e.template && e.viewModel) {
				var t = document.createElement("div");
				t.setAttribute("id", "DIALOG-VM"), t.setAttribute("data-bind", "template:__DIALOG_TPL");
				var n = "function" === utils.classof(e.viewModel) ? new e.viewModel : e.viewModel;
				n.__DIALOG_TPL = e.template, n.onclose && (e.onclose = function() {
					n.onclose()
				}), ko.applyBindings(n, t)
			}
			var o = ko.utils.extend({}, e);
			return o.id = o.id || "DIALOG-VM", o.skin = o.skin || "dialog-custom", o.content = t, o.onremove = function() {
				t && document.body.removeChild(t), e.onremove && e.onremove.call(this)
			}, dialog(o)
		}, utils.confirm = function(e, t, n, o) {
			var i = dialog.get("DIALOG-CONFIRM");
			i && i.remove(), dialog($.extend(o || {}, {
				id: "DIALOG-CONFIRM",
				title: "操作提示",
				skin: "dialog-custom",
				content: e,
				width: 240,
				ok: void 0 === t ? $.noop : t,
				cancel: void 0 === n ? $.noop : n
			})).showModal()
		}, utils.tip = function(e, t, n, o) {
			3 === arguments.length && "function" == typeof n && (o = n, n = 0), clearTimeout(dialog.timeout), "boolean" == typeof t && (t = t ? "succeed" : "defalut");
			var i = dialog.get("DIALOG-TIP");
			i && i.remove();
			var r = dialog({
				id: "DIALOG-TIP",
				title: "操作提示",
				padding: "30px",
				content: '<span class="' + t + '">' + e + "</span>",
				ok: o || $.noop
			}).showModal();
			return n && (dialog.timeout = setTimeout(function() {
				r.close().remove()
			}, n)), r
		}
	}
});


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
            if(position == 1 && !$.cookie('indexShowMoreTime')) {
                getDialog(position);
            } else if (position == 2 && !$.cookie('regShowMoreTime')) {
                getDialog(position);
            } else if (position == 3 && !$.cookie('betShowMoreTime'))  {
                getDialog(position);
            } else if (position == 4 && !$.cookie('depositShowMoreTime')) {
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
                data: {'position': po, 'code': '9','language': languagePop},
                success: function (data) {

                    $.each(data,function(n,value){
                        if(languagePop == data[n].language){
                            dialogNotice = data;
                            tip(po);
                        }
                    })

                }
            });
        }
        
        function setCookie(x,position){
        	var date = new Date();
        	if (x == 0) {
        		date = new Date( // 当天23:59
        		new Date(new Date().toLocaleDateString()).getTime() + 24 * 60 * 60
        				* 1000 - 1);
        	} else {
        		date.setTime(date.getTime() + (x * 60 * 1000));
        	}
            if(position == 1) {
                $.cookie('indexShowMoreTime', 'show1fenzhong', { expires: date });
            } else if (position == 2) {
                $.cookie('regShowMoreTime', 'show1fenzhong', { expires: date });
            } else if (position == 3)  {
                $.cookie('betShowMoreTime', 'show1fenzhong', { expires: date });
            } else if (position == 4) {
                $.cookie('depositShowMoreTime', 'show1fenzhong', { expires: date });
            }
        }

        function getNotice() {
            $.ajax({
                url: base + "/newNotices.do",
                dataType: "json",
                cache: false,
                data: {'code': '13'},
                success: function (data) {
                    $.each(data,function(n,value){
                        if(languagePop == data[n].language){
                        newsNoticeData = data;
                        notice();
                        }
                    })
                }
            });
        }

        //滚动公告
        function notice() {
            if (newsNoticeData) {
                var _content = "";
                $.each(newsNoticeData, function (i, v) {
                    v.content = v.content;
                    _content += v.content + "                ";
                });
                $(".station_news").html('<marquee onmouseout="this.start();" onmouseover="this.stop();" style="cursor: pointer;">'+_content+'</marquee>');
                //$("#notice").html('<marquee onmouseout="this.start();" onmouseover="this.stop();" style="cursor: pointer;">'+_content+'</marquee>');
            }
        }

        //显示弹窗公告
        function tip(position) {
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

            var okVal=Admin.close;

            /*if(popShowTime == 0){
            	// okVal="当天不再提示";
            	okVal=Admin.notPromptDay;

            }else{
            	okVal= Admin.notPromptMinutes.replace('{0}',popShowTime);
            }*/
            utils.dialog({
                id: "tpl-message",
                title:  Admin.platformNotice,
                skin: "dialog-notice",
                template: 'tpl-message',
                viewModel: vm,
                okValue: okVal,
                ok: function () {
                	setCookie(popShowTime,position);
                }
            }).showModal();
            $('.ui-popup.ui-popup-modal.ui-popup-show.ui-popup-focus').css('top','100px');
            $('.ui-popup .ui-dialog-close').click(function(){
            	setCookie(popShowTime,position);
            })
        }

        return this;
    },

    /// 弹窗站内信
    ///showDialog : 是否显示弹窗
    ///showNotice : 是否显示滚动
    ///position: 弹出位置 1：首页，2:会员中心
    messageReceive: function (showDialog, showNotice, position) {
        noticeFn.message.hasInit = true;
        var that = this;
        var dialogNotice = [];
        var newsNoticeData;
        if (showDialog) {
            getDialog(position);
        }

        function getDialog(po) {
            $.ajax({
                url: base + "/stationReceiveMessageNews.do",
                dataType: "json",
                cache: false,
                data: {},
                success: function (data) {
                    dialogNotice = data;
                    tip();
                }
            });
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

            var okVal=Admin.close;

            utils.dialog({
                id: "tpl-message",
                title: Admin.stationMes,
                skin: "dialog-notice",
                template: 'tpl-message',
                viewModel: vm,
                okValue: okVal,
                ok: function () {
                    $('.ui-popup .ui-dialog-close').click();
                }
            }).showModal();
            $('.ui-popup.ui-popup-modal.ui-popup-show.ui-popup-focus').css('top','100px');
        }

        return this;
    }
};