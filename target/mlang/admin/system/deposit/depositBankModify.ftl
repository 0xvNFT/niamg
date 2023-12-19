<form action="${adminBase}/depositBank/modifySave.do" class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.bank.modify.title"/></h4>
            </div>
            <div class="modal-body"><input type="hidden" name="id" value="${bank.id }">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.deposit.bank.payPlatformCode"/>：</td>
                        <td width="35%" class="text-left"><select name="payPlatformCode" class="form-control">
                         <#list bankList as pay>
                            <option value="${pay.code}" <#if pay.code == bank.payPlatformCode>selected="selected"</#if>>${pay.name }</option>
                        </#list>
                        </select></td>

                        <td width="15%" class="usdt-msg-class text-right media-middle" style="display: none"><@spring.message "admin.deposit.bank.tronLinkAddr"/>：</td>
                        <td class="usdt-msg-class text-left" style="display: none">
                            <input placeholder="<@spring.message "admin.deposit.bank.tronLinkAddr.right"/>" name="usdtBankCard" value="${bank.bankCard}" class="form-control"/>
                        </td>
                        <td width="15%" class="bank-msg-class text-right media-middle" style="display: none"><@spring.message "admin.deposit.bank.bankCard"/>：</td>
                        <td class="bank-msg-class text-left" style="display: none">
                            <input placeholder="<@spring.message "admin.deposit.bank.bankCard.right"/>" name="bankCard" value="${bank.bankCard}" class="form-control"/>
                        </td>

                    </tr>
                    <tr class="other-bank-info" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.withdraw.bankName"/>：</td>
                        <td colspan="3"><input placeholder="<@spring.message "admin.input.correct.bank.name"/>" name="bankName" value="${bank.bankName}" class="form-control"/></td>
                    </tr>
                    <tr class="bank-info-class" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.realName"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.realName.right"/>" name="realName"
                                                     value="${bank.realName }" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.bankAddress"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.bankAddress.right"/>" name="bankAddress"
                                                     value="${bank.bankAddress }" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.qrCodeImg"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.qrCodeImg.right"/>" name="qrCodeImg" value="${bank.qrCodeImg }" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.min"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" name="min" value="${bank.min }"
                                                     class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.max"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" value="${bank.max }"
                                                     class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.icon"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>" name="icon" value="${bank.icon }"
                                                     class="form-control regexp" regexp="[^<]+" tip="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>"/></td>
                    </tr>
                    <tr class="bank-rate-class" style="display: none">
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.depositRate"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.depositRate.right"/>" name="depositRate" value="${bank.depositRate}"
                                                     class="form-control money"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.withdrawRate"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.withdrawRate.right"/>" name="withdrawRate" value="${bank.withdrawRate}"
                                                     class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.sortNo.tip"/>" name="sortNo" value="${bank.sortNo }"
                                                     class="form-control" type="number"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.remark.right"/>" name="remark"  value="${bank.remark }" class="form-control"/></td>
                    </tr>
                    <tr>

                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.status"/>：</td>
                        <td class="text-left"><select name="status" class="form-control">
                            <option value="1"
                                    <#if bank.status == 1>selected</#if>
                            ><@spring.message "admin.deposit.bank.bankCard.status.no"/>
                            </option>
                            <option value="2"
                                    <#if 2==bank.status >selected</#if>
                            ><@spring.message "admin.deposit.bank.bankCard.status.yes"/>
                            </option>
                        </select></td>

                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.bgRemark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.bgRemark"/>" type="text" name="bgRemark" value="${bank.bgRemark }"
                                   class="form-control"></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>
                        <td colspan="3">
                            <#list degrees as d>
                                <label class="checkbox-inline"><input type="checkbox" name="degreeIds" value="${d.id }"
                                <#if bank.degreeIds?has_content && bank.degreeIds?contains(","+d.id+",")>checked</#if>>${d.name }</label>
                            </#list>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.groupIds"/>：</td>
                        <td colspan="3">
                            <#list groups as g>
                                <label class="checkbox-inline"><input type="checkbox" name="groupIds" value="${g.id }"
                                <#if bank.groupIds?has_content && bank.groupIds?contains(","+g.id+",")>checked</#if>>${g.name }</label>
                            </#list>
                        </td>
                    </tr>
<#--                    <tr>-->
<#--                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>-->
<#--                        <td colspan="3">-->
<#--                            <#list levels as le>-->
<#--                                <label class="checkbox-inline">-->
<#--                                <input type="checkbox" name="groupLevelId" value="${le.id }"-->
<#--                                <#list bankLevel as bl>-->
<#--                                    <#if bl == le.id>-->
<#--                                        checked-->
<#--                                    </#if>-->
<#--                                </#list>-->
<#--                            >${le.name }-->
<#--                                </label>-->
<#--                            </#list>-->
<#--                        </td>-->
<#--                    </tr>-->
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
         if('${bank.payPlatformCode}' === 'other') {
             $(".other-bank-info").show();
         }
        $('select[name="payPlatformCode"]').change(function () {
            if($(this).val() === 'other') {
                $(".other-bank-info").show();
            }else {
                $(".other-bank-info").hide();
            }
            var bankChangeName =$("select[name='payPlatformCode']").find("option:selected").text();
            if (bankChangeName === usdtName){
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
    })
</script>
