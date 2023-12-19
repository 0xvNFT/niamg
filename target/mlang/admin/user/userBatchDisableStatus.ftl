<form action="${adminBase}/user/doBatchDisableStatus.do" class="form-submit" id="member_disable_batch_modify_form_id">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.bat.dis.user"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <td width="30%" class="text-right media-middle"><@spring.message "admin.money.history.username"/>：</td>
                    <td width="70%">
                        <textarea name="usernames" style="height:150px;width:100%;"></textarea>
                        <div>
                            <br><@spring.message "admin.data.format.blank"/>
                            <br>如：aaaaaa bbbbbb cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.blank.div"/></span>
                            <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa,bbbbbb,cccccc <span style="font-size:10px;color:red"><@spring.message "admin.data.points.div"/></span>
                        </div>
                    </td>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
                <button class="btn btn-primary"><@spring.message "admin.deposit.handle.save"/></button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
requirejs(['jquery'], function () {
    var $form = $("#member_disable_batch_modify_form_id"),usernames = '';
    $("#user_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
        j = $(j);
        if (j.val()=='on') {
            return ;
        }
        usernames += j.val() + ",";
    });
    usernames = usernames.substring(0, usernames.length - 1);
    $form.find('textarea[name="usernames"]').html(usernames);
});
</script>
