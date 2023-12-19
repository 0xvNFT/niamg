<#if permManagerFn.hadPermission("zk:user:add")><div class="form-group table-tool">
	<a class="btn btn-primary open-dialog" href="${managerBase}/user/add.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
</div></#if>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${managerBase}/user/list.do',
		columns : [ { 
			field : 'username',
			title : '<@spring.message "manager.account.number"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.table.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				<#if permManagerFn.hadPermission("zk:user:auth")>return Fui.statusFormatter({val:value,url:"${managerBase}/user/changeStatus.do?id="+row.id+"&status="});
				<#else>switch(value){case 1:return "<@spring.message "admin.deposit.bank.bankCard.status.no"/>";case 2:return "<@spring.message "admin.deposit.bank.bankCard.status.yes"/>";}</#if>
			}
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		<#if permManagerFn.hadAnyOnePermission("zk:user:auth","zk:user:reset:pwd","zk:user:delete","zk:user:setpwd2")>}, {
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter</#if>
		} ]
	});
	function operateFormatter(value, row, index) {
		var s= '';
		<#if permManagerFn.hadPermission("zk:user:auth")>if(row.type!=1){
	    		s='<a class="open-dialog" href="${managerBase}/user/showSetAuth.do?id='+row.id+'"><@spring.message "manager.perm.setting"/></a>&nbsp;&nbsp;';
	    }</#if>
		<#if permManagerFn.hadPermission("zk:user:reset:pwd")>s=s+'<a class="todo-ajax" href="${managerBase}/user/resetPwd.do?id='+row.id+'" title="<@spring.message "manager.resetting.user"/>【'+row.username+'】<@spring.message "manager.init.password.is"/>"><@spring.message "manager.restting.password"/></a>&nbsp;&nbsp;';</#if>
		<#if permManagerFn.hadPermission("zk:user:setpwd2")>s=s+'<a class="open-dialog" href="${managerBase}/user/setPwd2.do?id='+row.id+'" title="<@spring.message "manager.setting.is"/>【'+row.username+'】<@spring.message "manager.second.password"/>"><@spring.message "manager.second.password"/></a>&nbsp;&nbsp;';</#if>
	    <#if permManagerFn.hadPermission("zk:user:delete")>if(row.type!=1){s=s+'<a class="todo-ajax" href="${managerBase}/user/delete.do?id='+row.id+'" title="<@spring.message "manager.sure.delete.user"/>【'+row.username+'】"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';}</#if>
	    return s;
	}
});
</script>
