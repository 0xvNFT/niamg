<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="startTime" value="${startTime}"
                       placeholder="<@spring.message "manager.begin.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                 aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name="endTime" class="form-control fui-date" value="${endTime}"
                       placeholder="<@spring.message "manager.end.date"/>"> <span class="glyphicon glyphicon-th form-control-feedback"
                                                 aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${managerBase}/globalReport/list.do',
            pagination: false,
            responseHandler: function (res) {
                return {
                    rows: res
                };
            },
            columns: [{
                title: '<@spring.message "manager.station.point"/>',
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    return row.stationName + "(" + row.stationCode + ")";
                }
            }, {
                field: 'isOpenSysLotAddResult',
                title: '<@spring.message "manager.switch.on.off"/>',
                align: 'center',
                width: '150px',
                formatter: function (value, row, index) {
                    if(value == 2) {
                        return '<span class="label label-success"><@spring.message "manager.switch.on"/></span>';
                    }else {
                        return '<span class="label label-danger"><@spring.message "manager.switch.off"/></span>';
                    }
                }
            }, {
                title: "<@spring.message "manager.lettory.name"/>",
                valign: "center",
                align: "center",
                formatter: function (value, row, index) {
                    var r = row.lotteryBetAmount || 0;
                    r = r - (row.lotteryWinAmount || 0);
                    r = parseFloat(r);
                    r = Math.round(r * 100) / 100;
                    var s = (row.lotteryBetAmount || 0) + "<br>";
                    s = s + '<span class="text-danger">' + (row.lotteryWinAmount || 0) + "</span><br>";
                    s = s + '<span class="text-success">' + r + '</span>';
                    return s;
                }
            }, {
                title: "<@spring.message "manager.system.lottery"/>",
                valign: "center",
                align: "center",
                formatter: function (value, row, index) {
                    var r = row.sysLotteryBetAmount || 0;
                    r = r - (row.sysLotteryWinAmount || 0);
                    r = parseFloat(r);
                    r = Math.round(r * 100) / 100;
                    var s = (row.sysLotteryBetAmount || 0) + "<br>";
                    s = s + '<span class="text-danger">' + (row.sysLotteryWinAmount || 0) + "</span><br>";
                    s = s + '<span class="text-success">' + r + '</span>';
                    return s;
                }
            }, {
                title: "<@spring.message "manager.six.lottery.other"/>",
                valign: "center",
                align: "center",
                formatter: function (value, row, index) {
                    var r = row.olhcBetAmount || 0;
                    r = r - (row.olhcWinAmount || 0);
                    r = parseFloat(r);
                    r = Math.round(r * 100) / 100;
                    var s = (row.olhcBetAmount || 0) + "<br>";
                    s = s + '<span class="text-danger">' + (row.olhcWinAmount || 0) + "</span><br>";
                    s = s + '<span class="text-success">' + r + '</span>';
                    return s;
                }
            }, {
                title: "<@spring.message "manager.six.lottery"/>",
                valign: "center",
                align: "center",
                formatter: function (value, row, index) {
                    var r = row.markSixBetAmount || 0;
                    r = r - (row.markSixWinAmount || 0);
                    r = parseFloat(r);
                    r = Math.round(r * 100) / 100;
                    var s = (row.markSixBetAmount || 0) + "<br>";
                    s = s + '<span class="text-danger">' + (row.markSixWinAmount || 0) + "</span><br>";
                    s = s + '<span class="text-success">' + r + '</span>';
                    return s;
                }
            }, {
                field: 'depositAmount',
                title: '<@spring.message "manager.total.money"/>',
                align: 'center',
                formatter: moneyFormatter
            }, {
                field: 'depositHandlerArtificial',
                title: '<@spring.message "manager.hand.input.money"/>',
                align: 'center',
                formatter: moneyFormatter
            }, {
                field: 'depositArtificial',
                title: '<@spring.message "admin.deposit.type.manual"/>',
                align: 'center',
                formatter: moneyFormatter
            }, {
                field: 'withdrawAmount',
                title: '<@spring.message "manager.total.draw"/>',
                align: 'center',
                formatter: moneyFormatter
            }, {
                field: 'withdrawArtificial',
                title: '<@spring.message "manager.hand.out.money"/>',
                align: 'center',
                formatter: moneyFormatter
            }]
        });

        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return 0;
            }
            var f = parseFloat(value);
            f = Math.round(f * 100) / 100;
            if (value > 0) {
                return ['<span class="text-danger">', '</span>'].join(f);
            }
            return ['<span class="text-primary">', '</span>'].join(f);
        }
    });
</script>
