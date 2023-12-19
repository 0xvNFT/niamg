<div id="station_confg_tabs_wrap_id">
	<ul class="nav nav-tabs mb10px">
		<li class="active"><a href="#" data-toggle="tab" oname="bangding"><@spring.message "manager.bound.station.config"/></a></li>
		<li><a href="#" data-toggle="tab" oname="shezhi"><@spring.message "manager.setting.station.config"/></a></li>
	</ul>
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="form-inline">
				<button class="btn btn-primary saveAllConfig" disabled="disabled"><@spring.message "admin.deposit.handle.save"/></button>
				<button class="btn btn-primary selectAll" disabled="disabled"><@spring.message "manager.all.selected"/></button>
				<select class="form-control" name="stationId"><option value="0"><@spring.message "manager.station.selected"/></option>
				<#list stations as s><option value="${s.id}">${s.name}</option></#list>
				</select>
				<div class="btn-group btn-group-sm eleTabs">
				</div>
			</div>
		</div>
		<div class="panel-body settingsCon"></div>
	</div>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/manager/stationConfig.js?v=6.0'],function($registerconfig){
	$registerconfig.render();
});
</script>
