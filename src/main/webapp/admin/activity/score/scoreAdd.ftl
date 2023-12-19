<form action="${adminBase}/scoreHistory/doSave.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.menu.scoreHis.add"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right media-middle"><@spring.message "manager.oper.type"/>：</td>
                        <td><select name="type" class="form-control">
                            <option value="6" selected><@spring.message "admin.add.scores"/></option>
                            <option value="7"><@spring.message "admin.deduce.scores"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.username"/>：</td>
                        <td><input type="text" name="username" class="form-control" placeholder="<@spring.message "admin.deposit.handle.username"/>" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.scores"/>：</td>
                        <td><input type="text" name="score" class="form-control digits" /></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.operator.reason"/>：</td>
                        <td><input type="text" name="remark" class="form-control" /></td>
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
