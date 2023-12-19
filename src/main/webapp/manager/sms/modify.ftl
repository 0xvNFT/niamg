<form action="${managerBase}/sysGatewayConfig/save.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.update.station.domain"/></h4>
            </div><input type="hidden" name="id" value="${sms.id}">
            <div class="modal-body">
                 <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle">国家：</td>
                        <td><input type="text" class="form-control" name="country" value="${sms.country}"/></td>
                    </tr>
                    <tr>
						<td class="text-right media-middle">语种代码：</td>
						<td><select class="form-control" name="language">
                            <option value="es"<#if sms.language=='es'>selected</#if>>es</option>
                            <option value="th"<#if sms.language=='th'>selected</#if>>th</option>
                            <option value="vi"<#if sms.language=='vi'>selected</#if>>vi</option>
                            <option value="ms"<#if sms.language=='ms'>selected</#if>>ms</option>
                            <option value="id"<#if sms.language=='id'>selected</#if>>id</option>
                            <option value="in"<#if sms.language=='in'>selected</#if>>in</option>
                            <option value="br"<#if sms.language=='br'>selected</#if>>br</option>
                            <option value="ja"<#if sms.language=='ja'>selected</#if>>ja</option>
                            <option value="en"<#if sms.language=='en'>selected</#if>>en</option>
                            <option value="cn"<#if sms.language=='cn'>selected</#if>>cn</option>
                        </select></td>
					</tr>
					<tr>
						<td class="text-right media-middle">appKey：</td>
						<td><input type="text" class="form-control" name="appKey" value="${sms.appKey}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle">appSecret：</td>
						<td><input type="text" class="form-control" name="appSecret" value="${sms.appSecret}"/></td>
					</tr>
					<tr>
						<td class="text-right media-middle">appCode：</td>
						<td><input type="text" class="form-control" name="appCode" value="${sms.appCode}"/></td>
					</tr>
                    <tr>
						<td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.status"/>：</td>
                        <td><select class="form-control" name="status">
                            <option value="1"<#if sms.status==1>selected</#if>><@spring.message "admin.disabled"/></option>
                            <option value="2"<#if sms.status==2>selected</#if>><@spring.message "admin.enable"/></option>
                        </select></td>
					</tr>
                    <tr>
                        <td class="text-right media-middle">短信内容模板：</td>
                        <td colspan="3"><textarea rows="3" cols="52" name="content">${sms.content}</textarea></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">网关地址：</td>
                        <td colspan="3"><input type="text" class="form-control" name="gatewayUrl" value="${sms.gatewayUrl}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">后台登录帐号：</td>
                        <td colspan="3"><input type="text" class="form-control" name="account" value="${sms.account}"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">后台登录密码：</td>
                        <td colspan="3"><input type="text" class="form-control" name="password" value="${sms.password}"/></td>
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
