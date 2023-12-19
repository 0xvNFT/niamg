<form id="member_modify_pwd_form_id1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title">修改密码</h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="userId" value="${userId}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right">用户名：</td>
                        <td>${username}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">密码类型：</td>
                        <td>
                            <select name="pwdType" class="form-control">
                                <option value="1" selected>登录密码</option>
                                <option value="2">提款密码</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">新密码：</td>
                        <td><input type="password" class="form-control" name="pwd" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">确认密码：</td>
                        <td><input type="password" class="form-control" name="rpwd" /></td>
                    </tr>
                     <#if onOffReceiptPwd>
                             <tr>
                                 <td class="text-right media-middle">二级密码：</td>
                                 <td><input type="password" class="form-control" name="password" required/></td>
                             </tr>
                     </#if>
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
