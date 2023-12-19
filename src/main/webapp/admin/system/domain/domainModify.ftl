<form action="${adminBase}/domain/doModify.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.domain.modifyTitle"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" value="${domain.id}" name="id">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.domain"/>：</td>
                        <td>${domain.name}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultProxy"/>：</td>
                        <td><input type="text" class="form-control" name="proxyUsername" value="${domain.proxyUsername}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultAgent"/>：</td>
                        <td><input type="text" class="form-control" name="agentName" value="${domain.agentName}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultHome"/>：</td>
                        <td><input type="text" class="form-control" name="defaultHome"value="${domain.defaultHome}"/>
                            <div>/register.do  <@spring.message "admin.domain.homeRegister"/></div>
                            <div>/login.do  <@spring.message "admin.domain.homeLogin"/></div>
                            <div>/m/index.do  <@spring.message "admin.domain.homeWap"/></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.sortNo"/>：</td>
                        <td><input type="text" class="form-control" name="sortNo" value="${domain.sortNo}"/></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "admin.domain.ipMode"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1"<#if domain.ipMode==1>checked</#if>> <@spring.message "admin.domain.ipMode.safe"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2"<#if domain.ipMode==2>checked</#if>> <@spring.message "admin.domain.ipMode.common"/></label>
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