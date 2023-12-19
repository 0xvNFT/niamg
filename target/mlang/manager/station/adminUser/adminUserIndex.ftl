<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select>
	        <input class="form-control" name="username" type="text" placeholder="<@spring.message "manager.account.number"/>">
	        <select class="form-control" name="type"><option value=""><@spring.message "manager.all.types"/></option>
				<#list types as s><option value="${s.type}">${s.desc}</option></#list>
			</select>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/adminUser/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={},groups={},types={};
<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
<#list groups as s>groups["${s.id}"]="${s.name}";</#list>
<#list types as s>types["${s.type}"]="${s.desc}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/adminUser/list.do',
		columns : [ { 
			field : 'stationId',
			title : '<@spring.message "manager.station.name"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return stations[value+''];
			}
		}, {
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
				return Fui.statusFormatter({val:value,url:"${managerBase}/adminUser/changeStatus.do?id="+row.id+"&stationId="+row.stationId+"&status="});
			}
		}, {
			title : '<@spring.message "admin.withdraw.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				var s='';
				if(row.original!=2){
					s='<a class="open-dialog" href="${managerBase}/adminUser/showModify.do?id='+row.id+'&stationId='+row.stationId+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>&nbsp;&nbsp;';
				}
				return s+'<a class="open-dialog" href="${managerBase}/adminUser/showUpPwd.do?id='+row.id+'&stationId='+row.stationId+'"><@spring.message "manager.password.update"/></a>';
			}
		}, {
			field : 'type',
			title : '<@spring.message "manager.user.types"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return types[value+''];
			}
		}, {
			field : 'groupName',
			title : '<@spring.message "manager.group.types"/>',
			align : 'center',
			valign : 'middle'
		
		}, {
			field : 'lastLoginIp',
			title : '<@spring.message "manager.logout.name.ip"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'lastLoginIpStr',
			title : '<@spring.message "manager.logout.address"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		}, {
			field : 'lastLoginTime',
			title : '<@spring.message "manager.last.logout.time"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		} ]
	});
});
</script>
