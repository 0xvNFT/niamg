<form id="online_deposit_remark_modify_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.update.remark"/></h4>
            </div>
            <input type="hidden" name="id" value="${onlineDeposit.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td>
                            <textarea name="remark" class="form-control">${onlineDeposit.bgRemark}</textarea>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.closed"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
    requirejs(['jquery'], function () {
        var $form = $("#online_deposit_remark_modify_form_id");
        $form.submit(function () {
            confirmCommit();
            return false;
        });

        function confirmCommit() {
            $.ajax({
                url: "${adminBase}/depositOnline/onlineDepositremarkModifySave.do",
                data: $form.serialize(),
                success: function (result) {
                    layer.closeAll();
                    layer.msg('<@spring.message "admin.deposit.handle.save.ok"/>!');
                    var $table = $(".fui-box.active").data("bootstrapTable");
                    if ($table && $table.length) {
                        $table.bootstrapTable('refresh');
                    }
                }
            });
        }
    });
</script>
