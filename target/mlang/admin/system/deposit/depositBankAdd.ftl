<form action="${adminBase}/depositBank/addSave.do" class="form-submit" id="dep_bank_add_form_id">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.bank.bankCard.add.title"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.deposit.bank.payPlatformCode"/>：</td>
                        <td width="35%"><select name="payPlatformCode" class="form-control">
                        <#list bankList as pay>
                            <option value="${pay.code}">${pay.name }</option>
                        </#list>
                        </select></td>

                        <td width="15%" class="usdt-msg-class text-right media-middle" style="display: none"><@spring.message "admin.deposit.bank.tronLinkAddr"/>：</td>
                        <td class="usdt-msg-class text-left" style="display: none">
                            <input placeholder="<@spring.message "admin.deposit.bank.tronLinkAddr.right"/>" name="usdtBankCard" class="form-control"/>
                        </td>
                        <td width="15%" class="bank-msg-class text-right media-middle" style="display: none"><@spring.message "admin.deposit.bank.bankCard"/>：</td>
                        <td class="bank-msg-class text-left" style="display: none">
                            <input placeholder="<@spring.message "admin.deposit.bank.bankCard.right"/>" name="bankCard" class="form-control"/>
                        </td>
                    </tr>
                    <tr class="other-bank-info" style="display: none">
                        <td class="text-right media-middle"><@spring.message "base.stationBankName"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.input.correct.bank.name"/>" name="bankName" class="form-control"/></td>
                    </tr>

                    <tr class="bank-info-class" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.realName"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.realName.right"/>" name="realName" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.bankAddress"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.bankAddress.right"/>" name="bankAddress" class="form-control"/></td>
                    </tr>

                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.qrCodeImg"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.qrCodeImg.right"/>" name="qrCodeImg" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.min"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" name="min" class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.max"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.icon"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>" name="icon" class="form-control regexp" regexp="[^<]+"
                                   tip="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>"/></td>
                    </tr>
                    <tr class="bank-rate-class" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.depositRate"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.depositRate.right"/>" name="depositRate" class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.withdrawRate"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.withdrawRate.right"/>" name="withdrawRate" class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.sortNo.tip"/>" name="sortNo" class="form-control"
                                                     type="number"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.remark.right"/>" name="remark" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.status"/>：</td>
                        <td><select name="status" class="form-control">
                            <option value="1"><@spring.message "admin.deposit.bank.bankCard.status.no"/></option>
                            <option value="2" selected><@spring.message "admin.deposit.bank.bankCard.status.yes"/></option>
                        </select></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.bgRemark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.bgRemark"/>" type="text" name="bgRemark" class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>
                        <td colspan="3">
                            <#list degrees as d>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${d.id }" checked>${d.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.groupIds"/>：</td>
                        <td colspan="3">
                            <#list groups as g>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${g.id }" checked>${g.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <#if onOffReceiptPwd>
                     <tr>
                         <td class="text-right media-middle"><@spring.message "admin.deposit.handle.password"/>：</td>
                         <td colspan="3"><input type="password" class="form-control" name="password" required/></td>
                     </tr>
                    </#if>
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
<script type="text/javascript">
    var usdtName = '${usdtName}';
    $(function () {
         $('select[name="payPlatformCode"]').change(function () {
             if($(this).val() === 'other') {
                  $(".other-bank-info").show();
             }else {
                 $(".other-bank-info").hide();
             }
             var checkText =$("select[name='payPlatformCode']").find("option:selected").text();
             if (checkText === usdtName){
                 // 设置提交的表单 bankName 以"USDT"参数传递，以防后台进行相关验证时 bankCode 不为"USDT" （当银行卡资料管理 新增类型 选择为 OTHER 时，会出现此情况）
                 $("#dep_bank_add_form_id").find("[name='bankName']").val(checkText);

                 $(".bank-info-class").hide();
                 $(".usdt-msg-class").show();
                 $(".bank-msg-class").hide();
                 $(".bank-rate-class").show();
             }else {
                 $(".bank-info-class").show();
                 $(".usdt-msg-class").hide();
                 $(".bank-msg-class").show();
                 $(".bank-rate-class").hide();
             }
         });
        $('select[name="payPlatformCode"]').trigger('change');
    });

</script>

