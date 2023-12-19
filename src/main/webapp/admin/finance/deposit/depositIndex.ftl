<style>
    .bootstrap-select.btn-group .dropdown-toggle .filter-option {
        display: inline-block;
        overflow: hidden;
        width: 86px;
        text-align: left;
    }

    .dropdown-menu {
        margin: 2px -8px 0;
    }


</style>
<form class="fui-search table-tool" method="post" id="deposit_pay_online_rd_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="startTime" value="${startTime}"
                       placeholder="<@spring.message "admin.srartTime"/>" format="YYYY-MM-DD HH:mm:ss"
                       autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='today'><@spring.message "admin.today"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='yesterday'><@spring.message "admin.yesterday"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <select class="form-control" name="type">
                    <option value=""><@spring.message "admin.deposit.type.all"/></option>
                    <option value="1"><@spring.message "admin.deposit.type.online"/></option>
                    <option value="3"><@spring.message "admin.deposit.type.bank"/></option>
                    <option value="4"><@spring.message "admin.deposit.type.manual"/></option>
                </select>
            </div>
            <div class="input-group">
                <select id="pay" name="payId" class="form-control selectpicker" multiple
                        data-live-search="true" title="<@spring.message "admin.deposit.pay.type.all"/>">
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="status">
                    <option value=""><@spring.message "admin.deposit.status.all"/></option>
                    <option value="1" class="text-success"><@spring.message "admin.deposit.status.no"/></option>
                    <option value="10" class="text-success"><@spring.message "admin.deposit.status.ing"/></option>
                    <option value="2" class="text-primary"><@spring.message "admin.deposit.status.success"/></option>
                    <option value="3" class="text-danger"><@spring.message "admin.deposit.status.fail"/></option>
                    <option value="4" class="text-default"><@spring.message "admin.deposit.status.timeout"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" id="handlerType" name="handlerType">
                    <option value=""><@spring.message "admin.deposit.handler.all"/></option>
                    <option value="2" <#if handleType == 2 >selected</#if>
                            class="text-success"><@spring.message "admin.deposit.handler.system"/></option>
                    <option value="1" <#if handleType == 1 >selected</#if>
                            class="text-danger"><@spring.message "admin.deposit.handler.manual"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control selectpicker" data-live-search="true"
                        title="<@spring.message "admin.deposit.all.degree"/>" multiple
                        id="deposit_account_level_id">
                    <#list levels as le>
                        <option value="${le.id}">${le.name}</option>
                    </#list>
                </select>
            </div>
            <input type="hidden" id="deposit_account_level_str_id" name="degreeIds">
            <input type="hidden" id="deposit_account_pay_str_id" name="payIds">
            <input type="hidden" id="deposit_account_pay_str_code" name="onlineCode">
            <input type="hidden" id="registerTime" name="registerTime" value="${registerTime }">
            <input type="hidden" id="registerTimeEnd" name="registerTimeEnd" value="${registerTimeEnd }">
            <input type="hidden" id="isDeposit" name="isDeposit" value='${isDeposit}'>
            <button class="btn btn-primary fui-date-search"
                    id="depositButton"><@spring.message "admin.deposit.button.search"/></button>
            <#if permAdminFn.hadPermission("admin:deposit:export")>
                <button class="btn btn-primary exportBtn"
                        type="button"><@spring.message "admin.deposit.button.export"/></button>
            </#if>
            <div class="input-group">
                <input style="margin-left: 10px;" type="checkbox" id="autoRefreshDeposit" value="1"
                       name="autoRefreshDeposit">
                <label for="autoRefreshDeposit"
                       style="margin-bottom: 0"><@spring.message "admin.deposit.button.autoFresh"/></label>
            </div>
            <div class="input-group">
                <select id="refreshDepositTable" title="<@spring.message "admin.deposit.button.autoFresh.scope"/>"
                        class="form-control">
                    <option value="10" selected>10</option>
                    <option value="30">30</option>
                    <option value="60">60</option>
                    <option value="120">120</option>
                </select>
            </div>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss"
                       value="${endTime}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn"
                    data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <select class="form-control" name="depositNum">
                <option value=""><@spring.message "admin.deposit.num"/></option>
                <option value="1" <#if depositNum ==1>selected</#if> ><@spring.message "admin.deposit.num.first"/></option>
                <option value="2" <#if depositNum ==2>selected</#if> ><@spring.message "admin.deposit.num.second"/></option>
                <option value="3" <#if depositNum ==3>selected</#if> ><@spring.message "admin.deposit.num.third"/></option>
            </select>
            <@spring.message "admin.deposit.money.range"/>:
            <div class="input-group">
                <input type="text" style="width:105px;" class="form-control" name="minMoney" value="${minMoney}"
                       placeholder="<@spring.message "admin.deposit.money.min" />">
            </div>
            <@spring.meaasge "admin.until"/>
            <div class="input-group">
                <input type="text" style="width:105px;" class="form-control" name="maxMoney" value="${maxMoney}"
                       placeholder="<@spring.message "admin.deposit.money.max" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" value="${proxyName}"
                       placeholder="<@spring.message "admin.deposit.parent" />">
            </div>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" class="form-control" name="username" value="${username}"
                       placeholder="<@spring.message "admin.deposit.username" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="orderId"
                       placeholder="<@spring.message "admin.deposit.orderId" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentUser" value="${agentUser}"
                       placeholder="<@spring.message "admin.deposit.agent" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="opUsername" value="${opUsername}"
                       placeholder="<@spring.message "admin.deposit.opuser" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="bgRemark"
                       placeholder="<@spring.message "admin.deposit.bgRemark" />">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="referrer"
                       placeholder="<@spring.message "admin.recommendUsername" />">
            </div>

            <div class="input-group">
                <input style="margin-left: 10px" type="checkbox" id="checkOrderStatus" value="1"
                       name="checkOrderStatus">
                <label for="checkOrderStatus"><@spring.message "admin.deposit.no.or.ing" /></label>
            </div>
        </div>
    </div>
