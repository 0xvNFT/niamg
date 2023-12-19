<#if permManagerFn.hadPermission("zk:partner:add")><div class="form-group table-tool">
	<a class="btn btn-primary open-dialog" href="${managerBase}/partner/add.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
</div></#if>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
	Fui.addBootstrapTable({
		url : '${managerBase}/partner/list.do',
		columns : [ { 
			field : 'name',
			title : '<@spring.message "manager.blank.user"/>',
			align : 'center',
			valign : 'middle'
		 },{
            field: 'remark',
            title: '<@spring.message "manager.oper.rec"/>',
            align: 'center',
            valign: 'middle',
            formatter: contentFormatter
          <#if permManagerFn.hadPermission("zk:partner:modify")>},{
            title: '<@spring.message "admin.withdraw.table.lockFlag"/>',
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index){
            	return '<a class="open-dialog" href="${managerBase}/partner/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>';
            }
          </#if>
		}]
	});
	function contentFormatter(value, row, index) {
        value = value.replace(/[<>&"]/g, function (c) {
            return {'<': '&lt;', '>': '&gt;', '&': '&amp;', '"': '&quot;'}[c];
        });
        if (!value) {
            return "-";
        }
        if (value.length > 20) {
            return '<a href="javascript:void(0)" class="open-text" dialog-text="' + value + '">' + value.substr(0, 20) + '...</a>';
        } else {
            return value;
        }
    }
});
</script>
