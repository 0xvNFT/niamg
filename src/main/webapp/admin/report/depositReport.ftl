<form class="fui-search table-tool" method="post" id="deposit_report_total_statistic_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startDate" value="${curDate}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
            <button class="btn btn-primary reset" type="button"><@spring.message "manager.resetting"/></button>
            <#if permAdminFn.hadPermission("admin:depositReport:export")>
                <button class="btn btn-primary exportBtn" type="button"><@spring.message "admin.deposit.button.export"/></button>
            </#if>
            <select class="form-control" name="depositType">
                <option value=""> <@spring.message "admin.charge.types"/></option>
                <option value="1"><@spring.message "admin.deposit.type.online"/></option>
                <option value="2"><@spring.message "admin.deposit.type.fast"/></option>
                <option value="3"><@spring.message "admin.deposit.type.bank"/></option>
                <option value="4"><@spring.message "admin.deposit.type.manual"/></option>
            </select>

        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endDate" class="form-control fui-date" value="${curDate}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="payName" placeholder="<@spring.message "admin.deposit.online.payName"/>">
            </div>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<#if permAdminFn.hadPermission("admin:depositReport:export")><div class="hidden">
    <form id="deposit_report_rd_export_fmId" action="${adminBase}/depositReport/export.do" target="_blank" method="post">
        <input type="hidden" name="username"/>
        <input type="hidden" name="proxyName"/>
        <input type="hidden" name="startDate"/>
        <input type="hidden" name="endDate"/>
        <input type="hidden" name="depositType"/>
    </form>
</div></#if>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#deposit_report_total_statistic_form_id");
        <#if permAdminFn.hadPermission("admin:depositReport:export")>$form.find(".exportBtn").click(function () {
            var $form1 = $("#deposit_report_rd_export_fmId");
            $form1.find("[name='payName']").val($form.find("[name='payName']").val());
            $form1.find("[name='startDate']").val($form.find("[name='startDate']").val());
            $form1.find("[name='endDate']").val($form.find("[name='endDate']").val());
            $form1.find("[name='depositType']").val($form.find("[name='depositType']").val());
            $form1.submit();
        });</#if>
        Fui.addBootstrapTable({
            url: '${adminBase}/depositReport/list.do',
            showPageSummary: true,
            showAllSummary: true,
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
                field: 'payName',
                title: Admin.payName,
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.page.sum"/>:";
                },
                allSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.all.sum"/>:";
                }
            }, {
                    field: 'depositType',
                    title: Admin.payTypes,
                    align: 'center',
                    valign: 'middle',
                formatter: function (value) {
                    switch (value) {
                        case 1: return  "<@spring.message "admin.deposit.type.online"/>"
                        case 2: return  "<@spring.message "admin.deposit.type.fast"/>"
                        case 3: return  "<@spring.message "admin.deposit.type.bank"/>"
                        case 4: return  "<@spring.message "admin.deposit.type.manual"/>"
                    }
                }

                }
                , {
                field: 'depositTimes',
                title: Admin.chargeCount,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: numberFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositTimes');
                }
            }, {
                field: 'successTimes',
                title: Admin.chargeSuccCount,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: numberFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'successTimes');
                }
            }, {
                field: 'depositUser',
                title: Admin.submitPayPer,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: numberFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositUser');
                }
            }, {
                field: 'depositAmount',
                title: Admin.submitPayMoney,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsMoneyFormatter(aggsData.totalMoney ? aggsData.totalMoney.toFixed(2) : "0.00");
                }
            }, {
                field: 'successAmount',
                title: Admin.paySuccMoney,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'successAmount');
                }, allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsMoneyFormatter(aggsData.successMoney ? aggsData.successMoney.toFixed(2) : "0.00");
                }
            }, {
                field: 'minMoney',
                title: Admin.minChargeMoney,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'minMoney');
                }
            }, {
                field: 'maxMoney',
                title: Admin.maxChargeMoney,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'maxMoney');
                }
            }, {
                field: 'failedTimes',
                title: Admin.chargeFailNum,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: numberFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'failedTimes');
                }
            }, {
                title: Admin.chargeSuccPer,
                align: 'center',
                valign: 'middle',
                formatter: successRateFormatter,
            }],
            onLoadSuccess: function (d) {
                $form.find(".search-btn").prop("disabled", false).html('<@spring.message "manager.check.select"/>');
                $form.find(".reset").prop("disabled", false).html('<@spring.message "manager.resetting"/>');
                return false;
            }
            , queryParams: function (params) {
                $form.find(".search-btn").prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                $form.find(".reset").prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                return params;
            }
        });
        function getTotal(rows, itemKey) {
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                var r = rows[i];
                if (!r[itemKey]) {
                    continue;
                }
                total += r[itemKey];
            }
            return aggsMoneyFormatter((itemKey === "depositUser" || itemKey === "depositTimes" || itemKey === "successTimes" || itemKey === "failedTimes" ? total : total.toFixed(2)));
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined || value == 0) {
                return "<span class='text-primary'>0.00</span>";
            }
            if (value > 0) {
                return ['<span class="text-danger">', '</span>'].join(value
                        .toFixed(2));
            }
            return ['<span class="text-primary">', '</span>'].join(value
                    .toFixed(2));
        }

        function aggsMoneyFormatter(value) {
            if (value === undefined || value == 0) {
                return "<span class='text-primary'>0</span>";
            }
            if (value && value > 0) {
                return ['<span class="text-danger">', '</span>'].join(value);
            }
            return ['<span class="text-primary">', '</span>'].join(value);
        }

        function successRateFormatter(value, row, index) {
            var r = row;
            var rate = (r.successTimes / r.depositTimes) * 100;
            if (rate > 0) {
                return ['<span class="text-danger">', '</span>'].join(rate.toFixed(2) + "%");
            }
            return ['<span class="text-primary">', '</span>'].join(rate.toFixed(2) + "%");
        }

        function numberFormatter(value, row, index) {
            return aggsMoneyFormatter(value);
        }

    });
</script>
