<form action="${adminBase}/adminUser/addAdminUser.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.add.adminUser"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.username"/>：</td>
                        <td width="35%" class="text-left"><input name="username" class="form-control required"
                                                                 type="text"></td>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.adminUserGroup"/>：</td>
                        <td width="35%" class="text-left"><select name="groupId" class="form-control">
                        <#list groups as g>
                            <option value="${g.id }">${g.name }</option>
                        </#list>
                        </select></td>
                    </tr>
                    <tr class="pwd_tr">
                        <td class="text-right media-middle"><@spring.message "admin.password"/>：</td>
                        <td class="text-left"><input name="password" class="form-control required" type="password"/>
                        </td>
                        <td class="text-right media-middle"><@spring.message "admin.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="1"><@spring.message "admin.disabled"/></option>
                            <option value="2" selected><@spring.message "admin.enable"/></option>
                        </select></td>
                    </tr>
                    <tr class="pwd_tr">
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td class="text-left"><input name="remark" class="form-control" type="text"/>
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
