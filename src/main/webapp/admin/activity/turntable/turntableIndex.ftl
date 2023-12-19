<#if permAdminFn.hadPermission("admin:turntable:add")><div class="table-tool">
    <button class="btn btn-primary open-dialog cached" type="button" url="${adminBase}/turntable/showAddScore.do"><@spring.message "admin.score.set.act"/></button>
    <button class="btn btn-info open-dialog cached" type="button" url="${adminBase}/turntable/showAddMoney.do"><@spring.message "admin.charge.bet.set.act"/></button>
</div></#if>
<table class="fui-default-table"></table>
<h3><span class="label label-info"><@spring.message "admin.notice.info.round.rule"/>;</span></h3>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function(){
	Fui.addBootstrapTable({
		url: '${adminBase}/turntable/list.do',
		columns: [{
			field: 'name',
            title: Admin.actionName,
            align: 'center',
            valign: 'middle',
            formatter: ruleFormatter
        }, {
            field : 'joinType',
            title : Admin.actAttendType,
            align : 'center',
            valign : 'middle',
            formatter : joinTypeFormatter
        }, {
            field: 'score',
            title: Admin.consumeScore,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'playCount',
            title: Admin.dailyCount,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'beginDatetime',
            title: Admin.beginTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'endDatetime',
            title: Admin.endTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
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
            field: 'createDatetime',
            title: Admin.createTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
		}]
	});
	function joinTypeFormatter(value, row, index) {
        if (value == 1) {
            return "<@spring.message "admin.pay.record"/>";
        }else if(value == 2){
            return "<@spring.message "admin.bit.weight"/>";
        }else {
            return "<@spring.message "admin.scores"/>";
        }
    }
    function statusFormatter(value, row, index) {
        return Fui.statusFormatter({
            val: value,
            url: "${adminBase}/turntable/changeStatus.do?id=" + row.id + "&status="
        })
    }
	function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return '';
        }
        if (value > 0) {
            return '<span class="text-danger">'+value+'</span>';
        }
        return '<span class="text-primary">'+value+'</span>';
    }
    function ruleFormatter(value, row, index) {
        return '<a class="open-tab" id="turntable_set_rule_id" data-refresh="true" href="${adminBase}/turntable/showSetRule.do?id=' + row.id + '" title="<@spring.message "admin.menu.turnable.rule"/>">' + value + '</a>';
    }
    function operateFormatter(value, row, index) {
    	var s = '<a class="todo-ajax" href="${adminBase}/turntable/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.name+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
        if('2' == row.joinType || '1' == row.joinType) {
            return s+'&nbsp;&nbsp;<a class="open-dialog" href="${adminBase}/turntable/showModifyMoney.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>';
        } else {
            return s+'&nbsp;&nbsp;<a class="open-dialog" href="${adminBase}/turntable/showModifyScore.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>';
        }
    }
});
</script>