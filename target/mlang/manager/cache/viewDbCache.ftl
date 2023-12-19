<div class="modal-dialog" id="view_db_cache_divid">
	<div class="modal-content">
		<div class="modal-header">
			<button type="button" class="close fui-close" aria-hidden="true">&times;</button>
			<h4 class="modal-title"><@spring.message "manager.clean.cache.mem"/></h4>
		</div>
		<div class="modal-body"><div style="height:500px;overflow:auto;">
			<table class="table table-bordered table-striped">
				<tr>
					<td width="50%">key</td>
					<td width="50%"><@spring.message "admin.deposit.table.lockFlag"/></td>
				</tr>
				<#list list as k><tr>
					<td>${k}</td>
					<td><a href="javascript:void(0)" db="${db}" key="${k}" class="viewDetail"><@spring.message "manager.info.view"/></a>&nbsp; &nbsp;
						<a href="javascript:void(0)" db="${db}" key="${k}" class="deleteCache"><@spring.message "admin.deposit.bank.bankCard.del"/></a></td>
				</tr></#list>
			</table></div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
		</div>
	</div>
</div>
<script>
requirejs(['jquery','bootstrap','Fui'],function(){
	var $con=$("#view_db_cache_divid");
	$con.find(".viewDetail").click(function(){
		var $it=$(this),db=$it.attr("db"),key=$it.attr("key");
		$.get("${managerBase}/cache/getCacheContext.do?key="+key+"&db="+db,function(text){
			layer.alert(text);
		},"text");
	});
	
	$con.find(".deleteCache").click(function(){
		var $it=$(this),db=$it.attr("db"),key=$it.attr("key");
		$.get("${managerBase}/cache/delCache.do?key="+key+"&db="+db,function(json){
			layer.msg(json.msg);
		},"json");
	});
	
});
</script>