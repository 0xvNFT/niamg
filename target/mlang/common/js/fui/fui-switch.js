define(['bootstrapSwitch'],function(){
	return {
		initSwitch:function(eles,$p){
			eles.each(function(){
				var it=$(this)
					,url=it.data("url")
					,onVal=it.data("on-value")
					,onText=it.data("on-text")
					,offVal=it.data("off-value")
					,offText=it.data("off-text")
					,size=it.data("size")||"mini"
					,tip=it.attr("tip")||((typeof Admin=='undefined')?"确定要切换为":Admin.sureSwitchTo)
					,callback=it.data("callback")
					,eventSwitch=true;
				it.bootstrapSwitch({size:size});
				it.on('switchChange.bootstrapSwitch', function(event, state) {
					if(!eventSwitch){
						eventSwitch=true;
						return false;
					}
					var t,url1=url,v=false;
					if(state){
						t=onText;
						url1+=onVal;
						v=false;
					}else{
						t=offText;
						url1+=offVal;
						v=true;
					}
					var inde=layer.confirm(tip+t, function(){
						$.ajax({
							url:url1
							,type:"POST"
							,dataType:"json"
							,beforeSend:function(){
								layer.load(2);
							}
							,success: function(json){
								if(json.success){
									if(callback){
										if (! $.isFunction(callback)) callback = eval('(' + callback + ')');
										callback(json);
									}else{
										layer.msg(json.msg||(((typeof Admin=='undefined')?"已修改成":Admin.sureSwitchTo)+t));
									}
								}else{
									layer.msg(json.msg);
									eventSwitch=false;
									it.bootstrapSwitch("state",v);
								}
							}
							,error: function(xhr, status, error) {
								layer.msg(((typeof Admin=='undefined')?'网络异常':Admin.networkError) + error);
							}
							,errorFn:function(json,ceipstate){
								eventSwitch=false;
								it.bootstrapSwitch("state",v);
							}
							,complete:function(){
								layer.closeAll('loading');
							}
						});
						layer.close(inde);
					},function(){
						eventSwitch=false;
						it.bootstrapSwitch("state",v);
						layer.close(inde);
					});
				});
			});
		}
	}
});