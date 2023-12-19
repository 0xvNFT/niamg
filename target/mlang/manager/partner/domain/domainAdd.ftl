<form action="${managerBase}/partnerDomain/add.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.bound.oper.domain"/></h4>
            </div>
            <div class="modal-body">
                 <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "manager.coper"/>：</td>
                        <td><select class="form-control" name="partnerId">
							<#list partners as s><option value="${s.id}">${s.name}</option></#list>
						</select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "manager.domain"/>：</td>
                        <td><textarea name="domains" class="form-control" rows="3"></textarea>
                        <div><@spring.message "manager.multi.blank.domain"/></div></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "manager.get.ip.model"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1" checked><@spring.message "manager.safe.pattern"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2"><@spring.message "manager.common.pattern"/></label>
						</td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.remark.bgRemark"/>：</td>
                        <td><input type="text" class="form-control" name="remark"/></td>
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