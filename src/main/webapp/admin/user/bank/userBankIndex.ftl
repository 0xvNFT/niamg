<form class="fui-search table-tool" method="post" id="member_bank_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="username" value="${username }"
                       placeholder="<@spring.message "admin.username"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="realname"
                       placeholder="<@spring.message "admin.realName"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="cardNo" placeholder="<@spring.message "admin.cardNo"/>"
                       style="width:240px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="bankName"
                       placeholder="<@spring.message "admin.bankNameLike"/>" style="width:240px;">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="bankAddress"
                       placeholder="<@spring.message "admin.bankAddressLike"/>" style="width:240px;">
            </div>
            <div class="input-group">
                <select name="degreeId" class="form-control selectpicker" data-live-search="true"
                        title="<@spring.message "admin.degree.all"/>" multiple>
                    <#list degrees as le>
                        <option value="${le.id}">${le.name}</option>
                    </#list>
                </select>
            </div>
            <input type="hidden" name="degreeIds">
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:userBank:add")>
                <button class="btn btn-primary open-dialog cached" type="button"
                        url="${adminBase}/userBank/showAdd.do"><@spring.message "admin.add"/></button></#if>
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
            } else {
                $form.find("[name='degreeIds']").val("");
            }
        });
        Fui.addBootstrapTable({
            url: '${adminBase}/userBank/list.do',
            columns: [{
                field: 'username',
                title: Admin.username,
                align: 'center',
                valign: 'middle',
                formatter: accountFormatter
            }, {
                field: 'degreeName',
                title: Admin.degreeName,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'cardNo',
                title: Admin.cardNo,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'realName',
                title: Admin.realName,
                align: 'center',
                valign: 'middle',
                formatter: realNameFormatter
            }, {
                field: 'bankName',
                title: Admin.bankName,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'bankAddress',
                title: Admin.bankAddress,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'createTime',
                title: Admin.createTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                width: '80',
                valign: 'middle',
                formatter: statusFormatter
            }, {
                title: Admin.operating,
                align: 'center',
                valign: 'middle',
                width: '120',
                formatter: operateFormatter
            }, {
                field: 'remarks',
                title: Admin.remark,
                align: 'center',
                valign: 'middle',
                sortable: true,
                formatter: remarkFormatter
            }]
        });

        function accountFormatter(value, row, index) {
            <#if permAdminFn.hadPermission("admin:user")>
            return '<a class="open-dialog" href="${adminBase}/user/detail.do?id=' + row.userId + '"><span class="text-danger">' + value + '</span></a>';
            <#else>
            return value;
            </#if>
        }

        function realNameFormatter(value, row, index) {
            var s = "";
            if (row.realName) {
                s = s + row.realName;
            }
            <#if permAdminFn.hadPermission("admin:userBank:update:realName")>
            s += '&nbsp;&nbsp<a class="open-dialog" href="${adminBase}/userBank/showUpdateRealName.do?id=' + row.id + '"><i class="fa fa-pencil-alt"></i></a>';
            </#if>
            return s;
        }

        function remarkFormatter(value, row, index) {
            var content;
            if (value == null) {
                content = "";
            } else {
                content = value;
            }
            if (content && content.length > 6) {
                content = content.substring(0, 6) + "...";
            }
            return '<span class="text-danger">' + content + '</span>';
        }

        function statusFormatter(value, row, index) {
            <#if permAdminFn.hadPermission("admin:userBank:update:status")>
            return Fui.statusFormatter({
                val: value,
                url: "${adminBase}/userBank/changeStatus.do?id=" + row.id + "&status="
            });
            <#else>
            if (value == 1) {
                return Admin.disable;
            } else {
                return Admin.enable
            }
            </#if>
        }

        function operateFormatter(value, row, index) {
            var str = "";
            <#if permAdminFn.hadPermission("admin:userBank:update")>
            str += '<a class="open-dialog" href="${adminBase}/userBank/showModify.do?id=' + row.id + '">' + Admin.update + '</a> &nbsp; ';
            </#if>
            <#if permAdminFn.hadPermission("admin:userBank:delete")>
            str += '<a class="todo-ajax" href="${adminBase}/userBank/delete.do?id=' + row.id + '" title="' + Admin.delConfirm.replace('{0}', row.cardNo) + '">' + Admin.delete + '</a>';
            </#if>
            return str;
        }
    });
</script>
