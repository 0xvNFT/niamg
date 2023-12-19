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
			</div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
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
		url : '${adminBase}/appFeedback/list.do',
		columns : [ {
		    field : 'userName',
			title : Admin.feedUser,
			align : 'center',
			valign : 'middle'
		},{
            field: 'content',
            title: Admin.contentDetail,
            align: 'center',
            width: '500',
            valign: 'bottom',
            formatter: contentFormatter
        },{
            field: 'isReply',
            title : Admin.status,
            align : 'center',
            valign : 'middle',
			formatter: statusFormatter
        }, {
            field : 'createDatetime',
            title : Admin.feedDate,
            align : 'center',
            valign : 'middle',
            formatter : Fui.formatDatetime
        }, {
			title : Admin.op,
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});

    function statusFormatter(value, row, index) {
        return value == 2 ? "<@spring.message "admin.already.answer"/>" : "<@spring.message "admin.await.answer"/>";
    }

    function contentFormatter(value, row, index) {
        if (value == null || value == "") {
            return value;
        }
        if (value.length > 15) {
            return value.substr(0, 15) + '...';
        } else {
            return value;
        }
    }

	function operateFormatter(value, row, index) {
        if (row.isReply == 0) {
            return '<a class="open-dialog" href="${adminBase}/appFeedback/reply.do?id=' + row.id + '"><@spring.message "admin.answer"/></a>   ' +
                    '<a class="open-dialog" href="${adminBase}/appFeedback/view.do?id=' + row.id + '"><@spring.message "manager.info.view"/></a>   ' +
                    '<a class="todo-ajax" href="${adminBase}/appFeedback/delete.do?id=' + row.id + '" title="<@spring.message "admin.sure.del.option"/>' + '？"><@spring.message "admin.menu.del"/></a>';
        } else {
            return '<a class="open-dialog" href="${adminBase}/appFeedback/modify.do?id=' + row.id + '"><@spring.message "admin.menu.modify"/></a>   ' +
            '<a class="open-dialog" href="${adminBase}/appFeedback/view.do?id=' + row.id + '"><@spring.message "manager.info.view"/></a>   ' +
            '<a class="todo-ajax" href="${adminBase}/appFeedback/delete.do?id=' + row.id + '" title="<@spring.message "admin.sure.del.option"/>' + '？"><@spring.message "admin.menu.del"/></a>';
        }
    }
});
</script>
