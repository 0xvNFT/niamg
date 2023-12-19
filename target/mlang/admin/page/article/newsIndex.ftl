<div class="table-tool">
    <div class="form-group">
    <#if permAdminFn.hadPermission("admin:article:news:add")>
        <button class="btn btn-primary open-dialog cached" url="${adminBase}/article/add.do?type=2"><@spring.message "admin.menu.add"/></button>
    </#if>
    </div>
</div>
<table class="fui-default-table"></table>
<h3><span class="label label-info"><@spring.message "admin.mobile.notice.info"/>;</span></h3>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/article/list.do?type=2',
            columns: [{
                field: 'title',
                title: Admin.announceName,
                align: 'center',
                width: '200',
                valign: 'middle'
            }, {
                field: 'type',
                title: Admin.announceType,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: articleType
            }, {
                field: 'status',
                title: Admin.announceStats,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                field: 'updateTime',
                title: Admin.beginTime,
                align: 'center',
                width: '300',
                valign: 'middle',
                formatter: Fui.formatDate
            }, {
                field: 'overTime',
                title: Admin.endTime,
                align: 'center',
                width: '300',
                valign: 'middle',
                formatter: Fui.formatDate
            }, {
                title: Admin.op,
                align: 'center',
                width: '200',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });

        //格式化公告类型
        function articleType(value) {
            switch (value) {
                case 1:
                    return '<@spring.message "admin.about.us"/>';
                    break;
                case 2:
                    return '<@spring.message "admin.draw.help"/>';
                    break;
                case 3:
                    return '<@spring.message "admin.deposit.help"/>';
                    break;
                case 4:
                    return '<@spring.message "admin.union.way"/>';
                    break;
                case 5:
                    return '<@spring.message "admin.union.protocol"/>';
                    break;
                case 6:
                    return '<@spring.message "admin.contact.us"/>';
                    break;
                case 7:
                    return '<@spring.message "admin.often.que"/>';
                    break;
                case 8:
                    return '<@spring.message "admin.six.talk"/>';
                    break;
                case 9:
                    return '<@spring.message "admin.common.win.not"/>';
                    break;
                case 10:
                    return '<@spring.message "admin.login.win.notice"/>';
                    break;
                case 13:
                    return '<@spring.message "admin.round.notice"/>';
                    break;
                case 16:
                    return '<@spring.message "admin.last.news"/>';
                    break;
                case 17:
                    return '<@spring.message "admin.sign.rule"/>';
                    break;
                case 18:
                    return '<@spring.message "admin.newer.text"/>';
                    break;
                case 19:
                    return '<@spring.message "admin.mobile.windows"/>';
                    break;
                case 20:
                    return '<@spring.message "admin.res.terms"/>';
                    break;
                case 21:
                    return '<@spring.message "admin.last.discount"/>';
                    break;
                case 22:
                    return '<@spring.message "admin.red.rule"/>';
                    break;
                case 23:
                    return '<@spring.message "admin.vip.level.notice"/>';
                    break;
            }
        }

        function statusFormatter(value, row, index) {
        <#if permAdminFn.hadPermission("admin:article:news:modify")>
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/article/changeStatus.do?id=" + row.id + "&status="
            });
        <#else>
            return value == 1 ? "<@spring.message "admin.disabled"/>" : "<@spring.message "admin.enable"/>";
        </#if>
        }

        function operateFormatter(value, row, index) {
            return [
            <#if permAdminFn.hadPermission("admin:article:news:modify")>
                '<a class="open-dialog" href="${adminBase}/article/modify.do?type=2&id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.modify"/>">',
                '<i class="glyphicon glyphicon-pencil"></i>', '</a>  ',
            </#if>
            <#if permAdminFn.hadPermission("admin:article:news:delete")>
                '<a class="todo-ajax" href="${adminBase}/article/delete.do?id=', row.id, '" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“', row.title, '”？">',
                '<i class="glyphicon glyphicon-remove"></i>', '</a>'</#if>]
                    .join('');
        }
    });
</script>
