<form action="${managerBase}/stationDomain/modify.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.update.station.domain"/></h4>
            </div><input type="hidden" name="id" value="${domain.id}">
            <div class="modal-body">
                 <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "manager.domain"/>：</td>
                        <td>${domain.name}</td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "admin.deposit.table.depositType"/>：</td>
						<td><select class="form-control" name="type">
                            <option value="1"<#if domain.type==1>selected</#if>><@spring.message "manager.common.use"/></option>
                            <option value="2"<#if domain.type==2>selected</#if>><@spring.message "manager.system.name"/></option>
                            <option value="3"<#if domain.type==3>selected</#if>><@spring.message "manager.proxy.per.admin"/></option>
                            <option value="4"<#if domain.type==4>selected</#if>><@spring.message "manager.font"/></option>
                            <option value="5"<#if domain.type==5>selected</#if>><@spring.message "manager.app.domain"/></option>
                        </select></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "manager.default.proxy"/>：</td>
						<td><input type="text" class="form-control" name="proxyUsername"value="${domain.proxyUsername}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "manager.default.proxy.per"/>：</td>
						<td><input type="text" class="form-control" name="agentName"value="${domain.agentName}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle"><@spring.message "manager.default.main"/>：</td>
						<td><input type="text" class="form-control" name="defaultHome" value="${domain.defaultHome}"/></td>
					</tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "manager.get.ip.model"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1"<#if 1==domain.ipMode>checked</#if>> <@spring.message "manager.safe.pattern"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2"<#if 2==domain.ipMode>checked</#if>> <@spring.message "manager.common.pattern"/></label>
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
