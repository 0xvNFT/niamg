define(['template','moment','jquery','bootstrap','Fui','bootstrap_editable'],function(template,moment){
	var source='{{each menus as fstNode}}<div class="checkbox"><label>'
  		+'<input type="checkbox" class="menu_id" id="perm_menu_{{fstNode.id}}" value="{{fstNode.id}}"'
  		+'{{checkeds[fstNode.id]}}> {{fstNode.title}}</label><div class="checkbox secCheckBox">{{each fstNode.childs as secNode}}'
		+'<label><input type="checkbox" class="menu_id" id="perm_menu_{{secNode.id}}" parent="{{secNode.parentId}}" value="{{secNode.id}}"'
		+'{{checkeds[secNode.id]}}> {{secNode.title}}</label><div class="checkbox thdCheckBox">{{each secNode.childs as thdNode}}'
		+'<label class="checkbox-inline"><input type="checkbox" class="end" parent="{{thdNode.parentId}}" pparent="{{fstNode.id}}"'
		+' value="{{thdNode.id}}" {{checkeds[thdNode.id]}}> {{thdNode.title}}</label>{{/each}}</div>{{/each}}</div></div>{{/each}}';
	function loadMenuPerm($con, stationId){
		$.getJSON(baseInfo.adminBaseUrl+"/stationPerm/list.do?stationId="+stationId,function(json){
			json.checkeds={};
			if(json.menuIds && json.menuIds.length>0){
				for(var i=0;i<json.menuIds.length;i++){
					json.checkeds[json.menuIds[i]]="checked";
				}
			}
			$con.find(".settingsCon").html(template.compile(source)(json));
			$con.find(".menu_id").click(function(){
				var i=$(this).val();
				if($(this).prop("checked")){
					$con.find("[parent='"+i+"']").prop("checked",true);
					$con.find("[pparent='"+i+"']").prop("checked",true);
				}else{
					$con.find("[parent='"+i+"']").prop("checked",false);
					$con.find("[pparent='"+i+"']").prop("checked",false);
				}
			});
		});
	}
	return {
		render:function(){
			var $con=$("#station_menu_perm_wrap_id");
			$con.find(".selectAll").click(function(){
				if($(this).attr("checked1")=="checked"){
					$con.find("[type='checkbox']").prop("checked",false);
					$(this).attr("checked1","");
				}else{
					$con.find("[type='checkbox']").prop("checked",true);
					$(this).attr("checked1","checked");
				}
			});
			$con.find(".saveAllConfig").click(function(){
				var ids = [];
				$con.find("[type='checkbox']:checked").each(function(){
					ids.push($(this).val());
				});
				$.ajax({
					url : baseInfo.adminBaseUrl+"/stationPerm/save.do",
					data : {
						stationId : $con.find("[name='stationId']").val(),
						ids : ids.join(",")
					},
					success : function(json) {
						layer.msg(json.msg);
					}
				});
			});
			$con.find("[name='stationId']").change(function(){
				var stationId = $(this).val();
				if (stationId == 0) {
					$con.find(".saveAllConfig").prop("disabled",true);
					$con.find(".selectAll").prop("disabled",true);
					$con.find(".settingsCon").html("");
					return;
				}
				$con.find(".saveAllConfig").prop("disabled",false);
				$con.find(".selectAll").prop("disabled",false);
				loadMenuPerm($con, stationId);
			});
		}
	}
});