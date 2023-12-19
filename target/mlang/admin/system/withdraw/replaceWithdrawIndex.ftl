<div id="finance_depositset_con_warp_id">
    <ul class="nav nav-tabs mb10px">
        <li class="active"><a href="#" data-toggle="tab" oname="online"><@spring.message "admin.menu.replaceWithdraw"/></a></li>
    </ul>
    <form class="fui-search table-tool" method="post">
        <div class="form-group zaixianzhifu">
            <div class="form-inline">
                <div class="input-group">
                    <input type="text" class="form-control" name="name" placeholder="<@spring.message "admin.menu.replaceWithdraw.paynanme"/>">
                </div>
                <div class="input-group">
                    <select class="form-control" name="status">
                        <option value="0"><@spring.message "admin.deposit.status.all"/></option>
                        <option value="2"><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                        <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                    </select>
                </div>
                <button class="btn btn-primary"><@spring.message "manager.check.select"/></button>
                <button class="btn btn-primary open-dialog add-btn1" url="${adminBase}/replaceWithdraw/add.do"><@spring.message "admin.menu.add"/></button>
            </div>
        </div>
    </form>
    <table class="fui-default-table"></table>
</div>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/replaceWithdraw/list.do',
            columns: [{
                field: 'payName',
                title: '<@spring.message "admin.menu.replaceWithdraw.paynanme"/>',
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
                field: 'payGetway',
                title: '<@spring.message "admin.deposit.online.payment.gateway"/>',
                align: 'center',
                valign: 'middle',
                formatter: contentSizeFormatter
            }, {
                field: 'status',
                title: '<@spring.message "admin.status"/>',
                align: 'center',
                valign: 'middle',
                formatter: onlineStatusFormatter
            }, {
                field: 'remark',
                title: '<@spring.message "admin.remark"/>',
                align: 'center',
                valign: 'middle',
                formatter: remarkSizeFormatter
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
                return ['<span class="text-danger">', '</span>'].join(value);
            }
            return ['<span class="text-primary">', '</span>'].join(value);
        }

        function contentSizeFormatter(value, row, index) {
            if (value && value.length > 15) {
                return value.substring(0, 15) + "...";
            }
            return value;
        }

        function remarkSizeFormatter(value, row, index) {
            if (value && value.length > 15) {
                var v = value.substring(0, 15) + "..."
                return '<a class="open-dialog text-danger" style="color:#000000"  href="' + baseInfo.adminBaseUrl + '/replaceWithdraw/remarkModify.do?id=' + row.id + ' ">' + v + '</a>';
            } else if (value != undefined && value != "") {
                var v = value.substring(0, 15)
                return '<a class="open-dialog text-danger" style="color:#000000"  href="' + baseInfo.adminBaseUrl + '/replaceWithdraw/remarkModify.do?id=' + row.id + ' "> ' + v + '</a>';
            }
            return '<a class="open-dialog text-danger" style="color:#000000"  href="' + baseInfo.adminBaseUrl + '/replaceWithdraw/remarkModify.do?id=' + row.id + ' ">-</a>';
        }

        function onlineStatusFormatter(value, row, index) {
            return Fui.statusFormatter({
                val: value,
                url: baseInfo.adminBaseUrl + "/replaceWithdraw/updStatus.do?id=" + row.id + "&status="
            });
        }

        function defFormatter(value, row, index) {
            if (value === 2) {
                return ['<span class="text-success">', '</span>'].join("<@spring.message "admin.withdraw.info.boolean.yes"/>");
            }
            return ['<span class="text-danger">', '</span>'].join("<@spring.message "admin.withdraw.info.boolean.no"/>");
        }

        function onlineOperateFormatter(value, row, index) {
            return ['<a class="open-dialog" href="' + baseInfo.adminBaseUrl + '/replaceWithdraw/modify.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>  ',
                '<a class="todo-ajax" href="' + baseInfo.adminBaseUrl + '/replaceWithdraw/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', row.payName, '”？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>']
                .join('');
        }
    });
</script>
