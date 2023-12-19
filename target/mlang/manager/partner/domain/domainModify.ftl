<form action="${managerBase}/partnerDomain/modify.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.update.oper.domain"/></h4>
            </div><input type="hidden" name="id" value="${domain.id}">
            <div class="modal-body">
                 <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "manager.domain"/>：</td>
                        <td>${domain.name}</td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "manager.get.ip.model"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1"<#if 1==domain.ipMode>checked</#if>><@spring.message "manager.safe.pattern"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2"<#if 2==domain.ipMode>checked</#if>><@spring.message "manager.common.pattern"/></label>
						</td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td colspan="3"><input type="text" class="form-control" name="remark"value="${domain.remark}"/></td>
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