<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 00:00:00" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.deposit.handle.username"/>">
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 23:59:59" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
        url: '${adminBase}/redPacketRecord/list.do',
        columns: [{
            field: 'packetId',
            title: Admin.redPacCode,
            align: 'center',
            valign: 'middle'
        },{
            field: 'username',
            title: Admin.userName,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'packetName',
            title: Admin.redPacTitle,
            align: 'center',
            valign: 'middle',
            pageSummaryFormat: function (rows, aggsData) {
                return "<@spring.message "admin.deposit.table.page.sum"/>:";
            },
            allSummaryFormat: function (rows, aggsData) {
                return "<@spring.message "admin.deposit.table.all.sum"/>:";
            }
        }, {
            field: 'money',
            title: Admin.redCashVal,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return getTotal(rows, 'money');
            },
            allSummaryFormat: function (rows, aggsData) {
                if (!aggsData) {
                    return "0.00"
                }
                return aggsData.totalMoney ? aggsData.totalMoney.toFixed(2) : "0.00";
            }
        }, {
            field: 'createDatetime',
            title: Admin.createTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'ip',
            title: 'IP',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: recordStatusFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: recordOpFormatter
        }]
    });
    function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return '';
        }
        if (value > 0) {
            return '<span class="text-danger">'+value+'</span>';
        }
        return '<span class="text-primary">'+value+'</span>';
    }
    function recordStatusFormatter(value, row, index) {
        if (value === 1) {
            return '<span class="text-success"><@spring.message "admin.deposit.status.ing"/></span>';
        } else if (value === 2) {
            return '<span class="text-primary"><@spring.message "admin.handle.process.success"/></span>';
        }
        return '<span class="text-danger"><@spring.message "admin.handle.process.fail"/></span>';
    }
     function recordOpFormatter(value, row, index) {
        if (row.status != 1) {
            return "<@spring.message "admin.have.processed"/>";
        }
        return '<a class="open-dialog" href="${adminBase}/redPacketRecord/showHandle.do?id=' + row.id + '" title="<@spring.message "admin.process"/>"><@spring.message "admin.process"/></a>';
    }
});
</script>
