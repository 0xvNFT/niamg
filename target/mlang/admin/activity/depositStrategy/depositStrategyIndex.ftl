<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <select class="form-control" name="depositType">
                    <option value=""><@spring.message "admin.all.pay.styles"/></option>
                    <option value="7"><@spring.message "admin.deposit.type.bank"/></option>
                    <option value="1"><@spring.message "admin.deposit.type.manual"/></option>
                    <option value="5"><@spring.message "admin.deposit.type.online"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="giftType">
                    <option value="0"><@spring.message "admin.all.give.types"/></option>
                    <option value="1"><@spring.message "admin.fixed.num"/></option>
                    <option value="2"><@spring.message "admin.float.scale"/></option>
                </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
        <#if permAdminFn.hadPermission("admin:depositStrategy:add")>
            <a class="btn btn-primary open-dialog" type="button" url="${adminBase}/depositStrategy/showAdd.do?type=1"><@spring.message "admin.add.hand.cash.strategy"/></a>
            <a class="btn btn-primary open-dialog" type="button" url="${adminBase}/depositStrategy/showAdd.do?type=7"><@spring.message "admin.add.bank.cash.strategy"/></a>
            <a class="btn btn-primary open-dialog" type="button" url="${adminBase}/depositStrategy/showAdd.do?type=5"><@spring.message "admin.add.online.cash.strategy"/></a>
        </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <div class="input-group">
                <select class="form-control" name="valueType">
                    <option value=""><@spring.message "admin.all.give.types"/></option>
                    <option value="1"><@spring.message "admin.color.cash"/></option>
                    <option value="2"><@spring.message "admin.scores"/></option>
                </select>
            </div>
        </div>
    </div>
</form>
<div style="padding: 10px;">
    <div style="color:red;"><@spring.message "admin.query.condition.act.begin"/></div>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: "${adminBase}/depositStrategy/list.do",
            columns: [{
                field: 'depositType',
                title: Admin.depositType,
                align: 'center',
                formatter: dtFormatter
            }, {
                field: 'giftType',
                title: Admin.giveType,
                align: 'center',
                formatter: gtFormatter
            }, {
                field: 'valueType',
                title: Admin.giveWay,
                align: 'center',
                formatter: vtFormatter
            }, {
                field: 'depositCount',
                title: Admin.giveRate,
                align: 'center',
                formatter: dcFormatter
            }, {
                field: 'giftValue',
                title: Admin.giveVal,
                align: 'center',
                formatter: giftValueFormatter
            }, {
                field: 'bonusBackValue',
                title: Admin.backGiveVal,
                align: 'center',
                formatter: backGiftValueFormatter
            }
            , {
                field: 'upperLimit',
                title: Admin.giveUp,
                align: 'center',
                formatter: limitFormatter
            }, {
                field: 'betRate',
                title: Admin.turnMultiple,
                align: 'center',
                formatter: betMulFormatter
            }, {
                field: 'bonusBackBetRate',
                title: Admin.backTurnMultiple,
                align: 'center',
                formatter: betMulFormatter
            }, {
                field: 'backBonusLevelDiff',
                title: Admin.bonusBacklevelDistance,
                align: 'center',
                formatter: backBonusLevelDiffFormatter
            }
            , {
                field: 'status',
                title: Admin.status,
                align: 'center',
                formatter: statusFormatter
            }, {
                title: Admin.depositMoney,
                align: 'center',
                formatter: comMoneyFormatter
            }, {
                field: 'startDatetime',
                title: Admin.beginTime,
                align: 'center',
                formatter: Fui.formatDate
            }, {
                field: 'endDatetime',
                title: Admin.endTime,
                align: 'center',
                formatter: Fui.formatDate
            }, {
                title: Admin.op,
                align: 'center',
                formatter: operateFormatter
            }]
        });

        function dtFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            switch (value) {
                case 7:
                    return "<@spring.message "admin.deposit.type.bank"/>";
                case 1:
                    return "<@spring.message "admin.deposit.type.manual"/>";
                case 5:
                    return "<@spring.message "admin.deposit.type.online"/>";
            }
        }

        function gtFormatter(value, row, index) {
            if (value == 2) {
                return "<@spring.message "admin.float.scale"/>";
            }
            return "<@spring.message "admin.fixed.num"/>";
        }

        function vtFormatter(value, row, index) {
            if (value == 2) {
                return "<@spring.message "admin.scores"/>";
            }
            return "<@spring.message "admin.color.cash"/>";
        }

        function giftValueFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            if (row.giftType == 2) {
                return value + "%";
            } else {
                if (row.valueType == 2) {
                    return value + "<@spring.message "admin.scores"/>";
                }
                return value + "<@spring.message "admin.deposit.table.money.unit"/>";
            }
        }

        function backGiftValueFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            if (row.backGiftType == 2) {
                return value + "%";
            } else {
                if (row.valueType == 2) {
                    return value + "<@spring.message "admin.scores"/>";
                }
                return value + "<@spring.message "admin.deposit.table.money.unit"/>";
            }
        }

        function limitFormatter(value, row, index) {
            if (row.giftType == 2) {
                if (!value || value == 0) {
                    return "<@spring.message "admin.no.limit"/>";
                }
                var un = "<@spring.message "admin.deposit.table.money.unit"/>";
                if (row.valueType == 2) {
                    un = "<@spring.message "admin.scores"/>";
                }
                return "<@spring.message "admin.max.seed.give"/>" + value + un;
            }
            return "-";
        }

        function betMulFormatter(value, row, index) {
            if (!value) return "0";
            return Fui.toDecimal(value, 2);
        }

        function backBonusLevelDiffFormatter(value, row, index) {
            if (!value) {
                return "-";
            }
            if (row.backGiftType == 2) {
                return value + "%";
            } else {
                if (row.valueType == 2) {
                    return value + "<@spring.message "admin.scores"/>";
                }
                return value + "<@spring.message "admin.deposit.table.money.unit"/>";
            }
        }

        function dcFormatter(value, row, index) {
            if (!value || value == 0) {
                return "<@spring.message "admin.every.one"/>";
            }
            switch (value) {
                case 1:
                    return "<@spring.message "admin.first.pay"/>";
                case 444:
                    return "<@spring.message "admin.daily.first.charge"/>";
                case 2:
                    return "<@spring.message "admin.second.pay"/>";
                case 3:
                    return "<@spring.message "admin.third.pay"/>";
                case 222:
                    return "<@spring.message "admin.two.pay.before"/>";
                case 333:
                    return "<@spring.message "admin.three.pay.before"/>";
            }
        }

        function comMoneyFormatter(value, row, index) {
            var min = row.minMoney, max = row.maxMoney;
            if (!min && !max) {
                return "<@spring.message "admin.not.limited"/>";
            }
            var un = "<@spring.message "admin.deposit.table.money.unit"/>";
            if (!min && max) {
                return "<@spring.message "admin.less.equal"/>" + max + un;
            }
            if (min && !max) {
                return "<@spring.message "admin.more"/>" + min + un;
            }
            return "<@spring.message "admin.more"/>" + min + un + "<br><@spring.message "admin.less.equal.and"/>" + max + un;
        }

        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/depositStrategy/updStatus.do?id=" + row.id + "&status="
            });
        }

        function operateFormatter(value, row, index) {
            return '<a class="open-dialog" href="${adminBase}/depositStrategy/showModify.do?id=' + row.id + '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><@spring.message "admin.deposit.bank.bankCard.modify"/></a>    ' +
                    '<a class="todo-ajax" href="${adminBase}/depositStrategy/delete.do?id=' + row.id + '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
        }
    });
</script>
