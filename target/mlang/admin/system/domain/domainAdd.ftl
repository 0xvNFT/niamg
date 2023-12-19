<form action="${adminBase}/domain/doSave.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.domain.addTitle"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.domain"/>：</td>
                        <td><textarea name="domains" class="form-control" rows="3"></textarea>
                            <@spring.message "admin.domain.addDomainTip"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultProxy"/>：</td>
                        <td><input type="text" class="form-control" name="proxyUsername" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultAgent"/>：</td>
                        <td><input type="text" class="form-control" name="agentName" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.domain.defaultHome"/>：</td>
                        <td><input type="text" class="form-control" name="defaultHome"/>
                            <div>/register.do  <@spring.message "admin.domain.homeRegister"/></div>
                            <div>/login.do  <@spring.message "admin.domain.homeLogin"/></div>
                            <div>/m/index.do  <@spring.message "admin.domain.homeWap"/></div>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.sortNo"/>：</td>
                        <td><input type="text" class="form-control" name="sortNo"/></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "admin.domain.ipMode"/>：</td>
						<td>
							<label class="radio-inline"><input type="radio" name="ipMode" value="1"> <@spring.message "admin.domain.ipMode.safe"/></label>
							<label class="radio-inline"><input type="radio" name="ipMode" value="2" checked> <@spring.message "admin.domain.ipMode.common"/></label>
						</td>
					</tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td><input type="text" class="form-control" name="remark"/></td>
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