<form id="member_guest_status_batch_modify_form_id" action="${adminBase}/accGuest/batchChangeStatus.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title">批量修改试玩账户状态(先选择用户,再点击批量修改用户状态)</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle">会员账号：</td>
                        <td width="70%">
                            <textarea name="accounts" style="height:150px;width:100%;"></textarea>
                            <div>
                                <br>数据格式：会员1,会员2,会员3 使用逗号或者空格隔开
                                <br>如：aaaaaa bbbbbb cccccc <span style="font-size:10px;color:red">使用空格隔开</span>
                                <br> &nbsp; &nbsp; &nbsp; &nbsp;aaaaaa,bbbbbb,cccccc <span style="font-size:10px;color:red">使用逗号隔开</span>
                            </div>
                        </td></tr>
                    <tr>
                        <td width="30%" class="text-right media-middle">状态：</td>
                        <td width="70%">
                            <label class="radio-inline"><input type="radio" name="accountStatus" value="2">启用</label>
                            <label class="radio-inline"><input type="radio" name="accountStatus" value="1">禁用</label>
                        </td></tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close">关闭</button>
                <button class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</form>
<script type="application/javascript">
    requirejs(['jquery'], function () {

        var $form = $("#member_guest_status_batch_modify_form_id");
        function getCheckBoxUsername() {
            var usernames = '';
            $("#guest_manager_datagrid_id").find('tbody input:checkbox:checked').each(function (i, j) {
                j = $(j);
                if (j.val() == 'on') {//避免抓到同一列的switch button
                    return;
                }
                usernames += j.data("name") + ",";
                // usernames += j.val() + ",";
            });

            usernames = usernames.substring(0, usernames.length - 1);
            $form.find('textarea[name="accounts"]').html(usernames);
        }
        getCheckBoxUsername();
    });
</script>