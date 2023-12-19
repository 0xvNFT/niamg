<form class="fui-search table-tool" method="post" id="report_finance_form_id">
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
                <input type="text" class="form-control" name="agentName" placeholder="<@spring.message "admin.belong.proxy.query"/>">
            </div>
            <div class="input-group">
                <label class="sr-only" for="account"><@spring.message "admin.simple.user.query"/></label>
                <input type="text" class="form-control" name="username" value="${username}" placeholder="<@spring.message "admin.simple.user.query"/>">
            </div>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "manager.check.select"/></button>
            <button class="btn btn-primary reset" type="button"><@spring.message "manager.resetting"/></button>
            <#if permAdminFn.hadPermission("admin:moneyReport:export")>
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
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<#if permAdminFn.hadPermission("admin:moneyReport:export")><div class="hidden">
    <form id="money_report_rd_export_fmId" action="${adminBase}/moneyReport/export.do" target="_blank" method="post">
        <input type="hidden" name="username"/>
        <input type="hidden" name="proxyName"/>
        <input type="hidden" name="startTime"/>
        <input type="hidden" name="endTime"/>
    </form>
</div></#if>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#report_finance_form_id");
        <#if permAdminFn.hadPermission("admin:moneyReport:export")>$form.find(".exportBtn").click(function () {
            var $form1 = $("#money_report_rd_export_fmId");
            $form1.find("[name='username']").val($form.find("[name='username']").val());
            $form1.find("[name='startTime']").val($form.find("[name='startTime']").val());
            $form1.find("[name='endTime']").val($form.find("[name='endTime']").val());
            $form1.find("[name='proxyName']").val($form.find("[name='proxyName']").val());
            $form1.submit();
        });</#if>
        Fui.addBootstrapTable({
            url: '${adminBase}/moneyReport/list.do',
            columns: [{
                field: 'username',
                title: Admin.uerAccount,
                align: 'center',
                valign: 'middle',
                formatter: usernameFormatter
            }, {
                field: 'userType',
                title: Admin.memberType,
                align: 'center',
                valign: 'middle',
                formatter: typeFormatter
            }, {
            	field: 'proxyName',
                title: Admin.belongProxy,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'depositAmount',
                title: Admin.onlineDepositTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'depositArtificial',
                title: Admin.handAddMoney,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'subGiftAmount',
                title: Admin.colorMoneyOut,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'withdrawAmount',
                title: Admin.onlineDrawTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: withdrawFormatter
            }, {
                field: 'withdrawArtificial',
                title: Admin.handDeduce,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'liveRebateAmount',
                title: Admin.backWaterTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'proxyRebateAmount',
                title: Admin.backPointTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'depositGiftAmount',
                title: Admin.depositGiveTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'activeAwardAmount',
                title: Admin.actGiveTot,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }, {
                field: 'giftOtherAmount',
                title: Admin.otherAddMoeny,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: moneyFormatter
            }]
            , onLoadSuccess: function (d, r) {
                if (r && r.msg) {
                    layer.msg(r.msg);
                }
                $form.find(".search-btn").prop("disabled", false).html('<@spring.message "admin.deposit.button.search"/>');
                $form.find(".reset").prop("disabled", false).html('<@spring.message "manager.resetting"/>');
                return false;
            }
            , queryParams: function (params) {
                $form.find(".search-btn").prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                $form.find(".reset").prop("disabled", true).html('<img src="${base}/common/js/layer/theme/default/loading-2.gif" width="26" height="20">');
                return params;
            }
        });
        function usernameFormatter(value, row, index) {
            return '<a class="open-tab" href="${adminBase}/user/detail.do?id='+row.userId+'" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew">' + value + '</a>';
         }
        function withdrawFormatter(value, row, index) {
            if (value === undefined) {
                return "<span class='text-primary'>0.00</span>";
            }
            return '<span class="text-danger">'+Fui.toDecimal(Math.abs(value), 2)+'</span>';
        }

        function typeFormatter(value, row, index) {
            var name = '<@spring.message "admin.unknow"/>';
            switch (value) {
                case 130:
                    name = '<@spring.message "admin.withdraw.type.member"/>';
                    break;
                case 120:
                    name = '<@spring.message "admin.withdraw.type.proxy"/>';
                    break;
            }
            return name;
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
