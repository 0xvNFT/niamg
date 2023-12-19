<form action="${adminBase}/adminUser/withdrawLimitModifySave.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.adminUser.withdrawHandelLimitTitle"/></h4>
            </div>
            <input type="hidden" name="id" value="${user.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.adminUser.withdrawHandelLimit"/>：</td>
                        <td>
                        	<input name="withdrawLimit" class="form-control required" type="text" value="${user.withdrawLimit }">
                        </td>
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