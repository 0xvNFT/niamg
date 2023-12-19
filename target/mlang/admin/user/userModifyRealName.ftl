<form action="${adminBase}/user/doModifyRealName.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.update.real.nam"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <input type="hidden" value="${memberInfo.userId}" name="id">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.real.name"/>：</td>
                        <td>
                            <input type="text" name="realName" value="${memberInfo.realName}"class="form-control" />
                        </td>
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
