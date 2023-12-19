<#if permAdminFn.hadPermission("admin:drawRuleStrategy:add")><div class="table-tool">
	<button class="btn btn-primary open-dialog" url="${adminBase}/drawFeeStrategy/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></button>
</div></#if>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
        url: "${adminBase}/drawFeeStrategy/list.do",
        columns: [{
            field: 'feeType',
            title: Admin.drawFeeType,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        }, {
            field: 'feeValue',
            title: Admin.handFeeValue,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                if (row.feeType == 1) {
                    return '<span class="label label-warning">'+value+'<@spring.message "admin.deposit.table.money.unit"/></span>';
                }else {
                	return '<span class="label label-danger">'+value+'%</span>'; ;
                }
            }
        }, {
            field: 'drawNum',
            title: Admin.dailyFreeDraw,
            align: 'center',
            valign: 'middle'
        },{
            field: 'upperLimit',
            title: Admin.drawFeeTop,
            align: 'center',
            valign: 'middle'
        },{
            field: 'lowerLimit',
            title: Admin.drawFeeDown,
            align: 'center',
            valign: 'middle'
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
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: statusFormatter
        }, {
            field: 'remark',
            title: Admin.remark,
            align: 'center',
            valign: 'middle',
            formatter: remarkFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });

    function operateFormatter(value, row, index) {
        var s='';
        <#if permAdminFn.hadPermission("admin:drawFeeStrategy:modify")>
        s=s+'<a class="open-dialog" href="${adminBase}/drawFeeStrategy/showModify.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>';
        </#if>
        <#if permAdminFn.hadPermission("admin:drawFeeStrategy:delete")>
        s=s+'&nbsp;&nbsp<a class="todo-ajax" href="${adminBase}/drawFeeStrategy/delete.do?id='+ row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？"><i class="glyphicon glyphicon-remove"></i></a>';
        </#if>
        return s;
    }
    function statusFormatter(value, row, index) {
        return Fui.statusFormatter({
            val: value,
            url: "${adminBase}/drawFeeStrategy/changeStatus.do?id=" + row.id + "&status="
        });
    }
    function remarkFormatter(value, row, index) {
        if (!value) {
            return "";
        }
        if (value.length < 20) {
            return value;
        }
        return '<a class="open-text" dialog-text="' + value + '" dialog-title="<@spring.message "admin.money.history.remark.detail"/>" title="' + value + '">'+value.substr(0, 20) + "..."+'</span>';
    }
    function typeFormatter(value, row, index) {
        if (value == 1) {
            return '<span class="label label-primary"><@spring.message "admin.draw.fee.fixed"/></span>';
        } else if (value == 2) {
            return '<span class="label label-success"><@spring.message "admin.draw.fee.float"/></span>';
        }
    }
});
</script>

