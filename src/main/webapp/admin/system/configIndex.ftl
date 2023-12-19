<div id="station_confg_tabs_wrap_id">
	<div class="panel panel-default">
		<div class="panel-heading">
			<div class="form-inline">
				<@spring.message "admin.stationConfig"/>
				<div class="btn-group btn-group-sm eleTabs"></div>
			</div>
		</div>
		<div class="panel-body settingsCon"></div>
	</div>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/admin/stationConfig.js?v=6.0'],function($f){
	$f.render();
});
</script>