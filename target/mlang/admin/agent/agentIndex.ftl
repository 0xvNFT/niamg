<div class="table-tool" >
    <#if permAdminFn.hadPermission("admin:agent:add")>
        <a class="btn btn-primary open-dialog" href="${adminBase}/agent/showAdd.do"><@spring.message "admin.add"/></a>
    </#if>
</div>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs([], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/agent/list.do',
            columns: [{
                field: 'name',
                title: Admin.proxyMerName,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createUser',
                title: Admin.creator,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'remark',
                title: Admin.remark,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    <#if permAdminFn.hadPermission("admin:agent:status")>return Fui.statusFormatter({
                        val: value,
                        url: "${adminBase}/agent/changeStatus.do?id=" + row.id + "&status="
                    });
                    <#else>switch (value) {
                        case 1:
                            return Admin.disable;
                        case 2:
                            return Admin.enable;
                    }
                    </#if>
                }
             },{
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
			}]
        });

        function operateFormatter(value, row, index) {
            var s = '';
            <#if permAdminFn.hadPermission("admin:agent:delete")>
                s = s + '<a class="todo-ajax" href="${adminBase}/agent/delete.do?id=' + row.id + '" title="'+Admin.delConfirm.replace('{0}',row.name) + '">'+Admin.delete+'</a> &nbsp;&nbsp;';
            </#if>
            <#if permAdminFn.hadPermission("admin:agent:update")>
            	s = s + '<a class="open-dialog" href="${adminBase}/agent/showModify.do?id=' + row.id + '">'+Admin.update+'</a>'
            </#if>
            return s;
        }
    });
</script>
