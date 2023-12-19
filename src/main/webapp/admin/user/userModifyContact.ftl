<form action="${adminBase}/user/doModifyContact.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.vip.contact"/></h4>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" value="${memberInfo.userId }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right"><@spring.message "manager.username.input"/>：</td>
                        <td>${username}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.telephone"/>：</td>
                        <td><input type="text" class="form-control" name="phone" value="${memberInfo.phone }"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">WeChat：</td>
                        <td class="text-left"><input type="text" class="form-control" name="wechat"
                                                     value="${memberInfo.wechat }"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.mail"/>：</td>
                        <td><input type="text" class="form-control email" name="email" value="${memberInfo.email }"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">QQ：</td>
                        <td><input type="text" class="form-control" name="qq" value="${memberInfo.qq }"/></td>
                    </tr>
                     <tr>
                        <td class="text-right media-middle">Facebook：</td>
                        <td><input type="text" class="form-control" name="facebook" value="${memberInfo.facebook }"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">Line：</td>
                        <td class="text-left"><input type="text" class="form-control" name="line"
                                                     value="${memberInfo.line }"/></td>
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