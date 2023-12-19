<form action="${adminBase}/agentUser/doAdd.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.proxy.merchant.acc"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
                        <td width="35%"><input name="username" class="form-control required" type="text"></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.proxy.merchant"/>：</td>
                        <td width="35%"><select name="agentId" class="form-control">
                        	<#list agents as g><option value="${g.id }">${g.name }</option></#list>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.password"/>：</td>
                        <td><input name="password" class="form-control required" type="password"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td><select name="status" class="form-control">
                            <option value="1"><@spring.message "admin.disable"/></option>
                            <option value="2" selected><@spring.message "admin.enable"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.realName"/>：</td>
                        <td><input name="realName" class="form-control" type="text"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td><input name="remark" class="form-control" type="text"/></td>
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
