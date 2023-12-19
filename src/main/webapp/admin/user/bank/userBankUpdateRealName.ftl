<form action="${adminBase}/userBank/doUpdateRealName.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.userBank.updateRealNameTitle"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <input type="hidden" value="${bank.id}" name="id">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.realName"/>：</td>
                        <td>
                            <input type="text" name="realName" value="${bank.realName}"class="form-control" />
                        </td>
                    </tr>
                    <#if onOffReceiptPwd>
                     <tr>
                         <td class="text-right media-middle"><@spring.message "manager.second.password"/>：</td>
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