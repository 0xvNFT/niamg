<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" id="type">
				<option value=""><@spring.message "admin.deposit.type.all"/></option>
				<option value="1"><@spring.message "manager.all.send"/></option>
				<option value="2"><@spring.message "manager.point.place"/></option>
			</select>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/announcement/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${managerBase}/announcement/list.do',
		columns : [ { 
			field : 'type',
			title : '<@spring.message "manager.notice.types"/>',
			align : 'center',
			width : '100',
			formatter : function(value, row, index) {
				switch(value){
					case 1:return "<@spring.message "manager.all.send"/>";
					case 2:return "<@spring.message "manager.point.place"/>";
				}
			}
		}, {
			field : 'startDatetime',
			title : '<@spring.message "manager.begin.time"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'endDatetime',
			title : '<@spring.message "manager.end.time"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			title : '<@spring.message "admin.withdraw.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
	function operateFormatter(value, row, index) {
		return  '<a class="todo-ajax" href="${managerBase}/announcement/delete.do?id='+row.id+'" title="<@spring.message "manager.sure.del.notice"/>？"><i class="glyphicon glyphicon-remove"></i></a>';
	}
});
</script>
