<form class="fui-search table-tool" method="post" id="member_bank_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="bankName" placeholder="<@spring.message "admin.bankNameLike"/>" style="width:240px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="bankCode" placeholder="<@spring.message "admin.bankCodeLike"/>" style="width:240px;">
            </div>
            <input type="hidden" name="degreeIds">
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:bankConfig:add")><button class="btn btn-primary open-dialog cached" type="button"url="${adminBase}/bankConfig/showAdd.do"><@spring.message "admin.add"/></button></#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        var $form = $("#member_bank_manager_form_id");
        $form.find("[name='degreeId']").change(function () {
            var types = $(this).val();
            if (types) {
                types = types.join(",");
                $form.find("[name='degreeIds']").val(types);
            }else {
                $form.find("[name='degreeIds']").val("");
            }
        });
        Fui.addBootstrapTable({
            url: '${adminBase}/bankConfig/list.do',
            columns: [{
                field: 'name',
                title: Admin.bankName,
                align: 'center',
                valign: 'middle',
            }, {
                field: 'code',
                title: Admin.bankCode,
                align: 'center',
                valign: 'middle'
            },{
                field: 'createTime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter : Fui.formatDatetime
            },{
                field: 'sortNo',
                title: Admin.sortNo,
                align: 'center',
                valign: 'middle'
            },{
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                width: '120',
                formatter: operateFormatter
            }]
        });

        function operateFormatter(value, row, index) {
            var str = "";
            <#if permAdminFn.hadPermission("admin:bankConfig:modify")>
            str += '<a class="open-dialog" href="${adminBase}/bankConfig/showModify.do?id=' + row.id + '">'+Admin.update+'</a> &nbsp; ';
            </#if>
            <#if permAdminFn.hadPermission("admin:bankConfig:delete")>
            str += '<a class="todo-ajax" href="${adminBase}/bankConfig/delete.do?id=' + row.id + '" title="' + Admin.delConfirm.replace('{0}',row.name)+ '">'+Admin.delete+'</a>';
            </#if>
            return str;
        }
    });
</script>
