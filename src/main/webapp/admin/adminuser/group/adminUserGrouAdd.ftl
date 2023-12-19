<form action="${adminBase}/adminUserGroup/doAdd.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.adminUserGroup.addTitle"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.adminUserGroup.name"/>：</td>
                        <td><input name="name" class="form-control required" type="text"></td>
                    </tr>
                    <#if loginUser.type <= 50>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.type"/>：</td>
                        <td class="media-middle"><label class="radio-inline"><input type="radio" name="type" value="1"><@spring.message "admin.adminUserGroup.type1"/></label>&nbsp;
                            <label class="radio-inline"><input type="radio" name="type" value="2"><@spring.message "admin.adminUserGroup.type2"/></label>&nbsp;
                            <label class="radio-inline"><input type="radio" name="type" value="3"><@spring.message "admin.adminUserGroup.type3"/></label>&nbsp;
                            <label class="radio-inline"><input type="radio" name="type" value="4"><@spring.message "admin.adminUserGroup.type4"/></label>&nbsp;
                            <label class="radio-inline"><input type="radio" name="type" value="5" checked="checked"><@spring.message "admin.adminUserGroup.type5"/></label>
                        </td>
                    </tr>
                    </#if>
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