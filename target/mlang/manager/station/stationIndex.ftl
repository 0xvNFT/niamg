<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group"><select class="form-control"name="partnerId">
				<option value=""><@spring.message "manager.all.coperation"/></option>
				<#list partners as s><option value="${s.id}">${s.name}</option></#list>
			</select></div>
	        <input class="form-control" name="name" type="text" placeholder="<@spring.message "manager.station.name"/>">
	        <input class="form-control" name="code" type="text" placeholder="<@spring.message "admin.money.history.id"/>">
			<button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/station/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var partners={},langs={},bzs={};<#list partners as s>partners["${s.id}"]="${s.name}";</#list>
<#list languages as s>langs["${s.name()}"]="${s.desc}";</#list>
<#list currencies as s>bzs["${s.name()}"]="${s.desc}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/station/list.do',
		columns : [{
			field : 'partnerId',
			title : '<@spring.message "manager.coper"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(!value){return "";}
				return partners[""+value];
			}
		},{
			field : 'name',
			title : '<@spring.message "manager.station.name"/>',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'code',
			title : '<@spring.message "admin.money.history.id"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'currency',
			title : '<@spring.message "manager.money.types"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return bzs[value];
			}
		},{
			field : 'language',
			title : '<@spring.message "manager.language.types"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return langs[value];
			}
		}, {
			title : '<@spring.message "admin.deposit.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value, row, index) {
				return '<a class="open-dialog" href="${managerBase}/station/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>'+
					'&nbsp;<a class="open-dialog" href="${managerBase}/station/showModifyCode.do?id='+row.id+'">修改编号</a>';
			}
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.bank.bankCard.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/station/changeStatus.do?id="+row.id+"&status="});
			}
		},{
			field : 'bgStatus',
			title : '<@spring.message "manager.back.admin.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/station/changeBgStatus.do?id="+row.id+"&status="});
			}
		}, {
			field : 'createDatetime',
			title : '<@spring.message "manager.datetime.create"/>',
			align : 'center',
			valign : 'middle',
			formatter : Fui.formatDatetime
		} ]
	});
});
</script>
