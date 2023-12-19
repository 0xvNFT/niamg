<form class="fui-search table-tool" method="post" id="teamReportFormId">
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
            <div class="input-group">
                <select class="form-control" name="level">
                    <option value=""><@spring.message "admin.floor.level.degree"/></option>
                    <#list 1 .. lowestLevel as i><option value="${i}">${i}<@spring.message "admin.level.degree"/></option></#list>
                </select>
            </div>
            <div class="input-group">
            	<select class="form-control selectpicker" data-live-search="true" title="<@spring.message "admin.deposit.all.degree"/>" multiple id="teamReportUserDegreeId">
                    <#list degrees as le>
                        <option value="${le.id}">${le.name}</option>
                    </#list>
                </select>
                <input type="hidden" name="degreeIds">
            </div>
            <div class="input-group">
                <label class="sr-only" for="agent"><@spring.message "admin.vip.remark"/></label>
                <input type="text" class="form-control" name="userRemark" placeholder="<@spring.message "admin.vip.remark"/>">
            </div>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "manager.check.select"/></button>
            <button class="btn btn-primary reset" type="button"><@spring.message "manager.resetting"/></button>
            <#if permAdminFn.hadPermission("admin:teamReport:export")>
                <button class="btn btn-primary exportBtn" type="button"><@spring.message "admin.deposit.button.export"/></button>
            </#if>
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
                <label class="sr-only" for="account"><@spring.message "admin.simple.user.query"/></label>
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.simple.user.query"/>">
            </div>
            <div class="input-group">
                <label class="sr-only" for="account"><@spring.message "admin.withdraw.agentUser"/></label>
                <input type="text" class="form-control" name="agentName" placeholder="<@spring.message "admin.belong.proxy.query"/>">
            </div>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<div style="color: red;">
    <@spring.message "admin.remark.win.lose.compute"/>
    <div style="padding-left: 42px;">
        <@spring.message "admin.vip.win.lose.valid"/>；
        <br> <@spring.message "admin.proxy.win.lose.valid"/>；
        <div style="color: blue;"><@spring.message "admin.total.result.time"/></div>
    </div>
</div>
<#if permAdminFn.hadPermission("admin:teamReport:export")><div class="hidden">
    <form id="team_report_rd_export_fmId" action="${adminBase}/teamReport/export.do" target="_blank" method="post">
        <input type="hidden" name="username"/>
        <input type="hidden" name="proxyName"/>
        <input type="hidden" name="startDate"/>
        <input type="hidden" name="endDate"/>
        <input type="hidden" name="degreeIds">
        <input type="hidden" name="userRemark">
        <input type="hidden" name="agentName">
    </form>
