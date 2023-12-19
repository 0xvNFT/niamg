<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group keyword">
                <input type="text" class="form-control" name="name" placeholder="<@spring.message "admin.domain"/>">
            </div>
            <div class="input-group keyword">
                <input type="text" class="form-control" name="proxyUsername" placeholder="<@spring.message "admin.proxyUsername"/>">
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
        <#if permAdminFn.hadPermission("admin:domain:add")>
            <button class="btn btn-primary open-dialog cached" url="${adminBase}/domain/showAdd.do"><@spring.message "admin.add"/></button>
        </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/domain/list.do',
            columns: [{
                field: 'name',
                title: Admin.domain,
                align: 'center'
            }, {
                field: 'agentName',
                title: Admin.agentName,
                align: 'center'
            }, {
                field: 'proxyUsername',
                title: Admin.proxyUsername,
                align: 'center'
            },{
                field: 'status',
                title: Admin.status,
                align: 'center',
                formatter: statusFormatter
            }, {
                field: 'defaultHome',
                title: Admin.defaultHomePage,
                align: 'center'
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                formatter: Fui.formatDatetime
            }, {
                field: 'sortNo',
                title: Admin.sortNo,
                align: 'center'
            }, {
                field: 'switchDomain',
                title: Admin.switchDomain,
                align: 'center',
                formatter: domainStatusFormatter
            }, {
                field: 'remark',
                title: Admin.remark,
                align: 'center',
                valign: 'middle',
                formatter: remarkFormatter
            }, {
                title: Admin.operating,
                align: 'center',
                formatter: operateFormatter
            }]
        });

        
        function remarkFormatter(value, row, index) {
            var content = value;
            var eContent = $.trim(value);
            if (content && content.length > 15) {
                content = content.substring(0, 15) + "...";
            }
            if (!content || !eContent) {
                content = Admin.addRemark;
            }
            return '<a class="open-dialog text-danger" href="${adminBase}/domain/remarkModify.do?id=' + row.id + '" title="' + value + '">' + content + '</a>';
        }
        function statusFormatter(value, row, index) {
            return Fui.statusFormatter({val: value, url: "${adminBase}/domain/changeStatus.do?id=" + row.id + "&status="});
        }
        
        function domainStatusFormatter(value, row, index) {
            return Fui.statusFormatter({val: value, url: "${adminBase}/domain/updSwitchStatus.do?id=" + row.id + "&status="});
        }

        function operateFormatter(value, row, index) {
        return ''
        <#if permAdminFn.hadPermission("admin:domain:modify")>
                +'<a class="open-dialog" href="${adminBase}/domain/showModify.do?id=' + row.id + '">'+Admin.update+'</a> &nbsp; '
        </#if>
        <#if permAdminFn.hadPermission("admin:domain:delete")>
                + '<a class="todo-ajax" href="${adminBase}/domain/delete.do?id=' + row.id + '" title="'+Admin.delConfirm.replace('{0}',row.name) + '">'+Admin.delete+'</a>';
        </#if>
        }
    });
</script>