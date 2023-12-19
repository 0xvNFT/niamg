<form class="fui-search table-tool" method="post">
    <div class="form-group">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="payName"
                       placeholder="<@spring.message "admin.deposit.online.payName"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="status">
                    <option value="0"><@spring.message "admin.deposit.bank.bankCard.status"/></option>
                    <option value="2"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                    <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.deposit.button.search"/></button>
            <a class="btn btn-primary open-dialog"
               href="${adminBase}/depositOnline/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></a>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/depositOnline/list.do',
            columns: [{
                field: 'payName',
                title: '<@spring.message "admin.deposit.online.payName"/>',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'min',
                title: '<@spring.message "admin.deposit.money.min"/>',
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'max',
                title: '<@spring.message "admin.deposit.money.max"/>',
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'url',
                title: '<@spring.message "admin.deposit.online.url"/>',
                align: 'center',
                valign: 'middle',
                formatter: contentSizeFormatter
            }, {
                field: 'payAlias',
                title: '<@spring.message "admin.deposit.online.payAlias"/>',
                align: 'center',
                valign: 'middle',
            }, {
                field: 'bgRemark',
                title: '<@spring.message "admin.remark"/>',
                align: 'center',
                valign: 'middle',
                formatter: remarkSizeFormatter
            },
            <#--    {-->
            <#--    field: 'systemType',-->
            <#--    title: '<@spring.message "admin.deposit.online.systemType"/>',-->
            <#--    align: 'center',-->
            <#--    valign: 'middle',-->
            <#--    formatter: systemTypeFormatter-->
            <#--}, -->
                {
                field: 'status',
                title: '<@spring.message "admin.status"/>',
                align: 'center',
                valign: 'middle',
                formatter: onlineStatusFormatter
            }, {
                field: 'def',
                title: '<@spring.message "admin.deposit.online.def"/>',
                align: 'center',
                valign: 'middle',
                formatter: defFormatter
            }, {
                field: 'sortNo',
                title: '<@spring.message "admin.deposit.bank.bankCard.sortNo"/>',
                align: 'center',
                valign: 'middle'
            }, {
                title: '<@spring.message "admin.deposit.table.lockFlag"/>',
                align: 'center',
                valign: 'middle',
                formatter: onlineOperateFormatter
            }]
        });

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            if (value > 0) {
                return '<span class="text-danger">' + value + '</span>';
            }
            return '<span class="text-primary">' + value + '</span>';
        }

		function contentSizeFormatter(value, row, index) {
			if (value && value.length > 15) {
				return value.substring(0, 15) + "...";
			}
			return value;
		}

		function systemTypeFormatter(value, row, index) {
			if (value == '0') return '<@spring.message "admin.deposit.online.systemType.all"/>';
			if (value == '1') return '<@spring.message "admin.deposit.online.systemType.ios"/>';
			if (value == '2') return '<@spring.message "admin.deposit.online.systemType.android"/>';
			return '<@spring.message "admin.deposit.online.systemType.all"/>'
		}

        function remarkSizeFormatter(value, row, index) {
            if (value && value.length > 10) {
                var v = value.substring(0, 10) + "..."
                return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositOnline/onlineDepositremarkModify.do?id=' + row.id + ' ">' + v + '</a>';
            } else if (value != "") {
                return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositOnline/onlineDepositremarkModify.do?id=' + row.id + ' "> ' + value + '</a>';
            }
            return '<a class="open-dialog" style="color:#000000" href="${adminBase}/depositOnline/onlineDepositremarkModify.do?id=' + row.id + ' ">-</a>';
        }

		function defFormatter(value, row, index) {
			if (value === 2) {
				return ['<span class="text-success">', '</span>'].join('<@spring.message "admin.withdraw.info.boolean.yes"/>');
			}
			return ['<span class="text-danger">', '</span>'].join('<@spring.message "admin.withdraw.info.boolean.no"/>');
		}


        function onlineStatusFormatter(value, row, index) {
            <#if permAdminFn.hadPermission("admin:depositOnline:modify")>
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/depositOnline/updStatus.do?id=" + row.id + "&status="
            });
            <#else>
            return value == 1 ? '<span class="label label-danger"><@spring.message "admin.deposit.bank.bankCard.no"/></span>' : '<span class="label label-success"><@spring.message "admin.deposit.bank.bankCard.yes"/></span>';
            </#if>
        }

        function onlineOperateFormatter(value, row, index) {
            return '<a class="open-dialog" href="${adminBase}/depositOnline/showModify.do?id=' + row.id + '"><@spring.message "admin.deposit.bank.bankCard.modify"/></a> &nbsp;  ' +
                '<a class="todo-ajax" href="${adminBase}/depositOnline/delete.do?id=' + row.id + '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“' + row.payName + '”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a> &nbsp;  ' +
                '<a class="open-dialog" href="${adminBase}/depositOnline/detail.do?id=' + row.id + '" title="<@spring.message "admin.deposit.bank.bankCard.detail"/>"><@spring.message "admin.deposit.bank.bankCard.detail"/></a>';
        }
    });
</script>
