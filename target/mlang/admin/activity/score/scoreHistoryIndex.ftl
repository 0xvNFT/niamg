<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${startTime}" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.deposit.handle.username"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="type">
	                <option value=""><@spring.message "admin.deposit.type.all"/></option>
	            <#list scoreTypes as ty>
	                <option value="${ty.type}">${ty.name}</option>
	            </#list>
            </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
        <#if permAdminFn.hadPermission("admin:scoreHistory:add")>
            <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/scoreHistory/showAdd.do"><@spring.message "admin.menu.scoreHis.add"/></button>
            <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/scoreHistory/showBatchAdd.do"><@spring.message "admin.bat.score"/></button>
        </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${endTime}" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
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
        url: '${adminBase}/scoreHistory/list.do',
        columns: [{
            field: 'username',
            title: Admin.memberAcc,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'type',
            title: Admin.changeType,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        }, {
            field: 'beforeScore',
            title: Admin.beforeScoreEx,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'score',
            title: Admin.exchangeScores,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'afterScore',
            title: Admin.afterScoreEx,
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'createDatetime',
            title: Admin.changeTimeSys,
            align: 'center',
            valign: 'middle',
            width: '150px',
            formatter: Fui.formatDatetime
        }, {
            field: 'remark',
            title: Admin.remark,
            align: 'left',
            valign: 'middle'
        }]
    });
    var typeData={};<#list scoreTypes as ty>typeData["${ty.type}"]="${ty.name}";</#list>
    function typeFormatter(value, row, index) {
        return typeData['type'+value+'value'];
    }

    function moneyFormatter(value, row, index) {
        if (value === undefined) {
            return value;
        }
        if (value > 0) {
            return ['<span class="text-danger">', '</span>'].join(value);
        }
        return ['<span class="text-primary">', '</span>'].join(value);
    }
});
</script>
