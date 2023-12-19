<form class="fui-search table-tool" method="post">
	<div class="form-group">
		<div class="form-inline">
			<div class="input-group">
				<input type="text" class="form-control" name="bankName"
					placeholder="<@spring.message "admin.deposit.bank.bankCard.bankName"/>">
			</div>
			<div class="input-group">
				<select class="form-control" name="bankStatus">
					<option value="0"><@spring.message "admin.deposit.bank.bankCard.status"/></option>
					<option value="2"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
					<option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
				</select>
			</div>
			<button class="btn btn-primary"><@spring.message "admin.deposit.button.search"/></button>
			<a class="btn btn-primary open-dialog" href="${adminBase}/depositBank/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
		</div>
	</div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
        url: '${adminBase}/depositBank/list.do',
        columns: [{
        	field: 'bankName',
	        title: '<@spring.message "admin.deposit.bank.bankCard.bankName"/>',
	        align: 'center',
	        width: '140px',
	        valign: 'middle'
	    },{
	        field: 'bankCard',
	        title: '<@spring.message "admin.deposit.bank.bankCard"/>' + ' / ' + '<@spring.message "admin.deposit.bank.tronLinkAddr"/>',
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'realName',
	        title: '<@spring.message "admin.deposit.bank.bankCard.realName"/>',
	        align: 'center',
	        width: '200px',
	        valign: 'middle'
	    },{
	        field: 'bankAddress',
	        title: '<@spring.message "admin.deposit.bank.bankCard.bankAddress"/>',
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'min',
	        title: '<@spring.message "admin.deposit.bank.bankCard.min"/>',
	        align: 'center',
	        valign: 'middle',
	        formatter: moneyFormatter
	    },{
	        field: 'max',
	        title: '<@spring.message "admin.deposit.bank.bankCard.max"/>',
	        align: 'center',
	        valign: 'middle',
	        formatter: moneyFormatter
	    },{
			field: 'depositRate',
			title: '<@spring.message "admin.deposit.bank.depositRate"/>',
			align: 'center',
			valign: 'middle',
			formatter: moneyFormatter
		},{
			field: 'withdrawRate',
			title: '<@spring.message "admin.deposit.bank.withdrawRate"/>',
			align: 'center',
			valign: 'middle',
			formatter: moneyFormatter
		},{
	        field: 'sortNo',
	        title: '<@spring.message "admin.deposit.bank.bankCard.sortNo"/>',
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'status',
	        title: '<@spring.message "admin.deposit.bank.bankCard.status"/>',
	        align: 'center',
	        valign: 'middle',
	        formatter: bankStatusFormatter
	    },{
	        field: 'bgRemark',
	        title: '<@spring.message "admin.deposit.bank.bankCard.bgRemark"/>',
	        align: 'center',
	        valign: 'middle',
	        formatter: bankremarkSizeFormatter
	    },{
	        title: '<@spring.message "admin.deposit.table.lockFlag"/>',
	        align: 'center',
	        width: '200',
	        valign: 'middle',
	        formatter: bankOperateFormatter
	    }]
	});
	function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return value;
        }
        if (value > 0) {
            return '<span class="text-danger">'+value+'</span>';
        }
        return '<span class="text-primary">'+value+'</span>';
    }
    function bankremarkSizeFormatter(value, row, index) {
        if (value && value.length > 10) {
            var v = value.substring(0, 10) + "..."
            return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositBank/bankDepositremarkModify.do?id=' + row.id + ' ">' + v + '</a>';
        } else if (value != "") {
            return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositBank/bankDepositremarkModify.do?id=' + row.id + ' "> ' + value + '</a>';
        }
        return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositBank/bankDepositremarkModify.do?id=' + row.id + ' ">-</a>';
    }
    function bankStatusFormatter(value, row, index) {
        <#if permAdminFn.hadPermission("admin:depositBank:modify")>
            return Fui.statusFormatter({val: value,url: "${adminBase}/depositBank/updStatus.do?id=" + row.id + "&status="});
        <#else>
            return value == 1 ? '<span class="label label-danger"><@spring.message "admin.deposit.bank.bankCard.no"/></span>' : '<span class="label label-success"><@spring.message "admin.deposit.bank.bankCard.yes"/></span>';
        </#if>
    }
    function bankOperateFormatter(value, row, index) {
        return '<a class="open-dialog" href="${adminBase}/depositBank/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a> &nbsp;  '+
            '<a class="todo-ajax" href="${adminBase}/depositBank/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.bankName+'”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a> &nbsp;  '+
            '<a class="open-dialog" href="${adminBase}/depositBank/detail.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.detail"/>"><@spring.message "admin.deposit.bank.bankCard.detail"/></a>';
    }
});
</script>
