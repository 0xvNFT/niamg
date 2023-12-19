<form class="fui-search table-tool" method="post" id="system_advice_form_id">
    <div class="form-group fui-data-wrap">
        <div class="form-inline">
            <div class="input-group">
                <input type="text" class="form-control fui-date" format="YYYY-MM-DD HH:mm:ss" value="${startTime}"
                       name="startTime" placeholder="<@spring.message "manager.begin.date"/>" autocomplete="off">
                <span class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='today'><@spring.message "manager.today.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='yesterday'><@spring.message "manager.yesterday.date"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisWeek'><@spring.message "manager.week.date"/></button>
            <div class="input-group">
                <select class="form-control" name="sendType">
                    <option value="" selected><@spring.message "admin.advice.types"/></option>
                    <option value="1"><@spring.message "admin.submit.advice"/></option>
                    <option value="2"><@spring.message "admin.complaint.me"/></option>
                </select>
            </div>
            <button class="btn btn-primary fui-date-search"><@spring.message "admin.deposit.button.search"/></button>
            <#if permAdminFn.hadPermission("admin:adviceFeedback:del")>
                <button class="btn btn-primary batchDelete"><@spring.message "admin.batch.del"/></button>
            </#if>
        </div>
        <div class="form-inline mt5px">
            <div class="input-group">
                <input type="text" name=endTime format="YYYY-MM-DD HH:mm:ss" class="form-control fui-date"
                       placeholder="<@spring.message "manager.end.date"/>" value="${endTime}" autocomplete="off"> <span
                        class="glyphicon glyphicon-th form-control-feedback" aria-hidden="true"></span>
            </div>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastWeek'><@spring.message "manager.last.week"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='thisMonth'><@spring.message "manager.this.month"/></button>
            <button type="button" class="btn btn-default fui-date-btn" data-target='lastMonth'><@spring.message "manager.last.month"/></button>
            <div class="input-group">
                <input type="text" class="form-control" name="sendAccount" placeholder="<@spring.message "admin.money.history.username"/>">
            </div>
        </div>
    </div>
</form>
<table id="advice_datagrid_tb"></table>
<script type="text/javascript">
    requirejs(['jquery', 'bootstrap', 'Fui'], function () {
        Fui.addBootstrapTable({
            id: 'advice_datagrid_tb',
            url: '${adminBase}/adviceFeedback/list.do',
            columns: [{
                field: 'checkbox',
                title: '<input type="checkbox" class="check_all">',
                align: 'center',
                vilign: 'middle',
                formatter: operateCheckboxMatter
            }, {
                field: 'sendUsername',
                title: Admin.memberAcc,
                align: 'center',
                valign: 'middle'
            }, {
                field: 'sendType',
                title: Admin.adviceType,
                align: 'center',
                valign: 'middle',
                formatter:sendTypeFormat
            }, {
                field: 'content',
                title: Admin.contentDetail,
                align: 'center',
                valign: 'middle',
                formatter: itemFormat
            }, {
                field: 'createTime',
                title: Admin.submitTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                field: 'finalTime',
                title: Admin.feedbackTime,
                align: 'center',
                valign: 'middle',
                formatter: Fui.formatDatetime
            }, {
                field: 'status',
                title: Admin.status,
                align: 'center',
                valign: 'middle',
                formatter:statusFormat
            }, {
                title: Admin.op,
                align: 'center',
                valign: 'middle',
                formatter: operateFormatter
            }]
        });

        function operateFormatter(value, row, index) {
            return [
            	'<a class="open-dialog" href="${adminBase}/adviceFeedback/reply.do?id=', row.id, '" title="<@spring.message "admin.answer"/>"><@spring.message "admin.answer"/></a> ']
                .join('');
        }

        function itemFormat(value, row, index) {
        	var enterIndex = 15;
            if (value && value.length > enterIndex) {
                return '<a class="open-dialog" href="${adminBase}/adviceFeedback/item.do?id=' + row.id + ' ">' + value.substring(0, 15) + '......' + '</a>';
            }
            return '<a class="open-dialog" href="${adminBase}/adviceFeedback/item.do?id=' + row.id + ' ">' + row.content + '</a>'
        }

        function sendTypeFormat(value, row, index) {
            if (!value) {
                value = "";
            }
            if (row.sendType == 1) {
                return "<@spring.message "admin.submit.advice"/>";
            } else if (row.sendType == 2) {
                return "<@spring.message "admin.complaint.me"/>";
            }

        }
        function statusFormat(value, row, index) {
            if (!value) {
                value = "";
            }
            if (row.status == 1) {
                return "<@spring.message "admin.await.answer"/>";
            } else if (row.status == 2) {
                return "<@spring.message "admin.already.answer"/>";
            }

        }
        function operateCheckboxMatter(value, row, index) {
            return '<input type="checkbox" value="' + row.id + '">';
        }

        var $formId = $("#system_advice_form_id"), $table = $("#advice_datagrid_tb");

        $formId.on("click", "button.batchDelete", function (e) {
            //取消原始的事件
            e.preventDefault();
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
                    url: '${adminBase}/adviceFeedback/delete.do',
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
