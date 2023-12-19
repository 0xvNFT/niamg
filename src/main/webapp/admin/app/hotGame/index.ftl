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
                    <button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'>><@spring.message "admin.dayBefore"/></button>
                    <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
                    <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
                    <button class="btn btn-primary open-dialog" url="${base}/admin/appHotGame/add.do"><@spring.message "admin.add"/></button>
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
            url :'${base}/admin/appHotGame/list.do',
            columns : [ {
                field : 'name',
                title : Admin.gameNameMeth,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'type',
                title : Admin.gameTypes,
                align : 'center',
                valign : 'middle',
                formatter : gameTypeFormatter
            }, {
                field : 'sortNo',
                title : Admin.sortNum,
                align : 'center',
                valign : 'middle'
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
            return Fui.statusFormatter({val:value,url:"${base}/admin/appHotGame/updStatus.do?id="+row.id+"&status="});
        }

        function gameTypeFormatter(value, row, index) {
            if (row.type == 1){
                return "<@spring.message "manager.lettory.name"/>";
            }else if (row.type == 2){
                return "<@spring.message "admin.live"/>";
            }else if (row.type == 3){
                return "<@spring.message "admin.egame"/>";
            }else if (row.type == 4){
                return "<@spring.message "admin.sport"/>";
            }else if (row.type == 5){
                return "<@spring.message "admin.esport"/>";
            }else if (row.type == 6){
                return "<@spring.message "admin.fishing"/>";
            }else if (row.type == 7){
                return "<@spring.message "admin.chess"/>";
            }else if (row.type == 8){
                return "<@spring.message "admin.define.info"/>";
            }else if (row.type == 9){
                return "<@spring.message "admin.red.packet"/>";
            }else {
                return "<@spring.message "admin.other.thing"/>";
            }
        }

        function operateFormatter(value, row, index) {
            return  '<a class="open-dialog" href="${base}/admin/appHotGame/modify.do?id='+row.id+'"><@spring.message "admin.menu.modify"/></a>   '+
                '<a class="todo-ajax" href="${base}/admin/appHotGame/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.game.del"/>['+row.name+']？"><@spring.message "admin.menu.del"/></a>';
        }
    });
</script>
