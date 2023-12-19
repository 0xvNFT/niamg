<form class="fui-search table-tool" method="post">
	<div class="form-group fui-data-wrap">
		<div class="form-inline">
			<div class="input-group"><select class="form-control" name="stationId"><option value=""><@spring.message "manager.all.station"/></option>
			<#list stations as s><option value="${s.id}">${s.name}</option></#list>
			</select></div>
	        <input class="form-control" name="name" type="text" placeholder="<@spring.message "manager.domain.name"/>">
	        <input class="form-control" name="proxyUsername" type="text" placeholder="<@spring.message "manager.proxy.name"/>">
	        <div class="input-group"><select class="form-control" name="type"><option value=""><@spring.message "manager.all.types"/></option>
				<option value="1"><@spring.message "manager.common.use"/></option>
                <option value="2"><@spring.message "manager.station.admin.back"/></option>
                <option value="3"><@spring.message "manager.proxy.per.admin"/></option>
                <option value="4"><@spring.message "manager.font"/></option>
                <option value="5"><@spring.message "manager.app.domain"/></option>
			</select></div>
			<button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${managerBase}/stationDomain/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var stations={};<#list stations as s>stations["${s.id}"]="${s.name}";</#list>
	Fui.addBootstrapTable({
		url : '${managerBase}/stationDomain/list.do',
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
			title : '<@spring.message "manager.domain"/>',
			align : 'center',
			valign : 'middle'
		},{
			field : 'status',
			title : '<@spring.message "admin.deposit.bank.bankCard.status"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				return Fui.statusFormatter({val:value,url:"${managerBase}/stationDomain/changeStatus.do?id="+row.id+"&status="});
			}
		}, {
			title : '<@spring.message "admin.withdraw.table.lockFlag"/>',
			align : 'center',
			valign : 'middle',
			formatter : operateFormatter
		}, {
			field : 'type',
			title : '<@spring.message "admin.deposit.table.depositType"/>',
			align : 'center',
			valign : 'middle',
			formatter : function(value,row,index){
				if(value == 5){
					return '<span class="label label-success"><@spring.message "manager.app.domain"/></span>';
				} else if(value == 4){
					return '<span class="label label-success"><@spring.message "manager.font.domain"/></span>';
				} else if(value == 2){
					return '<span class="label label-danger"><@spring.message "manager.back.domain"/></span>';
				} else if(value == 3){
					return '<span class="label label-info"><@spring.message "manager.proxy.back.domain"/></span>';
				}else {
					return '<span class="label label-primary"><@spring.message "manager.common.domain.use"/></span>';
				}
			}
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
		return  '<a class="open-dialog" href="${managerBase}/stationDomain/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>'+
			'&nbsp;&nbsp;&nbsp;<a class="todo-ajax" href="${managerBase}/stationDomain/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.name+'”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
	}
});
</script>
