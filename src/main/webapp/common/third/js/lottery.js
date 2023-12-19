function init(){
	var type=$(".nav li[data-type]:first").data('type'),forwardGame1=tcgdata;
	if(type=='tcg'){
		searchData=tcgdata;
	}
	
	//数据获取
	new Vue({
		el: '#cbox',
		data: {
			searchData:searchData,
			tabTxt: type,
			gameLength: searchData.length
		},
		methods: {
			// 根据传参显示对应内容
			tabChange:function(type){
				this.tabTxt = type;
				if(type == 'tcg'){
					this.searchData = tcgdata;
				}
				this.gameLength = this.searchData.length;
			},
			
			// 面包屑导航
			/*gameType:function(tab,type){
				if(tab == 'tcg'){
					if(type == 'tcgdata') this.searchData = tcgdata;

				}else if(tab == 'mg'){
					if(type == 'mgdata') this.searchData = mgdata;
				}else if(tab == 'skywind'){
					if(type == 'skywinddata') this.searchData = skyWindData;
				}else if(tab == 'pt'){
					switch(type){
					case 'ptdata':this.searchData = ptdata;break;
					default:
						var d=[];
						for(var i=0;i<ptdata.length;i++){
							if(ptdata[i].tId==type){
								d.push(ptdata[i]);
							}
						}
						this.searchData = d;
					}
				}
				this.gameLength = this.searchData.length;
			},*/

			// 搜索指定游戏
			/*searchGame:function(){
				var searchGameName = $('#elenew-search-game').val(),allData;
				if(this.tabTxt == 'ag')	{
					allData = agdata;
				}
				if(this.tabTxt == 'mg') {
					allData = mgdata;
				}
				if(this.tabTxt == 'pt')	{
					allData = ptdata;
				}
				if(this.tabTxt == 'skywind')	{
					allData = skyWindData;
				}
				if(searchGameName){
					searchGameName=searchGameName.toLowerCase();
					this.searchData = [];
					for(var i = 0; i < allData.length; i++){
						if(allData[i].name.toLowerCase().includes(searchGameName)){
							this.searchData.push(allData[i]);
						}
					}
				}else{
					this.searchData=allData;
				}
				this.gameLength = this.searchData.length;
			},*/

			forwardGame1:function(isLogin,gameId,className){
				className=className||"ctl-btn-lite";
				if(!isLogin){
					return '<div class="elenew-game-ctl-links"><a href="javascript:void(0);"class="unlogin '+className+'">Start Game</a></div>';
				}
				var s='<div class="elenew-game-ctl-links"><a href="'+base;
				switch(this.tabTxt){
				case "tcg":
					s=s+'/third/forwardTcg.do';
					break;
				}
				return s+'" target="_blank" class="'+className+'">Start Game</a></div>';
			}
		}
	})

	$('.elenew-gn-btn').hover(function () {
		$('.elenew-gn-icon').toggleClass('on');
		$('.elenew-gn-wrap').slideToggle();
	});

	// 游戏展示方式切换
	$('.elenew-viewbtn-mini').click(function(){
		$('.elenew-game-wrap').removeClass('elenew-view-block');
		$('.elenew-game-wrap').addClass('elenew-view-mini');
		$(this).addClass('view-active');
		$('.elenew-viewbtn-block').removeClass('view-active');
	});
	$('.elenew-viewbtn-block').click(function(){
		$('.elenew-game-wrap').removeClass('elenew-view-mini');
		$('.elenew-game-wrap').addClass('elenew-view-block');
		$(this).addClass('view-active');
		$('.elenew-viewbtn-mini').removeClass('view-active');
	});
}
(function($) {
	// 备份jquery的ajax方法
	var _ajax = $.ajax;

	// 重写jquery的ajax方法
	$.ajax = function(opt) {
		if (!opt.dataType) {
			opt.dataType = "json";
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
					layer.alert("[" + opt.url + "] 404 not found");
				} else {
					fn.error(XMLHttpRequest, textStatus, errorThrown);
				}
			},
			success : function(data, textStatus, xhr) {
				var ceipstate = xhr.getResponseHeader("ceipstate")
				if (ceipstate == 1) {// 正常响应
					fn.success(data, textStatus, xhr);
				} else if (ceipstate == 2) {// 后台异常
					layer.alert(data.msg);
				} else if (ceipstate == 3) { // 业务异常
					layer.alert(data.msg);
				} else if (ceipstate == 4) {// 未登陆异常
					layer.alert(data.msg);
				} else if (ceipstate == 5) {// 没有权限
					layer.alert(data.msg);
				} else {
					fn.success(data, textStatus, xhr);
				}
			}
		});
		_ajax(_opt);
	};
	init();
	$("#cbox").on("click",".unlogin",function(){
		layer.open({
	        type : 1,
	        title : Admin.notLogin,
	        shadeClose : true,
	        shade : 0.8,
	        area : [ '380px', '200px' ],
	        content : $('#loginfrom')
	    });
	});
})(jQuery);
