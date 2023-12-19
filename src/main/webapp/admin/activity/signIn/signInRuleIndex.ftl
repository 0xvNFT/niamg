<div class="form-group table-tool">
<#if permAdminFn.hadPermission("admin:signInRule:add")>
    <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/signInRule/showAdd.do"><@spring.message "admin.menu.add"/></button>
</#if>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
        url: '${adminBase}/signInRule/list.do',
        columns: [{
            field: 'days',
            title: Admin.signDays,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'score',
            title: Admin.jackScores,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'money',
            title: Admin.jackCash,
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
            	if(value){
            		if(row.betRate){
            			return value+'<br><@spring.message "admin.need.bet"/>'+ row.betRate+'<@spring.message "admin.multiple.num"/>'
            		}
            		return value;
            	}
            }
        }, {
            field: 'todayDeposit',
            title: Admin.dailyChargeLimit,
            align: 'center',
            valign: 'middle',
            formatter: depositFormatter
        }, {
            field: 'depositMoney',
            title: Admin.dailyMinCash,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'todayBet',
            title: Admin.dailyIsNotBet,
            align: 'center',
            valign: 'middle',
            formatter: depositFormatter
        }, {
            field: 'betNeed',
            title: Admin.minBetCash,
            align: 'center',
            valign: 'middle',
            formatter: betNeedFormatter
        },{
            field: 'degreeNames',
            title: Admin.vaildVipLevel,
            align: 'center',
            valign: 'middle'
        },{
            field: 'groupNames',
            title: Admin.vaildVipGroup,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'isReset',
            title: Admin.isNotReSet,
            align: 'center',
            valign: 'middle',
            formatter: depositFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });
    function betNeedFormatter(value, row, index) {
    	if (!row.betNeed) {
            return "-";
        }
        if (row.todayBet == 4) {
            return row.betNeed +"<@spring.message "admin.multiple.cash"/>";
        }
        return row.betNeed +"<@spring.message "admin.deposit.table.money.unit"/>";
    }
    function depositFormatter(value, row, index) {
        if (value == 1) {
            return '<span class="label label-info"><@spring.message "admin.withdraw.info.boolean.no"/></span>';
        } else {
            return '<span class="label label-success"><@spring.message "admin.withdraw.info.boolean.yes"/></span>';
        }
    }
    function operateFormatter(value, row, index) {
        return '<a class="open-dialog" href="${adminBase}/signInRule/showModify.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>&nbsp;&nbsp;'+
            '<a class="todo-ajax" href="${adminBase}/signInRule/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？"><i class="glyphicon glyphicon-remove"></i></a>';
    }
});
</script>
