<form action="${adminBase}/depositBank/bankDepositremarkModifySave.do" class="form-submit">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.bank.remark.title"/></h4>
            </div>
            <input type="hidden" name="id" value="${bankDeposit.id }">
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td>
                            <textarea name="remark" class="form-control">${bankDeposit.bgRemark}</textarea>
                        </td>
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