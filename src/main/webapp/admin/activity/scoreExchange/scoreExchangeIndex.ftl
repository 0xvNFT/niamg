<form class="fui-search table-tool" method="post">
    <div class="form-group">
        <div class="form-inline">
            <div class="input-group">
                <select class="form-control" name="type">
                    <option value="" selected><@spring.message "admin.deposit.type.all"/></option>
                    <option value="1"><@spring.message "admin.cash.exchange.scores"/></option>
                    <option value="2"><@spring.message "admin.score.exchange.cash"/></option>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="name" placeholder="<@spring.message "admin.exchange.config.name"/>">
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
        <#if permAdminFn.hadPermission("admin:scoreExchange:add")>
            <button class="btn btn-primary open-dialog cached" url="${adminBase}/scoreExchange/showAdd.do"><@spring.message "admin.add"/></button>
        </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery'], function () {
    Fui.addBootstrapTable({
        url: '${adminBase}/scoreExchange/list.do',
        columns: [{
            field: 'name',
            title: Admin.exchangeConfig,
            align: 'center',
            valign: 'middle'
        },{
            field: 'type',
            title: Admin.payTypes,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        },{
            title: Admin.exchangeScale,
            align: 'center',
            valign: 'middle',
            formatter: ratioFormatter
        },{
            field: 'minVal',
            title: Admin.singleMinVal,
            align: 'center',
            valign: 'middle'
        },{
            field: 'maxVal',
            title: Admin.singleMaxVal,
            align: 'center',
            valign: 'middle'
        },{
            field: 'status',
            title: Admin.stationStatus,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
        },{
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });
    function typeFormatter(value, row, index) {
        if (value == 1) {
            return "<span class='text-primary'><@spring.message "admin.cash.exchange.scores"/></span>";
        } else if (value == 2) {
            return "<span class='text-primary'><@spring.message "admin.score.exchange.cash"/></span>";
        }
        return value;
    }
    function ratioFormatter(value, row, index) {
        return row.numerator + ":" + row.denominator;
    }
    function statusFormatter(value, row, index) {
        return Fui.statusFormatter({
            val: value,
            url: "${adminBase}/scoreExchange/changeStatus.do?id=" + row.id + "&status="
        });
    }
    function operateFormatter(value, row, index) {
        return '<a class="open-dialog" href="${adminBase}/scoreExchange/showModify.do?id='+row.id+'"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>'+
            '&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/scoreExchange/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.name+'”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
    }
});
</script>