<form action="${adminBase}/adminUser/updateUser.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.modify.adminUser"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <input type="hidden" name = "id" value="${user.id}">
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
                        <td width="35%" class="text-left">
                            <input name="username" class="form-control" disabled type="text" value="${user.username}"></td>
                    </tr>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.adminUserGroup"/>：</td>
                        <td width="35%" class="text-left"><select name="groupId" class="form-control">
                        <#list groups as g>
                            <option value="${g.id }"<#if g.id = user.groupId> selected</#if>>${g.name }</option>
                        </#list>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td class="text-left">
                            <input name="remark" class="form-control" type="text" value="${user.remark}">
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
