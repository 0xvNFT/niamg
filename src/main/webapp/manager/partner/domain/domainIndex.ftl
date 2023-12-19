<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group"><select class="form-control" name="partnerId">
				<option value=""><@spring.message "manager.all.coperation"/></option>
				<#list partners as s><option value="${s.id}">${s.name}</option></#list>
			</select></div>
	        <input class="form-control" name="name" type="text" placeholder="<@spring.message "manager.domain.name"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/partnerDomain/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var partners={};<#list partners as s>partners["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/partnerDomain/list.do',
		columns : [ { 
			field : 'partnerId',
			title : '<@spring.message "manager.coper"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return partners[''+value];
			}
		}, {
			field : 'name',
			title : '<@spring.message "manager.domain"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.table.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/partnerDomain/changeStatus.do?id="+row.id+"&status="});
			}
		}, {
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		}, {
			field : 'ipMode',
			title : '<@spring.message "manager.get.ip.model"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value == 2){
					return '<span class="label label-success"><@spring.message "manager.common.pattern"/></span>';
				}else {
					return '<span class="label label-primary"><@spring.message "manager.safe.pattern"/></span>';
				}
			}
		},{
            field : 'remark',
            title : '<@spring.message "admin.withdraw.remark"/>',
            align : 'center',
            valign : 'middle'
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		
		} ]
	});
	function operateFormatter(value, row, index) {
		return  '<a class="open-dialog" href="${managerBase}/partnerDomain/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>'+
			'&nbsp;&nbsp;&nbsp;<a class="todo-ajax" href="${managerBase}/partnerDomain/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.name+'”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
	}
});
</script>
