<form action="${adminBase}/user/doAddMember.do" method="post" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "manager.vip.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <input type="hidden" name="modifyType" value="1">
                    <tbody>
                    <tr>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.account.login"/>：</td>
                        <td width="20%"><input type="text" class="form-control required" name="username"/></td>
                        <td width="13%" class="text-right media-middle"><@spring.message "manager.password.input"/>：</td>
                        <td width="20%"><input type="password" class="form-control required" name="pwd"/></td>
                        <td width="13%" class="text-right media-middle"><@spring.message "admin.proxy.merchant"/>：</td>
                        <td><input type="text" class="form-control" name="agentName"/></td>
                    </tr>
                    <tr>
                    	<#if proxyModel!=4><td class="text-right media-middle"><@spring.message "admin.point.proxy"/>：</td>
                        <td><input type="text" name="proxyName" class="form-control"/></td></#if>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.table.degreeName"/>：</td>
                        <td<#if proxyModel==4> colspan="2"</#if>><select name="degreeId" class="form-control">
                            <#list degrees as le><option value="${le.id}">${le.name}</option></#list>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.vip.group"/>：</td>
                        <td<#if proxyModel==4> colspan="2"</#if>><select name="groupId" class="form-control">
                            <#list groups as le><option value="${le.id}">${le.name}</option></#list>
                        </select></td>
                    </tr>
                    <tr>
                    	<td class="text-right media-middle"><@spring.message "admin.deposit.table.realName"/>：</td>
                        <td><input type="text" class="form-control" name="realName"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.telephone"/>：</td>
                        <td><input type="text" class="form-control" name="phone"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.mail"/>：</td>
                        <td><input type="text" class="form-control email" name="email"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle">QQ：</td>
                        <td><input type="text" class="form-control" name="qq"/></td>
                        <td class="text-right media-middle">Line：</td>
                        <td><input type="text" class="form-control" name="wechat"/></td>
                        <td class="text-right media-middle">Facebook：</td>
                        <td><input type="text" class="form-control" name="facebook"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.remark"/>：</td>
                        <td colspan="5"><input type="text" class="form-control" name="remark"/></td>
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
