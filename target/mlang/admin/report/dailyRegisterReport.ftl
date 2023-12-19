<form class="fui-search table-tool" method="post" id="report_dailyregister_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startDate" value="${endDate}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" placeholder="<@spring.message "admin.proxy.account.det"/>" style="width:150px;">
            </div>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
             <#if permAdminFn.hadPermission("admin:dailyRegisterReport:export")>
                <a class="btn btn-primary exportBtn" href="#" title="<@spring.message "admin.sure.export"/>？"><@spring.message "admin.deposit.button.export"/></a>
             </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endDate" class="form-control fui-date" value="${endDate}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="agentName" placeholder="<@spring.message "admin.merchant.account"/>" style="width:150px;">
            </div>
        </div>
    </div>
</form>
<#if permAdminFn.hadPermission("admin:dailyRegisterReport:export")><div class="hidden">
    <form id="report_dailyregister_form_export_fmId" action="${adminBase}/dailyRegisterReport/export.do" target="_blank" method="post">
        <input type="hidden" name="startDate"/>
        <input type="hidden" name="endDate"/>
        <input type="hidden" name="agentName"/>
        <input type="hidden" name="proxyName"/>
    </form>
</div></#if>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#report_dailyregister_form_id");
        <#if permAdminFn.hadPermission("admin:dailyRegisterReport:export")>$form.find(".exportBtn").click(function () {
            var $form1 = $("#report_dailyregister_form_export_fmId");
            $form1.find("[name='startDate']").val($form.find("[name='startDate']").val());
            $form1.find("[name='endDate']").val($form.find("[name='endDate']").val());
            $form1.find("[name='agentName']").val($form.find("[name='agentName']").val());
            $form1.find("[name='proxyName']").val($form.find("[name='proxyName']").val());
            $form1.submit();
        });</#if>
        Fui.addBootstrapTable({
            url: '${adminBase}/dailyRegisterReport/list.do',
            showPageSummary: true,
            showFooter: true,
            columns: [{
                field: 'statDate',
                title: Admin.dayDate,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDate,
                pageSummaryFormat: function (rows) {
                    return Admin.total;
                }
            }, {
                field: 'registerNum',
                title: Admin.registAllPer,
                align: 'center',
                valign: 'middle',
                formatter: registerNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'registerNum');
                }
            }, {
                field: 'memberNum',
                title: Admin.regVipPerNum,
                align: 'center',
                valign: 'middle',
                formatter: registerNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'memberNum');
                }
            }, {
                field: 'proxyNum',
                title: Admin.regProxyNum,
                align: 'center',
                valign: 'middle',
                formatter: registerNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'proxyNum');
                }
            }, {
                field: 'firstDeposit',
                title: Admin.firstPayValNum,
                align: 'center',
                valign: 'middle',
                formatter: firstDepositNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'firstDeposit');
                }
            },{
                field: 'secDeposit',
                title: Admin.secPayPerNum,
                align: 'center',
                valign: 'middle',
                formatter: secDepositNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'secDeposit');
                }
            },{
                field: 'thirdDeposit',
                title: Admin.thirdPayPerNum,
                align: 'center',
                valign: 'middle',
                formatter: thirdDepositNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'thirdDeposit');
                }
            },{
                field: 'deposit',
                title: Admin.dailyRegPayCount,
                align: 'center',
                valign: 'middle',
                formatter: depositNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal1(rows, 'deposit');
                }
            },{
                field: 'depositMoney',
                title: Admin.dailyRegPayCash,
                align: 'center',
                valign: 'middle',
                formatter: depositNumFormatter,
                pageSummaryFormat: function (rows) {
                    return getTotal(rows, 'depositMoney');
                }
            }]
            , onLoadSuccess: function (d, r) {
                if (r && r.msg) {
                    layer.msg(r.msg);
                }
                $form.find(".search-btn").prop("disabled", false).html('<@spring.message "admin.deposit.button.search"/>');
                return false;
            }
            , queryParams: function (params) {
                $form.find(".search-btn").prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                return params;
            }
        });
        function aggsMoneyFormatter(value) {
            if (value && value > 0) {
                return '<span class="text-danger">'+value+'</span>';
            }
            return '<span class="text-primary">'+value+'</span>';
        }
        function getTotal1(rows, itemKey) {
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                var r = rows[i];
                if (!r[itemKey]) {
                    continue;
                }
                total += r[itemKey];
            }
            return aggsMoneyFormatter(total);
        }
        function getTotal(rows, itemKey) {
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                var r = rows[i];
                if (!r[itemKey]) {
                    continue;
                }
                total += r[itemKey];
            }
            return aggsMoneyFormatter(total.toFixed(2));
        }
        function registerNumFormatter(value, row, index) {
            return '<a class="open-tab" href="${adminBase}/user/index.do?queryDate=' + row.statDate + '&proxyName={[name=\'proxyName\']:}">' + value + '</a>'
        }

        function firstDepositNumFormatter(value, row, index) {
            return '<a class="open-tab" title="<@spring.message "admin.website.pay.record"/>" href="${adminBase}/finance/deposit/index.do?startTime=' +
                    row.statDate + ' 00:00:00&endTime=' + row.statDate + ' 23:59:59&depositNum=1&proxyName={[name=\'proxyName\']:}">' + value + '</a>';
        }
        function secDepositNumFormatter(value, row, index) {
            return '<a class="open-tab" title="<@spring.message "admin.website.pay.record"/>" href="${adminBase}/finance/deposit/index.do?startTime=' +
                    row.statDate + ' 00:00:00&endTime=' + row.statDate + ' 23:59:59&depositNum=2&proxyName={[name=\'proxyName\']:}">' + value + '</a>';
        }
        function thirdDepositNumFormatter(value, row, index) {
            if (!value || value == undefined) {
                value = 0;
            }
            return '<a class="open-tab" title="<@spring.message "admin.website.pay.record"/>" href="${adminBase}/finance/deposit/index.do?startTime=' +
                row.statDate + ' 00:00:00&endTime=' + row.statDate + ' 23:59:59&depositNum=3&proxyName={[name=\'proxyName\']:}">' + value + '</a>';
        }
        function depositNumFormatter(value, row, index) {
            return '<a class="open-tab" title="<@spring.message "admin.website.pay.record"/>" href="${adminBase}/finance/deposit/index.do?startTime=' +
                    row.statDate + ' 00:00:00&endTime=' + row.statDate + ' 23:59:59&registerTime='+ row.statDate +' 00:00:00&registerTimeEnd='+ row.statDate +' 23:59:59&proxyName={[name=\'proxyName\']:}">' + value + '</a>';
        }
    });
    </script>
