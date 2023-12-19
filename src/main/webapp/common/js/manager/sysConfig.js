define(['template','moment','jquery','bootstrap','Fui','bootstrap_editable'],function(template,moment){
	var source='{{each configGroups as gp}}<div class="panel panel-default">'
		+'<div class="panel-heading"><a role="button" data-toggle="collapse" href="#config_list_collapse_{{configMap[gp].id}}"'
		+'aria-expanded="true" aria-controls="config_list_collapse_{{configMap[gp].id}}">{{gp}}</a></div>'
		+'<div class="collapse in" id="config_list_collapse_{{configMap[gp].id}}"><div class="panel-body">'
		+'<table class="table table-bordered table-striped">'
		+'<tbody>{{each configMap[gp].sons as conf}}'
		+'<tr><td width="30%" class="text-center">{{conf.name}}</td>'
		+'<td width="70%"><a style="word-break: break-all;max-width:50%;display:inline-block;" href="#" data-type="{{conf.eleType}}" data-dtype="{{conf.dataType}}"'
		+'data-value="{{conf.initValue}}" data-pk="{{conf.key}}"'
		+'data-title="{{conf.title}}"></a></td></tr>{{/each}}'
		+'</tbody></table></div></div></div>{{/each}}';
	window.moment=moment;
	function setParams(params) {
		if ($(this).attr("data-type") === 'checklist') {
			params.value = params.value.join(",");
		}
		return params;
	}
	function initXeditable(data){
		var $content=$("#config_list_content_id").html(template.compile(source)(data));
		$content.find("a").each(function() {
			var options = {
					url : baseInfo.adminBaseUrl+'/config/save.do',
					emptytext : '无配置',
					params : setParams,
					placement : 'right',
					ajaxOptions:{
						dataType:"json",
						success:function(json){
							layer.msg(json.msg||"修改成功");
						}
					}
				},type = $(this).attr("data-type")
				,dtype=$(this).attr("data-dtype")
				,source=[]
				,val = $(this).attr("data-value");
			if (val !== undefined && val !== "") {
				$(this).html(val.replace(/</g,"&lt;"));
			}
			if (type === 'select') {
				options.source = source;
                switch (dtype) {
                    case "switchSelect":
                        options.source = [{"off": "关闭"}, {"on": "开启"}];
                        break;
                    case 'modelSelect':
                        options.source = [{"rand": "随机"}, {"pct": "中奖百分比"}];
                        break;
                }
				var arr = options.source;
				for (var i = 0; i < arr.length; i++) {
					var obj = arr[i];
					if (obj[val] !== undefined) {
						$(this).html(obj[val]);
						break;
					}
				}
			} else if (type === 'combodate') {
				var fmt = 'HH:mm'
					,cdconf={
						format : fmt
		                ,minYear: 2015
		                ,maxYear: 2030
		                ,minuteStep: 1
					};
				if (source) {
					var src = eval("(" + source + ")");
					for ( var key in src) {
						if (key != "format") {
							cdconf[key] = src[key];
						}
					}
				}
				options.template = fmt;
				options.format = fmt;
				options.viewformat = fmt;
				options.combodate = cdconf;
			} else if (type === 'textarea') {
				options.placement = 'top';
			}
			$(this).editable(options);
		});
	}
	return {
		render:function(){
			$.get(baseInfo.adminBaseUrl+"/config/getConfigs.do",function(data){
				for(var kk in data.configMap){
					var arr=data.configMap[kk].sons;
					for (var i = 0; i < arr.length; i++) {
	                     item = arr[i];
	                     if (item.eleType.indexOf("Select") > 0) {
	                         item.dataType = item.eleType;
	                         item.eleType = "select";
	                     }
	                 }
				}
				initXeditable(data);
			},"json");
		}
	}
});