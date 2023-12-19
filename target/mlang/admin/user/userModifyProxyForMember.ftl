<form action="${adminBase}/user/doModifyProxy.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.belong.proxy"/></h4>
            </div>
            <input type="hidden" name="id" value="${member.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%"class="text-right media-middle"><@spring.message "admin.current.user"/>：</td>
                        <td class="media-middle">${member.username }</td>
                    </tr><tr>
                        <td class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" value="${member.proxyName}" class="form-control"/></td>
                    </tr>
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