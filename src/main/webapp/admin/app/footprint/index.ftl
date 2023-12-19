<div class="table-tool">
    <form class="fui-search table-tool" method="post">
        <div class="form-group zhongjiang">
            <div class="form-group fui-data-wrap">
                <div class="form-inline">
                    <div class="input-group">
                        <input type="text" class="form-control fui-date" name="startDate" value="${startDate}"
                               placeholder="<@spring.message "admin.startDate"/>" autocomplete="off">
                        <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
                    <div class="input-group">
                        <input type="text" class="form-control" name="account" placeholder="<@spring.message "admin.money.history.username"/>" style="width:120px;">
                    </div>
                    <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
                    <button class="btn btn-primary open-dialog" url="${base}/admin/appGameFoot/add.do"><@spring.message "admin.add"/></button>
                </div>
                <div class="form-inline mt5px">
                    <div class="input-group">
                        <input type="text" name="endDate" class="form-control fui-date"
                               value="${endDate}" placeholder="<@spring.message "admin.endDate"/>" autocomplete="off">
                        <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
                    </div>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
                </div>
            </div>
        </div>
    </form>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery','bootstrap','Fui'],function(){
        Fui.addBootstrapTable({
            url :'${base}/admin/appGameFoot/list.do',
            columns : [ {
                field : 'gameName',
                title : Admin.gameNameMeth,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'userName',
                title : Admin.accountNum,
                align : 'center',
                valign : 'middle',
                formatter : accountFormatter
            }, {
                field : 'visitNum',
                title : Admin.visitNum,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'gameType',
                title : Admin.gameTypes,
                align : 'center',
                valign : 'middle',
                formatter : gameTypeFormatter
            }, {
                field : 'type',
                title : Admin.footPrintType,
                align : 'center',
                valign : 'middle',
                formatter : typeFormatter
            }, {
                field : 'createDatetime',
                title : Admin.addDate,
                align : 'center',
                valign : 'middle',
                formatter : Fui.formatDatetime
            }, {
                field : 'status',
                title : Admin.status,
                align : 'center',
                valign : 'middle',
                formatter : statusFormatter
            }, {
                title : Admin.op,
                align : 'center',
                valign : 'middle',
                formatter : operateFormatter
            } ]
        });
        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({val:value,url:"${base}/admin/appGameFoot/updStatus.do?id="+row.id+"&status="});
        }

        function typeFormatter(value, row, index) {
            if (row.type == 1) {
                return "<@spring.message "admin.cent.config.foot"/>"
            }
            return "<@spring.message "admin.foot.print"/>";
        }

        function accountFormatter(value, row, index) {
            if (row.userName) {
                return row.userName
            }
            return "<@spring.message "admin.admins"/>";
        }

        function gameTypeFormatter(value, row, index) {
            if (row.gameType == 1){
                return "<@spring.message "manager.lettory.name"/>";
            }else if (row.gameType == 2){
                return '<@spring.message "admin.live"/>';
            }else if (row.gameType == 3){
                return "<@spring.message "admin.egame"/>";
            }else if (row.gameType == 4){
                return "<@spring.message "admin.sport"/>";
            }else if (row.gameType == 5){
                return "<@spring.message "admin.esport"/>";
            }else if (row.gameType == 6){
                return "<@spring.message "admin.fishing"/>";
            }else if (row.gameType == 7){
                return "<@spring.message "admin.chess"/>";
            }else if (row.gameType == 8){
                return "<@spring.message "admin.define.info"/>";
            }else if (row.gameType == 9){
                return "<@spring.message "admin.red.packet"/>";
            }else {
                return "<@spring.message "admin.other.thing"/>";
            }
        }

        function operateFormatter(value, row, index) {
            return  '<a class="open-dialog" href="${base}/admin/appGameFoot/modify.do?id='+row.id+'"><@spring.message "admin.menu.modify"/></a>   '+
                '<a class="todo-ajax" href="${base}/admin/appGameFoot/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.foot.print.del"/>['+row.gameName+']？"><@spring.message "admin.menu.del"/></a>';
        }
    });
</script>
