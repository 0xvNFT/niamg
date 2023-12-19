<form class="fui-search table-tool" method="post" id="riskLoginFormId">
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
                <input type="text" class="form-control" name="username" value="${username}" placeholder="<@spring.message "admin.simple.user.query"/>">
            </div>
            <div class="input-group">
                <select class="form-control selectpicker" data-live-search="true" title="<@spring.message "admin.deposit.all.degree"/>" multiple name="degreeIdSelect">
                    <#list degrees as d>
                        <option value="${d.id}">${d.name}</option>
                    </#list>
                </select>
                <input type="hidden" name="degreeIds">
            </div>
            <button class="btn btn-primary search-btn fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
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
                <input type="text" class="form-control" name="proxyName" placeholder="<@spring.message "admin.proxy.down.line.query"/>">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentName" placeholder="<@spring.message "admin.withdraw.agentUser"/>">
            </div>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'],function(){
	var $form=$("#riskLoginFormId");
	$form.find("[name='degreeIdSelect']").change(function () {
        var types = $(this).val();
        if (types) {
            types = types.join(",");
            $form.find("[name='degreeIds']").val(types);
        } else {
            $form.find("[name='degreeIds']").val("");
        }
    });
    Fui.addBootstrapTable({
        url: '${adminBase}/riskLogin/list.do',
        columns: [{
            field: 'username',
            title: Admin.uerAccount,
            align: 'center',
            valign: 'middle',
            formatter: usernameFormatter
        },{
            field: 'type',
            title: Admin.type,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        },{
	        field: 'degreeName',
	        title: Admin.memberLevel,
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'createDatetime',
	        title: Admin.registerTime,
	        align: 'center',
	        valign: 'middle',
	        sortable:true,
	        formatter: Fui.formatDatetime
	    },{
	        field: 'lastLoginDatetime',
	        title: Admin.lastLoginTime,
	        align: 'center',
	        valign: 'middle',
	        sortable:true,
	        formatter: Fui.formatDatetime
	    },{
	        title: Admin.notLoginTime,
	        align: 'center',
	        valign: 'middle',
	        formatter: lastDateFormatter
	    },{
	        field: 'registerIp',
	        title: Admin.registerIp,
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'lastLoginIp',
	        title: Admin.lastLoginIp,
	        align: 'center',
	        valign: 'middle'
        }]
    });
    function usernameFormatter(value, row, index) {
        return '<a class="open-tab" href="${adminBase}/user/detail.do?id='+row.id+'" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew"><span class="text-danger">'+value+'</span></a>';
    }
    var memType = {
        130: "<@spring.message "admin.withdraw.type.member"/>",
        120: "<@spring.message "admin.withdraw.type.proxy"/>"
    };
    function typeFormatter(value, row, index) {
        return memType[value];
    }
    function lastDateFormatter(value, row, index) {
        var nowTimes = new Date().getTime();
        var lastLoginDatetime = row.lastLoginDatetime;
        if (!lastLoginDatetime) {
            lastLoginDatetime = row.createDatetime;
        }
        var date = new Date(lastLoginDatetime).getTime();
        return ~~((nowTimes - date) / (24 * 60 * 60 * 1000)) + "<@spring.message "admin.unit.day"/>";
    }
});
</script>