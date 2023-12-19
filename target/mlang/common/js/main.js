define(['jquery','template','bootstrap','Fui'], function(template) {
	window.jQuery=$;
	// 备份jquery的ajax方法
	var _ajax = $.ajax;
	window.$ajax = _ajax;
	// 重写jquery的ajax方法
	$.ajax = function(opt) {
		if (!opt.dataType) {
			opt.dataType = "json";
		}
		if(opt.cache==undefined){//默认不缓存
			opt.cache=false;
		}
		if (!opt.type) {
			opt.type = "post";
		}
		// 备份opt中error和success方法
		var fn = {
			error : function(XMLHttpRequest, textStatus, errorThrown) {
			},
			success : function(data, textStatus, xhr) {
			}
		}
		if (opt.error) {
			fn.error = opt.error;
		}
		if (opt.success) {
			fn.success = opt.success;
		}

		// 扩展增强处理
		var _opt = $.extend(opt, {
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				var statusCode = XMLHttpRequest.status;
				// 错误方法增强处理
				if (statusCode == 404) {
					layer.msg("[" + opt.url + "] 404 not found");
				} 
				fn.error(XMLHttpRequest, textStatus, errorThrown);
				layer.closeAll('loading');
			},
			success : function(data, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate");
				layer.closeAll('loading');
				if(ceipstate==1){
					fn.success(data, textStatus, xhr);
				}else if (ceipstate ==2||ceipstate==3) {// 后台异常
					if($.type(data)=='string'){
						data=$.parseJSON(data);
					}
                    layer.msg(data.msg||"后台异常，请联系管理员!");
					if(opt.errorFn){
						opt.errorFn(data,ceipstate);
					}
				} else if (ceipstate == 4) {// 未登陆异常
					if($.type(data)=='string'){
						data=$.parseJSON(data);
					}
					layer.alert(data.msg||"登录状态错误，可能是ip变动",function(){
						if(Fui.config.loginDialogUrl){
							layer.closeAll('loading');
							Fui.openDialog({id:'login-dialog-url',url:Fui.config.loginDialogUrl});
							if(opt.errorFn){
								opt.errorFn(data,ceipstate);
							}
						}else{
							window.location.href = Fui.config.adminBaseUrl ? Fui.config.adminBaseUrl : "/";
						}
					});
				} else if (ceipstate == 5) {// 没有权限
					if($.type(data)=='string'){
						data=$.parseJSON(data);
					}
					layer.msg(data.msg||"没有权限");
					if(opt.errorFn){
						opt.errorFn(data,ceipstate);
					}
				} else {
					fn.success(data, textStatus, xhr);
				}
			}
		});
		_ajax(_opt);
	};
	/**
	 * 扩展String方法
	 */
	$.extend(String.prototype, {
		isPositiveInteger:function(){
			return (new RegExp(/^[1-9]\d*$/).test(this));
		},
		isInteger:function(){
			return (new RegExp(/^\d+$/).test(this));
		},
		isNumber: function(value, element) {
			return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
		},
		trim:function(){
			return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
		},
		startsWith:function (pattern){
			return this.indexOf(pattern) === 0;
		},
		endsWith:function(pattern) {
			var d = this.length - pattern.length;
			return d >= 0 && this.lastIndexOf(pattern) === d;
		},
		replaceAll:function(os, ns){
			return this.replace(new RegExp(os,"gm"),ns);
		},
		replaceTmBySelector:function(_box){
			var $parent = _box || $(document);
			return this.replace(RegExp("{([^:}]+)(?::([^}]*))?}","g"), //支持{#id:默认值}和{.className:默认值},（:默认值）这部分可以没有
			function($0,$1,$2){
				var $input = $parent.find($1);
				return $input.val() ? $input.val() : ((typeof($2)!="undefined")?$2:$0);
			});
		},
		isFinishedTm:function(){
			return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this)); 
		},
		isSpaces:function() {
			for(var i=0; i<this.length; i+=1) {
				var ch = this.charAt(i);
				if (ch!=' '&& ch!="\n" && ch!="\t" && ch!="\r") {return false;}
			}
			return true;
		},
		isUrl:function(){
			return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
		},
		isExternalUrl:function(){
			return this.isUrl() && this.indexOf("://"+document.domain) == -1;
		}
	});

	$.extend(Date.prototype, {
		// 对Date的扩展，将 Date 转化为指定格式的String
		// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
		// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
		// 例子：
		// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
		// (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
		format : function(fmt) {
			var o = {
				"M+" : this.getMonth() + 1, // 月份
				"d+" : this.getDate(), // 日
				"h+" : this.getHours(), // 小时
				"m+" : this.getMinutes(), // 分
				"s+" : this.getSeconds(), // 秒
				"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
				"S" : this.getMilliseconds()
			// 毫秒
			};
			if (/(y+)/.test(fmt))
				fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			for ( var k in o)
				if (new RegExp("(" + k + ")").test(fmt))
					fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
							: (("00" + o[k]).substr(("" + o[k]).length)));
			return fmt;
		}
	});
	
	Fui.init({
		baseUrl:baseInfo.baseUrl,
		adminBaseUrl:baseInfo.adminBaseUrl,
		loginDialogUrl:baseInfo.loginDialogUrl,
		navUrl:baseInfo.navUrl,
		homeUrl:baseInfo.homeUrl
	});
	
	Fui.navbar.set({elem:"#side-menu",url:Fui.config.navUrl}).render();
	Fui.initUI($("#page-wrapper"));
	Fui.tab.set({elem:"#page-wrapper"}).init();
	$(window).on('resize', function() {
		var h=$(this).height() - 61;
		h= h-$("#fui_tab_header").outerHeight();
		$('#fui_tab_content').height(h);
		if ($(this).width() < 769) {
            $('body').addClass('mini-navbar');
            $('.navbar-static-side').fadeIn();
        }
	}).resize();
	
	//判断浏览器是否支持html5本地存储
    var  localStorageSupport=(('localStorage' in window) && window['localStorage'] !== null);

	function SmoothlyMenu() {
	    if (!$('body').hasClass('mini-navbar')) {
	        $('#side-menu').hide();
	        setTimeout(
	            function () {
	                $('#side-menu').fadeIn(500);
	            }, 100);
	    } else if ($('body').hasClass('fixed-sidebar')) {
	        $('#side-menu').hide();
	        setTimeout(
	            function () {
	                $('#side-menu').fadeIn(500);
	            }, 300);
	    } else {
	        $('#side-menu').removeAttr('style');
	    }
	}
	
    $(".navbar-min").click(function(){
    		var $it=$(this);
    	 // 收起左侧菜单
        if ($it.data("hidn")!=1) {
            $("body").addClass('mini-navbar');
            SmoothlyMenu();

            if (localStorageSupport) {
                localStorage.setItem("collapse_menu", 'on');
            }
            $it.data("hidn",1).addClass("fa-angle-double-right").removeClass("fa-angle-double-left");
        } else {
            $("body").removeClass('mini-navbar');
            SmoothlyMenu();

            if (localStorageSupport) {
                localStorage.setItem("collapse_menu", 'off');
            }
            $it.data("hidn",0).addClass("fa-angle-double-left").removeClass("fa-angle-double-right");
        }
    });
    if (localStorageSupport) {
        var collapse = localStorage.getItem("collapse_menu");
        if (collapse == 'on') {
        	 	$('body').addClass('mini-navbar');
            $(".navbar-min").data("hidn",1).addClass("fa-angle-double-right").removeClass("fa-angle-double-left");
        }
    }
    
    function directOpenUrl(){
		//直接使用浏览器打开模版的
		var $direct=$(".direct_open_url");
		if($direct.length){
			var url=$direct.attr("url")
				,con = $direct.html()
				,item=Fui.navbar.getMenuItem(url);
			if(con){
				con=con.substring(4,con.length-5);
			}
			if(!item && window.location.pathname==Fui.config.homeUrl){
				item={id:"home",title:Admin.HomePage,closed:false};
			}
			$direct.remove();
			if(item){
				Fui.tab.tabAdd({id:item.id,title:item.title,closed:item.closed,url:url,content:con});
				$("#side-menu").find("a[href='"+url+"']").parents(".nav-second-level").prev().click();
			}
		}
	}
	if(Fui.config.homeUrl && window.location.pathname!=Fui.config.homeUrl){
		Fui.tab.tabAdd({id:"home",title:Admin.HomePage,closed:false,url:Fui.config.homeUrl,callback:directOpenUrl});
	}else{
		directOpenUrl();
	}
});
