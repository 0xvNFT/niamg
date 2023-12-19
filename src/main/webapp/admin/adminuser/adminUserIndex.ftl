<form class="fui-search table-tool" method="post" id="member_bank_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <select name="groupId" class="form-control">
                    <option value=""><@spring.message "admin.all"/></option>
                    <#list groups as g>
                        <option value="${g.id }">${g.name }</option>
                    </#list>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:adminUser:add")>
                <a class="btn btn-primary open-dialog" href="${adminBase}/adminUser/add.do"><@spring.message "admin.add"/></a>
            </#if>
            <#if permAdminFn.hadPermission("admin:adminUser:online")>
                <div class="input-group">
                    <input style="margin-left: 20px" type="checkbox" value="true" name="onlineStatus">
                    <label for="onlineStatus" style="margin-bottom: 0"><@spring.message "admin.onlyOnlineUser"/></label>
                </div>
            </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
    requirejs([], function () {
        Fui.addBootstrapTable({
            url: '${adminBase}/adminUser/list.do',
            columns: [{
                field: 'username',
                title: Admin.username,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'groupName',
                title: Admin.groupName,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'remark',
                title: Admin.remark,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'addMoneyLimit',
                title: Admin.addMoneyLimit,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value || value == 0) {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/moneyLimitModify.do?id=' + row.id + '">' + value + '</a>';
                    } else {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/moneyLimitModify.do?id=' + row.id + '">'+Admin.addMoneyUnLimit+'</a>';
                    }
                }
            }, {
                field: 'depositLimit',
                title: Admin.depositHandelLimit,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value || value == 0) {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/depositLimitModify.do?id=' + row.id + '">' + value + '</a>';
                    } else {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/depositLimitModify.do?id=' + row.id + '">'+Admin.addMoneyUnLimit+'</a>';
                    }
                }
            }, {
                field: 'withdrawLimit',
                title: Admin.withdrawHandelLimit,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value || value == 0) {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/withdrawLimitModify.do?id=' + row.id + '">' + value + '</a>';
                    } else {
                        return '<a class="open-dialog text-danger" href="${adminBase}/adminUser/withdrawLimitModify.do?id=' + row.id + '">'+Admin.addMoneyUnLimit+'</a>';
                    }
                }
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    <#if permAdminFn.hadPermission("admin:adminUser:status")>return Fui.statusFormatter({
                        val: value,
                        url: "${adminBase}/adminUser/changeStatus.do?id=" + row.id + "&status="
                    });
                    <#else>switch (value) {
                        case 1:
                            return Admin.disable;
                        case 2:
                            return Admin.enable;
                    }
                    </#if>
                }
            <#if permAdminFn.hadPermission("admin:user:online")>
            },{
                field: 'online',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter: function (value, row, index) {
                    if (value) {
                        return '<label class="label label-success">'+Admin.online+'</label>'
                    }else {
                        return '<label class="label label-default">'+Admin.offline+'</label>'
                    }
                }
            </#if>
             },{
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
			}]
        });

        function operateFormatter(value, row, index) {
            var s = '';
            <#if permAdminFn.hadPermission("admin:adminUser:delete")>
            if (row.original==1) {
                s = s + '<a class="todo-ajax" href="${adminBase}/adminUser/delete.do?id=' + row.id + '" title="'+Admin.delConfirm.replace('{0}',row.username) + '">'+Admin.delete+'</a> &nbsp;&nbsp;';
            }
            </#if>
            <#if permAdminFn.hadPermission("admin:adminUser:reset:pwd")>
                s = s + '<a class="todo-ajax" href="${adminBase}/adminUser/resetPwd.do?id=' + row.id + '" title="'+Admin.resetPwdTitle.replace('{0}',row.username)+'">'+Admin.resetPwd+'</a>&nbsp;&nbsp;';
            </#if>
            <#if permAdminFn.hadPermission("admin:adminUser:reset:pwd")>
                s = s + '<a class="todo-ajax" href="${adminBase}/adminUser/resetReceiptPwd.do?id=' + row.id + '" title="'+Admin.resetReceiptPwdTitle.replace('{0}',row.username) + '">'+Admin.resetReceiptPwd+'</a>&nbsp;&nbsp;';
            </#if>
            <#if permAdminFn.hadPermission("admin:adminUser:update")>
            s = s + '<a class="open-dialog" href="${adminBase}/adminUser/update.do?id=' + row.id + '">'+Admin.update+'</a>'
            </#if>
            return s;
        }
    });
</script>
