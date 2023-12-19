<form action="${adminBase}/promotion/doModifyAccessPage.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.extend.page.update"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.extend.page"/>：</td>
                        <td>
                            <select name="accessPage" class="form-control">
                                <option value="1"><@spring.message "admin.register"/></option>
                                <option value="2"><@spring.message "admin.main.page"/></option>
                                <option value="3"><@spring.message "admin.discount.activit"/></option>
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