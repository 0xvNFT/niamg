<form id="member_modify_pwd_form_id1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.modify.pwd"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="userId" value="${userId}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right"><@spring.message "admin.modify.username"/>：</td>
                        <td>${username}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.modify.passwordType"/>：</td>
                        <td>
                            <select name="pwdType" class="form-control">
                                <option value="1" selected><@spring.message "admin.login.password"/></option>
                                <option value="2"><@spring.message "admin.withdrawal.password"/></option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.password.new"/>：</td>
                        <td><input type="password" class="form-control" name="pwd" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.password.verify"/>：</td>
                        <td><input type="password" class="form-control" name="rpwd" /></td>
                    </tr>
                     <#if onOffReceiptPwd>
                             <tr>
                                 <td class="text-right media-middle"><@spring.message "admin.secondPwd"/>：</td>
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
<script type="text/javascript">
    requirejs(['jquery'],function(){
        var $form=$("#member_modify_pwd_form_id1");
        $form.submit(function(){
            var password = $form.find("[name='pwd']").val()
                    ,rpassword = $form.find("[name='rpwd']").val()
                    ,type= $form.find("[name='pwdType']").val();
            if (!password) {
                layer.msg("密码不能为空！");
                return false;
            }
            if (!rpassword) {
                layer.msg("确认密码不能为空！");
                return false;
            }
            if (password !== rpassword) {
                layer.msg("两次密码不一致！");
                return false;
            }
            $.ajax({
                url : "${adminBase}/accGuest/pwdModifySave.do",
                data : {
                    userId: '${userId }',
                    newPwd : password,
                    okPwd : rpassword,
                    type:type ,
                    password : $form.find("[name='password']").val()
                },
                success : function(result) {
                    layer.closeAll();
                    layer.msg("密码修改成功！");
                }
            });
            return false;
        });
    });
</script>
