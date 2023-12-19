<form class="fui-search table-tool" method="post">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <select class="form-control" name="platform">
                    <option value=""><@spring.message "admin.deposit.type.all"/></option>
					<#list platforms as p><option value="${p.value}">${p.title}</option></#list>
                </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
             <#if permAdminFn.hadPermission("admin:transferOutLimit:add")>
                <a class="btn btn-primary open-dialog" url="${adminBase}/transferOutLimit/detail.do"><@spring.message "admin.add"/></a>
                <span style="color: red;font-size: 20px"><@spring.message "base.admin.deposit.span.descrip"/></span>
             </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery','bootstrap','Fui'],function(){
        var platforms=[];
<#list platforms as p>platforms['${p.value}']="${p.title}";</#list>
        Fui.addBootstrapTable({
            url : '${adminBase}/transferOutLimit/list.do',
            columns : [ {
                field : 'platform',
                title : Admin.sysGameTypes,
                align : 'center',
                valign : 'middle',
                formatter : platformFormatter
            }, {
                field : 'minMoney',
                title : Admin.minTurn,
                align : 'center',
                valign : 'middle'
            }, {
                field : 'maxMoney',
                title : Admin.maxTurn,
                align : 'center',
                valign : 'middle'
            }, {
                title: Admin.op,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
        function platformFormatter(value, row, index) {
            return platforms[row.platform+""];
        }
        function operateFormatter(value, row, index) {
            return [
            <#if permAdminFn.hadPermission("admin:transferOutLimit:modify")>
                '<a class="open-dialog" href="${adminBase}/transferOutLimit/detail.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
                '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
            </#if>
            <#if permAdminFn.hadPermission("admin:transferOutLimit:del")>
                '<a class="todo-ajax" href="${adminBase}/transferOutLimit/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', platforms[row.platform], '”？">',
                '<i class="glyphicon glyphicon-remove"></i>', '</a>'
            </#if>]
                    .join('');
        }
    });
</script>
