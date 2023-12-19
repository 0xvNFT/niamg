<form action="${adminBase}/agentUser/doModifyRealName.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.proxy.merchant.acc"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" name="id" value="${agentUser.id}">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
                        <td>${agentUser.username}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.realName"/>：</td>
                        <td><input name="realName" class="form-control" type="text" value="${agentUser.realName}"/></td>
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
