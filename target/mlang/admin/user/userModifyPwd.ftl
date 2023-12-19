<form id="member_modify_pwd_form_id1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.password.update"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${id}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right"><@spring.message "manager.username.input"/>：</td>
                        <td>${username}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.new.password"/>：</td>
                        <td><input type="password" class="form-control" name="pwd" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.confirm.password"/>：</td>
                        <td><input type="password" class="form-control" name="rpwd" /></td>
                    </tr>
                    <#if receiptPwd>
                         <tr>
                             <td class="text-right media-middle"><@spring.message "admin.deposit.handle.password"/>：</td>
                             <td><input type="password" class="form-control" name="password" required/></td>
                         </tr>
                     </#if>
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
<script type="text/javascript">
    requirejs(['jquery'],function(){
        var $form=$("#member_modify_pwd_form_id1");
        $form.submit(function(){
            var password = $form.find("[name='pwd']").val(),rpassword = $form.find("[name='rpwd']").val();
            if (!password) {
                layer.msg("<@spring.message "admin.not.blank.password"/>");
                return false;
            }
            if (!rpassword) {
                layer.msg("<@spring.message "admin.confirm.pass.not"/>");
                return false;
            }
            if (password !== rpassword) {
                layer.msg("<@spring.message "admin.second.pass.not.same"/>");
                return false;
            }
            $.ajax({
                url : "${adminBase}/user/doUpdatePwd.do",
                data : {
                    id: '${id }',
                    pwd : password,
                    rpwd : rpassword,
                    password : $form.find("[name='password']").val()
                },
                success : function(result) {
                    layer.closeAll();
                    layer.msg("<@spring.message "admin.pass.succ"/>");
                }
            });
            return false;
        });
    });
</script>
