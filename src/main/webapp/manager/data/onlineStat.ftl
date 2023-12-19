<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${managerBase}/onlineStat/list.do',
            pagination: false,
            responseHandler: function (res) {
                return {
                    rows: res
                };
            },
            columns: [{
                field: 'code',
                title: '<@spring.message "manager.code.station"/>',
                align: 'center',
                valign: 'middle'
            }, {
                field: 'name',
                title: '<@spring.message "manager.station.name"/>',
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    return "<@spring.message "admin.deposit.table.all.sum"/>:";
                }
            }, {
                field: 'onlineNum',
                title: '<@spring.message "manager.online.number"/>',
                align: 'center',
                valign: 'middle',
                pageSummaryFormat: function (rows, aggsData) {
                    var r = 0;
                    for (var i = rows.length - 1; i >= 0; i--) {
                        r = r + rows[i].onlineNum;
                    }
                    return r;
                }
            }]
        });
    });
</script>
