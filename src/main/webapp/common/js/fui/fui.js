define(['bootstrap','layer','metisMenu'], function(){
	try {
        localStorage.setItem('isPrivateMode', '1');
        localStorage.removeItem('isPrivateMode');
        window.isPrivateMode = false;// 不是 Safari 无痕模式
    } catch(e) {
        window.isPrivateMode = true;// 是 Safari 无痕模式
    }
	function pageAjaxDone(json){
		if(json.msg) layer.msg(json.msg);
		if (!json.unReloadTable){
			var $table=$(".fui-box.active").data("bootstrapTable");
			if($table && $table.length){
				$table.bootstrapTable('refresh');
			}
		}
	}
	function ajaxTodo(url, callback,options){
		callback=callback||pageAjaxDone;
		if (! $.isFunction(callback)) callback = eval('(' + callback + ')');
		$.ajax({
			type:'POST',
			url:url,
			dataType:"json",
			cache: false,
			success: function(json){
				json=$.extend(json, options);
				callback(json);
			},
			error:function(){
				layer.msg((typeof Admin=='undefined')?'网络异常':Admin.networkError);
			},
			complete:function(){
				layer.closeAll('loading');
			}
		});
	}
	var Fui={
        getFuiTableHeight:function(event) {
            return $(window).height() - 150
        },
		config:{
			baseUrl:"",
			adminBaseUrl:"",
			indexUrl:"",
			loginDialogUrl:"" //session timeout
		},
		cacheMap:{},
		init:function(options){
			this.config = $.extend(this.config, options);
		},
		formatDatetime:function(time){
			if (!time) {
				return "";
			}
			if(/^[\d]{4}-[\d]{2}-[\d]{2} [\d]{2}:[\d]{2}:[\d]{2}/.test(time)){
				return time;
			}
			time = new Date(time);
			return time.format("yyyy-MM-dd hh:mm:ss");
		},
		formatDate:function(date){
			if (!date) {
				return "";
			}
			if(/^[\d]{4}-[\d]{2}-[\d]{2}/.test(date)){
				return date;
			}
			date = new Date(date);
			return date.format("yyyy-MM-dd");
		},
		formatZhDatetime:function(date){
			if (!date) {
				return "";
			}
			date = new Date(date);
			return date.format("M月d日,hh:mm:ss");
		},
		statusFormatter:function(option) {
			var onVal=option.onVal||2
				,offVal=option.offVal||1
				,onText=option.onText||((typeof Admin=='undefined')?'启用':Admin.enable)
				,offText=option.offText||((typeof Admin=='undefined')?'禁用':Admin.disable)
				,val=option.val
				,url=option.url
				,callback=option.callback;
			var col='<input class="fui-switch" type="checkbox" data-on-text="'+onText+'" data-off-text="'+offText+'"';
			if(val==onVal){
				col+=' checked';
			}
			if(callback){
				col+=' data-callback="'+callback+'"';
			}
			col+=' data-url="'+url+'" data-on-value="'+onVal+'" data-off-value="'+offVal+'">';
			return col;
		},
		//制保留n位小数，如：2，会在2后面补上00.即2.00 
		toDecimal:function(num,n) {
			if(n<=0){
				return num;
			}
			if(isNaN(num)){
				return false;
			}
			var f = parseFloat(num);
			if (isNaN(f)) {
				return false;
			}
			f = Math.pow(10, n);
			f = Math.round(num * f) / f;
			f = f.toString();
			var rs = f.indexOf('.');
			if (rs < 0) {
				rs = f.length;
				f += '.';
			} else if (f.length - rs > n + 1) {
				return f.substring(0, rs + n + 1)
			}
			while (f.length <= rs + 2) {
				f += '0';
			}
			return f;
		},
		/**
		 * 缓存
		 * @param table
		 * @param settings
		 * @returns
		 */
		setCache : function(table,settings){
			table = table || 'fui';
			if(!window.JSON || !window.JSON.parse) return;
			if (window.isPrivateMode || !window.localStorage)return;
			 // 不是 Safari 无痕模式并且能用 localStorage
			//如果settings为null，则删除表
			if(settings === null){
				return delete localStorage[table];
			}
			settings = typeof settings === 'object' ? settings  : {key: settings};
			var data =null;
			try{
				data = JSON.parse(localStorage[table]);
			} catch(e){
				data = {};
			}
			if(settings.value) data[settings.key] = settings.value;
			if(settings.remove) delete data[settings.key];
			localStorage[table] = JSON.stringify(data);
			return settings.key ? data[settings.key] : data;
		},
		/**
		 * 初始化UI元素
		 */
		initUI:function(_box){
			var $p = $(_box || document);
			$(".open-dialog",$p).click(function(event){
				var $this = $(this)
					,url = unescape($this.attr("href")||$this.attr("url")).replaceTmBySelector($(event.target).parents(".fui-box:first"))
					,cache=$this.hasClass("cached")
					,title=$this.attr("dialog-title");
				if (!url.isFinishedTm()) {
					layer.msg($this.attr("warn")||((typeof Admin=='undefined')?'请选择一条数据':Admin.selectOne));
					return false;
				}
				var option = {url:url,cache:cache}
					,w = $this.attr("width")
					,h = $this.attr("height");
				option.area =[(w?w+"px":""),(h?h+"px":"")];
				if(title){
					option.title=title;
				}
				Fui.openDialog(option);
				return false;
			});
			$(".open-text",$p).click(function(event){
				var $this = $(this)
					,text=$this.attr("dialog-text")
					,title=$this.attr("dialog-title")||((typeof Admin=='undefined')?"详情":Admin.detail);
				var w = $this.attr("width"),h = $this.attr("height");
				var area =[(w?w+"px":"600px"),(h?h+"px":"300px")];
				if(!text){
					text=$this.html();
				}
				layer.open({
				  type: 1,
				  title : title,
				  skin: 'layui-layer-rim', //加上边框
				  area: area, //宽高
				  content: text
				});
				return false;
			});
			$(".todo-ajax",$p).click(function(event){
				var layerLoadIndex=layer.load(2);
				var $this = $(this);
				var url=$this.attr("href")||$this.attr("url");
				url = unescape(url).replaceTmBySelector($(event.target).parents(".fui-box:first"));
				if (!url.isFinishedTm()) {
					layer.msg($this.attr("warn")||((typeof Admin=='undefined')?'请选择一条数据':Admin.selectOne));
					layer.close(layerLoadIndex);
					return false;
				}
				var title = $this.attr("title");
				if (title) {
					var layerIndex=layer.confirm(title,{cancel:function(){layer.close(layerLoadIndex);}}, function(){
							ajaxTodo(url, $this.attr("callback"),{unReloadTable:$this.attr("unReloadTable")});
							layer.close(layerIndex);
						},function(){
							layer.close(layerLoadIndex);
						});
				} else {
					ajaxTodo(url, $this.attr("callback"),{unReloadTable:$this.attr("unReloadTable")});
				}
				event.preventDefault();
			});
			$(".open-tab",$p).click(function(event){
				var it = $(this)
					,url=it.attr("href")||it.attr("url")
					,option
					,item=null;
				url = unescape(url).replaceTmBySelector($(event.target).parents(".fui-box:first"));
				item=Fui.navbar.getMenuItem(url)
				if(item){
					option = {
						id:it.attr("id")||item.id
						,title:it.attr("title")||item.title
						,url:url
						,icon:item.icon
						,closed:item.closed
						,refresh:it.data("refresh")
						,unBreadcrumb:it.data("unbreadcrumb")
					};
				}else{
					option = {
						id:it.attr("id")
						,title:it.attr("title")||it.text()
						,url:url
						,icon:it.data("icon")
						,closed:it.data("closed")
						,refresh:it.data("refresh")
						,unBreadcrumb:it.data("unbreadcrumb")
					};
				}
				Fui.tab.tabAdd(option);
				event.preventDefault();
			});
			var formSubmit=$(".form-submit",$p);
			if(formSubmit.length){
				requirejs(['fui_form'],function(FuiForm){
					FuiForm.initFormValidator(formSubmit,$p);
				})
			}
			var dateEle=$(".fui-date",$p);
			if(dateEle.length){
				requirejs(['fui_datetime'],function(FuiDateTime){
					FuiDateTime.initDatetime(dateEle,$p);
				})
			}
			var $fuiSearchForm=$("form.fui-search",$p);
			$fuiSearchForm.submit(function(){
				var $table=$fuiSearchForm.parents(".fui-box:first").data("bootstrapTable");
				if(!$table){
					layer.msg("表格不存在");
					return false;
				}
				$table.bootstrapTable('refreshOptions',{pageNumber:1});
				return false;
			});
			$fuiSearchForm.find(".reset").click(function(){
				$fuiSearchForm[0].reset();
				$fuiSearchForm.submit();
			});
			var $switch=$(".fui-switch",$p);
			if($switch.length){
				requirejs(['fui_switch'],function(FuiSwitch){
					FuiSwitch.initSwitch($switch,$p);
				});
			}
			var $selectpicker=$(".selectpicker",$p);
			if($selectpicker.length){
				requirejs(['jquery','bootstrapSelect'],function(){
					$selectpicker.selectpicker();
				})
			}
		},
		addBootstrapTable:function(options){
			requirejs(['fui_table'],function(FuiTable){
				var $table=null;//$('#' + options.id)
				if(!options.id){
					$table=$(".fui-dialog.fui-box").find(".fui-default-table");
					if(!$table[0]){
						$table=$(".fui-box.active").find(".fui-default-table");
					}
				}else{
					if(typeof options.id === 'string'){
						$table=$('#' + options.id);
					}else{
						$table=options.id;
					}
				}
				var $parent=$table.parents(".fui-box:first")
					,$form=$parent.find("form.fui-search")
					,$tool=$parent.find(".table-tool");
				if($table.length==0){
					layer.msg("没有找到页面上的table");
					return;
				}
				options.table=$table;
				options.toolbar=$tool;
				if($form.length){
					options.form=$form;
				}
				if(options.unBindParent==undefined){//一个页面有多个table时，可以设置一个绑定，其他不绑定
					$parent.data("bootstrapTable",FuiTable.createBootstrapTable(options));
				}else{
					FuiTable.createBootstrapTable(options);
				}
			});
		},
		openDialog:function(option){
			if(option.id){
				if($('#'+option.id)[0])return;
			}
			var layerIndex=0
				,layerLoadIndex=layer.load(2);
			var op={
				id:option.id
				,type: 1
				,title: false
				,closeBtn: false
				,shadeClose: false
				,maxWidth:1024
				,skin:'fui-dialog fui-box'
				,move:'.modal-header h4'
				,offset:'t'
			    //,zIndex: layer.zIndex
			    ,success: function(layero){
			    	//layer.setTop(layero);
			    	layero.find('.fui-close').click(function(){
			    		var cancelFun=$(this).data("cancelFun");
			    		if(cancelFun){
							if (! $.isFunction(cancelFun)) cancelFun = eval('(' + cancelFun + ')');
							cancelFun();
						}
		    			layer.close(layerIndex);
		    		});
			    	Fui.initUI(layero);

			    	if(option.callback){
		    			option.callback(layero);
		    		}
			    	layero.find(".modal-title").html(option.title);
			    	layer.close(layerLoadIndex);
			    }
			};
			if(option.area){
				op.area=option.area;
			}
			if(option.offset){
				op.offset=option.offset;
			}
			if(option.url){
				var cacheContent=Fui.cacheMap[option.url];
				if(option.cache && cacheContent){
					op.content=cacheContent;
					layerIndex=layer.open(op);
				}else{
					$.ajax({
						type: 'GET',
						url: option.url,
						dataType: 'html',
						cache:option.cache||false,
						success: function(html, status, xhr) {
							op.content=html;
							layerIndex=layer.open(op);
							if(option.cache){
								Fui.cacheMap[option.url]=html;
							}
						},
						error: function(xhr, status, error) {
							layer.msg(((typeof Admin=='undefined')?'打开弹窗发生错误:':Admin.openDialogErr) + error);
						}
					});
				}
			}else{
				op.content=option.content;
				layerIndex=layer.open(op);
			}
		},
		navbarDefaultSetting :{//navbar默认配置
			elem : undefined, // 容器
			data : undefined, // 数据源
			url : undefined, // 数据源地址
			type : 'GET', // 读取方式
			cached : false, // 是否使用缓存
			spreadOne : true // 设置是否只展开一个二级菜单
		}
	};
	
	var cacheName = 'tb_navbar';
	var Navbar = function() {
		/**
		 *  默认配置 
		 */
		this.config =$.extend({},Fui.navbarDefaultSetting);
		this.v = '0.0.1';
	};
	/**
	 * 获取html字符串{spread:false,children:[{href:"",icon:"",title:"",id:""}],icon:"",href:"",title:"",id:""}
	 * @param {Object} data 
	 */
	function getNavbarHtml(data) {
		var ulHtml = ''
			,item=null,ic=null, href=null
			,adminBaseUrl=Fui.config.adminBaseUrl;
		for(var i = 0; i < data.length; i++) {
			item=data[i];
			item.ptitle='';
			item.level=1;
			item.id="fui_tab_"+(item.id||new Date().getTime());
			ulHtml += '<li';
			if(item.spread) {
				ulHtml += ' class="active"';
			}
			if(item.children !== undefined && item.children.length > 0) {
				ulHtml += '><a href="javascript:;"><i class="fa ' + item.icon 
				+ '"></i><span class="nav-label">' + data[i].title 
				+ '</span> <span class="fa arrow"></span></a>';
				ulHtml += '<ul class="nav nav-second-level">'
				for(var j = 0; j < item.children.length; j++) {
					ic=item.children[j];
					ic.ptitle=item.title;
					ic.level=2;
					href=ic.href;
					if((href.indexOf(adminBaseUrl)!=0 && href.indexOf("http://")==-1)|| href.indexOf('/admin/')!=0 ){
						href=adminBaseUrl+href;
					}
					ic.href=href;
					if(ic.type && ic.type==2){
						ulHtml += '<li><a href="' + href + '" target="_blank">';
					}else{
						ic.id="fui_tab_"+(ic.id||new Date().getTime());
						ulHtml += '<li><a href="' + href + '" class="J_menuItem url-a" id="'+ic.id+'">';
					}
					ulHtml +=  ic.title + '</a></li>';
				}
				ulHtml += '</ul>';
			} else {
				href=item.href;
				if(href == undefined || item.href==''){
					href='javascript:void(0);';
				}else if(href.indexOf(adminBaseUrl)!=0 && href.indexOf("http://")==-1){
					href=adminBaseUrl+href;
				}
				item.href=href;
				if(item.type && item.type==2){
					ulHtml += '><a href="' + href + '" target="_blank">';
				}else{
					ulHtml += '><a href="' + href + '" class="url-a" id="'+item.id+'">';
				}
				ulHtml += '<i class="fa ' + item.icon + '"></i>';
				ulHtml += '<span class="nav-label">' + item.title + '</span></a>';
			}
			ulHtml += '</li>';
		}
		ulHtml += '</ul>';
		return ulHtml;
	};
	/**
	 * 使用url初始化菜单
	 */
	function initNavbarByUrl(_config,$container){
		$.ajax({
			type: _config.type,
			url: _config.url,
			async: true, //_config.async,
			dataType: 'json',
			success: function(result, status, xhr) {
				//添加缓存
				Fui.setCache(cacheName, {
					key: 'navbar',
					value: result
				});
				_config.data=result;
				$container.append(getNavbarHtml(result));
				initNavbarEventFn(_config,$container);
			},
			error: function(xhr, status, error) {
				layer.msg('Navbar error:' + error);
				return;
			},
			complete: function(xhr, status) {
				_config.elem = $container;
			}
		});
	}
	function initNavbarEventFn(_config,$container){
		$('#side-menu').metisMenu();
		$container.find("a.url-a").each(function(){
			$(this).on('click',function(event){
				var it=$(this);
				Fui.tab.tabAdd({id:it.attr("id"),title:it.text(),url:it.attr("href"),closed:it.data("closed")});
				event.preventDefault();
			});
		});
		$('#side-menu>li').click(function () {
	        if ($('body').hasClass('mini-navbar')) {
	        		$('.navbar-min').trigger('click');
	        }
	    });
	    $('#side-menu>li li a').click(function () {
	        if ($(window).width() < 769) {
	        		$('.navbar-min').trigger('click');
	        }
	    });
	}
	$.extend(Navbar.prototype,{
		render : function() {
			var _that = this;
			var _config = _that.config;
			if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
				layer.msg('Navbar error: elem undefined.');
				return;
			}
			var $container;
			if(typeof(_config.elem) === 'string') {
				$container = $(_config.elem);
			}
			if(typeof(_config.elem) === 'object') {
				$container = _config.elem;
			}
			if($container.length === 0) {
				layer.msg('Navbar error:Refresh please!');
				return ;
			}
			if(_config.data === undefined && _config.url === undefined) {
				layer.msg('Navbar error:undefined url.');
				return ;
			}
			if(_config.data !== undefined && typeof(_config.data) === 'object') {
				$container.append(getNavbarHtml(_config.data));
				_config.elem = $container;
				initNavbarEventFn(_config,$container);
			} else {
				if(_config.cached) {
					var cacheNavbar = Fui.setCache(cacheName);
					if(cacheNavbar.navbar === undefined) {
						initNavbarByUrl(_config,$container);
					} else {
						$container.append(getNavbarHtml(cacheNavbar.navbar));
						_config.elem = $container;
						initNavbarEventFn(_config,$container);
					}
				} else {
					//清空缓存
					Fui.setCache(cacheName, null);
					initNavbarByUrl(_config,$container);
				}
			}
			
			return _that;
		},
		/**
		 * 配置Navbar
		 * @param {Object} options
		 */
		set : function(options) {
			var that = this;
			that.config.data = undefined;
			$.extend(true, that.config, options);
			return that;
		},
		/**
		 * 清除缓存
		 */
		cleanCached : function(){
			Fui.setCache(cacheName,null);
		},
		/**
		 * 获取菜单项
		 */
		getMenuItem:function(url){
			if(!url)return null;
			var data = this.config.data;
			if(!data){
				return null;
			}
			var item=null,ic=null,url1=url;
			if(url1.indexOf("?")>0){
				url1=url1.substring(0,url1.indexOf("?"));
			}
			for(var i = 0; i < data.length; i++) {
				item=data[i];
				if(item.href && (item.href==url || item.href==url1)){
					return item;
				}else{
					if(item.children !== undefined && item.children.length > 0) {
						for(var j = 0; j < item.children.length; j++) {
							ic=item.children[j];
							if(ic.href && (ic.href==url || ic.href==url1)){
								return ic;
							}
						}
					}
				}
			}
			return null;
		}
	});
Fui.navbar=new Navbar();
	
	var Tab = function() {
		this.config = {
			elem: undefined,
			closed: true, //是否包含删除按钮				
			autoRefresh: true
		};
	};
	function addTabContent(id,title,content,layerIndex){
		//添加tab
		TabELEM.titleBox.append(title);
		TabELEM.contentBox.append(content);
		layer.close(layerIndex);
		Fui.initUI(TabELEM.contentBox.find("#content_"+id));
		//切换到当前打开的选项卡
		TabELEM.titleBox.find('li[data-tabid=' + id + ']').find('a[role="tab"]').tab('show');
		$(window).resize();
	}
	function getTabContentPrefix(id,data,html,istab){
		var content='';
		if(!istab){
			content='<div role="tabpanel" class="tab-pane fui-box" id="content_'+id+'">';
		}
		content+='<div class="container-fluid">'+html+"</div>";
		if(!istab){
			content+="</div>";
		}
		return content;
	}
	var TabELEM = {};
	$.extend(Tab.prototype,{
		/**
		 * 参数设置
		 * @param {Object} options
		 */
		set : function(options) {
			var that = this;
			$.extend(true, that.config, options);
			return that;
		},
		/**
		 * 初始化
		 */
		init : function() {
			var that = this;
			var _config = that.config;
			if(typeof(_config.elem) !== 'string' && typeof(_config.elem) !== 'object') {
				layer.msg('Tab error:Refresh please!.');
				return;
			}
			var $container;
			if(typeof(_config.elem) === 'string') {
				$container = $('' + _config.elem + '');
			}
			if(typeof(_config.elem) === 'object') {
				$container = _config.elem;
			}
			if($container.length === 0) {
				layer.msg('Tab error:Refresh please!.');
				return;
			}
			_config.elem = $container;
			TabELEM.titleBox = $container.children('ul#fui_tab_header');
			TabELEM.contentBox = $container.children('div#fui_tab_content');
			
			TabELEM.titleBox.on('click','i.fui-tab-close[data-tabid]', function() {
				var it=$(this),id=it.data("tabid"),
					tabIndex=that.exists(id),//从0开始的，如果是第四个tab  则该值为3
					len=TabELEM.titleBox.find('li').length,
					li=it.parent(),
					isActive=li.hasClass("active");
				li.remove();
				$("#content_"+id).remove();
				if(isActive){
					if(tabIndex>=len-1){//-1是因为tabIndex从0开始，需要减1
						tabIndex=len-2;
					}else{
						tabIndex--;
					}
					TabELEM.titleBox.find('li').eq(tabIndex).find('a[role="tab"]').tab('show');
				}
			});
			return that;
		},
		close:function(id){
			TabELEM.titleBox.find('i.fui-tab-close[data-tabid='+id+']').click();
		},
		/**
		 * 查询tab是否存在，如果存在则返回索引值，不存在返回-1
		 * @param {String} 标题
		 */
		exists : function(id) {
			var that = TabELEM.titleBox === undefined ? this.init() : this,
				tabIndex = -1;
			TabELEM.titleBox.find('li').each(function(i, e) {
				var tabid = $(this).data('tabid');
				if(tabid === id) {
					tabIndex = i;
				};
			});
			return tabIndex;
		},
		/**
		 * 添加选择卡，如果选择卡存在则获取焦点
		 * @param {Object} data
		 */
		tabAdd : function(data) {
			var that = this;
			var _config = that.config;
			var id=data.id||new Date().getTime();
			var tabIndex = that.exists(id);
			if(tabIndex === -1) {
				var title = '<li data-tabid="'+id+'"><a href="#content_'+id+'" role="tab" data-toggle="tab">';
				title += '<span>' + data.title + '</span></a>';
				if(_config.closed && data.closed!=false) {
					title += '<i class="fui-tab-close fa fa-times-circle" data-tabid="'+id+'"></i>';
				}
				title+='</li>';
				if(data.content){
					addTabContent(id,title,getTabContentPrefix(id,data,data.content),layerIndex);
					if(data.callback){
						data.callback();
					}
				} else{
					var layerIndex=layer.load(2);
					var cacheContent=null;
					if(!data.refresh && id!='fui_tab_301'){
						cacheContent=Fui.cacheMap[data.url];
					}
					if(cacheContent){
						addTabContent(id,title,getTabContentPrefix(id,data,cacheContent),layerIndex);
						if(data.callback){
							data.callback();
						}
					}else{
						$.ajax({
							type: 'GET',
							url: data.url,
							dataType: 'html',
							cache:!data.refresh,
							success: function(html, status, xhr) {
								addTabContent(id,title,getTabContentPrefix(id,data,html),layerIndex);
								Fui.cacheMap[data.url]=html;
								if(data.callback){
									data.callback();
								}
							},
							error: function(xhr, status, error) {
								layer.msg('打开标签发生错误:' + error);
								if(data.callback){
									data.callback();
								}
							}
						});
					}
				}
			} else {
				TabELEM.titleBox.find('li').eq(tabIndex).find('a[role="tab"]').tab('show');
				//自动刷新
				if((_config.autoRefresh && data.url)||data.refresh){
					$.get(data.url,function(html){
						var con=TabELEM.contentBox.find("#content_"+id);
						con.html(getTabContentPrefix(id,data,html,true));
						Fui.initUI(con);
						if(data.callback){
							data.callback();
						}
					},"html");
				}else{
					if(data.callback){
						data.callback();
					}
				}
			}
		}
	});
	Fui.tab=new Tab();
	
	window.Fui=Fui;
	return Fui;
});
