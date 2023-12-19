<form class="fui-search table-tool" method="post" id="memdrawrd_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <input id="paySwitch" type="text" style="display: none" value="${paySwitch}">
            <input id="cancelPay" type="text" style="display: none" value="${cancelPay}">
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
            <div class="input-group">
                <select class="form-control" name="drawType">
                    <option value=""><@spring.message "admin.all.records"/></option>
                    <option value="2"><@spring.message "admin.payment.records"/></option>
                    <option value="1"><@spring.message "admin.withdrawal.records"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="status">
                    <option value="" class="text-warning"><@spring.message "admin.withdraw.status.all"/></option>
                    <option value="1" class="text-success"><@spring.message "admin.withdraw.status.undo"/></option>
                    <option value="10" class="text-success"><@spring.message "admin.withdraw.status.doing"/></option>
                    <option value="2" class="text-primary"><@spring.message "admin.withdraw.status.success"/></option>
                    <option value="3" class="text-danger"><@spring.message "admin.withdraw.status.fail"/></option>
                    <option value="4" class="text-default"><@spring.message "admin.withdraw.status.timeout"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="type">
                    <option value="" class="text-warning" selected><@spring.message "admin.withdraw.type.all"/></option>
                    <option value="1" class="text-primary"><@spring.message "admin.withdraw.type.member"/></option>
                    <option value="2" class="text-warning"><@spring.message "admin.withdraw.type.proxy"/></option>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="username" value="${username}" placeholder="<@spring.message "admin.deposit.username"/>">
            </div>
            <div class="input-group">
                <select class="form-control selectpicker" data-live-search="true" title="<@spring.message "admin.deposit.all.degree"/>" multiple
                        id="draw_account_level_id">
                    <#list levels as le>
                        <option value="${le.id}">${le.name}</option>
                    </#list>
                </select>
            </div>
            <input type="hidden" id="draw_account_level_str_id" name="levelIds">

            <button class="btn btn-primary fui-date-search" id="withdrawButton"><@spring.message "admin.deposit.button.search"/></button>
            <#if permAdminFn.hadPermission("withdraw:export")>
                <button class="btn btn-primary exportBtn" type="button"><@spring.message "admin.deposit.button.export"/></button>
            </#if>
            <div class="input-group">
                <input style="margin-left: 10px;" type="checkbox" id="autoRefreshWithdraw" value="1"
                       name="autoRefreshWithdraw">
                <label for="autoRefreshWithdraw" style="margin-bottom: 0"><@spring.message "admin.deposit.button.autoFresh"/></label>
            </div>
            <div class="input-group">
                <select id="refreshWithdrawTable" title="<@spring.message "admin.deposit.button.autoFresh.scope"/>" class="form-control">
                    <option value="10" selected>10</option>
                    <option value="30">30</option>
                    <option value="60">60</option>
                    <option value="120">120</option>
                </select>
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
                <select class="form-control" name="withdrawNum">
                    <option value=""><@spring.message "admin.withdraw.num"/></option>
                    <option value="1" <#if withdrawNum ==1>selected</#if> ><@spring.message "admin.withdraw.first"/></option>
                    <option value="2" <#if withdrawNum ==2>selected</#if>><@spring.message "admin.withdraw.second"/></option>
                </select>
                <input type="text" class="form-control" style="width:169px;" name="orderId" value="${orderId}" placeholder="<@spring.message "admin.withdraw.orderId"/>">
                <input type="text" class="form-control" style="width:150px;" name="opUsername" value="${opUsername}" placeholder="<@spring.message "admin.withdraw.opUsername"/>">
                <input type="text" class="form-control" style="width:150px;" name="proxyName" value="${proxyName}" placeholder="<@spring.message "admin.withdraw.proxyName"/>">

                <div class="input-group">
                    <input style="margin-left: 10px" type="checkbox" id="checkWithDrawOrderStatus" value="1"
                           name="checkWithDrawOrderStatus">
                    <label for="checkWithDrawOrderStatus"><@spring.message "admin.deposit.no.or.ing"/></label>
                </div>
            </div>
            <div class="form-inline mt5px">
                <@spring.message "admin.deposit.money.range"/>:
                <div class="input-group">
                    <input type="text" class="form-control" style="width:105px;" name="minMoney" value="${minMoney}"
                           placeholder="<@spring.message "admin.deposit.money.min"/>">
                </div>
                <@spring.message "admin.until"/>
                <div class="input-group">
                    <input type="text" class="form-control" style="width:105px;" name="maxMoney" value="${maxMoney}"
                           placeholder="<@spring.message "admin.deposit.money.max"/>">
                </div>

                <input type="text" class="form-control" style="width:150px;" name="agentUser" value="${agentUser}" placeholder="<@spring.message "admin.withdraw.agentUser"/>">
                <input type="text" class="form-control" style="width:150px;" name="bankName"
                       placeholder="<@spring.message "admin.withdraw.bankName"/>">
                <input type="text" class="form-control" style="width:150px;" name="referrer"
                       placeholder="<@spring.message "admin.recommendUsername"/>">
            </div>
        </div>
    </div>
