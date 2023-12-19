<form action="${adminBase}/userBank/addSave.do" class="form-submit" id="userBankAddFormId">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.userBank.addTitle"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.userBank.own"/>：</td>
                        <td class="text-left">
                            <input type="text" name="username"  class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.realName"/>：</td>
                        <td class="text-left">
                            <input type="text" name="realName"  class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.cardNo"/>：</td>
                        <td class="text-left">
                            <input type="text" name="cardNo"  class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.ownBankName"/>：</td>
                        <td>
                            <select id="bank" class="form-control selectpicker" data-live-search="true">
                                <option value=""><@spring.message "admin.userBank.selectBank"/></option>
                                <#list bankList as bk>
                                    <option value="${bk.code}">${bk.name}</option>
                                </#list>
                            </select>
                        </td>
                        <input name="bankCode" id="bankCode"  type="hidden"/>
                    </tr>
                    <tr style="display: none" class="other-bank">
                        <td class="text-right media-middle"><@spring.message "admin.ownBankName"/>：</td>
                        <td class="text-left">
                            <input name="bankName" id="bankName" class="form-control" />
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.bankAddressLike"/>：</td>
                        <td class="text-left">
                            <input type="text" name="bankAddress" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.remark"/>：</td>
                        <td class="text-left">
                            <textarea name="remarks" class="form-control"></textarea>
                        </td>
                    </tr>
                    <#if onOffReceiptPwd>
                     <tr>
                         <td class="text-right media-middle"><@spring.message "admin.secondPwd"/>：</td>
                         <td><input type="password" class="form-control" name="password" required/></td>
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
<script type="application/javascript">
    requirejs(['jquery'], function () {
    	let form=$("#userBankAddFormId");
        form.find("#bank").change(function () {
            form.find("#bankCode").val($(this).val());
            if($(this).val() === 'other') {
               	form.find("#bankName").val("");
               	form.find(".other-bank").show();
            }else {
                form.find("#bankName").val($(this).find("option:selected").text());
                form.find(".other-bank").hide();
            }
        });
    });
</script>
