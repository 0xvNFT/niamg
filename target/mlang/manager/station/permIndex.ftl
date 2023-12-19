<div id="station_menu_perm_wrap_id">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="form-inline">
				<button class="btn btn-primary saveAllConfig" disabled="disabled">保存<@spring.message ""/></button>
				<button class="btn btn-primary selectAll" disabled="disabled">全选<@spring.message ""/></button>
				<select class="form-control" name="stationId"><option value="0">选择站点<@spring.message ""/></option>
				<#list stations as s><option value="${s.id}">${s.name}</option></#list>
				</select>
			</div>
		</div>
		<div class="panel-body settingsCon"></div>
	</div>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/manager/permIndex.js?v=2'],function($configIndex){
	$configIndex.render();
});
</script>