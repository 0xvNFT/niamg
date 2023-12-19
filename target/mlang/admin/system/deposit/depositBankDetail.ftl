<form class="form-submit">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close fui-close" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><@spring.message "admin.deposit.bank.detail.title"/></h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped">
                    <tbody>
                    <tr>
                        <td width="15%" class="text-right media-middle"><@spring.message "admin.deposit.bank.payPlatformCode"/>：</td>
                        <td width="35%" class="text-left">
                            <select name="payPlatformCode" class="form-control" disabled="disabled">
                        <#list bankList as pay>
                            <option value="${pay.code }"
                                    <#if pay.code == bank.payPlatformCode>selected="selected"</#if>>${pay.name }</option>
                        </#list>
                        </select></td>


                        <td width="15%" class="text-right media-middle" <#if isFirstUSDT != 1> style="display: none" </#if>><@spring.message "admin.deposit.bank.tronLinkAddr"/>：</td>
                        <td class="text-left" <#if isFirstUSDT != 1> style="display: none" </#if>><input placeholder="<@spring.message "admin.deposit.bank.tronLinkAddr.right"/>" disabled="disabled" <#if isFirstUSDT == 1>name="bankCard" value="${bank.bankCard }</#if>"
                                                     class="form-control"/></td>

                        <td width="15%" class="text-right media-middle" <#if isFirstUSDT == 1> style="display: none" </#if>><@spring.message "admin.deposit.bank.bankCard"/>：</td>
                        <td class="text-left" <#if isFirstUSDT == 1> style="display: none" </#if>><input placeholder="<@spring.message "admin.deposit.bank.bankCard.right"/>" disabled="disabled" <#if isFirstUSDT != 1>name="bankCard" value="${bank.bankCard }</#if>"
                                                     class="form-control"/></td>

                    </tr>
                    <tr class="other-bank-info" style="display: none">
                        <td class="text-right media-middle">银行名称：</td>
                        <td colspan="3"><input placeholder="请输入正确的银行名称" disabled="disabled" name="bankName" value="${bank.bankName}" class="form-control"/></td>
                    </tr>
                    <tr <#if isFirstUSDT == 1> style="display: none" </#if>>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.realName"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.realName.right"/>" name="realName"
                                                     value="${bank.realName }" disabled="disabled" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.bankAddress"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.bankAddress.right"/>" name="bankAddress"
                                                     value="${bank.bankAddress }" disabled="disabled" class="form-control"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.qrCodeImg"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.qrCodeImg.right"/>" name="qrCodeImg" disabled="disabled" value="${bank.qrCodeImg }" class="form-control"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.min"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.min.right"/>" disabled="disabled" name="min" value="${bank.min }"
                                                     class="form-control money"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.max"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.max.right"/>" name="max" value="${bank.max }"
                                                     class="form-control money" disabled="disabled"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.icon"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>" name="icon" value="${bank.icon }"
                                                     class="form-control regexp" disabled="disabled" regexp="[^<]+" tip="<@spring.message "admin.deposit.bank.bankCard.icon.right"/>"/></td>
                    </tr>
                    <tr class="bank-rate-class" <#if isFirstUSDT != 1> style="display: none" </#if>>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.depositRate"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.depositRate.right"/>" name="depositRate" value="${bank.depositRate}"
                                                     class="form-control money" disabled="disabled"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.withdrawRate"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.withdrawRate.right"/>" name="withdrawRate" value="${bank.withdrawRate}"
                                                     class="form-control money" disabled="disabled"/></td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.sortNo"/>：</td>
                        <td class="text-left"><input placeholder="<@spring.message "admin.deposit.bank.bankCard.sortNo.tip"/>" name="sortNo" value="${bank.sortNo }"
                                                     class="form-control" type="number" disabled="disabled"/></td>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.remark"/>：</td>
                        <td><input placeholder="<@spring.message "admin.deposit.bank.bankCard.remark.right"/>" name="remark" disabled="disabled" value="${bank.remark }" class="form-control"/></td>
                    </tr>
                    <tr>

                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.status"/>：</td>
                        <td class="text-left"><select name="status" disabled="disabled" class="form-control">
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
                                   disabled="disabled"   class="form-control"></td>
                    </tr>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.degreeIds"/>：</td>
                        <td colspan="3">${degreeNames}</td>
                    </tr>
                    <tr>
                        <td class="text-right media-middle"><@spring.message "admin.deposit.bank.bankCard.groupIds"/>：</td>
                        <td colspan="3">${groupNames}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default fui-close"><@spring.message "admin.deposit.handle.close"/></button>
            </div>
        </div>
    </div>
</form>