</div></#if>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#teamReportFormId");
        <#if permAdminFn.hadPermission("admin:teamReport:export")>$form.find(".exportBtn").click(function () {
            var $form1 = $("#team_report_rd_export_fmId");
            $form1.find("[name='username']").val($form.find("[name='username']").val());
            $form1.find("[name='startDate']").val($form.find("[name='startDate']").val());
            $form1.find("[name='endDate']").val($form.find("[name='endDate']").val());
            $form1.find("[name='proxyName']").val($form.find("[name='proxyName']").val());
            $form1.find("[name='degreeIds']").val($form.find("[name='degreeIds']").val());
            $form1.find("[name='userRemark']").val($form.find("[name='userRemark']").val());
            $form1.find("[name='agentName']").val($form.find("[name='agentName']").val());
            $form1.submit();
        });</#if>

        //等级
        $form.find("#teamReportUserDegreeId").change(function () {
            var types = $(this).val();
            if (types) {
                types = types.join(",");
                $form.find("[name='degreeIds']").val(types);
            } else {
                $form.find("[name='degreeIds']").val("");
            }
        });

        Fui.addBootstrapTable({
            url: '${adminBase}/teamReport/list.do',
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
                field: 'userType',
                title: Admin.type,
                align: 'center',
                valign: 'middle',
                formatter: typeFormatter,
                pageSummaryFormat: function (rows, aggsData){
                    return Admin.subtotal;
                }
            }, {
                field: 'money',
                title: Admin.balanceValue,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'money');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return '<@spring.message "admin.total"/>';
                }
            }, {
                field: 'liveBetAmount',
                title: Admin.payCount,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'liveBetAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'liveBetAmount');
                }
            }, {
                field: 'liveWinAmount',
                title: Admin.jackCount,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'liveWinAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'liveWinAmount');
                }
            }, {
                field: 'depositAmount',
                title: Admin.onlineDepositTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'depositAmount');
                }
            }, {
                field: 'depositArtificial',
                title: Admin.handAddMoney,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositArtificial');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'depositArtificial');
                }
            }, {
                field: 'depositGiftAmount',
                title: Admin.depositGiveTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'depositGiftAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'depositGiftAmount');
                }
            }, {
                field: 'giftOtherAmount',
                title: Admin.otherAddTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'giftOtherAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'giftOtherAmount');
                }
            }, {
                field: 'withdrawAmount',
                title: Admin.onlineDrawTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'withdrawAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'withdrawAmount');
                }
            }, {
                field: 'withdrawArtificial',
                title: Admin.handDeduce,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'withdrawArtificial');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'withdrawArtificial');
                }
            }, {
                field: 'subGiftAmount',
                title: Admin.colorMoneyOut,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'subGiftAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'subGiftAmount');
                }
            }, {
                field: 'liveRebateAmount',
                title: Admin.backWaterTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'liveRebateAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'liveRebateAmount');
                }
            }, {
                field: 'proxyRebateAmount',
                title: Admin.backPointTot,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'proxyRebateAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'proxyRebateAmount');
                }
            }, {
                field: 'activeAwardAmount',
                title: Admin.actCashSum,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                sortable: true,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'activeAwardAmount');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'activeAwardAmount');
                }
            }, {
                field: 'profitAndLoss',
                title: Admin.earnLose,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, 'profitAndLoss');
                },
                allSummaryFormat: function (rows, aggsData) {
                    return totalFormatter(aggsData, 'profitAndLoss');
                }
            }]
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
            return aggsMoneyFormatter(total.toFixed(2));
        }

        function optFormatter(value, row, index) {
            value = row.agentId;
            var col = "";
            if (value && value != 0) {
                col += '<a href="javascript:void(0)" class="uplevel"><span class="text-success upLevel" id="' + row.agentName + '"><@spring.message "admin.up.level"/></span></a>';
            }
            value = row.accountType;
            if (value == 4) {
                col += '<a href="javascript:void(0)" class="downlevel"><span class="text-success nextLevel" id="' + row.account + '"><@spring.message "admin.down.level"/></span></a>';
            }
            return col;
        }

        function usernameFormatter(value, row, index) {
            return '<a class="open-tab" href="${adminBase}/user/detail.do?id='+row.userId+'" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew"><span class="text-danger">'+value+'</span></a>';
        }

        var memType = {
            130: "<@spring.message "admin.withdraw.type.member"/>",
            120: "<@spring.message "admin.withdraw.type.proxy"/>"
        };

        function typeFormatter(value, row, index) {
            return memType[value];
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined || value == 0) {
                return "<span class='text-primary'>0.00</span>";
            }
            if (value > 0) {
                return '<span class="text-danger">'+value.toFixed(2)+'</span>'
            }
            return '<span class="text-primary">'+value.toFixed(2)+'</span>'
        }

        function totalFormatter(aggsData, key) {
            var money = "0.00";
            if (aggsData && aggsData[key]) {
                money = aggsData[key].toFixed(2);
            }
            return aggsMoneyFormatter(money);
        }

        function aggsMoneyFormatter(value) {
            if (value && value > 0) {
                return '<span class="text-danger">'+value+'</span>';
            }
            return '<span class="text-primary">'+value+'</span>'
        }
    });
</script>
