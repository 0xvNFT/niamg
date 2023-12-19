<form class="fui-search table-tool" method="post" id="risk${gameType}FormId">
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
	var $form=$("#riskLiveFormId");
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
        url: '${adminBase}/risk${gameType}/list.do',
        showPageSummary: true,
        showAllSummary: false,
        showFooter: true,
        columns: [{
            field: 'username',
            title: Admin.uerAccount,
            align: 'center',
            valign: 'middle',
            formatter: usernameFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return "<@spring.message "admin.total"/>";
            }
        },{
            field: 'userType',
            title: Admin.type,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        },{
            field: 'realName',
            title: Admin.userName,
            align: 'center',
            valign: 'middle'
        },{
	        field: 'degreeName',
	        title: Admin.memberLevel,
	        align: 'center',
	        valign: 'middle'
	    },{
	        field: 'betAmount',
	        sortable: true,
	        title: Admin.drawMoneyPay,
	        align: 'center',
	        valign: 'middle',
	        formatter: moneyFormatter,
	        pageSummaryFormat: function (rows, aggsData) {
	           return getTotal(rows,'betAmount');
	        }
	    },{
	        field: 'winAmount',
	        sortable: true,
	        title: Admin.jackPotWin,
	        align: 'center',
	        valign: 'middle',
	        formatter: moneyFormatter,
	        pageSummaryFormat: function (rows, aggsData) {
	        	return getTotal(rows,'winAmount');
	        }
	    },{
	        field: 'profit',
	        title: Admin.earnLose,
	        align: 'center',
	        valign: 'middle',
	        sortable: true,
	        formatter: moneyFormatter,
	        pageSummaryFormat: function (rows, aggsData) {
	            return getTotal(rows, 'profit');
	        }
	    },{
	        field: 'betTimes',
	        title: Admin.drawFeeCount,
	        align: 'center',
	        valign: 'middle',
	        sortable: true,
	        pageSummaryFormat: function (rows, aggsData) {
	            return getTotal(rows, 'betTimes',1);
	        }
	    },{
	        field: 'winTimes',
	        title: Admin.jackPotWinCount,
	        align: 'center',
	        valign: 'middle',
	        sortable: true,
	        pageSummaryFormat: function (rows, aggsData) {
	            return getTotal(rows, 'winTimes',1);
	        }
	    },{
	        field: 'winRate',
	        title: Admin.jackPotPer,
	        align: 'center',
	        valign: 'middle',
	        sortable: true,
	        formatter: function (value, row, index) {
                return handlerWinRate(row['betTimes']||0,row['winTimes']||0);
            },
            pageSummaryFormat: function (rows, aggsData) {
                return "--";
            }

	     }, {
	        field:'rebateAmount',
	        title: Admin.reedBackValue,
	        align: 'center',
	        valign: 'middle',
	        sortable: true,
	        formatter: moneyFormatter,
	        pageSummaryFormat: function (rows, aggsData) {
	            return getTotal(rows, 'rebateAmount');
	        }
        }]
    });
    function usernameFormatter(value, row, index) {
        return '<a class="open-tab" href="${adminBase}/user/detail.do?id='+row.userId+'" title="<@spring.message "admin.check.member.info"/>"  id="fui_tab_detailNew"><span class="text-danger">'+value+'</span></a>';
    }
    var memType = {
        130: "<@spring.message "admin.withdraw.info.member"/>",
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
        return ~~((nowTimes - date) / (24 * 60 * 60 * 1000)) + "天<@spring.message ""/>";
    }
    function getTotal(rows,field,isMoney){
		var total = 0;
	    for (var i = 0; i < rows.length; i++) {
	    	var r = rows[i];
	        if (!r[field]) {
	            continue;
	        }
	        total += r[field];
	    }
	    if(isMoney){
	    	return moneyFormatter(total.toFixed(2));
	    }
       	return total;
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
    function handlerWinRate(betTimes,winTimes) {
        try {
            if (betTimes == 0 && winTimes > 0) {
                return "<span class='text-danger'>100%</span>";
            }
            if (winTimes == 0) {
                return "<span class='text-primary'>0</span>"
            }
            var winRate = Fui.toDecimal((~~(winTimes / betTimes * 10000)) / 100, 2);//乘于10000取整再除于100
            if (winRate > 70) {
                return "<span class='text-danger'>" + winRate + "%</span>";
            }
            if (winRate > 50) {
                return "<span class='text-warning'>" + winRate + "%</span>";
            }
            if (winRate == 0) {
                return "<span class='text-primary'>0</span>";
            }
            return "<span class='text-success'>" + winRate + "%</span>";
        } catch (e) {
        }
    }
});
</script>