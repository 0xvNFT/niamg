<#if permAdminFn.hadPermission("admin:adminUserGroup:add")><div class="form-group table-tool">
    <a class="btn btn-primary open-dialog" href="${adminBase}/adminUserGroup/add.do"><@spring.message "admin.add"/></a>
</div></#if>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery','bootstrap','Fui'],function(){
        Fui.addBootstrapTable({
            url : '${adminBase}/adminUserGroup/list.do',
            columns : [{
                field : 'name',
                title : Admin.groupName,
                align : 'center',
                valign : 'middle',
            <#if permAdminFn.hadAnyOnePermission("admin:adminUserGroup:auth","admin:adminUserGroup:delete","admin:adminUserGroup:update")>}, {
                title : Admin.operating,
                align : 'center',
                valign : 'middle',
                formatter : operateFormatter</#if>
            } ]
        });
        function operateFormatter(value, row, index) {
            var s= '';
            <#if permAdminFn.hadPermission("admin:adminUserGroup:auth")>
            if(("${loginUser.type<=50}"=="true" && ( row.type ==1 || row.type ==2 || row.type ==4))
            	|| (row.type ==3 && "${loginUser.type<=90}"=="true") 
            	|| row.type ==5){
                s='<a class="open-dialog" href="${adminBase}/adminUserGroup/auth.do?id='+row.id+'">'+Admin.setAuth+'</a>&nbsp;&nbsp;';
            }</#if>
            <#if permAdminFn.hadPermission("admin:adminUserGroup:delete")>
            if(("${loginUser.type<=90}"=="true" && row.type ==3)
            	|| row.type ==5){
                s=s+'<a class="todo-ajax" href="${adminBase}/adminUserGroup/delete.do?id='+row.id+'" title="'+Admin.delConfirm.replace('{0}',row.name)+'">'+Admin.delete+'</a>&nbsp;&nbsp;';
            }</#if>
            <#if permAdminFn.hadPermission("admin:adminUserGroup:update")>
                s= s+ '<a class=" open-dialog" href="${adminBase}/adminUserGroup/update.do?id='+row.id+'">'+Admin.update+'</a> '
            </#if>
            return s;
        }
    });
</script>