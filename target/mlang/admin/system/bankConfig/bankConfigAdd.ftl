<form action="${adminBase}/bankConfig/addSave.do" class="form-submit" id="userBankAddFormId">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.bankConfig.addTitle"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.type"/>：</td>
                        <td class="text-left">
                            <select name="bankCode" class="form-control">
                                <#list banks as bank>
                                    <option value="${bank.bankCode}">${bank.bankName }</option>
                                </#list>
                            </select>
                        </td>
                    </tr>
                    <tr id="other_bank_name">
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.bankName"/>：</td>
                        <td class="text-left">
                            <input type="text" name="name"  class="form-control"/>
                        </td>
                    </tr>
                    <tr id="other_bank_code">
                        <td class="text-right media-middle"><@spring.message "admin.bankCodeLike"/>：</td>
                        <td class="text-left">
                            <input type="text" name="code"  class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.serial.num"/>：</td>
                        <td class="text-left">
                            <input type="text" required="required" name="sortNo"  class="form-control"/>
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
<script type="text/javascript">
    $(function () {
        $('select[name="bankCode"]').change(function () {
            var checkText =$("select[name='bankCode']").find("option:selected").text();
            if (checkText === 'OTHER'){
                $("#other_bank_code").show();
                $("#other_bank_name").show();
            }else {
                $("#other_bank_code").hide();
                $("#other_bank_name").hide();
            }
        });
        $('select[name="bankCode"]').trigger('change');
    });
</script>
