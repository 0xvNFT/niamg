<#if permAdminFn.hadPermission("admin:userAvatar:add")><div class="table-tool">
	<button class="btn btn-primary open-dialog cached" url="${adminBase}/userAvatar/showAdd.do"><@spring.message "admin.add"/></button>
</div> </#if>
<table id="product_datagrid_tb"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            id: 'product_datagrid_tb',
            url: "${adminBase}/userAvatar/list.do",
            columns: [{
                field: 'url',
                title: Admin.userAvatarUrl,
                align: 'center',
                valign: 'middle',
                formatter: imgFormatter
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
        function imgFormatter(value, row, index) {
            if (value) {
                return '<img class="avatar" src="'+ value +'"/>';
            }
            return '-';
        }
        function operateFormatter(value, row, index) {
        	let s='';
            <#if permAdminFn.hadPermission("admin:userAvatar:modify")>
            s=s+'<a class="open-dialog" href="${adminBase}/userAvatar/showModify.do?id='+row.id+'">'+Admin.update+'</a>&nbsp;&nbsp;';
            </#if>
            <#if permAdminFn.hadPermission("admin:userAvatar:delete")>
            s=s+'<a class="todo-ajax" href="${adminBase}/userAvatar/delete.do?id='+row.id+'" title="'+ Admin.delConfirm.replace('{0}','')+'">'+Admin.delete+'</a>';
            </#if>
            return  s;
        }
    });
</script>