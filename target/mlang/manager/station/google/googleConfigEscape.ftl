<form action="${managerBase}/googleAuthConfig/addEscape.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title">过滤管理员</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle">站点：</td>
                        <td>${station.name}[${station.code}]<input type="hidden" name="stationId" value="${station.id}"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">管理员账号：</td>
                        <td><dl style="margin-bottom:2px"><#list userGroupMap?keys as key>
                        		 <dt>${key}:</dt><dd style="margin-left:20px"><#list userGroupMap["${key}"] as u>
                        			<label class="checkbox-inline<#if hadSet?seq_contains("${u}")> disabled bg-danger</#if>">
								  <input type="checkbox" name="username" value="${u}"<#if hadSet?seq_contains("${u}")> disabled</#if>> ${u}
								</label>
                        		</#list></dd></#list></dl>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close">关闭</button>
                <button class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</form>
