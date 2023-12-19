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
                <select class="form-control" name="type">
                <option value=""><@spring.message "admin.deposit.type.all"/></option>
                <#list types as t><option value="${t.type}">${t.title}</option></#list>
            </select>
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
                <input type="text" class="form-control" name="proxyName"  placeholder="<@spring.message "admin.proxy.down.line.query"/>">
            </div>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<div style="font-size:15px;color:red;margin-top:6px"><@spring.message "admin.all.game.bet.num.change"/></div>
<script type="text/javascript">
requirejs(['jquery','bootstrap','Fui'],function(){
var types={};<#list types as t>types["${t.type}"]="${t.title}";</#list>
    Fui.addBootstrapTable({
        url : '${adminBase}/finance/betNumHistory/list.do',
        showPageSummary: true,
        showAllSummary: true,
        showFooter: true,
        columns : [ {
            field : 'username',
            title : Admin.memberAcc,
            align : 'center',
            valign : 'middle'
        }, {
            field : 'type',
            title : Admin.changeType,
            align : 'center',
            valign : 'middle',
            formatter :  function(value, row, index) {
            	return types[''+value];
            }
        }, {
            field : 'beforeNum',
            title : Admin.beforeBetNum,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return "<@spring.message "admin.deposit.table.page.sum"/>:";
            },
            allSummaryFormat: function (rows, aggsData) {
                return "<@spring.message "admin.deposit.table.all.sum"/>:";
            }
        }, {
            field : 'betNum',
            title : Admin.changeBetNum,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter,
            pageSummaryFormat: function (rows, aggsData) {
                return getTotal(rows, 'betNum');
            },
            allSummaryFormat: function (rows, aggsData) {
                if (!aggsData) {
                    return "0.00"
                }
                return (aggsData.betNumCount ? aggsData.betNumCount.toFixed(2) : "0.00");
            }
        }, {
            field : 'afterNum',
            title : Admin.afterBetNum,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter
        }, {
            field : 'createDatetime',
            title : Admin.exchangeDate,
            align : 'center',
            valign : 'middle',
            width : '150px',
            formatter : Fui.formatDatetime
        },
        //     {
        //     field : 'createDatetimeStr',
        //     title : Admin.exchangeDate,
        //     align : 'center',
        //     valign : 'middle',
        //     width : '150px'
        // },
            {
            field : 'beforeDrawNeed',
            title : Admin.excBefNeed,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter
        }, {
            field : 'drawNeed',
            title : Admin.excDrawNeed,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter
        }, {
            field : 'afterDrawNeed',
            title : Admin.excAftDrawNeed,
            align : 'center',
            valign : 'middle',
            formatter : moneyFormatter
        }, {
            field : 'operatorName',
            title : Admin.operator,
            align : 'center',
            valign : 'middle'
         }, {
            field : 'orderId',
            title : Admin.orderNum,
            align : 'center',
            valign : 'middle'
        }, {
            field : 'remark',
            title : Admin.remark,
            align : 'left',
            valign : 'middle',
            formatter : remarkFormatter
        } ]
    });
    function moneyFormatter(value, row, index) {
        if (!value) {
            return value;
        }
        if (value > 0) {
            return '<span class="text-danger">'+value+'</span>';
        }
        return '<span class="text-primary">'+value+'</span>';
    }

    function remarkFormatter(value, row, index) {
        if (!value) {
            return "";
        }
        if (value.length < 6) {
            return value;
        }
        return '<a class="open-text" dialog-text="'+value+'" dialog-title="<@spring.message "admin.money.history.remark.detail"/>" title="'+value+'">'+value.substr(0,6)+'...</span>';
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
});
</script>
