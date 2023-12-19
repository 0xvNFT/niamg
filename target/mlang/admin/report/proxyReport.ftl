<form class="fui-search table-tool" method="post" id="report_total_statistic_form_id">
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
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" placeholder="<@spring.message "admin.proxy.down.line.query"/>">
            </div>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "manager.check.select"/></button>
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
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<div style="color: red;">
    <@spring.message "admin.remark.win.lose.compute"/>
    <div style="padding-left: 42px;">
        <br> <@spring.message "admin.win.lose.valid.pay.proxy"/>；
        <div style="color: blue;"><@spring.message "admin.total.result.time"/></div>
    </div>
</div>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#report_total_statistic_form_id");
        Fui.addBootstrapTable({
            url: '${adminBase}/proxyReport/list.do',
            showPageSummary: true,
            showAllSummary: true,
            showFooter: true,
            columns: [{
                field: 'username',
                title: Admin.uerAccount,
                align: 'center',
                valign: 'middle',
                formatter: usernameFormatter
            }, {
                sortable: true,
                field: 'level',
                title: Admin.proxyLevel,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value) {
                        return value + "<@spring.message "admin.proxy.level"/>";
                    }
                    return "-";
                },
                pageSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.page.sum"/>:";
                },
                allSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.all.sum"/>:";
                }
            },{
                field: 'money',
                title: Admin.balanceValue,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0, row;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        row = rows[i];
                        if (row.money != null) {
                            r = r + row.money;
                        }
                    }
                    return r.toFixed(2);
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsData.moneySum ? aggsData.moneySum.toFixed(2) : "0.00";
                }
            }, {
                field: 'firstDeposit',
                title: Admin.firstPayNum,
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0, row;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        row = rows[i];
                        if (row.firstDeposit != null) {
                            r = r + row.firstDeposit;
                        }
                    }
                    return r ? r : '0';
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0"
                    }
                    return aggsData.firstDepositSum ? aggsData.firstDepositSum : "0";
                }
            }, {
                field: 'secDeposit',
                title: Admin.secPayNum,
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0, row;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        row = rows[i];
                        if (row.secDeposit != null) {
                            r = r + row.secDeposit;
                        }
                    }
                    return r ? r : '0';
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0"
                    }
                    return aggsData.secDepositSum ? aggsData.secDepositSum : "0";
                }
            }, {
                field: 'betUserNum',
                title: Admin.payPerNum,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'depositUserNum',
                title: Admin.chargePerNum,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'totalBetAmount',
                title: Admin.payCount,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'totalWinAmount',
                title: Admin.jackCount,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'depositAmount',
                title: Admin.depositSum,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    return moneyFormatter(row.depositAmount + row.depositArtificial);
                },
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0, row;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        row = rows[i];
                        if (row.depositAmount != null) {
                            r = r + row.depositAmount + row.depositArtificial;
                        }
                    }
                    return r.toFixed(2);
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsData.depositAmountSum ? aggsData.depositAmountSum.toFixed(2) : "0.00";
                }
            }, {
                field: 'withdrawAmount',
                title: Admin.drawSum,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0, row;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        row = rows[i];
                        if (row.withdrawAmount != null) {
                            r = r + row.withdrawAmount;
                        }
                    }
                    return r.toFixed(2);
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsData.withdrawAmountSum ? aggsData.withdrawAmountSum.toFixed(2) : "0.00";
                }
            }, {
                field: 'totalGiftAmount',
                title: Admin.actCashSum,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'totalWinOrLose',
                title: Admin.earnLose,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }]
        });
        function usernameFormatter(value, row, index) {
            return '<a class="open-tab" href="${adminBase}/user/detail.do?id='+row.userId+'" title="查看会员详情"  id="fui_tab_detailNew"><span class="text-danger">' + value + '</span></a>';
        }
        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return "<span class='text-primary'>0.00</span>";
            }
            if (value > 0) {
                return '<span class="text-danger">'+Fui.toDecimal(value, 2)+'</span>'
            }
            return '<span class="text-primary">'+Fui.toDecimal(value, 2)+'</span>'
        }
    });
</script>
