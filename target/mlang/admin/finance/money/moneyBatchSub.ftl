<form action="${adminBase}/finance/moneyOpe/batchSubMoney.do" class="form-submit" id="money_ope_batch_sub_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.bat.decode.money"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="type" value="2">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.money.history.username"/>：</td>
                        <td width="70%"><textarea name="usernames" style="height:150px;width:100%;"></textarea>
                            <br><@spring.message "admin.data.format.multi.blank.divide"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "manager.oper.type"/>：</td>
                        <td>
                            <label class="radio-inline"><input type="radio" name="type" value="2" checked><@spring.message "admin.common.deduce.cash"/></label>
                            <label class="radio-inline"><input type="radio" name="type" value="35"><@spring.message "admin.color.deduce.cash"/></label>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "admin.operate.cash"/>：</td>
                        <td><input type="text" autocomplete="off" class="form-control" name="money" /></td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "admin.operate.reason"/>：</td>
                        <td><textarea class="form-control" name="remark"></textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right active media-middle"><@spring.message "admin.deposit.bgRemark"/>：</td>
                        <td><textarea class="form-control" name="bgRemark"></textarea></td>
                    </tr>
                    <#if isReceiptPwd>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.password"/>：</td>
                        <td><input type="password" class="form-control" name="password" required/></td>
                    </tr>
                    </#if>
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
<script type="application/javascript">
    requirejs(['jquery'], function () {
        var $form = $("#money_ope_batch_sub_form_id");
        var usernames = '';
        $("#user_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
            j = $(j);
            if (j.val()=='on') {
                return ;
            }
            usernames += j.val() + ",";
        });
        if (usernames) {
            $form.find("select[name='modelType']").val(2).change();
            usernames = usernames.substring(0, usernames.length - 1);
            $form.find('textarea[name="usernames"]').html(usernames);
        }
    });
</script>
