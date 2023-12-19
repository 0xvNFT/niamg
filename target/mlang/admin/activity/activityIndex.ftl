<div class="fui-search table-tool">
    <div class="form-group fui-data-wrap">
    <#if permAdminFn.hadPermission("admin:activity:add")>
        <button class="btn btn-primary open-dialog" url="${adminBase}/activity/add.do"><@spring.message "admin.add"/></button>
    </#if>
    </div>
</div>
<table id="agentactivity_datagrid_tb"></table>
<h3><span class="label label-info"><@spring.message "admin.discount.picture.best"/>。</span></h3>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            id: 'agentactivity_datagrid_tb',
            url: '${adminBase}/activity/list.do',
            columns: [{
                field: 'title',
                title: Admin.actTitle,
                align: 'center',
                width: '200',
                valign: 'middle'
            }, {
                field: 'language',
                title: Admin.langTypes,
                align: 'center',
                width: '200',
                valign: 'middle'
            }, {
                field: 'status',
                title: Admin.actStatus,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                field: 'updateTime',
                title: Admin.releaseTime,
                align: 'center',
                width: '300',
                valign: 'middle',
                formatter: Fui.formatDate
            }, {
                field: 'overTime',
                title: Admin.passTime,
                align: 'center',
                width: '300',
                valign: 'middle',
                formatter: Fui.formatDate
            }, {
                field: 'deviceType',
                title: Admin.deviceType,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: formatDeviceType
            }, {
                title: Admin.op,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });

        function contentFormatter(value, row, index) {
            if (value == null || value == "") {
                return value;
            }
            if (value.length > 5) {
                return value.substr(0, 5) + '...';
            } else {
                return value;
            }
        }

        function formatDeviceType(deviceType) {
            let formattedDeviceType;
            switch (deviceType) {
                case 1:
                    formattedDeviceType = "<@spring.message "admin.all"/>";
                    break;
                case 2:
                    formattedDeviceType = "<@spring.message "admin.pc"/>";
                    break;
                case 3:
                    formattedDeviceType = "<@spring.message "admin.app"/>";
                    break;
                default:
                    formattedDeviceType = "<@spring.message "admin.unknow"/>"; // Handle any other cases if needed
                    break;
            }
            return formattedDeviceType;
        }

        function statusFormatter(value, row, index) {
        <#if permAdminFn.hadPermission("admin:activity:update")>
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/activity/changeStatus.do?id=" + row.id + "&status="
            });
        <#else>
            return value == 1 ? "<@spring.message "admin.disabled"/>" : "<@spring.message "admin.enable"/>";
        </#if>
        }

        function operateFormatter(value, row, index) {
            return [
            <#if permAdminFn.hadPermission("admin:activity:update")>
                '<a class="open-dialog" href="${adminBase}/activity/modify.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
                '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
            </#if>
            <#if permAdminFn.hadPermission("admin:activity:delete")>
                '<a class="todo-ajax" href="${adminBase}/activity/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', row.title, '”？">',
                '<i class="glyphicon glyphicon-remove"></i>', '</a>'
            </#if>]
                    .join('');
        }
    });
</script>