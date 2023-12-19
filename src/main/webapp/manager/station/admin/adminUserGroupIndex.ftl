<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/adminUserGroup/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations=[];
<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/adminUserGroup/list.do',
		columns : [ { 
			field : 'stationId',
			title : '<@spring.message "manager.station.name"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return stations[value+''];
			}
		}, {
			field : 'name',
			title : '<@spring.message "manager.blank.user"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'type',
			title : '<@spring.message "admin.deposit.table.depositType"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				switch(value){
					case 1:return '<span class="label label-default"><@spring.message "manager.all.oper.can"/></span>';
					case 2:return '<span class="label label-primary"><@spring.message "manager.api.can.oper"/></span>';
					case 3:return '<span class="label label-success"><@spring.message "manager.coper.marchant.update.perm"/></span>';
					case 4:return '<span class="label label-info"><@spring.message "manager.cent.only.look"/></span>';
					case 5:return '<span class="label label-warning"><@spring.message "manager.update.perm.can"/></span>';
				}
				return "";
			}
		}, {
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
	function operateFormatter(value, row, index) {
		var s= '';
		<#if permManagerFn.hadPermission("zk:adminUserGroup:auth")>s='<a class="open-dialog" href="${managerBase}/adminUserGroup/showAddAuth.do?id='+row.id+'&stationId='+row.stationId+'"><@spring.message "manager.perm.setting"/></a>&nbsp;&nbsp;';</#if>
		<#if permManagerFn.hadPermission("zk:adminUserGroup:update")>s=s+'<a class="open-dialog" href="${managerBase}/adminUserGroup/showModify.do?id='+row.id+'&stationId='+row.stationId+'"><i class="glyphicon glyphicon-pencil"></i></a>&nbsp;&nbsp;';</#if>
	    <#if permManagerFn.hadPermission("zk:adminUserGroup:del")>s=s+'<a class="todo-ajax" href="${managerBase}/adminUserGroup/delete.do?id='+row.id+'&stationId='+row.stationId+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.name+'”？"><i class="glyphicon glyphicon-remove"></i></a>';</#if>
		return s;
	}
});
</script>
