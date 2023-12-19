<form class="fui-search table-tool" method="post" id="system_dummydata_form_id">
    <div class="form-group">
        <div class="form-inline">
        	<div class="input-group">
        		 <button class="btn btn-primary batchDelete" type="button"><@spring.message "admin.batch.del"/></button>
        	</div>
            <div class="input-group">
                <select name="type" class="form-control">
                    <option value="" selected><@spring.message "admin.withdraw.type.all"/></option>
                    <option value="1"><@spring.message "admin.red.jack.data"/></option>
                    <option value="2"><@spring.message "admin.round.jack.data"/></option>
                    <option value="3"><@spring.message "admin.invite.jack.data"/></option>
                </select>
            </div>
            <div class="input-group">
                <input type="text" class="form-control fui-date" name="begin" value="${startTime }" placeholder="<@spring.message "admin.startTime"/>">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <div class="input-group">
                <input type="text" name="end" class="form-control fui-date" value="${endTime }" placeholder="<@spring.message "admin.endTime"/>">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button class="btn btn-warning"><@spring.message "admin.search"/></button>
            <#if permAdminFn.hadPermission("admin:dummyData:add")>
        	<button class="btn btn-danger open-dialog" url="${adminBase}/dummyData/showAddWinData.do" type="button"><@spring.message "admin.random.data"/></button>
	        <button class="btn btn-primary open-dialog cached" url="${adminBase}/dummyData/showAdd.do" type="button"><@spring.message "admin.menu.add"/></button>
		    </#if>
        </div>
    </div>
</form>

<table class="fui-default-table" id="dummydata_tb_id"></table>
<script type="text/javascript">
requirejs(['jquery', 'bootstrap', 'Fui'], function () {
    Fui.addBootstrapTable({
     	id: 'dummydata_tb_id',
        url: "${adminBase}/dummyData/list.do",
        columns: [{
            field: 'checkbox',
            title: '<input type="checkbox" class="check_all">',
            align: 'center',
            vilign: 'middle',
            formatter: function(value, row, index) {
	            return '<input type="checkbox" value="' + row.id + '">';
	        }
        },{
            field: 'username',
            title: Admin.userName,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'type',
            title: Admin.jackTypes,
            align: 'center',
            valign: 'middle',
            formatter: typeFormatter
        }, {
            field: 'winMoney',
            title: Admin.jackBonus,
            align: 'center',
            sortable:true,
            valign: 'middle'
        }, {
            field: 'itemName',
            title: Admin.jackTitle,
            align: 'center',
            valign: 'middle'
        }, {
            field: 'winTime',
            title: Admin.dateTime,
            align: 'center',
            valign: 'middle',
            formatter: Fui.formatDatetime
        <#if permAdminFn.hadPermission("admin:dummyData:delete")>}, {
            title: Admin.op,
            align: 'center',
            valign: 'middle',
            formatter: function(value, row, index) {
	            return '<a class="todo-ajax" href="${adminBase}/dummyData/delete.do?ids='+row.id+'" title="<@spring.message "admin.sure.del.jack.data"/>?"><@spring.message "admin.deposit.bank.bankCard.del"/></a>';
	        }</#if>
        }]
    });
    function typeFormatter(value, row, index) {
        if (value == 1 || value == '1') {
            return '<@spring.message "admin.red.jack.data"/>';
        } else if (value == 2 || value == '2') {
            return '<@spring.message "admin.round.jack.data"/>'
        } else if (value == 3 || value == '3') {
            return '<@spring.message "admin.invite.jack.data"/>'
        }
    }
    var $formId = $("#system_dummydata_form_id"), $table = $("#dummydata_tb_id");
    $table.on("click", "input.check_all", function () {
        var $this = $(this), isChecked = $this.prop('checked');
        $table.find('tbody input:checkbox').prop('checked', isChecked);
    });
    $formId.on("click", "button.batchDelete", function (e) {
        var ids = '';
        $table.find('tbody input:checkbox:checked').each(function (i, j) {
            j = $(j);
            ids += j.val() + ",";
        })
        if (!ids) {
            layer.msg('<@spring.message "admin.sure.del.item"/>');
            return;
        }
        ids = ids.substring(0, ids.length - 1);
        layer.confirm('<@spring.message "admin.sure.del.selected.item"/>？', {btn: ['<@spring.message "manager.sure.name"/>', '<@spring.message "admin.member.info.cancle"/>']}, function (index) {
            $.ajax({
                url: '${adminBase}/dummyData/delete.do',
                data: {ids: ids},
                success: function (res) {
                    if (res.success) {
                        layer.msg('<@spring.message "manager.delete.success"/>');
                        $table.find('input:checked').prop('checked', false);
                        $table.bootstrapTable('refresh');
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
});
</script>
