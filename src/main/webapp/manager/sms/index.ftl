<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={};<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/sysGatewayConfig/list.do',
		columns : [ {
			field : 'country',
			title : '国家',
			align : 'center',
			valign : 'middle',
		},{
			field : 'language',
			title : '语种代号',
			align : 'center',
			valign : 'middle',
		}, {
			field : 'appKey',
			title : 'appKey',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'appSecret',
			title : 'appSecret',
			align : 'center',
			valign : 'middle'
		}, {
			field : 'appCode',
			title : 'appCode',
			align : 'center',
			valign : 'middle'
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.bank.bankCard.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/sysGatewayConfig/updStatus.do?id="+row.id+"&status="});
			}
		},{
			field : 'content',
			title : '短信内容模板',
			align : 'center',
			valign : 'middle'
		},{
			field : 'gatewayUrl',
			title : '网关地址',
			align : 'center',
			valign : 'middle'
		},{
			field : 'account',
			title : '后台登录帐号',
			align : 'center',
			valign : 'middle'
		},{
			field : 'password',
			title : '后台登录密码',
			align : 'center',
			valign : 'middle'
		}, {
			title : '<@spring.message "admin.withdraw.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		} ]
	});
	function operateFormatter(value, row, index) {
		return  '<a class="open-dialog" href="${managerBase}/sysGatewayConfig/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>';
	}
});
</script>
