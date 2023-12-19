<div id="station_registerconfig_con_warp_id">
	<ul class="nav nav-tabs mb10px">
		<li class="active"><a href="#" data-toggle="tab" oname="huiyuan"><@spring.message "admin.registerConfig.member"/></a></li>
		<li><a href="#" data-toggle="tab" oname="daili"><@spring.message "admin.registerConfig.proxy"/></a></li>
	</ul>
	<table class="fui-default-table"></table>
</div>
<script type="text/javascript">
requirejs(['${base}/common/js/admin/registerconfig.js?v=4'],function($f){
	$f.render();
});
</script>