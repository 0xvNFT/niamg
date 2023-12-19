<#if permAdminFn.hadPermission("admin:turntableGift:add")>
<div class="table-tool">
    <div class="form-group">
        <button class="btn btn-primary open-dialog cached" url="${adminBase}/turntableGift/showAdd.do"><@spring.message "admin.add"/></button>
    </div>
</div></#if>
<table id="product_datagrid_tb"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            id: 'product_datagrid_tb',
            url: "${adminBase}/turntableGift/list.do",
            columns: [{
                field: 'productImg',
                title: Admin.jackPic,
                align: 'center',
                valign: 'middle',
                formatter: imgFormatter
            },{
                field: 'productName',
                title: Admin.jackTitle,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'price',
                title: Admin.jackValue,
                align: 'center',
                valign: 'middle',
                formatter: moneyFormatter
            }, {
                field: 'createDatetime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });
        function moneyFormatter(value, row, index) {
            if (value === undefined) {
                return value;
            }
            if (value > 0) {
                return '<span class="text-danger">'+value+'</span>';
            }
            return '<span class="text-primary">'+value+'</span>';
        }
        function imgFormatter(value, row, index) {
            if (value === undefined) {
                return '-';
            }
            if (value) {
                return '<img alt="'+row.productName+'" title="'+row.productDesc+'" src="'+ value +'" class="avatar"/>';
            }
        }
        function operateFormatter(value, row, index) {
            var s='';
            <#if permAdminFn.hadPermission("admin:turntableGift:modify")>
                s=s+'<a class="open-dialog" href="${adminBase}/turntableGift/showModify.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>"><i class="glyphicon glyphicon-pencil"></i></a>';
            </#if>
            <#if permAdminFn.hadPermission("admin:turntableGift:delete")>
                s=s+'&nbsp;&nbsp;<a class="todo-ajax" href="${adminBase}/turntableGift/delete.do?id='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.productName+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
            </#if>
            return s;
        }
    });
</script>