<form action="${adminBase}/appDomain/addSave.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.insert.app.domain"/></h4>
            </div>
            <div class="modal-body">
                <span style="color: red"><@spring.message "admin.app.domain.access.end"/></span>
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right vmid"><@spring.message "manager.app.domain"/>：</td>
                        <td><input name="domainUrl" placeholder="<@spring.message "admin.input.app.domain.write"/>" class="form-control required" type="text" minlength="5" maxlength="300"></td>
                    </tr>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "admin.ssl.certificate"/>：</td>
                        <td width="40%">
                            <select name="hasVertify" class="form-control">
                                <option value="2" selected><@spring.message "admin.have.certificate"/></option>
                                <option value="1"><@spring.message "admin.have.not.certificate"/></option>
                            </select>
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
