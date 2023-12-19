<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="form-group">
				<div class="input-group">
					<input type="text" class="form-control fui-date" name="startTime" placeholder="<@spring.message "admin.startDate"/>" value="${startDate}"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
				</div>
				<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
				<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
				<div class="input-group">
					<#--<input type="text" class="form-control" name="username" placeholder="所属用户" value="${user.username}">-->
				</div>
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
			<button class="btn btn-primary open-dialog" type="button" url="${adminBase}/appDomain/add.do"><@spring.message "admin.add"/></button>
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
		url : '${adminBase}/appDomain/list.do',
		columns : [ {
		    field : 'domainUrl',
			title : Admin.appDomain,
			align : 'center',
			valign : 'middle'
		}, {
            field : 'hasVertify',
            title : Admin.isNotCertificate,
            align : 'center',
            valign : 'middle',
            formatter : vertifyFormatter
        }, {
            field : 'createDatetime',
            title : Admin.addDate,
            align : 'center',
            valign : 'middle',
            formatter : Fui.formatDatetime
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
        return Fui.statusFormatter({val:value,url:"${adminBase}/appDomain/changeStatus.do?id="+row.id+"&status="});
    }
	//
	// function closeDialog(){
	// 	console.log('closedialog --- ');
	// 	var $table=$(".fui-box.active").data("bootstrapTable");
	// 	if($table && $table.length){
	// 		console.log('closedialog 222--- ');
	// 		$table.bootstrapTable('refresh');
	// 	}
	// }

    function vertifyFormatter(value, row, index) {
        if (row.hasVertify == 2){
            return "<@spring.message "admin.have.certificate"/>";
		}
		return "<@spring.message "admin.have.not.certificate"/>";
    }

	function operateFormatter(value, row, index) {
        return  '<a class="open-dialog" href="${adminBase}/appDomain/modify.do?id='+row.id+'"><@spring.message "admin.menu.modify"/></a>   '+
				'<a class="todo-ajax" href="${adminBase}/appDomain/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.del.domain"/>['+row.domainUrl+']？"><@spring.message "admin.menu.del"/></a>';
	}
});
</script>