</form>
<div class="hidden">
    <form id="pay_online_rd_export_fmId" action="${adminBase}/finance/deposit/export.do" target="_blank" method="post">
        <input type="hidden" name="username"/>
        <input type="hidden" name="type"/>
        <input type="hidden" name="status"/>
        <input type="hidden" name="handlerType"/>
        <input type="hidden" name="startTime"/>
        <input type="hidden" name="endTime"/>
        <input type="hidden" name="orderId"/>
        <input type="hidden" name="proxyName">
        <input type="hidden" name="payIds">
        <input type="hidden" name="opUsername">
        <input type="hidden" name="depositNum">
        <input type="hidden" name="degreeIds">
        <input type="hidden" name="payBankName">
        <input type="hidden" name="onlineCode">
        <input type="hidden" name="agentUser">
    </form>
</div>
<table class="fui-default-table">

</table>
<h3><span class="label label-danger"><@spring.message "admin.deposit.tip" /></span></h3>
<script type="text/javascript">
    requirejs(['clipboard', 'jquery', 'bootstrap', 'Fui', 'bootstrapSelect'], function (Clipboard) {
        var cacheName = 'DepositTable'
            , refreshDepositTableSecond = 10
            , refreshDepositTableKey = 'refreshDepositTable_' + baseInfo.username
            , refreshDepositTableTimer;
        var cacheSound = Fui.setCache(cacheName);
        if (cacheSound) {
            if (cacheSound[refreshDepositTableKey]) {
                refreshDepositTableSecond = cacheSound[refreshDepositTableKey];
                $("#refreshDepositTable").val(refreshDepositTableSecond);
            }
        }

        function refreshDepositTable() {
            clearTimeout(refreshDepositTableTimer);
            $("#depositButton").click();
            var flag = $("#autoRefreshDeposit").prop('checked');
            if (flag) {
                refreshDepositTableTimer = setTimeout(refreshDepositTable, 1000 * refreshDepositTableSecond);
            }
        }

        var $form = $("#deposit_pay_online_rd_form_id");
        $form.find("[id='refreshDepositTable']").change(function () {
            refreshDepositTableSecond = $(this).val();
            Fui.setCache(cacheName, {key: refreshDepositTableKey, value: refreshDepositTableSecond});
            clearTimeout(refreshDepositTableTimer);
            var flag = $("#autoRefreshDeposit").prop('checked');
            if (flag) {
                refreshDepositTableTimer = setTimeout(refreshDepositTable, 1000 * refreshDepositTableSecond);
            }
        });
        $form.find("[id='autoRefreshDeposit']").change(function () {
            var flag = $(this).prop('checked');
            if (flag) {
                refreshDepositTableTimer = setTimeout(refreshDepositTable, 1000 * refreshDepositTableSecond);
            } else {
                clearTimeout(refreshDepositTableTimer);
            }
        });
        $form.find(".exportBtn").click(function () {
            var $form1 = $("#pay_online_rd_export_fmId");
            $form1.find("[name='username']").val($form.find("[name='username']").val());
            $form1.find("[name='orderId']").val($form.find("[name='orderId']").val());
            $form1.find("[name='status']").val($form.find("[name='status']").val());
            $form1.find("[name='startTime']").val($form.find("[name='startTime']").val());
            $form1.find("[name='endTime']").val($form.find("[name='endTime']").val());
            $form1.find("[name='type']").val($form.find("[name='type']").val());
            $form1.find("[name='handlerType']").val($form.find("[name='handlerType']").val());
            $form1.find("[name='payIds']").val($form.find("[name='payIds']").val());
            $form1.find("[name='onlineCode']").val($form.find("[name='onlineCode']").val());
            $form1.find("[name='opUsername']").val($form.find("[name='opUsername']").val());
            $form1.find("[name='depositNum']").val($form.find("[name='depositNum']").val());
            $form1.find("[name='degreeIds']").val($form.find("[name='degreeIds']").val());
            $form1.find("[name='proxyName']").val($form.find("[name='proxyName']").val());
            $form1.find("[name='payBankName']").val($form.find("[name='payBankName']").val());
            $form1.find("[name='isDeposit']").val($form.find("[name='isDeposit']").val());
            $form1.find("[name='agentUser']").val($form.find("[name='agentUser']").val());
            $form1.submit();
        });
        $form.find("[id='deposit_account_level_id']").change(function () {
            var types = $(this).val();
            if (types) {
                types = types.join(",");
                $form.find("[id='deposit_account_level_str_id']").val(types);
            } else {
                $form.find("[id='deposit_account_level_str_id']").val("");
            }
        });
        $form.find("[name='payId']").change(function () {
            var multiple = "on";
            var types = $(this).val();
            var regexp = /^[\d,]+$/;
            if (types) {
                if (multiple === 'on') {
                    types = types.join(",");
                }
                if (regexp.test(types)) {
                    $form.find("[id='deposit_account_pay_str_id']").val(types);
                } else {
                    $form.find("[id='deposit_account_pay_str_code']").val(types);
                }
            } else {
                $form.find("[id='deposit_account_pay_str_id']").val("")
                $form.find("[id='deposit_account_pay_str_code']").val("");
            }
        });

        // var bankWay = {1: '网银转账', 2: 'ATM入款', 3: '银行柜台', 4: '手机转账', 5: '支付宝', 6: '微信'};
        Fui.addBootstrapTable({
            showPageSummary: true,
            showAllSummary: true,
            showFooter: true,
            url: "${adminBase}/finance/deposit/list.do",
            columns: [{
                field: 'orderId',
                title: '<@spring.message "admin.deposit.orderId"/>',
                align: 'center',
                width: '300',
                valign: 'middle',
                formatter: orderFormatter
            }<#if !isDynamicOdds || !isFixedRate>
                , {
                    field: 'proxyName',
                    title: '<@spring.message "admin.deposit.table.proxyName"/>',
                    align: 'center',
                    valign: 'middle'
                }
                </#if>
                , {
                    field: 'depositType',
                    title: '<@spring.message "admin.deposit.table.depositType"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: typeFormatter
                }, {
                    field: 'payName',
                    title: '<@spring.message "admin.deposit.table.payName"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        if (row.payBankName) {
                            return value + "<br/><span  class='label label-info'>" + row.payBankName + "</span>";
                        } else {
                            return value;
                        }

                    },
                    pageSummaryFormat: function (rows, aggsData) {
                        return '<@spring.message "admin.deposit.table.page.sum"/>:';
                    },
                    allSummaryFormat: function (rows, aggsData) {
                        return '<@spring.message "admin.deposit.table.all.sum"/><@spring.message "admin.deposit.table.all.sum"/>:<br/><@spring.message "admin.deposit.table.people.num"/>:';
                    }
                }, {
                    field: 'money',
                    <#if moneyUnit>
                        title: '<@spring.message "admin.deposit.table.money"/>(/${moneyUnit})',
                    <#else>
                        title: '<@spring.message "admin.deposit.table.money"/>(/<@spring.message "admin.deposit.table.money.unit"/>)',
                    </#if>
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: moneyPayFormatter,
                    pageSummaryFormat: function (rows, aggsData) {
                        return getTotal(rows, 'money');
                    },
                    allSummaryFormat: function (rows, aggsData) {

                        if (!aggsData) {
                            return "0.00" + "<br/> 0"
                        }

                        return (aggsData.totalMoney ? aggsData.totalMoney.toFixed(2) : "0.00") + "<br/>" + rows[0].paytimes;
                    }
                }, {
                    field: 'depositVirtualCoinNum',
                    title: '<@spring.message "admin.virtual.coin.num"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: moneyPayFormatter
                }, {
                    field: 'depositVirtualCoinRate',
                    title: '<@spring.message "admin.virtual.coin.rate"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: moneyPayFormatter
                }, {
                    field: 'giftMoney',
                    <#if moneyUnit>
                    title: '<@spring.message "admin.deposit.table.giftMoney"/>(/${moneyUnit})',
                    <#else>
                    title: '<@spring.message "admin.deposit.table.giftMoney"/>(/<@spring.message "admin.deposit.table.money.unit"/>)',
                    </#if>
                    align: 'center',
                    valign: 'middle',
                    formatter: moneyFormatter,
                    pageSummaryFormat: function (rows, aggsData) {
                        return getTotal(rows, 'giftMoney');
                        ;
                    },
                    allSummaryFormat: function (rows, aggsData) {
                        if (!aggsData) {
                            return "0.00"
                        }
                        return aggsData.totalGiftMoney ? aggsData.totalGiftMoney.toFixed(2) : "0.00";
                    }
                }, {
                    field: 'realName',
                    title: '<@spring.message "admin.deposit.table.realName"/>',
                    align: 'center',
                    valign: 'middle',
                }, {
                    field: 'depositor',
                    title: "<@spring.message "admin.deposit.table.depositor"/>",
                    align: 'center',
                    valign: 'middle'
                },
                {
                    field: 'createDatetime',
                    title: '<@spring.message "admin.deposit.table.createDatetime"/>',
                    align: 'center',
                    sortable: true,
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return value ? Fui.formatDatetime(value) : "-";
                    }
                }, {
                    field: 'opDatetime',
                    title: '<@spring.message "admin.deposit.table.opDatetime"/>',
                    align: 'center',
                    sortable: true,
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        return value ? Fui.formatDatetime(value) : "-";
                    }
                }, {
                    field: 'status',
                    title: '<@spring.message "admin.deposit.table.status"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: statusFormatter
                }, {
                    field: 'lockFlag',
                    title: '<@spring.message "admin.deposit.table.lockFlag"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: operateFormatter
                }, {
                    field: 'opUsername',
                    title: '<@spring.message "admin.deposit.table.opUsername"/>',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'remark',
                    title: '<@spring.message "admin.deposit.table.remark"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: remarkSizeFormatter
                }, {
                    field: 'bgRemark',
                    title: '<@spring.message "admin.deposit.table.bgRemark"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: descFormatter
                }],
            onLoadSuccess: function ($table) {
                copyFun();
                //初始化复制按钮
                $('.need-copy').click(function () {
                    var needCopyBtns = $('.need-copy');
                    if ($('.need-copy').length <= 0) return;
                    var lens = needCopyBtns.length;
                    for (var i = 0; i < lens; i++) {
                        var cb = new Clipboard(needCopyBtns[i]);
                        cb.on('success', function (e) {
                            layer.msg('<@spring.message "admin.deposit.table.copy.success"/>！');//, 复制内容: ' + e.text);
                            e.clearSelection();
                        });
                    }
                });
            }
        });

        function orderFormatter(value, row, index) {
            var s = '';
            <#--<#if onOffCopybtn>
            s = '<a class="<#if onOffNewAccdetail>open-tab<#else>open-dialog</#if>" href="${adminBase}/user/detail<#if onOffNewAccdetail>New</#if>.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username + '</span></a>(' + row.degreeName + ')' + '&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="' + row.username + '" style="color: #337ab7;"></i>';
            <#else>
            s = '<a class="<#if onOffNewAccdetail>open-tab<#else>open-dialog</#if>" href="${adminBase}/user/detail<#if onOffNewAccdetail>New</#if>.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username + '</span></a>(' + row.degreeName + ')';
            </#if>-->
            s = '<a class="open-tab text-danger" data-refresh="true" href="${adminBase}/user/detail.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username + '</span></a>(' + row.degreeName + ')';
            if (row.first) {
                s += '&nbsp;&nbsp<span class="label label-success"><@spring.message "admin.deposit.table.first"/></span>';
            }
            if (row.warning) {
                s += '&nbsp;&nbsp<span class="label label-danger"><@spring.message "admin.deposit.table.warn"/></span>';
            }

            var tronScanAddr = value;
            if (row.usdtDeposit) {
                tronScanAddr = '<a href="' + row.tronScanUrl + '" title="<@spring.message "admin.deposit.table.detail"/>" target="_blank">' +  value + '</a>';
            }

            <#if onOffCopybtn>
            s += '<br>' + tronScanAddr + '&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="' + row.orderId + '" style="color: #337ab7;"></i>';
            <#else>
            s += '<br>' + tronScanAddr;
            </#if>

            return s;
        }

        function orderTypeFormatter(value, row, index) {
            if (row.secondReview) {
                return '<span style="color:red">'+'<@spring.message "admin.deposit.order.fake"/>'+'</span>';
            } else {
                return '<span style="color:green">'+'<@spring.message "admin.deposit.order.normal"/>'+'</span>';
            }
        }

        function moneyPayFormatter(value, row, index) {
            if (value === undefined) {
                return "0.00";
            }
            var str = '';
            if (row.payPlatformCode == 'USDT' && row.depositVirtualCoinNum !== undefined && row.depositVirtualCoinRate !== undefined) {
                str = row.depositVirtualCoinNum + " * " + row.depositVirtualCoinRate + '<br/>';
            }
            if (value > 0) {
                if(row.stationLanguage == 'vi'){
                    <#if onOffCopybtn>
                    return str + '<span class="text-danger">' + formatMoney(Fui.toDecimal(value, 0)/1000) + 'K</span>' + '&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="' + Fui.toDecimal(value, 2) + '" style="color: #337ab7;"></i>';
                    <#else>
                    return str + ['<span class="text-danger">', '</span>'].join(formatMoney(Fui.toDecimal(value, 2)/1000) + "K");
                    </#if>
                }else{
                    <#if onOffCopybtn>
                    return str + '<span class="text-danger">' + formatMoney(Fui.toDecimal(value, 2)) + '</span>' + '&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="' + Fui.toDecimal(value, 2) + '" style="color: #337ab7;"></i>';
                    <#else>
                    return str + ['<span class="text-danger">', '</span>'].join(formatMoney(Fui.toDecimal(value, 2)));
                    </#if>
                }
            }
            return ['<span class="text-primary">', '</span>'].join("0.00");
        }

        function formatMoney(value) {
            return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }

        var payOnlineType = {
            1: '<@spring.message "admin.deposit.type.online"/>',
            2: '<@spring.message "admin.deposit.type.fast"/>',
            3: '<@spring.message "admin.deposit.type.bank"/>',
            4: '<@spring.message "admin.deposit.type.manual"/>',
            5: '<@spring.message "admin.deposit.type.api"/>'
        };

        function remarkSizeFormatter(value, row, index) {
            if (value) {
                if (value.length > 6) {
                    return ['<a class="open-text" dialog-text="' + value + '" dialog-title="<@spring.message "admin.deposit.table.detail"/>" title="' + value + '">', '</span>'].join(value.substr(0, 6) + "...");
                }
                return value;
            }
            return '';
        }

        function typeFormatter(value, row, index) {
            return payOnlineType[value] + handerTypeCons[row.handlerType];
        }

        function descFormatter(value, row, index) {
            if (row.status != 1 && row.bgRemark === "") {
                return '<a class="open-dialog text-danger"  href="${adminBase}/finance/deposit/remarkModify.do?id=' + row.id + '" title="' + value + '">' + '<@spring.message "admin.deposit.remark.add"/>' + '</a>';
            }
            if (value === undefined) {
                return "-";
            }
            var enterIndex = 6;
            if (value && value.length > enterIndex) {
                return '<a class="open-dialog"  href="${adminBase}/finance/deposit/remarkModify.do?id=' + row.id + '" title="' + value + '">' + value.substring(0, 6) + '......' + '</a>';
            }
            return '<a class="open-dialog"  href="${adminBase}/finance/deposit/remarkModify.do?id=' + row.id + '" title="' + value + '">' + value + '</a>';
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return "0.00";
            }
            if (value > 0) {
                return ['<span class="text-danger">', '</span>'].join(Fui.toDecimal(value, 2));
            }
            return ['<span class="text-primary">', '</span>'].join("0.00");
        }

        function getTotal(rows, itemKey) {
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                var r = rows[i];
                if (!r[itemKey] || r.status != 2) {
                    continue;
                }
                total += r[itemKey];
            }
            return total.toFixed(2) + "";
        }

        var handerTypeCons = {
            1: '<br><span class="text-danger"><@spring.message "admin.deposit.handler.manual"/></span>',
            2: '<br><span class="text-danger"><@spring.message "admin.deposit.handler.system"/></span>'
        };

        function statusFormatter(value, row, index) {
            if (value == 1 && row.lockFlag == 1) {
                return '<span class="text-success"><@spring.message "admin.deposit.status.no"/></span>';
            }
            switch (value) {
                case 1:
                    return '<span class="text-success"><@spring.message "admin.deposit.status.no"/></span>';
                case 2:
                    return '<span class="text-primary"><@spring.message "admin.deposit.status.success"/></span>';
                case 3:
                    return '<span class="text-danger"><@spring.message "admin.deposit.status.fail"/></span>';
                case 4:
                    return '<span class="text-default"><@spring.message "admin.deposit.status.timeout"/></span>';
            }
        }

        function operateFormatter(value, row, index) {
            if ((value == 1 || value == '1') && (row.status == '1' || row.status == 1)) {
                <#if permAdminFn.hadPermission("admin:deposit:ope")>
                return ['<a class="todo-ajax" href="${adminBase}/finance/deposit/lock.do?lockFlag=2&id=', row.id, '" ><@spring.message "admin.deposit.status.be.lock"/></a> ',
                    '<a class="open-dialog" href="${adminBase}/finance/deposit/handle.do?status=3&id=', row.id, '" title="<@spring.message "admin.deposit.status.be.fail"/>"><@spring.message "admin.deposit.status.be.fail"/></a>'].join('');
                <#else>
                return '<@spring.message "admin.deposit.status.no.lock"/>';
                </#if>
            } else if ((value == 2 || value == '2') && (row.status == '1' || row.status == 1)) {
                <#if permAdminFn.hadPermission("admin:deposit:ope")>
                if (baseInfo.username == row.opUsername) {
                    return ['<a class="open-dialog" href="${adminBase}/finance/deposit/handle.do?status=2&id=', row.id, '" title="<@spring.message "admin.deposit.status.be.success"/>"><@spring.message "admin.deposit.status.be.success"/></a> ',
                        '<a class="todo-ajax" href="${adminBase}/finance/deposit/lock.do?lockFlag=1&id=', row.id, '" title="<@spring.message "admin.deposit.status.be.unlock"/>"><@spring.message "admin.deposit.status.be.unlock"/></a>  ',
                        '<a class="open-dialog" href="${adminBase}/finance/deposit/handle.do?status=3&id=', row.id, '" title="<@spring.message "admin.deposit.status.be.fail"/>"><@spring.message "admin.deposit.status.be.fail"/></a>'].join('');
                }

                </#if>
                return '<@spring.message "admin.deposit.status.has.bean.lock"/>';
            } else if (row.status == '3' || row.status == 3) {
                <#if permAdminFn.hadPermission("admin:deposit:ope")>
                if (baseInfo.username == row.opUsername) {
                    if (new Date().getTime() - new Date(row.modifyDatetime).getTime() < 3600000) {
                        return ['<a class="todo-ajax" href="${adminBase}/finance/deposit/recovery.do?id=', row.id, '" title="<@spring.message "admin.deposit.status.be.recovery.tip"/>"><@spring.message "admin.deposit.status.be.recovery"/></a>'].join('');
                    }
                }
                </#if>
            }
            return '<@spring.message "admin.deposit.status.be.finish"/>';
        }

        $("select[name='type']").change(function () {
            getPayCombos($(this).val());
        });

        function getPayCombos(type) {
            var multiple = 'on';
            $("select[name='payBankName']").css('display', 'none');
            $("select[name='payBankName']").val("");
            $("#deposit_account_pay_str_code").val("");
            $("#deposit_account_pay_str_id").val("");
            $.ajax({
                url: "${adminBase}/finance/deposit/getPayCombos.do",
                data: {'type': type},
                success: function (json) {
                    var html = "";
                    if (type === 1 || type === '1') {
                        html += " <select id='onlineCode' name='onlineCode' class='form-control selectpicker' data-live-search='true'>";
                        $("#pay").attr("name", "onlineCode");
                        $("select[name='payBankName']").css('display', 'block');
                    } else if (type === 3 || type === '3') {
                        html += " <select id='payPlatformId' name='payId' class='form-control selectpicker' data-live-search='true'>";
                        $("#pay").attr("name", "payId")
                    } else {
                        html += "<select id='payPlatformId' name='payId' class='form-control selectpicker' data-live-search='true'>";
                        $("#pay").attr("name", "payId")
                    }
                    if (multiple === "off") {
                        html += ' <option value=""><@spring.message "admin.deposit.pay.type.all"/></option>';
                    }
                    $.each(json, function (index, item) {
                        if (type === 1 || type === '1') {
                            html += "<option value='" + item.payName + "'>" + item.payName + "</option>";
                        } else if (type === 3 || type === '3') {
                            html += "<option value='" + item.id + "'>" + item.bankName + "(" + item.realName + ")" + "</option>";
                        } else {
                            html += "<option value='" + item.id + "'>" + item.payName + "(" + item.realName + ")" + "</option>";
                        }
                    });
                    html += "</select>";
                    $("#pay").html(html);
                    $('#pay').selectpicker('refresh');
                    $('#pay').selectpicker('render');
                }
            });
        }

        function copyFun() {
            var needCopyBtns = $('.need-copy');
            if ($('.need-copy').length <= 0) return;
            var lens = needCopyBtns.length;
            for (var i = 0; i < lens; i++) {
                var cb = new Clipboard(needCopyBtns[i]);
                cb.on('success', function (e) {
                    layer.msg('<@spring.message "admin.deposit.table.copy.success"/>！');//, 复制内容: ' + e.text);
                    e.clearSelection();
                });
            }
        }
    });
    $("#checkOrderStatus").click(function () {
        $("#depositButton").click();
    });
</script>
