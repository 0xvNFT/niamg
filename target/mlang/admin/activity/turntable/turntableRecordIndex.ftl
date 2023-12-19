<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${startTime} 00:00:00" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
				<select class="form-control" name="awardType">
                    <option value=""><@spring.message "admin.deposit.type.all"/></option>
                    <option value="4"><@spring.message "admin.scores"/></option>
                    <option value="2"><@spring.message "admin.banknote"/></option>
                    <option value="3"><@spring.message "admin.jack"/></option>
                </select>
			</div>
			<div class="input-group">
				<input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.deposit.handle.username"/>">
			</div>
			<button class="btn btn-primary search-btn fui-date-search"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline">
			<div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${endTime} 23:59:59" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
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
requirejs(['jquery', 'bootstrap', 'Fui'], function(){
	Fui.addBootstrapTable({
		url: '${adminBase}/turntableRecord/list.do',
		columns: [{
			field: 'turntableId',
            title: Admin.roundPan,
            align: 'center',
            valign: 'middle'
        }, {
			field: 'username',
            title: Admin.jackWin,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'awardType',
            title: Admin.jackTypes,
            align: 'center',
            valign: 'middle',
            formatter: awardTypeFormatter
        }, {
            field: 'giftName',
            title: Admin.jackName,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'createDatetime',
            title: Admin.createTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: awardStatusFormatter
        }, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: awardOpFormatter
        }, {
            field: 'remark',
            title: Admin.remarkIns,
            align: 'center',
            valign: 'middle'
        }]
	});
	function awardTypeFormatter(value, row, index) {
        if (value === 3) {
            return "<span class='text-primary'><@spring.message "admin.jack"/></span>";
        } else if (value === 4) {
            return "<span class='text-primary'><@spring.message "admin.scores"/></span>";
        }
        return "<span class='text-primary'><@spring.message "admin.banknote"/></span>";
    }
    function awardStatusFormatter(value, row, index) {
        if (value === 1) {
            return '<span class="text-success"><@spring.message "admin.deposit.status.ing"/></span>';
        } else if (value === 2) {
            return '<span class="text-primary"><@spring.message "admin.handle.process.success"/></span>';
        }
        return '<span class="text-danger"><@spring.message "admin.handle.process.fail"/></span>';
    }
    function awardOpFormatter(value, row, index) {
        if (row.status != 1) {
            return "";
        }
        return '<a class="open-dialog" href="${adminBase}/turntableRecord/showHandel.do?id=' + row.id + '" title="<@spring.message "admin.process"/>"><@spring.message "admin.process"/></a>';
    }
});
</script>