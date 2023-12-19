<form class="fui-search table-tool" method="post" id="member_bank_manager_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control" name="username" placeholder="<@spring.message "admin.username"/>" style="width:120px;">
            </div>
            <div class="input-group">
                <select name="agentId" class="form-control">
                    <option value=""><@spring.message "admin.select.all"/></option>
                    <#list agents as g><option value="${g.id }">${g.name }</option></#list>
                </select>
            </div>
            <button class="btn btn-primary"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:agentUser:add")>
                <a class="btn btn-primary open-dialog" href="${adminBase}/agentUser/showAdd.do"><@spring.message "admin.add"/></a>
            </#if>
        </div>
    </div>
</form>
<table class="fui-default-table"></table>
<script type="text/javascript">
requirejs([], function () {
var agents={};<#list agents as g>agents["${g.id }"]="${g.name }";</#list>
    Fui.addBootstrapTable({
        url: '${adminBase}/agentUser/list.do',
        columns: [{
            field: 'username',
            title: Admin.username,
            align: 'center',
            valign: 'middle'
        },{
            field: 'agentId',
            title: Admin.agentName,
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
            	return agents[value+''];
            }
        },{
            field: 'realName',
            title: Admin.userFirst,
            align: 'center',
            valign: 'middle',
            formatter: realNameFormatter
        },{
            field: 'createUser',
            title: Admin.creator,
            align: 'center',
            valign: 'middle'
        },{
            field: 'createDatetime',
            title: Admin.createTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        }, {
            field: 'remark',
            title: Admin.remark,
            align: 'center',
            valign: 'middle',
            formatter: remarkFormatter
        }, {
            field: 'status',
            title: Admin.status,
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
                <#if permAdminFn.hadPermission("admin:agentUser:status")>return Fui.statusFormatter({
                    val: value,
                    url: "${adminBase}/agentUser/changeStatus.do?id=" + row.id + "&status="
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
	function realNameFormatter(value, row, index) {
        var s = "";
        if (row.realName) {
            s = s + row.realName;
        }
        <#if permAdminFn.hadPermission("admin:agentUser:realName")>
        s += '&nbsp;&nbsp<a class="open-dialog" href="${adminBase}/agentUser/showModifyRealName.do?id=' + row.id + '" title="<@spring.message "admin.update.real.name"/>"><i class="fa fa-pencil-alt"></i></a>';
        </#if>
        return s;
    }
    function remarkFormatter(value, row, index) {
        var content = value;
        if (content && content.length > 6) {
            content = content.substring(0, 6) + "...";
        }
        <#if permAdminFn.hadPermission("admin:agentUser:remark")>
        if (!content || !$.trim(value)) {
            content = "<@spring.message "admin.menu.update.remark"/>";
        }
        return '<a class="open-dialog text-danger" href="${adminBase}/agentUser/showModifyRemark.do?id=' + row.id + '" title="' + value + '">' + content + '</a>';
        <#else>
        return content;
        </#if>
    }
    function operateFormatter(value, row, index) {
        var s = '';
        <#if permAdminFn.hadPermission("admin:agentUser:del")>
            s = s + '<a class="todo-ajax" href="${adminBase}/agentUser/delete.do?id=' + row.id + '" title="'+Admin.delConfirm.replace('{0}',row.username) + '">'+Admin.delete+'</a> &nbsp;&nbsp;';
        </#if>
        <#if permAdminFn.hadPermission("admin:agentUser:pwd")>
            s = s + '<a class="todo-ajax" href="${adminBase}/agentUser/resetPwd.do?id=' + row.id + '" title="'+Admin.resetPwdTitle.replace('{0}',row.username)+'">'+Admin.resetPwd+'</a>&nbsp;&nbsp;';
        </#if>
        return s;
    }
});
</script>
