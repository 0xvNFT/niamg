<div class="form-group table-tool">
	<a class="btn btn-primary open-dialog" href="${managerBase}/cache/deleteCache.do"><@spring.message "manager.cache.del"/></a>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		sidePagination: 'client',
		pagination:false,
		url : "${managerBase}/cache/list.do",
		columns : [{
			field : 'name',
			title : '<@spring.message "manager.cache.name"/>',
			align : 'center',
			valign : 'middle'
		}, {
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return  '<a class="open-dialog" href="${managerBase}/cache/viewDb.do?db='+row.db+'"><@spring.message "manager.info.view"/></a>'
						+'&nbsp;&nbsp;<a class="todo-ajax" href="${managerBase}/cache/flushCache.do?name='+row.key+'&db='+row.db+'" title="<@spring.message "manager.sure.cache.clean"/>“'+row.name+'”<@spring.message "manager.cache.que"/>？"><@spring.message "manager.cache.clean"/></a>';
			}
		},{
			field : 'db',
			title : '<@spring.message "manager.database.name"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'timeout',
			title : '<@spring.message "manager.second.pass"/>',
			align : 'center',
			valign : 'middle'
		}]
	});
});
</script>