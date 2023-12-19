<div class="form-group table-tool">
    <#if permAdminFn.hadPermission("admin:signInRule:add")>
        <button class="btn btn-primary open-dialog" type="button" url="${adminBase}/signInRule2/showAdd.do"><@spring.message "admin.menu.add"/></button>
    </#if>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/signInRule2/list.do',
            columns: [{
                field: 'days',
                title: Admin.signDays,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'dayGiftConfig',
                title: Admin.jackCash,
                align: 'center',
                valign: 'middle',
                formatter: giftConfigFormatter
            }, {
                field: 'todayDeposit',
                title: Admin.dailyChargeLimit,
                align: 'center',
                valign: 'middle',
                formatter: depositFormatter
            }, {
                field: 'depositMoney',
                title: Admin.dailyMinCash,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'todayBet',
                title: Admin.dailyIsNotBet,
                align: 'center',
                valign: 'middle',
                formatter: depositFormatter
            }, {
                field: 'betNeed',
                title: Admin.minBetCash,
                align: 'center',
                valign: 'middle',
                formatter: betNeedFormatter
            },{
                field: 'degreeNames',
                title: Admin.vaildVipLevel,
                align: 'center',
                valign: 'middle'
            },{
                field: 'groupNames',
                title: Admin.vaildVipGroup,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'isReset',
                title: Admin.isNotReSet,
                align: 'center',
                valign: 'middle',
                formatter: depositFormatter
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
        function betNeedFormatter(value, row, index) {
            if (!row.betNeed) {
                return "-";
            }
            if (row.todayBet == 4) {
                return row.betNeed +"<@spring.message "admin.multiple.cash"/>";
            }
            return row.betNeed +"<@spring.message "admin.deposit.table.money.unit"/>";
        }
        function depositFormatter(value, row, index) {
            if (value == 1) {
                return '<span class="label label-info"><@spring.message "admin.withdraw.info.boolean.no"/></span>';
            } else {
                return '<span class="label label-success"><@spring.message "admin.withdraw.info.boolean.yes"/></span>';
            }
        }
        function giftConfigFormatter(value, row, index) {
            if(value){
                    let configs = JSON.parse(value);
                    var gift = '';
                    for (let i = 0; i < configs.length; i++) {
                            var day = "day"+configs[i].day;
                            var score = " <@spring.message "admin.jack.scores"/>:"+configs[i].score;
                            var cash = " <@spring.message "admin.jack.color.cash"/>:"+configs[i].cash;
                            var betRate = " <@spring.message "admin.color.cash.need.bet"/>:"+configs[i].betRate;
                            gift += day+score+cash+betRate+"</br>"
                    }
                    return gift;
            }
            return '-';
        }
        function operateFormatter(value, row, index) {
            return '<a class="open-dialog" href="${adminBase}/signInRule2/showModify.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>&nbsp;&nbsp;'+
                '<a class="todo-ajax" href="${adminBase}/signInRule2/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>？"><i class="glyphicon glyphicon-remove"></i></a>';
        }
    });
</script>
