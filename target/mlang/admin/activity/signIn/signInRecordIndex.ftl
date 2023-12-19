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
	            <input type="text" class="form-control" name="signIp" placeholder="<@spring.message "admin.sign.ip"/>">
	        </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
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
        url: '${adminBase}/signInRecord/list.do',
        columns: [{
            field: 'username',
            title: Admin.memberAcc,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'signDays',
            title: Admin.signDays,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'ip',
            title: Admin.signIp,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'score',
            title: Admin.jackScores,
            align: 'center',
            valign: 'middle'
        },{
            field: 'money',
            title: Admin.jackCash,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'signDate',
            title: Admin.signTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }]
    });
});
</script>
