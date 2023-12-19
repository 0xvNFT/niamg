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
                    <button class="btn btn-primary fui-date-search"><@spring.message "admin.search"/></button>
                    <button class="btn btn-primary open-dialog" url="${base}/admin/appIndexMenu/add.do"><@spring.message "admin.add"/></button>
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
            url :'${base}/admin/appIndexMenu/list.do',
            columns : [ {
                field : 'name',
                title : Admin.menuName,
                align : 'center',
                valign : 'middle'
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
            return Fui.statusFormatter({val:value,url:"${base}/admin/appIndexMenu/updStatus.do?id="+row.id+"&status="});
        }


        function operateFormatter(value, row, index) {
            return  '<a class="open-dialog" href="${base}/admin/appIndexMenu/modify.do?id='+row.id+'"><@spring.message "admin.menu.modify"/></a>   '+
                '<a class="todo-ajax" href="${base}/admin/appIndexMenu/delete.do?id='+row.id+'" title="<@spring.message "admin.sure.del.menu"/>['+row.name+']？"><@spring.message "admin.menu.del"/></a>';
        }
    });
</script>
