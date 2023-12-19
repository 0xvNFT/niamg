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
                <input type="text" class="form-control" name="username" value="${username}" placeholder="<@spring.message "admin.deposit.handle.username"/>">
            </div>
            <div class="input-group">
                <label class="sr-only" for="type"><@spring.message "admin.deposit.table.depositType"/></label>
                <select class="form-control selectpicker" title="<@spring.message "admin.deposit.type.all"/>" data-live-search="true" id="memmnyrd_type_id" multiple data-selected-text-format="count>2">
                    <#list types as ty><option value="${ty.type}">${ty.name}</option></#list>
                </select>
                <input name="type" value="${moneyType}" id="memmnyrd_type_str_id" type="hidden">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="orderId" placeholder="<@spring.message "admin.money.history.orderId"/>">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="agentUser" value="${agentUser}" placeholder="<@spring.message "admin.withdraw.agentUser"/>">
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
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
            <div class="input-group">
                <input type="text" class="form-control" name="proxyName" value="${proxyName}" placeholder="<@spring.message "admin.deposit.parent"/>">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="operatorName" value="${operatorName}" placeholder="<@spring.message "admin.deposit.opuser"/>" style="width: 120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="minMoney" value="${minMoney}" placeholder="<@spring.message "admin.money.history.min"/>" style="width: 150px;">
            </div>
            <@spring.message "admin.until"/>
            <div class="input-group">
                <input type="text" class="form-control" name="maxMoney" value="${maxMoney}" placeholder="<@spring.message "admin.money.history.max"/>" style="width: 150px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="remark"  placeholder="<@spring.message "admin.money.history.remark"/>" style="width: 150px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="referrer"  placeholder="<@spring.message "admin.recommendUsername"/>" style="width: 150px;">
            </div>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'], function () {
    var moneyType = '${moneyType}',$selctpicker = $("#memmnyrd_type_id"),$types=$("#memmnyrd_type_str_id"),types={};
    <#list types as ty>types["${ty.type}"]="${ty.name}";</#list>
    $selctpicker.change(function () {
        var val = $(this).val();
        if (val && val !== '') {
            $types.val(val.join(","));
        } else {
            $types.val("");
        }
    });
    setTimeout(function () {
        if (moneyType || moneyType !== '') {
            $selctpicker.selectpicker('val', moneyType.split(',')).trigger("change");
        }
    }, 400);
    Fui.addBootstrapTable({
        showPageSummary: true,
        showAllSummary: true,
        showFooter: true,
        url: '${proxyBase}/finance/moneyHistory/list.do',
        columns: [{
            field: 'id',
            title: '<@spring.message "admin.money.history.id"/>',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'username',
            title: '<@spring.message "admin.money.history.username"/>',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'type',
            title: '<@spring.message "admin.money.history.type"/>',
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
		        return types[value];
		    }
        }, {
            field: 'beforeMoney',
            title: '<@spring.message "admin.money.history.beforeMoney"/>',
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return '<@spring.message "admin.money.history.pageSum"/>:';
            },
            allSummaryFormat: function (rows, aggsData) {
                return '<@spring.message "admin.money.history.allSum"/>:';
            }
        }, {
            field: 'money',
            title: '<@spring.message "admin.money.history.money"/>',
            align: 'center',
            valign: 'middle',
            sortable: true,
            formatter: changeMoneyFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return getTotal(rows, 'money');
            },
            allSummaryFormat: function (rows, aggsData) {
                if (!aggsData) {
                    return "0.00" + "<br/> 0"
                }
                return (aggsData.money ? aggsData.money.toFixed(2) : "0.00") ;
            }
        }, {
            field: 'afterMoney',
            title: '<@spring.message "admin.money.history.afterMoney"/>',
            align: 'center',
            valign: 'middle',
            formatter: moneyFormatter
        }, {
            field: 'createDatetime',
            title: '<@spring.message "admin.money.history.createDatetime"/>',
            align: 'center',
            valign: 'middle',
            width: '150px',
            formatter: Fui.formatDatetime
        }, {
            field: 'orderId',
            title: '<@spring.message "admin.money.history.orderId"/>',
            align: 'center',
            valign: 'middle',
            width: '150px'
        }, {
            field: 'operatorName',
            title: '<@spring.message "admin.money.history.operatorName"/>',
            align: 'center',
            valign: 'middle'
        }, {
            field: 'remark',
            title: '<@spring.message "admin.money.history.remark"/>',
            align: 'center',
            valign: 'middle',
            formatter: remarkFormatter
        }]
    });
    function remarkFormatter(value, row, index) {
        if (!value) {
            return "";
        }
        if (value.length < 20) {
            return value;
        }
       return "";
    }
    function getTotal(rows, itemKey) {
        var total = 0;
        for (var i = 0; i < rows.length; i++) {
            var r = rows[i];
            if (!r[itemKey]) {
                continue;
            }
            total += r[itemKey];
        }
        return total.toFixed(2) + "";
    }
    function moneyFormatter(value, row, index) {
        if (!value) {
            return value;
        }
        if (value > 0) {
            return '<span class="text-primary">'+parseFloat(value.toFixed(4))+'</span>';
        }
        return '<span class="text-primary">0</span>';
    }
    function changeMoneyFormatter(value, row, index) {
        if (value === undefined) {
            return value;
        }
        if (row.add) {
            return '<span style="color:green">'+parseFloat(value.toFixed(4))+'</span>';
        } else {
            return '<span style="color:red">'+parseFloat(value.toFixed(4))+'</span>';
        }
    }
});
</script>