</form>
<table id="memdrawrd_datagrid_tb"></table>
<div class="hidden">
    <form id="memdrawrd_export_fm_id" action="${proxyBase}/finance/withdraw/export.do" method="post"
          target="_blank">
        <input type="hidden" name="startTime"/>
        <input type="hidden" name="status"/>
        <input type="hidden" name="type"/>
        <input type="hidden" name="username"/>
        <input type="hidden" name="endTime"/>
        <input type="hidden" name="proxyName"/>
        <input type="hidden" name="pay"/>
        <input type="hidden" name="opUsername"/>
        <input type="hidden" name="orderId"/>
        <input type="hidden" name="levelIds"/>
        <input type="hidden" name="payId"/>
        <input type="hidden" name="agentUser"/>
        <input type="hidden" name="bankName"/>
        <input type="hidden" name="withdrawNum"/>
    </form>
</div>
<script type="text/javascript">
    requirejs(['clipboard','jquery', 'bootstrap', 'Fui'], function (Clipboard) {
        var cacheName = 'WithdrawTable'
            , refreshWithdrawTableSecond = 10
            , refreshWithdrawTableSecondKey = 'refreshWithdrawTable_' + baseInfo.username
            , refreshWithdrawTableTimer;
        var cacheSound = Fui.setCache(cacheName);
        if (cacheSound) {
            if (cacheSound[refreshWithdrawTableSecondKey]) {
                refreshWithdrawTableSecond = cacheSound[refreshWithdrawTableSecondKey];
                $("#refreshWithdrawTable").val(refreshWithdrawTableSecond);
            }
        }
        function refreshWithdrawTable() {
            clearTimeout(refreshWithdrawTableTimer);
            $("#withdrawButton").click();
            var flag = $("#autoRefreshWithdraw").prop('checked');
            if (flag) {
                refreshWithdrawTableTimer = setTimeout(refreshWithdrawTable, 1000 * refreshWithdrawTableSecond);
            }
        }
        var $form = $("#memdrawrd_form_id");
        $form.find("[id='refreshWithdrawTable']").change(function () {
            refreshWithdrawTableSecond = $(this).val();
            Fui.setCache(cacheName, {key: refreshWithdrawTableSecondKey, value: refreshWithdrawTableSecond});
            clearTimeout(refreshWithdrawTableTimer);
            var flag = $("#autoRefreshWithdraw").prop('checked');
            if (flag) {
                refreshWithdrawTableTimer = setTimeout(refreshWithdrawTable, 1000 * refreshWithdrawTableSecond);
            }
        });
        $form.find("[id='autoRefreshWithdraw']").change(function () {
            var flag = $(this).prop('checked');
            if (flag) {
                refreshWithdrawTableTimer = setTimeout(refreshWithdrawTable, 1000 * refreshWithdrawTableSecond);
            } else {
                clearTimeout(refreshWithdrawTableTimer);
            }
        });
        $form.find(".exportBtn").click(function () {
            var $form1 = $("#memdrawrd_export_fm_id");
            $form1.find("[name='startTime']").val($form.find("[name='startTime']").val());
            $form1.find("[name='status']").val($form.find("[name='status']").val());
            $form1.find("[name='type']").val($form.find("[name='type']").val());
            $form1.find("[name='username']").val($form.find("[name='username']").val());
            $form1.find("[name='endTime']").val($form.find("[name='endTime']").val());
            $form1.find("[name='proxyName']").val($form.find("[name='proxyName']").val());
            $form1.find("[name='pay']").val($form.find("[name='pay']").val());
            $form1.find("[name='opUsername']").val($form.find("[name='opUsername']").val());
            $form1.find("[name='orderId']").val($form.find("[name='orderId']").val());
            $form1.find("[name='levelIds']").val($form.find("[name='levelIds']").val());
            $form1.find("[name='payId']").val($form.find("[name='payId']").val());
            $form1.find("[name='agentUser']").val($form.find("[name='agentUser']").val());
            $form1.find("[name='bankName']").val($form.find("[name='bankName']").val());
            $form1.find("[name='withdrawNum']").val($form.find("[name='withdrawNum']").val());
            $form1.submit();
        });
        $form.find("[id='draw_account_level_id']").change(function () {
            var types = $(this).val();
            if (types) {
                types = types.join(",");
                $form.find("[id='draw_account_level_str_id']").val(types);
            } else {
                $form.find("[id='draw_account_level_str_id']").val("");
            }
        });
        Fui.addBootstrapTable({
            id: 'memdrawrd_datagrid_tb',
            url: '${proxyBase}/finance/withdraw/list.do',
            showPageSummary: true,
            showAllSummary: true,
            showFooter: true,
            columns: [{
	                field: 'orderId',
	                title: '<@spring.message "admin.withdraw.table.orderId"/>',
	                align: 'center',
	                valign: 'middle',
	                formatter: orderFormatter
            		},{
                    field: 'proxyName',
                    title: '<@spring.message "admin.withdraw.table.proxyName"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        let s = "";
                        if (value) {
                            s += value;
                        }else {
                            s += "-"
                        }
                        if (row.agentUser) {
                            s += '<br><span class="text text-success" title="<@spring.message "admin.withdraw.table.agentName"/>">' + row.agentName + "</span>"
                        }
                        return s;
                    }
                },{
                    field: 'degreeName',
                    title: '<@spring.message "admin.withdraw.table.degreeName"/>',
                    align: 'center',
                    valign: 'middle'
                }, {
                    field: 'realName',
                    title: '<@spring.message "admin.withdraw.table.realName"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: realNameFormatter
                }, {
                    field: 'cardNo',
                    title: '<@spring.message "admin.withdraw.table.cardNo"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: cardNoFormatter,
                    pageSummaryFormat: function (rows, aggsData) {
                        return '<@spring.message "admin.deposit.table.page.sum"/>:';
                    },
                    allSummaryFormat: function (rows, aggsData) {
                        return '<@spring.message "admin.deposit.table.all.sum"/>:<br/><@spring.message "admin.deposit.table.people.num"/>:';
                    }
                }, {
                    field: 'drawMoney',
                    title: '<@spring.message "admin.withdraw.table.drawMoney"/>',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: drawMoneyFormatter,
                    pageSummaryFormat: function (rows, aggsData) {
                        return getTotal(rows, "drawMoney");
                    },
                    allSummaryFormat: function (rows, aggsData) {
                        if (!aggsData) {
                            return "0.00" + "<br/> 0"
                        }
                        return (aggsData.totalMoney ? aggsData.totalMoney.toFixed(2) : "0.00") + "<br/>" + aggsData.drawTimes;;
                    }
                }, <#if onOffDrawFee>{
                field: 'feeMoney',
                title: '<@spring.message "admin.withdraw.table.feeMoney"/>',
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter,
                pageSummaryFormat: function (rows, aggsData) {
                    return getTotal(rows, "feeMoney");
                },
                allSummaryFormat: function (rows, aggsData) {
                    if (!aggsData) {
                        return "0.00"
                    }
                    return aggsData.feeMoney ? aggsData.feeMoney.toFixed(2) : "0.00";
                }
            },</#if> {
                    field: 'createDatetime',
                    title: '<@spring.message "admin.withdraw.table.createDatetime"/>',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: Fui.formatDatetime
                }, {
                    field: 'opDatetime',
                    title: '<@spring.message "admin.withdraw.table.opDatetime"/>',
                    align: 'center',
                    valign: 'middle',
                    sortable: true,
                    formatter: Fui.formatDatetime
                }, {
                    field: 'status',
                    title: '<@spring.message "admin.withdraw.table.status"/>',
                    align: 'center',
                    valign: 'middle',
                    formatter: statusFormatter
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
        function realNameFormatter(value, row, index) {
        	<#if onOffCopybtn>
        	return  value+'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+value+'" style="color: #337ab7;"></i>';
            <#else>
        	return  value;
            </#if>
        }
        function drawMoneyFormatter(value, row, index) {
            if (value === undefined) {
                return "0.00";
            }
            if (value > 0) {
                if (value >= 10000 && value < 100000) {
                    <#if onOffCopybtn>
                    return '<span class="text-danger">' + Fui.toDecimal(value, 2) + '</span>'+'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+Fui.toDecimal(value, 2)+'" style="color: #337ab7;"></i>';
                    <#else>
                    return '<span class="text-danger">' + Fui.toDecimal(value, 2) + '</span>';
                    </#if>
                } else if (value >= 100000){
                    <#if onOffCopybtn>
                    return '<span class="text-warning">' + Fui.toDecimal(value, 2) + '</span>'+'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+Fui.toDecimal(value, 2)+'" style="color: #337ab7;"></i>';
                    <#else>
                    return '<span class="text-warning">' + Fui.toDecimal(value, 2) + '</span>';
                    </#if>
                }else {
                    <#if onOffCopybtn>
                    return '<span class="text-primary">' + Fui.toDecimal(value, 2) + '</span>'+'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+Fui.toDecimal(value, 2)+'" style="color: #337ab7;"></i>';
                    <#else>
                    return '<span class="text-primary">' + Fui.toDecimal(value, 2) + '</span>';
                    </#if>
                }


            }
            return '<span class="text-primary">0.00</span>';

        }

        function cardNoFormatter(value, row, index) {
        	<#if onOffCopybtn>
        	return row.bankName +'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+row.bankName+'" style="color: #337ab7;"></i>'+ "<br>" + value+'&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+value+'" style="color: #337ab7;"></i>';
            <#else>
        	return row.bankName + "<br>" + value;
            </#if>
        }

        function orderFormatter(value, row, index) {
            var s ='';
        	<#--<#if onOffCopybtn>
        	s= '<a class="<#if onOffNewAccdetail>open-tab<#else>open-dialog</#if>" href="${proxyBase}/user/detail<#if onOffNewAccdetail>New</#if>.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username +'</span></a>&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+row.username+'" style="color: #337ab7;"></i>';
            <#else>
        	s= '<a class="<#if onOffNewAccdetail>open-tab<#else>open-dialog</#if>" href="${proxyBase}/user/detail<#if onOffNewAccdetail>New</#if>.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username +'</span></a>';
            </#if>-->
            s = '<a class="open-tab text-danger" data-refresh="true" href="${proxyBase}/user/detail.do?id=' + row.userId + '" title="<@spring.message "admin.deposit.table.detail"/>" id="fui_tab_detailNew" style="overflow:scroll;"><span class="text-danger">' + row.username +'</span></a>';
            if (row.first) {
                s += '&nbsp;&nbsp<span class="label label-success"><@spring.message "admin.deposit.table.first"/></span>';
            }
            if (row.warning) {
                s += '&nbsp;&nbsp<span class="label label-danger"><@spring.message "admin.deposit.table.warn"/></span>';
            }
            <#if onOffCopybtn>
            s+= '<br>' + value + '&nbsp;&nbsp<i class="fa fa-files-o need-copy" data-clipboard-text="'+row.orderId+'" style="color: #337ab7;"></i>';
            <#else>
            s+= '<br>' + value;
            </#if>
            return s;
        }

        function betCheckFormatter(value, row, index) {
            <#--var check = '<a class="open-tab" title="彩票投注记录" href="${proxyBase}/lotOrder/index.do?username=' + row.username + '">彩票</a>&nbsp;&nbsp;';-->
            <#--if ('${onOffReal}' == 'true') {-->
            <#--    check += '<a class="open-tab" title="真人投注记录" href="${proxyBase}/realOrder/index.do?username=' + row.username + '">真人</a>&nbsp;&nbsp;';-->
            <#--}-->
            <#--if ('${onOffEgame}' == 'true') {-->
            <#--    check += '<a class="open-tab" title="电子投注记录" href="${proxyBase}/egameOrder/index.do?username=' + row.username + '">电子</a><br>';-->
            <#--}-->
            <#--if ('${onOffShaba}' == 'true') {-->
            <#--    check += '<a class="open-tab" title="第三方体育投注记录" href="${proxyBase}/thirdSportRecord/index.do?username=' + row.username + '">第三方体育</a>&nbsp;&nbsp;';-->
            <#--}-->
            <#--if ('${onOffSport}' == 'true') {-->
            <#--    check += '<a class="open-tab" title="体育投注记录" href="${proxyBase}/sportOrder/index.do?username=' + row.username + '">体育</a>&nbsp;&nbsp;';-->
            <#--}-->
            <#--if ('${onOffChess}' == 'true') {-->
            <#--    check += '<a class="open-tab" title="棋牌投注记录" href="${proxyBase}/chessOrder/index.do?username=' + row.username + '">棋牌</a>';-->
            <#--}-->
            <#--return check;-->
        }

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return "0.00";
            }
            if (value > 0) {
                return '<span class="text-danger">' + Fui.toDecimal(value, 2) + '</span>';
            }
            return '<span class="text-primary">0.00</span>';

        }

        function getTotal(rows, itemKey) {
            var total = 0;
            for (var i = 0; i < rows.length; i++) {
                var r = rows[i];
                if (!r[itemKey] || (r.status != 2 && r.status != 1)) {
                    continue;
                }
                total += r[itemKey];
            }
            return total.toFixed(2) + "";
        }

        var allStatus = {
            1: '<span class="text-success"><@spring.message "admin.withdraw.status.undo"/></span>',
            2: '<span class="text-primary"><@spring.message "admin.withdraw.status.success"/></span>',
            3: '<span class="text-danger"><@spring.message "admin.withdraw.status.fail"/></span>',
            4: '<span class="text-default"><@spring.message "admin.withdraw.status.timeout"/></span>',
            5: '<span class="text-default"><@spring.message "admin.withdraw.status.doing"/></span>'
        };

        function statusFormatter(value, row, index) {

            if (value == 1 && row.lockFlag == 2 && row.payId === null) {
                return allStatus[10];
            } else if (value == 1 && row.lockFlag == 2 && row.payId !== null) {
                return allStatus[5];
            }
            return allStatus[value];
        }

        function operateFormatter(value, row, index) {
            var str = "";
            var cancel = "";
            if (value == 1 && row.status == 1) {
                return ['<a class="todo-ajax" href="${proxyBase}/finance/withdraw/lock.do?lockFlag=2&id=', row.id, '" ><@spring.message "admin.deposit.status.be.lock"/></a>'].join('');
            } else if (value == 2 && row.status == 1 && row.type != 2) {
                if (baseInfo.username == row.opUsername) {
                    if ($("#paySwitch").val()) {
                        str += "<a class='open-dialog' href='${proxyBase}/finance/withdraw/handle.do?type=2&id=" + row.id + "', ' title='代付处理'>   代付处理</a>";
                    }
                    return ['<a class="todo-ajax" href="${proxyBase}/finance/withdraw/lock.do?lockFlag=1&id=', row.id, '" title="<@spring.message "admin.deposit.status.be.unlock"/>"><@spring.message "admin.deposit.status.be.unlock"/></a>  ',
                        '<a class="open-dialog" href="${proxyBase}/finance/withdraw/handle.do?id=', row.id, '" title="<@spring.message "admin.withdraw.table.handle"/>"><@spring.message "admin.withdraw.table.handle"/></a>', str].join('');
                }
                return "<@spring.message "admin.deposit.status.has.bean.lock"/>";
            }else if (value == 2 && row.status == 1 && row.type == 2) {
                if ($("#cancelPay").val()) {
                    cancel += "<a class='todo-ajax' href='${proxyBase}/finance/withdraw/cancelPay.do?payId=" + row.id + "', ' title='取消前请与第三方确认无法出款，出款概不负责'>   取消</a>";
                }
                return ['<a class="todo-ajax" href="${proxyBase}/finance/withdraw/search.do?payId=', row.id, '" title="查询代付">查询代付</a>', cancel].join('');
            }
            return "已操作";
        }
        function copyFun(){
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


    function descFormatter(value, row, index) {
        if (row.status === 2 || row.status === 3) {
            if (value === undefined || value === "") {
                return '<a class="open-dialog text-danger"  href="${proxyBase}/finance/withdraw/remarkModify.do?id=' + row.id + '" title="' + value + '"><@spring.message "admin.deposit.remark.add"/></a>';
            }
            var enterIndex = 6;
            if (value && value.length > enterIndex) {
                return '<a class="open-dialog"  href="${proxyBase}/finance/withdraw/remarkModify.do?id=' + row.id + '" title="' + value + '">' + value.substring(0, 6) + '.....</a>';
            }
            return '<a class="open-dialog"  href="${proxyBase}/finance/withdraw/remarkModify.do?id=' + row.id + '" title="' + value + '">' + value + '</a>';
        }
        return value;
    }

    $("#checkWithDrawOrderStatus").click(function () {
        $("#withdrawButton").click();
    })

</script>
