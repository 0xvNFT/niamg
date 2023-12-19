<form action="${adminBase}/redPacketRecord/handlerRecord.do" class="form-submit">
    <div class="modal-dialog"><input type="hidden" name="id" value="${record.id }">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.red.process.record"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="30%" class="text-right"><@spring.message "manager.username.input"/>：</td>
                        <td class="text-left">${record.username }</td>
                    </tr>
                    <tr>
                        <td class="text-right"><@spring.message "admin.cash.money.paper"/>：</td>
                        <td class="text-left">${record.money}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.handle.result"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="2" selected><@spring.message "admin.deposit.status.be.success"/></option>
                            <option value="3"><@spring.message "admin.deposit.status.be.fail"/></option>
                        </select></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.process.info"/>：</td>
                        <td class="text-left"><textarea name="remark" class="form-control"></textarea></td>
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