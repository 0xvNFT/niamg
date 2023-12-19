<form id="guest_manager_add_form_id" method="post">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.accGuest.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <input type="hidden" name="modifyType" value="1">
                    <tbody>
                    <tr>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
                        <td width="30%" class="text-left">
                            <input type="text" class="form-control" name="username"/>
                        </td>
                        <td width="20%" class="text-right media-middle"><@spring.message "admin.realName"/>：</td>
                        <td width="30%" class="text-left">
                            <input type="text" class="form-control" name="realName"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                        <td class="text-left">
                            <select name="groupId" class="form-control">
                                <#list groups as group>
                                    <option value="${group.id}" >${group.name}</option>
                                </#list>
                            </select>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                        <td class="text-left">
                            <select name="levelId" class="form-control">
                                <#list degrees as level>
                                    <option value="${level.id}" >${level.name}</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left">
                            <select name="status" class="form-control">
                                <option value="2" selected><@spring.message "admin.enable"/></option>
                                <option value="1"><@spring.message "admin.disabled"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.password"/>：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="pwd"/>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.withdrawal.password"/>：</td>
                        <td class="text-left">
                            <input type="password" class="form-control" name="receiptPwd"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript">
    requirejs(['jquery'], function () {
        var $guestManagerAddFormId = $("#guest_manager_add_form_id");
        $guestManagerAddFormId.submit(function () {
            var password = $guestManagerAddFormId.find("[name='pwd']").val();
            var receiptPwd = $guestManagerAddFormId.find("[name='receiptPwd']").val();
            if (!password) {
                layer.msg('<@spring.message "admin.not.blank.password"/>');
                return false;
            }
            /*
            if (!receiptPwd) {
                layer.msg('<@spring.message "admin.not.blank.receiptPwd"/>');
                return false;
            }
            */
            confirmCommit();
            return false;
        });

        function confirmCommit() {
            $.ajax({
                url: "${adminBase}/accGuest/addGuestSave.do",
                data: $guestManagerAddFormId.serialize(),
                success: function (result) {
                    layer.closeAll();
                    layer.msg('<@spring.message "admin.deposit.handle.save.ok"/>');
                    var $table = $(".fui-box.active").data("bootstrapTable");
                    if ($table && $table.length) {
                        $table.bootstrapTable('refresh');
                    }
                }
            });
        }
    });
</script>
