<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group keyword">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "manager.username.input"/>">
            </div>
            <button class="btn btn-primary"><@spring.message "manager.check.select"/></button>
        <#if permAdminFn.hadPermission("admin:playerConfig:add")>
            <button class="btn btn-primary open-dialog cached" url="${adminBase}/playerConfig/add.do"><@spring.message "admin.deposit.bank.bankCard.add"/></button>
        </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['${base}/common/js/admin/thirdConfig.js'], function ($ele) {
        Fui.addBootstrapTable({
            url: '${adminBase}/playerConfig/list.do',
            columns: [{
                field: 'username',
                title: Admin.userName,
                align: 'center',
                width: '200',
                valign: 'middle'
            }, {
                field: 'configName',
                title: Admin.configName,
                align: 'center',
                width: '180',
                valign: 'bottom'
            }, {
                field: 'configValue',
                title: Admin.configVal,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: configValueFormatter
            }, {
                title: Admin.op,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });

        function operateFormatter(value, row, index) {
            return [
            <#if permAdminFn.hadPermission("admin:playerConfig:modify")>
                '<a class="open-dialog" href="${adminBase}/playerConfig/modify.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
                '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
            </#if>
            <#if permAdminFn.hadPermission("admin:playerConfig:delete")>
                '<a class="todo-ajax" href="${adminBase}/playerConfig/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', row.title, '”？">',
                '<i class="glyphicon glyphicon-remove"></i>', '</a>'
            </#if>]
                    .join('');
        }
    });
</script>
