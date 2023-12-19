<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="partnerId"><option value=""><@spring.message "manager.all.operation"/></option>
			<#list partners as s><option value="${s.id}">${s.name}</option></#list>
			</select>
			<input name="username" class="form-control" type="text" placeholder="<@spring.message "manager.admin.account"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<#if permManagerFn.hadPermission("zk:partnerUser:add")><a class="btn btn-primary open-dialog" href="${managerBase}/partnerUser/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a></#if>
		</div>
	</div>
</form>

<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var partners={};<#list partners as s>partners["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/partnerUser/list.do',
		columns : [ { 
			field : 'username',
			title : '<@spring.message "manager.account.number"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.bank.bankCard.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				<#if permManagerFn.hadPermission("zk:partnerUser:status")>return Fui.statusFormatter({val:value,url:"${managerBase}/partnerUser/changeStatus.do?id="+row.id+"&status="});<#else>switch(value){case 1:return "<@spring.message "admin.deposit.bank.bankCard.status.no"/>";case 2:return "<@spring.message "admin.deposit.bank.bankCard.status.yes"/>";}</#if>
			}
		},{
			field : 'partnerId',
			title : '<@spring.message "manager.coper"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return partners[""+value];
			}
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		<#if permManagerFn.hadAnyOnePermission("zk:partnerUser:auth","zk:partnerUser:pwd","zk:partnerUser:setpwd2","zk:partnerUser:del")>}, {
			title : '<@spring.message "admin.withdraw.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter</#if>
		} ]
	});
	function operateFormatter(value, row, index) {
		var s= '';
		<#if permManagerFn.hadPermission("zk:partnerUser:auth")>if(row.original!=2){
	    		s='<a class="open-dialog" href="${managerBase}/partnerUser/showSetAuth.do?id='+row.id+'"><@spring.message "manager.perm.setting"/></a>&nbsp;&nbsp;';
	    }</#if>
		<#if permManagerFn.hadPermission("zk:partnerUser:pwd")>s=s+'<a class="open-dialog" href="${managerBase}/partnerUser/updatePwd.do?id='+row.id+'"><@spring.message "manager.password.update"/></a>&nbsp;&nbsp;';</#if>
		<#if permManagerFn.hadPermission("zk:partnerUser:setpwd2")>s=s+'<a class="open-dialog" href="${managerBase}/partnerUser/setPwd2.do?id='+row.id+'" title="<@spring.message "manager.setting.is"/>【'+row.username+'】<@spring.message "manager.second.password"/>"><@spring.message "manager.second.password"/></a>&nbsp;&nbsp;';</#if>
	    <#if permManagerFn.hadPermission("zk:partnerUser:del")>if(row.original!=2){s=s+'<a class="todo-ajax" href="${managerBase}/partnerUser/delete.do?id='+row.id+'" title="<@spring.message "manager.sure.delete.user"/>【'+row.username+'】"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';}</#if>
	    return s;
	}
});
</script>
