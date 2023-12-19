<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="form-group">
				<div class="input-group">
					<input type="text" class="form-control fui-date" name="startTime" placeholder="<@spring.message "admin.startDat"/>" value="${startDate}"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
				</div>
				<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
				<div class="input-group">
					<#--<input type="text" class="form-control" name="username" placeholder="所属用户" value="${user.username}">-->
				</div>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
			<button class="btn btn-primary open-dialog" type="button" url="${adminBase}/appLauncher/add.do"><@spring.message "admin.upload.picture"/></button>
		</div>
		<div class="form-inline mt5px">
			<div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" placeholder="<@spring.message "admin.endDate"/>" value="${endDate}"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${adminBase}/appLauncher/list.do',
		columns : [ {
			field : 'imgRealName',
			title : Admin.pictureTitle,
			align : 'center',
			valign : 'middle'
		}, {
			field : 'createDatetime',
			title : Admin.addDate,
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			title : Admin.pathRoad,
			align : 'center',
			valign : 'middle',
			formatter : pathFormatter
		}, {
            field : 'status',
            title : Admin.status,
            align : 'center',
            valign : 'middle',
            formatter : statusFormatter
        }, {
			title : Admin.op,
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
    function statusFormatter(value, row, index) {
        return Fui.statusFormatter({val:value,url:"${adminBase}/appLauncher/changeStatus.do?id="+row.id+"&status="});
    }
	function pathFormatter(value, row, index) {
		return '<a href="${pic_space_web_domain}'+row.imgPath+'" target="_blank">${pic_space_web_domain}'+row.imgPath+'</a>';
	}
	function operateFormatter(value, row, index) {
		return '<a class="todo-ajax" href="${adminBase}/appLauncher/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.pic.del"/>['+row.imgRealName+']？"><@spring.message "admin.menu.del"/></a>';
	}
});
</script>
