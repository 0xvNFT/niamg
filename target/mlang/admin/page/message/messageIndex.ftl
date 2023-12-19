<form class="fui-search table-tool" method="post" id="system_message_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
				<input type="text" class="form-control fui-date" name="startTime" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 00:00:00" placeholder="<@spring.message "admin.startTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "admin.today"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "admin.yesterday"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "admin.thisWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayBefore'><@spring.message "admin.dayBefore"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthBefore'><@spring.message "admin.monthBefore"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="title" placeholder="<@spring.message "admin.title.heading"/>">
            </div>
            <div class="input-group">
                <select class="form-control" name="sendType">
                	<option value=""><@spring.message "admin.all.send"/></option>
                    <option value="3"><@spring.message "manager.station.point"/></option>
                    <option value="1"><@spring.message "admin.system.opr"/></option>
                    <option value="2"><@spring.message "admin.member.vip.user.name"/></option>
                </select>
            </div>
            <div class="input-group">
                <select class="form-control" name="receiveType">
                    <option value=""><@spring.message "admin.all.receive"/></option>
                    <option value="1"><@spring.message "admin.simple.vip"/></option>
                    <option value="2"><@spring.message "admin.assign.multi.vip"/></option>
                    <option value="3"><@spring.message "admin.all.group.send"/></option>
                    <option value="4"><@spring.message "admin.withdraw.table.degreeName"/></option>
                    <option value="5"><@spring.message "admin.vip.group"/></option>
                    <option value="6"><@spring.message "admin.proxy.line.multi"/></option>
                </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "manager.check.select"/></button>
            <#if permAdminFn.hadPermission("admin:message:add")>
                <button class="btn btn-primary open-dialog" url="${adminBase}/message/showAdd.do"><@spring.message "admin.deposit.bank.bankCard.add"/></button>
            </#if>
            <#if permAdminFn.hadPermission("admin:message:delete")>
                <button class="btn btn-primary batchDelete" type="button"><@spring.message "admin.batch.del"/></button>
            </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
				<input type="text" name="endTime" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${curDate} 23:59:59" placeholder="<@spring.message "admin.endTime"/>" autocomplete="off"> <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
			</div>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "admin.lastWeek"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "admin.thisMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "admin.lastMonth"/></button>
			<button type="button" class="btn btn-default fui-date-btn" data-target='dayAfter'><@spring.message "admin.dayAfter"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='monthAfter'><@spring.message "admin.monthAfter"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="sendUsername" placeholder="<@spring.message "admin.seed.account"/>">
            </div>
            <div class="input-group">
                <input type="text" class="form-control" name="receiveUsername" placeholder="<@spring.message "admin.receive.account"/>">
            </div>
        </div>
    </div>
</form>
<table id="message1_datagrid_tb"></table>
<script type="text/javascript">
var degrees = {},groups={};<#list degrees as g>degrees["${g.id}"]="${g.name}";</#list>
<#list groups as g>groups["${g.id}"]="${g.name}";</#list>
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
        id: 'message1_datagrid_tb',
        url: '${adminBase}/message/list.do',
        columns: [{
            title: '<input type="checkbox" class="check_all">',
            align: 'center',
            vilign: 'middle',
            formatter: function(value, row, index) {
		        return '<input type="checkbox" value="' + row.id + '">';
		    }
        },{
            field: 'title',
            title: Admin.stationInfoTit,
            align: 'center',
            valign: 'middle',
            formatter:function(value, row, index) {
		        return '<a class="open-dialog" href="${adminBase}/message/viewDetail.do?id=' + row.id + '">' + row.title + '</a>'
		    }
        },{
            field: 'sendUsername',
            title: Admin.stationSeed,
            align: 'center',
            valign: 'middle'
        },{
            field: 'receiveUsernames',
            title: Admin.receiveItem,
            align: 'center',
            valign: 'middle',
            formatter: receiveUsernameFormat
        },{
            field: 'createTime',
            title: Admin.createTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        },{
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: operateFormatter
        }]
    });
    function operateFormatter(value, row, index) {
        return '<a class="todo-ajax" href="${adminBase}/message/delete.do?ids='+row.id+'" title="<@spring.message "admin.deposit.bank.bankCard.delOk"/>“'+row.title+'”？"><i class="glyphicon glyphicon-remove"></i></a>';
    }
    function receiveUsernameFormat(value, row, index) {
        var s = value||"";
        if (s.length > 20) {
            s = '<a href="javascript:void(0)" class="open-text" dialog-text="' + value + '">' + value.substr(0, 20) + '...</a>';
        }
        if (row.receiveType == 1) {
            return "<@spring.message "admin.simple.vip"/>：" + (s);
        } else if (row.receiveType == 2) {
            return "<@spring.message "admin.multi.vip"/>：" + s;
        } else if (row.receiveType == 3) {
            return "<@spring.message "admin.all.group.send"/>";
        } else if (row.receiveType == 4) {
            return "<@spring.message "admin.level.send.group"/>:" + degrees[''+row.degreeId];
        } else if (row.receiveType == 5) {
            return "<@spring.message "admin.group.send"/>:" +groups[''+row.groupId];
        } else if (row.receiveType == 6) {
            return "<@spring.message "admin.proxy.line.multi"/>:" +s;
        }
        return "";
    }
    var $formId = $("#system_message_form_id"), $table = $("#message1_datagrid_tb");
    $formId.on("click", "button.batchDelete", function (e) {
        //取消原始的事件
        var ids = '';
        $table.find('tbody input:checkbox:checked').each(function (i, j) {
            ids += $(j).val() + ",";
        })
        if (!ids) {
            layer.msg('<@spring.message "admin.sure.del.item"/>');
            return;
        }
        ids = ids.substring(0, ids.length - 1);
        layer.confirm('<@spring.message "admin.sure.del.selected.item"/>？', {btn: ['<@spring.message "manager.sure.name"/>', '<@spring.message "admin.member.info.cancle"/>']}, function (index) {
            $.ajax({
                url: '${adminBase}/message/delete.do',
                data: {ids: ids},
                success: function (res) {
                    if (res.success) {
                        layer.msg('<@spring.message "manager.delete.success"/>');
                        $table.find('input:checked').prop('checked', false);
                        refresh();
                    } else {
                        layer.msg(res.msg);
                    }
                    layer.close(index);
                }
            })
        }, function () {
            $table.find('input:checked').prop('checked', false);
        })
    })
    $table.on("click", "input.check_all", function () {
        var $this = $(this), isChecked = $this.prop('checked');
        $table.find('tbody input:checkbox').prop('checked', isChecked);
    });
    function refresh() {
        var $table = $formId.parents(".fui-box").data("bootstrapTable");
        if ($table && $table.length) {
            $table.bootstrapTable('refresh');
        }
    }
});
</script>
